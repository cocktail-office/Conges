package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOComponent;

/**
 * Composant sous menu. Le composante communique
 * avec la page appelante via l'instance de la classe
 * {@link A_SubMenuCtrl}
 * 
 * @author ctarade
 *
 */
public class SubMenu extends WOComponent {
	
	/** le controleur */
	public A_SubMenuCtrl ctrl;
	
	public SubMenu(WOContext context) {
		super(context);
	}
}