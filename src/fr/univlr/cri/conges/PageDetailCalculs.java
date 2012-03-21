package fr.univlr.cri.conges;

import java.util.Hashtable;

import com.webobjects.appserver.*;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.YCRIWebPage;
import fr.univlr.cri.conges.eos.modele.planning.I_ClasseMetierNotificationParametre;
import fr.univlr.cri.conges.objects.Parametre;
import fr.univlr.cri.conges.objects.Planning;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.util.StringCtrl;

/*
 * Copyright Consortium Coktail, 12 avr. 07
 * 
 * cyril.tarade at univ-lr.fr
 * 
 * Ce logiciel est un programme informatique servant � [rappeler les
 * caract�ristiques techniques de votre logiciel]. 
 * 
 * Ce logiciel est r�gi par la licence CeCILL soumise au droit fran�ais et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffus�e par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".
 * 
 * En contrepartie de l'accessibilit� au code source et des droits de copie,
 * de modification et de redistribution accord�s par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limit�e.  Pour les m�mes raisons,
 * seule une responsabilit� restreinte p�se sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les conc�dants successifs.
 * 
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
 * 
 * Le fait que vous puissiez acc�der � cet en-t�te signifie que vous avez 
 * pris connaissance de la licence CeCILL, et que vous en avez accept� les
 * termes.
 */

/**
 * Page type 'popup' detaillant tous les calculs de droits a conges.
 * 
 *  @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */

