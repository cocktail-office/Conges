package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.constantes.ConstsMenu;

/**
 * Fonctionnalités diverses
 * 
 * @author ctarade
 *
 */
public class PageAdminDivers 
	extends YCRIWebPage {
	
	// gestion du menu
	public class PageAdminDiversSubMenuCtrl extends A_SubMenuCtrl {

		public NSArray labelList() {
			return ConstsMenu.PAGE_ADMIN_DIVERS_SUB_MENU_LABEL_LIST;
		}

		public NSArray tipList() {
			return ConstsMenu.PAGE_ADMIN_DIVERS_SUB_MENU_TIP_LIST;
		}

		public boolean isMenuMessage() 			{	
			return getLabelSelected().equals(ConstsMenu.MENU_ADMIN_DIVERS_MESSAGES_LABEL); 
		}
	
		public boolean isMenuVacances()    	{		
			return getLabelSelected().equals(ConstsMenu.MENU_ADMIN_DIVERS_VACANCES_LABEL);
		}

	}
	
	public PageAdminDiversSubMenuCtrl pageAdminDiversSubMenuCtrl;
	
	public PageAdminDivers(WOContext context) {	
		super(context);
		pageAdminDiversSubMenuCtrl = new PageAdminDiversSubMenuCtrl();
	}
}