/*
 * Copyright Universit� de La Rochelle 2004
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
package fr.univlr.cri.conges.print;

/**
 * Constantes pour l'ensemble des impressions
 * @author Cyril TARADE <cyril.tarade at univ-lr.fr>
 */
public interface ConstsPrint {

  
	// fichiers CSV
	
	// encodage du fichier cvs genere pour avoir un affichage correct des accents sous windows
	public final static String CSV_ENCODING = "ISO-8859-1";
	public final static String CSV_COLUMN_SEPARATOR = "\t";
	public final static String CSV_NEW_LINE = "\n";

	public final static String CSV_CONGES_SERVICE_CODE_TRAVAIL		             		= "T";
	public final static String CSV_CONGES_SERVICE_CODE_ABSENCE                   = "A";
	public final static String CSV_CONGES_SERVICE_CODE_ABSENCE_EN_COURS_VAL      = "V";
	public final static String CSV_CONGES_SERVICE_CODE_NON_TRAVAIL	             	= "N";

	//
	
	public final static String XSL_CONGES_SERVICE_CODE_TRAVAIL		             		= "T";
	public final static String XSL_CONGES_SERVICE_CODE_ABSENCE                   = "A";
	public final static String XSL_CONGES_SERVICE_CODE_ABSENCE_EN_COURS_VAL      = "V";
	public final static String XSL_CONGES_SERVICE_CODE_NON_TRAVAIL	             	= "N";
    
	public final static String XML_DEFAULT_PATH						= "/tmp/donnees.xml";

	public final static String XML_CONGES_SERVICE_DEMI_JOURN					= "/tmp/CongesServiceDemiJourn.xml";
	public final static String PDF_CONGES_SERVICE_DEMI_JOURN					=	 "/tmp/CongesServiceDemiJourn.pdf";
	public final static String ID_CONGES_SERVICE_DEMI_JOURN						= "CONGES_SERVICE_DEMI_JOURN";
  
	public final static String XML_CONGES_DEMANDE_CET               = "/tmp/CongesDemandeCET.xml";
	public final static String PDF_CONGES_DEMANDE_CET               = "/tmp/CongesDemandeCET.pdf";
	public final static String ID_CONGES_DEMANDE_CET                = "CONGES_DEMANDE_CET";
  
	public final static String XML_CONGES_DEM_GARDE_ENFANT          = "/tmp/CongesDemGardeEnfant.xml";
	public final static String PDF_CONGES_DEM_GARDE_ENFANT          = "/tmp/CongesDemGardeEnfant.pdf";
	public final static String ID_CONGES_DEM_GARDE_ENFANT           = "CONGES_DEM_GARDE_ENFANT";
  
	public final static String XML_CONGES_SITUATION_CET             = "/tmp/CongesSituationCET.xml";
	public final static String PDF_CONGES_SITUATION_CET             = "/tmp/CongesSituationCET.pdf";
	public final static String ID_CONGES_SITUATION_CET              = "CONGES_SITUATION_CET";
  
	public final static String XML_CONGES_LISTE_TRANSACTION_CET     = "/tmp/CongesListeTransactionCET.xml";
	public final static String PDF_CONGES_LISTE_TRANSACTION_CET     = "/tmp/CongesListeTransactionCET.pdf";
	public final static String ID_CONGES_LISTE_TRANSACTION_CET      = "CONGES_LISTE_TRANS_CET";
  
	public final static String XML_CONGES_LISTE_CET             			= "/tmp/CongesListeCET.xml";
	public final static String PDF_CONGES_LISTE_CET             			= "/tmp/CongesListeCET.pdf";
	public final static String ID_CONGES_LISTE_CET              			= "CONGES_LISTE_CET";

	public final static String XML_CONGES_HORAIRES             				= "/tmp/CongesHoraires.xml";
	public final static String PDF_CONGES_HORAIRES             				= "/tmp/CongesHoraires.pdf";
	public final static String ID_CONGES_HORAIRES              				= "CONGES_HORAIRES";

	// demande de conge papier
	public final static String XML_CONGES_DEM_CONGE_PAPIER     				= "/tmp/CongesDemCongePapier.xml";
	public final static String PDF_CONGES_DEM_CONGE_PAPIER     				= "/tmp/CongesDemCongePapier.pdf";
	public final static String ID_CONGES_DEM_CONGE_PAPIER      				= "CONGES_DEM_CONGE_PAPIER";
	public final static String XML_COMMENT_CONGES_DEM_CONGE_PAPIER		=	"Edition de la demande d'autorisation d'absence pour garde d'enfant";
	
