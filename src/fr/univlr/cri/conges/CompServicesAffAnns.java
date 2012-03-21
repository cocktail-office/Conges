package fr.univlr.cri.conges;

import com.webobjects.eocontrol.EOOrQualifier;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableDictionary;

import fr.univlr.cri.conges.eos.modele.conges.EOStructureAutorisee;
import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOVIndividuConges;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.util.ArrayCtrl;
import fr.univlr.cri.webapp.CRIDataBus;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.appserver.WOResponse;

/**
 * Composant de selection des plannings par service et par annee.
 * 
 * @author ctarade
 */
public class CompServicesAffAnns 
	extends YCRIWebPage {
	
  // bindings
	
  // faut-il n'afficher que les services autorise (oui par defaut)
  public Boolean isOnlyServicesAutorise = Boolean.TRUE;
  
  // popup des annees univ
  private NSArray anneeUnivList;
  public String anneeUnivItem;
  public String anneeUnivSelected;
  private String anneeUnivSelectedPrev;
  
  // les display groups
  public WODisplayGroup composantesDisplayGroup;
  public WODisplayGroup servicesDisplayGroup;
  public WODisplayGroup affAnnsDisplayGroup;
  
  // temoins de refresh oui non
  public boolean shouldRefreshComposanteDG = true;
  public boolean shouldRefreshAffAnnsDG = true;
  public boolean shouldRefreshServiceDG = true;
  
  // 
  public EOStructure composanteItem;
  public EOStructure composanteSelected;
  
  // 
  public EOStructure serviceItem;
  public EOStructure serviceSelected;
  
  // 
  public EOAffectationAnnuelle affAnnItem;
  public EOAffectationAnnuelle affAnnSelected;
	
  // recherche
  public NSArray searchList;
  public EOVIndividuConges searchItem;
  public String searchString;
  
	public CompServicesAffAnns(WOContext context) {
		super(context);
    // selection par defaut de l'annee universitaire en cours
    setAnneeUnivSelected(DateCtrlConges.anneeUnivForDate(laSession.dateRef()));
	}
	
  
  public void appendToResponse(WOResponse aResponse, WOContext aContext) {
  	if (shouldRefreshComposanteDG) {
  		refreshComposanteDG();
  		shouldRefreshComposanteDG = false;
  		shouldRefreshServiceDG = true;
  	}
    if (shouldRefreshServiceDG) {
      refreshServiceDG();
      shouldRefreshServiceDG = false;
      shouldRefreshAffAnnsDG = true;
    }
    if (shouldRefreshAffAnnsDG) {
      refreshAffAnnDG();
      shouldRefreshAffAnnsDG = false;
    }
    super.appendToResponse(aResponse, aContext);
  }
  
  // navigation
  
  /**
   * Selection d'une composante
   */
  public WOComponent doComposanteSelection() {
  	composanteSelected = composanteItem;
  	serviceSelected = null;
  	affAnnSelected = null;
   	shouldRefreshServiceDG = true;
   	shouldRefreshAffAnnsDG = true;
  	// raz de la liste suite a selection
  	searchList = null;
  	return null;
  }
  
  /**
   * Selection d'un service
   */
  public WOComponent doServiceSelection() {
   	shouldRefreshAffAnnsDG = true;
   	serviceSelected = serviceItem;
  	affAnnSelected = null;
  	// raz de la liste suite a selection
  	searchList = null;
  	return null;
  }
  
  /**
   * Selection d'un planning
   */
  public WOComponent doAffAnnSelection() {
  	affAnnSelected = affAnnItem;
  	// raz de la liste suite a selection
  	searchList = null;
  	return null;
  }
  
  /** 
   * Lancer la recherche
   */
  public WOComponent doSearch() {
  	searchList = EOVIndividuConges.findRecordsLike(
  			edc, 
  			searchString, 
  			anneeUnivSelected,
  			cngUserInfo().isAdmin() ? null : servicesDisplayGroup.displayedObjects(), true, true);
  	return null;
  }
  
  /**
   * Selection d'un planning issu de la recherche
   * @return
   */
  public WOComponent doSearchItemSelectionAffAnn() {
  	composanteSelected = searchItem.toStructure().toComposante();
  	serviceSelected = searchItem.toStructure();
  	affAnnSelected = searchItem.toAffectationAnnuelle();
  	// on recharge la liste des services si on a changé de composante
  	if (!isTheComposanteSelected()) {
  		shouldRefreshComposanteDG = true;
  	}
  	// on recharge la liste de plannings si on a changé de service
  	if (!isTheServiceSelected()) {
  		shouldRefreshAffAnnsDG = true;
  	}
  	// raz de la liste suite a selection
  	searchList = null;
  	return null;
  }
  
  /**
   * Selection d'un service issu de la recherche
   * @return
   */
  public WOComponent doSearchItemSelectionService() {
  	composanteSelected = searchItem.toStructure().toComposante();
  	serviceSelected = searchItem.toStructure();
  	// on recharge la liste des services si on a changé de composante
  	if (!isTheComposanteSelected()) {
  		shouldRefreshComposanteDG = true;
  	}
  	// on recharge la liste de plannings si on a changé de service
  	if (!isTheServiceSelected()) {
  		shouldRefreshAffAnnsDG = true;
  	}
  	// raz de la liste suite a selection
  	searchList = null;
  	return null;
  }
  
  /**
   * Selection d'une composante issue de la recherche
   * @return
   */
  public WOComponent doSearchItemSelectionComposante() {
  	composanteSelected = searchItem.toStructure().toComposante();
  	// on recharge la liste des services si on a changé de composante
  	if (!isTheComposanteSelected()) {
  		shouldRefreshComposanteDG = true;
  	}
  	// raz de la liste suite a selection
  	searchList = null;
  	return null;
  }
  
  /**
   * Selection d'un planning issu de la recherche
   * @return
   */
  public WOComponent doSearchItemSelection() {
  	composanteSelected = searchItem.toStructure().toComposante();
  	serviceSelected = searchItem.toStructure();
  	affAnnSelected = searchItem.toAffectationAnnuelle();
  	// on recharge la liste des services si on a changé de composante
  	if (!isTheComposanteSelected()) {
  		shouldRefreshComposanteDG = true;
  	}
  	// on recharge la liste de plannings si on a changé de service
  	if (!isTheServiceSelected()) {
  		shouldRefreshAffAnnsDG = true;
  	}
  	// raz de la liste suite a selection
  	searchList = null;
  	return null;
  }
  
  // getters

  public NSArray getAnneeUnivList() {
    if (anneeUnivList == null) {
      anneeUnivList = new NSArray();
      // liste des annees ou il y a eu des services autorises
      anneeUnivList = EOStructureAutorisee.findAllDebutAnneeUnivStringInContext(edc);
      anneeUnivList = anneeUnivList.arrayByAddingObject(DateCtrlConges.anneeUnivForDate(laSession.dateRef()));
      anneeUnivList = anneeUnivList.arrayByAddingObject(DateCtrlConges.anneeUnivForDate(
          laSession.dateRef().timestampByAddingGregorianUnits(1, 0, 0, 0, 0, 0)));
      // on vire les doubles au cas ou
      anneeUnivList = ArrayCtrl.removeDoublons(anneeUnivList);
    }
    return anneeUnivList;
  }

  // setters
  
  /**
   * Lors du changement de l'annee universitaire, on 
   * reinitialise la liste des services et de la selection de planning
   */
  public void setAnneeUnivSelected(String value) {
    anneeUnivSelectedPrev = anneeUnivSelected;
    anneeUnivSelected = value;
    if (anneeUnivSelectedPrev != null && !anneeUnivSelectedPrev.equals(anneeUnivSelected)) {
      shouldRefreshServiceDG = true;    
    	// raz de la selection
    	affAnnSelected = null;
    }
  }
    
  // refresh des display group
  
  private void refreshComposanteDG() {
    // initialisation du displaygroup des composantes
    composantesDisplayGroup.fetch();
    // qualifier additionnel restrictif pour les administrateurs de composante
    if (laSession.cngUserInfo().isAdmComposante()) {
    	NSArray arrayQuals = new NSArray();
    	for (int i = 0; i < laSession.cngUserInfo().getListAdmComposante().count(); i++) {
    		EOStructure recComposante = (EOStructure) laSession.cngUserInfo().getListAdmComposante().objectAtIndex(i);
    		arrayQuals = arrayQuals.arrayByAddingObject(CRIDataBus.newCondition(
    				EOStructure.C_STRUCTURE_KEY+"=%@", new NSArray(recComposante.cStructure())));
			}
    	EOQualifier qualRestrict = new EOOrQualifier(arrayQuals);
    	composantesDisplayGroup.setQualifier(qualRestrict);
    	composantesDisplayGroup.updateDisplayedObjects();
    }
    // selection de la premiere composante
    composantesDisplayGroup.selectsFirstObjectAfterFetch();
    // si une seule composante, on la selectionne d'emblée
    if (composantesDisplayGroup.displayedObjects().count() == 1) {
    	composanteItem = (EOStructure) composantesDisplayGroup.selectedObject();
    	doComposanteSelection();
    }
  }
  
  private void refreshServiceDG() {
  	if (composanteSelected != null) {
      // initialisation du displaygroup des services
      NSMutableDictionary dicoService = new NSMutableDictionary();
      dicoService.setObjectForKey(
         isOnlyServicesAutorise == Boolean.TRUE ?"1":"0", "isStructureAutorisee");
      dicoService.setObjectForKey(anneeUnivSelected, "annee");
      // specifier la composante
      dicoService.setObjectForKey(composanteSelected.cStructure(), "cStructureComposante");
      servicesDisplayGroup.queryBindings().setDictionary(dicoService.immutableClone());
      servicesDisplayGroup.fetch();
      // qualifier additionnel restrictif pour les administrateurs de composante
      if (laSession.cngUserInfo().isAdmComposante()) {
      	NSArray arrayQuals = new NSArray();
      	for (int i = 0; i < laSession.cngUserInfo().getListAdmComposante().count(); i++) {
      		EOStructure recComposante = (EOStructure) laSession.cngUserInfo().getListAdmComposante().objectAtIndex(i);
      		arrayQuals = arrayQuals.arrayByAddingObject(CRIDataBus.newCondition(
      				EOStructure.C_STRUCTURE_COMPOSANTE_KEY + "=%@", new NSArray(recComposante.cStructure())));
  			}
      	EOQualifier qualRestrict = new EOOrQualifier(arrayQuals);
      	servicesDisplayGroup.setQualifier(qualRestrict);
      	servicesDisplayGroup.updateDisplayedObjects();
      }
      // selection du premier service
      servicesDisplayGroup.selectsFirstObjectAfterFetch();
      // si un seule service, on le selectionne d'emblée
      if (servicesDisplayGroup.displayedObjects().count() == 1) {
      	serviceItem = (EOStructure) servicesDisplayGroup.selectedObject();
      	// on memorise la selection du planning, car si le service
      	// est unique, la methode doServiceSelection() efface celle ci
      	EOAffectationAnnuelle prevAffAnnSelected = affAnnSelected;
      	doServiceSelection();
      	affAnnSelected = prevAffAnnSelected;
      }
  	} else {
  		servicesDisplayGroup.setObjectArray(new NSArray());
  	}
  }
  
  
  /**
   * forcer les donnees du DisplayGroup a etre rechargees.
   */
  private void refreshAffAnnDG() {
    if (serviceSelected != null) {
      affAnnsDisplayGroup.queryBindings().setObjectForKey(serviceSelected, "service");
      affAnnsDisplayGroup.queryBindings().setObjectForKey(anneeUnivSelected, "annee");      
      affAnnsDisplayGroup.queryBindings().setObjectForKey(
      		DateCtrlConges.dateDebutAnneePourStrPeriodeAnnee(anneeUnivSelected), "dateDebut");
      affAnnsDisplayGroup.queryBindings().setObjectForKey(
      		DateCtrlConges.dateDebutAnneePourStrPeriodeAnnee(anneeUnivSelected).timestampByAddingGregorianUnits(1,0,-1,0,0,0), 
      		"dateFin");
      affAnnsDisplayGroup.qualifyDataSource(); // fetch
      // classer par nom
      affAnnsDisplayGroup.setSortOrderings(EOAffectationAnnuelle.SORT_INDIVIDU);
      affAnnsDisplayGroup.updateDisplayedObjects();  // sort 
      // selection de la 1ere affAnn
      affAnnsDisplayGroup.selectsFirstObjectAfterFetch();     
    } else {
    	affAnnsDisplayGroup.setObjectArray(new NSArray());
    }
  }
  
  // boolean interface

  /**
   * 
   */
  public boolean isTheComposanteSelected() {
  	return composanteItem == composanteSelected;
  }

  /**
   * 
   */
  public boolean isTheServiceSelected() {
  	return serviceItem == serviceSelected;
  }
  
  /**
   * 
   */
  public boolean isTheAffAnnSelected() {
  	return affAnnItem == affAnnSelected;
  }
}