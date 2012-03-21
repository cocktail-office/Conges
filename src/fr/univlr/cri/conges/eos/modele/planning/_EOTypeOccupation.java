// _EOTypeOccupation.java
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

// DO NOT EDIT.  Make changes to EOTypeOccupation.java instead.
package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOTypeOccupation extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "TypeOccupation";
	public static final String ENTITY_TABLE_NAME = "CONGES.PLNG_TYP_OCC";

	// Attributes
	public static final String CONGE_LEGAL_KEY = "congeLegal";
	public static final String D_CREATION_KEY = "dCreation";
	public static final String D_MODIFICATION_KEY = "dModification";
	public static final String FLAG_DRH_KEY = "flagDRH";
	public static final String LIBELLE_KEY = "libelle";
	public static final String LIBELLE_COURT_KEY = "libelleCourt";
	public static final String QUANTUM_KEY = "quantum";
	public static final String SENS_IMPUTATION_KEY = "sensImputation";

	public static final String CONGE_LEGAL_COLKEY = "CONGE_LEGAL";
	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String D_MODIFICATION_COLKEY = "D_MODIFICATION";
	public static final String FLAG_DRH_COLKEY = "FLG_DRH";
	public static final String LIBELLE_COLKEY = "LIBELLE";
	public static final String LIBELLE_COURT_COLKEY = "LIBELLE_COURT";
	public static final String QUANTUM_COLKEY = "QUANTUM";
	public static final String SENS_IMPUTATION_COLKEY = "SENS_IMPUTATION";

	// Relationships
	public static final String TOS_TYPE_OCCUPATION_PARAMETRE_KEY = "tosTypeOccupationParametre";

	// Utilities methods
  public EOTypeOccupation localInstanceIn(EOEditingContext editingContext) {
    EOTypeOccupation localInstance = (EOTypeOccupation)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public Integer congeLegal() {
    return (Integer) storedValueForKey("congeLegal");
  }

  public void setCongeLegal(Integer value) {
    takeStoredValueForKey(value, "congeLegal");
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

  public Integer flagDRH() {
    return (Integer) storedValueForKey("flagDRH");
  }

  public void setFlagDRH(Integer value) {
    takeStoredValueForKey(value, "flagDRH");
  }

  public String libelle() {
    return (String) storedValueForKey("libelle");
  }

  public void setLibelle(String value) {
    takeStoredValueForKey(value, "libelle");
  }

  public String libelleCourt() {
    return (String) storedValueForKey("libelleCourt");
  }

  public void setLibelleCourt(String value) {
    takeStoredValueForKey(value, "libelleCourt");
  }

  public String quantum() {
    return (String) storedValueForKey("quantum");
  }

  public void setQuantum(String value) {
    takeStoredValueForKey(value, "quantum");
  }

  public String sensImputation() {
    return (String) storedValueForKey("sensImputation");
  }

  public void setSensImputation(String value) {
    takeStoredValueForKey(value, "sensImputation");
  }

  public NSArray tosTypeOccupationParametre() {
    return (NSArray)storedValueForKey("tosTypeOccupationParametre");
  }

  public NSArray tosTypeOccupationParametre(EOQualifier qualifier) {
    return tosTypeOccupationParametre(qualifier, null, false);
  }

  public NSArray tosTypeOccupationParametre(EOQualifier qualifier, boolean fetch) {
    return tosTypeOccupationParametre(qualifier, null, fetch);
  }

  public NSArray tosTypeOccupationParametre(EOQualifier qualifier, NSArray sortOrderings, boolean fetch) {
    NSArray results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupationParametre.TO_TYPE_OCCUPATION_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray qualifiers = new NSMutableArray();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupationParametre.fetchTypeOccupationParametres(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = tosTypeOccupationParametre();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToTosTypeOccupationParametreRelationship(fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupationParametre object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "tosTypeOccupationParametre");
  }

  public void removeFromTosTypeOccupationParametreRelationship(fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupationParametre object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosTypeOccupationParametre");
  }

  public fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupationParametre createTosTypeOccupationParametreRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("TypeOccupationParametre");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "tosTypeOccupationParametre");
    return (fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupationParametre) eo;
  }

  public void deleteTosTypeOccupationParametreRelationship(fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupationParametre object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosTypeOccupationParametre");
    editingContext().deleteObject(object);
  }

  public void deleteAllTosTypeOccupationParametreRelationships() {
    Enumeration objects = tosTypeOccupationParametre().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteTosTypeOccupationParametreRelationship((fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupationParametre)objects.nextElement());
    }
  }


  public static EOTypeOccupation createTypeOccupation(EOEditingContext editingContext, Integer congeLegal
, NSTimestamp dCreation
, NSTimestamp dModification
, Integer flagDRH
, String quantum
, String sensImputation
) {
    EOTypeOccupation eo = (EOTypeOccupation) EOUtilities.createAndInsertInstance(editingContext, _EOTypeOccupation.ENTITY_NAME);    
		eo.setCongeLegal(congeLegal);
		eo.setDCreation(dCreation);
		eo.setDModification(dModification);
		eo.setFlagDRH(flagDRH);
		eo.setQuantum(quantum);
		eo.setSensImputation(sensImputation);
    return eo;
  }

  public static NSArray fetchAllTypeOccupations(EOEditingContext editingContext) {
    return _EOTypeOccupation.fetchAllTypeOccupations(editingContext, null);
  }

  public static NSArray fetchAllTypeOccupations(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOTypeOccupation.fetchTypeOccupations(editingContext, null, sortOrderings);
  }

  public static NSArray fetchTypeOccupations(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOTypeOccupation.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOTypeOccupation fetchTypeOccupation(EOEditingContext editingContext, String keyName, Object value) {
    return _EOTypeOccupation.fetchTypeOccupation(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOTypeOccupation fetchTypeOccupation(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOTypeOccupation.fetchTypeOccupations(editingContext, qualifier, null);
    EOTypeOccupation eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOTypeOccupation)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one TypeOccupation that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOTypeOccupation fetchRequiredTypeOccupation(EOEditingContext editingContext, String keyName, Object value) {
    return _EOTypeOccupation.fetchRequiredTypeOccupation(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOTypeOccupation fetchRequiredTypeOccupation(EOEditingContext editingContext, EOQualifier qualifier) {
    EOTypeOccupation eoObject = _EOTypeOccupation.fetchTypeOccupation(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no TypeOccupation that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOTypeOccupation localInstanceIn(EOEditingContext editingContext, EOTypeOccupation eo) {
    EOTypeOccupation localInstance = (eo == null) ? null : (EOTypeOccupation)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