	// reformulation de la demande CET de l'agent
	public final static String XML_CONGES_REFORM_DEMANDE_CET					= "/tmp/CongesReformulationDemandeCet.xml";
	public final static String PDF_CONGES_REFORM_DEMANDE_CET   				= "/tmp/CongesReformulationDemandeCet.pdf";
	public final static String ID_CONGES_REFORM_DEMANDE_CET    				= "CONGES_REFORM_DEMANDE_CET";
	
	
	
	// nom des variables
	
	public final static String XML_ELEMENT_DEMANDE = "demande";
	public final static String XML_ELEMENT_DEMANDES = "demandes";
	
	public final static String DEMANDE_ARRAY_DICO = "demandeArrayDico";
	
	public final static String XML_KEY_TYPE_OCCUPATION = "typeOccupation";
	public final static String XML_KEY_DATE_IMPRESSION = "dateImpression";
	public final static String XML_KEY_CONGES_RESTANTS_AVANT_DEMANDE_EN_JOURS 			= "congesRestantsAvantDemandeEnJours";
	public final static String XML_KEY_CONGES_RESTANTS_AVANT_DEMANDE_EN_HEURES 		= "congesRestantsAvantDemandeEnHeures";
	public final static String XML_KEY_CONGES_RESTANTS_APRES_DEMANDE_EN_JOURS 			= "congesRestantsApresDemandeEnJours";
	public final static String XML_KEY_CONGES_RESTANTS_APRES_DEMANDE_EN_HEURES 		= "congesRestantsApresDemandeEnHeures";
	public final static String XML_KEY_DUREE_JOURNEE = "dureeJournee";
	public final static String XML_KEY_DUREE_OCCUPATION = "dureeOccupation";
	public final static String XML_KEY_DATE_DEBUT_OCCUPATION = "dateDebutOccupation";
	public final static String XML_KEY_DATE_FIN_OCCUPATION = "dateFinOccupation";
	public final static String XML_KEY_MOTIF_OCCUPATION = "motifOccupation";
	public final static String XML_KEY_VISEURS = "viseurs";
	public final static String XML_KEY_VISEUR = "viseur";
	public final static String XML_KEY_RESPONSABLES = "responsables";
	public final static String XML_KEY_RESPONSABLE = "responsable";
	public final static String XML_KEY_SERVICE_LIBELLE_COURT = "serviceCourt";
	public final static String XML_KEY_SERVICE_LIBELLE_LONG = "serviceLong";
	
	public final static String XML_KEY_TITRE = "titre";
	
	public final static String XML_KEY_MAIN_LOGO_URL = "mainLogoUrl";

	public final static String XML_KEY_VILLE 										= "ville";

	
	// adresse etablissement
	
	public final static String XML_KEY_ETAB_ADRESSE_LIBELLE 	= "adrLibelle";
	public final static String XML_KEY_ETAB_ADRESSE_ADRESSE_1 = "adrAdresse1";
	public final static String XML_KEY_ETAB_ADRESSE_ADRESSE_2 = "adrAdresse2";
	public final static String XML_KEY_ETAB_ADRESSE_CP_VILLE 	= "codePostalVille";
	

	// identite
	
	public final static String XML_KEY_CIVILITE 								=	"civilite";
	public final static String XML_KEY_NOM_DEMANDEUR 						= "nomDemandeur";
	public final static String XML_KEY_PRENOM_DEMANDEUR 				= "prenomDemandeur";
	public final static String XML_KEY_QUALITE									=	"qualite";
	
	
	// données RH
	
	public final static String XML_KEY_GRADE_DEMANDEUR 					= "grade";
	public final static String XML_KEY_AFFECTATION_LISTE				= "listeAffectation";
	
	
	// demande d'alimentation cet
	
	public final static String XML_KEY_ANNEE_CIVILE_N																					= "anneeCivileN";
	public final static String XML_KEY_ANNEE_UNIV_N_M_1																				=	"anneeUnivNm1";
	
	public final static String XML_KEY_SOLDE_CET_AVANT_DEMANDE_EN_JOURS_7H00				=	"soldeCetAvantEpargneEnJours7h00";
	
	public final static String XML_KEY_TOTAL_EPARGNE_DEMANDEE_EN_JOURS_7H00					=	"totalEpargneDemandeeEnJours7h00";

