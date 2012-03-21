// _EOAffectationAnnuelle.java
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

// DO NOT EDIT.  Make changes to EOAffectationAnnuelle.java instead.
package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOAffectationAnnuelle extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "AffectationAnnuelle";
	public static final String ENTITY_TABLE_NAME = "CONGES.PLNG_AFF_ANN";

	// Attributes
	public static final String ANNEE_KEY = "annee";
	public static final String DATE_DEBUT_ANNEE_KEY = "dateDebutAnnee";
	public static final String D_CREATION_KEY = "dCreation";
	public static final String D_MODIFICATION_KEY = "dModification";
	public static final String FLAG_DEPASSEMENT_CONGES_AUTORISE_KEY = "flagDepassementCongesAutorise";
	public static final String FLAG_DEP_SEM_HAUTES_KEY = "flagDepSemHautes";
	public static final String FLAG_HORS_NORME_KEY = "flagHorsNorme";
	public static final String FLAG_PASSE_DROIT_KEY = "flagPasseDroit";
	public static final String FLAG_TEMPS_PARTIEL_ANNUALISE_KEY = "flagTempsPartielAnnualise";
	public static final String STATUS_PLANNING_KEY = "statusPlanning";

	public static final String ANNEE_COLKEY = "ANNEE";
	public static final String DATE_DEBUT_ANNEE_COLKEY = "DTE_DEBUT_ANNEE";
	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String D_MODIFICATION_COLKEY = "D_MODIFICATION";
	public static final String FLAG_DEPASSEMENT_CONGES_AUTORISE_COLKEY = "FLG_DEP_CONGES";
	public static final String FLAG_DEP_SEM_HAUTES_COLKEY = "FLG_DEP_SEM_HAUTES";
	public static final String FLAG_HORS_NORME_COLKEY = "FLG_HORS_NORME";
	public static final String FLAG_PASSE_DROIT_COLKEY = "FLG_PASSE_DROIT";
	public static final String FLAG_TEMPS_PARTIEL_ANNUALISE_COLKEY = "FLG_TPA";
	public static final String STATUS_PLANNING_COLKEY = "STS_PLANNING";

	// Relationships
	public static final String ANNULATION_PERIODE_FERMETURES_KEY = "annulationPeriodeFermetures";
	public static final String CALCUL_AFF_ANNS_KEY = "calculAffAnns";
	public static final String HORAIRES_KEY = "horaires";
	public static final String OCCUPATIONS_KEY = "occupations";
	public static final String PERIODES_KEY = "periodes";
	public static final String REPART_VALIDATIONS_KEY = "repartValidations";
	public static final String TO_INDIVIDU_DEMANDEUR_KEY = "toIndividuDemandeur";
	public static final String TOS_ALERTE_KEY = "tosAlerte";
	public static final String TOS_MOUVEMENT_KEY = "tosMouvement";
	public static final String TOS_PERIODE_FERMETURE_KEY = "tosPeriodeFermeture";

	// Utilities methods
  public EOAffectationAnnuelle localInstanceIn(EOEditingContext editingContext) {
    EOAffectationAnnuelle localInstance = (EOAffectationAnnuelle)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public String annee() {
    return (String) storedValueForKey("annee");
  }

  public void setAnnee(String value) {
    takeStoredValueForKey(value, "annee");
  }

  public NSTimestamp dateDebutAnnee() {
    return (NSTimestamp) storedValueForKey("dateDebutAnnee");
  }

  public void setDateDebutAnnee(NSTimestamp value) {
    takeStoredValueForKey(value, "dateDebutAnnee");
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

  public String flagDepassementCongesAutorise() {
    return (String) storedValueForKey("flagDepassementCongesAutorise");
  }

  public void setFlagDepassementCongesAutorise(String value) {
    takeStoredValueForKey(value, "flagDepassementCongesAutorise");
  }

  public String flagDepSemHautes() {
    return (String) storedValueForKey("flagDepSemHautes");
  }

  public void setFlagDepSemHautes(String value) {
    takeStoredValueForKey(value, "flagDepSemHautes");
  }

  public String flagHorsNorme() {
    return (String) storedValueForKey("flagHorsNorme");
  }

  public void setFlagHorsNorme(String value) {
    takeStoredValueForKey(value, "flagHorsNorme");
  }

  public String flagPasseDroit() {
    return (String) storedValueForKey("flagPasseDroit");
  }

  public void setFlagPasseDroit(String value) {
    takeStoredValueForKey(value, "flagPasseDroit");
  }

  public String flagTempsPartielAnnualise() {
    return (String) storedValueForKey("flagTempsPartielAnnualise");
  }

  public void setFlagTempsPartielAnnualise(String value) {
    takeStoredValueForKey(value, "flagTempsPartielAnnualise");
  }

  public String statusPlanning() {
    return (String) storedValueForKey("statusPlanning");
  }

  public void setStatusPlanning(String value) {
    takeStoredValueForKey(value, "statusPlanning");
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
  
  public NSArray annulationPeriodeFermetures() {
    return (NSArray)storedValueForKey("annulationPeriodeFermetures");
  }

  public NSArray annulationPeriodeFermetures(EOQualifier qualifier) {
    return annulationPeriodeFermetures(qualifier, null, false);
  }

  public NSArray annulationPeriodeFermetures(EOQualifier qualifier, boolean fetch) {
    return annulationPeriodeFermetures(qualifier, null, fetch);
  }

  public NSArray annulationPeriodeFermetures(EOQualifier qualifier, NSArray sortOrderings, boolean fetch) {
    NSArray results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture.TO_AFFECTATION_ANNUELLE_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray qualifiers = new NSMutableArray();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture.fetchAnnulationPeriodeFermetures(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = annulationPeriodeFermetures();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToAnnulationPeriodeFermeturesRelationship(fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "annulationPeriodeFermetures");
  }

  public void removeFromAnnulationPeriodeFermeturesRelationship(fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "annulationPeriodeFermetures");
  }

  public fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture createAnnulationPeriodeFermeturesRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("AnnulationPeriodeFermeture");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "annulationPeriodeFermetures");
    return (fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture) eo;
  }

  public void deleteAnnulationPeriodeFermeturesRelationship(fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "annulationPeriodeFermetures");
  }

  public void deleteAllAnnulationPeriodeFermeturesRelationships() {
    Enumeration objects = annulationPeriodeFermetures().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteAnnulationPeriodeFermeturesRelationship((fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture)objects.nextElement());
    }
  }

  public NSArray calculAffAnns() {
    return (NSArray)storedValueForKey("calculAffAnns");
  }

  public NSArray calculAffAnns(EOQualifier qualifier) {
    return calculAffAnns(qualifier, null, false);
  }

  public NSArray calculAffAnns(EOQualifier qualifier, boolean fetch) {
    return calculAffAnns(qualifier, null, fetch);
  }

  public NSArray calculAffAnns(EOQualifier qualifier, NSArray sortOrderings, boolean fetch) {
    NSArray results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(fr.univlr.cri.conges.eos.modele.planning.EOCalculAffectationAnnuelle.TO_AFFECTATION_ANNUELLE_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray qualifiers = new NSMutableArray();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = fr.univlr.cri.conges.eos.modele.planning.EOCalculAffectationAnnuelle.fetchCalculAffAnns(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = calculAffAnns();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToCalculAffAnnsRelationship(fr.univlr.cri.conges.eos.modele.planning.EOCalculAffectationAnnuelle object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "calculAffAnns");
  }

  public void removeFromCalculAffAnnsRelationship(fr.univlr.cri.conges.eos.modele.planning.EOCalculAffectationAnnuelle object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "calculAffAnns");
  }

  public fr.univlr.cri.conges.eos.modele.planning.EOCalculAffectationAnnuelle createCalculAffAnnsRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("CalculAffAnn");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "calculAffAnns");
    return (fr.univlr.cri.conges.eos.modele.planning.EOCalculAffectationAnnuelle) eo;
  }

  public void deleteCalculAffAnnsRelationship(fr.univlr.cri.conges.eos.modele.planning.EOCalculAffectationAnnuelle object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "calculAffAnns");
    editingContext().deleteObject(object);
  }

  public void deleteAllCalculAffAnnsRelationships() {
    Enumeration objects = calculAffAnns().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteCalculAffAnnsRelationship((fr.univlr.cri.conges.eos.modele.planning.EOCalculAffectationAnnuelle)objects.nextElement());
    }
  }

  public NSArray horaires() {
    return (NSArray)storedValueForKey("horaires");
  }

  public NSArray horaires(EOQualifier qualifier) {
    return horaires(qualifier, null, false);
  }

  public NSArray horaires(EOQualifier qualifier, boolean fetch) {
    return horaires(qualifier, null, fetch);
  }

  public NSArray horaires(EOQualifier qualifier, NSArray sortOrderings, boolean fetch) {
    NSArray results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(fr.univlr.cri.conges.eos.modele.planning.EOHoraire.AFFECTATION_ANNUELLE_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray qualifiers = new NSMutableArray();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = fr.univlr.cri.conges.eos.modele.planning.EOHoraire.fetchHoraires(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = horaires();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToHorairesRelationship(fr.univlr.cri.conges.eos.modele.planning.EOHoraire object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "horaires");
  }

  public void removeFromHorairesRelationship(fr.univlr.cri.conges.eos.modele.planning.EOHoraire object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "horaires");
  }

  public fr.univlr.cri.conges.eos.modele.planning.EOHoraire createHorairesRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("Horaire");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "horaires");
    return (fr.univlr.cri.conges.eos.modele.planning.EOHoraire) eo;
  }

  public void deleteHorairesRelationship(fr.univlr.cri.conges.eos.modele.planning.EOHoraire object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "horaires");
    editingContext().deleteObject(object);
  }

  public void deleteAllHorairesRelationships() {
    Enumeration objects = horaires().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteHorairesRelationship((fr.univlr.cri.conges.eos.modele.planning.EOHoraire)objects.nextElement());
    }
  }

  public NSArray occupations() {
    return (NSArray)storedValueForKey("occupations");
  }

  public NSArray occupations(EOQualifier qualifier) {
    return occupations(qualifier, null, false);
  }

  public NSArray occupations(EOQualifier qualifier, boolean fetch) {
    return occupations(qualifier, null, fetch);
  }

  public NSArray occupations(EOQualifier qualifier, NSArray sortOrderings, boolean fetch) {
    NSArray results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(fr.univlr.cri.conges.eos.modele.planning.EOOccupation.AFFECTATION_ANNUELLE_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray qualifiers = new NSMutableArray();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = fr.univlr.cri.conges.eos.modele.planning.EOOccupation.fetchOccupations(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = occupations();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToOccupationsRelationship(fr.univlr.cri.conges.eos.modele.planning.EOOccupation object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "occupations");
  }

  public void removeFromOccupationsRelationship(fr.univlr.cri.conges.eos.modele.planning.EOOccupation object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "occupations");
  }

  public fr.univlr.cri.conges.eos.modele.planning.EOOccupation createOccupationsRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("Occupation");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "occupations");
    return (fr.univlr.cri.conges.eos.modele.planning.EOOccupation) eo;
  }

  public void deleteOccupationsRelationship(fr.univlr.cri.conges.eos.modele.planning.EOOccupation object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "occupations");
    editingContext().deleteObject(object);
  }

  public void deleteAllOccupationsRelationships() {
    Enumeration objects = occupations().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteOccupationsRelationship((fr.univlr.cri.conges.eos.modele.planning.EOOccupation)objects.nextElement());
    }
  }

  public NSArray periodes() {
    return (NSArray)storedValueForKey("periodes");
  }

  public NSArray periodes(EOQualifier qualifier) {
    return periodes(qualifier, null, false);
  }

  public NSArray periodes(EOQualifier qualifier, boolean fetch) {
    return periodes(qualifier, null, fetch);
  }

  public NSArray periodes(EOQualifier qualifier, NSArray sortOrderings, boolean fetch) {
    NSArray results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(fr.univlr.cri.conges.eos.modele.planning.EOPeriodeAffectationAnnuelle.AFFECTATION_ANNUELLE_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray qualifiers = new NSMutableArray();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = fr.univlr.cri.conges.eos.modele.planning.EOPeriodeAffectationAnnuelle.fetchPeriodeAffectationAnnuelles(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = periodes();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToPeriodesRelationship(fr.univlr.cri.conges.eos.modele.planning.EOPeriodeAffectationAnnuelle object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "periodes");
  }

  public void removeFromPeriodesRelationship(fr.univlr.cri.conges.eos.modele.planning.EOPeriodeAffectationAnnuelle object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "periodes");
  }

  public fr.univlr.cri.conges.eos.modele.planning.EOPeriodeAffectationAnnuelle createPeriodesRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("PeriodeAffectationAnnuelle");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "periodes");
    return (fr.univlr.cri.conges.eos.modele.planning.EOPeriodeAffectationAnnuelle) eo;
  }

  public void deletePeriodesRelationship(fr.univlr.cri.conges.eos.modele.planning.EOPeriodeAffectationAnnuelle object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "periodes");
  }

  public void deleteAllPeriodesRelationships() {
    Enumeration objects = periodes().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deletePeriodesRelationship((fr.univlr.cri.conges.eos.modele.planning.EOPeriodeAffectationAnnuelle)objects.nextElement());
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
      EOQualifier inverseQualifier = new EOKeyValueQualifier(fr.univlr.cri.conges.eos.modele.conges.EORepartValidation.AFFECTATION_ANNUELLE_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
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

  public NSArray tosAlerte() {
    return (NSArray)storedValueForKey("tosAlerte");
  }

  public NSArray tosAlerte(EOQualifier qualifier) {
    return tosAlerte(qualifier, null, false);
  }

  public NSArray tosAlerte(EOQualifier qualifier, boolean fetch) {
    return tosAlerte(qualifier, null, fetch);
  }

  public NSArray tosAlerte(EOQualifier qualifier, NSArray sortOrderings, boolean fetch) {
    NSArray results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(fr.univlr.cri.conges.eos.modele.conges.EOAlerte.AFFECTATION_ANNUELLE_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
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
      results = tosAlerte();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToTosAlerteRelationship(fr.univlr.cri.conges.eos.modele.conges.EOAlerte object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "tosAlerte");
  }

  public void removeFromTosAlerteRelationship(fr.univlr.cri.conges.eos.modele.conges.EOAlerte object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosAlerte");
  }

  public fr.univlr.cri.conges.eos.modele.conges.EOAlerte createTosAlerteRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("Alerte");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "tosAlerte");
    return (fr.univlr.cri.conges.eos.modele.conges.EOAlerte) eo;
  }

  public void deleteTosAlerteRelationship(fr.univlr.cri.conges.eos.modele.conges.EOAlerte object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosAlerte");
    editingContext().deleteObject(object);
  }

  public void deleteAllTosAlerteRelationships() {
    Enumeration objects = tosAlerte().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteTosAlerteRelationship((fr.univlr.cri.conges.eos.modele.conges.EOAlerte)objects.nextElement());
    }
  }

  public NSArray tosMouvement() {
    return (NSArray)storedValueForKey("tosMouvement");
  }

  public NSArray tosMouvement(EOQualifier qualifier) {
    return tosMouvement(qualifier, null, false);
  }

  public NSArray tosMouvement(EOQualifier qualifier, boolean fetch) {
    return tosMouvement(qualifier, null, fetch);
  }

  public NSArray tosMouvement(EOQualifier qualifier, NSArray sortOrderings, boolean fetch) {
    NSArray results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(fr.univlr.cri.conges.eos.modele.planning.EOMouvement.TO_AFFECTATION_ANNUELLE_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray qualifiers = new NSMutableArray();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = fr.univlr.cri.conges.eos.modele.planning.EOMouvement.fetchMouvements(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = tosMouvement();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToTosMouvementRelationship(fr.univlr.cri.conges.eos.modele.planning.EOMouvement object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "tosMouvement");
  }

  public void removeFromTosMouvementRelationship(fr.univlr.cri.conges.eos.modele.planning.EOMouvement object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosMouvement");
  }

  public fr.univlr.cri.conges.eos.modele.planning.EOMouvement createTosMouvementRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("Mouvement");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "tosMouvement");
    return (fr.univlr.cri.conges.eos.modele.planning.EOMouvement) eo;
  }

  public void deleteTosMouvementRelationship(fr.univlr.cri.conges.eos.modele.planning.EOMouvement object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosMouvement");
  }

  public void deleteAllTosMouvementRelationships() {
    Enumeration objects = tosMouvement().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteTosMouvementRelationship((fr.univlr.cri.conges.eos.modele.planning.EOMouvement)objects.nextElement());
    }
  }

  public NSArray tosPeriodeFermeture() {
    return (NSArray)storedValueForKey("tosPeriodeFermeture");
  }

  public NSArray tosPeriodeFermeture(EOQualifier qualifier) {
    return tosPeriodeFermeture(qualifier, null, false);
  }

  public NSArray tosPeriodeFermeture(EOQualifier qualifier, boolean fetch) {
    return tosPeriodeFermeture(qualifier, null, fetch);
  }

  public NSArray tosPeriodeFermeture(EOQualifier qualifier, NSArray sortOrderings, boolean fetch) {
    NSArray results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(fr.univlr.cri.conges.eos.modele.planning.EOPeriodeFermeture.TO_AFFECTATION_ANNUELLE_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray qualifiers = new NSMutableArray();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = fr.univlr.cri.conges.eos.modele.planning.EOPeriodeFermeture.fetchPeriodeFermetures(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = tosPeriodeFermeture();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToTosPeriodeFermetureRelationship(fr.univlr.cri.conges.eos.modele.planning.EOPeriodeFermeture object) {
    addObjectToBothSidesOfRelationshipWithKey(object, "tosPeriodeFermeture");
  }

  public void removeFromTosPeriodeFermetureRelationship(fr.univlr.cri.conges.eos.modele.planning.EOPeriodeFermeture object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosPeriodeFermeture");
  }

  public fr.univlr.cri.conges.eos.modele.planning.EOPeriodeFermeture createTosPeriodeFermetureRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("PeriodeFermeture");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "tosPeriodeFermeture");
    return (fr.univlr.cri.conges.eos.modele.planning.EOPeriodeFermeture) eo;
  }

  public void deleteTosPeriodeFermetureRelationship(fr.univlr.cri.conges.eos.modele.planning.EOPeriodeFermeture object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "tosPeriodeFermeture");
    editingContext().deleteObject(object);
  }

  public void deleteAllTosPeriodeFermetureRelationships() {
    Enumeration objects = tosPeriodeFermeture().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteTosPeriodeFermetureRelationship((fr.univlr.cri.conges.eos.modele.planning.EOPeriodeFermeture)objects.nextElement());
    }
  }


  public static EOAffectationAnnuelle createAffectationAnnuelle(EOEditingContext editingContext, String annee
, NSTimestamp dateDebutAnnee
, NSTimestamp dCreation
, NSTimestamp dModification
, String flagDepassementCongesAutorise
, String flagDepSemHautes
, String flagHorsNorme
, String flagPasseDroit
, String flagTempsPartielAnnualise
, String statusPlanning
) {
    EOAffectationAnnuelle eo = (EOAffectationAnnuelle) EOUtilities.createAndInsertInstance(editingContext, _EOAffectationAnnuelle.ENTITY_NAME);    
		eo.setAnnee(annee);
		eo.setDateDebutAnnee(dateDebutAnnee);
		eo.setDCreation(dCreation);
		eo.setDModification(dModification);
		eo.setFlagDepassementCongesAutorise(flagDepassementCongesAutorise);
		eo.setFlagDepSemHautes(flagDepSemHautes);
		eo.setFlagHorsNorme(flagHorsNorme);
		eo.setFlagPasseDroit(flagPasseDroit);
		eo.setFlagTempsPartielAnnualise(flagTempsPartielAnnualise);
		eo.setStatusPlanning(statusPlanning);
    return eo;
  }

  public static NSArray fetchAllAffectationAnnuelles(EOEditingContext editingContext) {
    return _EOAffectationAnnuelle.fetchAllAffectationAnnuelles(editingContext, null);
  }

  public static NSArray fetchAllAffectationAnnuelles(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOAffectationAnnuelle.fetchAffectationAnnuelles(editingContext, null, sortOrderings);
  }

  public static NSArray fetchAffectationAnnuelles(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOAffectationAnnuelle.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOAffectationAnnuelle fetchAffectationAnnuelle(EOEditingContext editingContext, String keyName, Object value) {
    return _EOAffectationAnnuelle.fetchAffectationAnnuelle(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOAffectationAnnuelle fetchAffectationAnnuelle(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOAffectationAnnuelle.fetchAffectationAnnuelles(editingContext, qualifier, null);
    EOAffectationAnnuelle eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOAffectationAnnuelle)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one AffectationAnnuelle that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOAffectationAnnuelle fetchRequiredAffectationAnnuelle(EOEditingContext editingContext, String keyName, Object value) {
    return _EOAffectationAnnuelle.fetchRequiredAffectationAnnuelle(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOAffectationAnnuelle fetchRequiredAffectationAnnuelle(EOEditingContext editingContext, EOQualifier qualifier) {
    EOAffectationAnnuelle eoObject = _EOAffectationAnnuelle.fetchAffectationAnnuelle(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no AffectationAnnuelle that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOAffectationAnnuelle localInstanceIn(EOEditingContext editingContext, EOAffectationAnnuelle eo) {
    EOAffectationAnnuelle localInstance = (eo == null) ? null : (EOAffectationAnnuelle)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
