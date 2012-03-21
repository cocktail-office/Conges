package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.objects.Planning;


/**
 * Interface de saisie d'une demande CET pour le compte d'un agent
 * 
 * @author ctarade
 *
 */
public class AdminCETSaisieDemande 
	extends A_ComponentAnneeUniv {

	// agent trouvé
	public EOIndividu individuSelected;
	
	// liste des affectations annuelles de l'agent pour l'année donnée
	public NSArray affAnnList;
	public EOAffectationAnnuelle affAnnSelected;
	public EOAffectationAnnuelle affAnnItem;	

	public Planning planningForAffAnnSelected;
	
	public AdminCETSaisieDemande(WOContext context) {
		super(context);
	}
	
	/**
	 * RAZ de l'individu
	 */
	public void onChangeAnneUnivSelectionnee() {
		setIndividuSelected(null);
	}

	
	/**
	 * Controler que l'individu selectionné peut faire l'objet
	 * d'une saisie (demande non faite ...)
	 * @param value
	 */
	public void setIndividuSelected(EOIndividu value) {
		individuSelected = value;
		
		// raz
		planningForAffAnnSelected = null;
		affAnnList = null;
		
		if (individuSelected != null) {
			affAnnList = EOAffectationAnnuelle.findAffectationsAnnuelleInContext(
					edc, individuSelected.oid(), null, selectedAnneeUniv);
			
			// on selectionne le premier
			if (affAnnList.count() > 0) {
				setAffAnnSelected((EOAffectationAnnuelle) affAnnList.objectAtIndex(0));
			}

		}
		
	}

	/**
	 * Charger le planning {@link #planningForAffAnnSelected} lors du
	 * changement de selection d'affectation annuelle
	 * 
	 * @param value
	 */
	public void setAffAnnSelected(EOAffectationAnnuelle value) {
		affAnnSelected = value;
		if (affAnnSelected != null) {
			planningForAffAnnSelected = Planning.newPlanning(affAnnSelected, cngUserInfo(), selectedAnneeUniv);
			planningForAffAnnSelected.setType("R");
		} else {
			planningForAffAnnSelected = null;
		}
	}

}