	public final static String XML_KEY_IS_MAINTIEN_ANCIEN_CET																	=	"isMaintienAncienCet";
	public final static String XML_KEY_IS_RENONCEMENT 																				=	"isRenoncement";
	public final static String XML_KEY_IS_EPARGNE																							= "isEpargne";
	public final static String XML_KEY_IS_EXERCICE_DROIT_OPTION																=	"isExerciceDroitOption";
	
	public final static String XML_KEY_DROIT_A_CONGES_N_M_1_EN_JOURS_7H00 						= "droitACongesNm1EnJours7h00";
	public final static String XML_KEY_DROIT_A_CONGES_N_M_1_EN_HEURES 										= "droitACongesNm1EnHeures";
	public final static String XML_KEY_CONSOMMATION_CONGES_N_M_1_EN_JOURS_7H00 	= "consommationCongesNm1EnJours7h00";
	public final static String XML_KEY_CONSOMMATION_CONGES_N_M_1_EN_HEURES 					= "consommationCongesNm1EnHeures";
	public final static String XML_KEY_RELIQUATS_N_M_1_EN_JOURS_7H00 											= "reliquatsNm1EnJours7h00";
	public final static String XML_KEY_RELIQUATS_N_M_1_EN_HEURES 															= "reliquatsNm1EnHeures";
	public final static String XML_KEY_TRANSFERT_EN_JOURS_7H00 																= "transfertEnJours7h00";
	public final static String XML_KEY_TRANSFERT_EN_HEURES 																		= "transfertEnHeures";
	public final static String XML_KEY_DEMANDE_EPARGNE_EN_JOURS_7H00 											= "demandeEpargneEnJours7h00";
	public final static String XML_KEY_DEMANDE_EPARGNE_EN_HEURES 															= "demandeEpargneEnHeures";
	public final static String XML_KEY_SOLDE_CET_APRES_DEMANDE_EN_JOURS_7H00 			= "soldeCetApresEpargneEnJours7h00";
	public final static String XML_KEY_SOLDE_CET_APRES_DEMANDE_EN_HEURES 							= "soldeCetApresEpargneEnHeures";

	public final static String XML_KEY_DATE_ANNEE_CIVILE_N																		= "dateAnneeCivileN";

	public final static String XML_KEY_DEMANDE_TOTAL_OPTIONS_EN_JOURS_7H00_ANCIEN_SYSTEME						= "demandeTotalOptionsEnJours7h00AncienSysteme";
	public final static String XML_KEY_DEMANDE_TRANSFERT_RAFP_EN_JOURS_7H00_ANCIEN_SYSTEME					= "demandeTransfertRafpEnJours7h00AncienSysteme";
	public final static String XML_KEY_DEMANDE_INDEMNISATION_EN_JOURS_7H00_ANCIEN_SYSTEME						= "demandeIndemnisationEnJours7h00AncienSysteme";
	public final static String XML_KEY_DEMANDE_MAINTIEN_CET_EN_JOURS_7H00_ANCIEN_SYSTEME							= "demandeMaintienCetEnJours7h00AncienSysteme";

	public final static String XML_KEY_DEMANDE_TOTAL_OPTIONS_EN_HEURES_ANCIEN_SYSTEME						= "demandeTotalOptionsEnHeuresAncienSysteme";
	public final static String XML_KEY_DEMANDE_TRANSFERT_RAFP_EN_HEURES_ANCIEN_SYSTEME					= "demandeTransfertRafpEnHeuresAncienSysteme";
	public final static String XML_KEY_DEMANDE_INDEMNISATION_EN_HEURES_ANCIEN_SYSTEME						= "demandeIndemnisationEnHeuresAncienSysteme";
	public final static String XML_KEY_DEMANDE_MAINTIEN_CET_EN_HEURES_ANCIEN_SYSTEME							= "demandeMaintienCetEnHeuresAncienSysteme";

	
	public final static String XML_KEY_DEMANDE_TOTAL_OPTIONS_EN_JOURS_7H00						= "demandeTotalOptionsEnJours7h00";
	public final static String XML_KEY_DEMANDE_TRANSFERT_RAFP_EN_JOURS_7H00					= "demandeTransfertRafpEnJours7h00";
	public final static String XML_KEY_DEMANDE_INDEMNISATION_EN_JOURS_7H00						= "demandeIndemnisationEnJours7h00";
	public final static String XML_KEY_DEMANDE_MAINTIEN_CET_EN_JOURS_7H00							= "demandeMaintienCetEnJours7h00";

