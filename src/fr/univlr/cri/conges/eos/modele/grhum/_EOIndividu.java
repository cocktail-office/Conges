// _EOIndividu.java
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

// DO NOT EDIT.  Make changes to EOIndividu.java instead.
package fr.univlr.cri.conges.eos.modele.grhum;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOIndividu extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "Individu";
	public static final String ENTITY_TABLE_NAME = "GRHUM.INDIVIDU_ULR";

	// Attributes
	public static final String C_CIVILITE_KEY = "cCivilite";
	public static final String NOM_KEY = "nom";
	public static final String NOM_JF_KEY = "nomJF";
	public static final String OID_KEY = "oid";
	public static final String PERS_ID_KEY = "persId";
	public static final String PRENOM_KEY = "prenom";
	public static final String QUALITE_KEY = "qualite";

	public static final String C_CIVILITE_COLKEY = "C_CIVILITE";
	public static final String NOM_COLKEY = "NOM_USUEL";
	public static final String NOM_JF_COLKEY = "NOM_PATRONYMIQUE";
	public static final String OID_COLKEY = "NO_INDIVIDU";
	public static final String PERS_ID_COLKEY = "PERS_ID";
	public static final String PRENOM_COLKEY = "PRENOM";
	public static final String QUALITE_COLKEY = "IND_QUALITE";

	// Relationships
	public static final String REPART_COMPTES_KEY = "repartComptes";
	public static final String TOS_CET_KEY = "tosCET";
	public static final String TOS_CONTRAT_KEY = "tosContrat";
	public static final String TOS_ELEMENT_CARRIERE_KEY = "tosElementCarriere";
	public static final String TOS_ENFANT_KEY = "tosEnfant";
	public static final String TOS_PREFERENCE_KEY = "tosPreference";
	public static final String TOS_REPART_ENFANT_KEY = "tosRepartEnfant";
	public static final String TO_V_PERSONNEL_ACTUEL_KEY = "toVPersonnelActuel";

	// Utilities methods
  public EOIndividu localInstanceIn(EOEditingContext editingContext) {
    EOIndividu localInstance = (EOIndividu)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public String cCivilite() {
    return (String) storedValueForKey("cCivilite");
  }

  public void setCCivilite(String value) {
    takeStoredValueForKey(value, "cCivilite");
  }

  public String nom() {
    return (String) storedValueForKey("nom");
  }

  public void setNom(String value) {
    takeStoredValueForKey(value, "nom");
  }

  public String nomJF() {
    return (String) storedValueForKey("nomJF");
  }

  public void setNomJF(String value) {
    takeStoredValueForKey(value, "nomJF");
  }

  public Long oid() {
    return (Long) storedValueForKey("oid");
  }

  public void setOid(Long value) {
    takeStoredValueForKey(value, "oid");
  }

  public Double persId() {
    return (Double) storedValueForKey("persId");
  }

  public void setPersId(Double value) {
    takeStoredValueForKey(value, "persId");
  }

  public String prenom() {
    return (String) storedValueForKey("prenom");
  }

  public void setPrenom(String value) {
    takeStoredValueForKey(value, "prenom");
  }

  public String qualite() {
    return (String) storedValueForKey("qualite");
  }

  public void setQualite(String value) {
    takeStoredValueForKey(value, "qualite");
  }

  public fr.univlr.cri.conges.eos.modele.grhum.EOVPersonnelActuel toVPersonnelActuel() {
    return (fr.univlr.cri.conges.eos.modele.grhum.EOVPersonnelActuel)storedValueForKey("toVPersonnelActuel");
  }

  public void setToVPersonnelActuelRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOVPersonnelActuel value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.grhum.EOVPersonnelActuel oldValue = toVPersonnelActuel();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "toVPersonnelActuel");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "toVPersonnelActuel");
    }
  }
  
  public NSArray repartComptes() {
    return (NSArray)storedValueForKey("repartComptes");
  }

  public NSArray repartComptes(EOQualifier qualifier) {
    return repartComptes(qualifier, null);
  }

  public NSArray repartComptes(EOQualifier qualifier, NSArray sortOrderings) {
    NSArray results;
      results = repartComptes();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    return results;
  }
  
  public void addToRepartComptesRelationship(EOGenericRecord object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "repartComptes");
  }

  public void removeFromRepartComptesRelationship(EOGenericRecord object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "repartComptes");
  }

  public EOGenericRecord createRepartComptesRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("ul_RepartCompte");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "repartComptes");
    return (EOGenericRecord) eo;
  }

  public void deleteRepartComptesRelationship(EOGenericRecord object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "repartComptes");
    editingContext().deleteObject(object);
  }

  public void deleteAllRepartComptesRelationships() {
    Enumeration objects = repartComptes().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteRepartComptesRelationship((EOGenericRecord)objects.nextElement());
    }
  }

  public NSArray tosCET() {
    return (NSArray)storedValueForKey("tosCET");
  }

  public NSArray tosCET(EOQualifier qualifier) {
    return tosCET(qualifier, null, false);
  }

  public NSArray tosCET(EOQualifier qualifier, boolean fetch) {
    return tosCET(qualifier, null, fetch);
  }

  public NSArray tosCET(EOQualifier qualifier, NSArray sortOrderings, boolean fetch) {
    NSArray results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(fr.univlr.cri.conges.eos.modele.conges.EOCET.INDIVIDU_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray qualifiers = new NSMutableArray();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = fr.univlr.cri.conges.eos.modele.conges.EOCET.fetchCETs(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = tosCET();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToTosCETRelationship(fr.univlr.cri.conges.eos.modele.conges.EOCET object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "tosCET");
  }

  public void removeFromTosCETRelationship(fr.univlr.cri.conges.eos.modele.conges.EOCET object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosCET");
  }

  public fr.univlr.cri.conges.eos.modele.conges.EOCET createTosCETRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("CET");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "tosCET");
    return (fr.univlr.cri.conges.eos.modele.conges.EOCET) eo;
  }

  public void deleteTosCETRelationship(fr.univlr.cri.conges.eos.modele.conges.EOCET object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosCET");
    editingContext().deleteObject(object);
  }

  public void deleteAllTosCETRelationships() {
    Enumeration objects = tosCET().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteTosCETRelationship((fr.univlr.cri.conges.eos.modele.conges.EOCET)objects.nextElement());
    }
  }

  public NSArray tosContrat() {
    return (NSArray)storedValueForKey("tosContrat");
  }

  public NSArray tosContrat(EOQualifier qualifier) {
    return tosContrat(qualifier, null, false);
  }

  public NSArray tosContrat(EOQualifier qualifier, boolean fetch) {
    return tosContrat(qualifier, null, fetch);
  }

  public NSArray tosContrat(EOQualifier qualifier, NSArray sortOrderings, boolean fetch) {
    NSArray results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(fr.univlr.cri.conges.eos.modele.grhum.EOContrat.TO_INDIVIDU_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray qualifiers = new NSMutableArray();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = fr.univlr.cri.conges.eos.modele.grhum.EOContrat.fetchContrats(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = tosContrat();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToTosContratRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOContrat object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "tosContrat");
  }

  public void removeFromTosContratRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOContrat object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosContrat");
  }

  public fr.univlr.cri.conges.eos.modele.grhum.EOContrat createTosContratRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("Contrat");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "tosContrat");
    return (fr.univlr.cri.conges.eos.modele.grhum.EOContrat) eo;
  }

  public void deleteTosContratRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOContrat object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosContrat");
    editingContext().deleteObject(object);
  }

  public void deleteAllTosContratRelationships() {
    Enumeration objects = tosContrat().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteTosContratRelationship((fr.univlr.cri.conges.eos.modele.grhum.EOContrat)objects.nextElement());
    }
  }

  public NSArray tosElementCarriere() {
    return (NSArray)storedValueForKey("tosElementCarriere");
  }

  public NSArray tosElementCarriere(EOQualifier qualifier) {
    return tosElementCarriere(qualifier, null, false);
  }

  public NSArray tosElementCarriere(EOQualifier qualifier, boolean fetch) {
    return tosElementCarriere(qualifier, null, fetch);
  }

  public NSArray tosElementCarriere(EOQualifier qualifier, NSArray sortOrderings, boolean fetch) {
    NSArray results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(fr.univlr.cri.conges.eos.modele.grhum.EOElementCarriere.TO_INDIVIDU_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray qualifiers = new NSMutableArray();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = fr.univlr.cri.conges.eos.modele.grhum.EOElementCarriere.fetchElementCarrieres(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = tosElementCarriere();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToTosElementCarriereRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOElementCarriere object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "tosElementCarriere");
  }

  public void removeFromTosElementCarriereRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOElementCarriere object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosElementCarriere");
  }

  public fr.univlr.cri.conges.eos.modele.grhum.EOElementCarriere createTosElementCarriereRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("ElementCarriere");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "tosElementCarriere");
    return (fr.univlr.cri.conges.eos.modele.grhum.EOElementCarriere) eo;
  }

  public void deleteTosElementCarriereRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOElementCarriere object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosElementCarriere");
    editingContext().deleteObject(object);
  }

  public void deleteAllTosElementCarriereRelationships() {
    Enumeration objects = tosElementCarriere().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteTosElementCarriereRelationship((fr.univlr.cri.conges.eos.modele.grhum.EOElementCarriere)objects.nextElement());
    }
  }

  public NSArray tosEnfant() {
    return (NSArray)storedValueForKey("tosEnfant");
  }

  public NSArray tosEnfant(EOQualifier qualifier) {
    return tosEnfant(qualifier, null);
  }

  public NSArray tosEnfant(EOQualifier qualifier, NSArray sortOrderings) {
    NSArray results;
      results = tosEnfant();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    return results;
  }
  
  public void addToTosEnfantRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOEnfant object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "tosEnfant");
  }

  public void removeFromTosEnfantRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOEnfant object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosEnfant");
  }

  public fr.univlr.cri.conges.eos.modele.grhum.EOEnfant createTosEnfantRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("Enfant");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "tosEnfant");
    return (fr.univlr.cri.conges.eos.modele.grhum.EOEnfant) eo;
  }

  public void deleteTosEnfantRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOEnfant object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosEnfant");
    editingContext().deleteObject(object);
  }

  public void deleteAllTosEnfantRelationships() {
    Enumeration objects = tosEnfant().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteTosEnfantRelationship((fr.univlr.cri.conges.eos.modele.grhum.EOEnfant)objects.nextElement());
    }
  }

  public NSArray tosPreference() {
    return (NSArray)storedValueForKey("tosPreference");
  }

  public NSArray tosPreference(EOQualifier qualifier) {
    return tosPreference(qualifier, null, false);
  }

  public NSArray tosPreference(EOQualifier qualifier, boolean fetch) {
    return tosPreference(qualifier, null, fetch);
  }

  public NSArray tosPreference(EOQualifier qualifier, NSArray sortOrderings, boolean fetch) {
    NSArray results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(fr.univlr.cri.conges.eos.modele.conges.EOPreference.TO_INDIVIDU_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray qualifiers = new NSMutableArray();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = fr.univlr.cri.conges.eos.modele.conges.EOPreference.fetchPreferences(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = tosPreference();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToTosPreferenceRelationship(fr.univlr.cri.conges.eos.modele.conges.EOPreference object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "tosPreference");
  }

  public void removeFromTosPreferenceRelationship(fr.univlr.cri.conges.eos.modele.conges.EOPreference object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosPreference");
  }

  public fr.univlr.cri.conges.eos.modele.conges.EOPreference createTosPreferenceRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("Preference");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "tosPreference");
    return (fr.univlr.cri.conges.eos.modele.conges.EOPreference) eo;
  }

  public void deleteTosPreferenceRelationship(fr.univlr.cri.conges.eos.modele.conges.EOPreference object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosPreference");
    editingContext().deleteObject(object);
  }

  public void deleteAllTosPreferenceRelationships() {
    Enumeration objects = tosPreference().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteTosPreferenceRelationship((fr.univlr.cri.conges.eos.modele.conges.EOPreference)objects.nextElement());
    }
  }

  public NSArray tosRepartEnfant() {
    return (NSArray)storedValueForKey("tosRepartEnfant");
  }

  public NSArray tosRepartEnfant(EOQualifier qualifier) {
    return tosRepartEnfant(qualifier, null);
  }

  public NSArray tosRepartEnfant(EOQualifier qualifier, NSArray sortOrderings) {
    NSArray results;
      results = tosRepartEnfant();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    return results;
  }
  
  public void addToTosRepartEnfantRelationship(fr.univlr.cri.conges.eos.modele.grhum.EORepartEnfant object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "tosRepartEnfant");
  }

  public void removeFromTosRepartEnfantRelationship(fr.univlr.cri.conges.eos.modele.grhum.EORepartEnfant object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosRepartEnfant");
  }

  public fr.univlr.cri.conges.eos.modele.grhum.EORepartEnfant createTosRepartEnfantRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("RepartEnfant");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "tosRepartEnfant");
    return (fr.univlr.cri.conges.eos.modele.grhum.EORepartEnfant) eo;
  }

  public void deleteTosRepartEnfantRelationship(fr.univlr.cri.conges.eos.modele.grhum.EORepartEnfant object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosRepartEnfant");
    editingContext().deleteObject(object);
  }

  public void deleteAllTosRepartEnfantRelationships() {
    Enumeration objects = tosRepartEnfant().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteTosRepartEnfantRelationship((fr.univlr.cri.conges.eos.modele.grhum.EORepartEnfant)objects.nextElement());
    }
  }


  public static EOIndividu createIndividu(EOEditingContext editingContext, String nom
, Long oid
, Double persId
, String prenom
) {
    EOIndividu eo = (EOIndividu) EOUtilities.createAndInsertInstance(editingContext, _EOIndividu.ENTITY_NAME);    
		eo.setNom(nom);
		eo.setOid(oid);
		eo.setPersId(persId);
		eo.setPrenom(prenom);
    return eo;
  }

  public static NSArray fetchAllIndividus(EOEditingContext editingContext) {
    return _EOIndividu.fetchAllIndividus(editingContext, null);
  }

  public static NSArray fetchAllIndividus(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOIndividu.fetchIndividus(editingContext, null, sortOrderings);
  }

  public static NSArray fetchIndividus(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOIndividu.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOIndividu fetchIndividu(EOEditingContext editingContext, String keyName, Object value) {
    return _EOIndividu.fetchIndividu(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOIndividu fetchIndividu(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOIndividu.fetchIndividus(editingContext, qualifier, null);
    EOIndividu eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOIndividu)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one Individu that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOIndividu fetchRequiredIndividu(EOEditingContext editingContext, String keyName, Object value) {
    return _EOIndividu.fetchRequiredIndividu(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOIndividu fetchRequiredIndividu(EOEditingContext editingContext, EOQualifier qualifier) {
    EOIndividu eoObject = _EOIndividu.fetchIndividu(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no Individu that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOIndividu localInstanceIn(EOEditingContext editingContext, EOIndividu eo) {
    EOIndividu localInstance = (eo == null) ? null : (EOIndividu)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
