// _EODbTodoDone.java
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

// DO NOT EDIT.  Make changes to EODbTodoDone.java instead.
package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EODbTodoDone extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "DbTodoDone";
	public static final String ENTITY_TABLE_NAME = "CONGES.DB_TODO_DONE";

	// Attributes
	public static final String DTD_KEY_KEY = "dtdKey";
	public static final String DTD_REQ_KEY = "dtdReq";

	public static final String DTD_KEY_COLKEY = "DTD_KEY";
	public static final String DTD_REQ_COLKEY = "DTD_REQ";

	// Relationships

	// Utilities methods
  public EODbTodoDone localInstanceIn(EOEditingContext editingContext) {
    EODbTodoDone localInstance = (EODbTodoDone)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public Integer dtdKey() {
    return (Integer) storedValueForKey("dtdKey");
  }

  public void setDtdKey(Integer value) {
    takeStoredValueForKey(value, "dtdKey");
  }

  public String dtdReq() {
    return (String) storedValueForKey("dtdReq");
  }

  public void setDtdReq(String value) {
    takeStoredValueForKey(value, "dtdReq");
  }


  public static EODbTodoDone createDbTodoDone(EOEditingContext editingContext, Integer dtdKey
, String dtdReq
) {
    EODbTodoDone eo = (EODbTodoDone) EOUtilities.createAndInsertInstance(editingContext, _EODbTodoDone.ENTITY_NAME);    
		eo.setDtdKey(dtdKey);
		eo.setDtdReq(dtdReq);
    return eo;
  }

  public static NSArray fetchAllDbTodoDones(EOEditingContext editingContext) {
    return _EODbTodoDone.fetchAllDbTodoDones(editingContext, null);
  }

  public static NSArray fetchAllDbTodoDones(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EODbTodoDone.fetchDbTodoDones(editingContext, null, sortOrderings);
  }

  public static NSArray fetchDbTodoDones(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EODbTodoDone.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EODbTodoDone fetchDbTodoDone(EOEditingContext editingContext, String keyName, Object value) {
    return _EODbTodoDone.fetchDbTodoDone(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EODbTodoDone fetchDbTodoDone(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EODbTodoDone.fetchDbTodoDones(editingContext, qualifier, null);
    EODbTodoDone eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EODbTodoDone)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one DbTodoDone that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EODbTodoDone fetchRequiredDbTodoDone(EOEditingContext editingContext, String keyName, Object value) {
    return _EODbTodoDone.fetchRequiredDbTodoDone(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EODbTodoDone fetchRequiredDbTodoDone(EOEditingContext editingContext, EOQualifier qualifier) {
    EODbTodoDone eoObject = _EODbTodoDone.fetchDbTodoDone(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no DbTodoDone that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EODbTodoDone localInstanceIn(EOEditingContext editingContext, EODbTodoDone eo) {
    EODbTodoDone localInstance = (eo == null) ? null : (EODbTodoDone)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
