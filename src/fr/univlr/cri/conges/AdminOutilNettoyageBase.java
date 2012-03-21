package fr.univlr.cri.conges;


import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.YCRIWebPage;
import fr.univlr.cri.conges.databus.CngPlanningBus;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOPeriodeAffectationAnnuelle;
import fr.univlr.cri.webext.CRIAlertPage;
import fr.univlr.cri.webext.CRIAlertResponder;

/**
 * Vision des données de l'application, presentees tel qu'elles
 * sont interpretes par le modele EOF.
 * 
 * @author Cyril TARADE <cyril.tarade at univ-lr.fr>
 */

public class AdminOutilNettoyageBase extends YCRIWebPage {

  // l'affectation annuelle d'entree selectionnee
  public EOAffectationAnnuelle selectedAffAnn;
  
  // les periodes
  public EOPeriodeAffectationAnnuelle period;
  
  // item
  public EOAffectationAnnuelle affAnn;
  
  public AdminOutilNettoyageBase(WOContext context) {
    super(context);
  }
  
  /**
   * Supprimer le planning selectionné via le composant {@link CompServicesAffAnnsNew}
   * @return
   */
  public WOComponent deleteSelectedAffAnn() {
  	return deleteAffAnn(selectedAffAnn);
  }
  
  /**
   * Supprimer le planning item affAnn
   * @return
   */
  public WOComponent deleteAffAnn() {
  	return deleteAffAnn(affAnn);
  }
  
  
  /**
   * Suppression d'un planning
   */
  private WOComponent deleteAffAnn(EOAffectationAnnuelle anAffAnn) {
    StringBuffer sb = new StringBuffer();
    sb.append("<center><br>&Ecirc;tes vous sur de vouloir supprimer<br><br>").
    append("le planning suivant : ").append(anAffAnn.toString()).
    append("<br><br>Cette op&eacute;ration va effacer TOUTES les donn&eacute;es pour<BR>").
    append("l'ann&eacute;e universitaire s&eacute;lectionn&eacute;e !!!<br><br>Vous &ecirc;tes <b>VRAIMENT</b> s&ucirc;r ?</center>");
    return CRIAlertPage.newAlertPageWithResponder(this, "Suppression de planning",
        sb.toString(),
        "OUI SUPPRIMER", "NON ANNULER", null, CRIAlertPage.INFO,
        new AskConfirmSuppAffAnnResponder(this.parent().parent(), anAffAnn));
  }
  /**
   * Ecran de Demande de suppression d'affectation annuelle
   * 
   * @author Cyril TARADE <cyril.tarade at univ-lr.fr>
   */
  private class AskConfirmSuppAffAnnResponder implements CRIAlertResponder {
    
  	private WOComponent caller;
  	private EOAffectationAnnuelle affAnnToDelete;
  	
    public AskConfirmSuppAffAnnResponder(
    		WOComponent aCaller,
    		EOAffectationAnnuelle anAffAnn) {
      caller = aCaller;
      affAnnToDelete = anAffAnn;
    }

    public WOComponent respondToButton(int buttonNo) {
      if (buttonNo == 1) {
      	// deselectionner s'il s'agit de la selection par le composant 
      	if (affAnnToDelete == selectedAffAnn) {
          selectedAffAnn = null;
      	}
        planningBus().supprimeAffAnn(affAnnToDelete);
      }
      return caller;
    }
  }
    
  /**
   * La liste des affectations annuelles à supprimer
   * @return
   */
  public NSArray affAnnASupprimer() {
  	return planningBus().getPlanningASupprimer(edc);
  }
  
  
  // ** BUS DE DONNEES **
  
  private CngPlanningBus planningBus() {
    return laSession.cngDataCenter().planningBus();
  }
  
}