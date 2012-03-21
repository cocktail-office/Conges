/*
 * Copyright COCKTAIL (www.cocktail.org), 1995, 2010 This software
 * is governed by the CeCILL license under French law and abiding by the
 * rules of distribution of free software. You can use, modify and/or
 * redistribute the software under the terms of the CeCILL license as
 * circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * As a counterpart to the access to the source code and rights to copy, modify
 * and redistribute granted by the license, users are provided only with a
 * limited warranty and the software's author, the holder of the economic
 * rights, and the successive licensors have only limited liability. In this
 * respect, the user's attention is drawn to the risks associated with loading,
 * using, modifying and/or developing or reproducing the software by the user
 * in light of its specific status of free software, that may mean that it
 * is complicated to manipulate, and that also therefore means that it is
 * reserved for developers and experienced professionals having in-depth
 * computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling
 * the security of their systems and/or data to be ensured and, more generally,
 * to use and operate it in the same conditions as regards security. The
 * fact that you are presently reading this means that you have had knowledge
 * of the CeCILL license and that you accept its terms.
 */

package fr.univlr.cri.conges.constantes;

import com.webobjects.foundation.NSArray;

/**
 * L'ensemble des constantes des menus.
 * 
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */
public interface ConstsMenu {

	public final static String MENU_PERSO_FIC_ROSE 	= "Fiche Rose";
	public final static String MENU_PERSO_PLA_PREV 	= "Planning Pr&eacute;visionnel";
	public final static String MENU_PERSO_PLA_REEL 	= "Planning R&eacute;el";
	public final static String MENU_PERSO_PLA_TEST 	= "Planning Test"; //"Test-O-Matic";
	public final static String MENU_PERSO_HORAIRES 	= "Horaires";
	public final static String MENU_PERSO_CET 			= "CET";
	public final static String MENU_PERSO_IMP_DIVERS= "Imprim&eacute;s divers";
	public final static String MENU_PERSO_HISTO 		= "Historique";
	public final static String MENU_PERSO_PLA_BACK  = "Planning backup";
	
	/** le menu d'un planning desactive */
	 public final static NSArray MENU_ITEMS_PLANNING_PREVIOUS = new NSArray(
			new String[]{ MENU_PERSO_FIC_ROSE, MENU_PERSO_PLA_PREV,	MENU_PERSO_PLA_REEL});
	 
	 /** le menu d'un planning de quelqu'un d'autre en cours */
	 public final static NSArray MENU_ITEMS_PLANNING_OTHER_CURRENT = new NSArray(
			new String[]{ MENU_PERSO_FIC_ROSE, MENU_PERSO_PLA_PREV,	MENU_PERSO_PLA_REEL,
					MENU_PERSO_HORAIRES});

	 /** le menu d'un planning personnel en cours */
	 public final static NSArray MENU_ITEMS_PLANNING_SELF_CURRENT = new NSArray(
			new String[]{ MENU_PERSO_FIC_ROSE, MENU_PERSO_PLA_PREV,	MENU_PERSO_PLA_REEL,
					MENU_PERSO_PLA_TEST, MENU_PERSO_HORAIRES});
	 
	 
	/** le menu pour la validation d'une demande */ 
	public final static NSArray MENU_ITEMS_DETAIL_DEMANDE  = new NSArray(
			new String[]{
					MENU_PERSO_PLA_PREV, MENU_PERSO_PLA_REEL, MENU_PERSO_HORAIRES});
	
	public final static String MENU_ADMIN_PARAMETRE 			= "Param&egrave;tres";
	public final static String MENU_ADMIN_PARAMETRE_TIP 		= "Configuration de l'application";
	public final static String MENU_ADMIN_OUTILS 					= "Outils";
	public final static String MENU_ADMIN_OUTILS_TIP 					= "Utilitaires pour faciliter la gestion de l'application sans passer par la base de donn&eacute;es et/ou le moniteur";
	public final static String MENU_ADMIN_DROITS 					= "Droits";
	public final static String MENU_ADMIN_DROITS_TIP 					= "&Eacute;crans de gestion de droits des agents";
	public final static String MENU_ADMIN_SUPP_FER 				= "Fermetures";
	public final static String MENU_ADMIN_SUPP_FER_TIP 				= "Gestion des fermetures / cong&eacute;s forc&eacute;s par groupes (&eacute;tablissement, composante, service et agent)";
	public final static String MENU_ADMIN_CET 						= "CET";
	public final static String MENU_ADMIN_CET_TIP 						= "Gestion des Comptes Epargne Temps";
	public final static String MENU_ADMIN_JRTI 						= "<s>Rachat de cong&eacute;s</s>";
	public final static String MENU_ADMIN_JRTI_TIP 						= "NE PLUS UTILISER";
	public final static String MENU_ADMIN_SUIVI_LABEL					= "Suivi";
	public final static String MENU_ADMIN_SUIVI_TIP		 				= "Interface de suivi rapide des donn&eacute;es dans l'application (avec export CSV)";
	public final static String MENU_ADMIN_DIVERS_LABEL				=	"Divers";
	public final static String MENU_ADMIN_DIVERS_TIP					=	"Param&egrave;tres divers";

