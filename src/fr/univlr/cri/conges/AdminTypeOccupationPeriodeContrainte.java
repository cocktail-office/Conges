package fr.univlr.cri.conges;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation;
import fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupationParametre;

/**
 * Ecran de gestion des contraintes des types d'occupations par période
 * 
 * @author Cyril TARADE <cyril.tarade at cocktail.org>
 */
public class AdminTypeOccupationPeriodeContrainte 
	extends YCRIWebPage {
	
	/** binding */
	public EOTypeOccupation typeOcc;
	
	public EOTypeOccupationParametre typeOccParametreSelected;
	public EOTypeOccupationParametre typeOccParametreItem;
	
	public boolean isEnCoursDAjout;

	public NSTimestamp newDDebut;
	public NSTimestamp newDFin;
	
	public AdminTypeOccupationPeriodeContrainte(WOContext context) {
		super(context);
		typeOccParametreSelected = null;
	}
	
	/**
	 * Entrer en mode modification
	 * @return
	 */
	public WOComponent modifier() {
		setErrorMessage(null);
		typeOccParametreSelected = typeOccParametreItem;
		return null;
	}

	/**
	 * Ajout d'une nouvelle contrainte
	 * @return
	 */
	public WOComponent toAjout() {
		setErrorMessage(null);
		newDDebut = null;
		newDFin = null;
		typeOccParametreSelected = null;
		isEnCoursDAjout = true;
		return null;
	}

	/**
	 * Annuler l'operation en cours
	 * @return
	 */
	public WOComponent annuler() {
		setErrorMessage(null);
		newDDebut = null;
		newDFin = null;
		isEnCoursDAjout = false;
		typeOccParametreSelected = null;
		edc.revert();
		return null;
	}

	/**
	 * Sauvegarder
	 * @return
	 * @throws Throwable 
	 */
	public WOComponent sauvegarder() throws Throwable {
		setErrorMessage(null);
		try {
			if (isEnCoursDAjout) {
				EOTypeOccupationParametre.nouvelleContrainteForcee(
						edc, newDDebut, newDFin, typeOcc);
			}
			sauvegarde(false);
			if (isEnCoursDAjout) {
				newDDebut = null;
				newDFin = null;
			}
			isEnCoursDAjout = false;
			typeOccParametreSelected = null;
		} catch (Exception e) {
			setErrorMessage(e.getMessage());
		}
		return null;
	}

	/**
	 * Supprimer
	 * @return
	 * @throws Throwable 
	 */
	public WOComponent supprimer() throws Throwable {
		edc.deleteObject(typeOccParametreItem);
		sauvegarder();
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isEnCoursDeModification() {
		boolean isEnCoursDeModification = false;
		
		if (typeOccParametreSelected != null) {
			isEnCoursDeModification = true;
		}
		
		return isEnCoursDeModification;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isAfficherSauvegarderAnnuler() {
		boolean isAfficherSauvegarderAnnuler = false;
		
		if(isEnCoursDAjout || isEnCoursDeModification()) {
			isAfficherSauvegarderAnnuler = true;
		}
		
		return isAfficherSauvegarderAnnuler;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isAfficherAjouter() {
		boolean isAfficherAjouter = false;
		
		if(!isEnCoursDAjout && !isEnCoursDeModification()) {
			isAfficherAjouter = true;
		}
		
		return isAfficherAjouter;
	}
	
}