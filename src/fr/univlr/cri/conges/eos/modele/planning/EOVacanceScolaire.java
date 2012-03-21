// EOVacanceScolaire.java
// Created on Fri Oct 01 10:07:40  2004 by Apple EOModeler Version 5.2

package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.util.wo5.DateCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRSort;


public class EOVacanceScolaire extends _EOVacanceScolaire {

	public EOVacanceScolaire() {
		super();
	}

	/**
	 * liste de toutes les vacances scolaires pour une periode
	 * @param ec
	 * @param dteDebut
	 * @param dteFin
	 * @return
	 */
	public static NSArray findVacancesScolairesForPeriode(EOEditingContext ec, NSTimestamp debut, NSTimestamp fin) {
		EOQualifier qual = CRIDataBus.newCondition(
				"("+DATE_DEBUT_KEY+">= %@ and "+DATE_DEBUT_KEY+" <= %@) or " +
				"("+DATE_FIN_KEY+" >= %@ and "+DATE_FIN_KEY+" <= %@)", 
				new NSArray(new Object[] {debut, fin, debut, fin}));
		return fetchVacanceScolaires(ec, qual, LRSort.newSort(DATE_DEBUT_KEY));
	}

	/**
	 * retourne vrai si le jour est inclus dans une des periodes de vacance arrays
	 * @param ec
	 * @param dteDebut
	 * @param dteFin
	 * @return
	 */
	public static boolean jourIsVacanceInArray(NSTimestamp date, NSArray array) {
		EOQualifier qual = CRIDataBus.newCondition(
				DATE_DEBUT_KEY + " <=%@ and " + DATE_FIN_KEY +" >=%@", 
				new NSArray(new Object[] {date, date}));
		return EOQualifier.filteredArrayWithQualifier(array, qual).count() > 0;
	}

	public static EOVacanceScolaire newDefaultRecordInContext(EOEditingContext ec) {
	    EOVacanceScolaire vacance = new EOVacanceScolaire();
	     vacance.setDateDebut(DateCtrl.now());
	    vacance.setDateFin(DateCtrl.now());
	    ec.insertObject(vacance);
	    return vacance;
	}
}
