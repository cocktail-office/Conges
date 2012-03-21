// _EODroit.java
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

// DO NOT EDIT.  Make changes to EODroit.java instead.
package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EODroit extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "Droit";
	public static final String ENTITY_TABLE_NAME = "CONGES.CNG_DROIT";

	// Attributes
	public static final String CDR_NIVEAU_KEY = "cdrNiveau";
	public static final String CDR_TYPE_KEY = "cdrType";
	public static final String C_STRUCTURE_KEY = "cStructure";
	public static final String D_CREATION_KEY = "dCreation";
	public static final String D_MODIFICATION_KEY = "dModification";
	public static final String NO_INDIVIDU_KEY = "noIndividu";
	public static final String NO_INDIVIDU_RESP_KEY = "noIndividuResp";

	public static final String CDR_NIVEAU_COLKEY = "CDR_NIVEAU";
	public static final String CDR_TYPE_COLKEY = "CDR_TYPE";
	public static final String C_STRUCTURE_COLKEY = "C_STRUCTURE";
	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String D_MODIFICATION_COLKEY = "D_MODIFICATION";
	public static final String NO_INDIVIDU_COLKEY = "NO_INDIVIDU";
	public static final String NO_INDIVIDU_RESP_COLKEY = "NO_INDIVIDU_RESP";

	// Relationships
	public static final String TO_INDIVIDU_KEY = "toIndividu";
	public static final String TO_INDIVIDU_RESP_KEY = "toIndividuResp";
	public static final String TO_STRUCTURE_KEY = "toStructure";

	// Utilities methods
  public EODroit localInstanceIn(EOEditingContext editingContext) {
    EODroit localInstance = (EODroit)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public Integer cdrNiveau() {
    return (Integer) storedValueForKey("cdrNiveau");
  }

  public void setCdrNiveau(Integer value) {
    takeStoredValueForKey(value, "cdrNiveau");
  }

  public String cdrType() {
    return (String) storedValueForKey("cdrType");
  }

  public void setCdrType(String value) {
    takeStoredValueForKey(value, "cdrType");
  }

  public String cStructure() {
    return (String) storedValueForKey("cStructure");
  }

  public void setCStructure(String value) {
    takeStoredValueForKey(value, "cStructure");
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

  public Integer noIndividu() {
    return (Integer) storedValueForKey("noIndividu");
  }

  public void setNoIndividu(Integer value) {
    takeStoredValueForKey(value, "noIndividu");
  }

  public Integer noIndividuResp() {
    return (Integer) storedValueForKey("noIndividuResp");
  }

  public void setNoIndividuResp(Integer value) {
    takeStoredValueForKey(value, "noIndividuResp");
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
  
  public fr.univlr.cri.conges.eos.modele.grhum.EOIndividu toIndividuResp() {
    return (fr.univlr.cri.conges.eos.modele.grhum.EOIndividu)storedValueForKey("toIndividuResp");
  }

  public void setToIndividuRespRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOIndividu value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.grhum.EOIndividu oldValue = toIndividuResp();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "toIndividuResp");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "toIndividuResp");
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
  

  public static EODroit createDroit(EOEditingContext editingContext, Integer cdrNiveau
, String cdrType
, String cStructure
, NSTimestamp dCreation
, NSTimestamp dModification
, Integer noIndividuResp
, fr.univlr.cri.conges.eos.modele.grhum.EOIndividu toIndividuResp, fr.univlr.cri.conges.eos.modele.grhum.EOStructure toStructure) {
    EODroit eo = (EODroit) EOUtilities.createAndInsertInstance(editingContext, _EODroit.ENTITY_NAME);    
		eo.setCdrNiveau(cdrNiveau);
		eo.setCdrType(cdrType);
		eo.setCStructure(cStructure);
		eo.setDCreation(dCreation);
		eo.setDModification(dModification);
		eo.setNoIndividuResp(noIndividuResp);
    eo.setToIndividuRespRelationship(toIndividuResp);
    eo.setToStructureRelationship(toStructure);
    return eo;
  }

  public static NSArray fetchAllDroits(EOEditingContext editingContext) {
    return _EODroit.fetchAllDroits(editingContext, null);
  }

  public static NSArray fetchAllDroits(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EODroit.fetchDroits(editingContext, null, sortOrderings);
  }

  public static NSArray fetchDroits(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EODroit.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EODroit fetchDroit(EOEditingContext editingContext, String keyName, Object value) {
    return _EODroit.fetchDroit(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EODroit fetchDroit(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EODroit.fetchDroits(editingContext, qualifier, null);
    EODroit eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EODroit)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one Droit that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EODroit fetchRequiredDroit(EOEditingContext editingContext, String keyName, Object value) {
    return _EODroit.fetchRequiredDroit(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EODroit fetchRequiredDroit(EOEditingContext editingContext, EOQualifier qualifier) {
    EODroit eoObject = _EODroit.fetchDroit(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no Droit that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EODroit localInstanceIn(EOEditingContext editingContext, EODroit eo) {
    EODroit localInstance = (eo == null) ? null : (EODroit)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
