// _EOEnfant.java
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

// DO NOT EDIT.  Make changes to EOEnfant.java instead.
package fr.univlr.cri.conges.eos.modele.grhum;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOEnfant extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "Enfant";
	public static final String ENTITY_TABLE_NAME = "GRHUM.ENFANT";

	// Attributes
	public static final String D_DECES_KEY = "dDeces";
	public static final String D_NAISSANCE_KEY = "dNaissance";
	public static final String NOM_KEY = "nom";
	public static final String PRENOM_KEY = "prenom";
	public static final String SEXE_KEY = "sexe";
	public static final String TEM_VALIDE_KEY = "temValide";

	public static final String D_DECES_COLKEY = "D_DECES";
	public static final String D_NAISSANCE_COLKEY = "D_NAISSANCE";
	public static final String NOM_COLKEY = "NOM";
	public static final String PRENOM_COLKEY = "PRENOM";
	public static final String SEXE_COLKEY = "SEXE";
	public static final String TEM_VALIDE_COLKEY = "TEM_VALIDE";

	// Relationships

	// Utilities methods
  public EOEnfant localInstanceIn(EOEditingContext editingContext) {
    EOEnfant localInstance = (EOEnfant)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


	// Accessors methods
  public String dDeces() {
    return (String) storedValueForKey("dDeces");
  }

  public void setDDeces(String value) {
    takeStoredValueForKey(value, "dDeces");
  }

  public NSTimestamp dNaissance() {
    return (NSTimestamp) storedValueForKey("dNaissance");
  }

  public void setDNaissance(NSTimestamp value) {
    takeStoredValueForKey(value, "dNaissance");
  }

  public String nom() {
    return (String) storedValueForKey("nom");
  }

  public void setNom(String value) {
    takeStoredValueForKey(value, "nom");
  }

  public String prenom() {
    return (String) storedValueForKey("prenom");
  }

  public void setPrenom(String value) {
    takeStoredValueForKey(value, "prenom");
  }

  public String sexe() {
    return (String) storedValueForKey("sexe");
  }

  public void setSexe(String value) {
    takeStoredValueForKey(value, "sexe");
  }

  public String temValide() {
    return (String) storedValueForKey("temValide");
  }

  public void setTemValide(String value) {
    takeStoredValueForKey(value, "temValide");
  }


  public static EOEnfant createEnfant(EOEditingContext editingContext, NSTimestamp dNaissance
, String nom
, String prenom
, String sexe
) {
    EOEnfant eo = (EOEnfant) EOUtilities.createAndInsertInstance(editingContext, _EOEnfant.ENTITY_NAME);    
		eo.setDNaissance(dNaissance);
		eo.setNom(nom);
		eo.setPrenom(prenom);
		eo.setSexe(sexe);
    return eo;
  }

  public static NSArray fetchAllEnfants(EOEditingContext editingContext) {
    return _EOEnfant.fetchAllEnfants(editingContext, null);
  }

  public static NSArray fetchAllEnfants(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOEnfant.fetchEnfants(editingContext, null, sortOrderings);
  }

  public static NSArray fetchEnfants(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOEnfant.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOEnfant fetchEnfant(EOEditingContext editingContext, String keyName, Object value) {
    return _EOEnfant.fetchEnfant(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOEnfant fetchEnfant(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOEnfant.fetchEnfants(editingContext, qualifier, null);
    EOEnfant eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOEnfant)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one Enfant that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOEnfant fetchRequiredEnfant(EOEditingContext editingContext, String keyName, Object value) {
    return _EOEnfant.fetchRequiredEnfant(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOEnfant fetchRequiredEnfant(EOEditingContext editingContext, EOQualifier qualifier) {
    EOEnfant eoObject = _EOEnfant.fetchEnfant(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no Enfant that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOEnfant localInstanceIn(EOEditingContext editingContext, EOEnfant eo) {
    EOEnfant localInstance = (eo == null) ? null : (EOEnfant)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
