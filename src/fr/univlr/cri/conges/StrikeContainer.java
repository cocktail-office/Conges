package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOComponent;

/**
 * Composant permettant de barrer une chaine avec 
 * les balise HTML <s> et </s>
 * 
 * @author ctarade
 *
 */
public class StrikeContainer extends WOComponent {

	// faut-il barrer (par défaut non)
	public boolean isStrike = false;
	
	public StrikeContainer(WOContext context) {
		super(context);
	}
}