package fr.univlr.cri.conges.objects.occupations;

import java.util.GregorianCalendar;

import com.webobjects.eocontrol.EOCustomObject;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.databus.CngAlerteBus;
import fr.univlr.cri.conges.databus.CngOccupationBus;
import fr.univlr.cri.conges.eos.modele.conges.EOCETTransaction;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOCalculAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOOccupation;
import fr.univlr.cri.conges.eos.modele.planning.EOPeriodeFermeture;
import fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation;
import fr.univlr.cri.conges.objects.Jour;
import fr.univlr.cri.conges.objects.JourReliquatCongesNode;
import fr.univlr.cri.conges.objects.Planning;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.wo5.DateCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRSort;

/**
 * @author egeze
 */
public class Occupation extends EOCustomObject {

	public final static String LIBELLE_COURT_CONGES_ANNUEL = "C_ANN";

	// le noms des methodes qui peuvent etres appeles par NSKeyValueCoding
	public final static String LE_DEBIT_RELIQUATS_KEY = "leDebitReliquats";
	public final static String LE_DEBIT_CONGES_KEY = "leDebitConges";
	public final static String LE_DEBIT_DECHARGE_SYNDICALE_KEY = "leDebitDechargeSyndicale";
	public final static String LE_DEBIT_CET_KEY = "leDebitCET";
	public final static String LA_VALEUR_KEY = "laValeur";
	public final static String CALCULER_VALEUR_KEY = "calculerValeur";
	public final static String DUREE_REELLE_KEY = "dureeReelle";
	public final static String LES_NODES_JOURS_KEY = "lesNodesJours";

	public Planning lePlanning;
	public EOTypeOccupation leType;
	private EOOccupation occupation;
	private NSTimestamp leDebutTS;
	private NSTimestamp laFinTS;
	public int laValeur;
	private String errorMsg;
	private String leMotif;
	public EOEditingContext edc;

	// liste des jours avec les decomptes reliquats / conges
	private NSArray lesNodesJours;

	// pour les fermetures en horaire non forcé, on doit créer une Occupation type
	// congé "normal"
	// afin que le décompte se fasse sur la durée travailleé. On utilise donc ce
	// témoin à cet
	// effet pour savoir sur quoi on travaille
	private boolean isFermeture;

/**
   * Constructeur appele directement par les sous classes et par la methode :
   *  @see {@link EOPeriodeFermeture#calculerValeur()}
   *  
   * et par key value coding via :
   * @see {@link CngOccupationBus#addOccupation(EOEditingContext, Planning, EOTypeOccupation, NSTimestamp, NSTimestamp, String)
   * @see {@link CngOccupationBus#updateOccupation(EOEditingContext, Planning, EOOccupation, NSTimestamp, NSTimestamp, String)} 
   */
	public Occupation(EOTypeOccupation unType, Planning unPlanning, NSTimestamp debutTS, NSTimestamp finTS, String unMotif, EOEditingContext ec) {
		super();
		leType = unType;
		lePlanning = unPlanning;
		leDebutTS = debutTS;
		laFinTS = finTS;
		leMotif = unMotif;
		edc = ec;
		initDefaultValues();
	}

	/**
	 * Constructueur appele par key value coding via la methode :
	 * 
	 * @see {@link CngAlerteBus#accepteValid(EOEditingContext, fr.univlr.cri.conges.eos.modele.conges.EOAlerte, fr.univlr.cri.conges.databus.CngUserInfo)}
	 */
	public Occupation(EOOccupation uneOccupation, EOEditingContext ec) {
		super();
		occupation = uneOccupation;
		leType = occupation.typeOccupation();
		edc = ec;
		initDefaultValues();
	}

