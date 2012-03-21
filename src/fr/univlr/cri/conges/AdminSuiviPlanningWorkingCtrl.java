package fr.univlr.cri.conges;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOOrQualifier;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.print.PrintCSV;
import fr.univlr.cri.webapp.CRIDataBus;

/**
 * 
 */

/**
 * @author Cyril TARADE <cyril.tarade at cocktail.org>
 *
 */
public class AdminSuiviPlanningWorkingCtrl {

	private String annee;
	private NSTimestamp dateDebut;
	private NSTimestamp dateFin;
	private EOEditingContext ec;
	private NSArray serviceArray;
	private WOComponent caller;
	
	private WOResponse lastCsvResponse;
	
	private boolean isTravailTermine;
	
	public AdminSuiviPlanningWorkingCtrl(
			String annee, NSTimestamp dateDebut, NSTimestamp dateFin, EOEditingContext ec, NSArray serviceArray, WOComponent caller) {
		super();
		this.annee = annee;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.ec = ec;
		this.serviceArray = serviceArray;
		this.caller = caller;
		isTravailTermine = false;
	}
	

	
	/** 
	 * Methode de generation du flux CSV
	 */
	public void generateCsvResponse() { 
		NSArray affAnnArray = new NSArray();
		
		// filtrer d'après la liste des services selectionnés
		NSMutableDictionary bindings = new NSMutableDictionary();
		
		bindings.setObjectForKey(annee, 				"annee");
		bindings.setObjectForKey(dateDebut, 		"dateDebut");
		bindings.setObjectForKey(dateFin, 			"dateFin");
		
		affAnnArray = EOUtilities.objectsWithFetchSpecificationAndBindings(
				ec, EOAffectationAnnuelle.ENTITY_NAME, 
				EOAffectationAnnuelle.AFFECTATIONS_ANNUELLES_POUR_MOIS_FETCH_SPEC, bindings);
	
		// filtrer d'après la liste des services selectionnés
		NSMutableArray qualArray = new NSMutableArray();
		for (int i=0; i<serviceArray.count(); i++) {
			EOQualifier qual = CRIDataBus.newCondition(
					EOAffectationAnnuelle.STRUCTURE_KEY + "=%@",
					new NSArray(serviceArray.objectAtIndex(i)));
			qualArray.addObject(qual);
		}
		
		affAnnArray = EOQualifier.filteredArrayWithQualifier(
				affAnnArray, new EOOrQualifier(qualArray));
		
		lastCsvResponse = PrintCSV.printCsvPlanningPourPeriode(
				"export", dateDebut, dateFin, affAnnArray, "R");
		
		isTravailTermine = true;
	}

	public final boolean isTravailTermine() {
		return isTravailTermine;
	}



	public final WOComponent getCaller() {
		return caller;
	}



	public final WOResponse getLastCsvResponse() {
		return lastCsvResponse;
	}
	
}
