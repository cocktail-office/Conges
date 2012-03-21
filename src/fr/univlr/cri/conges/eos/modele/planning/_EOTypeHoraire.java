// _EOTypeHoraire.java
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

// DO NOT EDIT.  Make changes to EOTypeHoraire.java instead.
package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOTypeHoraire extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "TypeHoraire";
	public static final String ENTITY_TABLE_NAME = "CONGES.PLNG_TYP_HOR";

	// Attributes
	public static final String D_CREATION_KEY = "dCreation";
	public static final String D_MODIFICATION_KEY = "dModification";
	public static final String FLAG_HORAIRE_SEMAINE_HAUTE_KEY = "flagHoraireSemaineHaute";
	public static final String FLAG_HORS_NORME_KEY = "flagHorsNorme";
	public static final String TOTAL_KEY = "total";

	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String D_MODIFICATION_COLKEY = "D_MODIFICATION";
	public static final String FLAG_HORAIRE_SEMAINE_HAUTE_COLKEY = "FLG_SEMAINE_HAUTE";
	public static final String FLAG_HORS_NORME_COLKEY = "FLG_HORS_NORME";
	public static final String TOTAL_COLKEY = "TOTAL";

	// Relationships

	// Utilities methods
  public EOTypeHoraire localInstanceIn(EOEditingContext editingContext) {
    EOTypeHoraire localInstance = (EOTypeHoraire)EOUtilities.localInstanceOfObject(editingContext, this);
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

  public Integer flagHoraireSemaineHaute() {
    return (Integer) storedValueForKey("flagHoraireSemaineHaute");
  }

  public void setFlagHoraireSemaineHaute(Integer value) {
    takeStoredValueForKey(value, "flagHoraireSemaineHaute");
  }

  public Integer flagHorsNorme() {
    return (Integer) storedValueForKey("flagHorsNorme");
  }

  public void setFlagHorsNorme(Integer value) {
    takeStoredValueForKey(value, "flagHorsNorme");
  }

  public Integer total() {
    return (Integer) storedValueForKey("total");
  }

  public void setTotal(Integer value) {
    takeStoredValueForKey(value, "total");
  }


  public static EOTypeHoraire createTypeHoraire(EOEditingContext editingContext, NSTimestamp dCreation
, NSTimestamp dModification
) {
    EOTypeHoraire eo = (EOTypeHoraire) EOUtilities.createAndInsertInstance(editingContext, _EOTypeHoraire.ENTITY_NAME);    
		eo.setDCreation(dCreation);
		eo.setDModification(dModification);
    return eo;
  }

  public static NSArray fetchAllTypeHoraires(EOEditingContext editingContext) {
    return _EOTypeHoraire.fetchAllTypeHoraires(editingContext, null);
  }

  public static NSArray fetchAllTypeHoraires(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOTypeHoraire.fetchTypeHoraires(editingContext, null, sortOrderings);
  }

  public static NSArray fetchTypeHoraires(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOTypeHoraire.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOTypeHoraire fetchTypeHoraire(EOEditingContext editingContext, String keyName, Object value) {
    return _EOTypeHoraire.fetchTypeHoraire(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOTypeHoraire fetchTypeHoraire(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOTypeHoraire.fetchTypeHoraires(editingContext, qualifier, null);
    EOTypeHoraire eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOTypeHoraire)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one TypeHoraire that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOTypeHoraire fetchRequiredTypeHoraire(EOEditingContext editingContext, String keyName, Object value) {
    return _EOTypeHoraire.fetchRequiredTypeHoraire(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOTypeHoraire fetchRequiredTypeHoraire(EOEditingContext editingContext, EOQualifier qualifier) {
    EOTypeHoraire eoObject = _EOTypeHoraire.fetchTypeHoraire(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no TypeHoraire that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOTypeHoraire localInstanceIn(EOEditingContext editingContext, EOTypeHoraire eo) {
    EOTypeHoraire localInstance = (eo == null) ? null : (EOTypeHoraire)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
