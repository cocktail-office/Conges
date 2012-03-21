// _EOMailTodoDone.java
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

// DO NOT EDIT.  Make changes to EOMailTodoDone.java instead.
package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOMailTodoDone extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "MailTodoDone";
	public static final String ENTITY_TABLE_NAME = "CONGES.MAIL_TODO_DONE";

	// Attributes
	public static final String MTD_KEY_KEY = "mtdKey";
	public static final String MTD_REQ_KEY = "mtdReq";

	public static final String MTD_KEY_COLKEY = "MTD_KEY";
	public static final String MTD_REQ_COLKEY = "MTD_REQ";

	// Relationships

	// Utilities methods
  public EOMailTodoDone localInstanceIn(EOEditingContext editingContext) {
    EOMailTodoDone localInstance = (EOMailTodoDone)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public Integer mtdKey() {
    return (Integer) storedValueForKey("mtdKey");
  }

  public void setMtdKey(Integer value) {
    takeStoredValueForKey(value, "mtdKey");
  }

  public String mtdReq() {
    return (String) storedValueForKey("mtdReq");
  }

  public void setMtdReq(String value) {
    takeStoredValueForKey(value, "mtdReq");
  }


  public static EOMailTodoDone createMailTodoDone(EOEditingContext editingContext, Integer mtdKey
, String mtdReq
) {
    EOMailTodoDone eo = (EOMailTodoDone) EOUtilities.createAndInsertInstance(editingContext, _EOMailTodoDone.ENTITY_NAME);    
		eo.setMtdKey(mtdKey);
		eo.setMtdReq(mtdReq);
    return eo;
  }

  public static NSArray fetchAllMailTodoDones(EOEditingContext editingContext) {
    return _EOMailTodoDone.fetchAllMailTodoDones(editingContext, null);
  }

  public static NSArray fetchAllMailTodoDones(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOMailTodoDone.fetchMailTodoDones(editingContext, null, sortOrderings);
  }

  public static NSArray fetchMailTodoDones(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOMailTodoDone.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOMailTodoDone fetchMailTodoDone(EOEditingContext editingContext, String keyName, Object value) {
    return _EOMailTodoDone.fetchMailTodoDone(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOMailTodoDone fetchMailTodoDone(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOMailTodoDone.fetchMailTodoDones(editingContext, qualifier, null);
    EOMailTodoDone eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOMailTodoDone)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one MailTodoDone that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOMailTodoDone fetchRequiredMailTodoDone(EOEditingContext editingContext, String keyName, Object value) {
    return _EOMailTodoDone.fetchRequiredMailTodoDone(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOMailTodoDone fetchRequiredMailTodoDone(EOEditingContext editingContext, EOQualifier qualifier) {
    EOMailTodoDone eoObject = _EOMailTodoDone.fetchMailTodoDone(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no MailTodoDone that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOMailTodoDone localInstanceIn(EOEditingContext editingContext, EOMailTodoDone eo) {
    EOMailTodoDone localInstance = (eo == null) ? null : (EOMailTodoDone)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
