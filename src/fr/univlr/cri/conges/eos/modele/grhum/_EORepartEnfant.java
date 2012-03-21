// _EORepartEnfant.java
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

// DO NOT EDIT.  Make changes to EORepartEnfant.java instead.
package fr.univlr.cri.conges.eos.modele.grhum;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EORepartEnfant extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "RepartEnfant";
	public static final String ENTITY_TABLE_NAME = "GRHUM.REPART_ENFANT";

	// Attributes


	// Relationships
	public static final String TO_ENFANT_KEY = "toEnfant";

	// Utilities methods
  public EORepartEnfant localInstanceIn(EOEditingContext editingContext) {
    EORepartEnfant localInstance = (EORepartEnfant)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public fr.univlr.cri.conges.eos.modele.grhum.EOEnfant toEnfant() {
    return (fr.univlr.cri.conges.eos.modele.grhum.EOEnfant)storedValueForKey("toEnfant");
  }

  public void setToEnfantRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOEnfant value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.grhum.EOEnfant oldValue = toEnfant();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "toEnfant");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "toEnfant");
    }
  }
  

  public static EORepartEnfant createRepartEnfant(EOEditingContext editingContext, fr.univlr.cri.conges.eos.modele.grhum.EOEnfant toEnfant) {
    EORepartEnfant eo = (EORepartEnfant) EOUtilities.createAndInsertInstance(editingContext, _EORepartEnfant.ENTITY_NAME);    
    eo.setToEnfantRelationship(toEnfant);
    return eo;
  }

  public static NSArray fetchAllRepartEnfants(EOEditingContext editingContext) {
    return _EORepartEnfant.fetchAllRepartEnfants(editingContext, null);
  }

  public static NSArray fetchAllRepartEnfants(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EORepartEnfant.fetchRepartEnfants(editingContext, null, sortOrderings);
  }

  public static NSArray fetchRepartEnfants(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EORepartEnfant.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EORepartEnfant fetchRepartEnfant(EOEditingContext editingContext, String keyName, Object value) {
    return _EORepartEnfant.fetchRepartEnfant(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EORepartEnfant fetchRepartEnfant(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EORepartEnfant.fetchRepartEnfants(editingContext, qualifier, null);
    EORepartEnfant eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EORepartEnfant)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one RepartEnfant that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EORepartEnfant fetchRequiredRepartEnfant(EOEditingContext editingContext, String keyName, Object value) {
    return _EORepartEnfant.fetchRequiredRepartEnfant(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EORepartEnfant fetchRequiredRepartEnfant(EOEditingContext editingContext, EOQualifier qualifier) {
    EORepartEnfant eoObject = _EORepartEnfant.fetchRepartEnfant(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no RepartEnfant that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EORepartEnfant localInstanceIn(EOEditingContext editingContext, EORepartEnfant eo) {
    EORepartEnfant localInstance = (eo == null) ? null : (EORepartEnfant)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
