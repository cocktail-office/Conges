// _EOStructureAutorisee.java
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

// DO NOT EDIT.  Make changes to EOStructureAutorisee.java instead.
package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOStructureAutorisee extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "StructureAutorisee";
	public static final String ENTITY_TABLE_NAME = "CONGES.CNG_STRUCTURE_AUTORISEE";

	// Attributes
	public static final String ANNEE_KEY = "annee";
	public static final String D_CREATION_KEY = "dCreation";
	public static final String D_MODIFICATION_KEY = "dModification";

	public static final String ANNEE_COLKEY = "ANNEE";
	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String D_MODIFICATION_COLKEY = "D_MODIFICATION";

	// Relationships
	public static final String STRUCTURE_KEY = "structure";

	// Utilities methods
  public EOStructureAutorisee localInstanceIn(EOEditingContext editingContext) {
    EOStructureAutorisee localInstance = (EOStructureAutorisee)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public String annee() {
    return (String) storedValueForKey("annee");
  }

  public void setAnnee(String value) {
    takeStoredValueForKey(value, "annee");
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

  public fr.univlr.cri.conges.eos.modele.grhum.EOStructure structure() {
    return (fr.univlr.cri.conges.eos.modele.grhum.EOStructure)storedValueForKey("structure");
  }

  public void setStructureRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOStructure value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.grhum.EOStructure oldValue = structure();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "structure");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "structure");
    }
  }
  

  public static EOStructureAutorisee createStructureAutorisee(EOEditingContext editingContext, String annee
, NSTimestamp dCreation
, NSTimestamp dModification
, fr.univlr.cri.conges.eos.modele.grhum.EOStructure structure) {
    EOStructureAutorisee eo = (EOStructureAutorisee) EOUtilities.createAndInsertInstance(editingContext, _EOStructureAutorisee.ENTITY_NAME);    
		eo.setAnnee(annee);
		eo.setDCreation(dCreation);
		eo.setDModification(dModification);
    eo.setStructureRelationship(structure);
    return eo;
  }

  public static NSArray fetchAllStructureAutorisees(EOEditingContext editingContext) {
    return _EOStructureAutorisee.fetchAllStructureAutorisees(editingContext, null);
  }

  public static NSArray fetchAllStructureAutorisees(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOStructureAutorisee.fetchStructureAutorisees(editingContext, null, sortOrderings);
  }

  public static NSArray fetchStructureAutorisees(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOStructureAutorisee.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOStructureAutorisee fetchStructureAutorisee(EOEditingContext editingContext, String keyName, Object value) {
    return _EOStructureAutorisee.fetchStructureAutorisee(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOStructureAutorisee fetchStructureAutorisee(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOStructureAutorisee.fetchStructureAutorisees(editingContext, qualifier, null);
    EOStructureAutorisee eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOStructureAutorisee)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one StructureAutorisee that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOStructureAutorisee fetchRequiredStructureAutorisee(EOEditingContext editingContext, String keyName, Object value) {
    return _EOStructureAutorisee.fetchRequiredStructureAutorisee(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOStructureAutorisee fetchRequiredStructureAutorisee(EOEditingContext editingContext, EOQualifier qualifier) {
    EOStructureAutorisee eoObject = _EOStructureAutorisee.fetchStructureAutorisee(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no StructureAutorisee that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOStructureAutorisee localInstanceIn(EOEditingContext editingContext, EOStructureAutorisee eo) {
    EOStructureAutorisee localInstance = (eo == null) ? null : (EOStructureAutorisee)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
