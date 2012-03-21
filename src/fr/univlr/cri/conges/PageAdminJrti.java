package fr.univlr.cri.conges;

import com.webobjects.appserver.*;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.YCRIWebPage;
import fr.univlr.cri.conges.constantes.ConstsJour;
import fr.univlr.cri.conges.constantes.ConstsPlanning;
import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.objects.Planning;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.ycrifwk.utils.UtilDb;

/**
 * Ecran de gestion des rachats de RTT et CET (1 a 4 jours)
 * 
 * @author ctarade
 */
public class PageAdminJrti extends YCRIWebPage {
	
	public EOAffectationAnnuelle selectedAffAnn;
	public EOStructure selectedService;
    
	// la liste du nombre de jours rachetables
	public NSArray jourList;
	public Integer jourItem;
	public Integer jourJrtiSelected;
	
	// tous les jours possibles
	private final static Integer N_DAY_0 = new Integer(0);
	private final static Integer N_DAY_1 = new Integer(1);
	private final static Integer N_DAY_2 = new Integer(2);
	private final static Integer N_DAY_3 = new Integer(3);
	private final static Integer N_DAY_4 = new Integer(4);

	// le champ libre de jour a racheter
	public Integer jourCet;	

	// le champ libre de jour a racheter sur CET vers RAFP
	public Integer jourBasculeCetVersRafp;	
	
	// message d'erreur
	public String errorMessage;
	
	public PageAdminJrti(WOContext context) {
		super(context);
		initComponent();
	}

	/**
	 * Initialisation du composant
	 */
	private void initComponent() {
		// 1 a 4 jours rachetables (0 pour pas de rachat)
		jourList = new NSArray(new Integer[]{N_DAY_0,N_DAY_1,N_DAY_2,N_DAY_3,N_DAY_4});
	}

	public boolean isAffAnnSelected() {
		return selectedAffAnn != null;
	}
	
	/**
	 * Enregistrer la valeur dans la base de donn�es.
	 */
	private void saveRecord() {
		if (selectedAffAnn != null) {
			selectedAffAnn.setJoursJrti(jourJrtiSelected);
			selectedAffAnn.setJoursRachatCetApresDecision(jourCet);
			selectedAffAnn.setJoursBasculeRafpApresDecision(jourBasculeCetVersRafp);
		}
	}
    
	/**
	 * Bouton enregister
	 * @throws Throwable 
	 */
	public WOComponent enregistrer() throws Throwable {
		if (isValid()) {
			saveRecord();
	    UtilDb.save(edc, true);

			// recalculer les valeurs
			recharger();
		}
		return null;
	}
	
	/**
	 * Indique s'il n'y a pas de depassement
	 * @return
	 */
	private boolean isValid() {
		boolean isValid = true;
		
		errorMessage = StringCtrl.emptyString();
		
		int jourJrtiInDatabase = selectedAffAnn.joursJrti().intValue();
		boolean hasJrtiChanged = (jourJrtiSelected.intValue() != jourJrtiInDatabase);
		// controler que le restant de conges permet la rachat
		if (hasJrtiChanged) {
			isValid = (jourJrtiSelected.intValue() * ConstsJour.DUREE_JOUR_7H00) <= (minutesCongesRestant() + jourJrtiInDatabase * ConstsJour.DUREE_JOUR_7H00);
			if (!isValid) {
				errorMessage = "Les cong&eacute;s restants ne sont pas suffisants pour faire ce rachat !";
			}
		}
		
		if (isValid) {
			// controler que le restant de conges permet la rachat
			int jourCetInDatabase = selectedAffAnn.joursRachatCetApresDecision().intValue();
			boolean hasRachatCetChanged = (jourCet.intValue() != jourCetInDatabase);
			// controler que le restant de conges permet la rachat
			if (hasRachatCetChanged) {
				isValid = (jourCet.intValue() * ConstsJour.DUREE_JOUR_7H00) <= (minutesCetRestant() + jourCetInDatabase * ConstsJour.DUREE_JOUR_7H00);
				if (!isValid) {
					errorMessage = "L'&eacute;pargne CET restante n'est pas suffisante pour faire ce rachat !";
				}
			}
		}		
		
		if (isValid) {
			// controler que le restant de conges permet la rachat
			int jourBasculeCetVersRafpInDatabase = selectedAffAnn.joursBasculeCetVersRafpApresDecision().intValue();
			boolean hasRachatBasculeCetVersRafpChanged = (jourBasculeCetVersRafp.intValue() != jourBasculeCetVersRafpInDatabase);
			// controler que le restant de conges permet la rachat
			if (hasRachatBasculeCetVersRafpChanged) {
				isValid = (jourCet.intValue() * ConstsJour.DUREE_JOUR_7H00) <= (minutesCetRestant() + jourBasculeCetVersRafpInDatabase * ConstsJour.DUREE_JOUR_7H00);
				if (!isValid) {
					errorMessage = "L'&eacute;pargne CET restante n'est pas suffisante pour faire ce rachat !";
				}
			}
		}
		
		return isValid;		
	}