	public final static String MENU_ADMIN_PARAMETRE_SERV_AUT_LABEL 						= "Services autoris&eacute;s";
	public final static String MENU_ADMIN_PARAMETRE_SERV_AUT_TIP 								= "Services et agents &agrave; faire g&eacute;rer par l'application";
	public final static String MENU_ADMIN_PARAMETRE_HORAIRES_TYPES_LABEL = "Horaires Types";
	public final static String MENU_ADMIN_PARAMETRE_HORAIRES_TYPES_TIP 		= "Gestion des cycles hedbomadaires";
	public final static String MENU_ADMIN_PARAMETRE_TYPE_OCC_LABEL 						= "Types occupations";
	public final static String MENU_ADMIN_PARAMETRE_TYPE_OCC_TIP 								= "D&eacute;tail des types de cong&eacute;s g&eacute;r&eacute;s par l'application";
	public final static String MENU_ADMIN_PARAMETRE_PARAMETRES_LABEL					= "Param&egrave;tres g&eacute;n&eacute;raux";
	public final static String MENU_ADMIN_PARAMETRE_PARAMETRES_TIP							= "Saisie des param&eacute;tres globaux de l'application";
	public final static String MENU_ADMIN_PARAMETRE_HORAIRE_LABEL								= "Param&egrave;tres horaires";
	public final static String MENU_ADMIN_PARAMETRE_HORAIRE_TIP									= "Saisie des param&eacute;tres li&eacute;s aux horaires";
	public final static String MENU_ADMIN_PARAMETRE_CET_LABEL										= "Param&egrave;tres CET";
	public final static String MENU_ADMIN_PARAMETRE_CET_TIP											= "Saisie des param&eacute;tres du Compte &Eacute;pargne Temps";

	public final static String MENU_ADMIN_OUTIL_SYNCHRONISATION_LABEL		= "Synchronisation";
	public final static String MENU_ADMIN_OUTIL_SYNCHRONISATION_TIP				= "Mettre &agrave; jour les plannings d'apr&egrave;s la base de donn&eacute;es";
	public final static String MENU_ADMIN_OUTIL_NETTOYAGE_BASE_LABEL			= "Nettoyage base de donn&eacute;es";
	public final static String MENU_ADMIN_OUTIL_NETTOYAGE_BASE_TIP					= "Supprimer des plannings obsol&egrave;tes";
	
	public final static String MENU_ADMIN_DROIT_GESTION_LABEL			= "Profils & Droits";
	public final static String MENU_ADMIN_DROIT_GESTION_TIP				= "Gestion de l'affectation des droits aux agents";
	public final static String MENU_ADMIN_DROIT_LISTE_LABEL				= "Etat droits / cible";
	public final static String MENU_ADMIN_DROIT_LISTE_TIP					= "Liste globale de tous les droits affect&eacute;s sur cible (service, chef de service, agent, ...)";
	public final static String MENU_ADMIN_PASSE_DROIT_LISTE_LABEL				= "Etat droits individuels";
	public final static String MENU_ADMIN_PASSE_DROIT_LISTE_TIP					= "Liste globale de tous les passes droits accord&eacute;s par agent (hors-normes, d&eacute;passement de cong&eacute;s, ...)";
	public final static String MENU_ADMIN_DROIT_ADMIN_LABEL				= "Administrateurs";
	public final static String MENU_ADMIN_DROIT_ADMIN_TIP					= "Gestion de la liste des administrateurs g&eacute;n&eacute;raux de l'application";
	public final static String MENU_ADMIN_DROIT_DRH_LABEL				= "DRH";
	public final static String MENU_ADMIN_DROIT_DRH_TIP					= "Gestion de la liste des DRH g&eacute;n&eacute;raux de l'application";

	public final static String MENU_ADMIN_SUIVI_OCCUPATION_LABEL	= "Occupations";
	public final static String MENU_ADMIN_SUIVI_OCCUPATION_TIP		= "Suivi de l'ensemble des absences et occupations saisies dans l'application";
	public final static String MENU_ADMIN_SUIVI_CALCUL_LABEL			= "Calculs";
	public final static String MENU_ADMIN_SUIVI_CALCUL_TIP				= "Suivi des valeurs num&eacute;riques des plannings";
	public final static String MENU_ADMIN_SUIVI_PLANNING_LABEL		= "Plannings";
	public final static String MENU_ADMIN_SUIVI_PLANNING_TIP			= "Export des plannings annuels pour des service";
	
