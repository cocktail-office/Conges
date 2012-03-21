package fr.univlr.cri.conges;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;


import fr.univlr.cri.conges.eos.modele.planning.EOVacanceScolaire;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.util.wo5.DateCtrl;
import fr.univlr.cri.ycrifwk.utils.UtilDb;

/**
 * Ecran de gestion des vacances scolaires
 * @author ctarade
 *
 */
public class PageAdminFermetures 
	extends A_ComponentAnneeUniv {
    
	public NSArray lesVacancesSelectionnees;
	public EOVacanceScolaire uneVacance;
	public NSTimestamp dateDebutVacance;
	public NSTimestamp dateFinVacance;
	public boolean isModeCreationVacance;
	
	// 
	private NSArray lesVacances; 
    
	public PageAdminFermetures(WOContext context) {
		super(context);
	}
   
	public void reset() {
		resetVacance();
	}
  
	public void resetVacance() {
		isModeCreationVacance = false;
		if (uneVacanceSelectionnee()!= null) {
			dateDebutVacance 	= uneVacanceSelectionnee().dateDebut();
			dateFinVacance 	= uneVacanceSelectionnee().dateFin();
		}
	}
 
	private NSTimestamp dateDebutAnneeSelected()  {
		return DateCtrlConges.dateDebutAnneePourStrPeriodeAnnee(lAnneeUnivSelectionnee);
	}
    
	public NSArray lesVacances() {
		if (lesVacances == null) {
			lesVacances = EOVacanceScolaire.findVacancesScolairesForPeriode(edc,
					dateDebutAnneeSelected(), dateDebutAnneeSelected().timestampByAddingGregorianUnits(1,0,-1,0,0,0));
		}
		return lesVacances;
	}
    
	public EOVacanceScolaire uneVacanceSelectionnee() {
		if (lesVacancesSelectionnees != null && lesVacancesSelectionnees.count() > 0)
			return (EOVacanceScolaire)lesVacancesSelectionnees.objectAtIndex(0);
		else
			return null;
	}

	// METHODES DE DISPLAY
    
	public String displayUneVacance() {
		return DateCtrl.dateToString(uneVacance.dateDebut()) + " au " + DateCtrl.dateToString(uneVacance.dateFin());
	}
    
 	// METHODES DE NAVIGATION
	
	public WOComponent ajouterVacance() {
		isModeCreationVacance = true;
		dateDebutVacance = dateFinVacance = laSession.dateRef();
		return null;
	}
	
	public WOComponent enregistrerVacance() throws Throwable {
		if (dateDebutVacance != null && 
				dateFinVacance != null && 
				DateCtrl.isBeforeEq(dateDebutVacance, dateFinVacance)) {
			EOVacanceScolaire vacance = null;
			if (isModeCreationVacance) {
				vacance = EOVacanceScolaire.newDefaultRecordInContext(edc);
			} else {
				vacance = uneVacanceSelectionnee();
			}
			vacance.setDateDebut(dateDebutVacance);
			vacance.setDateFin(dateFinVacance);
	    UtilDb.save(edc, true);
	    lesVacances = null;
		}
		resetVacance();
		return null;
	}
	
	public WOComponent annuler() {
		edc.revert();
		resetVacance();
		return null;
	}
	
	
	public WOComponent supprimerVacance() throws Throwable {
		edc.deleteObject(uneVacanceSelectionnee());
		UtilDb.save(edc, true);
		lesVacances = null;
		return null;
	}

	@Override
	public void onChangeAnneUnivSelectionnee() {
		lesVacances = null;
	}
}