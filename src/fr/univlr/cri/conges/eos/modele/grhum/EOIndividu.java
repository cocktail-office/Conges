// EOIndividu.java
// Created on Thu Jun 03 15:13:08  2004 by Apple EOModeler Version 5.2

package fr.univlr.cri.conges.eos.modele.grhum;

import com.webobjects.eocontrol.*;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.conges.EOCET;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.util.ArrayCtrl;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRSort;
import fr.univlr.cri.ycrifwk.utils.UtilDb;

public class EOIndividu extends _EOIndividu {
	
	public EOIndividu() {
		super();
	}
  
    
	// METHODES RAJOUTEES
    
	public final static LRSort INDIVIDU_SORT = LRSort.newSort(NOM_KEY+","+PRENOM_KEY);
  
    
	public String nomComplet() {
		String nomComplet = "";
		nomComplet = nom()+" "+prenom();
		return nomComplet;
	}
	
	
	private final static int NOM_MAX_LENGTH = 18;
	
	/**
	 * Le nom complet mais réduit pour qu'il loge dans les interfaces
	 * s'il est trop long ...
	 * @return
	 */
	public String nomCompletNomTronque() {
		String nomCompletNomTronque = "";
		nomCompletNomTronque = nom();
		if (nomCompletNomTronque.length() > NOM_MAX_LENGTH) {
			nomCompletNomTronque = nom().substring(0, NOM_MAX_LENGTH-4) + " ...";
		}
		nomCompletNomTronque += " " + prenom();
		return nomCompletNomTronque;
	}
	
	/**
	 * nom + prenom + qualite (pour s'y retrouver dans les doublons)
	 */
	public String nomCompletQualite() {
	  return nomComplet() + 
	  (!StringCtrl.isEmpty(qualite()) ? " (" + StringCtrl.capitalizeWords(qualite()) + ")" : "");
	}
     
    
    /**
     * retourne vrai si l'individu appartient a la structure via RepartStructure
     */
    public boolean belongStructureRepartStructure(String cStructure) {
    	EOQualifier qual = CRIDataBus.newCondition("persId = %@ AND oidStructure = %@", 
    			new NSArray(new Object[] { persId(), cStructure}));
    	NSArray records = UtilDb.fetchArray(editingContext(), EORepartition.ENTITY_NAME, qual, null);
    	return (records.count() > 0);
    }
    
    /**
     * Lien vers le CET de l'agent
     * @return
     */
    public EOCET toCET() {
      NSArray tosCET = tosCET();
      EOCET record = null;
      if (tosCET.count() > 0) {
        record = (EOCET) tosCET.lastObject();
      }
      return record;
    }

    private final static String LIBELLE_STATUT_INCONNU 				= "Inconnu";
    private final static String LIBELLE_STATUT_FONCTIONNAIRE 	= "Titulaire";
    private final static String LIBELLE_STATUT_CONTRACTUEL 		= "Non titulaire";
    private final static String LIBELLE_STATUT_CDI 						= "Contractuel en CDI";
    
    private final static int STATUT_INCONNU 			= -1;
    private final static int STATUT_FONCTIONNAIRE = 0;
    private final static int STATUT_CONTRACTUEL 	= 1;
    private final static int STATUT_CDI					 	= 2;

		/**
		 * Donner le libellé du grade d'un agent
		 * - fonctionnaire : le grade s'il est trouvé, sinon le libellé "fonctionnaire"
		 * - contractuel : le libelle "contractuel" ou "cdi"
		 * @return
		 */
		public String getLibelleGradeForIndividu(EOAffectationAnnuelle affAnn) {
			return getLibelleGradeForIndividu(affAnn.dateDebutAnnee(), affAnn.dateFinAnnee());
		}
		
