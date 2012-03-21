package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

import fr.univlr.cri.conges.eos.modele.planning.EOHoraire;
import fr.univlr.cri.conges.objects.Mois;
import fr.univlr.cri.conges.objects.Planning;
import fr.univlr.cri.conges.objects.Semaine;

/**
 * Un composant affichant une liste de semaine
 * sous forme d'un calendrier.
 * 
 * @author ctarade
 */
public class CalendrierSemaineACocher 
	extends YCRIWebPage {
	
	// variable entrante : le planning concerné
	public Planning planning;
	
	// variable entrante : la liste des semaines a exclure
	public NSArray arraySemainesDisponibles;
	
	// variable entrante : l'horaire a associer
	public EOHoraire horaire;

	// variable sortante : la liste des semaines selectionnées
	public NSMutableArray arraySemainesSelectionnees;
	
	
	// un mois dans la liste
	public Mois moisItem;
	// une semaine dans la liste
	public Semaine semaineItem;
	
	private final static String SEMAINE_SELECTIONNEE 				= "vert";
	private final static String SEMAINE_NON_SELECTIONNEE 		= "blanc";
	private final static String SEMAINE_NON_ASSOCIABLE_A_HORAIRE	 		= "grisClair";
	private final static String SEMAINE_NON_DISPONIBLE 					= "gris";
	
	public CalendrierSemaineACocher(WOContext context) {
		super(context);
	}
	
	/**
	 * Indique si l'horaire passé en parametre peut être associé 
	 * ou non à la semaine (si non, alors on grise la coche)
	 * @return
	 */
	private boolean isSemaineNonAssociableAHoraire() {
		return horaire == null || !horaire.isAssociableALaSemaine(semaineItem);
	}
	
	
	/**
	 * 
	 * @return
	 */
	private boolean isSemaineNonDisponible() {
		return arraySemainesDisponibles != null && !arraySemainesDisponibles.containsObject(semaineItem);
	}
	
	/**
	 * Disponibilitié de la case à cocher
	 * @return
	 */
	public boolean isDisabledCheckboxSemaineItem() {
		return isSemaineNonAssociableAHoraire() || isSemaineNonDisponible();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDefaultSemaineItemCssClasse() {
		String clazz = SEMAINE_NON_DISPONIBLE;
		if (!isSemaineNonDisponible()) {
			if (getIsCheckedSemaineItem()) {
				clazz = SEMAINE_SELECTIONNEE;
			} else if (isSemaineNonAssociableAHoraire()) {
				clazz = SEMAINE_NON_ASSOCIABLE_A_HORAIRE;
			} else {
				clazz = SEMAINE_NON_SELECTIONNEE;
			}
		}
		return clazz;
	}
	
	public void setIsCheckedSemaineItem(boolean value) {
		if (value) {
			arraySemainesSelectionnees.addObject(semaineItem);
		} else {
			arraySemainesSelectionnees.removeIdenticalObject(semaineItem);
		}
	}
	
	public boolean getIsCheckedSemaineItem() {
		return arraySemainesSelectionnees.containsObject(semaineItem);
	}
}