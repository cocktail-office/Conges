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
public class PageAdminCET 
	extends YCRIWebPage {
	
	// gestion du menu
	public class PageAdminCETSubMenuCtrl extends A_SubMenuCtrl {

		public NSArray labelList() {
			return ConstsMenu.PAGE_ADMIN_CET_SUB_MENU_LABEL_LIST;
		}

		public NSArray tipList() {
			return ConstsMenu.PAGE_ADMIN_CET_SUB_MENU_TIP_LIST;
		}

		public boolean isMenuListeDemande() 			{	
			return getLabelSelected().equals(ConstsMenu.MENU_ADMIN_CET_LISTE_DEMANDE_LABEL); 
		}
	
		public boolean isMenuSaisieDemande()    	{		
			return getLabelSelected().equals(ConstsMenu.MENU_ADMIN_CET_SAISIE_DEMANDE_LABEL);
		}
	
		public boolean isMenuRegularisation()    	{		
			return getLabelSelected().equals(ConstsMenu.MENU_ADMIN_CET_REGULARISATION_LABEL);
		}
	
		public boolean isMenuSituation()    	{		
			return getLabelSelected().equals(ConstsMenu.MENU_ADMIN_CET_SITUATION_LABEL);
		}

	}
	
	public PageAdminCETSubMenuCtrl subMenuCtrl;
	
	public PageAdminCET(WOContext context) {
		super(context);
		subMenuCtrl = new PageAdminCETSubMenuCtrl();
	}
	
}