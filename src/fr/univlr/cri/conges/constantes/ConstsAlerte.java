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

/**
 * Les constantes relatives a tous les objets de 
 * type <code>EOAlerte</code>
 * 
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */

public interface ConstsAlerte {

  public final static String ALERT_PREFIX_LIBELLE_MODIF_P_REEL = "Modification du planning réel";
  public final static String ALERT_PREFIX_LIBELLE_VALID_P_PREV = "Validation du planning prévisionnel";
  public final static String ALERT_MAIL_SUBJECT_PREFIX = "[Conges] ";
  public final static String TXT_ERREUR_RESPONSABLE = 
    "Attention\n" + 
    "L'application n'a pas pu detecter vos responsables\n" + 
    "La demande n'est pas prise en compte\n" + 
    "L'annuaire n'est pas renseigné correctement\n" +
    "Veuillez contacter le CRI ...";
  public final static String TXT_ERREUR_RESPONSABLE_HTML = 
    "Attention<br>" + 
    "L'application n'a pas pu detecter vos responsables<br>" + 
    "La demande n'est pas prise en compte<br>" + 
    "L'annuaire n'est pas renseigné correctement<br>" + 
    "Veuillez contacter le CRI ...";

  /** le titre du mail venant du demandeur */
  public final static String PREFIX_MAIL_TITLE_DEMANDE = ALERT_MAIL_SUBJECT_PREFIX + "demande de ";
  /** le nom du demandeur */
  public final static String VAR_DEMANDEUR 				= "%DEMANDEUR%";
  /** le libelle de l'alerte */
  public final static String VAR_LIBELLE_ALERTE		= "%LIBELLE_ALERTE%";
  /** l'URL de l'application Conges */
  public final static String VAR_APP_URL						= "%APPURL%";
  /** la partie specifique selon la nature de l'alerte */
  public final static String VAR_SPEC							= "%SPEC%";
  /** le verbe de l'action a realiser par le valideur */
  public final static String VAR_VERBE							= "%VERBE%";
  /** l'adresse email de sam associee au plugin sam-conges */
  public final static String VAR_SAM_MAIL					= "%APP_SAM_MAIL%";
  /** la terminaison de l'attribut de la table correspondant a la demande */
  public final static String VAR_ATTR							=	"%ATTR%";
  /** la valeur du hashcode associee a l'alerte */
  public final static String VAR_HASH							=	"%HASH%";
  /** la variable du texte relatif a la delegation */
  public final static String VAR_CONTENT_DELEGATION			=	"%CONTENT_DELEGATION%";

  /** Le contenu du message envoye pour une alerte generique, sans SAM */
  public final static String PATTERN_NEW_ALERT = 
  	"Une demande de "+VAR_DEMANDEUR+VAR_CONTENT_DELEGATION+" concernant :\n" + 
  	VAR_LIBELLE_ALERTE + "\n vient d'etre créée.\n" + 
  	"\n" +
  	VAR_SPEC + "Vous pouvez la consulter et/ou la valider à cette adresse:\n" + 
  	VAR_APP_URL;
  
  /** Le contenu du message envoye pour une alerte generique, avec SAM */
  public final static String PATTERN_NEW_ALERT_SAM = 
  	PATTERN_NEW_ALERT + "\n" +
  	"\n" +
  	"----------------\n" +
  	"Actions par mail\n" +
  	"\n" + 
  	"Vous pouvez "+VAR_VERBE+" la demande directement en cliquant sur ce lien :\n" +
  	VAR_SAM_MAIL + "?subject=ACCEPT$CAL_HASHCODE_" + VAR_ATTR + "$" + VAR_HASH + "\n" +
  	/*"ou bien en envoyant un mail a l'adresse "+appSamMail+" avec en sujet:\n " + subjectSamCongesAccept +
        "\n\n" + */
  	"\n"+
  	"Vous pouvez refuser la demande directement en cliquant sur ce lien :\n" +
  	VAR_SAM_MAIL + "?subject=REFUSE$CAL_HASHCODE_" + VAR_ATTR + "$" + VAR_HASH + "\n";
  /*"ou bien en envoyant un mail a l'adresse "+appSamMail+" avec en sujet:\n" + subjectSamCongesRefuse;*/
   
  
  /** Nouvelle occupation : le nombre de conges restants */
  public final static String VAR_CONGES_RESTANTS = "%CONGES_RESTANTS%";
  /** Nouvelle occupation : la phrase contenant toutes ses specifites */
  public final static String VAR_SPEC_NOUVELLE_OCCUPATION = 
  	"Congés restants après validation : " + VAR_CONGES_RESTANTS + "\n" +
  	"\n";
  
  /** mode debug : la variable contenant l'adresse email de l'administrateur */
  public final static String VAR_ADMIN_MAIL 		= "%APP_ADMIN_MAIL%";
  /** mode debug : la variable contenant l'adresse email du destinataire */
  public final static String VAR_MAIL_TO 			= "%MAIL_TO%";
  /** mode debug : la variable contenant l'adresse email des cc */
  public final static String VAR_MAIL_CC 			= "%MAIL_CC%";

  /** mode debug : le prefix de tout le message email reexpedie */
  public final static String PREFIX_CONTENT_DEBUG = 
  	"---------\n" + 
  	"Attention, le mail suivant n'a ete envoye qu'a(ux) adresse(s) suivante(s) : \n" + 
  	VAR_ADMIN_MAIL + "\n" + 
  	"En production, les destinataires seront : " + VAR_MAIL_TO + ", cc : " + VAR_MAIL_CC + "\n" +
  	"---------\n" +
  	"\n";

  /** l'ensemble des caracteres utilises pour construire les hashcode */
	public final static String HASH_DICTIONARY = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ0123456789";
 
	
	// delegation

  /** la variable du nom du delegue */
  public final static String VAR_DELEGUE								=	"%DELEGUE%";
  /** le texte concernant la delegation */
	public final static String CONTENT_DELEGATION 				= " (de " + VAR_DELEGUE + " par délégation)";
  
	// libelles des reponses lies au traitement des alerte
	
	public final static String ALERTE_LIBELLE_ACCEPTEE 		= "ACCEPTEE";
	public final static String ALERTE_LIBELLE_REFUSEE 		= "REFUSEE";
	public final static String ALERTE_LIBELLE_VISEE 			= "VISEE";
	
}
