package fr.univlr.cri.conges;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.conges.EOCET;
import fr.univlr.cri.conges.eos.modele.conges.EOCETTransaction;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.ycrifwk.utils.UtilDb;

/**
 * Ecran de saisie de régularisation CET
 * 
 * @author ctarade
 *
 */
public class AdminCETRegularisation 
	extends YCRIWebPage {

	// la date de début de régime pérenne pour affichage
	public String dateCetDebutRegimePerenne = EOCET.STR_DATE_CET_DEBUT_REGIME_PERENNE;
	
	// l'agent a qui faire la saisie
	public EOIndividu individuSelected;
	
	// 
	public String motif;
	public String valeur;
	public NSTimestamp dateValeur;
	
	//
	public boolean isModeSaisieTransaction;
	
	// temoin pour forcer le rafraichissement de l'etat CET de l'agent
	public boolean shouldReloadCetEtat;
	
	// valeur bidon
	public NSTimestamp cetEtatDateDebutAnnee;
	
	public AdminCETRegularisation(WOContext context) {
		super(context);
	}
	
	
	// setters
	
	/**
	 * Positioner les valeurs par défaut si changement d'individu
	 */
	public void setIndividuSelected(EOIndividu value) {
		EOIndividu prevIndividuSelected = individuSelected;
		individuSelected = value;
		if (individuSelected != prevIndividuSelected) {
			motif = "CET initial suite à mutation";
			valeur = "00:00";
			dateValeur = DateCtrlConges.date1erJanAnneeUniv(DateCtrlConges.now());
			isModeSaisieTransaction = true;
			shouldReloadCetEtat = true;
		}
	}
	
	
	// getters
	
	/**
	 * Le binding dateDebutAnnee de {@link CetEtat}
	 */
	public NSTimestamp getCetEtatDateDebutAnnee() {
		return DateCtrlConges.dateDebutAnnee(DateCtrlConges.now());
	}
	
	
	// navigation
	
	/**
	 * Passer en mode saisie d'une nouvelle transaction
	 */
	public WOComponent toSaisieNouvelleTransaction() {
		isModeSaisieTransaction = true;
		
		return null;
	}
	
	
	// actions
	
	/**
	 * Sauvegarde la nouvelle transaction
	 */
	public WOComponent doSaisieNouvelleTransaction() {
		
		EOCETTransaction.createTransaction(
				individuSelected, motif, dateValeur, valeur);
		
		try {
			UtilDb.save(edc, false);
			// masquer la zone de saisie de la transaction
			isModeSaisieTransaction = false;
			shouldReloadCetEtat = true;
		} catch (Throwable e) {
			setErrorMessage(e.getMessage());
		}
		
		return null;
	}
	
	/**
	 * Annuler la saisie de la nouvelle transaction
	 * @return
	 */
	public WOComponent doAnnulerSaisieNouvelleTransaction() {
		isModeSaisieTransaction = false;
		
		return null;
	}
}