package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOComponent;

/**
 * Boite d'erreur
 * @author ctarade
 */
public class CngErrorBox extends WOComponent {

	public boolean showError;
		
	public CngErrorBox(WOContext context) {
		super(context);
	}
}