package fr.univlr.cri.conges;

import com.webobjects.appserver.*;

import fr.univlr.cri.conges.print.CngPdfBoxCtrl;
import fr.univlr.cri.webapp.CRIWebComponent;
import fr.univlr.cri.webext.CRIAlertPage;

/**
 * Boite contenant le lien pour produire
 * une document PDF via SIX.
 * 
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */

public class CngPdfBoxLink extends CRIWebComponent {
	
	public CngPdfBoxLink(WOContext context) {
		super(context);
	}
	
	/**
	 * Methode reliee au clic sur le lien. Affiche 
	 * la page de chargement, si une impression n'est 
	 * pas deja en cours.
	 */
	public WOComponent doPrint() {
		CngPdfBoxComponent nextPage = (CngPdfBoxComponent) pageWithName(
				CngPdfBoxComponent.class.getName());
		if (!nextPage.isWorking()) {
			nextPage.setCurrentCtrl(ctrl());
			return nextPage; 
		} else {
			return CRIAlertPage.newAlertPageWithMessage(this, 
					"Edition annulee", "Un fichier PDF est deja en cours de creation.\n" +
					"Veuillez attendre la fin de cette tache avant d'en relancer une autre.", CRIAlertPage.NONE);
		}
	}
	
	public boolean synchronizesVariablesWithBindings() {
		return false;
	}
	
	/** Lien a afficher */
	public String printLabel() {
		return (String) valueForBinding("printLabel");
	}
	
	/** Tooltip sur le lien */
	public String printTip() {
		return (String) valueForBinding("printTip");
	}
	
	/** le controleur */
	private CngPdfBoxCtrl ctrl() {
		return (CngPdfBoxCtrl) valueForBinding("ctrl");
	}

	// variable bindons pour java 1.4.2 ....
	public String printLabel;
	public String printTip;
	public CngPdfBoxCtrl ctrl;

}
	