package fr.univlr.cri.conges.databus;

import com.webobjects.eocontrol.EOAndQualifier;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.eocontrol.EOOrQualifier;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.constantes.ConstsAlerte;
import fr.univlr.cri.conges.constantes.ConstsDroit;
import fr.univlr.cri.conges.constantes.ConstsOccupation;
import fr.univlr.cri.conges.constantes.ConstsPlanning;
import fr.univlr.cri.conges.eos.modele.conges.EOAlerte;
import fr.univlr.cri.conges.eos.modele.conges.EODroit;
import fr.univlr.cri.conges.eos.modele.conges.EORepartValidation;
import fr.univlr.cri.conges.eos.modele.conges.EOVAlerte;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOCalculAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOOccupation;
import fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation;
import fr.univlr.cri.conges.eos.modele.planning.I_ClasseMetierNotificationParametre;
import fr.univlr.cri.conges.objects.Parametre;
import fr.univlr.cri.conges.objects.Planning;
import fr.univlr.cri.conges.objects.Responsabilite;
import fr.univlr.cri.conges.objects.finder.FinderClasse;
import fr.univlr.cri.conges.objects.occupations.Occupation;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.util.ArrayCtrl;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.*;
import fr.univlr.cri.ycrifwk.utils.UtilDb;

/*
 * Copyright Universit� de La Rochelle 2006
 *
 * ctarade@univ-lr.fr
 *
 * Ce logiciel est un programme informatique servant � g�rer les comptes
 * informatiques des utilisateurs. 
 * 
 * Ce logiciel est r�gi par la licence CeCILL soumise au droit fran�ais et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffus�e par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".

 * En contrepartie de l'accessibilit� au code source et des droits de copie,
 * de modification et de redistribution accord�s par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limit�e.  Pour les m�mes raisons,
 * seule une responsabilit� restreinte p�se sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les conc�dants successifs.

 * A cet �gard  l'attention de l'utilisateur est attir�e sur les risques
 * associ�s au chargement,  � l'utilisation,  � la modification et/ou au
 * d�veloppement et � la reproduction du logiciel par l'utilisateur �tant 
 * donn� sa sp�cificit� de logiciel libre, qui peut le rendre complexe � 
 * manipuler et qui le r�serve donc � des d�veloppeurs et des professionnels
 * avertis poss�dant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invit�s � charger  et  tester  l'ad�quation  du
 * logiciel � leurs besoins dans des conditions permettant d'assurer la
 * s�curit� de leurs syst�mes et ou de leurs donn�es et, plus g�n�ralement, 
 * � l'utiliser et l'exploiter dans les m�mes conditions de s�curit�. 

 * Le fait que vous puissiez acc�der � cet en-t�te signifie que vous avez 
 * pris connaissance de la licence CeCILL, et que vous en avez accept� les
 * termes.
 */

/**
 * Classe de manipulation des alertes.
 * 
 * @author Cyril TARADE <cyril.tarade at univ-lr.fr>
 */

