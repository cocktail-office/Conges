package fr.univlr.cri.conges;



import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.eos.modele.conges.EOMessage;

import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.CRIWebAction;
import fr.univlr.cri.webapp.LRUserInfo;
import fr.univlr.cri.webext.*;

/**
 * Page de connexion a l'application HAmAC.
 * 
 * @author ctarade
 */
public class Main extends YCRIWebPage {
  
	public CRILoginResponder loginResponder = new LoginResponder();
  public boolean isBadNavigator = false;
  public EOMessage unMessage;
  
  public Main(WOContext context) {
    super(context);
  }
  
  public void appendToResponse(WOResponse aResponse, WOContext aContext) {
    super.appendToResponse(aResponse, aContext);
 }
  
  public String lienPbConnexion() {
    return criApp.contactMail();
  }
  /*
  private String linkCasLoginPrefix;
  
  private String linkCasLoginPrefix() {
    if (linkCasLoginPrefix == null) {
      linkCasLoginPrefix = app.getApplicationURL(context());
      if (!linkCasLoginPrefix.endsWith("/"))
        linkCasLoginPrefix += "/";
    }
    return linkCasLoginPrefix;
  }*/

  // lien vers l'appli DTWeb
  
  private String linkAppDT;

  public String linkAppDT() {
    if (linkAppDT == null)
      linkAppDT = app.config().stringForKey("DT_APPLIW3");
    return linkAppDT;
  }
  
  // les liens vers les fichiers 
  
  private String linkTutorial;
  private String linkCircu;
  private String linkDoc;
  
  public String linkTutorial() {
    if (linkTutorial == null)
      linkTutorial = app.config().stringForKey("VIEWLET_URL");
    return linkTutorial;
  }
  
  public String linkCircu() {
    if (linkCircu == null)
      linkCircu = app.config().stringForKey("URL_CIRCU");
    return linkCircu;
  }
  
  public String linkDoc() {
    if (linkDoc == null)
      linkDoc = app.config().stringForKey("URL_DOC");
    return linkDoc;
  }

  
  public String linkCasLogin() {
    //return  linkCasLoginPrefix() + "wa/casLogin"; //?wosid=" + session().sessionID();
  	//return CRIWebAction.getLoginActionURL(context(), true, null, session().storesIDsInURLs(), null);
  	// return CRIWebAction.getDefaultLoginActionURL(context());
  	//return CRIWebAction.getLoginActionURL(context(), false, null, true, null);
  	return CRIWebAction.getLoginActionURL(context(), true, null, true, null);
  }

  
  public NSArray lesMessagesEnCours() {
      return EOMessage.findCurrentMessagesInContext(edc);
  }
  
  class LoginResponder implements CRILoginResponder {

		public WOComponent loginAccepted(CRILogin loginComponent) {
	    LRUserInfo ui = null;
	    	
		  // Recuperation des donnees associees a l'individu connecte
		  ui = loginComponent.loggedUserInfo();
		  
		  // choix de la page de destination selon l'individu
		  return laSession.loginRedirect(ui.persId());
		}
	
		public boolean acceptLoginName(String loginName) {
		  return criApp.acceptLoginName(loginName);
		}
	
		public boolean acceptEmptyPassword() {
		  return criApp.config().booleanForKey("ACCEPT_EMPTY_PASSWORD");
		}
	    
		public String getRootPassword() {
		  return criApp.getRootPassword();
		}
  }
  
  // boolean interface
  
  public boolean isLinkAppDTExists() {
    return !StringCtrl.isEmpty(linkAppDT());
  }

  public boolean isLinkTutorialExists() {
    return !StringCtrl.isEmpty(linkTutorial());
  }
  
  public boolean isLinkCircuExists() {
    return !StringCtrl.isEmpty(linkCircu());
  }
  
  public boolean isLinkDocExists() {
    return !StringCtrl.isEmpty(linkDoc());
  }
  
  
  // transformation de Application en variable de classe
  // pour permettre les bindings directement dans l'interface
  
  public Application app() {
  	return super.app;
  }
}