// _EOContrat.java
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

// DO NOT EDIT.  Make changes to EOContrat.java instead.
package fr.univlr.cri.conges.eos.modele.grhum;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOContrat extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "Contrat";
	public static final String ENTITY_TABLE_NAME = "GRHUM.CONTRAT";

	// Attributes
	public static final String C_TYPE_CONTRAT_TRAV_KEY = "cTypeContratTrav";
	public static final String D_DEB_CONTRAT_TRAV_KEY = "dDebContratTrav";
	public static final String D_FIN_CONTRAT_TRAV_KEY = "dFinContratTrav";
	public static final String TEM_ANNULATION_KEY = "temAnnulation";

	public static final String C_TYPE_CONTRAT_TRAV_COLKEY = "C_TYPE_CONTRAT_TRAV";
	public static final String D_DEB_CONTRAT_TRAV_COLKEY = "D_DEB_CONTRAT_TRAV";
	public static final String D_FIN_CONTRAT_TRAV_COLKEY = "D_FIN_CONTRAT_TRAV";
	public static final String TEM_ANNULATION_COLKEY = "TEM_ANNULATION";

	// Relationships
	public static final String TO_INDIVIDU_KEY = "toIndividu";

	// Utilities methods
  public EOContrat localInstanceIn(EOEditingContext editingContext) {
    EOContrat localInstance = (EOContrat)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public String cTypeContratTrav() {
    return (String) storedValueForKey("cTypeContratTrav");
  }

  public void setCTypeContratTrav(String value) {
    takeStoredValueForKey(value, "cTypeContratTrav");
  }

  public NSTimestamp dDebContratTrav() {
    return (NSTimestamp) storedValueForKey("dDebContratTrav");
  }

  public void setDDebContratTrav(NSTimestamp value) {
    takeStoredValueForKey(value, "dDebContratTrav");
  }

  public NSTimestamp dFinContratTrav() {
    return (NSTimestamp) storedValueForKey("dFinContratTrav");
  }

  public void setDFinContratTrav(NSTimestamp value) {
    takeStoredValueForKey(value, "dFinContratTrav");
  }

  public String temAnnulation() {
    return (String) storedValueForKey("temAnnulation");
  }

  public void setTemAnnulation(String value) {
    takeStoredValueForKey(value, "temAnnulation");
  }

  public fr.univlr.cri.conges.eos.modele.grhum.EOIndividu toIndividu() {
    return (fr.univlr.cri.conges.eos.modele.grhum.EOIndividu)storedValueForKey("toIndividu");
  }

  public void setToIndividuRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOIndividu value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.grhum.EOIndividu oldValue = toIndividu();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "toIndividu");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "toIndividu");
    }
  }
  

  public static EOContrat createContrat(EOEditingContext editingContext, String cTypeContratTrav
, NSTimestamp dDebContratTrav
, NSTimestamp dFinContratTrav
, fr.univlr.cri.conges.eos.modele.grhum.EOIndividu toIndividu) {
    EOContrat eo = (EOContrat) EOUtilities.createAndInsertInstance(editingContext, _EOContrat.ENTITY_NAME);    
		eo.setCTypeContratTrav(cTypeContratTrav);
		eo.setDDebContratTrav(dDebContratTrav);
		eo.setDFinContratTrav(dFinContratTrav);
    eo.setToIndividuRelationship(toIndividu);
    return eo;
  }

  public static NSArray fetchAllContrats(EOEditingContext editingContext) {
    return _EOContrat.fetchAllContrats(editingContext, null);
  }

  public static NSArray fetchAllContrats(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOContrat.fetchContrats(editingContext, null, sortOrderings);
  }

  public static NSArray fetchContrats(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOContrat.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOContrat fetchContrat(EOEditingContext editingContext, String keyName, Object value) {
    return _EOContrat.fetchContrat(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOContrat fetchContrat(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOContrat.fetchContrats(editingContext, qualifier, null);
    EOContrat eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOContrat)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one Contrat that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOContrat fetchRequiredContrat(EOEditingContext editingContext, String keyName, Object value) {
    return _EOContrat.fetchRequiredContrat(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOContrat fetchRequiredContrat(EOEditingContext editingContext, EOQualifier qualifier) {
    EOContrat eoObject = _EOContrat.fetchContrat(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no Contrat that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOContrat localInstanceIn(EOEditingContext editingContext, EOContrat eo) {
    EOContrat localInstance = (eo == null) ? null : (EOContrat)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
