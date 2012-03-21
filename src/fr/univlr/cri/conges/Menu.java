package fr.univlr.cri.conges;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

import fr.univlr.cri.conges.PageAccueil;
import fr.univlr.cri.conges.PageAdministration;
import fr.univlr.cri.conges.PageChangeIdentite;
import fr.univlr.cri.conges.PageDemandes;
import fr.univlr.cri.conges.YCRIWebPage;
import fr.univlr.cri.conges.constantes.ConstsDroit;
import fr.univlr.cri.conges.databus.CngDroitBus;
import fr.univlr.cri.conges.objects.*;

public class Menu extends YCRIWebPage {
  
  // element du popup des structures
	public Responsabilite currentStructure;
     
  
  public Menu(WOContext context) {
    super(context);
  }
  
  public WOComponent afficheAccueil() {
    return laSession.getSavedPageWithName(PageAccueil.class.getName());
  }

  public WOComponent afficheDemandes() {
    return laSession.getSavedPageWithName(PageDemandes.class.getName());
  }

  /**
   * click sur planning perso
   */
  public WOComponent affichePlannings() {
    PagePlannings nextPage = (PagePlannings)laSession.getSavedPageWithName(PagePlannings.class.getName());
    // on met son planning ou il faut
    laSession.preparerPlanningForIndividu(laSession.individuConnecte());
    nextPage.initialiserVisualisation();
    return nextPage;
  }

  public WOComponent afficheService() {
    WOComponent nextPage = pageWithName(PageService.class.getName());
    return nextPage;
  }

  public WOComponent afficheAdministration() 	{    	
  	return (PageAdministration)laSession.getSavedPageWithName(PageAdministration.class.getName());  
  }
  
  public WOComponent afficheChangeIdentite() 	{    	
  	return (PageChangeIdentite)laSession.getSavedPageWithName(PageChangeIdentite.class.getName());  
  }
  
  public WOComponent afficherPreferences() {
  	return (PagePreferences) laSession.getSavedPageWithName(PagePreferences.class.getName());
  }

  public WOComponent afficheAPropos() 			{  		
  	return null; /*return (ChangeLog)laSession.getSavedPageWithName(ChangeLog.class.getName());  } */
  }
     
  public WOComponent logout() {
    edc.invalidateAllObjects();
  	WOComponent nextPage = laSession.logout();
  	return nextPage;
  }


	// CONDITIONNAL POUR LA DISPONIBILITE DES ITEMS DU MENU
  private Boolean _peutVoirPlusDUnService, _hasVisaSurServiceEnCours, _hasVisuSurServiceEnCours, 
  	_voitExactement1Service, _peutVoirMenuAdministration, _peutVoirPreferences;
  
  public boolean peutVoirPlusDUnService() {		
  	if (_peutVoirPlusDUnService == null)
  		_peutVoirPlusDUnService = new Boolean(laSession.structuresDispoParNiveau()!=null && 
  				laSession.structuresDispoParNiveau().count() > 1);	 
  	return _peutVoirPlusDUnService.booleanValue();    
  }
	
  public boolean hasVisaSurServiceEnCours() {		     
  	if (_hasVisaSurServiceEnCours == null)
  		_hasVisaSurServiceEnCours = new Boolean(laSession.structuresDispoParNiveauSelection()!=null && 
  				droitBus().isMinimumNiveauVisa(laSession.structuresDispoParNiveauSelection().getNiveauMax()));     
  	return _hasVisaSurServiceEnCours.booleanValue();    
	}

  public boolean hasVisuSurServiceEnCours() {		     
  	if (_hasVisuSurServiceEnCours == null)
  		_hasVisuSurServiceEnCours = new Boolean(laSession.structuresDispoParNiveauSelection()!=null && 
  				droitBus().isMinimumNiveauVisu(laSession.structuresDispoParNiveauSelection().getNiveauMax()));     
  	return _hasVisuSurServiceEnCours.booleanValue();
  }

  public boolean voitExactement1Service() {		   
  	if (_voitExactement1Service == null)
  		_voitExactement1Service = new Boolean(laSession.structuresDispoParNiveau().count() == 1);     
  	return _voitExactement1Service.booleanValue();  
  }
	
	public boolean peutVoirMenuAdministration() {
		if (_peutVoirMenuAdministration == null)
	  	_peutVoirMenuAdministration = new Boolean(cngUserInfo().isAdmin() || cngUserInfo().isAdmComposante() || cngUserInfo().isDrh());   
		return _peutVoirMenuAdministration.booleanValue();    
	}
  
	/**
	 * Les preferences sont accessibles pour les utilisateurs ayant un droit
	 * minimum de visa
	 * @return
	 */
	public boolean peutVoirPreferences() {
		if (_peutVoirPreferences == null) {
			// administrateur ou drh
			_peutVoirPreferences = new Boolean(cngUserInfo().isAdmin() ||
					cngUserInfo().isDrh() ||
					cngUserInfo().isAdmComposante() ||
					cngUserInfo().isDrhComposante());
			if (_peutVoirPreferences.booleanValue() == false) {
				// sinon au moins niveau visa 
				Integer niveauMax = (Integer) laSession.structuresDispoParNiveau().valueForKeyPath("@max.niveauMax");
				if (niveauMax.intValue() >= ConstsDroit.DROIT_NIVEAU_VISA.intValue()); {
					_peutVoirPreferences = Boolean.TRUE;
				}
			}
		}
		return _peutVoirPreferences.booleanValue();
	}
	
	// bus de donnees
	
	private CngDroitBus droitBus() {
		return laSession.cngDataCenter().droitBus();
	}
}