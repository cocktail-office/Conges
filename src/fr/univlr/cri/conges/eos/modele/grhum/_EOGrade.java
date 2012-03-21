// _EOGrade.java
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

// DO NOT EDIT.  Make changes to EOGrade.java instead.
package fr.univlr.cri.conges.eos.modele.grhum;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOGrade extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "Grade";
	public static final String ENTITY_TABLE_NAME = "GRHUM.GRADE";

	// Attributes
	public static final String LL_GRADE_KEY = "llGrade";

	public static final String LL_GRADE_COLKEY = "LL_GRADE";

	// Relationships

	// Utilities methods
  public EOGrade localInstanceIn(EOEditingContext editingContext) {
    EOGrade localInstance = (EOGrade)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public String llGrade() {
    return (String) storedValueForKey("llGrade");
  }

  public void setLlGrade(String value) {
    takeStoredValueForKey(value, "llGrade");
  }


  public static EOGrade createGrade(EOEditingContext editingContext, String llGrade
) {
    EOGrade eo = (EOGrade) EOUtilities.createAndInsertInstance(editingContext, _EOGrade.ENTITY_NAME);    
		eo.setLlGrade(llGrade);
    return eo;
  }

  public static NSArray fetchAllGrades(EOEditingContext editingContext) {
    return _EOGrade.fetchAllGrades(editingContext, null);
  }

  public static NSArray fetchAllGrades(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOGrade.fetchGrades(editingContext, null, sortOrderings);
  }

  public static NSArray fetchGrades(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOGrade.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOGrade fetchGrade(EOEditingContext editingContext, String keyName, Object value) {
    return _EOGrade.fetchGrade(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOGrade fetchGrade(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOGrade.fetchGrades(editingContext, qualifier, null);
    EOGrade eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOGrade)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one Grade that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOGrade fetchRequiredGrade(EOEditingContext editingContext, String keyName, Object value) {
    return _EOGrade.fetchRequiredGrade(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOGrade fetchRequiredGrade(EOEditingContext editingContext, EOQualifier qualifier) {
    EOGrade eoObject = _EOGrade.fetchGrade(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no Grade that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOGrade localInstanceIn(EOEditingContext editingContext, EOGrade eo) {
    EOGrade localInstance = (eo == null) ? null : (EOGrade)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
