package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.objects.Parametre;
/**
 * Ecran qui rassemble l'ensemble des parametres de l'application.
 * 
 * @author ctarade
 */
public class AdminParametreParametres
	extends A_AdminParametre {

    
  public Parametre parametreHeuresDues 				= Parametre.PARAM_HEURES_DUES;
  public Parametre parametreDateMaxReliquat 	= Parametre.PARAM_DATE_MAX_RELIQUAT;

  
  public Parametre parametreAutoriserSaisieAbsenceSurPlanningPrevisionnel 		= 	Parametre.PARAM_AUTORISER_SAISIE_ABSENCE_SUR_PLANNING_PREVISIONNEL;
  public Parametre parametreAutoriserSaisieAbsenceSurPlanningPrevisionnelEnCoursDAnnee 		= 	Parametre.PARAM_FIN_DEMANDE_AUTORISER_SAISIE_ABSENCE_SUR_PLANNING_PREVISIONNEL_EN_COURS_D_ANNEE;

	public Parametre parametreDureeJourConversion							= Parametre.PARAM_DUREE_JOUR_CONVERSION;
	
	public Parametre parametreHeuresCongesMaxi = Parametre.PARAM_HEURES_CONGES_MAXI;
	public Parametre parametreNbreSemainesHautesMaxi = Parametre.PARAM_NBRE_SEMAINES_HAUTES_MAXI;
	public Parametre parametreNbreSemainesHautesMaxiDepassement = Parametre.PARAM_NBRE_SEMAINES_HAUTES_MAXI_DEPASSEMENT;
	
	public Parametre parametreDebutAnneeUniversitaire = Parametre.PARAM_DEBUT_ANNEE_UNIVERSITAIRE;
	
	public Parametre parametreShowCet 														= Parametre.PARAM_SHOW_CET;
	public Parametre parametreShowPause 													= Parametre.PARAM_SHOW_PAUSE;
	public Parametre parametreShowDemandeGardeEnfant 							= Parametre.PARAM_SHOW_DEMANDE_GARDE_ENFANT;
	public Parametre parametreShowDemandeCongesPapier 						= Parametre.PARAM_SHOW_DEMANDE_CONGES_PAPIER;
	
	public Parametre parametreValidationPlanningNiveau 						= Parametre.PARAM_VALIDATION_PLANNING_NIVEAU;
	public NSArray parametreValidationPlanningNiveauPopupValueList = 
			new NSArray(new String[] {
					Parametre.VALIDATION_PLANNING_NIVEAU_VALUE_RESPONSABLE, Parametre.VALIDATION_PLANNING_NIVEAU_DRH_VALUE	});
	
	public Parametre parametreAlerteNbAnneesAnterieuresVisibles = Parametre.PARAM_ALERTE_NB_ANNEES_ANTERIEURES_VISIBLES;
	
  public AdminParametreParametres(WOContext context) {
		super(context);
	}

	@Override
	public Parametre[] parametreList() {
		return new Parametre[] {
				parametreHeuresDues, 
				parametreDateMaxReliquat, 
				parametreAutoriserSaisieAbsenceSurPlanningPrevisionnel, 
				parametreAutoriserSaisieAbsenceSurPlanningPrevisionnelEnCoursDAnnee, 
				parametreDureeJourConversion,
				parametreHeuresCongesMaxi,
				parametreNbreSemainesHautesMaxi,
				parametreNbreSemainesHautesMaxiDepassement,
				parametreDebutAnneeUniversitaire,
				parametreShowCet,
				parametreShowPause,
				parametreShowDemandeGardeEnfant,
				parametreShowDemandeCongesPapier,
				parametreValidationPlanningNiveau,
				parametreAlerteNbAnneesAnterieuresVisibles
		};
	}
}