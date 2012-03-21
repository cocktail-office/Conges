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
 * Regroupe l'ensemble des constantes utilise par
 * la classe <code>Planning</code>
 * 
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */

public interface ConstsPlanning {

	// etats du plannings
	
	/** code planning non valide */
	public final static String PLANNING_STATUT_INVALIDE = "0";
	/** code planning valide */
	public final static String PLANNING_STATUT_VALIDE = "1";
	/** code planning en cours de validation */
	public final static String PLANNING_STATUT_EN_COURS_DE_VALIDATION = "2";
	/** code planning en cours de modification */
	public final static String PLANNING_STATUT_EN_COURS_DE_MODIFICATION = "3";
	/** code planning en cours de validation qui recu un visa */
	public final static String PLANNING_STATUT_EN_COURS_DE_VALIDATION_VISE = "4";
	/** code planning en cours de modification qui recu un visa */
	public final static String PLANNING_STATUT_EN_COURS_DE_MODIFICATION_VISE = "5";
	
	
	// natures de planning
	
	/** code planning reel */
	public final static String REEL = "R";
	/** code planning previsionnel */
	public final static String PREV = "P";
	/** code planning test */
	public final static String TEST = "T";
	/** code planning backup (copie de sauvegarde pour retablissement lors du refus de validation par exemple) */
	public final static String SAVE = "S";
	
	/** les codes des types de plannings "visibles" */
	public final static NSArray TYPES_PLANNINGS_VISIBLES = 
		new NSArray(new String[] { REEL, PREV });
	
	/**
	 * TODO trouver un moyen plus fiable 
	 * le debut du libelle de l'occupation type fermeture annuelle ou decalee
	 */
	public final static String PREFIX_LIBELLE_FERMETURE = "Fermeture";
	
	// les codes couleurs associes au statut du planning
	public final static String HTML_COLOR_PLANNING_EN_COURS_DE_MODIFICATION = "#aa6201";
	public final static String HTML_COLOR_PLANNING_EN_COURS_DE_VALIDATION 		= "#01619d";
	public final static String HTML_COLOR_PLANNING_INVALIDE 													= "#ff0000";
	public final static String HTML_COLOR_PLANNING_VALIDE															= "#01b403";
	public final static String HTML_COLOR_PLANNING_TEST 															= "#800080";
	
	// les libelles associes au statut du planning
	public final static String HTML_LABEL_PLANNING_EN_COURS_DE_MODIFICATION = "PLANNING EN COURS DE MODIFICATION";
	public final static String HTML_LABEL_PLANNING_EN_COURS_DE_VALIDATION 		= "PLANNING EN COURS DE VALIDATION";
	public final static String HTML_LABEL_PLANNING_INVALIDE 													= "PLANNING INVALIDE";
	public final static String HTML_LABEL_PLANNING_VALIDE															= "PLANNING VALID&Eacute;";
	public final static String HTML_LABEL_PLANNING_TEST 															= "PLANNING DE TEST";

	
	// les messages info bulles associes a l'etat du planning
  public final static String HTML_MSG_PLANNING_NON_VALIDE =
    "est invalide.<br>" 					+
    "Saisissez des horaires dans l onglet <b>Horaires</b>.<br>"			+
    "<b>Associez les horaires</b> aux semaines de l affectation<br>"		+
    "en cliquant sur un numéro de semaine.<br>"						+
    "<b>Saisissez des absences</b> sur le planning en cliquant<br>"		+
    "sur un numéro de jour."										;
  public final static String HTML_MSG_PLANNING_VALIDE =
    "est validé.<br>"						+
    "<b>Saisissez des absences</b> sur le planning en cliquant<br>"		+
    "sur un numéro de jour.<br>"									+
    "Modifiez les horaires en cliquant sur le bouton<br>"			+
    "&quot;MODIFIER PLANNING ET HORAIRES (DEVALIDER)&quot;.";		
  public final static String HTML_MSG_PLANNING_EN_COURS_DE_MODIFICATION =
    "est en cours de modification.<br>"	+
    "Saisissez de nouveaux horaires dans l onglet <b>Horaires</b>.<br>"	+
    "<b>Associez</b> ces derniers aux semaines de l affectation<br>"		+
    "en cliquant sur un numéro de semaine.";
  public final static String HTML_MSG_PLANNING_EN_COURS_DE_VALIDATION =
    "est en cours de validation.<br>"	+
    "Vous ne pouvez rien saisir tant que un des<br>" 				+
    "responsable ne l aura pas validé.";	
  public final static String HTML_MSG_PLANNING_DE_TEST =
    "est le planning de test.<br>"		+
    "Vous pouvez associer des horaires et saisir des conges<br>" 	+
    "a votre guise (sans <b>aucun transfert de mail</b> ni validation!)";
	
  // les messages info bulle associes a l'impossibilite de valider
  // le prefixe du message d'avertissement si le prev. n'est pas validable
  public final static String HTML_MSG_VALIDATION_PREVISIONNEL_IMPOSSIBLE_PREFIX = 
  	"<b>Vous ne pouvez valider votre pr&eacute;visionnel pour la raison suivante : </b><br/><br/>";
  // le nombre total de semaines non associ�es affichables dans le message
  public final static int MSG_VALIDATION_PREVISIONNEL_COUNT_MAX_SEMAINE_DISPLAYED = 10;
  // 
  public final static String HTML_MSG_VALIDATION_PREVISIONNEL_IMPOSSIBLE_NO_HORAIRE =
  	HTML_MSG_VALIDATION_PREVISIONNEL_IMPOSSIBLE_PREFIX + "Pas d'horaire enregistr&eacute; sur cette ann&eacute;e universitaire";
  // 
  public final static String HTML_MSG_VALIDATION_PREVISIONNEL_IMPOSSIBLE_LIMITE_CONGES_DEPASSEE =
  	HTML_MSG_VALIDATION_PREVISIONNEL_IMPOSSIBLE_PREFIX + "La limite de droit &agrave; cong&eacute;s est d&eacute;pass&eacute;e";
  // 
  public final static String HTML_MSG_VALIDATION_PREVISIONNEL_IMPOSSIBLE_LIMITE_SEM_HAUTE_DEPASSEE =
  	HTML_MSG_VALIDATION_PREVISIONNEL_IMPOSSIBLE_PREFIX + "La limite maximale de semaines hautes associ&eacute;es est d&eacute;pass&eacute;e";
  // 
  public final static String HTML_MSG_VALIDATION_PREVISIONNEL_IMPOSSIBLE_SEM_ACTIVES_NON_ASSO_PREFIX =
  	HTML_MSG_VALIDATION_PREVISIONNEL_IMPOSSIBLE_PREFIX + "Les semaines suivantes n'ont pas d'horaire associ&eacute; (total=";
}
