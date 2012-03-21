package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;
import com.webobjects.foundation.NSValidation;

import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.util.ArrayCtrl;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRSort;

public class EOVIndividuConges extends _EOVIndividuConges {

    public EOVIndividuConges() {
        super();
    }

    public void validateForInsert() throws NSValidation.ValidationException {
        this.validateObjectMetier();
        validateBeforeTransactionSave();
        super.validateForInsert();
    }

    public void validateForUpdate() throws NSValidation.ValidationException {
        this.validateObjectMetier();
        validateBeforeTransactionSave();
        super.validateForUpdate();
    }

    public void validateForDelete() throws NSValidation.ValidationException {
        super.validateForDelete();
    }

    /**
     * Apparemment cette methode n'est pas appelee.
     * @see com.webobjects.eocontrol.EOValidation#validateForUpdate()
     */    
    public void validateForSave() throws NSValidation.ValidationException {
        validateObjectMetier();
        validateBeforeTransactionSave();
        super.validateForSave();
        
    }

    /**
     * Peut etre appele a partir des factories.
     * @throws NSValidation.ValidationException
     */
    public void validateObjectMetier() throws NSValidation.ValidationException {
      

    }
    
    /**
     * A appeler par les validateforsave, forinsert, forupdate.
     *
     */
    private final void validateBeforeTransactionSave() throws NSValidation.ValidationException {
           
    }
    
    // ajouts

   
    
		/**
		 * Retourne la chaine qualifier permettant d'effectuer une recherche
		 * a partir de l'entite <code>VIndividuConges</code>
		 * @param searchString : liste de mots a rechercher, s�par�s par des espaces
		 * @param shouldSearchInStructure faut-il chercher dans les libelles de structures
		 * @param shouldSearchInIndividu faut-il chercher dans les libelles de services
		 * @return
		 */
		private static String getStringQualifierNomPrenomService(
				String searchString, boolean shouldSearchInStructure, boolean shouldSearchInIndividu) {
			NSArray arrSearch = NSArray.componentsSeparatedByString(searchString.toUpperCase(), " ");
			
			StringBuffer sbQual = new StringBuffer("");
			
			
			if (arrSearch.count() == 1) {
				String firstWord = (String) arrSearch.objectAtIndex(0);
				// 1 seul mot : on cherche sur le nom / prenom / c_structure / libelleLong
		 		if (shouldSearchInStructure) {
		 			sbQual.append("("+LC_STRUCTURE_KEY+" caseInsensitiveLike '*"+firstWord+"*' or ");
		 			sbQual.append(TO_STRUCTURE_KEY + "." + EOStructure.LIBELLE_LONG_KEY+" caseInsensitiveLike '*"+firstWord+"*')");
				}
				if (shouldSearchInIndividu) {
					if (sbQual.length() > 0) {
						sbQual.append(" or ");
					}
					sbQual.append("("+NOM_USUEL_KEY+" caseInsensitiveLike '*"+firstWord+"*' or ");
					sbQual.append(""+PRENOM_KEY+" caseInsensitiveLike '*"+firstWord+"*')");
				}
			} else if (arrSearch.count() == 2) {
				// 2 mots : on cherche dans le couple nom / prenom, ou sur la structure
				String firstWord = (String) arrSearch.objectAtIndex(0);
				String secondWord = (String) arrSearch.objectAtIndex(1);
		 		if (shouldSearchInStructure) {
		 			sbQual.append("("+LC_STRUCTURE_KEY+" caseInsensitiveLike '*"+firstWord+"*' or ");
		 			sbQual.append(TO_STRUCTURE_KEY + "." + EOStructure.LIBELLE_LONG_KEY+" caseInsensitiveLike '*"+firstWord+"*' or ");
		 			sbQual.append(LC_STRUCTURE_KEY+" caseInsensitiveLike '*"+secondWord+"*' or ");
		 			sbQual.append(TO_STRUCTURE_KEY + "." + EOStructure.LIBELLE_LONG_KEY+" caseInsensitiveLike '*"+secondWord+"*')");
				}
				if (shouldSearchInIndividu) {
					if (sbQual.length() > 0) {
						sbQual.append(" or ");
					}
					sbQual.append("("+NOM_USUEL_KEY+" caseInsensitiveLike '*"+firstWord+ " " + secondWord + "*') or ");
					sbQual.append("("+PRENOM_KEY+" caseInsensitiveLike '*"+firstWord+ " " + secondWord + "*') or ");
					sbQual.append("("+NOM_USUEL_KEY+" caseInsensitiveLike '*"+firstWord+"*' and "+PRENOM_KEY+" caseInsensitiveLike '*"+secondWord+"*') or ");
					sbQual.append("("+PRENOM_KEY+" caseInsensitiveLike '*"+firstWord+"*' and "+NOM_USUEL_KEY+" caseInsensitiveLike '*"+secondWord+"*')");
				}
			}
			
			return sbQual.toString();
		}

