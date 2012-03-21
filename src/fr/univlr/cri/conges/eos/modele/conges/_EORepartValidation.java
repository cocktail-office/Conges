// _EORepartValidation.java
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

// DO NOT EDIT.  Make changes to EORepartValidation.java instead.
package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EORepartValidation extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "RepartValidation";
	public static final String ENTITY_TABLE_NAME = "CONGES.CNG_HIST_VAL_VIS";

	// Attributes
	public static final String D_CREATION_KEY = "dCreation";
	public static final String D_MODIFICATION_KEY = "dModification";
	public static final String OID_INDIVIDU_KEY = "oidIndividu";
	public static final String TYPE_KEY = "type";

	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String D_MODIFICATION_COLKEY = "D_MODIFICATION";
	public static final String OID_INDIVIDU_COLKEY = "OID_INDIVIDU";
	public static final String TYPE_COLKEY = "TYP_VAL";

	// Relationships
	public static final String AFFECTATION_ANNUELLE_KEY = "affectationAnnuelle";
	public static final String INDIVIDU_KEY = "individu";
	public static final String OCCUPATION_KEY = "occupation";

	// Utilities methods
  public EORepartValidation localInstanceIn(EOEditingContext editingContext) {
    EORepartValidation localInstance = (EORepartValidation)EOUtilities.localInstanceOfObject(editingContext, this);
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

  public Integer oidIndividu() {
    return (Integer) storedValueForKey("oidIndividu");
  }

  public void setOidIndividu(Integer value) {
    takeStoredValueForKey(value, "oidIndividu");
  }

  public String type() {
    return (String) storedValueForKey("type");
  }

  public void setType(String value) {
    takeStoredValueForKey(value, "type");
  }

  public fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle affectationAnnuelle() {
    return (fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle)storedValueForKey("affectationAnnuelle");
  }

  public void setAffectationAnnuelleRelationship(fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle oldValue = affectationAnnuelle();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "affectationAnnuelle");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "affectationAnnuelle");
    }
  }
  
  public fr.univlr.cri.conges.eos.modele.grhum.EOIndividu individu() {
    return (fr.univlr.cri.conges.eos.modele.grhum.EOIndividu)storedValueForKey("individu");
  }

  public void setIndividuRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOIndividu value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.grhum.EOIndividu oldValue = individu();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "individu");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "individu");
    }
  }
  
  public fr.univlr.cri.conges.eos.modele.planning.EOOccupation occupation() {
    return (fr.univlr.cri.conges.eos.modele.planning.EOOccupation)storedValueForKey("occupation");
  }

  public void setOccupationRelationship(fr.univlr.cri.conges.eos.modele.planning.EOOccupation value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.planning.EOOccupation oldValue = occupation();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "occupation");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "occupation");
    }
  }
  

  public static EORepartValidation createRepartValidation(EOEditingContext editingContext, NSTimestamp dCreation
, NSTimestamp dModification
, Integer oidIndividu
, String type
, fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle affectationAnnuelle, fr.univlr.cri.conges.eos.modele.grhum.EOIndividu individu) {
    EORepartValidation eo = (EORepartValidation) EOUtilities.createAndInsertInstance(editingContext, _EORepartValidation.ENTITY_NAME);    
		eo.setDCreation(dCreation);
		eo.setDModification(dModification);
		eo.setOidIndividu(oidIndividu);
		eo.setType(type);
    eo.setAffectationAnnuelleRelationship(affectationAnnuelle);
    eo.setIndividuRelationship(individu);
    return eo;
  }

  public static NSArray fetchAllRepartValidations(EOEditingContext editingContext) {
    return _EORepartValidation.fetchAllRepartValidations(editingContext, null);
  }

  public static NSArray fetchAllRepartValidations(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EORepartValidation.fetchRepartValidations(editingContext, null, sortOrderings);
  }

  public static NSArray fetchRepartValidations(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EORepartValidation.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EORepartValidation fetchRepartValidation(EOEditingContext editingContext, String keyName, Object value) {
    return _EORepartValidation.fetchRepartValidation(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EORepartValidation fetchRepartValidation(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EORepartValidation.fetchRepartValidations(editingContext, qualifier, null);
    EORepartValidation eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EORepartValidation)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one RepartValidation that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EORepartValidation fetchRequiredRepartValidation(EOEditingContext editingContext, String keyName, Object value) {
    return _EORepartValidation.fetchRequiredRepartValidation(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EORepartValidation fetchRequiredRepartValidation(EOEditingContext editingContext, EOQualifier qualifier) {
    EORepartValidation eoObject = _EORepartValidation.fetchRepartValidation(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no RepartValidation that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EORepartValidation localInstanceIn(EOEditingContext editingContext, EORepartValidation eo) {
    EORepartValidation localInstance = (eo == null) ? null : (EORepartValidation)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
