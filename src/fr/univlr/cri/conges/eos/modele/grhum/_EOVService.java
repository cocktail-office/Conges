// _EOVService.java
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

// DO NOT EDIT.  Make changes to EOVService.java instead.
package fr.univlr.cri.conges.eos.modele.grhum;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOVService extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "VService";
	public static final String ENTITY_TABLE_NAME = "GRHUM.V_SERVICE";

	// Attributes
	public static final String C_STRUCTURE_COMPOSANTE_KEY = "cStructureComposante";

	public static final String C_STRUCTURE_COMPOSANTE_COLKEY = "C_STRUCTURE_COMPOSANTE";

	// Relationships
	public static final String TO_COMPOSANTE_KEY = "toComposante";
	public static final String TO_STRUCTURE_KEY = "toStructure";

	// Utilities methods
  public EOVService localInstanceIn(EOEditingContext editingContext) {
    EOVService localInstance = (EOVService)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public String cStructureComposante() {
    return (String) storedValueForKey("cStructureComposante");
  }

  public void setCStructureComposante(String value) {
    takeStoredValueForKey(value, "cStructureComposante");
  }

  public fr.univlr.cri.conges.eos.modele.grhum.EOStructure toComposante() {
    return (fr.univlr.cri.conges.eos.modele.grhum.EOStructure)storedValueForKey("toComposante");
  }

  public void setToComposanteRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOStructure value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.grhum.EOStructure oldValue = toComposante();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "toComposante");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "toComposante");
    }
  }
  
  public fr.univlr.cri.conges.eos.modele.grhum.EOStructure toStructure() {
    return (fr.univlr.cri.conges.eos.modele.grhum.EOStructure)storedValueForKey("toStructure");
  }

  public void setToStructureRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOStructure value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.grhum.EOStructure oldValue = toStructure();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "toStructure");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "toStructure");
    }
  }
  

  public static EOVService createVService(EOEditingContext editingContext, String cStructureComposante
, fr.univlr.cri.conges.eos.modele.grhum.EOStructure toStructure) {
    EOVService eo = (EOVService) EOUtilities.createAndInsertInstance(editingContext, _EOVService.ENTITY_NAME);    
		eo.setCStructureComposante(cStructureComposante);
    eo.setToStructureRelationship(toStructure);
    return eo;
  }

  public static NSArray fetchAllVServices(EOEditingContext editingContext) {
    return _EOVService.fetchAllVServices(editingContext, null);
  }

  public static NSArray fetchAllVServices(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOVService.fetchVServices(editingContext, null, sortOrderings);
  }

  public static NSArray fetchVServices(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOVService.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOVService fetchVService(EOEditingContext editingContext, String keyName, Object value) {
    return _EOVService.fetchVService(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOVService fetchVService(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOVService.fetchVServices(editingContext, qualifier, null);
    EOVService eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOVService)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one VService that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOVService fetchRequiredVService(EOEditingContext editingContext, String keyName, Object value) {
    return _EOVService.fetchRequiredVService(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOVService fetchRequiredVService(EOEditingContext editingContext, EOQualifier qualifier) {
    EOVService eoObject = _EOVService.fetchVService(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no VService that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOVService localInstanceIn(EOEditingContext editingContext, EOVService eo) {
    EOVService localInstance = (eo == null) ? null : (EOVService)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
