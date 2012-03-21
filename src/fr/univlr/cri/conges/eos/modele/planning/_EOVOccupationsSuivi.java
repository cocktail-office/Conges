// _EOVOccupationsSuivi.java
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

// DO NOT EDIT.  Make changes to EOVOccupationsSuivi.java instead.
package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOVOccupationsSuivi extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "VOccupationsSuivi";
	public static final String ENTITY_TABLE_NAME = "CONGES.V_OCCUPATIONS_SUIVI";

	// Attributes
	public static final String COMPOSANTE_KEY = "composante";
	public static final String C_STRUCTURE_KEY = "cStructure";
	public static final String C_STRUCTURE_COMPOSANTE_KEY = "cStructureComposante";
	public static final String D_CREATION_KEY = "dCreation";
	public static final String DTE_DEBUT_KEY = "dteDebut";
	public static final String DTE_DEBUT_ANNEE_KEY = "dteDebutAnnee";
	public static final String DTE_FIN_KEY = "dteFin";
	public static final String MOTIF_KEY = "motif";
	public static final String NO_INDIVIDU_KEY = "noIndividu";
	public static final String NOM_USUEL_KEY = "nomUsuel";
	public static final String OID_AFF_ANN_KEY = "oidAffAnn";
	public static final String OID_OCC_KEY = "oidOcc";
	public static final String OID_TYP_OCC_KEY = "oidTypOcc";
	public static final String PRENOM_KEY = "prenom";
	public static final String SERVICE_KEY = "service";
	public static final String STATUS_KEY = "status";
	public static final String TEMPS_DECOMPTE_KEY = "tempsDecompte";
	public static final String TEMPS_SUR_HORAIRE_KEY = "tempsSurHoraire";
	public static final String TYPE_OCCUPATION_KEY = "typeOccupation";

	public static final String COMPOSANTE_COLKEY = "COMPOSANTE";
	public static final String C_STRUCTURE_COLKEY = "C_STRUCTURE";
	public static final String C_STRUCTURE_COMPOSANTE_COLKEY = "C_STRUCTURE_COMPOSANTE";
	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String DTE_DEBUT_COLKEY = "DTE_DEBUT";
	public static final String DTE_DEBUT_ANNEE_COLKEY = "DTE_DEBUT_ANNEE";
	public static final String DTE_FIN_COLKEY = "DTE_FIN";
	public static final String MOTIF_COLKEY = "MOTIF";
	public static final String NO_INDIVIDU_COLKEY = "NO_INDIVIDU";
	public static final String NOM_USUEL_COLKEY = "NOM_USUEL";
	public static final String OID_AFF_ANN_COLKEY = "OID_AFF_ANN";
	public static final String OID_OCC_COLKEY = "OID_OCC";
	public static final String OID_TYP_OCC_COLKEY = "OID_TYP_OCC";
	public static final String PRENOM_COLKEY = "PRENOM";
	public static final String SERVICE_COLKEY = "SERVICE";
	public static final String STATUS_COLKEY = "STATUS";
	public static final String TEMPS_DECOMPTE_COLKEY = "TEMPS_DECOMPTE";
	public static final String TEMPS_SUR_HORAIRE_COLKEY = "TEMPS_SUR_HORAIRE";
	public static final String TYPE_OCCUPATION_COLKEY = "TYPE_OCCUPATION";

	// Relationships
	public static final String TO_COMPOSANTE_KEY = "toComposante";
	public static final String TO_STRUCTURE_KEY = "toStructure";
	public static final String TO_TYPE_OCCUPATION_KEY = "toTypeOccupation";

	// Utilities methods
  public EOVOccupationsSuivi localInstanceIn(EOEditingContext editingContext) {
    EOVOccupationsSuivi localInstance = (EOVOccupationsSuivi)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public String composante() {
    return (String) storedValueForKey("composante");
  }

  public void setComposante(String value) {
    takeStoredValueForKey(value, "composante");
  }

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

  public NSTimestamp dCreation() {
    return (NSTimestamp) storedValueForKey("dCreation");
  }

  public void setDCreation(NSTimestamp value) {
    takeStoredValueForKey(value, "dCreation");
  }

  public NSTimestamp dteDebut() {
    return (NSTimestamp) storedValueForKey("dteDebut");
  }

  public void setDteDebut(NSTimestamp value) {
    takeStoredValueForKey(value, "dteDebut");
  }

  public NSTimestamp dteDebutAnnee() {
    return (NSTimestamp) storedValueForKey("dteDebutAnnee");
  }

  public void setDteDebutAnnee(NSTimestamp value) {
    takeStoredValueForKey(value, "dteDebutAnnee");
  }

  public NSTimestamp dteFin() {
    return (NSTimestamp) storedValueForKey("dteFin");
  }

  public void setDteFin(NSTimestamp value) {
    takeStoredValueForKey(value, "dteFin");
  }

  public String motif() {
    return (String) storedValueForKey("motif");
  }

  public void setMotif(String value) {
    takeStoredValueForKey(value, "motif");
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

  public Integer oidAffAnn() {
    return (Integer) storedValueForKey("oidAffAnn");
  }

  public void setOidAffAnn(Integer value) {
    takeStoredValueForKey(value, "oidAffAnn");
  }

  public Integer oidOcc() {
    return (Integer) storedValueForKey("oidOcc");
  }

  public void setOidOcc(Integer value) {
    takeStoredValueForKey(value, "oidOcc");
  }

  public Integer oidTypOcc() {
    return (Integer) storedValueForKey("oidTypOcc");
  }

  public void setOidTypOcc(Integer value) {
    takeStoredValueForKey(value, "oidTypOcc");
  }

  public String prenom() {
    return (String) storedValueForKey("prenom");
  }

  public void setPrenom(String value) {
    takeStoredValueForKey(value, "prenom");
  }

  public String service() {
    return (String) storedValueForKey("service");
  }

  public void setService(String value) {
    takeStoredValueForKey(value, "service");
  }

  public String status() {
    return (String) storedValueForKey("status");
  }

  public void setStatus(String value) {
    takeStoredValueForKey(value, "status");
  }

  public Integer tempsDecompte() {
    return (Integer) storedValueForKey("tempsDecompte");
  }

  public void setTempsDecompte(Integer value) {
    takeStoredValueForKey(value, "tempsDecompte");
  }

  public Integer tempsSurHoraire() {
    return (Integer) storedValueForKey("tempsSurHoraire");
  }

  public void setTempsSurHoraire(Integer value) {
    takeStoredValueForKey(value, "tempsSurHoraire");
  }

  public String typeOccupation() {
    return (String) storedValueForKey("typeOccupation");
  }

  public void setTypeOccupation(String value) {
    takeStoredValueForKey(value, "typeOccupation");
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
  
  public fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation toTypeOccupation() {
    return (fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation)storedValueForKey("toTypeOccupation");
  }

  public void setToTypeOccupationRelationship(fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation value) {
    if (value == null) {
    	fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation oldValue = toTypeOccupation();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "toTypeOccupation");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "toTypeOccupation");
    }
  }
  

  public static EOVOccupationsSuivi createVOccupationsSuivi(EOEditingContext editingContext, String composante
, String cStructure
, String cStructureComposante
, NSTimestamp dCreation
, NSTimestamp dteDebut
, NSTimestamp dteDebutAnnee
, NSTimestamp dteFin
, String motif
, Integer noIndividu
, String nomUsuel
, Integer oidAffAnn
, Integer oidOcc
, Integer oidTypOcc
, String prenom
, String service
, String status
, Integer tempsDecompte
, Integer tempsSurHoraire
, String typeOccupation
, fr.univlr.cri.conges.eos.modele.grhum.EOStructure toComposante, fr.univlr.cri.conges.eos.modele.grhum.EOStructure toStructure, fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation toTypeOccupation) {
    EOVOccupationsSuivi eo = (EOVOccupationsSuivi) EOUtilities.createAndInsertInstance(editingContext, _EOVOccupationsSuivi.ENTITY_NAME);    
		eo.setComposante(composante);
		eo.setCStructure(cStructure);
		eo.setCStructureComposante(cStructureComposante);
		eo.setDCreation(dCreation);
		eo.setDteDebut(dteDebut);
		eo.setDteDebutAnnee(dteDebutAnnee);
		eo.setDteFin(dteFin);
		eo.setMotif(motif);
		eo.setNoIndividu(noIndividu);
		eo.setNomUsuel(nomUsuel);
		eo.setOidAffAnn(oidAffAnn);
		eo.setOidOcc(oidOcc);
		eo.setOidTypOcc(oidTypOcc);
		eo.setPrenom(prenom);
		eo.setService(service);
		eo.setStatus(status);
		eo.setTempsDecompte(tempsDecompte);
		eo.setTempsSurHoraire(tempsSurHoraire);
		eo.setTypeOccupation(typeOccupation);
    eo.setToComposanteRelationship(toComposante);
    eo.setToStructureRelationship(toStructure);
    eo.setToTypeOccupationRelationship(toTypeOccupation);
    return eo;
  }

  public static NSArray fetchAllVOccupationsSuivis(EOEditingContext editingContext) {
    return _EOVOccupationsSuivi.fetchAllVOccupationsSuivis(editingContext, null);
  }

  public static NSArray fetchAllVOccupationsSuivis(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOVOccupationsSuivi.fetchVOccupationsSuivis(editingContext, null, sortOrderings);
  }

  public static NSArray fetchVOccupationsSuivis(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOVOccupationsSuivi.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOVOccupationsSuivi fetchVOccupationsSuivi(EOEditingContext editingContext, String keyName, Object value) {
    return _EOVOccupationsSuivi.fetchVOccupationsSuivi(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOVOccupationsSuivi fetchVOccupationsSuivi(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOVOccupationsSuivi.fetchVOccupationsSuivis(editingContext, qualifier, null);
    EOVOccupationsSuivi eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOVOccupationsSuivi)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one VOccupationsSuivi that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOVOccupationsSuivi fetchRequiredVOccupationsSuivi(EOEditingContext editingContext, String keyName, Object value) {
    return _EOVOccupationsSuivi.fetchRequiredVOccupationsSuivi(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOVOccupationsSuivi fetchRequiredVOccupationsSuivi(EOEditingContext editingContext, EOQualifier qualifier) {
    EOVOccupationsSuivi eoObject = _EOVOccupationsSuivi.fetchVOccupationsSuivi(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no VOccupationsSuivi that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOVOccupationsSuivi localInstanceIn(EOEditingContext editingContext, EOVOccupationsSuivi eo) {
    EOVOccupationsSuivi localInstance = (eo == null) ? null : (EOVOccupationsSuivi)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
