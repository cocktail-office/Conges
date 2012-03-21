// _EOAdresse.java
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

// DO NOT EDIT.  Make changes to EOAdresse.java instead.
package fr.univlr.cri.conges.eos.modele.grhum;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOAdresse extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "Adresse";
	public static final String ENTITY_TABLE_NAME = "GRHUM.ADRESSE";

	// Attributes
	public static final String ADR_ADRESSE1_KEY = "adrAdresse1";
	public static final String ADR_ADRESSE2_KEY = "adrAdresse2";
	public static final String CODE_POSTAL_KEY = "codePostal";
	public static final String VILLE_KEY = "ville";

	public static final String ADR_ADRESSE1_COLKEY = "ADR_ADRESSE1";
	public static final String ADR_ADRESSE2_COLKEY = "ADR_ADRESSE2";
	public static final String CODE_POSTAL_COLKEY = "CODE_POSTAL";
	public static final String VILLE_COLKEY = "VILLE";

	// Relationships

	// Utilities methods
  public EOAdresse localInstanceIn(EOEditingContext editingContext) {
    EOAdresse localInstance = (EOAdresse)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public String adrAdresse1() {
    return (String) storedValueForKey("adrAdresse1");
  }

  public void setAdrAdresse1(String value) {
    takeStoredValueForKey(value, "adrAdresse1");
  }

  public String adrAdresse2() {
    return (String) storedValueForKey("adrAdresse2");
  }

  public void setAdrAdresse2(String value) {
    takeStoredValueForKey(value, "adrAdresse2");
  }

  public String codePostal() {
    return (String) storedValueForKey("codePostal");
  }

  public void setCodePostal(String value) {
    takeStoredValueForKey(value, "codePostal");
  }

  public String ville() {
    return (String) storedValueForKey("ville");
  }

  public void setVille(String value) {
    takeStoredValueForKey(value, "ville");
  }


  public static EOAdresse createAdresse(EOEditingContext editingContext) {
    EOAdresse eo = (EOAdresse) EOUtilities.createAndInsertInstance(editingContext, _EOAdresse.ENTITY_NAME);    
    return eo;
  }

  public static NSArray fetchAllAdresses(EOEditingContext editingContext) {
    return _EOAdresse.fetchAllAdresses(editingContext, null);
  }

  public static NSArray fetchAllAdresses(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOAdresse.fetchAdresses(editingContext, null, sortOrderings);
  }

  public static NSArray fetchAdresses(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOAdresse.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOAdresse fetchAdresse(EOEditingContext editingContext, String keyName, Object value) {
    return _EOAdresse.fetchAdresse(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOAdresse fetchAdresse(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOAdresse.fetchAdresses(editingContext, qualifier, null);
    EOAdresse eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOAdresse)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one Adresse that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOAdresse fetchRequiredAdresse(EOEditingContext editingContext, String keyName, Object value) {
    return _EOAdresse.fetchRequiredAdresse(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOAdresse fetchRequiredAdresse(EOEditingContext editingContext, EOQualifier qualifier) {
    EOAdresse eoObject = _EOAdresse.fetchAdresse(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no Adresse that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOAdresse localInstanceIn(EOEditingContext editingContext, EOAdresse eo) {
    EOAdresse localInstance = (eo == null) ? null : (EOAdresse)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
