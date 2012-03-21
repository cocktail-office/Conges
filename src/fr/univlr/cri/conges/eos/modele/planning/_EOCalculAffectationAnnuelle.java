// _EOCalculAffectationAnnuelle.java
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

// DO NOT EDIT.  Make changes to EOCalculAffectationAnnuelle.java instead.
package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOCalculAffectationAnnuelle extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "CalculAffAnn";
	public static final String ENTITY_TABLE_NAME = "CONGES.PLNG_CLC_AFF_ANN";

	// Attributes
	public static final String D_CREATION_KEY = "dCreation";
	public static final String D_MODIFICATION_KEY = "dModification";
	public static final String FLAG_NATURE_KEY = "flagNature";
	public static final String MINUTES_BILAN_KEY = "minutesBilan";
	public static final String MINUTES_CONGES_CONSOMMEES_KEY = "minutesCongesConsommees";
	public static final String MINUTES_CONSOMMEES_DATE_RELIQUAT_KEY = "minutesConsommeesDateReliquat";
	public static final String MINUTES_DECHARGE_SYNDICALE_RESTANTES_KEY = "minutesDechargeSyndicaleRestantes";
	public static final String MINUTES_DECOMPTE_LEGAL_KEY = "minutesDecompteLegal";
	public static final String MINUTES_DUES_KEY = "minutesDues";
	public static final String MINUTES_RELIQUAT_CONSOMMEES_DATE_RELIQUAT_KEY = "minutesReliquatConsommeesDateReliquat";
	public static final String MINUTES_RELIQUAT_RESTANTES_KEY = "minutesReliquatRestantes";
	public static final String MINUTES_RESTANTES_KEY = "minutesRestantes";
	public static final String MINUTES_TRAVAILLEES_KEY = "minutesTravaillees";

	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String D_MODIFICATION_COLKEY = "D_MODIFICATION";
	public static final String FLAG_NATURE_COLKEY = "FLG_NATURE";
	public static final String MINUTES_BILAN_COLKEY = "MINUTES_BILAN";
	public static final String MINUTES_CONGES_CONSOMMEES_COLKEY = "MINUTES_CONGES_CONSOMMEES";
	public static final String MINUTES_CONSOMMEES_DATE_RELIQUAT_COLKEY = "MINUTES_CONS_DTE_RELIQUAT";
	public static final String MINUTES_DECHARGE_SYNDICALE_RESTANTES_COLKEY = "MINUTES_SYND_RESTANTES";
	public static final String MINUTES_DECOMPTE_LEGAL_COLKEY = "MINUTES_DECOMPTE_LEGAL";
	public static final String MINUTES_DUES_COLKEY = "MINUTES_DUES";
	public static final String MINUTES_RELIQUAT_CONSOMMEES_DATE_RELIQUAT_COLKEY = "MINUTES_REL_CONS_DTE_RELIQUAT";
	public static final String MINUTES_RELIQUAT_RESTANTES_COLKEY = "MINUTES_RELIQUAT_RESTANT";
	public static final String MINUTES_RESTANTES_COLKEY = "MINUTES_RESTANTES";
	public static final String MINUTES_TRAVAILLEES_COLKEY = "MINUTES_TRAVAILLEES";

	// Relationships
	public static final String TO_AFFECTATION_ANNUELLE_KEY = "toAffectationAnnuelle";

	// Utilities methods
  public EOCalculAffectationAnnuelle localInstanceIn(EOEditingContext editingContext) {
    EOCalculAffectationAnnuelle localInstance = (EOCalculAffectationAnnuelle)EOUtilities.localInstanceOfObject(editingContext, this);
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

  public String flagNature() {
    return (String) storedValueForKey("flagNature");
  }

  public void setFlagNature(String value) {
    takeStoredValueForKey(value, "flagNature");
  }

  public Integer minutesBilan() {
    return (Integer) storedValueForKey("minutesBilan");
  }

  public void setMinutesBilan(Integer value) {
    takeStoredValueForKey(value, "minutesBilan");
  }

  public Integer minutesCongesConsommees() {
    return (Integer) storedValueForKey("minutesCongesConsommees");
  }

  public void setMinutesCongesConsommees(Integer value) {
    takeStoredValueForKey(value, "minutesCongesConsommees");
  }

  public Integer minutesConsommeesDateReliquat() {
    return (Integer) storedValueForKey("minutesConsommeesDateReliquat");
  }

  public void setMinutesConsommeesDateReliquat(Integer value) {
    takeStoredValueForKey(value, "minutesConsommeesDateReliquat");
  }

  public Integer minutesDechargeSyndicaleRestantes() {
    return (Integer) storedValueForKey("minutesDechargeSyndicaleRestantes");
  }

  public void setMinutesDechargeSyndicaleRestantes(Integer value) {
    takeStoredValueForKey(value, "minutesDechargeSyndicaleRestantes");
  }

  public Integer minutesDecompteLegal() {
    return (Integer) storedValueForKey("minutesDecompteLegal");
  }

  public void setMinutesDecompteLegal(Integer value) {
    takeStoredValueForKey(value, "minutesDecompteLegal");
  }

  public Integer minutesDues() {
    return (Integer) storedValueForKey("minutesDues");
  }

  public void setMinutesDues(Integer value) {
    takeStoredValueForKey(value, "minutesDues");
  }

  public Integer minutesReliquatConsommeesDateReliquat() {
    return (Integer) storedValueForKey("minutesReliquatConsommeesDateReliquat");
  }

  public void setMinutesReliquatConsommeesDateReliquat(Integer value) {
    takeStoredValueForKey(value, "minutesReliquatConsommeesDateReliquat");
  }

  public Integer minutesReliquatRestantes() {
    return (Integer) storedValueForKey("minutesReliquatRestantes");
  }

  public void setMinutesReliquatRestantes(Integer value) {
    takeStoredValueForKey(value, "minutesReliquatRestantes");
  }

  public Integer minutesRestantes() {
    return (Integer) storedValueForKey("minutesRestantes");
  }

  public void setMinutesRestantes(Integer value) {
    takeStoredValueForKey(value, "minutesRestantes");
  }

  public Integer minutesTravaillees() {
    return (Integer) storedValueForKey("minutesTravaillees");
  }

  public void setMinutesTravaillees(Integer value) {
    takeStoredValueForKey(value, "minutesTravaillees");
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
  

  public static EOCalculAffectationAnnuelle createCalculAffAnn(EOEditingContext editingContext, NSTimestamp dCreation
, NSTimestamp dModification
, String flagNature
, Integer minutesBilan
, Integer minutesCongesConsommees
, Integer minutesConsommeesDateReliquat
, Integer minutesDechargeSyndicaleRestantes
, Integer minutesDecompteLegal
, Integer minutesDues
, Integer minutesReliquatConsommeesDateReliquat
, Integer minutesReliquatRestantes
, Integer minutesRestantes
, Integer minutesTravaillees
, fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle toAffectationAnnuelle) {
    EOCalculAffectationAnnuelle eo = (EOCalculAffectationAnnuelle) EOUtilities.createAndInsertInstance(editingContext, _EOCalculAffectationAnnuelle.ENTITY_NAME);    
		eo.setDCreation(dCreation);
		eo.setDModification(dModification);
		eo.setFlagNature(flagNature);
		eo.setMinutesBilan(minutesBilan);
		eo.setMinutesCongesConsommees(minutesCongesConsommees);
		eo.setMinutesConsommeesDateReliquat(minutesConsommeesDateReliquat);
		eo.setMinutesDechargeSyndicaleRestantes(minutesDechargeSyndicaleRestantes);
		eo.setMinutesDecompteLegal(minutesDecompteLegal);
		eo.setMinutesDues(minutesDues);
		eo.setMinutesReliquatConsommeesDateReliquat(minutesReliquatConsommeesDateReliquat);
		eo.setMinutesReliquatRestantes(minutesReliquatRestantes);
		eo.setMinutesRestantes(minutesRestantes);
		eo.setMinutesTravaillees(minutesTravaillees);
    eo.setToAffectationAnnuelleRelationship(toAffectationAnnuelle);
    return eo;
  }

  public static NSArray fetchAllCalculAffAnns(EOEditingContext editingContext) {
    return _EOCalculAffectationAnnuelle.fetchAllCalculAffAnns(editingContext, null);
  }

  public static NSArray fetchAllCalculAffAnns(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOCalculAffectationAnnuelle.fetchCalculAffAnns(editingContext, null, sortOrderings);
  }

  public static NSArray fetchCalculAffAnns(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOCalculAffectationAnnuelle.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOCalculAffectationAnnuelle fetchCalculAffAnn(EOEditingContext editingContext, String keyName, Object value) {
    return _EOCalculAffectationAnnuelle.fetchCalculAffAnn(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOCalculAffectationAnnuelle fetchCalculAffAnn(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOCalculAffectationAnnuelle.fetchCalculAffAnns(editingContext, qualifier, null);
    EOCalculAffectationAnnuelle eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOCalculAffectationAnnuelle)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one CalculAffAnn that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOCalculAffectationAnnuelle fetchRequiredCalculAffAnn(EOEditingContext editingContext, String keyName, Object value) {
    return _EOCalculAffectationAnnuelle.fetchRequiredCalculAffAnn(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOCalculAffectationAnnuelle fetchRequiredCalculAffAnn(EOEditingContext editingContext, EOQualifier qualifier) {
    EOCalculAffectationAnnuelle eoObject = _EOCalculAffectationAnnuelle.fetchCalculAffAnn(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no CalculAffAnn that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOCalculAffectationAnnuelle localInstanceIn(EOEditingContext editingContext, EOCalculAffectationAnnuelle eo) {
    EOCalculAffectationAnnuelle localInstance = (eo == null) ? null : (EOCalculAffectationAnnuelle)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
