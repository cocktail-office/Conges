package fr.univlr.cri.conges;


import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.constantes.ConstsJour;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.I_ClasseMetierNotificationParametre;
import fr.univlr.cri.conges.objects.Parametre;

/**
 * Formulaire de gestion des options pour le CET ancien système :
 * - transfert de tout dans le régime pérenne
 * ou
 * - maintien de tout ou partie dans l'ancien système
 * - RAFP / indemnisation de tout ou partie
 * 
 * @author ctarade
 *
 */
public class CetOptionAncienSysteme 
	extends YCRIWebPage
		implements I_ClasseMetierNotificationParametre {
	
	// bindings entrants
	// planning concerné 
	public EOAffectationAnnuelle affAnn;
	
	// verrou sur l'ensemble des élements
	public boolean isDisabled;
	
	// bindings sortants
	
	/** 
	 * indique si l'agent renonce à tout son CET ancien et donc
	 * transferer tout son solde vers le régime pérenne
	 */
	public Boolean isRenoncement;

	public Integer optionIndemnisationJourSelected;
	public Integer optionTransfertRafpJourSelected;
	public Integer optionMaintienMinuteSelected;
	
	// etat de selection des valeurs
	public boolean isSelectedOptionIndemnisation;
	public boolean isSelectedOptionTransfertRafp;
	
	// items des listes
	public Integer optionIndemnisationJourItem;
	public Integer optionTransfertRafpJourItem;

	//
	private static boolean isCetOptionAncienSystemeAuDela20Jours;
	
	public CetOptionAncienSysteme(WOContext context) {	
		super(context);
	}

  /**
   * @deprecated
   * @see #initStaticField(Parametre)
   */
	public static void initStaticFields(
			boolean anIsCetOptionAncienSystemeAuDela20Joursi) {
		isCetOptionAncienSystemeAuDela20Jours = anIsCetOptionAncienSystemeAuDela20Joursi;
	}
		
  /**
   * @see I_ClasseMetierNotificationParametre
   * @param parametre
   */
  public static void initStaticField(
  		Parametre parametre) {
  	if (parametre == Parametre.PARAM_CET_OPTION_ANCIEN_SYSTEME_AU_DELA_20_JOURS) {
  		isCetOptionAncienSystemeAuDela20Jours = parametre.getParamValueBoolean().booleanValue();
  	}
  }
	
	/**
	 * {@link #optionTotalMinute()} convertie en jour à 7h00
	 * @return
	 */
	public float optionTotalJour() {
		int optionMinutes = optionTotalMinute();
		
		float optionTotalJour = (float) optionMinutes / (float) ConstsJour.DUREE_JOUR_7H00;
		
		return optionTotalJour;
	}
	
	/**
	 * Le nombre total de minutes concernés par les options :
	 * c'est le solde CET au 31/12/2008
	 * @return
	 */
	private int optionTotalMinute() {
		int optionTotalMinute = 0;
		
		optionTotalMinute = affAnn.individu().toCET().minutesRestantesAncienRegime();
		
		return optionTotalMinute;
	}
	
  /**
   * La liste des jours que l'utilisateur peut demander en indemnisation.
   * On ne prend que des jours de 7h00.
   * 
   * TODO regle métier de derniere minute : seul l'éxécédent des 20 jours
   * est soumis au droit d'option
   * 
   * @return
   */
  public NSArray optionIndemnisationJourList() {
  	int maximumJour = (int) optionTotalJour();
  	
  	// seulement l'excédent des 20 jours
  	maximumJour = excedent20j(maximumJour);

  	NSArray indemnisationJourList = new NSArray(new Integer(0));

  	if (maximumJour > 0) {
  		for (int i=1; i<maximumJour+1; i++) {
  			indemnisationJourList = indemnisationJourList.arrayByAddingObject(i);
    	}
  	}
  	
  	return indemnisationJourList;
  }

  
  /**
   * La liste des jours que l'utilisateur peut transferer en RAFP.
   * On ne prend que des jours de 7h00.
   * 
   * TODO regle métier de derniere minute : seul l'éxécédent des 20 jours
   * est soumis au droit d'option
   * 
   * @return
   */
  public NSArray optionTransfertRafpJourList() {
  	int maximumJour = (int) (optionTotalJour() - optionIndemnisationJourSelected.intValue());
  	
  	// seulement l'excédent des 20 jours
  	maximumJour = excedent20j(maximumJour);
  	
  	NSArray rafpJourList = new NSArray(new Integer(0));

  	if (maximumJour > 0) {
  		for (int i=1; i<maximumJour+1; i++) {
  			rafpJourList = rafpJourList.arrayByAddingObject(i);
    	}
  	}
  	
  	return rafpJourList;
  }

  /**
   * Donne l'excedent au dela 20 jours d'une valeur si le parametre
   * de l'application CET_OPTION_ANCIEN_SYSTEME_AU_DELA_20_JOURS est 
   * a YES. Sinon jours est retourné.
   * @param jours
   * @return
   */
  private int excedent20j(int jours) {
  	int excedent20j = 0;

  	if (isCetOptionAncienSystemeAuDela20Jours) {
    	if (jours > 20) {
    		excedent20j = jours - 20;
    	} 
  	} else {
  		excedent20j = jours;
  	}
  	 	
  	return excedent20j;
  }
  
  /**
   * Le nombre de minutes que l'utilisateur destine au maintien dans l'ancien régime.
   * Cette valeur dépend {@link #optionIndemnisationJourSelected} et {@link #optionTransfertRafpJourSelected}
   * @return <code>null</code> si l'une des données à minima n'est saisi
   */
  public Integer optionMaintienMinuteSelected() {
  	Integer optionMaintienJourSelected = null;
  	if (optionTransfertRafpJourSelected != null && optionIndemnisationJourSelected != null) {
  		optionMaintienJourSelected = new Integer( 
  			optionTotalMinute() - (optionIndemnisationJourSelected.intValue() + optionTransfertRafpJourSelected.intValue()) * ConstsJour.DUREE_JOUR_7H00);
  	}
  	return optionMaintienJourSelected;
  }
	
	/**
	 * {@link #optionMaintienMinuteSelected()} convertie en jour à 7h00
	 * @return
	 */
  public Float optionMaintienJourSelected() {
  	return new Float((float) optionMaintienMinuteSelected() / (float) ConstsJour.DUREE_JOUR_7H00);
  }
  
	// navigation

	/**
	 * Choisir l'option de renoncer
	 */
	public WOComponent doRenoncer() {
		isRenoncement = Boolean.TRUE;
		optionIndemnisationJourSelected = null;
		optionTransfertRafpJourSelected = null;
		isSelectedOptionIndemnisation = false;
		isSelectedOptionTransfertRafp = false;
		return null;
	}

	/**
	 * Choisir l'option de ne pas renoncer.
	 * 2 cas possibles : 
	 * - droit d'option possible
	 * - droit d'option non possible
	 */
	public WOComponent doNePasRenoncer() {
		isRenoncement = Boolean.FALSE;
		
		if (affAnn.cetFactory().isDroitOptionAncienRegimeApresRenoncementPossible()) {
			// on laisse choisir l'agent
			optionIndemnisationJourSelected = null;
			optionTransfertRafpJourSelected = null;
			isSelectedOptionIndemnisation = false;
			isSelectedOptionTransfertRafp = false;
		}	else {
			// on pré-remplit
			optionIndemnisationJourSelected = new Integer(0);
			optionTransfertRafpJourSelected = new Integer(0);
			isSelectedOptionIndemnisation = true;
			isSelectedOptionTransfertRafp = true;
		}
		
		return null;
	}

	/**
	 * Choisir l'option de ne pas renoncer
	 */
	public WOComponent doUnSelectRenoncement() {
		isRenoncement = null;
		return null;
	}

  /**
   * Indiquer que la valeur a indemniser a été selectionnée
   */
  public WOComponent doSelectIndemnisation() {
  	isSelectedOptionIndemnisation = true;

  	// on fait la selection directe de l'indemnisation celle ci
  	// dans certains cas pour accélerer la saisie
  	boolean shouldSelectRafp = false;
  	
  	// on preselectionne le nombre de jours à transferer  à 0 s'il est vide
  	// sauf pour les contractuels, ce sera la valeur maximum puisqu'ils 
  	// n'ont pas droits à la RAFP
  	if (isPasAutoriseARafp()) {
  		optionTransfertRafpJourSelected = (Integer) optionTransfertRafpJourList().lastObject();
  		// pas on peut valider directement car la valeur
  		// est dépendante du maintien, sans autre choix possible
  		shouldSelectRafp = true;
  	} else {
  		optionTransfertRafpJourSelected = new Integer(0);
  	}
  	
  	// la liste proposée en indemnisation est 0
  	// uniquement, alors on peut valider directement
  	if (!shouldSelectRafp && optionTransfertRafpJourList().count() == 1) {
  		shouldSelectRafp = true;
  	}
  	
  	//
  	if (shouldSelectRafp) {
  		doSelectTransfertRafp();
  	}
  	
  	return null;
  }
	

  /**
   * Indiquer que la valeur a transfert RAFP a été selectionnée
   */
  public WOComponent doSelectTransfertRafp() {
  	isSelectedOptionTransfertRafp = true;
  	return null;
  }
 
  /**
   * Indiquer que la valeur a maintenir a été déselectionnée
   */
  public WOComponent doUnSelectIndemnisation() {
  	isSelectedOptionIndemnisation = false;
  	isSelectedOptionTransfertRafp = false;
  	return null;
  }
  
  /**
   * Indiquer que la valeur a indemniser a été déselectionnée
   */
  public WOComponent doUnSelectTransfertRafp() {
  	isSelectedOptionTransfertRafp = false;
  	return null;
  }

	
  // disponibilité élements interface
  
	public boolean isSelectedRenoncement() {
		return isRenoncement != null;
	} 
	
  /**
   * Determiner si la personne connectée est autorisée a faire une demande
   * de transfert en RAFP
   */
  public boolean isPasAutoriseARafp() {
  	return affAnn.cetFactory().isPasAutoriseARafp();
  }
  
  /**
   * Visibilité de la liste des jours à transferer en RAFP
   * @return
   */
  public boolean isShowPopUpTransfertRafp() {
  	return isSelectedOptionIndemnisation;
  }
  
  /**
   * Visibilité du nombre de jours à maintenir
   * @return
   */
  public boolean isShowStrMaintien() {
  	return isSelectedOptionIndemnisation && isSelectedOptionTransfertRafp;
  }
  
  /**
   * Disponibilité de la liste des jours à transferer en RAFP
   * @return
   */
  public boolean isDisabledPopUpTransfertRafp() {
  	return isDisabled || isSelectedOptionTransfertRafp;
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
  public boolean isShowBtnDoSelectIndemnisation() {
  	return !isDisabled && !isSelectedOptionIndemnisation;
  }
  
  /**
   * 
   * @return
   */
  public boolean isShowBtnDoSelectTransfertRafp() {
  	return !isDisabled && isSelectedOptionIndemnisation && !isSelectedOptionTransfertRafp;
  }
  
  /**
   * 
   * @return
   */
  public boolean isShowLnkDoUnSelectIndemnisation() {
  	return !isDisabled && isSelectedOptionIndemnisation;
  }
  
  /**
   * 
   * @return
   */
  public boolean isShowLnkDoUnSelectTransfertRafp() {
  	return !isDisabled && isSelectedOptionTransfertRafp;
  }
  
  
  // setters

	public final void setOptionIndemnisationJourSelected(Integer value) {
		optionIndemnisationJourSelected = value;
		if (optionIndemnisationJourSelected == null) {
			isSelectedOptionIndemnisation = false;
		}
	}

	public final void setOptionTransfertRafpJourSelected(Integer value) {
		optionTransfertRafpJourSelected = value;
		if (optionTransfertRafpJourSelected == null) {
			isSelectedOptionTransfertRafp = false;
		}
	}
	
	public final void setOptionMaintienMinuteSelected(Integer value) {
		
	}
}