	public final static String MENU_ADMIN_CET_LISTE_DEMANDE_LABEL			= "Liste des demandes";
	public final static String MENU_ADMIN_CET_LISTE_DEMANDE_TIP				= "Gestion de liste des demandes CET faites par les agents";
	public final static String MENU_ADMIN_CET_SAISIE_DEMANDE_LABEL		= "Saisie demande";
	public final static String MENU_ADMIN_CET_SAISIE_DEMANDE_TIP			= "Saisie de demande CET &agrave; la place d'un agent";
	public final static String MENU_ADMIN_CET_REGULARISATION_LABEL		= "R&eacute;gularisation";
	public final static String MENU_ADMIN_CET_REGULARISATION_TIP			= "Saisie de compte CET initial pour un agent poss&eacute;dant un CET dans un autre &eacute;tablissement (suite &agrave; mutation par exemple ...)";
	public final static String MENU_ADMIN_CET_SITUATION_LABEL					= "Situation des comptes ouverts";
	public final static String MENU_ADMIN_CET_SITUATION_TIP						= "Situation de l'ensemble des comptes CET dans l'&eacute;tablissement";
	
	public final static String MENU_ADMIN_DIVERS_MESSAGES_LABEL	= "Messages";
	public final static String MENU_ADMIN_DIVERS_MESSAGES_TIP 	= "Gestion des messages &agrave; afficher en page d'accueil de l'application";
	public final static String MENU_ADMIN_DIVERS_VACANCES_LABEL = "Vacances Scolaires";
	public final static String MENU_ADMIN_DIVERS_VACANCES_TIP 	= "Gestion des p&eacute;riodes de vacances scolaires";
	
	/** le menu de l'administrateur global */
	public final static NSArray MENU_ITEMS_ADMINISTRATION_ADMIN_GLOBAL = new NSArray(new String[]{
				MENU_ADMIN_DROITS, MENU_ADMIN_PARAMETRE, MENU_ADMIN_OUTILS,
				MENU_ADMIN_SUPP_FER,  MENU_ADMIN_CET, MENU_ADMIN_SUIVI_LABEL, 
				MENU_ADMIN_DIVERS_LABEL});
	public final static NSArray PAGE_ADMINISTRATION_ADMIN_GLOBAL_MENU_TIP_LIST = new NSArray(new String[	]{
				MENU_ADMIN_DROITS_TIP, MENU_ADMIN_PARAMETRE_TIP, MENU_ADMIN_OUTILS_TIP, 
				MENU_ADMIN_SUPP_FER_TIP, MENU_ADMIN_CET_TIP, MENU_ADMIN_SUIVI_TIP, 
				MENU_ADMIN_DIVERS_TIP});
	
	
	/** le menu du DRH global */
	public final static NSArray MENU_ITEMS_ADMINISTRATION_DRH_GLOBAL = new NSArray(new String[]{
				MENU_ADMIN_CET});
	public final static NSArray PAGE_ADMINISTRATION_DRH_GLOBAL_MENU_TIP_LIST = new NSArray(new String[]{
				MENU_ADMIN_CET_TIP});
	
	
	/** le menu de l'administrateur de composante */
	public final static NSArray MENU_ITEMS_ADMINISTRATION_ADMIN_COMPOSANTE = new NSArray(new String[]{
				MENU_ADMIN_DROITS});
	public final static NSArray PAGE_ADMINISTRATION_ADMIN_COMPOSANTE_MENU_TIP_LIST = new NSArray(new String[]{
				MENU_ADMIN_DROITS});

	
	/** le sous menu des droits */
	public final static NSArray PAGE_ADMIN_GENERAL_DROIT_SUB_MENU_LABEL_LIST = new NSArray(new String[] {
			MENU_ADMIN_DROIT_GESTION_LABEL, MENU_ADMIN_DROIT_LISTE_LABEL, MENU_ADMIN_PASSE_DROIT_LISTE_LABEL, MENU_ADMIN_DROIT_ADMIN_LABEL, MENU_ADMIN_DROIT_DRH_LABEL});
	public final static NSArray PAGE_ADMIN_GENERAL_DROIT_SUB_MENU_TIP_LIST = new NSArray(new String[] {
			MENU_ADMIN_DROIT_GESTION_TIP, MENU_ADMIN_DROIT_LISTE_TIP, MENU_ADMIN_PASSE_DROIT_LISTE_TIP, MENU_ADMIN_DROIT_ADMIN_TIP, MENU_ADMIN_DROIT_DRH_TIP});

