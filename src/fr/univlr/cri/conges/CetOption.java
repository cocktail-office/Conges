package fr.univlr.cri.conges;


import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.constantes.ConstsJour;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;

/**
 * Ecran de choix des options CET, parmi :
 * - maintien
 * - indemnisation
 * - bascule RAFP
 * 
 * L'écran affiche également la valeur totale de maintien obligatoire
 * 
 * @author ctarade
 *
 */
public class CetOption 
	extends YCRIWebPage {

	// bindings entrants
	// planning concerné 
	public EOAffectationAnnuelle affAnn;
	
	// valeur de l'épargne attendue (facultative)
	public Integer epargneJour;
	
	// valeur du transfert depuis l'ancien système (facultatif)
	public Integer transfertAncienCetMinutes;
	
	// verrou sur l'ensemble des élements
	public boolean isDisabled;
	
	// bindings sortants 
	public Integer optionMaintienJourSelected;
	public Integer optionIndemnisationJourSelected;
	public Integer optionTransfertRafpJourSelected;
	
	// etat de selection des valeurs
	public boolean isSelectedOptionMaintien;
	public boolean isSelectedOptionIndemnisation;
	
	// items des listes
	public Integer optionMaintienJourItem;
	public Integer optionIndemnisationJourItem;
	public Integer optionTransfertRafpJourItem;
	
	public CetOption(WOContext context) {
		super(context);
	}
		
	/**
	 * Affichage du solde avant option
	 * @return
	 */
	public Float soldeAvantOption() {
		int transfertMinutes = (transfertAncienCetMinutes != null ? transfertAncienCetMinutes.intValue() : 0);
		
		return new Float((float) (affAnn.cetFactory().soldeEnMinutes() + transfertMinutes) / (float) ConstsJour.DUREE_JOUR_7H00);
	}
	
	/**
	 * Affichage du solde après option du maintien.
	 * @return
	 */
	public Float soldeApresOption() {
		float soldeAvantOption = soldeAvantOption().floatValue();
		float epargneJours = (float) (epargneJour != null ? epargneJour : 0);
			
		return new Float(soldeAvantOption + epargneJours - optionTotalJour() + (float) optionMaintienJourSelected.intValue());
	}
	
	/**
	 * Le nombre total de jours concernés par les options
	 * @return
	 */
	public Integer optionTotalJour() {
		int epargneMinutes = (epargneJour != null ? epargneJour * ConstsJour.DUREE_JOUR_7H00 : 0);
		int transfertMinutes = (transfertAncienCetMinutes != null ? transfertAncienCetMinutes.intValue() : 0);
		
		Integer optionTotalJour = new Integer(
				affAnn.cetFactory().depassementSeuil20JoursPourEpargneCetEnMinutes(
						epargneMinutes + transfertMinutes) / ConstsJour.DUREE_JOUR_7H00);
		
		return optionTotalJour;
	}
	
  /**
   * La liste des jours que l'utilisateur peut définir pour le maintien en CET. 
   * Cette valeur dépend de {@link #epargneJour}
   * @return
   */
  public NSArray optionMaintienJourList() {
  	int epargneMinutes = (epargneJour != null ? epargneJour * ConstsJour.DUREE_JOUR_7H00 : 0);
  	int transfertMinutes = (transfertAncienCetMinutes != null ? transfertAncienCetMinutes.intValue() : 0);
		
  	int maximumMinutes = 
  		affAnn.cetFactory().maintienCetMaximumAuDela20JEnMinutes(
  				epargneMinutes + transfertMinutes);
  	
  	int maximumJour = maximumMinutes / ConstsJour.DUREE_JOUR_7H00;
  	
   	NSArray maintienJourList = new NSArray(0);
    
  	if (maximumJour != 0) {
    	for (int i=1; i<maximumJour+1; i++) {
    		maintienJourList = maintienJourList.arrayByAddingObject(i);
    	}
  	} 
  	
  	return maintienJourList;
  }
  
  /**
   * La liste des jours que l'utilisateur peut définir pour leur indemnisation.
   * Cette valeur dépend {@link #optionTotalJour()} et {@link #optionMaintienJourSelected}
   * @return
   */
  public NSArray optionIndemnisationJourList() {
  	int maximumJour = optionTotalJour().intValue() - optionMaintienJourSelected.intValue();
  	
  	NSArray indemnisationJourList = new NSArray(new Integer(0));

  	if (maximumJour > 0) {
  		for (int i=1; i<maximumJour+1; i++) {
  			indemnisationJourList = indemnisationJourList.arrayByAddingObject(i);
    	}
  	}
  	
  	return indemnisationJourList;
  }

  
  /**
   * Le nombre de jours que l'utilisateur destinés au rachat RAFP.
   * Cette valeur dépend {@link #optionTotalJour()}, {@link #optionMaintienJourSelected} et {@link #optionIndemnisationJourSelected}
   * @return <code>null</code> si l'une des données à minima n'est saisi
   */
  public Integer optionTransfertRafpJourSelected() {
  	Integer optionTransfertRafpJourSelected = null;
  	if (optionMaintienJourSelected != null && optionIndemnisationJourSelected != null) {
  		optionTransfertRafpJourSelected = new Integer(
  				optionTotalJour().intValue() - optionMaintienJourSelected.intValue() - optionIndemnisationJourSelected.intValue());
  	}
  	return optionTransfertRafpJourSelected;
  }

  
  // navigation
  
  /**
   * Indiquer que la valeur a maintenir a été selectionnée
   */
  public WOComponent doSelectMaintien() {
  	isSelectedOptionMaintien = true;

  	// on fait la selection directe de l'indemnisation celle ci
  	// dans certains cas pour accélerer la saisie
  	boolean shouldSelectIndemnisation = false;
  	
  	// on preselectionne le nombre de jours à racheter à 0 s'il est vide
  	// sauf pour les contractuels, ce sera la valeur maximum puisqu'ils 
  	// n'ont pas droits à la RAFP
  	if (isPasAutoriseARafp()) {
  		optionIndemnisationJourSelected = (Integer) optionIndemnisationJourList().lastObject();
  		// pas on peut valider directement car la valeur
  		// est dépendante du maintien, sans autre choix possible
  		shouldSelectIndemnisation = true;
  	} else {
  		optionIndemnisationJourSelected = new Integer(0);
  	}
  	
  	// la liste proposée en indemnisation est 0
  	// uniquement, alors on peut valider directement
  	if (!shouldSelectIndemnisation && optionIndemnisationJourList().count() == 1) {
  		shouldSelectIndemnisation = true;
  	}
  	
  	//
  	if (shouldSelectIndemnisation) {
  		doSelectIndemnisation();
  	}
  	
  	return null;
  }
  
  /**
   * Indiquer que la valeur a maintenir a été selectionnée
   */
  public WOComponent doSelectIndemnisation() {
  	isSelectedOptionIndemnisation = true;
  	return null;
  }
  
  /**
   * Indiquer que la valeur a maintenir a été déselectionnée
   */
  public WOComponent doUnSelectMaintien() {
  	isSelectedOptionMaintien = false;
  	isSelectedOptionIndemnisation = false;
  	return null;
  }
  
  /**
   * Indiquer que la valeur a indemniser a été déselectionnée
   */
  public WOComponent doUnSelectIndemnisation() {
  	isSelectedOptionIndemnisation = false;
  	return null;
  }
  
  
  // disponibilité élements interface
  
  /**
   * Determiner si la personne connectée est autorisée a faire une demande
   * de transfert en RAFP
   */
  public boolean isPasAutoriseARafp() {
  	return affAnn.cetFactory().isPasAutoriseARafp();
  }
  
  /**
   * Visibilité de la liste des jours à indemniser
   * @return
   */
  public boolean showPopUpIndemnisation() {
  	return isSelectedOptionMaintien;
  }
  
  /**
   * Visibilité du nombre de jours à basculer en RAFP
   * @return
   */
  public boolean showStrRafp() {
  	return isSelectedOptionMaintien && isSelectedOptionIndemnisation;
  }
  
  /**
   * Disponibilité de la liste des jours à maintenir
   * @return
   */
  public boolean isDisabledPopUpMaintienJour() {
  	return isDisabled || isSelectedOptionMaintien;
  }
  
  /**
   * Disponibilité de la liste des jours à indemniser
   * @return
   */
  public boolean isDisabledPopUpIndemnisationJour() {
  	return isDisabled || isSelectedOptionIndemnisation || isPasAutoriseARafp();
  }
  
  /**
   * 
   * @return
   */
  public boolean showBtnDoSelectMaintien() {
  	return !isDisabled && !isSelectedOptionMaintien;
  }
  
  /**
   * 
   * @return
   */
  public boolean showBtnDoSelectIndemnisation() {
  	return !isDisabled && isSelectedOptionMaintien && !isSelectedOptionIndemnisation;
  }
  
  /**
   * 
   * @return
   */
  public boolean showLnkDoUnSelectMaintien() {
  	return !isDisabled && isSelectedOptionMaintien;
  }
  
  /**
   * 
   * @return
   */
  public boolean showLnkDoUnSelectIndemnisation() {
  	return !isDisabled && isSelectedOptionIndemnisation;
  }
  
  /**
   * Indique si l'option est précédée d'une épargne
   * @return
   */
  public boolean hasEpargne() {
  	return epargneJour != null && epargneJour.intValue() > 0;
  }
  
  // setters


	public final void setOptionMaintienJourSelected(Integer value) {
		optionMaintienJourSelected = value;
		if (optionMaintienJourSelected == null) {
			isSelectedOptionMaintien = false;
		}
	}

	public final void setOptionIndemnisationJourSelected(Integer value) {
		optionIndemnisationJourSelected = value;
		if (optionIndemnisationJourSelected == null) {
			isSelectedOptionIndemnisation = false;
		}
	}
	
	/**
	 * Pour ignorer l'attribution de valeur via binding
	 * @param isSelectedOptionMaintien
	 */
	public final void setSelectedOptionMaintien(boolean isSelectedOptionMaintien) {

	}

	/**
	 * Pour ignorer l'attribution de valeur via binding
	 * @param isSelectedOptionIndemnisation
	 */
	public final void setSelectedOptionIndemnisation(
			boolean isSelectedOptionIndemnisation) {

	}
  
}