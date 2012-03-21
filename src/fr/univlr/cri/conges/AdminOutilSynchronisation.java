package fr.univlr.cri.conges;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.ycrifwk.utils.UtilDb;


/**
 * Ecran d'outils a destination de l'administrateur pour assurer la maintenance 
 * et la coherence de la base de donnees.
 * 
 * @author ctarade
 */
public class AdminOutilSynchronisation extends A_PageAdminCreationAffAnn {

	public AdminOutilSynchronisation(WOContext context) {
		super(context);
	}
  
  /**
   * Certains contrats ne sont pas reportes sur l'annee universitaire suivante.
   * Cela arrive dans le cas ou ce dernier est saisi apres l'autorisation de service.
   * Ce bouton permet de reinserer les contrats manquant pour tous les services autorises.
   * @return
   * @throws Throwable 
   */
  public WOComponent synchroniser() throws Throwable {
    if (lAnneeUnivSelectionnee != null) {
    	NSArray result = planningBus().autoriserServicesPourDateInContext(edc, lesServicesAutorises(), selectedAnneeUniv, false);
    	lastTotalAffAnn = ((Integer) result.objectAtIndex(0)).intValue();
    	lastTotalPeriode = ((Integer) result.objectAtIndex(1)).intValue();
    	lastMessage = (String) result.objectAtIndex(2);
      if (lastTotalAffAnn> 0 || lastTotalPeriode > 0) {
        UtilDb.save(edc, true);

        lesServicesAutorises = null;
        // forcer le refresh
        laSession.setLesStructureAutorisee(null);
      }
      shouldShowLastTotalAffAnn = true;
    }
    return null;
  }

  /**
   * Changement d'annee univ : rechargement de la liste des 
   * services autorises
   */
	public void onChangeAnneUnivSelectionnee() {
		 lesServicesAutorises = null;	   
	}

	/**
	 * Arret de l'application
	 */
	public WOComponent arreterApplication() {
		app.appStop(laSession.cngUserInfo());
		return null;
	}
}