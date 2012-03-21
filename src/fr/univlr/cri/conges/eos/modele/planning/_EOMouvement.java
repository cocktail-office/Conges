// _EOMouvement.java
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

// DO NOT EDIT.  Make changes to EOMouvement.java instead.
package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOMouvement extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "Mouvement";
	public static final String ENTITY_TABLE_NAME = "CONGES.PLNG_MOUVEMENT";

	// Attributes
	public static final String D_CREATION_KEY = "dCreation";
	public static final String D_MODIFICATION_KEY = "dModification";
	public static final String MOUVEMENT_LIBELLE_KEY = "mouvementLibelle";
	public static final String MOUVEMENT_MINUTES_KEY = "mouvementMinutes";
	public static final String MOUVEMENT_TYPE_KEY = "mouvementType";

	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String D_MODIFICATION_COLKEY = "D_MODIFICATION";
	public static final String MOUVEMENT_LIBELLE_COLKEY = "MOUVEMENT_LIBELLE";
	public static final String MOUVEMENT_MINUTES_COLKEY = "MOUVEMENT_MINUTES";
	public static final String MOUVEMENT_TYPE_COLKEY = "MOUVEMENT_TYPE";

	// Relationships
	public static final String TO_AFFECTATION_ANNUELLE_KEY = "toAffectationAnnuelle";

	// Utilities methods
  public EOMouvement localInstanceIn(EOEditingContext editingContext) {
    EOMouvement localInstance = (EOMouvement)EOUtilities.localInstanceOfObject(editingContext, this);
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

  public String mouvementLibelle() {
    return (String) storedValueForKey("mouvementLibelle");
  }

  public void setMouvementLibelle(String value) {
    takeStoredValueForKey(value, "mouvementLibelle");
  }

  public Integer mouvementMinutes() {
    return (Integer) storedValueForKey("mouvementMinutes");
  }

  public void setMouvementMinutes(Integer value) {
    takeStoredValueForKey(value, "mouvementMinutes");
  }

  public Integer mouvementType() {
    return (Integer) storedValueForKey("mouvementType");
  }

  public void setMouvementType(Integer value) {
    takeStoredValueForKey(value, "mouvementType");
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
  

  public static EOMouvement createMouvement(EOEditingContext editingContext, NSTimestamp dCreation
, NSTimestamp dModification
, Integer mouvementMinutes
, Integer mouvementType
, fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle toAffectationAnnuelle) {
    EOMouvement eo = (EOMouvement) EOUtilities.createAndInsertInstance(editingContext, _EOMouvement.ENTITY_NAME);    
		eo.setDCreation(dCreation);
		eo.setDModification(dModification);
		eo.setMouvementMinutes(mouvementMinutes);
		eo.setMouvementType(mouvementType);
    eo.setToAffectationAnnuelleRelationship(toAffectationAnnuelle);
    return eo;
  }

  public static NSArray fetchAllMouvements(EOEditingContext editingContext) {
    return _EOMouvement.fetchAllMouvements(editingContext, null);
  }

  public static NSArray fetchAllMouvements(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOMouvement.fetchMouvements(editingContext, null, sortOrderings);
  }

  public static NSArray fetchMouvements(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOMouvement.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOMouvement fetchMouvement(EOEditingContext editingContext, String keyName, Object value) {
    return _EOMouvement.fetchMouvement(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOMouvement fetchMouvement(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOMouvement.fetchMouvements(editingContext, qualifier, null);
    EOMouvement eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOMouvement)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one Mouvement that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOMouvement fetchRequiredMouvement(EOEditingContext editingContext, String keyName, Object value) {
    return _EOMouvement.fetchRequiredMouvement(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOMouvement fetchRequiredMouvement(EOEditingContext editingContext, EOQualifier qualifier) {
    EOMouvement eoObject = _EOMouvement.fetchMouvement(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no Mouvement that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOMouvement localInstanceIn(EOEditingContext editingContext, EOMouvement eo) {
    EOMouvement localInstance = (eo == null) ? null : (EOMouvement)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