	/**
	 * Constructueur appele par key value coding via la methode :
	 * 
	 * @see {@link CngAlerteBus#accepteValid(EOEditingContext, fr.univlr.cri.conges.eos.modele.conges.EOAlerte, fr.univlr.cri.conges.databus.CngUserInfo)}
	 * @see {@link Planning#calculerOccupationChronologiques()}
	 */
	public Occupation(EOOccupation uneOccupation, Planning unPlanning, EOEditingContext ec) {
		super();
		occupation = uneOccupation;
		lePlanning = unPlanning;
		leType = occupation.typeOccupation();
		edc = ec;
		initDefaultValues();
	}

	/**
	 * Il faut initialiser certains attributs lors de l'instanciation de cette
	 * classe
	 */
	private void initDefaultValues() {
		laValeur = 0;
		isFermeture = false;
		setLeDebitCET(0);
		setLeDebitConges(0);
		setLeDebitDechargeSyndicale(0);
		setLeDebitReliquats(0);
		setLesNodesJours(new NSArray());
	}

	// conges legal
	public boolean isCongeLegal() {
		return leType.isCongeLegal();
	}

	// soumis a validation de la drh, puis reinjection des conges
	public boolean isCongeDRH() {
		return leType.isCongeDRH();
	}

	// occupation qui intervient sur une ou plusieurs heures seulement
	public boolean isOccupationMinute() {
		return leType.isOccupationMinute();
	}

	// a prendre en compte dans le bilan (hsup-ccomp)
	public boolean isAbsenceBilan() {
		return leType.isAbsenceBilan();
	}

	// forcer l'horaire a 35h00 pour 1 100%
	public boolean isHoraireForce() {
		return leType.isHoraireForce(dateDebut(), dateFin());
	}

	// est-ce une absence prise sur CET
	public boolean isAbsenceCET() {
		return leType.isAbsenceCET();
	}

	// est-ce une absence prise sur decharge syndicale
	public boolean isDechargeSyndicale() {
		return leType.isDechargeSyndicale();
	}

