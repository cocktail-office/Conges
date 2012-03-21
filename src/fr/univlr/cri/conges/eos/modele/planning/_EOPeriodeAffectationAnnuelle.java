// _EOPeriodeAffectationAnnuelle.java
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

// DO NOT EDIT.  Make changes to EOPeriodeAffectationAnnuelle.java instead.
package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOPeriodeAffectationAnnuelle extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "PeriodeAffectationAnnuelle";
	public static final String ENTITY_TABLE_NAME = "CONGES.PLNG_PER_AFF_ANN";

	// Attributes
	public static final String DATE_DEBUT_KEY = "dateDebut";
	public static final String DATE_FIN_KEY = "dateFin";
	public static final String D_CREATION_KEY = "dCreation";
	public static final String D_MODIFICATION_KEY = "dModification";

	public static final String DATE_DEBUT_COLKEY = "DTE_DEBUT";
	public static final String DATE_FIN_COLKEY = "DTE_FIN";
	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String D_MODIFICATION_COLKEY = "D_MODIFICATION";

	// Relationships
	public static final String AFFECTATION_KEY = "affectation";
	public static final String AFFECTATION_ANNUELLE_KEY = "affectationAnnuelle";
	public static final String PLANNING_HEBDOMADAIRES_KEY = "planningHebdomadaires";

	// Utilities methods
  public EOPeriodeAffectationAnnuelle localInstanceIn(EOEditingContext editingContext) {
    EOPeriodeAffectationAnnuelle localInstance = (EOPeriodeAffectationAnnuelle)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public NSTimestamp dateDebut() {
    return (NSTimestamp) storedValueForKey("dateDebut");
  }

  public void setDateDebut(NSTimestamp value) {
    takeStoredValueForKey(value, "dateDebut");
  }

  public NSTimestamp dateFin() {
    return (NSTimestamp) storedValueForKey("dateFin");
  }

  public void setDateFin(NSTimestamp value) {
    takeStoredValueForKey(value, "dateFin");
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

  public fr.univlr.cri.conges.eos.modele.grhum.EOAffectation affectation() {
    return (fr.univlr.cri.conges.eos.modele.grhum.EOAffectation)storedValueForKey("affectation");
  }

  public void setAffectationRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOAffectation value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.grhum.EOAffectation oldValue = affectation();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "affectation");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "affectation");
    }
  }
  
  public fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle affectationAnnuelle() {
    return (fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle)storedValueForKey("affectationAnnuelle");
  }

  public void setAffectationAnnuelleRelationship(fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle oldValue = affectationAnnuelle();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "affectationAnnuelle");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "affectationAnnuelle");
    }
  }
  
  public NSArray planningHebdomadaires() {
    return (NSArray)storedValueForKey("planningHebdomadaires");
  }

  public NSArray planningHebdomadaires(EOQualifier qualifier) {
    return planningHebdomadaires(qualifier, null, false);
  }

  public NSArray planningHebdomadaires(EOQualifier qualifier, boolean fetch) {
    return planningHebdomadaires(qualifier, null, fetch);
  }

  public NSArray planningHebdomadaires(EOQualifier qualifier, NSArray sortOrderings, boolean fetch) {
    NSArray results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(fr.univlr.cri.conges.eos.modele.planning.EOPlanningHebdomadaire.PERIODE_AFFECTATION_ANNUELLE_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray qualifiers = new NSMutableArray();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = fr.univlr.cri.conges.eos.modele.planning.EOPlanningHebdomadaire.fetchPlanningHebdomadaires(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = planningHebdomadaires();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToPlanningHebdomadairesRelationship(fr.univlr.cri.conges.eos.modele.planning.EOPlanningHebdomadaire object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "planningHebdomadaires");
  }

  public void removeFromPlanningHebdomadairesRelationship(fr.univlr.cri.conges.eos.modele.planning.EOPlanningHebdomadaire object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "planningHebdomadaires");
  }

  public fr.univlr.cri.conges.eos.modele.planning.EOPlanningHebdomadaire createPlanningHebdomadairesRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("PlanningHebdomadaire");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "planningHebdomadaires");
    return (fr.univlr.cri.conges.eos.modele.planning.EOPlanningHebdomadaire) eo;
  }

  public void deletePlanningHebdomadairesRelationship(fr.univlr.cri.conges.eos.modele.planning.EOPlanningHebdomadaire object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "planningHebdomadaires");
    editingContext().deleteObject(object);
  }

  public void deleteAllPlanningHebdomadairesRelationships() {
    Enumeration objects = planningHebdomadaires().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deletePlanningHebdomadairesRelationship((fr.univlr.cri.conges.eos.modele.planning.EOPlanningHebdomadaire)objects.nextElement());
    }
  }


  public static EOPeriodeAffectationAnnuelle createPeriodeAffectationAnnuelle(EOEditingContext editingContext, NSTimestamp dCreation
, NSTimestamp dModification
, fr.univlr.cri.conges.eos.modele.grhum.EOAffectation affectation, fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle affectationAnnuelle) {
    EOPeriodeAffectationAnnuelle eo = (EOPeriodeAffectationAnnuelle) EOUtilities.createAndInsertInstance(editingContext, _EOPeriodeAffectationAnnuelle.ENTITY_NAME);    
		eo.setDCreation(dCreation);
		eo.setDModification(dModification);
    eo.setAffectationRelationship(affectation);
    eo.setAffectationAnnuelleRelationship(affectationAnnuelle);
    return eo;
  }

  public static NSArray fetchAllPeriodeAffectationAnnuelles(EOEditingContext editingContext) {
    return _EOPeriodeAffectationAnnuelle.fetchAllPeriodeAffectationAnnuelles(editingContext, null);
  }

  public static NSArray fetchAllPeriodeAffectationAnnuelles(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOPeriodeAffectationAnnuelle.fetchPeriodeAffectationAnnuelles(editingContext, null, sortOrderings);
  }

  public static NSArray fetchPeriodeAffectationAnnuelles(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOPeriodeAffectationAnnuelle.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOPeriodeAffectationAnnuelle fetchPeriodeAffectationAnnuelle(EOEditingContext editingContext, String keyName, Object value) {
    return _EOPeriodeAffectationAnnuelle.fetchPeriodeAffectationAnnuelle(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOPeriodeAffectationAnnuelle fetchPeriodeAffectationAnnuelle(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOPeriodeAffectationAnnuelle.fetchPeriodeAffectationAnnuelles(editingContext, qualifier, null);
    EOPeriodeAffectationAnnuelle eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOPeriodeAffectationAnnuelle)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one PeriodeAffectationAnnuelle that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOPeriodeAffectationAnnuelle fetchRequiredPeriodeAffectationAnnuelle(EOEditingContext editingContext, String keyName, Object value) {
    return _EOPeriodeAffectationAnnuelle.fetchRequiredPeriodeAffectationAnnuelle(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOPeriodeAffectationAnnuelle fetchRequiredPeriodeAffectationAnnuelle(EOEditingContext editingContext, EOQualifier qualifier) {
    EOPeriodeAffectationAnnuelle eoObject = _EOPeriodeAffectationAnnuelle.fetchPeriodeAffectationAnnuelle(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no PeriodeAffectationAnnuelle that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOPeriodeAffectationAnnuelle localInstanceIn(EOEditingContext editingContext, EOPeriodeAffectationAnnuelle eo) {
    EOPeriodeAffectationAnnuelle localInstance = (eo == null) ? null : (EOPeriodeAffectationAnnuelle)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
