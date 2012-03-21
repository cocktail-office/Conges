package fr.univlr.cri.conges;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.YCRIWebPage;
import fr.univlr.cri.conges.constantes.ConstsJour;
import fr.univlr.cri.conges.eos.modele.grhum.EOAdresse;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.I_ClasseMetierNotificationParametre;
import fr.univlr.cri.conges.objects.CetFactory;
import fr.univlr.cri.conges.objects.Parametre;
import fr.univlr.cri.conges.objects.Planning;
import fr.univlr.cri.conges.print.CngPdfBoxCtrl;
import fr.univlr.cri.conges.print.ConstsPrint;
import fr.univlr.cri.conges.print.PrintDemandeCET;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.util.wo5.DateCtrl;
import fr.univlr.cri.ycrifwk.utils.UtilDb;

/**
 * Page relative a la situation CET de l'agent connecte
 * 
 * @author ctarade
 */
public class PageCET 
	extends YCRIWebPage 
		implements I_ClasseMetierNotificationParametre {

  // -- variables entrantes --
	
  public Planning lePlanning;
  // indique s'il faut ignorer la methode isSaisieDemandeAutorisee
  public boolean isForceSaisieAutorisee = false;
	
	// bindings des options de l'ancien régime
	public Boolean isRenoncementAncienSysteme;
	public Integer demandeMaintienMinuteAncienSysteme;
	public Integer demandeIndemnisationJourAncienSysteme;
	public Integer demandeRafpJourAncienSysteme;
	public boolean isDemandeIndemnisationJourSelectedAncienSysteme;
  public boolean isDemandeTransfertRafpJourSelectedAncienSysteme;
	
	// bindings de l'epargne
  public Integer demandeEpargneJour;

  // binding des options
  public Integer demandeMaintienForceJour;
  public Integer demandeMaintienJour;
  public Integer demandeIndemnisationJour;
  public Integer demandeRafpJour;
  public boolean isDemandeMaintienJourSelected;
  public boolean isDemandeIndemnisationJourSelected;
  
  // variables statiques
  private static String messageEditionDemandeEpargneCet;
  
  public PageCET(WOContext context) {
    super(context);
  }
  
  /**
   * @see I_ClasseMetierNotificationParametre
   * @param parametre
   */
  public static void initStaticField(
  		Parametre parametre) {
  	if (parametre == Parametre.PARAM_MESSAGE_EDITION_DEMANDE_EPARGNE_CET) {
  		messageEditionDemandeEpargneCet = parametre.getParamValueString();
  	}
  }
  
  /**
   * Impression de la demande d'epargne CET
   */
  public class PdfBoxDemandeCETCtrl extends CngPdfBoxCtrl {

		public PdfBoxDemandeCETCtrl(Class aGenericSixPrintClass, EOEditingContext anEc) {
			super(aGenericSixPrintClass, anEc);
		}

		public NSDictionary buildDico() {
		  NSMutableDictionary dico = new NSMutableDictionary();

		  // message a afficher lorsque le fichier PDF est disponible
		  if (!StringCtrl.isEmpty(messageEditionDemandeEpargneCet)) {
			  dico.setObjectForKey(messageEditionDemandeEpargneCet, ConstsPrint.DICO_KEY_ENDING_MESSAGE);
		  }
		  
			// URL du logo
	    dico.setObjectForKey(app.mainLogoUrl(), ConstsPrint.XML_KEY_MAIN_LOGO_URL);
	 
	    // nom prenom civilité qualité
			dico.setObjectForKey(individu().nom(), 					ConstsPrint.XML_KEY_NOM_DEMANDEUR);
			dico.setObjectForKey(individu().prenom(), 			ConstsPrint.XML_KEY_PRENOM_DEMANDEUR);
	    dico.setObjectForKey(individu().cCivilite(), 		ConstsPrint.XML_KEY_CIVILITE);
	    String strQualite = ".......................";
	    if (!StringCtrl.isEmpty(individu().qualite())) {
	    	strQualite = StringCtrl.capitalizeWords(individu().qualite());
	    }
	    dico.setObjectForKey(strQualite, 								ConstsPrint.XML_KEY_QUALITE);
	    
	    // données RH 
	    NSArray affectations = (NSArray) affAnn().structure().listePeresToComposante().valueForKey(EOStructure.LIBELLE_LONG_KEY);
	    dico.setObjectForKey(affectations,							ConstsPrint.XML_KEY_AFFECTATION_LISTE);
	    if (CetFactory.isAppVerifierStatutDemandeEpargneCet()) {
		    String grade = individu().getLibelleGradeForIndividu(affAnn());
		    dico.setObjectForKey(grade,											ConstsPrint.XML_KEY_GRADE_DEMANDEUR);
	    }
	    
	    // adresse de l'etablissement
	    EOStructure etablissement = EOStructure.getStructureRacineInContext(edc);
	    dico.setObjectForKey(etablissement.libelleLong(),		ConstsPrint.XML_KEY_ETAB_ADRESSE_LIBELLE);
	    EOAdresse adresse = EOAdresse.getAdresseRacineInContext(edc);
	    String adrAdresse1 = "";
	    if (!StringCtrl.isEmpty(adresse.adrAdresse1())) {
	    	adrAdresse1 = adresse.adrAdresse1();
	    }
	    String adrAdresse2 = "";
	    if (!StringCtrl.isEmpty(adresse.adrAdresse2())) {
	    	adrAdresse2 = adresse.adrAdresse2();
	    }
	    String codePostalVille = "";
	    if (!StringCtrl.isEmpty(adresse.codePostal()) && !StringCtrl.isEmpty(adresse.ville())) {
	    	codePostalVille = adresse.codePostal() + " " + adresse.ville();
	    }
	    dico.setObjectForKey(adrAdresse1,				ConstsPrint.XML_KEY_ETAB_ADRESSE_ADRESSE_1);
	    dico.setObjectForKey(adrAdresse2,				ConstsPrint.XML_KEY_ETAB_ADRESSE_ADRESSE_2);
	    dico.setObjectForKey(codePostalVille,		ConstsPrint.XML_KEY_ETAB_ADRESSE_CP_VILLE);

	    // annees de references
	    String strAnneeCivileEnCours = DateCtrlConges.dateToString(
	    		DateCtrlConges.dateToDebutAnneeCivile(affAnn().dateDebutAnnee())).substring(6, 10);
	    dico.setObjectForKey(strAnneeCivileEnCours, 		ConstsPrint.XML_KEY_ANNEE_CIVILE_N);
	    dico.setObjectForKey(getStrAnneeUnivNm1(), 			ConstsPrint.XML_KEY_ANNEE_UNIV_N_M_1);
	    
	    
	    //
	    // -- spécifités de la demande d'épargne CET
	    //
	    
	    // - == les variables de base == -
	    
	    // maintenir des jours dans l'ancien CET O/N (possible une seule fois)
	    boolean isMaintienAncienCet = false;
	    if ((affAnn().toMouvementCetDemandeMaintienCetAncienRegime() != null && affAnn().toMouvementCetDemandeMaintienCetAncienRegime().mouvementMinutes().intValue() > 0) ||
	    		(affAnn().toMouvementCetDemandeTransfertRafpAncienRegime() != null && affAnn().toMouvementCetDemandeTransfertRafpAncienRegime().mouvementMinutes().intValue() > 0) ||
	    		(affAnn().toMouvementCetDemandeIndemnisationAncienRegime() != null && affAnn().toMouvementCetDemandeIndemnisationAncienRegime().mouvementMinutes().intValue() > 0)) {
	    	isMaintienAncienCet = true;
	    }
	    dico.setObjectForKey(
	    		isMaintienAncienCet ? "true" : "false",							ConstsPrint.XML_KEY_IS_MAINTIEN_ANCIEN_CET);
	    
	    // exercice du droit d'option si maintien ancien CET	
	    if (isMaintienAncienCet) {
	    	int demandeTransfertRafpEnJourA7h00AncienSysteme = affAnn().toMouvementCetDemandeTransfertRafpAncienRegime().mouvementJoursA7h00Arrondi().intValue();
	    	int demandeIndemnisationEnJourA7h00AncienSysteme = affAnn().toMouvementCetDemandeIndemnisationAncienRegime().mouvementJoursA7h00Arrondi().intValue();
	    	float demandeMaintienCetEnJourA7h00AncienSysteme = affAnn().toMouvementCetDemandeMaintienCetAncienRegime().mouvementJoursA7h00().floatValue();
	    	float totalEnJourA7h00 = 
	    		(float) demandeTransfertRafpEnJourA7h00AncienSysteme + 
	    		(float) demandeIndemnisationEnJourA7h00AncienSysteme +
	    		demandeMaintienCetEnJourA7h00AncienSysteme;
	    	      	
	    	dico.setObjectForKey(
	    			new Float(totalEnJourA7h00), 																					ConstsPrint.XML_KEY_DEMANDE_TOTAL_OPTIONS_EN_JOURS_7H00_ANCIEN_SYSTEME);
	    	dico.setObjectForKey(
	    			new Integer(demandeTransfertRafpEnJourA7h00AncienSysteme), 		ConstsPrint.XML_KEY_DEMANDE_TRANSFERT_RAFP_EN_JOURS_7H00_ANCIEN_SYSTEME);
	     	dico.setObjectForKey(
	    			new Integer(demandeIndemnisationEnJourA7h00AncienSysteme), 		ConstsPrint.XML_KEY_DEMANDE_INDEMNISATION_EN_JOURS_7H00_ANCIEN_SYSTEME);
	     	dico.setObjectForKey(
	    			new Float(demandeMaintienCetEnJourA7h00AncienSysteme), 						ConstsPrint.XML_KEY_DEMANDE_MAINTIEN_CET_EN_JOURS_7H00_ANCIEN_SYSTEME);
	
	    	String demandeTransfertRafpEnHeuresAncienSysteme = affAnn().toMouvementCetDemandeTransfertRafpAncienRegime().mouvementHeures();
	    	String demandeIndemnisationEnHeuresAncienSysteme = affAnn().toMouvementCetDemandeIndemnisationAncienRegime().mouvementHeures();
	    	String demandeMaintienCetEnHeuresAncienSysteme 	 = affAnn().toMouvementCetDemandeMaintienCetAncienRegime().mouvementHeures();
	    	String totalEnHeures			 = 
	    		TimeCtrl.stringForMinutes(
	    				TimeCtrl.getMinutes(demandeTransfertRafpEnHeuresAncienSysteme) + 
	    				TimeCtrl.getMinutes(demandeIndemnisationEnHeuresAncienSysteme) + 
	    				TimeCtrl.getMinutes(demandeMaintienCetEnHeuresAncienSysteme));
	     	
	    	dico.setObjectForKey(
	    			totalEnHeures, 																	ConstsPrint.XML_KEY_DEMANDE_TOTAL_OPTIONS_EN_HEURES_ANCIEN_SYSTEME);
	    	dico.setObjectForKey(
	    			demandeTransfertRafpEnHeuresAncienSysteme, 			ConstsPrint.XML_KEY_DEMANDE_TRANSFERT_RAFP_EN_HEURES_ANCIEN_SYSTEME);
	     	dico.setObjectForKey(
	     			demandeIndemnisationEnHeuresAncienSysteme, 			ConstsPrint.XML_KEY_DEMANDE_INDEMNISATION_EN_HEURES_ANCIEN_SYSTEME);
	     	dico.setObjectForKey(
	     			demandeMaintienCetEnHeuresAncienSysteme, 				ConstsPrint.XML_KEY_DEMANDE_MAINTIEN_CET_EN_HEURES_ANCIEN_SYSTEME);
	    }

	    
	    // renoncer O/N à ses jours de l'ancien CET
	    boolean isRenoncement = false;
	    if (affAnn().toMouvementCetDemandeTransfertRegimePerenne() != null && 
	    		affAnn().toMouvementCetDemandeTransfertRegimePerenne().mouvementMinutes().intValue() > 0) {
	    	isRenoncement = true;
	    }
	    dico.setObjectForKey(
	    		isRenoncement ? "true" : "false",			ConstsPrint.XML_KEY_IS_RENONCEMENT);
	    
	    
	    // valeur du transfert si renoncement
	    int transfertEnMinutes = 0;
	    if (isRenoncement) {
	    	transfertEnMinutes = individu().toCET().minutesRestantesAncienRegime();
	    }
	 
	    // valeur de l'epargne demandée sur les reliquats de congés N-1
	    boolean isEpargne = false;
	    if (affAnn().toMouvementCetDemandeEpargne() != null &&
	    		affAnn().toMouvementCetDemandeEpargne().mouvementMinutes().intValue() > 0) {
	    	isEpargne = true;
	    }
	    int epargneEnMinutes 	= affAnn().toMouvementCetDemandeEpargne().mouvementMinutes().intValue();
	    dico.setObjectForKey(
	    		isEpargne ? "true" : "false", 											ConstsPrint.XML_KEY_IS_EPARGNE);
	    
	    // exercice du droit d'option O/N
	    boolean isExerciceDroitOption = cetFactory().isOptionPossible(epargneEnMinutes + transfertEnMinutes);
	    dico.setObjectForKey(
	    		isExerciceDroitOption ? "true" : "false", 					ConstsPrint.XML_KEY_IS_EXERCICE_DROIT_OPTION);
	    
	    // -- EPARGNE OU RENONCEMENT : tableau d'alimentation --
	    

	    // solde avant demande
	    int soldeCetAvantDemandeEnMinutes = 0;
	    if (individu().toCET() != null) {
	    	soldeCetAvantDemandeEnMinutes = individu().toCET().minutesRestantesRegimePerenne(affAnn().dateDebutAnnee());
	    }

	    // tableau récapitulatif d'alimentation
	    if (isRenoncement || isEpargne) {

		    float soldeCetAvantDemandeEnJour7h00 = ((float) soldeCetAvantDemandeEnMinutes) / ConstsJour.DUREE_JOUR_7H00;
		    dico.setObjectForKey(
		    		new Float(soldeCetAvantDemandeEnJour7h00), 											ConstsPrint.XML_KEY_SOLDE_CET_AVANT_DEMANDE_EN_JOURS_7H00);
		   
		    // montant total de l'épargne demandée
		    if (isEpargne) {
			    dico.setObjectForKey(
			    		affAnn().toMouvementCetDemandeEpargne().mouvementJoursA7h00Arrondi(), ConstsPrint.XML_KEY_TOTAL_EPARGNE_DEMANDEE_EN_JOURS_7H00);
		    }
		    
		    // droits à congés année N-1
		    float droitCongesAnneePrecedenteEnJour7h00 = cetFactory().droitCongesAnneePrecedenteEnJour7h00();
		    String droitCongesAnneePrecedenteEnHeures 	= cetFactory().droitCongesAnneePrecedenteEnHeures();
		    dico.setObjectForKey(
		    		new Float(droitCongesAnneePrecedenteEnJour7h00), 					ConstsPrint.XML_KEY_DROIT_A_CONGES_N_M_1_EN_JOURS_7H00);
		    dico.setObjectForKey(
		    		droitCongesAnneePrecedenteEnHeures, 											ConstsPrint.XML_KEY_DROIT_A_CONGES_N_M_1_EN_HEURES);
		    
		    // consommation conges annee N-1
		    float consommationCongesAnneePrecedenteEnJour7h00 = cetFactory().consommationCongesAnneePrecedenteEnJour7h00();
		    String consommationCongesAnneePrecedenteEnHeures 	= cetFactory().consommationCongesAnneePrecedenteEnHeures();
		    dico.setObjectForKey(
		    		new Float(consommationCongesAnneePrecedenteEnJour7h00), 					ConstsPrint.XML_KEY_CONSOMMATION_CONGES_N_M_1_EN_JOURS_7H00);
		    dico.setObjectForKey(
		    		consommationCongesAnneePrecedenteEnHeures, 													ConstsPrint.XML_KEY_CONSOMMATION_CONGES_N_M_1_EN_HEURES);
		    
		    // reliquats
		    float reliquatInitialEnJour7h00 = affAnn().reliquatInitialEnJour7h00();
		    String reliquatInitialEnHeure		= affAnn().reliquatInitialEnHeures();
		    dico.setObjectForKey(
		    		new Float(reliquatInitialEnJour7h00), 					ConstsPrint.XML_KEY_RELIQUATS_N_M_1_EN_JOURS_7H00);
		    dico.setObjectForKey(
		    		reliquatInitialEnHeure, 												ConstsPrint.XML_KEY_RELIQUATS_N_M_1_EN_HEURES);

		    // valeur du transfert en régime pérenne si renoncement
		    if (isRenoncement) {
			    Float transfertEnJour7h00 = affAnn().toMouvementCetDemandeTransfertRegimePerenne().mouvementJoursA7h00();
			    String transfertEnHeures = affAnn().toMouvementCetDemandeTransfertRegimePerenne().mouvementHeures();
			    dico.setObjectForKey(
			    		transfertEnJour7h00, 												ConstsPrint.XML_KEY_TRANSFERT_EN_JOURS_7H00);
			    dico.setObjectForKey(
			    		transfertEnHeures, 													ConstsPrint.XML_KEY_TRANSFERT_EN_HEURES);
		    }
		    
		    // valeur de l'epargne demandée
		    if (isEpargne) {
			    float epargneDemandeEnJour7h00 = (float) affAnn().toMouvementCetDemandeEpargne().mouvementJoursA7h00Arrondi();
			    String epargneDemandeEnHeures = affAnn().toMouvementCetDemandeEpargne().mouvementHeures();
			    dico.setObjectForKey(
			    		new Integer((int) epargneDemandeEnJour7h00), 			ConstsPrint.XML_KEY_DEMANDE_EPARGNE_EN_JOURS_7H00);
			    dico.setObjectForKey(
			    		epargneDemandeEnHeures, 													ConstsPrint.XML_KEY_DEMANDE_EPARGNE_EN_HEURES);
		    }
		    
		    // soldes après versements
		    int soldeCetApresDemandeEpargneEnMinutes = soldeCetAvantDemandeEnMinutes;
		    if (isRenoncement) {
		    	soldeCetApresDemandeEpargneEnMinutes += affAnn().toMouvementCetDemandeTransfertRegimePerenne().mouvementMinutes().intValue();
		    }
		    if (isEpargne) {
		    	soldeCetApresDemandeEpargneEnMinutes += affAnn().toMouvementCetDemandeEpargne().mouvementMinutes().intValue();
		    }
		    
		    float soldeCetApresDemandeEpargneEnJour7h00 = (float) soldeCetApresDemandeEpargneEnMinutes / (float) ConstsJour.DUREE_JOUR_7H00;
		    String soldeCetApresDemandeEpargneEnHeures = TimeCtrl.stringForMinutes(soldeCetApresDemandeEpargneEnMinutes);
		    dico.setObjectForKey(
		    		new Float(soldeCetApresDemandeEpargneEnJour7h00), 			ConstsPrint.XML_KEY_SOLDE_CET_APRES_DEMANDE_EN_JOURS_7H00);
		    dico.setObjectForKey(
		    		soldeCetApresDemandeEpargneEnHeures, 										ConstsPrint.XML_KEY_SOLDE_CET_APRES_DEMANDE_EN_HEURES);
		    
	    }
	    
	    
	    // -- EXERCICE DU DROIT D'OPTION --
	  
	    // verifier si son solde final donnerait lieu a faire les options
	    
	    if (isExerciceDroitOption) {
	    	
	    	String strDateAnneeCivileN = DateCtrlConges.dateToString(DateCtrlConges.dateToDebutAnneeCivile(affAnn().dateFinAnnee()));
	    	
	    	dico.setObjectForKey(
	    			strDateAnneeCivileN, 	ConstsPrint.XML_KEY_DATE_ANNEE_CIVILE_N);
	    	
	    	int valeurEpargneSoumiseAOptionEnJourA7h00Arrondi = cetFactory().depassementSeuil20JoursPourEpargneCetEnJours7h00Arrondi(
	    			epargneEnMinutes + transfertEnMinutes);
	    	int demandeTransfertRafpEnJourA7h00 = affAnn().toMouvementCetDemandeTransfertRafp().mouvementJoursA7h00Arrondi();
	    	int demandeIndemnisationEnJourA7h00 = affAnn().toMouvementCetDemandeIndemnisation().mouvementJoursA7h00Arrondi();
	    	int demandeMaintienCetEnJourA7h00 	= affAnn().toMouvementCetDemandeMaintienCet().mouvementJoursA7h00Arrondi();
		    	
	    	dico.setObjectForKey(
	    			new Integer(valeurEpargneSoumiseAOptionEnJourA7h00Arrondi), 	ConstsPrint.XML_KEY_DEMANDE_TOTAL_OPTIONS_EN_JOURS_7H00);
	    	dico.setObjectForKey(
	    			new Integer(demandeTransfertRafpEnJourA7h00), 												ConstsPrint.XML_KEY_DEMANDE_TRANSFERT_RAFP_EN_JOURS_7H00);
	     	dico.setObjectForKey(
	    			new Integer(demandeIndemnisationEnJourA7h00), 												ConstsPrint.XML_KEY_DEMANDE_INDEMNISATION_EN_JOURS_7H00);
	     	dico.setObjectForKey(
	    			new Integer(demandeMaintienCetEnJourA7h00), 													ConstsPrint.XML_KEY_DEMANDE_MAINTIEN_CET_EN_JOURS_7H00);
	    	
	    }
	    
	    // soldes après épargne et option
	    int soldeCetApresDemandeEpargneEtOptionEnMinutes = 
	    	soldeCetAvantDemandeEnMinutes
	    	+ transfertEnMinutes
	    	+ affAnn().toMouvementCetDemandeEpargne().mouvementMinutes().intValue()
	    	- affAnn().toMouvementCetDemandeIndemnisation().mouvementMinutes().intValue()
	    	- affAnn().toMouvementCetDemandeTransfertRafp().mouvementMinutes().intValue();

	    float soldeCetApresDemandeEpargneEtOptionEnJour7h00 = (float) soldeCetApresDemandeEpargneEtOptionEnMinutes / (float) ConstsJour.DUREE_JOUR_7H00;
	    dico.setObjectForKey(
	    		new Float(soldeCetApresDemandeEpargneEtOptionEnJour7h00), 	ConstsPrint.XML_KEY_SOLDE_CET_APRES_EPARGNE_ET_DECISION_EN_JOURS_7H00);
	    
	    // -- bas de page
	    
	    // ville
	    dico.setObjectForKey(app.appGrhumVille(), 			ConstsPrint.XML_KEY_VILLE);
	    
	    // date impression
	    String strdateImpression = DateCtrl.dateToString(DateCtrl.now());
	    // cas particulier pour la première année : vu le retard, on positionne
	    // les demandes de 2009/2010 au 31/12/2009
	    if (DateCtrlConges.isSameDay(
	    			DateCtrlConges.dateToDebutAnneeUniv(DateCtrlConges.stringToDate("31/12/2009")), affAnn().dateDebutAnnee())) {
	    	strdateImpression = "31/12/2009";
	    }
	    
			dico.setObjectForKey(strdateImpression, 				ConstsPrint.XML_KEY_DATE_IMPRESSION);
		
		  // le titre du document pour la demande sur régime pérenne
		  StringBuffer sbTitle = new StringBuffer();
		  if (isRenoncement) {
		  	if (!isEpargne && !isExerciceDroitOption) {
		  		sbTitle.append("transfert d'un CET crédité au 31/12/2008 sur le CET dispositif pérenne en vigueur à compter du 01/01/2009");
		  	} else {
		  		sbTitle.append("fermeture d'un CET crédité au 31/12/2008 et transfert des jours inscrits sur le CET ");
		  		sbTitle.append("dispositif pérenne en vigueur à compter du 01/01/2009");
		  	}
		  }
		  
		  // 
		  boolean isOuverture = true;
		  if (individu().toCET() != null) {
		  	isOuverture = false;
		  }
	
		  if (isOuverture || isEpargne) {
		  	if (sbTitle.length() > 0) {
		  		sbTitle.append(" + ");
		  	}
		  	sbTitle.append("demande ");
		  	if (isOuverture) {
		  		sbTitle.append("d'ouverture ");
		  	}
		  	if (isOuverture && isEpargne) {
		  		sbTitle.append("et ");
		  	}
		  	if (isEpargne) {
			  	sbTitle.append("d'alimentation cet (congés acquis au titre de l'année universitaire ");
			  	sbTitle.append(getStrAnneeUnivNm1());
			  	sbTitle.append(")");
		  	}
		  }
		  
		  /*
		  if (isEpargne) {
		  	if (sbTitle.length() > 0) {
		  		sbTitle.append(" + demande ");
		  	} else {
		  		sbTitle.append("demande d'ouverture et ");
		  	}
		  	sbTitle.append("d'alimentation cet (congés acquis au titre de l'année universitaire ");
		  	sbTitle.append(getStrAnneeUnivNm1());
		  	sbTitle.append(")");
		  }*/
		  
		  if (isExerciceDroitOption) {
		  	if (sbTitle.length() > 0) {
		  		sbTitle.append(" + ");
		  	}
		  	sbTitle.append("exercice du droit d'option");
		  }
		  
		  dico.setObjectForKey(sbTitle.toString().toUpperCase(), 				ConstsPrint.XML_KEY_TITRE);
	  
		  
		  // la suite de la phrase 'Accord du président de l'université concernant ...'
		  StringBuffer sbSuffixAccordPresident = new StringBuffer();
		  if (isRenoncement) {
		  	sbSuffixAccordPresident.append("le transfert des jours du CET crédité au 31/12/2008");
		  }
		  
		  if (isEpargne) {
		  	if (sbSuffixAccordPresident.length() > 0) {
		  		if (isExerciceDroitOption) {
			  		sbSuffixAccordPresident.append(", ");
		  		} else {
			  		sbSuffixAccordPresident.append(" et ");
		  		}
		  	}
		  	sbSuffixAccordPresident.append("le versement de congés acquis au titre de l'année universitaire ");
		  	sbTitle.append(getStrAnneeUnivNm1());
		  }
		  
		  if (isExerciceDroitOption) {
		  	if (sbSuffixAccordPresident.length() > 0) {
		  		sbSuffixAccordPresident.append(" et ");
		  	}
		  	sbSuffixAccordPresident.append("l'exercice du droit d'option");
		  }
		  
		  dico.setObjectForKey(sbSuffixAccordPresident.toString(), 				ConstsPrint.XML_KEY_SUFFIXE_PHRASE_ACCORD_PRESIDENT);
		  
		  // la suite de la phrase 'Solde du CET après ...'
		  StringBuffer sbSuffixSoldeCetFinal = new StringBuffer();
		  if (isRenoncement || isEpargne) {
		  	sbSuffixSoldeCetFinal.append("versement");
		  }
		  
		  if (isExerciceDroitOption) {
		  	if (sbSuffixSoldeCetFinal.length() > 0) {
		  		sbSuffixSoldeCetFinal.append(" et ");
		  	}
		  	sbSuffixSoldeCetFinal.append("exercice du droit d'option");
		  }
			 
		  dico.setObjectForKey(sbSuffixSoldeCetFinal.toString(), 				ConstsPrint.XML_KEY_SUFFIXE_PHRASE_SOLDE_CET_FINAL);
		  
		  
	    return dico;
		}
		
		public String fileName() {
			return "DemandeCET_"+StringCtrl.toBasicString(individu().nom()) + "_"	+
				StringCtrl.toBasicString(individu().prenom()) + "_" +
				StringCtrl.replace(DateCtrlConges.anneeUnivForDate(affAnn().dateDebutAnnee()), "/", "_");
		}
  }
  
  /** */
  public PdfBoxDemandeCETCtrl ctrlDemandeEpargneCet() {
  	return new PdfBoxDemandeCETCtrl(PrintDemandeCET.class, edc);
  }

  
  /**
   * quelques methodes pour alleger le code
   */  
  public Planning planning() 								{ return lePlanning; }
  public EOAffectationAnnuelle affAnn()			{	return planning().affectationAnnuelle(); }
  private CetFactory cetFactory()						{	return affAnn().cetFactory(); }
  private EOIndividu individu()							{	return affAnn().individu(); }

  
  // doc sur le CET
  
  private NSArray _arrNoteCetUrl;
  public String urlItem;
  
  public NSArray getArrNoteCetUrl() {
  	if (_arrNoteCetUrl == null) {
  		_arrNoteCetUrl = NSArray.componentsSeparatedByString(app.appNoteCetUrl(), "|");
  	}
  	return _arrNoteCetUrl;
  }
  
  /**
   * Le nom du fichier pointé par l'URL
   * @return
   */
  public String getUrlItemLibelle() {
  	return urlItem.substring(urlItem.lastIndexOf("/")+1, urlItem.length());
  }
  
  public boolean isLinkNoteCETExists() {
    return !StringCtrl.isEmpty(app.appNoteCetUrl());
  }
  
  
  // gestion de l'affichage des formulaires
  // de demande d'épargne et d'option
  
  
  /**
   * Indiquer si la saisie est autorisée :
   * - oui si {@link #isForceSaisieAutorisee} est a <code>true</code>
   * - sinon uniquement les personnes autorisées et en période d'ouverture
   */
  public boolean isSaisieDemandeAutorisee() {
  	return isForceSaisieAutorisee || (
  			(cngUserInfo().isAllowedModifyPlanning(affAnn()) && cetFactory().isPeriodeDemandeEpargneCet()));
  }
  

  /**
   * Faut-il afficher le formulaire de demande d'option CET
   * sur l'ancien régime
   */
  public boolean isShowFormulaireDemandeOptionCetAncienSysteme() {
  	boolean showFormulaireDemandeOptionCetAncienSysteme = false;
  	
  	// test si la personne connectée est la titulaire du planning
  	// (l'admin peut le faire pour tout le monde)
  	if (cngUserInfo().isAdmin() || planning().isConnectedOwner()) {
  		showFormulaireDemandeOptionCetAncienSysteme = true;
  	}
  	
  	if (showFormulaireDemandeOptionCetAncienSysteme) {

  		showFormulaireDemandeOptionCetAncienSysteme = false;

  		// verifier s'il remplis les conditions
  		if (cetFactory().isDemandePassageRegimePerenneFaisable(isForceSaisieAutorisee)) {
  			showFormulaireDemandeOptionCetAncienSysteme = true;
  		}
  		
  	}
  	
  	return showFormulaireDemandeOptionCetAncienSysteme;
  }
  
  /**
   * Faut-il afficher l'un ou l'autre des formulaires du régime
   * pérenne sur la demande d'épargne ou du droit d'option
   */
  public boolean isShowFormulairesRegimePerenne() {
  	boolean showFormulairesRegimePerenne = false;
  	

  	// test si la personne connectée est la titulaire du planning
  	// (l'admin peut le faire pour tout le monde)
  	if (cngUserInfo().isAdmin() || planning().isConnectedOwner()) {
  		showFormulairesRegimePerenne = true;
  	}
  	
  	if (showFormulairesRegimePerenne) {
  		
  		showFormulairesRegimePerenne = false;
  		
  		// verifier s'il doit statuer sur un CET ancien 
  		// et si oui, controlers si celle ci est prise ou pas
  		if (cetFactory().isDemandePassageRegimePerenneFaisable(isForceSaisieAutorisee)) {
  			
  			if (isRenoncementAncienSysteme != null) {
  				if (isRenoncementAncienSysteme == Boolean.TRUE ||
  						(isRenoncementAncienSysteme == Boolean.FALSE && 
  								isDemandeIndemnisationJourSelectedAncienSysteme && isDemandeTransfertRafpJourSelectedAncienSysteme)) {
  					showFormulairesRegimePerenne = true;
  				}
  			}
  		} else {
  			// pas de CET, voir s'il peut faire une épargne  			
  			// ou s'il doit faire une option dans le cas ou il n'epargne pas
  			if (cetFactory().isEpargneFaisable(isForceSaisieAutorisee) ||
  					cetFactory().isOptionFaisable(0, isForceSaisieAutorisee)) {
  				showFormulairesRegimePerenne = true;
  			}
  		}
  		
  	}
  	
  	return showFormulairesRegimePerenne;
  }
  
  /**
   * Faut-il afficher le formulaire de demande d'épargne CET
   */
  public boolean isShowFormulaireDemandeEpargneCet() {
  	boolean showFormulaireDemandeEpargneCet = false;
  	
  	// test si la personne connectée est la titulaire du planning
  	// (l'admin peut le faire pour tout le monde)
  	if (cngUserInfo().isAdmin() || planning().isConnectedOwner()) {
  		showFormulaireDemandeEpargneCet = true;
  	}
  	
  	// test si son etat CET et statut le permet
  	if (showFormulaireDemandeEpargneCet &&
  			cetFactory().isEpargneFaisable(isForceSaisieAutorisee)) {
  		showFormulaireDemandeEpargneCet = true;
  	}
  	
  	return showFormulaireDemandeEpargneCet;
  }
  
  /**
   * Faut-il afficher le formulaire de demande d'option CET
   * sur le régime pérenne
   */
  public boolean isShowFormulaireDemandeOptionCet() {
  	boolean showFormulaireDemandeOptionCet = false;
  	
  	// test si la personne connectée est la titulaire du planning
  	// (l'admin peut le faire pour tout le monde)
  	if (cngUserInfo().isAdmin() || planning().isConnectedOwner()) {
  		showFormulaireDemandeOptionCet = true;
  	}
  	
  	if (showFormulaireDemandeOptionCet) {

			showFormulaireDemandeOptionCet = false;
  		// 2 cas
    	
    	// 1 : il ne peut pas faire de demande d'épargne, alors
    	// on test s'il a de l'éxcedent
  		if (!isShowFormulaireDemandeEpargneCet()) { 
  			
  			int demandeEpargneMinutes = (demandeEpargneJour != null ? demandeEpargneJour.intValue() * ConstsJour.DUREE_JOUR_7H00 : 0);
  			
  			if (cetFactory().isOptionFaisable(demandeEpargneMinutes + getTransfertAncienSystemeMinutes(), isForceSaisieAutorisee)) {
    			showFormulaireDemandeOptionCet = true;
  			}
  		} else {
  			// 2 : il peut faire une demande d'épargne, alors 
  			// on attend qu'il ait saisi quelque chose, et si 
  			// ensuite ce quelque chose lui ouvre le droit à option
  			if (demandeEpargneJour != null) {
  				
  				int demandeEpargneMinutes = demandeEpargneJour.intValue() * ConstsJour.DUREE_JOUR_7H00;
  				
  				// s'il veut faire une demande d'épargne, alors on vérifie 
  				// s'il a de l'éxcédent
  				if (cetFactory().isOptionFaisable(demandeEpargneMinutes + getTransfertAncienSystemeMinutes(), isForceSaisieAutorisee)) {
  					showFormulaireDemandeOptionCet = true;
  				}
  			}
  		}
  	}
  	
  	return showFormulaireDemandeOptionCet;
  }

  /**
   * Faut-il afficher le bouton d'enregistrement de la demande
   */
  public boolean isShowBtnValiderDemande() {
  	boolean showBtnValiderDemande = false;
  	
  	// si demande d'épargne
  	if (isShowFormulaireDemandeEpargneCet()) {
  		
  		// est-ce qu'une valeur a été selectionnée
  		if (demandeEpargneJour != null) {
  			showBtnValiderDemande = true;
  		}
  		
  		// décision à prendre ?
  		if (showBtnValiderDemande) {
  			int demandeEpargneMinutes = demandeEpargneJour.intValue() * ConstsJour.DUREE_JOUR_7H00;

  			boolean isOptionFaisable = cetFactory().isOptionFaisable(
  					demandeEpargneMinutes + getTransfertAncienSystemeMinutes(), isForceSaisieAutorisee);
  			
  			// décisions choisies ?
  			if (isOptionFaisable) {
  				if (!isDemandeIndemnisationJourSelected || !isDemandeIndemnisationJourSelected) {
  					showBtnValiderDemande = false;
  				}
  			}
  		}
  				
  	}
  	
  	return showBtnValiderDemande;
  }
  
  
  // etapes de validation
 
  /**
   * validation des demandes d'épargne de CET et enregistrement du choix des options
   * @return
   */
  public WOComponent doValiderDemande() {
  	
  	//
  	// -- données concernant l'ancien systeme --
  	//
  	if (isShowFormulaireDemandeOptionCetAncienSysteme()) {
  	

  		if (isRenoncementAncienSysteme == Boolean.TRUE) {	
  			// renoncement - on indique le transfert intégral
  			// et on indique explicitement que les options demandées sont à 0
  			// car le transfert en CET pérénne est intégral
   			cetFactory().doDemandeAncienRegime(
   					individu().toCET().minutesRestantesAncienRegime(), 0, 0, 0);

  		} else {
  			// non renoncement - on indique le transfert de zéro 
  		// et on détaille les options demandées
   			cetFactory().doDemandeAncienRegime(
   					0, 
   					demandeIndemnisationJourAncienSysteme.intValue() * ConstsJour.DUREE_JOUR_7H00,
   					demandeRafpJourAncienSysteme.intValue() * ConstsJour.DUREE_JOUR_7H00,
   					demandeMaintienMinuteAncienSysteme);
  			
  		}

  		
  	}
  	
  	//
  	// -- données concernant le régime pérenne --
  	//
  	
  	// pre-remplir les valeurs d'option si pas d'option a faire
  	if (!isShowFormulaireDemandeOptionCet()) {
  		// les jours forcés sont tous ceux épargnée
  		demandeMaintienForceJour = demandeEpargneJour;
  		// le reste est à 0
  		demandeMaintienJour = new Integer(0);
  		demandeIndemnisationJour = new Integer(0);
  		demandeRafpJour = new Integer(0);
  	}

  	// création des mouvements
  	cetFactory().doDemandeRegimePerenne(
  			demandeEpargneJour * ConstsJour.DUREE_JOUR_7H00,
  			demandeMaintienJour * ConstsJour.DUREE_JOUR_7H00,
  			demandeMaintienForceJour * ConstsJour.DUREE_JOUR_7H00,
  			demandeIndemnisationJour * ConstsJour.DUREE_JOUR_7H00,
  			demandeRafpJour * ConstsJour.DUREE_JOUR_7H00);
		
		// sauvegarde
		try {
			UtilDb.save(edc, false);
			// refaire les calculs par rapport à ce nouveau blocage (les reliquats sont
			// maintenant diminués de la valeur bloquée)
			planning().calculerOccupationChronologiques();
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
	
  	return null;
  }
  

  /**
   * L'année universitaire précedente au format XXXX/YYYY
   * @return
   */
  public String getStrAnneeUnivNm1() {
    String strAnneeUnivNm1 = DateCtrlConges.anneeUnivForDate(
    		affAnn().dateDebutAnnee().timestampByAddingGregorianUnits(0, 0, -1, 0, 0, 0));
    
    return strAnneeUnivNm1;
  }
  /**
   * L'année universitaire en cours au format XXXX/YYYY
   * @return
   */
  public String getStrAnneeUnivN() {
    String strAnneeUnivN = DateCtrlConges.anneeUnivForDate(
    		affAnn().dateDebutAnnee());
    
    return strAnneeUnivN;
  }
  
  /**
   * La date du jour
   * @return
   */
  public NSTimestamp getDateDuJour() {
  	return DateCtrlConges.now();
  }
  
  /**
   * Les minutes a transferer vers le régime pérénne
   * pour le composante {@link CetEpargne} si l'agent
   * a renoncé à l'ancien régime
   * @return
   */
  public int getTransfertAncienSystemeMinutes() {
  	int minutes = 0;

  	if (isRenoncementAncienSysteme == Boolean.TRUE && cetFactory().isCET31122008Restant()) {
  		minutes = affAnn().individu().toCET().minutesRestantesAncienRegime();
  	}

  	return minutes;
  }
  
  // setters

  /**
   * Lors du changement de planning, on remet à zéro
   * les selectionnes liées à la demande
   */
  public void setLePlanning(Planning value) {
  	Planning prevPlanning = lePlanning;
		lePlanning = value;
		if (lePlanning != prevPlanning) {
			isRenoncementAncienSysteme = null;
			demandeMaintienMinuteAncienSysteme = null;
			demandeIndemnisationJourAncienSysteme = null;
			demandeRafpJourAncienSysteme = null;
			setDemandeEpargneJour(null);
		}
	}
  
  /**
   * Lors du changement de la valeur épargnée, il faut 
   * remettre à zéro les selections du formulaire lié
   * aux option
   */
  public void setDemandeEpargneJour(Integer value) {
  	demandeEpargneJour = value;
  	demandeMaintienJour = null;
  	demandeIndemnisationJour = null;
  	demandeRafpJour = null;
  }

  /**
   * setter bidon
   * @see CetEpargne
   * @param value
   */
  public void setTransfertAncienSystemeMinutes(Integer value) {
  	
  }

}