// EOStructure.java
// Created on Thu Jun 03 15:13:30  2004 by Apple EOModeler Version 5.2

package fr.univlr.cri.conges.eos.modele.grhum;


import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.constantes.ConstsDroit;
import fr.univlr.cri.conges.eos.modele.conges.EODroit;
import fr.univlr.cri.conges.eos.modele.conges.EOStructureAutorisee;
import fr.univlr.cri.util.ArrayCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.ycrifwk.utils.UtilDb;
import fr.univlr.cri.ycrifwk.utils.UtilMisc;

public class EOStructure extends _EOStructure {

    public EOStructure() {
        super();
    }

    // METHODES RAJOUTEES
    
    public final static String DISPLAY_KEY = "display";
    
    /**
     * gestion de l'affichage d'une structure dans une liste
     * @return
     */
    public String display() {
        String displayStructureItem = "";
        displayStructureItem = libelleLong();
        if (displayStructureItem.length() > 40)
            displayStructureItem = displayStructureItem.substring(0,37) + "...";
        displayStructureItem += " (" + libelleCourt();
        displayStructureItem += (toStructurePere() != null ?  " - " + toStructurePere().libelleCourt() : "" ) + ")";
        return displayStructureItem;
    }
    
    
    /**
     * tla structure est-elle une composante de l universite ?
     * @return
     */
    public boolean isComposanteBoolean() {
        return "C".equals(cTypeStructure()) || "E".equals(cTypeStructure()) || "ES".equals(cTypeStructure());
    }
    
    /**
     * retourne la liste des structures pere jusqu'a la composante d appartenance
     * ex : LABO l3i -> LABOS -> SCIENCES
     * @return
     */
    public NSArray listePeresToComposante() {
        NSArray liste = new NSArray(this);
        EOStructure prevPere= this;
        EOStructure currentPere= prevPere.toStructurePere();
        
        while (!currentPere.isComposanteBoolean() && prevPere != currentPere) {
            prevPere = currentPere;
            currentPere = prevPere.toStructurePere();
            liste = liste.arrayByAddingObject(currentPere);
        }
        if (currentPere != this) {
            liste = liste.arrayByAddingObject(currentPere);
        }
        return liste;
    }
    
    /**
     * trouver la composante dont d�pend un service
     */
    public EOStructure toComposante() {
    	// attention, si une structure de type service ne l'est
    	// plus et que des alertes sont dessus, alors plus de composante
    	// donc plantage .........
    	EOStructure recComposante = (EOStructure)storedValueForKey(TO_COMPOSANTE_KEY);
    	if (recComposante == null) {
    		recComposante = this;
    	}
    	return recComposante;
    }
    
    public final static String TOS_SERVICE_FILS_KEY = "tosServiceFils";
    
    /**
     * liste de tous les sous services au sens annuaire (on l'inclue si elle meme est aussi service)
     * ex : UFR DROIT -> ADGE DROIT + INSTITUT DE GESTION
     */
    public NSArray tosServiceFils() {
      EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(IS_SERVICE_KEY + " = %@", new NSArray(new Integer(1)));
      NSArray serviceFilsDirect = EOQualifier.filteredArrayWithQualifier(tosStructureFille(), qual);
      NSArray serviceFilsSuivant = (NSArray) tosStructureFille().valueForKeyPath(TOS_SERVICE_FILS_KEY);
      return ArrayCtrl.removeDoublons(UtilMisc.applatirNSArrays(serviceFilsDirect.arrayByAddingObjectsFromArray(serviceFilsSuivant)));
    }
    
    /**
     * Structures filles, excluant les boucles pour eviter les StackOverflowError
     * @return
     */
    public NSArray tosStructureFille() {
    	NSArray filles = (NSArray) storedValueForKey(TOS_STRUCTURE_FILLE_KEY);
    	return EOQualifier.filteredArrayWithQualifier(filles, CRIDataBus.newCondition(C_STRUCTURE_KEY + " <> '" + cStructure() +"'"));
    }


		public static EOStructure getStructureRacineInContext(EOEditingContext ec) {
		  return fetchStructure(ec, C_TYPE_STRUCTURE_KEY, "E");
		}


