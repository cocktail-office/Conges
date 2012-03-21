package fr.univlr.cri.conges;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;

import fr.univlr.cri.conges.ParentPlannings;
import fr.univlr.cri.conges.constantes.ConstsAlerte;
import fr.univlr.cri.conges.constantes.ConstsMenu;
import fr.univlr.cri.conges.constantes.ConstsPlanning;
import fr.univlr.cri.conges.databus.CngAlerteBus;
import fr.univlr.cri.conges.databus.CngPlanningBus;
import fr.univlr.cri.conges.eos.modele.conges.*;
import fr.univlr.cri.conges.eos.modele.planning.*;
import fr.univlr.cri.conges.objects.*;
import fr.univlr.cri.conges.print.CngPdfBoxCtrl;
import fr.univlr.cri.conges.print.ConstsPrint;
import fr.univlr.cri.conges.print.PrintHoraires;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.LRLog;
import fr.univlr.cri.webext.*;
import fr.univlr.cri.ycrifwk.utils.UtilDb;


public class PagePlannings 
	extends ParentPlannings
		implements ConstsPlanning {
    
  public final static String CRI_POPUP_SUFFIX = "<br><br><i>Cliquez sur la fen&egrave;tre pour la fermer</i>";
	
  public String txtErreur;

  /** la raison pour laquelle le planning previsionnel ne peut etre valide */
  public String htmlTextValidationPlanningPrevisionnelImpossible;
  
  public String menuSelectionne = null;
  public EOAffectationAnnuelle affAnn = null; 
	
  // backup de l'ancien planning : on reaffiche le popup javascript uniquement quand il change
  private Planning ancienPlanning;
  private boolean planningHasChanged;

  public PagePlannings(WOContext context) {
    super(context);
  }
  
  public void setSelectedAffAnn(EOAffectationAnnuelle value) {
    laSession.setSelectedAffAnnPerso(value);
  	initialiserVisualisation();
  }
  
  public EOAffectationAnnuelle selectedAffAnn() {
    return laSession.selectedAffAnnPerso();
  }
    
  public Planning lePlanning() {
    return laSession.getLePlanningSelectionne();
  }
    
  public boolean hasErreur() {
    return txtErreur != null;
  }
    
  public void appendToResponse(WOResponse arg0, WOContext arg1) {
    // ancien et nouveau planning identiques ?
    planningHasChanged = (ancienPlanning != lePlanning());
    ancienPlanning = lePlanning();
    //  on controle si le planning reel est correct (limite des 390h00 ou 8 semaines hautes)
    if (lePlanning().isPReel() && ConstsMenu.MENU_PERSO_PLA_REEL.equals(menuSelectionne()) && (
        lePlanning().limiteCongesDepassee() || 
        lePlanning().limiteSemainesHautesDepassee() )) {
      // il reste a STATUS_NON_VALIDE s'il etait STATUS_NON_VALIDE
      if (!(lePlanning().isNonValide())) {
      	// on fait une copie dans le planning de sauvegarde
      	planningBus().dupliquerPlanning(
            lePlanning().affectationAnnuelle(), lePlanning().type(), ConstsPlanning.SAVE, false);
        lePlanning().affectationAnnuelle().setStatusPlanning(Planning.PLANNING_STATUT_EN_COURS_DE_MODIFICATION);        
  	    try {
					UtilDb.save(edc, true);
				} catch (Throwable e) {
					e.printStackTrace();
				}
        LRLog.log(lePlanning().affectationAnnuelle().individu().nomComplet() + 
        		" : planning automatiquement force a 'en cours de modification' (" + 
        		(lePlanning().limiteCongesDepassee() ? 
        				"limite de conges depassee)" : 
        				"limite de semaines hautes depassee)"));
      }
    }
    super.appendToResponse(arg0, arg1);
  }
  	
  /**
   * Methode appelee lorsque le service est change
   */
  public WOComponent changeService() {
    return null;
  }
    
  /**
   * Forcer l'initialisation et la selection des
   * bons items du menu d'apres l'etat du planning
   * en cours
   */
  public void initialiserVisualisation() {
    EOAffectationAnnuelle affectation = lePlanning().affectationAnnuelle();
    NSArray horaires = affectation.horaires();
    if (horaires != null && horaires.count()>0) {
      if (lePlanning().isNonValide())
        setMenuSelectionne(ConstsMenu.MENU_PERSO_PLA_PREV);
      else
        setMenuSelectionne(ConstsMenu.MENU_PERSO_PLA_REEL);
    } else {
      setMenuSelectionne(ConstsMenu.MENU_PERSO_HORAIRES);
    }
  }
    
  /**
   * @return
   */
  public String menuSelectionne() {
    if (menuSelectionne == null)
      menuSelectionne=(String)laSession.menuItemsPersonnel().objectAtIndex(0);
    return menuSelectionne;
  }
  
  /**
   * Force le type du planning d'apres le menu selectionne
   */
  public void setMenuSelectionne(String value) {
  	menuSelectionne = value;
    if (ConstsMenu.MENU_PERSO_PLA_REEL.equals(menuSelectionne) ||
        ConstsMenu.MENU_PERSO_FIC_ROSE.equals(menuSelectionne))
      lePlanning().setType("R");
    else if (ConstsMenu.MENU_PERSO_PLA_PREV.equals(menuSelectionne))
      lePlanning().setType("P"); 
    // si pas de type au planning, on force le previsionnel
    if (StringCtrl.isEmpty(lePlanning().type()))
      lePlanning().setType("P");
  }

  /**
   * Est-ce autorise de devalider le planning reel
   */
  public boolean isPlanningDevalidable() {
  	boolean isPlanningDevalidable = false;
  
  	if (ConstsMenu.MENU_PERSO_PLA_REEL.equals(menuSelectionne())) {
  		if (lePlanning().isValide() && cngUserInfo().isAllowedModifyPlanning(lePlanning().affectationAnnuelle())) {
  			isPlanningDevalidable = true;
  		}
  	}
  	return isPlanningDevalidable;
  }
  
  /**
   * Est-ce autorise de valider le planning reel
   *
   * @param verifDemandeur : faut-il s'assure que le demandeur est bien la personne connectee
   */
  private boolean isPlanningValidable(boolean verifDemandeur) {
  	boolean isPlanningValidable = false;
     
  	if (ConstsMenu.MENU_PERSO_PLA_REEL.equals(menuSelectionne()) &&
  		!lePlanning().limiteCongesDepassee() &&
  		!lePlanning().limiteSemainesHautesDepassee() &&
        (verifDemandeur ? cngUserInfo().isAllowedModifyPlanning(lePlanning().affectationAnnuelle()) : true)) {
  		if (lePlanning().isEnCoursDeModification()) {
  			isPlanningValidable = true;
  		}
  	}
  	return isPlanningValidable;
  }
  
  public boolean isPlanningValidable() {
    return isPlanningValidable(true);
  }
  
  /**
   * Est-ce autorise d'annuler la demande de validation du planning reel
   */
  private boolean isAnnulationValidationPReelPossible(boolean verifDemandeur) {
    boolean isAnnulationValidationPReelPossible = false;
  	if (ConstsMenu.MENU_PERSO_PLA_REEL.equals(menuSelectionne())) {
  	  EOAffectationAnnuelle affectationAnnuelle = lePlanning().affectationAnnuelle();
  	  if (lePlanning().isEnCoursDeValidation() &&
          lePlanning().semainesLibres().count()==0 &&
          affectationAnnuelle.horaires().count()>0 &&
          !lePlanning().limiteCongesDepassee() &&
          !lePlanning().limiteSemainesHautesDepassee() &&
          (verifDemandeur ? cngUserInfo().isAllowedModifyPlanning(affectationAnnuelle) : true)
    	) {
    	  isAnnulationValidationPReelPossible = true;
    	}
    }
  
  	return isAnnulationValidationPReelPossible;
  }
  
  public boolean isAnnulationValidationPReelPossible() {
    return isAnnulationValidationPReelPossible(true);
  }
  
  /**
   * Devalidation du planning reel. Message
   * de confirmation
   */
  
  public WOComponent devaliderPReel() {
    // page de confirmation
    DevaliderPReelResponder responder = new DevaliderPReelResponder(this);
    return CRIAlertPage.newAlertPageWithResponder(this, "Modification du planning<br>",
        "<center>Confirmation de l'op&eacute;ration:<br><br>"+
        "Etes vous sur de vouloir d&eacute;valider "+
        (laSession.isIndividuConnecte() ? 
        		"votre planning" : 
      			"le planning de " + laSession.selectedAffAnnPerso().individu().nomComplet()) +
        " ?",
        "Confirmer", "Annuler", null, CRIAlertPage.QUESTION, responder);
  }
  
  /**
   * Recopie du planning reel vers le planning de test.
   * Message de confirmation
   */
  public WOComponent recopierReelToTest() {
    // page de confirmation
    RecopierReelToTestResponder responder = new RecopierReelToTestResponder(this);
    return CRIAlertPage.newAlertPageWithResponder(this, "Recopie de planning<br>",
        "<center>Confirmation de l'op&eacute;ration:<br><br>"+
        "Etes vous sur de vouloir &eacute;craser votre planning de test avec le r&eacute;el?",
        "Confirmer", "Annuler", null, CRIAlertPage.QUESTION, responder);
  }
  
  
  /**
   * Pour les lignes de log, on affiche le nom du delegue qui
   * a fait la demande si besoin
   * @return
   */
  private String logSuffixDelegation() {
  	String result = "";
  	if (!laSession.isIndividuConnecte()) {
  		return " (" + laSession.individuConnecte().nomComplet() + " par delegation)";
  	}
  	return result;
  }
  
  /**
   * La classe interne - l'implementation de AlertResponder pour
     * la devalidation du planning.
   */	 
  public class DevaliderPReelResponder implements CRIAlertResponder {
  	
    private WOComponent parentComponent;
    public DevaliderPReelResponder(WOComponent aParentComponent) {
      parentComponent = aParentComponent;
    }
  	
    public WOComponent respondToButton(int buttonNo) {
      switch (buttonNo) {
      case 2: return parentComponent;
      case 1: 
      	// on va sauvegarder l'association actuelle 
      	// pour offrir une page de comparaison entre
      	// ancienne et nouvelle version du planning
      	EOAffectationAnnuelle recAffAnn = lePlanning().affectationAnnuelle();
      	planningBus().dupliquerPlanning(recAffAnn, "R", "S", false);
      	recAffAnn.setStatusPlanning(Planning.PLANNING_STATUT_EN_COURS_DE_MODIFICATION);
        // supprimer les historiques de visa et de validation
      	recAffAnn.removeRepartVisa();      	
      	recAffAnn.removeRepartValidation();
        //
  	    try {
  	    	UtilDb.save(edc, true);
  	    } catch (Throwable e) {
  	    	e.printStackTrace();
  	    }
        LRLog.log(recAffAnn.individu().nomComplet() + " : devalidation planning reel" + logSuffixDelegation());
        return parentComponent;
      default: return null;
      }
    }
  } 
  
  /**
   * La classe interne - l'implementation de AlertResponder pour
   * la recopie du planning.
   */    
  public class RecopierReelToTestResponder implements CRIAlertResponder {
    
    private WOComponent parentComponent;
    public RecopierReelToTestResponder(WOComponent aParentComponent) {
      parentComponent = aParentComponent;
    }
    
    public WOComponent respondToButton(int buttonNo) {
      switch (buttonNo) {
      case 2: return parentComponent;
      case 1: 
        // dupliquer le reel vers le test
        laSession.cngDataCenter().planningBus().dupliquerPlanning(
            lePlanning().affectationAnnuelle(), "R", "T", true);
  	    try {
  	    	UtilDb.save(edc, true);
  	    } catch (Throwable e) {
  	    	e.printStackTrace();
  	    }
        // forcer le rechargement du planning
        lePlanning().forceReloadPlanning("T");
        lePlanning().setType("T");
        LRLog.log(lePlanning().affectationAnnuelle().individu().nomComplet() + " : recopie planning reel vers test" + logSuffixDelegation());
        return parentComponent;
      default: return null;
      }
    }
  } 
  
  public WOComponent validerPReel() throws Throwable {
    EOAffectationAnnuelle recAffAnn = lePlanning().affectationAnnuelle();
  	EOAlerte alerte = EOAlerte.newEOAlerteInContext(null, edc, lePlanning());
  	alerte.setAffectationAnnuelleRelationship(recAffAnn);
  	// on precise l'annee universitaire
  	alerte.setLibelle(
  			ConstsAlerte.ALERT_PREFIX_LIBELLE_MODIF_P_REEL + " " +
  			DateCtrlConges.anneeUnivForDate(lePlanning().affectationAnnuelle().dateDebutAnnee()));
  	alerte.insertInEditingContext(edc);
  	// Changement du status du planning ==> Planning en cours de validation
  	recAffAnn.setStatusPlanning(Planning.PLANNING_STATUT_EN_COURS_DE_VALIDATION);
  	// si demande par delegation, on enregistre le demandeur
  	if (!laSession.isIndividuConnecte()) {
  		recAffAnn.setToIndividuDemandeurRelationship(laSession.individuConnecte());
  	}
  	try {
  	  alerte.sendMailsNouvelleAlerte(laSession);
  	} catch (Exception e) {
  	  txtErreur = e.getMessage();      
  	  edc.revert();
  	}
    UtilDb.save(edc, true);
  	LRLog.log(recAffAnn.individu().nomComplet() + " : demande validation planning reel" + logSuffixDelegation());
    return null;
  }
  
  public WOComponent annulerValidationPrev() throws Throwable {
  	EOAffectationAnnuelle recAffAnn = lePlanning().affectationAnnuelle();
  	// Changement du status du planning ==> Planning non valide
  	recAffAnn.setStatusPlanning(Planning.PLANNING_STATUT_INVALIDE);
    edc.deleteObject(alerteBus().findAlertePPrevForPlanning(lePlanning()));
    // supprimer les historiques de visa et de validation
    recAffAnn.removeRepartVisa();      	
    recAffAnn.removeRepartValidation();
    UtilDb.save(edc, true);
  	LRLog.log(recAffAnn.individu().nomComplet() + " : annulation demande validation planning previsionnel" + logSuffixDelegation());
    return null;
  }
  
  public WOComponent annulerValidationReel() throws Throwable {
  	EOAffectationAnnuelle recAffAnn = lePlanning().affectationAnnuelle();
  	// Changement du status du planning ==> Planning en cours de validation
  	recAffAnn.setStatusPlanning(Planning.PLANNING_STATUT_EN_COURS_DE_MODIFICATION);
    // on efface l'alerte associee si elle existe
    EOAlerte alertePReel = alerteBus().findAlertePReelForPlanning(lePlanning());
    if (alertePReel != null) {
    	edc.deleteObject(alertePReel);
    }
    // supprimer les historiques de visa et de validation
    recAffAnn.removeRepartVisa();      	
    recAffAnn.removeRepartValidation();
    UtilDb.save(edc, true);
  	LRLog.log(recAffAnn.individu().nomComplet() + " : annulation demande validation planning reel" + logSuffixDelegation());
  	return null;
  }
  
  /**
   * Faut-il afficher le warning indiquant les raisons de l'impossibilite
   * de valider le previsionnel ?
   * @return
   */
  public boolean isShowWarningValidationPlanningPrevisionnelImpossible() {
  	return !StringCtrl.isEmpty(htmlTextValidationPlanningPrevisionnelImpossible);
  }
  
  /**
   * Est-ce autorise de valider le planning prev.
   * Cette methode initialise <code>htmlTextValidationPlanningPrevisionnelImpossible</code>
   * avec la raison pourlaquelle le planning n'est pas validable si besoin.
   * 
   * @param verifDemandeur : faut-il s'assure que le demandeur est bien la personne connectee
   */
  private boolean isValidationPlanningPrevisionnelPossible(boolean verifDemandeur) {
  	htmlTextValidationPlanningPrevisionnelImpossible = "";
  	
  	if (ConstsMenu.MENU_PERSO_PLA_PREV.equals(menuSelectionne())) {
  		EOAffectationAnnuelle affectationAnnuelle = lePlanning().affectationAnnuelle();
  
  		if (lePlanning().isNonValide()) {
  			
  			// y a-t-il des horaires ?
  			if (affectationAnnuelle.horaires().count() == 0) {
  				htmlTextValidationPlanningPrevisionnelImpossible = HTML_MSG_VALIDATION_PREVISIONNEL_IMPOSSIBLE_NO_HORAIRE;
  				return false;
  			} 
  			
  			// la limite des conges est-elle depassee ?
  			if (lePlanning().limiteCongesDepassee()) {
  				htmlTextValidationPlanningPrevisionnelImpossible = HTML_MSG_VALIDATION_PREVISIONNEL_IMPOSSIBLE_LIMITE_CONGES_DEPASSEE;
  				return false;
  			}
  			
  			// la limite de semaines hautes est-celle depassee ?
  			if (lePlanning().limiteSemainesHautesDepassee()) {
   				htmlTextValidationPlanningPrevisionnelImpossible = HTML_MSG_VALIDATION_PREVISIONNEL_IMPOSSIBLE_LIMITE_SEM_HAUTE_DEPASSEE;
  				return false;
  			}

  			// est-ce que toutes les semaines attendues sont associees ?
  			if (!lePlanning().isToutesLesSemainesActivesAssociees()) {
  				NSArray semainesNonAssociees = lePlanning().semainesActivitesNonAssociees();
  				// on fait un post traitement pour avoir unicite sur les dates de debut de semaine
  				NSArray semainesUniques = new NSArray();
  				NSArray debutsUniques = new NSArray();
  				for (int i=0; i<semainesNonAssociees.count(); i++) {
  					Semaine semaine = (Semaine) semainesNonAssociees.objectAtIndex(i);
  					// controle si pas deja traitee
  					if (!debutsUniques.containsObject(semaine.debut())) {
    					debutsUniques = debutsUniques.arrayByAddingObject(semaine.debut());
    					semainesUniques = semainesUniques.arrayByAddingObject(semaine);
  					}
  				}
  				// on peut maintenant construire le message
  				htmlTextValidationPlanningPrevisionnelImpossible = HTML_MSG_VALIDATION_PREVISIONNEL_IMPOSSIBLE_SEM_ACTIVES_NON_ASSO_PREFIX; 
  				htmlTextValidationPlanningPrevisionnelImpossible += semainesUniques.count()+"): ";
  				for (int i=0; i<semainesUniques.count() && i<MSG_VALIDATION_PREVISIONNEL_COUNT_MAX_SEMAINE_DISPLAYED; i++) {
  					Semaine semaine = (Semaine) semainesUniques.objectAtIndex(i);
  					htmlTextValidationPlanningPrevisionnelImpossible += 
  						"<br/>&nbsp;&raquo;&nbsp;num&eacute;ro " + semaine.numero() + " du " + 
  						DateCtrlConges.dateToString(semaine.debut()) + " au " + DateCtrlConges.dateToString(semaine.fin());
  				}
  				// ajouter (...) si la liste est trop grandes
  				if (semainesUniques.count() > MSG_VALIDATION_PREVISIONNEL_COUNT_MAX_SEMAINE_DISPLAYED) {
  					htmlTextValidationPlanningPrevisionnelImpossible += "<br/>&nbsp;&raquo;&nbsp; (...)";
  				}
   				return false;
  			}
  			
  			// verification sur l'identite de l'agent connecte
  			if (verifDemandeur && !cngUserInfo().isAllowedModifyPlanning(lePlanning().affectationAnnuelle())) {
  				// pas de message particulier ici
  				return false;
  			}
  			
  			return true;
  		}
  		
  	}
  	return false;
  }
  
  public boolean isValidationPlanningPrevisionnelPossible() {
    return isValidationPlanningPrevisionnelPossible(true);
  }
  
  /**
   * Est-ce autorise d'annuler la demande de validation du planning prev
   */
  private boolean isDevalidationPlanningPrevisionnelPossible(boolean verifDemandeur) {
  	boolean isDevalidationPlanningPrevisionnelPossible = false;
  	if (ConstsMenu.MENU_PERSO_PLA_PREV.equals(menuSelectionne())) {
  	  EOAffectationAnnuelle affectationAnnuelle = lePlanning().affectationAnnuelle();
  	  // on essaye de trouver l'alerte associee a cette demande
  	  EOAlerte alertePPrev = alerteBus().findAlertePPrevForPlanning(lePlanning());
  	  if (alertePPrev != null &&
  	      lePlanning().isEnCoursDeValidation() &&
  	      lePlanning().semainesLibres().count()==0 &&
  	      affectationAnnuelle.horaires().count()>0 &&
  	      !lePlanning().limiteCongesDepassee() &&
  	      !lePlanning().limiteSemainesHautesDepassee()&& 
          (verifDemandeur ? cngUserInfo().isAllowedModifyPlanning(affectationAnnuelle) : true)) {
  	    isDevalidationPlanningPrevisionnelPossible = true;
  	  }
  	}
  	return isDevalidationPlanningPrevisionnelPossible;
  }
  
  public boolean isDevalidationPlanningPrevisionnelPossible() {
    return isDevalidationPlanningPrevisionnelPossible(true);
  }
  
  public WOComponent validerPlanningPrevisionnel() throws Throwable {
  	EOAffectationAnnuelle recAffAnn = lePlanning().affectationAnnuelle();
  	EOAlerte alerte = EOAlerte.newEOAlerteInContext(null, edc, lePlanning());
  	alerte.setAffectationAnnuelleRelationship(recAffAnn);
  	// on precise l'annee universitaire
  	alerte.setLibelle(
  			ConstsAlerte.ALERT_PREFIX_LIBELLE_VALID_P_PREV + " " +
  			DateCtrlConges.anneeUnivForDate(lePlanning().affectationAnnuelle().dateDebutAnnee()));
  	alerte.insertInEditingContext(edc);
  	// Changement du status du planning ==> Planning en cours de validation
  	recAffAnn.setStatusPlanning(Planning.PLANNING_STATUT_EN_COURS_DE_VALIDATION);
  	// si demande par delegation, on enregistre le demandeur
  	if (!laSession.isIndividuConnecte()) {
  		recAffAnn.setToIndividuDemandeurRelationship(laSession.individuConnecte());
  	}
  	try {
  	  alerte.sendMailsNouvelleAlerte(laSession);
  	} catch (Exception e) {
  	  txtErreur = e.getMessage();
  	  edc.revert();
  	  return null;
  	}
    UtilDb.save(edc, true);
  	LRLog.log(recAffAnn.individu().nomComplet() + " : demande validation planning previsionnel" + logSuffixDelegation());
   	setMenuSelectionne(ConstsMenu.MENU_PERSO_PLA_PREV);
  	return null;
  }
  
  
  /**
   * Faut-il afficher la popup avec le message d'information
   * sur un depassement (conges / semaines hautes)
   */
  public boolean shouldDisplayPlanningExceedAlert() {
    return !StringCtrl.isEmpty(strPlanningExceedAlert());
  }
  
  /**
   * Le message de depassement
   */
  public String strPlanningExceedAlert() {
    StringBuffer strBuf = new StringBuffer();
    if (!lePlanning().isPTest()) {
      // limite des 390h00
      if (lePlanning().limiteCongesDepassee() && (
          ( lePlanning().isPPrev() && ConstsMenu.MENU_PERSO_PLA_PREV.equals(menuSelectionne()) ) ||
          ( lePlanning().isPReel() && ConstsMenu.MENU_PERSO_PLA_REEL.equals(menuSelectionne()) ) )
      )
        strBuf.append(lePlanning().limiteCongesDepasseeMsg());
      // 8 semaines hautes
      if (lePlanning().limiteSemainesHautesDepassee() && ( 
          ( lePlanning().isPPrev() && ConstsMenu.MENU_PERSO_PLA_PREV.equals(menuSelectionne()) ) ||
          ( lePlanning().isPReel() && ConstsMenu.MENU_PERSO_PLA_REEL.equals(menuSelectionne()) ) )
      )
        strBuf.append(lePlanning().limiteSemainesHautesDepasseeMsg());
  	}
  	return strBuf.toString();
  }
  
  /**
   * Faut-il afficher la popup avec le message d'information
   * sur l'etat du planning
   */
  public boolean shouldDisplayPlanningStateAlert() {
    return !StringCtrl.isEmpty(htmlPlanningStateAlert());
  }
  
  /**
   * code javascript qui affiche une boite de dialogue d'info sur l'etat
   * (1 fois seulement par planning, et pas pour le planning valide)
   */
  public String htmlPlanningStateAlert() {
    StringBuffer bufStr = new StringBuffer();
    if (planningHasChanged) {
      if (lePlanning().isNonValide())				
        bufStr.append(prefixMsg()).append(HTML_MSG_PLANNING_NON_VALIDE);
      else if (lePlanning().isEnCoursDeModification())	
        bufStr.append(prefixMsg()).append(HTML_MSG_PLANNING_EN_COURS_DE_MODIFICATION);
      else if (lePlanning().isEnCoursDeValidation())
        bufStr.append(prefixMsg()).append(HTML_MSG_PLANNING_EN_COURS_DE_VALIDATION);
      // ajout de la mention 'cliquer pour fermer'
      if (bufStr.length() > 0)
        bufStr.append(CRI_POPUP_SUFFIX);
  	}
  	return bufStr.toString(); 	
  }
  
  /**
   * info bulle selon l'etat du planning en passant
   * sur le point d'interrogation
   */
  public String htmlTextInfoPlanning() {
  	StringBuffer sb = new StringBuffer(prefixMsg());
  	if (lePlanning().isPTest()) 				
  		sb.append(HTML_MSG_PLANNING_DE_TEST);
  	else if (lePlanning().isNonValide())			
  		sb.append(HTML_MSG_PLANNING_NON_VALIDE);
  	else if (lePlanning().isValide())			
  		sb.append(HTML_MSG_PLANNING_VALIDE).append(suffixMsgHistValVis());
  	else if (lePlanning().isEnCoursDeModification())	
  		sb.append(HTML_MSG_PLANNING_EN_COURS_DE_MODIFICATION);
  	else if (lePlanning().isEnCoursDeValidation())		
  		sb.append(HTML_MSG_PLANNING_EN_COURS_DE_VALIDATION);
  	return "<center>"+sb.toString()+"</center>";
  }
  
  /**
   * mettre "votre planning" ou "le planning de machin" avant le message
   * @return
   */
  private String prefixMsg() {
    if (laSession.isIndividuConnecte())
      return "Votre planning ";
    else
      return "Le planning de " + 
        laSession.selectedAffAnnPerso().individu().nomComplet() + " ";
  }
  
  /**
   * Les informations relatives au visa / validation du planning
   */
  private String suffixMsgHistValVis() {
  	StringBuffer str = new StringBuffer();
  	try {
    	EORepartValidation repartValidation = lePlanning().affectationAnnuelle().repartValidation();
    	if (repartValidation != null) {
    		str.append("<br/>&raquo; valid&eacute; par : <b>").append(repartValidation.individu().nomComplet()).append("</b>");
    	}
    	EORepartValidation repartVisa = lePlanning().affectationAnnuelle().repartVisa();
    	if (repartVisa != null) {
    		str.append("<br/>&raquo; vis&eacute; par : <b>").append(repartVisa.individu().nomComplet()).append("</b>");
    	}
    	if (str.length() > 0) {
    		str.insert(0, "<br/><br/><i><u>Validations</u>");
    		str.append("</i>");
    	}
  	} catch (Exception e) {
  	}
  	return str.toString();
  	
  }
  
  /**
   * L'affichage du planning dans la popup : <anneeUniv> : <structure>
   */
  public String displayString() {
    String displayString = null;
    displayString = affAnn.annee()+" : "+(affAnn.structure() != null ? affAnn.structure().libelleCourt() : "<erreur>");
    return displayString;
  }
  
  // BOUTONS DE L'ADMINISTRATEUR
  
  /**
   * Passer l'etat du planning a VALIDE. Si le planning etait en cours 
   * de validation (pas de reel), alors on recopie
   * @throws Throwable 
   */
  public WOComponent adminValider() throws Throwable {
    /*String prevType = lePlanning().type();
    // on copie le prev vers le reel si le reel n'existe pas
    lePlanning().setType(Planning.REEL);
    boolean recopiePrev = !lePlanning().isToutesLesSemainesActivesAssociees();
    lePlanning().setType(prevType);
    if (recopiePrev) {      
      planningBus().dupliquerPlanning(
      		lePlanning().affectationAnnuelle(), Planning.PREV, Planning.REEL, false);
      // recharger le planning
      lePlanning().forceReloadPlanning(Planning.REEL);
    }
    lePlanning().affectationAnnuelle().setStatusPlanning(Planning.PLANNING_STATUT_VALIDE);         
    */
  	
    // enregistrement du valideur de planning
    EORepartValidation.newRepartValidation(
    		edc, cngUserInfo(), affAnn, null, EORepartValidation.FLAG_VALIDATION_VALUE);
    if (lePlanning().isNonValide()) {
      planningBus().dupliquerPlanning(
      		lePlanning().affectationAnnuelle(), Planning.PREV, Planning.REEL, false);
      // recharger le planning
      lePlanning().forceReloadPlanning(Planning.REEL);
  	}
    lePlanning().affectationAnnuelle().setStatusPlanning(Planning.PLANNING_STATUT_VALIDE);         
     	
    // on supprime l'eventuelle alerte associee
    EOAlerte alerte =   alerteBus().findAlertePPrevForPlanning(lePlanning());
    if (alerte == null)
      alerte =   alerteBus().findAlertePReelForPlanning(lePlanning());
    if (alerte != null)
      edc.deleteObject(alerte);
    UtilDb.save(edc, true);
    try {
      StringBuffer logBuffer = new StringBuffer(laSession.cngUserInfo().nomEtPrenom());
      logBuffer.insert(0, "ADMIN : ");
      logBuffer.append(" valide ");    
      logBuffer.append(" (").append(lePlanning().affectationAnnuelle().individu().nomComplet()).append("/");
      logBuffer.append(lePlanning().affectationAnnuelle().structure().libelleCourt()).append("/");
      logBuffer.append(lePlanning().affectationAnnuelle().annee()).append(")");
      LRLog.log(logBuffer.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * remise a zero du plannign 
   * @return
   */
  public WOComponent adminRazPlanning() {
    StringBuffer sb = new StringBuffer();
    sb.append("<center><br>Etes vous sur de vouloir remettre &agrave; z&eacute;ro<br><br>").
    append("le planning en cours : ").append(lePlanning().affectationAnnuelle().toString()).
    append("<br><br>Cette op&eacute;ration va effacer TOUTES les donn&eacute;es pour<BR>").
    append("l'ann&eacute;e universitaire selectionn&eacute;e (cong&eacute; et associations ) ").
    append("!!<br><br>Vous etes <b>VRAIMENT</b> sur ?</center>");
    return CRIAlertPage.newAlertPageWithResponder(this, "RAZ de planning",
        sb.toString(),
        "Confirmer", "Annuler", null, CRIAlertPage.INFO,
        new AskAdminRazPlanningResponder(this));  }
  /**
   * Ecran de Demande de remise a zero du planning
   * 
   * @author Cyril TARADE <cyril.tarade at univ-lr.fr>
   */
  private class AskAdminRazPlanningResponder implements CRIAlertResponder {
    private WOComponent caller;
    public AskAdminRazPlanningResponder(WOComponent aCaller) {
      caller = aCaller;
    }

    public WOComponent respondToButton(int buttonNo) {
      if (buttonNo == 1) {
        try {
					planningBus().razPlanning(lePlanning());
				} catch (Throwable e1) {
					e1.printStackTrace();
				}
        try {
          StringBuffer logBuffer = new StringBuffer(laSession.cngUserInfo().nomEtPrenom());
          logBuffer.insert(0, "ADMIN : ");
          logBuffer.append(" RAZ Planning ");    
          logBuffer.append(" (").append(lePlanning().affectationAnnuelle().individu().nomComplet()).append("/");
          logBuffer.append(lePlanning().affectationAnnuelle().structure().libelleCourt()).append("/");
          logBuffer.append(lePlanning().affectationAnnuelle().annee()).append(")");
          LRLog.log(logBuffer.toString());
        } catch (Exception e) {
          e.printStackTrace();
        }
        // recharger tous les planning
        lePlanning().forceReloadPlanning("P");
        lePlanning().forceReloadPlanning("R");
        lePlanning().forceReloadPlanning("T");
        lePlanning().forceReloadPlanning("S");
      }
      return caller;
    }
  }

  
  /**
   * dispo du le bouton de validaiton pour un admin
   */
  public boolean showBtnAdminValider() {
    if (laSession.isAdministrateur() && 
        laSession.isIndividuConnecte() == false &&
        lePlanning().isValide() == false &&
        (lePlanning().isPPrev() || lePlanning().isPReel()) &&
        (ConstsMenu.MENU_PERSO_PLA_PREV.equals(menuSelectionne()) || ConstsMenu.MENU_PERSO_PLA_REEL.equals(menuSelectionne())) &&
        (isValidationPlanningPrevisionnelPossible(false) || 
            isPlanningValidable(false) || 
            isDevalidationPlanningPrevisionnelPossible(false) || 
            isAnnulationValidationPReelPossible(false))) {
      return  true;
    }
    return false;
  }
  
  /**
   * dispo du bouton RAZ du planning pour l'admin.
   */
  public boolean showBtnAdminRazPlanning() {
    if (laSession.isAdministrateur() && 
        !laSession.isIndividuConnecte() &&
        (ConstsMenu.MENU_PERSO_PLA_PREV.equals(menuSelectionne()) || ConstsMenu.MENU_PERSO_PLA_REEL.equals(menuSelectionne()))) {
      return true;
    }
    return false;
  }  
  
  /**
   * La couleur HTML associee a l'etat du planning 
   * <code>lePlanning()</code>.
   * @return
   */
  public String getPlanningHtmlColor() {
  	String htmlColor = "#000000";
  	if (lePlanning().isValide()) {
  		htmlColor = HTML_COLOR_PLANNING_VALIDE;
  	} else if (lePlanning().isEnCoursDeModification()) {
  		htmlColor = HTML_COLOR_PLANNING_EN_COURS_DE_MODIFICATION;
  	} else if (lePlanning().isEnCoursDeValidation()) {
  		htmlColor = HTML_COLOR_PLANNING_EN_COURS_DE_VALIDATION;
  	} else if (lePlanning().isNonValide()) {
  		htmlColor = HTML_COLOR_PLANNING_INVALIDE;
  	} else if (lePlanning().isPTest()) {
  		htmlColor = HTML_COLOR_PLANNING_VALIDE;
  	}
  	return htmlColor;
  }
  
  /**
   * La libelle HTML associee a l'etat du planning 
   * <code>lePlanning()</code>.
   * @return
   */
  public String getPlanningHtmlLabel() {
  	String htmlLabel = "STATUT INCONNU";
  	if (lePlanning().isValide()) {
  		htmlLabel = HTML_LABEL_PLANNING_VALIDE;
  	} else if (lePlanning().isEnCoursDeModification()) {
  		htmlLabel = HTML_LABEL_PLANNING_EN_COURS_DE_MODIFICATION;
  	} else if (lePlanning().isEnCoursDeValidation()) {
  		htmlLabel = HTML_LABEL_PLANNING_EN_COURS_DE_VALIDATION;
  	} else if (lePlanning().isNonValide()) {
  		htmlLabel = HTML_LABEL_PLANNING_INVALIDE;
  	} else if (lePlanning().isPTest()) {
  		htmlLabel = HTML_LABEL_PLANNING_VALIDE;
  	}
  	return htmlLabel;
  }
  
  
  // BUS DE DONNEES
  
  private CngAlerteBus alerteBus() {
    return laSession.cngDataCenter().alerteBus();
  }
  
  private CngPlanningBus planningBus() {
    return laSession.cngDataCenter().planningBus();
  }
  
  
  // IMPRESSIONS
  
  /**
   * Impression des horaires de l'agent
   */
  public class PdfBoxHorairesCtrl extends CngPdfBoxCtrl {

		public PdfBoxHorairesCtrl(Class aGenericSixPrintClass, EOEditingContext anEc) {
			super(aGenericSixPrintClass, anEc);
		}

		public NSDictionary buildDico() {
	  	NSMutableDictionary dico = new NSMutableDictionary();
	  	dico.setObjectForKey(
					lePlanning().affectationAnnuelle().annee(), "anneeUniv");
			dico.setObjectForKey(
					lePlanning().affectationAnnuelle().structure().libelleLong(), "service");
			dico.setObjectForKey(
					new NSArray(lePlanning().affectationAnnuelle()), "listAffectationAnnuelle");
			dico.setObjectForKey(
					DateCtrlConges.dateToString(DateCtrlConges.now()), "dateImpression");
			dico.setObjectForKey(
					lePlanning().type(), "typePlanning");
	    // URL du logo
	    dico.setObjectForKey(app.mainLogoUrl(), ConstsPrint.XML_KEY_MAIN_LOGO_URL);
			return dico;
		}
		
		public String fileName() {
			return "Horaires_" + StringCtrl.toBasicString(lePlanning().affectationAnnuelle().individu().nom()) + "_"
			+StringCtrl.toBasicString(lePlanning().affectationAnnuelle().individu().prenom()) + "_" +
  		StringCtrl.replace(DateCtrlConges.anneeUnivForDate(lePlanning().affectationAnnuelle().dateDebutAnnee()), "/", "_");
		}
  }
  
  /** */
  public PdfBoxHorairesCtrl ctrlHoraires() {
  	return new PdfBoxHorairesCtrl(PrintHoraires.class, edc);
  }

	public Planning planningBinding() {
		return lePlanning();
	}
 
  
}