package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.eocontrol.EOQualifier;

import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRLog;

/**
 * Liste des passe droits et autres privilèges accordés
 * 
 * @author ctarade
 */
public class AdminPasseDroitList 
	extends A_ComponentAnneeUniv {
  
	//
	public WODisplayGroup affectationAnnuelleDg;
	public EOAffectationAnnuelle affectationAnnuelleItem;
	
	// temoin s'il ne faut afficher que les passe droit ou tout le monde
	public boolean isVoirUniquementAgentPasseDroit;
	
	// recherche
	public String searchString;
	
	public AdminPasseDroitList(WOContext context) {
		super(context);
		initComponent();
	}
	/**
	 * 
	 */
	private void initComponent() {
		// limiter la liste au passe droit par defaut
		setIsVoirUniquementAgentPasseDroit(true);
	}

	@Override
	public void onChangeAnneUnivSelectionnee() {
		affectationAnnuelleDg.queryBindings().setObjectForKey(selectedAnneeUniv, EOAffectationAnnuelle.DATE_DEBUT_ANNEE_KEY);
		affectationAnnuelleDg.qualifyDataSource(); // fetch
		// classement alphabetique ici pour accelerer l'affichage
		affectationAnnuelleDg.setSortOrderings(EOAffectationAnnuelle.SORT_STRUCTURE_ET_INDIVIDU);
    // sort 
		affectationAnnuelleDg.updateDisplayedObjects(); 	
	}
	
	/**
	 *
	 * @param value
	 */
	public void setIsVoirUniquementAgentPasseDroit(boolean value) {
		isVoirUniquementAgentPasseDroit = value;
		if (!isVoirUniquementAgentPasseDroit) {
			// oter tous les bindings restrictifs
			affectationAnnuelleDg.queryBindings().removeObjectForKey(EOAffectationAnnuelle.FLAG_DEPASSEMENT_CONGES_AUTORISE_KEY);
			affectationAnnuelleDg.queryBindings().removeObjectForKey(EOAffectationAnnuelle.FLAG_DEP_SEM_HAUTES_KEY);
			affectationAnnuelleDg.queryBindings().removeObjectForKey(EOAffectationAnnuelle.FLAG_HORS_NORME_KEY);
			affectationAnnuelleDg.queryBindings().removeObjectForKey(EOAffectationAnnuelle.FLAG_PASSE_DROIT_KEY);
			affectationAnnuelleDg.queryBindings().removeObjectForKey(EOAffectationAnnuelle.FLAG_TEMPS_PARTIEL_ANNUALISE_KEY);
		} else {
			// les remettre
			affectationAnnuelleDg.queryBindings().setObjectForKey("1", EOAffectationAnnuelle.FLAG_DEPASSEMENT_CONGES_AUTORISE_KEY);
			affectationAnnuelleDg.queryBindings().setObjectForKey("1", EOAffectationAnnuelle.FLAG_DEP_SEM_HAUTES_KEY);
			affectationAnnuelleDg.queryBindings().setObjectForKey("1", EOAffectationAnnuelle.FLAG_HORS_NORME_KEY);
			affectationAnnuelleDg.queryBindings().setObjectForKey("1", EOAffectationAnnuelle.FLAG_PASSE_DROIT_KEY);
			affectationAnnuelleDg.queryBindings().setObjectForKey("1", EOAffectationAnnuelle.FLAG_TEMPS_PARTIEL_ANNUALISE_KEY);
		}
		affectationAnnuelleDg.qualifyDataSource(); // fetch
	}

	
	/**
	 * 
	 * @param value
	 */
	public void setSearchString(String value) {
		searchString = value;
		
		// qualifier
  	if (!StringCtrl.isEmpty(searchString)) {
  		EOQualifier qualSearch = CRIDataBus.newCondition(
  				EOIndividu.getStringQualifierIndividuForNomPrenom(
  	  				EOAffectationAnnuelle.INDIVIDU_KEY, searchString),
  				null);
  		affectationAnnuelleDg.setQualifier(qualSearch);
  	} else {
  		affectationAnnuelleDg.setQualifier(null);
  	}
  	affectationAnnuelleDg.qualifyDataSource(); // fetch
	}
}