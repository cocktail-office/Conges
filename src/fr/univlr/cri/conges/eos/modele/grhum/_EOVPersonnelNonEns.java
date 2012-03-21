// _EOVPersonnelNonEns.java
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

// DO NOT EDIT.  Make changes to EOVPersonnelNonEns.java instead.
package fr.univlr.cri.conges.eos.modele.grhum;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOVPersonnelNonEns extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "VPersonnelNonEns";
	public static final String ENTITY_TABLE_NAME = "GRHUM.V_PERSONNEL_NON_ENS";

	// Attributes
	public static final String CON_CAR_KEY = "conCar";
	public static final String D_DEBUT_KEY = "dDebut";
	public static final String D_FIN_KEY = "dFin";
	public static final String NO_DOSSIER_PERS_KEY = "noDossierPers";

	public static final String CON_CAR_COLKEY = "CON_CAR";
	public static final String D_DEBUT_COLKEY = "D_DEBUT";
	public static final String D_FIN_COLKEY = "D_FIN";
	public static final String NO_DOSSIER_PERS_COLKEY = "NO_DOSSIER_PERS";

	// Relationships
	public static final String TO_INDIVIDU_KEY = "toIndividu";

	// Utilities methods
  public EOVPersonnelNonEns localInstanceIn(EOEditingContext editingContext) {
    EOVPersonnelNonEns localInstance = (EOVPersonnelNonEns)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public String conCar() {
    return (String) storedValueForKey("conCar");
  }

  public void setConCar(String value) {
    takeStoredValueForKey(value, "conCar");
  }

  public NSTimestamp dDebut() {
    return (NSTimestamp) storedValueForKey("dDebut");
  }

  public void setDDebut(NSTimestamp value) {
    takeStoredValueForKey(value, "dDebut");
  }

  public NSTimestamp dFin() {
    return (NSTimestamp) storedValueForKey("dFin");
  }

  public void setDFin(NSTimestamp value) {
    takeStoredValueForKey(value, "dFin");
  }

  public Integer noDossierPers() {
    return (Integer) storedValueForKey("noDossierPers");
  }

  public void setNoDossierPers(Integer value) {
    takeStoredValueForKey(value, "noDossierPers");
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
  

  public static EOVPersonnelNonEns createVPersonnelNonEns(EOEditingContext editingContext, Integer noDossierPers
, fr.univlr.cri.conges.eos.modele.grhum.EOIndividu toIndividu) {
    EOVPersonnelNonEns eo = (EOVPersonnelNonEns) EOUtilities.createAndInsertInstance(editingContext, _EOVPersonnelNonEns.ENTITY_NAME);    
		eo.setNoDossierPers(noDossierPers);
    eo.setToIndividuRelationship(toIndividu);
    return eo;
  }

  public static NSArray fetchAllVPersonnelNonEnses(EOEditingContext editingContext) {
    return _EOVPersonnelNonEns.fetchAllVPersonnelNonEnses(editingContext, null);
  }

  public static NSArray fetchAllVPersonnelNonEnses(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOVPersonnelNonEns.fetchVPersonnelNonEnses(editingContext, null, sortOrderings);
  }

  public static NSArray fetchVPersonnelNonEnses(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOVPersonnelNonEns.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOVPersonnelNonEns fetchVPersonnelNonEns(EOEditingContext editingContext, String keyName, Object value) {
    return _EOVPersonnelNonEns.fetchVPersonnelNonEns(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOVPersonnelNonEns fetchVPersonnelNonEns(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOVPersonnelNonEns.fetchVPersonnelNonEnses(editingContext, qualifier, null);
    EOVPersonnelNonEns eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOVPersonnelNonEns)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one VPersonnelNonEns that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOVPersonnelNonEns fetchRequiredVPersonnelNonEns(EOEditingContext editingContext, String keyName, Object value) {
    return _EOVPersonnelNonEns.fetchRequiredVPersonnelNonEns(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOVPersonnelNonEns fetchRequiredVPersonnelNonEns(EOEditingContext editingContext, EOQualifier qualifier) {
    EOVPersonnelNonEns eoObject = _EOVPersonnelNonEns.fetchVPersonnelNonEns(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no VPersonnelNonEns that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOVPersonnelNonEns localInstanceIn(EOEditingContext editingContext, EOVPersonnelNonEns eo) {
    EOVPersonnelNonEns localInstance = (eo == null) ? null : (EOVPersonnelNonEns)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
