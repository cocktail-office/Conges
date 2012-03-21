// EOPlanningHebdomadaire.java
// Created on Thu Jun 17 08:48:32  2004 by Apple EOModeler Version 5.2

package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.objects.Semaine;

public class EOPlanningHebdomadaire extends _EOPlanningHebdomadaire {

	/** classement chronologique */
	public static NSArray ARRAY_SORT  = new NSArray( new Object[] {
			EOSortOrdering.sortOrderingWithKey(DATE_DEBUT_SEMAINE_KEY, EOSortOrdering.CompareAscending)});
	/** filtrage des semaines reelles */
	public final static EOQualifier QUAL_REEL = EOQualifier.qualifierWithQualifierFormat(FLAG_NATURE_KEY+"='R'", null);
	/** filtrage des semaines reelles */
	public final static EOQualifier QUAL_PREV = EOQualifier.qualifierWithQualifierFormat(FLAG_NATURE_KEY+"='P'", null);
	/** filtrage des semaines reelles */
	public final static EOQualifier QUAL_TEST = EOQualifier.qualifierWithQualifierFormat(FLAG_NATURE_KEY+"='T'", null);
	
    public EOPlanningHebdomadaire() {
    	super();
    }

    public NSTimestamp dateFinSemaine() {
        return dateDebutSemaine().timestampByAddingGregorianUnits(0,0,6,0,0,0);
    }
    
	public void insertInEditingContext(EOEditingContext ec) {
		if (ec != null) {
			if (ec.globalIDForObject(this) == null) {
				ec.insertObject(this);
			}
		}
	}
	
	// ajouts pour correction bug semaine 52 
	
	private Semaine semaine;

	public final Semaine getSemaine() {
		return semaine;
	}

	public final void setSemaine(Semaine semaine) {
		this.semaine = semaine;
	}
	
	public final static String HAS_SEMAINE_KEY = "hasSemaine";
	
	public boolean hasSemaine() {
		boolean hasSemaine = false;
		
		if (getSemaine() != null) {
			hasSemaine = true;
		}

		return hasSemaine;
	}
}
