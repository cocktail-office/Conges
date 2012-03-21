// _EOCET.java
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

// DO NOT EDIT.  Make changes to EOCET.java instead.
package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOCET extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "CET";
	public static final String ENTITY_TABLE_NAME = "CONGES.CNG_CET";

	// Attributes
	public static final String D_CREATION_KEY = "dCreation";
	public static final String D_MODIFICATION_KEY = "dModification";

	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String D_MODIFICATION_COLKEY = "D_MODIFICATION";

	// Relationships
	public static final String C_ET_TRANSACTIONS_KEY = "cETTransactions";
	public static final String INDIVIDU_KEY = "individu";

	// Utilities methods
  public EOCET localInstanceIn(EOEditingContext editingContext) {
    EOCET localInstance = (EOCET)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
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

  public fr.univlr.cri.conges.eos.modele.grhum.EOIndividu individu() {
    return (fr.univlr.cri.conges.eos.modele.grhum.EOIndividu)storedValueForKey("individu");
  }

  public void setIndividuRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOIndividu value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.grhum.EOIndividu oldValue = individu();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "individu");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "individu");
    }
  }
  
  public NSArray cETTransactions() {
    return (NSArray)storedValueForKey("cETTransactions");
  }

  public NSArray cETTransactions(EOQualifier qualifier) {
    return cETTransactions(qualifier, null, false);
  }

  public NSArray cETTransactions(EOQualifier qualifier, boolean fetch) {
    return cETTransactions(qualifier, null, fetch);
  }

  public NSArray cETTransactions(EOQualifier qualifier, NSArray sortOrderings, boolean fetch) {
    NSArray results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(fr.univlr.cri.conges.eos.modele.conges.EOCETTransaction.CET_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray qualifiers = new NSMutableArray();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = fr.univlr.cri.conges.eos.modele.conges.EOCETTransaction.fetchCETTransactions(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = cETTransactions();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToCETTransactionsRelationship(fr.univlr.cri.conges.eos.modele.conges.EOCETTransaction object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "cETTransactions");
  }

  public void removeFromCETTransactionsRelationship(fr.univlr.cri.conges.eos.modele.conges.EOCETTransaction object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "cETTransactions");
  }

  public fr.univlr.cri.conges.eos.modele.conges.EOCETTransaction createCETTransactionsRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("CETTransaction");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "cETTransactions");
    return (fr.univlr.cri.conges.eos.modele.conges.EOCETTransaction) eo;
  }

  public void deleteCETTransactionsRelationship(fr.univlr.cri.conges.eos.modele.conges.EOCETTransaction object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "cETTransactions");
    editingContext().deleteObject(object);
  }

  public void deleteAllCETTransactionsRelationships() {
    Enumeration objects = cETTransactions().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteCETTransactionsRelationship((fr.univlr.cri.conges.eos.modele.conges.EOCETTransaction)objects.nextElement());
    }
  }


  public static EOCET createCET(EOEditingContext editingContext, NSTimestamp dCreation
, NSTimestamp dModification
, fr.univlr.cri.conges.eos.modele.grhum.EOIndividu individu) {
    EOCET eo = (EOCET) EOUtilities.createAndInsertInstance(editingContext, _EOCET.ENTITY_NAME);    
		eo.setDCreation(dCreation);
		eo.setDModification(dModification);
    eo.setIndividuRelationship(individu);
    return eo;
  }

  public static NSArray fetchAllCETs(EOEditingContext editingContext) {
    return _EOCET.fetchAllCETs(editingContext, null);
  }

  public static NSArray fetchAllCETs(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOCET.fetchCETs(editingContext, null, sortOrderings);
  }

  public static NSArray fetchCETs(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOCET.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOCET fetchCET(EOEditingContext editingContext, String keyName, Object value) {
    return _EOCET.fetchCET(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOCET fetchCET(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOCET.fetchCETs(editingContext, qualifier, null);
    EOCET eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOCET)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one CET that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOCET fetchRequiredCET(EOEditingContext editingContext, String keyName, Object value) {
    return _EOCET.fetchRequiredCET(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOCET fetchRequiredCET(EOEditingContext editingContext, EOQualifier qualifier) {
    EOCET eoObject = _EOCET.fetchCET(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no CET that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOCET localInstanceIn(EOEditingContext editingContext, EOCET eo) {
    EOCET localInstance = (eo == null) ? null : (EOCET)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
