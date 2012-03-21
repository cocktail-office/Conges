package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOComponent;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.util.StringCtrl;

/**
 * Composant de recherche et de selection d'un individu
 * dans le referentiel
 *
 * @author ctarade
 */
public class SearchIndividu 
	extends YCRIWebPage {

	/** la chaine de recherche */
	public String strSearch;
	
	/** resultats de la recherche */
	public NSArray individuList;
	public EOIndividu individuItem;
	public EOIndividu individuSelected;
	  
	/** indique dans quelles zones de la population chercher */
	public boolean isOnlyPersonnelActuel;
	
	public SearchIndividu(WOContext context) {
		super(context);
		// on recherche par defaut parmi les personnels
		isOnlyPersonnelActuel = true;		
	}

  /**
   * Effectuer la recherche
   */
  public WOComponent doSearch() {
    if (!StringCtrl.isEmpty(strSearch)) {
    	individuList = EOIndividu.findIndividuForNomOrPrenom(
    			edc, strSearch, isOnlyPersonnelActuel);
    }
    return null;
  }
}