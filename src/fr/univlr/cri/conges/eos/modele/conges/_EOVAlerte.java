// _EOVAlerte.java
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

// DO NOT EDIT.  Make changes to EOVAlerte.java instead.
package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOVAlerte extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "VAlerte";
	public static final String ENTITY_TABLE_NAME = "CONGES.V_ALERTE";

	// Attributes
	public static final String ANNEE_KEY = "annee";
	public static final String C_STRUCTURE_KEY = "cStructure";
	public static final String C_STRUCTURE_COMPOSANTE_KEY = "cStructureComposante";
	public static final String D_CREATION_KEY = "dCreation";
	public static final String FLAG_DRH_KEY = "flagDRH";
	public static final String NO_INDIVIDU_KEY = "noIndividu";
	public static final String OCC_DTE_DEBUT_KEY = "occDteDebut";
	public static final String OCC_DTE_FIN_KEY = "occDteFin";
	public static final String OCC_STATUT_KEY = "occStatut";
	public static final String OID_KEY = "oid";
	public static final String OID_AFF_ANN_KEY = "oidAffAnn";
	public static final String OID_OCC_KEY = "oidOcc";
	public static final String PLG_STATUT_KEY = "plgStatut";
	public static final String T_OCCLIBELLE_COURT_KEY = "tOcclibelleCourt";

	public static final String ANNEE_COLKEY = "ANNEE";
	public static final String C_STRUCTURE_COLKEY = "C_STRUCTURE";
	public static final String C_STRUCTURE_COMPOSANTE_COLKEY = "C_STRUCTURE_COMPOSANTE";
	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String FLAG_DRH_COLKEY = "FLG_DRH";
	public static final String NO_INDIVIDU_COLKEY = "NO_INDIVIDU";
	public static final String OCC_DTE_DEBUT_COLKEY = "OCC_DTE_DEBUT";
	public static final String OCC_DTE_FIN_COLKEY = "OCC_DTE_FIN";
	public static final String OCC_STATUT_COLKEY = "OCC_STATUS";
	public static final String OID_COLKEY = "OID";
	public static final String OID_AFF_ANN_COLKEY = "OID_AFF_ANN";
	public static final String OID_OCC_COLKEY = "OID_OCC";
	public static final String PLG_STATUT_COLKEY = "PLG_STATUS";
	public static final String T_OCCLIBELLE_COURT_COLKEY = "T_LIBELLE_COURT";

	// Relationships

	// Utilities methods
  public EOVAlerte localInstanceIn(EOEditingContext editingContext) {
    EOVAlerte localInstance = (EOVAlerte)EOUtilities.localInstanceOfObject(editingContext, this);
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

  public Integer flagDRH() {
    return (Integer) storedValueForKey("flagDRH");
  }

  public void setFlagDRH(Integer value) {
    takeStoredValueForKey(value, "flagDRH");
  }

  public Integer noIndividu() {
    return (Integer) storedValueForKey("noIndividu");
  }

  public void setNoIndividu(Integer value) {
    takeStoredValueForKey(value, "noIndividu");
  }

  public NSTimestamp occDteDebut() {
    return (NSTimestamp) storedValueForKey("occDteDebut");
  }

  public void setOccDteDebut(NSTimestamp value) {
    takeStoredValueForKey(value, "occDteDebut");
  }

  public NSTimestamp occDteFin() {
    return (NSTimestamp) storedValueForKey("occDteFin");
  }

  public void setOccDteFin(NSTimestamp value) {
    takeStoredValueForKey(value, "occDteFin");
  }

  public Integer occStatut() {
    return (Integer) storedValueForKey("occStatut");
  }

  public void setOccStatut(Integer value) {
    takeStoredValueForKey(value, "occStatut");
  }

  public Integer oid() {
    return (Integer) storedValueForKey("oid");
  }

  public void setOid(Integer value) {
    takeStoredValueForKey(value, "oid");
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

  public String plgStatut() {
    return (String) storedValueForKey("plgStatut");
  }

  public void setPlgStatut(String value) {
    takeStoredValueForKey(value, "plgStatut");
  }

  public String tOcclibelleCourt() {
    return (String) storedValueForKey("tOcclibelleCourt");
  }

  public void setTOcclibelleCourt(String value) {
    takeStoredValueForKey(value, "tOcclibelleCourt");
  }


  public static EOVAlerte createVAlerte(EOEditingContext editingContext, String annee
, Integer oid
) {
    EOVAlerte eo = (EOVAlerte) EOUtilities.createAndInsertInstance(editingContext, _EOVAlerte.ENTITY_NAME);    
		eo.setAnnee(annee);
		eo.setOid(oid);
    return eo;
  }

  public static NSArray fetchAllVAlertes(EOEditingContext editingContext) {
    return _EOVAlerte.fetchAllVAlertes(editingContext, null);
  }

  public static NSArray fetchAllVAlertes(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOVAlerte.fetchVAlertes(editingContext, null, sortOrderings);
  }

  public static NSArray fetchVAlertes(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOVAlerte.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOVAlerte fetchVAlerte(EOEditingContext editingContext, String keyName, Object value) {
    return _EOVAlerte.fetchVAlerte(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOVAlerte fetchVAlerte(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOVAlerte.fetchVAlertes(editingContext, qualifier, null);
    EOVAlerte eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOVAlerte)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one VAlerte that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOVAlerte fetchRequiredVAlerte(EOEditingContext editingContext, String keyName, Object value) {
    return _EOVAlerte.fetchRequiredVAlerte(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOVAlerte fetchRequiredVAlerte(EOEditingContext editingContext, EOQualifier qualifier) {
    EOVAlerte eoObject = _EOVAlerte.fetchVAlerte(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no VAlerte that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOVAlerte localInstanceIn(EOEditingContext editingContext, EOVAlerte eo) {
    EOVAlerte localInstance = (eo == null) ? null : (EOVAlerte)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
