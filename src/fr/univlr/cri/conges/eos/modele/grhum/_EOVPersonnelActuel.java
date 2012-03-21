// _EOVPersonnelActuel.java
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

// DO NOT EDIT.  Make changes to EOVPersonnelActuel.java instead.
package fr.univlr.cri.conges.eos.modele.grhum;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOVPersonnelActuel extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "VPersonnelActuel";
	public static final String ENTITY_TABLE_NAME = "GRHUM.V_PERSONNEL_ACTUEL";

	// Attributes
	public static final String NO_DOSSIER_PERS_KEY = "noDossierPers";

	public static final String NO_DOSSIER_PERS_COLKEY = "NO_DOSSIER_PERS";

	// Relationships
	public static final String INDIVIDU_KEY = "individu";

	// Utilities methods
  public EOVPersonnelActuel localInstanceIn(EOEditingContext editingContext) {
    EOVPersonnelActuel localInstance = (EOVPersonnelActuel)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public Integer noDossierPers() {
    return (Integer) storedValueForKey("noDossierPers");
  }

  public void setNoDossierPers(Integer value) {
    takeStoredValueForKey(value, "noDossierPers");
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
  

  public static EOVPersonnelActuel createVPersonnelActuel(EOEditingContext editingContext, Integer noDossierPers
) {
    EOVPersonnelActuel eo = (EOVPersonnelActuel) EOUtilities.createAndInsertInstance(editingContext, _EOVPersonnelActuel.ENTITY_NAME);    
		eo.setNoDossierPers(noDossierPers);
    return eo;
  }

  public static NSArray fetchAllVPersonnelActuels(EOEditingContext editingContext) {
    return _EOVPersonnelActuel.fetchAllVPersonnelActuels(editingContext, null);
  }

  public static NSArray fetchAllVPersonnelActuels(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOVPersonnelActuel.fetchVPersonnelActuels(editingContext, null, sortOrderings);
  }

  public static NSArray fetchVPersonnelActuels(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOVPersonnelActuel.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOVPersonnelActuel fetchVPersonnelActuel(EOEditingContext editingContext, String keyName, Object value) {
    return _EOVPersonnelActuel.fetchVPersonnelActuel(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOVPersonnelActuel fetchVPersonnelActuel(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOVPersonnelActuel.fetchVPersonnelActuels(editingContext, qualifier, null);
    EOVPersonnelActuel eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOVPersonnelActuel)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one VPersonnelActuel that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOVPersonnelActuel fetchRequiredVPersonnelActuel(EOEditingContext editingContext, String keyName, Object value) {
    return _EOVPersonnelActuel.fetchRequiredVPersonnelActuel(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOVPersonnelActuel fetchRequiredVPersonnelActuel(EOEditingContext editingContext, EOQualifier qualifier) {
    EOVPersonnelActuel eoObject = _EOVPersonnelActuel.fetchVPersonnelActuel(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no VPersonnelActuel that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOVPersonnelActuel localInstanceIn(EOEditingContext editingContext, EOVPersonnelActuel eo) {
    EOVPersonnelActuel localInstance = (eo == null) ? null : (EOVPersonnelActuel)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
