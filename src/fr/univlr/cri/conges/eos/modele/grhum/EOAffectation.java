// EOAffectation.java
// Created on Sat Jun 19 20:16:44  2004 by Apple EOModeler Version 5.2

package fr.univlr.cri.conges.eos.modele.grhum;

import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.util.ArrayCtrl;
import fr.univlr.cri.util.wo5.DateCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.ycrifwk.utils.UtilDb;

public class EOAffectation extends _EOAffectation {

	public EOAffectation() {
		super();
	}
	
   // methodes rajoutees
    
    /**
     * Affichage de ce type de donnees dans les interface
     */
    public String toString() {
      String str = 
      	individu().nomComplet() + " - " + structure().display() + " - " +
        (dDebAffectation() != null ? DateCtrl.dateToString(dDebAffectation()) : " pas de debut") + " - " +
        (dFinAffectation() != null ? DateCtrl.dateToString(dFinAffectation()) : " pas de fin") +
        " @ " + numQuotation() +"%";
      return str;
    }
    
    
    
    private Number oid;
		/** le qualifier qui permet de connaitre les affectations disponible sur une periode */
    private static String STR_QUAL_AFFECTATION_EXISTING_ON_PERIOD = 			
			"(" +
			"(" + D_FIN_AFFECTATION_KEY + " = nil)" +
			" or " +
			"(" + D_DEB_AFFECTATION_KEY + " < %@ and " + D_FIN_AFFECTATION_KEY + " > %@)" +
			" or " +
			"(" + D_FIN_AFFECTATION_KEY + " >= %@ and " + D_FIN_AFFECTATION_KEY + " <= %@)" +
			" or " +
			"(" + D_DEB_AFFECTATION_KEY + " >= %@ and " + D_DEB_AFFECTATION_KEY + " <= %@)" +
			")" +
			" and toVPersonnelNonEns.toIndividu.persId <> nil";
    
    /**
     * En lecture seule, pour les logs
     * @return -1 si erreur
     */
    public Number oid() {
      if (oid == null) {
        oid = new Integer(-1);
        Number pk = (Number) EOUtilities.primaryKeyForObject(editingContext(), this).valueForKey("oid");
        if (pk != null)
          oid = pk;
      }
      return oid;
    }

		/**
		 * Permet de recuperer selon le binding:
		 * Toutes les affectations d'un service sur une periode
		 * Toutes les affectations d'un individu sur une periode
		 * @param bindings
		 * @param ec
		 * @return
		 */
		public static NSArray findAffectationsGepetoInContext(EOEditingContext ec, 
				EOIndividu individu, EOStructure structure, NSTimestamp dDebut, NSTimestamp dFin) {
			String strQual = STR_QUAL_AFFECTATION_EXISTING_ON_PERIOD;
		
		  NSArray args = new NSArray(new NSTimestamp[] {dDebut, dFin, dDebut, dFin, dDebut, dFin});
		  
		  if (individu != null) {
		    strQual += " and " + INDIVIDU_KEY + " = %@";
		    args = args.arrayByAddingObject(individu);
		  }
		  
		  if (structure != null) {
		    strQual += " and " + STRUCTURE_KEY + " = %@";
		    args = args.arrayByAddingObject(structure);
		  }
		  
		  NSArray records = fetchAffectations(ec, CRIDataBus.newCondition(strQual, args), null);
		  return ArrayCtrl.removeDoublons(records);
		}

		/**
		 * @param oidAffectation
		 * @param context
		 * @return
		 */
		public static EOAffectation findAffectationGepetoForOidInContext(Number oidAffectation, EOEditingContext ec) {
		    EOQualifier qual = EOQualifier.qualifierWithQualifierFormat("oid = %@", new NSArray(oidAffectation));
		    NSArray results = UtilDb.fetchArray(ec, EOAffectation.ENTITY_NAME, qual, null);
			EOAffectation affectation = null;
			if (results.count() > 0) {
			    affectation = (EOAffectation) results.lastObject();
			}
			return affectation;
		}
}
