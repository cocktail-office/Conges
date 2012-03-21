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
package fr.univlr.cri.conges;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSDictionary;

import fr.univlr.cri.conges.constantes.ConstsApplication;
import fr.univlr.cri.conges.databus.CngUserInfo;
import fr.univlr.cri.conges.eos.modele.conges.EOAlerte;
import fr.univlr.cri.conges.eos.modele.conges.EOParametre;
import fr.univlr.cri.conges.eos.modele.grhum.EOAffectation;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.objects.Parametre;
import fr.univlr.cri.util.EOModelCtrl;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.LRLog;
import fr.univlr.cri.webapp.VersionCocktail;
import fr.univlr.cri.webext.CRIAlertPage;
import fr.univlr.cri.ycrifwk.YCRIApplication;
import fr.univlr.cri.ycrifwk.utils.UtilException;

public class Application
		extends YCRIApplication {

	public String customConfigTableName() {
		return EOParametre.ENTITY_NAME;
	}

	public String[] listeVariablesGrhumParametre() {
		return new String[] {};
	}

	public static void main(String argv[]) {
		WOApplication.main(argv, Application.class);
	}

	public Application() {
		super();
		setDefaultRequestHandler(requestHandlerForKey(directActionRequestHandlerKey()));
	}

	public void initApplication() {

		super.initApplication();

		rawLogAppInfos();
		rawLogVersionInfos();
		rawLogModelInfos();
		/*
		 * initTimeZones();
		 */

		LRLog.setLevel(LRLog.LEVEL_SILENT);
		if (debugLevel() >= 1) {
			LRLog.setLevel(LRLog.LEVEL_BASIC);
			if (debugLevel() >= 2) {
				LRLog.setLevel(LRLog.LEVEL_DEBUG);
			}
		}

		// modification de la table de l'entite Affectation du modele Grhum
		// et de ul_Affectation du modele UserLogin si necessaire
		if (!StringCtrl.isEmpty(otherTableNameAffectation())) {
			EOModel grhumModel = (EOModel) EOModelCtrl.getModelsDico().objectForKey("Grhum");
			EOEntity affectationEntity = grhumModel.entityNamed(EOAffectation.ENTITY_NAME);
			affectationEntity.setExternalName(otherTableNameAffectation());
			EOModel useLoginModel = (EOModel) EOModelCtrl.getModelsDico().objectForKey("UserLogin");
			EOEntity ulAffectationEntity = useLoginModel.entityNamed("ul_Affectation");
			ulAffectationEntity.setExternalName(otherTableNameAffectation());
		}

		// initialisation de la classe d'alertes
		EOAlerte.initStaticFields(
				debug(),
				mailBus(),
				dataBus(),
				config().stringForKey(APP_URL_KEY),
				appUseSam(),
				config().stringForKey("APP_ADMIN_MAIL"),
				config().stringForKey("APP_SAM_MAIL"));

		// creation automatique des parametres non annualisés absents si besoin
		for (Parametre parametre : Parametre.ALL_PARAMETRE_NON_ANNUALISE) {
			// cet appel va faire la création automatique avec la valeur par defaut
			// si le parametre n'existe pas
			parametre.propagate();
		}

		/*
		 * for (Parametre parametre : Parametre.ALL_PARAMETRE_NON_ANNUALISE) {
		 * parametre.propagate(); }
		 */

		// initialisation des heures dues
		EOAffectationAnnuelle.initStaticFields(
				retrieveDicoHeuresDues(),
				retrieveDicoDateMaxReliquat(),
				retrieveDicoDateDebutDemandeCet(),
				retrieveDicoDateFinDemandeCet(),
				retrieveDicoSeuilCongesConsommesJour7h00Nm1PourEpargneCet(),
				retrieveDicoSeuilReliquatHeuresPourEpargneCet(),
				retrieveDicoPlafondEpargneCet());

	}

	public String mainModelName() {
		return "UserLogin";
	}

	public String configTableName() {
		return "ul_GrhumParametres";
	}

	public String configFileName() {
		return "Conges.config";
	}

	/**
	 * Constantes de l'application
	 */
	private Boolean _debug;
	private String _cStructureAdmin, _cStructureDrh;
	// private Boolean _isHoraireForceFermeture; // les fermetures sont forcees a
	// 35h00 ?
	private Boolean _appUseSam;
	private String _appVersionDateInstanceInfo;
	private String _appGrhumVille;
	private String _otherTableNameAffectation;
	private String _appNoteCetUrl;

	public boolean debug() {
		if (_debug == null)
			_debug = new Boolean(config().booleanForKey("DEBUG"));
		return _debug.booleanValue();
	}

	public String cStructureAdmin() {
		if (_cStructureAdmin == null)
			_cStructureAdmin = config().stringForKey("C_STRUCTURE_ADMIN");
		return _cStructureAdmin;
	}

	public String cStructureDrh() {
		if (_cStructureDrh == null)
			_cStructureDrh = config().stringForKey("C_STRUCTURE_DRH");
		return _cStructureDrh;
	}

	public boolean appUseSam() {
		if (_appUseSam == null)
			_appUseSam = new Boolean(config().booleanForKey("APP_USE_SAM"));
		return _appUseSam.booleanValue();
	}

	public String appGrhumVille() {
		if (_appGrhumVille == null)
			_appGrhumVille = config().stringForKey("GRHUM_VILLE");
		return _appGrhumVille;
	}

	private String otherTableNameAffectation() {
		if (_otherTableNameAffectation == null)
			_otherTableNameAffectation = config().stringForKey(ConstsApplication.CONFIG_OTHER_TABLE_NAME_AFFECTATION_KEY);
		return _otherTableNameAffectation;
	}

	public String appNoteCetUrl() {
		if (_appNoteCetUrl == null) {
			_appNoteCetUrl = config().stringForKey(ConstsApplication.CONFIG_NOTE_CET_URL_KEY);
		}
		return _appNoteCetUrl;
	}

	private int _extNotifCode = ConstsApplication.EXT_NOTIF_CODE_UNDEFINED;

	/**
	 * La facon dont on surveille les verifications survenues dans les donnees
	 * externes, du type la table AFFECTATION de GRHUM. 2 types possibles :
	 * DirectAction ou Trigger.
	 */
	public int extNotifCode() {
		if (_extNotifCode == ConstsApplication.EXT_NOTIF_CODE_UNDEFINED) {
			String extNotif = config().stringForKey(ConstsApplication.KEY_CONFIG_EXT_NOTIF);
			_extNotifCode = ConstsApplication.EXT_NOTIF_CODE_NONE;
			if (!StringCtrl.isEmpty(extNotif)) {
				if (ConstsApplication.EXT_NOTIF_LABEL_DIRECT_ACTION.equals(extNotif)) {
					_extNotifCode = ConstsApplication.EXT_NOTIF_CODE_DIRECT_ACTION;
				} else if (ConstsApplication.EXT_NOTIF_LABEL_TRIGGER.equals(extNotif)) {
					_extNotifCode = ConstsApplication.EXT_NOTIF_CODE_TRIGGER;
				}
			}
		}
		return _extNotifCode;
	}

	/**
	 * methode lancee immediatement apres le constructeur
	 */
	public void startRunning() {
		// lancer les demons de gestion de donnees externes
		TasksLauncher tasksLauncher = new TasksLauncher();
		tasksLauncher.start();
		// la stat a son cricri
		// _ZStatTmp.doStats();
		// System.exit(-1);
	}

	/**
	 * Retourne la liste des heures dues par annee universitaire sous forme de
	 * paires cle>valeur ; (String)"01/09/2004">(String)"1542:15".
	 * 
	 * Ces valeurs sont lues depuis l'entite <code>Parametre</code>, parmi toutes
	 * les cles qui commence par la chaine 'VOLUME_HORAIRE_ANNUEL_'
	 */
	public NSDictionary retrieveDicoHeuresDues() {
		return EOParametre.retrieveDicoParam(new EOEditingContext(), Parametre.PREFIX_PARAM_HEURES_DUES, Parametre.PARAM_VALUE_TYPE_STRING);
	}

	/**
	 * Retourne la liste des dates max de reliquat par annee universitaire sous
	 * forme de paires cle>valeur ; (String)"01/09/2004">(String)31/12/2004.
	 * 
	 * Ces valeurs sont lues depuis l'entite <code>Parametre</code>, parmi toutes
	 * les cles qui commence par la chaine 'DATE_MAX_RELIQUAT_'
	 */
	public NSDictionary retrieveDicoDateMaxReliquat() {
		return EOParametre.retrieveDicoParam(new EOEditingContext(), Parametre.PREFIX_PARAM_DATE_MAX_RELIQUAT, Parametre.PARAM_VALUE_TYPE_STRING);
	}

	/**
	 * Retourne la liste des dates de debut de demande CET par annee universitaire
	 * sous forme de paires cle>valeur ; (String)"01/09/2004">(String)28/02/2005.
	 * 
	 * Ces valeurs sont lues depuis l'entite <code>Parametre</code>, parmi toutes
	 * les cles qui commence par la chaine 'PREFIX_DEBUT_DEMANDE_CET'
	 */
	public NSDictionary retrieveDicoDateDebutDemandeCet() {
		return EOParametre.retrieveDicoParam(new EOEditingContext(), Parametre.PREFIX_DEBUT_DEMANDE_CET, Parametre.PARAM_VALUE_TYPE_STRING);
	}

	/**
	 * Retourne la liste des dates de fin de demande CET par annee universitaire
	 * sous forme de paires cle>valeur ; (String)"01/09/2004">(String)28/02/2005.
	 * 
	 * Ces valeurs sont lues depuis l'entite <code>Parametre</code>, parmi toutes
	 * les cles qui commence par la chaine 'DATE_FIN_DEMANDE_CET_'
	 */
	public NSDictionary retrieveDicoDateFinDemandeCet() {
		return EOParametre.retrieveDicoParam(new EOEditingContext(), Parametre.PREFIX_FIN_DEMANDE_CET, Parametre.PARAM_VALUE_TYPE_STRING);
	}

	/**
	 * Retourne la liste des seuils jours à 7h00 pour pouvoir demander une epargne
	 * CET, par annee universitaire sous forme de paires cle>valeur ;
	 * (String)"01/09/2004">(String)"20".
	 * 
	 * Ces valeurs sont lues depuis l'entite <code>Parametre</code>, parmi toutes
	 * les cles qui commence par la chaine
	 * 'SEUIL_CONGES_CONSOMMES_JOUR_7H00_NM1_POUR_EPARGNE_CET_'
	 */
	public NSDictionary retrieveDicoSeuilCongesConsommesJour7h00Nm1PourEpargneCet() {
		return EOParametre.retrieveDicoParam(new EOEditingContext(), Parametre.PREFIX_SEUIL_CONGES_CONSOMMES_JOUR_7H00_NM1_POUR_EPARGNE_CET, Parametre.PARAM_VALUE_TYPE_STRING);
	}

	/**
	 * Retourne la liste des minimums d'heures de reliquat pour pouvoir demander
	 * une epargne CET, par annee universitaire sous forme de paires cle>valeur ;
	 * (String)"01/09/2004">(String)"14:00".
	 * 
	 * Ces valeurs sont lues depuis l'entite <code>Parametre</code>, parmi toutes
	 * les cles qui commence par la chaine
	 * 'SEUIL_RELIQUAT_HEURES_POUR_EPARGNE_CET_'
	 */
	public NSDictionary retrieveDicoSeuilReliquatHeuresPourEpargneCet() {
		return EOParametre.retrieveDicoParam(new EOEditingContext(), Parametre.PREFIX_SEUIL_RELIQUAT_HEURES_POUR_EPARGNE_CET, Parametre.PARAM_VALUE_TYPE_STRING);
	}

	/**
	 * Retourne la liste des maximums de jours à 7h00 de reliquat d'une demande
	 * d'epargne CET, par annee universitaire sous forme de paires cle>valeur ;
	 * (String)"01/09/2004">(String)"14:00".
	 * 
	 * Ces valeurs sont lues depuis l'entite <code>Parametre</code>, parmi toutes
	 * les cles qui commence par la chaine 'PREFIX_PLAFOND_EPARGNE_CET_'
	 */
	public NSDictionary retrieveDicoPlafondEpargneCet() {
		return EOParametre.retrieveDicoParam(new EOEditingContext(), Parametre.PREFIX_PLAFOND_EPARGNE_CET, Parametre.PARAM_VALUE_TYPE_STRING);
	}

	/**
	 * TODO provient de YCRIFwk pour avoir CRIAlertPage -> pb depuis passage dans
	 * les package gestion des exceptions : redirection vers une page d'erreur
	 * puis envoi du mail a l'adresse {@link #appErrorMail()} si defini, sinon a
	 * adm
	 */
	public WOResponse handleException(Exception anException, WOContext aContext) {
		LRLog.log("- Exception");
		anException.printStackTrace();
		if (anException.getCause() != null) {
			LRLog.log("- Exception.getCause()");
			anException.getCause().printStackTrace();
		}
		LRLog.log("-----------");
		String contenuPage =
				"Une erreur est survenue dans l'application ...<br>" +
						"Un mail contenant le message d'erreur a été envoyé a l'administrateur de l'application.<br>" +
						"L'erreur sera corrigée au plus vite.<br><br>" +
						"Contenu de l'erreur :<br><br>" +
						"<textarea cols='75' rows='15'>" +
						UtilException.stackTraceToString(anException, false) +
						(anException.getCause() != null ?
								"\nCaused by : " + UtilException.stackTraceToString(anException.getCause(), false) : "") +
						"</textarea><br><br>" +
						"<center><a href='" + getApplicationInstanceURL(aContext) + "'>Fermer la session</a></center>";

		CRIAlertPage page = (CRIAlertPage) pageWithName(CRIAlertPage.class.getName(), aContext);
		page.showMessage(null, "Une erreur est survenue ...", contenuPage, null, null, null, CRIAlertPage.ERROR, null);
		String contenuMail = "Une exception est survenue ...\n";
		contenuMail += "Elle provient de la machine qui a l'ip *" + getRequestIPAddress(aContext.request()) + "*\n";
		contenuMail += "La personne qui a provoqué l'erreur est : ";
		Session cngSession = ((Session) aContext.session());
		CngUserInfo uiConnected = cngSession.cngUserInfo();
		if (uiConnected != null) {
			contenuMail += "*" + uiConnected.nomEtPrenom() + "*\n\n";
			contenuMail += uiConnected.toString();
		} else {
			contenuMail += "????????]";
		}
		contenuMail += "\nsessionId = " + cngSession.sessionID();
		contenuMail += "\n\n";
		//
		String to = appAdminMail();
		String from = to;
		if (uiConnected != null && !StringCtrl.isEmpty(uiConnected.email())) {
			from = uiConnected.email();
		}
		UtilException.sendExceptionTrace(mailBus(), name(), from, to, contenuMail, anException);
		WOResponse response = page.generateResponse();
		return response;
	}

	/**
	 * Les informations sur la base de donnees : - no version - date version -
	 * connection bdd
	 */
	public String appVersionDateInstanceInfo() {
		if (_appVersionDateInstanceInfo == null) {
			_appVersionDateInstanceInfo = appVersionCocktail().htmlVersion();
		}
		return _appVersionDateInstanceInfo;
	}

	// ** controle de version **

	// 1 seule instance
	private VersionCocktail _appVersionCocktail;

	// controle de versions
	public VersionCocktail appVersionCocktail() {
		if (_appVersionCocktail == null) {
			_appVersionCocktail = new fr.univlr.cri.conges.Version();
		}
		return _appVersionCocktail;
	}

	// activation du service de collecte automatique
	public boolean appShouldSendCollecte() {
		return true;
	}

	// 1 seule instance
	private VersionCocktail _appVersionCocktailDb;

	// collecte : lecture de la version de la base installee
	public VersionCocktail appVersionCocktailDb() {
		if (_appVersionCocktailDb == null) {
			_appVersionCocktailDb = new fr.univlr.cri.conges.VersionOracleDbConges();
		}
		return _appVersionCocktailDb;
	}

	// ** parametres **

	/**
	 * Les parametres obligatoires
	 */
	public String[] configMandatoryKeys() {
		return new String[] { APP_USE_CAS_KEY, "APP_ID", "APP_TIME_ZONE", "DEBUG_LEVEL",
				"APP_ADMIN_MAIL", "APP_LOG_CONNEXION", MAIN_LOGO_URL_KEY, "MAIN_WEB_SITE_URL",
				"C_STRUCTURE_ADMIN", ConstsApplication.CONFIG_C_STRUCTURE_DRH_KEY, "DEBUG", "APP_USE_SAM",
				"GRHUM_VILLE",
				ConstsApplication.CONFIG_GRHUM_PRESIDENT_KEY,
				ConstsApplication.CONFIG_GRHUM_ETAB_KEY };
	}

	/**
	 * Les parametres optionnels de GHRUM_PARAMETRES
	 */
	public String[] configOptionalKeys() {
		return new String[] { "HTML_CSS_STYLES", "HTML_IMAGES_ROOT", "HTML_JSCRIPT_ROOT", "HTML_URL_LOGOS",
				ConstsApplication.CONFIG_OTHER_TABLE_NAME_AFFECTATION_KEY, "APP_SAM_MAIL",
				ConstsApplication.CONFIG_APP_DATECTRL_ADDITIONAL_HOLIDAY_KEY, ConstsApplication.CONFIG_APP_DATECTRL_IGNORING_HOLIDAY_KEY };
	}

	// TODO integrer la notion de params custom dans CRIWebApp
	/*
	 * private final static String SB_SEPARATOR = ", ";
	 * 
	 * // variables facultatives
	 * 
	 * private boolean checkCustomParams() { boolean hasMandatoryMissing = false;
	 * StringBuffer sb = new
	 * StringBuffer("Controle de la presence des valeurs dans " +
	 * EOParametre.ENTITY_TABLE_NAME + "\n" +
	 * "-------------------------------------------------------\n\n");
	 * StringBuffer sbMandatoryMissing = new StringBuffer(); Hashtable<String,
	 * Object> hMandatoryFound = new Hashtable<String, Object>(); String[] keys =
	 * listeVariablesCustomParametre(); if (keys != null && keys.length > 0) { for
	 * (int i = 0; i < keys.length; i++) { String key = (String) keys[i]; Object
	 * value = customConfigStringForKey(key); if (value == null) {
	 * sbMandatoryMissing.append(key).append(SB_SEPARATOR); hasMandatoryMissing =
	 * true; } else { hMandatoryFound.put(key, value); } } // enlever le dernier
	 * separateur et ajouter le message d'erreur if (sbMandatoryMissing.length() >
	 * 0) { int sbSize = sbMandatoryMissing.length();
	 * sbMandatoryMissing.replace(sbSize - SB_SEPARATOR.length(), sbSize, "");
	 * sbMandatoryMissing.insert(0, "  > Valeurs de " +
	 * EOParametre.ENTITY_TABLE_NAME + " absents : ");
	 * sbMandatoryMissing.append(" - ERREUR"); } else {
	 * sbMandatoryMissing.insert(0, "  > Tous les valeurs de " +
	 * EOParametre.ENTITY_TABLE_NAME + " sont presentes - OK"); }
	 * sb.append(sbMandatoryMissing).append("\n");
	 * sb.append(getFormatted(hMandatoryFound)); } LRLog.rawLog(sb.toString());
	 * return !hasMandatoryMissing; }
	 */
	private static String createKeyValue(final String key, final Object value, int keyLength) {
		if (key.length() > keyLength) {
			keyLength = key.length();
		}
		return StringCtrl.extendWithChars(key, " ", keyLength, false) + " = " + value;
	}

	/**
	 * Afficher le contenu formaté clé = valeur d'une {@link Hashtable}
	 */
	/*
	 * private static StringBuffer getFormatted(Hashtable<?,?> hashtable) { //
	 * determiner la taille de cle la plus long int maxLength = 0; Enumeration<?>
	 * enumKeys = hashtable.keys(); while (enumKeys.hasMoreElements()) { String
	 * key = (String) enumKeys.nextElement(); if (key.length() > maxLength) {
	 * maxLength = key.length(); } } // affichage StringBuffer sb = new
	 * StringBuffer(); enumKeys = hashtable.keys(); while
	 * (enumKeys.hasMoreElements()) { String key = (String)
	 * enumKeys.nextElement(); String value = (String) hashtable.get(key);
	 * sb.append("    * ").append(createKeyValue(key, value,
	 * maxLength)).append("\n"); } sb.append("\n"); return sb; }
	 */

	// arret de l'application

	/**
	 * Execute la commande d'arret de l'application
	 */
	public void appStop(CngUserInfo ui) {
		LRLog.log(">> Arret de l'application");
		// notifier l'administrateur de l'arret
		String contactMail = contactMail();
		if (!StringCtrl.isEmpty(contactMail)) {
			mailBus().sendMail(contactMail, contactMail, null, "Arret de HAmAC",
					"L'application vient d'etre arretee par " + ui.nomEtPrenom());
		}
		System.exit(-1);
	}

	/**
	 * Initialise le TimeZone a utiliser pour l'application.
	 */
	/*
	 * private void initTimeZones() { //appLog().trace(new
	 * NSArray(TimeZone.getAvailableIDs()));
	 * LRLog.log("Initialisation du NSTimeZone"); LRLog.log(
	 * "NSTimeZone par defaut recupere sur le systeme (avant initialisation) " +
	 * NSTimeZone.defaultTimeZone()); String tz = (String)
	 * config().valueForKey("APP_TIME_ZONE");
	 * 
	 * // config().stringForKey("DEFAULT_TIME_ZONE") if (StringCtrl.isEmpty(tz)) {
	 * LRLog
	 * .log("Le parametre APP_TIME_ZONE n'est pas defini dans le fichier .config."
	 * ); TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
	 * NSTimeZone.setDefaultTimeZone(NSTimeZone.timeZoneWithName("Europe/Paris",
	 * false)); } else { NSTimeZone ntz = NSTimeZone.timeZoneWithName(tz, false);
	 * if (ntz == null) { LRLog.log(
	 * "Le parametre APP_TIME_ZONE defini dans le fichier .config n'est pas valide ("
	 * + tz + ")"); TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
	 * NSTimeZone.setDefaultTimeZone(NSTimeZone.timeZoneWithName("Europe/Paris",
	 * false)); } else { TimeZone.setDefault(ntz);
	 * NSTimeZone.setDefaultTimeZone(ntz); } }
	 * LRLog.log("NSTimeZone par defaut utilise dans l'application " +
	 * NSTimeZone.defaultTimeZone()); NSTimestampFormatter ntf = new
	 * NSTimestampFormatter();
	 * LRLog.log("Les NSTimestampFormatter analyseront les dates avec le NSTimeZone "
	 * + ntf.defaultParseTimeZone());
	 * LRLog.log("Les NSTimestampFormatter afficheront les dates avec le NSTimeZone "
	 * + ntf.defaultFormatTimeZone()); }
	 */

}