		/**
		 * liste de toutes les composantes
		 * @param ec
		 * @return
		 */
		public static NSArray findAllComposantesInContext(EOEditingContext ec) {
		  NSArray toutesLesComposantesInContext = null;
		  
		  EOQualifier qual = CRIDataBus.newCondition(
		  		C_TYPE_STRUCTURE_KEY+" = 'C' OR "+C_TYPE_STRUCTURE_KEY+" = 'E' OR "+C_TYPE_STRUCTURE_KEY+" = 'ES'", null);
		  toutesLesComposantesInContext = UtilDb.fetchArray(ec, EOStructure.ENTITY_NAME, qual, null);
		  toutesLesComposantesInContext = ArrayCtrl.removeDoublons(toutesLesComposantesInContext);
		   return toutesLesComposantesInContext;
		}


		/**
		 * trouver la structure d'apres son code
		 * @param ec
		 * @param cStructure
		 * @return
		 */
		public static EOStructure findStructureForCstructureInContext(EOEditingContext ec, String cStructure) {
		  EOQualifier qual = EOQualifier.qualifierWithQualifierFormat("oid = %@", new NSArray(cStructure));
		  NSArray records = UtilDb.fetchArray(ec, EOStructure.ENTITY_NAME, qual, null);
		  EOStructure record = null;
		  if (records.count() > 0) {
		    record = (EOStructure) records.lastObject();
		  }
		  return record;
		}


		/**
		 * liste des structures dont l'individu passe ne parametes est reponsable du chef de service uniquement
		 * @param ec
		 * @param individu
		 * @param niveau
		 * @return
		 */
		public static NSArray findStructureWithChefDeServiceForIndividuResponsableAndNiveau(EOEditingContext ec, EOIndividu individu, Integer niveau) {
			EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(
					EODroit.TO_INDIVIDU_RESP_KEY + "=%@ AND "+EODroit.CDR_NIVEAU_KEY+" = %@ AND "+EODroit.CDR_TYPE_KEY+" = %@",
												new NSArray (new Object[] { individu, niveau, ConstsDroit.DROIT_CIBLE_CHEF_DE_SERVICE}));
			NSArray droits = ec.objectsWithFetchSpecification(new EOFetchSpecification(EODroit.ENTITY_NAME, qual, null));
			NSMutableArray structures = new NSMutableArray();
			for (int i=0; i<droits.count(); i++)
				structures.addObject(((EODroit)droits.objectAtIndex(i)).toStructure());
			return structures.immutableClone();
		}


		/**
		 * liste des structures dont l'individu passe en parametres est responsable
		 * @param ec
		 * @param individu
		 * @param niveau
		 * @return
		 */
		public static NSArray findStructureForIndividuAndNiveau(EOEditingContext ec, EOIndividu individu, Integer niveau) {
			
			EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(
					EODroit.TO_INDIVIDU_RESP_KEY + "=%@ and "+
					EODroit.CDR_NIVEAU_KEY+" = %@ and "+
					EODroit.CDR_TYPE_KEY+" = %@",
					new NSArray (new Object[] { individu, niveau, ConstsDroit.DROIT_CIBLE_SERVICE}));
			
			NSArray droits = EODroit.fetchDroits(ec, qual, null);

			NSArray structures = new NSArray();
			for (int i=0; i<droits.count(); i++) {
				EODroit droit = (EODroit) droits.objectAtIndex(i);
				if (!structures.containsObject(droit.toStructure())) {
					structures = structures.arrayByAddingObject(droit.toStructure());
				}
			}

			return structures;
		}


		/**
		 * liste des structures dont l'individu passe en parametre est chef de service
		 * @param ec
		 * @param individu
		 * @return
		 */
		public static NSArray findStructureForChefDeService(EOEditingContext ec, EOIndividu individu) {
			EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(
					RESPONSABLE_KEY + "=%@ AND "+ IS_SERVICE_KEY + "=%@", 
		          new NSArray(new Object[]{individu, "1"}));
			EOFetchSpecification fetchSpec = new EOFetchSpecification(EOStructure.ENTITY_NAME, qual, null);
		      NSArray records = ec.objectsWithFetchSpecification(fetchSpec);
		      // glicage des doublons
		      return ArrayCtrl.removeDoublons(records);
		}


		/**
		 * liste des structure autorisee a utiliser l'application conges pour une date donnee
		 */
		public static NSArray servicesAutorisesInContext(EOEditingContext ec, NSTimestamp dateRef) {
			return (NSArray) EOStructureAutorisee.findAllStructureAutoriseeForAnneeUnivInContext(ec, dateRef).valueForKey(EOStructureAutorisee.STRUCTURE_KEY);
		}
      
}