	public boolean isValide() {
		boolean isValide = true;

		if (!DateCtrl.isAfterEq(dateFin(), dateDebut())) {
			isValide = false;
			setErrorMsg("La date de d&eacute;but doit &ecirc;tre inf&eacute;rieure &agrave; la date de fin.");
		}

		if (isValide) {
			if (isOccupationMinute()) {
				// la date de debut et la date de fin doivent etre le meme jour
				if (DateCtrl.isAfterEq(dateDebut(), dateFin()) || DateCtrl.isBeforeEq(dateFin(), dateDebut())) {
					setErrorMsg("La date de d&eacute;but n'est pas la meme que la date de fin");
					isValide = false;
				}

				// l'heure de debut doit etre avant l'heure de fin
				if (isValide && TimeCtrl.getMinutesOfDay(dateDebut()) > TimeCtrl.getMinutesOfDay(dateFin())) {
					setErrorMsg("L'heure de d&eacute;but est apr&egrave;s l'heure de fin");
					isValide = false;
				}
			} else {
				NSArray lesJoursOccupes = lePlanning.lesJours(TimeCtrl.dateToMinuit(dateDebut()), TimeCtrl.dateToMinuit(dateFin()));
				Jour premierJour = (Jour) lesJoursOccupes.objectAtIndex(0);
				Jour dernierJour = (Jour) lesJoursOccupes.lastObject();
				GregorianCalendar leDebutGC = new GregorianCalendar();
				GregorianCalendar laFinGC = new GregorianCalendar();
				boolean premierJourIsPM = false, dernierJourIsAM = false;

				leDebutGC.setTime(dateDebut());
				if (leDebutGC.get(GregorianCalendar.AM_PM) == GregorianCalendar.PM) {
					premierJourIsPM = true;
				}
				laFinGC.setTime(dateFin());
				if (laFinGC.get(GregorianCalendar.AM_PM) == GregorianCalendar.AM) {
					dernierJourIsAM = true;
				}

				// Verification du statuts des differents jours sujets a l'occupation
				int index = 0;
				while (isValide && index < lesJoursOccupes.count()) {
					Jour unJour = (Jour) lesJoursOccupes.objectAtIndex(index++);
					boolean isPremierJour = unJour.equals(premierJour);
					boolean isPremierJourAndIsPM = isPremierJour && premierJourIsPM;
					boolean isDernierJour = unJour.equals(dernierJour);
					boolean isDernierJourAndIsAM = isDernierJour && dernierJourIsAM;

					// pas de blocage sur un jour ferie ou chome
					boolean isChomeOuFerie = unJour.isChome() || unJour.isFerie();
					boolean isTravaille = unJour.isTravaille();
					isValide = isChomeOuFerie || isTravaille;

					// on ne bloque pas pour des conges a suivre (fin precedent AM - debut
					// suivant PM)
					boolean isPremierJourOK = isPremierJourAndIsPM && (unJour.isTravaillePM() || unJour.isCongeAM() || unJour.isCongeLegalAM());
					boolean isDernierJourOK = isDernierJourAndIsAM && (unJour.isTravailleAM() || unJour.isCongePM() || unJour.isCongeLegalPM());
					isValide = isValide || isPremierJourOK || isDernierJourOK;

					if (!isValide) {
						setErrorMsg("La journ&eacute;e du " + DateCtrlConges.dateToString(unJour.date()) + " n'est pas disponible.");
					}

					if (isValide) {
						// // verifier qu'il n'y a pas d'absence déjà existante
						// boolean isJourNonOccupe = true;
						//
						// // contrôle sur la journée complète
						// isJourNonOccupe = !unJour.isCongeJourneeComplete() &&
						// !unJour.isCongeLegalJourneeComplete() && !unJour.isFerme();
						//
						// // contrôle particulier si le debut ou la fin du congé est une
						// demi
						// // journée
						// if (isPremierJourAndIsPM) {
						// isJourNonOccupe = !unJour.isCongePM() &&
						// !unJour.isCongeLegalPM();
						// } else if (isDernierJourAndIsAM) {
						// isJourNonOccupe = !unJour.isCongeAM() &&
						// !unJour.isCongeLegalAM();
						// }
						//
						// isValide = isJourNonOccupe;

						boolean isJourOccupe = false;

						// contrôle sur la journée complète
						if (unJour.isCongeJourneeComplete() ||
								unJour.isCongeLegalJourneeComplete() ||
								unJour.isFerme()) {
							isJourOccupe = true;
						}

						// contrôle particulier si le debut ou la fin du congé est une demi
						// journée
						if (!isJourOccupe) {
							if (isPremierJourAndIsPM) {
								if (unJour.isCongePM() || unJour.isCongeLegalPM()) {
									isJourOccupe = true;
								}
							} else if (isDernierJourAndIsAM) {
								if (unJour.isCongeAM() || unJour.isCongeLegalAM()) {
									isJourOccupe = true;
								}
							}
						}

						if (isJourOccupe) {
							isValide = false;
						}

						if (!isValide) {
							setErrorMsg("La journ&eacute;e du " + DateCtrlConges.dateToString(unJour.date()) + " est d&eacute;j&agrave; occup&eacute;e.");
						}
					}
				}
			}
		}

		return isValide;
	}

	/**
	 * repercuter les calculs dans toutes les structures de donnees mises en jeu
	 * dans le planning.
	 */
	public void confirmer() {

		// les conges legaux : on recredite leur cout
		if (isCongeLegal()) {
			recrediter();
		} else {
			// les autres conges a la demi journee
			for (int i = 0; i < lesNodesJours().count(); i++) {
				JourReliquatCongesNode unNode = (JourReliquatCongesNode) lesNodesJours().objectAtIndex(i);
				unNode.confirmer(lePlanning.type());
			}
			//
			setLeDebitDechargeSyndicale(((Number) (lesNodesJours().valueForKeyPath("@sum." + JourReliquatCongesNode.DEBIT_DECHARGE_SYNDICALE_KEY))).intValue());
			setLeDebitReliquats(((Number) (lesNodesJours().valueForKeyPath("@sum." + JourReliquatCongesNode.DEBIT_RELIQUATS_KEY))).intValue());
			setLeDebitConges(((Number) (lesNodesJours().valueForKeyPath("@sum." + JourReliquatCongesNode.DEBIT_CONGES_KEY))).intValue());
			setLeDebitCET(((Number) (lesNodesJours().valueForKeyPath("@sum." + JourReliquatCongesNode.DEBIT_CET_KEY))).intValue());
		}
	}

