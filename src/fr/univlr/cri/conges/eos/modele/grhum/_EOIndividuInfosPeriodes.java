// _EOIndividuInfosPeriodes.java
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

// DO NOT EDIT.  Make changes to EOIndividuInfosPeriodes.java instead.
package fr.univlr.cri.conges.eos.modele.grhum;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOIndividuInfosPeriodes extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "IndividuInfosPeriodes";
	public static final String ENTITY_TABLE_NAME = "CONGES.INDIVIDU_INFOS_PERIODES";

	// Attributes
	public static final String D_CREATION_KEY = "dCreation";
	public static final String D_DEB_PERIODE_KEY = "dDebPeriode";
	public static final String D_FIN_PERIODE_KEY = "dFinPeriode";
	public static final String D_MODIFICATION_KEY = "dModification";
	public static final String HEURES_DUES_KEY = "heuresDues";
	public static final String HEURES_IMPUTEES3112_KEY = "heuresImputees3112";
	public static final String HEURES_RESTANTES_KEY = "heuresRestantes";
	public static final String HEURES_TRAVAILLEES_KEY = "heuresTravaillees";
	public static final String LIMITE_BASSE_KEY = "limiteBasse";
	public static final String NO_INDIVIDU_KEY = "noIndividu";
	public static final String TEM_VALIDE_KEY = "temValide";

	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String D_DEB_PERIODE_COLKEY = "D_DEB_PERIODE";
	public static final String D_FIN_PERIODE_COLKEY = "D_FIN_PERIODE";
	public static final String D_MODIFICATION_COLKEY = "D_MODIFICATION";
	public static final String HEURES_DUES_COLKEY = "HEURES_DUES";
	public static final String HEURES_IMPUTEES3112_COLKEY = "HEURES_IMPUTEES_3112";
	public static final String HEURES_RESTANTES_COLKEY = "HEURES_RESTANTES";
	public static final String HEURES_TRAVAILLEES_COLKEY = "HEURES_TRAVAILLEES";
	public static final String LIMITE_BASSE_COLKEY = "LIMITE_BASSE";
	public static final String NO_INDIVIDU_COLKEY = "NO_INDIVIDU";
	public static final String TEM_VALIDE_COLKEY = "TEM_VALIDE";

	// Relationships
	public static final String INDIVIDU_KEY = "individu";

	// Utilities methods
  public EOIndividuInfosPeriodes localInstanceIn(EOEditingContext editingContext) {
    EOIndividuInfosPeriodes localInstance = (EOIndividuInfosPeriodes)EOUtilities.localInstanceOfObject(editingContext, this);
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

  public NSTimestamp dDebPeriode() {
    return (NSTimestamp) storedValueForKey("dDebPeriode");
  }

  public void setDDebPeriode(NSTimestamp value) {
    takeStoredValueForKey(value, "dDebPeriode");
  }

  public NSTimestamp dFinPeriode() {
    return (NSTimestamp) storedValueForKey("dFinPeriode");
  }

  public void setDFinPeriode(NSTimestamp value) {
    takeStoredValueForKey(value, "dFinPeriode");
  }

  public NSTimestamp dModification() {
    return (NSTimestamp) storedValueForKey("dModification");
  }

  public void setDModification(NSTimestamp value) {
    takeStoredValueForKey(value, "dModification");
  }

  public String heuresDues() {
    return (String) storedValueForKey("heuresDues");
  }

  public void setHeuresDues(String value) {
    takeStoredValueForKey(value, "heuresDues");
  }

  public String heuresImputees3112() {
    return (String) storedValueForKey("heuresImputees3112");
  }

  public void setHeuresImputees3112(String value) {
    takeStoredValueForKey(value, "heuresImputees3112");
  }

  public String heuresRestantes() {
    return (String) storedValueForKey("heuresRestantes");
  }

  public void setHeuresRestantes(String value) {
    takeStoredValueForKey(value, "heuresRestantes");
  }

  public String heuresTravaillees() {
    return (String) storedValueForKey("heuresTravaillees");
  }

  public void setHeuresTravaillees(String value) {
    takeStoredValueForKey(value, "heuresTravaillees");
  }

  public String limiteBasse() {
    return (String) storedValueForKey("limiteBasse");
  }

  public void setLimiteBasse(String value) {
    takeStoredValueForKey(value, "limiteBasse");
  }

  public Integer noIndividu() {
    return (Integer) storedValueForKey("noIndividu");
  }

  public void setNoIndividu(Integer value) {
    takeStoredValueForKey(value, "noIndividu");
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
  

  public static EOIndividuInfosPeriodes createIndividuInfosPeriodes(EOEditingContext editingContext, NSTimestamp dCreation
, NSTimestamp dDebPeriode
, NSTimestamp dFinPeriode
, NSTimestamp dModification
, Integer noIndividu
, fr.univlr.cri.conges.eos.modele.grhum.EOIndividu individu) {
    EOIndividuInfosPeriodes eo = (EOIndividuInfosPeriodes) EOUtilities.createAndInsertInstance(editingContext, _EOIndividuInfosPeriodes.ENTITY_NAME);    
		eo.setDCreation(dCreation);
		eo.setDDebPeriode(dDebPeriode);
		eo.setDFinPeriode(dFinPeriode);
		eo.setDModification(dModification);
		eo.setNoIndividu(noIndividu);
    eo.setIndividuRelationship(individu);
    return eo;
  }

  public static NSArray fetchAllIndividuInfosPeriodeses(EOEditingContext editingContext) {
    return _EOIndividuInfosPeriodes.fetchAllIndividuInfosPeriodeses(editingContext, null);
  }

  public static NSArray fetchAllIndividuInfosPeriodeses(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOIndividuInfosPeriodes.fetchIndividuInfosPeriodeses(editingContext, null, sortOrderings);
  }

  public static NSArray fetchIndividuInfosPeriodeses(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOIndividuInfosPeriodes.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOIndividuInfosPeriodes fetchIndividuInfosPeriodes(EOEditingContext editingContext, String keyName, Object value) {
    return _EOIndividuInfosPeriodes.fetchIndividuInfosPeriodes(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOIndividuInfosPeriodes fetchIndividuInfosPeriodes(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOIndividuInfosPeriodes.fetchIndividuInfosPeriodeses(editingContext, qualifier, null);
    EOIndividuInfosPeriodes eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOIndividuInfosPeriodes)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one IndividuInfosPeriodes that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOIndividuInfosPeriodes fetchRequiredIndividuInfosPeriodes(EOEditingContext editingContext, String keyName, Object value) {
    return _EOIndividuInfosPeriodes.fetchRequiredIndividuInfosPeriodes(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOIndividuInfosPeriodes fetchRequiredIndividuInfosPeriodes(EOEditingContext editingContext, EOQualifier qualifier) {
    EOIndividuInfosPeriodes eoObject = _EOIndividuInfosPeriodes.fetchIndividuInfosPeriodes(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no IndividuInfosPeriodes that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOIndividuInfosPeriodes localInstanceIn(EOEditingContext editingContext, EOIndividuInfosPeriodes eo) {
    EOIndividuInfosPeriodes localInstance = (eo == null) ? null : (EOIndividuInfosPeriodes)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
