package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.constantes.ConstsMenu;

/**
 * Ecran rassemblant les écrans de suivi
 * 
 * @author ctarade
 *
 */
public class PageAdminSuivi
	extends YCRIWebPage {
	
	// gestion du menu
	public class PageAdminSuiviSubMenuCtrl extends A_SubMenuCtrl {

		public NSArray labelList() {
			return ConstsMenu.PAGE_ADMIN_SUIVI_SUB_MENU_LABEL_LIST;
		}

		public NSArray tipList() {
			return ConstsMenu.PAGE_ADMIN_SUIVI_SUB_MENU_TIP_LIST;
		}

		public boolean isMenuSuiviOccupations() 			{	
			return getLabelSelected().equals(ConstsMenu.MENU_ADMIN_SUIVI_OCCUPATION_LABEL);
		}
	
		public boolean isMenuSuiviCalculs()    	{		
			return getLabelSelected().equals(ConstsMenu.MENU_ADMIN_SUIVI_CALCUL_LABEL);
		}
	
		public boolean isMenuSuiviPlannings()    	{		
			return getLabelSelected().equals(ConstsMenu.MENU_ADMIN_SUIVI_PLANNING_LABEL);
		}
	

	}
	
	public PageAdminSuiviSubMenuCtrl subMenuCtrl;
	
	public PageAdminSuivi(WOContext context) {
		super(context);
		subMenuCtrl = new PageAdminSuiviSubMenuCtrl();
	}
	
}