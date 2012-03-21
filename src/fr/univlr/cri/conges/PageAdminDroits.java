package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.constantes.ConstsMenu;

/**
 * Ecran rassemblant les droits
 * 
 * @author ctarade
 *
 */
public class PageAdminDroits
	extends YCRIWebPage {
	
	// gestion du menu
	public class PageAdminDroitsSubMenuCtrl extends A_SubMenuCtrl {

		public NSArray labelList() {
			return cngUserInfo().isAdmin() ? ConstsMenu.PAGE_ADMIN_GENERAL_DROIT_SUB_MENU_LABEL_LIST : ConstsMenu.PAGE_ADMIN_COMPOSANTE_DROIT_SUB_MENU_LABEL_LIST;
		}

		public NSArray tipList() {
			return cngUserInfo().isAdmin() ? ConstsMenu.PAGE_ADMIN_GENERAL_DROIT_SUB_MENU_TIP_LIST : ConstsMenu.PAGE_ADMIN_COMPOSANTE_DROIT_SUB_MENU_TIP_LIST;
		}

		public boolean isMenuDroitGestion() 			{	
			return getLabelSelected().equals(ConstsMenu.MENU_ADMIN_DROIT_GESTION_LABEL);
		}
	
		public boolean isMenuDroitListe()    	{		
			return getLabelSelected().equals(ConstsMenu.MENU_ADMIN_DROIT_LISTE_LABEL);
		}
	
		public boolean isMenuPasseDroitListe()    	{		
			return getLabelSelected().equals(ConstsMenu.MENU_ADMIN_PASSE_DROIT_LISTE_LABEL);
		}

		public boolean isMenuDroitAdmin() 			{	
			return getLabelSelected().equals(ConstsMenu.MENU_ADMIN_DROIT_ADMIN_LABEL);
		}

		public boolean isMenuDroitDRH() 			{	
			return getLabelSelected().equals(ConstsMenu.MENU_ADMIN_DROIT_DRH_LABEL);
		}
	
	}
	
	public PageAdminDroitsSubMenuCtrl subMenuCtrl;
	
	public PageAdminDroits(WOContext context) {
		super(context);
		subMenuCtrl = new PageAdminDroitsSubMenuCtrl();
	}
	
}