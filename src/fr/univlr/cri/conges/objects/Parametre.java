package fr.univlr.cri.conges.objects;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.databus.CngParametreBus;
import fr.univlr.cri.conges.eos.modele.conges.EOParametre;
import fr.univlr.cri.conges.eos.modele.planning.I_ClasseMetierNotificationParametre;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.LRLog;
import fr.univlr.cri.webapp.LRRecord;

public class Parametre {

	// types possibles des variables dans la table de parametres
	public final static int PARAM_VALUE_TYPE_INTEGER 	= 0;
	public final static int PARAM_VALUE_TYPE_STRING 	= 1;
	public final static int PARAM_VALUE_TYPE_BOOLEAN 	= 2;
	public final static int PARAM_VALUE_TYPE_FLOAT 		= 3;
	
	// les interfaces a afficher par parametre
	public final static int TYPE_INTERFACE_HEURES_LIMITE_24		= 0;
	public final static int TYPE_INTERFACE_HEURES_SANS_LIMITE = 1;
	public final static int TYPE_INTERFACE_DATE 							= 2;
	public final static int TYPE_INTERFACE_TEXTFIELD_TEXTE		= 3;
	public final static int TYPE_INTERFACE_TEXTFIELD_ENTIER		= 4;
	public final static int TYPE_INTERFACE_TEXTFIELD_1_NBR_VIR= 5;
	public final static int TYPE_INTERFACE_CASE_A_COCHER 			= 6;
	public final static int TYPE_INTERFACE_ZONE_DE_TEXTE 			= 7;
	public final static int TYPE_INTERFACE_POPUP				 			= 8;
	
	//
	private String paramKey;
	private boolean isAnnualise;
	private Class<I_ClasseMetierNotificationParametre>[] classeMetierArray;
	private String defaultValueString;
	private int paramType;
	private int typeInterfaceSaisie;
	
	// les valeurs par defaut pour les dates ...
	public final static String DATE_FIN_ANNEE_CIVILE 								= "DATE_FIN_ANNEE_CIVILE";
	public final static String DATE_FIN_ANNEE_CIVILE_PLUS_2_MOIS 		= "DATE_FIN_ANNEE_CIVILE_PLUS_2_MOIS";
	
	public Parametre(
			String aParamKey, 
			boolean anIsAnnualise, 
			Class<I_ClasseMetierNotificationParametre>[] aClasseMetierArray, 
			String aDefaultValueString, 
			int aParamType,
			int aTypeInterfaceSaisie) {
		super();
		paramKey = aParamKey;
		isAnnualise = anIsAnnualise;
		classeMetierArray = aClasseMetierArray;
		defaultValueString = aDefaultValueString;
		paramType = aParamType;
		typeInterfaceSaisie = aTypeInterfaceSaisie;
	}


	public final String getParamKey() {
		return paramKey;
	}


	public final boolean isAnnualise() {
		return isAnnualise;
	}


	public final Class<I_ClasseMetierNotificationParametre>[] getClasseMetierArray() {
		return classeMetierArray;
	}


	public final String getDefaultValueString() {
		return defaultValueString;
	}


	public final int getParamType() {
		return paramType;
	}


	public final int getTypeInterfaceSaisie() {
		return typeInterfaceSaisie;
	}
	
	
	public final boolean isParamTypeInteger() {
		return paramType == PARAM_VALUE_TYPE_INTEGER;
	}
	
	
	public final boolean isTypeInterfaceHeuresSansLimite() {
		return getTypeInterfaceSaisie() == TYPE_INTERFACE_HEURES_SANS_LIMITE;
	}
	
	
	public final boolean isTypeInterfaceHeuresLimite24() {
		return getTypeInterfaceSaisie() == TYPE_INTERFACE_HEURES_LIMITE_24;
	}

	
	public final boolean isTypeInterfaceDate() {
		return getTypeInterfaceSaisie() == TYPE_INTERFACE_DATE;
	}
	
	
	public final boolean isTypeInterfaceCaseACocher() {
		return getTypeInterfaceSaisie() == TYPE_INTERFACE_CASE_A_COCHER;
	}
	
	
	public final boolean isTypeInterfaceTextfieldTexte() {
		return getTypeInterfaceSaisie() == TYPE_INTERFACE_TEXTFIELD_TEXTE;
	}
	
	
	public final boolean isTypeInterfaceTextfieldEntier() {
		return getTypeInterfaceSaisie() == TYPE_INTERFACE_TEXTFIELD_ENTIER;
	}
	
	
	public final boolean isTypeInterfaceTextfield1NombreApresVirgule() {
		return getTypeInterfaceSaisie() == TYPE_INTERFACE_TEXTFIELD_1_NBR_VIR;
	}
	
	
	public final boolean isTypeInterfaceZoneDeTexte() {
		return getTypeInterfaceSaisie() == TYPE_INTERFACE_ZONE_DE_TEXTE;
	}
	
	
	public final boolean isTypeInterfacePopup() {
		return getTypeInterfaceSaisie() == TYPE_INTERFACE_POPUP;
	}
	
	
	
	// transactions avec la base de données

	/** l'année universitaire si parametre annualisé */
	public NSTimestamp anneeUniv;
	/** la valeur du parametre en String */
	private String paramValueString;
	// valeur precedente pour detecter les changements
	private String prevParamValueString;
	/** la valeur du parametre en NSTimestamp */
	private NSTimestamp paramValueNSTimestamp;
	/** la valeur du parametre en Boolean */
	private Boolean paramValueBoolean;
	/** la valeur du parametre en Integer */
	private Integer paramValueInteger;
	/** la valeur du parametre en Float */
	private Float paramValueFloat;
	
  /** indique si un des parametre a été créé de facon automatique */
  private boolean isParamCrationAutomatique;
  

  // bug de CRITimeField : les getters sont appelée juste après le setters avant la sauvegarde
  //TODO mettre en private
  /*public boolean shouldIgnoreGetterTimeField;*/
	
	/**
	 * La clé complète pour un parametre annualisé (préfixe + suffixe)
	 * Utilisé aussi pour faire un identifiant unique
	 * @return
	 */
	public String fullParamKey() {
		String fullParamKey = getParamKey();
		
		if (isAnnualise()) {
			String strAnnee = DateCtrlConges.anneeUnivForDate(
	  			DateCtrlConges.dateToDebutAnneeUniv(anneeUniv)).substring(0,4);
			
			fullParamKey += strAnnee;
		}
		
		return fullParamKey;
	}
	

	/**
   * Mise a jour des donnees saisies dans la table de parametres
   * (entite <code>Parametre</code>).
   * 
   * La methode fait ensuite appel aux methodes statiques qui
   * reinitilise les classes metiers qui utilisent ces valeurs
   */
  public void updateParams() {
  	
  	// determiner s'il faut sauver (oui s'il y a eu un changement)
  	boolean shouldSave = true;
  	if (!isParamCrationAutomatique && 
  			paramValueString != null && 
  			prevParamValueString != null && 
  			paramValueString.equals(prevParamValueString)) {
  		shouldSave = false;
  	}
  	
  	if (shouldSave) {

    	// la valeur qui sera réellement enregistrée en base
    	String paramValueStringToSave = paramValueString;
    	String prevParamValueStringSaved = (isParamCrationAutomatique ? "null" : prevParamValueString);
    	// cas particuliers : il faut modifier la valeur à sauvegarder
    	// pour les heures et leur stockage (en heures ou minutes .....)
  		if (isTypeInterfaceHeuresLimite24() || 
  				isTypeInterfaceHeuresSansLimite()) {
  			// il faut transformer les minutes en heures
  			if (isParamTypeInteger()) {
  				paramValueStringToSave = Integer.toString(TimeCtrl.getMinutes(paramValueString));
  				prevParamValueStringSaved = (isParamCrationAutomatique ? "null" : Integer.toString(TimeCtrl.getMinutes(prevParamValueString)));
  			}
  		}
    	
    	// sauvegarde dans la base de données
    	paramBus().updateParametre(
      		null, fullParamKey(), paramValueStringToSave);
   
    	LRLog.log("Sauvegarde du parametre '" + fullParamKey() + "' : '" + paramValueStringToSave + "' "
    			+ "(valeur precedente '" + prevParamValueStringSaved+"')");
    	
    	// pour éviter les boucles infinies
    	isParamCrationAutomatique = false;
    	
    	// propagation vers les classes métier
    	propagate();
    	
    	// memoriser la derniere valeur sauvegardée
    	prevParamValueString = paramValueString;

  	}
  	
  }
  
