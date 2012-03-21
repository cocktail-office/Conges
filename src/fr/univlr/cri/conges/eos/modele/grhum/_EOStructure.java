// _EOStructure.java
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

// DO NOT EDIT.  Make changes to EOStructure.java instead.
package fr.univlr.cri.conges.eos.modele.grhum;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOStructure extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "Structure";
	public static final String ENTITY_TABLE_NAME = "GRHUM.STRUCTURE_ULR";

	// Attributes
	public static final String C_STRUCTURE_KEY = "cStructure";
	public static final String C_STRUCTURE_COMPOSANTE_KEY = "cStructureComposante";
	public static final String C_TYPE_STRUCTURE_KEY = "cTypeStructure";
	public static final String IS_ARCHIVE_KEY = "isArchive";
	public static final String IS_COMPOSANTE_KEY = "isComposante";
	public static final String IS_SERVICE_KEY = "isService";
	public static final String IS_STRUCTURE_AUTORISEE_KEY = "isStructureAutorisee";
	public static final String LIBELLE_COURT_KEY = "libelleCourt";
	public static final String LIBELLE_LONG_KEY = "libelleLong";
	public static final String PERS_ID_KEY = "persId";

	public static final String C_STRUCTURE_COLKEY = "C_STRUCTURE";
	public static final String C_STRUCTURE_COMPOSANTE_COLKEY = "$attribute.columnName";
	public static final String C_TYPE_STRUCTURE_COLKEY = "C_TYPE_STRUCTURE";
	public static final String IS_ARCHIVE_COLKEY = "isArchive";
	public static final String IS_COMPOSANTE_COLKEY = "$attribute.columnName";
	public static final String IS_SERVICE_COLKEY = "$attribute.columnName";
	public static final String IS_STRUCTURE_AUTORISEE_COLKEY = "$attribute.columnName";
	public static final String LIBELLE_COURT_COLKEY = "LC_STRUCTURE";
	public static final String LIBELLE_LONG_COLKEY = "LL_STRUCTURE";
	public static final String PERS_ID_COLKEY = "PERS_ID";

	// Relationships
	public static final String INDIVIDUS_KEY = "individus";
	public static final String REPARTITIONS_KEY = "repartitions";
	public static final String RESPONSABLE_KEY = "responsable";
	public static final String TO_AFFECTATION_KEY = "toAffectation";
	public static final String TO_COMPOSANTE_KEY = "toComposante";
	public static final String TOS_ADRESSE_KEY = "tosAdresse";
	public static final String TOS_COMPOSANTE_ANNULATION_PERIODE_FERMETURE_KEY = "tosComposanteAnnulationPeriodeFermeture";
	public static final String TOS_REPART_PERSONNE_ADRESSE_KEY = "tosRepartPersonneAdresse";
	public static final String TOS_SERVICE_ANNULATION_PERIODE_FERMETURE_KEY = "tosServiceAnnulationPeriodeFermeture";
	public static final String TOS_STRUCTURE_FILLE_KEY = "tosStructureFille";
	public static final String TO_STRUCTURE_AUTORISEE_KEY = "toStructureAutorisee";
	public static final String TO_STRUCTURE_PERE_KEY = "toStructurePere";
	public static final String TO_V_SERVICE_KEY = "toVService";

	// Utilities methods
  public EOStructure localInstanceIn(EOEditingContext editingContext) {
    EOStructure localInstance = (EOStructure)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public String cStructure() {
    return (String) storedValueForKey("cStructure");
  }

  public void setCStructure(String value) {
    takeStoredValueForKey(value, "cStructure");
  }

  public String cStructureComposante() {
    return (String) storedValueForKey("cStructureComposante");
  }

  public void setCStructureComposante(String value) {
    takeStoredValueForKey(value, "cStructureComposante");
  }

  public String cTypeStructure() {
    return (String) storedValueForKey("cTypeStructure");
  }

  public void setCTypeStructure(String value) {
    takeStoredValueForKey(value, "cTypeStructure");
  }

  public String isArchive() {
    return (String) storedValueForKey("isArchive");
  }

  public void setIsArchive(String value) {
    takeStoredValueForKey(value, "isArchive");
  }

  public String isComposante() {
    return (String) storedValueForKey("isComposante");
  }

  public void setIsComposante(String value) {
    takeStoredValueForKey(value, "isComposante");
  }

  public String isService() {
    return (String) storedValueForKey("isService");
  }

  public void setIsService(String value) {
    takeStoredValueForKey(value, "isService");
  }

  public String isStructureAutorisee() {
    return (String) storedValueForKey("isStructureAutorisee");
  }

  public void setIsStructureAutorisee(String value) {
    takeStoredValueForKey(value, "isStructureAutorisee");
  }

  public String libelleCourt() {
    return (String) storedValueForKey("libelleCourt");
  }

  public void setLibelleCourt(String value) {
    takeStoredValueForKey(value, "libelleCourt");
  }

  public String libelleLong() {
    return (String) storedValueForKey("libelleLong");
  }

  public void setLibelleLong(String value) {
    takeStoredValueForKey(value, "libelleLong");
  }

  public Integer persId() {
    return (Integer) storedValueForKey("persId");
  }

  public void setPersId(Integer value) {
    takeStoredValueForKey(value, "persId");
  }

  public fr.univlr.cri.conges.eos.modele.grhum.EOIndividu responsable() {
    return (fr.univlr.cri.conges.eos.modele.grhum.EOIndividu)storedValueForKey("responsable");
  }

  public void setResponsableRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOIndividu value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.grhum.EOIndividu oldValue = responsable();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "responsable");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "responsable");
    }
  }
  
  public fr.univlr.cri.conges.eos.modele.grhum.EOStructure toComposante() {
    return (fr.univlr.cri.conges.eos.modele.grhum.EOStructure)storedValueForKey("toComposante");
  }

  public void setToComposanteRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOStructure value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.grhum.EOStructure oldValue = toComposante();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "toComposante");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "toComposante");
    }
  }
  
  public fr.univlr.cri.conges.eos.modele.grhum.EOStructure toStructurePere() {
    return (fr.univlr.cri.conges.eos.modele.grhum.EOStructure)storedValueForKey("toStructurePere");
  }

  public void setToStructurePereRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOStructure value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.grhum.EOStructure oldValue = toStructurePere();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "toStructurePere");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "toStructurePere");
    }
  }
  
  public fr.univlr.cri.conges.eos.modele.grhum.EOVService toVService() {
    return (fr.univlr.cri.conges.eos.modele.grhum.EOVService)storedValueForKey("toVService");
  }

  public void setToVServiceRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOVService value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.grhum.EOVService oldValue = toVService();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "toVService");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "toVService");
    }
  }
  
  public NSArray individus() {
    return (NSArray)storedValueForKey("individus");
  }

  public NSArray individus(EOQualifier qualifier) {
    return individus(qualifier, null);
  }

  public NSArray individus(EOQualifier qualifier, NSArray sortOrderings) {
    NSArray results;
      results = individus();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    return results;
  }
  
  public void addToIndividusRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOIndividu object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "individus");
  }

  public void removeFromIndividusRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOIndividu object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "individus");
  }

  public fr.univlr.cri.conges.eos.modele.grhum.EOIndividu createIndividusRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("IndividuPrsId");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "individus");
    return (fr.univlr.cri.conges.eos.modele.grhum.EOIndividu) eo;
  }

  public void deleteIndividusRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOIndividu object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "individus");
    editingContext().deleteObject(object);
  }

  public void deleteAllIndividusRelationships() {
    Enumeration objects = individus().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteIndividusRelationship((fr.univlr.cri.conges.eos.modele.grhum.EOIndividu)objects.nextElement());
    }
  }

  public NSArray repartitions() {
    return (NSArray)storedValueForKey("repartitions");
  }

  public NSArray repartitions(EOQualifier qualifier) {
    return repartitions(qualifier, null, false);
  }

  public NSArray repartitions(EOQualifier qualifier, boolean fetch) {
    return repartitions(qualifier, null, fetch);
  }

  public NSArray repartitions(EOQualifier qualifier, NSArray sortOrderings, boolean fetch) {
    NSArray results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(fr.univlr.cri.conges.eos.modele.grhum.EORepartition.STRUCTURE_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray qualifiers = new NSMutableArray();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = fr.univlr.cri.conges.eos.modele.grhum.EORepartition.fetchRepartitions(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = repartitions();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToRepartitionsRelationship(fr.univlr.cri.conges.eos.modele.grhum.EORepartition object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "repartitions");
  }

  public void removeFromRepartitionsRelationship(fr.univlr.cri.conges.eos.modele.grhum.EORepartition object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "repartitions");
  }

  public fr.univlr.cri.conges.eos.modele.grhum.EORepartition createRepartitionsRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("Repartition");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "repartitions");
    return (fr.univlr.cri.conges.eos.modele.grhum.EORepartition) eo;
  }

  public void deleteRepartitionsRelationship(fr.univlr.cri.conges.eos.modele.grhum.EORepartition object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "repartitions");
  }

  public void deleteAllRepartitionsRelationships() {
    Enumeration objects = repartitions().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteRepartitionsRelationship((fr.univlr.cri.conges.eos.modele.grhum.EORepartition)objects.nextElement());
    }
  }

  public NSArray toAffectation() {
    return (NSArray)storedValueForKey("toAffectation");
  }

  public NSArray toAffectation(EOQualifier qualifier) {
    return toAffectation(qualifier, null);
  }

  public NSArray toAffectation(EOQualifier qualifier, NSArray sortOrderings) {
    NSArray results;
      results = toAffectation();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    return results;
  }
  
  public void addToToAffectationRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOAffectation object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "toAffectation");
  }

  public void removeFromToAffectationRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOAffectation object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "toAffectation");
  }

  public fr.univlr.cri.conges.eos.modele.grhum.EOAffectation createToAffectationRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("Affectation");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "toAffectation");
    return (fr.univlr.cri.conges.eos.modele.grhum.EOAffectation) eo;
  }

  public void deleteToAffectationRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOAffectation object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "toAffectation");
    editingContext().deleteObject(object);
  }

  public void deleteAllToAffectationRelationships() {
    Enumeration objects = toAffectation().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteToAffectationRelationship((fr.univlr.cri.conges.eos.modele.grhum.EOAffectation)objects.nextElement());
    }
  }

  public NSArray tosAdresse() {
    return (NSArray)storedValueForKey("tosAdresse");
  }

  public NSArray tosAdresse(EOQualifier qualifier) {
    return tosAdresse(qualifier, null);
  }

  public NSArray tosAdresse(EOQualifier qualifier, NSArray sortOrderings) {
    NSArray results;
      results = tosAdresse();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    return results;
  }
  
  public void addToTosAdresseRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOAdresse object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "tosAdresse");
  }

  public void removeFromTosAdresseRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOAdresse object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosAdresse");
  }

  public fr.univlr.cri.conges.eos.modele.grhum.EOAdresse createTosAdresseRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("Adresse");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "tosAdresse");
    return (fr.univlr.cri.conges.eos.modele.grhum.EOAdresse) eo;
  }

  public void deleteTosAdresseRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOAdresse object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosAdresse");
    editingContext().deleteObject(object);
  }

  public void deleteAllTosAdresseRelationships() {
    Enumeration objects = tosAdresse().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteTosAdresseRelationship((fr.univlr.cri.conges.eos.modele.grhum.EOAdresse)objects.nextElement());
    }
  }

  public NSArray tosComposanteAnnulationPeriodeFermeture() {
    return (NSArray)storedValueForKey("tosComposanteAnnulationPeriodeFermeture");
  }

  public NSArray tosComposanteAnnulationPeriodeFermeture(EOQualifier qualifier) {
    return tosComposanteAnnulationPeriodeFermeture(qualifier, null);
  }

  public NSArray tosComposanteAnnulationPeriodeFermeture(EOQualifier qualifier, NSArray sortOrderings) {
    NSArray results;
      results = tosComposanteAnnulationPeriodeFermeture();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    return results;
  }
  
  public void addToTosComposanteAnnulationPeriodeFermetureRelationship(fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "tosComposanteAnnulationPeriodeFermeture");
  }

  public void removeFromTosComposanteAnnulationPeriodeFermetureRelationship(fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosComposanteAnnulationPeriodeFermeture");
  }

  public fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture createTosComposanteAnnulationPeriodeFermetureRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("AnnulationPeriodeFermeture");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "tosComposanteAnnulationPeriodeFermeture");
    return (fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture) eo;
  }

  public void deleteTosComposanteAnnulationPeriodeFermetureRelationship(fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosComposanteAnnulationPeriodeFermeture");
    editingContext().deleteObject(object);
  }

  public void deleteAllTosComposanteAnnulationPeriodeFermetureRelationships() {
    Enumeration objects = tosComposanteAnnulationPeriodeFermeture().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteTosComposanteAnnulationPeriodeFermetureRelationship((fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture)objects.nextElement());
    }
  }

  public NSArray tosRepartPersonneAdresse() {
    return (NSArray)storedValueForKey("tosRepartPersonneAdresse");
  }

  public NSArray tosRepartPersonneAdresse(EOQualifier qualifier) {
    return tosRepartPersonneAdresse(qualifier, null);
  }

  public NSArray tosRepartPersonneAdresse(EOQualifier qualifier, NSArray sortOrderings) {
    NSArray results;
      results = tosRepartPersonneAdresse();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    return results;
  }
  
  public void addToTosRepartPersonneAdresseRelationship(fr.univlr.cri.conges.eos.modele.grhum.EORepartPersonneAdresse object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "tosRepartPersonneAdresse");
  }

  public void removeFromTosRepartPersonneAdresseRelationship(fr.univlr.cri.conges.eos.modele.grhum.EORepartPersonneAdresse object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosRepartPersonneAdresse");
  }

  public fr.univlr.cri.conges.eos.modele.grhum.EORepartPersonneAdresse createTosRepartPersonneAdresseRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("RepartPersonneAdresse");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "tosRepartPersonneAdresse");
    return (fr.univlr.cri.conges.eos.modele.grhum.EORepartPersonneAdresse) eo;
  }

  public void deleteTosRepartPersonneAdresseRelationship(fr.univlr.cri.conges.eos.modele.grhum.EORepartPersonneAdresse object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosRepartPersonneAdresse");
    editingContext().deleteObject(object);
  }

  public void deleteAllTosRepartPersonneAdresseRelationships() {
    Enumeration objects = tosRepartPersonneAdresse().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteTosRepartPersonneAdresseRelationship((fr.univlr.cri.conges.eos.modele.grhum.EORepartPersonneAdresse)objects.nextElement());
    }
  }

  public NSArray tosServiceAnnulationPeriodeFermeture() {
    return (NSArray)storedValueForKey("tosServiceAnnulationPeriodeFermeture");
  }

  public NSArray tosServiceAnnulationPeriodeFermeture(EOQualifier qualifier) {
    return tosServiceAnnulationPeriodeFermeture(qualifier, null);
  }

  public NSArray tosServiceAnnulationPeriodeFermeture(EOQualifier qualifier, NSArray sortOrderings) {
    NSArray results;
      results = tosServiceAnnulationPeriodeFermeture();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    return results;
  }
  
  public void addToTosServiceAnnulationPeriodeFermetureRelationship(fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "tosServiceAnnulationPeriodeFermeture");
  }

  public void removeFromTosServiceAnnulationPeriodeFermetureRelationship(fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosServiceAnnulationPeriodeFermeture");
  }

  public fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture createTosServiceAnnulationPeriodeFermetureRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("AnnulationPeriodeFermeture");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "tosServiceAnnulationPeriodeFermeture");
    return (fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture) eo;
  }

  public void deleteTosServiceAnnulationPeriodeFermetureRelationship(fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosServiceAnnulationPeriodeFermeture");
    editingContext().deleteObject(object);
  }

  public void deleteAllTosServiceAnnulationPeriodeFermetureRelationships() {
    Enumeration objects = tosServiceAnnulationPeriodeFermeture().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteTosServiceAnnulationPeriodeFermetureRelationship((fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture)objects.nextElement());
    }
  }

  public NSArray tosStructureFille() {
    return (NSArray)storedValueForKey("tosStructureFille");
  }

  public NSArray tosStructureFille(EOQualifier qualifier) {
    return tosStructureFille(qualifier, null, false);
  }

  public NSArray tosStructureFille(EOQualifier qualifier, boolean fetch) {
    return tosStructureFille(qualifier, null, fetch);
  }

  public NSArray tosStructureFille(EOQualifier qualifier, NSArray sortOrderings, boolean fetch) {
    NSArray results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(fr.univlr.cri.conges.eos.modele.grhum.EOStructure.TO_STRUCTURE_PERE_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray qualifiers = new NSMutableArray();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = fr.univlr.cri.conges.eos.modele.grhum.EOStructure.fetchStructures(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = tosStructureFille();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToTosStructureFilleRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOStructure object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "tosStructureFille");
  }

  public void removeFromTosStructureFilleRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOStructure object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosStructureFille");
  }

  public fr.univlr.cri.conges.eos.modele.grhum.EOStructure createTosStructureFilleRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("Structure");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "tosStructureFille");
    return (fr.univlr.cri.conges.eos.modele.grhum.EOStructure) eo;
  }

  public void deleteTosStructureFilleRelationship(fr.univlr.cri.conges.eos.modele.grhum.EOStructure object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosStructureFille");
    editingContext().deleteObject(object);
  }

  public void deleteAllTosStructureFilleRelationships() {
    Enumeration objects = tosStructureFille().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteTosStructureFilleRelationship((fr.univlr.cri.conges.eos.modele.grhum.EOStructure)objects.nextElement());
    }
  }

  public NSArray toStructureAutorisee() {
    return (NSArray)storedValueForKey("toStructureAutorisee");
  }

  public NSArray toStructureAutorisee(EOQualifier qualifier) {
    return toStructureAutorisee(qualifier, null, false);
  }

  public NSArray toStructureAutorisee(EOQualifier qualifier, boolean fetch) {
    return toStructureAutorisee(qualifier, null, fetch);
  }

  public NSArray toStructureAutorisee(EOQualifier qualifier, NSArray sortOrderings, boolean fetch) {
    NSArray results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(fr.univlr.cri.conges.eos.modele.conges.EOStructureAutorisee.STRUCTURE_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray qualifiers = new NSMutableArray();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = fr.univlr.cri.conges.eos.modele.conges.EOStructureAutorisee.fetchStructureAutorisees(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = toStructureAutorisee();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToToStructureAutoriseeRelationship(fr.univlr.cri.conges.eos.modele.conges.EOStructureAutorisee object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "toStructureAutorisee");
  }

  public void removeFromToStructureAutoriseeRelationship(fr.univlr.cri.conges.eos.modele.conges.EOStructureAutorisee object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "toStructureAutorisee");
  }

  public fr.univlr.cri.conges.eos.modele.conges.EOStructureAutorisee createToStructureAutoriseeRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("StructureAutorisee");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "toStructureAutorisee");
    return (fr.univlr.cri.conges.eos.modele.conges.EOStructureAutorisee) eo;
  }

  public void deleteToStructureAutoriseeRelationship(fr.univlr.cri.conges.eos.modele.conges.EOStructureAutorisee object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "toStructureAutorisee");
    editingContext().deleteObject(object);
  }

  public void deleteAllToStructureAutoriseeRelationships() {
    Enumeration objects = toStructureAutorisee().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteToStructureAutoriseeRelationship((fr.univlr.cri.conges.eos.modele.conges.EOStructureAutorisee)objects.nextElement());
    }
  }


  public static EOStructure createStructure(EOEditingContext editingContext, String cStructure
, String isArchive
, String isComposante
, Integer persId
) {
    EOStructure eo = (EOStructure) EOUtilities.createAndInsertInstance(editingContext, _EOStructure.ENTITY_NAME);    
		eo.setCStructure(cStructure);
		eo.setIsArchive(isArchive);
		eo.setIsComposante(isComposante);
		eo.setPersId(persId);
    return eo;
  }

  public static NSArray fetchAllStructures(EOEditingContext editingContext) {
    return _EOStructure.fetchAllStructures(editingContext, null);
  }

  public static NSArray fetchAllStructures(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOStructure.fetchStructures(editingContext, null, sortOrderings);
  }

  public static NSArray fetchStructures(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOStructure.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOStructure fetchStructure(EOEditingContext editingContext, String keyName, Object value) {
    return _EOStructure.fetchStructure(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOStructure fetchStructure(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOStructure.fetchStructures(editingContext, qualifier, null);
    EOStructure eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOStructure)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one Structure that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOStructure fetchRequiredStructure(EOEditingContext editingContext, String keyName, Object value) {
    return _EOStructure.fetchRequiredStructure(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOStructure fetchRequiredStructure(EOEditingContext editingContext, EOQualifier qualifier) {
    EOStructure eoObject = _EOStructure.fetchStructure(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no Structure that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOStructure localInstanceIn(EOEditingContext editingContext, EOStructure eo) {
    EOStructure localInstance = (eo == null) ? null : (EOStructure)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
