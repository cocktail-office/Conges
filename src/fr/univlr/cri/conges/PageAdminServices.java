package fr.univlr.cri.conges;

import java.util.Enumeration;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSSet;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.conges.EOStructureAutorisee;
import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.webapp.LRLog;
import fr.univlr.cri.ycrifwk.utils.UtilDb;

public class PageAdminServices 
	extends A_PageAdminCreationAffAnn {

	/** la liste de tous les services de l'etablissement */
	public NSArray lesServices;
	public EOStructure unService;
	public NSArray lesServicesSelectionnes;
	public EOStructure unServiceAutorise;
	public NSArray lesServicesAutorisesSelectionnes;
	
	// temoin service archives O/N
	public boolean isShowServicesArchives = false;
 	
	// faut-il supprimer les plannings en cas de desautorisation
	public boolean isDeletePlanning = false;
	
  public PageAdminServices(WOContext context) {
    super(context);
  }
  
  /**
   * changement de l'annee univ : raz de la liste des services
   * et des selections
   */
  public void onChangeAnneUnivSelectionnee() {
    lesServices = lesServicesAutorises = lesServicesAutorisesSelectionnes = lesServicesSelectionnes = null;
    // si l annee selectionnee est la suivante, on selectionne par defaut tous les services de l'annee precedente
    if (DateCtrlConges.isSameDay(selectedAnneeUniv, laSession.dateRef().timestampByAddingGregorianUnits(1, 0, -1, 0, 0, 0))) {
      lesServicesSelectionnes = (NSArray) EOStructureAutorisee.findAllStructureAutoriseeForAnneeUnivInContext(
          edc, laSession.dateRef()).valueForKeyPath("structure");
    }
    // determiner si des parametres nouveaux ont ete créées automatiquement
    // car il n'existaient pas pour l'année selectionnée
  }

  /**
   * Liste des services autorisables.
   * On enleve les services "archives"
   * @return
   */
  public NSArray lesServices() {
    if (lesServices == null) {
    	NSMutableDictionary bindings = new NSMutableDictionary();
    	if (!isShowServicesArchives) {
    		bindings = new NSMutableDictionary("0", EOStructure.IS_ARCHIVE_KEY);
    	}
      lesServices = EOUtilities.objectsWithFetchSpecificationAndBindings(edc, EOStructure.ENTITY_NAME, "fetchService", bindings);
      if (lesServicesAutorises() != null) {
        NSSet setLesServices = new NSSet(lesServices);
        NSSet setLesServicesAutorises = new NSSet((NSArray) (lesServicesAutorises()));
        setLesServices = setLesServices.setBySubtractingSet(setLesServicesAutorises);
        lesServices = setLesServices.allObjects();
      }
      EOSortOrdering libelleLongOrdering = EOSortOrdering.sortOrderingWithKey(EOStructure.LIBELLE_LONG_KEY, EOSortOrdering.CompareAscending);
      lesServices = EOSortOrdering.sortedArrayUsingKeyOrderArray(lesServices, new NSArray(libelleLongOrdering));
    }
    return lesServices;
  }

  /**
   * Autoriser un ou plusieurs services
   * @return
   * @throws Throwable
   */
  public WOComponent autoriser() throws Throwable {
    if (selectedAnneeUniv != null) {
    	//
    	NSArray result = planningBus().autoriserServicesPourDateInContext(edc, lesServicesSelectionnes, selectedAnneeUniv, true);
    	lastTotalAffAnn = ((Integer) result.objectAtIndex(0)).intValue();
    	lastTotalPeriode = ((Integer) result.objectAtIndex(0)).intValue();
    	UtilDb.save(edc, true);
    	LRLog.log(" > commited");      
    	lesServices = null;
    	lesServicesAutorises = null;
    	lesServicesSelectionnes = null;
    	// forcer le refresh
    	laSession.setLesStructureAutorisee(null);
    	shouldShowLastTotalAffAnn = true;
    }
    return null;
  }
  
  /**
   * Autoriser l'ensemble des services de l'année précédente
   * vers l'année courante
   * @throws Throwable 
   */
  public WOComponent autoriserIdentiqueAnneeNm1() throws Throwable {
  	// recuperer la liste des services autorisés de l'année N-1
  	NSTimestamp currAnneeUniv = selectedAnneeUniv;
  	setLAnneeUnivSelectionnee(DateCtrlConges.anneeUnivForDate(currAnneeUniv.timestampByAddingGregorianUnits(-1,0,0,0,0,0)));
  	NSMutableArray servicesAutorisesNm1 = new NSMutableArray(lesServicesAutorises());
  	// remettre la bonne annee
  	setLAnneeUnivSelectionnee(DateCtrlConges.anneeUnivForDate(currAnneeUniv));
  	// ne conserver que ceux qui ne sont pas autorisés pour cette année
  	servicesAutorisesNm1.removeObjectsInArray(lesServicesAutorises());
  	// les selectionner
  	lesServicesSelectionnes = servicesAutorisesNm1.immutableClone();
  	// appeler la methode d'autorisation
  	return autoriser();
  }

  /**
   * Désautoriser un service pour une année universtatire
   * @return
   * @throws Throwable
   */
  public WOComponent supprimer() throws Throwable {
    if (lesServicesAutorisesSelectionnes != null && lesServicesAutorisesSelectionnes.count() > 0 && selectedAnneeUniv != null) {
      EOStructure leServiceSelectionne = (EOStructure) lesServicesAutorisesSelectionnes.lastObject();
      edc.deleteObject(EOStructureAutorisee.findStructureAutoriseeForStructureAndAnneeUnivInContext(edc, leServiceSelectionne, selectedAnneeUniv));
      LRLog.log(" > unallowing service "+ DateCtrlConges.anneeUnivForDate(selectedAnneeUniv) +" : " + leServiceSelectionne.display());      
     
      NSMutableDictionary bindings = new NSMutableDictionary();
      bindings.setObjectForKey(leServiceSelectionne, "service");
      bindings.setObjectForKey(selectedAnneeUniv, "dateDebut");
        
      NSArray affectationsAnnuelles = EOAffectationAnnuelle.findAffectationsAnnuellesInContext(bindings, edc);
      Enumeration enumAffectationsAnnuelles = affectationsAnnuelles.objectEnumerator();
      while (enumAffectationsAnnuelles.hasMoreElements()) {
      	EOAffectationAnnuelle uneAffectationAnnuelle = (EOAffectationAnnuelle) enumAffectationsAnnuelles.nextElement();
      	LRLog.log("  > deleting planning " +  uneAffectationAnnuelle.annee() + " " + uneAffectationAnnuelle.individu().nomComplet());      
      	edc.deleteObject(uneAffectationAnnuelle);
      }
      
      UtilDb.save(edc, true);
    	LRLog.log(" > commited");      

      lesServices = null;
      lesServicesAutorises = null;
      lesServicesAutorisesSelectionnes = null;
      // forcer le refresh
      laSession.setLesStructureAutorisee(null);
    }
    return null;
  }
  
 
  // setters
  
  /**
   * Si on veut voir les services archivés ou inversement, on force
   * le rechargement de la liste des services
   */
  public void setIsShowServicesArchives(boolean value) {
  	isShowServicesArchives = value;
  	lesServices = null;
  	lesServicesSelectionnes = null;
  }
  
}