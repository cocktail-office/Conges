// _EOMailTodo.java
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

// DO NOT EDIT.  Make changes to EOMailTodo.java instead.
package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOMailTodo extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "MailTodo";
	public static final String ENTITY_TABLE_NAME = "CONGES.MAIL_TODO";

	// Attributes
	public static final String MTO_KEY_KEY = "mtoKey";
	public static final String MTO_REQ_KEY = "mtoReq";

	public static final String MTO_KEY_COLKEY = "MTO_KEY";
	public static final String MTO_REQ_COLKEY = "MTO_REQ";

	// Relationships

	// Utilities methods
  public EOMailTodo localInstanceIn(EOEditingContext editingContext) {
    EOMailTodo localInstance = (EOMailTodo)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public Integer mtoKey() {
    return (Integer) storedValueForKey("mtoKey");
  }

  public void setMtoKey(Integer value) {
    takeStoredValueForKey(value, "mtoKey");
  }

  public String mtoReq() {
    return (String) storedValueForKey("mtoReq");
  }

  public void setMtoReq(String value) {
    takeStoredValueForKey(value, "mtoReq");
  }


  public static EOMailTodo createMailTodo(EOEditingContext editingContext, Integer mtoKey
, String mtoReq
) {
    EOMailTodo eo = (EOMailTodo) EOUtilities.createAndInsertInstance(editingContext, _EOMailTodo.ENTITY_NAME);    
		eo.setMtoKey(mtoKey);
		eo.setMtoReq(mtoReq);
    return eo;
  }

  public static NSArray fetchAllMailTodos(EOEditingContext editingContext) {
    return _EOMailTodo.fetchAllMailTodos(editingContext, null);
  }

  public static NSArray fetchAllMailTodos(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOMailTodo.fetchMailTodos(editingContext, null, sortOrderings);
  }

  public static NSArray fetchMailTodos(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOMailTodo.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOMailTodo fetchMailTodo(EOEditingContext editingContext, String keyName, Object value) {
    return _EOMailTodo.fetchMailTodo(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOMailTodo fetchMailTodo(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOMailTodo.fetchMailTodos(editingContext, qualifier, null);
    EOMailTodo eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOMailTodo)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one MailTodo that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOMailTodo fetchRequiredMailTodo(EOEditingContext editingContext, String keyName, Object value) {
    return _EOMailTodo.fetchRequiredMailTodo(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOMailTodo fetchRequiredMailTodo(EOEditingContext editingContext, EOQualifier qualifier) {
    EOMailTodo eoObject = _EOMailTodo.fetchMailTodo(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no MailTodo that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOMailTodo localInstanceIn(EOEditingContext editingContext, EOMailTodo eo) {
    EOMailTodo localInstance = (eo == null) ? null : (EOMailTodo)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
