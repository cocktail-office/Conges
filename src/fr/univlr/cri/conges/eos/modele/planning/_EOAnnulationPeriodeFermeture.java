// _EOAnnulationPeriodeFermeture.java
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

// DO NOT EDIT.  Make changes to EOAnnulationPeriodeFermeture.java instead.
package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOAnnulationPeriodeFermeture extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "AnnulationPeriodeFermeture";
	public static final String ENTITY_TABLE_NAME = "CONGES.PLNG_ANN_PER_FER";

	// Attributes
	public static final String D_CREATION_KEY = "dCreation";
	public static final String D_MODIFICATION_KEY = "dModification";

	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String D_MODIFICATION_COLKEY = "D_MODIFICATION";

	// Relationships
	public static final String TO_AFFECTATION_ANNUELLE_KEY = "toAffectationAnnuelle";
	public static final String TO_COMPOSANTE_KEY = "toComposante";
	public static final String TO_PERIODE_FERMETURE_KEY = "toPeriodeFermeture";
	public static final String TO_STRUCTURE_KEY = "toStructure";

	// Utilities methods
  public EOAnnulationPeriodeFermeture localInstanceIn(EOEditingContext editingContext) {
    EOAnnulationPeriodeFermeture localInstance = (EOAnnulationPeriodeFermeture)EOUtilities.localInstanceOfObject(editingContext, this);
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

  public fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle toAffectationAnnuelle() {
    return (fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle)storedValueForKey("toAffectationAnnuelle");
  }

  public void setToAffectationAnnuelleRelationship(fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle oldValue = toAffectationAnnuelle();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "toAffectationAnnuelle");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "toAffectationAnnuelle");
    }
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
  
  public fr.univlr.cri.conges.eos.modele.planning.EOPeriodeFermeture toPeriodeFermeture() {
    return (fr.univlr.cri.conges.eos.modele.planning.EOPeriodeFermeture)storedValueForKey("toPeriodeFermeture");
  }

  public void setToPeriodeFermetureRelationship(fr.univlr.cri.conges.eos.modele.planning.EOPeriodeFermeture value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.planning.EOPeriodeFermeture oldValue = toPeriodeFermeture();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "toPeriodeFermeture");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "toPeriodeFermeture");
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
  

  public static EOAnnulationPeriodeFermeture createAnnulationPeriodeFermeture(EOEditingContext editingContext, NSTimestamp dCreation
, NSTimestamp dModification
, fr.univlr.cri.conges.eos.modele.planning.EOPeriodeFermeture toPeriodeFermeture) {
    EOAnnulationPeriodeFermeture eo = (EOAnnulationPeriodeFermeture) EOUtilities.createAndInsertInstance(editingContext, _EOAnnulationPeriodeFermeture.ENTITY_NAME);    
		eo.setDCreation(dCreation);
		eo.setDModification(dModification);
    eo.setToPeriodeFermetureRelationship(toPeriodeFermeture);
    return eo;
  }

  public static NSArray fetchAllAnnulationPeriodeFermetures(EOEditingContext editingContext) {
    return _EOAnnulationPeriodeFermeture.fetchAllAnnulationPeriodeFermetures(editingContext, null);
  }

  public static NSArray fetchAllAnnulationPeriodeFermetures(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOAnnulationPeriodeFermeture.fetchAnnulationPeriodeFermetures(editingContext, null, sortOrderings);
  }

  public static NSArray fetchAnnulationPeriodeFermetures(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOAnnulationPeriodeFermeture.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOAnnulationPeriodeFermeture fetchAnnulationPeriodeFermeture(EOEditingContext editingContext, String keyName, Object value) {
    return _EOAnnulationPeriodeFermeture.fetchAnnulationPeriodeFermeture(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOAnnulationPeriodeFermeture fetchAnnulationPeriodeFermeture(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOAnnulationPeriodeFermeture.fetchAnnulationPeriodeFermetures(editingContext, qualifier, null);
    EOAnnulationPeriodeFermeture eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOAnnulationPeriodeFermeture)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one AnnulationPeriodeFermeture that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOAnnulationPeriodeFermeture fetchRequiredAnnulationPeriodeFermeture(EOEditingContext editingContext, String keyName, Object value) {
    return _EOAnnulationPeriodeFermeture.fetchRequiredAnnulationPeriodeFermeture(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOAnnulationPeriodeFermeture fetchRequiredAnnulationPeriodeFermeture(EOEditingContext editingContext, EOQualifier qualifier) {
    EOAnnulationPeriodeFermeture eoObject = _EOAnnulationPeriodeFermeture.fetchAnnulationPeriodeFermeture(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no AnnulationPeriodeFermeture that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOAnnulationPeriodeFermeture localInstanceIn(EOEditingContext editingContext, EOAnnulationPeriodeFermeture eo) {
    EOAnnulationPeriodeFermeture localInstance = (eo == null) ? null : (EOAnnulationPeriodeFermeture)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
