// EOTypeHoraire.java
// Created on Thu Jun 17 08:48:34  2004 by Apple EOModeler Version 5.2

package fr.univlr.cri.conges.eos.modele.planning;


import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableDictionary;

import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.webapp.LRSort;

public class EOTypeHoraire extends _EOTypeHoraire {

    public EOTypeHoraire() {
        super();
    }
    
	public String duree() {
		String duree = "";
		int total = total().intValue();
		int heures = total/60;
		int minutes = total%60;
		
		duree = String.valueOf(heures)+"h";
		if (heures<10) duree = "0"+duree;
		if (minutes<10) duree = duree+"0";
		duree = duree + String.valueOf(minutes);
		
		return duree;
	}

	public String duree(int uneDuree) {
		String duree = "";
		int total = uneDuree;
		int heures = total/60;
		int minutes = total%60;
		
		duree = String.valueOf(heures)+"h";
		if (heures<10) duree = "0"+duree;
		if (minutes<10) duree = duree+"0";
		duree = duree + String.valueOf(minutes);
		
		return duree;
	}

	public String dureeHeures() {
		String duree = "";
		int total = total().intValue();
		int heures = total/60;
		int minutes = total%60;
		
		duree = String.valueOf(heures)+":";
		if (heures<10) duree = "0"+duree;
		if (minutes<10) duree = duree+"0";
		duree = duree + String.valueOf(minutes);
		
		return duree;
	}

	/**
	 * @return
	 */
	public boolean isTypeSemaineHaute() {
		boolean isTypeSemaineHaute = false;

		if (flagHoraireSemaineHaute().equals(Integer.valueOf("1"))) {
			isTypeSemaineHaute = true;		
		}
		
		return isTypeSemaineHaute;
	}

	public void setIsTypeSemaineHaute(boolean value) {
	    if (value == true)
	        setFlagHoraireSemaineHaute(new Integer(1));
	    else
	        setFlagHoraireSemaineHaute(new Integer(0));
	}
	
	public boolean isHorsNorme() {
		boolean isHorsNorme = false;

		if (flagHorsNorme().equals(Integer.valueOf("1"))) {
		    isHorsNorme = true;		
		}
		
		return isHorsNorme;
	}
	
	public void setIsHorsNorme(boolean value) {
	    if (value == true)
	        setFlagHorsNorme(new Integer(1));
	    else
	        setFlagHorsNorme(new Integer(0));
	}
	
	public void setDureeHeures(String value) {
	    if (value == null)
	        value = "00:00";
	    setTotal(new Integer(TimeCtrl.getMinutes(value)));
	}

	public static EOTypeHoraire newDefaultRecordInContext(EOEditingContext ec) {
	    EOTypeHoraire typeHoraire = new EOTypeHoraire();
	    typeHoraire.setFlagHoraireSemaineHaute(new Integer(0));
	    typeHoraire.setFlagHorsNorme(new Integer(0));
	    typeHoraire.setTotal(new Integer(0));
	    ec.insertObject(typeHoraire);
	    return typeHoraire;
	}

	public static NSArray findAllTypeHoraire(EOEditingContext ec, boolean cacheHorsNorme) {
	    NSMutableDictionary bindings = new NSMutableDictionary();
	    if (cacheHorsNorme == true) {
	        bindings.setObjectForKey(new Integer(0), "horsNormes");
	    }
	    
	    NSArray lesTypesHoraires = EOUtilities.objectsWithFetchSpecificationAndBindings(
	    		ec, EOTypeHoraire.ENTITY_NAME, "TypesHoraireNormaux", bindings);
	 	
	    return EOSortOrdering.sortedArrayUsingKeyOrderArray(lesTypesHoraires, LRSort.newSort(TOTAL_KEY));		
	
	}
	

}
