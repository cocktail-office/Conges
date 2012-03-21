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
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.appserver._private.WOHTTPHeadersDictionary;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.databus.CngDataCenter;
import fr.univlr.cri.conges.databus.CngUserInfo;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.CRIWebPage;
import fr.univlr.cri.webapp.LRLog;
import fr.univlr.cri.ycrifwk.utils.UtilDb;

/** * classe qui surcharge CRIWebPage
 * permet de rajouter des methodes dans les composants */
public class YCRIWebPage 
	extends CRIWebPage {

  public static Application app = (Application) Application.application();;
  public Session laSession;
  public EOEditingContext edc;
	
  // fond des zones de texte grise pour les navigateurs IE / Safari
  public final static String STYLE_TEXTAREA_DISABLED = "background:lightgrey;text-color:darkgrey";
    
  // le navigateur utilise est-il Internet Explorer
  public boolean isUsingNavigatorIE;
 
  // gestion des messages d'erreur
	private String errorMessage;
	
	// les tips html sur les attributs de l'entite EOAffectationAnnuelle
	public String htmlTipDepSemHautes 	= EOAffectationAnnuelle.getHtmlTipForKey(EOAffectationAnnuelle.FLAG_DEP_SEM_HAUTES_KEY);
	public String htmlTipHorsNorme 			= EOAffectationAnnuelle.getHtmlTipForKey(EOAffectationAnnuelle.FLAG_HORS_NORME_KEY);
	public String htmlTipPasseDroit 		= EOAffectationAnnuelle.getHtmlTipForKey(EOAffectationAnnuelle.FLAG_PASSE_DROIT_KEY);
	public String htmlTipCalculAuto 		= EOAffectationAnnuelle.getHtmlTipForKey(EOAffectationAnnuelle.IS_CALCUL_AUTOMATIQUE_KEY);
	public String htmlTipTPA 						= EOAffectationAnnuelle.getHtmlTipForKey(EOAffectationAnnuelle.FLAG_TEMPS_PARTIEL_ANNUALISE_KEY);
	public String htmlTipDepConges 			= EOAffectationAnnuelle.getHtmlTipForKey(EOAffectationAnnuelle.FLAG_DEPASSEMENT_CONGES_AUTORISE_KEY);
	public String htmlTipRelAuto 				= EOAffectationAnnuelle.getHtmlTipForKey(EOAffectationAnnuelle.IS_RELIQUAT_AUTOMATIQUE_KEY);
	public String htmlTipDechSynd 			= EOAffectationAnnuelle.getHtmlTipForKey(EOAffectationAnnuelle.IS_DECHARGE_SYNDICALE_KEY);
 
  public YCRIWebPage(WOContext context) {
    super(context);
    this.initObject();
  }
  
  public void appendToResponse(WOResponse arg0, WOContext arg1) {
    // verification du navigateur
    isUsingNavigatorIE = StringCtrl.containsIgnoreCase(
      (String)((NSArray)(((WOHTTPHeadersDictionary)context().request().valueForKey("headers")).valueForKey("user-agent"))).lastObject(),
      "MSIE");
  
    super.appendToResponse(arg0, arg1);
    
    // raz de l'erreur
		errorMessage = null;
  }
	
  /*
  public void takeValueForKey(Object arg0, String arg1) {
    LRLog.log(getClass().getName() + " key="+arg1+ " value="+arg0);
    super.takeValueForKey(arg0, arg1);
  }*/
  
	
  private void initObject() {
    laSession = (Session)session();
    edc = laSession.ec();
  }
	
    public void log(Object objet) {
	  String prefix = "<" + getClass().getName() + "> ";
	  if (objet != null) {
	    try {
	      LRLog.log(prefix + objet.toString());
	    }
	    catch (Throwable e) {
	      e.printStackTrace();
	    }
	  }
	  else {
	    LRLog.log(prefix + "null");
	  }
	}

	public WOComponent neFaitRien() {
	    return null;
	}
	
	public WOComponent sauvegarde() throws Throwable {
		return sauvegarde(true);
	}
	
	public WOComponent sauvegarde(boolean shouldPrintStackTrace) throws Throwable {
		UtilDb.save(edc, shouldPrintStackTrace);
		return neFaitRien();
	}
	
	/**
	 * construction dynamique du libelle de la valeur nulle dans les liste modifiables HTML
	 * centrage + completion a droite et a gauche par : "<---" ... "--->"
	 * @param lesStrings
	 * @param leLibelleNul
	 * @return
	 */
	public String noSelectionString(NSArray lesStrings, String leLibelleNul) {
	    String leLibelleCentral = " " + leLibelleNul.toUpperCase() + " ";
	    final String avt = "<--";
	    final String apr = "-->";
	    String str = avt + leLibelleCentral + apr;
	    if (lesStrings != null && lesStrings.count() > 0) {
		    int tailleMax = ((Number) lesStrings.valueForKeyPath("@max.length")).intValue();
		    if (tailleMax > leLibelleNul.length()) {
		        int nbIterations = (tailleMax - leLibelleCentral.length()) /2;
		        str = leLibelleCentral;
		        for (int i = 0; i < nbIterations; i++) {
		            str = "-" + str + "-";
		        }
		        str = "<" + str + ">";
		    }
	    }
	    return str;
	}
	
	private String linkCasLogin;
	private String linkAppDT;
	
	/**
	 * lien vers la connexion a travers CAS
	 * @return
	 */
	public String linkCasLogin() {
	  if (linkCasLogin == null) {
	    linkCasLogin = app.getApplicationURL(context());
	    if (!linkCasLogin.endsWith("/")) {
	      linkCasLogin += "/";
	    }
	    linkCasLogin += "wa/casLogin";   
	  }
	  return linkCasLogin;
	}
    
	/**
	 * lien vers l'application DT Web
	 * @return
	 */
	public String linkAppDT() {
	  if (linkAppDT == null) {
	    linkAppDT = app.config().stringForKey("DT_APPLIW3");
	  }
	  return linkAppDT;
	}
	
	/**
	 * Pointeur vers le centre de donnees
	 */
	public CngDataCenter dataCenter() {
		return laSession.cngDataCenter(); 
	}

	/**
	 * Pointeur vers les infos de l'utilisateur connecte
	 * @return
	 */
	public CngUserInfo cngUserInfo() {
		return laSession.cngUserInfo();
	}


	/**
	 * Indique s'il y a un message d'erreur a afficher
	 * @return
	 */
	public boolean hasError() {
		return !StringCtrl.isEmpty(errorMessage);
	}

	public final void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public final String getErrorMessage() {
		return errorMessage;
	}
}
	