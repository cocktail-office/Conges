package fr.univlr.cri.conges;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver._private.WOForm;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.constantes.ConstsJour;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.objects.Planning;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;

/**
 * Le formulaire de la gestion de la demande et 
 * de la décision d'épargne de CET.
 * 
 * Ce composant doit être à l'intérieur d'un {@link WOForm}
 * 
 * @author ctarade
 */
public class CetEpargne
	extends YCRIWebPage {
	
	// bindings
	/** le planning concerné : entrant */
	public Planning planning;
	/** verrouiller le formulaire */
	public boolean isDisabled;
	
	/** la valeur de l'épargne décidée : le binding sortant est donné par la méthode {@link #getEpargneSelected()} */
	public Integer localEpargneSelected;
	
	// variable bidon pour ignorer la valeur entrée en binding
	public Integer epargneSelected;
	
	// variable bidon pour ignorer la valeur entrée en binding
	public Integer maintienJourObligatoire;
	
	/** liste des jours */
	public NSArray epargneList;
	public Integer epargneItem;
	
	/** etat de selection des valeurs */
	public boolean isSelectedEpargne;
	
	/** valeur a transferer l'ancien CET vers le régime pérenne */
	public Integer transfertAncienCetMinutes;

	/** congés restants avant la demande d'épargne*/
	public String congesRestantsEnHeures;

	/** congés restants avant la demande d'épargne*/
	public String congesRestantsEnJourA7h00;
	
	
	public CetEpargne(WOContext context) {
		super(context);
	}
	
	/**
	 * 
	 */
	public EOAffectationAnnuelle getAffAnn() {
		return planning.affectationAnnuelle();
	}
	
	/**
	 * On doit remettre certaines valeurs à zéro lorsque
	 * le planning passé en paramètre est nouveau
	 * @param value
	 */
	public void setPlanning(Planning value) {
		Planning prevPlanning = planning;
		planning = value;
		if (planning != prevPlanning) {
			congesRestantsEnHeures = TimeCtrl.stringDureeToHeure(planning.congesGlobalRestants());
			congesRestantsEnJourA7h00 = planning.congesGlobalRestantsEnJourA7h00();
			localEpargneSelected = null;
			epargneSelected = null;
			transfertAncienCetMinutes = null;
			resetEpargneList();
		}
	}
	
	/**
   * La liste des valeurs en jours épargnables.
   * Si aucun, on met uniquement le tableau avec 0 jours
   * @return
   */
  public NSArray resetEpargneList() {
  	epargneList = new NSArray(new Integer(0));
  	int reliquatPourBlocageCetMaxEnJour7h00Arrondi = getAffAnn().cetFactory().reliquatPourBlocageCetMaxEnJour7h00Arrondi();
  	
  	if (reliquatPourBlocageCetMaxEnJour7h00Arrondi > 0) {
    	for (int i=0; i<reliquatPourBlocageCetMaxEnJour7h00Arrondi; i++) {
    		epargneList = epargneList.arrayByAddingObject(new Integer(i+1));
    	}
  	} 
  	
  	// preselection de la premiere valeur
  	if (localEpargneSelected == null) {
  		localEpargneSelected = (Integer) epargneList.objectAtIndex(0); 
  	}
  	
  	return epargneList;
  }
  
  
  // getters 
  
  /**
   * Méthode appelée par les sur composants.
   * Retourne null si {@link #isSelectedEpargne} est a <code>false</code>
   */
  public Integer getEpargneSelected() {
  	Integer epargneSelected = null;
  	if (isSelectedEpargne) {
  		epargneSelected = localEpargneSelected;
  	}
  	return epargneSelected;
  }
	
	/**
	 * Le nombre de jours obligatoire à maintenir.
	 * Prendre la valeur qui se dégage du CET actuel + épargne
	 * @return
	 */
	public Integer getMaintienJourObligatoire() {
		int epargneMinutes = localEpargneSelected.intValue() * ConstsJour.DUREE_JOUR_7H00;
		int transfertMinutes = (transfertAncienCetMinutes != null ? transfertAncienCetMinutes.intValue() : 0);		
		
		Integer maintienJourObligatoire = new Integer(
				getAffAnn().cetFactory().maintienObligatoireCetPourDemandeEpargneCetEnMinutes(
						epargneMinutes + transfertMinutes) / ConstsJour.DUREE_JOUR_7H00);
	
		return maintienJourObligatoire;
	}
	
	/**
	 * Le droit à congés restants suite à l'épargne en heures
	 * @return
	 */
	public String getCongesRestantsApresEpargneEnHeures() {
		int epargneMinutes = localEpargneSelected.intValue() * ConstsJour.DUREE_JOUR_7H00;
		return getAffAnn().cetFactory().congesRestantsApresEpargneEnHeures(epargneMinutes, cngUserInfo());
	}

	/**
	 * Le droit à congés restants suite à l'épargne en jours à 7h00
	 * @return
	 */
	public float getCongesRestantsApresEpargneEnJourA7h00() {
		int epargneMinutes = localEpargneSelected.intValue() * ConstsJour.DUREE_JOUR_7H00;
		return getAffAnn().cetFactory().congesRestantsApresEpargneEnJour7h00(epargneMinutes, cngUserInfo());
	}
	
	
  // actions
  
  /**
   * Valider le formulaire et donc enregistrer la selection de 
   * la variable {@link #epargneSelected}
   */
  public WOComponent doSelectEpargne() {
  	isSelectedEpargne = true;
  	return null;
  }
  
  /**
   * Devalider le formulaire et déselection de 
   * la variable {@link #epargneSelected}
   */
  public WOComponent doUnSelectEpargne() {
  	isSelectedEpargne = false;
  	return null;
  }
  
  
  // disponibilité éléments interface

  /**
   * 
   * @return
   */
  public boolean showBtnDoSelectEpargne() {
  	return !isDisabled && !isSelectedEpargne;
  }
  
  /**
   * 
   * @return
   */
  public boolean showLnkDoUnSelectEpargne() {
  	return !isDisabled && isSelectedEpargne;
  }
  
  /**
   * 
   * @return
   */
  public boolean isDisabledPopUpEpargne() {
  	return isDisabled || isSelectedEpargne;
  }
  
  
  // affichage
  
  /**
   * L'affichage de l'année universtaire des reliquats
   * qui vont devenir l'épargne CET
   */
  public String getAnneeUnivReliquats() {
  	String anneeUniv = "";
  	
  	anneeUniv = DateCtrlConges.anneeUnivForDate(getAffAnn().dateDebutAnnee().timestampByAddingGregorianUnits(-1,0,0,0,0,0));
  	
  	return anneeUniv;
  }
}