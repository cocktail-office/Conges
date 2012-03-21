package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.constantes.ConstsMenu;

/**
 * Ecran rassemblant la gestion du compte epargne temps
 * pour le gestionnaire
 * 
 * @author ctarade
 *
 */
public class PageAdminParametres
	extends YCRIWebPage {
	
	// gestion du menu
	public class PageAdminParametresSubMenuCtrl extends A_SubMenuCtrl {

		public NSArray labelList() {
			return ConstsMenu.PAGE_ADMIN_PARAMETRE_SUB_MENU_LABEL_LIST; 
		}

		public NSArray tipList() {
			return ConstsMenu.PAGE_ADMIN_PARAMETRE_SUB_MENU_TIP_LIST;
		}

		public boolean isMenuServicesAutorises() 			{	
			return getLabelSelected().equals(ConstsMenu.MENU_ADMIN_PARAMETRE_SERV_AUT_LABEL); 
		}
	
		public boolean isMenuHoraireTypes()    	{		
			return getLabelSelected().equals(ConstsMenu.MENU_ADMIN_PARAMETRE_HORAIRES_TYPES_LABEL);
		}
	
		public boolean isMenuTypeOccupation()    	{		
			return getLabelSelected().equals(ConstsMenu.MENU_ADMIN_PARAMETRE_TYPE_OCC_LABEL);
		}
	
		public boolean isMenuParametres()    	{		
			return getLabelSelected().equals(ConstsMenu.MENU_ADMIN_PARAMETRE_PARAMETRES_LABEL);
		}
	
		public boolean isMenuHoraire()    	{		
			return getLabelSelected().equals(ConstsMenu.MENU_ADMIN_PARAMETRE_HORAIRE_LABEL);
		}
	
		public boolean isMenuCet()    	{		
			return getLabelSelected().equals(ConstsMenu.MENU_ADMIN_PARAMETRE_CET_LABEL);
		}

	}
	
	public PageAdminParametresSubMenuCtrl subMenuCtrl;
	
	public PageAdminParametres(WOContext context) {
		super(context);
		subMenuCtrl = new PageAdminParametresSubMenuCtrl();
	}
	
}