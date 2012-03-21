package fr.univlr.cri.conges;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.conges.EOParametre;
import fr.univlr.cri.conges.eos.modele.conges.EOStructureAutorisee;
import fr.univlr.cri.conges.objects.Parametre;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.util.ArrayCtrl;

/**
 * Interfaces de gestion des parametres de l'application
 * en lien avec la classe {@link EOParametre}
 * 
 * @author ctarade
 */
public abstract class A_AdminParametre 
	extends YCRIWebPage {

	// liste des années universitaires pour les parametres annualisés
	public NSArray strAnneeUnivArray;
	public String strAnneeUnivItem;
	public String strAnneeUnivSelected;
	public NSTimestamp dateAnneeUnivSelected;
	
	public A_AdminParametre(WOContext context) {
		super(context);
		initComponent();
	}
	
	private void initComponent() {
		strAnneeUnivArray = new NSArray();
		// liste des annees ou il y a eu des services autorises
		strAnneeUnivArray = EOStructureAutorisee.findAllDebutAnneeUnivStringInContext(edc);
		strAnneeUnivArray = strAnneeUnivArray.arrayByAddingObject(DateCtrlConges.anneeUnivForDate(laSession.dateRef()));
		strAnneeUnivArray = strAnneeUnivArray.arrayByAddingObject(DateCtrlConges.anneeUnivForDate(
          laSession.dateRef().timestampByAddingGregorianUnits(1, 0, 0, 0, 0, 0)));
      // on vire les doubles au cas ou
		strAnneeUnivArray = ArrayCtrl.removeDoublons(strAnneeUnivArray);
		 // selection par defaut de l'annee universitaire en cours
		setStrAnneeUnivSelected(DateCtrlConges.anneeUnivForDate(laSession.dateRef()));
	}
	 
  public void setStrAnneeUnivSelected(String value) {
  	strAnneeUnivSelected = value;
    dateAnneeUnivSelected = DateCtrlConges.dateDebutAnneePourStrPeriodeAnnee(strAnneeUnivSelected);
  }

	/** la liste des parametres gérés par l'écran */
  public abstract Parametre[] parametreList();

  /**
   * Enregistrement de l'ensemble des parametres
   * d'après ce qui est saisi dans le formulaire.
   * 
   * Parcourre l'ensemble du tableau {@link #parametreList()}
   * et appel la méthode {@link Parametre#updateParams()}
   * @return
   */
  public WOComponent doUpdateAllParametres() {
  	
  	for (Parametre parametre : parametreList()) {
  		parametre.updateParams();
  	}
  	
  	return null;
  }
}
