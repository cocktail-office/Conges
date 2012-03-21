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

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver._private.WOHTTPHeadersDictionary;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;

import fr.univlr.cri.conges.Main;
import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.CRIWebAction;
import fr.univlr.cri.webapp.CRIWebApplication;
import fr.univlr.cri.webapp.LRDataResponse;
import fr.univlr.cri.webapp.LRLog;
import fr.univlr.cri.webext.CRIAlertPage;

public class DirectAction extends CRIWebAction  {
  
  private CRIWebApplication app;	
  
  public DirectAction(WORequest aRequest) {
  	super(aRequest);
  	app = (CRIWebApplication)criApp; 
  }
  
  protected CRIWebApplication app() { return app; }
  
  protected Session cngSession() {
    Session cngSession = (Session) existingSession();
    if (cngSession == null) {
      cngSession = (Session) session();
    }
    return cngSession;
  }
  
  public WOActionResults defaultAction() {
    WOComponent nextPage = null;
    checkNavigator();   
    Session cngSession = cngSession();
    if (cngSession.isBadNavigator()) {      
      // mauvais navigateur : 
      nextPage = criApp.pageWithName(CRIAlertPage.class.getName(), context());
      String msg = 
        "Votre navigateur ne permet pas l'accès à l'application des congés ...<br><br>" +
        "Faire une <a href=\"" + app.config().stringForKey("DT_APPLIW3") +"\">Demande de Travaux</a><br><br>" +
        "Retour au site de l'<a href=\"" + app.config().stringForKey("MAIN_WEB_SITE_URL") + "\">Université</a>";
      ((CRIAlertPage)nextPage).showMessage(null, "ERREUR", msg, null, null, null, CRIAlertPage.ERROR, null);
      // fermeture de la session
      cngSession.terminate();
    } else {
      nextPage = pageWithName(Main.class.getName());
    }

    return nextPage;
  }

  /**
   * CAS : traitement authentification OK.
   * Message d'erreur si on ne trouve pas l'individu associe au compte
   */
  public WOActionResults loginCasSuccessPage(String netid) {
  	try {
      EOEditingContext edc = cngSession().ec();
      NSArray ulComptes = (NSArray)EOUtilities.objectsMatchingValues(edc,"ul_Compte",new NSDictionary(netid,"cptLogin"));
      EOGenericRecord ulCompte = (EOGenericRecord)ulComptes.lastObject();
      NSArray repartComptes = (NSArray)ulCompte.valueForKeyPath("toRepartCompte");
      EOGenericRecord repartCompte = (EOGenericRecord)repartComptes.lastObject();
      Number persId = (Number)repartCompte.valueForKeyPath("toIndividuUlr.persId");
      return cngSession().loginRedirect(persId);	
  	} catch (Exception e) {
  		String errMessage = "Impossible de retrouver l'individu associ&eacute; au login : '" + netid +"'";
  		return loginCasFailurePage(errMessage, LRLog.getMessageForException(e));
		}
  }

	/**
	 * CAS : traitement authentification en echec
	 */
	public WOActionResults loginCasFailurePage(String errorMessage, String arg1) {
		StringBuffer msg = new StringBuffer();
		msg.append("Une erreur s'est produite lors de l'authentification de l'utilisateur&nbsp;:<br><br>");
		if (errorMessage != null)
		  msg.append("&nbsp;:<br><br>").append(errorMessage);
		return getErrorPage(msg.toString());
	}

	/**
	 * CAS : page par defaut si CAS n'est pas parametre
	 */
	public WOActionResults loginNoCasPage() {
      return pageWithName(Main.class.getName());
	}


	/**
	 * affiche une page avec un message d'erreur
	 */
	private WOComponent getErrorPage(String errorMessage) {
	  CRIAlertPage page = (CRIAlertPage)criApp.pageWithName(
	  		CRIAlertPage.class.getName(), context());
	  page.showMessage(null, "ERREUR", errorMessage,
					   null, null, null, CRIAlertPage.ERROR, null);
	  return page;
	}
	

