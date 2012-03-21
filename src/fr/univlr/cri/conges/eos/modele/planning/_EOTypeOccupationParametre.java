// _EOTypeOccupationParametre.java
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

// DO NOT EDIT.  Make changes to EOTypeOccupationParametre.java instead.
package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOTypeOccupationParametre extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "TypeOccupationParametre";
	public static final String ENTITY_TABLE_NAME = "CONGES.PLNG_TYP_OCC_PARAM";

	// Attributes
	public static final String CONTRAINTE_KEY = "contrainte";
	public static final String D_CREATION_KEY = "dCreation";
	public static final String D_DEB_VALIDITE_KEY = "dDebValidite";
	public static final String D_FIN_VALIDITE_KEY = "dFinValidite";
	public static final String D_MODIFICATION_KEY = "dModification";

	public static final String CONTRAINTE_COLKEY = "CONTRAINTE";
	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String D_DEB_VALIDITE_COLKEY = "D_DEB_VALIDITE";
	public static final String D_FIN_VALIDITE_COLKEY = "D_FIN_VALIDITE";
	public static final String D_MODIFICATION_COLKEY = "D_MODIFICATION";

	// Relationships
	public static final String TO_TYPE_OCCUPATION_KEY = "toTypeOccupation";

	// Utilities methods
  public EOTypeOccupationParametre localInstanceIn(EOEditingContext editingContext) {
    EOTypeOccupationParametre localInstance = (EOTypeOccupationParametre)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public String contrainte() {
    return (String) storedValueForKey("contrainte");
  }

  public void setContrainte(String value) {
    takeStoredValueForKey(value, "contrainte");
  }

  public NSTimestamp dCreation() {
    return (NSTimestamp) storedValueForKey("dCreation");
  }

  public void setDCreation(NSTimestamp value) {
    takeStoredValueForKey(value, "dCreation");
  }

  public NSTimestamp dDebValidite() {
    return (NSTimestamp) storedValueForKey("dDebValidite");
  }

  public void setDDebValidite(NSTimestamp value) {
    takeStoredValueForKey(value, "dDebValidite");
  }

  public NSTimestamp dFinValidite() {
    return (NSTimestamp) storedValueForKey("dFinValidite");
  }

  public void setDFinValidite(NSTimestamp value) {
    takeStoredValueForKey(value, "dFinValidite");
  }

  public NSTimestamp dModification() {
    return (NSTimestamp) storedValueForKey("dModification");
  }

  public void setDModification(NSTimestamp value) {
    takeStoredValueForKey(value, "dModification");
  }

  public fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation toTypeOccupation() {
    return (fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation)storedValueForKey("toTypeOccupation");
  }

  public void setToTypeOccupationRelationship(fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation oldValue = toTypeOccupation();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "toTypeOccupation");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "toTypeOccupation");
    }
  }
  

  public static EOTypeOccupationParametre createTypeOccupationParametre(EOEditingContext editingContext, String contrainte
, NSTimestamp dCreation
, NSTimestamp dModification
, fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation toTypeOccupation) {
    EOTypeOccupationParametre eo = (EOTypeOccupationParametre) EOUtilities.createAndInsertInstance(editingContext, _EOTypeOccupationParametre.ENTITY_NAME);    
		eo.setContrainte(contrainte);
		eo.setDCreation(dCreation);
		eo.setDModification(dModification);
    eo.setToTypeOccupationRelationship(toTypeOccupation);
    return eo;
  }

  public static NSArray fetchAllTypeOccupationParametres(EOEditingContext editingContext) {
    return _EOTypeOccupationParametre.fetchAllTypeOccupationParametres(editingContext, null);
  }

  public static NSArray fetchAllTypeOccupationParametres(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOTypeOccupationParametre.fetchTypeOccupationParametres(editingContext, null, sortOrderings);
  }

  public static NSArray fetchTypeOccupationParametres(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOTypeOccupationParametre.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOTypeOccupationParametre fetchTypeOccupationParametre(EOEditingContext editingContext, String keyName, Object value) {
    return _EOTypeOccupationParametre.fetchTypeOccupationParametre(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOTypeOccupationParametre fetchTypeOccupationParametre(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOTypeOccupationParametre.fetchTypeOccupationParametres(editingContext, qualifier, null);
    EOTypeOccupationParametre eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOTypeOccupationParametre)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one TypeOccupationParametre that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOTypeOccupationParametre fetchRequiredTypeOccupationParametre(EOEditingContext editingContext, String keyName, Object value) {
    return _EOTypeOccupationParametre.fetchRequiredTypeOccupationParametre(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOTypeOccupationParametre fetchRequiredTypeOccupationParametre(EOEditingContext editingContext, EOQualifier qualifier) {
    EOTypeOccupationParametre eoObject = _EOTypeOccupationParametre.fetchTypeOccupationParametre(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no TypeOccupationParametre that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOTypeOccupationParametre localInstanceIn(EOEditingContext editingContext, EOTypeOccupationParametre eo) {
    EOTypeOccupationParametre localInstance = (eo == null) ? null : (EOTypeOccupationParametre)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
