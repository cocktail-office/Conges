package fr.univlr.cri.conges;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture;
import fr.univlr.cri.conges.eos.modele.planning.EOPeriodeFermeture;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.wo5.DateCtrl;
import fr.univlr.cri.webext.CRIAlertPage;
import fr.univlr.cri.webext.CRIAlertResponder;
import fr.univlr.cri.ycrifwk.utils.UtilDb;

/**
 * Page de gestion des fermetures :
 * - etablissement
 * - par composante
 * - par service
 * - par planning
 * 
 * @author ctarade
 */
public class PageAdminPeriodeFermeture 
	extends YCRIWebPage {

	/** la composante selectionnée */
	public EOStructure composanteSelected;
	/** le service selectionné */
	public EOStructure serviceSelected;
	/** le planning selectionné */
	public EOAffectationAnnuelle affAnnSelected;
	/** l'année universistaire selectionnée */ 
  public String anneeUnivSelected;
	// cache pour eviter les recalculs intempestifs
  private NSTimestamp _dateDebutAnneeUnivSelected;
  private NSTimestamp _dateFinAnneeUnivSelected;
  
  /** items */
  public EOPeriodeFermeture fermetureItem;
  
  /** cache des periodes de femetures */
  private NSArray _fermetureSortedList;
  
  // temoin interne pour ignorer certains setters
  private boolean shouldIgnoreSetIsNotAnnuleeFermetureItemForServiceSelected;
  private boolean shouldIgnoreSetIsNotAnnuleeFermetureItemForAffAnnSelected;
  
  /** dates de saisie de nouvelles fermeture */
  public NSTimestamp dateDebutFermeture;
  public NSTimestamp dateFinFermeture;
  
	public PageAdminPeriodeFermeture(WOContext context) {
		super(context);
    // selection par defaut de l'annee universitaire en cours
		anneeUnivSelected = DateCtrlConges.anneeUnivForDate(laSession.dateRef());
		//
		shouldIgnoreSetIsNotAnnuleeFermetureItemForServiceSelected = false;
		shouldIgnoreSetIsNotAnnuleeFermetureItemForAffAnnSelected = false;
		// on positionne date debut et date fin 
		initDateDebutDateFin();
	}
	
	/**
	 * 
	 */
	private void initDateDebutDateFin() {
		dateDebutFermeture = TimeCtrl.dateToMinuit(DateCtrl.now());
		dateFinFermeture = dateDebutFermeture.timestampByAddingGregorianUnits(0, 0, 1, 0, 0, 0);
	}
	
	/**
	 * Raz du cache des fermtures
	 */
	public void appendToResponse(WOResponse arg0, WOContext arg1) {
		_fermetureSortedList = null;
		super.appendToResponse(arg0, arg1);
	}


	/**
	 * 
	 * @return
	 */
	private NSArray fermetureSortedList() {
		if (_fermetureSortedList == null) {
			_fermetureSortedList = EOPeriodeFermeture.findSortedPeriodeFermeturesInContext(
					edc, getDateDebutAnneeUnivSelected(), getDateFinAnneeUnivSelected());
		}	
		return _fermetureSortedList;
	}
	
	
	// methodes internes

	/**
	 * La date de debut d'année correspondant a {@link #anneeUnivSelected}
	 */
	private NSTimestamp getDateDebutAnneeUnivSelected() {
		if (_dateDebutAnneeUnivSelected == null) {
			_dateDebutAnneeUnivSelected = DateCtrlConges.dateDebutAnneePourStrPeriodeAnnee(anneeUnivSelected);
		}
		return _dateDebutAnneeUnivSelected;
	}
	
	/**
	 * La date de fin d'année correspondant a {@link #anneeUnivSelected}
	 */
	private NSTimestamp getDateFinAnneeUnivSelected() {
		if (_dateFinAnneeUnivSelected == null) {
			_dateFinAnneeUnivSelected = DateCtrlConges.dateToFinAnneeUniv(getDateDebutAnneeUnivSelected());
		}
		return _dateFinAnneeUnivSelected;
	}
	
	
	// setters
	
	/**
	 * RAZ des cache des dates
	 */
	public void setAnneeUnivSelected(String value) {
		anneeUnivSelected = value;
		// raz du cache des dates
		_dateDebutAnneeUnivSelected = null;
		_dateFinAnneeUnivSelected = null;
	}
	
	/**
	 * Interception de la case a cocher pour annuler / ractivier une fermeture {@link #fermetureItem}
	 * pour la composante {@link #composanteSelected}
	 * @param value
	 * @throws Throwable 
	 */
	public void setIsNotAnnuleeFermetureItemForComposanteSelected(boolean isActive) throws Throwable {
		if (isActive) {
			EOAnnulationPeriodeFermeture existingAnnulation = EOAnnulationPeriodeFermeture.findAnnulationPeriodeFermetureForFermetureAndComposante(
					edc, fermetureItem, composanteSelected);
			edc.deleteObject(existingAnnulation);
			
		} else {
			EOAnnulationPeriodeFermeture newAnnulation = EOAnnulationPeriodeFermeture.createAnnulationPeriodeFermeture(edc, fermetureItem);
			newAnnulation.setToComposanteRelationship(composanteSelected);
		}
		// on ignore le setter sur le service
		shouldIgnoreSetIsNotAnnuleeFermetureItemForServiceSelected = true;
		UtilDb.save(edc, true);
	}
	
	/**
	 * Interception de la case a cocher pour annuler / ractivier une fermeture {@link #fermetureItem}
	 * pour le service {@link #serviceSelected}
	 * @param value
	 * @throws Throwable 
	 */
	public void setIsNotAnnuleeFermetureItemForServiceSelected(boolean isActive) throws Throwable {
		if (!shouldIgnoreSetIsNotAnnuleeFermetureItemForServiceSelected) {
			if (isActive) {
				EOAnnulationPeriodeFermeture existingAnnulation = EOAnnulationPeriodeFermeture.findAnnulationPeriodeFermetureForFermetureAndService(
						edc, fermetureItem, serviceSelected);
				edc.deleteObject(existingAnnulation);
				
			} else {
				EOAnnulationPeriodeFermeture newAnnulation = EOAnnulationPeriodeFermeture.createAnnulationPeriodeFermeture(edc, fermetureItem);
				newAnnulation.setToStructureRelationship(serviceSelected);
			}
		} 
		// ne plus ignorer ce setter lors du prochain appel
		shouldIgnoreSetIsNotAnnuleeFermetureItemForServiceSelected = false;
		// on ignore le setter sur les plannings
		shouldIgnoreSetIsNotAnnuleeFermetureItemForAffAnnSelected = true;
		UtilDb.save(edc, true);
	}
	
	/**
	 * Interception de la case a cocher pour annuler / ractivier une fermeture {@link #fermetureItem}
	 * pour le planning {@link #affAnnSelected}
	 * @param value
	 * @throws Throwable 
	 */
	public void setIsNotAnnuleeFermetureItemForAffAnnSelected(boolean isActive) throws Throwable {
		if (!shouldIgnoreSetIsNotAnnuleeFermetureItemForAffAnnSelected) {
			if (isActive) {
				EOAnnulationPeriodeFermeture existingAnnulation = EOAnnulationPeriodeFermeture.findAnnulationPeriodeFermetureForFermetureAndAffAnn(
						edc, fermetureItem, affAnnSelected);
				edc.deleteObject(existingAnnulation);
			} else {
				EOAnnulationPeriodeFermeture newAnnulation = EOAnnulationPeriodeFermeture.createAnnulationPeriodeFermeture(edc, fermetureItem);
				newAnnulation.setToAffectationAnnuelleRelationship(affAnnSelected);
			}
		}
		// ne plus ignorer ce setter lors du prochain appel
		shouldIgnoreSetIsNotAnnuleeFermetureItemForAffAnnSelected = false;
		UtilDb.save(edc, true);
	}
	
	// getters
	
	/**
	 * Liste des fermetures de l'ensemble de l'etablissement 
	 * @return
	 */
	public NSArray getFermetureEtablissementList() {
		return EOPeriodeFermeture.filterPeriodesFermeturesEtablissement(
				fermetureSortedList(), getDateDebutAnneeUnivSelected(), getDateFinAnneeUnivSelected());
	}
	
	/**
	 * Liste des fermetures de la composante {@link #composanteSelected}
	 * @return
	 */
	public NSArray getFermetureComposanteList() {
		NSArray result = new NSArray();
		if (composanteSelected != null) {
			result = EOPeriodeFermeture.filterPeriodesFermeturesComposante(
					fermetureSortedList(), getDateDebutAnneeUnivSelected(), getDateFinAnneeUnivSelected(), composanteSelected);
		}
		return result;
	}
	
	/**
	 * Liste des fermetures du service {@link #serviceSelected}
	 * @return
	 */
	public NSArray getFermetureServiceList() {
		NSArray result = new NSArray();
		if (serviceSelected != null) {
			result = EOPeriodeFermeture.filterPeriodesFermeturesService(
					fermetureSortedList(), getDateDebutAnneeUnivSelected(), getDateFinAnneeUnivSelected(), serviceSelected);
		}
		return result;
	}
	
	/**
	 * Liste des fermetures du planning {@link #affAnnSelected}
	 * @return
	 */
	public NSArray getFermetureAffAnnList() {
		NSArray result = new NSArray();
		if (affAnnSelected != null) {
			result = EOPeriodeFermeture.filtereriodesFermeturesPlanning(
					fermetureSortedList(), getDateDebutAnneeUnivSelected(), getDateFinAnneeUnivSelected(), affAnnSelected);
		}
		return result;
	}

	/**
	 * Indique si la fermeture est annulée pour la composante selectionnée
	 */
	public boolean isNotAnnuleeFermetureItemForComposanteSelected() {
		return composanteSelected != null && !EOAnnulationPeriodeFermeture.isAnnuleePeriodeFermetureForComposante(
				edc, fermetureItem, composanteSelected);
	}

	/**
	 * Indique si la fermeture est annulée pour le service selectionné.
	 */
	public boolean isNotAnnuleeFermetureItemForServiceSelected() {
		return serviceSelected != null && !EOAnnulationPeriodeFermeture.isAnnuleePeriodeFermetureForService(
				edc, fermetureItem, serviceSelected);
	}

	/**
	 * Indique si la fermeture est annulée pour la planing selectionné
	 */
	public boolean isNotAnnuleeFermetureItemForAffAnnSelected() {
		return affAnnSelected != null && !EOAnnulationPeriodeFermeture.isAnnuleePeriodeFermetureForPlanning(
				edc, fermetureItem, affAnnSelected);
	}
	
	/**
	 * Indique si une composante est selectionnée
	 * @return
	 */
	public boolean isAComposanteSelected() {
		return composanteSelected != null;
	}
	
	/**
	 * Indique si un service est selectionné
	 * @return
	 */
	public boolean isAServiceSelected() {
		return serviceSelected != null;
	}
	
	/**
	 * Indique si un planning est selectionné
	 * @return
	 */
	public boolean isAnAffAnnSelected() {
		return affAnnSelected != null;
	}
	
	/**
	 * On autorise pas la modification de la coche du service
	 * si la fermeture est annulée pour la composante
	 * @return
	 */
	public boolean isDisabledChkService() {
		return !isNotAnnuleeFermetureItemForComposanteSelected();
	}
	
	/**
	 * On autorise pas la modification de la coche du service
	 * si la fermeture est annulée pour le service
	 * @return
	 */
	public boolean isDisabledChkAffAnn() {
		return !isNotAnnuleeFermetureItemForComposanteSelected() || !isNotAnnuleeFermetureItemForServiceSelected();
	}
	
	
	// ajout de fermetures
	
	/**
	 * Fermeture etablissement
	 * @throws Throwable 
	 */
	public WOComponent doAddFermetureEtablissement() throws Throwable {
		EOPeriodeFermeture.createPeriodeFermetureDateDebutDateFin(edc, dateDebutFermeture, dateFinFermeture);
		UtilDb.save(edc, true);
		return null;
	}
	
	/**
	 * Fermeture composante
	 * @throws Throwable 
	 */
	public WOComponent doAddFermetureComposante() throws Throwable {
		EOPeriodeFermeture newPeriodeFermeture = EOPeriodeFermeture.createPeriodeFermetureDateDebutDateFin(edc, dateDebutFermeture, dateFinFermeture);
		newPeriodeFermeture.setToComposanteRelationship(composanteSelected);
		UtilDb.save(edc, true);
		return null;
	}
	
	/**
	 * Fermeture service
	 * @throws Throwable 
	 */
	public WOComponent doAddFermetureService() throws Throwable {
		EOPeriodeFermeture newPeriodeFermeture = EOPeriodeFermeture.createPeriodeFermetureDateDebutDateFin(edc, dateDebutFermeture, dateFinFermeture);
		newPeriodeFermeture.setToStructureRelationship(serviceSelected); 
		UtilDb.save(edc, true);
		return null;
	}
	
	/**
	 * Fermeture planning
	 * @throws Throwable 
	 */
	public WOComponent doAddFermetureAffAnn() throws Throwable {
		EOPeriodeFermeture newPeriodeFermeture = EOPeriodeFermeture.createPeriodeFermetureDateDebutDateFin(edc, dateDebutFermeture, dateFinFermeture);
		newPeriodeFermeture.setToAffectationAnnuelleRelationship(affAnnSelected);
		UtilDb.save(edc, true);
		// on force le rechargement du planning s'il était en session
		laSession.clearCachePlanningForAffectationAnnuelle(affAnnSelected);
		return null;
	}
	
	
	// suppression de fermeture
	
	/**
	 * Suppression d'une periode de fermeture
	 * @throws Throwable 
	*/
	public WOComponent doDelFermetureItem() {
    // page de confirmation
		DeleteFermetureResponder responder = new DeleteFermetureResponder(fermetureItem);
    return CRIAlertPage.newAlertPageWithResponder(this, "Suppression d'une fermeture<br>",
        "Etes vous sur de vouloir <b>effacer</b> la fermeture "+
        fermetureItem.display() + "  ?<br/>" + 
        "Il s'agit d'un effacement de donn&eacute;es, la fermeture ainsi que toutes <br/>" + 
        "les annulations qui en ont &eacute;t&eacute; faites <u>seront perdues</u> !<br/><br/>" + 
        "<font class=\"textNote\">Note : pour conserver la fermeture, mais l'annuler uniquement pour une composante, service ou planning<br/>" + 
        "c'est avec les cases &agrave; cocher que cela se fait.</font>", 
        "Confirmer l'effacement", "Ne pas supprimer", null, CRIAlertPage.QUESTION, responder);
  }
  
  /**
   * La classe interne - l'implementation de AlertResponder pour
   * la suppression d'une fermeture
   */	 
  public class DeleteFermetureResponder implements CRIAlertResponder {
  	
  	private EOPeriodeFermeture fermetureToDelete;
  	
    public DeleteFermetureResponder(EOPeriodeFermeture aFermetureToDelete) {
    	super();
    	fermetureToDelete = aFermetureToDelete;
    }
  	
    public WOComponent respondToButton(int buttonNo) {
      switch (buttonNo) {
      case 2: return PageAdminPeriodeFermeture.this.parent();
      case 1: 
      	// si c'est une fermeture de planning
    		// on force son rechargement s'il était en session
      	if (fermetureToDelete.toAffectationAnnuelle() != null) {
      		laSession.clearCachePlanningForAffectationAnnuelle(fermetureToDelete.toAffectationAnnuelle());
      	}
    	 	edc.deleteObject(fermetureToDelete);
    		try {
    			UtilDb.save(edc, true);
    		} catch (Throwable e) {
    			e.printStackTrace();
    		}
        return PageAdminPeriodeFermeture.this.parent();
      default: return null;
      }
    }
  } 
}