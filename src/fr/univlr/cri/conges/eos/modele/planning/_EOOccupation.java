// _EOOccupation.java
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

// DO NOT EDIT.  Make changes to EOOccupation.java instead.
package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOOccupation extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "Occupation";
	public static final String ENTITY_TABLE_NAME = "CONGES.PLNG_OCC";

	// Attributes
	public static final String DATE_DEBUT_KEY = "dateDebut";
	public static final String DATE_FIN_KEY = "dateFin";
	public static final String D_CREATION_KEY = "dCreation";
	public static final String D_MODIFICATION_KEY = "dModification";
	public static final String DUREE_REELLE_KEY = "dureeReelle";
	public static final String FLAG_NATURE_KEY = "flagNature";
	public static final String MOTIF_KEY = "motif";
	public static final String OID_AFFECTATION_ANNUELLE_KEY = "oidAffectationAnnuelle";
	public static final String OID_TYPE_OCCUPATION_KEY = "oidTypeOccupation";
	public static final String STATUS_KEY = "status";
	public static final String VALEUR_MINUTES_KEY = "valeurMinutes";

	public static final String DATE_DEBUT_COLKEY = "DTE_DEBUT";
	public static final String DATE_FIN_COLKEY = "DTE_FIN";
	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String D_MODIFICATION_COLKEY = "D_MODIFICATION";
	public static final String DUREE_REELLE_COLKEY = "DUREE";
	public static final String FLAG_NATURE_COLKEY = "FLG_NATURE";
	public static final String MOTIF_COLKEY = "MOTIF";
	public static final String OID_AFFECTATION_ANNUELLE_COLKEY = "OID_AFF_ANN";
	public static final String OID_TYPE_OCCUPATION_COLKEY = "TYPE";
	public static final String STATUS_COLKEY = "STATUS";
	public static final String VALEUR_MINUTES_COLKEY = "VALEUR";

	// Relationships
	public static final String AFFECTATION_ANNUELLE_KEY = "affectationAnnuelle";
	public static final String ALERTES_KEY = "alertes";
	public static final String REPART_VALIDATIONS_KEY = "repartValidations";
	public static final String TO_INDIVIDU_DEMANDEUR_KEY = "toIndividuDemandeur";
	public static final String TYPE_OCCUPATION_KEY = "typeOccupation";

	// Utilities methods
  public EOOccupation localInstanceIn(EOEditingContext editingContext) {
    EOOccupation localInstance = (EOOccupation)EOUtilities.localInstanceOfObject(editingContext, this);
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

  public Integer dureeReelle() {
    return (Integer) storedValueForKey("dureeReelle");
  }

  public void setDureeReelle(Integer value) {
    takeStoredValueForKey(value, "dureeReelle");
  }

  public String flagNature() {
    return (String) storedValueForKey("flagNature");
  }

  public void setFlagNature(String value) {
    takeStoredValueForKey(value, "flagNature");
  }

  public String motif() {
    return (String) storedValueForKey("motif");
  }

  public void setMotif(String value) {
    takeStoredValueForKey(value, "motif");
  }

  public Integer oidAffectationAnnuelle() {
    return (Integer) storedValueForKey("oidAffectationAnnuelle");
  }

  public void setOidAffectationAnnuelle(Integer value) {
    takeStoredValueForKey(value, "oidAffectationAnnuelle");
  }

  public Integer oidTypeOccupation() {
    return (Integer) storedValueForKey("oidTypeOccupation");
  }

  public void setOidTypeOccupation(Integer value) {
    takeStoredValueForKey(value, "oidTypeOccupation");
  }

  public String status() {
    return (String) storedValueForKey("status");
  }

  public void setStatus(String value) {
    takeStoredValueForKey(value, "status");
  }

  public Integer valeurMinutes() {
    return (Integer) storedValueForKey("valeurMinutes");
  }

  public void setValeurMinutes(Integer value) {
    takeStoredValueForKey(value, "valeurMinutes");
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
  
  public fr.univlr.cri.conges.eos.modele.grhum.EOIndividu toIndividuDemandeur() {
    return (fr.univlr.cri.conges.eos.modele.grhum.EOIndividu)storedValueForKey("toIndividuDemandeur");
  }

  public void setToIndividuDemandeurRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOIndividu value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.grhum.EOIndividu oldValue = toIndividuDemandeur();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "toIndividuDemandeur");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "toIndividuDemandeur");
    }
  }
  
  public fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation typeOccupation() {
    return (fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation)storedValueForKey("typeOccupation");
  }

  public void setTypeOccupationRelationship(fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation oldValue = typeOccupation();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "typeOccupation");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "typeOccupation");
    }
  }
  
  public NSArray alertes() {
    return (NSArray)storedValueForKey("alertes");
  }

  public NSArray alertes(EOQualifier qualifier) {
    return alertes(qualifier, null, false);
  }

  public NSArray alertes(EOQualifier qualifier, boolean fetch) {
    return alertes(qualifier, null, fetch);
  }

  public NSArray alertes(EOQualifier qualifier, NSArray sortOrderings, boolean fetch) {
    NSArray results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(fr.univlr.cri.conges.eos.modele.conges.EOAlerte.OCCUPATION_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray qualifiers = new NSMutableArray();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = fr.univlr.cri.conges.eos.modele.conges.EOAlerte.fetchAlertes(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = alertes();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToAlertesRelationship(fr.univlr.cri.conges.eos.modele.conges.EOAlerte object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "alertes");
  }

  public void removeFromAlertesRelationship(fr.univlr.cri.conges.eos.modele.conges.EOAlerte object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "alertes");
  }

  public fr.univlr.cri.conges.eos.modele.conges.EOAlerte createAlertesRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("Alerte");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "alertes");
    return (fr.univlr.cri.conges.eos.modele.conges.EOAlerte) eo;
  }

  public void deleteAlertesRelationship(fr.univlr.cri.conges.eos.modele.conges.EOAlerte object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "alertes");
    editingContext().deleteObject(object);
  }

  public void deleteAllAlertesRelationships() {
    Enumeration objects = alertes().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteAlertesRelationship((fr.univlr.cri.conges.eos.modele.conges.EOAlerte)objects.nextElement());
    }
  }

  public NSArray repartValidations() {
    return (NSArray)storedValueForKey("repartValidations");
  }

  public NSArray repartValidations(EOQualifier qualifier) {
    return repartValidations(qualifier, null, false);
  }

  public NSArray repartValidations(EOQualifier qualifier, boolean fetch) {
    return repartValidations(qualifier, null, fetch);
  }

  public NSArray repartValidations(EOQualifier qualifier, NSArray sortOrderings, boolean fetch) {
    NSArray results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(fr.univlr.cri.conges.eos.modele.conges.EORepartValidation.OCCUPATION_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray qualifiers = new NSMutableArray();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = fr.univlr.cri.conges.eos.modele.conges.EORepartValidation.fetchRepartValidations(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = repartValidations();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToRepartValidationsRelationship(fr.univlr.cri.conges.eos.modele.conges.EORepartValidation object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "repartValidations");
  }

  public void removeFromRepartValidationsRelationship(fr.univlr.cri.conges.eos.modele.conges.EORepartValidation object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "repartValidations");
  }

  public fr.univlr.cri.conges.eos.modele.conges.EORepartValidation createRepartValidationsRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("RepartValidation");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "repartValidations");
    return (fr.univlr.cri.conges.eos.modele.conges.EORepartValidation) eo;
  }

  public void deleteRepartValidationsRelationship(fr.univlr.cri.conges.eos.modele.conges.EORepartValidation object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "repartValidations");
  }

  public void deleteAllRepartValidationsRelationships() {
    Enumeration objects = repartValidations().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteRepartValidationsRelationship((fr.univlr.cri.conges.eos.modele.conges.EORepartValidation)objects.nextElement());
    }
  }


  public static EOOccupation createOccupation(EOEditingContext editingContext, NSTimestamp dCreation
, NSTimestamp dModification
) {
    EOOccupation eo = (EOOccupation) EOUtilities.createAndInsertInstance(editingContext, _EOOccupation.ENTITY_NAME);    
		eo.setDCreation(dCreation);
		eo.setDModification(dModification);
    return eo;
  }

  public static NSArray fetchAllOccupations(EOEditingContext editingContext) {
    return _EOOccupation.fetchAllOccupations(editingContext, null);
  }

  public static NSArray fetchAllOccupations(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOOccupation.fetchOccupations(editingContext, null, sortOrderings);
  }

  public static NSArray fetchOccupations(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOOccupation.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOOccupation fetchOccupation(EOEditingContext editingContext, String keyName, Object value) {
    return _EOOccupation.fetchOccupation(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOOccupation fetchOccupation(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOOccupation.fetchOccupations(editingContext, qualifier, null);
    EOOccupation eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOOccupation)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one Occupation that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOOccupation fetchRequiredOccupation(EOEditingContext editingContext, String keyName, Object value) {
    return _EOOccupation.fetchRequiredOccupation(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOOccupation fetchRequiredOccupation(EOEditingContext editingContext, EOQualifier qualifier) {
    EOOccupation eoObject = _EOOccupation.fetchOccupation(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no Occupation that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOOccupation localInstanceIn(EOEditingContext editingContext, EOOccupation eo) {
    EOOccupation localInstance = (eo == null) ? null : (EOOccupation)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
