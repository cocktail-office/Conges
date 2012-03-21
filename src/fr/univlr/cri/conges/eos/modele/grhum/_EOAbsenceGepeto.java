// _EOAbsenceGepeto.java
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

// DO NOT EDIT.  Make changes to EOAbsenceGepeto.java instead.
package fr.univlr.cri.conges.eos.modele.grhum;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOAbsenceGepeto extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "AbsencesGepeto";
	public static final String ENTITY_TABLE_NAME = "GRHUM.ABSENCES";

	// Attributes
	public static final String ABS_AMPM_DEBUT_KEY = "absAmpmDebut";
	public static final String ABS_AMPM_FIN_KEY = "absAmpmFin";
	public static final String ABS_DEBUT_KEY = "absDebut";
	public static final String ABS_FIN_KEY = "absFin";
	public static final String ABS_MOTIF_KEY = "absMotif";
	public static final String ABS_VALIDE_KEY = "absValide";
	public static final String D_CREATION_KEY = "dCreation";
	public static final String D_MODIFICATION_KEY = "dModification";

	public static final String ABS_AMPM_DEBUT_COLKEY = "ABS_AMPM_DEBUT";
	public static final String ABS_AMPM_FIN_COLKEY = "ABS_AMPM_FIN";
	public static final String ABS_DEBUT_COLKEY = "ABS_DEBUT";
	public static final String ABS_FIN_COLKEY = "ABS_FIN";
	public static final String ABS_MOTIF_COLKEY = "ABS_MOTIF";
	public static final String ABS_VALIDE_COLKEY = "ABS_VALIDE";
	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String D_MODIFICATION_COLKEY = "D_MODIFICATION";

	// Relationships
	public static final String TO_INDIVIDU_KEY = "toIndividu";
	public static final String TO_TYPE_ABSENCE_GEPETO_KEY = "toTypeAbsenceGepeto";

	// Utilities methods
  public EOAbsenceGepeto localInstanceIn(EOEditingContext editingContext) {
    EOAbsenceGepeto localInstance = (EOAbsenceGepeto)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public String absAmpmDebut() {
    return (String) storedValueForKey("absAmpmDebut");
  }

  public void setAbsAmpmDebut(String value) {
    takeStoredValueForKey(value, "absAmpmDebut");
  }

  public String absAmpmFin() {
    return (String) storedValueForKey("absAmpmFin");
  }

  public void setAbsAmpmFin(String value) {
    takeStoredValueForKey(value, "absAmpmFin");
  }

  public NSTimestamp absDebut() {
    return (NSTimestamp) storedValueForKey("absDebut");
  }

  public void setAbsDebut(NSTimestamp value) {
    takeStoredValueForKey(value, "absDebut");
  }

  public NSTimestamp absFin() {
    return (NSTimestamp) storedValueForKey("absFin");
  }

  public void setAbsFin(NSTimestamp value) {
    takeStoredValueForKey(value, "absFin");
  }

  public String absMotif() {
    return (String) storedValueForKey("absMotif");
  }

  public void setAbsMotif(String value) {
    takeStoredValueForKey(value, "absMotif");
  }

  public String absValide() {
    return (String) storedValueForKey("absValide");
  }

  public void setAbsValide(String value) {
    takeStoredValueForKey(value, "absValide");
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
  
  public fr.univlr.cri.conges.eos.modele.conges.EOTypeAbsenceGepeto toTypeAbsenceGepeto() {
    return (fr.univlr.cri.conges.eos.modele.conges.EOTypeAbsenceGepeto)storedValueForKey("toTypeAbsenceGepeto");
  }

  public void setToTypeAbsenceGepetoRelationship(fr.univlr.cri.conges.eos.modele.conges.EOTypeAbsenceGepeto value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.conges.EOTypeAbsenceGepeto oldValue = toTypeAbsenceGepeto();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "toTypeAbsenceGepeto");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "toTypeAbsenceGepeto");
    }
  }
  

  public static EOAbsenceGepeto createAbsencesGepeto(EOEditingContext editingContext, fr.univlr.cri.conges.eos.modele.grhum.EOIndividu toIndividu) {
    EOAbsenceGepeto eo = (EOAbsenceGepeto) EOUtilities.createAndInsertInstance(editingContext, _EOAbsenceGepeto.ENTITY_NAME);    
    eo.setToIndividuRelationship(toIndividu);
    return eo;
  }

  public static NSArray fetchAllAbsencesGepetos(EOEditingContext editingContext) {
    return _EOAbsenceGepeto.fetchAllAbsencesGepetos(editingContext, null);
  }

  public static NSArray fetchAllAbsencesGepetos(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOAbsenceGepeto.fetchAbsencesGepetos(editingContext, null, sortOrderings);
  }

  public static NSArray fetchAbsencesGepetos(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOAbsenceGepeto.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOAbsenceGepeto fetchAbsencesGepeto(EOEditingContext editingContext, String keyName, Object value) {
    return _EOAbsenceGepeto.fetchAbsencesGepeto(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOAbsenceGepeto fetchAbsencesGepeto(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOAbsenceGepeto.fetchAbsencesGepetos(editingContext, qualifier, null);
    EOAbsenceGepeto eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOAbsenceGepeto)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one AbsencesGepeto that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOAbsenceGepeto fetchRequiredAbsencesGepeto(EOEditingContext editingContext, String keyName, Object value) {
    return _EOAbsenceGepeto.fetchRequiredAbsencesGepeto(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOAbsenceGepeto fetchRequiredAbsencesGepeto(EOEditingContext editingContext, EOQualifier qualifier) {
    EOAbsenceGepeto eoObject = _EOAbsenceGepeto.fetchAbsencesGepeto(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no AbsencesGepeto that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOAbsenceGepeto localInstanceIn(EOEditingContext editingContext, EOAbsenceGepeto eo) {
    EOAbsenceGepeto localInstance = (eo == null) ? null : (EOAbsenceGepeto)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
