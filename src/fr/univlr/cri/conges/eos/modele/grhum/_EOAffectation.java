// _EOAffectation.java
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

// DO NOT EDIT.  Make changes to EOAffectation.java instead.
package fr.univlr.cri.conges.eos.modele.grhum;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOAffectation extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "Affectation";
	public static final String ENTITY_TABLE_NAME = "GRHUM.AFFECTATION";

	// Attributes
	public static final String D_DEB_AFFECTATION_KEY = "dDebAffectation";
	public static final String DEN_QUOTATION_KEY = "denQuotation";
	public static final String D_FIN_AFFECTATION_KEY = "dFinAffectation";
	public static final String NUM_QUOTATION_KEY = "numQuotation";
	public static final String TEM_VALIDE_KEY = "temValide";

	public static final String D_DEB_AFFECTATION_COLKEY = "D_DEB_AFFECTATION";
	public static final String DEN_QUOTATION_COLKEY = "DEN_QUOT_AFFECTATION";
	public static final String D_FIN_AFFECTATION_COLKEY = "D_FIN_AFFECTATION";
	public static final String NUM_QUOTATION_COLKEY = "NUM_QUOT_AFFECTATION";
	public static final String TEM_VALIDE_COLKEY = "TEM_VALIDE";

	// Relationships
	public static final String INDIVIDU_KEY = "individu";
	public static final String STRUCTURE_KEY = "structure";
	public static final String TO_V_PERSONNEL_NON_ENS_KEY = "toVPersonnelNonEns";

	// Utilities methods
  public EOAffectation localInstanceIn(EOEditingContext editingContext) {
    EOAffectation localInstance = (EOAffectation)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public NSTimestamp dDebAffectation() {
    return (NSTimestamp) storedValueForKey("dDebAffectation");
  }

  public void setDDebAffectation(NSTimestamp value) {
    takeStoredValueForKey(value, "dDebAffectation");
  }

  public Integer denQuotation() {
    return (Integer) storedValueForKey("denQuotation");
  }

  public void setDenQuotation(Integer value) {
    takeStoredValueForKey(value, "denQuotation");
  }

  public NSTimestamp dFinAffectation() {
    return (NSTimestamp) storedValueForKey("dFinAffectation");
  }

  public void setDFinAffectation(NSTimestamp value) {
    takeStoredValueForKey(value, "dFinAffectation");
  }

  public Integer numQuotation() {
    return (Integer) storedValueForKey("numQuotation");
  }

  public void setNumQuotation(Integer value) {
    takeStoredValueForKey(value, "numQuotation");
  }

  public String temValide() {
    return (String) storedValueForKey("temValide");
  }

  public void setTemValide(String value) {
    takeStoredValueForKey(value, "temValide");
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
  
  public fr.univlr.cri.conges.eos.modele.grhum.EOVPersonnelNonEns toVPersonnelNonEns() {
    return (fr.univlr.cri.conges.eos.modele.grhum.EOVPersonnelNonEns)storedValueForKey("toVPersonnelNonEns");
  }

  public void setToVPersonnelNonEnsRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOVPersonnelNonEns value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.grhum.EOVPersonnelNonEns oldValue = toVPersonnelNonEns();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "toVPersonnelNonEns");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "toVPersonnelNonEns");
    }
  }
  

  public static EOAffectation createAffectation(EOEditingContext editingContext, fr.univlr.cri.conges.eos.modele.grhum.EOIndividu individu, fr.univlr.cri.conges.eos.modele.grhum.EOStructure structure, fr.univlr.cri.conges.eos.modele.grhum.EOVPersonnelNonEns toVPersonnelNonEns) {
    EOAffectation eo = (EOAffectation) EOUtilities.createAndInsertInstance(editingContext, _EOAffectation.ENTITY_NAME);    
    eo.setIndividuRelationship(individu);
    eo.setStructureRelationship(structure);
    eo.setToVPersonnelNonEnsRelationship(toVPersonnelNonEns);
    return eo;
  }

  public static NSArray fetchAllAffectations(EOEditingContext editingContext) {
    return _EOAffectation.fetchAllAffectations(editingContext, null);
  }

  public static NSArray fetchAllAffectations(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOAffectation.fetchAffectations(editingContext, null, sortOrderings);
  }

  public static NSArray fetchAffectations(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOAffectation.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOAffectation fetchAffectation(EOEditingContext editingContext, String keyName, Object value) {
    return _EOAffectation.fetchAffectation(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOAffectation fetchAffectation(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOAffectation.fetchAffectations(editingContext, qualifier, null);
    EOAffectation eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOAffectation)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one Affectation that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOAffectation fetchRequiredAffectation(EOEditingContext editingContext, String keyName, Object value) {
    return _EOAffectation.fetchRequiredAffectation(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOAffectation fetchRequiredAffectation(EOEditingContext editingContext, EOQualifier qualifier) {
    EOAffectation eoObject = _EOAffectation.fetchAffectation(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no Affectation that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOAffectation localInstanceIn(EOEditingContext editingContext, EOAffectation eo) {
    EOAffectation localInstance = (eo == null) ? null : (EOAffectation)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
