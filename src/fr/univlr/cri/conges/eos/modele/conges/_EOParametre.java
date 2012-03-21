// _EOParametre.java
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

// DO NOT EDIT.  Make changes to EOParametre.java instead.
package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOParametre extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "Parametre";
	public static final String ENTITY_TABLE_NAME = "CONGES.CNG_PARAMETRE";

	// Attributes
	public static final String D_CREATION_KEY = "dCreation";
	public static final String D_MODIFICATION_KEY = "dModification";
	public static final String PARAM_KEY_KEY = "paramKey";
	public static final String PARAM_VALUE_KEY = "paramValue";

	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String D_MODIFICATION_COLKEY = "D_MODIFICATION";
	public static final String PARAM_KEY_COLKEY = "PARAM_KEY";
	public static final String PARAM_VALUE_COLKEY = "PARAM_VALUE";

	// Relationships

	// Utilities methods
  public EOParametre localInstanceIn(EOEditingContext editingContext) {
    EOParametre localInstance = (EOParametre)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public NSTimestamp dCreation() {
    return (NSTimestamp) storedValueForKey("dCreation");
  }

  public void setDCreation(NSTimestamp value) {
    takeStoredValueForKey(value, "dCreation");
  }

  public NSTimestamp dModification() {
    return (NSTimestamp) storedValueForKey("dModification");
  }

  public void setDModification(NSTimestamp value) {
    takeStoredValueForKey(value, "dModification");
  }

  public String paramKey() {
    return (String) storedValueForKey("paramKey");
  }

  public void setParamKey(String value) {
    takeStoredValueForKey(value, "paramKey");
  }

  public String paramValue() {
    return (String) storedValueForKey("paramValue");
  }

  public void setParamValue(String value) {
    takeStoredValueForKey(value, "paramValue");
  }


  public static EOParametre createParametre(EOEditingContext editingContext, NSTimestamp dCreation
, NSTimestamp dModification
, String paramKey
) {
    EOParametre eo = (EOParametre) EOUtilities.createAndInsertInstance(editingContext, _EOParametre.ENTITY_NAME);    
		eo.setDCreation(dCreation);
		eo.setDModification(dModification);
		eo.setParamKey(paramKey);
    return eo;
  }

  public static NSArray fetchAllParametres(EOEditingContext editingContext) {
    return _EOParametre.fetchAllParametres(editingContext, null);
  }

  public static NSArray fetchAllParametres(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOParametre.fetchParametres(editingContext, null, sortOrderings);
  }

  public static NSArray fetchParametres(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOParametre.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOParametre fetchParametre(EOEditingContext editingContext, String keyName, Object value) {
    return _EOParametre.fetchParametre(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOParametre fetchParametre(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOParametre.fetchParametres(editingContext, qualifier, null);
    EOParametre eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOParametre)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one Parametre that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOParametre fetchRequiredParametre(EOEditingContext editingContext, String keyName, Object value) {
    return _EOParametre.fetchRequiredParametre(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOParametre fetchRequiredParametre(EOEditingContext editingContext, EOQualifier qualifier) {
    EOParametre eoObject = _EOParametre.fetchParametre(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no Parametre that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOParametre localInstanceIn(EOEditingContext editingContext, EOParametre eo) {
    EOParametre localInstance = (eo == null) ? null : (EOParametre)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