  /**
   * Propager la valeur du parametre vers l'ensemble des classes
   * métiers impactées
   */
  public void propagate() {
  	
  	// on test si paramValueString est non vide car cette methode 
  	// est aussi appelée au lancement de l'application
  	
  	if (StringCtrl.isEmpty(paramValueString)) {
  		getParamValueString();
  	}
  	
  	LRLog.trace("Application de la valeur du parametre '" + fullParamKey() + "' : '" + paramValueString + "'");
  	for (Class<?> classeMetier : getClasseMetierArray()) {
  		Class<?>[] types = new Class[]{Parametre.class};
  		Method mainMethod = null;
			try {
				mainMethod = classeMetier.getMethod(
						I_ClasseMetierNotificationParametre.INIT_STATIC_FIELD_STATIC_METHOD_NAME, types);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
  		try {
				mainMethod.invoke(null, new Object[]{this});
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			LRLog.trace(" > vers la classe metier " + classeMetier.getName());
  	}
  }
  
  
  // getters
  

  /**
   * Recuperer le parametre {@link String}
   */
  public String getParamValueString() {
  
		String paramKey = fullParamKey();
		LRRecord rec = paramBus().fetchParametre(ec(), paramKey);
		if (rec == null) {

			// creation du parametre avec sa valeur par défaut
			String paramDefaultValue = getDefaultValueString();
			
			// cas particulier pour les dates 
			if (isTypeInterfaceDate()) {
				if (paramDefaultValue.equals(Parametre.DATE_FIN_ANNEE_CIVILE)) {
					paramDefaultValue = DateCtrlConges.dateToString(
      				DateCtrlConges.dateToFinAnneeCivile(anneeUniv));
				} else if (paramDefaultValue.equals(Parametre.DATE_FIN_ANNEE_CIVILE_PLUS_2_MOIS)) {
					paramDefaultValue = DateCtrlConges.dateToString(
      				DateCtrlConges.dateToFinAnneeCivile(anneeUniv).timestampByAddingGregorianUnits(0,2,0,0,0,0));
				}
			}
			
			rec = paramBus().addParametre(
					null, paramKey, paramDefaultValue);
			isParamCrationAutomatique = true;
		}
		paramValueString = rec.stringForKey(EOParametre.PARAM_VALUE_KEY);
		
  	// memoriser la derniere valeur issue de la base
  	prevParamValueString = paramValueString;
		
		// cas particuliers pour les heures et leur stockage (en heures ou minutes .....)
		if (isTypeInterfaceHeuresLimite24() || 
				isTypeInterfaceHeuresSansLimite()) {
			// il faut transformer les minutes en heures
			if (isParamTypeInteger()) {
				paramValueString = TimeCtrl.stringForMinutes(Integer.parseInt(paramValueString));
				prevParamValueString = TimeCtrl.stringForMinutes(Integer.parseInt(prevParamValueString));
			}
		}
		
  	
  	// sauvegarde immédiate en cas de création automatique par l'application
  	if (isParamCrationAutomatique) {
  		updateParams();
  		isParamCrationAutomatique = false;
  	}
			
    return paramValueString;
  }

  /**
   * Recuperer le parametre en {@link NSTimestamp}
   */
  public NSTimestamp getParamValueNSTimestamp() {
  	paramValueNSTimestamp = DateCtrlConges.stringToDate(getParamValueString());
    return paramValueNSTimestamp;
  }

  /**
   * Recuperer le parametre en {@link Boolean}
   */
  public Boolean getParamValueBoolean() {
  	paramValueBoolean = getParamValueString().equals("YES") ? Boolean.TRUE : Boolean.FALSE;
    return paramValueBoolean;
  }

  /**
   * Recuperer le parametre en {@link Integer}
   */
  public Integer getParamValueInteger() {
  	paramValueInteger = new Integer(Integer.parseInt(getParamValueString()));
    return paramValueInteger;
  }

  /**
   * Recuperer le parametre en {@link Float}
   */
  public Float getParamValueFloat() {
  	paramValueFloat = new Float(Float.parseFloat(getParamValueString()));
    return paramValueFloat;
  }
 
    
  // setters
  
  /**
   * @param string
   */
  public void setParamValueString(String string) {
  	paramValueString = string;
  	/*// Gestion du bug de CRITimeField
  	if (isTypeInterfaceHeuresSansLimite() ||
  			isTypeInterfaceHeuresLimite24()) {
    	shouldIgnoreGetterTimeField = true;
  	}*/
  }
  
  /**
   * @param nsTimestamp
   */
  public void setParamValueNSTimestamp(NSTimestamp nsTimestamp) {
  	paramValueNSTimestamp = nsTimestamp;
  	setParamValueString(DateCtrlConges.dateToString(paramValueNSTimestamp));
  }
  
  /**
   * @param bolean
   */
  public void setParamValueBoolean(Boolean bolean) {
  	paramValueBoolean = bolean;
  	setParamValueString(paramValueBoolean.booleanValue() == true ? "YES" : "NO");
  }
  
  /**
   * @param integer
   */
  public void setParamValueInteger(Integer integer) {
  	paramValueInteger = integer;
  	setParamValueString(Integer.toString(paramValueInteger.intValue()));
  }
  
  /**
   * @param floate
   */
  public void setParamValueFloat(Float floate) {
  	paramValueFloat = floate;
  	setParamValueString(Float.toString(paramValueFloat.floatValue()));
  }
  
    
  
  // ** les bus de donnees **
  
  private static EOEditingContext _ec;
  private static CngParametreBus _paramBus;
  
  private static EOEditingContext ec() {
  	if (_ec == null) {
  		_ec = new EOEditingContext();
  	}
  	return _ec;
  }
  
  private static CngParametreBus paramBus() {
  	if (_paramBus == null) {
  		_paramBus = new CngParametreBus(ec());
  	}
    return _paramBus;
  }


	public final NSTimestamp getAnneeUniv() {
		return anneeUniv;
	}


	public final void setAnneeUniv(NSTimestamp anneeUniv) {
		this.anneeUniv = anneeUniv;
	}
  
	
	// ///////////// //
	// constantes 
	// ///////////// //
  
	// parametres annualisés (préfixé, suivis de l'année concernée)
	public final static String PREFIX_PLAFOND_EPARGNE_CET 																															= "PLAFOND_EPARGNE_CET_";
	public final static String PREFIX_SEUIL_RELIQUAT_HEURES_POUR_EPARGNE_CET 																= "SEUIL_RELIQUAT_HEURES_POUR_EPARGNE_CET_";
	public final static String PREFIX_SEUIL_CONGES_CONSOMMES_JOUR_7H00_NM1_POUR_EPARGNE_CET 	= "SEUIL_CONGES_CONSOMMES_JOUR_7H00_NM1_POUR_EPARGNE_CET_";
	public final static String PREFIX_FIN_DEMANDE_CET																																		= "DATE_FIN_DEMANDE_CET_";
	public final static String PREFIX_DEBUT_DEMANDE_CET																																	= "DATE_DEBUT_DEMANDE_CET_";
	public final static String PREFIX_PARAM_DATE_MAX_RELIQUAT 																													= "DATE_MAX_RELIQUAT_";
	public final static String PREFIX_PARAM_HEURES_DUES 																																= "VOLUME_HORAIRE_ANNUEL_";
	
	

	// parametres non annualisés
	private final static String DUREE_HORAIRE_HEBDO_MINI 																																								= "DUREE_HORAIRE_HEBDO_MINI";
	private final static String DUREE_HORAIRE_HEBDO_MAXI 																																								= "DUREE_HORAIRE_HEBDO_MAXI";
	private final static String DUREE_HORAIRE_HEBDO_MAXI_HORS_NORMES 																																	= "DUREE_HORAIRE_HEBDO_MAXI_HORS_NORMES";
	private final static String DUREE_JOUR_CONVERSION 																																										= "DUREE_JOUR_CONVERSION";
	private final static String LOCK_HORAIRES_TYPES 																																											= "LOCK_HORAIRES_TYPES";
	
	private final static String MESSAGE_EDITION_DEMANDE_EPARGNE_CET	 																																	= "MESSAGE_EDITION_DEMANDE_EPARGNE_CET";
	private final static String VERIFIER_STATUT_DEMANDE_EPARGNE_CET 																																		= "VERIFIER_STATUT_DEMANDE_EPARGNE_CET";
	private final static String AUTORISER_DEMANDE_EPARGNE_CET_CDD_NON_CDI 																												= "AUTORISER_DEMANDE_EPARGNE_CET_CDD_NON_CDI";
	private final static String CET_OPTION_ANCIEN_SYSTEME_AU_DELA_20_JOURS 																											= "CET_OPTION_ANCIEN_SYSTEME_AU_DELA_20_JOURS";
	private final static String AUTORISER_SAISIE_ABSENCE_SUR_PLANNING_PREVISIONNEL																				=	"AUTORISER_SAISIE_ABSENCE_SUR_PLANNING_PREVISIONNEL"; 
	private final static String AUTORISER_SAISIE_ABSENCE_SUR_PLANNING_PREVISIONNEL_EN_COURS_D_ANNEE			=	"AUTORISER_SAISIE_ABSENCE_SUR_PLANNING_PREVISIONNEL_EN_COURS_D_ANNEE"; 
	private final static String SIGNATURE_PRESIDENT																																											= "SIGNATURE_PRESIDENT"; 
		
	// classes metiers associées
  private final static Class<I_ClasseMetierNotificationParametre>[] HEURES_DUES_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle.class};
	private final static Class<I_ClasseMetierNotificationParametre>[] DATE_MAX_RELIQUAT_CLASSE_METIER_ARRAY = new Class[] {	
			fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle.class};
	private final static Class<I_ClasseMetierNotificationParametre>[] DEBUT_DEMANDE_CET_CLASSE_METIER_ARRAY = new Class[] {	
			fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle.class};
	private final static Class<I_ClasseMetierNotificationParametre>[] FIN_DEMANDE_CET_CLASSE_METIER_ARRAY = new Class[] {	
			fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle.class};
	private final static Class<I_ClasseMetierNotificationParametre>[] SEUIL_CONGES_CONSOMMES_JOUR_7H00_NM1_POUR_EPARGNE_CET_CLASSE_METIER_ARRAY = new Class[] {	
			fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle.class};
	private final static Class<I_ClasseMetierNotificationParametre>[] SEUIL_RELIQUAT_HEURES_POUR_EPARGNE_CET_CLASSE_METIER_ARRAY = new Class[] {	
			fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle.class};
	private final static Class<I_ClasseMetierNotificationParametre>[] PLAFOND_EPARGNE_CET_CLASSE_METIER_ARRAY = new Class[] {	
			fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle.class};
	
	
	private final static Class<I_ClasseMetierNotificationParametre>[] DUREE_HORAIRE_HEBDO_MINI_CLASSE_METIER_ARRAY = new Class[] {	
		fr.univlr.cri.conges.objects.HoraireHebdomadaire.class};
	private final static Class<I_ClasseMetierNotificationParametre>[] DUREE_HORAIRE_HEBDO_MAXI_CLASSE_METIER_ARRAY = new Class[] {	
		fr.univlr.cri.conges.objects.HoraireHebdomadaire.class, fr.univlr.cri.conges.eos.modele.planning.EOHoraire.class};
	private final static Class<I_ClasseMetierNotificationParametre>[] DUREE_HORAIRE_HEBDO_MAXI_HORS_NORMES_CLASSE_METIER_ARRAY = new Class[] {	
		fr.univlr.cri.conges.objects.HoraireHebdomadaire.class, fr.univlr.cri.conges.eos.modele.planning.EOHoraire.class};
	private final static Class<I_ClasseMetierNotificationParametre>[] DUREE_JOUR_CONVERSION_CLASSE_METIER_ARRAY = new Class[] {	
			fr.univlr.cri.conges.PageDetailCalculs.class, fr.univlr.cri.conges.Plannings.class, fr.univlr.cri.conges.objects.PlanningCalcul.class};
	private final static Class<I_ClasseMetierNotificationParametre>[] LOCK_HORAIRES_TYPES_CLASSE_METIER_ARRAY = new Class[] {	
			fr.univlr.cri.conges.PageAdminHoraires.class, fr.univlr.cri.conges.PersoHoraires.class};
	
	
	private final static Class<I_ClasseMetierNotificationParametre>[] MESSAGE_EDITION_DEMANDE_EPARGNE_CET_CLASSE_METIER_ARRAY = new Class[] {	
			fr.univlr.cri.conges.PageCET.class};
	private final static Class<I_ClasseMetierNotificationParametre>[] VERIFIER_STATUT_DEMANDE_EPARGNE_CET_CLASSE_METIER_ARRAY = new Class[] {	
		fr.univlr.cri.conges.objects.CetFactory.class};
	private final static Class<I_ClasseMetierNotificationParametre>[] AUTORISER_DEMANDE_EPARGNE_CET_CDD_NON_CDI_CLASSE_METIER_ARRAY = new Class[] {	
		fr.univlr.cri.conges.objects.CetFactory.class};
	private final static Class<I_ClasseMetierNotificationParametre>[] CET_OPTION_ANCIEN_SYSTEME_AU_DELA_20_JOURS_CLASSE_METIER_ARRAY = new Class[] {	
		fr.univlr.cri.conges.CetOptionAncienSysteme.class};
	private final static Class<I_ClasseMetierNotificationParametre>[] AUTORISER_SAISIE_ABSENCE_SUR_PLANNING_PREVISIONNEL_CLASSE_METIER_ARRAY = new Class[] {	
			fr.univlr.cri.conges.JourPlanning.class};
	private final static Class<I_ClasseMetierNotificationParametre>[] AUTORISER_SAISIE_ABSENCE_SUR_PLANNING_PREVISIONNEL_EN_COURS_D_ANNEE_CLASSE_METIER_ARRAY = new Class[] {	
			fr.univlr.cri.conges.JourPlanning.class};
	private final static Class<I_ClasseMetierNotificationParametre>[] SIGNATURE_PRESIDENT_CLASSE_METIER_ARRAY = new Class[] {	
			fr.univlr.cri.conges.AdminCETListeDemande.class};
	
	// valeurs par defaut
  private final static String HEURES_DUES_DEFAULT_VALUE_STRING									= "1542:00";
	private final static String DATE_MAX_RELIQUAT_DEFAULT_VALUE_STRING				= Parametre.DATE_FIN_ANNEE_CIVILE;
	private final static String DEBUT_DEMANDE_CET_DEFAULT_VALUE_STRING				= Parametre.DATE_FIN_ANNEE_CIVILE;
	private final static String FIN_DEMANDE_CET_DEFAULT_VALUE_STRING						= Parametre.DATE_FIN_ANNEE_CIVILE_PLUS_2_MOIS;
  private final static String SEUIL_CONGES_CONSOMMES_JOUR_7H00_NM1_POUR_EPARGNE_CET_DEFAULT_VALUE_STRING									= "20";
  private final static String SEUIL_RELIQUAT_HEURES_POUR_EPARGNE_CET_DEFAULT_VALUE_STRING									= "00:00";
  private final static String PLAFOND_EPARGNE_CET_DEFAULT_VALUE_STRING									= "25";
  
  
  private final static String DUREE_HORAIRE_HEBDO_MINI_DEFAULT_VALUE_STRING													= "32:00";
  private final static String DUREE_HORAIRE_HEBDO_MAXI_DEFAULT_VALUE_STRING													= "40:00";
  private final static String DUREE_HORAIRE_HEBDO_MAXI_HORS_NORMES_DEFAULT_VALUE_STRING	= "44:00";
  private final static String DUREE_JOUR_CONVERSION_DEFAULT_VALUE_STRING																= "07:20";
  private final static String LOCK_HORAIRES_TYPES_DEFAULT_VALUE_STRING																		= "NO";
  
  
  private final static String MESSAGE_EDITION_DEMANDE_EPARGNE_CET_DEFAULT_VALUE_STRING = 
		"<center>Apr&egrave;s avoir sign&eacute; ce document et l'avoir fait viser par votre responsable, "+
		"veuillez l'envoyer &agrave; la DRH via votre responsable hi&eacute;rarchique</center>";
  
  private final static String VERIFIER_STATUT_DEMANDE_EPARGNE_CET_DEFAULT_VALUE_STRING															= "NO";
  private final static String AUTORISER_DEMANDE_EPARGNE_CET_CDD_NON_CDI_DEFAULT_VALUE_STRING									= "NO"; 
  private final static String CET_OPTION_ANCIEN_SYSTEME_AU_DELA_20_JOURS_DEFAULT_VALUE_STRING								= "YES";
  private final static String AUTORISER_SAISIE_ABSENCE_SUR_PLANNING_PREVISIONNEL_DEFAULT_VALUE_STRING									= "YES";
  private final static String AUTORISER_SAISIE_ABSENCE_SUR_PLANNING_PREVISIONNEL_EN_COURS_D_ANNEE_DEFAULT_VALUE_STRING									= "YES";
  private final static String SIGNATURE_PRESIDENT_DEFAULT_VALUE_STRING									= "LE PRESIDENT";

	// classe de gestion du parametre
  public final static Parametre PARAM_HEURES_DUES = new Parametre(
  		Parametre.PREFIX_PARAM_HEURES_DUES, 
  		true,
  		HEURES_DUES_CLASSE_METIER_ARRAY, 
  		HEURES_DUES_DEFAULT_VALUE_STRING, 
  		Parametre.PARAM_VALUE_TYPE_STRING,
  		Parametre.TYPE_INTERFACE_HEURES_SANS_LIMITE);
	public final static Parametre PARAM_DATE_MAX_RELIQUAT = new Parametre(
			Parametre.PREFIX_PARAM_DATE_MAX_RELIQUAT,
			true,
			DATE_MAX_RELIQUAT_CLASSE_METIER_ARRAY, 
			DATE_MAX_RELIQUAT_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_STRING,
			Parametre.TYPE_INTERFACE_DATE);	
	public final static Parametre PARAM_DEBUT_DEMANDE_CET = new Parametre(
			Parametre.PREFIX_DEBUT_DEMANDE_CET, 
			true,
			DEBUT_DEMANDE_CET_CLASSE_METIER_ARRAY, 
			DEBUT_DEMANDE_CET_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_STRING,
			Parametre.TYPE_INTERFACE_DATE);
	public final static Parametre PARAM_FIN_DEMANDE_CET = new Parametre(
			Parametre.PREFIX_FIN_DEMANDE_CET, 
			true,
			FIN_DEMANDE_CET_CLASSE_METIER_ARRAY, 
			FIN_DEMANDE_CET_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_STRING,
			Parametre.TYPE_INTERFACE_DATE);
	public final static Parametre PARAM_SEUIL_CONGES_CONSOMMES_JOUR_7H00_NM1_POUR_EPARGNE_CET = new Parametre(
			Parametre.PREFIX_SEUIL_CONGES_CONSOMMES_JOUR_7H00_NM1_POUR_EPARGNE_CET, 
			true,
			SEUIL_CONGES_CONSOMMES_JOUR_7H00_NM1_POUR_EPARGNE_CET_CLASSE_METIER_ARRAY, 
			SEUIL_CONGES_CONSOMMES_JOUR_7H00_NM1_POUR_EPARGNE_CET_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_INTEGER,
			Parametre.TYPE_INTERFACE_TEXTFIELD_ENTIER);
	public final static Parametre PARAM_SEUIL_RELIQUAT_HEURES_POUR_EPARGNE_CET = new Parametre(
			Parametre.PREFIX_SEUIL_RELIQUAT_HEURES_POUR_EPARGNE_CET, 
			true,
			SEUIL_RELIQUAT_HEURES_POUR_EPARGNE_CET_CLASSE_METIER_ARRAY, 
			SEUIL_RELIQUAT_HEURES_POUR_EPARGNE_CET_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_STRING,
			Parametre.TYPE_INTERFACE_HEURES_SANS_LIMITE);
	public final static Parametre PARAM_PLAFOND_EPARGNE_CET = new Parametre(
			Parametre.PREFIX_PLAFOND_EPARGNE_CET, 
			true,
			PLAFOND_EPARGNE_CET_CLASSE_METIER_ARRAY, 
			PLAFOND_EPARGNE_CET_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_INTEGER,
			Parametre.TYPE_INTERFACE_TEXTFIELD_ENTIER);
	

	public final static Parametre PARAM_DUREE_HORAIRE_HEBDO_MINI = new Parametre(
			DUREE_HORAIRE_HEBDO_MINI, 
			false,
			DUREE_HORAIRE_HEBDO_MINI_CLASSE_METIER_ARRAY, 
			DUREE_HORAIRE_HEBDO_MINI_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_STRING,
			Parametre.TYPE_INTERFACE_HEURES_SANS_LIMITE);
	public final static Parametre PARAM_DUREE_HORAIRE_HEBDO_MAXI = new Parametre(
			DUREE_HORAIRE_HEBDO_MAXI, 
			false,
			DUREE_HORAIRE_HEBDO_MAXI_CLASSE_METIER_ARRAY, 
			DUREE_HORAIRE_HEBDO_MAXI_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_STRING,
			Parametre.TYPE_INTERFACE_HEURES_SANS_LIMITE);
	public final static Parametre PARAM_DUREE_HORAIRE_HEBDO_MAXI_HORS_NORMES = new Parametre(
			DUREE_HORAIRE_HEBDO_MAXI_HORS_NORMES, 
			false,
			DUREE_HORAIRE_HEBDO_MAXI_HORS_NORMES_CLASSE_METIER_ARRAY, 
			DUREE_HORAIRE_HEBDO_MAXI_HORS_NORMES_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_STRING,
			Parametre.TYPE_INTERFACE_HEURES_SANS_LIMITE);
	public final static Parametre PARAM_DUREE_JOUR_CONVERSION = new Parametre(
			DUREE_JOUR_CONVERSION, 
			false,
			DUREE_JOUR_CONVERSION_CLASSE_METIER_ARRAY, 
			DUREE_JOUR_CONVERSION_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_STRING,
			Parametre.TYPE_INTERFACE_HEURES_LIMITE_24);
	public final static Parametre PARAM_LOCK_HORAIRES_TYPES = new Parametre(
			LOCK_HORAIRES_TYPES, 
			false,
			LOCK_HORAIRES_TYPES_CLASSE_METIER_ARRAY, 
			LOCK_HORAIRES_TYPES_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_STRING,
			Parametre.TYPE_INTERFACE_CASE_A_COCHER);
	
	
	public final static Parametre PARAM_VERIFIER_STATUT_DEMANDE_EPARGNE_CET = new Parametre(
			VERIFIER_STATUT_DEMANDE_EPARGNE_CET, 
			false,
			VERIFIER_STATUT_DEMANDE_EPARGNE_CET_CLASSE_METIER_ARRAY, 
			VERIFIER_STATUT_DEMANDE_EPARGNE_CET_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_BOOLEAN,
			Parametre.TYPE_INTERFACE_CASE_A_COCHER);
	public final static Parametre PARAM_AUTORISER_DEMANDE_EPARGNE_CET_CDD_NON_CDI = new Parametre(
			AUTORISER_DEMANDE_EPARGNE_CET_CDD_NON_CDI, 
			false,
			AUTORISER_DEMANDE_EPARGNE_CET_CDD_NON_CDI_CLASSE_METIER_ARRAY, 
			AUTORISER_DEMANDE_EPARGNE_CET_CDD_NON_CDI_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_BOOLEAN,
			Parametre.TYPE_INTERFACE_CASE_A_COCHER);
	public final static Parametre PARAM_CET_OPTION_ANCIEN_SYSTEME_AU_DELA_20_JOURS = new Parametre(
			CET_OPTION_ANCIEN_SYSTEME_AU_DELA_20_JOURS, 
			false,
			CET_OPTION_ANCIEN_SYSTEME_AU_DELA_20_JOURS_CLASSE_METIER_ARRAY, 
			CET_OPTION_ANCIEN_SYSTEME_AU_DELA_20_JOURS_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_BOOLEAN,
			Parametre.TYPE_INTERFACE_CASE_A_COCHER);
	public final static Parametre PARAM_MESSAGE_EDITION_DEMANDE_EPARGNE_CET = new Parametre(
			MESSAGE_EDITION_DEMANDE_EPARGNE_CET, 
			false,
			MESSAGE_EDITION_DEMANDE_EPARGNE_CET_CLASSE_METIER_ARRAY, 
			MESSAGE_EDITION_DEMANDE_EPARGNE_CET_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_STRING,
			Parametre.TYPE_INTERFACE_ZONE_DE_TEXTE);
	public final static Parametre PARAM_AUTORISER_SAISIE_ABSENCE_SUR_PLANNING_PREVISIONNEL = new Parametre(
			AUTORISER_SAISIE_ABSENCE_SUR_PLANNING_PREVISIONNEL, 
			false,
			AUTORISER_SAISIE_ABSENCE_SUR_PLANNING_PREVISIONNEL_CLASSE_METIER_ARRAY, 
			AUTORISER_SAISIE_ABSENCE_SUR_PLANNING_PREVISIONNEL_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_BOOLEAN,
			Parametre.TYPE_INTERFACE_CASE_A_COCHER);
	public final static Parametre PARAM_FIN_DEMANDE_AUTORISER_SAISIE_ABSENCE_SUR_PLANNING_PREVISIONNEL_EN_COURS_D_ANNEE = new Parametre(
			AUTORISER_SAISIE_ABSENCE_SUR_PLANNING_PREVISIONNEL_EN_COURS_D_ANNEE, 
			false,
			AUTORISER_SAISIE_ABSENCE_SUR_PLANNING_PREVISIONNEL_EN_COURS_D_ANNEE_CLASSE_METIER_ARRAY, 
			AUTORISER_SAISIE_ABSENCE_SUR_PLANNING_PREVISIONNEL_EN_COURS_D_ANNEE_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_BOOLEAN,
			Parametre.TYPE_INTERFACE_CASE_A_COCHER);
	public final static Parametre PARAM_SIGNATURE_PRESIDENT = new Parametre(
			SIGNATURE_PRESIDENT, 
			false,
			SIGNATURE_PRESIDENT_CLASSE_METIER_ARRAY, 
			SIGNATURE_PRESIDENT_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_STRING,
			Parametre.TYPE_INTERFACE_TEXTFIELD_TEXTE);
	
	// horaires nom des parametres
	private final static String AM_DEBUT_MINI 												= "AM_DEBUT_MINI";
	private final static String AM_DEBUT_MAXI 												= "AM_DEBUT_MAXI";
	private final static String PM_FIN_MINI 													= "PM_FIN_MINI";
	private final static String PM_FIN_MAXI 													= "PM_FIN_MAXI";
	private final static String PAUSE_RTT_GAIN 												= "PAUSE_RTT_GAIN";
	private final static String PAUSE_MERIDIENNE_DUREE_MINI 					= "PAUSE_MERIDIENNE_DUREE_MINI";
	private final static String PAUSE_MERIDIENNE_DEBUT_MINI 					= "PAUSE_MERIDIENNE_DEBUT_MINI";
	private final static String PAUSE_MERIDIENNE_FIN_MAXI 						= "PAUSE_MERIDIENNE_FIN_MAXI";
	private final static String PAUSE_RTT_DUREE 											= "PAUSE_RTT_DUREE";
	private final static String DEMI_JOURNEE_DUREE_MAXI								= "DEMI_JOURNEE_DUREE_MAXI";
	
  // horaires classes metiers
  private final static Class<I_ClasseMetierNotificationParametre>[] AM_DEBUT_MINI_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.objects.HoraireJournalier.class, fr.univlr.cri.conges.PersoHoraires.class};
  private final static Class<I_ClasseMetierNotificationParametre>[] AM_DEBUT_MAXI_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.objects.HoraireJournalier.class, fr.univlr.cri.conges.PersoHoraires.class};
  private final static Class<I_ClasseMetierNotificationParametre>[] PM_FIN_MINI_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.objects.HoraireJournalier.class, fr.univlr.cri.conges.PersoHoraires.class};
  private final static Class<I_ClasseMetierNotificationParametre>[] PM_FIN_MAXI_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.objects.HoraireJournalier.class, fr.univlr.cri.conges.PersoHoraires.class};
  private final static Class<I_ClasseMetierNotificationParametre>[] PAUSE_RTT_GAIN_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.PersoHoraires.class};
  private final static Class<I_ClasseMetierNotificationParametre>[] PAUSE_MERIDIENNE_DUREE_MINI_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.PersoHoraires.class, fr.univlr.cri.conges.objects.HoraireJournalier.class};
  private final static Class<I_ClasseMetierNotificationParametre>[] PAUSE_MERIDIENNE_DEBUT_MINI_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.PersoHoraires.class, fr.univlr.cri.conges.objects.HoraireJournalier.class};
  private final static Class<I_ClasseMetierNotificationParametre>[] PAUSE_MERIDIENNE_FIN_MAXI_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.PersoHoraires.class, fr.univlr.cri.conges.objects.HoraireJournalier.class};
  private final static Class<I_ClasseMetierNotificationParametre>[] PAUSE_RTT_DUREE_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.objects.HoraireJournalier.class, fr.univlr.cri.conges.PersoHoraires.class, fr.univlr.cri.conges.PageService.class};
  private final static Class<I_ClasseMetierNotificationParametre>[] DEMI_JOURNEE_DUREE_MAXI_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.PersoHoraires.class, fr.univlr.cri.conges.objects.HoraireJournalier.class, fr.univlr.cri.conges.objects.HoraireHebdomadaire.class};

  // horaires valeurs par defaut
  private final static String AM_DEBUT_MINI_DEFAULT_VALUE_STRING																						= "480";
  private final static String AM_DEBUT_MAXI_DEFAULT_VALUE_STRING																						= "555";
  private final static String PM_FIN_MINI_DEFAULT_VALUE_STRING																							= "990";
  private final static String PM_FIN_MAXI_DEFAULT_VALUE_STRING																							= "1140";
  private final static String PAUSE_RTT_GAIN_DEFAULT_VALUE_STRING																					= "360";
  private final static String PAUSE_MERIDIENNE_DUREE_MINI_DEFAULT_VALUE_STRING								= "25";
  private final static String PAUSE_MERIDIENNE_DEBUT_MINI_DEFAULT_VALUE_STRING								= "705";
  private final static String PAUSE_MERIDIENNE_FIN_MAXI_DEFAULT_VALUE_STRING										= "840";
  private final static String PAUSE_RTT_DUREE_DEFAULT_VALUE_STRING																				= "20";
  private final static String DEMI_JOURNEE_DUREE_MAXI_DEFAULT_VALUE_STRING												= "300";
  
	// parametres horaires
	public final static Parametre PARAM_AM_DEBUT_MINI = new Parametre(
			AM_DEBUT_MINI, 
			false,
			AM_DEBUT_MINI_CLASSE_METIER_ARRAY, 
			AM_DEBUT_MINI_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_INTEGER,
			Parametre.TYPE_INTERFACE_HEURES_LIMITE_24);
	public final static Parametre PARAM_AM_DEBUT_MAXI = new Parametre(
			AM_DEBUT_MAXI, 
			false,
			AM_DEBUT_MAXI_CLASSE_METIER_ARRAY, 
			AM_DEBUT_MAXI_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_INTEGER,
			Parametre.TYPE_INTERFACE_HEURES_LIMITE_24);
	public final static Parametre PARAM_PM_FIN_MINI = new Parametre(
			PM_FIN_MINI, 
			false,
			PM_FIN_MINI_CLASSE_METIER_ARRAY, 
			PM_FIN_MINI_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_INTEGER,
			Parametre.TYPE_INTERFACE_HEURES_LIMITE_24);
	public final static Parametre PARAM_PM_FIN_MAXI = new Parametre(
			PM_FIN_MAXI, 
			false,
			PM_FIN_MAXI_CLASSE_METIER_ARRAY, 
			PM_FIN_MAXI_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_INTEGER,
			Parametre.TYPE_INTERFACE_HEURES_LIMITE_24);
	public final static Parametre PARAM_PAUSE_RTT_GAIN = new Parametre(
			PAUSE_RTT_GAIN, 
			false,
			PAUSE_RTT_GAIN_CLASSE_METIER_ARRAY, 
			PAUSE_RTT_GAIN_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_INTEGER,
			Parametre.TYPE_INTERFACE_HEURES_SANS_LIMITE);
	public final static Parametre PARAM_PAUSE_MERIDIENNE_DUREE_MINI = new Parametre(
			PAUSE_MERIDIENNE_DUREE_MINI, 
			false,
			PAUSE_MERIDIENNE_DUREE_MINI_CLASSE_METIER_ARRAY, 
			PAUSE_MERIDIENNE_DUREE_MINI_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_INTEGER,
			Parametre.TYPE_INTERFACE_HEURES_SANS_LIMITE);
	public final static Parametre PARAM_PAUSE_MERIDIENNE_DEBUT_MINI = new Parametre(
			PAUSE_MERIDIENNE_DEBUT_MINI, 
			false,
			PAUSE_MERIDIENNE_DEBUT_MINI_CLASSE_METIER_ARRAY, 
			PAUSE_MERIDIENNE_DEBUT_MINI_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_INTEGER,
			Parametre.TYPE_INTERFACE_HEURES_LIMITE_24);
	public final static Parametre PARAM_PAUSE_MERIDIENNE_FIN_MAXI = new Parametre(
			PAUSE_MERIDIENNE_FIN_MAXI, 
			false,
			PAUSE_MERIDIENNE_FIN_MAXI_CLASSE_METIER_ARRAY, 
			PAUSE_MERIDIENNE_FIN_MAXI_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_INTEGER,
			Parametre.TYPE_INTERFACE_HEURES_LIMITE_24);
	public final static Parametre PARAM_PAUSE_RTT_DUREE = new Parametre(
			PAUSE_RTT_DUREE, 
			false,
			PAUSE_RTT_DUREE_CLASSE_METIER_ARRAY, 
			PAUSE_RTT_DUREE_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_INTEGER,
			Parametre.TYPE_INTERFACE_HEURES_SANS_LIMITE);
	public final static Parametre PARAM_DEMI_JOURNEE_DUREE_MAXI = new Parametre(
			DEMI_JOURNEE_DUREE_MAXI, 
			false,
			DEMI_JOURNEE_DUREE_MAXI_CLASSE_METIER_ARRAY, 
			DEMI_JOURNEE_DUREE_MAXI_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_INTEGER,
			Parametre.TYPE_INTERFACE_HEURES_LIMITE_24);
  

	// bonifications nom des parametres
	private final static String COEF_HSUP_DEBORDEMENT 										= "COEF_HSUP_DEBORDEMENT";
	private final static String COEF_HSUP_SAM_MAT_5J 											= "COEF_HSUP_SAM_MAT_5J";
	private final static String COEF_HSUP_SAM_APREM_DIM_JF								= "COEF_HSUP_SAM_APREM_DIM_JF";
  private final static String HEURES_TRAVAILLEES_MINI_HOR_BONUS 				= "HEURES_TRAVAILLEES_MINI_HOR_BONUS";
  private final static String DEBUT_JOURNEE_BONUS 											= "DEBUT_JOURNEE_BONUS";
  private final static String FIN_JOURNEE_BONUS 												= "FIN_JOURNEE_BONUS";
  private final static String COEF_SAMEDI_MATIN_BONUS 									= "COEF_SAMEDI_MATIN_BONUS";
  private final static String COEF_DEBORDEMENT_BONUS 										= "COEF_DEBORDEMENT_BONUS";
  
  // bonifications classes metiers
  private final static Class<I_ClasseMetierNotificationParametre>[] COEF_HSUP_DEBORDEMENT_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.objects.occupations.HeuresSupplementaires.class};
  private final static Class<I_ClasseMetierNotificationParametre>[] COEF_HSUP_SAM_MAT_5J_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.objects.occupations.HeuresSupplementaires.class};
  private final static Class<I_ClasseMetierNotificationParametre>[] COEF_HSUP_SAM_APREM_DIM_JF_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.objects.HoraireJournalier.class, fr.univlr.cri.conges.objects.occupations.HeuresSupplementaires.class};
  private final static Class<I_ClasseMetierNotificationParametre>[] HEURES_TRAVAILLEES_MINI_HOR_BONUS_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.objects.HoraireJournalier.class};
  private final static Class<I_ClasseMetierNotificationParametre>[] DEBUT_JOURNEE_BONUS_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.objects.HoraireJournalier.class};
  private final static Class<I_ClasseMetierNotificationParametre>[] FIN_JOURNEE_BONUS_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.objects.HoraireJournalier.class};
  private final static Class<I_ClasseMetierNotificationParametre>[] COEF_SAMEDI_MATIN_BONUS_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.objects.HoraireJournalier.class};
  private final static Class<I_ClasseMetierNotificationParametre>[] COEF_DEBORDEMENT_BONUS_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.objects.HoraireJournalier.class};
	
  // bonifications valeurs par defaut
  private final static String COEF_HSUP_DEBORDEMENT_DEFAULT_VALUE_STRING													= "1.1";
  private final static String COEF_HSUP_SAM_MAT_5J_DEFAULT_VALUE_STRING														= "1.2";
  private final static String COEF_HSUP_SAM_APREM_DIM_JF_DEFAULT_VALUE_STRING								= "1.5";
  private final static String HEURES_TRAVAILLEES_MINI_HOR_BONUS_DEFAULT_VALUE_STRING	= "02:00";
  private final static String DEBUT_JOURNEE_BONUS_DEFAULT_VALUE_STRING															= "07:00";
  private final static String FIN_JOURNEE_BONUS_DEFAULT_VALUE_STRING																	= "19:00";
  private final static String COEF_SAMEDI_MATIN_BONUS_DEFAULT_VALUE_STRING											= "1.2";
  private final static String COEF_DEBORDEMENT_BONUS_DEFAULT_VALUE_STRING												= "1.2";
 
  
	// parametres bonifications
	public final static Parametre PARAM_COEF_HSUP_DEBORDEMENT = new Parametre(
			COEF_HSUP_DEBORDEMENT, 
			false,
			COEF_HSUP_DEBORDEMENT_CLASSE_METIER_ARRAY, 
			COEF_HSUP_DEBORDEMENT_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_FLOAT,
			Parametre.TYPE_INTERFACE_TEXTFIELD_1_NBR_VIR);
	public final static Parametre PARAM_COEF_HSUP_SAM_MAT_5J = new Parametre(
			COEF_HSUP_SAM_MAT_5J, 
			false,
			COEF_HSUP_SAM_MAT_5J_CLASSE_METIER_ARRAY, 
			COEF_HSUP_SAM_MAT_5J_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_FLOAT,
			Parametre.TYPE_INTERFACE_TEXTFIELD_1_NBR_VIR);
	public final static Parametre PARAM_COEF_HSUP_SAM_APREM_DIM_JF = new Parametre(
			COEF_HSUP_SAM_APREM_DIM_JF, 
			false,
			COEF_HSUP_SAM_APREM_DIM_JF_CLASSE_METIER_ARRAY, 
			COEF_HSUP_SAM_APREM_DIM_JF_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_FLOAT,
			Parametre.TYPE_INTERFACE_TEXTFIELD_1_NBR_VIR);
	public final static Parametre PARAM_HEURES_TRAVAILLEES_MINI_HOR_BONUS = new Parametre(
			HEURES_TRAVAILLEES_MINI_HOR_BONUS, 
			false,
			HEURES_TRAVAILLEES_MINI_HOR_BONUS_CLASSE_METIER_ARRAY, 
			HEURES_TRAVAILLEES_MINI_HOR_BONUS_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_STRING,
			Parametre.TYPE_INTERFACE_HEURES_SANS_LIMITE);
	public final static Parametre PARAM_DEBUT_JOURNEE_BONUS = new Parametre(
			DEBUT_JOURNEE_BONUS, 
			false,
			DEBUT_JOURNEE_BONUS_CLASSE_METIER_ARRAY, 
			DEBUT_JOURNEE_BONUS_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_STRING,
			Parametre.TYPE_INTERFACE_HEURES_LIMITE_24);
	public final static Parametre PARAM_FIN_JOURNEE_BONUS = new Parametre(
			FIN_JOURNEE_BONUS, 
			false,
			FIN_JOURNEE_BONUS_CLASSE_METIER_ARRAY, 
			FIN_JOURNEE_BONUS_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_STRING,
			Parametre.TYPE_INTERFACE_HEURES_LIMITE_24);
	public final static Parametre PARAM_COEF_SAMEDI_MATIN_BONUS = new Parametre(
			COEF_SAMEDI_MATIN_BONUS, 
			false,
			COEF_SAMEDI_MATIN_BONUS_CLASSE_METIER_ARRAY, 
			COEF_SAMEDI_MATIN_BONUS_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_FLOAT,
			Parametre.TYPE_INTERFACE_TEXTFIELD_1_NBR_VIR);
	public final static Parametre PARAM_COEF_DEBORDEMENT_BONUS = new Parametre(
			COEF_DEBORDEMENT_BONUS, 
			false,
			COEF_DEBORDEMENT_BONUS_CLASSE_METIER_ARRAY, 
			COEF_DEBORDEMENT_BONUS_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_FLOAT,
			Parametre.TYPE_INTERFACE_TEXTFIELD_1_NBR_VIR);
  
  // plafonds
	private final static String HEURES_CONGES_MAXI 															= "HEURES_CONGES_MAXI";
	private final static String NBRE_SEMAINES_HAUTES_MAXI 												= "NBRE_SEMAINES_HAUTES_MAXI";
	private final static String NBRE_SEMAINES_HAUTES_MAXI_DEPASSEMENT 				= "NBRE_SEMAINES_HAUTES_MAXI_DEPASSEMENT";
  
  // plafonds valeurs par defaut
  private final static String HEURES_CONGES_MAXI_DEFAULT_VALUE_STRING																					= "390";
  private final static String NBRE_SEMAINES_HAUTES_MAXI_DEFAULT_VALUE_STRING														= "8";
  private final static String NBRE_SEMAINES_HAUTES_MAXI_DEPASSEMENT_DEFAULT_VALUE_STRING		= "10";
  
  // plafonds classes metiers
  private final static Class<I_ClasseMetierNotificationParametre>[] HEURES_CONGES_MAXI_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.objects.PlanningContraintes.class};
  private final static Class<I_ClasseMetierNotificationParametre>[] NBRE_SEMAINES_HAUTES_MAXI_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.objects.PlanningContraintes.class};
  private final static Class<I_ClasseMetierNotificationParametre>[] NBRE_SEMAINES_HAUTES_MAXI_DEPASSEMENT_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.objects.PlanningContraintes.class};
	
  // plafonds objets parametres
	public final static Parametre PARAM_HEURES_CONGES_MAXI = new Parametre(
			HEURES_CONGES_MAXI, 
			false,
			HEURES_CONGES_MAXI_CLASSE_METIER_ARRAY, 
			HEURES_CONGES_MAXI_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_INTEGER,
			Parametre.TYPE_INTERFACE_TEXTFIELD_ENTIER);
	public final static Parametre PARAM_NBRE_SEMAINES_HAUTES_MAXI = new Parametre(
			NBRE_SEMAINES_HAUTES_MAXI, 
			false,
			NBRE_SEMAINES_HAUTES_MAXI_CLASSE_METIER_ARRAY, 
			NBRE_SEMAINES_HAUTES_MAXI_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_INTEGER,
			Parametre.TYPE_INTERFACE_TEXTFIELD_ENTIER);
	public final static Parametre PARAM_NBRE_SEMAINES_HAUTES_MAXI_DEPASSEMENT = new Parametre(
			NBRE_SEMAINES_HAUTES_MAXI_DEPASSEMENT, 
			false,
			NBRE_SEMAINES_HAUTES_MAXI_DEPASSEMENT_CLASSE_METIER_ARRAY, 
			NBRE_SEMAINES_HAUTES_MAXI_DEPASSEMENT_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_INTEGER,
			Parametre.TYPE_INTERFACE_TEXTFIELD_ENTIER);
 
  // divers
	private final static String DEBUT_ANNEE_UNIVERSITAIRE 					= "DEBUT_ANNEE_UNIVERSITAIRE";
  // divers valeurs par defaut
  private final static String DEBUT_ANNEE_UNIVERSITAIRE_DEFAULT_VALUE_STRING																					= "01/09";
  // divers classes metiers
  private final static Class<I_ClasseMetierNotificationParametre>[] DEBUT_ANNEE_UNIVERSITAIRE_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.utils.DateCtrlConges.class};
  // divers objets parametres
	public final static Parametre PARAM_DEBUT_ANNEE_UNIVERSITAIRE = new Parametre(
			DEBUT_ANNEE_UNIVERSITAIRE, 
			false,
			DEBUT_ANNEE_UNIVERSITAIRE_CLASSE_METIER_ARRAY, 
			DEBUT_ANNEE_UNIVERSITAIRE_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_STRING,
			Parametre.TYPE_INTERFACE_TEXTFIELD_TEXTE);
	
	
	// parametres rappatriés du fichier .config
	private final static String SHOW_CET 											= "SHOW_CET";
	private final static String SHOW_PAUSE 										= "SHOW_PAUSE";
	private final static String SHOW_DEMANDE_GARDE_ENFANT	 		= "SHOW_DEMANDE_GARDE_ENFANT";
	private final static String SHOW_DEMANDE_CONGES_PAPIER 		= "SHOW_DEMANDE_CONGES_PAPIER";
	
  // parametres rappatriés du fichier .config valeurs par defaut
  private final static String SHOW_CET_DEFAULT_VALUE_STRING																						= "YES";
  private final static String SHOW_PAUSE_DEFAULT_VALUE_STRING																					= "YES";
  private final static String SHOW_DEMANDE_GARDE_ENFANT_DEFAULT_VALUE_STRING							= "NO";
  private final static String SHOW_DEMANDE_CONGES_PAPIER_DEFAULT_VALUE_STRING						= "NO";

  // parametres rappatriés du fichier .config classes metiers
  private final static Class<I_ClasseMetierNotificationParametre>[] SHOW_CET_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.Session.class};
  private final static Class<I_ClasseMetierNotificationParametre>[] SHOW_PAUSE_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.eos.modele.planning.EOHoraire.class, fr.univlr.cri.conges.PageService.class, fr.univlr.cri.conges.PersoHoraires.class};
  private final static Class<I_ClasseMetierNotificationParametre>[] SHOW_DEMANDE_GARDE_ENFANT_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.Session.class};
  private final static Class<I_ClasseMetierNotificationParametre>[] SHOW_DEMANDE_CONGES_PAPIER_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.PageSaisieOccupation.class, fr.univlr.cri.conges.A_PagePrintDemCongePapier.class};
	
  // parametres rappatriés du fichier .config objets parametres
	public final static Parametre PARAM_SHOW_CET = new Parametre(
			SHOW_CET, 
			false,
			SHOW_CET_CLASSE_METIER_ARRAY, 
			SHOW_CET_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_BOOLEAN,
			Parametre.TYPE_INTERFACE_CASE_A_COCHER);
	public final static Parametre PARAM_SHOW_PAUSE = new Parametre(
			SHOW_PAUSE, 
			false,
			SHOW_PAUSE_CLASSE_METIER_ARRAY, 
			SHOW_PAUSE_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_BOOLEAN,
			Parametre.TYPE_INTERFACE_CASE_A_COCHER);
	public final static Parametre PARAM_SHOW_DEMANDE_GARDE_ENFANT = new Parametre(
			SHOW_DEMANDE_GARDE_ENFANT, 
			false,
			SHOW_DEMANDE_GARDE_ENFANT_CLASSE_METIER_ARRAY, 
			SHOW_DEMANDE_GARDE_ENFANT_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_BOOLEAN,
			Parametre.TYPE_INTERFACE_CASE_A_COCHER);
	public final static Parametre PARAM_SHOW_DEMANDE_CONGES_PAPIER = new Parametre(
			SHOW_DEMANDE_CONGES_PAPIER, 
			false,
			SHOW_DEMANDE_CONGES_PAPIER_CLASSE_METIER_ARRAY, 
			SHOW_DEMANDE_CONGES_PAPIER_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_BOOLEAN,
			Parametre.TYPE_INTERFACE_CASE_A_COCHER);
	  
	// parametre pour le niveau de validation des plannings
	public final static String VALIDATION_PLANNING_NIVEAU = "VALIDATION_PLANNING_NIVEAU";
	public final static String VALIDATION_PLANNING_NIVEAU_VALUE_RESPONSABLE = "0";
	public final static String VALIDATION_PLANNING_NIVEAU_DRH_VALUE 								= "1";
	// valeur par defaut
  private final static String VALIDATION_PLANNING_NIVEAU_DEFAULT_VALUE_STRING																						= "1";
  // classes metiers
  private final static Class<I_ClasseMetierNotificationParametre>[] VALIDATION_PLANNING_NIVEAU_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.databus.CngPlanningBus.class, fr.univlr.cri.conges.databus.CngAlerteBus.class};
  // objet parametre
	public final static Parametre PARAM_VALIDATION_PLANNING_NIVEAU = new Parametre(
			VALIDATION_PLANNING_NIVEAU, 
			false,
			VALIDATION_PLANNING_NIVEAU_CLASSE_METIER_ARRAY, 
			VALIDATION_PLANNING_NIVEAU_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_STRING,
			Parametre.TYPE_INTERFACE_POPUP);
	
	
	// parametre time machine pour les alertes
	public final static String ALERTE_NB_ANNEES_ANTERIEURES_VISIBLES = "ALERTE_NB_ANNEES_ANTERIEURES_VISIBLES";
	// valeur par defaut
	public final static String ALERTE_NB_ANNEES_ANTERIEURES_VISIBLES_DEFAULT_VALUE_STRING = "-1";
  // classes metiers
  private final static Class<I_ClasseMetierNotificationParametre>[] ALERTE_NB_ANNEES_ANTERIEURES_VISIBLES_CLASSE_METIER_ARRAY = new Class[] {	
  		fr.univlr.cri.conges.databus.CngAlerteBus.class};
  // objet parametre
	public final static Parametre PARAM_ALERTE_NB_ANNEES_ANTERIEURES_VISIBLES = new Parametre(
			ALERTE_NB_ANNEES_ANTERIEURES_VISIBLES, 
			false,
			ALERTE_NB_ANNEES_ANTERIEURES_VISIBLES_CLASSE_METIER_ARRAY, 
			ALERTE_NB_ANNEES_ANTERIEURES_VISIBLES_DEFAULT_VALUE_STRING, 
			Parametre.PARAM_VALUE_TYPE_INTEGER,
			Parametre.TYPE_INTERFACE_TEXTFIELD_ENTIER);
	
	/** la liste de tous les parametres non annualisés */
	public final static Parametre[] ALL_PARAMETRE_NON_ANNUALISE =
		
		new Parametre[]{
        PARAM_HEURES_CONGES_MAXI,
        PARAM_NBRE_SEMAINES_HAUTES_MAXI,
        PARAM_NBRE_SEMAINES_HAUTES_MAXI_DEPASSEMENT,
        PARAM_AM_DEBUT_MINI,
        PARAM_AM_DEBUT_MAXI,
        PARAM_PM_FIN_MINI,
        PARAM_PM_FIN_MAXI,
        PARAM_PAUSE_RTT_GAIN,
        PARAM_PAUSE_MERIDIENNE_DUREE_MINI,
        PARAM_PAUSE_MERIDIENNE_DEBUT_MINI,
        PARAM_PAUSE_MERIDIENNE_FIN_MAXI,
        PARAM_PAUSE_RTT_DUREE,
        PARAM_DEMI_JOURNEE_DUREE_MAXI,
        PARAM_COEF_HSUP_DEBORDEMENT,
        PARAM_COEF_HSUP_SAM_MAT_5J,
        PARAM_COEF_HSUP_SAM_APREM_DIM_JF,
        PARAM_DEBUT_ANNEE_UNIVERSITAIRE,	
        PARAM_HEURES_TRAVAILLEES_MINI_HOR_BONUS,
        PARAM_DEBUT_JOURNEE_BONUS,
        PARAM_FIN_JOURNEE_BONUS,
        PARAM_COEF_SAMEDI_MATIN_BONUS,
        PARAM_COEF_DEBORDEMENT_BONUS,
        PARAM_DUREE_HORAIRE_HEBDO_MINI,
        PARAM_DUREE_HORAIRE_HEBDO_MAXI,
        PARAM_DUREE_HORAIRE_HEBDO_MAXI_HORS_NORMES,
        PARAM_DUREE_JOUR_CONVERSION,
        PARAM_LOCK_HORAIRES_TYPES,
        PARAM_VALIDATION_PLANNING_NIVEAU,
        PARAM_MESSAGE_EDITION_DEMANDE_EPARGNE_CET,
        PARAM_VERIFIER_STATUT_DEMANDE_EPARGNE_CET,
        PARAM_AUTORISER_DEMANDE_EPARGNE_CET_CDD_NON_CDI,
        PARAM_CET_OPTION_ANCIEN_SYSTEME_AU_DELA_20_JOURS,
        PARAM_AUTORISER_SAISIE_ABSENCE_SUR_PLANNING_PREVISIONNEL,
        PARAM_FIN_DEMANDE_AUTORISER_SAISIE_ABSENCE_SUR_PLANNING_PREVISIONNEL_EN_COURS_D_ANNEE,
        PARAM_SIGNATURE_PRESIDENT,
        PARAM_SHOW_CET,
        PARAM_SHOW_PAUSE,
        PARAM_SHOW_DEMANDE_GARDE_ENFANT,
        PARAM_SHOW_DEMANDE_CONGES_PAPIER,
        PARAM_ALERTE_NB_ANNEES_ANTERIEURES_VISIBLES};	
	
	
	/** la liste de tous les parametres annualisés */
	public final static Parametre[] ALL_PARAMETRE_ANNUALISE =
		new Parametre[]{
			PARAM_HEURES_DUES,
			PARAM_DATE_MAX_RELIQUAT,
			PARAM_DEBUT_DEMANDE_CET,
			PARAM_FIN_DEMANDE_CET,
			PARAM_SEUIL_CONGES_CONSOMMES_JOUR_7H00_NM1_POUR_EPARGNE_CET,
			PARAM_SEUIL_RELIQUAT_HEURES_POUR_EPARGNE_CET,
			PARAM_PLAFOND_EPARGNE_CET};
	
  
	
}