	public final static String XML_KEY_SOLDE_CET_APRES_EPARGNE_ET_DECISION_EN_JOURS_7H00 			= "soldeCetApresEpargneEtDecisionEnJours7h00";
	
	public final static String XML_KEY_SUFFIXE_PHRASE_ACCORD_PRESIDENT = "suffixePhraseAccordPresident";
	
	public final static String XML_KEY_SUFFIXE_PHRASE_SOLDE_CET_FINAL 	= "suffixePhraseSoldeCetFinal";

	
	// decision CET
	public final static String XML_KEY_DATE_DEMANDE						=	"dateDemande";
	public final static String XML_KEY_DATE_VALEUR_CET				=	"dateValeurCet";
	
	public final static String XML_KEY_SIGNATURE_PRESIDENT		=	"signaturePresident";
	public final static String XML_KEY_GRHUM_ETAB							=	"grhumEtab";
	public final static String XML_KEY_GRHUM_PRESIDENT				=	"grhumPresident";
	
	public final static String XML_KEY_DECISION_EPARGNE_EN_JOURS_7H00 																= "decisionEpargneEnJours7h00";
	public final static String XML_KEY_DECISION_TOTAL_OPTIONS_EN_JOURS_7H00											= "decisionTotalOptionsEnJours7h00";
	public final static String XML_KEY_DECISION_TOTAL_EN_JOURS_7H00																			=	"decisionTotalEnJours7h00";
	public final static String XML_KEY_SOLDE_CET_APRES_DECISION_EPARGNE_EN_JOURS_7H00	=	"soldeCetApresDecisionTotalEnJours7h00";
	
	
	// situation CET
	
	public final static String SITUATION_ARRAY_DICO																																										 = "situationArrayDico";
	
	public final static String XML_KEY_SITUATION_BLOC_ANCIEN_REGIME																																		= "blocAncienRegime";
	public final static String DICO_KEY_SITUATION_ARRAY_LIGNE_CREDIT_ANCIEN_REGIME																			= "arrayLigneCreditAncienRegime";

	public final static String XML_KEY_SITUATION_BLOC_REGIME_PERENNE																																	= "blocRegimePerenne";
	public final static String DICO_KEY_SITUATION_ARRAY_LIGNE_CREDIT_REGIME_PERENNE																		= "arrayLigneCreditRegimePerenne";

	public final static String XML_KEY_SITUATION_TRANSACTION																																						= "transaction";
	public final static String XML_KEY_SITUATION_TRANSACTION_LIBELLE																																	= "transactionLibelle";
	public final static String XML_KEY_SITUATION_TRANSACTION_SOLDE_INITIAL_EN_JOURS_7H00													= "transactionSoldeInitialEnJours7h00";
	public final static String XML_KEY_SITUATION_TRANSACTION_VALEUR_EN_JOURS_7H00				       									= "transactionValeurEnJours7h00";

	public final static String DICO_KEY_SITUATION_DICO_DEBITABLES																																				= "dicoDebitables";
	public final static String DICO_KEY_SITUATION_ARRAY_DEBITABLES																																			= "arrayDebitables";
	public final static String XML_KEY_SITUATION_BLOC_DEBITABLES																																				= "blocDebitables";

	public final static String DICO_KEY_SITUATION_SOLDE_INTERMEDIAIRE																																= "soldeIntermediaire";

	public final static String XML_KEY_SITUATION_LIGNE_DEBITABLE																																				= "ligneDebitable";
	public final static String XML_KEY_SITUATION_DEBITABLE_LIBELLE																																			= "debitableLibelle";
	public final static String XML_KEY_SITUATION_DEBITABLE_VALEUR_EN_JOURS_7H00																						= "debitableValeurEnJours7h00";
	public final static String XML_KEY_SITUATION_DEBITABLE_SOLDE_FINAL_EN_JOURS_7H00																	= "debitableSoldeFinalEnJours7h00";
	
	
	public final static String XML_KEY_SITUATION_SOLDE_FINAL_EN_JOURS_7H00																											= "soldeFinalEnJours7h00";
	
	public final static String XML_KEY_SITUATION_DATE_ARRET																																							= "dateArret";
	
	
	// messages
	
	
	public final static String DICO_KEY_ENDING_MESSAGE = "endingMessage";
	
}
