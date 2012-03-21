package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.appserver.WOMessage;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.eos.modele.conges.EOTypeAbsenceGepeto;
import fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation;
import fr.univlr.cri.conges.objects.occupations.Occupation;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRRecord;

/**
 * Page d'administration des types d'occupations gerees par HAmAC, ainsi
 * que leur lien avec les types d'occupations de GRH.
 * 
 * @author ctarade
 */
public class PageAdminTypeOccupation extends YCRIWebPage {
	
	/** le display group contenant les enregistrement <code>EOTypeOccupation</code> */
	public WODisplayGroup typeOccDg;
	
	/** un type dans la liste */
	public EOTypeOccupation typeOccItem;
	
	public PageAdminTypeOccupation(WOContext context) {
		super(context);
	}
	
	/**
	 *  affichage du type absence de GRHUM / GEPETO / MANGUE
	 */
	public LRRecord absTypeGepeto() {
		return (LRRecord) laSession.dataBus().fetchObject(
				EOTypeAbsenceGepeto.ENTITY_NAME,
				CRIDataBus.newCondition(
						EOTypeAbsenceGepeto.TO_TYPE_OCCUPATION_KEY + "=%@", 
						new NSArray(typeOccItem)));
	}
	
	/**
	 * Certain type d'occupations peuvent necessiter des 
	 * informations compl�mentaires
	 */
	public boolean isTypeOccItemNeedsInfo() {
		boolean needs = false;

		if (typeOccItem.isFermeture() || 
				typeOccItem.libelleCourt().equals(Occupation.LIBELLE_COURT_CONGES_ANNUEL) ||
				typeOccItem.isAbsenceBilan() ||
				typeOccItem.isAbsenceCET()) {
			needs = true;
		}
		
		return needs;
	}
	
	/**
	 * Le message d'information pour le type d'occupations
	 * @return
	 */
	public String getHtmlInfo() {
		String strInfo = "";
		if (isTypeOccItemNeedsInfo()) {
			if (typeOccItem.isFermeture()) {
				strInfo = "Le param&egrave;tre de ce type de cong&eacute; impacte &eacute;galement les fermetures globales de l'&eacute;tablissement<br>" +
				"(ex: en fixant les horaires forc&eacute;s sur ce type, les fermetures d&eacute;cal&eacute;es ET 'normales'<br>" +
				"seront touch&eacute;es, et vice-versa)";
			} else if (typeOccItem.libelleCourt().equals(Occupation.LIBELLE_COURT_CONGES_ANNUEL)) {
				strInfo = "C'est le type de cong&eacute; 'classique', le plus souvent utilis&eacute; par les agents.";
			} else if (typeOccItem.isAbsenceBilan()) {
				strInfo = "Les types H_SUP et C_COMP se compensent mutellement. Les heures suppl&eacute;mentaires ont<br>" +
						"une dur&eacute;e de vie jusqu'à la fin de l'ann&eacute;e universitaire, au del&agrave;, ces derni&egrave;res sont<br>" +
						"'perdues'. Si les cong&eacute;s compensateurs d&eacute;passent les heures supp., alors l'&eacute;xc&eacute;dent est<br>" + "" +
						"d&eacute;bit&eacute; des cong&eacute;s annuels de l'agent";
			} else if (typeOccItem.isAbsenceCET()) {
				strInfo = "Absence sp&eacute;ciale en lien direct avec le compte &eacute;pargne temps de l'agent.";
			}
		}
		return WOMessage.stringByEscapingHTMLString(strInfo);
	}
}