	/**
	 * Pour certaines occupations, il est necessaire de redonner les heures
	 * decomptees (concours edu nat sur presentation du justificatif) A developper
	 * de maniere specifique pour les occupations concernees.
	 */
	public void accepter() {
		// le conge DRH ne "coute" rien une fois que la DRH l'a accepte
		// en terme de reliquat et de droit a conge

		if (isCongeDRH()) {
			recrediter();
		}
	}

	/**
	 * Redonner la valeur des conges
	 */
	private void recrediter() {
		GregorianCalendar leDebutGC = new GregorianCalendar();
		GregorianCalendar laFinGC = new GregorianCalendar();
		leDebutGC.setTime(dateDebut());
		laFinGC.setTime(dateFin());

		for (int i = 0; i < lesNodesJours().count(); i++) {
			JourReliquatCongesNode unNode = (JourReliquatCongesNode) lesNodesJours().objectAtIndex(i);
			unNode.recrediter(lePlanning.type());
		}
		setLaValeur(0);
		// le recredit remet a zero le debit de reliquat et de conges
		setLeDebitConges(0);
		setLeDebitReliquats(0);
	}

	public void supprimer() {
		EOAffectationAnnuelle affectationAnnuelle = occupation.affectationAnnuelle();
		EOCalculAffectationAnnuelle compte = affectationAnnuelle.calculAffAnn(occupation.flagNature());
		compte.addMinutesRestantesForOccupation(this);
	}

	/**
	 * Indique si le jour est a traiter dans le calcul de la valeur.
	 * 
	 * @param jour
	 * @return
	 */
	private boolean isJourATraiter(Jour jour) {
		boolean isJourATraiter = true;

		// on ne traite pas les jours de conges legaux ou de fermeture. Attention,
		// on traite quand meme les fermetures si l'occupation est une fermeture
		if (jour.isCongeLegalJourneeComplete() || (!isFermeture && jour.isFerme())) {
			isJourATraiter = false;
		}

		return isJourATraiter;
	}

	/**
	 * Indique si le matin est a traiter dans le calcul de la valeur.
	 * 
	 * @param jour
	 * @return
	 */
	private boolean isJourAMATraiter(Jour jour) {
		boolean isJourAMATraiter = true;

		// on ne traite pas les matins de conges legaux
		if (jour.isCongeLegalAM() || jour.isCongeLegalJourneeComplete()) {
			isJourAMATraiter = false;
		}

		return isJourAMATraiter;
	}

	/**
	 * Indique si le matin est a traiter dans le calcul de la valeur.
	 * 
	 * @param jour
	 * @return
	 */
	private boolean isJourPMATraiter(Jour jour) {
		boolean isJourPMATraiter = true;

		// on ne traite pas les apres midi de de conges legaux
		if (jour.isCongeLegalPM() || jour.isCongeLegalJourneeComplete()) {
			isJourPMATraiter = false;
		}

		return isJourPMATraiter;
	}

