package fr.univlr.cri.conges.objects;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOOrQualifier;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.AdminCETListeDemande;
import fr.univlr.cri.conges.PageAdminCETOld;
import fr.univlr.cri.conges.PageCET;
import fr.univlr.cri.conges.PersoFicheRose;
import fr.univlr.cri.conges.constantes.ConstsJour;
import fr.univlr.cri.conges.databus.CngDroitBus;
import fr.univlr.cri.conges.databus.CngPreferenceBus;
import fr.univlr.cri.conges.databus.CngUserInfo;
import fr.univlr.cri.conges.eos.modele.conges.EOCET;
import fr.univlr.cri.conges.eos.modele.conges.EOCETTransaction;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOMouvement;
import fr.univlr.cri.conges.eos.modele.planning.EOVIndividuConges;
import fr.univlr.cri.conges.eos.modele.planning.I_ClasseMetierNotificationParametre;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.ycrifwk.utils.UtilDb;

/**
 * L'ensemble des traitements liés au CET par rapport 
 * à un objet {@link EOAffectationAnnuelle}
 * 
 * @author ctarade
 */
public class CetFactory 
	implements I_ClasseMetierNotificationParametre {
	
	//
	private EOAffectationAnnuelle affAnn;
	// faut-il vérifier le statut RH (contrat, titulaire) pour les demandes d'épargne ?
	private static boolean appVerifierStatutDemandeEpargneCet;
	// faut-il autoriser les demandes d'épargnes pour les contractuels non CDI ?
	private static boolean appAutoriserDemandeEpargneCetCddNonCdi;
	// gestion des messages d'erreur
	private String errorMessage;
	
	/**
	 * Constructeur référençant l'affectation annuelle associée
	 * @param affAnn
	 */
	public CetFactory(EOAffectationAnnuelle affAnn) {
		this.affAnn = affAnn;
	}
	
  /**
   * @see I_ClasseMetierNotificationParametre
   * @param parametre
   */
  public static void initStaticField(
  		Parametre parametre) {
  	if (parametre == Parametre.PARAM_VERIFIER_STATUT_DEMANDE_EPARGNE_CET) {
  		appVerifierStatutDemandeEpargneCet = parametre.getParamValueBoolean().booleanValue();
  	} else if (parametre == Parametre.PARAM_AUTORISER_DEMANDE_EPARGNE_CET_CDD_NON_CDI) {
  		appAutoriserDemandeEpargneCetCddNonCdi = parametre.getParamValueBoolean();
  	}
  }
  

	
	/**
	 * Raccourci vers l'affectation annuelle concernée
	 * @return
	 */
	private EOAffectationAnnuelle affAnn() {
		return affAnn;
	}
	
	/**
	 * Effacer le cache 
	 */
	public void clearCache() {
  	_isPeriodeDemandeEpargneCet = null;
  	_plafondEpargneCetEnMinutes = null;
  	_reliquatPourBlocageCetMaxEnMinutes = null;
	}

	/**
	 * 
	 */
	private void resetError() {
		errorMessage = null;
	}

	/**
	 * 
	 * @return
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasError() {
		return !StringCtrl.isEmpty(errorMessage);
	}
	

  private Integer _reliquatPourBlocageCetMaxEnMinutes; 
  
  /**
   * La valeur maximale que l'agent peut demander en blocage CET.
   * 
   * La règle est (1 jour = 7h00) :
   * - ne pas dépasser le plafond maximum de 25 j., déduit du droit à congés
   *  normal pour un agent de l'éduction national de 45 j., auquel on ôte les
   *  20 j. d'épargne minimum pour accèder (paramètre de l'application PLAFOND_EPARGNE_CET_<ANNEE>)
   * 
   * Possible uniquement si lors du planning N-1, l'agent a pris au moins 20 j. sur ses congés annuels.
   * Mais on ne bloque pas sur cette contrainte (cas des changement de service ou mutation ... ce sera
   * un message affiché au demandeur et à l'administrateur qui prendra la décision qui convient)
   * Ces 20 j. peuvent être paramétrés avec SEUIL_CONGES_CONSOMMES_JOUR_7H00_NM1_POUR_EPARGNE_CET_<ANNEE>
   * 
   * Valeurs à maximiser par le reliquats de congés (on n'épargne pas plus que ce que l'on a en reliquats)
   * Valeurs à ponderer à la (aux) quotité(s) d'affectation au service
   */
  public int reliquatPourBlocageCetMaxEnMinutes() {
  	if (_reliquatPourBlocageCetMaxEnMinutes == null) {
    	int reliquatPourBlocageCetMaxEnMinutes = 0;
    	
    	// verifier s'il a du reliquat initial
    	int reliquatInitialEnMinutes = PlanningCalcul.reliquatInitialEnMinutesForAffAnn(
    			affAnn().editingContext(), affAnn());
    	if (reliquatInitialEnMinutes > 0) {
    	
    		// maximiser le reliquats par le plafond
    		reliquatPourBlocageCetMaxEnMinutes = reliquatInitialEnMinutes;
    		
    		int plafondEpargneCetEnMinutes = plafondEpargneCetEnMinutes();
    		if (reliquatPourBlocageCetMaxEnMinutes > plafondEpargneCetEnMinutes) {
    			reliquatPourBlocageCetMaxEnMinutes = plafondEpargneCetEnMinutes;
    		}
    	}
    	
    	_reliquatPourBlocageCetMaxEnMinutes = new Integer(reliquatPourBlocageCetMaxEnMinutes);
  	}
  	return _reliquatPourBlocageCetMaxEnMinutes;
  }
  

  private Integer _plafondEpargneCetEnMinutes;
  
  /**
   * Donne le plafond des minutes epargnables en CET
   * @return
   */
  private int plafondEpargneCetEnMinutes() { 	
  	if (_plafondEpargneCetEnMinutes == null) {
      float plafondEpargneCetEnMinutes = 0;
      
      // on classe les periodes chronologiquement
      /*NSArray periodes = affAnn.periodes();
       
      for (int i=0; i<periodes.count(); i++) {
      	EOPeriodeAffectationAnnuelle curPeriode = (EOPeriodeAffectationAnnuelle) periodes.objectAtIndex(i);
      	plafondEpargneCetEnMinutes += curPeriode.valeurPonderee(affAnn().plafondEpargneCet() * ConstsJour.DUREE_JOUR_7H00);
      }*/
      
      plafondEpargneCetEnMinutes = affAnn().quotiteMoyennePlanningUnique(false) * (float) affAnn().plafondEpargneCet() * (float) ConstsJour.DUREE_JOUR_7H00;
    
      _plafondEpargneCetEnMinutes = new Integer(Math.round(plafondEpargneCetEnMinutes));
  		
  	}
  	
  	return _plafondEpargneCetEnMinutes.intValue();
  }

  
  /**
   * {@link #consommationCongesAnneePrecedenteEnMinutes()} en heures
   * @return
   */
  public String consommationCongesAnneePrecedenteEnHeures() {
  	String consommationCongesAnneePrecedenteEnHeures = null;
  	
  	consommationCongesAnneePrecedenteEnHeures = TimeCtrl.stringForMinutes(consommationCongesAnneePrecedenteEnMinutes());
  
  	return consommationCongesAnneePrecedenteEnHeures;
  }
  
  /**
   * {@link #consommationCongesAnneePrecedenteEnMinutes()} en jours à 7h00
   * @return
   */
  public float consommationCongesAnneePrecedenteEnJour7h00() {
  	float consommationCongesAnneePrecedenteEnJour7h00 = (float) consommationCongesAnneePrecedenteEnMinutes() / (float)ConstsJour.DUREE_JOUR_7H00;

  	return consommationCongesAnneePrecedenteEnJour7h00;
  }

  /**
   * La consommation de conges sur l'année N-1.
   * Cette valeur doit atteindre un minimum pour que l'agent 
   * puisse prétendre a une épargne CET
   * @return
   */
  private int consommationCongesAnneePrecedenteEnMinutes() {
  	int consommationCongesAnneePrecedenteEnMinutes = 0;
  	
  	EOEditingContext ec = affAnn().editingContext();
  	
  	EOVIndividuConges prevRecord = EOVIndividuConges.getPrevRecord(ec, affAnn());
  	if (prevRecord != null) {
  		consommationCongesAnneePrecedenteEnMinutes = prevRecord.cngConsomme().intValue();
  		
  		// si pas calculé, il faut le faire
  		if (consommationCongesAnneePrecedenteEnMinutes == -1) {
  			// rechargement
				CngUserInfo ui = new CngUserInfo(
						new CngDroitBus(ec),
						new CngPreferenceBus(ec),
						ec,
						affAnn().individu().persId());
				// 
				EOAffectationAnnuelle prevAffAnn = 
					(EOAffectationAnnuelle) EOAffectationAnnuelle.findAffectationsAnnuelleInContext(
						ec, affAnn().individu().oid(), affAnn().structure().cStructure(), prevRecord.dteDebutAnnee()).lastObject();
				Planning planning = Planning.newPlanning(
						prevAffAnn, ui, prevAffAnn.dateDebutAnnee());
				planning.setType("R");
				// voilà, on a maintenant la valeur calculée
				consommationCongesAnneePrecedenteEnMinutes = planning.affectationAnnuelle().calculAffAnn("R").minutesCongesConsommees();

  		}
  	}
  	
  	return consommationCongesAnneePrecedenteEnMinutes;
  }
  
  /**
   * {@link #droitCongesAnneePrecedenteEnMinutes()} en heures
   * @return
   */
  public String droitCongesAnneePrecedenteEnHeures() {
  	String droitCongesAnneePrecedenteEnHeures = null;
  	
  	droitCongesAnneePrecedenteEnHeures = TimeCtrl.stringForMinutes(droitCongesAnneePrecedenteEnMinutes());
  
  	return droitCongesAnneePrecedenteEnHeures;
  }
  
  /**
   * {@link #droitCongesAnneePrecedenteEnMinutes()} en jours à 7h00
   * @return
   */
  public float droitCongesAnneePrecedenteEnJour7h00() {
  	float droitCongesAnneePrecedenteEnJour7h00 = (float) droitCongesAnneePrecedenteEnMinutes() / (float)ConstsJour.DUREE_JOUR_7H00;

  	return droitCongesAnneePrecedenteEnJour7h00;
  }

  /**
   * La droits de conges sur l'année N-1.
   * @return
   */
  private int droitCongesAnneePrecedenteEnMinutes() {
  	int droitCongesAnneePrecedenteEnMinutes = 0;
  	
  	EOVIndividuConges prevRecord = EOVIndividuConges.getPrevRecord(affAnn().editingContext(), affAnn());
  	if (prevRecord != null) {
  		droitCongesAnneePrecedenteEnMinutes = prevRecord.cngInitial().intValue();
  	}
  	
  	return droitCongesAnneePrecedenteEnMinutes;
  }
  
  /**
   * 
   * @return
   */
  public boolean hasConsommationCongesAnneePrecedenteMinimumPourEpargneCet() {
  	return consommationCongesAnneePrecedenteEnMinutes() >= affAnn().seuilCongesConsommesJour7h00Nm1PourEpargneCet() * ConstsJour.DUREE_JOUR_7H00;
  }
  
  /**
   * #reliquatPourBlocageCetMaxEnMinutes() converties en heures
   * @return
   */
  public String reliquatPourBlocageCetMaxEnHeures() {
  	String reliquatPourBlocageCetMaxEnHeures = null;
  	if (reliquatPourBlocageCetMaxEnMinutes() != -1) {
  		reliquatPourBlocageCetMaxEnHeures = TimeCtrl.stringForMinutes(reliquatPourBlocageCetMaxEnMinutes());
  	}
  	return reliquatPourBlocageCetMaxEnHeures;
  }
  
  /**
   * #reliquatPourBlocageCetMaxEnMinutes() converties en jours a 7h00 et arrondies
   * @return
   */
  public Integer reliquatPourBlocageCetMaxEnJour7h00Arrondi() {
  	Integer reliquatPourBlocageCetMaxEnJour = null;
  	if (reliquatPourBlocageCetMaxEnMinutes() != -1) {
  		reliquatPourBlocageCetMaxEnJour = reliquatPourBlocageCetMaxEnMinutes() / ConstsJour.DUREE_JOUR_7H00;
  	}
  	return reliquatPourBlocageCetMaxEnJour;
  }
  
  // etat du blocage de reliquat pour bascule CET
  
  /**
   * @see PersoFicheRose
   * Indique si la décision relative à la demande d'épargne à été faite.
   * Il doit nécéssairement exister un enregistrement de demande correspondant.
   */
  public boolean isEpargneCetDecisionPrise() {
 	 return affAnn().toMouvementCetDemandeEpargne() != null && affAnn().toMouvementCetDecisionEpargne() != null;
  }

  /**
   * @see PersoFicheRose
   * Indique si la demande d'épargne CET n'a pas encore fait l'objet d'une décision.
   */
  public boolean isEpargneCetDemandeEnAttenteDeDecision() {
 	 return affAnn().toMouvementCetDemandeEpargne() != null && affAnn().toMouvementCetDecisionEpargne() == null; 
  }
 
  /**
   * Indique si aucune demande d'épargne CET n'a été faite (ni demande, ni décision)
   */
  public boolean isEpargneCetDemandeNonFaite() {
  	return affAnn().toMouvementCetDemandeEpargne() == null && affAnn().toMouvementCetDecisionEpargne() == null;
  }

  /**
   * @deprecated
   * @see PageAdminCETOld
   * Savoir si la quantité demandée et la même que la quantité épargnée
   */
  public boolean isEpargneCetDemandeDecisionIdentique() {
  	return isEpargneCetDecisionPrise() &&
  	affAnn().toMouvementCetDemandeEpargne().mouvementMinutes().intValue() == affAnn().toMouvementCetDecisionEpargne().mouvementMinutes().intValue();
  }

  
  private NSArray _affAnnList;

  /**
   * La liste de toutes les EOAffectationAnnuelle de l'agent
   */
  private NSArray affAnnList() {
  	if (_affAnnList == null) {
  		_affAnnList = EOAffectationAnnuelle.findAffectationsAnnuelleInContext(
  				affAnn().editingContext(), affAnn().individu().oid(), null, null);
  	}
  	return _affAnnList;
  }
  
  
  /**
   * Indique si aucune demande de transfert de l'ancien régime vers le régime pérenne
   * n'a été faite (ni demande, ni décision) 
   * 
   * @param isVerifierToutesAnnee
   * 	le controle doit se faire sur toutes les années. mettre <code>false</code> pour
   *  la annee de {@link #affAnn()}
   */
  private boolean isJamaisStatueAncienRegime(boolean isVerifierToutesAnnee) {
  	// on va rechercher s'il existe au moins un mouvement 
  	// qui permet de savoir s'il y a eu demande et/ou décision
  	// sur l'ancien régime
  	boolean isJamaisStatueAncienRegime = true;
  
  	String strQual = 
  			EOMouvement.MOUVEMENT_TYPE_KEY + "=" + EOMouvement.TYPE_CET_DEMANDE_TRANSFERT_REGIME_PERENNE + " or " +
  			EOMouvement.MOUVEMENT_TYPE_KEY + "=" + EOMouvement.TYPE_CET_DECISION_TRANSFERT_REGIME_PERENNE + " or " +
  			EOMouvement.MOUVEMENT_TYPE_KEY + "=" + EOMouvement.TYPE_CET_DEMANDE_INDEMNISATION_ANCIEN_REGIME + " or " +
  			EOMouvement.MOUVEMENT_TYPE_KEY + "=" + EOMouvement.TYPE_CET_DECISION_INDEMNISATION_ANCIEN_REGIME + " or " +
  			EOMouvement.MOUVEMENT_TYPE_KEY + "=" + EOMouvement.TYPE_CET_DEMANDE_TRANSFERT_RAFP_ANCIEN_REGIME + " or " +
  			EOMouvement.MOUVEMENT_TYPE_KEY + "=" + EOMouvement.TYPE_CET_DECISION_TRANSFERT_RAFP_ANCIEN_REGIME + " or " +
  			EOMouvement.MOUVEMENT_TYPE_KEY + "=" + EOMouvement.TYPE_CET_DEMANDE_MAINTIEN_CET_ANCIEN_REGIME + " or " +
  			EOMouvement.MOUVEMENT_TYPE_KEY + "=" + EOMouvement.TYPE_CET_DECISION_MAINTIEN_CET_ANCIEN_REGIME;
  	
  	// 
  	if (isVerifierToutesAnnee) {
  		// sur toutes les années
  		NSArray arrQualAffAnn = new NSArray();
  		for (int i=0; i<affAnnList().count(); i++) {
  			EOQualifier qual = CRIDataBus.newCondition(
  					EOMouvement.TO_AFFECTATION_ANNUELLE_KEY + "=%@ and (" + strQual + ")",
  					new NSArray(affAnnList().objectAtIndex(i)));
  			arrQualAffAnn = arrQualAffAnn.arrayByAddingObject(qual);
  		}
  		
  		if (EOMouvement.fetchMouvements(
  				affAnn().editingContext(), new EOOrQualifier(arrQualAffAnn), null).count() > 0) {
    		isJamaisStatueAncienRegime = false;
  		}
  	} else {
  		// seulement pour cette année
    	if (affAnn().tosMouvement(CRIDataBus.newCondition(strQual)).count() > 0) {
    		isJamaisStatueAncienRegime = false;
    	}

  	}
  	
  	return isJamaisStatueAncienRegime;
  }
 
  /**
   * @deprecated
   * @see PageAdminCET
   * Indique si aucune demande de transfert de l'ancien système vers le régime pérenne
   * n'a pas encore fait l'objet d'une décision.
   */
  public boolean isTransfertCetDemandeEnAttenteDeDecision() {
  	return affAnn().toMouvementCetDemandeTransfertRegimePerenne() != null && affAnn().toMouvementCetDecisionTransfertRegimePerenne() == null; 
  }
 
  
  // ecran de gestion CET

  
  /**
   * La consommation sur reliquats et congés sur la periode
   * de validité des reliquats N'a de sens que pour le plannings
   * reel car cette valeur concerne les demandes liées au CET, 
   * donc au réel uniquement.
   * @return
   */
  public String consommationCongesReliquatPendantValiditeReliquatEnHeures() {
  	String consommationCongesReliquatPendantValiditeReliquatEnHeures = null;
  	if (affAnn().calculAffAnn("R").minutesConsommeesDateReliquat() != -1) {
  		consommationCongesReliquatPendantValiditeReliquatEnHeures = TimeCtrl.stringForMinutes(affAnn().calculAffAnn("R").minutesConsommeesDateReliquat());
  	}
  	return consommationCongesReliquatPendantValiditeReliquatEnHeures;
  }

  
  /**
   * La consommation sur reliquats sur la periode
   * de validité des reliquats N'a de sens que pour le plannings
   * reel car cette valeur concerne les demandes liées au CET, 
   * donc au réel uniquement.
   * @return
   */
  public int consommationReliquatPendantValiditeReliquatEnMinutes() {
  	int consommationReliquatPendantValiditeReliquatEnMinutes = 0;
  	if (affAnn().calculAffAnn("R").minutesReliquatConsommeesDateReliquat() != -1) {
  		consommationReliquatPendantValiditeReliquatEnMinutes = affAnn().calculAffAnn("R").minutesReliquatConsommeesDateReliquat();
  	}
  	return consommationReliquatPendantValiditeReliquatEnMinutes;
  }
  
  /**
   * {@link #consommationReliquatPendantValiditeReliquatEnMinutes()} en heures
   * @return
   */
  public String consommationReliquatPendantValiditeReliquatEnHeures() {
  	String consommationReliquatPendantValiditeReliquatEnHeures = TimeCtrl.stringForMinutes(consommationReliquatPendantValiditeReliquatEnMinutes());

  	return consommationReliquatPendantValiditeReliquatEnHeures;
  }
  
  /**
   * #consommationReliquatPendantValiditeReliquatEnMinutes() converties en jours a 7h00
   * @return
   */
  public float consommationReliquatPendantValiditeReliquatEnJour7h00() {
  	float consommationReliquatPendantValiditeReliquat7h00 = (float)consommationReliquatPendantValiditeReliquatEnMinutes() / (float)ConstsJour.DUREE_JOUR_7H00;

  	return consommationReliquatPendantValiditeReliquat7h00;
  }

  /**
   * La valeur des reliquats de congés non consommés sur congés
   * pendant la date de validité des reliquats
   * @return
   */
  public int reliquatNonConsommePendantValiditeReliquatEnMinutes() {
  	return affAnn().reliquatInitialEnMinutes() - consommationReliquatPendantValiditeReliquatEnMinutes();
  }
  
  /**
   * {@link #reliquatNonConsommePendantValiditeReliquatEnMinutes()} en heures
   * @return
   */
  public String reliquatNonConsommePendantValiditeReliquatEnHeures() {
  	return TimeCtrl.stringForMinutes(reliquatNonConsommePendantValiditeReliquatEnMinutes());
  }


  /**
   * {@link #reliquatNonConsommePendantValiditeReliquatEnMinutes()} en jours à 7h00
   * @return
   */
  public float reliquatNonConsommePendantValiditeReliquatEnJour7h00() {
  	float reliquatInitialEnJour7h00 = (float)reliquatNonConsommePendantValiditeReliquatEnMinutes() / (float)ConstsJour.DUREE_JOUR_7H00;

  	return reliquatInitialEnJour7h00;
  }

  
  // savoir si la periode de bascule CET est autorisee
  
  private Boolean _isPeriodeDemandeEpargneCet;
  
  /**
   * Indique si la periode de saisie CET est ouverte
   * @return
   */
  public boolean isPeriodeDemandeEpargneCet() {
    if (_isPeriodeDemandeEpargneCet == null) {
    	_isPeriodeDemandeEpargneCet = new Boolean(
          DateCtrlConges.isAfterEq(DateCtrlConges.now(), affAnn().dateDebutDemandeCet()) &&
          DateCtrlConges.isBeforeEq(DateCtrlConges.now(), affAnn().dateFinDemandeCet()));
    }
    return _isPeriodeDemandeEpargneCet.booleanValue();
  }
  
  
  // repartition des epargnes CET (options CET, RAFP ou paiement)
  
  /**
   * Indique si l'épargne qui va être demandée conduit l'utilisateur
   * a devoir faire une option (oui s'il dépasse le seuil de 20 jours
   * à 7h00 en CET)
   */
  public boolean isOptionPossible(
  		int epargneEnMinutes) {
  	boolean isOptionPossible = false;
  	
  	// voir si avec son epargne, il depasse les 20 jours
  	int totalAvecEpargne = soldeEnMinutes() + epargneEnMinutes;
  	
		if (totalAvecEpargne > 20 * ConstsJour.DUREE_JOUR_7H00) {
			isOptionPossible = true;
		}
  	
		return isOptionPossible;
  }
 
  /**
   * La valeur minimum que l'agent doit mettre en CET en considérant
   * que son épargne sera de epargneEnMinutes, et qu'il y aurait un transfert
   * de CET de l'ancien vers le nouveau système.
   * 
   * L'agent doit conserver au minimum 20 jours en CET (épargne inclue)
   */
  public int maintienObligatoireCetPourDemandeEpargneCetEnMinutes(
  		int epargneEnMinutes) {
  	int minutes = 0;
  
  	// voir si avec son epargne, il depasse les 20 jours
		int totalSansEpargne = soldeEnMinutes();
		
  	int totalAvecEpargne = totalSansEpargne + epargneEnMinutes;
		boolean hasMoinsDe20JoursEnCetAvecEpargne = true;
		if (totalAvecEpargne > 20 * ConstsJour.DUREE_JOUR_7H00) {
			hasMoinsDe20JoursEnCetAvecEpargne = false;
		}
  	
		if (hasMoinsDe20JoursEnCetAvecEpargne) {
			// moins de 20 jours : toute l'epargne doit etre en CET
			minutes = epargneEnMinutes;
		} else {
			// plus de 20 jours : le delta entre total sans epargne si
			// - de 20 jours et 20 jours
			if (totalSansEpargne < 20 * ConstsJour.DUREE_JOUR_7H00) {
				minutes = ( 20 * ConstsJour.DUREE_JOUR_7H00 - totalSansEpargne);
				// on arrondit au jour supérieur
				int cetMinimumPourEpargneCetEnJours = minutes / ConstsJour.DUREE_JOUR_7H00;
				// faut-il arrondir ?
				if (minutes != 0 && 
						minutes % ConstsJour.DUREE_JOUR_7H00 != 0) {
					cetMinimumPourEpargneCetEnJours += 1;
				}
				minutes = cetMinimumPourEpargneCetEnJours * ConstsJour.DUREE_JOUR_7H00;
			}
		}
		
  	return minutes;
  }
  
  /**
   * La valeur maximum de congés d'une épargne CET qui peuvent
   * être maintenus dans le CET de l'agent, en considérant
   * que son épargne sera epargneEnMinutes, en supplément d'un
   * maintien fixe obligatoire de 20j. déjà appliqué.
   * 
   * A partir de 20 jours épargnés, la progression maximum est de 10 jours.
   * Pour moins de 20, la progression maximum est de 20 jours, maximisé par
   * un total épargné en CET de 30 jours.
   * 
   * @param epargneEnMinutes : la valeur de l'épargne demandée
   * @return
   */
  public int maintienCetMaximumAuDela20JEnMinutes(
  		int epargneEnMinutes) {

  	int totalSansEpargne = soldeEnMinutes();
  	
  	// le nombre de minutes de progression du solde max pour cette épargne
  	int progressionMax = 0;

  	// de la progression uniquement s'il y a épargne
  	if (epargneEnMinutes > 0) {

    	// le solde est sous la barre des 20j.
    	if (totalSansEpargne < 20 * ConstsJour.DUREE_JOUR_7H00) {
    		// progression maximisée jusqu'au 30ème j. de solde
    		progressionMax = 30 * ConstsJour.DUREE_JOUR_7H00 - totalSansEpargne;
    		// accroissement maximum de 30j.
    		if (progressionMax > 30 * ConstsJour.DUREE_JOUR_7H00) {
    			progressionMax = 30 * ConstsJour.DUREE_JOUR_7H00;
    		}
    		
    	} else {
    		// le solde est au dessus des 20j.
    		// la progression maximum est de 10j.
    		progressionMax = 10 * ConstsJour.DUREE_JOUR_7H00;
    	}
    	
    	// maximiser la progression à hauteur de l'épargne
  		if (progressionMax > epargneEnMinutes) {
  			progressionMax = epargneEnMinutes;
  		}
  		
  	}

  	// valeur du maintien forcé en régime pérenne associé à cette épargne
  	int maintienForce = 20 * ConstsJour.DUREE_JOUR_7H00;
  	// s'il n'atteint pas les 20, on maintient le tout
  	// normalement, ce cas ne se présente jamais
  	if (totalSansEpargne + epargneEnMinutes < 20 * ConstsJour.DUREE_JOUR_7H00) {
  		maintienForce = totalSansEpargne + epargneEnMinutes;
  	}
  	
  	// valeur finale
  	int maintienMaxAuDela20J = totalSansEpargne + progressionMax - maintienForce;

  	// maximisé à 40j
  	if (maintienMaxAuDela20J > 40 * ConstsJour.DUREE_JOUR_7H00) {
  		maintienMaxAuDela20J = 40 * ConstsJour.DUREE_JOUR_7H00;
  	}
  	
  	return maintienMaxAuDela20J;
  }
  
  /**
   * {@link #maintienCetMaximumAuDela20JEnMinutes(int)} converties en jours à 7h00,
   * arrondi à l'entier inférieur
   * @param epargneEnMinutes
   * @return
   */
  public int maintienCetMaximumAuDela20JEnJour7h00Arrondi(
  		int epargneEnMinutes) {
  	return maintienCetMaximumAuDela20JEnMinutes(epargneEnMinutes) / ConstsJour.DUREE_JOUR_7H00;
  }
  
  /**
   * La valeur du dépassement de CET de l'agent s'il fait une épargne
   * de epargneEnMinutes sur les 20 jours de maintien obligatoire.
   * 
   * @param epargneEnMinutes
   * @return
   */
  public int depassementSeuil20JoursPourEpargneCetEnMinutes(
  		int epargneEnMinutes) {
  	int depassement = 0;
  	
  	// voir si avec son epargne, il depasse les 20 jours
  	int totalAvecEpargne = soldeEnMinutes() + epargneEnMinutes;
		if (totalAvecEpargne > 20 * ConstsJour.DUREE_JOUR_7H00) {
			depassement = totalAvecEpargne - 20 * ConstsJour.DUREE_JOUR_7H00;
		}
		
		return depassement;
  }  
  /**
   * {@link #depassementSeuil20JoursPourDemandeEpargneCetEnMinutes(int, int)} 
   * convertit en jours à 7h00 arrondi à l'entier inférieur
   */
  public int depassementSeuil20JoursPourEpargneCetEnJours7h00Arrondi(
  		int epargneEnMinutes) {
  	int depassement = 0;
  	
  	depassement = depassementSeuil20JoursPourEpargneCetEnMinutes(
  			epargneEnMinutes) / ConstsJour.DUREE_JOUR_7H00;
		
		return depassement;
  }
  
  /**
   * Indique si le statut de l'agent lui permet de faire une demande d'épargne CET
   * @return
   */
  private boolean isStatutAutorise() {
  	boolean isStatutAutorise = false;
  	
  	// faut-il vérifier le statut
  	if (appVerifierStatutDemandeEpargneCet) {
  		// voir alors si on autorise les demandes uniquement pour les
  		// CDI et les fonctionnaires
  		if (!appAutoriserDemandeEpargneCetCddNonCdi) {
  			isStatutAutorise = !affAnn().individu().isContractuel(affAnn()) || affAnn().individu().isContractuelCDI(affAnn());
  		} else {
  			isStatutAutorise = true;
  		}
  	} else {
  		// pas de vérification de statut
  		isStatutAutorise = true;
  	}
  	
  	return isStatutAutorise;
  }
  
  /**
   * Indique si l'agent peut faire une demande d'épargne CET :
   * - une demande d'épargne n'est pas encore faite
   * - la période de saisie est ouverte
   * - son statut est autorisé
   * - il possède au moins 1 jour a 7h00 de reliquat initial
   * @param shouldIgnorePeriodeOuverture TODO
   * @return
   */
  public boolean isEpargneFaisable(boolean shouldIgnorePeriodeOuverture) {
  	boolean isEpargneFaisable = true;
  	
  	// verification que la demande n'est pas faite
  	if (!isEpargneCetDemandeNonFaite()) {
  		isEpargneFaisable = false;
  	}
  	
  	// verification que la période est ouverte
  	if (!shouldIgnorePeriodeOuverture) {
    	if (isEpargneFaisable && !isPeriodeDemandeEpargneCet()) {
    		isEpargneFaisable = false;
    	}
  	}
  	
  	// verification du statut
  	if (isEpargneFaisable && !isStatutAutorise()) {
  		isEpargneFaisable = false;
  	}
  	
  	// verification s'il y a du reliquat disponible
  	if (isEpargneFaisable && 
  			((int) affAnn().reliquatInitialEnJour7h00()) == 0) {
  		isEpargneFaisable = false;
  	}
  	
  	// verification que le seuil minimum de reliquats est atteint
  	if (isEpargneFaisable &&
  			affAnn().reliquatInitialEnMinutes() < TimeCtrl.getMinutes(affAnn().seuilReliquatHeuresPourEpargneCet())) {
  		isEpargneFaisable = false;
  	}
  	
  	return isEpargneFaisable;
  }
  
  /**
   * Indique si l'agent peut décider parmi ses options
   * - la période de saisie est ouverte
   * - autorisé
   * - il possède au moins 20 jours de solde avec ou sans l'épargne
   * @return
   */
  public boolean isOptionFaisable(
  		int epargneEnMinutes,
  		boolean shouldIgnorePeriodeOuverture) {
  	boolean isOptionFaisable = true;

  	// verification que la période est ouverte
  	if (!shouldIgnorePeriodeOuverture) {
    	if (!isPeriodeDemandeEpargneCet()) {
    		isOptionFaisable = false;
    	}
  	}
  	
  	// verification du statut
  	if (isOptionFaisable && !isStatutAutorise()) {
  		isOptionFaisable = false;
  	}
  	
  	// verification s'il a de l'excedent sur les 20 jours à maintenir quoi qu'il en soit
  	if (isOptionFaisable && 
  			depassementSeuil20JoursPourEpargneCetEnMinutes(
  					epargneEnMinutes) / ConstsJour.DUREE_JOUR_7H00 == 0) {
  		isOptionFaisable = false;
  	}
  	
  	return isOptionFaisable;
  }
  
  
  
  // gestion de l'ancien régime
  
  /**
   * Indique si l'agent possède encore du CET sur l'ancien
   * régime du 31/12/2008
   */
  public boolean isCET31122008Restant() {
  	boolean isCET31122008Restant = true;
  	if (affAnn().individu().toCET() == null ||
				affAnn().individu().toCET().minutesRestantesAncienRegime() == 0) {
			isCET31122008Restant = false;
		}
		
  	return isCET31122008Restant;
  }

  /**
   * Indique si l'agent est autorisé à exercer son droit d'option sur l'ancien régime :
   * uniquement si l'agent n'a jamais exercé de droit d'option sur son ancien régime
   * 
   * @return
   */
  public boolean isDroitOptionAncienRegimeApresRenoncementPossible() {
  	boolean isDroitOptionAncienRegimeApresRenoncementPossible = true;
  	
  	isDroitOptionAncienRegimeApresRenoncementPossible = isJamaisStatueAncienRegime(true);
  	
  	return isDroitOptionAncienRegimeApresRenoncementPossible;
  }
  
  /**
   * Indique si l'agent peut faire une demande de passage de son CET en régime pérénne :
   * - demande de passage en CET pérenne non faite (peut être fait une seule et unique fois par agent)
   * - période ouverte
   * - possède un solde non vide en CET au 31/12/2008
   * 
   * @param shouldIgnorePeriodeOuverture 
   * 	indique s'il faut ignorer si la periode CET est ouverte
   */
  public boolean isDemandePassageRegimePerenneFaisable(boolean shouldIgnorePeriodeOuverture) {
  	boolean isDemandePassageRegimePerenneFaisable = true;
  	
  	// est-ce que l'agent ou la DRH déjà statué sur son ancien régime CET ?
   	if (!isJamaisStatueAncienRegime(false)) {
  		isDemandePassageRegimePerenneFaisable = false;
   	}
  	
  	// verification que la période est ouverte
   	if (!shouldIgnorePeriodeOuverture) {
    	if (isDemandePassageRegimePerenneFaisable && 
    			!isPeriodeDemandeEpargneCet()) {
    		isDemandePassageRegimePerenneFaisable = false;
    	}
   	}
  	
  	// vérifier s'il possède du solde de CET au 31/12/2008
  	if (isDemandePassageRegimePerenneFaisable &&
  			!isCET31122008Restant()) {
  		isDemandePassageRegimePerenneFaisable = false;
  	}
  	
  	return isDemandePassageRegimePerenneFaisable;
  }
  

  /**
   * Donne le solde du CET de l'agent dans le régime pérenne
   * @return
   */
  public int soldeEnMinutes() {
  	int soldeEnMinutes = 0;
		if (affAnn().individu().toCET() != null) {
			// solde régime pérenne
			soldeEnMinutes += affAnn().individu().toCET().minutesRestantesRegimePerenne(affAnn().dateDebutAnnee());
		}
		return soldeEnMinutes;
  }
  
  /**
   * {@link #soldeEnMinutes()} converties en jours à 7h00
   * @return
   */
  public float soldeEnJour7h00() {
  	float soldeEnJour7h00 = (float) soldeEnMinutes() / (float)ConstsJour.DUREE_JOUR_7H00;

  	return soldeEnJour7h00;
  }

  /**
   * Donne le solde du CET de l'agent dans l'ancien regime
   * @return
   */
  public int soldeAncienRegimeEnMinutes() {
  	int soldeAncienRegimeEnMinutes = 0;
		if (affAnn().individu().toCET() != null) {
			// solde régime pérenne
			soldeAncienRegimeEnMinutes += affAnn().individu().toCET().minutesRestantesAncienRegime();
		}
		return soldeAncienRegimeEnMinutes;
  }
  
  /**
   * {@link #soldeAncienRegimeEnMinutes()} converties en jours à 7h00
   * @return
   */
  public float soldeAncienRegimeEnJour7h00() {
  	float soldeAncienRegimeEnMinutes = (float) soldeAncienRegimeEnMinutes() / (float)ConstsJour.DUREE_JOUR_7H00;

  	return soldeAncienRegimeEnMinutes;
  }

	
  /**
   * Determiner si la personne connectée est autorisée a faire une demande
   * de transfert en RAFP
   */
  public boolean isPasAutoriseARafp() {
  	boolean isPasAutoriseARafp = true;
  	
  	// faut-il prendre en compte le statut
  	if (appVerifierStatutDemandeEpargneCet) {
  		isPasAutoriseARafp = affAnn.individu().isContractuel(affAnn);
  	} else {
  		isPasAutoriseARafp = false;
  	}
  	
  	return isPasAutoriseARafp;
  }
  
  /**
   * Donne le nombre de minutes de congés restantes après
   * l'épargne demandée.
   * TODO c'est pas très très propre la création puis 
   * suppression derriere ...
   * @param epargneEnMinutes
   * @return
   */
  public int congesRestantsApresEpargneEnMinutes(
  		int epargneEnMinutes, CngUserInfo userInfo) {
  	int minutesCongesRestantsApresEpargne = 0;
  	
  	// on va créer un faux enregistrement de demande d'épargne
  	EOMouvement mouvementEpargneFake = EOMouvement.newMouvement(affAnn(), EOMouvement.TYPE_CET_DEMANDE_EPARGNE, epargneEnMinutes);
  	Planning planning = Planning.newPlanning(affAnn(), userInfo, affAnn().dateDebutAnnee());
  	planning.setType("R");
  	
  	// on memorise la valeur de congés restants issus de ce calcul
  	minutesCongesRestantsApresEpargne = planning.congesGlobalRestantsEnMinutes();
  	
  	// et on supprime les faux enregistrements precedents
  	affAnn().removeFromTosMouvementRelationship(mouvementEpargneFake);
  	affAnn().editingContext().deleteObject(mouvementEpargneFake);
   	// sauvegarder la suppression ...
		try {
			UtilDb.save(affAnn().editingContext(), true);
		} catch (Throwable e) {
			e.printStackTrace();
		}
  	
  	
  	return minutesCongesRestantsApresEpargne;
  }
  
  /**
   * {@link #congesRestantsApresEpargneEnMinutes(int, CngUserInfo)} convertis en heures
   * @param epargneEnMinutes
   * @return
   */
  public String congesRestantsApresEpargneEnHeures(
  		int epargneEnMinutes, CngUserInfo userInfo) {
  	return TimeCtrl.stringForMinutes(congesRestantsApresEpargneEnMinutes(epargneEnMinutes, userInfo));
  }
  
  /**
   * {@link #congesRestantsApresEpargneEnMinutes(int, CngUserInfo)} convertis en jours à 7h00
   * @param epargneEnMinutes
   * @return
   */
  public float congesRestantsApresEpargneEnJour7h00(
  		int epargneEnMinutes, CngUserInfo userInfo) {
  	float congesRestantsApresEpargneEnJour7h00 = 
  		(float) congesRestantsApresEpargneEnMinutes(epargneEnMinutes, userInfo) / (float)ConstsJour.DUREE_JOUR_7H00;

  	return congesRestantsApresEpargneEnJour7h00;
  }
  

	
	// 

	/**
	 * le solde du CET de la demande après transfert, épargne et exercice du droit d'option
	 * @return
	 */
	public float soldeApresDemandeTransfertEpargneEtExerciceDroitDOptionEnMinutes() {
		float solde = (float) soldeEnMinutes();

		if (affAnn().toMouvementCetDemandeTransfertRegimePerenne() != null &&
				affAnn().toMouvementCetDemandeTransfertRegimePerenne().mouvementMinutes().intValue() > 0) {
			solde += affAnn().toMouvementCetDemandeTransfertRegimePerenne().mouvementMinutes().intValue();
		}
		
		if (affAnn().toMouvementCetDemandeEpargne() != null &&
				affAnn().toMouvementCetDemandeEpargne().mouvementMinutes().intValue() > 0) {
			solde += affAnn().toMouvementCetDemandeEpargne().mouvementMinutes().intValue();
		}
		
		if (affAnn().toMouvementCetDemandeIndemnisation() != null &&
				affAnn().toMouvementCetDemandeIndemnisation().mouvementMinutes().intValue() > 0) {
			solde -= affAnn().toMouvementCetDemandeIndemnisation().mouvementMinutes().intValue();
		}
		
		if (affAnn().toMouvementCetDemandeTransfertRafp() != null &&
				affAnn().toMouvementCetDemandeTransfertRafp().mouvementMinutes().intValue() > 0) {
			solde -= affAnn().toMouvementCetDemandeTransfertRafp().mouvementMinutes().intValue();
		}
		
		return solde;
	}

	/**
	 * le solde du CET de la decision après transfert, épargne et exercice du droit d'option
	 * @return
	 */
	public float soldeApresDecisionTransfertEpargneEtExerciceDroitDOptionEnMinutes() {
		float solde = (float) soldeEnMinutes();

		if (affAnn().toMouvementCetDecisionTransfertRegimePerenne() != null &&
				affAnn().toMouvementCetDecisionTransfertRegimePerenne().mouvementMinutes().intValue() > 0) {
			solde += affAnn().toMouvementCetDecisionTransfertRegimePerenne().mouvementMinutes().intValue();
		}
		
		if (affAnn().toMouvementCetDecisionEpargne() != null &&
				affAnn().toMouvementCetDecisionEpargne().mouvementMinutes().intValue() > 0) {
			solde += affAnn().toMouvementCetDecisionEpargne().mouvementMinutes().intValue();
		}
		
		if (affAnn().toMouvementCetDecisionIndemnisation() != null &&
				affAnn().toMouvementCetDecisionIndemnisation().mouvementMinutes().intValue() > 0) {
			solde -= affAnn().toMouvementCetDecisionIndemnisation().mouvementMinutes().intValue();
		}
		
		if (affAnn().toMouvementCetDecisionTransfertRafp() != null &&
				affAnn().toMouvementCetDecisionTransfertRafp().mouvementMinutes().intValue() > 0) {
			solde -= affAnn().toMouvementCetDecisionTransfertRafp().mouvementMinutes().intValue();
		}
		
		return solde;
	}
	
	
  
  // ecran de gestion des demandes CET : les états
  
  
  public final static int CET_ETAT_INCONNU 													= 0;
  public final static int CET_ETAT_DEMANDE_NON_TRAITEE 							= 1;
  public final static int CET_ETAT_DEMANDE_AUTRE_EPARGNE_SAISIE 		= 2;
  public final static int CET_ETAT_DEMANDE_ACCEPTEE_IDENTIQUE 			= 3;
  public final static int CET_ETAT_DEMANDE_ACCEPTEE_DIFFERENTE 			= 4;
 
  /**
   * Retourne l'etat de la demande CET 
   * @return
   */
  public int etatDemande() {
  	int etatDemande = CET_ETAT_INCONNU;

  	if (isAdminDemandeAccepteeALIdentique()) {
  		etatDemande = CET_ETAT_DEMANDE_ACCEPTEE_IDENTIQUE;
  	} else if (isAdminDemandeAccepteeDecisionDifferente()) {
  		etatDemande = CET_ETAT_DEMANDE_ACCEPTEE_DIFFERENTE;
  	} else if (isAdminDemandeAutreEpargneSaisie()) {
  		etatDemande = CET_ETAT_DEMANDE_AUTRE_EPARGNE_SAISIE;
  	} else if (isAdminDemandeNonTraitee()) {
  		etatDemande = CET_ETAT_DEMANDE_NON_TRAITEE;
  	} 
  	
  	return  etatDemande;
  }
  
  /**
   * Indique peut passer à l'état donné en parametre
   * @return
   */
  public boolean isTransitionEtatAutorisee(int etatSuivant) {
  	boolean isTransitionEtatAutorisee = false;

  	int etatDemande = etatDemande();
  	
  	if (etatSuivant == CET_ETAT_DEMANDE_ACCEPTEE_IDENTIQUE) {
  		isTransitionEtatAutorisee = (etatDemande == CET_ETAT_DEMANDE_NON_TRAITEE);
  	} else if (etatSuivant == CET_ETAT_DEMANDE_ACCEPTEE_DIFFERENTE) {
  		isTransitionEtatAutorisee = (etatDemande == CET_ETAT_DEMANDE_AUTRE_EPARGNE_SAISIE);
  	} else if (etatSuivant == CET_ETAT_DEMANDE_AUTRE_EPARGNE_SAISIE) {
  		isTransitionEtatAutorisee = (etatDemande == CET_ETAT_DEMANDE_NON_TRAITEE);
  	} else if (etatSuivant == CET_ETAT_DEMANDE_NON_TRAITEE) {
  		isTransitionEtatAutorisee = (etatDemande != CET_ETAT_DEMANDE_NON_TRAITEE && etatDemande != CET_ETAT_INCONNU);
  	} 
  	
  	return isTransitionEtatAutorisee;
  }
 
  /**
   * Indique si la demande et la decision CET sont exactement les
   * mêmes en terme de valeur
   * @return
   */
  public boolean isAdminDemandeAccepteeALIdentique() {
  	boolean isDemandeAccepteeALIdentique = true;
  	
  	// maintien ancien regime
  	if (!isIdentiqueValeurMouvement(
  			affAnn().toMouvementCetDemandeMaintienCetAncienRegime(),	
  			affAnn().toMouvementCetDecisionMaintienCetAncienRegime())) {
  		isDemandeAccepteeALIdentique = false;
  	}
 
   	// indemnisation ancien regime
  	if (isDemandeAccepteeALIdentique && !isIdentiqueValeurMouvement(
  			affAnn().toMouvementCetDemandeIndemnisationAncienRegime(),	
  			affAnn().toMouvementCetDecisionIndemnisationAncienRegime())) {
  		isDemandeAccepteeALIdentique = false;
  	}
  	 
   	// transfert RAFP ancien regime
  	if (isDemandeAccepteeALIdentique && !isIdentiqueValeurMouvement(
  			affAnn().toMouvementCetDemandeTransfertRafpAncienRegime(),	
  			affAnn().toMouvementCetDecisionTransfertRafpAncienRegime())) {
  		isDemandeAccepteeALIdentique = false;
  	}
  	 
   	// transfert vers régime pérenne
  	if (isDemandeAccepteeALIdentique && !isIdentiqueValeurMouvement(
  			affAnn().toMouvementCetDemandeTransfertRegimePerenne(),	
  			affAnn().toMouvementCetDecisionTransfertRegimePerenne())) {
  		isDemandeAccepteeALIdentique = false;
  	}
  	 
   	// épargne
  	if (isDemandeAccepteeALIdentique && !isIdentiqueValeurMouvement(
  			affAnn().toMouvementCetDemandeEpargne(),	
  			affAnn().toMouvementCetDecisionEpargne())) {
  		isDemandeAccepteeALIdentique = false;
  	}
  	 
   	// maintien
  	if (isDemandeAccepteeALIdentique && !isIdentiqueValeurMouvement(
  			affAnn().toMouvementCetDemandeMaintienCet(),	
  			affAnn().toMouvementCetDecisionMaintienCet())) {
  		isDemandeAccepteeALIdentique = false;
  	}
  	 
   	// maintien forécé
  	if (isDemandeAccepteeALIdentique && !isIdentiqueValeurMouvement(
  			affAnn().toMouvementCetDemandeMaintienCetForce(),	
  			affAnn().toMouvementCetDecisionMaintienCetForce())) {
  		isDemandeAccepteeALIdentique = false;
  	}

  	// indemnisation
  	if (isDemandeAccepteeALIdentique && !isIdentiqueValeurMouvement(
  			affAnn().toMouvementCetDemandeIndemnisation(),	
  			affAnn().toMouvementCetDecisionIndemnisation())) {
  	  isDemandeAccepteeALIdentique = false;
  	}
 	 
   	// transfert RAFP
  	if (isDemandeAccepteeALIdentique && !isIdentiqueValeurMouvement(
   			affAnn().toMouvementCetDemandeTransfertRafp(),	
   			affAnn().toMouvementCetDecisionTransfertRafp())) {
  		isDemandeAccepteeALIdentique = false;
  	}
  	
  	return isDemandeAccepteeALIdentique;
  }
  
  /**
   * Prise de décision sur l'acceptation ou non à l'identique de
   * la demande. On fait la sauvegarde immédiate car cette méthode
   * est appelée par directement par l'interface {@link AdminCETListeDemande}
	 *
   * @param value
   * @throws Throwable 
   */
  public void setIsAdminDemandeAccepteeALIdentique(boolean isAcceptee) throws Throwable {
  	
  	if (isAcceptee) {
  		doAccepterIdentique();
  	} else {
  		doSupprimeDecision();
  	}
  	
  	UtilDb.save(affAnn().editingContext(), true);
  	

  	setterSilencer = true;

  }
  
  // bidouille pour que le setter setIsAdminDemandeAccepteeDecisionDifferente() ne 
  // soit pas appélé quand setIsAdminDemandeAccepteeALIdentique() l'est (pour éviter
  // d'avoir acceptation puis refus derriere immédiatement ... c'est une limite lié
  // a l'interface web "non ajax"
  private boolean setterSilencer;
  
  /**
   * Prise de décision sur l'acceptation différente de
   * la demande. On fait la sauvegarde immédiate car cette méthode
   * est appelée par directement par l'interface {@link AdminCETListeDemande}
   * A noter que la seule valeur possible de part la construction
   * de l'interface est <code>false</code>
	 *
   * @param value
   * @throws Throwable 
   */
  public void setIsAdminDemandeAccepteeDecisionDifferente(boolean isAcceptee) throws Throwable {

  	if (setterSilencer) {
  		setterSilencer = false;
  		return;
  	}
  		
  	
  	if (isAcceptee) {

  		// ce cas n'arrive normalement pas
  
  	} else {
  		doSupprimeDecisionDroitOption();
  		
    	UtilDb.save(affAnn().editingContext(), true);
  	}
  }
  
  
  /**
   * Indique la decision CET est faite mais pas à 
   * l'identique de la demande
   * @return
   */
  public boolean isAdminDemandeAccepteeDecisionDifferente() {
  	boolean isDemandeAccepteeDecisionDifferente = true;
  	
  	// maintien ancien regime
  	if (!isIdentiqueValeurMouvement(
  			affAnn().toMouvementCetDemandeMaintienCetAncienRegime(),	
  			affAnn().toMouvementCetDecisionMaintienCetAncienRegime())) {
  		isDemandeAccepteeDecisionDifferente = false;
  	}
 
   	// indemnisation ancien regime
  	if (isDemandeAccepteeDecisionDifferente && !isIdentiqueValeurMouvement(
  			affAnn().toMouvementCetDemandeIndemnisationAncienRegime(),	
  			affAnn().toMouvementCetDecisionIndemnisationAncienRegime())) {
  		isDemandeAccepteeDecisionDifferente = false;
  	}
  	 
   	// transfert RAFP ancien regime
  	if (isDemandeAccepteeDecisionDifferente && !isIdentiqueValeurMouvement(
  			affAnn().toMouvementCetDemandeTransfertRafpAncienRegime(),	
  			affAnn().toMouvementCetDecisionTransfertRafpAncienRegime())) {
  		isDemandeAccepteeDecisionDifferente = false;
  	}
  	 
   	// transfert vers régime pérenne
  	if (isDemandeAccepteeDecisionDifferente && !isIdentiqueValeurMouvement(
  			affAnn().toMouvementCetDemandeTransfertRegimePerenne(),	
  			affAnn().toMouvementCetDecisionTransfertRegimePerenne())) {
  		isDemandeAccepteeDecisionDifferente = false;
  	}
  	 
   	// épargne
  	if (isDemandeAccepteeDecisionDifferente && (
  			affAnn().toMouvementCetDemandeEpargne() == null ||
  			affAnn().toMouvementCetDecisionEpargne() == null)) {
  		isDemandeAccepteeDecisionDifferente = false;
  	}
  	 
   	// maintien
  	if (isDemandeAccepteeDecisionDifferente && (
  			affAnn().toMouvementCetDemandeMaintienCet() == null ||
  			affAnn().toMouvementCetDecisionMaintienCet() == null)) {
  		isDemandeAccepteeDecisionDifferente = false;
  	}
  	 
   	// maintien forcé
  	if (isDemandeAccepteeDecisionDifferente && (
  			affAnn().toMouvementCetDemandeMaintienCetForce() == null ||
  			affAnn().toMouvementCetDecisionMaintienCetForce() == null)) {
  		isDemandeAccepteeDecisionDifferente = false;
  	}

  	// indemnisation
  	if (isDemandeAccepteeDecisionDifferente && (
  			affAnn().toMouvementCetDemandeIndemnisation() == null ||
  			affAnn().toMouvementCetDecisionIndemnisation() == null)) {
  	  isDemandeAccepteeDecisionDifferente = false;
  	}
 	 
   	// transfert RAFP
  	if (isDemandeAccepteeDecisionDifferente && (
   			affAnn().toMouvementCetDemandeTransfertRafp() == null ||
   			affAnn().toMouvementCetDecisionTransfertRafp() == null)) {
  		isDemandeAccepteeDecisionDifferente = false;
  	}
  	
  	// test de la différence des valeurs
  	if (isDemandeAccepteeDecisionDifferente && (
  			isIdentiqueValeurMouvement(affAnn().toMouvementCetDemandeEpargne(), affAnn().toMouvementCetDecisionEpargne()) &&
  			isIdentiqueValeurMouvement(affAnn().toMouvementCetDemandeMaintienCet(), affAnn().toMouvementCetDecisionMaintienCet()) &&
  			isIdentiqueValeurMouvement(affAnn().toMouvementCetDemandeMaintienCetForce(), affAnn().toMouvementCetDecisionMaintienCetForce()) &&
  			isIdentiqueValeurMouvement(affAnn().toMouvementCetDemandeIndemnisation(), affAnn().toMouvementCetDecisionIndemnisation()) &&
  			isIdentiqueValeurMouvement(affAnn().toMouvementCetDemandeTransfertRafp(), affAnn().toMouvementCetDecisionTransfertRafp()))) {
  		isDemandeAccepteeDecisionDifferente = false;
  	}
  			
  	
  	return isDemandeAccepteeDecisionDifferente;
  }
  
  /**
   * Indique si {@link #isAdminDemandeAccepteeALIdentique()} ou 
   * {@link #isAdminDemandeAccepteeDecisionDifferente()}
   * @see PageCET
   * @return
   */
  public boolean isAdminDemandeAcceptee() {
  	return isAdminDemandeAccepteeALIdentique() || isAdminDemandeAccepteeDecisionDifferente();
  }
  
  /**
   * Indique s'il faut retourner a l'agent la demande, pour qu'il 
   * accepte la décision qui a été différente (épargne modifiée,
   * et droit d'option aussi)
   * @return
   */
  public boolean isAdminDemandeAutreEpargneSaisie() {
  	boolean isAdminDemandeAutreEpargneSaisie = false;
  	
  	// maintien ancien regime
  	if (isIdentiqueValeurMouvement(
  			affAnn().toMouvementCetDemandeMaintienCetAncienRegime(),	
  			affAnn().toMouvementCetDecisionMaintienCetAncienRegime())) {
  		isAdminDemandeAutreEpargneSaisie = true;
  	}
 
   	// indemnisation ancien regime
  	if (isAdminDemandeAutreEpargneSaisie && !isIdentiqueValeurMouvement(
  			affAnn().toMouvementCetDemandeIndemnisationAncienRegime(),	
  			affAnn().toMouvementCetDecisionIndemnisationAncienRegime())) {
  		isAdminDemandeAutreEpargneSaisie = true;
  	}
  	 
   	// transfert RAFP ancien regime
  	if (isAdminDemandeAutreEpargneSaisie && !isIdentiqueValeurMouvement(
  			affAnn().toMouvementCetDemandeTransfertRafpAncienRegime(),	
  			affAnn().toMouvementCetDecisionTransfertRafpAncienRegime())) {
  		isAdminDemandeAutreEpargneSaisie = true;
  	}
  	 
   	// transfert vers régime pérenne
  	if (isAdminDemandeAutreEpargneSaisie && !isIdentiqueValeurMouvement(
  			affAnn().toMouvementCetDemandeTransfertRegimePerenne(),	
  			affAnn().toMouvementCetDecisionTransfertRegimePerenne())) {
  		isAdminDemandeAutreEpargneSaisie = true;
  	}
  	 
  	// demande épargne + maintien + maintien forcé + indemnisation + rafp doivent exister
  	if (isAdminDemandeAutreEpargneSaisie && (
  			affAnn().toMouvementCetDemandeEpargne() == null ||
  			affAnn().toMouvementCetDemandeMaintienCet() == null ||
  			affAnn().toMouvementCetDemandeMaintienCetForce() == null ||
  			affAnn().toMouvementCetDemandeIndemnisation() == null ||
  			affAnn().toMouvementCetDemandeEpargne() == null)) {
  		isAdminDemandeAutreEpargneSaisie = false;
  	}

  	// décision épargne doit exister
  	if (isAdminDemandeAutreEpargneSaisie &&
  			affAnn().toMouvementCetDecisionEpargne() == null) {
  		isAdminDemandeAutreEpargneSaisie = false;
  	}

  	// décisions maintien + maintien forcé + indemnisation + rafp ne doivent pas exister
   	if (isAdminDemandeAutreEpargneSaisie && (
  			affAnn().toMouvementCetDecisionMaintienCet() != null ||
  			affAnn().toMouvementCetDecisionMaintienCetForce() != null ||
  			affAnn().toMouvementCetDecisionIndemnisation() != null ||
  			affAnn().toMouvementCetDecisionTransfertRafp() != null)) {
  		isAdminDemandeAutreEpargneSaisie = false;
  	}

  	return isAdminDemandeAutreEpargneSaisie;
  }
 
  /**
   * Indique si on attend le retour de l'agent quant à la 
   * proposition différente d'épargne.
   * @return
   */
  public boolean isAdminDemandeNonTraitee() {
  	boolean isNonTraitee = true;
  	
   	// demande épargne + maintien + maintien forcé + indemnisation + rafp doivent exister
  	if (affAnn().toMouvementCetDemandeEpargne() == null ||
  			affAnn().toMouvementCetDemandeMaintienCet() == null ||
  			affAnn().toMouvementCetDemandeMaintienCetForce() == null ||
  			affAnn().toMouvementCetDemandeIndemnisation() == null ||
  			affAnn().toMouvementCetDemandeEpargne() == null) {
  		isNonTraitee = false;
  	}
  	
  	// décisions épargne + maintien + maintien forcé + indemnisation + rafp ne doivent pas exister
   	if (isNonTraitee && (
   			affAnn().toMouvementCetDecisionEpargne() != null ||
   			affAnn().toMouvementCetDecisionMaintienCet() != null ||
  			affAnn().toMouvementCetDecisionMaintienCetForce() != null ||
  			affAnn().toMouvementCetDecisionIndemnisation() != null ||
  			affAnn().toMouvementCetDecisionTransfertRafp() != null)) {
   		isNonTraitee = false;
  	}

  	return isNonTraitee;
  }
  
  
  /**
   * Tester si deux mouvements sont identiques en valeurs
   * @param mouvement1
   * @param mouvement2
   */
  private boolean isIdentiqueValeurMouvement(EOMouvement mouvement1, EOMouvement mouvement2) {
  	boolean isIdentiqueValeurMouvement = true;

  	if (mouvement1 == null && mouvement2 == null) {
  		return true;
  	}
  	
  	if ((mouvement1 == null && mouvement2 != null) ||
  			(mouvement1 != null && mouvement2 == null)) {
  		isIdentiqueValeurMouvement = false;
  	}
  	
  	if (isIdentiqueValeurMouvement) {
  		if (mouvement1.mouvementMinutes().intValue() != mouvement2.mouvementMinutes().intValue()) {
  			isIdentiqueValeurMouvement = false;
  		}
  	}
  	
  	return isIdentiqueValeurMouvement;
  }
  
  
  // les decisions
  
  /**
   * Enregistre une décision sur l'ancien régime CET
   * d'un agent. Cette décision doit mentionner :
   * - le nombre de minutes à transferer en régime pérenne
   * - à défaut : le contenu de l'exercice du droit d'option
   * 
   * Dans tous les cas, les 4 enregistrement mouvement de types :
   * - {@link EOMouvement#TYPE_CET_DECISION_TRANSFERT_REGIME_PERENNE}
   * - {@link EOMouvement#TYPE_CET_DECISION_INDEMNISATION_ANCIEN_REGIME}
   * - {@link EOMouvement#TYPE_CET_DECISION_TRANSFERT_RAFP_ANCIEN_REGIME}
   * - {@link EOMouvement#TYPE_CET_DECISION_MAINTIEN_CET_ANCIEN_REGIME}
   * doivent être créés.
   */
  private void doDecisionAncienRegime(
  		int minutesTransfertRegimePerenne,
  		int minutesIndemnisation,
  		int minutesTransfertRafp,
  		int minutesMaintienCet) {
		EOMouvement.newMouvement(
				affAnn(), EOMouvement.TYPE_CET_DECISION_TRANSFERT_REGIME_PERENNE,	minutesTransfertRegimePerenne);
		EOMouvement.newMouvement(
				affAnn(), EOMouvement.TYPE_CET_DECISION_INDEMNISATION_ANCIEN_REGIME,	minutesIndemnisation);
		EOMouvement.newMouvement(
				affAnn(), EOMouvement.TYPE_CET_DECISION_TRANSFERT_RAFP_ANCIEN_REGIME,	minutesTransfertRafp);
		EOMouvement.newMouvement(
				affAnn(), EOMouvement.TYPE_CET_DECISION_MAINTIEN_CET_ANCIEN_REGIME,		minutesMaintienCet);
  }
  

  /**
   * Enregistre une décision sur le régime pérénne d'un agent.
   * Cette décision doit mentionner :
   * - la valeur de l'épargne (0 si pas d'épargne)
   * - le contenu de l'exercice du droit d'option (dont 20j doivent
   *  être placés en maintien)
   *  
   *  Si l'un des paramètres est <code>null</code> alors, on ne fait
   *  pas la création dans la rubrique associée
   */
  private void doDecisionRegimePerenne(
  		Integer minutesEpargne,
  		Integer minutesMaintienCet,
  		Integer minutesMaintienCetForce,
  		Integer minutesIndemnisation,
  		Integer minutesTransfertRafp
  ) {
  	// memoriser le montant de l'épargne totale
  	if (minutesEpargne != null) {
  		EOMouvement.newMouvement(
  				affAnn(), EOMouvement.TYPE_CET_DECISION_EPARGNE,	minutesEpargne.intValue());
  	}
		// la valeur en maintien CET
  	if (minutesMaintienCet != null) {
  		EOMouvement.newMouvement(
  				affAnn(), EOMouvement.TYPE_CET_DECISION_MAINTIEN_CET, minutesMaintienCet.intValue());
  	}
		// la valeur en maintien obligatoire CET
  	if (minutesMaintienCetForce != null) {
  		EOMouvement.newMouvement(
  				affAnn(), EOMouvement.TYPE_CET_DECISION_MAINTIEN_CET_FORCE, minutesMaintienCetForce.intValue());
  	}
		// la valeur en indemnisation
		if (minutesIndemnisation != null) {
			EOMouvement.newMouvement(
					affAnn(), EOMouvement.TYPE_CET_DECISION_INDEMNISATION,	minutesIndemnisation.intValue());
  	}
		// la valeur en transfert RAFP
  	if (minutesTransfertRafp != null) {
  		EOMouvement.newMouvement(
  				affAnn(), EOMouvement.TYPE_CET_DECISION_TRANSFERT_RAFP, minutesTransfertRafp.intValue());
  	}
  }
 
  /**
   * Création d'un enregistrement {@link EOCETTransaction} lors
   * de la décision. Cet enregistrement ne doit exister que si
   * les décisions contiennent au moins un crédit (transfert ou épargne)
   */
  private void doCreationTransactionSiCredit() {

  	if ((affAnn().toMouvementCetDecisionTransfertRegimePerenne() != null &&
  			affAnn().toMouvementCetDecisionTransfertRegimePerenne().mouvementMinutes().intValue() > 0) ||
  			(affAnn().toMouvementCetDecisionEpargne() != null &&
  					affAnn().toMouvementCetDecisionEpargne().mouvementMinutes().intValue() > 0)) {
  		
  		EOCETTransaction transaction = EOCETTransaction.createCETTransaction(
  				affAnn().editingContext(), affAnn().individu().toCET());
  		transaction.setToAffectationAnnuelleRelationship(affAnn());
  	}
  	
  }
  
  /**
   * Prise de la décision sur l'ancien régime : 
   * - indemnisation
   * - RAFP
   * - maintien
   * - transfert CET pérenne
   * 
   * Tous ces éléments sont identiques à la demande
   */
  private void doDecisionAncienRegimeIdentiqueALaDemande() {
  	if (affAnn().toMouvementCetDemandeTransfertRegimePerenne() != null) {
    	doDecisionAncienRegime(
    			affAnn().toMouvementCetDemandeTransfertRegimePerenne().mouvementMinutes().intValue(),
    			affAnn().toMouvementCetDemandeIndemnisationAncienRegime().mouvementMinutes().intValue(),
    			affAnn().toMouvementCetDemandeTransfertRafpAncienRegime().mouvementMinutes().intValue(),
    			affAnn().toMouvementCetDemandeMaintienCetAncienRegime().mouvementMinutes().intValue());
  	}
  }
  
  /**
   * Acceptation de la demande à l'identique
   * @return
   */
  public void doAccepterIdentique() {
  	
  	EOCET.doCreationCetSiInexistant(affAnn().individu());
  	
  	doDecisionAncienRegimeIdentiqueALaDemande();

  	doDecisionRegimePerenne(
  			affAnn().toMouvementCetDemandeEpargne().mouvementMinutes().intValue(),
  			affAnn().toMouvementCetDemandeMaintienCet().mouvementMinutes().intValue(),
  			affAnn().toMouvementCetDemandeMaintienCetForce().mouvementMinutes().intValue(),
  			affAnn().toMouvementCetDemandeIndemnisation().mouvementMinutes().intValue(),
  			affAnn().toMouvementCetDemandeTransfertRafp().mouvementMinutes().intValue());

  	// on ne crée une transaction que s'il y a du crédit
  	doCreationTransactionSiCredit();
  }
  
  /**
   * Passage de l'état de demande vers l'état en attente
   * de saisie d'une autre épargne par le gestionnaire.
   * 
   * On enregistrer un {@link EOMouvement} de type décision
   * d'épargne.
   * 
   * @return <code>true</code> si pas d'erreur de validation.
   * @see #getErrorMessage() pour avoir le message d'erreur
   */
  public boolean doSaisieDecisionAutreEpargne(
  		int minutesDecisionEpargne) {

  	boolean isDecisionOk = true;
  	
  	resetError();

  	// pas d'épargne négative
  	if (minutesDecisionEpargne < 0) {
  		errorMessage = "L'épargne ne peut pas être négative";
  		isDecisionOk = false;
  	}
  	
  	// pas supérieure aux reliquats
  	if (isDecisionOk &&
  			minutesDecisionEpargne > (int) affAnn().reliquatInitialEnMinutes()) {
  		errorMessage = "L'épargne (" + minutesDecisionEpargne / ConstsJour.DUREE_JOUR_7H00 +"j.) " +
  			"ne peut pas être supérieure aux reliquats de congés (" + 
  			affAnn().reliquatInitialEnMinutes() / ConstsJour.DUREE_JOUR_7H00 + "j.)";
  		isDecisionOk = false;
  	}
  	
  	// pas supérieure au plafond
  	if (isDecisionOk &&
  			minutesDecisionEpargne > reliquatPourBlocageCetMaxEnMinutes()) {
  		errorMessage = "L'épargne  (" + minutesDecisionEpargne / ConstsJour.DUREE_JOUR_7H00 +" j.) " +
  			"ne peut pas être supérieure au plafond maximum (" + 
  			reliquatPourBlocageCetMaxEnMinutes() / ConstsJour.DUREE_JOUR_7H00 + "j.)";
  		isDecisionOk = false;
  	}
  	
  	if (isDecisionOk) {
  		
    	EOCET.doCreationCetSiInexistant(affAnn().individu());
    	
    	doDecisionAncienRegimeIdentiqueALaDemande();
    	
    	doDecisionRegimePerenne(
    			new Integer(minutesDecisionEpargne),
    			null,
    			null,
    			null,
    			null);
  	} 
  	
  	return isDecisionOk;
  }
  
  /**
   * Passage de l'état de épargne saisie vers droit
   * d'option saisi par le gestionnaire.
   * 
   * On enregistrer 4 {@link EOMouvement}
   * 
   * @return <code>true</code> si pas d'erreur de validation.
   * @see #getErrorMessage() pour avoir le message d'erreur
   */
  public boolean doSaisieDecisionAutreDroitOption(
  		int minutesMaintienCet,
  		int minutesIndemnisation,
  		int minutesTransfertRafp) {
  	
  	boolean isDecisionOk = true;
  	
  	resetError();

  	// toutes les valeurs doivent être positives
  	if (minutesMaintienCet < 0 ||
  			minutesIndemnisation < 0 ||
  			minutesTransfertRafp < 0) {
  		errorMessage = "Le droit d'option ne doit pas contenir de valeurs négatives";
  		isDecisionOk = false;
  	}
  	  	
  	// la somme des options doit être égale au dépassement de 20 jours
  	int alimentationEnMinutes = 0;
  	if (isDecisionOk) {
    
  		if (affAnn().toMouvementCetDecisionTransfertRegimePerenne() != null &&
    			affAnn().toMouvementCetDecisionTransfertRegimePerenne().mouvementMinutes().intValue() > 0) {
    		alimentationEnMinutes += affAnn().toMouvementCetDecisionTransfertRegimePerenne().mouvementMinutes().intValue();
    	}
    	if (affAnn().toMouvementCetDecisionEpargne() != null &&
    			affAnn().toMouvementCetDecisionEpargne().mouvementMinutes().intValue() > 0) {
    		alimentationEnMinutes += affAnn().toMouvementCetDecisionEpargne().mouvementMinutes().intValue();
    	}

    	int depassementEnJours7h00Arrondi = depassementSeuil20JoursPourEpargneCetEnJours7h00Arrondi(alimentationEnMinutes);
    	int sommeEnJours7h00 = (minutesMaintienCet + minutesIndemnisation + minutesTransfertRafp) / ConstsJour.DUREE_JOUR_7H00;
    	
    	if (sommeEnJours7h00 != depassementEnJours7h00Arrondi) {
    		errorMessage = "La somme des options n'est pas égale au total soumis au droit d'option : \\n" + 
  			"- maintien : " + minutesMaintienCet / ConstsJour.DUREE_JOUR_7H00 + "\\n" + 
  			"- indemnisation : " + minutesIndemnisation / ConstsJour.DUREE_JOUR_7H00 + "\\n" + 
  			"- transfert RAFP : " + minutesTransfertRafp / ConstsJour.DUREE_JOUR_7H00 + "\\n" + 
  			"- somme : " + sommeEnJours7h00 + "\\n" + 
  			"- total attendu : " + depassementEnJours7h00Arrondi;
    		isDecisionOk = false;
    	}
    
  	}
  	
  	// le maintien ne doit excéder un certain seuil
  	if (isDecisionOk &&
  			minutesMaintienCet > maintienCetMaximumAuDela20JEnMinutes(alimentationEnMinutes)) {
  		errorMessage = "Le maintien en CET dépasse le maximum autorisé pour cette épargne";
  		isDecisionOk = false;
  	}
  	
  	// calcul du maintienFORCE
  	
  	int minutesMaintienCetForce = new Integer(
  			affAnn().cetFactory().maintienObligatoireCetPourDemandeEpargneCetEnMinutes(
  					alimentationEnMinutes) / ConstsJour.DUREE_JOUR_7H00);
  	
  	
  	if (isDecisionOk) {

    	doDecisionRegimePerenne(
    			null,
    			new Integer(minutesMaintienCet),
    			new Integer(minutesMaintienCetForce),
    			new Integer(minutesIndemnisation),
    			new Integer(minutesTransfertRafp));

  	}

  	
  	return isDecisionOk;
  }
  
  
  // demandes
  
  
  /**
   * Enregistre une demande sur l'ancien régime CET
   * d'un agent. Cette demande doit mentionner :
   * - le nombre de minutes à transferer en régime pérenne
   * - à défaut : le contenu de l'exercice du droit d'option
   * 
   * Dans tous les cas, les 4 enregistrement mouvement de types :
   * - {@link EOMouvement#TYPE_CET_DEMANDE_TRANSFERT_REGIME_PERENNE}
   * - {@link EOMouvement#TYPE_CET_DEMANDE_INDEMNISATION_ANCIEN_REGIME}
   * - {@link EOMouvement#TYPE_CET_DEMANDE_TRANSFERT_RAFP_ANCIEN_REGIME}
   * - {@link EOMouvement#TYPE_CET_DEMANDE_MAINTIEN_CET_ANCIEN_REGIME}
   * doivent être créés.
   */
  public void doDemandeAncienRegime(
  		int minutesTransfertRegimePerenne,
  		int minutesIndemnisation,
  		int minutesTransfertRafp,
  		int minutesMaintienCet) {
		EOMouvement.newMouvement(
				affAnn(), EOMouvement.TYPE_CET_DEMANDE_TRANSFERT_REGIME_PERENNE,	minutesTransfertRegimePerenne);
		EOMouvement.newMouvement(
				affAnn(), EOMouvement.TYPE_CET_DEMANDE_INDEMNISATION_ANCIEN_REGIME,	minutesIndemnisation);
		EOMouvement.newMouvement(
				affAnn(), EOMouvement.TYPE_CET_DEMANDE_TRANSFERT_RAFP_ANCIEN_REGIME,	minutesTransfertRafp);
		EOMouvement.newMouvement(
				affAnn(), EOMouvement.TYPE_CET_DEMANDE_MAINTIEN_CET_ANCIEN_REGIME,		minutesMaintienCet);
  }

  /**
   * Enregistre une demande sur le régime pérénne d'un agent.
   * Cette demande doit mentionner :
   * - la valeur de l'épargne (0 si pas d'épargne)
   * - le contenu de l'exercice du droit d'option (dont 20j doivent
   *  être placés en maintien)
   *  
   * Dans tous les cas, les 5 enregistrement mouvement de types :
   * - {@link EOMouvement#TYPE_CET_DEMANDE_EPARGNE}
   * - {@link EOMouvement#TYPE_CET_DEMANDE_MAINTIEN_CET}
   * - {@link EOMouvement#TYPE_CET_DEMANDE_MAINTIEN_CET_FORCE}
   * - {@link EOMouvement#TYPE_CET_DEMANDE_INDEMNISATION}
   * - {@link EOMouvement#TYPE_CET_DEMANDE_TRANSFERT_RAFP}
   * doivent être créés.
   */
  public void doDemandeRegimePerenne(
  		int minutesEpargne,
  		int minutesMaintienCet,
  		int minutesMaintienCetForce,
  		int minutesIndemnisation,
  		int minutesTransfertRafp
  ) {
  	// memoriser le montant de l'épargne totale
		EOMouvement.newMouvement(
				affAnn(), EOMouvement.TYPE_CET_DEMANDE_EPARGNE,	minutesEpargne);
		// la valeur en maintien CET
		EOMouvement.newMouvement(
				affAnn(), EOMouvement.TYPE_CET_DEMANDE_MAINTIEN_CET, minutesMaintienCet);
		// la valeur en maintien obligatoire CET
		EOMouvement.newMouvement(
				affAnn(), EOMouvement.TYPE_CET_DEMANDE_MAINTIEN_CET_FORCE, minutesMaintienCetForce);
		// la valeur en indemnisation
		EOMouvement.newMouvement(
				affAnn(), EOMouvement.TYPE_CET_DEMANDE_INDEMNISATION,	minutesIndemnisation);
		// la valeur en transfert RAFP
		EOMouvement.newMouvement(
				affAnn(), EOMouvement.TYPE_CET_DEMANDE_TRANSFERT_RAFP, minutesTransfertRafp);
  }

  
  
  // raz

  
  /**
   * Effectue la suppression de l'ensemble des demandes
   * CET liées au planning. Supprime tous les enregistrements
   * de type mouvement CET.
   */
  public void doSupprimeDemande() {
  	EOEditingContext ec = affAnn().editingContext();
  	
  	if (affAnn().toMouvementCetDemandeTransfertRegimePerenne() != null) {
    	ec.deleteObject(affAnn().toMouvementCetDemandeTransfertRegimePerenne());
    	ec.deleteObject(affAnn().toMouvementCetDemandeIndemnisationAncienRegime());
    	ec.deleteObject(affAnn().toMouvementCetDemandeTransfertRafpAncienRegime());
    	ec.deleteObject(affAnn().toMouvementCetDemandeMaintienCetAncienRegime());
  	}
  	
  	// supprimer la demande d'epargne
  	ec.deleteObject(affAnn().toMouvementCetDemandeEpargne());
  	// supprimer la demande de maintien en cet
  	ec.deleteObject(affAnn().toMouvementCetDemandeMaintienCet());
  	// supprimer la demande de maintien en cet (forcé)
  	ec.deleteObject(affAnn().toMouvementCetDemandeMaintienCetForce());
  	// supprimer la demande d'indemnisation
  	ec.deleteObject(affAnn().toMouvementCetDemandeIndemnisation());
  	// supprimer la demande de transfert RAFP
  	ec.deleteObject(affAnn().toMouvementCetDemandeTransfertRafp());

  }
  
  /**
   * Effectue la suppression de l'ensemble des décisions
   * CET liées au planning. Supprime tous les enregistrements
   * de type mouvement CET.
   * @throws Throwable 
   */
  public void doSupprimeDecision() {
  	EOEditingContext ec = affAnn().editingContext();
  	
  	if (affAnn().toMouvementCetDecisionTransfertRegimePerenne() != null) {
    	ec.deleteObject(affAnn().toMouvementCetDecisionTransfertRegimePerenne());
    	ec.deleteObject(affAnn().toMouvementCetDecisionIndemnisationAncienRegime());
    	ec.deleteObject(affAnn().toMouvementCetDecisionTransfertRafpAncienRegime());
    	ec.deleteObject(affAnn().toMouvementCetDecisionMaintienCetAncienRegime());
  	}
  	
  	// supprimer la décision d'epargne
  	if (affAnn().toMouvementCetDecisionEpargne() != null) {
  		ec.deleteObject(affAnn().toMouvementCetDecisionEpargne());
  	}
  	
  	// supprimer les décisions du droit d'option
  	doSupprimeDecisionDroitOption();
  	
  	// supprimer la transaction de crédit si nécéssaire
  	EOCETTransaction transaction = EOCETTransaction.fetchCETTransaction(
  			affAnn().editingContext(), EOCETTransaction.TO_AFFECTATION_ANNUELLE_KEY, affAnn());
  	if (transaction != null) {
  		EOCETTransaction.delete(transaction);
  	}
  }
  
  /**
   * Effectue la suppression des décisions liées au 
   * droit d'option
   * @throws Throwable 
   */
  public void doSupprimeDecisionDroitOption() {
  	EOEditingContext ec = affAnn().editingContext();
  
  	// supprimer la décision de maintien en cet
  	if (affAnn().toMouvementCetDecisionMaintienCet() != null) {
  		ec.deleteObject(affAnn().toMouvementCetDecisionMaintienCet());
  	}
  	// supprimer la décision de maintien en cet (forcé)
  	if (affAnn().toMouvementCetDecisionMaintienCetForce() != null) {
  		ec.deleteObject(affAnn().toMouvementCetDecisionMaintienCetForce());
  	}
  	// supprimer la décision d'indemnisation
  	if (affAnn().toMouvementCetDecisionIndemnisation() != null) {
  		ec.deleteObject(affAnn().toMouvementCetDecisionIndemnisation());
  	}
  	// supprimer la décision de transfert RAFP
  	if (affAnn().toMouvementCetDecisionTransfertRafp() != null) {
  		ec.deleteObject(affAnn().toMouvementCetDecisionTransfertRafp());
  	}
  	
  }
  
  //

	public static final boolean isAppVerifierStatutDemandeEpargneCet() {
		return appVerifierStatutDemandeEpargneCet;
	}
  
}
