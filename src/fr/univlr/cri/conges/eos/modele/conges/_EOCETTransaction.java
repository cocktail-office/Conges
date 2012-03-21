// _EOCETTransaction.java
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

// DO NOT EDIT.  Make changes to EOCETTransaction.java instead.
package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOCETTransaction extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "CETTransaction";
	public static final String ENTITY_TABLE_NAME = "CONGES.CNG_CET_TRANSACTION";

	// Attributes
	public static final String DATE_VALEUR_KEY = "dateValeur";
	public static final String D_CREATION_KEY = "dCreation";
	public static final String D_MODIFICATION_KEY = "dModification";
	public static final String MINUTES_DEBITEES_KEY = "minutesDebitees";
	public static final String MOTIF_KEY = "motif";
	public static final String VALEUR_KEY = "valeur";

	public static final String DATE_VALEUR_COLKEY = "DTE_VALEUR";
	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String D_MODIFICATION_COLKEY = "D_MODIFICATION";
	public static final String MINUTES_DEBITEES_COLKEY = "MINUTES_DEBITEES";
	public static final String MOTIF_COLKEY = "MOTIF";
	public static final String VALEUR_COLKEY = "VALEUR";

	// Relationships
	public static final String CET_KEY = "cet";
	public static final String TO_AFFECTATION_ANNUELLE_KEY = "toAffectationAnnuelle";

	// Utilities methods
  public EOCETTransaction localInstanceIn(EOEditingContext editingContext) {
    EOCETTransaction localInstance = (EOCETTransaction)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public NSTimestamp dateValeur() {
    return (NSTimestamp) storedValueForKey("dateValeur");
  }

  public void setDateValeur(NSTimestamp value) {
    takeStoredValueForKey(value, "dateValeur");
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

  public Integer minutesDebitees() {
    return (Integer) storedValueForKey("minutesDebitees");
  }

  public void setMinutesDebitees(Integer value) {
    takeStoredValueForKey(value, "minutesDebitees");
  }

  public String motif() {
    return (String) storedValueForKey("motif");
  }

  public void setMotif(String value) {
    takeStoredValueForKey(value, "motif");
  }

  public Integer valeur() {
    return (Integer) storedValueForKey("valeur");
  }

  public void setValeur(Integer value) {
    takeStoredValueForKey(value, "valeur");
  }

  public fr.univlr.cri.conges.eos.modele.conges.EOCET cet() {
    return (fr.univlr.cri.conges.eos.modele.conges.EOCET)storedValueForKey("cet");
  }

  public void setCetRelationship(fr.univlr.cri.conges.eos.modele.conges.EOCET value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.conges.EOCET oldValue = cet();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "cet");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "cet");
    }
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
  

  public static EOCETTransaction createCETTransaction(EOEditingContext editingContext, NSTimestamp dCreation
, NSTimestamp dModification
, Integer minutesDebitees
, fr.univlr.cri.conges.eos.modele.conges.EOCET cet) {
    EOCETTransaction eo = (EOCETTransaction) EOUtilities.createAndInsertInstance(editingContext, _EOCETTransaction.ENTITY_NAME);    
		eo.setDCreation(dCreation);
		eo.setDModification(dModification);
		eo.setMinutesDebitees(minutesDebitees);
    eo.setCetRelationship(cet);
    return eo;
  }

  public static NSArray fetchAllCETTransactions(EOEditingContext editingContext) {
    return _EOCETTransaction.fetchAllCETTransactions(editingContext, null);
  }

  public static NSArray fetchAllCETTransactions(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOCETTransaction.fetchCETTransactions(editingContext, null, sortOrderings);
  }

  public static NSArray fetchCETTransactions(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOCETTransaction.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOCETTransaction fetchCETTransaction(EOEditingContext editingContext, String keyName, Object value) {
    return _EOCETTransaction.fetchCETTransaction(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOCETTransaction fetchCETTransaction(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOCETTransaction.fetchCETTransactions(editingContext, qualifier, null);
    EOCETTransaction eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOCETTransaction)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one CETTransaction that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOCETTransaction fetchRequiredCETTransaction(EOEditingContext editingContext, String keyName, Object value) {
    return _EOCETTransaction.fetchRequiredCETTransaction(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOCETTransaction fetchRequiredCETTransaction(EOEditingContext editingContext, EOQualifier qualifier) {
    EOCETTransaction eoObject = _EOCETTransaction.fetchCETTransaction(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no CETTransaction that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOCETTransaction localInstanceIn(EOEditingContext editingContext, EOCETTransaction eo) {
    EOCETTransaction localInstance = (eo == null) ? null : (EOCETTransaction)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
