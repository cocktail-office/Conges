// _EOPlanningHebdomadaire.java
/*
 * Copyright Cocktail, 2001-2008 
 * 
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software. You can use, 
 * modify and/or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 * 
 * As a counterpart to the access to the source code and rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty and the software's author, the holder of the
 * economic rights, and the successive licensors have only limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading, using, modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean that it is complicated to manipulate, and that also
 * therefore means that it is reserved for developers and experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and, more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */

// DO NOT EDIT.  Make changes to EOPlanningHebdomadaire.java instead.
package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOPlanningHebdomadaire extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "PlanningHebdomadaire";
	public static final String ENTITY_TABLE_NAME = "CONGES.PLNG_PLG_HEB";

	// Attributes
	public static final String DATE_DEBUT_SEMAINE_KEY = "dateDebutSemaine";
	public static final String D_CREATION_KEY = "dCreation";
	public static final String D_MODIFICATION_KEY = "dModification";
	public static final String FLAG_NATURE_KEY = "flagNature";

	public static final String DATE_DEBUT_SEMAINE_COLKEY = "DTE_DEB_SEMAINE";
	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String D_MODIFICATION_COLKEY = "D_MODIFICATION";
	public static final String FLAG_NATURE_COLKEY = "NATURE";

	// Relationships
	public static final String HORAIRE_KEY = "horaire";
	public static final String PERIODE_AFFECTATION_ANNUELLE_KEY = "periodeAffectationAnnuelle";

	// Utilities methods
  public EOPlanningHebdomadaire localInstanceIn(EOEditingContext editingContext) {
    EOPlanningHebdomadaire localInstance = (EOPlanningHebdomadaire)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public NSTimestamp dateDebutSemaine() {
    return (NSTimestamp) storedValueForKey("dateDebutSemaine");
  }

  public void setDateDebutSemaine(NSTimestamp value) {
    takeStoredValueForKey(value, "dateDebutSemaine");
  }

  public NSTimestamp dCreation() {
    return (NSTimestamp) storedValueForKey("dCreation");
  }

  public void setDCreation(NSTimestamp value) {
    takeStoredValueForKey(value, "dCreation");
  }

  public NSTimestamp dModification() {
    return (NSTimestamp) storedValueForKey("dModification");
  }

  public void setDModification(NSTimestamp value) {
    takeStoredValueForKey(value, "dModification");
  }

  public String flagNature() {
    return (String) storedValueForKey("flagNature");
  }

  public void setFlagNature(String value) {
    takeStoredValueForKey(value, "flagNature");
  }

  public fr.univlr.cri.conges.eos.modele.planning.EOHoraire horaire() {
    return (fr.univlr.cri.conges.eos.modele.planning.EOHoraire)storedValueForKey("horaire");
  }

  public void setHoraireRelationship(fr.univlr.cri.conges.eos.modele.planning.EOHoraire value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.planning.EOHoraire oldValue = horaire();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "horaire");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "horaire");
    }
  }
  
  public fr.univlr.cri.conges.eos.modele.planning.EOPeriodeAffectationAnnuelle periodeAffectationAnnuelle() {
    return (fr.univlr.cri.conges.eos.modele.planning.EOPeriodeAffectationAnnuelle)storedValueForKey("periodeAffectationAnnuelle");
  }

  public void setPeriodeAffectationAnnuelleRelationship(fr.univlr.cri.conges.eos.modele.planning.EOPeriodeAffectationAnnuelle value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.planning.EOPeriodeAffectationAnnuelle oldValue = periodeAffectationAnnuelle();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "periodeAffectationAnnuelle");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "periodeAffectationAnnuelle");
    }
  }
  

  public static EOPlanningHebdomadaire createPlanningHebdomadaire(EOEditingContext editingContext, NSTimestamp dCreation
, NSTimestamp dModification
) {
    EOPlanningHebdomadaire eo = (EOPlanningHebdomadaire) EOUtilities.createAndInsertInstance(editingContext, _EOPlanningHebdomadaire.ENTITY_NAME);    
		eo.setDCreation(dCreation);
		eo.setDModification(dModification);
    return eo;
  }

  public static NSArray fetchAllPlanningHebdomadaires(EOEditingContext editingContext) {
    return _EOPlanningHebdomadaire.fetchAllPlanningHebdomadaires(editingContext, null);
  }

  public static NSArray fetchAllPlanningHebdomadaires(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOPlanningHebdomadaire.fetchPlanningHebdomadaires(editingContext, null, sortOrderings);
  }

  public static NSArray fetchPlanningHebdomadaires(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOPlanningHebdomadaire.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOPlanningHebdomadaire fetchPlanningHebdomadaire(EOEditingContext editingContext, String keyName, Object value) {
    return _EOPlanningHebdomadaire.fetchPlanningHebdomadaire(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOPlanningHebdomadaire fetchPlanningHebdomadaire(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOPlanningHebdomadaire.fetchPlanningHebdomadaires(editingContext, qualifier, null);
    EOPlanningHebdomadaire eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOPlanningHebdomadaire)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one PlanningHebdomadaire that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOPlanningHebdomadaire fetchRequiredPlanningHebdomadaire(EOEditingContext editingContext, String keyName, Object value) {
    return _EOPlanningHebdomadaire.fetchRequiredPlanningHebdomadaire(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOPlanningHebdomadaire fetchRequiredPlanningHebdomadaire(EOEditingContext editingContext, EOQualifier qualifier) {
    EOPlanningHebdomadaire eoObject = _EOPlanningHebdomadaire.fetchPlanningHebdomadaire(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no PlanningHebdomadaire that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOPlanningHebdomadaire localInstanceIn(EOEditingContext editingContext, EOPlanningHebdomadaire eo) {
    EOPlanningHebdomadaire localInstance = (eo == null) ? null : (EOPlanningHebdomadaire)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
