package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.conges.EOParametre;
import fr.univlr.cri.conges.objects.Parametre;

/**
 * TODO pour la gestion des heures, lors du clic sur le bouton valider,
 * les valeurs ne sont pas enregistrées. Seul 
 * 
 * Composant de gestion d'un parametre {@link EOParametre}
 * 
 * @author ctarade
 */
public class CompParametre
	extends YCRIWebPage {
	
	// bindings	
	/** le parametre (uniquement le préfixe si parametre annualisé) */ 
	public Parametre parametre;
	// optionnel : la taille pour les zones de texte
  public int size = 20;
  public int maxLength = 50;
  // pour les popups : la liste des valeurs
  public NSArray popupValueList;
  
  public String popupItem;
	
	/**
	 * 
	 * @param context
	 */
	public CompParametre(WOContext context) {
		super(context);
	}  
	
	/**
   * 
   */
  public void appendToResponse(WOResponse arg0, WOContext arg1) {	
   	super.appendToResponse(arg0, arg1);
   	addLocalCss(arg0, "css/Admin.css");
  }
	
  
  // 
  
  public void setAnneeUniv(NSTimestamp anneeUniv) {
  	parametre.setAnneeUniv(anneeUniv);
  }

  public NSTimestamp getAnneeUniv() {
  	return parametre.getAnneeUniv();
  }
 
  public void setPopupSelection(String selection) {
  	parametre.setParamValueString(selection);
  }
  
  public String getPopupSelection() {
  	return parametre.getParamValueString();
  }
}