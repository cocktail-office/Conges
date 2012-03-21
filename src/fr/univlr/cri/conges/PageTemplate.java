package fr.univlr.cri.conges;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;

import fr.univlr.cri.conges.YCRIWebPage;


public class PageTemplate extends YCRIWebPage {

  public String pageTitle;
  public Boolean useToolTip;
  
  // s'agit-il d'une fenetre popup ? Si oui, on cache le menu
  // TODO changer en showMenu (inverse)
  public Boolean isPopup = Boolean.FALSE;
  
  public PageTemplate(WOContext context) {
    super(context);
  }
 
  public void appendToResponse(WOResponse aResponse, WOContext aContext) {
    super.appendToResponse(aResponse, aContext);
    // ajouter la CSS de conges
    addLocalCss(aResponse, "css/Conges.css");
    // ajouter les scripts JS pour conges
    addLocalJScript(aResponse, "js/jsTemplate.js");    
  }

  
  // GESTION DU TEMPS DE GENERATION DE LA PAGE
  
  public boolean showGenerationDuration() {
  	return false;//laSession.cngUserInfo() != null && laSession.cngUserInfo().isAdmin();
  }
  
   /**
   * Ce getter est appelle a la fin, quand la page est affichee
   */
  public String getGenerationDuration() {
  	String result = Double.toString(((double)System.currentTimeMillis()-laSession.getStartTime())/1000.0);
  	return result;
  }
}