	/**
	 * Calcul du temps a debiter En general la duree de l'occupation est egale la
	 * duree de travail prevue Retourne -1 si un des jours concernes possede deja
	 * un status != TRAVAILLE NOTE : le calcul pour les durées forcées se font
	 * dans la methode {@link EOPeriodeFermeture#calculerValeur()}
	 */
	public void calculerValeur() {

		NSArray nodesJours = new NSArray();

		if (isOccupationMinute()) {
			// OCCUPATION A LA MINUTE
			// Le debit de cette occupation est egal a la duree de travail
			// (celle de l'horaire) prevue pendant la duree de l'absence
			Jour jour = (Jour) lePlanning.lesJours(dateDebut(), dateFin()).lastObject();

			// trouver sur quelle partie de l'horaire il se trouve (matin ou apres
			// midi)
			int indexJour = jour.getIndexJour();
			int heureEmbauchePM = jour.semaine().horaire().minutesAtIndexDayAndPosInDay(indexJour, 2);
			boolean isPM = (TimeCtrl.getMinutesOfDay(dateDebut()) >= heureEmbauchePM);

			if ((isPM && isJourPMATraiter(jour) || (!isPM && isJourAMATraiter(jour)))) {
				setLaValeur(TimeCtrl.getMinutesOfDay(dateFin()) - TimeCtrl.getMinutesOfDay(dateDebut()));
				JourReliquatCongesNode leNode = new JourReliquatCongesNode(jour, lePlanning, this);
				leNode.setMinutesJour(laValeur());
				nodesJours = nodesJours.arrayByAddingObject(leNode);
			}

		} else {

			// OCCUPATIONS A LA DEMI JOURNEE : on construit le tableau
			// des durees journalieres

			// les jours concernes ne sont que ceux sur l'année universitaire
			// ATTENTION, on prend explicitement les valeurs issues de lePlanning,
			// pour ne pas prendre celle occupation, cela pose problème pour les
			// occupations à cheval sur 2 années universtaires
			NSTimestamp dateDebutJour = dateDebut();
			if (DateCtrl.isBefore(dateDebut(), lePlanning.affectationAnnuelle().dateDebutAnnee())) {
				dateDebutJour = lePlanning.affectationAnnuelle().dateDebutAnnee();
			}
			NSTimestamp dateFinJour = dateFin();
			if (DateCtrl.isAfter(dateFin(), lePlanning.affectationAnnuelle().dateFinAnnee())) {
				dateFinJour = lePlanning.affectationAnnuelle().dateFinAnnee();
			}

			NSArray lesJoursOccupes = lePlanning.lesJours(dateDebutJour, dateFinJour);
			GregorianCalendar leDebutGC = new GregorianCalendar();
			GregorianCalendar laFinGC = new GregorianCalendar();
			leDebutGC.setTime(dateDebut());
			laFinGC.setTime(dateFin());

			// remplissage des durees journalieres occupees
			for (int i = 0; i < lesJoursOccupes.count(); i++) {
				int minutesJour = 0;
				Jour jour = (Jour) lesJoursOccupes.objectAtIndex(i);

				// verif debut absence l'aprem
				if (i == 0 && leDebutGC.get(GregorianCalendar.AM_PM) == GregorianCalendar.PM) {
					if (isJourPMATraiter(jour)) {
						minutesJour = jour.dureePMTravailleeEnMinutesDisponible(lePlanning.type());
						jour.consommerPM(lePlanning.type(), minutesJour);
						// minutesJour = jour.dureePMTravailleeEnMinutes();
					}

					// verif fin absence le matin
				} else if (i == lesJoursOccupes.count() - 1 && laFinGC.get(GregorianCalendar.AM_PM) == GregorianCalendar.AM) {
					if (isJourAMATraiter(jour)) {
						minutesJour = jour.dureeAMTravailleeEnMinutesDisponible(lePlanning.type());
						jour.consommerAM(lePlanning.type(), minutesJour);
						// minutesJour = jour.dureeAMTravailleeEnMinutes();

					}

					// un jour complet : dureeTravailleeEnMinutes convient
				} else if (isJourATraiter(jour)) {
					int minutesJourAM = jour.dureeAMTravailleeEnMinutesDisponible(lePlanning.type());
					jour.consommerAM(lePlanning.type(), minutesJourAM);
					int minutesJourPM = jour.dureePMTravailleeEnMinutesDisponible(lePlanning.type());
					jour.consommerPM(lePlanning.type(), minutesJourPM);
					minutesJour = minutesJourAM + minutesJourPM;
					// minutesJour = jour.dureeTravailleeEnMinutes();

				}

				// mémoriser le "débit"
				jour.consommer(lePlanning.type(), minutesJour);

				JourReliquatCongesNode unNode = new JourReliquatCongesNode(jour, lePlanning, this);
				unNode.setMinutesJour(minutesJour);
				nodesJours = nodesJours.arrayByAddingObject(unNode);

			}

		}

		setLesNodesJours(nodesJours);
		setLaValeur(((Number) (lesNodesJours().valueForKeyPath("@sum." + JourReliquatCongesNode.MINUTES_JOUR_KEY))).intValue());
	}

