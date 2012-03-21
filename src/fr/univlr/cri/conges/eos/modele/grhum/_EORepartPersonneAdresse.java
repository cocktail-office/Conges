// _EORepartPersonneAdresse.java
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

// DO NOT EDIT.  Make changes to EORepartPersonneAdresse.java instead.
package fr.univlr.cri.conges.eos.modele.grhum;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EORepartPersonneAdresse extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "RepartPersonneAdresse";
	public static final String ENTITY_TABLE_NAME = "GRHUM.REPART_PERSONNE_ADRESSE";

	// Attributes


	// Relationships
	public static final String TO_ADRESSE_KEY = "toAdresse";

	// Utilities methods
  public EORepartPersonneAdresse localInstanceIn(EOEditingContext editingContext) {
    EORepartPersonneAdresse localInstance = (EORepartPersonneAdresse)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public fr.univlr.cri.conges.eos.modele.grhum.EOAdresse toAdresse() {
    return (fr.univlr.cri.conges.eos.modele.grhum.EOAdresse)storedValueForKey("toAdresse");
  }

  public void setToAdresseRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOAdresse value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.grhum.EOAdresse oldValue = toAdresse();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "toAdresse");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "toAdresse");
    }
  }
  

  public static EORepartPersonneAdresse createRepartPersonneAdresse(EOEditingContext editingContext, fr.univlr.cri.conges.eos.modele.grhum.EOAdresse toAdresse) {
    EORepartPersonneAdresse eo = (EORepartPersonneAdresse) EOUtilities.createAndInsertInstance(editingContext, _EORepartPersonneAdresse.ENTITY_NAME);    
    eo.setToAdresseRelationship(toAdresse);
    return eo;
  }

  public static NSArray fetchAllRepartPersonneAdresses(EOEditingContext editingContext) {
    return _EORepartPersonneAdresse.fetchAllRepartPersonneAdresses(editingContext, null);
  }

  public static NSArray fetchAllRepartPersonneAdresses(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EORepartPersonneAdresse.fetchRepartPersonneAdresses(editingContext, null, sortOrderings);
  }

  public static NSArray fetchRepartPersonneAdresses(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EORepartPersonneAdresse.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EORepartPersonneAdresse fetchRepartPersonneAdresse(EOEditingContext editingContext, String keyName, Object value) {
    return _EORepartPersonneAdresse.fetchRepartPersonneAdresse(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EORepartPersonneAdresse fetchRepartPersonneAdresse(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EORepartPersonneAdresse.fetchRepartPersonneAdresses(editingContext, qualifier, null);
    EORepartPersonneAdresse eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EORepartPersonneAdresse)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one RepartPersonneAdresse that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EORepartPersonneAdresse fetchRequiredRepartPersonneAdresse(EOEditingContext editingContext, String keyName, Object value) {
    return _EORepartPersonneAdresse.fetchRequiredRepartPersonneAdresse(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EORepartPersonneAdresse fetchRequiredRepartPersonneAdresse(EOEditingContext editingContext, EOQualifier qualifier) {
    EORepartPersonneAdresse eoObject = _EORepartPersonneAdresse.fetchRepartPersonneAdresse(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no RepartPersonneAdresse that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EORepartPersonneAdresse localInstanceIn(EOEditingContext editingContext, EORepartPersonneAdresse eo) {
    EORepartPersonneAdresse localInstance = (eo == null) ? null : (EORepartPersonneAdresse)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
