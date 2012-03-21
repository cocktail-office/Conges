// _EOVIndividuConges.java
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

// DO NOT EDIT.  Make changes to EOVIndividuConges.java instead.
package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOVIndividuConges extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "VIndividuConges";
	public static final String ENTITY_TABLE_NAME = "CONGES.V_INDIVIDU_CONGES";

	// Attributes
	public static final String CNG_CONSOMME_KEY = "cngConsomme";
	public static final String CNG_FINAL_KEY = "cngFinal";
	public static final String CNG_INITIAL_KEY = "cngInitial";
	public static final String C_STRUCTURE_KEY = "cStructure";
	public static final String D_DEB_AFFECTATION_KEY = "dDebAffectation";
	public static final String D_FIN_AFFECTATION_KEY = "dFinAffectation";
	public static final String DTE_DEBUT_ANNEE_KEY = "dteDebutAnnee";
	public static final String DTE_FIN_ANNEE_KEY = "dteFinAnnee";
	public static final String LC_STRUCTURE_KEY = "lcStructure";
	public static final String NO_INDIVIDU_KEY = "noIndividu";
	public static final String NOM_USUEL_KEY = "nomUsuel";
	public static final String NO_SEQ_AFFECTATION_KEY = "noSeqAffectation";
	public static final String OID_AFF_ANN_KEY = "oidAffAnn";
	public static final String OID_PER_AFF_ANN_KEY = "oidPerAffAnn";
	public static final String PRENOM_KEY = "prenom";
	public static final String QUOTITE_KEY = "quotite";
	public static final String REGULATION_KEY = "regulation";
	public static final String REL_INIT_KEY = "relInit";

	public static final String CNG_CONSOMME_COLKEY = "CNG_CONSOMME";
	public static final String CNG_FINAL_COLKEY = "CNG_FINAL";
	public static final String CNG_INITIAL_COLKEY = "CNG_INITIAL";
	public static final String C_STRUCTURE_COLKEY = "C_STRUCTURE";
	public static final String D_DEB_AFFECTATION_COLKEY = "D_DEB_AFFECTATION";
	public static final String D_FIN_AFFECTATION_COLKEY = "D_FIN_AFFECTATION";
	public static final String DTE_DEBUT_ANNEE_COLKEY = "DTE_DEBUT_ANNEE";
	public static final String DTE_FIN_ANNEE_COLKEY = "DTE_DEBUT_ANNEE+365";
	public static final String LC_STRUCTURE_COLKEY = "LC_STRUCTURE";
	public static final String NO_INDIVIDU_COLKEY = "NO_INDIVIDU";
	public static final String NOM_USUEL_COLKEY = "NOM_USUEL";
	public static final String NO_SEQ_AFFECTATION_COLKEY = "NO_SEQ_AFFECTATION";
	public static final String OID_AFF_ANN_COLKEY = "OID_AFF_ANN";
	public static final String OID_PER_AFF_ANN_COLKEY = "OID_PER_AFF_ANN";
	public static final String PRENOM_COLKEY = "PRENOM";
	public static final String QUOTITE_COLKEY = "QUOTITE";
	public static final String REGULATION_COLKEY = "REGULATION";
	public static final String REL_INIT_COLKEY = "REL_INIT";

	// Relationships
	public static final String TO_AFFECTATION_ANNUELLE_KEY = "toAffectationAnnuelle";
	public static final String TOS_ELEMENT_CARRIERE_KEY = "tosElementCarriere";
	public static final String TO_STRUCTURE_KEY = "toStructure";

	// Utilities methods
  public EOVIndividuConges localInstanceIn(EOEditingContext editingContext) {
    EOVIndividuConges localInstance = (EOVIndividuConges)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public Integer cngConsomme() {
    return (Integer) storedValueForKey("cngConsomme");
  }

  public void setCngConsomme(Integer value) {
    takeStoredValueForKey(value, "cngConsomme");
  }

  public Integer cngFinal() {
    return (Integer) storedValueForKey("cngFinal");
  }

  public void setCngFinal(Integer value) {
    takeStoredValueForKey(value, "cngFinal");
  }

  public Integer cngInitial() {
    return (Integer) storedValueForKey("cngInitial");
  }

  public void setCngInitial(Integer value) {
    takeStoredValueForKey(value, "cngInitial");
  }

  public String cStructure() {
    return (String) storedValueForKey("cStructure");
  }

  public void setCStructure(String value) {
    takeStoredValueForKey(value, "cStructure");
  }

  public NSTimestamp dDebAffectation() {
    return (NSTimestamp) storedValueForKey("dDebAffectation");
  }

  public void setDDebAffectation(NSTimestamp value) {
    takeStoredValueForKey(value, "dDebAffectation");
  }

  public NSTimestamp dFinAffectation() {
    return (NSTimestamp) storedValueForKey("dFinAffectation");
  }

  public void setDFinAffectation(NSTimestamp value) {
    takeStoredValueForKey(value, "dFinAffectation");
  }

  public NSTimestamp dteDebutAnnee() {
    return (NSTimestamp) storedValueForKey("dteDebutAnnee");
  }

  public void setDteDebutAnnee(NSTimestamp value) {
    takeStoredValueForKey(value, "dteDebutAnnee");
  }

  public NSTimestamp dteFinAnnee() {
    return (NSTimestamp) storedValueForKey("dteFinAnnee");
  }

  public void setDteFinAnnee(NSTimestamp value) {
    takeStoredValueForKey(value, "dteFinAnnee");
  }

  public String lcStructure() {
    return (String) storedValueForKey("lcStructure");
  }

  public void setLcStructure(String value) {
    takeStoredValueForKey(value, "lcStructure");
  }

  public Integer noIndividu() {
    return (Integer) storedValueForKey("noIndividu");
  }

  public void setNoIndividu(Integer value) {
    takeStoredValueForKey(value, "noIndividu");
  }

  public String nomUsuel() {
    return (String) storedValueForKey("nomUsuel");
  }

  public void setNomUsuel(String value) {
    takeStoredValueForKey(value, "nomUsuel");
  }

  public Integer noSeqAffectation() {
    return (Integer) storedValueForKey("noSeqAffectation");
  }

  public void setNoSeqAffectation(Integer value) {
    takeStoredValueForKey(value, "noSeqAffectation");
  }

  public Integer oidAffAnn() {
    return (Integer) storedValueForKey("oidAffAnn");
  }

  public void setOidAffAnn(Integer value) {
    takeStoredValueForKey(value, "oidAffAnn");
  }

  public Integer oidPerAffAnn() {
    return (Integer) storedValueForKey("oidPerAffAnn");
  }

  public void setOidPerAffAnn(Integer value) {
    takeStoredValueForKey(value, "oidPerAffAnn");
  }

  public String prenom() {
    return (String) storedValueForKey("prenom");
  }

  public void setPrenom(String value) {
    takeStoredValueForKey(value, "prenom");
  }

  public Integer quotite() {
    return (Integer) storedValueForKey("quotite");
  }

  public void setQuotite(Integer value) {
    takeStoredValueForKey(value, "quotite");
  }

  public Integer regulation() {
    return (Integer) storedValueForKey("regulation");
  }

  public void setRegulation(Integer value) {
    takeStoredValueForKey(value, "regulation");
  }

  public Integer relInit() {
    return (Integer) storedValueForKey("relInit");
  }

  public void setRelInit(Integer value) {
    takeStoredValueForKey(value, "relInit");
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
  
  public NSArray tosElementCarriere() {
    return (NSArray)storedValueForKey("tosElementCarriere");
  }

  public NSArray tosElementCarriere(EOQualifier qualifier) {
    return tosElementCarriere(qualifier, null);
  }

  public NSArray tosElementCarriere(EOQualifier qualifier, NSArray sortOrderings) {
    NSArray results;
      results = tosElementCarriere();
      if (qualifier != null) {
        results = (NSArray)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
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


  public static EOVIndividuConges createVIndividuConges(EOEditingContext editingContext, Integer oidPerAffAnn
) {
    EOVIndividuConges eo = (EOVIndividuConges) EOUtilities.createAndInsertInstance(editingContext, _EOVIndividuConges.ENTITY_NAME);    
		eo.setOidPerAffAnn(oidPerAffAnn);
    return eo;
  }

  public static NSArray fetchAllVIndividuCongeses(EOEditingContext editingContext) {
    return _EOVIndividuConges.fetchAllVIndividuCongeses(editingContext, null);
  }

  public static NSArray fetchAllVIndividuCongeses(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOVIndividuConges.fetchVIndividuCongeses(editingContext, null, sortOrderings);
  }

  public static NSArray fetchVIndividuCongeses(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOVIndividuConges.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOVIndividuConges fetchVIndividuConges(EOEditingContext editingContext, String keyName, Object value) {
    return _EOVIndividuConges.fetchVIndividuConges(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOVIndividuConges fetchVIndividuConges(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOVIndividuConges.fetchVIndividuCongeses(editingContext, qualifier, null);
    EOVIndividuConges eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOVIndividuConges)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one VIndividuConges that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOVIndividuConges fetchRequiredVIndividuConges(EOEditingContext editingContext, String keyName, Object value) {
    return _EOVIndividuConges.fetchRequiredVIndividuConges(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOVIndividuConges fetchRequiredVIndividuConges(EOEditingContext editingContext, EOQualifier qualifier) {
    EOVIndividuConges eoObject = _EOVIndividuConges.fetchVIndividuConges(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no VIndividuConges that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOVIndividuConges localInstanceIn(EOEditingContext editingContext, EOVIndividuConges eo) {
    EOVIndividuConges localInstance = (eo == null) ? null : (EOVIndividuConges)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
