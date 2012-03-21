// _EOMessage.java
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

// DO NOT EDIT.  Make changes to EOMessage.java instead.
package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.foundation.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import fr.univlr.cri.conges.objects.A_YCRIDateGenericRecord;

//@SuppressWarnings("all")
public abstract class _EOMessage extends  A_YCRIDateGenericRecord {
	public static final String ENTITY_NAME = "Message";
	public static final String ENTITY_TABLE_NAME = "CONGES.CNG_MESSAGE";

	// Attributes
	public static final String D_CREATION_KEY = "dCreation";
	public static final String D_MODIFICATION_KEY = "dModification";
	public static final String MES_CONTENU_KEY = "mesContenu";
	public static final String MES_DATE_DEBUT_KEY = "mesDateDebut";
	public static final String MES_DATE_FIN_KEY = "mesDateFin";
	public static final String MES_TITRE_KEY = "mesTitre";

	public static final String D_CREATION_COLKEY = "D_CREATION";
	public static final String D_MODIFICATION_COLKEY = "D_MODIFICATION";
	public static final String MES_CONTENU_COLKEY = "MES_CONTENU";
	public static final String MES_DATE_DEBUT_COLKEY = "MES_DATE_DEBUT";
	public static final String MES_DATE_FIN_COLKEY = "MES_DATE_FIN";
	public static final String MES_TITRE_COLKEY = "MES_TITRE";

	// Relationships

	// Utilities methods
  public EOMessage localInstanceIn(EOEditingContext editingContext) {
    EOMessage localInstance = (EOMessage)EOUtilities.localInstanceOfObject(editingContext, this);
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

  public String mesContenu() {
    return (String) storedValueForKey("mesContenu");
  }

  public void setMesContenu(String value) {
    takeStoredValueForKey(value, "mesContenu");
  }

  public NSTimestamp mesDateDebut() {
    return (NSTimestamp) storedValueForKey("mesDateDebut");
  }

  public void setMesDateDebut(NSTimestamp value) {
    takeStoredValueForKey(value, "mesDateDebut");
  }

  public NSTimestamp mesDateFin() {
    return (NSTimestamp) storedValueForKey("mesDateFin");
  }

  public void setMesDateFin(NSTimestamp value) {
    takeStoredValueForKey(value, "mesDateFin");
  }

  public String mesTitre() {
    return (String) storedValueForKey("mesTitre");
  }

  public void setMesTitre(String value) {
    takeStoredValueForKey(value, "mesTitre");
  }


  public static EOMessage createMessage(EOEditingContext editingContext, NSTimestamp dCreation
, NSTimestamp dModification
, String mesContenu
, NSTimestamp mesDateDebut
, NSTimestamp mesDateFin
, String mesTitre
) {
    EOMessage eo = (EOMessage) EOUtilities.createAndInsertInstance(editingContext, _EOMessage.ENTITY_NAME);    
		eo.setDCreation(dCreation);
		eo.setDModification(dModification);
		eo.setMesContenu(mesContenu);
		eo.setMesDateDebut(mesDateDebut);
		eo.setMesDateFin(mesDateFin);
		eo.setMesTitre(mesTitre);
    return eo;
  }

  public static NSArray fetchAllMessages(EOEditingContext editingContext) {
    return _EOMessage.fetchAllMessages(editingContext, null);
  }

  public static NSArray fetchAllMessages(EOEditingContext editingContext, NSArray sortOrderings) {
    return _EOMessage.fetchMessages(editingContext, null, sortOrderings);
  }

  public static NSArray fetchMessages(EOEditingContext editingContext, EOQualifier qualifier, NSArray sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EOMessage.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray eoObjects = (NSArray)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EOMessage fetchMessage(EOEditingContext editingContext, String keyName, Object value) {
    return _EOMessage.fetchMessage(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOMessage fetchMessage(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray eoObjects = _EOMessage.fetchMessages(editingContext, qualifier, null);
    EOMessage eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EOMessage)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one Message that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOMessage fetchRequiredMessage(EOEditingContext editingContext, String keyName, Object value) {
    return _EOMessage.fetchRequiredMessage(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EOMessage fetchRequiredMessage(EOEditingContext editingContext, EOQualifier qualifier) {
    EOMessage eoObject = _EOMessage.fetchMessage(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no Message that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EOMessage localInstanceIn(EOEditingContext editingContext, EOMessage eo) {
    EOMessage localInstance = (eo == null) ? null : (EOMessage)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
