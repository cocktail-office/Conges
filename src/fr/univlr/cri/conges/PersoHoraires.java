package fr.univlr.cri.conges;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eocontrol.EONotQualifier;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.YCRIWebPage;
import fr.univlr.cri.conges.constantes.ConstsHoraire;
import fr.univlr.cri.conges.databus.CngHoraireBus;
import fr.univlr.cri.conges.eos.modele.grhum.EOAffectation;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOHoraire;
import fr.univlr.cri.conges.eos.modele.planning.EOPeriodeAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOPlanningHebdomadaire;
import fr.univlr.cri.conges.eos.modele.planning.EOTypeHoraire;
import fr.univlr.cri.conges.eos.modele.planning.I_ClasseMetierNotificationParametre;
import fr.univlr.cri.conges.objects.HoraireHebdomadaire;
import fr.univlr.cri.conges.objects.HoraireJournalier;
import fr.univlr.cri.conges.objects.Parametre;
import fr.univlr.cri.conges.objects.Planning;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.ArrayCtrl;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRSort;
import fr.univlr.cri.ycrifwk.utils.UtilDb;

/**
 * Ecran de gestion des horaires hebdomadaires individuels
 * 
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */
public class PersoHoraires 
	extends YCRIWebPage 
		implements I_ClasseMetierNotificationParametre {

  private Planning lePlanning;
    
  public boolean isNouvelHoraire;
  private boolean isModifHoraire;
  public HoraireHebdomadaire unHoraireHebdomadaire,unHoraireHebdomadaireSelectionne;
  public NSArray lesHorairesHebdomadaires;
  public NSArray lesTypesHoraires;
  public EOTypeHoraire unTypeHoraire,unTypeHoraireSelectionne;
  public Number uneQuotite, uneQuotiteSelectionnee;
  public HoraireJournalier unHoraireJournalier;
  public String totalHebdomadaireComptabilise;
  public String leLibelleHoraire;
  public String erreurHoraire;
  private String laDureeHebdo;  
  public String laCouleur;
  
  // sauvegarde de l'horaire avant modif (en cas d'annulation)
  private HoraireHebdomadaire unHoraireHebdomadaireSelectionneSauvegarde;
  
  private static int pauseMeridienneDureeMini;
  private static int pauseMeridienneDebMini;
  private static int pauseMeridienneFinMaxi;
  private static int aMDebutMini;
  private static int aMDebutMaxi;
  private static int pMFinMini;
  private static int pMFinMaxi;
  private static int pauseRTTDuree;
  private static int pauseRTTGain;
  private static int demiJourneeDureeMaxi;  
  
  public boolean isDisabled = false;
  
	/** indique si on en mode "contraint" : base sur les horaires types ou libre */
  private static boolean isAppLockHorairesTypes;

  public boolean isAutoriseeDureeLibre;
  public boolean isCacherHorairesTypes;
	
  // faut-il afficher la pause RTT	
	private static boolean isShowPause;


  public PersoHoraires(WOContext context) {
    super(context);
    initComponent();
  }
  
  /**
   * 
   */
  private void initComponent() {
  	isAutoriseeDureeLibre = !isAppLockHorairesTypes;
  	// par defaut, on ne se calle pas sur les durees standards
  	setCacherHorairesTypes(true);
  	// sauf si on ne l'autorise pas
  	if (!isAutoriseeDureeLibre) {
  		setCacherHorairesTypes(false);
  	}
  }
  
  /**
	 * @see I_ClasseMetierNotifiableParametre
	 */
	public static void initStaticField(Parametre parametre) {
		if (parametre == Parametre.PARAM_AM_DEBUT_MINI) {
			aMDebutMini = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_AM_DEBUT_MAXI) {
			aMDebutMaxi = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_PM_FIN_MINI) {
			pMFinMini = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_PM_FIN_MAXI) {
			pMFinMaxi = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_PAUSE_RTT_GAIN) {
			pauseRTTGain = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_PAUSE_MERIDIENNE_DUREE_MINI) {
			pauseMeridienneDureeMini = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_PAUSE_MERIDIENNE_DEBUT_MINI) {
			pauseMeridienneDebMini = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_PAUSE_MERIDIENNE_FIN_MAXI) {
			pauseMeridienneFinMaxi = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_PAUSE_RTT_DUREE) {
			pauseRTTDuree = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_LOCK_HORAIRES_TYPES) {
			isAppLockHorairesTypes = parametre.getParamValueBoolean().booleanValue();
		} else if (parametre == Parametre.PARAM_SHOW_PAUSE) {
  		isShowPause = parametre.getParamValueBoolean().booleanValue();
  	} else if (parametre == Parametre.PARAM_DEMI_JOURNEE_DUREE_MAXI) {
  		demiJourneeDureeMaxi = TimeCtrl.getMinutes(parametre.getParamValueString());
  	}
  	
	}

  
  public void appendToResponse(WOResponse arg0, WOContext arg1) {
  	super.appendToResponse(arg0, arg1);
  	// la css de la pause
  	addTextCss(arg0, cssClassPause());
  }
  	
  public void reset() {
    super.reset();
  	isNouvelHoraire=false;
  	isModifHoraire=false;
  	lesHorairesHebdomadaires=null;
  	lesTypesHoraires=null;
  	leLibelleHoraire=null;
  	erreurHoraire = null;
  }

  // navigation

  public WOComponent masquerHorairesTypes() {
    setCacherHorairesTypes(true);
    return neFaitRien();
  }
  
  public WOComponent montrerHorairesTypes() {
    setCacherHorairesTypes(false);
    return neFaitRien();
  }
	
   /**
    * @return
    */
    public WOComponent doSelectUnHoraireHebdomadaire() {
     isNouvelHoraire=false;
     unHoraireHebdomadaireSelectionne = unHoraireHebdomadaire;
     masquerHorairesTypes();
     laDureeHebdo = StringCtrl.replace(unHoraireHebdomadaireSelectionne.getTotal(), "h", ":");
     laCouleur = unHoraireHebdomadaireSelectionne.horaire().couleur();
     uneQuotiteSelectionnee = unHoraireHebdomadaireSelectionne.horaire().quotite();
     
     setLeLibelleHoraire(unHoraireHebdomadaireSelectionne.nom());		
     return null;
   }
 	
   
  public WOComponent nouvelHoraire() {
    HoraireHebdomadaire newHoraireHebdo = new HoraireHebdomadaire();
    newHoraireHebdo.setHoraire(null);
    newHoraireHebdo.setNom(ConstsHoraire.DEFAULT_NAME_HORAIRE);
    isNouvelHoraire=true;
    laCouleur = ConstsHoraire.DEFAULT_COLOR_HORAIRE;
    
    laSession.setIsDisabledMenu(true);
	
    if (lesHorairesHebdomadaires !=null) {
      lesHorairesHebdomadaires = lesHorairesHebdomadaires.arrayByAddingObject(newHoraireHebdo);
    } else {
      lesHorairesHebdomadaires = new NSArray(newHoraireHebdo);
    }
    unHoraireHebdomadaireSelectionne = newHoraireHebdo;	

    // on selectionne par defaut la premier quotite
    uneQuotiteSelectionnee = null;
    if (!ArrayCtrl.isEmpty(lesQuotites())) {
    	uneQuotiteSelectionnee = (Number) lesQuotites().objectAtIndex(0);
    }

  	// selection du premier horaire type par defaut
  	if (!ArrayCtrl.isEmpty(lesTypesHoraires())) {
  		setUnTypeHoraireSelectionne((EOTypeHoraire) lesTypesHoraires().objectAtIndex(0));
  	}
    
    setLeLibelleHoraire(ConstsHoraire.DEFAULT_NAME_HORAIRE);
    
    return null;
  }
  
  /**
   * Dupliquer un horaire.
   * @return
   * @throws Throwable 
   */
  public WOComponent dupliquerHoraire() throws Throwable {
    HoraireHebdomadaire copieHoraireHebdo = HoraireHebdomadaire.dupliquer(unHoraireHebdomadaireSelectionne);
    // on le selectionne
    unHoraireHebdomadaireSelectionne = copieHoraireHebdo;
    // on met a jour le nom
    leLibelleHoraire = unHoraireHebdomadaireSelectionne.nom();
    // sauvegarde dans la base de données
    enregistrerHoraireHebdomadaire();
    return null;
  }

  /**
   * Indique si un horaire est modifiable. Les conditions pour le changer
   * sont : 
   * - il n'est associe a aucune semaine
   * - il n'est associe que sur le planning de test
   * - planning invalide et pas de reel
   * 
   * @return
   */
  public boolean isLockedHoraire() {   
  	if (unHoraireHebdomadaireSelectionne != null && unHoraireHebdomadaireSelectionne.horaire() != null) {
  		
  		EOHoraire horaire = unHoraireHebdomadaireSelectionne.horaire();
  		
  		// a-t-il deja ete associe
  		EOQualifier qualAssoHoraire = CRIDataBus.newCondition(
  				EOPlanningHebdomadaire.HORAIRE_KEY + " = %@", new NSArray(horaire));
  		NSArray listSemaine = laSession.dataBus().fetchArray(
  				edc, EOPlanningHebdomadaire.ENTITY_NAME, qualAssoHoraire, null);
  		if (listSemaine.count() == 0) {
  			return false;
  		}
  		
  		// n'est-il que sur le test
  		NSArray listSemaineNTest = EOQualifier.filteredArrayWithQualifier(
  				listSemaine, new EONotQualifier(EOPlanningHebdomadaire.QUAL_TEST));
  		if (listSemaineNTest.count() == 0) {
  			return false;
  		}
  		
  		// associe au previsionnel, planning invalide et pas de reel
  		/*if (lePlanning().isNonValide()) {
  			NSArray listSemaineReel = EOQualifier.filteredArrayWithQualifier(
    				listSemaine, EOPlanningHebdomadaire.QUAL_REEL);
    		if (listSemaineReel.count() == 0) {
    			return false;
    		}
  		}*/
  		//TODO test a revoir : on ne peut pas supprimer un horaire
  		// qui est sur le prévisionnel alors que le réel existe avec
  		// d'autres horaires
  		
  	}
    return true;
  }
  /**
   * Disponibilite du bouton de suppression d'horaire
   * @return
   */
  public boolean showBtnModifHoraire() {
  	return unHoraireHebdomadaireSelectionne != null && !isLockedHoraire();
  }
  
  /**
   * Disponibilite du bouton de suppression d'horaire
   * @return
   */
  public boolean showBtnSuppHoraire() {
  	return unHoraireHebdomadaireSelectionne != null && !isLockedHoraire();
  }
  
  /**
   * Disponibilite du bouton de copier d'horaire
   * @return
   */
  public boolean showBtnDupliquerHoraire() {
  	return unHoraireHebdomadaireSelectionne != null && !isNouvelHoraire && !isModifHoraire;
  }
  
  /**
   * suppression d'un horaire
   * @throws Throwable 
   */
	public WOComponent supprimerHoraireSelectionne() throws Throwable {
		if (unHoraireHebdomadaireSelectionne!=null && unHoraireHebdomadaireSelectionne.horaire()!=null) {
			edc.deleteObject(unHoraireHebdomadaireSelectionne.horaire());
	    UtilDb.save(edc, true);
			// forcer le rechargement des plannings "P" et "T"
			lePlanning().forceReloadPlanning("T");
			lePlanning().forceReloadPlanning("P");
		}
		lePlanning().removeHoraireHebdomadaire(unHoraireHebdomadaireSelectionne);
		lesHorairesHebdomadaires=null;
		unHoraireHebdomadaireSelectionne = null;
		isNouvelHoraire = false;
		return null;
	}
	
	/**
	 * modification d'un horaire
	 */
	public WOComponent doModifierUnHoraireHebdomadaire() {
		//detaillerUnHoraire();
		//doSelectUnHoraireHebdomadaire();
		unHoraireHebdomadaireSelectionneSauvegarde = unHoraireHebdomadaireSelectionne;
	  isNouvelHoraire = true;
	  isModifHoraire = true;
	  laSession.setIsDisabledMenu(true);
		// forcer le rechargement des plannings "P" et "T"
		lePlanning().forceReloadPlanning("T");
		lePlanning().forceReloadPlanning("P");
	  return null;
	}

	/**
	 * 
	 */
	public WOComponent selectionnerUnTypeHoraire() {
	  if (unTypeHoraireSelectionne != null || StringCtrl.isEmpty(laDureeHebdo)) {
	  	if (isCacherHorairesTypes()) {
	  		setLeLibelleHoraire(ConstsHoraire.DEFAULT_NAME_HORAIRE);
	  	} else {
		    setLeLibelleHoraire(dureeHoraireARealiser());
	  	}
	  } else if (StringCtrl.isEmpty(leLibelleHoraire())) {
	  	setLeLibelleHoraire(ConstsHoraire.DEFAULT_NAME_HORAIRE);          
	  }
	  return null;
	}
	

	public WOComponent saisirUnHoraire() {
		//((Plannings)this.parent()).setSelectedItemMenu(laSession.MENU_PERSO_HORAIRES);
		erreurHoraire = null;
		unHoraireHebdomadaireSelectionne.recalculerTotaux();
		return null;
	}
	


   
  public WOComponent copierHoraireJournalierPrecedent() {
      int indexDest = 0;
      NSArray lesHorairesJournaliers = unHoraireHebdomadaireSelectionne.horairesJournaliers();
      HoraireJournalier horaireSource = null;
      HoraireJournalier horaireDest = (HoraireJournalier)unHoraireJournalier;
      indexDest= lesHorairesJournaliers.indexOfObject(horaireDest);
      horaireSource = (HoraireJournalier)lesHorairesJournaliers.objectAtIndex(indexDest-1);
      
      //((Plannings)this.parent()).setSelectedItemMenu(laSession.MENU_PERSO_HORAIRES);
      unHoraireHebdomadaireSelectionne.recopierHoraire(horaireSource,horaireDest);
      
      return null;
  }
  public WOComponent effacerHoraireJournalier() {
      //((Plannings)this.parent()).setSelectedItemMenu(laSession.MENU_PERSO_HORAIRES);
      unHoraireHebdomadaireSelectionne.effacerHoraire(unHoraireJournalier);
      
      return null;
  }
  public WOComponent validerUnHoraireJournalier() {
  	unHoraireHebdomadaireSelectionne.recalculerTotaux();
  	return null;
  }
  
  /**
   * Enregistrer un nouvel horaire / des modifications d'horaire
   * @return
   * @throws Throwable
   */
  public WOComponent enregistrerHoraireHebdomadaire() throws Throwable {
    unHoraireHebdomadaireSelectionne.recalculerTotaux();
    
    // si la duree est libre, alors la duree a realiser 
    // n'est plus une contrainte
    String dureeARealiser = dureeHoraireARealiser();
    if (isCacherHorairesTypes()) {
    	dureeARealiser = unHoraireHebdomadaireSelectionne.getTotal();
    }
    
    if (unHoraireHebdomadaireSelectionne.isValide(
    			lePlanning().affectationAnnuelle(), 
    			dureeARealiser, 
    			uneQuotiteSelectionnee, 
    			leLibelleHoraire())) { 	                  
    	EOHoraire newHoraire = unHoraireHebdomadaireSelectionne.horaire();
                  
      // pas en modification, on cree un nouvel horaire
      if (isModifHoraire == false) {  
        newHoraire = new EOHoraire();
        newHoraire.addObjectToBothSidesOfRelationshipWithKey(lePlanning().affectationAnnuelle(), "affectationAnnuelle");
        newHoraire.insertInEditingContext(edc);
      }
      
      newHoraire.setHoraires(unHoraireHebdomadaireSelectionne.toStringHoraires());
      newHoraire.setPauses(unHoraireHebdomadaireSelectionne.toStringPauses());
      newHoraire.setDureesAM(unHoraireHebdomadaireSelectionne.toStringDureesAM());
      newHoraire.setDureesPM(unHoraireHebdomadaireSelectionne.toStringDureesPM());
      newHoraire.setNom(leLibelleHoraire());
      newHoraire.setDurees(unHoraireHebdomadaireSelectionne.toStringDurees());
      newHoraire.setQuotite(new Integer(uneQuotiteSelectionnee.intValue()));
      
      // modif, on enleve l'ancien horaire et on le replace par le nouveau
      if (isModifHoraire) {
        lePlanning().removeHoraireHebdomadaire(unHoraireHebdomadaireSelectionne);
      }
      
      unHoraireHebdomadaireSelectionne.setHoraire(newHoraire);
      lePlanning().addHoraireHebdomadaire(unHoraireHebdomadaireSelectionne);   
      //lesHorairesHebdomadairesSelectionnes = new NSArray(unHoraireHebdomadaireSelectionne);
      isNouvelHoraire = false;
      isModifHoraire = false;
      laSession.setIsDisabledMenu(false);
      erreurHoraire="";       
    } 
    else {
      erreurHoraire="Horaire incorrect : " + unHoraireHebdomadaireSelectionne.erreur();      
    }
  
    //((Plannings)this.parent()).setSelectedItemMenu(laSession.MENU_PERSO_HORAIRES);
    return enregistrerCouleur();
  }
  
  


  
  public WOComponent annulerHoraireHebdomadaire() {
    // on remet l'ancien horaire en cas de modif
    if (isModifHoraire)
      unHoraireHebdomadaireSelectionne.setHoraire(unHoraireHebdomadaireSelectionneSauvegarde.horaire());
    reset();        
    laSession.setIsDisabledMenu(false);     
    return  null;
  }



  
  // getters 
  
  public Planning lePlanning() {
    return lePlanning;
}
  
  public HoraireJournalier unHoraireJournalier() {
      return unHoraireJournalier;
  }
  public String totalHebdomadaireComptabilise() {
      return totalHebdomadaireComptabilise;
  }




  public NSArray lesTypesHoraires() {
    if (lesTypesHoraires == null) {
      if (lePlanning().affectationAnnuelle().isHorsNorme()) {
        lesTypesHoraires = lePlanning().lesTypesHorairesHorsNormes();
      }
      else {
        lesTypesHoraires = lePlanning().lesTypesHorairesNormaux();          
      }
    }
    return lesTypesHoraires;
  }
  
  public String dureeHoraireARealiser() {
    String dureeHoraireARealiser = "0h00";

    if (laDureeHebdo != null) {
      if (!lePlanning().affectationAnnuelle().isTempsPartielAnnualise()) {
        if (uneQuotiteSelectionnee != null) {
          dureeHoraireARealiser = TimeCtrl.stringForMinutes(TimeCtrl.getMinutes(laDureeHebdo) * uneQuotiteSelectionnee.intValue() / 100);
        }
      }
      else {
          dureeHoraireARealiser = laDureeHebdo;            
      }
      dureeHoraireARealiser = StringCtrl.replace(dureeHoraireARealiser, ":", "h");
    } 
    return dureeHoraireARealiser;
  }


  /**
   * @return
   */
  public String leLibelleHoraire() {
      return leLibelleHoraire;
  }

  public String onchange(){
      String onchange="";
      
      if (lePlanning().affectationAnnuelle().isPasseDroit() == false) {
          onchange="validerHoraire(this,";
          onchange+=  pauseMeridienneDureeMini+",";
          onchange+=  pauseMeridienneDebMini+",";
          onchange+=  pauseMeridienneFinMaxi+",";
          onchange+=  aMDebutMini+",";
          onchange+=  aMDebutMaxi+",";
          onchange+=  pMFinMini+",";
          onchange+=  pMFinMaxi+",";
          onchange+=  pauseRTTDuree+",";
          onchange+=  pauseRTTGain+",";
          onchange+=  demiJourneeDureeMaxi+",";
          onchange+=  "'N');";

      } else {
          onchange = "validerHoraire(this,0,0,0,0,0,0,0,";
          onchange+=  pauseRTTDuree+",";
          onchange+=  pauseRTTGain+",";
          onchange+=  "0,'O');";
      }

      return onchange;
  }


  /**
   * Liste des quotités disponibles
   * @return
   */
  public NSArray lesQuotites() {
  	NSArray affectations = (NSArray)lePlanning().affectationAnnuelle().periodes().valueForKey(
  			EOPeriodeAffectationAnnuelle.AFFECTATION_KEY);
  	// classer par quotité croissante
  	affectations = LRSort.sortedArray(affectations, EOAffectation.NUM_QUOTATION_KEY);
  	// descendre à la valeur de la quotité
  	NSArray lesQuotites = (NSArray) affectations.valueForKey(EOAffectation.NUM_QUOTATION_KEY);
  	// on enleve les doublons
  	lesQuotites = ArrayCtrl.removeDoublons(lesQuotites);
  	return lesQuotites;
  }

  
  /**
   * @return
   */
  public NSArray lesHorairesHebdomadaires() {
    if (lesHorairesHebdomadaires == null) {
      lesHorairesHebdomadaires = lePlanning().horairesHebdomadaires();
      if (lesHorairesHebdomadaires == null || lesHorairesHebdomadaires.count()==0) {
        isNouvelHoraire=false;
        lesHorairesHebdomadaires=null;
        unHoraireHebdomadaireSelectionne=null;
      } else {
        isNouvelHoraire=false;
        unHoraireHebdomadaireSelectionne = (HoraireHebdomadaire)lesHorairesHebdomadaires.objectAtIndex(0);
      }
          
      if (unHoraireHebdomadaireSelectionne != null){
      	// Uniquement pour initialiser la liste des types
        if (unHoraireHebdomadaireSelectionne.horaire()!=null) {
          //masquerHorairesTypes();
          laDureeHebdo = (TimeCtrl.stringForMinutes(unHoraireHebdomadaireSelectionne.horaire().minutesDureeHebdo()));
          laCouleur = unHoraireHebdomadaireSelectionne.horaire().couleur();
          unTypeHoraire=unTypeHoraireSelectionne;
          //lesQuotites();
          uneQuotiteSelectionnee = unHoraireHebdomadaireSelectionne.horaire().quotite();
          setLeLibelleHoraire(unHoraireHebdomadaireSelectionne.nom());
        }
      }
    } 
    return lesHorairesHebdomadaires;
  }

  public String heureDebutAMName() {
      String heureDebutAMName="debutAM";
      
      heureDebutAMName += "_"+unHoraireJournalier.libelleJour();
      
      return heureDebutAMName;
  }
  public String heureFinAMName() {
      String heureFinAMName="finAM";
      
      heureFinAMName += "_"+unHoraireJournalier.libelleJour();
      
      return heureFinAMName;
  }
  public String heureDebutPMName() {
      String heureDebutPMName="debutPM";
      
      heureDebutPMName += "_"+unHoraireJournalier.libelleJour();
      
      return heureDebutPMName;
  }
  public String heureFinPMName() {
      String heureFinPMName="finPM";
      
      heureFinPMName += "_"+unHoraireJournalier.libelleJour();
      
      return heureFinPMName;
  }
  public String heureDebutPauseName() {
      String heureDebutPauseName="debutPause";
      
      heureDebutPauseName += "_"+unHoraireJournalier.libelleJour();
      
      return heureDebutPauseName;
  }
  public String heureDebutPauseClassName(){
      String heureDebutPauseClassName = "TFCenterGrey";
      
      if (unHoraireJournalier!=null && 
          StringCtrl.isEmpty(unHoraireJournalier.getHeureDebutPause())==false) {
          heureDebutPauseClassName = "TFCenter";
      }
      
      return heureDebutPauseClassName;
  }
  
  public String horaireJournalierTotalName() {
      String horaireJournalierTotalName="totalJournalier";
      
      horaireJournalierTotalName += "_"+unHoraireJournalier.libelleJour();
      
      return horaireJournalierTotalName;
  }

  public String onClickCopierHoraireJournalier() {
      String onClickCopierHoraire="";
      HoraireJournalier dest = unHoraireJournalier();
      if (dest!=null) {
          NSArray horairesJournaliers = unHoraireHebdomadaireSelectionne.horairesJournaliers();
          int indexDest = horairesJournaliers.indexOfObject(unHoraireJournalier);
          HoraireJournalier source = (HoraireJournalier)horairesJournaliers.objectAtIndex(indexDest-1);
      
          onClickCopierHoraire="copierHorairePrecedent('";
          onClickCopierHoraire += source.libelleJour();
          onClickCopierHoraire += "','";
          onClickCopierHoraire += unHoraireJournalier.libelleJour();
          onClickCopierHoraire += "');";
      }
      
      return onClickCopierHoraire;
  }
  
  public String onClickEffacerHoraireJournalier() {
      String onClickEffacerHoraireJournalier="";
      
      HoraireJournalier dest = unHoraireJournalier();
      if (dest!=null) {
      
          onClickEffacerHoraireJournalier="return (effacerHoraire('";
          onClickEffacerHoraireJournalier += dest.libelleJour();
          onClickEffacerHoraireJournalier += "'));";
      }
      return onClickEffacerHoraireJournalier;
  }
  
  // setters
  public void setTotalHebdomadaireComptabilise(String newTotalHebdomadaireComptabilise) {
    totalHebdomadaireComptabilise = newTotalHebdomadaireComptabilise;
  }   

  public void setUnHoraireJournalier(HoraireJournalier newUnHoraireJournalier) {
    unHoraireJournalier = newUnHoraireJournalier;
  }


  public void setLeLibelleHoraire(String string) {
    leLibelleHoraire = string;
    unHoraireHebdomadaireSelectionne.setNom(leLibelleHoraire);
  }   

  public void setUnTypeHoraireSelectionne(EOTypeHoraire value) {
    unTypeHoraireSelectionne = value;
    if (unTypeHoraireSelectionne != null) {
      laDureeHebdo = (TimeCtrl.stringForMinutes(unTypeHoraireSelectionne.total().intValue()));        
    }
 }


  public void setLesHorairesHebdomadaires(NSArray array) {
      lesHorairesHebdomadaires = array;
  }
  
  /**
   * setter planning : si planning different, on RAZ la liste des horaires de l'agent
   * @param value
   */
    public void setLePlanning(Planning value) {
        if (lePlanning != null && lePlanning != value) {
            reset();
        }
        lePlanning = value;
    }
 
    // boolean interface
  
  /**
   * Message "Vous pouvez sasir le planning prev" :
   * - le planning est invalide et 
   * - des horaires sont saisis et
   * - pas d'erreur dans l'horaire en cours de saisie
   */
  public boolean showInfoPrev() {
  	boolean show = false;
  	EOAffectationAnnuelle affectation = lePlanning().affectationAnnuelle();
  	if (!hasErreurHoraire() && 
  			lePlanning().isNonValide() && 
  			affectation.horaires()!=null && 
  			affectation.horaires().count()>0)
  		show = true;
  	return show;
  }

  /**
   * Message comment saisir un horaire :
   * - aucun horaire est saisi ou
   * - debut de la saisie d'un horaire
   * @return
   */
  public boolean showInfoHoraire() {
    boolean show = false;
    EOAffectationAnnuelle affectation = lePlanning().affectationAnnuelle();
    if (
        ((affectation.horaires() == null || affectation.horaires().count() == 0) && unHoraireHebdomadaire == null) ||
        (
            unHoraireHebdomadaireSelectionne != null &&
            (
                unHoraireHebdomadaireSelectionne.horairesJournaliers() == null ||
                unHoraireHebdomadaireSelectionne.horairesJournaliers().count() == 0)))
      show = true;
    return show;
  }
  
  public boolean hasErreurHoraire() {
    return !StringCtrl.isEmpty(erreurHoraire);
  }
  
  public boolean isNotNouvelHoraire() {
      return !isNouvelHoraire && !isModifHoraire;
  }


  public boolean hasSelectedHoraire(){
  	return unHoraireHebdomadaireSelectionne != null;
  }

  /**
   * On affiche d'une maniere visible l'horaire selectionn�
   * @return
   */
  public boolean isTheHoraireHebdomadaireSelectionne() {
  	return unHoraireHebdomadaire == unHoraireHebdomadaireSelectionne;
  }
  
  /**
   * On n'autorise pas le changement en mode saisie de nouvel
   * horaire ou bien si l'horaire en cours est celui selectionn�
   * @return
   */
  public boolean isDisabledLnkDoSelectHoraire() {
  	return isNouvelHoraire || isTheHoraireHebdomadaireSelectionne();
  }
  

  public boolean isLundi() {
    return unHoraireJournalier.libelleJour().equals("Lundi");
  }
  
  /**
   * on ne peux modifier la couleur que des ses propres horaires
   * @return
   */
  public boolean isDisabledBtnModifierCouleur() {
    boolean isDisabled = true;

    if (cngUserInfo().isAllowedModifyPlanning(lePlanning.affectationAnnuelle())) {
      isDisabled = false;
    }
    
    return isDisabled;
  }
  
  public WOComponent enregistrerCouleur() throws Throwable {
    if (!StringCtrl.isEmpty(laCouleur)) {
      if (unHoraireHebdomadaireSelectionne.horaire() != null) {
        unHoraireHebdomadaireSelectionne.horaire().setCouleur(laCouleur);
        UtilDb.save(edc, true);
      }
    }    
    return neFaitRien();
  }

  /**
   * l'horaire existe-il dans la base de donnees?
   * @return
   */
  public boolean isHoraireDejaEnregistre() {
    return unHoraireHebdomadaireSelectionne != null && unHoraireHebdomadaireSelectionne.horaire() != null;
  }
  
  /**
   * R�cup�ration de tous les horaires de l'ann�e universitaire
   * pr�c�dente.
   * @throws Throwable 
   */
  public WOComponent importPrevHoraires() throws Throwable {
    NSArray prevsAffAnn = EOAffectationAnnuelle.findAffectationsAnnuelleInContext(
        edc, lePlanning().affectationAnnuelle().individu().oid(),
        lePlanning().affectationAnnuelle().structure().cStructure(), 
        lePlanning().affectationAnnuelle().dateDebutAnnee().timestampByAddingGregorianUnits(-1,0,0,0,0,0));
    if (prevsAffAnn.count() > 0) {
      EOAffectationAnnuelle prefAffAnn = (EOAffectationAnnuelle) prevsAffAnn.lastObject();
      NSArray prevHoraires = prefAffAnn.horaires();
      for (int i = 0; i < prevHoraires.count(); i++) {
        EOHoraire prevHoraire = (EOHoraire) prevHoraires.objectAtIndex(i);
  			CngHoraireBus.dupliqueHoraire(lePlanning(), prevHoraire, 
  					prevHoraire.nom()+" "+lePlanning().affectationAnnuelle().annee());
      }
      UtilDb.save(edc, true);
      lesHorairesHebdomadaires = null;
    }
        
    return null;
  }
  
  /**
   * On ne peut pas importer les horaires si
   * certains sont deja saisis ou s'il n'existe
   * pas d'horaires sur l'annee universitaire
   * precedente. On affiche le bouton de recuperation
   * si 1 seul horaire est present, et que c'est celui
   * qui fait charniere entre les 2 annees.
   */
  public boolean showBtnImportPrevHoraires() {
  	if (prevsAffAnn().count() > 0) {
  		if (lesHorairesHebdomadaires() == null || lesHorairesHebdomadaires().count() == 0) {
  			return true;
  		}
  		if (lesHorairesHebdomadaires().count() == 1) {
  			HoraireHebdomadaire horaireHebdo = (HoraireHebdomadaire) lesHorairesHebdomadaires().lastObject();
  			if (!StringCtrl.isEmpty(horaireHebdo.nom()) && 
  					horaireHebdo.nom().startsWith(ConstsHoraire.PREFIX_LABEL_HORAIRE_CHARNIERE)) {
  				return true;
  			}
  		}
  	}
  	return false;
  }
  
  /**
   * La liste des affectations annuelles precedentes
   */
  private NSArray prevsAffAnn() {
    return EOAffectationAnnuelle.findAffectationsAnnuelleInContext(
        edc, lePlanning().affectationAnnuelle().individu().oid(),
        lePlanning().affectationAnnuelle().structure().cStructure(), 
        lePlanning().affectationAnnuelle().dateDebutAnnee().timestampByAddingGregorianUnits(-1,0,0,0,0,0));
  }

  private String _cssClassPause;
  
  /**
   * Si la pause doit etre masquee, alors on la masque
   * en CSS. 
   */
  private String cssClassPause() {
  	if (_cssClassPause == null) {
  		_cssClassPause = ".pause {\n";
  		if (!isShowPause) {
  			_cssClassPause += "display:none;";
  		}
  		_cssClassPause += "\n}";
  	}
  	return _cssClassPause;
  }
  
  public String getStrHtmlContraintesEmbaucheDebauche() {
  	return unHoraireHebdomadaireSelectionne.getStrHtmlContraintesEmbaucheDebauche(
  			lePlanning().affectationAnnuelle(), uneQuotiteSelectionnee);
  }
  
  // SETTER
  public void setDisabled(boolean value) {
  	isDisabled = value;
  }

	public final boolean isCacherHorairesTypes() {
		return isCacherHorairesTypes;
	}

	public final void setCacherHorairesTypes(boolean isCacherHorairesTypes) {
		this.isCacherHorairesTypes = isCacherHorairesTypes;
	}

	public final int getPauseRTTDuree() {
		return pauseRTTDuree;
	}

}