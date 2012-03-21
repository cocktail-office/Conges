// _EORepartition.java
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

// DO NOT EDIT.  Make changes to EORepartition.java instead.
package fr.univlr.cri.conges.eos.modele.grhum;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EORepartition extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "Repartition";
	public static final String ENTITY_TABLE_NAME = "GRHUM.REPART_STRUCTURE";

	// Attributes


	// Relationships
	public static final String INDIVIDU_KEY = "individu";
	public static final String STRUCTURE_KEY = "structure";

	// Utilities methods
  public EORepartition localInstanceIn(EOEditingContext editingContext) {
    EORepartition localInstance = (EORepartition)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
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
  
  public fr.univlr.cri.conges.eos.modele.grhum.EOStructure structure() {
    return (fr.univlr.cri.conges.eos.modele.grhum.EOStructure)storedValueForKey("structure");
  }

  public void setStructureRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOStructure value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.grhum.EOStructure oldValue = structure();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "structure");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "structure");
    }
  }
  

  public static EORepartition createRepartition(EOEditingContext editingContext, fr.univlr.cri.conges.eos.modele.grhum.EOIndividu individu, fr.univlr.cri.conges.eos.modele.grhum.EOStructure structure) {
    EORepartition eo = (EORepartition) EOUtilities.createAndInsertInstance(editingContext, _EORepartition.ENTITY_NAME);    
    eo.setIndividuRelationship(individu);
    eo.setStructureRelationship(structure);
    return eo;
  }

  public static NSArray fetchAllRepartitions(EOEditingContext editingContext) {
    return _EORepartition.fetchAllRepartitions(editingContext, null);
  }

  public static NSArray fetchAllRepartitions(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EORepartition.fetchRepartitions(editingContext, null, sortOrderings);
  }

  public static NSArray fetchRepartitions(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EORepartition.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EORepartition fetchRepartition(EOEditingContext editingContext, String keyName, Object value) {
    return _EORepartition.fetchRepartition(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EORepartition fetchRepartition(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EORepartition.fetchRepartitions(editingContext, qualifier, null);
    EORepartition eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EORepartition)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one Repartition that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EORepartition fetchRequiredRepartition(EOEditingContext editingContext, String keyName, Object value) {
    return _EORepartition.fetchRequiredRepartition(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EORepartition fetchRequiredRepartition(EOEditingContext editingContext, EOQualifier qualifier) {
    EORepartition eoObject = _EORepartition.fetchRepartition(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no Repartition that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EORepartition localInstanceIn(EOEditingContext editingContext, EORepartition eo) {
    EORepartition localInstance = (eo == null) ? null : (EORepartition)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
