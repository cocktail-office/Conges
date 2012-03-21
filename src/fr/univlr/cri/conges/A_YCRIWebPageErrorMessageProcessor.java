/**
 * 
 */
package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;

import fr.univlr.cri.util.StringCtrl;

/**
 * Une classe permettant de faire la gestion des messages d'erreurs
 * 
 * @author ctarade
 */
public abstract class A_YCRIWebPageErrorMessageProcessor
	extends YCRIWebPage {

	/**
	 * Le message d'erreur s'il y a un probleme
	 */
	private String errMsg;


	public A_YCRIWebPageErrorMessageProcessor(WOContext content) {
		super(content);
		clearError();
	}
	
	/**
	 * Indique si une erreur est survenue lors de la validation.
	 * On verifie s'il y a quelque chose dans le msg d'erreur
	 * <code>errMsg</code>
	 * @return
	 */
	public boolean hasError() {
		return !StringCtrl.isEmpty(errMsg);
	}
	
	/**
	 * RAZ du temoin d'erreur
	 */
	public final void clearError() {
		errMsg = StringCtrl.emptyString();
	}
	
	/**
	 * 
	 * @return
	 */
	public final String getErrMsg() {
		return errMsg;
	}

	/**
	 * 
	 * @return
	 */
	public final void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
}