	public int laValeur() {
		return laValeur;
	}

	public void setLaValeur(int value) {
		laValeur = value;
	}

	public String errorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String string) {
		errorMsg = string;
	}

	public NSArray lesNodesJours() {
		return lesNodesJours;
	}

	public void setLesNodesJours(NSArray value) {
		lesNodesJours = value;
	}

	private int leDebitDechargeSyndicale;

	public int leDebitDechargeSyndicale() {
		return leDebitDechargeSyndicale;
	}

	private void setLeDebitDechargeSyndicale(int value) {
		leDebitDechargeSyndicale = value;
	}

	private int leDebitReliquats;

	public int leDebitReliquats() {
		return leDebitReliquats;
	}

	public void setLeDebitReliquats(int value) {
		leDebitReliquats = value;
	}

	private int leDebitConges;

	public int leDebitConges() {
		return leDebitConges;
	}

	public void setLeDebitConges(int value) {
		leDebitConges = value;
	}

	private int leDebitCET;

	public int leDebitCET() {
		return leDebitCET;
	}

	public void setLeDebitCET(int value) {
		leDebitCET = value;
	}

	public EOAffectationAnnuelle affectationAnnuelle() {
		return (occupation != null ? occupation.affectationAnnuelle() : lePlanning.affectationAnnuelle());
	}

	// TODO 06/01/2005 : rajout de dateDebut pour le sort effectue dans
	// Planning.initialise()
	public NSTimestamp dateDebut() {
		return (occupation != null ? occupation.dateDebut() : leDebutTS);
	}

	public NSTimestamp dateFin() {
		return (occupation != null ? occupation.dateFin() : laFinTS);
	}

	public String leMotif() {
		return (occupation != null ? occupation.motif() : leMotif);
	}

	/**
	 * TODO doublon avec la methode @see EOOccupation#tosTransaction() La liste
	 * des transactions CET candidates pour que le debit de cette occupation y
	 * soient preleves.
	 */
	public NSArray tosTransactionCet() {
		NSArray result = new NSArray();

		if (isAbsenceCET()) {
			result = EOQualifier.filteredArrayWithQualifier(
					affectationAnnuelle().individu().toCET().cETTransactions(),
					CRIDataBus.newCondition(
							EOCETTransaction.DATE_VALEUR_KEY + "<=%@ and " +
									EOCETTransaction.MINUTES_RESTANTES_KEY + ">0",
							new NSArray(dateFin())));

			result = LRSort.sortedArray(result, EOCETTransaction.DATE_VALEUR_KEY);
		}

		return result;
	}

	/**
	 * Donne le lien vers l'enregistrement EOOccupation associe si disponible
	 * 
	 * @return
	 */
	public EOOccupation recOccupation() {
		return occupation;
	}

	//

	public final void setIsFermeture(boolean isFermeture) {
		this.isFermeture = isFermeture;
	}
}
