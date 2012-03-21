package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;

import fr.univlr.cri.conges.objects.Parametre;

/**
 * Ecran de parametres liés aux horaires
 * 
 * @author ctarade
 */
public class AdminParametreHoraire 
	extends A_AdminParametre {

	public Parametre parametreAmDebutMini	 									= Parametre.PARAM_AM_DEBUT_MINI;
	public Parametre parametreAmDebutMaxi 									= Parametre.PARAM_AM_DEBUT_MAXI;
	public Parametre parametrePmFinMini 										=	Parametre.PARAM_PM_FIN_MINI;
	public Parametre parametrePmFinMaxi 										= Parametre.PARAM_PM_FIN_MAXI;
	public Parametre parametrePauseRttGain 									= Parametre.PARAM_PAUSE_RTT_GAIN;
	public Parametre parametrePauseMeridienneDebutMini	    = Parametre.PARAM_PAUSE_MERIDIENNE_DEBUT_MINI;
	public Parametre parametrePauseMeridienneDureeMini 			= Parametre.PARAM_PAUSE_MERIDIENNE_DUREE_MINI;
	public Parametre parametrePauseMeridienneFinMaxi 				= Parametre.PARAM_PAUSE_MERIDIENNE_FIN_MAXI;
	public Parametre parametrePauseRttDuree 								= Parametre.PARAM_PAUSE_RTT_DUREE;
	public Parametre parametreDemiJourneeDureeMaxi					= Parametre.PARAM_DEMI_JOURNEE_DUREE_MAXI;
	
	public Parametre parametreHeuresTravailleesMiniHorBonus = Parametre.PARAM_HEURES_TRAVAILLEES_MINI_HOR_BONUS;
	public Parametre parametreDebutJourneeBonus							= Parametre.PARAM_DEBUT_JOURNEE_BONUS;
	public Parametre parametreFinJourneeBonus 							= Parametre.PARAM_FIN_JOURNEE_BONUS;
	public Parametre parametreCoefSamediMatinBonus 					= Parametre.PARAM_COEF_SAMEDI_MATIN_BONUS;
	public Parametre parametreCoefDebordementBonus 					= Parametre.PARAM_COEF_DEBORDEMENT_BONUS;
	
	public Parametre parametreCoefHsupDebordement						= Parametre.PARAM_COEF_HSUP_DEBORDEMENT;
	public Parametre parametreCoefHsupSamMat5j		 					= Parametre.PARAM_COEF_HSUP_SAM_MAT_5J;
	public Parametre parametreCoefHsupSamApremDimJf					= Parametre.PARAM_COEF_HSUP_SAM_APREM_DIM_JF;
	
	public Parametre parametreDureeHoraireHebdoMini						= Parametre.PARAM_DUREE_HORAIRE_HEBDO_MINI;
	public Parametre parametreDureeHoraireHebdoMaxi						= Parametre.PARAM_DUREE_HORAIRE_HEBDO_MAXI;
	public Parametre parametreDureeHoraireHebdoMaxiHorsNormes	= Parametre.PARAM_DUREE_HORAIRE_HEBDO_MAXI_HORS_NORMES;
	public Parametre parametreLockHorairesTypes								= Parametre.PARAM_LOCK_HORAIRES_TYPES;
	
	public AdminParametreHoraire(WOContext context) {
		super(context);
	}
	
	@Override
	public Parametre[] parametreList() {
		return new Parametre[] {
				parametreAmDebutMini, 
				parametreAmDebutMaxi, 
				parametrePmFinMini, 
				parametrePmFinMaxi, 
				parametrePauseRttGain,
				parametrePauseMeridienneDebutMini,
				parametrePauseMeridienneDureeMini, 
				parametrePauseMeridienneFinMaxi, 
				parametrePauseRttDuree, 
				parametreDemiJourneeDureeMaxi,
				parametreHeuresTravailleesMiniHorBonus, 
				parametreDebutJourneeBonus,
				parametreFinJourneeBonus,
				parametreCoefSamediMatinBonus, 
				parametreCoefDebordementBonus, 
				parametreCoefHsupDebordement, 
				parametreCoefHsupSamMat5j, 
				parametreCoefHsupSamApremDimJf,
				parametreDureeHoraireHebdoMini,
				parametreDureeHoraireHebdoMaxi, 
				parametreDureeHoraireHebdoMaxiHorsNormes, 
				parametreLockHorairesTypes
		};
	}
}