public class CngAlerteBus 
	extends CngDataBus
	 implements I_ClasseMetierNotificationParametre {

  public CngAlerteBus(EOEditingContext editingContext) {
    super(editingContext);
  }

  // niveau de validation des planning
  private static String validationPlanningNiveau;
  // nombre d'année en arriere à remonter pour la visibilité des alertes
  private static int alerteNbAnneesAnterieuresVisibles;
  
  /**
   * @see I_ClasseMetierNotificationParametre
   * @param parametre
   */
  public static void initStaticField(
  		Parametre parametre) {
  	if (parametre == Parametre.PARAM_VALIDATION_PLANNING_NIVEAU) {
  		validationPlanningNiveau = parametre.getParamValueString();
  	} else if (parametre == Parametre.PARAM_ALERTE_NB_ANNEES_ANTERIEURES_VISIBLES) {
  		alerteNbAnneesAnterieuresVisibles = parametre.getParamValueInteger().intValue();
  	}
  }
  
  /**
   * Retourne toutes les alertes associees au cles primaires
   * passees en parametres. Si recsVAlerte est vide, alors on 
   * retourne le tableau vide;
   */
  public NSArray fetchEOAlerteFromOids(EOEditingContext ec, NSArray recsVAlerte) {
    NSArray alertes = new NSArray();
    if (recsVAlerte.count() > 0) {
      NSArray qualifiers = new NSArray();
      for (int i = 0; i < recsVAlerte.count(); i++) {
        Number anOid = ((LRRecord) recsVAlerte.objectAtIndex(i)).numberForKey("oid");
        qualifiers = qualifiers.arrayByAddingObject(newCondition("oidR="+anOid.intValue()));
      }
      EOOrQualifier orQual = new EOOrQualifier(qualifiers);
      alertes = fetchArray(ec, EOAlerte.ENTITY_NAME, orQual, null, true);
    }
    return alertes;
  }
  
  /**
   * 
   * @return
   */
  private EOQualifier getAnneeUnivVisibleVAlerteQualifier() {
  	EOQualifier qual = null;
  	
  	if (alerteNbAnneesAnterieuresVisibles >= 0) {
  		//NSMutableArray qualList = new NSMutableArray();
  		NSTimestamp now = DateCtrlConges.now();
  		/*for (int i=0; i<=alerteNbAnneesAnterieuresVisibles; i++) {
  			NSTimestamp anneeUniv = now.timestampByAddingGregorianUnits(-i, 0, 0, 0, 0, 0);
  			String anneeUnivStr = DateCtrlConges.anneeUnivForDate(anneeUniv);
  			qualList.addObject(
  					CRIDataBus.newCondition(
  							EOVAlerte.ANNEE_KEY+"=%@", new NSArray(anneeUnivStr)));
  		}
  		qual = new EOOrQualifier(qualList);*/

			NSTimestamp anneeUniv = now.timestampByAddingGregorianUnits(-alerteNbAnneesAnterieuresVisibles, 0, 0, 0, 0, 0);
			String anneeUnivStr = DateCtrlConges.anneeUnivForDate(anneeUniv);
  		qual = CRIDataBus.newCondition(
  				EOVAlerte.ANNEE_KEY + ">=%@",
  				new NSArray(anneeUnivStr));
  	}
  	
  	return qual;
  }
  
  /**
   * Recuperer toutes les alertes au travers de la vue V_ALERTE
   * sur les années visibles
   */
  public NSArray fetchViewAlerts(EOEditingContext ec) {
    NSArray alertesOid = (NSArray) fetchArray(
    		ec, EOVAlerte.ENTITY_NAME, getAnneeUnivVisibleVAlerteQualifier(), null, true);
    return alertesOid;
  }
  
  /**
   * Retourne la liste des alertes filtrees parmi une liste
   * d'objets <code>LRRecord</code> issus de l'entite <code>VAlerte</code>.
   * La visibilte est restreinte aux droits fixes dans l'objet
   * <code>Responsabilite</code>. Les objets sont du meme type que
   * ceux passes en entree.
   */
  public NSArray getValidationAlertOids(EOEditingContext ec, NSArray recsVAlerte) {

    long tDebut = System.currentTimeMillis();

    // les droits fixes dans la session actuelle
    Responsabilite responsabilite = cngSession().structuresDispoParNiveauSelection();

    // individu connecte
    Number noIndividuConnecte = cngSession().individuConnecte().oid();
    
    NSArray recsValidationVAlerte = new NSArray();

    // Cas particulier d'un individu de la DRH charge de valider les conges types
    if (cngSession().isDrh()) {
    	// les occupations de type DRH global (flagDRH=1)
      recsValidationVAlerte = recsValidationVAlerte.arrayByAddingObjectsFromArray(
      		filterAlertOids(recsVAlerte, 
      				(String) responsabilite.getStructure().cStructure(), 
      				null, 
      				noIndividuConnecte, 
      				null, 
      				FILTER_OCC_DRH_GLOBAL));
      // les demandes concernant les plannings si la validation des plannings est parametree comme ca
      if (validationPlanningNiveau.equals(Parametre.VALIDATION_PLANNING_NIVEAU_DRH_VALUE)) {
        recsValidationVAlerte = recsValidationVAlerte.arrayByAddingObjectsFromArray(
        		filterAlertOids(recsVAlerte, 
        				(String) responsabilite.getStructure().cStructure(), 
        				null, 
        				noIndividuConnecte, 
        				null, 
        				FILTER_PLANNING));
      }
    }
    // les DRH locaux a une composante
    if (droitBus().isDrhComposante(
    			ec, 
    			cngSession().individuConnecte(),
    			cngSession().structuresDispoParNiveauSelection().getStructure())) {
    		// occupations DRH validables par un DRH de composante (flagDRH=2)
    	recsValidationVAlerte = recsValidationVAlerte.arrayByAddingObjectsFromArray(
    			filterAlertOids(recsVAlerte, 
    					(String) responsabilite.getStructure().cStructure(),
    					(String) responsabilite.getStructure().toComposante().cStructure(), 
    					noIndividuConnecte, 
    					null, 
    					FILTER_OCC_DRH_COMPOSANTE));
      // les demandes concernant les plannings si la validation des plannings est parametree comme ca
      if (validationPlanningNiveau.equals(Parametre.VALIDATION_PLANNING_NIVEAU_DRH_VALUE)) {
        recsValidationVAlerte = recsValidationVAlerte.arrayByAddingObjectsFromArray(
        		filterAlertOids(recsVAlerte, 
        				(String) responsabilite.getStructure().cStructure(),
        				(String) responsabilite.getStructure().toComposante().cStructure(), 
        				noIndividuConnecte, 
        				null, 
        				FILTER_PLANNING));
      }
    }
    
    if (droitBus().isMinimumNiveauValidation(responsabilite.getNiveauParDefautSurService())) {
      // alertes plannings 
    	// les demandes concernant les plannings si la validation des plannings est parametree comme ca
      if (validationPlanningNiveau.equals(Parametre.VALIDATION_PLANNING_NIVEAU_VALUE_RESPONSABLE)) {
      	recsValidationVAlerte = recsValidationVAlerte.arrayByAddingObjectsFromArray(
     	    filterAlertOids(recsVAlerte, 
     	    		responsabilite.getStructure().cStructure(), 
     	    		null, 
     	    		noIndividuConnecte, 
     	    		null, 
     	    		FILTER_PLANNING));
      }
      // alertes occupation
      recsValidationVAlerte = recsValidationVAlerte.arrayByAddingObjectsFromArray(
          filterAlertOids(recsVAlerte, 
          		responsabilite.getStructure().cStructure(), 
          		null, 
          		noIndividuConnecte, 
          		null, 
          		FILTER_OCC_NON_DRH));
    } else {
      // au moins 1 personne a valider dans le service ?
      if (droitBus().isMinimumNiveauValidation(responsabilite.getNiveauMax())) {
        NSArray droits = responsabilite.getDroitList();
        for (int i = 0; i < droits.count(); i++) {
          EODroit droit = (EODroit) droits.objectAtIndex(i);
          if (!ConstsDroit.DROIT_CIBLE_SERVICE.equals(droit.cdrType())) {
            if (droitBus().isMinimumNiveauValidation(droit.cdrNiveau())) {
              // filtrage par rapport a l'individu concerne par ce droit
              EOIndividu individuCible = null;
              if (ConstsDroit.DROIT_CIBLE_CHEF_DE_SERVICE.equals(droit.cdrType())) {
                individuCible = droit.toStructure().responsable();
              } else {
                individuCible = droit.toIndividu();
              }
              if (individuCible != null) {
                // alertes plannings 
              	// les demandes concernant les plannings si la validation des plannings est parametree comme ca
                if (validationPlanningNiveau.equals(Parametre.VALIDATION_PLANNING_NIVEAU_VALUE_RESPONSABLE)) {
                	recsValidationVAlerte = recsValidationVAlerte.arrayByAddingObjectsFromArray(
               	    filterAlertOids(recsVAlerte, 
               	    		responsabilite.getStructure().cStructure(), 
               	    		null, 
               	    		null, 
               	    		individuCible.oid(), 
               	    		FILTER_PLANNING));
                }
                // alertes occupation
                recsValidationVAlerte = recsValidationVAlerte.arrayByAddingObjectsFromArray(
                    filterAlertOids(recsVAlerte, 
                    		responsabilite.getStructure().cStructure(), 
                    		null,
                    		null, 
                    		individuCible.oid(), 
                    		FILTER_OCC_NON_DRH));
              }
            }
          }
        }
      }
    }

    // on enleve les doublons
    recsValidationVAlerte = ArrayCtrl.removeDoublons(recsValidationVAlerte);

    // on classe par date
    NSArray sortDate = new NSArray(EOSortOrdering.sortOrderingWithKey(
    		EOVAlerte.D_CREATION_KEY, EOSortOrdering.CompareAscending));
    recsValidationVAlerte = EOSortOrdering.sortedArrayUsingKeyOrderArray(
    		recsValidationVAlerte, sortDate);
    

    LRLog.rawLog("fetching " + (recsValidationVAlerte.count() >10 ? ( recsValidationVAlerte.count()>100 ? "" : " "): "  ")+ 
        recsValidationVAlerte.count() + " alerts val. : [" + responsabilite.getStructure().display() + 
        "] - " +(System.currentTimeMillis()-tDebut)+ "ms", 2);
    
    return recsValidationVAlerte;
  }
  
  // masque des alertes deja visees 
  private final static EOQualifier QUAL_HIDE_ALERTES_DEJA_VISEES = EOQualifier.qualifierWithQualifierFormat(
      "(oidOcc <> nil AND occStatut <> %@ AND occStatut <> %@) OR " +
      "(oidOcc = nil AND plgStatut <> %@ AND plgStatut <> %@)", 
      new NSArray( new Object[] {
      		new Integer(Integer.parseInt(ConstsOccupation.CODE_VISEE)),
      		new Integer(Integer.parseInt(ConstsOccupation.CODE_EN_COURS_DE_SUPPRESSION_VISEE)),
      		ConstsPlanning.PLANNING_STATUT_EN_COURS_DE_MODIFICATION_VISE,
      		ConstsPlanning.PLANNING_STATUT_EN_COURS_DE_VALIDATION_VISE}));
  
  
  /**
   * Retourne la liste des alertes filtrees parmi une liste
   * d'objets <code>LRRecord</code> issus de l'entite <code>VAlerte</code>.
   * La visibilte est restreinte aux droits fixes dans l'objet
   * <code>Responsabilite</code>. Les objets sont du meme type que
   * ceux passes en entree.
   */
  public NSArray getVisaAlertOids(EOEditingContext ec, NSArray recsVAlerte) {

    long tDebut = System.currentTimeMillis();
  
    Responsabilite responsabilite = cngSession().structuresDispoParNiveauSelection();
    
    // individu connecte
    Number noIndividuConnecte = cngSession().individuConnecte().oid();

    NSArray recsVisaVAlerte = new NSArray();
  
    // alertes globales du service
    if (responsabilite.getNiveauParDefautSurService().intValue() == ConstsDroit.DROIT_NIVEAU_VISA.intValue()) {
    	// alertes plannings 	
      // les demandes concernant les plannings si la validation des plannings est parametree comme ca
      if (validationPlanningNiveau.equals(Parametre.VALIDATION_PLANNING_NIVEAU_VALUE_RESPONSABLE)) {
      	recsVisaVAlerte = recsVisaVAlerte.arrayByAddingObjectsFromArray(
      			filterAlertOids(recsVAlerte, 
      					responsabilite.getStructure().cStructure(), 
      					null, 
      					noIndividuConnecte, 
      					null, 
      					FILTER_PLANNING));
      }
      // alertes occupation
      recsVisaVAlerte = recsVisaVAlerte.arrayByAddingObjectsFromArray(
          filterAlertOids(recsVAlerte, 
          		responsabilite.getStructure().cStructure(), 
          		null, 
          		noIndividuConnecte, 
          		null, 
          		FILTER_OCC_NON_DRH));
    } else {
      // au moins 1 personne a viser dans le service ?
      if (droitBus().isMinimumNiveauVisa(responsabilite.getNiveauMax())) {
        NSArray droits = responsabilite.getDroitList();
        for (int i = 0; i < droits.count(); i++) {
          EODroit droit = (EODroit) droits.objectAtIndex(i);
          if (!ConstsDroit.DROIT_CIBLE_SERVICE.equals(droit.cdrType())) {
            // filtrage par rapport a l'individu concerne par ce droit
            EOIndividu individuCible = null;
            if (ConstsDroit.DROIT_CIBLE_CHEF_DE_SERVICE.equals(droit.cdrType()))
              individuCible = droit.toStructure().responsable();
            else
              individuCible = droit.toIndividu();
            if (individuCible != null) {
	            if (droit.cdrNiveau().intValue() == ConstsDroit.DROIT_NIVEAU_VISA.intValue()) {
	            	// cas des niveau 2 : visibilite absences en visa
	            	recsVisaVAlerte = recsVisaVAlerte.arrayByAddingObjectsFromArray(
	            			filterAlertOids(recsVAlerte, 
	            					responsabilite.getStructure().cStructure(), 
	                  		null, 
	                  		null, 
	                  		individuCible.oid(), 
	                  		FILTER_OCC_NON_DRH));
	            	// les demandes concernant les plannings si la validation des plannings est faite au niveau du responsable
	            	if (validationPlanningNiveau.equals(Parametre.VALIDATION_PLANNING_NIVEAU_VALUE_RESPONSABLE)) {
	              	recsVisaVAlerte = recsVisaVAlerte.arrayByAddingObjectsFromArray(
	              			filterAlertOids(recsVAlerte, 
		                  		responsabilite.getStructure().cStructure(), 
		                  		null, 
		                  		null, 
		                  		individuCible.oid(), 
	              					FILTER_PLANNING));
	              }
	            } else {
	              // cas des niveaux 3 : visibilite des alertes DRH en visa
	              recsVisaVAlerte = recsVisaVAlerte.arrayByAddingObjectsFromArray(
	                  filterAlertOids(recsVAlerte, 
	                  		responsabilite.getStructure().cStructure(), 
	                  		null, 
	                  		null, 
	                  		individuCible.oid(),  
	                  		FILTER_OCC_DRH_VISA));
	              // les demandes concernant les plannings si la validation des plannings est parametree comme ca
	              if (validationPlanningNiveau.equals(Parametre.VALIDATION_PLANNING_NIVEAU_DRH_VALUE)) {
	              	recsVisaVAlerte = recsVisaVAlerte.arrayByAddingObjectsFromArray(
	              			filterAlertOids(recsVAlerte, 
		                  		responsabilite.getStructure().cStructure(), 
		                  		null, 
		                  		null, 
		                  		individuCible.oid(), 
	              					FILTER_PLANNING));
	              }
	            }
            }
          }
        }
      }
    }
  
    // alertes DRH de tout le service pour le chef de service
    if (droitBus().isMinimumNiveauValidation(responsabilite.getNiveauParDefautSurService())) {
      // alertes DRH masques pour la visa pour les DRH
      if (!cngSession().isDrh()) {
        recsVisaVAlerte = recsVisaVAlerte.arrayByAddingObjectsFromArray(
            filterAlertOids(recsVAlerte, 
            		responsabilite.getStructure().cStructure(), 
            		null, 
            		noIndividuConnecte, 
            		null, 
            		FILTER_OCC_DRH_VISA));
      }
      // les demandes concernant les plannings si la validation des plannings est parametree comme ca
      if (validationPlanningNiveau.equals(Parametre.VALIDATION_PLANNING_NIVEAU_DRH_VALUE)) {
      	recsVisaVAlerte = recsVisaVAlerte.arrayByAddingObjectsFromArray(
      			filterAlertOids(recsVAlerte, 
            		responsabilite.getStructure().cStructure(), 
            		null, 
            		noIndividuConnecte, 
            		null, 
      					FILTER_PLANNING));
      }
    }
    
    // on cache les absences deja visées et plannings deja visées
    recsVisaVAlerte = EOQualifier.filteredArrayWithQualifier(recsVisaVAlerte, QUAL_HIDE_ALERTES_DEJA_VISEES);
  
    // on enleve les doublons
    recsVisaVAlerte = ArrayCtrl.removeDoublons(recsVisaVAlerte);
  
    // on classe par date
    NSArray sortDate = new NSArray(EOSortOrdering.sortOrderingWithKey("dCreation", EOSortOrdering.CompareAscending));
    recsVisaVAlerte = EOSortOrdering.sortedArrayUsingKeyOrderArray(recsVisaVAlerte, sortDate);
  
    LRLog.rawLog("fetching " + (recsVisaVAlerte.count() >10 ? ( recsVisaVAlerte.count()>100 ? "" : " "): "  ")+ 
        recsVisaVAlerte.count() + " alerts visa : [" + responsabilite.getStructure().display() + 
        "] - " +(System.currentTimeMillis()-tDebut)+ "ms", 2); 
    
    return  recsVisaVAlerte;

  }
  
  // les modes d'utilisation de la methode filterAlertOids()
  
  /** n'afficher que les alertes concernant les DRH de l'etablissement */
  private final static int FILTER_OCC_DRH_GLOBAL 			= 1;
  /** n'afficher que les alertes concernant les DRH de composantes */
  private final static int FILTER_OCC_DRH_COMPOSANTE 	= 2;
  /** n'afficher que les alertes DRH a faire visa par les valideurs */
  private final static int FILTER_OCC_DRH_VISA 				= 3;
  /** n'afficher que les alertes concernant les occupations non DRH */
  private final static int FILTER_OCC_NON_DRH 				= 4;
  /** n'afficher que les alertes concernant les plannings  */
  private final static int FILTER_PLANNING 						= 5;
 
  // les qualifiers fixes de la methode filterAlertOids()
  /** DRH global */
  private final static EOQualifier QUAL_OCC_DRH_GLOBAL = newCondition(
      "flagDRH <> %@ AND (occStatut = %@ OR occStatut = %@ OR occStatut = %@ OR occStatut = %@ )", 
      new NSArray(new Object[] {
          EOTypeOccupation.FLAG_NON_DRH, 
          new Integer(Integer.parseInt(ConstsOccupation.CODE_EN_COURS_DE_SUPPRESSION)), 
          new Integer(ConstsOccupation.CODE_EN_COURS_DE_VALIDATION), 
       		new Integer(ConstsOccupation.CODE_VISEE),
     			new Integer(ConstsOccupation.CODE_EN_COURS_DE_SUPPRESSION_VISEE)}));
  /** DRH composantes */
  private final static EOQualifier QUAL_OCC_DRH_COMPOSANTE = newCondition(
      "flagDRH = %@ AND (occStatut = %@ OR occStatut = %@ OR occStatut = %@ OR occStatut = %@ )", 
      new NSArray(new Object[] {
      		EOTypeOccupation.FLAG_DRH_COMPOSANTE, 
      		new Integer(ConstsOccupation.CODE_EN_COURS_DE_SUPPRESSION), 
      		new Integer(ConstsOccupation.CODE_EN_COURS_DE_VALIDATION), 
      		new Integer(ConstsOccupation.CODE_VISEE),
      		new Integer(ConstsOccupation.CODE_EN_COURS_DE_SUPPRESSION_VISEE)}));
  /** DRH a viser par les responsables */
  private final static EOQualifier QUAL_OCC_DRH_VISA = newCondition(
      "flagDRH <> %@ AND (occStatut = %@ OR occStatut = %@) AND occStatut <> %@ AND occStatut <> %@ ", 
      new NSArray(new Object[] {
      		EOTypeOccupation.FLAG_NON_DRH, 
      		new Integer(ConstsOccupation.CODE_EN_COURS_DE_SUPPRESSION), 
      		new Integer(ConstsOccupation.CODE_EN_COURS_DE_VALIDATION), 
      		new Integer(ConstsOccupation.CODE_VISEE),
      		new Integer(ConstsOccupation.CODE_EN_COURS_DE_SUPPRESSION_VISEE)}));
  /** occupations non DRH */
  private final static EOQualifier QUAL_OCC_NON_DRH = newCondition(
      "(flagDRH = %@ or flagDRH = nil) AND (occStatut = %@ OR occStatut = %@ OR occStatut = %@ OR occStatut = %@ )", 
      new NSArray(new Object[] {
      		EOTypeOccupation.FLAG_NON_DRH, 
      		new Integer(ConstsOccupation.CODE_EN_COURS_DE_SUPPRESSION), 
      		new Integer(ConstsOccupation.CODE_EN_COURS_DE_VALIDATION), 
      		new Integer(ConstsOccupation.CODE_VISEE),
      		new Integer(ConstsOccupation.CODE_EN_COURS_DE_SUPPRESSION_VISEE)}));
  /** planning */
  private final static EOQualifier QUAL_PLANNING = newCondition("oidOcc = nil");
  /** aucune alertes */
  private final static EOQualifier QUAL_NOTHING = newCondition("oid = -1");
  
  /**
   * filtrer des alertes issus de l'entite <code>VAlerte</code>
   * 
   * @param allAlerts : la liste des alertes a filtrer
   * @param cStructureServiceConcerne : la structure concernee -> utilisee pour le filtrage des alertes liees au service
   * @param cStructureComposanteConcerne : la composante concernee -> utilisee pour la validation des conges DRH pour les resp de composantes
   * @param noIndividuAExclure
   * @param noIndividuAInclure
   * @param filterType : le filtre attendu (voir les QUAL_)
   */
  private NSArray filterAlertOids(NSArray allAlerts, 
  		String cStructureServiceConcerne, String cStructureComposanteConcerne, 
  		Number noIndividuAExclure, Number noIndividuAInclure, 
  		int filterType) {
 
  	// les qualifiers d'ouverture
  	EOQualifier qualOpen = QUAL_NOTHING; 
    if (filterType == FILTER_PLANNING)
    	qualOpen = QUAL_PLANNING;
    else if (filterType == FILTER_OCC_DRH_GLOBAL)        
    	qualOpen = QUAL_OCC_DRH_GLOBAL;
    else if (filterType == FILTER_OCC_DRH_VISA)
    	qualOpen = QUAL_OCC_DRH_VISA;
    else if (filterType == FILTER_OCC_DRH_COMPOSANTE)
    	qualOpen = QUAL_OCC_DRH_COMPOSANTE;
    else if (filterType == FILTER_OCC_NON_DRH)
    	qualOpen = QUAL_OCC_NON_DRH;
    
    // les qualifiers de restriction
    NSArray arrayQualsClose = new NSArray(); 
    if (!StringCtrl.isEmpty(cStructureServiceConcerne))
      arrayQualsClose = arrayQualsClose.arrayByAddingObject(newCondition(EOVAlerte.C_STRUCTURE_KEY+"='"+cStructureServiceConcerne+"'"));
    if (!StringCtrl.isEmpty(cStructureComposanteConcerne))
      arrayQualsClose = arrayQualsClose.arrayByAddingObject(newCondition(EOVAlerte.C_STRUCTURE_COMPOSANTE_KEY+"='"+cStructureComposanteConcerne+"'"));
    if (noIndividuAExclure != null)
      arrayQualsClose = arrayQualsClose.arrayByAddingObject(newCondition(EOVAlerte.NO_INDIVIDU_KEY+"<>"+noIndividuAExclure.intValue()));
    if (noIndividuAInclure != null)
      arrayQualsClose = arrayQualsClose.arrayByAddingObject(newCondition(EOVAlerte.NO_INDIVIDU_KEY+"="+noIndividuAInclure.intValue()));
    EOQualifier qualClose = new EOAndQualifier(arrayQualsClose);
 
    // on ignore toutes les hsup et conges comp sur une annee universitaire anterieures
    EOQualifier qualIgnoreOldHSupCComp =
        newCondition("("+EOVAlerte.T_OCCLIBELLE_COURT_KEY+"<>%@ and "+EOVAlerte.T_OCCLIBELLE_COURT_KEY+"<>%@) or " +
            "(" +
            "	("+EOVAlerte.T_OCCLIBELLE_COURT_KEY+"=%@ or "+EOVAlerte.T_OCCLIBELLE_COURT_KEY+"=%@) and " +
            "	("+EOVAlerte.OCC_DTE_DEBUT_KEY+">=%@ and "+EOVAlerte.OCC_DTE_DEBUT_KEY+"<=%@)" +
            ")",
            new NSArray(new Object[]{
                cngSession().typeCongesCompensateurs().libelleCourt(), cngSession().typeHeuresSupplementaires().libelleCourt(), 
                cngSession().typeCongesCompensateurs().libelleCourt(), cngSession().typeHeuresSupplementaires().libelleCourt(), 
                cngSession().debutAnnee(), cngSession().finAnnee()}));
        
    // on fait un tableau de tout ce ptit monde
    // on prend pas le qualifier de restriction sur les hsup / ccomp
    // si on est sur du filtrage de planning
    NSArray qualList = new NSArray(new EOQualifier[] {qualClose, qualOpen});
    /*if (filterType != FILTER_PLANNING) {
    	qualList = qualList.arrayByAddingObject(qualIgnoreOldHSupCComp);
    }*/
    EOQualifier qual = new EOAndQualifier(qualList);
  
    LRLog.rawLog("filter qual : " + qual, 2);
    
    NSArray alerts = EOQualifier.filteredArrayWithQualifier(allAlerts, qual);
     
    return alerts;
  }
  
  /**
   * La liste des année universitaires associées a des alertes.
   * @param alerts
   * @return
   */
  public NSArray getAnneeUnivListForAlerts(NSArray alerts) {
  	NSArray listAll = (NSArray) alerts.valueForKeyPath(EOAlerte.AFFECTATION_ANNUELLE_KEY + "." + EOAffectationAnnuelle.ANNEE_KEY);
  	
  	NSMutableArray list = new NSMutableArray();
  	// de combien d'années doit-on remonter 
  	// 0 = uniquement l'année en cours
  	if (alerteNbAnneesAnterieuresVisibles >= 0) {

  		NSTimestamp now = DateCtrlConges.now();
  		for (int i=0; i<=alerteNbAnneesAnterieuresVisibles; i++) {
  			NSTimestamp anneeUniv = now.timestampByAddingGregorianUnits(-i, 0, 0, 0, 0, 0);
  			String anneeUnivStr = DateCtrlConges.anneeUnivForDate(anneeUniv);
  			if (listAll.containsObject(anneeUnivStr)) {
  				list.addObject(anneeUnivStr);
  			}
  		}
  		
  		// remettage dans l'ordre chronologique
  		NSMutableArray listDisordered = new NSMutableArray(list);
  		list = new NSMutableArray();
  		for (int i=0; i<listDisordered.count(); i++) {
  			list.addObject(listDisordered.objectAtIndex(listDisordered.count() -1 -i));
  		}
  		
  		// on oubli pas les années universitaires suivantes
  		for (int i=0; i<listAll.count(); i++) {
  			String annee = (String) listAll.objectAtIndex(i);
  			NSTimestamp anneeDate = DateCtrlConges.dateDebutAnneePourStrPeriodeAnnee(annee);
  			if (DateCtrlConges.isAfter(anneeDate, now) && 
  					!list.containsObject(annee)) {
  				list.addObject(annee);
  			}
  		}
  		
  	} else {
  		list = new NSMutableArray(listAll);
  	}
  	
  	list = new NSMutableArray(ArrayCtrl.removeDoublons(list));
  	
  	return list;
  }

  /**
   * Donne le nombre d'alerte adressees a la DRH
   * parmi une liste d'alertes provenant de
   * l'entite <code>VAlerte</code>
   */
  public int countAlertDRH(NSArray allAlerts) {
    return EOQualifier.filteredArrayWithQualifier(allAlerts,
        newCondition("flagDRH <> nil AND flagDRH <> %@", new NSArray(EOTypeOccupation.FLAG_NON_DRH))).count();
  }
  

  /**
   * Trouve l'alerte associée a la validation d'un planning reel
   * L'objet est un <code>LRRecord</code> de l'entite <code>Alerte</code>
   */
  public EOAlerte findAlertePReelForPlanning(Planning planning) {
    EOAlerte recAlerte = null;
    NSArray recsVAlerte = fetchArray("VAlerte", newCondition("oidAffAnn = " + 
        planning.affectationAnnuelle().oid().intValue() +"  AND oidOcc = nil"), null);
    if (recsVAlerte.count() > 0) {
      LRRecord recVAlerte = (LRRecord) recsVAlerte.lastObject();
      NSArray recsAlerte = EOQualifier.filteredArrayWithQualifier(
          fetchEOAlerteFromOids(planning.affectationAnnuelle().editingContext(), new NSArray(recVAlerte)),
          newCondition("isModificationReel = %@", new NSArray(new Boolean(true))));
      if (recsAlerte.count() > 0)
        recAlerte = (EOAlerte) recsAlerte.lastObject();
    }
    return recAlerte;
  }
  
    
  /**
   * Trouve l'alerte associée a la validation d'un planning prévisionnel
   * L'objet est un <code>LRRecord</code> de l'entite <code>Alerte</code>
   */
  public EOAlerte findAlertePPrevForPlanning(Planning planning) {
    EOAlerte recAlerte = null;
    NSArray recsVAlerte = fetchArray("VAlerte", newCondition("oidAffAnn = " + 
        planning.affectationAnnuelle().oid().intValue() +"  AND oidOcc = nil"), null);
    if (recsVAlerte.count() > 0) {
      LRRecord recVAlerte = (LRRecord) recsVAlerte.lastObject();
      NSArray recsAlerte = EOQualifier.filteredArrayWithQualifier(
          fetchEOAlerteFromOids(planning.affectationAnnuelle().editingContext(), new NSArray(recVAlerte)),
          newCondition("isValidationPrev = %@", new NSArray(new Boolean(true))));
      if (recsAlerte.count() > 0)
        recAlerte = (EOAlerte) recsAlerte.lastObject();
    }
    return recAlerte;
  }

  
  /**
   * trouve toutes les alertes associees a un planning
   * L'object est un <code>NSArray</code> de 
   * <code>LRRecord</code> de l'entite <code>Alerte</code>
   */
  public NSArray findAlertesForPlanning(Planning planning) {
    NSArray recsVAlerte = (NSArray) fetchArray(
    		EOVAlerte.ENTITY_NAME, newCondition(
    				EOVAlerte.OID_AFF_ANN_KEY+"="+planning.affectationAnnuelle().oid().intValue()), null);
    return fetchEOAlerteFromOids(planning.affectationAnnuelle().editingContext(), recsVAlerte);
  }
  
  /**
   * Indique si l'utilisateur <code>uiResp</code> possede
   * le droit <code>niveau</code> pour l'alerte <code>alerte</code>.
   */
  private boolean isResponsable(EOAlerte alerte, CngUserInfo uiResp, Integer niveau, boolean isAccept)  {
  	// DRH et Admin ont tous les droits
  	if (uiResp.isAdmin() || uiResp.isDrh() || 
  			uiResp.isDrhComposanteForPlanning(alerte.affectationAnnuelle()) ||
  			uiResp.isAdmComposanteForPlanning(alerte.affectationAnnuelle()))
  		return true;
  	try {
  		boolean isVisa = niveau.intValue() == ConstsDroit.DROIT_NIVEAU_VISA.intValue();
  		// le tableau est un tableau de <code>Double</code> ...
  		NSArray responsablesPersId = (isVisa ?
    			(NSArray) planningBus().viseurs(droitBus(), alerte.affectationAnnuelle(), alerte.occupation(), true).valueForKey("persId") : 
    			(NSArray) planningBus().responsables(droitBus(), alerte.affectationAnnuelle(), alerte.occupation(), true).valueForKey("persId"));
    	Double uiRespPersIdDouble = new Double(uiResp.persId().intValue());
     	setErrorMessage("Nous n'avez pas les droits suffisant pour effectuer l'operation suivante:\n" +
     			"Droit d" + (isAccept ? "'accepter " : "e refuser ") +
     			(isVisa ? "le visa" : "la validation") + " sur le planning de " +
     			alerte.affectationAnnuelle().individu().nomComplet());
     	return responsablesPersId.containsObject(uiRespPersIdDouble);  	
  	} catch (Exception e) {
  		setErrorMessage("Erreur lors de la recherche des responsables. " +
  				"\nVeuillez contacter le service informatique");
    	return false;
  	}
  }
  
  /**
   * Action d'accepter la demande provenant d'une alerte.
   * @throws Throwable 
   */
  public boolean accepteValid(EOEditingContext ec, EOAlerte alerte, CngUserInfo uiResp) {
    boolean transactionOK = false;
    
    // determiner si le valideur est bien declare comme valideur
    if (!isResponsable(alerte, uiResp, ConstsDroit.DROIT_NIVEAU_VALIDATION, true)) {
    	return false;
    }
    
    EOOccupation occupation = alerte.occupation();
    boolean hasException = false;
    StringBuffer logBuffer = new StringBuffer(uiResp.nomEtPrenom());
    logBuffer.append(" accepte ");
    
    if (occupation != null) {
      try {
        
        Object aTypeOccupation = null;
        // on recupere la liste de toutes les occupations associees en base
        NSArray recsOccupation = EOOccupation.findOccupationsInContext(ec, occupation.affectationAnnuelle().individu(),
            occupation.affectationAnnuelle().structure(), occupation.typeOccupation(), occupation.flagNature(),
            occupation.dateDebut(), occupation.dateFin(), occupation.status());
        
        for (int i = 0; i < recsOccupation.count(); i++) {
          EOOccupation recOcc = (EOOccupation) recsOccupation.objectAtIndex(i);
          // on reimpute les demandes DRH (besoin d'un objet planning dans le constructeur pour accepter() )
          if (recOcc.isCongeDRH()) {
          	EOAffectationAnnuelle affAnn = alerte.affectationAnnuelle();
            Planning planningPourAlerte = Planning.newPlanning(
            		affAnn, uiResp, affAnn.dateDebutAnnee());
            // ca touche le planning reel
            planningPourAlerte.setType("R");
            Object arguments[] = { recOcc, planningPourAlerte, ec };
            Class argumentTypes[] = { EOOccupation.class, Planning.class, EOEditingContext.class };
            aTypeOccupation = FinderClasse.findClassForType(recOcc.typeOccupation(), arguments, argumentTypes);
          } else {
            Object arguments[] = { recOcc, ec };
            Class argumentTypes[] = { EOOccupation.class, EOEditingContext.class };
            aTypeOccupation = FinderClasse.findClassForType(recOcc.typeOccupation(), arguments, argumentTypes);
          }

          if (recOcc.status().equals(ConstsOccupation.CODE_EN_COURS_DE_SUPPRESSION) || 
          		recOcc.status().equals(ConstsOccupation.CODE_EN_COURS_DE_SUPPRESSION_VISEE)) {
            ((NSKeyValueCoding) aTypeOccupation).valueForKey("supprimer");
            recOcc.setStatus(ConstsOccupation.CODE_SUPPRIMEE);
            logBuffer.append("suppression");
          } else {
            // conges DRH : il faut forcer le calcul de la valeur pour l acceptation
            if (occupation.isCongeDRH()) {
              ((NSKeyValueCoding) aTypeOccupation).valueForKey("calculerValeur");
              ((NSKeyValueCoding) aTypeOccupation).valueForKey(ConstsOccupation.CONFIRMER_KEY);
            }
            ((NSKeyValueCoding) aTypeOccupation).valueForKey(ConstsOccupation.ACCEPTER_KEY);

            // TODO Validation d'une demande avec recOcc de type astreinte ou heure sup
            recOcc.setStatus(ConstsOccupation.CODE_VALIDEE);
            logBuffer.append("validation");
          }

          // enregistrement du valideur de recOcc
          if (EORepartValidation.individuHasValideOccupation(ec, recOcc, uiResp) == false) {
          	EORepartValidation.newRepartValidation(
            		ec, uiResp, recOcc.affectationAnnuelle(), recOcc, EORepartValidation.FLAG_VALIDATION_VALUE);
          }        
        }
        
        logBuffer.append(" occ. ").append(alerte.oidOccupation());
        logBuffer.append(" (").append(occupation.affectationAnnuelle().individu().nomComplet()).append(")");
      } catch (Throwable e) {
        e.printStackTrace();
        hasException = true;
      }
    } else {
      // Cas particulier de la validation du P.Previsionnel ou du P. Reel
      EOAffectationAnnuelle affAnn = alerte.affectationAnnuelle();
      // enlever l'historique de la validation precedente 
      affAnn.removeRepartValidation();
      // si le planning n'est pas pass� par la case visa, on enleve l'historique visa
      if (!affAnn.isPlanningVise()) {
      	affAnn.removeRepartVisa();
      }
      // allez hop on valide
      affAnn.setStatusPlanning(ConstsPlanning.PLANNING_STATUT_VALIDE);
      //
      if (alerte.isValidationPrev()) {
        planningBus().dupliquerPlanning(affAnn, "P", "R", false);
        logBuffer.append("validation planning prev. ");
      } else
        logBuffer.append("modification planning reel ");
      // enregistrement du valideur de planning
      EORepartValidation.newRepartValidation(ec, uiResp, affAnn, occupation, EORepartValidation.FLAG_VALIDATION_VALUE);
      logBuffer.append(" (").append(affAnn.individu().nomComplet()).append("/");
      logBuffer.append(affAnn.structure().libelleCourt()).append("/");
      logBuffer.append(affAnn.annee()).append(")");
      
    }

    if (!hasException) {
      alerte.sendMailAlerteTraitee(uiResp, ConstsAlerte.ALERTE_LIBELLE_ACCEPTEE);
      ec.deleteObject(alerte);
      try {
  	    UtilDb.save(ec, true);
        transactionOK = true;
      } catch (Throwable e) {
      	e.printStackTrace();
			}
    }

    if (transactionOK)
      LRLog.log(logBuffer.toString());
    
    return transactionOK;
  }
  
  /** 
   * Le responsable refuse  demande. On efface l'alerte
   * et on refuse l'ensemble des occupations basees sur cette alerte.
   * 
   * Fusion des 2 methodes refuseValide et refuseVisa car le traitement 
   * est exactement le meme (sauf pour les libelles et controles de niveau)
   */
  public boolean refuse(EOEditingContext ec, EOAlerte alerte, CngUserInfo uiResp, Integer niveau) {
    boolean transactionOK = false;
  	
    // determiner si le valideur est bien declare comme valideur
    if (!isResponsable(alerte, uiResp, niveau, false)) {
    	return false;
    }
    
    StringBuffer logBuffer = new StringBuffer(uiResp.nomEtPrenom());
    logBuffer.append(" refuse ");
    
    EOOccupation occupation = alerte.occupation();
    EOAffectationAnnuelle affAnn = alerte.affectationAnnuelle();


    if (occupation != null) {
    
      // on recupere la liste de toutes les occupations associees en base
      NSArray recsOccupation = EOOccupation.findOccupationsInContext(ec, occupation.affectationAnnuelle().individu(),
          occupation.affectationAnnuelle().structure(), occupation.typeOccupation(), occupation.flagNature(),
          occupation.dateDebut(), occupation.dateFin(), occupation.status());

      for (int i = 0; i < recsOccupation.count(); i++) {
        EOOccupation recOcc = (EOOccupation) recsOccupation.objectAtIndex(i);
        // cas spéciaux ou le refus entraine la validation du congé voire plus
        if (ConstsOccupation.CODE_EN_COURS_DE_SUPPRESSION.equals(recOcc.status()) == true) {
        	// le refus de suppression d'une occupation en cours de suppression entraine sa validation
          recOcc.setStatus(ConstsOccupation.CODE_VALIDEE);
        } else if (recOcc.isCongeDRH() && ConstsOccupation.CODE_VISEE.equals(recOcc.status()) == true) {
        	// le refus de validation d'un conge DRH visé entraine sa validation et sa transformation en congé annuel
          recOcc.setStatus(ConstsOccupation.CODE_VALIDEE);
          recOcc.setTypeOccupationRelationship(
          		EOTypeOccupation.getTypeOccupationForLibelleCourtInContext(ec, Occupation.LIBELLE_COURT_CONGES_ANNUEL));
          // on oublie pas de positionner le valideur en viseur
          recOcc.removeRepartValidation();
          if (recOcc.repartVisa() != null) {
          	EOIndividu viseur = recOcc.repartVisa().individu();
          	CngUserInfo uiViseur = new CngUserInfo(droitBus(), preferenceBus(), ec, viseur.persId());
          	EORepartValidation.newRepartValidation(
            		ec, uiViseur, recOcc.affectationAnnuelle(), recOcc, EORepartValidation.FLAG_VALIDATION_VALUE);
          }
          recOcc.resetCacheValidation();
        }	else {
          EOCalculAffectationAnnuelle calculs = affAnn.calculAffAnn(recOcc.flagNature());
          recOcc.setStatus(ConstsOccupation.CODE_REFUSEE);
          calculs.addMinutesRestantes(recOcc.valeurMinutes().intValue());
          recOcc.setValeurMinutes(new Integer(0));
        }
      }
      
      if (ConstsOccupation.CODE_EN_COURS_DE_SUPPRESSION.equals(occupation.status())) {
        logBuffer.append("suppression");
      } else {
      	if(niveau == ConstsDroit.DROIT_NIVEAU_VALIDATION) {
          logBuffer.append("validation");
      	} else {
          logBuffer.append("visa");
      	}
      }
      logBuffer.append(" occ. ").append(alerte.oidOccupation());
      logBuffer.append(" (").append(occupation.affectationAnnuelle().individu().nomComplet()).append(")");

    } else {
      // Cas particulier de la validation du Planning
      // le planning passe en cours de modif si en cours de validation + alerte modification
      if (alerte.isModificationReel() && affAnn.isPlanningEnCoursDeValidation()) {
        logBuffer.append("modification planning reel (niveau ");
      	if(niveau == ConstsDroit.DROIT_NIVEAU_VALIDATION) {
          logBuffer.append(" validation)");
      	} else {
          logBuffer.append(" visa)");
      	}
        // on remet en modif
      	affAnn.setStatusPlanning(ConstsPlanning.PLANNING_STATUT_EN_COURS_DE_MODIFICATION);
        // on restaure la sauvegarde
        planningBus().dupliquerPlanning(alerte.affectationAnnuelle(), "S", "R", false);
     
      } else {
        logBuffer.append("validation planning prev. (niveau ");
      	if(niveau == ConstsDroit.DROIT_NIVEAU_VALIDATION) {
          logBuffer.append(" validation)");
      	} else {
          logBuffer.append(" visa)");
      	}
        // sinon invalide
      	affAnn.setStatusPlanning(ConstsPlanning.PLANNING_STATUT_INVALIDE);
      }
      logBuffer.append(" (").append(affAnn.individu().nomComplet()).append("/");
      logBuffer.append(affAnn.structure().libelleCourt()).append("/");
      logBuffer.append(affAnn.annee()).append(")");

      // pour un refus, on enleve tous les historiques de validation associes
      affAnn.removeRepartVisa();
      affAnn.removeRepartValidation();
    }

    alerte.sendMailAlerteTraitee(uiResp, ConstsAlerte.ALERTE_LIBELLE_REFUSEE);
    ec.deleteObject(alerte);
    try {
	    UtilDb.save(ec, true);
      transactionOK = true;
    } catch (Throwable e) {
      e.printStackTrace();
    }
  
    if (transactionOK)
      LRLog.log(logBuffer.toString());
    
    return transactionOK;
  }
  
  
  /**
   * Le responsable vise la demande
   */
  public boolean accepteVisa(EOEditingContext ec, EOAlerte alerte, CngUserInfo uiResp) {
    boolean transactionOK = false;

    // determiner si le valideur est bien declare comme viseur
    if (!isResponsable(alerte, uiResp, ConstsDroit.DROIT_NIVEAU_VISA, true)) {
    	return false;
    }    

    StringBuffer logBuffer = new StringBuffer(uiResp.nomEtPrenom());
    logBuffer.append(" accepte visa");
    
    alerte.setFlagReponse(null);
    
    if (alerte.occupation() != null) {
    	// visa d'une occupation
      logBuffer.append(" occ. ").append(alerte.oidOccupation());
      logBuffer.append(" (").append(alerte.occupation().affectationAnnuelle().individu().nomComplet()).append(")");
     
      // on recupere la liste de toutes les occupations associees en base
      NSArray recsOccupation = EOOccupation.findOccupationsInContext(ec, alerte.occupation().affectationAnnuelle().individu(),
          alerte.occupation().affectationAnnuelle().structure(), alerte.occupation().typeOccupation(), alerte.occupation().flagNature(),
          alerte.occupation().dateDebut(), alerte.occupation().dateFin(), alerte.occupation().status());

      boolean isDemandeSuppression = alerte.occupation().status().equals(ConstsOccupation.CODE_EN_COURS_DE_SUPPRESSION);
      
      for (int i = 0; i < recsOccupation.count(); i++) {
        EOOccupation recOcc = (EOOccupation) recsOccupation.objectAtIndex(i);
        // si c'est une demande de suppression, on passe le flag a suppression visee
        // sinon visee simplement
        if (isDemandeSuppression) {
          recOcc.setStatus(ConstsOccupation.CODE_EN_COURS_DE_SUPPRESSION_VISEE);
        } else {
          recOcc.setStatus(ConstsOccupation.CODE_VISEE);
        }
        // enregistrement du viseur de l'occupation si pas deja fait
        if (EORepartValidation.individuHasViseOccupation(ec, recOcc, uiResp) == false) {
        	EORepartValidation.newRepartValidation(ec, uiResp, 
              recOcc.affectationAnnuelle(), recOcc, EORepartValidation.FLAG_VISEE_VALUE);
        }
      }
    } else {
    	EOAffectationAnnuelle recAffAnn = alerte.affectationAnnuelle();
    	// visa sur un planning
    	if (recAffAnn.isPlanningEnCoursDeModification()) {
    		recAffAnn.setStatusPlanning(ConstsPlanning.PLANNING_STATUT_EN_COURS_DE_MODIFICATION_VISE);
        logBuffer.append("modification planning reel ");
    	} else if (recAffAnn.isPlanningEnCoursDeValidation()) {
    		recAffAnn.setStatusPlanning(ConstsPlanning.PLANNING_STATUT_EN_COURS_DE_VALIDATION_VISE);
     		logBuffer.append("validation planning prev. ");
    	} 
      // enregistrement du viseur de l'occupation si pas deja fait
      if (EORepartValidation.individuHasVisePlanning(ec, recAffAnn, uiResp) == false) {
      	EORepartValidation.newRepartValidation(ec, uiResp, 
        		recAffAnn, null, EORepartValidation.FLAG_VISEE_VALUE);
      }
      logBuffer.append(" (").append(recAffAnn.individu().nomComplet()).append("/");
      logBuffer.append(recAffAnn.structure().libelleCourt()).append("/");
      logBuffer.append(recAffAnn.annee()).append(")");
    }
    
    alerte.sendMailAlerteTraitee(uiResp, ConstsAlerte.ALERTE_LIBELLE_VISEE);
   
    try {
	    UtilDb.save(ec, true);
      transactionOK = true;
    } catch (Throwable e) {
      e.printStackTrace();
    }
    
    if (transactionOK)
    	LRLog.log(logBuffer.toString());
    
    return transactionOK;
  }
  
  /**
   * Supprimer l'alerte associee a une occupation. On cherche egalement
   * a supprimer les alertes pour le record "a cheval" si l'occupation
   * est doublee
   */
  public boolean deleteAlertsForOccupation(EOEditingContext ec, EOOccupation occupation) {
    boolean transactionOK = false;
    
    // recherche des occupations "clones"
    NSArray recsOccupation = EOOccupation.findOccupationsInContext(ec, occupation.affectationAnnuelle().individu(),
        occupation.affectationAnnuelle().structure(), occupation.typeOccupation(), occupation.flagNature(), 
        occupation.dateDebut(), occupation.dateFin(), occupation.status());
   
    for (int i = 0; i < recsOccupation.count(); i++) {
      EOOccupation recOcc = (EOOccupation) recsOccupation.objectAtIndex(i);
      // recherche de l'alerte associee
      EOAlerte recAlert = (EOAlerte) fetchObject(ec, "Alerte", 
          newCondition("oidOccupation=%@", new NSArray(recOcc.valueForKey("oid"))));
      if (recAlert != null) {
        ec.deleteObject(recAlert);
        transactionOK = true;
      }
    }
   
    return transactionOK;
  }

  /**
   * Retrouver la personne associee a une adresse email
   */
  public CngUserInfo findUserForEmail(String userMail) {
  	CngUserInfo ui = null;
  	// On retrouve d'abord le domaine et le email de l'utilisateur
    if ((StringCtrl.normalize(userMail).length() == 0) ||
    		userMail.startsWith("@")) return null;
    if (userMail.endsWith("@"))
      userMail = userMail.substring(userMail.length()-1);
    int idxAt = userMail.indexOf("@");
    String mail, domaine;
    if (idxAt == -1) {
      mail = userMail;
      domaine = cngApp().config().stringForKey("GRHUM_DOMAINE_PRINCIPAL");
    } else {
      mail = userMail.substring(0, idxAt);
      domaine = userMail.substring(idxAt+1);
    }
    // On cherche d'abord mail@docmaine
    NSArray users = fetchArray("ul_Compte", 
    		newCondition(
    				"cptEmail caseInsensitiveLike %@ AND cptDomaine caseInsensitiveLike %@",
    				new NSArray(new String[] {mail, domaine})), null);
    // Sinon, on essaie login@domaine
    if (users.count() == 0) {
    	users = fetchArray("ul_Compte", 
    			newCondition(
    					"cptLogin caseInsensitiveLike %@ AND cptDomaine caseInsensitiveLike %@",
    					new NSArray(new String[] {mail, domaine})), null);
    }
    // La priorite la plus importante est a la fin de la liste
    if (users.count() > 0) {
    	LRUserInfoDB uidb = new LRUserInfoDB(this);
    	uidb.compteForLogin(
    			(String)((EOGenericRecord) users.lastObject()).valueForKey("cptLogin"), 
    			null, true);
    	ui = new CngUserInfo(droitBus(), preferenceBus(), editingContext(), uidb.persId());
    }
    // Sinon, rien trouve
    return ui;
  }
  
  /**
   * Retrouver une alerte d'apres un hashcode
   * @param attribute
   * @param hashCode
   */
  public EOAlerte findAlerteForHashCode(EOEditingContext ec, String attribute, String hashCode) {
  	return (EOAlerte) fetchObject(ec, "Alerte", newCondition(attribute+"='"+hashCode+"'", null));
  }
  
  
  /**
   * Envoyer l'accuse que l'operation s'est bien deroulee
   * a l'utilisateur.
   */
  public void sendMailOperationSuccessfull(String libelleAlert, String nomPrenom, 
  		String emailTo, boolean isAccept, boolean isVisa) {
		String content = "Votre mail demandant d" +
			(isAccept ? "'accepter" : "e refuser") + " " +
			(isVisa ? "le visa" : "la validation") + " de l'alerte : \n\"" +
			libelleAlert + "\"\n(planning de " + nomPrenom + ")" +
			" a été traité avec succes par l'application.";
		
		String title = "[Conges] Operation réussie";
		
	  if (cngDataCenter().cngApp().debug()) {
      title = "MODE TEST: " + title;
      content = "email a destination de : " + emailTo + "\n\n" + content;
      emailTo = cngDataCenter().cngApp().appAdminMail();
    }
		
		cngDataCenter().cngApp().mailBus().sendMail(
				cngApp().appAdminMail(), emailTo, null, title, content);    
  }
  
  
  // liens vers les autres bus de donnees
  
  private CngDroitBus droitBus() {
  	return cngDataCenter().droitBus();
  }
 
  private CngPreferenceBus preferenceBus() {
  	return cngDataCenter().preferenceBus();
  }

}
