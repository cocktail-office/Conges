// _EOHoraire.java
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

// DO NOT EDIT.  Make changes to EOHoraire.java instead.
package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOHoraire extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "Horaire";
	public static final String ENTITY_TABLE_NAME = "CONGES.PLNG_HOR";

	// Attributes
	public static final String COULEUR_KEY = "couleur";
	public static final String D_CREATION_KEY = "dCreation";
	public static final String D_MODIFICATION_KEY = "dModification";
	public static final String DUREES_KEY = "durees";
	public static final String DUREES_AM_KEY = "dureesAM";
	public static final String DUREES_PM_KEY = "dureesPM";
	public static final String HORAIRES_KEY = "horaires";
	public static final String NOM_KEY = "nom";
	public static final String PAUSES_KEY = "pauses";
	public static final String QUOTITE_KEY = "quotite";

	public static final String COULEUR_COLKEY = "COULEUR";
	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String D_MODIFICATION_COLKEY = "D_MODIFICATION";
	public static final String DUREES_COLKEY = "DUREES";
	public static final String DUREES_AM_COLKEY = "DUREES_AM";
	public static final String DUREES_PM_COLKEY = "DUREES_PM";
	public static final String HORAIRES_COLKEY = "HORAIRES";
	public static final String NOM_COLKEY = "NOM";
	public static final String PAUSES_COLKEY = "PAUSES";
	public static final String QUOTITE_COLKEY = "QUOTITE";

	// Relationships
	public static final String AFFECTATION_ANNUELLE_KEY = "affectationAnnuelle";
	public static final String PLANNING_HEBDOMADAIRES_KEY = "planningHebdomadaires";

	// Utilities methods
  public EOHoraire localInstanceIn(EOEditingContext editingContext) {
    EOHoraire localInstance = (EOHoraire)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public String couleur() {
    return (String) storedValueForKey("couleur");
  }

  public void setCouleur(String value) {
    takeStoredValueForKey(value, "couleur");
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

  public String durees() {
    return (String) storedValueForKey("durees");
  }

  public void setDurees(String value) {
    takeStoredValueForKey(value, "durees");
  }

  public String dureesAM() {
    return (String) storedValueForKey("dureesAM");
  }

  public void setDureesAM(String value) {
    takeStoredValueForKey(value, "dureesAM");
  }

  public String dureesPM() {
    return (String) storedValueForKey("dureesPM");
  }

  public void setDureesPM(String value) {
    takeStoredValueForKey(value, "dureesPM");
  }

  public String horaires() {
    return (String) storedValueForKey("horaires");
  }

  public void setHoraires(String value) {
    takeStoredValueForKey(value, "horaires");
  }

  public String nom() {
    return (String) storedValueForKey("nom");
  }

  public void setNom(String value) {
    takeStoredValueForKey(value, "nom");
  }

  public String pauses() {
    return (String) storedValueForKey("pauses");
  }

  public void setPauses(String value) {
    takeStoredValueForKey(value, "pauses");
  }

  public Integer quotite() {
    return (Integer) storedValueForKey("quotite");
  }

  public void setQuotite(Integer value) {
    takeStoredValueForKey(value, "quotite");
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
      EOQualifier inverseQualifier = new EOKeyValueQualifier(fr.univlr.cri.conges.eos.modele.planning.EOPlanningHebdomadaire.HORAIRE_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
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


  public static EOHoraire createHoraire(EOEditingContext editingContext, NSTimestamp dCreation
, NSTimestamp dModification
) {
    EOHoraire eo = (EOHoraire) EOUtilities.createAndInsertInstance(editingContext, _EOHoraire.ENTITY_NAME);    
		eo.setDCreation(dCreation);
		eo.setDModification(dModification);
    return eo;
  }

  public static NSArray fetchAllHoraires(EOEditingContext editingContext) {
    return _EOHoraire.fetchAllHoraires(editingContext, null);
  }

  public static NSArray fetchAllHoraires(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOHoraire.fetchHoraires(editingContext, null, sortOrderings);
  }

  public static NSArray fetchHoraires(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOHoraire.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOHoraire fetchHoraire(EOEditingContext editingContext, String keyName, Object value) {
    return _EOHoraire.fetchHoraire(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOHoraire fetchHoraire(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOHoraire.fetchHoraires(editingContext, qualifier, null);
    EOHoraire eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOHoraire)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one Horaire that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOHoraire fetchRequiredHoraire(EOEditingContext editingContext, String keyName, Object value) {
    return _EOHoraire.fetchRequiredHoraire(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOHoraire fetchRequiredHoraire(EOEditingContext editingContext, EOQualifier qualifier) {
    EOHoraire eoObject = _EOHoraire.fetchHoraire(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no Horaire that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOHoraire localInstanceIn(EOEditingContext editingContext, EOHoraire eo) {
    EOHoraire localInstance = (eo == null) ? null : (EOHoraire)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
