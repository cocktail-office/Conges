// _EODbTodoCrash.java
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

// DO NOT EDIT.  Make changes to EODbTodoCrash.java instead.
package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EODbTodoCrash extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "DbTodoCrash";
	public static final String ENTITY_TABLE_NAME = "CONGES.DB_TODO_CRASH";

	// Attributes
	public static final String DTC_KEY_KEY = "dtcKey";
	public static final String DTC_MESSAGE_KEY = "dtcMessage";
	public static final String DTC_REQ_KEY = "dtcReq";

	public static final String DTC_KEY_COLKEY = "DTC_KEY";
	public static final String DTC_MESSAGE_COLKEY = "DTC_MESSAGE";
	public static final String DTC_REQ_COLKEY = "DTC_REQ";

	// Relationships

	// Utilities methods
  public EODbTodoCrash localInstanceIn(EOEditingContext editingContext) {
    EODbTodoCrash localInstance = (EODbTodoCrash)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public Integer dtcKey() {
    return (Integer) storedValueForKey("dtcKey");
  }

  public void setDtcKey(Integer value) {
    takeStoredValueForKey(value, "dtcKey");
  }

  public String dtcMessage() {
    return (String) storedValueForKey("dtcMessage");
  }

  public void setDtcMessage(String value) {
    takeStoredValueForKey(value, "dtcMessage");
  }

  public String dtcReq() {
    return (String) storedValueForKey("dtcReq");
  }

  public void setDtcReq(String value) {
    takeStoredValueForKey(value, "dtcReq");
  }


  public static EODbTodoCrash createDbTodoCrash(EOEditingContext editingContext, Integer dtcKey
, String dtcReq
) {
    EODbTodoCrash eo = (EODbTodoCrash) EOUtilities.createAndInsertInstance(editingContext, _EODbTodoCrash.ENTITY_NAME);    
		eo.setDtcKey(dtcKey);
		eo.setDtcReq(dtcReq);
    return eo;
  }

  public static NSArray fetchAllDbTodoCrashs(EOEditingContext editingContext) {
    return _EODbTodoCrash.fetchAllDbTodoCrashs(editingContext, null);
  }

  public static NSArray fetchAllDbTodoCrashs(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EODbTodoCrash.fetchDbTodoCrashs(editingContext, null, sortOrderings);
  }

  public static NSArray fetchDbTodoCrashs(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EODbTodoCrash.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EODbTodoCrash fetchDbTodoCrash(EOEditingContext editingContext, String keyName, Object value) {
    return _EODbTodoCrash.fetchDbTodoCrash(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EODbTodoCrash fetchDbTodoCrash(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EODbTodoCrash.fetchDbTodoCrashs(editingContext, qualifier, null);
    EODbTodoCrash eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EODbTodoCrash)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one DbTodoCrash that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EODbTodoCrash fetchRequiredDbTodoCrash(EOEditingContext editingContext, String keyName, Object value) {
    return _EODbTodoCrash.fetchRequiredDbTodoCrash(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EODbTodoCrash fetchRequiredDbTodoCrash(EOEditingContext editingContext, EOQualifier qualifier) {
    EODbTodoCrash eoObject = _EODbTodoCrash.fetchDbTodoCrash(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no DbTodoCrash that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EODbTodoCrash localInstanceIn(EOEditingContext editingContext, EODbTodoCrash eo) {
    EODbTodoCrash localInstance = (eo == null) ? null : (EODbTodoCrash)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