public class PageDetailCalculs 
	extends YCRIWebPage 
		implements I_ClasseMetierNotificationParametre {

	public Planning lePlanning;
  public String lesDureesJournalieresItem;
	
  /** id du span contenant les conges initiaux en jours */
  public final String LABEL_DIV_CONGES_INITIAUX_JOURS 			= "divCongesInitiauxJours";
  /** id du span contenant les conges restants en jours */
  public final String LABEL_DIV_CONGES_RESTANTS_JOURS 			= "divCongesRestantsJours";
  /** id du span contenant les regularisations en jours */
  public final String LABEL_DIV_REGULARISATION_JOURS 				= "divCongesRegularisationJours";
  /** id du span contenant les jrti en jours */
  public final String LABEL_DIV_JRTI_JOURS 									= "divCongesJrtiJours";
  /** id du span contenant les jrti en jours */
  public final String LABEL_DIV_BLOCAGE_RELIQUATS_CET_JOURS = "divBlocageReliquatsJours";
  /** id du span contenant les reliquats initiaux en jours */
  public final String LABEL_DIV_RELIQUATS_INITIAUX_JOURS 		= "divReliquatsInitiauxJours";
  /** id du span contenant les reliquats restants en jours */
  public final String LABEL_DIV_RELIQUATS_RESTANTS_JOURS 		= "divReliquatsRestantsJours";
  /** id du span contenant les decompte de conges legaux en jours */
  public final String LABEL_DIV_DECOMPTE_LEGAL_JOURS 				= "divCongesDecompteLegalJours";
  /** id du span contenant la valeur total initiale des conges en jours */
  public final String LABEL_DIV_CONGES_TOTAL_INITIAUX_JOURS = "divCongesTotalInitiauxJours";
  /** id du span contenant la valeur total restante des conges en jours */
  public final String LABEL_DIV_CONGES_TOTAL_RESTANTS_JOURS = "divCongesTotalRestantsJours";
  /** id du span contenant la valeur total initiale des decharges syndicales en jours */
  public final String LABEL_DIV_DECHARGE_SYNDICALE_INITIAUX_JOURS = "divDechargeSyndicaleInitiauxJours";
  /** id du span contenant la valeur total restante des decharges syndicales en jours */
  public final String LABEL_DIV_DECHARGE_SYNDICALE_RESTANTS_JOURS = "divDechargeSyndicaleRestantsJours";
  /** nom de l'input HTML dans lequel est saisit la duree d'une journee en heure */
  public final String INPUT_DUREE_JOURNEE 		= "document.forms['FormDetailsCalculs'].inputDureeJournee";

  
  public static String dureeJourneeApp =  Parametre.PARAM_DUREE_JOUR_CONVERSION.getParamValueString();
  /** valeur initiale de la duree d'une journee */
  public String dureeJournee = dureeJourneeApp;
  /** nom de la methode javascript de convertion*/
  private final static String JS_METHOD_CONVERT = "convertirHeuresToJoursFromElement";
  
  /** patterns du code javascript genere pour les calculs de conversion */
  private final String ON_BLUR_INPUT_DUREE = 
  	"formatter(document.forms['FormDetailsCalculs'].inputDureeJournee,true);" +
  		JS_METHOD_CONVERT + "(" +
  		"'%"+KEY_CONGES_INITIAUX+"%'," +
  		INPUT_DUREE_JOURNEE+"," +
  		"'"+LABEL_DIV_CONGES_INITIAUX_JOURS+"')" +
  		";" +
  		JS_METHOD_CONVERT + "(" +
  		"'%"+KEY_CONGES_RESTANTS+"%'," +
  		INPUT_DUREE_JOURNEE+"," +
  		"'"+LABEL_DIV_CONGES_RESTANTS_JOURS+"')" +
  		";" +
  		JS_METHOD_CONVERT + "(" +
  		"'%"+KEY_RELIQUATS_INITIAUX+"%'," +
  		INPUT_DUREE_JOURNEE+"," +
  		"'"+LABEL_DIV_RELIQUATS_INITIAUX_JOURS+"')" +
  		";" +
  		JS_METHOD_CONVERT + "(" +
  		"'%"+KEY_RELIQUATS_RESTANTS+"%'," +
  		INPUT_DUREE_JOURNEE+"," +
  		"'"+LABEL_DIV_RELIQUATS_RESTANTS_JOURS+"')" +
  		";" +
  		JS_METHOD_CONVERT + "(" +
  		"'%"+KEY_CONGES_TOT_INITIAUX+"%'," +
  		INPUT_DUREE_JOURNEE+"," +
  		"'"+LABEL_DIV_CONGES_TOTAL_INITIAUX_JOURS+"')" +
  		";" +
  		JS_METHOD_CONVERT + "(" +
  		"'%"+KEY_CONGES_TOT_RESTANTS+"%'," +
  		INPUT_DUREE_JOURNEE+"," +
  		"'"+LABEL_DIV_CONGES_TOTAL_RESTANTS_JOURS+"')" +
  		";" ;

  /** morceau de javascript dedie a la conversion de la regularisation */
  private final String ON_BLUR_REGULARISATION =
  	JS_METHOD_CONVERT + "(" +
		"'%"+KEY_CONGES_REGULARISATION+"%'," +
		INPUT_DUREE_JOURNEE+"," +
		"'"+LABEL_DIV_REGULARISATION_JOURS+"')" +
		";";
  
  /** morceau de javascript dedie a la conversion des jrti */
  private final String ON_BLUR_JRTI =
  	JS_METHOD_CONVERT + "(" +
		"'%"+KEY_CONGES_JRTI+"%'," +
		INPUT_DUREE_JOURNEE+"," +
		"'"+LABEL_DIV_JRTI_JOURS+"')" +
		";";
  
  /** morceau de javascript dedie a la conversion des jrti */
  private final String ON_BLUR_BLOCAGE_RELIQUATS_CET =
  	JS_METHOD_CONVERT + "(" +
		"'%"+KEY_BLOCAGE_RELIQUATS_CET+"%'," +
		INPUT_DUREE_JOURNEE+"," +
		"'"+LABEL_DIV_BLOCAGE_RELIQUATS_CET_JOURS+"')" +
		";";
  
  /** morceau de javascript dedie a la conversion des decomptes legaux */
  private final String ON_BLUR_DECOMPTE_LEGAL=
  	JS_METHOD_CONVERT + "(" +
		"'%"+KEY_DECOMPTE_LEGAL+"%'," +
		INPUT_DUREE_JOURNEE+"," +
		"'"+LABEL_DIV_DECOMPTE_LEGAL_JOURS+"')" +
		";";
  
  /** morceau de javascript dedie a la conversion des decharges syndicales initiales */
  private final String ON_BLUR_DECH_SYND_INIT =
  	JS_METHOD_CONVERT + "(" +
		"'%"+KEY_DECH_SYND_INITIAUX+"%'," +
		INPUT_DUREE_JOURNEE+"," +
		"'"+LABEL_DIV_DECHARGE_SYNDICALE_INITIAUX_JOURS+"')" +
		";";
  
  /** morceau de javascript dedie a la conversion des decharges syndicales restantes */
  private final String ON_BLUR_DECH_SYND_REST =
  	JS_METHOD_CONVERT + "(" +
		"'%"+KEY_DECH_SYND_RESTANTS+"%'," +
		INPUT_DUREE_JOURNEE+"," +
		"'"+LABEL_DIV_DECHARGE_SYNDICALE_RESTANTS_JOURS+"')" +
		";";
 	
  /** */
  private final String ON_KEY_DOWN_INPUT_DUREE_PREFIX = "if(event.keyCode==13){";
  private final String ON_KEY_DOWN_INPUT_DUREE_SUFFIX 	= "}";
  
  /** le nom des variable utilisees par le dico de conversion */
  private final static String KEY_CONGES_INITIAUX 					= "CONGES_INITIAUX";
  private final static String KEY_CONGES_RESTANTS 					= "CONGES_RESTANTS";
  private final static String KEY_CONGES_REGULARISATION 		= "CONGES_REGULARISATION";
  private final static String KEY_CONGES_JRTI 							= "CONGES_JRTI";
  private final static String KEY_BLOCAGE_RELIQUATS_CET			= "BLOCAGE_RELIQUATS_CET";
  private final static String KEY_RELIQUATS_INITIAUX 				= "RELIQUATS_INITIAUX";
  private final static String KEY_RELIQUATS_RESTANTS 				= "RELIQUATS_RESTANTS";
  private final static String KEY_DECOMPTE_LEGAL 						= "DECOMPTE_LEGAL";
  private final static String KEY_CONGES_TOT_INITIAUX 			= "CONGES_TOT_INITIAUX";
  private final static String KEY_CONGES_TOT_RESTANTS 			= "CONGES_TOT_RESTANTS";
  private final static String KEY_DECH_SYND_INITIAUX 				= "DECH_SYND_INITIAUX";
  private final static String KEY_DECH_SYND_RESTANTS 				= "DECH_SYND_RESTANTS";
  
  /** les valeurs concretes de gestion du code javascript pour le planning en cours*/
  public String onBlurInputDuree;
  public String onKeyDownInputDuree;
  /** dico de conversion utilises pour la creation du code JS par replaceWithDico*/
  private Hashtable dicoConversion;
  
	public PageDetailCalculs(WOContext context) {
		super(context);
	}

  /**
   * @see I_ClasseMetierNotificationParametre
   * @param parametre
   */
  public static void initStaticField(
  		Parametre parametre) {
  	if (parametre == Parametre.PARAM_DUREE_JOUR_CONVERSION) {
  		dureeJourneeApp = parametre.getParamValueString();
  	} 
  }
  
  public void appendToResponse(WOResponse response, WOContext arg1) {
  	// generer le code js de conversion
  	generateCodeJavascriptConversion();
  	//
    super.appendToResponse(response, arg1);
    // le code javascript utilise pour la manipulation de durees
    addLocalJScript(response, "jscript/CRITimeField.js", "CRIWebExt3");
    // lors du premier chargement, il faut effectuer la converion
    addHTMLBinding(response, "onload", onBlurInputDuree, TAG_OPEN_BODY);    
  }
  
  
  /**
   * Genere le code javascript utilise dans la conversion des jours
   * en heures. Ce code est specifique a chaque planning. On reinstancie
   * les variables concretes <code>dicoConversion</code>, 
   * <code>onBlurInputDuree</code> et <code>onKeyDownInputDuree</code>
   */
  private void generateCodeJavascriptConversion() {
  	// base de conversion
  	dicoConversion = new Hashtable();
  	dicoConversion.put(KEY_CONGES_INITIAUX, 				lePlanning.congesInitiauxEnHeures());
  	dicoConversion.put(KEY_CONGES_RESTANTS, 				lePlanning.congesRestants());
  	dicoConversion.put(KEY_CONGES_REGULARISATION, 	lePlanning.regularistationEnHeures());
  	dicoConversion.put(KEY_CONGES_JRTI, 						lePlanning.jrtiEnHeures());
  	dicoConversion.put(KEY_BLOCAGE_RELIQUATS_CET, 	lePlanning.blocageReliquatsCetEnHeures());
  	dicoConversion.put(KEY_RELIQUATS_INITIAUX, 			lePlanning.reliquatInitial());
  	dicoConversion.put(KEY_RELIQUATS_RESTANTS, 			getStrHeuresReliquatRestant());
  	dicoConversion.put(KEY_DECOMPTE_LEGAL, 					lePlanning.decompteLegal());
  	dicoConversion.put(KEY_CONGES_TOT_INITIAUX, 		lePlanning.congesInitiauxSansDecompte());
  	dicoConversion.put(KEY_CONGES_TOT_RESTANTS, 		lePlanning.congesGlobalRestants());
  	dicoConversion.put(KEY_DECH_SYND_INITIAUX, 			lePlanning.dechargeSyndicaleInitiauxEnHeures());
  	dicoConversion.put(KEY_DECH_SYND_RESTANTS, 			lePlanning.dechargeSyndicaleRestantsEnHeures());
  	// creation du code a partir des patterns
  	onBlurInputDuree 		= StringCtrl.replaceWithDico(
  			ON_BLUR_INPUT_DUREE + (hasDecompteLegal() ? 				ON_BLUR_DECOMPTE_LEGAL : "") +  
  			ON_BLUR_INPUT_DUREE + (hasRegulation() ? 						ON_BLUR_REGULARISATION : "") + 
  			ON_BLUR_INPUT_DUREE + (hasJrti() ? 									ON_BLUR_JRTI : "") + 
  			ON_BLUR_INPUT_DUREE + (hasBlocageReliquatsCet() ? 	ON_BLUR_BLOCAGE_RELIQUATS_CET : "") + 
  			ON_BLUR_INPUT_DUREE + (hasDechargeSyndicale() ? 		ON_BLUR_DECH_SYND_INIT : "") + 
  			ON_BLUR_INPUT_DUREE + (hasDechargeSyndicale() ? 		ON_BLUR_DECH_SYND_REST : "") ,
  			dicoConversion);
  	onKeyDownInputDuree = ON_KEY_DOWN_INPUT_DUREE_PREFIX + onBlurInputDuree + ON_KEY_DOWN_INPUT_DUREE_SUFFIX;
  }
    
	public void externeSetLePlanning(Planning value) {
		lePlanning = value;
	}
	
	public boolean hasDecompteLegal() {
		return lePlanning.decompteLegalEnMinutes() > 0;
	}
	
	public boolean hasRegulation() {
		return lePlanning.regularistationEnMinutes() != 0;
	}
	
	public boolean hasJrti() {
		return lePlanning.getJrti() != null;
	}
	
	public boolean hasBlocageReliquatsCet() {
		return lePlanning.getBlocageReliquatsCet() != null;
	}
	
	public boolean hasDechargeSyndicale() {
		return lePlanning.affectationAnnuelle().isDechargeSyndicale();
	}
	
  /**
   * Si le reliquat est negatif, alors la ligne concernant le
   * reliquat indique "dont reliquat"
   */
  public boolean hasReliquatNegatif() {
    return lePlanning.reliquatInitial().startsWith("-");
  }
    
	// TODO mettre la date de fin d'annee si c'est passe
	public String heuresTravailleesRealisees() {
		return lePlanning.heuresTravailleesRealisees(laSession.dateRef());
	}
	
	/**
	 * Specificite pour cet ecran. Si le reliquat initial est negatif, alors
	 * on l'affiche comme tel, on ne le passe pas a 0.
	 */
	public String getStrHeuresReliquatRestant() {
		String strResult = null;
		if (lePlanning.reliquatInitial().startsWith("-")) {
			strResult = lePlanning.reliquatInitial();
	  } else {
	  	strResult = lePlanning.reliquatRestant();
	  }
		return strResult;
	}
	
	/** calcul d'impots : debut d'annee univ */
	public NSTimestamp dateDebutAnneeUniv() {
		return DateCtrlConges.dateToDebutAnneeUniv(lePlanning.affectationAnnuelle().dateDebutAnnee());
	}
	/** calcul d'impots : fin d'annee civile */
	public NSTimestamp dateFinAnneeCivile() {
		return DateCtrlConges.dateToFinAnneeCivile(lePlanning.affectationAnnuelle().dateDebutAnnee());
	}
	/** calcul d'impots : debut d'annee civile */
	public NSTimestamp dateDebutAnneeCivile() {
		return DateCtrlConges.date1erJanAnneeUniv(lePlanning.affectationAnnuelle().dateDebutAnnee());
	}
	/** calcul d'impots : fin d'annee univ */
	public NSTimestamp dateFinAnneeUniv() {
		return DateCtrlConges.dateToFinAnneeUniv(lePlanning.affectationAnnuelle().dateDebutAnnee());
	}
}