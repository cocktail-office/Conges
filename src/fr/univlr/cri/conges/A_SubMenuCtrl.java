package fr.univlr.cri.conges;

import com.webobjects.appserver.WOComponent;
import com.webobjects.foundation.NSArray;

/**
 * Classe de gestion des sous menus
 * 
 * @author ctarade
 *
 */
public abstract class A_SubMenuCtrl {

	// gestion du menu
	public int menuIndex;
	public String labelItem;
	public String titleItem;
	public String tipItem;
	public String labelSelected = null;
	
	public A_SubMenuCtrl() {
		initMenu();
	}
	
	/**
	 * 
	 */
	private void initMenu() {
		// selection du premier libellé
		if (labelList().count() > 0) {
			menuIndex = 0;
			labelSelected = (String) labelList().objectAtIndex(0);
		}
	}
	
	/** la liste de tous les libellés du menu */
	public abstract NSArray labelList();
	
	/** la liste de toutes les infos bulles du menu */
	public abstract NSArray tipList();
	
	/**
	 * 
	 * @return
	 */
	public final String tabMenuCssClass() {
		String tabMenuCssClass = "tab";
		
		if (labelItem.equals(labelSelected)) {
			tabMenuCssClass = "selectedTab";
		}
		
		return tabMenuCssClass;
	}

	/**
	 * 
	 * @return
	 */
	public final String tabMenuCssId() {
		String idTabMenu = "tab"+menuIndex;
		
		return idTabMenu;
	}	

	/**
	 * 
	 * @return
	 */
	public WOComponent doSelectLabelItem() {
		labelSelected = labelItem;
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public final String getTitleItem() {
		String titleItem = "";
		NSArray tipList = tipList();
		if (tipList != null && menuIndex < tipList.count()){
			titleItem = (String) tipList.objectAtIndex(menuIndex);
		}
		return titleItem;
	}
	
	public final String getLabelSelected() {
		return labelSelected;
	}
}
