package fr.univlr.cri.conges;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.util.ArrayCtrl;
import fr.univlr.cri.webapp.LRSort;

/**
 * Ecran de suivi des plannings (exports csv ...)
 * 
 * @author Cyril TARADE <cyril.tarade at cocktail.org>
 */
public class AdminSuiviPlanning 
	extends A_PageAdminCreationAffAnn {	
	
	// periode d'interrogation
	private NSTimestamp dDebut;
	private NSTimestamp dFin;
	
	// liste des services selectionnés dans le browser de tous les services
	private NSArray lesServicesAutorisesSelected;
	public EOStructure serviceAutoriseItem;
	
	// liste des services a prendre en compte
	public NSMutableArray serviceArray;
	public NSArray serviceArraySelected;
	public EOStructure serviceItem;
	
	// le controleur de l'édition
	private AdminSuiviPlanningWorkingCtrl adminSuiviPlanningWorkingCtrl;
		
	public AdminSuiviPlanning(WOContext context) {
		super(context);
	}
	
	/* (non-Javadoc)
	 * @see fr.univlr.cri.conges.A_PageAdminCreationAffAnn#onChangeAnneUnivSelectionnee()
	 */
	@Override
	public void onChangeAnneUnivSelectionnee() {
		// changer la période d'interrogation
		setDDebut(selectedAnneeUniv);
		setDFin(DateCtrlConges.dateToFinAnneeUniv(getDDebut()));
		// RAZ de la liste des services
		lesServicesAutorises = null;
		setLesServicesAutorisesSelected(new NSArray());
		setServiceArray(new NSMutableArray());
		serviceArraySelected = new NSArray();
		//
		clearCtrl();
	}

	/**
	 * Raz du controle de l'édition (dès qu'une donnée en 
	 * entrée change, il faut RAZ)
	 */
	private void clearCtrl() {
		setAdminSuiviPlanningWorkingCtrl(null);
	}
	
	/**
	 * Permet de valider le formulaire et de faire 
	 * apparaitre le lien d'impression si tout est OK
	 * 
	 * @return
	 */
	private boolean valider() {
		boolean ok = true;
				
		clearError();

		if (getDDebut() == null) {
			setErrMsg("La date de début est obligatoire");
			ok = false;
		}
		
		if (ok &&
				getDFin() == null) {
			setErrMsg("La date de fin est obligatoire");
			ok = false;
		}
		
		if (ok &&
				DateCtrlConges.isAfter(getDDebut(), getDFin())) {
			setErrMsg("La date de fin doit être avant la date de début");
			ok = false;
		}
		
		if (ok &&
				ArrayCtrl.isEmpty(serviceArray)) {
			setErrMsg("Veuillez selectionner au moins un service");
			ok = false;
		}
		
		return ok;
	}
	
	
	/**
	 * La liste des services autorisés parmi lesquels on ôte 
	 * les selections {@link #serviceArray}
	 */
	@Override
	public NSArray lesServicesAutorises() {

		NSMutableArray array = new NSMutableArray();
		
		for (int i=0; i<super.lesServicesAutorises().count(); i++) {
			EOStructure service = (EOStructure) super.lesServicesAutorises().objectAtIndex(i);
			if (!serviceArray.containsObject(service)) {
				array.addObject(service);
			}
		}

		
		return array.immutableClone();
	}
  
	/**
	 * Ajouter des services à l'export
	 * @return
	 */
	public WOComponent selectionner() {
		
		clearError();
		
		if (!ArrayCtrl.isEmpty(lesServicesAutorisesSelected)) {
			
			clearCtrl();
			
			for (int i=0; i<lesServicesAutorisesSelected.count(); i++) {
				EOStructure service = (EOStructure) lesServicesAutorisesSelected.objectAtIndex(i);
				if (!serviceArray.containsObject(service)) {
					serviceArray.addObject(service); 
				}
			}
			
      // classement alpha
      serviceArray = new NSMutableArray(LRSort.sortedArray(serviceArray, EOStructure.LIBELLE_LONG_KEY));
		}
		
		return null;
	}
  
	/**
	 * Oter des services à l'export
	 * @return
	 */
	public WOComponent deselectionner() {

		clearError();
		
		if (!ArrayCtrl.isEmpty(serviceArraySelected)) {

			clearCtrl();

			for (int i=0; i<serviceArraySelected.count(); i++) {
				EOStructure service = (EOStructure) serviceArraySelected.objectAtIndex(i);
				serviceArray.removeIdenticalObject(service);
			}
		}
		
		return null;
	}
	
	/**
	 * N'autoriser l'impression que si des services ont été selectionnés
	 * @return
	 */
	public boolean isDisabledBtnPrintCsv() {
		boolean disabled = false;
		
		if (ArrayCtrl.isEmpty(serviceArray)) {
			disabled = true;
		}
		
		return disabled;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isAfficherLnkTelechargerFichierCsv() {
		boolean isAfficher = false;
		
		if (getAdminSuiviPlanningWorkingCtrl() != null &&
				getAdminSuiviPlanningWorkingCtrl().isTravailTermine()) {
			isAfficher = true;
		}
		
		return isAfficher;
	}
	
  // export CVS
  
	/**
	 * 
	 */
	public WOComponent printCsv() {
		
		if (valider()) {
			
			AdminSuiviPlanningWorking nextPage = (AdminSuiviPlanningWorking) pageWithName(
					AdminSuiviPlanningWorking.class.getName());

			if (!nextPage.isWorking()) {
				setAdminSuiviPlanningWorkingCtrl(new AdminSuiviPlanningWorkingCtrl(
						lAnneeUnivSelectionnee, getDDebut(), getDFin(), edc, getServiceArray(), this.parent().parent()));
				nextPage.setCtrl(getAdminSuiviPlanningWorkingCtrl());
				return nextPage; 
			} 
		}
			
		return null;
		
	}

	
	public final NSTimestamp getDDebut() {
		return dDebut;
	}

	public final void setDDebut(NSTimestamp dDebut) {
		clearCtrl();
		this.dDebut = dDebut;
	}

	public final NSTimestamp getDFin() {
		return dFin;
	}

	public final void setDFin(NSTimestamp dFin) {
		clearCtrl();
		this.dFin = dFin;
	}

	public final NSArray getLesServicesAutorisesSelected() {
		return lesServicesAutorisesSelected;
	}

	public final void setLesServicesAutorisesSelected(
			NSArray lesServiceAutorisesSelected) {
		this.lesServicesAutorisesSelected = lesServiceAutorisesSelected;
	}

	public final NSMutableArray getServiceArray() {
		return serviceArray;
	}

	public final void setServiceArray(NSMutableArray serviceArray) {
		this.serviceArray = serviceArray;
	}

	public final AdminSuiviPlanningWorkingCtrl getAdminSuiviPlanningWorkingCtrl() {
		return adminSuiviPlanningWorkingCtrl;
	}

	public final void setAdminSuiviPlanningWorkingCtrl(
			AdminSuiviPlanningWorkingCtrl adminSuiviPlanningWorkingCtrl) {
		this.adminSuiviPlanningWorkingCtrl = adminSuiviPlanningWorkingCtrl;
	}
}