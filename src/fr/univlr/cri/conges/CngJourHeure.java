package fr.univlr.cri.conges;

import java.math.BigDecimal;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.constantes.ConstsJour;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.ArrayCtrl;
import fr.univlr.cri.util.StringCtrl;

/**
 * Composant de selection d'une valeur soit en jour &agrave; 7h00 
 * (selection au travers d'un popup), soit en heure.
 * Ce composant doit être au sein d'un formulaire ayant
 * le binding multipleSubmit = <code>true</code>
 * 
 * @author ctarade
 */
public class CngJourHeure 
	extends YCRIWebPage {
	
	// variable sortante (tout est ramené à la minute)
	public int minutes;

	// parametres
	
	/** afficher la saisie en heures */
	public boolean showHeures;
	/** afficher la saisie en jours */
	public boolean showJours;
	/** liste statique des jours (facultatif) */
	public NSArray jourList;
	/** minutes maximum autorisées (facultatif) */
	public int minutesMax;
	
	// cas d'un affichage mixte : que prendre par défaut ?
	// si non précisé, on propose les heures d'abord
	public boolean showDefautHeures = true;
	// gestion du radio button permettant de switcher de l'un a l'autre
	public final String TYPE_HEURE = "H";
	public final String TYPE_JOUR = "J";
	public String typeSelected;

	// si la liste des jours en connue, alors on affiche
	// un popup, sinon une zone de texte libre
	public Integer jourSelected;
	public Integer jourItem;
	
	// si pas de liste de jours, alors zone de texte
	public BigDecimal jour;
	
	// selection des heures
	public String heures;
	
	public CngJourHeure(WOContext context) {
		super(context);
		initComponent();
	}
	
	/**
	 * 
	 */
	private void initComponent() {
		// selection radio par defaut si mixte
		if (isAffichageMixte()) {
			if (showDefautHeures) {
				typeSelected = TYPE_HEURE;
			} else {
				typeSelected = TYPE_JOUR;
			}
		} else {
			if (showHeures) {
				typeSelected = TYPE_HEURE;
			} else {
				typeSelected = TYPE_JOUR;
			}
		}
	}
	
	
	// getters
	
	/**
	 * Indique si pour la selection des jours, il faut afficher un 
	 * popup ou bien une zone libre
	 */
	public boolean isJourLibre() {
		return ArrayCtrl.isEmpty(jourList);
	}
	
	/**
	 * Faut-il afficher les jours et les heures en meme temps (i.e.
	 * les radios buttons)
	 * @return
	 */
	public boolean isAffichageMixte() {
		return showHeures && showJours;
	}

	/**
	 * Desactiver les zones des jours
	 * tout est actif en parametres non mixte
	 * @return
	 */
	public boolean isDisabledJour() {
		return isAffichageMixte() && typeSelected.equals(TYPE_HEURE);
	}
	
	/**
	 * Desactiver les zones des heures
	 * tout est actif en parametres non mixte
	 * @return
	 */
	public boolean isDisabledHeure() {
		return isAffichageMixte() && typeSelected.equals(TYPE_JOUR);
	}
	
	/**
	 * Le nom du bouton cache pour le rafraichissement la page
	 * afin d'avoir unicité si ce composant est affiché plusieurs
	 * fois dans une page
	 * @return
	 */
	public String getNeFaitRienBtnName() {
		return "NeFaitRien_" + hashCode();
	}
	
	/**
	 * Le code js on click pour rafraichir la page en appelant le
	 * bon bouton
	 * @return
	 */
	public String getOnClickRadio() {
		return getNeFaitRienBtnName() + ".click();";
	}
	
	// setters
	
	/**
	 * La selection du popup des jours
	 */
	public void setJourSelected(Integer value) {
		jourSelected = value;
		minutes = 0;
		if (jourSelected != null && !isJourLibre() && typeSelected.equals(TYPE_JOUR)) {
			minutes = jourSelected.intValue() * ConstsJour.DUREE_JOUR_7H00;
		}
	}
	
	/**
	 * L'entrée libre de la valeur des jours
	 */
	public void setJour(BigDecimal value) {
		jour = value;
		minutes = 0;
		if (jour != null && isJourLibre() && typeSelected.equals(TYPE_JOUR)) {
			minutes = jour.intValue() * ConstsJour.DUREE_JOUR_7H00;
		}
	}
	
	/**
	 * L'entrée libre de la valeur des jours
	 */
	public void setHeures(String value) {
		heures = value;
		minutes = 0;
		if (!StringCtrl.isEmpty(heures) && typeSelected.equals(TYPE_HEURE)) {
			minutes = TimeCtrl.getMinutes(heures);
		}
	}
	
	//TODO controle de dépassement
}