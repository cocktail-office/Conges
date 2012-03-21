// _EOTypeAbsenceGepeto.java
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

// DO NOT EDIT.  Make changes to EOTypeAbsenceGepeto.java instead.
package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOTypeAbsenceGepeto extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "TypeAbsenceGepeto";
	public static final String ENTITY_TABLE_NAME = "CONGES.CNG_TYPE_ABS_GEPETO";

	// Attributes
	public static final String ABS_TYPE_CODE_KEY = "absTypeCode";

	public static final String ABS_TYPE_CODE_COLKEY = "ABS_TYPE_CODE";

	// Relationships
	public static final String TO_TYPE_OCCUPATION_KEY = "toTypeOccupation";

	// Utilities methods
  public EOTypeAbsenceGepeto localInstanceIn(EOEditingContext editingContext) {
    EOTypeAbsenceGepeto localInstance = (EOTypeAbsenceGepeto)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public String absTypeCode() {
    return (String) storedValueForKey("absTypeCode");
  }

  public void setAbsTypeCode(String value) {
    takeStoredValueForKey(value, "absTypeCode");
  }

  public fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation toTypeOccupation() {
    return (fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation)storedValueForKey("toTypeOccupation");
  }

  public void setToTypeOccupationRelationship(fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation oldValue = toTypeOccupation();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "toTypeOccupation");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "toTypeOccupation");
    }
  }
  

  public static EOTypeAbsenceGepeto createTypeAbsenceGepeto(EOEditingContext editingContext, String absTypeCode
, fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation toTypeOccupation) {
    EOTypeAbsenceGepeto eo = (EOTypeAbsenceGepeto) EOUtilities.createAndInsertInstance(editingContext, _EOTypeAbsenceGepeto.ENTITY_NAME);    
		eo.setAbsTypeCode(absTypeCode);
    eo.setToTypeOccupationRelationship(toTypeOccupation);
    return eo;
  }

  public static NSArray fetchAllTypeAbsenceGepetos(EOEditingContext editingContext) {
    return _EOTypeAbsenceGepeto.fetchAllTypeAbsenceGepetos(editingContext, null);
  }

  public static NSArray fetchAllTypeAbsenceGepetos(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOTypeAbsenceGepeto.fetchTypeAbsenceGepetos(editingContext, null, sortOrderings);
  }

  public static NSArray fetchTypeAbsenceGepetos(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOTypeAbsenceGepeto.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOTypeAbsenceGepeto fetchTypeAbsenceGepeto(EOEditingContext editingContext, String keyName, Object value) {
    return _EOTypeAbsenceGepeto.fetchTypeAbsenceGepeto(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOTypeAbsenceGepeto fetchTypeAbsenceGepeto(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOTypeAbsenceGepeto.fetchTypeAbsenceGepetos(editingContext, qualifier, null);
    EOTypeAbsenceGepeto eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOTypeAbsenceGepeto)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one TypeAbsenceGepeto that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOTypeAbsenceGepeto fetchRequiredTypeAbsenceGepeto(EOEditingContext editingContext, String keyName, Object value) {
    return _EOTypeAbsenceGepeto.fetchRequiredTypeAbsenceGepeto(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOTypeAbsenceGepeto fetchRequiredTypeAbsenceGepeto(EOEditingContext editingContext, EOQualifier qualifier) {
    EOTypeAbsenceGepeto eoObject = _EOTypeAbsenceGepeto.fetchTypeAbsenceGepeto(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no TypeAbsenceGepeto that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOTypeAbsenceGepeto localInstanceIn(EOEditingContext editingContext, EOTypeAbsenceGepeto eo) {
    EOTypeAbsenceGepeto localInstance = (eo == null) ? null : (EOTypeAbsenceGepeto)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
