package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOComponent;

/**
 * Composant permettant de mettre en gras une chaine avec 
 * les balise HTML <b> et </b>
 * 
 * @author ctarade
 *
 */
public class BoldContainer extends WOComponent {

	// faut-il mettre en gras (par défaut non)
	public boolean isBold = false;
	
	public BoldContainer(WOContext context) {
		super(context);
	}
}