package fr.univlr.cri.conges;
import java.util.Enumeration;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.constantes.ConstsDroit;
import fr.univlr.cri.conges.databus.CngAlerteBus;
import fr.univlr.cri.conges.eos.modele.conges.EOAlerte;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRSort;
import fr.univlr.cri.ycrifwk.utils.UtilDb;

public class PageDemandes 
	extends YCRIWebPage
		implements ConstsDroit {

  // cache des objets EOAlerte provenant de la table Alerte
  private NSArray _lesAlertesAValider, _lesAlertesAViser;
  
  // taille des tableaux pour affichage depuis une autre page.
  public int totalAlertesAValider;
  public int totalAlertesAViser;
  public int totalAlertesValidationDRH;
  public int totalAlertesVisaDRH;
  
  // cache des objets LRRecord provenant de la vue VAlerte
  private NSArray validationAlertOids, visaAlertOids;
  
  public EOAlerte uneAlerte;

  // pointeur vers l'instance de la page du menu, 
  // pour determiner sa visibilite sur les pages
  private Menu menu;
  
  // classement des alertes par date et par demandeur
  private final static LRSort SORT_DATE_ASC 				= LRSort.newSort("dCreation", LRSort.Ascending);
  private final static LRSort SORT_DATE_DESC 				= LRSort.newSort("dCreation", LRSort.Descending);
  private final static LRSort SORT_DEMANDEUR_ASC 		= LRSort.newSort("affectationAnnuelle.individu.nomComplet", LRSort.Ascending);
  private final static LRSort SORT_DEMANDEUR_DESC 	= LRSort.newSort("affectationAnnuelle.individu.nomComplet", LRSort.Descending);
  private LRSort sortAlertesAValider = SORT_DATE_ASC;
  private LRSort sortAlertesAViser = SORT_DATE_DESC;
  
  // l'ancre HTML a atteindre au prochain rechargement
  private String targetPosition;
  
  // la liste des années universitaires concernées
  public NSArray anneeUnivList;
  public String anneeUnivItem;
  public String anneeUnivSelected;
  public boolean isAfficherAnneeUnivList;
  
  public PageDemandes(WOContext context) {
    super(context);
    // chercher les alertes si seule cette page est disponible
    menu = (Menu) laSession.getSavedPageWithName(Menu.class.getName());
    if (menu.voitExactement1Service()) {
      laSession.doRefreshViewAlerts();
    }
    // on recupere les enregistrements VAlerte concernes par cet ecran
    doRefreshAlertOids();
    // liste des années universitaires
    anneeUnivList = alerteBus().getAnneeUnivListForAlerts(
    		lesAlertesAValider().arrayByAddingObjectsFromArray(lesAlertesAViser()));
    // on selectionne l'actuelle par defaut
    if (anneeUnivList.count() > 0) {
    	String anneeUnivCurr = DateCtrlConges.anneeUnivForDate(DateCtrlConges.now());
    	if (anneeUnivList.containsObject(anneeUnivCurr)) {
    		anneeUnivSelected = anneeUnivCurr;
    	} else {
    		anneeUnivSelected = (String) anneeUnivList.lastObject();
    	}
    } else {
    	anneeUnivSelected = null;
    }
    // on affiche la liste déroulante s'il y a plus d'une seul année
  	isAfficherAnneeUnivList = false;
    if (anneeUnivList.count() > 1) {
    	isAfficherAnneeUnivList = true;
    }
  }
  
  private void doRefreshAlertOids() {
    validationAlertOids = alerteBus().getValidationAlertOids(
        edc, laSession.recordsVAlerte());
    totalAlertesAValider = validationAlertOids.count();
    visaAlertOids = alerteBus().getVisaAlertOids(
        edc, laSession.recordsVAlerte());
    totalAlertesAViser = visaAlertOids.count();
    totalAlertesValidationDRH = alerteBus().countAlertDRH(validationAlertOids);
    totalAlertesVisaDRH = alerteBus().countAlertDRH(visaAlertOids);
  }

  private void doRefreshAlerts() {
    _lesAlertesAValider = _lesAlertesAViser = null;
  }
  
  public void appendToResponse(WOResponse response, WOContext context) {
    super.appendToResponse(response, context);
    laSession.setPageEnCours(this.getClass().getName());
    doRefreshAlertOids();
    if (!StringCtrl.isEmpty(targetPosition)) {
    	addTextJScriptAtEnd(response, "document.location='#"+targetPosition+"'");
    }
  }

  
  public WOComponent detaillerLaDemande() {
    WOComponent nextPage = laSession.getSavedPageWithName(
        PageDetailDemande.class.getName());
    if (uneAlerte.isValidationPrev())
      laSession.setSelectedAffAnnPerso(uneAlerte.affectationAnnuelle());
    else
      laSession.setSelectedAffAnnPerso(uneAlerte.affectationAnnuelle());
    nextPage.reset();
    ((PageDetailDemande) nextPage).setLaDemande(uneAlerte);
    if (lesAlertesAViser().containsObject(uneAlerte))
      ((PageDetailDemande) nextPage).setIsPourViser(true);
      else
      	((PageDetailDemande) nextPage).setIsPourViser(false);

    return nextPage;
  }

  public boolean isVisee() {
    boolean isVisee = false;
    if (uneAlerte.occupation() != null) {
      isVisee = uneAlerte.occupation().isVisee();
    } else {
      isVisee = uneAlerte.affectationAnnuelle().isPlanningVise();
    }
    return isVisee;
  }

  public boolean isAcceptee()   {    
  	return uneAlerte.isAcceptee(); 
  }
 
  public boolean isRefusee()    {    
  	return uneAlerte.isRefusee(); 
  }
  
  
  /**
   * Determiner si l'alerte concernant une occupation
   * ou un planning modifie / cree par delegation
   * @return
   */
  public boolean isAlerteDelegation() {
  	return 
  		(uneAlerte.occupation() == null && uneAlerte.affectationAnnuelle().toIndividuDemandeur() != null) ||
  		(uneAlerte.occupation() != null && uneAlerte.occupation().toIndividuDemandeur() != null);
  }

  /**
   * L'enregistrement <code>EOIndividu</code> de la personne
   * deleguee qui a effectue la demande
   * @return
   */
  private EOIndividu individuDelegue() {
  	EOIndividu delegue = null;
  	if (isAlerteDelegation()) {
  		if (uneAlerte.occupation() == null) {
  			delegue = uneAlerte.affectationAnnuelle().toIndividuDemandeur();
  		} else {
  			delegue = uneAlerte.occupation().toIndividuDemandeur();
  		}
  	}
  	return delegue;
  }
  
  public String getTipDelegue() {
  	return "Par d&eacute;l&eacute;gation :<br><b>"+individuDelegue().nomComplet()+"</b>";
  }
  

  /**
   * les alertes a valider
   */
  private NSArray lesAlertesAValider() {
    if (_lesAlertesAValider == null) {
      _lesAlertesAValider = alerteBus().fetchEOAlerteFromOids(edc, validationAlertOids);
      _lesAlertesAValider = EOSortOrdering.sortedArrayUsingKeyOrderArray(_lesAlertesAValider, sortAlertesAValider);
    }
    return _lesAlertesAValider;
  }

  /**
   * Pour la WORepetition, les alertes pour l'annee selectionnée
   * @return
   */
  public NSArray lesAlertesAValiderPourAnnee() {
  	NSArray result = lesAlertesAValider();
  	
  	if (anneeUnivSelected != null) {
  		result = EOQualifier.filteredArrayWithQualifier(
    			result, CRIDataBus.newCondition(
    					EOAlerte.AFFECTATION_ANNUELLE_KEY + "." + EOAffectationAnnuelle.ANNEE_KEY + "=%@", new NSArray(anneeUnivSelected)));
  	} else {
  		///
  	}
  	
  	return result;
  }
  
  /**
   * les alertes a viser
   */
  public NSArray lesAlertesAViser() {
    if (_lesAlertesAViser == null) {
      _lesAlertesAViser = alerteBus().fetchEOAlerteFromOids(edc, visaAlertOids);
      _lesAlertesAViser = EOSortOrdering.sortedArrayUsingKeyOrderArray(_lesAlertesAViser, sortAlertesAViser);
    }
    return _lesAlertesAViser;
  }


  /**
   * Pour la WORepetition, les alertes pour l'annee selectionnée
   * @return
   */
  public NSArray lesAlertesAViserPourAnnee() {
  	NSArray result = lesAlertesAViser();
  	
  	if (anneeUnivSelected != null) {
  		result = EOQualifier.filteredArrayWithQualifier(
    			result, CRIDataBus.newCondition(
    					EOAlerte.AFFECTATION_ANNUELLE_KEY + "." + EOAffectationAnnuelle.ANNEE_KEY + "=%@", new NSArray(anneeUnivSelected)));
  	}
  	
  	return result;
  }


  /**
   * Enregistrement de toutes operation realisees sur la
   * liste des demandes a valider. Envoi de mail et 
   * mise a jour de la base de donnees.
   * @throws Throwable 
   */
  public WOComponent enregistrerDemandesAValider() throws Throwable {
    NSArray alertesAValider = lesAlertesAValider();
    Enumeration enumAlertes = alertesAValider.objectEnumerator();
    int nbAlertesSupprimees = 0;
    while (enumAlertes.hasMoreElements()) {
      EOAlerte alerte = (EOAlerte) enumAlertes.nextElement();
      if (alerte.isAcceptee()) {
        if (alerteBus().accepteValid(edc, alerte, laSession.cngUserInfo()))
          nbAlertesSupprimees++;
      } else if (alerte.isRefusee())
        if (alerteBus().refuse(edc, alerte, laSession.cngUserInfo(), DROIT_NIVEAU_VALIDATION))
          nbAlertesSupprimees++;
    }

    // refetchage de toutes les alertes
    _lesAlertesAValider = null;
    if (nbAlertesSupprimees > 0) {
      laSession.doRefreshViewAlerts();
      doRefreshAlertOids();
      doRefreshAlerts();
    }
    
    return null;
  }

  /**
   * Enregistrement de toutes operation realisees sur la
   * liste des demandes a viser. Envoi de mail et 
   * mise a jour de la base de donnees.
   */
  public WOComponent enregistrerDemandesAViser() {
    NSArray alertes = lesAlertesAViser();
    int nbAlertesSupprimees = 0;
    Enumeration enumAlertes = alertes.objectEnumerator();
    while (enumAlertes.hasMoreElements()) {
      EOAlerte alerte = (EOAlerte) enumAlertes.nextElement();
      if (alerte.isAcceptee()) {
        if (alerteBus().accepteVisa(edc, alerte, laSession.cngUserInfo()))
          nbAlertesSupprimees++;
      } else if (alerte.isRefusee()) {
        if (alerteBus().refuse(edc, alerte, laSession.cngUserInfo(), DROIT_NIVEAU_VISA))
          nbAlertesSupprimees++;       
      }
    }

    if (nbAlertesSupprimees > 0) {
      laSession.doRefreshViewAlerts();
      doRefreshAlertOids();
      doRefreshAlerts();
    }
    
    return null;
  }

  public WOComponent enregistreEtNeFaitRien() throws Throwable {
    UtilDb.save(edc, true);
    return neFaitRien();
  }

  /**
   * couleur du fond de la cellule en fct de son etat
   * 
   * @return
   */
  public String bgColorDemande() {
    if (isAcceptee())
      return "trDemAcc";
    if (isRefusee())
      return "trDemRef";
    if (isVisee())
      return "trDemVis";
    return "trDemAtt";
  }

  // GESTION DES DECISIONS DIRECTEMENT PAR LA COCHE
  // TODO corriger bug clic sur accepte quand deja refus : rien ne change
  public void setIsAcceptee(boolean value) throws Throwable {
    if (value == true)
      uneAlerte.setFlagReponse("1");
    else
      uneAlerte.setFlagReponse(null);
    UtilDb.save(edc, true);
    ancreAlerte(uneAlerte);
  }

  public void setIsRefusee(boolean value) throws Throwable {
    if (value == true)
      uneAlerte.setFlagReponse("0");
    else
      uneAlerte.setFlagReponse(null);
    UtilDb.save(edc, true);	
    ancreAlerte(uneAlerte);
  }

  public WOComponent suspendreDemande() throws Throwable {
    uneAlerte.setFlagReponse(null);
    UtilDb.save(edc, true);
    ancreAlerte(uneAlerte);
    return null;
  }

  private void ancreAlerte(EOAlerte alerte) {
    targetPosition = Integer.toString(alerte.oidR().intValue());
  }
  
  // -- CLASSEMENT DES ALERTES 
  
  /**
   * Classement des demandes a valider par date ASC
   */
  public WOComponent sortDateDemandesAValider() {
  	sortAlertesAValider = (sortAlertesAValider == SORT_DATE_ASC ? SORT_DATE_DESC : SORT_DATE_ASC);
  	doRefreshAlerts();
  	return null;
  }
  
  /**
   * Classement des demandes a viser par date 
   */
  public WOComponent sortDateDemandesAViser() {
  	sortAlertesAViser = (sortAlertesAViser == SORT_DATE_ASC ? SORT_DATE_DESC : SORT_DATE_ASC);
  	doRefreshAlerts();
  	return null;
  }
  
  /**
   * Classement des demandes a valider par Demandeur 
   */
  public WOComponent sortDemandeurDemandesAValider() {
  	sortAlertesAValider = (sortAlertesAValider == SORT_DEMANDEUR_ASC ? SORT_DEMANDEUR_DESC : SORT_DEMANDEUR_ASC);
  	doRefreshAlerts();
    return null;
  }
  
  /**
   * Classement des demandes a viser par Demandeur 
   */
  public WOComponent sortDemandeurDemandesAViser() {
  	sortAlertesAViser = (sortAlertesAViser == SORT_DEMANDEUR_ASC ? SORT_DEMANDEUR_DESC : SORT_DEMANDEUR_ASC);
  	doRefreshAlerts();
    return null;
  }

  // -- BUS DE DONNEES --
  
  private CngAlerteBus alerteBus() {
    return laSession.cngDataCenter().alerteBus();
  }
}