	/**
	 * filtrage navigateurs
	 * 
	 * Mozilla Windows 	: ("Mozilla/5.0 (Windows; U; Windows NT 5.1; fr-FR; rv:1.6) Gecko/20040113")
	 * IE 6.0 Windows	: ("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 1.1.4322)")
	 * Netscape 4.78 Win: ("Mozilla/4.78 [fr] (Windows NT 5.0; U)")  <---- A FILTRER
	 * Mozilla MacOSX	: ("Mozilla/5.0 (Macintosh; U; PPC Mac OS X Mach-O; fr-FR; rv:1.3.1) Gecko/20030425")
	 * FireFox MacOSX	: ("Mozilla/5.0 (Macintosh; U; PPC Mac OS X Mach-O; en-US; rv:1.7) Gecko/20040803 Firefox/0.9.3")
	 * Safari MacOSX	: ("Mozilla/5.0 (Macintosh; U; PPC Mac OS X; fr) AppleWebKit/125.4.2 (KHTML, like Gecko) Safari/125.9
	 */
	private void checkNavigator()  {
	  WORequest request = context().request();
	  String userAgent = ((WOHTTPHeadersDictionary)request.valueForKey("headers")).valueForKey("user-agent").toString();
	  Session cngSession = cngSession();
	  if (StringCtrl.containsIgnoreCase(userAgent, "Mozilla/4.78"))
	    cngSession.setBadNavigator(true);
	  else
	    cngSession.setBadNavigator(false);
	}
    
	/**
     * Retourne la directAction attendue d'apres son nom <code>daName</code>. 
     * Si rien n'a ete trouve, alors une page d'avertissement est affichee.
     */
	public WOActionResults performActionNamed(String aName) {
      WOActionResults result = null;
      try {
        result = super.performActionNamed(aName);
      } catch (Exception e) {
        result = getErrorPage("DirectAction introuvable : \"" + aName + "\"");
        e.printStackTrace();
      }
      return result;
    }

    private DAAgenda _daAgenda;
    private DAGepeto _daGepeto;
     
    private DAAgenda 	daAgenda() 	{ if (_daAgenda == null) 	_daAgenda = new DAAgenda(request()); 	return _daAgenda; }
    private DAGepeto 	daGepeto() 	{ if (_daGepeto == null) 	_daGepeto = new DAGepeto(request()); 	return _daGepeto; }
    
    //TODO : temporaires les DA de Conges sont en dur dans le code de GEPETO
    public WOActionResults changeCongeLegalAction()                 {   return daGepeto().changeCongeLegalAction(); }
    public WOActionResults changeDatesAffectationAction()           {   return daGepeto().changeDatesAffectationAction(); }
    public WOActionResults changeQuotiteAffectationAction()         {   return daGepeto().changeQuotiteAffectationAction(); }
    public WOActionResults changeStructureAffectationAction()       {   return daGepeto().changeStructureAffectationAction(); }
    public WOActionResults enregistreCongeLegalAction()             {   return daGepeto().enregistreCongeLegalAction(); }
    public WOActionResults nouvelleAffectationAction()              {   return daGepeto().nouvelleAffectationAction(); }
    public WOActionResults presencesDemiJourneesPourPeriodeAction() {   return daGepeto().presencesDemiJourneesPourPeriodeAction(); }
    public WOActionResults supprimeAffectationAction()              {   return daGepeto().supprimeAffectationAction(); }
    public WOActionResults supprimeCongeLegalAction()               {   return daGepeto().supprimeCongeLegalAction(); }
    
    //TODO : temporaires les DA de Conges sont en dur dans le code d'AGENDA / EDTWeb
    public WOActionResults horairesPourPeriodeAction()              {   return daAgenda().horairesPourPeriodeAction(); }
    public WOActionResults occupationsPourPeriodeAction()           {   return daAgenda().occupationsPourPeriodeAction(); }

    public WOActionResults loginCasSuccessPage(String arg0, NSDictionary arg1) {
      return loginCasSuccessPage(arg0);
    }

    public WOActionResults loginNoCasPage(NSDictionary arg0) {
      return loginNoCasPage();
    }

    /**
     * Affichage d'un document PDF.
		 * Le fichier affiche est celui dont le flux <code>NSData</code>
		 * est dans la variable <code>cngSession().lastPdfData()</code>
     */
    public WOActionResults printAction() {
    	NSData pdfData = cngSession().getLastPdfData();
    	LRDataResponse resp = new LRDataResponse();
  	  if (pdfData != null) {
  	    resp.setContent(pdfData, LRDataResponse.MIME_PDF);
  	    resp.setFileName(cngSession().getLastPdfCtrl().dateFileName() + ".pdf");
        resp.setHeader(String.valueOf(pdfData.length()), "Content-Length");
        resp.disableClientCaching();
        resp.removeHeadersForKey("Cache-Control");
        resp.removeHeadersForKey("pragma");
  	  } else {
  	  	resp.setContent("");
  	    resp.setHeader("0", "Content-Length");
  	  }   	
  	  return resp.generateResponse();
    }
    
    /**
     * Accéder directement à la page des demandes en s'affranchissant
     * du clic sur le lien cas si ce service est activé
     */
  	public WOActionResults connexionAction() {
      //
  		if (useCasService())
  			return pageForURL(getLoginActionURL(context(), true, null, false, null));
  		else
  			return loginNoCasPage(null);
  	}
}