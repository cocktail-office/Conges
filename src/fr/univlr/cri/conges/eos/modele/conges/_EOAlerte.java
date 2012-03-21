// _EOAlerte.java
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

// DO NOT EDIT.  Make changes to EOAlerte.java instead.
package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOAlerte extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "Alerte";
	public static final String ENTITY_TABLE_NAME = "CONGES.CNG_ALERTE";

	// Attributes
	public static final String CAL_HASHCODE_VAL_KEY = "calHashcodeVal";
	public static final String CAL_HASHCODE_VIS_KEY = "calHashcodeVis";
	public static final String COMMENTAIRE_KEY = "commentaire";
	public static final String D_CREATION_KEY = "dCreation";
	public static final String D_MODIFICATION_KEY = "dModification";
	public static final String FLAG_REPONSE_KEY = "flagReponse";
	public static final String LIBELLE_KEY = "libelle";
	public static final String OID_R_KEY = "oidR";

	public static final String CAL_HASHCODE_VAL_COLKEY = "CAL_HASHCODE_VAL";
	public static final String CAL_HASHCODE_VIS_COLKEY = "CAL_HASHCODE_VIS";
	public static final String COMMENTAIRE_COLKEY = "COMMENTAIRE";
	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String D_MODIFICATION_COLKEY = "D_MODIFICATION";
	public static final String FLAG_REPONSE_COLKEY = "FLG_REPONSE";
	public static final String LIBELLE_COLKEY = "LIBELLE";
	public static final String OID_R_COLKEY = "$attribute.columnName";

	// Relationships
	public static final String AFFECTATION_ANNUELLE_KEY = "affectationAnnuelle";
	public static final String OCCUPATION_KEY = "occupation";

	// Utilities methods
  public EOAlerte localInstanceIn(EOEditingContext editingContext) {
    EOAlerte localInstance = (EOAlerte)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public String calHashcodeVal() {
    return (String) storedValueForKey("calHashcodeVal");
  }

  public void setCalHashcodeVal(String value) {
    takeStoredValueForKey(value, "calHashcodeVal");
  }

  public String calHashcodeVis() {
    return (String) storedValueForKey("calHashcodeVis");
  }

  public void setCalHashcodeVis(String value) {
    takeStoredValueForKey(value, "calHashcodeVis");
  }

  public String commentaire() {
    return (String) storedValueForKey("commentaire");
  }

  public void setCommentaire(String value) {
    takeStoredValueForKey(value, "commentaire");
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

  public String flagReponse() {
    return (String) storedValueForKey("flagReponse");
  }

  public void setFlagReponse(String value) {
    takeStoredValueForKey(value, "flagReponse");
  }

  public String libelle() {
    return (String) storedValueForKey("libelle");
  }

  public void setLibelle(String value) {
    takeStoredValueForKey(value, "libelle");
  }

  public Integer oidR() {
    return (Integer) storedValueForKey("oidR");
  }

  public void setOidR(Integer value) {
    takeStoredValueForKey(value, "oidR");
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
  

  public static EOAlerte createAlerte(EOEditingContext editingContext, NSTimestamp dCreation
, NSTimestamp dModification
, fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle affectationAnnuelle) {
    EOAlerte eo = (EOAlerte) EOUtilities.createAndInsertInstance(editingContext, _EOAlerte.ENTITY_NAME);    
		eo.setDCreation(dCreation);
		eo.setDModification(dModification);
    eo.setAffectationAnnuelleRelationship(affectationAnnuelle);
    return eo;
  }

  public static NSArray fetchAllAlertes(EOEditingContext editingContext) {
    return _EOAlerte.fetchAllAlertes(editingContext, null);
  }

  public static NSArray fetchAllAlertes(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOAlerte.fetchAlertes(editingContext, null, sortOrderings);
  }

  public static NSArray fetchAlertes(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOAlerte.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOAlerte fetchAlerte(EOEditingContext editingContext, String keyName, Object value) {
    return _EOAlerte.fetchAlerte(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOAlerte fetchAlerte(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOAlerte.fetchAlertes(editingContext, qualifier, null);
    EOAlerte eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOAlerte)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one Alerte that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOAlerte fetchRequiredAlerte(EOEditingContext editingContext, String keyName, Object value) {
    return _EOAlerte.fetchRequiredAlerte(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOAlerte fetchRequiredAlerte(EOEditingContext editingContext, EOQualifier qualifier) {
    EOAlerte eoObject = _EOAlerte.fetchAlerte(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no Alerte that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOAlerte localInstanceIn(EOEditingContext editingContext, EOAlerte eo) {
    EOAlerte localInstance = (eo == null) ? null : (EOAlerte)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
