// _EOPeriodeFermeture.java
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

// DO NOT EDIT.  Make changes to EOPeriodeFermeture.java instead.
package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOPeriodeFermeture extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "PeriodeFermeture";
	public static final String ENTITY_TABLE_NAME = "CONGES.PLNG_PER_FER";

	// Attributes
	public static final String DATE_DEBUT_KEY = "dateDebut";
	public static final String DATE_FIN_KEY = "dateFin";
	public static final String D_CREATION_KEY = "dCreation";
	public static final String D_MODIFICATION_KEY = "dModification";

	public static final String DATE_DEBUT_COLKEY = "DTE_DEBUT";
	public static final String DATE_FIN_COLKEY = "DTE_FIN";
	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String D_MODIFICATION_COLKEY = "D_MODIFICATION";

	// Relationships
	public static final String ANNULATION_PERIODE_FERMETURES_KEY = "annulationPeriodeFermetures";
	public static final String TO_AFFECTATION_ANNUELLE_KEY = "toAffectationAnnuelle";
	public static final String TO_COMPOSANTE_KEY = "toComposante";
	public static final String TO_STRUCTURE_KEY = "toStructure";

	// Utilities methods
  public EOPeriodeFermeture localInstanceIn(EOEditingContext editingContext) {
    EOPeriodeFermeture localInstance = (EOPeriodeFermeture)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public NSTimestamp dateDebut() {
    return (NSTimestamp) storedValueForKey("dateDebut");
  }

  public void setDateDebut(NSTimestamp value) {
    takeStoredValueForKey(value, "dateDebut");
  }

  public NSTimestamp dateFin() {
    return (NSTimestamp) storedValueForKey("dateFin");
  }

  public void setDateFin(NSTimestamp value) {
    takeStoredValueForKey(value, "dateFin");
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
  
  public NSArray annulationPeriodeFermetures() {
    return (NSArray)storedValueForKey("annulationPeriodeFermetures");
  }

  public NSArray annulationPeriodeFermetures(EOQualifier qualifier) {
    return annulationPeriodeFermetures(qualifier, null, false);
  }

  public NSArray annulationPeriodeFermetures(EOQualifier qualifier, boolean fetch) {
    return annulationPeriodeFermetures(qualifier, null, fetch);
  }

  public NSArray annulationPeriodeFermetures(EOQualifier qualifier, NSArray sortOrderings, boolean fetch) {
    NSArray results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture.TO_PERIODE_FERMETURE_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray qualifiers = new NSMutableArray();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture.fetchAnnulationPeriodeFermetures(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = annulationPeriodeFermetures();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToAnnulationPeriodeFermeturesRelationship(fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "annulationPeriodeFermetures");
  }

  public void removeFromAnnulationPeriodeFermeturesRelationship(fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "annulationPeriodeFermetures");
  }

  public fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture createAnnulationPeriodeFermeturesRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("AnnulationPeriodeFermeture");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "annulationPeriodeFermetures");
    return (fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture) eo;
  }

  public void deleteAnnulationPeriodeFermeturesRelationship(fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "annulationPeriodeFermetures");
  }

  public void deleteAllAnnulationPeriodeFermeturesRelationships() {
    Enumeration objects = annulationPeriodeFermetures().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteAnnulationPeriodeFermeturesRelationship((fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture)objects.nextElement());
    }
  }


  public static EOPeriodeFermeture createPeriodeFermeture(EOEditingContext editingContext, NSTimestamp dCreation
, NSTimestamp dModification
) {
    EOPeriodeFermeture eo = (EOPeriodeFermeture) EOUtilities.createAndInsertInstance(editingContext, _EOPeriodeFermeture.ENTITY_NAME);    
		eo.setDCreation(dCreation);
		eo.setDModification(dModification);
    return eo;
  }

  public static NSArray fetchAllPeriodeFermetures(EOEditingContext editingContext) {
    return _EOPeriodeFermeture.fetchAllPeriodeFermetures(editingContext, null);
  }

  public static NSArray fetchAllPeriodeFermetures(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOPeriodeFermeture.fetchPeriodeFermetures(editingContext, null, sortOrderings);
  }

  public static NSArray fetchPeriodeFermetures(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOPeriodeFermeture.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOPeriodeFermeture fetchPeriodeFermeture(EOEditingContext editingContext, String keyName, Object value) {
    return _EOPeriodeFermeture.fetchPeriodeFermeture(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOPeriodeFermeture fetchPeriodeFermeture(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOPeriodeFermeture.fetchPeriodeFermetures(editingContext, qualifier, null);
    EOPeriodeFermeture eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOPeriodeFermeture)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one PeriodeFermeture that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOPeriodeFermeture fetchRequiredPeriodeFermeture(EOEditingContext editingContext, String keyName, Object value) {
    return _EOPeriodeFermeture.fetchRequiredPeriodeFermeture(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOPeriodeFermeture fetchRequiredPeriodeFermeture(EOEditingContext editingContext, EOQualifier qualifier) {
    EOPeriodeFermeture eoObject = _EOPeriodeFermeture.fetchPeriodeFermeture(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no PeriodeFermeture that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOPeriodeFermeture localInstanceIn(EOEditingContext editingContext, EOPeriodeFermeture eo) {
    EOPeriodeFermeture localInstance = (eo == null) ? null : (EOPeriodeFermeture)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
