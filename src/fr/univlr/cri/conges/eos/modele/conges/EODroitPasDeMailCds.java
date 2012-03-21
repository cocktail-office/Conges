// EODroitPasDeMailCds.java
// Created on Tue Sep 28 13:49:30  2004 by Apple EOModeler Version 5.2

package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;


public class EODroitPasDeMailCds extends _EODroitPasDeMailCds {

    public EODroitPasDeMailCds() {
    	super();
    }

		/**
		 * @param ec
		 * @param service
		 * Retourne une liste d'objets de l'entitie <code>DroitPasDeMailCds</code>
		 */
		public static EODroitPasDeMailCds findRecordForStructure(EOEditingContext ec, EOStructure service) {
			EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(
					C_STRUCTURE_KEY +"=%@", 
					new NSArray(service.cStructure()));
			NSArray records = ec.objectsWithFetchSpecification(new EOFetchSpecification(EODroitPasDeMailCds.ENTITY_NAME, qual, null));
			if (records.count() > 0)
				return (EODroitPasDeMailCds) records.objectAtIndex(0);
			else
				return null;
		}

		public static EODroitPasDeMailCds newDroitPasDeMail(EOEditingContext ec, EOStructure structure) {
			EODroitPasDeMailCds droit = new EODroitPasDeMailCds();
			droit.setCStructure((String)EOUtilities.primaryKeyForObject(ec, structure).objectForKey("oid"));
			ec.insertObject(droit);
			return droit;
		}

}
