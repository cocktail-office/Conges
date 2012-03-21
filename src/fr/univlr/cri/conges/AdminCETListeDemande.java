package fr.univlr.cri.conges;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eocontrol.EOAndQualifier;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSNumberFormatter;

import fr.univlr.cri.conges.constantes.ConstsApplication;
import fr.univlr.cri.conges.constantes.ConstsJour;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOMouvement;
import fr.univlr.cri.conges.eos.modele.planning.I_ClasseMetierNotificationParametre;
import fr.univlr.cri.conges.objects.CetFactory;
import fr.univlr.cri.conges.objects.Parametre;
import fr.univlr.cri.conges.print.CngPdfBoxCtrl;
import fr.univlr.cri.conges.print.ConstsPrint;
import fr.univlr.cri.conges.print.PrintReformulationDemandeCet;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRDataResponse;
import fr.univlr.cri.webext.CRIAlertPage;
import fr.univlr.cri.webext.CRIAlertResponder;

/**
 * Gestion des demandes CET des agents
 * 
 * @author ctarade
 * 
 */
public class AdminCETListeDemande
		extends A_ComponentAnneeUniv
		implements I_ClasseMetierNotificationParametre {

	// liste des demandes
	public EOAffectationAnnuelle affAnnItem;
	private EOAffectationAnnuelle affAnnSelected;

	// modification d'une épargne
	public Integer epargneDecisionEnJour7h00;

	// modification droit d'option
	public Integer maintienDecisionEnJour7h00;
	public Integer indemnisationDecisionEnJour7h00;
	public Integer transfertRafpDecisionEnJour7h00;

	// l'etat de la demande #affAnnItem
	private int etatDemande;

	// temoin pour savoir si on modifie l'épargne ou le droit d'option
	private boolean isModifDroitOption;

	// recherche
	public String searchString;

	/**
	 * La liste de toutes les demandes CET pour l'année universitaire selectionnée
	 * {@link A_ComponentAnneeUniv#selectedAnneeUniv}
	 * 
	 * @return
	 */
	public WODisplayGroup mouvementDg;
	public EOMouvement mouvementItem;

	private boolean shouldRefreshMouvementDg;

	public AdminCETListeDemande(WOContext context) {
		super(context);
		clearSelection();
		shouldRefreshMouvementDg = true;
	}

	private static String signaturePresident;

	// parametres GRHUM_PARAMETRES
	private static String grhumEtab = app.config().stringForKey(ConstsApplication.CONFIG_GRHUM_ETAB_KEY);
	private static String grhumPresident = app.config().stringForKey(ConstsApplication.CONFIG_GRHUM_PRESIDENT_KEY);
	private static String mainLogoURL = app.config().stringForKey(Application.MAIN_LOGO_URL_KEY);

	/**
	 * @deprecated
	 * @see #initStaticField(Parametre)
	 */
	public static void initStaticFields(
			String aSignaturePresident) {
		signaturePresident = aSignaturePresident;
		;
	}

	/**
	 * @see I_ClasseMetierNotificationParametre
	 */
	public static void initStaticField(
			Parametre parametre) {
		if (parametre == Parametre.PARAM_SIGNATURE_PRESIDENT) {
			signaturePresident = parametre.getParamValueString();
		}
	}

	/**
	 * forcer les donnees du DisplayGroup a etre rechargees.
	 */
	private void refreshMouvementDg() {
		mouvementDg.queryBindings().setObjectForKey(
				new Integer(EOMouvement.TYPE_CET_DEMANDE_EPARGNE), EOMouvement.MOUVEMENT_TYPE_KEY);
		mouvementDg.queryBindings().setObjectForKey(
				selectedAnneeUniv, EOAffectationAnnuelle.DATE_DEBUT_ANNEE_KEY);

		// qualifier
		NSArray quals = new NSArray();

		// ne conserver que les affectations annuelles ayant un individu et une
		// structure correctement renseignées (cas des AFFECTATION avec TEM_VALIDE =
		// 'N')
		quals = quals.arrayByAddingObject(
				CRIDataBus.newCondition(
						EOMouvement.TO_AFFECTATION_ANNUELLE_KEY + "." + EOAffectationAnnuelle.INDIVIDU_KEY + " <> nil and " +
								EOMouvement.TO_AFFECTATION_ANNUELLE_KEY + "." + EOAffectationAnnuelle.STRUCTURE_KEY + " <> nil"));

		if (!StringCtrl.isEmpty(searchString)) {
			EOQualifier qualSearch = CRIDataBus.newCondition(
					EOIndividu.getStringQualifierIndividuForNomPrenom(
							EOMouvement.TO_AFFECTATION_ANNUELLE_KEY + "." + EOAffectationAnnuelle.INDIVIDU_KEY, searchString),
					null);
			quals = quals.arrayByAddingObject(qualSearch);
		}

		mouvementDg.setQualifier(new EOAndQualifier(quals));

		mouvementDg.qualifyDataSource(); // fetch
		// classer par nom
		mouvementDg.setSortOrderings(EOMouvement.SORT_INDIVIDU);
		mouvementDg.updateDisplayedObjects(); // sort

	}

	/**
	 * Remise à zéro de certaines variables
	 */
	public void appendToResponse(WOResponse arg0, WOContext arg1) {
		//
		if (shouldRefreshMouvementDg) {
			refreshMouvementDg();
			shouldRefreshMouvementDg = false;
		}

		super.appendToResponse(arg0, arg1);
	}

	/**
	 * Changement d'année universitaire : on ne fait rien, la liste des mouvements
	 * est rechargée systèmatiquement
	 */
	public void onChangeAnneUnivSelectionnee() {
		shouldRefreshMouvementDg = true;
	}

	/**
	 * Raz selection
	 */
	private void clearSelection() {
		affAnnSelected = null;
		isModifDroitOption = false;
	}

	// getters

	/**
	 * Le nombre de jour maximum maintenable en CET d'après la décision d'épargne
	 * du gestionnaire
	 * 
	 * @return
	 */
	public int getMaintienDecisionMaxAffAnnSelected() {
		int maintienMax = 0;

		maintienMax = affAnnSelected.cetFactory().maintienCetMaximumAuDela20JEnJour7h00Arrondi(
				getAlimentationMinutes(affAnnSelected));

		return maintienMax;
	}

	/**
	 * La valeur des jours surlesquels doivent d'exercer le droit d'option
	 * 
	 * @return
	 */
	public int getTotalDecisionDroitOption() {
		int total = 0;

		total = affAnnSelected.cetFactory().depassementSeuil20JoursPourEpargneCetEnJours7h00Arrondi(
				getAlimentationMinutes(affAnnSelected));

		return total;
	}

	/**
	 * Demande : le solde du CET après transfert, épargne et exercice du droit
	 * d'option
	 * 
	 * @return
	 */
	public float getSoldeDemandeFinalEnJour7h00() {
		float solde = (float) 0;

		solde = affAnnItem.cetFactory().soldeApresDemandeTransfertEpargneEtExerciceDroitDOptionEnMinutes() / (float) ConstsJour.DUREE_JOUR_7H00;

		return solde;
	}

	/**
	 * Decision : le solde du CET après transfert, épargne et exercice du droit
	 * d'option
	 * 
	 * @return
	 */
	public float getSoldeDecisionFinalEnJour7h00() {
		float solde = (float) 0;

		solde = affAnnItem.cetFactory().soldeApresDecisionTransfertEpargneEtExerciceDroitDOptionEnMinutes() / (float) ConstsJour.DUREE_JOUR_7H00;

		return solde;
	}

	// setters

	/**
	 * 
	 */
	public void setMouvementItem(EOMouvement value) {
		mouvementItem = value;
		if (mouvementItem != null) {
			setAffAnnItem(mouvementItem.toAffectationAnnuelle());
		}
	}

	/**
	 * 
	 * @param value
	 */
	public void setSearchString(String value) {
		searchString = value;
		shouldRefreshMouvementDg = true;
	}

	/**
	 * Effectuer des sommes pour chaque changement de ligne
	 */
	private void setAffAnnItem(EOAffectationAnnuelle value) {
		affAnnItem = value;
		// etat de la demande
		if (affAnnItem != null) {
			etatDemande = affAnnItem.cetFactory().etatDemande();
		}
	}

	// display

	/**
	 * La classe CSS a appliquer aux demandes CET.
	 */
	public String getTrAffAnnItemClass() {
		String classCss = "listboxLine";

		if (affAnnItem.cetFactory().isAdminDemandeAccepteeALIdentique() ||
				(affAnnItem.cetFactory().isAdminDemandeAccepteeDecisionDifferente())) {
			classCss = "trDemAcc";
		} else if (affAnnItem.cetFactory().isAdminDemandeAutreEpargneSaisie()) {
			classCss = "trDemVis";
		}

		return classCss;
	}

	// disponibilité élements interface

	/**
	 * bouton de modification d'épargne
	 */
	public boolean isShowBtnToSaisieDecisionAutreEpargne() {
		boolean showBtnToSaisieDecisionAutreEpargne = true;

		// vérifier que rien n'est en train d'etre modifié
		if (affAnnSelected != null) {
			showBtnToSaisieDecisionAutreEpargne = false;
		}

		// seulement pour les demandes non traitées
		if (showBtnToSaisieDecisionAutreEpargne &&
				etatDemande != CetFactory.CET_ETAT_DEMANDE_NON_TRAITEE) {
			showBtnToSaisieDecisionAutreEpargne = false;
		}

		// pas de modification pour les demandes d'épargnes à zéro
		// dont il n'y aura pas de droit d'option derrire
		if (showBtnToSaisieDecisionAutreEpargne &&
				affAnnItem.toMouvementCetDemandeEpargne().mouvementMinutes().intValue() == 0 &&
				affAnnItem.toMouvementCetDemandeIndemnisation().mouvementMinutes().intValue() == 0 &&
				affAnnItem.toMouvementCetDemandeMaintienCet().mouvementMinutes().intValue() == 0 &&
				affAnnItem.toMouvementCetDemandeTransfertRafp().mouvementMinutes().intValue() == 0) {
			showBtnToSaisieDecisionAutreEpargne = false;
		}

		return showBtnToSaisieDecisionAutreEpargne;
	}

	/**
	 * On permet d'annuler la decision pour n'avoir que la demande initiale : - en
	 * mode edition, uniquement sur l'element en cours d'édition - en mode
	 * consulation, sur toutes celles qui sont en état en cours de saisie ou
	 * attente
	 * 
	 * @return
	 */
	public boolean isShowBtnDoSupprimerDecision() {
		boolean showBtnDoSupprimerDecision = true;

		// en mode édition ?
		if (affAnnSelected != null) {
			// oui : uniquement sur celle qu'on édite
			if (affAnnSelected != affAnnItem) {
				showBtnDoSupprimerDecision = false;
			}
			// si elle en état attendu
			if (showBtnDoSupprimerDecision &&
					!affAnnSelected.cetFactory().isTransitionEtatAutorisee(CetFactory.CET_ETAT_DEMANDE_NON_TRAITEE)) {
				showBtnDoSupprimerDecision = false;
			}
			// ne pas afficher si on modifie le droit d'option
			if (showBtnDoSupprimerDecision &&
					isModifDroitOption) {
				showBtnDoSupprimerDecision = false;
			}
		} else {
			// non : uniquement celles qui peuvent passer à l'état non traité
			// cas particulier : on ne montre pas le bouton pour les demandes
			// acceptées à l'identique
			// cela se fait par la case à cocher associée
			// autre cas : accepté mais différente, il faut passer par l'étape
			// suppression droit option
			if (etatDemande == CetFactory.CET_ETAT_DEMANDE_ACCEPTEE_IDENTIQUE ||
					etatDemande == CetFactory.CET_ETAT_DEMANDE_ACCEPTEE_DIFFERENTE ||
					!affAnnItem.cetFactory().isTransitionEtatAutorisee(CetFactory.CET_ETAT_DEMANDE_NON_TRAITEE)) {
				showBtnDoSupprimerDecision = false;
			}
		}

		return showBtnDoSupprimerDecision;
	}

	/**
	 * La modification de l'épargne n'est autorisée que si on modifie une épargne
	 * 
	 * @return
	 */
	public boolean isShowTFEpargneDecisionEnJour7h00() {
		boolean isShowTFEpargneDecisionEnJour7h00 = true;

		// vérifier qu'on est en mode edition et que la selection est celle en cours
		if (affAnnSelected == null || affAnnSelected != affAnnItem) {
			isShowTFEpargneDecisionEnJour7h00 = false;
		}

		// uniquement pour les demandes non traitées
		if (isShowTFEpargneDecisionEnJour7h00 &&
				etatDemande != CetFactory.CET_ETAT_DEMANDE_NON_TRAITEE) {
			isShowTFEpargneDecisionEnJour7h00 = false;
		}

		return isShowTFEpargneDecisionEnJour7h00;
	}

	/**
	 * Les coches d'acceptation à l'identique ne sont modifiables si - rien
	 * d'autre n'est en modification - le passage à l'état cible est autorisé
	 * 
	 * @return
	 */
	public boolean isDisabledChkIsAdminDemandeAccepteeALIdentique() {
		boolean isEnabled = true;

		// pas de modification
		if (affAnnSelected != null) {
			isEnabled = false;
		}

		if (isEnabled) {
			// vers l'état non traitée
			if (affAnnItem.cetFactory().isAdminDemandeAccepteeALIdentique()) {
				isEnabled = (affAnnItem.cetFactory().isTransitionEtatAutorisee(CetFactory.CET_ETAT_DEMANDE_NON_TRAITEE));
			} else {
				// vers l'état acceptée à l'identique
				isEnabled = (affAnnItem.cetFactory().isTransitionEtatAutorisee(CetFactory.CET_ETAT_DEMANDE_ACCEPTEE_IDENTIQUE));
			}
		}

		return !isEnabled;
	}

	/**
	 * La coche d'acceptation mais différente (libellée acceptée par l'agent)
	 * n'est disponible qui si elle est cochée
	 * 
	 * @return
	 */
	public boolean isDisabledChkIsAdminDemandeAccepteeDecisionDifferente() {
		boolean isEnabled = true;

		if (!affAnnItem.cetFactory().isAdminDemandeAccepteeDecisionDifferente()) {
			isEnabled = false;
		}

		return !isEnabled;
	}

	/**
	 * On affiche la demande d'épargne en mode non traitée
	 * 
	 * @return
	 */
	public boolean isShowDemandeEpargne() {
		boolean isShowDemandeEpargne = true;

		if (etatDemande != CetFactory.CET_ETAT_DEMANDE_NON_TRAITEE) {
			isShowDemandeEpargne = false;
		}

		return isShowDemandeEpargne;
	}

	/**
	 * La décision d'épargne s'affiche tout le temps sauf si la demande est non
	 * traitée.
	 * 
	 * @return
	 */
	public boolean isShowDecisionEpargne() {
		boolean isShowDecisionEpargne = true;

		if (etatDemande == CetFactory.CET_ETAT_DEMANDE_NON_TRAITEE) {
			isShowDecisionEpargne = false;
		}

		return isShowDecisionEpargne;
	}

	/**
	 * On affiche le droit d'option de la demande si : - non traitée - épargne
	 * saisie
	 * 
	 * @return
	 */
	public boolean isShowDemandeDroitOption() {
		boolean isShowDemandeDroitOption = false;

		if (etatDemande == CetFactory.CET_ETAT_DEMANDE_NON_TRAITEE ||
				etatDemande == CetFactory.CET_ETAT_DEMANDE_AUTRE_EPARGNE_SAISIE) {
			isShowDemandeDroitOption = true;
		}

		return isShowDemandeDroitOption;
	}

	/**
	 * La décision du droit s'affiche que pour les demandes acceptées
	 * 
	 * @return
	 */
	public boolean isShowDecisionDroitOption() {
		boolean isShowDecisionDroitOption = false;

		if (etatDemande == CetFactory.CET_ETAT_DEMANDE_ACCEPTEE_IDENTIQUE ||
				etatDemande == CetFactory.CET_ETAT_DEMANDE_ACCEPTEE_DIFFERENTE) {
			isShowDecisionDroitOption = true;
		}

		return isShowDecisionDroitOption;
	}

	/**
	 * On barre la demande visuellement lorsque l'on fait de la modification : -
	 * épargne saisie - en cours d'édition
	 * 
	 * @return
	 */
	public boolean isShowBarreDemande() {
		boolean isShowBarreDemandeEpargne = true;

		if (affAnnSelected == affAnnItem) {

		} else {
			isShowBarreDemandeEpargne = (etatDemande == CetFactory.CET_ETAT_DEMANDE_AUTRE_EPARGNE_SAISIE);
		}

		return isShowBarreDemandeEpargne;
	}

	/**
	 * bouton de saisie du droit d'option
	 */
	public boolean isShowBtnToSaisieDecisionAutreDroitOption() {
		boolean showBtnToSaisieDecisionAutreDroitOption = true;

		// vérifier que rien n'est en train d'etre modifié
		if (affAnnSelected != null) {
			showBtnToSaisieDecisionAutreDroitOption = false;
		}

		// seulement pour les demandes dont uniqment l'épargne modifiée est saisie
		if (showBtnToSaisieDecisionAutreDroitOption &&
				etatDemande != CetFactory.CET_ETAT_DEMANDE_AUTRE_EPARGNE_SAISIE) {
			showBtnToSaisieDecisionAutreDroitOption = false;
		}

		// uniquement pour les demande requierant un droit d'option
		if (showBtnToSaisieDecisionAutreDroitOption &&
				!affAnnItem.cetFactory().isOptionFaisable(getAlimentationMinutes(affAnnItem), true)) {
			showBtnToSaisieDecisionAutreDroitOption = false;
		}

		return showBtnToSaisieDecisionAutreDroitOption;
	}

	/**
	 * textfield de saisie du droit d'option
	 * 
	 * @return
	 */
	public boolean isShowTFDroitOptionDecisionEnJour7h00() {
		boolean isShowTFDroitOptionDecisionEnJour7h00 = true;

		// vérifier qu'on est en mode edition et que la selection est celle en cours
		if (affAnnSelected == null || affAnnSelected != affAnnItem) {
			isShowTFDroitOptionDecisionEnJour7h00 = false;
		}

		// uniquement en mode saisie autre épargne
		if (isShowTFDroitOptionDecisionEnJour7h00 &&
				etatDemande != CetFactory.CET_ETAT_DEMANDE_AUTRE_EPARGNE_SAISIE) {
			isShowTFDroitOptionDecisionEnJour7h00 = false;
		}

		return isShowTFDroitOptionDecisionEnJour7h00;
	}

	/**
	 * La bouton permettant la validation directe de la demande, sans saisie de
	 * droit d'option car non faisable
	 * 
	 * @return
	 */
	public boolean isShowBtnDoValiderSaisieDecisionAutreDroitOptionNonFaisableAffAnnSelected() {
		boolean isShow = true;

		// vérifier qu'on est pas en mode edition
		if (affAnnSelected != null) {
			isShow = false;
		}

		// uniquement en mode saisie autre épargne
		if (isShow &&
				etatDemande != CetFactory.CET_ETAT_DEMANDE_AUTRE_EPARGNE_SAISIE) {
			isShow = false;
		}

		// uniquement pour ceux qui ne peuvent pas faire le droit d'option
		if (isShow &&
				affAnnItem.cetFactory().isOptionFaisable(getAlimentationMinutes(affAnnItem), true)) {
			isShow = false;
		}

		return isShow;
	}

	/**
	 * La bouton permettant la suppression de la demande
	 * 
	 * @return
	 */
	public boolean isShowBtnDoSupprimerDemandeEtDecisionAffAnnItem() {
		boolean isShow = true;

		// vérifier qu'on est pas en mode edition
		if (affAnnSelected != null) {
			isShow = false;
		}

		return isShow;
	}

	// navigation

	/**
	 * Passer en mode saisie d'une autre épargne
	 */
	public WOComponent toSaisieDecisionAutreEpargneAffAnnItem() {
		affAnnSelected = affAnnItem;

		// on positionne la valeur de l'épargne identique à la demande
		epargneDecisionEnJour7h00 = affAnnSelected.toMouvementCetDemandeEpargne().mouvementJoursA7h00Arrondi();

		return null;
	}

	/**
	 * Passer en mode saisie d'un autre droit d'option
	 */
	public WOComponent toSaisieDecisionAutreDroitOptionAffAnnItem() {
		affAnnSelected = affAnnItem;

		// remise à zéro du droit d'option
		maintienDecisionEnJour7h00 = new Integer(0);
		indemnisationDecisionEnJour7h00 = new Integer(0);
		transfertRafpDecisionEnJour7h00 = new Integer(0);

		// indiquer explicitement qu'on modifie le droit d'option
		isModifDroitOption = true;

		return null;
	}

	// actions

	/**
	 * Demander le rafraichissement de la liste
	 */
	public WOComponent doRefreshMouvementDg() {
		shouldRefreshMouvementDg = true;
		return null;
	}

	/**
	 * Suppression de la decision liée à la demande
	 * 
	 * @throws Throwable
	 * 
	 */
	public WOComponent doSupprimerDecisionAffAnnItem() throws Throwable {
		affAnnItem.cetFactory().doSupprimeDecision();

		sauvegarde();

		// raz selection
		clearSelection();

		return null;
	}

	/**
	 * La classe interne
	 */
	public class SupprimerResponder implements CRIAlertResponder {

		private WOComponent parentComponent;

		public SupprimerResponder(WOComponent aParentComponent) {
			parentComponent = aParentComponent;
		}

		public WOComponent respondToButton(int buttonNo) {
			switch (buttonNo) {
			case 1:
				try {
					doSupprimerDemandeEtDecision(affAnnItem);
				} catch (Throwable e) {
					e.printStackTrace();
				}
				return parentComponent;
			case 2:
				return parentComponent;
			default:
				return neFaitRien();
			}
		}
	}

	public WOComponent doSupprimerDemandeEtDecisionAffAnnItem() {
		// page de confirmation
		SupprimerResponder responder = new SupprimerResponder(this.parent().parent());
		return CRIAlertPage.newAlertPageWithResponder(this, "Suppression de demande<br>",
				"<center>Confirmation de l'op&eacute;ration:<br><br>" +
						"Etes vous sur de vouloir supprimer la demande CET de '" + affAnnItem.individu().nomComplet() + "' ?",
				"OUI SUPPRIMER", "Annuler et conserver la demande", null, CRIAlertPage.QUESTION, responder);
	}

	/**
	 * Supprimer l'intégralité de la demande
	 * 
	 * @throws Throwable
	 * 
	 */
	private void doSupprimerDemandeEtDecision(EOAffectationAnnuelle affAnn) throws Throwable {
		affAnn.cetFactory().doSupprimeDecision();
		affAnn.cetFactory().doSupprimeDemande();

		sauvegarde();

		// raz selection
		clearSelection();
	}

	/**
	 * Enregistrer la valeur de l'épargne autre saisie
	 * 
	 * @return
	 * @throws Throwable
	 */
	public WOComponent doValiderSaisieDecisionAutreEpargneAffAnnSelected() throws Throwable {

		// on ne fait rien si c'est vide ...
		if (epargneDecisionEnJour7h00 != null) {
			if (affAnnSelected.cetFactory().doSaisieDecisionAutreEpargne(
						epargneDecisionEnJour7h00.intValue() * ConstsJour.DUREE_JOUR_7H00)) {
				sauvegarde();
				// raz selection
				clearSelection();
			} else {
				setErrorMessage(affAnnSelected.cetFactory().getErrorMessage());
			}
		}

		return null;
	}

	/**
	 * Annuler la saisie en cours
	 * 
	 * @return
	 */
	public WOComponent doAnnulerSaisieEnCours() throws Throwable {

		// raz selection
		clearSelection();

		return null;
	}

	/**
	 * Enregistrer les valeurs du droit d'option choisies pour un cas ou il faut
	 * faire un droit d'option
	 * 
	 * @return
	 * @throws Throwable
	 */
	public WOComponent doValiderSaisieDecisionAutreDroitOptionAffAnnSelected() throws Throwable {
		// vérifier que l'épargne est positive et qu'elle ne dépasse pas le max
		// à savoir la valeur du reliquat et de la valeur plafond
		if (maintienDecisionEnJour7h00 != null &&
				indemnisationDecisionEnJour7h00 != null &&
				transfertRafpDecisionEnJour7h00 != null) {
			if (affAnnSelected.cetFactory().doSaisieDecisionAutreDroitOption(
						maintienDecisionEnJour7h00.intValue() * ConstsJour.DUREE_JOUR_7H00,
						indemnisationDecisionEnJour7h00.intValue() * ConstsJour.DUREE_JOUR_7H00,
						transfertRafpDecisionEnJour7h00.intValue() * ConstsJour.DUREE_JOUR_7H00)) {
				sauvegarde();
				// raz selection
				clearSelection();
			} else {
				setErrorMessage(affAnnSelected.cetFactory().getErrorMessage());
			}
		}

		return null;
	}

	/**
	 * Enregistrer les valeurs du droit d'option choisies pour un cas ou le droit
	 * d'option n'est pas faisable
	 * 
	 * @return
	 * @throws Throwable
	 */
	public WOComponent doValiderSaisieDecisionAutreDroitOptionNonFaisableAffAnnSelected() throws Throwable {
		//
		if (affAnnItem.cetFactory().doSaisieDecisionAutreDroitOption(0, 0, 0)) {
			sauvegarde();
			// raz selection
			clearSelection();
		} else {
			setErrorMessage(affAnnItem.cetFactory().getErrorMessage());
		}

		return null;
	}

	// methodes internes

	/**
	 * Donne le total de l'alimentation prévu d'après la décision du gestionnaire
	 * : - transfert vers le régime pérenne - épargne
	 */
	private int getAlimentationMinutes(EOAffectationAnnuelle affAnn) {

		//
		int alimentationMinutes = 0;
		if (affAnn.toMouvementCetDecisionTransfertRegimePerenne() != null &&
				affAnn.toMouvementCetDecisionTransfertRegimePerenne().mouvementMinutes().intValue() > 0) {
			alimentationMinutes += affAnn.toMouvementCetDecisionTransfertRegimePerenne().mouvementMinutes().intValue();
		}
		if (affAnn.toMouvementCetDecisionEpargne() != null &&
				affAnn.toMouvementCetDecisionEpargne().mouvementMinutes().intValue() > 0) {
			alimentationMinutes += affAnn.toMouvementCetDecisionEpargne().mouvementMinutes().intValue();
		}

		return alimentationMinutes;
	}

	// export CSV

	/**
	 * Affichage des durees
	 */
	private NSNumberFormatter nf;

	private NSNumberFormatter nf() {
		if (nf == null) {
			nf = new NSNumberFormatter();
			nf.setPattern("0.00");
		}
		return nf;
	}

	/**
	 * 
	 */
	public WOResponse printCsv() {
		LRDataResponse resp = new LRDataResponse();
		StringBuffer sb = new StringBuffer();
		// configurer le DG pour que tout soit sur la même page
		int prevNumberOfObjectsPerBatch = mouvementDg.numberOfObjectsPerBatch();
		mouvementDg.setNumberOfObjectsPerBatch(0);

		// entete
		sb.append("agent").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
		sb.append("date").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
		sb.append("solde ancien init.").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
		sb.append("maintien ancien").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
		sb.append("indemn. ancien").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
		sb.append("rafp ancien").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
		sb.append("transfert ancien vers perenne").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
		sb.append("solde ancien final").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
		sb.append("reliquat init.").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
		sb.append("solde init.").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
		sb.append("epargne").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
		sb.append("maintien").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
		sb.append("indemn.").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
		sb.append("rafp").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
		sb.append("solde final").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
		sb.append("OK identique").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
		sb.append("OK different").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
		sb.append(ConstsPrint.CSV_NEW_LINE);

		//
		for (int i = 0; i < mouvementDg.displayedObjects().count(); i++) {
			EOMouvement mouvement = (EOMouvement) mouvementDg.allObjects().objectAtIndex(i);
			EOAffectationAnnuelle affAnn = mouvement.toAffectationAnnuelle();

			sb.append(affAnn.individu().nomComplet()).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
			sb.append(DateCtrlConges.dateToString(mouvement.dCreation())).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
			sb.append(nf().format(affAnn.cetFactory().soldeAncienRegimeEnJour7h00())).append(ConstsPrint.CSV_COLUMN_SEPARATOR);

			String maintienAncien = "";
			String indemnisationAncien = "";
			String transfertRafpAncien = "";
			String transfertPerenne = "";

			if (affAnn.toMouvementCetDemandeMaintienCetAncienRegime() != null) {
				maintienAncien = nf().format(affAnn.toMouvementCetDemandeMaintienCetAncienRegime().mouvementJoursA7h00());
			}
			if (affAnn.toMouvementCetDemandeIndemnisationAncienRegime() != null) {
				indemnisationAncien = nf().format(affAnn.toMouvementCetDemandeIndemnisationAncienRegime().mouvementJoursA7h00().floatValue());
			}
			if (affAnn.toMouvementCetDemandeTransfertRafpAncienRegime() != null) {
				transfertRafpAncien = nf().format(affAnn.toMouvementCetDemandeTransfertRafpAncienRegime().mouvementJoursA7h00().floatValue());
			}
			if (affAnn.toMouvementCetDemandeTransfertRegimePerenne() != null) {
				transfertPerenne = nf().format(affAnn.toMouvementCetDemandeTransfertRegimePerenne().mouvementJoursA7h00().floatValue());
			}

			sb.append(maintienAncien).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
			sb.append(indemnisationAncien).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
			sb.append(transfertRafpAncien).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
			sb.append(transfertPerenne).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
			sb.append(maintienAncien).append(ConstsPrint.CSV_COLUMN_SEPARATOR);

			String reliquat = nf().format(affAnn.reliquatInitialEnJour7h00());

			sb.append(reliquat).append(ConstsPrint.CSV_COLUMN_SEPARATOR);

			String soldeInital = nf().format(affAnn.cetFactory().soldeEnJour7h00());
			String epargne = "";
			String maintien = "";
			String indemnisation = "";
			String transfertRafp = "";

			if (affAnn.toMouvementCetDecisionEpargne() != null) {
				epargne = nf().format(affAnn.toMouvementCetDecisionEpargne().mouvementJoursA7h00());
			} else if (affAnn.toMouvementCetDemandeEpargne() != null) {
				epargne = nf().format(affAnn.toMouvementCetDemandeEpargne().mouvementJoursA7h00());
			}
			if (affAnn.toMouvementCetDecisionMaintienCet() != null) {
				maintien = nf().format(affAnn.toMouvementCetDecisionMaintienCet().mouvementJoursA7h00());
			} else if (affAnn.toMouvementCetDemandeMaintienCet() != null) {
				maintien = nf().format(affAnn.toMouvementCetDemandeMaintienCet().mouvementJoursA7h00());
			}
			if (affAnn.toMouvementCetDecisionIndemnisation() != null) {
				indemnisation = nf().format(affAnn.toMouvementCetDecisionIndemnisation().mouvementJoursA7h00());
			} else if (affAnn.toMouvementCetDemandeIndemnisation() != null) {
				indemnisation = nf().format(affAnn.toMouvementCetDemandeIndemnisation().mouvementJoursA7h00());
			}
			if (affAnn.toMouvementCetDecisionTransfertRafp() != null) {
				transfertRafp = nf().format(affAnn.toMouvementCetDecisionTransfertRafp().mouvementJoursA7h00());
			} else if (affAnn.toMouvementCetDemandeTransfertRafp() != null) {
				transfertRafp = nf().format(affAnn.toMouvementCetDemandeTransfertRafp().mouvementJoursA7h00());
			}

			sb.append(soldeInital).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
			sb.append(epargne).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
			sb.append(maintien).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
			sb.append(indemnisation).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
			sb.append(transfertRafp).append(ConstsPrint.CSV_COLUMN_SEPARATOR);

			String soldeFinal = "";
			int etat = affAnn.cetFactory().etatDemande();
			if (etat == CetFactory.CET_ETAT_DEMANDE_ACCEPTEE_DIFFERENTE ||
					etat == CetFactory.CET_ETAT_DEMANDE_ACCEPTEE_IDENTIQUE) {
				soldeFinal = nf().format(getSoldeDecisionFinalEnJour7h00());
			} else {
				soldeFinal = nf().format(getSoldeDemandeFinalEnJour7h00());
			}

			sb.append(soldeFinal).append(ConstsPrint.CSV_COLUMN_SEPARATOR);

			String strAccIdentique = "N";
			String strAccDifferente = "N";
			if (etat == CetFactory.CET_ETAT_DEMANDE_ACCEPTEE_IDENTIQUE) {
				strAccIdentique = "O";
			} else if (etat == CetFactory.CET_ETAT_DEMANDE_ACCEPTEE_DIFFERENTE) {
				strAccDifferente = "O";
			}

			sb.append(strAccIdentique).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
			sb.append(strAccDifferente);

			sb.append(ConstsPrint.CSV_NEW_LINE);

		}
		// remettre le DG comme précédemment
		mouvementDg.setNumberOfObjectsPerBatch(prevNumberOfObjectsPerBatch);
		NSData stream = new NSData(sb.toString(), ConstsPrint.CSV_ENCODING);
		resp.setContent(stream);
		resp.setContentEncoding(ConstsPrint.CSV_ENCODING);
		resp.setHeader(String.valueOf(stream.length()), "Content-Length");
		resp.setFileName(DateCtrlConges.dateToString(DateCtrlConges.now(), "%Y%m%d") + "_demandes_cet.csv");
		return resp;
	}

	// édition de la demande de reformulation de la demande

	/**
	 * Impression de la demande de reformulation de la demande
	 */
	public class PdfBoxReformulationCtrl extends CngPdfBoxCtrl {

		private EOAffectationAnnuelle affAnn;

		public PdfBoxReformulationCtrl(EOAffectationAnnuelle anAffAnn) {
			super(PrintReformulationDemandeCet.class, anAffAnn.editingContext());
			affAnn = anAffAnn;
		}

		public NSDictionary buildDico() {
			NSMutableDictionary dico = new NSMutableDictionary();

			// dates
			dico.setObjectForKey(
					DateCtrlConges.dateToString(DateCtrlConges.now()), ConstsPrint.XML_KEY_DATE_IMPRESSION);
			dico.setObjectForKey(
					DateCtrlConges.dateToString(affAnnItem.toMouvementCetDemandeEpargne().dCreation()), ConstsPrint.XML_KEY_DATE_DEMANDE);

			// URL du logo
			dico.setObjectForKey(mainLogoURL, ConstsPrint.XML_KEY_MAIN_LOGO_URL);

			// president
			dico.setObjectForKey(grhumEtab, ConstsPrint.XML_KEY_GRHUM_ETAB);
			dico.setObjectForKey(grhumPresident, ConstsPrint.XML_KEY_GRHUM_PRESIDENT);
			dico.setObjectForKey(signaturePresident, ConstsPrint.XML_KEY_SIGNATURE_PRESIDENT);

			// ville
			dico.setObjectForKey(app.appGrhumVille(), ConstsPrint.XML_KEY_VILLE);

			// nom prenom civilité qualité
			dico.setObjectForKey(affAnnItem.individu().nom(), ConstsPrint.XML_KEY_NOM_DEMANDEUR);
			dico.setObjectForKey(affAnnItem.individu().prenom(), ConstsPrint.XML_KEY_PRENOM_DEMANDEUR);
			dico.setObjectForKey(affAnnItem.individu().cCivilite(), ConstsPrint.XML_KEY_CIVILITE);
			String strQualite = ".......................";
			if (!StringCtrl.isEmpty(affAnnItem.individu().qualite())) {
				strQualite = StringCtrl.capitalizeWords(affAnnItem.individu().qualite());
			}
			dico.setObjectForKey(strQualite, ConstsPrint.XML_KEY_QUALITE);

			// données RH
			NSArray affectations = (NSArray) affAnnItem.structure().listePeresToComposante().valueForKey(EOStructure.LIBELLE_LONG_KEY);
			dico.setObjectForKey(affectations, ConstsPrint.XML_KEY_AFFECTATION_LISTE);
			if (CetFactory.isAppVerifierStatutDemandeEpargneCet()) {
				String grade = affAnnItem.individu().getLibelleGradeForIndividu(affAnnItem);
				dico.setObjectForKey(grade, ConstsPrint.XML_KEY_GRADE_DEMANDEUR);
			}

			// CET
			float epargneDemandeEnJour7h00 = (float) affAnnItem.toMouvementCetDemandeEpargne().mouvementJoursA7h00Arrondi();
			dico.setObjectForKey(
					new Integer((int) epargneDemandeEnJour7h00), ConstsPrint.XML_KEY_DEMANDE_EPARGNE_EN_JOURS_7H00);
			float epargneDecisionEnJour7h00 = (float) affAnnItem.toMouvementCetDecisionEpargne().mouvementJoursA7h00Arrondi();
			dico.setObjectForKey(
					new Integer((int) epargneDecisionEnJour7h00), ConstsPrint.XML_KEY_DECISION_EPARGNE_EN_JOURS_7H00);
			String strAnneeUnivNm1 = DateCtrlConges.anneeUnivForDate(
					affAnnItem.dateDebutAnnee().timestampByAddingGregorianUnits(0, 0, -1, 0, 0, 0));
			dico.setObjectForKey(strAnneeUnivNm1, ConstsPrint.XML_KEY_ANNEE_UNIV_N_M_1);
			int alimentationMinutes = getAlimentationMinutes(affAnnItem);
			boolean isDroitOption = affAnnItem.cetFactory().isOptionFaisable(alimentationMinutes, true);
			dico.setObjectForKey(
					isDroitOption ? "true" : "false", ConstsPrint.XML_KEY_IS_EXERCICE_DROIT_OPTION);
			int valeurEpargneSoumiseAOptionEnJourA7h00Arrondi = 0;
			if (isDroitOption) {
				valeurEpargneSoumiseAOptionEnJourA7h00Arrondi =
						affAnnItem.cetFactory().depassementSeuil20JoursPourEpargneCetEnJours7h00Arrondi(
								alimentationMinutes);
			}
			dico.setObjectForKey(
					new Integer(valeurEpargneSoumiseAOptionEnJourA7h00Arrondi), ConstsPrint.XML_KEY_DECISION_TOTAL_OPTIONS_EN_JOURS_7H00);
			if (isDroitOption) {
				String strDate = DateCtrlConges.dateToString(
						DateCtrlConges.date1erJanAnneeUniv(affAnnItem.dateDebutAnnee()).timestampByAddingGregorianUnits(0, 0, -1, 0, 0, 0),
						"%d %B %Y");
				dico.setObjectForKey(
						strDate, ConstsPrint.XML_KEY_DATE_VALEUR_CET);
				dico.setObjectForKey(
						("Droit d'option de jours inscrits au compte épargne temps au " + strDate).toUpperCase(), ConstsPrint.XML_KEY_TITRE);
				dico.setObjectForKey(
						new Integer(valeurEpargneSoumiseAOptionEnJourA7h00Arrondi + 20), ConstsPrint.XML_KEY_DECISION_TOTAL_EN_JOURS_7H00);
				float soldeCetApresDecisionEpargneEnJours7h00 = getSoldeDecisionFinalEnJour7h00();
				dico.setObjectForKey(
						new Float(soldeCetApresDecisionEpargneEnJours7h00), ConstsPrint.XML_KEY_SOLDE_CET_APRES_DECISION_EPARGNE_EN_JOURS_7H00);

			}

			return dico;
		}

		public String fileName() {
			return "Reformulation_" +
					StringCtrl.toBasicString(affAnn.individu().nomComplet()) + "_" +
					StringCtrl.replace(DateCtrlConges.anneeUnivForDate(affAnn.dateDebutAnnee()), "/", "_");
		}

	}

	/** */
	public PdfBoxReformulationCtrl ctrlReformulationAffAnnItem() {
		return new PdfBoxReformulationCtrl(affAnnItem);
	}
}