	/**
	 * Forcer le rechargement du plannings selectionne
	 * @return
	 */
	private void recharger() {
		// verifier s'il est dans la session ou pas
		if (laSession.isPlanningInCache(selectedAffAnn)) {
			Planning planning = laSession.getCachePlanningForAffAnn(selectedAffAnn);
			laSession.clearCachePlanning(planning);
		}
		// on le recalcule
		Planning planning = Planning.newPlanning(selectedAffAnn, cngUserInfo(), laSession.dateRef());
		planning.setType(ConstsPlanning.REEL);
		// et on force l'affectation annuelle et tout ce qui nous interresse a etre recharge
		EOAffectationAnnuelle.invalidateEOAffectationAnnuelle(edc, selectedAffAnn);
	}
	
	/**
	 * Override pour recalcul du reliquat lors du changement d'affectation
	 */
	public void setSelectedAffAnn(EOAffectationAnnuelle value) {
		selectedAffAnn = value;
		fillInterface();
		// annuler ce qu'il s'est pass� sur le setHeuresReliquatInitial()
		edc.revert();
	}
    
	/**
	 * Mettre a jour la valeur des jours selon l'affectation annuelle
	 * selectionnee
	 */
	private void fillInterface() {
		if (selectedAffAnn != null) {
			jourJrtiSelected = selectedAffAnn.joursJrti();
			jourCet = selectedAffAnn.joursRachatCetApresDecision();
			jourBasculeCetVersRafp = selectedAffAnn.joursBasculeCetVersRafpApresDecision();
		} else {
			jourJrtiSelected = N_DAY_0;
			jourCet = N_DAY_0;
			jourBasculeCetVersRafp = N_DAY_0;
		}
		// 
		errorMessage = StringCtrl.emptyString();
	}
	
	// getter
	
	/**
	 * Date de fin du planning en cours
	 */
	public String strDateFinAnnee() {
		return DateCtrlConges.dateToString(selectedAffAnn.dateFinAnnee());
	}
 
	/**
	 * Minutes restantes sur le planning en cours
	 */
	public String strCongesRestant() {
		return TimeCtrl.stringForMinutes(minutesCongesRestant());
	}
	
	/**
	 * 
	 * @return
	 */
	private int minutesCongesRestant() {
		return selectedAffAnn.calculAffAnn(ConstsPlanning.REEL).minutesRestantes().intValue();
	}
	
	/**
	 * Date de debut du planning en cours
	 */
	public String strDateDebutAnnee() {
		return DateCtrlConges.dateToString(selectedAffAnn.dateDebutAnnee());
	}
 
	/**
	 * Epargne CET restante a la date du planning en cours
	 */
	public String strCetRestant() {
		return TimeCtrl.stringForMinutes(minutesCetRestant());
	}
	
	/**
	 * 
	 * @return
	 */
	private int minutesCetRestant() {
		int minutes =  0;
		if (selectedAffAnn.individu().toCET() != null) {
			minutes = selectedAffAnn.individu().toCET().minutesRestantesAncienRegime();
		}
		return minutes;
	}
	
	
	// boolean
	
	/**
	 * 
	 */
	public boolean hasError() {
		return !StringCtrl.isEmpty(errorMessage);	
	}
}