	/** le sous menu des droits */
	public final static NSArray PAGE_ADMIN_COMPOSANTE_DROIT_SUB_MENU_LABEL_LIST = new NSArray(new String[] {
			MENU_ADMIN_DROIT_GESTION_LABEL});
	public final static NSArray PAGE_ADMIN_COMPOSANTE_DROIT_SUB_MENU_TIP_LIST = new NSArray(new String[] {
			MENU_ADMIN_DROIT_GESTION_TIP});
	
	/** le sous menu des parametre */
	public final static NSArray PAGE_ADMIN_PARAMETRE_SUB_MENU_LABEL_LIST = new NSArray(new String[] {
			MENU_ADMIN_PARAMETRE_SERV_AUT_LABEL, MENU_ADMIN_PARAMETRE_HORAIRES_TYPES_LABEL, MENU_ADMIN_PARAMETRE_TYPE_OCC_LABEL, 
			MENU_ADMIN_PARAMETRE_PARAMETRES_LABEL, MENU_ADMIN_PARAMETRE_HORAIRE_LABEL, MENU_ADMIN_PARAMETRE_CET_LABEL});
	public final static NSArray PAGE_ADMIN_PARAMETRE_SUB_MENU_TIP_LIST = new NSArray(new String[] {
			MENU_ADMIN_PARAMETRE_SERV_AUT_TIP, MENU_ADMIN_PARAMETRE_HORAIRES_TYPES_TIP, MENU_ADMIN_PARAMETRE_TYPE_OCC_TIP, 
			MENU_ADMIN_PARAMETRE_PARAMETRES_TIP, MENU_ADMIN_PARAMETRE_HORAIRE_TIP, MENU_ADMIN_PARAMETRE_CET_TIP});
		
	/** le sous menu des outils */
	public final static NSArray PAGE_ADMIN_OUTIL_SUB_MENU_LABEL_LIST = new NSArray(new String[] {
			MENU_ADMIN_OUTIL_SYNCHRONISATION_LABEL, MENU_ADMIN_OUTIL_NETTOYAGE_BASE_LABEL});
	public final static NSArray PAGE_ADMIN_OUTIL_SUB_MENU_TIP_LIST = new NSArray(new String[] {
			MENU_ADMIN_OUTIL_SYNCHRONISATION_TIP, MENU_ADMIN_OUTIL_NETTOYAGE_BASE_TIP});
	
	/** le sous menu des écrans de suivi */
	public final static NSArray PAGE_ADMIN_SUIVI_SUB_MENU_LABEL_LIST = new NSArray(new String[]{
			MENU_ADMIN_SUIVI_CALCUL_LABEL, MENU_ADMIN_SUIVI_OCCUPATION_LABEL, MENU_ADMIN_SUIVI_PLANNING_LABEL});
	public final static NSArray PAGE_ADMIN_SUIVI_SUB_MENU_TIP_LIST = new NSArray(new String[]{
			MENU_ADMIN_SUIVI_CALCUL_TIP, MENU_ADMIN_SUIVI_OCCUPATION_TIP, MENU_ADMIN_SUIVI_PLANNING_TIP});
	
	/** le sous menu des demandes CET */
	public final static NSArray PAGE_ADMIN_CET_SUB_MENU_LABEL_LIST = new NSArray(new String[] {
			MENU_ADMIN_CET_LISTE_DEMANDE_LABEL, MENU_ADMIN_CET_SAISIE_DEMANDE_LABEL, MENU_ADMIN_CET_REGULARISATION_LABEL, MENU_ADMIN_CET_SITUATION_LABEL});
	public final static NSArray PAGE_ADMIN_CET_SUB_MENU_TIP_LIST = new NSArray(new String[] {
			MENU_ADMIN_CET_LISTE_DEMANDE_TIP, MENU_ADMIN_CET_SAISIE_DEMANDE_TIP, MENU_ADMIN_CET_REGULARISATION_TIP, MENU_ADMIN_CET_SITUATION_TIP});

	/** le sous menu divers*/
	public final static NSArray PAGE_ADMIN_DIVERS_SUB_MENU_LABEL_LIST = new NSArray(new String[] {
			MENU_ADMIN_DIVERS_MESSAGES_LABEL, MENU_ADMIN_DIVERS_VACANCES_LABEL});	
	public final static NSArray PAGE_ADMIN_DIVERS_SUB_MENU_TIP_LIST = new NSArray(new String[] {
			MENU_ADMIN_DIVERS_MESSAGES_TIP, MENU_ADMIN_DIVERS_VACANCES_TIP});
	
}
