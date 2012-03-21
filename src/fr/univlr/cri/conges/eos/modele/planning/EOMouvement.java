package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSNumberFormatter;
import com.webobjects.foundation.NSTimestamp;
import com.webobjects.foundation.NSValidation;

import fr.univlr.cri.conges.constantes.ConstsJour;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.objects.A_DebitableSurCET;
import fr.univlr.cri.conges.objects.I_DebitableSurCET;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.util.wo5.DateCtrl;
import fr.univlr.cri.webapp.LRSort;

public class EOMouvement 
	extends _EOMouvement
		implements I_DebitableSurCET {

	public final static LRSort SORT_INDIVIDU = LRSort.newSort(
			EOMouvement.TO_AFFECTATION_ANNUELLE_KEY + "." + EOAffectationAnnuelle.INDIVIDU_KEY + "." + EOIndividu.NOM_KEY +","+
			EOMouvement.TO_AFFECTATION_ANNUELLE_KEY + "." + EOAffectationAnnuelle.INDIVIDU_KEY + "." + EOIndividu.PRENOM_KEY);			
			
  public EOMouvement() {
  	super();
  }

  public void validateForInsert() throws NSValidation.ValidationException {
  	this.validateObjectMetier();
  	validateBeforeTransactionSave();
  	super.validateForInsert();
  }

  public void validateForUpdate() throws NSValidation.ValidationException {
  	this.validateObjectMetier();
  	validateBeforeTransactionSave();
  	super.validateForUpdate();
  }

  public void validateForDelete() throws NSValidation.ValidationException {
  	super.validateForDelete();
  }

  /**
   * Apparemment cette methode n'est pas appelee.
   * @see com.webobjects.eocontrol.EOValidation#validateForUpdate()
   */    
  public void validateForSave() throws NSValidation.ValidationException {
  	validateObjectMetier();
  	validateBeforeTransactionSave();
  	super.validateForSave();
  }

  /**
   * Peut etre appele a partir des factories.
   * @throws NSValidation.ValidationException
   */
  public void validateObjectMetier() throws NSValidation.ValidationException {
  	// controler que le motif n'est pas vide pour les régularisations de solde
  	if (mouvementType().intValue() == TYPE_REGULARISATION_SOLDE_CONGES) {
  		if (StringCtrl.isEmpty(mouvementLibelle())) {
  			throw new NSValidation.ValidationException("Le motif d'une régularisation de solde est obligatoire !");
  		}
  	}
  }
  
  /**
   * A appeler par les validateforsave, forinsert, forupdate.
   *
   */
  private final void validateBeforeTransactionSave() throws NSValidation.ValidationException {
         
  }

    
  // ajouts
  
  public final static int TYPE_JRTI 																	= 0;
  
  public final static int TYPE_CET_INDEMNISATION_DECRET_2008_1136		=	1;
  
  public final static int TYPE_DECHARGE_SYNDICALE 										= 5;
  public final static int TYPE_RELIQUATS_NON_AUTOMATIQUES 						= 6;
  public final static int TYPE_DROITS_CONGES_NON_AUTOMATIQUES 				= 7;
  public final static int TYPE_REGULARISATION_SOLDE_CONGES 						= 8;

  
  // cet
  
  // --
  // les demandes de l'agent 
  // --
  
  // epargne totale pour une année
  public final static int TYPE_CET_DEMANDE_EPARGNE 																		= 3;
  
  // droit d'option sur le régime pérènne
  public final static int TYPE_CET_DEMANDE_MAINTIEN_CET_FORCE													= 13;
  public final static int TYPE_CET_DEMANDE_MAINTIEN_CET 															= 11;
  public final static int TYPE_CET_DEMANDE_INDEMNISATION															= 9;
  public final static int TYPE_CET_DEMANDE_TRANSFERT_RAFP 														= 10;

  // transfert de régime ancien vers le régime pérénne
  public final static int TYPE_CET_DEMANDE_TRANSFERT_REGIME_PERENNE 							= 15;

  // droit d'option sur l'ancien régime
  public final static int TYPE_CET_DEMANDE_MAINTIEN_CET_ANCIEN_REGIME 					= 18;
  public final static int TYPE_CET_DEMANDE_INDEMNISATION_ANCIEN_REGIME					= 19;
  public final static int TYPE_CET_DEMANDE_TRANSFERT_RAFP_ANCIEN_REGIME 			= 20;
  
  // --
  // les decisions du gestionnaire
  // --
  
  // epargne totale pour une année
  public final static int TYPE_CET_DECISION_EPARGNE 																	= 4;

  // droit d'option sur le régime pérènne
  public final static int TYPE_CET_DECISION_MAINTIEN_CET_FORCE												= 14;
  public final static int TYPE_CET_DECISION_MAINTIEN_CET 															= 12;
  public final static int TYPE_CET_DECISION_INDEMNISATION															= 17;
  public final static int TYPE_CET_DECISION_TRANSFERT_RAFP														= 2;

  // transfert de régime ancien vers le régime pérénne
  public final static int TYPE_CET_DECISION_TRANSFERT_REGIME_PERENNE 						= 16;

  // droit d'option sur l'ancien régime
  public final static int TYPE_CET_DECISION_MAINTIEN_CET_ANCIEN_REGIME 				= 22;
  public final static int TYPE_CET_DECISION_INDEMNISATION_ANCIEN_REGIME				= 23;
  public final static int TYPE_CET_DECISION_TRANSFERT_RAFP_ANCIEN_REGIME 		= 24;
  
  // vidage du CET suite au départ de l'agent ou non décision sur l'ancien régime
  public final static int TYPE_CET_VIDAGE_INDEMNISATION_ANCIEN_REGIME						=	25;
  public final static int TYPE_CET_VIDAGE_TRANSFERT_RAFP_ANCIEN_REGIME					=	26;
  public final static int TYPE_CET_VIDAGE_INDEMNISATION																=	27;
  public final static int TYPE_CET_VIDAGE_TRANSFERT_RAFP															=	28;

  /**
   * Creation d'un enregistrement
   * @param affectationAnnuelle
   * @param type
   * @param minutes
   * @return
   */
  public static EOMouvement newMouvement(
  		EOAffectationAnnuelle affectationAnnuelle, int type, int minutes) {
  	NSTimestamp now = DateCtrl.now();
  	EOMouvement newMouvement = createMouvement(
  			affectationAnnuelle.editingContext(), now, now, minutes, type, affectationAnnuelle);
  	return newMouvement;
  }
  
  /**
   * la valeur de {@link #mouvementMinutes()} converties en heures
   * @return
   */
  public String mouvementHeures() {
  	return TimeCtrl.stringForMinutes(mouvementMinutes());
  }

  /**
   * la valeur de {@link #mouvementMinutes()} converties et 
   * arrondies à l'entier inférieur en jours à 7h00
   * @return
   */
  public Integer mouvementJoursA7h00Arrondi() {
  	return new Integer(mouvementMinutes() / ConstsJour.DUREE_JOUR_7H00);
  }

  /**
   * la valeur de {@link #mouvementMinutes()} converties en jours à 7h00
   * @return
   */
  public Float mouvementJoursA7h00() {
  	return new Float((float) mouvementMinutes() / (float) ConstsJour.DUREE_JOUR_7H00);
  }
  
  /**
   * modifier la valeur de {@link #mouvementMinutes()} a partir d'une 
   * version en heures
   * @return
   */
  public void setMouvementHeures(String mouvementHeures) {
  	Integer mouvementMinutes = null;
  	if (StringCtrl.isEmpty(mouvementHeures)) {
  		mouvementMinutes = new Integer(0);
  	} else {
    	if (mouvementHeures.startsWith("-")) {
        mouvementMinutes = new Integer(-TimeCtrl.getMinutes(mouvementHeures.substring(1, mouvementHeures.length())));
    	} else {
        mouvementMinutes = new Integer(TimeCtrl.getMinutes(mouvementHeures));
    	}
  	}
  	setMouvementMinutes(mouvementMinutes);
  }
	
  // gestion de la repartition des rachats CET et bascule CET vers RAFP
  // sur les transactions de CET -> interface I_DebitableSurCET
	
	private class _EOMouvementDebitableSurCET 
		extends A_DebitableSurCET {

		/**
		 * Date de valeur d'un débitable sur CET. Les cas possibles sont
		 * les indemnisation, les RAFP ou les transfert.
		 * Dans tous les cas, c'est donc le 1er janvier de l'année civile.
		 */
		public NSTimestamp dateValeur() {
			NSTimestamp dateValeur = null;
			
			dateValeur = DateCtrlConges.date1erJanAnneeUniv(toAffectationAnnuelle().dateDebutAnnee());
			
			return dateValeur;
		}
		
		public Number dureeADebiter()	{			
			return mouvementMinutes();	
		}
		
		public String displayCet() {
			String displayCet = null;
			
			if (mouvementType().intValue() == TYPE_CET_INDEMNISATION_DECRET_2008_1136 ||
					mouvementType().intValue() == TYPE_CET_DECISION_INDEMNISATION ||
					mouvementType().intValue() == TYPE_CET_DECISION_INDEMNISATION_ANCIEN_REGIME) {
				displayCet = "Indemnisation";
				if (mouvementType().intValue() == TYPE_CET_INDEMNISATION_DECRET_2008_1136) {
					displayCet += " (décret 2008-1136)";
				} else if (mouvementType().intValue() == TYPE_CET_DECISION_INDEMNISATION_ANCIEN_REGIME) {
					displayCet += " (ancien régime)";
				}
			} else if (mouvementType().intValue() == TYPE_CET_DECISION_TRANSFERT_RAFP ||
					mouvementType().intValue() == TYPE_CET_DECISION_TRANSFERT_RAFP_ANCIEN_REGIME) {
				displayCet = "Transfert en RAFP";
				if (mouvementType().intValue() == TYPE_CET_DECISION_TRANSFERT_RAFP_ANCIEN_REGIME) {
					displayCet += " (ancien régime)";
				}
			} else if (mouvementType().intValue() == TYPE_CET_DECISION_TRANSFERT_REGIME_PERENNE) {
				displayCet = "Transfert vers le régime pérenne";
			} else if (mouvementType().intValue() == TYPE_CET_VIDAGE_INDEMNISATION) {
				displayCet = "Indemnisation de l'intégralité du CET";
			} else if (mouvementType().intValue() == TYPE_CET_VIDAGE_TRANSFERT_RAFP) { 
				displayCet = "Transfert en RAFP de l'intégralité du CET";
			}	else if (mouvementType().intValue() == TYPE_CET_VIDAGE_INDEMNISATION_ANCIEN_REGIME) {
				displayCet = "Indemnisation du CET ancien régime";
			} else if (mouvementType().intValue() == TYPE_CET_VIDAGE_TRANSFERT_RAFP_ANCIEN_REGIME) {
				displayCet = "Transfert en RAFP du CET ancien régime";
			}

			displayCet += " au " + DateCtrlConges.dateToString(dateValeur()) + " (";
			
			// formatter le nombre de jours différemment si c'est un transfert régime pérenne ou indemnisation / RAFP
			if (mouvementType().intValue() == TYPE_CET_DECISION_TRANSFERT_REGIME_PERENNE ||
					mouvementType().intValue() == TYPE_CET_VIDAGE_INDEMNISATION ||
					mouvementType().intValue() == TYPE_CET_VIDAGE_TRANSFERT_RAFP ||
					mouvementType().intValue() == TYPE_CET_VIDAGE_INDEMNISATION_ANCIEN_REGIME ||
					mouvementType().intValue() == TYPE_CET_VIDAGE_TRANSFERT_RAFP_ANCIEN_REGIME) {
				NSNumberFormatter nf = new NSNumberFormatter("0.00");
				displayCet += nf.format(mouvementJoursA7h00());
			} else {
				displayCet += mouvementJoursA7h00Arrondi();
			}

			displayCet += "j. à 7h00)";
			
			return displayCet;
		}
		
		public EOAffectationAnnuelle toAffectationAnnuelle() {
			return EOMouvement.this.toAffectationAnnuelle();		
		}
	}
	
	private _EOMouvementDebitableSurCET debitableSurCET = new _EOMouvementDebitableSurCET();

	public void clearCache() 									{		debitableSurCET.clearCache();	}
	public void debiter(int minutesADebiter) 	{		debitableSurCET.debiter(minutesADebiter);	}
	public int minutesRestantesADebiter() 		{		return debitableSurCET.minutesRestantesADebiter();	}
	public NSArray tosTransaction() 					{		return debitableSurCET.tosTransaction();	}
	public NSTimestamp dateValeur() 					{		return debitableSurCET.dateValeur();	}
	public String displayCet() 								{		return debitableSurCET.displayCet();	}
	public Number dureeADebiter() 						{		return debitableSurCET.dureeADebiter();	}
}
