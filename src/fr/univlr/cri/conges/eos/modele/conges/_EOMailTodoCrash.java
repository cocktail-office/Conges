// _EOMailTodoCrash.java
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

// DO NOT EDIT.  Make changes to EOMailTodoCrash.java instead.
package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOMailTodoCrash extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "MailTodoCrash";
	public static final String ENTITY_TABLE_NAME = "CONGES.MAIL_TODO_CRASH";

	// Attributes
	public static final String MTC_KEY_KEY = "mtcKey";
	public static final String MTC_MESSAGE_KEY = "mtcMessage";
	public static final String MTC_REQ_KEY = "mtcReq";

	public static final String MTC_KEY_COLKEY = "MTC_KEY";
	public static final String MTC_MESSAGE_COLKEY = "MTC_MESSAGE";
	public static final String MTC_REQ_COLKEY = "MTC_REQ";

	// Relationships

	// Utilities methods
  public EOMailTodoCrash localInstanceIn(EOEditingContext editingContext) {
    EOMailTodoCrash localInstance = (EOMailTodoCrash)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public Integer mtcKey() {
    return (Integer) storedValueForKey("mtcKey");
  }

  public void setMtcKey(Integer value) {
    takeStoredValueForKey(value, "mtcKey");
  }

  public String mtcMessage() {
    return (String) storedValueForKey("mtcMessage");
  }

  public void setMtcMessage(String value) {
    takeStoredValueForKey(value, "mtcMessage");
  }

  public String mtcReq() {
    return (String) storedValueForKey("mtcReq");
  }

  public void setMtcReq(String value) {
    takeStoredValueForKey(value, "mtcReq");
  }


  public static EOMailTodoCrash createMailTodoCrash(EOEditingContext editingContext, Integer mtcKey
, String mtcReq
) {
    EOMailTodoCrash eo = (EOMailTodoCrash) EOUtilities.createAndInsertInstance(editingContext, _EOMailTodoCrash.ENTITY_NAME);    
		eo.setMtcKey(mtcKey);
		eo.setMtcReq(mtcReq);
    return eo;
  }

  public static NSArray fetchAllMailTodoCrashs(EOEditingContext editingContext) {
    return _EOMailTodoCrash.fetchAllMailTodoCrashs(editingContext, null);
  }

  public static NSArray fetchAllMailTodoCrashs(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOMailTodoCrash.fetchMailTodoCrashs(editingContext, null, sortOrderings);
  }

  public static NSArray fetchMailTodoCrashs(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOMailTodoCrash.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOMailTodoCrash fetchMailTodoCrash(EOEditingContext editingContext, String keyName, Object value) {
    return _EOMailTodoCrash.fetchMailTodoCrash(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOMailTodoCrash fetchMailTodoCrash(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOMailTodoCrash.fetchMailTodoCrashs(editingContext, qualifier, null);
    EOMailTodoCrash eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOMailTodoCrash)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one MailTodoCrash that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOMailTodoCrash fetchRequiredMailTodoCrash(EOEditingContext editingContext, String keyName, Object value) {
    return _EOMailTodoCrash.fetchRequiredMailTodoCrash(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOMailTodoCrash fetchRequiredMailTodoCrash(EOEditingContext editingContext, EOQualifier qualifier) {
    EOMailTodoCrash eoObject = _EOMailTodoCrash.fetchMailTodoCrash(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no MailTodoCrash that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOMailTodoCrash localInstanceIn(EOEditingContext editingContext, EOMailTodoCrash eo) {
    EOMailTodoCrash localInstance = (eo == null) ? null : (EOMailTodoCrash)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
