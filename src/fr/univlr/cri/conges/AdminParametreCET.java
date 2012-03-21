package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;

import fr.univlr.cri.conges.objects.Parametre;

/**
 * Gestion des paramètres relatif au CET
 * 
 * @author ctarade
 */
public class AdminParametreCET 
	extends A_AdminParametre {

	public Parametre parametreDebutDemandeCet 	= Parametre.PARAM_DEBUT_DEMANDE_CET;
	public Parametre parametreFinDemandeCet 		= Parametre.PARAM_FIN_DEMANDE_CET;
  public Parametre parametreSeuilCongesConsommesJour7h00Nm1PourEpargneCet 		= 	Parametre.PARAM_SEUIL_CONGES_CONSOMMES_JOUR_7H00_NM1_POUR_EPARGNE_CET;
  public Parametre parametreSeuilReliquatHeuresPourEpargneCet 		= 	Parametre.PARAM_SEUIL_RELIQUAT_HEURES_POUR_EPARGNE_CET;
  public Parametre parametrePlafondEpargneCet	= Parametre.PARAM_PLAFOND_EPARGNE_CET;
  public Parametre parametreSignaturePresident 	= Parametre.PARAM_SIGNATURE_PRESIDENT;
  public Parametre parametreVerifierStatutDemandeEpargneCet = Parametre.PARAM_VERIFIER_STATUT_DEMANDE_EPARGNE_CET;
  public Parametre parametreAutoriserDemandeEpargneCetCddNonCdi = Parametre.PARAM_AUTORISER_DEMANDE_EPARGNE_CET_CDD_NON_CDI;
  public Parametre parametreCetOptionAncienSystemeAuDela20Jours = Parametre.PARAM_CET_OPTION_ANCIEN_SYSTEME_AU_DELA_20_JOURS;
  public Parametre parametreMessageEditionDemandeEpargneCet 	= Parametre.PARAM_MESSAGE_EDITION_DEMANDE_EPARGNE_CET;

	public AdminParametreCET(WOContext context) {
		super(context);
	}

	@Override
	public Parametre[] parametreList() {
		return new Parametre[] {
				parametreDebutDemandeCet, 
				parametreFinDemandeCet, 
				parametreSeuilCongesConsommesJour7h00Nm1PourEpargneCet,
				parametreSeuilReliquatHeuresPourEpargneCet,
				parametrePlafondEpargneCet, 
				parametreSignaturePresident, 
				parametreVerifierStatutDemandeEpargneCet,
				parametreAutoriserDemandeEpargneCetCddNonCdi,
				parametreCetOptionAncienSystemeAuDela20Jours, 
				parametreMessageEditionDemandeEpargneCet
		};
	}
}