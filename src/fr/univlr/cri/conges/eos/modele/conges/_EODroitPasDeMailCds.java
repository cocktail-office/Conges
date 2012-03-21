// _EODroitPasDeMailCds.java
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

// DO NOT EDIT.  Make changes to EODroitPasDeMailCds.java instead.
package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EODroitPasDeMailCds extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "DroitPasDeMailCds";
	public static final String ENTITY_TABLE_NAME = "CONGES.CNG_CDS_NO_MAIL";

	// Attributes
	public static final String C_STRUCTURE_KEY = "cStructure";
	public static final String D_CREATION_KEY = "dCreation";
	public static final String D_MODIFICATION_KEY = "dModification";

	public static final String C_STRUCTURE_COLKEY = "C_STRUCTURE";
	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String D_MODIFICATION_COLKEY = "D_MODIFICATION";

	// Relationships

	// Utilities methods
  public EODroitPasDeMailCds localInstanceIn(EOEditingContext editingContext) {
    EODroitPasDeMailCds localInstance = (EODroitPasDeMailCds)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public String cStructure() {
    return (String) storedValueForKey("cStructure");
  }

  public void setCStructure(String value) {
    takeStoredValueForKey(value, "cStructure");
  }

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


  public static EODroitPasDeMailCds createDroitPasDeMailCds(EOEditingContext editingContext, String cStructure
, NSTimestamp dCreation
, NSTimestamp dModification
) {
    EODroitPasDeMailCds eo = (EODroitPasDeMailCds) EOUtilities.createAndInsertInstance(editingContext, _EODroitPasDeMailCds.ENTITY_NAME);    
		eo.setCStructure(cStructure);
		eo.setDCreation(dCreation);
		eo.setDModification(dModification);
    return eo;
  }

  public static NSArray fetchAllDroitPasDeMailCdses(EOEditingContext editingContext) {
    return _EODroitPasDeMailCds.fetchAllDroitPasDeMailCdses(editingContext, null);
  }

  public static NSArray fetchAllDroitPasDeMailCdses(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EODroitPasDeMailCds.fetchDroitPasDeMailCdses(editingContext, null, sortOrderings);
  }

  public static NSArray fetchDroitPasDeMailCdses(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EODroitPasDeMailCds.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EODroitPasDeMailCds fetchDroitPasDeMailCds(EOEditingContext editingContext, String keyName, Object value) {
    return _EODroitPasDeMailCds.fetchDroitPasDeMailCds(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EODroitPasDeMailCds fetchDroitPasDeMailCds(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EODroitPasDeMailCds.fetchDroitPasDeMailCdses(editingContext, qualifier, null);
    EODroitPasDeMailCds eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EODroitPasDeMailCds)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one DroitPasDeMailCds that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EODroitPasDeMailCds fetchRequiredDroitPasDeMailCds(EOEditingContext editingContext, String keyName, Object value) {
    return _EODroitPasDeMailCds.fetchRequiredDroitPasDeMailCds(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EODroitPasDeMailCds fetchRequiredDroitPasDeMailCds(EOEditingContext editingContext, EOQualifier qualifier) {
    EODroitPasDeMailCds eoObject = _EODroitPasDeMailCds.fetchDroitPasDeMailCds(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no DroitPasDeMailCds that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EODroitPasDeMailCds localInstanceIn(EOEditingContext editingContext, EODroitPasDeMailCds eo) {
    EODroitPasDeMailCds localInstance = (eo == null) ? null : (EODroitPasDeMailCds)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
