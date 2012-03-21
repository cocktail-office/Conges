// _EODbTodo.java
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

// DO NOT EDIT.  Make changes to EODbTodo.java instead.
package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EODbTodo extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "DbTodo";
	public static final String ENTITY_TABLE_NAME = "CONGES.DB_TODO";

	// Attributes
	public static final String DTO_KEY_KEY = "dtoKey";
	public static final String DTO_REQ_KEY = "dtoReq";

	public static final String DTO_KEY_COLKEY = "DTO_KEY";
	public static final String DTO_REQ_COLKEY = "DTO_REQ";

	// Relationships

	// Utilities methods
  public EODbTodo localInstanceIn(EOEditingContext editingContext) {
    EODbTodo localInstance = (EODbTodo)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public Integer dtoKey() {
    return (Integer) storedValueForKey("dtoKey");
  }

  public void setDtoKey(Integer value) {
    takeStoredValueForKey(value, "dtoKey");
  }

  public String dtoReq() {
    return (String) storedValueForKey("dtoReq");
  }

  public void setDtoReq(String value) {
    takeStoredValueForKey(value, "dtoReq");
  }


  public static EODbTodo createDbTodo(EOEditingContext editingContext, Integer dtoKey
, String dtoReq
) {
    EODbTodo eo = (EODbTodo) EOUtilities.createAndInsertInstance(editingContext, _EODbTodo.ENTITY_NAME);    
		eo.setDtoKey(dtoKey);
		eo.setDtoReq(dtoReq);
    return eo;
  }

  public static NSArray fetchAllDbTodos(EOEditingContext editingContext) {
    return _EODbTodo.fetchAllDbTodos(editingContext, null);
  }

  public static NSArray fetchAllDbTodos(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EODbTodo.fetchDbTodos(editingContext, null, sortOrderings);
  }

  public static NSArray fetchDbTodos(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EODbTodo.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EODbTodo fetchDbTodo(EOEditingContext editingContext, String keyName, Object value) {
    return _EODbTodo.fetchDbTodo(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EODbTodo fetchDbTodo(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EODbTodo.fetchDbTodos(editingContext, qualifier, null);
    EODbTodo eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EODbTodo)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one DbTodo that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EODbTodo fetchRequiredDbTodo(EOEditingContext editingContext, String keyName, Object value) {
    return _EODbTodo.fetchRequiredDbTodo(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EODbTodo fetchRequiredDbTodo(EOEditingContext editingContext, EOQualifier qualifier) {
    EODbTodo eoObject = _EODbTodo.fetchDbTodo(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no DbTodo that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EODbTodo localInstanceIn(EOEditingContext editingContext, EODbTodo eo) {
    EODbTodo localInstance = (eo == null) ? null : (EODbTodo)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