		/**
		 * Effectuer une recherche parmi les plannings dans les noms, prenoms et 
		 * libelle de structures
		 * @param ec
		 * @param searchString
		 * @param dteDebutAnnee
		 * @param withinServiceList : liste des service sur laquel il faut restreindre 
		 * 				la recheche. si <em>null</em>, alors on ne fait pas de controle.
		 * @param shouldSearchInStructure faut-il chercher dans les libelles de structures
		 * @param shouldSearchInIndividu faut-il chercher dans les libelles de services
		 * @return
		 */
		public static NSArray findRecordsLike(
				EOEditingContext ec, String searchString, String dteDebutAnnee, NSArray withinServiceList, boolean shouldSearchInStructure, boolean shouldSearchInIndividu) {
			
			if (StringCtrl.isEmpty(searchString)) {
				return new NSArray();
			}
			
			StringBuffer sbQual = new StringBuffer("(");
			
			sbQual.append(EOVIndividuConges.getStringQualifierNomPrenomService(searchString, shouldSearchInStructure, shouldSearchInIndividu));
			
			sbQual.append(") and "+DTE_DEBUT_ANNEE_KEY+"=%@");
			if (!ArrayCtrl.isEmpty(withinServiceList)) {
				sbQual.append(" and (");
		  	for (int i=0; i<withinServiceList.count() ;i++) {
		  		EOStructure service = (EOStructure) withinServiceList.objectAtIndex(i);
		  		sbQual.append(TO_STRUCTURE_KEY + "." + EOStructure.C_STRUCTURE_KEY+"='"+service.cStructure()+"'");
		  		if (i<withinServiceList.count()-1) {
		  			sbQual.append(" or ");
		  		}
		  	}
		  	sbQual.append(")");
			}
			
		  return ec.objectsWithFetchSpecification(
		  		new EOFetchSpecification(
		  				ENTITY_NAME, 
		  				CRIDataBus.newCondition(
		  						sbQual.toString(), 
		  						new NSArray(DateCtrlConges.dateDebutAnneePourStrPeriodeAnnee(dteDebutAnnee))), 
		  				LRSort.newSort(TO_STRUCTURE_KEY + "." + EOStructure.LIBELLE_LONG_KEY+","+LC_STRUCTURE_KEY+","+NOM_USUEL_KEY)));
		}

		/**
		 * Recuperer des enregistrements depuis la vue <code>V_INDIVIDU_CONGES</code>
		 * filtrer d'apres les parametres : 
		 * @param noIndividu
		 * @param cStructure
		 */
		public static NSArray findRecords(EOEditingContext ec, Number noIndividu, String cStructure, NSTimestamp dteDebutAnnee) {
		  String strQual = (noIndividu != null ? NO_INDIVIDU_KEY+"=" + noIndividu.intValue() : null);
		  if (cStructure != null)
		    strQual = (strQual != null ? strQual+" AND "+C_STRUCTURE_KEY+"='"+cStructure+"'" : C_STRUCTURE_KEY+"='"+cStructure+"'");
		  NSArray args = new NSArray();
		  if (dteDebutAnnee != null) {
		    strQual = (strQual != null ? strQual+" AND "+DTE_DEBUT_ANNEE_KEY+"=%@" : DTE_DEBUT_ANNEE_KEY+"=%@");
		    args = args.arrayByAddingObject(dteDebutAnnee);
		  }
		
		  return ec.objectsWithFetchSpecification(new EOFetchSpecification(ENTITY_NAME, 
		      EOQualifier.qualifierWithQualifierFormat(strQual, args), null));
		}
		
		/**
		 * Obtenir l'enregistrement de l'année précedente de la vue <code>V_INDIVIDU_CONGES</code>
		 * @param ec
		 * @param noIndividu
		 * @param cStructure
		 * @param dteDebutAnnee
		 * @return
		 */
		public static EOVIndividuConges getPrevRecord(EOEditingContext ec, Number noIndividu, String cStructure, NSTimestamp dteDebutAnnee) {
			 NSTimestamp prevDteDebutAnnee = dteDebutAnnee.timestampByAddingGregorianUnits(-1,0,0,0,0,0);
			 // on recupere d'abord 
			 NSArray records = fetchVIndividuCongeses(
					 ec, 
					 CRIDataBus.newCondition(
							 NO_INDIVIDU_KEY + "=%@ and " + C_STRUCTURE_KEY + "=%@ and " + DTE_DEBUT_ANNEE_KEY + "=%@",
							 new NSArray(new Object[]{noIndividu, cStructure, prevDteDebutAnnee})),
			      LRSort.newSort(D_DEB_AFFECTATION_KEY));
			 EOVIndividuConges record = null;
			 if (records.count() > 0) {
				 record = (EOVIndividuConges) records.lastObject();
			 }
			 return record;
		}
		
		/**
		 * Obtenir l'enregistrement de l'année précedente de la vue <code>V_INDIVIDU_CONGES</code>
		 * @param ec
		 * @param affAnn
		 * @return
		 */
		public static EOVIndividuConges getPrevRecord(EOEditingContext ec, EOAffectationAnnuelle affAnn) {
			 return getPrevRecord(ec, affAnn.individu().oid(), affAnn.structure().cStructure(), affAnn.dateDebutAnnee());
		}
		
		/**
		 * Recuperer la valeur des conges restants de l'annee universitaire
		 * precedente pour un individu et un service. Le retour est
		 * <code>null</code> si la valeur n'existe pas. On utilise la vue
		 * <code>V_INDIVIDU_CONGES</code> pour un acces plus rapide. Il 
		 * peut y avoir plusieurs valeurs pour une annee. Dans ce cas, on 
		 * prends celle de l'affectation la plus recente. Cette valeur
		 * est en <b>minutes</b>.
		 */
		public static Number getPrevCongesRestants(EOEditingContext ec, Number noIndividu, String cStructure, NSTimestamp dteDebutAnnee) {
			EOVIndividuConges prevRecord = getPrevRecord(ec, noIndividu, cStructure, dteDebutAnnee);
		  Number congesRestants = null;
		  if (prevRecord != null) {
		  	congesRestants = prevRecord.cngFinal();
		  }
		  return congesRestants;
		}	
}
