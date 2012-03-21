package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.constantes.ConstsMenu;

/**
 * Ecran rassemblant les écrans utilitaires
 * 
 * @author ctarade
 *
 */
public class PageAdminOutils
	extends YCRIWebPage {
	
	// gestion du menu
	public class PageAdminOutilSubMenuCtrl extends A_SubMenuCtrl {

		public NSArray labelList() {
			return ConstsMenu.PAGE_ADMIN_OUTIL_SUB_MENU_LABEL_LIST;
		}

		public NSArray tipList() {
			return ConstsMenu.PAGE_ADMIN_OUTIL_SUB_MENU_TIP_LIST;
		}

		public boolean isMenuOutilSynchronisation() 			{	
			return getLabelSelected().equals(ConstsMenu.MENU_ADMIN_OUTIL_SYNCHRONISATION_LABEL);
		}
	
		public boolean isMenuOutilNettoyageBase()    	{		
			return getLabelSelected().equals(ConstsMenu.MENU_ADMIN_OUTIL_NETTOYAGE_BASE_LABEL);
		}
	

	}
	
	public PageAdminOutilSubMenuCtrl subMenuCtrl;
	
	public PageAdminOutils(WOContext context) {
		super(context);
		subMenuCtrl = new PageAdminOutilSubMenuCtrl();
	}
	
}