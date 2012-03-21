// EOStructureAutorisee.java
// Created on Thu Jul 22 17:38:44  2004 by Apple EOModeler Version 5.2

package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.util.ArrayCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRSort;


public class EOStructureAutorisee extends _EOStructureAutorisee {

	private final static LRSort STRUCTURE_AUTORISEE_SORT = LRSort.newSort(ANNEE_KEY);
	
	/**
	 * 
	 * @param ec
	 * @param structure
	 * @param dateRef
	 * @return
	 */
	public static EOStructureAutorisee findStructureAutoriseeForStructureAndAnneeUnivInContext(
			EOEditingContext ec, EOStructure structure, NSTimestamp dateRef) {
		EOQualifier qual = CRIDataBus.newCondition(
				STRUCTURE_KEY +"=%@ and " + ANNEE_KEY + "=%@",
	      new NSArray(new Object[]{structure, DateCtrlConges.anneeUnivForDate(dateRef)}));
		return fetchStructureAutorisee(ec, qual);
	}

	/**
	 * 
	 * @param ec
	 * @param dateRef
	 * @return
	 */
	public static NSArray findAllStructureAutoriseeForAnneeUnivInContext(EOEditingContext ec, NSTimestamp dateRef) {
		EOQualifier qual = CRIDataBus.newCondition(ANNEE_KEY + "=%@", new NSArray(DateCtrlConges.anneeUnivForDate(dateRef)));
		return fetchStructureAutorisees(ec, qual, null);
	}

	/**
	 * Liste de toutes les annees universitaires contenant des services autorises.
	 * Les resultats sont classes par ordre chronologique.
	 */
	public static NSArray findAllDebutAnneeUnivStringInContext(EOEditingContext ec) {
		NSArray anneeList = (NSArray) fetchAllStructureAutorisees(ec, STRUCTURE_AUTORISEE_SORT).valueForKey(ANNEE_KEY);
		anneeList = ArrayCtrl.removeDoublons(anneeList);
	  return anneeList;
	}

  
}
