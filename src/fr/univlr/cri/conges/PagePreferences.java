package fr.univlr.cri.conges;

import com.webobjects.appserver.*;

import fr.univlr.cri.conges.databus.CngPreferenceBus;

/**
 * Page de gestion des preferences personnelles de l'utilisateur
 * 
 * @author ctarade
 */
public class PagePreferences 
	extends YCRIWebPage {

	public PagePreferences(WOContext context) {
		super(context);
	}
	
	// getters
	
	public boolean isMailRecipisse() {
		return laSession.cngUserInfo().isMailRecipisse();
	}
	
	public boolean isMailRecipisseDelegation() {
		return laSession.cngUserInfo().isMailRecipisseDelegation();
	}
	
	// setters
	
	public void setIsMailRecipisse(Boolean value) {
		// recharger les preferences
		preferenceBus().updatePrefAppli(null, laSession.cngUserInfo(), value, null);
	}
	
	public void setIsMailRecipisseDelegation(Boolean value) {
		// recharger les preferences
		preferenceBus().updatePrefAppli(null, laSession.cngUserInfo(), null, value);
	}
	
	
	// raccourci vers les bus de donnees
	
	private CngPreferenceBus preferenceBus() {
		return laSession.cngDataCenter().preferenceBus();
	}
}