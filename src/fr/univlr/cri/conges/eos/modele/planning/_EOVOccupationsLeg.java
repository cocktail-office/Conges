// _EOVOccupationsLeg.java
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

// DO NOT EDIT.  Make changes to EOVOccupationsLeg.java instead.
package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOVOccupationsLeg extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "VOccupationsLeg";
	public static final String ENTITY_TABLE_NAME = "CONGES.V_OCCUPATIONS_LEG";

	// Attributes
	public static final String DTE_DEBUT_REEL_KEY = "dteDebutReel";
	public static final String DTE_FIN_REEL_KEY = "dteFinReel";
	public static final String MOTIF_KEY = "motif";
	public static final String NATURE_KEY = "nature";
	public static final String NO_INDIVIDU_KEY = "noIndividu";
	public static final String OID_KEY = "oid";
	public static final String OID_AFF_ANN_KEY = "oidAffAnn";
	public static final String STATUS_KEY = "status";
	public static final String TOCC_LIBELLE_COURT_KEY = "toccLibelleCourt";
	public static final String TYPE_OCC_KEY = "typeOcc";

	public static final String DTE_DEBUT_REEL_COLKEY = "DTE_DEBUT_REEL";
	public static final String DTE_FIN_REEL_COLKEY = "DTE_FIN_REEL";
	public static final String MOTIF_COLKEY = "MOTIF";
	public static final String NATURE_COLKEY = "FLG_NATURE";
	public static final String NO_INDIVIDU_COLKEY = "NO_INDIVIDU";
	public static final String OID_COLKEY = "OID";
	public static final String OID_AFF_ANN_COLKEY = "OID_AFF_ANN";
	public static final String STATUS_COLKEY = "STATUS";
	public static final String TOCC_LIBELLE_COURT_COLKEY = "TOCC_LIBELLE_COURT";
	public static final String TYPE_OCC_COLKEY = "TYPE_OCC";

	// Relationships

	// Utilities methods
  public EOVOccupationsLeg localInstanceIn(EOEditingContext editingContext) {
    EOVOccupationsLeg localInstance = (EOVOccupationsLeg)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public NSTimestamp dteDebutReel() {
    return (NSTimestamp) storedValueForKey("dteDebutReel");
  }

  public void setDteDebutReel(NSTimestamp value) {
    takeStoredValueForKey(value, "dteDebutReel");
  }

  public NSTimestamp dteFinReel() {
    return (NSTimestamp) storedValueForKey("dteFinReel");
  }

  public void setDteFinReel(NSTimestamp value) {
    takeStoredValueForKey(value, "dteFinReel");
  }

  public String motif() {
    return (String) storedValueForKey("motif");
  }

  public void setMotif(String value) {
    takeStoredValueForKey(value, "motif");
  }

  public String nature() {
    return (String) storedValueForKey("nature");
  }

  public void setNature(String value) {
    takeStoredValueForKey(value, "nature");
  }

  public Integer noIndividu() {
    return (Integer) storedValueForKey("noIndividu");
  }

  public void setNoIndividu(Integer value) {
    takeStoredValueForKey(value, "noIndividu");
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

  public String status() {
    return (String) storedValueForKey("status");
  }

  public void setStatus(String value) {
    takeStoredValueForKey(value, "status");
  }

  public String toccLibelleCourt() {
    return (String) storedValueForKey("toccLibelleCourt");
  }

  public void setToccLibelleCourt(String value) {
    takeStoredValueForKey(value, "toccLibelleCourt");
  }

  public Integer typeOcc() {
    return (Integer) storedValueForKey("typeOcc");
  }

  public void setTypeOcc(Integer value) {
    takeStoredValueForKey(value, "typeOcc");
  }


  public static EOVOccupationsLeg createVOccupationsLeg(EOEditingContext editingContext, Integer oid
) {
    EOVOccupationsLeg eo = (EOVOccupationsLeg) EOUtilities.createAndInsertInstance(editingContext, _EOVOccupationsLeg.ENTITY_NAME);    
		eo.setOid(oid);
    return eo;
  }

  public static NSArray fetchAllVOccupationsLegs(EOEditingContext editingContext) {
    return _EOVOccupationsLeg.fetchAllVOccupationsLegs(editingContext, null);
  }

  public static NSArray fetchAllVOccupationsLegs(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOVOccupationsLeg.fetchVOccupationsLegs(editingContext, null, sortOrderings);
  }

  public static NSArray fetchVOccupationsLegs(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOVOccupationsLeg.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOVOccupationsLeg fetchVOccupationsLeg(EOEditingContext editingContext, String keyName, Object value) {
    return _EOVOccupationsLeg.fetchVOccupationsLeg(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOVOccupationsLeg fetchVOccupationsLeg(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOVOccupationsLeg.fetchVOccupationsLegs(editingContext, qualifier, null);
    EOVOccupationsLeg eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOVOccupationsLeg)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one VOccupationsLeg that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOVOccupationsLeg fetchRequiredVOccupationsLeg(EOEditingContext editingContext, String keyName, Object value) {
    return _EOVOccupationsLeg.fetchRequiredVOccupationsLeg(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOVOccupationsLeg fetchRequiredVOccupationsLeg(EOEditingContext editingContext, EOQualifier qualifier) {
    EOVOccupationsLeg eoObject = _EOVOccupationsLeg.fetchVOccupationsLeg(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no VOccupationsLeg that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOVOccupationsLeg localInstanceIn(EOEditingContext editingContext, EOVOccupationsLeg eo) {
    EOVOccupationsLeg localInstance = (eo == null) ? null : (EOVOccupationsLeg)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
