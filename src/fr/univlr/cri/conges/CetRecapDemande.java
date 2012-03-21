package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;

import fr.univlr.cri.conges.constantes.ConstsJour;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;

/**
 * Récapitulatif textuel de la demande relative à un CET :
 * - maintien / exercice du droit d'option sur CET ancien régime
 * - transfert de l'ancien régime vers le régime pérenne
 * - épargne de reliquats 
 * - exercice du droit d'option sur le régime pérenne
 * 
 * @author ctarade
 *
 */
public class CetRecapDemande
	extends YCRIWebPage {
	
	// bindings entrants
	// planning concerné 
	public EOAffectationAnnuelle affAnn;
	
	public CetRecapDemande(WOContext context) {
		super(context);
	}

	
	// BOOLEAN INTERFACE
	
	/**
	 * Indique si l'agent a exercicé son droit d'option sur l'ancien régime
	 */
  public boolean isExerciceDroitOptionAncienRegime() {
  	return (affAnn.toMouvementCetDemandeIndemnisationAncienRegime() != null &&
  			affAnn.toMouvementCetDemandeMaintienCetAncienRegime() != null &&
  			affAnn.toMouvementCetDemandeTransfertRafpAncienRegime() != null &&(
  					affAnn.toMouvementCetDemandeIndemnisationAncienRegime().mouvementMinutes().intValue() > 0 ||
  					affAnn.toMouvementCetDemandeMaintienCetAncienRegime().mouvementMinutes().intValue() > 0 ||
  					affAnn.toMouvementCetDemandeTransfertRafpAncienRegime().mouvementMinutes().intValue() > 0));
  }
	
	/**
	 * Indique si l'agent a exercicé son droit d'option
	 */
  public boolean isExerciceDroitOption() {
  	return affAnn.toMouvementCetDemandeIndemnisation().mouvementMinutes().intValue() > 0 ||
  		affAnn.toMouvementCetDemandeMaintienCet().mouvementMinutes().intValue() > 0 ||
  		affAnn.toMouvementCetDemandeTransfertRafp().mouvementMinutes().intValue() > 0;
  }
 
	
	
	// DROIT D'OPTION SUR L'ANCIEN REGIME
	
	/**
	 * {@link #minutesTotalConcerneAncienRegime()} converties en jours a 7h00
	 * @return
	 */
	public Float getTotalJourConcerneAncienRegimeJoursA7h00() {
		Float total = null;
		
		total = new Float((float) minutesTotalConcerneAncienRegime() / (float) ConstsJour.DUREE_JOUR_7H00);
		
		return total;
	}
	
	/**
	 * {@link #minutesTotalConcerneAncienRegime()} converties en heures
	 * @return
	 */
	public String getTotalJourConcerneAncienRegimeHeures() {
		return TimeCtrl.stringForMinutes(minutesTotalConcerneAncienRegime());
	}
	
	/**
	 * Le nombre de minutes concernés pour l'exercice du droit 
	 * d'option sur l'ancien régime
	 * @return
	 */
	private int minutesTotalConcerneAncienRegime() {
		int minutes = 0;
		
		minutes = 
			affAnn.toMouvementCetDemandeTransfertRafpAncienRegime().mouvementMinutes().intValue() + 
			affAnn.toMouvementCetDemandeIndemnisationAncienRegime().mouvementMinutes().intValue() + 
			affAnn.toMouvementCetDemandeMaintienCetAncienRegime().mouvementMinutes().intValue();
		
		return minutes;
	}
	
	
	// EPARGNE DE RElIQUATS
	
	/**
	 * L'année universitaire N-1 (on se base sur {@link #affAnn})
	 */
	public String getStrAnneeUnivNm1() {
		return DateCtrlConges.anneeUnivForDate(affAnn.dateDebutAnnee().timestampByAddingGregorianUnits(-1,0,0,0,0,0));
	}
	
	
	// DROIT D'OPTION REGIME PERENNE

	/**
	 * {@link #minutesTotalConcerneAncienRegime()} converties en jours a 7h00
	 * @return
	 */
	public Float getTotalJourConcerneJoursA7h00() {
		Float total = null;
		
		total = new Float(minutesTotalConcerne() / ConstsJour.DUREE_JOUR_7H00);
		
		return total;
	}
	
	/**
	 * {@link #minutesTotalConcerneAncienRegime()} converties en heures
	 * @return
	 */
	public String getTotalJourConcerneHeures() {
		return TimeCtrl.stringForMinutes(minutesTotalConcerne());
	}
	
	/**
	 * Le nombre de minutes concernés pour l'exercice du droit 
	 * d'option sur l'ancien régime
	 * @return
	 */
	private int minutesTotalConcerne() {
		int minutes = 0;
		
		minutes = 
			affAnn.toMouvementCetDemandeTransfertRafp().mouvementMinutes().intValue() + 
			affAnn.toMouvementCetDemandeIndemnisation().mouvementMinutes().intValue() + 
			affAnn.toMouvementCetDemandeMaintienCet().mouvementMinutes().intValue();
		
		return minutes;
	}
	
}