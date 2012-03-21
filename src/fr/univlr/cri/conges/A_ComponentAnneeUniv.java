package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.conges.EOStructureAutorisee;
import fr.univlr.cri.conges.objects.Parametre;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.util.ArrayCtrl;

/**
 * Composant reposant sur la liste des années universtaires
 * gérées par HAmAC.
 * L'année universitaire courant est selectionée par défaut
 * 
 * @author ctarade
 *
 */
public abstract class A_ComponentAnneeUniv 	
	extends A_YCRIWebPageErrorMessageProcessor {

	  private NSArray lesAnneeUniv;
	  public String lAnneeUnivSelectionnee;
	  public NSTimestamp selectedAnneeUniv;
	  public String uneAnneeUniv;

	  /** la liste des services autorises pour l'annee universitaire selectionnee */
		public NSArray lesServicesAutorises;

	  public A_ComponentAnneeUniv(WOContext context) {
			super(context);
	    // selection par defaut de l'annee universitaire en cours
	    setLAnneeUnivSelectionnee(DateCtrlConges.anneeUnivForDate(laSession.dateRef()));
		}	
	  
	  public NSArray lesAnneeUniv() {
	    if (lesAnneeUniv == null) {
	      lesAnneeUniv = new NSArray();
	      // liste des annees ou il y a eu des services autorises
	      lesAnneeUniv = EOStructureAutorisee.findAllDebutAnneeUnivStringInContext(edc);
	      lesAnneeUniv = lesAnneeUniv.arrayByAddingObject(DateCtrlConges.anneeUnivForDate(laSession.dateRef()));
	      lesAnneeUniv = lesAnneeUniv.arrayByAddingObject(DateCtrlConges.anneeUnivForDate(
	          laSession.dateRef().timestampByAddingGregorianUnits(1, 0, 0, 0, 0, 0)));
	      // on vire les doubles au cas ou
	      lesAnneeUniv = ArrayCtrl.removeDoublons(lesAnneeUniv);
	    }
	    return lesAnneeUniv;
	  }
	 
	  public void setLAnneeUnivSelectionnee(String value) {
	    lAnneeUnivSelectionnee = value;
	    selectedAnneeUniv = DateCtrlConges.dateDebutAnneePourStrPeriodeAnnee(value);
    	// création automatique des parametres annualisés absents
    	for (Parametre parametre : Parametre.ALL_PARAMETRE_ANNUALISE) {
    		parametre.setAnneeUniv(selectedAnneeUniv);
    		// cet appel va faire la création automatique avec la valeur par defaut
    		// si le parametre n'existe pas
    		parametre.getParamValueString();
    	}
    	//
	    onChangeAnneUnivSelectionnee();
	  }
	 
	  /**
	   * changement de la selection de l'annee universitaire : traitement a effectuer
	   */
	  public abstract void onChangeAnneUnivSelectionnee();
	  
}