		/**
		 * Donner le libellé du grade d'un agent
		 * - fonctionnaire : le grade s'il est trouvé, sinon le libellé "fonctionnaire"
		 * - contractuel : le libelle "contractuel" ou "cdi"
		 * @return
		 */
		public String getLibelleGradeForIndividu(NSTimestamp dateDebut, NSTimestamp dateFin) {
		  
			String strStatut = LIBELLE_STATUT_INCONNU;
			
			int statut = getStatut(dateDebut, dateFin);
		  
		  if (statut == STATUT_FONCTIONNAIRE) {
		  	
		  	strStatut = LIBELLE_STATUT_FONCTIONNAIRE;
		  	
		  	NSArray lesElementsCarriere = EOElementCarriere.findElementCarriereForIndividuAndDateInContext(
		  			editingContext(), this, dateDebut, dateFin);  
		  	 
		  	// essayer de retrouver le grade de l'individu
		  	 	try {
		  	 		if (lesElementsCarriere.count() > 0) {
		  	 			strStatut = ((EOElementCarriere) lesElementsCarriere.lastObject()).toGrade().llGrade() + " (" + LIBELLE_STATUT_FONCTIONNAIRE + ")";
		      	} 
		  	 	} catch (Exception e) {
		  	 		e.printStackTrace();
					}
		  	
		  } else if (statut == STATUT_CONTRACTUEL) {
		  	strStatut = LIBELLE_STATUT_CONTRACTUEL;

		  } else if (statut == STATUT_CDI) {
		  	strStatut = LIBELLE_STATUT_CDI;
		  }
		  return strStatut;
		}

		/**
		 * Determiner le statut d'un agent sur une période.
		 * @param dateDebut
		 * @param dateFin
		 * @return
		 */
		private int getStatut(NSTimestamp dateDebut, NSTimestamp dateFin) {
		  NSArray lesElementsCarriere = EOElementCarriere.findElementCarriereForIndividuAndDateInContext(
		  		editingContext(), this, dateDebut, dateFin);  
		  // contractuel ou (fonctionnaire ou fonctionnaire stagiaire)
		  int statut = STATUT_INCONNU;
		  if (lesElementsCarriere.count() > 0) {
	      statut = STATUT_FONCTIONNAIRE;
		  } else {
		    statut = STATUT_CONTRACTUEL;
		    if (isContractuelCDI(dateDebut, dateFin)) {
		    	statut = STATUT_CDI;
		    }
		  }
		  return statut;
		}
		
		/**
		 * Indique si l'agent est en contrat sur la période de l'affectation annuelle
		 * @return
		 */
		public boolean isContractuel(EOAffectationAnnuelle affAnn) {
			NSArray lesElementsCarriere = EOElementCarriere.findElementCarriereForIndividuAndDateInContext(
					editingContext(), this, affAnn.dateDebutAnnee(), affAnn.dateFinAnnee());    
			return lesElementsCarriere.count() == 0;
		}
		
		/**
		 * Indique si l'agent est en CDI sur la période de l'affectation annuelle
		 * @return
		 */
		public boolean isContractuelCDI(EOAffectationAnnuelle affAnn) {	
			return isContractuelCDI(affAnn.dateDebutAnnee(), affAnn.dateFinAnnee());
		}
		
		/**
		 * Indique si l'agent est en CDI sur la période de l'affectation annuelle
		 * @return
		 */
		private boolean isContractuelCDI(NSTimestamp dateDebut, NSTimestamp dateFin) {
			NSArray contrats = EOContrat.findContratForIndividuAndDateInContext(
					editingContext(), this, dateDebut, dateFin);
			
			EOQualifier qualCdi = CRIDataBus.newCondition(EOContrat.C_TYPE_CONTRAT_TRAV_KEY + "='CI'", null);
			
			return EOQualifier.filteredArrayWithQualifier(contrats, qualCdi).count() > 0;
		}
		
		/**
		 * Retourne la chaine qualifier permettant d'effectuer une recherche sur un individu
		 * a partir d'un prefixe
		 * 
		 * @param prefixToIndividu:
		 * 	toOne vers l'entité Individu
		 * 
		 * @param searchString : 
		 * 	liste de mots a rechercher, séparés par des espaces
		 * 
		 * @return
		 */
		public static String getStringQualifierIndividuForNomPrenom(
				String prefixToIndividu,
				String searchString) {
			NSArray arrSearch = NSArray.componentsSeparatedByString(searchString.toUpperCase(), " ");
			
			StringBuffer sbQual = new StringBuffer("");
			
			if (arrSearch.count() == 1) {
				String firstWord = (String) arrSearch.objectAtIndex(0);
				// 1 seul mot : on cherche sur le nom / prenom / c_structure / libelleLong
		
				sbQual.append("("+prefixToIndividu + "." + EOIndividu.NOM_KEY+" caseInsensitiveLike '*"+firstWord+"*' or ");
				sbQual.append(""+prefixToIndividu + "." + EOIndividu.PRENOM_KEY+" caseInsensitiveLike '*"+firstWord+"*')");
		
			} else if (arrSearch.count() == 2) {
				// 2 mots : on cherche dans le couple nom / prenom, ou sur la structure
				String firstWord = (String) arrSearch.objectAtIndex(0);
				String secondWord = (String) arrSearch.objectAtIndex(1);
		 		
				if (sbQual.length() > 0) {
					sbQual.append(" or ");
				}
				sbQual.append("("+prefixToIndividu + "." + EOIndividu.NOM_KEY+" caseInsensitiveLike '*"+firstWord+ " " + secondWord + "*') or ");
				sbQual.append("("+prefixToIndividu + "." + EOIndividu.PRENOM_KEY+" caseInsensitiveLike '*"+firstWord+ " " + secondWord + "*') or ");
				sbQual.append("("+prefixToIndividu + "." + EOIndividu.NOM_KEY+" caseInsensitiveLike '*"+firstWord+"*' and "+prefixToIndividu + "." + 
						EOIndividu.PRENOM_KEY+" caseInsensitiveLike '*"+secondWord+"*')");
		
			}
			
			return sbQual.toString();
		}

