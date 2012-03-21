package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.foundation.*;
import com.webobjects.eocontrol.*;

import fr.univlr.cri.conges.databus.CngUserInfo;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOMouvement;
import fr.univlr.cri.conges.eos.modele.planning.EOOccupation;
import fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation;
import fr.univlr.cri.conges.objects.I_DebitableSurCET;
import fr.univlr.cri.conges.objects.Planning;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.util.ArrayCtrl;
import fr.univlr.cri.util.wo5.DateCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRSort;

public class EOCET 
	extends _EOCET {

	public final static String TOS_DEBITABLE_KEY 	= "tosDebitable";
	
	public final static String LIBELLE_TYPE_OCCUPATION = "C_CET";
  
  // on met 01/01/2009 car les CET 31/12/2008 sont mis a la date de valeur 01/01/2009 .....
	public final static String STR_DATE_CET_DEBUT_REGIME_PERENNE = "01/01/2009";
  public final static NSTimestamp DATE_CET_DEBUT_REGIME_PERENNE 	= DateCtrlConges.stringToDate(STR_DATE_CET_DEBUT_REGIME_PERENNE); 

	public final static LRSort SORT_INDIVIDU = LRSort.newSort(
			EOCET.INDIVIDU_KEY + "." + EOIndividu.NOM_KEY +","+
			EOCET.INDIVIDU_KEY + "." + EOIndividu.PRENOM_KEY);			

	public EOCET() {
		super();
	}

  public static EOCET createCET(
  		EOEditingContext editingContext, EOIndividu individu) {
  	NSTimestamp now = DateCtrlConges.now();
    return createCET(editingContext, now, now, individu);
  }

  /**
   * Donne le total de minutes restantes sur le CET sur 
   * toutes les transactions datant d'avant la date indiquée
   */
  private int minutesRestantesBefore(NSTimestamp date) {
  	EOQualifier qual = CRIDataBus.newCondition(EOCETTransaction.DATE_VALEUR_KEY + "<%@", new NSArray(date));
  	NSArray transactions = EOQualifier.filteredArrayWithQualifier(cETTransactions(), qual);
  	int minutesRestantes = 
  		((Number)transactions.valueForKey("@sum."+EOCETTransaction.VALEUR_KEY)).intValue() -
  		((Number)transactions.valueForKey("@sum."+EOCETTransaction.MINUTES_DEBITEES_KEY)).intValue();
  	return minutesRestantes;
  }

  /**
   * Donne le total de minutes credités sur le CET sur 
   * toutes les transactions datant d'avant la date indiquée
   */
  private int minutesCrediteesBefore(NSTimestamp date) {
  	EOQualifier qual = CRIDataBus.newCondition(EOCETTransaction.DATE_VALEUR_KEY + "<%@", new NSArray(date));
  	NSArray transactions = EOQualifier.filteredArrayWithQualifier(cETTransactions(), qual);
  	int minutesCreditees = 
  		((Number)transactions.valueForKey("@sum."+EOCETTransaction.VALEUR_KEY)).intValue();
  	return minutesCreditees;
  }
  
  /**
   * Donne le total de minutes restantes sur le CET sur 
   * toutes les transactions d'une période indiquée
   */
  private int minutesRestantesBeforeEqAndAfterEq(NSTimestamp dateDebut, NSTimestamp dateFin) {
  	EOQualifier qual = CRIDataBus.newCondition(
  			EOCETTransaction.DATE_VALEUR_KEY + ">=%@ and " +
  			EOCETTransaction.DATE_VALEUR_KEY + "<=%@", new NSArray(new NSTimestamp[]{
  					dateDebut, dateFin}));
  	NSArray transactions = EOQualifier.filteredArrayWithQualifier(cETTransactions(), qual);
  	int minutesRestantes = 
  		((Number)transactions.valueForKey("@sum."+EOCETTransaction.VALEUR_KEY)).intValue() -
  		((Number)transactions.valueForKey("@sum."+EOCETTransaction.MINUTES_DEBITEES_KEY)).intValue();
  	return minutesRestantes;
  }


  /**
   * Donne le total de minutes restantes sur le CET régime pérenne sur 
   * toutes les transactions datant d'avant la date indiquée
   */
	public int minutesRestantesAncienRegime() {
		int minutes = 0;

		minutes = minutesRestantesBefore(DATE_CET_DEBUT_REGIME_PERENNE);

		return minutes;
	}
	
  /**
   * Donne le total de minutes restantes sur le CET régime pérenne sur 
   * toutes les transactions datant d'avant la date indiquée
   */
	public int minutesRestantesRegimePerenne(NSTimestamp date) {
		int minutes = 0;
		
		if (DateCtrlConges.isAfterEq(date, DATE_CET_DEBUT_REGIME_PERENNE)) {
			minutes = minutesRestantesBeforeEqAndAfterEq(DATE_CET_DEBUT_REGIME_PERENNE, date);
		}
		
		return minutes;
	}


  
  /**
   * La liste des objets suceptibles de venir debiter le CET
   * jusqu'a une date (celle d'un planning). On utilise cette 
   * date pour faire les divers debits chronologiquement, en reference
   * a un planning.
   * 
   * @param dateDebutPlanning
   * 
   * @return une liste de <code>A_DebitableSurCet</code>
   */
  public NSArray tosDebitableBefore(NSTimestamp dateDebutPlanning) {
  
  	NSArray result = null;
  	
  	// d'abord tout sauf les occupations
  	EOQualifier qual = CRIDataBus.newCondition(
  			I_DebitableSurCET.DATE_VALEUR_KEY +"<%@",
				new NSArray(dateDebutPlanning.timestampByAddingGregorianUnits(1,0,0,0,0,0)));

  	result = EOQualifier.filteredArrayWithQualifier(tosDebitable(false), qual);

  	// apres les occupations
  	qual = CRIDataBus.newCondition(
  			I_DebitableSurCET.DATE_VALEUR_KEY +"<%@",
				new NSArray(dateDebutPlanning));

  	result = result.arrayByAddingObjectsFromArray(
  			EOQualifier.filteredArrayWithQualifier(tosOccupationCet(), qual));
  			
  	//TODO traiter les congés CET a cheval sur 2 plannings

  	return result;
  }
  
  
  /**
   * La liste des objets suceptibles de venir debiter le CET
   * apres une date (celle d'un planning). On utilise cette 
   * date pour faire les divers debits chronologiquement, en reference
   * a un planning.
   * 
   * @param dateFinPlanning
   * 
   * @return une liste de <code>A_DebitableSurCet</code>
   */
  public NSArray tosDebitableAfter(NSTimestamp dateFinPlanning) {
 
  	NSArray result = null;
  	
  	EOQualifier qual = CRIDataBus.newCondition(
  			I_DebitableSurCET.DATE_VALEUR_KEY +">%@",
				new NSArray(dateFinPlanning));
  		
  	result = EOQualifier.filteredArrayWithQualifier(tosDebitable(true), qual);
  	//TODO traiter les congés CET a cheval sur 2 plannings
  	
   	return result;
  }
  
  /**
   * La liste des debitables
   * 
   * @param withOccupation
   * 		faut-il mettre dans la liste {@link #tosOccupationCet()}
   * 	
   * 
   * @return
   */
  private NSArray tosDebitable(boolean withOccupation) {
  	
  	NSArray list = new NSArray();
  	
  	// indemnisation décret 2008-1136 (sur l'ancien régime uniquement)
  	list = list.arrayByAddingObjectsFromArray(tosMouvementIndemnisationDecret20081136());
  	// indemnisation de l'ancien régime
  	list = list.arrayByAddingObjectsFromArray(tosMouvementIndemnisationAncienRegime());
  	// transfert RAFP de l'ancien régime
  	list = list.arrayByAddingObjectsFromArray(tosMouvementTransfertRafpAncienRegime());
  	// transfert de l'ancien régime vers le régime pérenne
  	list = list.arrayByAddingObjectsFromArray(tosMouvementTransfertRegimePerenne());
   	// les vidages ancien régime
  	list = list.arrayByAddingObjectsFromArray(tosMouvementVidageIndemnisationAncienRegime());
  	list = list.arrayByAddingObjectsFromArray(tosMouvementVidageTransfertRafpAncienRegime());
  	// indemnisation du régime pérenne
  	list = list.arrayByAddingObjectsFromArray(tosMouvementIndemnisation());
  	// transfert rafp du régime pérenne
  	list = list.arrayByAddingObjectsFromArray(tosMouvementTransfertRafp());
  	
  	if (withOccupation) {
    	// occupations
    	list = list.arrayByAddingObjectsFromArray(tosOccupationCet());
  	}

  	// le vidage régime pérenne
  	list = list.arrayByAddingObjectsFromArray(tosMouvementVidageIndemnisation());
  	list = list.arrayByAddingObjectsFromArray(tosMouvementVidageTransfertRafp());
  	
  	// ne conserver ce qui ont du débit 
  	list = EOQualifier.filteredArrayWithQualifier(list, 
  			CRIDataBus.newCondition(I_DebitableSurCET.DUREE_A_DEBITER_KEY + ">0"));
  	
  	return list;
  }
  
  /**
   * L'ensemble de tous les objets debitables pour un CET :
   * @return
   */
  public NSArray tosDebitable() {
  	NSArray list = new NSArray();
  	
  	list = tosDebitable(true);
  	
  	return list;
  }
  

	/**
	 * Les enregistrements de mouvement de minutes indemnisées sur CET ancien régime
	 *  
	 * @return une liste de {@link EOMouvement}
	 */
	private NSArray tosMouvementIndemnisationAncienRegime() {
		return tosMouvementAllAffectionAnnuelleAndType(EOMouvement.TYPE_CET_DECISION_INDEMNISATION_ANCIEN_REGIME);
	}
	
	/**
	 * Les enregistrements de mouvement de minutes transferés du CET ancien regime vers la RAFP
	 *  
	 * @return une liste de {@link EOMouvement}
	 */
	private NSArray tosMouvementTransfertRafpAncienRegime() {
		return tosMouvementAllAffectionAnnuelleAndType(EOMouvement.TYPE_CET_DECISION_TRANSFERT_RAFP_ANCIEN_REGIME);
	}

	/**
	 * Les enregistrements de mouvement de minutes transférés de l'ancien
	 * CET vers le régime pérenne. Il ne peut y avoir au maximum
	 * qu'un seul élement dans ce tableau.
	 *  
	 * @return une liste de {@link EOMouvement}
	 */
	private NSArray tosMouvementTransfertRegimePerenne() {
		return tosMouvementAllAffectionAnnuelleAndType(EOMouvement.TYPE_CET_DECISION_TRANSFERT_REGIME_PERENNE);
	}
	

	/**
	 * Les enregistrements de mouvement de minutes indemnisées sur CET
	 *  
	 * @return une liste de {@link EOMouvement}
	 */
	private NSArray tosMouvementIndemnisationDecret20081136() {
		return tosMouvementAllAffectionAnnuelleAndType(EOMouvement.TYPE_CET_INDEMNISATION_DECRET_2008_1136);
	}

	/**
	 * Les enregistrements de vidage du CET pérenne en indemnisation
	 *  
	 * @return une liste de {@link EOMouvement}
	 */
	public NSArray tosMouvementVidageIndemnisation() {
		return tosMouvementAllAffectionAnnuelleAndType(EOMouvement.TYPE_CET_VIDAGE_INDEMNISATION);
	}

	/**
	 * Les enregistrements de vidage du CET pérenne en RAFP
	 *  
	 * @return une liste de {@link EOMouvement}
	 */
	public NSArray tosMouvementVidageTransfertRafp() {
		return tosMouvementAllAffectionAnnuelleAndType(EOMouvement.TYPE_CET_VIDAGE_TRANSFERT_RAFP);
	}

	/**
	 * Les enregistrements de vidage du CET pérenne en indemnisation (ancien régime)
	 *  
	 * @return une liste de {@link EOMouvement}
	 */
	public NSArray tosMouvementVidageIndemnisationAncienRegime() {
		return tosMouvementAllAffectionAnnuelleAndType(EOMouvement.TYPE_CET_VIDAGE_INDEMNISATION_ANCIEN_REGIME);
	}

	/**
	 * Les enregistrements de vidage du CET pérenne en RAFP (ancien régime)
	 *  
	 * @return une liste de {@link EOMouvement}
	 */
	public NSArray tosMouvementVidageTransfertRafpAncienRegime() {
		return tosMouvementAllAffectionAnnuelleAndType(EOMouvement.TYPE_CET_VIDAGE_TRANSFERT_RAFP_ANCIEN_REGIME);
	}
  

	/**
	 * Les enregistrements de mouvement de minutes indemnisées sur CET
	 *  
	 * @return une liste de {@link EOMouvement}
	 */
	private NSArray tosMouvementIndemnisation() {
		return tosMouvementAllAffectionAnnuelleAndType(EOMouvement.TYPE_CET_DECISION_INDEMNISATION);
	}
	
	
	/**
	 * Les enregistrements de mouvement de minutes transferés du CET vers la RAFP
	 *  
	 * @return une liste de {@link EOMouvement}
	 */
	private NSArray tosMouvementTransfertRafp() {
		return tosMouvementAllAffectionAnnuelleAndType(EOMouvement.TYPE_CET_DECISION_TRANSFERT_RAFP);
	}
	
	/**
	 * Liste des mouvements de l'ensemble des affectation annuelles 
	 * de l'agent titulaire d'un CET pour un type donné
	 * @param type
	 * @return
	 */
	private NSArray tosMouvementAllAffectionAnnuelleAndType(int type) {
		NSArray mouvementList = ArrayCtrl.applatirArray(
				(NSArray) EOAffectationAnnuelle.findAffectationsAnnuellesForIndividuInContext(
						editingContext(), individu()).valueForKey(EOAffectationAnnuelle.TOS_MOUVEMENT_KEY));
		mouvementList = EOQualifier.filteredArrayWithQualifier(
				mouvementList, CRIDataBus.newCondition(
						EOMouvement.MOUVEMENT_TYPE_KEY + "=%@", new NSArray(new Integer(type))));
	
		return mouvementList;
	}
	
	/**
	 * Les occupations de type CET associées
	 * 
	 * @return une liste de <code>EOOccupation</code>
	 */
	private NSArray tosOccupationCet() {
		NSArray occupationList = EOOccupation.findOccupationsForIndividuAndDebutAndFinAndType(
				editingContext(), individu(), typeOccupationCet());
		// on ne conserve que celle qui sont validées
		occupationList = EOQualifier.filteredArrayWithQualifier(occupationList,
				CRIDataBus.newCondition(EOOccupation.KEY_IS_VALIDEE+"=%@", new NSArray(Boolean.TRUE)));
		return occupationList;
	}
	
	private EOTypeOccupation _typeOccupationCet;
	
	/**
	 * Du cache sur le type occupation CET
	 * @return
	 */
	private EOTypeOccupation typeOccupationCet() {
		if (_typeOccupationCet == null) {
			_typeOccupationCet = EOTypeOccupation.getTypeOccupationForLibelleCourtInContext(
					editingContext(), EOCET.LIBELLE_TYPE_OCCUPATION);
		}
		return _typeOccupationCet;
	}
	

	// gestion des modification des transaction
	
	private CngUserInfo cngUserInfo;
	
	/**
	 * Indiquer la personne connectée, afin de pouvoir lancer
	 * la méthode {@link #doRecalculerPlannings()};
	 * @param value
	 */
	public void setCngUserInfo(CngUserInfo value) {
		cngUserInfo = value;
	}
	
	/**
	 * Effectuer le recalcul des plannings suite à la modification
	 * d'une donnée importante de la transaction, qui aura des impacts
	 * sur l'ensemble des débits des plannings
	 */
	public void doRecalculerPlannings() {
		if (cngUserInfo == null) {
			throw new NullPointerException(
					"La variable cngUserInfo est vide : impossible de recalculer les plannings de l'agent suite à " +
					"la modification de la date de valeur ou la valeur d'une transaction CET.");
		}
		// toutes les affectations annuelles
		NSArray affAnnList = EOAffectationAnnuelle.findAffectationsAnnuelleInContext(
  			editingContext(), individu().oid(), null, null);
		for (int i=0; i<affAnnList.count(); i++) {
			EOAffectationAnnuelle affAnn = (EOAffectationAnnuelle) affAnnList.objectAtIndex(i);
			Planning planning = Planning.newPlanning(affAnn, null, affAnn.dateDebutAnnee());
			planning.setType("R");
		}
	}
	
  /**
   * Création d'un enregistrement {@link EOCET} si l'individu
   * n'est possède déjà pas un
   */
  public static EOCET doCreationCetSiInexistant(EOIndividu individu) {
		EOCET cet = individu.toCET();
		// voir s'il a un compte CET
		if (cet == null) {
			cet = EOCET.createCET(individu.editingContext(), individu);
		}
		return cet;
  }

  
  // gestion du vidage
  
  
  /**
   * Demander le vidage du CET.
   * 
   */
  public void vider(int typeVidage) throws NSValidation.ValidationException {
  	// verifier qu'on a le bon type de vidage
  	if (typeVidage != EOMouvement.TYPE_CET_VIDAGE_INDEMNISATION &&
  			typeVidage != EOMouvement.TYPE_CET_VIDAGE_TRANSFERT_RAFP &&
  			typeVidage != EOMouvement.TYPE_CET_VIDAGE_INDEMNISATION_ANCIEN_REGIME &&
  			typeVidage != EOMouvement.TYPE_CET_VIDAGE_TRANSFERT_RAFP_ANCIEN_REGIME) {
  		throw new NSValidation.ValidationException("Le vidage de type '" + typeVidage + "' n'est " +
  				"pas reconnu ou non autorisé");
  	}
  	
  	// voir selon le type, si ce n'est pas déjà vidé par son "alter-ego"
  	if (typeVidage == EOMouvement.TYPE_CET_VIDAGE_INDEMNISATION && (
 				tosMouvementVidageIndemnisation().count() > 0 ||
 				tosMouvementVidageTransfertRafp().count() > 0)) {
  		throw new NSValidation.ValidationException("Ce CET a déjà été vidé");
  	}

  	if (typeVidage == EOMouvement.TYPE_CET_VIDAGE_TRANSFERT_RAFP && (
 				tosMouvementVidageIndemnisation().count() > 0 ||
 				tosMouvementVidageTransfertRafp().count() > 0)) {
  		throw new NSValidation.ValidationException("Ce CET a déjà été vidé");
  	}
  	
  	if (typeVidage == EOMouvement.TYPE_CET_VIDAGE_INDEMNISATION_ANCIEN_REGIME && (
 				tosMouvementVidageIndemnisationAncienRegime().count() > 0 ||
 				tosMouvementVidageTransfertRafpAncienRegime().count() > 0)) {
  		throw new NSValidation.ValidationException("Ce CET a déjà été vidé");
  	}
  	
  	if (typeVidage == EOMouvement.TYPE_CET_VIDAGE_TRANSFERT_RAFP_ANCIEN_REGIME && (
 				tosMouvementVidageIndemnisationAncienRegime().count() > 0 ||
 				tosMouvementVidageTransfertRafpAncienRegime().count() > 0)) {
  		throw new NSValidation.ValidationException("Ce CET a déjà été vidé");
  	}
  	
  	// ne pas autoriser le vidage ancien régime si vidé en pérenne
  	if ((
  			typeVidage == EOMouvement.TYPE_CET_VIDAGE_INDEMNISATION_ANCIEN_REGIME ||
  			typeVidage == EOMouvement.TYPE_CET_VIDAGE_TRANSFERT_RAFP_ANCIEN_REGIME) && (
  	 				tosMouvementVidageIndemnisation().count() > 0 ||
  	 				tosMouvementVidageTransfertRafp().count() > 0)) {
  		throw new NSValidation.ValidationException("Vous ne pouvez pas vider l'ancien régime " +
  				"si le régime pérenne l'a déjà été (le vidage du pérenne vide aussi l'ancien)");
  	}
  	
  	// on prend la bonne valeur
  	int mouvementMinutes = minutesRestantesAncienRegime();
  	if (typeVidage == EOMouvement.TYPE_CET_VIDAGE_INDEMNISATION ||
  			typeVidage == EOMouvement.TYPE_CET_VIDAGE_TRANSFERT_RAFP) {
  		mouvementMinutes += minutesRestantesRegimePerenne(DateCtrl.now());
  	}
  	
  	// recuperation du dernier planning
  	NSArray affAnnList = EOAffectationAnnuelle.findAffectationsAnnuelleInContext(
  			editingContext(), individu().oid(), null, null);

  	if (affAnnList.count() > 0) {
    	EOMouvement.newMouvement((EOAffectationAnnuelle) affAnnList.lastObject(), typeVidage, mouvementMinutes);
  	} else {
  		throw new NSValidation.ValidationException("L'agent n'a aucun planning");
  	}
  	
  }
  
  /**
   * Effacer l'enregistrement {@link EOMouvement} de vidage
   * @param typeVidage
   */
  public void supprimerMouvementVidage(int typeVidage) {
  	// recherche du mouvement
  	NSArray mouvementList = null;
  	if (typeVidage == EOMouvement.TYPE_CET_VIDAGE_INDEMNISATION) {
  		mouvementList = tosMouvementVidageIndemnisation();
  	} else if (typeVidage == EOMouvement.TYPE_CET_VIDAGE_TRANSFERT_RAFP) {
  		mouvementList = tosMouvementVidageTransfertRafp();
  	} else if (typeVidage == EOMouvement.TYPE_CET_VIDAGE_INDEMNISATION_ANCIEN_REGIME) {
  		mouvementList = tosMouvementVidageIndemnisationAncienRegime();
  	} else if (typeVidage == EOMouvement.TYPE_CET_VIDAGE_TRANSFERT_RAFP_ANCIEN_REGIME) {
  		mouvementList = tosMouvementVidageTransfertRafpAncienRegime();
  	} 
  	
  	if (!ArrayCtrl.isEmpty(mouvementList)) {
    	EOMouvement mouvement = (EOMouvement) mouvementList.objectAtIndex(0);
    		
    	mouvement.toAffectationAnnuelle().removeFromTosMouvementRelationship(mouvement);
    	editingContext().deleteObject(mouvement);
  	}
  }
}
