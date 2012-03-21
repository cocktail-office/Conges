package fr.univlr.cri.conges;


import com.webobjects.appserver.*;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.objects.Responsabilite;
import fr.univlr.cri.conges.utils.DateCtrlConges;

/**
 * Affichage des services disponibles a l'agent connect�.
 * Parmi ces services, il est possible d'acc�der aux interfaces des demandes,
 * pour les ayant droits.
 * 
 * @author ctarade
 */
public class PageAccueil extends YCRIWebPage {

  private Responsabilite structuresDispoParNiveauItem;
  public NSArray structuresDispoParNiveauSuperieurAUn;

  private WODisplayGroup displayGroupStructuresDispoParNiveau; // pour affichage CRINavigationBar
  private final static int NB_ITEMS_PAR_PAGE = 10;
 
  public NSArray searchList;
  public EOAffectationAnnuelle searchItem;
  public String searchString;
  
  public PageAccueil(WOContext context) {
    super(context);
  }
    
  public void appendToResponse(WOResponse response, WOContext context) {
    super.appendToResponse(response, context);
    laSession.setPageEnCours(this.getClass().getName());
  }
    
  public void awake() {
    // on refresh toutes les alertes a chaque affichage
    laSession.doRefreshViewAlerts();
    super.awake();
  }
    
  public WOComponent afficherPlanningPerso() {
    PagePlannings nextPage = (PagePlannings) laSession.getSavedPageWithName(PagePlannings.class.getName());
    nextPage.initialiserVisualisation();
    return nextPage;
  }
	
  public WOComponent detaillerService() { 
    laSession.setStructuresDispoParNiveauSelection(structuresDispoParNiveauItem);
    PageService nextPage = (PageService) pageWithName(PageService.class.getName());
    return nextPage;
  }
	
  private PageDemandes _pageDemande;
    
  /**
   * Pour chaque service, on a acces a tous les demandes.
   * Cette methode permet de recuperer les quantites contenues
   * dans cette sous page ainsi que de faire le lien vers
   * son affichage.
   */
  public PageDemandes pageDemande() {
    if (_pageDemande == null) {
      laSession.setStructuresDispoParNiveauSelection(structuresDispoParNiveauItem);
      _pageDemande = (PageDemandes) pageWithName(PageDemandes.class.getName());
    }
    return _pageDemande;
  }
	
  /**
   * @return
   */
  private NSArray structuresDispoParNiveauSuperieurOuEgalAUn() {
    structuresDispoParNiveauSuperieurAUn = laSession.structuresDispoParNiveau();
    EOQualifier qual = EOQualifier.qualifierWithQualifierFormat("niveauMax>%@",new NSArray(new Integer(0)));
    structuresDispoParNiveauSuperieurAUn = EOQualifier.filteredArrayWithQualifier(structuresDispoParNiveauSuperieurAUn,qual);
    NSArray orderings = new NSArray(EOSortOrdering.sortOrderingWithKey("structure.libelleLong", EOSortOrdering.CompareAscending));
    structuresDispoParNiveauSuperieurAUn = EOSortOrdering.sortedArrayUsingKeyOrderArray(structuresDispoParNiveauSuperieurAUn, orderings);
    return structuresDispoParNiveauSuperieurAUn;
  }


  /**
   * pour affichage CRINavigationBar
   * @return
   */
  public WODisplayGroup displayGroupStructuresDispoParNiveau() {
    if (displayGroupStructuresDispoParNiveau == null) {
      displayGroupStructuresDispoParNiveau = new WODisplayGroup();
      displayGroupStructuresDispoParNiveau.setNumberOfObjectsPerBatch(NB_ITEMS_PAR_PAGE);
      // suppression des structures doublons
      NSArray cStructureList = new NSArray();
      NSArray responsabiliteList = new NSArray();
      for (int i=0; i<structuresDispoParNiveauSuperieurOuEgalAUn().count(); i++) {
      	Responsabilite responsabilite = (Responsabilite) structuresDispoParNiveauSuperieurOuEgalAUn().objectAtIndex(i);
      	String cStructure = responsabilite.getStructure().cStructure();
      	if (!cStructureList.containsObject(cStructure)) {
      		responsabiliteList = responsabiliteList.arrayByAddingObject(responsabilite);
      		cStructureList = cStructureList.arrayByAddingObject(cStructure);
      	}
      }
      displayGroupStructuresDispoParNiveau.setObjectArray(responsabiliteList);
    }
    return displayGroupStructuresDispoParNiveau;
  }

  public boolean isAffichageSurPlusieursPages() {
    return displayGroupStructuresDispoParNiveau().allObjects().count() > NB_ITEMS_PAR_PAGE;
  }
	
  /**
   * @param array
   */
  public void setStructuresDispoParNiveauSuperieurAUn(NSArray array) {
    structuresDispoParNiveauSuperieurAUn = array;
  }
	
  public String displayStructuresDispoParNiveauItem() {  
    return structuresDispoParNiveauItem.getStructure().display();
  }

  // SETTER / GETTER
    
  public Responsabilite getStructuresDispoParNiveauItem() {
    return structuresDispoParNiveauItem;
  }

  public void setStructuresDispoParNiveauItem(Responsabilite value) {
    structuresDispoParNiveauItem = value;
    // raz du cache de la page 
    _pageDemande = null;
  }

  
  // recherche
  
  /** 
   * Lancer la recherche
   */
  public WOComponent doSearch() {
  	// restriction sur les services de recherche
  	NSArray strutureList = null;
  	if (!cngUserInfo().isAdmin()) {
  		strutureList = (NSArray) displayGroupStructuresDispoParNiveau().allObjects().valueForKey(Responsabilite.STRUCTURE_KEY);
  	}
  	// rechercher
  	searchList = EOAffectationAnnuelle.findRecordsLike(
  			edc, 
  			searchString, 
  			DateCtrlConges.anneeUnivForDate(laSession.dateRef()),
  			strutureList);
  	// restreindre par rapport aux droits de l'individu connecté
  	searchList = EOAffectationAnnuelle.filteredAffectationsForResponsabilites(
  			searchList, laSession.structuresDispoParNiveau());
  	return null;
  }
  
  
  /**
   * Selection d'un planning trouv� => affichage 
   * @return
   */
  public WOComponent doSearchItemSelection() {
    //laSession.preparerPlanningForIndividu(searchItem.individu());
    laSession.preparerPlanningForAffectationAnnuelle(searchItem);
  	PagePlannings nextPage = (PagePlannings) laSession.getSavedPageWithName(PagePlannings.class.getName());
    nextPage.initialiserVisualisation();
    return nextPage;
  }
}