		/**
		 * trouver la structure d'apres son persId
		 * @param ec
		 * @param cStructure
		 * @return
		 */
		public static EOIndividu findIndividuForPersIdInContext(EOEditingContext ec, Number persId) {
		  EOQualifier qual = CRIDataBus.newCondition(
		  		EOIndividu.PERS_ID_KEY+"=%@", new NSArray(persId));
		  return fetchIndividu(ec, qual);
		}


		/**
		 * trouver un individu selon un nom et/ou un prenom
		 * @param ec
		 * @param nomOuPrenom
		 * @param onlyInPersonnelActuel : <code>true</code> : restreindre la recherche aux personnels, <code>false</code> 
		 * @return
		 */
		public static NSArray findIndividuForNomOrPrenom(
				EOEditingContext ec, String nomOuPrenom, boolean onlyInPersonnelActuel) {
		  // mise en caps
		  nomOuPrenom = nomOuPrenom.toUpperCase();
		  NSArray findIndividuForNomOrPrenom = new NSArray();
		  NSArray mots = NSArray.componentsSeparatedByString(nomOuPrenom, " ");
		  String premierMot, deuxiemeMot;
		  premierMot = deuxiemeMot = "";
		  if (mots.count() > 0) {
		    premierMot = (String)mots.objectAtIndex(0);
		    if (mots.count() > 1) {
		      deuxiemeMot = (String)mots.objectAtIndex(1);
		    }
		    NSArray args = new NSArray(new String[] {
		    		"*"+nomOuPrenom+"*", 
		    		"*"+nomOuPrenom+"*", 
		    		"*"+premierMot+"*", 
		    		"*"+deuxiemeMot+"*", 
		    		"*"+premierMot+"*", 
		    		"*"+deuxiemeMot+"*"});
		    
		    String strQual = 
		    	"("+NOM_KEY+"  like %@ OR "+
		    	PRENOM_KEY+"  like %@ OR " +
    			"("+NOM_KEY+"  like %@ AND "+PRENOM_KEY+"  like %@) OR " +
					"("+PRENOM_KEY+"  like %@ AND "+NOM_KEY+"  like %@)" +
					")";
		    if (onlyInPersonnelActuel) {
		    	
		    	strQual += " and " + TO_V_PERSONNEL_ACTUEL_KEY + "." + EOVPersonnelActuel.NO_DOSSIER_PERS_KEY +" <> nil";
		    }
		    EOQualifier qual = CRIDataBus.newCondition(strQual, args);
		    findIndividuForNomOrPrenom = fetchIndividus(ec, qual, INDIVIDU_SORT);
		    findIndividuForNomOrPrenom = ArrayCtrl.removeDoublons(findIndividuForNomOrPrenom);
		  }
		  return findIndividuForNomOrPrenom;
		}


		/**
		 * tous les individus valides de l'universite
		 * @param ec
		 * @return
		 */
		public static NSArray findIndividuAll(EOEditingContext ec) {
			return fetchIndividus(ec, null,  INDIVIDU_SORT);
		}


		/**
		 * tous les individus du personnel de l'universite
		 * @param ec
		 * @return
		 */
		public static NSArray findIndividuInVPersonnelActuel(EOEditingContext ec) {
			return (NSArray) EOVPersonnelActuel.fetchVPersonnelActuels(ec, null, EOVPersonnelActuel.V_PERSONNEL_SORT).valueForKey(EOVPersonnelActuel.INDIVIDU_KEY);
		}


}
