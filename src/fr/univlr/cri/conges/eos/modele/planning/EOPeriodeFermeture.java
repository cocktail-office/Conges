package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSTimestamp;
import com.webobjects.foundation.NSValidation;

import fr.univlr.cri.conges.constantes.ConstsOccupation;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;
import fr.univlr.cri.conges.objects.A_Absence;
import fr.univlr.cri.conges.objects.I_Absence;
import fr.univlr.cri.conges.objects.Jour;
import fr.univlr.cri.conges.objects.JourReliquatCongesNode;
import fr.univlr.cri.conges.objects.Planning;
import fr.univlr.cri.conges.objects.occupations.Occupation;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.wo5.DateCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRSort;

public class EOPeriodeFermeture
		extends _EOPeriodeFermeture
		implements I_Absence {

	public EOPeriodeFermeture() {
		super();
	}

	public void validateForInsert() throws NSValidation.ValidationException {
		this.validateObjectMetier();
		validateBeforeTransactionSave();
		super.validateForInsert();
	}

	public void validateForUpdate() throws NSValidation.ValidationException {
		this.validateObjectMetier();
		validateBeforeTransactionSave();
		super.validateForUpdate();
	}

	public void validateForDelete() throws NSValidation.ValidationException {
		super.validateForDelete();
	}

	/**
	 * Apparemment cette methode n'est pas appelee.
	 * 
	 * @see com.webobjects.eocontrol.EOValidation#validateForUpdate()
	 */
	public void validateForSave() throws NSValidation.ValidationException {
		validateObjectMetier();
		validateBeforeTransactionSave();
		super.validateForSave();

	}

	/**
	 * Peut etre appele a partir des factories.
	 * 
	 * @throws NSValidation.ValidationException
	 */
	public void validateObjectMetier() throws NSValidation.ValidationException {
		// une fermeture ne peut etre que globale, ou sur une cible
		if ((toAffectationAnnuelle() != null && toStructure() != null) ||
					(toAffectationAnnuelle() != null && toComposante() != null) ||
					(toStructure() != null && toComposante() != null)) {
			throw new NSValidation.ValidationException(
					"Une fermeture ne peut être partagé par 2 types d'objets suivants : planning, service et/ou composante");
		}
		// controler que la date de debut existe
		if (dateDebut() == null) {
			throw new NSValidation.ValidationException(
					"La date de debut est absente");
		}
		// controler que la date de fin existe
		if (dateFin() == null) {
			throw new NSValidation.ValidationException(
					"La date de fin est absente");
		}
		// controler que la date de debut est bien avant la date de fin
		if (DateCtrl.isAfter(dateDebut(), dateFin())) {
			throw new NSValidation.ValidationException(
					"La date de debut doit etre avant la date de fin.");
		}

	}

	/**
	 * A appeler par les validateforsave, forinsert, forupdate.
	 * 
	 */
	private final void validateBeforeTransactionSave() throws NSValidation.ValidationException {

	}

	// methodes rajoutees

	// private Number debit;
	// private Integer debitHoraireReel; // valeur reelle de l'absence basee sur
	// l'horaire associe
	// private Integer laValeur;

	// liste des d�comptes en minutes pour chaque jour occup� (ordre
	// chronologique)
	// private NSArray lesNodesJours;

	// l'enregistrement EOTypeOccupation associé à une période de fermeture
	private EOTypeOccupation eoTypeOccupationPeriodeFermeture;

	private EOTypeOccupation getEoTypeOccupationPeriodeFermeture() {
		if (eoTypeOccupationPeriodeFermeture == null) {
			eoTypeOccupationPeriodeFermeture = EOTypeOccupation.getTypeOccupationForLibelleCourtInContext(
						editingContext(), EOTypeOccupation.LIBELLE_COURT_FERMETURE);
		}
		return eoTypeOccupationPeriodeFermeture;
	}

	public boolean isHeureSup() {
		return false;
	}

	// on enregistre chaque debit pour chaque type de planning
	// dans des dictionnaires du type (valeur, type_de_planning)
	private NSMutableDictionary dicoDebit = new NSMutableDictionary();
	private NSMutableDictionary dicoDebitHoraireReel = new NSMutableDictionary();
	private NSMutableDictionary dicoLaValeur = new NSMutableDictionary();
	private NSMutableDictionary dicoLesNodesJours = new NSMutableDictionary();

	public void setDebit(Number value) {
		dicoDebit.setObjectForKey(value, planning().type());
	}

	public void setDebitHoraireReel(Number value) {
		dicoDebitHoraireReel.setObjectForKey(value, planning().type());
	}

	public void setLaValeur(int value) {
		dicoLaValeur.setObjectForKey(new Integer(value), planning().type());
	}

	public void setLesNodesJours(NSArray value) {
		dicoLesNodesJours.setObjectForKey(value, planning().type());
	}

	private Number debitNumber() {
		return (Number) dicoDebit.valueForKey(planning().type());
	}

	private Number debitHoraireReel() {
		return (Number) dicoDebitHoraireReel.valueForKey(planning().type());
	}

	public Integer laValeur() {
		return (Integer) dicoLaValeur.valueForKey(planning().type());
	}

	public NSArray lesNodesJours() {
		return (NSArray) dicoLesNodesJours.valueForKey(planning().type());
	}

	/**
	 * Une absence pour fermeture est comptee a 7h00 par semaine completes si
	 * l'horaire est force, sinon le decompte se fait comme un conges classique.
	 */
	public void calculerValeur() {
		if (isHoraireForce()) {
			//
			int debitHoraireReel = 0;
			// le tableau contneant l'ensemble des débits
			NSMutableArray nodesJoursMutable = new NSMutableArray();
			// les jours occupés par cette fermeture
			NSArray jours = planning().lesJours(dateDebut(), dateFin());
			for (int i = 0; i < jours.count(); i++) {
				Jour jour = (Jour) jours.objectAtIndex(i);
				// on ne traite pas les jours de conges legaux
				// TODO traiter les cas demi journée de congé ... :(
				if (jour.isCongeLegalAM() || jour.isCongeLegalPM() || jour.isCongeLegalJourneeComplete()) {
					continue;
				}

				//
				debitHoraireReel += jour.dureeTravailleeEnMinutes();
				//
				JourReliquatCongesNode node = new JourReliquatCongesNode(jour, planning());
				// on met la valeur calculée par la methode doForcePlanning()
				node.setMinutesJour(jour.dureeTravailleeEnMinutes());
				// ajout a la liste
				nodesJoursMutable.addObject(node);
			}
			//
			setDebitHoraireReel(new Integer(debitHoraireReel));
			//
			setLesNodesJours(nodesJoursMutable.immutableClone());

		} else {
			// pas d'horaire force, on la compte comme un conges normal
			EOTypeOccupation typeCongeClassique = EOTypeOccupation.getTypeOccupationForLibelleCourtInContext(
						editingContext(), Occupation.LIBELLE_COURT_CONGES_ANNUEL);
			occupationNonForce = new Occupation(
						typeCongeClassique, planning(), dateDebut(), dateFin(), type(), editingContext());
			// tagger explicitement en fermeture
			occupationNonForce.setIsFermeture(true);
			occupationNonForce.calculerValeur();
			setLesNodesJours(occupationNonForce.lesNodesJours());
		}

		setLaValeur(((Number) (lesNodesJours().valueForKeyPath("@sum." + JourReliquatCongesNode.MINUTES_JOUR_KEY))).intValue());

	}

	/**
	 * 
	 */
	public void confirmer() {
		if (isHoraireForce()) {
			EOCalculAffectationAnnuelle comptes = planning().affectationAnnuelle().calculAffAnn(planning().type());
			// calculerValeur(lePlanning);
			// TODO deplacer la modification du calcul du compte (cf conges legaux)
			if (comptes.toAffectationAnnuelle().isCalculAutomatique()) {
				comptes.addMinutesTravaillees(laValeur().intValue());
				comptes.substractMinutesTravaillees(debitHoraireReel().intValue());
			}
			for (int i = 0; i < lesNodesJours().count(); i++) {
				JourReliquatCongesNode unNode = (JourReliquatCongesNode) lesNodesJours().objectAtIndex(i);
				unNode.confirmer(planning().type());
			}
			_absence.setLeDebitReliquats(((Number) (lesNodesJours().valueForKeyPath("@sum." + JourReliquatCongesNode.DEBIT_RELIQUATS_KEY))).intValue());
			_absence.setLeDebitConges(((Number) (lesNodesJours().valueForKeyPath("@sum." + JourReliquatCongesNode.DEBIT_CONGES_KEY))).intValue());
		} else {
			// pas force, calcul comme un conge annuel classique
			for (int i = 0; i < lesNodesJours().count(); i++) {
				JourReliquatCongesNode unNode = (JourReliquatCongesNode) lesNodesJours().objectAtIndex(i);
				unNode.confirmer(planning().type());
			}
			_absence.setLeDebitReliquats(((Number) (lesNodesJours().valueForKeyPath("@sum." + JourReliquatCongesNode.DEBIT_RELIQUATS_KEY))).intValue());
			_absence.setLeDebitConges(((Number) (lesNodesJours().valueForKeyPath("@sum." + JourReliquatCongesNode.DEBIT_CONGES_KEY))).intValue());
		}

	}

	// si l'horaire n'est pas force, on calcul comme un conge classique
	private Occupation occupationNonForce;

	// TODO 04/01/2005 : centraliser cette methode (existe deja dans
	// EOAffectationAnnuelle.java)
	public int nbJoursEntre(NSTimestamp debutPeriode, NSTimestamp dateJour) {
		long indexJour = (dateJour.getTime() - debutPeriode.getTime()) / (1000 * 60 * 60 * 24);
		if (indexJour < 0) {
			indexJour = 0;
		}
		return (new Long(indexJour)).intValue();
	}

	// finders

	/**
	 * Les fermetures concernant l'ensemble de l'etablissement
	 */
	public static NSArray filterPeriodesFermeturesEtablissement(
				NSArray array, NSTimestamp debut, NSTimestamp fin) {
		return EOQualifier.filteredArrayWithQualifier(
					array,
					CRIDataBus.newCondition(IS_FERMETURE_ETABLISSEMENT_KEY + "= %@",
							new NSArray(Boolean.TRUE)));
	}

	/**
	 * Les fermetures concernant l'ensemble de la composante
	 */
	public static NSArray filterPeriodesFermeturesComposante(
				NSArray array, NSTimestamp debut, NSTimestamp fin, EOStructure composante) {
		return EOQualifier.filteredArrayWithQualifier(
					array,
					CRIDataBus.newCondition(IS_FERMETURE_COMPOSANTE_KEY + "= %@ and " + TO_COMPOSANTE_KEY + "=%@",
							new NSArray(new Object[] { Boolean.TRUE, composante })));
	}

	/**
	 * Les fermetures concernant l'ensemble de la composante
	 */
	public static NSArray filterPeriodesFermeturesService(
				NSArray array, NSTimestamp debut, NSTimestamp fin, EOStructure service) {
		return EOQualifier.filteredArrayWithQualifier(
					array,
					CRIDataBus.newCondition(IS_FERMETURE_SERVICE_KEY + "= %@ and " + TO_STRUCTURE_KEY + "=%@",
							new NSArray(new Object[] { Boolean.TRUE, service })));
	}

	/**
	 * Les fermetures concernant le planning
	 */
	public static NSArray filtereriodesFermeturesPlanning(
				NSArray array, NSTimestamp debut, NSTimestamp fin, EOAffectationAnnuelle affAnn) {
		return EOQualifier.filteredArrayWithQualifier(
					array,
					CRIDataBus.newCondition(IS_FERMETURE_PLANNING_KEY + "= %@ and " + TO_AFFECTATION_ANNUELLE_KEY + "=%@",
							new NSArray(new Object[] { Boolean.TRUE, affAnn })));
	}

	/**
	 * Trouver les fermetures pour une periode
	 * 
	 * @param ec
	 * @param debut
	 * @param fin
	 * @return
	 */
	public static NSArray findSortedPeriodeFermeturesInContext(
				EOEditingContext ec, NSTimestamp debut, NSTimestamp fin) {
		// on avance au jour suivant de la fin pour le fetch en l'excluant
		NSTimestamp dFin = fin.timestampByAddingGregorianUnits(0, 0, 1, 0, 0, 0);
		EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(
					"(" + _EOPeriodeFermeture.DATE_DEBUT_KEY + " >=%@ and " + _EOPeriodeFermeture.DATE_DEBUT_KEY + " <%@) or " +
							"(" + _EOPeriodeFermeture.DATE_FIN_KEY + " >=%@ and " + _EOPeriodeFermeture.DATE_FIN_KEY + " <%@)",
					new NSArray(new Object[] { debut, dFin, debut, dFin }));
		LRSort orderDate = LRSort.newSort(_EOPeriodeFermeture.DATE_DEBUT_KEY);
		return fetchPeriodeFermetures(ec, qual, orderDate);
	}

	public String display() {
		return DateCtrlConges.dateToString(dateDebut()) + " - " + DateCtrlConges.dateToString(dateFin());
	}

	// interface I_Absence

	public class _PeriodeFermetureAbsence
			extends A_Absence {

		/**
		 * on specifie explicitement la methode pour eviter la boucle infinie car
		 * absence a un nom de methode identique
		 */
		public NSTimestamp dateDebut() {
			return (NSTimestamp) storedValueForKey(DATE_DEBUT_KEY);
		}

		/**
		 * on specifie explicitement la methode pour eviter la boucle infinie car
		 * absence a un nom de methode identique
		 */
		public NSTimestamp dateFin() {
			return (NSTimestamp) storedValueForKey(DATE_FIN_KEY);
		}

		public NSTimestamp dateCreation() {
			return EOPeriodeFermeture.this.dCreation();
		}

		public NSTimestamp dateModification() {
			return EOPeriodeFermeture.this.dModification();
		}

		public NSTimestamp dateValidation() {
			return null;
		}

		public NSTimestamp dateVisa() {
			return null;
		}

		public String debit() {
			String leDebit = null;

			if (debitNumber() != null)
				leDebit = TimeCtrl.stringHeureToDuree(
							TimeCtrl.stringForMinutes(debitNumber().intValue()));
			else
				leDebit = TimeCtrl.stringHeureToDuree(TimeCtrl.stringForMinutes(0));

			return leDebit;
		}

		public EOIndividu delegue() {
			return null;
		}

		public int dureeEnMinutes() {
			if (EOPeriodeFermeture.this.debitNumber() != null)
				return EOPeriodeFermeture.this.debitNumber().intValue();
			else
				return 0;
		}

		public boolean isAbsenceCET() {
			return false;
		}

		public boolean isCongeDRH() {
			return false;
		}

		public boolean isCongeDRHComposante() {
			return false;
		}

		public boolean isCongeLegal() {
			return false;
		}

		public boolean isFermeture() {
			return true;
		}

		public boolean isFermetureOriginale() {
			return true;
		}

		public boolean isOccupationJour() {
			return true;
		}

		public boolean isOccupationMinute() {
			return false;
		}

		public boolean isAbsenceBilan() {
			return false;
		}

		public int leDebitCET() {
			return 0;
		}

		private int leDebitConges;

		public int leDebitConges() {
			// LRLog.log("leDebitConges="+leDebitConges+ " (" +
			// TimeCtrl.stringForMinutes(leDebitConges) + ")");
			return leDebitConges;
		}

		public void setLeDebitConges(int value) {
			leDebitConges = value;
		}

		public int leDebitDechargeSyndicale() {
			return 0;
		}

		private int leDebitReliquats;

		public int leDebitReliquats() {
			// LRLog.log("leDebitReliquats="+leDebitReliquats+ " (" +
			// TimeCtrl.stringForMinutes(leDebitReliquats) + ")");
			return leDebitReliquats;
		}

		public void setLeDebitReliquats(int value) {
			leDebitReliquats = value;
		}

		public NSArray lesNodesJours() {
			return EOPeriodeFermeture.this.lesNodesJours();
		}

		public String libelleStatut() {
			String leStatus = ConstsOccupation.LIBELLE_VALIDEE;

			return leStatus;
		}

		public String motif() {
			String motif = "";
			if (isFermetureEtablissement()) {
				return "Fermeture de l'établissement";
			} else if (isFermetureComposante()) {
				return "Fermeture de la composante";
			} else if (isFermetureService()) {
				return "Fermeture du service";
			} else if (isFermeturePlanning()) {
				return "Fermeture spécifique au planning";
			}
			return motif;
		}

		/**
		 * on specifie explicitement la methode pour eviter la boucle infinie car
		 * absence a un nom de methode identique
		 */
		public void setDateDebut(NSTimestamp date) {
			takeStoredValueForKey(date, DATE_DEBUT_KEY);
		}

		/**
		 * on specifie explicitement la methode pour eviter la boucle infinie car
		 * absence a un nom de methode identique
		 */
		public void setDateFin(NSTimestamp date) {
			takeStoredValueForKey(date, DATE_FIN_KEY);
		}

		public String type() {
			String leType = "Fermeture annuelle";

			return leType;
		}

		public EOIndividu valideur() {
			return null;
		}

		public EOIndividu viseur() {
			return null;
		}

		public boolean isHoraireForce() {
			return getEoTypeOccupationPeriodeFermeture().isHoraireForce(dateDebut(), dateFin());
		}

		public int ordrePriorite() {
			return ORDRE_PRIORITE_2;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see fr.univlr.cri.conges.objects.I_Absence#sousOrdrePriorite()
		 */
		public int sousOrdrePriorite() {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	private _PeriodeFermetureAbsence _absence = new _PeriodeFermetureAbsence();

	public NSTimestamp dateDebut() {
		return _absence.dateDebut();
	}

	public NSTimestamp dateFin() {
		return _absence.dateFin();
	}

	public NSTimestamp dateCreation() {
		return _absence.dateCreation();
	}

	public NSTimestamp dateModification() {
		return _absence.dateModification();
	}

	public NSTimestamp dateValidation() {
		return _absence.dateValidation();
	}

	public NSTimestamp dateVisa() {
		return _absence.dateVisa();
	}

	public String debit() {
		return _absence.debit();
	}

	public EOIndividu delegue() {
		return _absence.delegue();
	}

	public String dureeComptabilisee() {
		return _absence.dureeComptabilisee();
	}

	public int dureeEnMinutes() {
		return _absence.dureeEnMinutes();
	}

	public boolean isAbsenceBilan() {
		return _absence.isAbsenceBilan();
	}

	public boolean isCongeDRH() {
		return _absence.isCongeDRH();
	}

	public boolean isCongeDRHComposante() {
		return _absence.isCongeDRHComposante();
	}

	public boolean isCongeLegal() {
		return _absence.isCongeLegal();
	}

	public boolean isFermeture() {
		return _absence.isFermeture();
	}

	public boolean isFermetureOriginale() {
		return _absence.isFermetureOriginale();
	}

	public boolean isOccupationJour() {
		return _absence.isOccupationJour();
	}

	public boolean isOccupationMinute() {
		return _absence.isOccupationMinute();
	}

	public int leDebitCET() {
		return _absence.leDebitCET();
	}

	public int leDebitConges() {
		return _absence.leDebitConges();
	}

	public int leDebitDechargeSyndicale() {
		return _absence.leDebitDechargeSyndicale();
	}

	public int leDebitReliquats() {
		return _absence.leDebitReliquats();
	}

	public String libelleStatut() {
		return _absence.libelleStatut();
	}

	public String motif() {
		return _absence.motif();
	}

	public void setDateDebut(NSTimestamp date) {
		_absence.setDateDebut(date);
	}

	public void setDateFin(NSTimestamp date) {
		_absence.setDateFin(date);
	}

	public String type() {
		return _absence.type();
	}

	public EOIndividu valideur() {
		return _absence.valideur();
	}

	public EOIndividu viseur() {
		return _absence.viseur();
	}

	public String duree() {
		return _absence.duree();
	}

	public boolean isHoraireForce() {
		return _absence.isHoraireForce();
	}

	public void doForceJour() {
		_absence.doForceJour();
	}

	public void setPlanning(Planning p) {
		_absence.setPlanning(p);
	}

	public EOAffectationAnnuelle affectationAnnuelle() {
		return _absence.affectationAnnuelle();
	}

	public int ordrePriorite() {
		return _absence.ordrePriorite();
	}

	public int sousOrdrePriorite() {
		return _absence.sousOrdrePriorite();
	}

	private Planning planning() {
		return _absence.planning();
	}

	// cible de la fermeture

	public final static String IS_FERMETURE_ETABLISSEMENT_KEY = "isFermetureEtablissement";
	public final static String IS_FERMETURE_COMPOSANTE_KEY = "isFermetureComposante";
	public final static String IS_FERMETURE_SERVICE_KEY = "isFermetureService";
	public final static String IS_FERMETURE_PLANNING_KEY = "isFermeturePlanning";

	public boolean isFermetureEtablissement() {
		return toComposante() == null && toStructure() == null && toAffectationAnnuelle() == null;
	}

	public boolean isFermetureComposante() {
		return toComposante() != null && toStructure() == null && toAffectationAnnuelle() == null;
	}

	public boolean isFermetureService() {
		return toComposante() == null && toStructure() != null && toAffectationAnnuelle() == null;
	}

	public boolean isFermeturePlanning() {
		return toComposante() == null && toStructure() == null && toAffectationAnnuelle() != null;
	}

	/**
	 * creation d'une fermeture
	 * 
	 * @param editingContext
	 * @param dateDebut
	 * @param dateFin
	 * @return
	 */
	public static EOPeriodeFermeture createPeriodeFermetureDateDebutDateFin(
				EOEditingContext editingContext, NSTimestamp dateDebut, NSTimestamp dateFin) {
		EOPeriodeFermeture eo = createPeriodeFermeture(
					editingContext, DateCtrl.now(), DateCtrl.now());
		eo.setDateDebut(dateDebut);
		// mise de la fin a PM
		eo.setDateFin(TimeCtrl.dateToMinuit(dateFin).timestampByAddingGregorianUnits(0, 0, 0, 12, 0, 0));
		return eo;
	}
}
