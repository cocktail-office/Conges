// EOOccupation.java
// Created on Thu Jun 17 08:48:23  2004 by Apple EOModeler Version 5.2

package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.constantes.ConstsOccupation;
import fr.univlr.cri.conges.eos.modele.conges.EORepartValidation;
import fr.univlr.cri.conges.eos.modele.grhum.EOAffectation;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;
import fr.univlr.cri.conges.objects.A_Absence;
import fr.univlr.cri.conges.objects.A_DebitableSurCET;
import fr.univlr.cri.conges.objects.I_Absence;
import fr.univlr.cri.conges.objects.I_DebitableSurCET;
import fr.univlr.cri.conges.objects.Planning;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.ArrayCtrl;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.util.wo5.DateCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.CRIWebApplication;
import fr.univlr.cri.webapp.LRRecord;
import fr.univlr.cri.webapp.LRSort;

public class EOOccupation
		extends _EOOccupation
		implements I_Absence, I_DebitableSurCET {

	// les noms des methodes pour faire des filtres
	public final static String KEY_IS_ABSENCE_CET = "isAbsenceCET";
	public final static String KEY_IS_VALIDEE = "isValidee";

	// private Number debit;
	private NSArray debitJours;

	public EOOccupation() {
		super();
	}

	public void insertInEditingContext(EOEditingContext ec) {
		if (ec != null) {
			if (ec.globalIDForObject(this) == null) {
				ec.insertObject(this);
			}
			// Insertion de l'affectation annuelle
			EOAffectationAnnuelle affectationAnnuelle = affectationAnnuelle();
			if (affectationAnnuelle != null && ec.globalIDForObject(affectationAnnuelle) == null) {
				affectationAnnuelle.insertInEditingContext(ec);
			}
		}
	}

	public boolean isRefusee() {
		return status().equals(ConstsOccupation.CODE_REFUSEE);
	}

	public boolean isSupprimee() {
		return status().equals(ConstsOccupation.CODE_SUPPRIMEE);
	}

	public boolean isValidee() {
		return status().equals(ConstsOccupation.CODE_VALIDEE);
	}

	public boolean isEnCoursDeSuppression() {
		return status().equals(ConstsOccupation.CODE_EN_COURS_DE_SUPPRESSION) || status().equals(ConstsOccupation.CODE_EN_COURS_DE_SUPPRESSION_VISEE);
	}

	public boolean isEnCoursDeValidation() {
		return status().equals(ConstsOccupation.CODE_EN_COURS_DE_VALIDATION) || status().equals(ConstsOccupation.CODE_VISEE);
	}

	private Boolean _isHeureSup, _isAbsenceCET, _isCongesDRHGlobal;

	public boolean isHeureSup() {
		if (_isHeureSup == null)
			_isHeureSup = new Boolean(typeOccupation().isHeuresSupplementaires());
		return _isHeureSup.booleanValue();
	}

	public boolean isAbsenceCET() {
		if (_isAbsenceCET == null)
			_isAbsenceCET = new Boolean(typeOccupation().isAbsenceCET());
		return _isAbsenceCET.booleanValue();
	}

	public boolean isCongeDRHGlobal() {
		if (_isCongesDRHGlobal == null)
			_isCongesDRHGlobal = new Boolean(typeOccupation().isCongeDRHGlobal());
		return _isCongesDRHGlobal.booleanValue();
	}

	public static EOOccupation newEOOccupationInContext(EOTypeOccupation unType, Planning unPlanning, EOEditingContext edc) {
		EOOccupation newEOOccupation = new EOOccupation();

		newEOOccupation.setTypeOccupationRelationship(unType);
		newEOOccupation.setAffectationAnnuelleRelationship(unPlanning.affectationAnnuelle());
		newEOOccupation.setFlagNature(unPlanning.type());
		newEOOccupation.setStatus(ConstsOccupation.CODE_EN_COURS_DE_VALIDATION);

		return newEOOccupation;
	}

	/**
	 * Indique si l'occupation est visee. N'est utilise que pour l'affichage du
	 * fond de couleur dans le composante PageDemandes
	 * 
	 * @return
	 */
	public boolean isVisee() {
		boolean isVisee = false;
		if (status() != null && (status().equals(ConstsOccupation.CODE_VISEE) || status().equals(ConstsOccupation.CODE_EN_COURS_DE_SUPPRESSION_VISEE)))
			isVisee = true;
		return isVisee;
	}

	public Number getOid() {
		return (Number) EOUtilities.primaryKeyForObject(editingContext(), this).valueForKey("oid");
	}

	// suivit des visa / validations
	private EORepartValidation _repartValidation, _repartVisa;

	public EORepartValidation repartValidation() {
		if (_repartValidation == null) {
			NSArray records = EORepartValidation.findIndividuValidantForOccupation(
					editingContext(), this, EORepartValidation.FLAG_VALIDATION_VALUE);
			if (records.count() > 0)
				_repartValidation = (EORepartValidation) records.lastObject();
		}
		return _repartValidation;
	}

	public EORepartValidation repartVisa() {
		if (_repartVisa == null) {
			NSArray records = EORepartValidation.findIndividuValidantForOccupation(
					editingContext(), this, EORepartValidation.FLAG_VISEE_VALUE);
			if (records.count() > 0)
				_repartVisa = (EORepartValidation) records.lastObject();
		}
		return _repartVisa;
	}

	/**
	 * Forcer a recharger la liste du viseur et du valideur
	 */
	public void resetCacheValidation() {
		_repartValidation = null;
		_repartVisa = null;
	}

	/**
	 * Effacer l'enregistrement <code>EORepartValidation</code> de validation de
	 * l'occupation
	 */
	public void removeRepartValidation() {
		EORepartValidation repartValidation = repartValidation();
		if (repartValidation != null) {
			repartValidation.removeObjectFromBothSidesOfRelationshipWithKey(
					repartValidation, EORepartValidation.OCCUPATION_KEY);
			editingContext().deleteObject(repartValidation);
		}
	}

	/**
	 * Effacer l'enregistrement <code>EORepartValidation</code> de visa de
	 * l'occupation
	 */
	public void removeRepartVisa() {
		EORepartValidation repartVisa = repartVisa();
		if (repartVisa != null) {
			repartVisa.removeObjectFromBothSidesOfRelationshipWithKey(
					repartVisa, EORepartValidation.OCCUPATION_KEY);
			editingContext().deleteObject(repartVisa);
		}
	}

	public NSArray debitJours() {
		return debitJours;
	}

	public void setDebitJours(NSArray value) {
		debitJours = value;
	}

	// methode de reset des caches

	public void resetCache() {
		// duree = null;
		if (_absence != null) {
			_absence.resetCache();
		}
	}

	//

	/**
	 * Indique si l'occupation peut etre supprimee par l'utilisateur sans qu'il y
	 * ai besoin de faire de demande de validation de la suppression
	 */
	public boolean isSupprimableSansValidation() {
		boolean isSupprimableSansValidation = false;

		// seules les occupations en cours de validation non vis�es
		if (status().equals(ConstsOccupation.CODE_EN_COURS_DE_VALIDATION)) {
			isSupprimableSansValidation = true;
		}

		return isSupprimableSansValidation;
	}

	//

	/**
	 * Valeur de l'occupation en heures
	 */
	public String getValeurEnHeures() {
		return TimeCtrl.stringHeureToDuree(TimeCtrl.stringForMinutes(valeurMinutes().intValue()));
	}

	// gestion de la repartition des rachats CET sur les transactions de CET
	// interface I_DebitableSurCET

	private class _OccupationDebitableSurCET
			extends A_DebitableSurCET {

		public NSTimestamp dateValeur() {
			return EOOccupation.this.dateDebut();
		}

		public Number dureeADebiter() {
			return EOOccupation.this.dureeReelle();
		}

		public EOAffectationAnnuelle toAffectationAnnuelle() {
			return EOOccupation.this.affectationAnnuelle();
		}

		public String displayCet() {
			return "Congé sur CET du " +
					DateCtrlConges.dateToString(EOOccupation.this.dateDebut()) + " au " +
					DateCtrlConges.dateToString(EOOccupation.this.dateFin()) + " (" +
					TimeCtrl.stringForMinutes(EOOccupation.this.dureeReelle().intValue()) + ")";
		}

	}

	private _OccupationDebitableSurCET _debitableSurCET = new _OccupationDebitableSurCET();

	public void clearCache() {
		_debitableSurCET.clearCache();
	}

	public void debiter(int minutesADebiter) {
		_debitableSurCET.debiter(minutesADebiter);
	}

	public int minutesRestantesADebiter() {
		return _debitableSurCET.minutesRestantesADebiter();
	}

	public NSArray tosTransaction() {
		return _debitableSurCET.tosTransaction();
	}

	public NSTimestamp dateValeur() {
		return _debitableSurCET.dateValeur();
	}

	public String displayCet() {
		return _debitableSurCET.displayCet();
	}

	public Number dureeADebiter() {
		return _debitableSurCET.dureeADebiter();
	}

	public EOAffectationAnnuelle toAffectationAnnuelle() {
		return _debitableSurCET.toAffectationAnnuelle();
	}

	// interface I_Absence

	private class _OccupationAbsence
			extends A_Absence {

		public NSTimestamp dateCreation() {
			return EOOccupation.this.dCreation();
		}

		public NSTimestamp dateModification() {
			return EOOccupation.this.dModification();
		}

		public NSTimestamp dateDebut() {
			return EOOccupation.this.dateDebut();
		}

		public NSTimestamp dateFin() {
			return EOOccupation.this.dateFin();
		}

		/**
		 * La date de validation est la date de creation de l'enregistrement de
		 * validation par un responsable
		 */
		public NSTimestamp dateValidation() {
			NSTimestamp date = null;
			if (EOOccupation.this.repartValidation() != null)
				date = EOOccupation.this.repartValidation().dCreation();
			return date;
		}

		/**
		 * La date de visa est la date de creation de l'enregistrement de visa par
		 * un viseur ou un responsable
		 */
		public NSTimestamp dateVisa() {
			NSTimestamp date = null;
			if (EOOccupation.this.repartVisa() != null)
				date = EOOccupation.this.repartVisa().dCreation();
			return date;
		}

		/**
		 * Debit : valeur de l'occupation transformée en heures
		 */
		public String debit() {
			String leDebit = "00h00";
			Number valeur = EOOccupation.this.valeurMinutes();

			if (valeur != null/* && valeur.intValue() > 0 */) {
				leDebit = TimeCtrl.stringHeureToDuree(TimeCtrl.stringForMinutes(valeur.intValue()));
			}

			return leDebit;
		}

		/**
	   * 
	   */
		public EOIndividu delegue() {
			return toIndividuDemandeur();
		}

		/**
	   * 
	   */
		public int dureeEnMinutes() {
			return valeurMinutes().intValue();
		}

		private Boolean _isAbsenceBilan, _isCongeDRH, _isCongeDRHComposante, _isCongeLegal, _isFermeture,
				_isOccupationJour, _isOccupationMinute, _isHoraireForce;

		public boolean isAbsenceBilan() {
			if (_isAbsenceBilan == null)
				_isAbsenceBilan = new Boolean(typeOccupation().isAbsenceBilan());
			return _isAbsenceBilan.booleanValue();
		}

		public boolean isCongeDRH() {
			if (_isCongeDRH == null)
				_isCongeDRH = new Boolean(typeOccupation().isCongeDRH());
			return _isCongeDRH.booleanValue();
		}

		public boolean isCongeDRHComposante() {
			if (_isCongeDRHComposante == null)
				_isCongeDRHComposante = new Boolean(typeOccupation().isCongeDRHComposante());
			return _isCongeDRHComposante.booleanValue();
		}

		public boolean isCongeLegal() {
			if (_isCongeLegal == null)
				_isCongeLegal = new Boolean(typeOccupation().isCongeLegal());
			return _isCongeLegal.booleanValue();
		}

		public boolean isFermeture() {
			if (_isFermeture == null)
				_isFermeture = new Boolean(typeOccupation().isFermeture());
			return _isFermeture.booleanValue();
		}

		public boolean isFermetureOriginale() {
			return false;
		}

		public boolean isOccupationJour() {
			if (_isOccupationJour == null)
				_isOccupationJour = new Boolean(typeOccupation().isOccupationJour());
			return _isOccupationJour.booleanValue();
		}

		public boolean isOccupationMinute() {
			if (_isOccupationMinute == null)
				_isOccupationMinute = new Boolean(typeOccupation().isOccupationMinute());
			return _isOccupationMinute.booleanValue();
		}

		private int leDebitCET;

		public int leDebitCET() {
			return leDebitCET;
		}

		public void setLeDebitCET(int value) {
			leDebitCET = value;
		}

		private int leDebitConges;

		public int leDebitConges() {
			return leDebitConges;
		}

		public void setLeDebitConges(int value) {
			leDebitConges = value;
		}

		private int leDebitDechargeSyndicale;

		public int leDebitDechargeSyndicale() {
			return leDebitDechargeSyndicale;
		}

		public void setLeDebitDechargeSyndicale(int value) {
			leDebitDechargeSyndicale = value;
		}

		private int leDebitReliquats;

		public int leDebitReliquats() {
			return leDebitReliquats;
		}

		public void setLeDebitReliquats(int value) {
			leDebitReliquats = value;
		}

		private NSArray lesNodesJours;

		public NSArray lesNodesJours() {
			return lesNodesJours;
		}

		public void setLesNodesJours(NSArray value) {
			lesNodesJours = value;
		}

		/**
		 * Le libelle de l'etat de validation de l'occupation
		 */
		public String libelleStatut() {
			String leStatus = (String) ConstsOccupation.LIBELLES_STATUTS.objectAtIndex(Integer.valueOf(status()).intValue());

			return leStatus;
		}

		public String motif() {
			return EOOccupation.this.motif();
		}

		public void setDateDebut(NSTimestamp date) {
			EOOccupation.this.setDateDebut(date);
		}

		public void setDateFin(NSTimestamp date) {
			EOOccupation.this.setDateFin(date);
		}

		public String type() {
			String leType = "???";
			if (typeOccupation() != null) {
				leType = typeOccupation().libelle();
			}
			return leType;
		}

		public EOIndividu valideur() {
			EOIndividu record = null;
			if (repartValidation() != null)
				record = repartValidation().individu();
			return record;
		}

		public EOIndividu viseur() {
			EOIndividu record = null;
			if (repartVisa() != null)
				record = repartVisa().individu();
			return record;
		}

		public boolean isHoraireForce() {
			if (_isHoraireForce == null)
				_isHoraireForce = new Boolean(typeOccupation().isHoraireForce(dateDebut(), dateFin()));
			return _isHoraireForce.booleanValue();
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
			int ordre = 0;
			if (isCongeDRH()) {
				ordre = ORDRE_PRIORITE_1;
			} else {
				ordre = ORDRE_PRIORITE_2;
			}
			return ordre;
		}

	}

	private _OccupationAbsence _absence = new _OccupationAbsence();

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

	public NSArray lesNodesJours() {
		return _absence.lesNodesJours();
	}

	public String libelleStatut() {
		return _absence.libelleStatut();
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

	public void setLeDebitConges(int intValue) {
		_absence.setLeDebitConges(intValue);
	}

	public void setLeDebitDechargeSyndicale(int intValue) {
		_absence.setLeDebitDechargeSyndicale(intValue);
	}

	public void setLeDebitReliquats(int intValue) {
		_absence.setLeDebitReliquats(intValue);
	}

	public void setLeDebitCET(int intValue) {
		_absence.setLeDebitCET(intValue);
	}

	public void setLesNodesJours(NSArray array) {
		_absence.setLesNodesJours(array);
	}

	public int ordrePriorite() {
		return _absence.ordrePriorite();
	}

	public int sousOrdrePriorite() {
		return _absence.sousOrdrePriorite();
	}

	/**
	 * Recuperer la liste des horaires d'un agent sous forme d'occupation sur une
	 * periode. On prends toutes les occupations, pas seulement celles qui sont
	 * uniquement incluses !
	 * 
	 * @param ec
	 *          : L'EOEditingContext surlequel fetcher
	 * @param noIndividu
	 *          : l'agent concerne
	 * @param dateDebut
	 *          : le debut de la periode
	 * @param dateFin
	 *          : la fin de la periode
	 * @param nature
	 *          : la nature des horaires souhaitees [facultatif]
	 * 
	 * @return un tableau de <code>LRRecord</code> representant l'entite VHoraires
	 * 
	 */
	public static NSArray findRecordsFromVHorairesInPeriod(EOEditingContext ec,
			CRIWebApplication app, Number noIndividu, NSTimestamp dateDebut, NSTimestamp dateFin, String nature) {

		// on passe par les OID_AFF_ANN, c'est plus rapide
		NSArray planningList = EOVIndividuConges.fetchVIndividuCongeses(ec,
				CRIDataBus.newCondition(
						EOVIndividuConges.NO_INDIVIDU_KEY + "=%@ and " +
								EOVIndividuConges.DTE_DEBUT_ANNEE_KEY + ">=%@",
						new NSArray(new Object[] {
								noIndividu, DateCtrlConges.dateToDebutAnneeUniv(dateDebut) })), null);

		NSArray records = new NSArray();

		if (planningList.count() > 0) {
			for (int i = 0; i < planningList.count(); i++) {
				EOVIndividuConges planning = (EOVIndividuConges) planningList.objectAtIndex(i);
				// On construit la requete
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT TO_CHAR(t0.DTE_DEBUT_REEL, 'DD/MM/YYYY HH24:MI'), TO_CHAR(t0.DTE_FIN_REEL, 'DD/MM/YYYY HH24:MI'), TO_CHAR(t0.DTE_JOUR, 'DD/MM/YYYY HH24:MI'), t0.FLG_NATURE, t0.NO_INDIVIDU, t0.OID_AFF_ANN  ");
				sql.append("FROM CONGES.V_HORAIRES t0 ");
				sql.append("WHERE ((t0.OID_AFF_ANN = ").append(planning.oidAffAnn()).append(" AND t0.FLG_NATURE = '").append(nature).append("') ");
				sql.append("AND (t0.DTE_JOUR >= to_date('").append(DateCtrl.dateToString(dateDebut)).append("', 'DD/MM/YYYY') ");
				sql.append("AND t0.DTE_JOUR < to_date('").append(DateCtrl.dateToString(TimeCtrl.dateToMinuit(dateFin).timestampByAddingGregorianUnits(0, 0, 1, 0, 0, 0))).append("', 'DD/MM/YYYY')))");

				records = records.arrayByAddingObjectsFromArray(EOUtilities.rawRowsForSQL(
						ec, app.mainModelName(), sql.toString(),
						new NSArray(new String[] { "dteDebutReel", "dteFinReel", "dteJour", "flgNature", "noIndividu", "oidAffAnn" })));
			}
		}

		return records;
	}

	/**
	 * Recuperer la liste des occupations d'un agent posees pendant une periode.
	 * On prends toutes les occupations, pas seulement celles qui sont uniquement
	 * incluses !
	 * 
	 * @param ec
	 *          : L'EOEditingContext surlequel fetcher
	 * @param noIndividu
	 *          : l'agent concerne
	 * @param dateDebut
	 *          : le debut de la periode
	 * @param dateFin
	 *          : la fin de la periode
	 * @param nature
	 *          : la nature des occupations souhaitees [facultatif]
	 * @param status
	 *          : le static des occupations (cf EOEOccupation) [facultatif]
	 * 
	 * @return un tableau de <code>LRRecord</code> representant l'entite
	 *         VOccupations
	 * 
	 */
	public static NSArray findRecordsFromVOccupationInPeriod(EOEditingContext ec,
			Number noIndividu, NSTimestamp dateDebut, NSTimestamp dateFin, String nature, String status) {
		NSArray recsOcc = new NSArray();
		NSArray recsFer = new NSArray();
		NSArray recsLeg = new NSArray();

		// on passe par les OID_AFF_ANN, c'est plus rapide
		NSArray planningList = EOVIndividuConges.fetchVIndividuCongeses(ec,
				CRIDataBus.newCondition(
						EOVIndividuConges.NO_INDIVIDU_KEY + "=%@ and " +
								EOVIndividuConges.DTE_DEBUT_ANNEE_KEY + ">=%@",
						new NSArray(new Object[] {
								noIndividu, DateCtrlConges.dateToDebutAnneeUniv(dateDebut) })), null);
		// on traite pas si pas de plannings
		if (planningList.count() > 0) {
			for (int i = 0; i < planningList.count(); i++) {
				EOVIndividuConges planning = (EOVIndividuConges) planningList.objectAtIndex(i);
				recsOcc = recsOcc.arrayByAddingObjectsFromArray(EOVOccupationsOcc.fetchVOccupationsOccs(ec,
						CRIDataBus.newCondition(EOVOccupationsOcc.OID_AFF_ANN_KEY + "=" + planning.oidAffAnn()), null));
				recsFer = recsFer.arrayByAddingObjectsFromArray(EOVOccupationsFer.fetchVOccupationsFers(ec,
						CRIDataBus.newCondition(EOVOccupationsFer.OID_AFF_ANN_KEY + "=" + planning.oidAffAnn()), null));
				recsLeg = recsLeg.arrayByAddingObjectsFromArray(EOVOccupationsLeg.fetchVOccupationsLegs(ec,
						CRIDataBus.newCondition(EOVOccupationsLeg.NO_INDIVIDU_KEY + "=" + noIndividu), null));
			}
			String strQualOcc =
					"((" + EOVOccupationsOcc.DTE_DEBUT_REEL_KEY + " >= %@ AND " + EOVOccupationsOcc.DTE_DEBUT_REEL_KEY + " <= %@) OR " +
							"(" + EOVOccupationsOcc.DTE_FIN_REEL_KEY + " >= %@ AND " + EOVOccupationsOcc.DTE_FIN_REEL_KEY + " <= %@) OR " +
							"(" + EOVOccupationsOcc.DTE_DEBUT_REEL_KEY + " <= %@ AND " + EOVOccupationsOcc.DTE_FIN_REEL_KEY + " >= %@))";
			String strQualFer =
					"((" + EOVOccupationsFer.DTE_DEBUT_REEL_KEY + " >= %@ AND " + EOVOccupationsFer.DTE_DEBUT_REEL_KEY + " <= %@) OR " +
							"(" + EOVOccupationsFer.DTE_FIN_REEL_KEY + " >= %@ AND " + EOVOccupationsFer.DTE_FIN_REEL_KEY + " <= %@) OR " +
							"(" + EOVOccupationsFer.DTE_DEBUT_REEL_KEY + " <= %@ AND " + EOVOccupationsFer.DTE_FIN_REEL_KEY + " >= %@))";
			String strQualLeg =
					"((" + EOVOccupationsLeg.DTE_DEBUT_REEL_KEY + " >= %@ AND " + EOVOccupationsLeg.DTE_DEBUT_REEL_KEY + " <= %@) OR " +
							"(" + EOVOccupationsLeg.DTE_FIN_REEL_KEY + " >= %@ AND " + EOVOccupationsLeg.DTE_FIN_REEL_KEY + " <= %@) OR " +
							"(" + EOVOccupationsLeg.DTE_DEBUT_REEL_KEY + " <= %@ AND " + EOVOccupationsLeg.DTE_FIN_REEL_KEY + " >= %@))";
			NSArray args =
					new NSArray(new Object[] {
							dateDebut, dateFin,
							dateDebut, dateFin,
							dateDebut, dateFin });

			if (!StringCtrl.isEmpty(nature)) {
				strQualOcc += " and " + EOVOccupationsOcc.NATURE_KEY + "='" + nature + "'";
				strQualFer += " and " + EOVOccupationsFer.NATURE_KEY + "='" + nature + "'";
				strQualLeg += " and " + EOVOccupationsLeg.NATURE_KEY + "='" + nature + "'";
			}
			if (!StringCtrl.isEmpty(status)) {
				strQualOcc += " and " + EOVOccupationsOcc.STATUS_KEY + "='" + status + "'";
				strQualFer += " and " + EOVOccupationsFer.STATUS_KEY + "='" + status + "'";
				strQualLeg += " and " + EOVOccupationsLeg.STATUS_KEY + "='" + status + "'";
			}

			recsOcc = EOQualifier.filteredArrayWithQualifier(recsOcc, CRIDataBus.newCondition(strQualOcc, args));
			recsFer = EOQualifier.filteredArrayWithQualifier(recsFer, CRIDataBus.newCondition(strQualFer, args));
			recsLeg = EOQualifier.filteredArrayWithQualifier(recsLeg, CRIDataBus.newCondition(strQualLeg, args));

			// dedoubloner les fermetures
			recsFer = dedoublonerParOid(recsFer);

		}

		NSArray result = recsLeg.arrayByAddingObjectsFromArray(recsFer.arrayByAddingObjectsFromArray(recsOcc));
		return result;
	}

	/**
	 * Ne conserver que les OID uniques par une liste d'enregistrement
	 * 
	 * @param arrayAvecDoublons
	 * @return
	 */
	private static NSArray dedoublonerParOid(NSArray arrayAvecDoublons) {
		NSArray arraySansDoublons = new NSArray();

		for (int i = 0; i < arrayAvecDoublons.count(); i++) {
			LRRecord record = (LRRecord) arrayAvecDoublons.objectAtIndex(i);
			if (!((NSArray) arraySansDoublons.valueForKey("oid")).containsObject(record.valueForKey("oid"))) {
				arraySansDoublons = arraySansDoublons.arrayByAddingObject(record);
			}
		}

		return arraySansDoublons;
	}

	/**
	 * permet d'oter les occupations refusees et supprimees d'une liste
	 * 
	 * @param occupations
	 * @return
	 */
	public static NSArray filtrerOccupationsVisiblesInMemory(NSArray occupations) {
		EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(STATUS_KEY + "<> %@ AND " + STATUS_KEY + " <> %@",
							new NSArray(new Object[] { ConstsOccupation.CODE_REFUSEE, ConstsOccupation.CODE_SUPPRIMEE }));
		return EOQualifier.filteredArrayWithQualifier(occupations, qual);
	}

	private final static String INDIVIDU_KEY =
			AFFECTATION_ANNUELLE_KEY + "." +
					EOAffectationAnnuelle.PERIODES_KEY + "." +
					EOPeriodeAffectationAnnuelle.AFFECTATION_KEY + "." +
					EOAffectation.INDIVIDU_KEY;

	private final static String STRUCTURE_KEY =
			AFFECTATION_ANNUELLE_KEY + "." +
					EOAffectationAnnuelle.PERIODES_KEY + "." +
					EOPeriodeAffectationAnnuelle.AFFECTATION_KEY + "." +
					EOAffectation.STRUCTURE_KEY;

	/**
	 * trouver les occupation par individu et type
	 * 
	 * @return une liste d'object <code>Occupation</code>
	 */
	public static NSArray findOccupationsForIndividuAndDebutAndFinAndType(
			EOEditingContext ec, EOIndividu individu, EOTypeOccupation typeOccupation) {

		String strQual = INDIVIDU_KEY + "=%@ and " + TYPE_OCCUPATION_KEY + "=%@";
		NSArray args = new NSArray(new LRRecord[] { individu, typeOccupation });
		EOQualifier qual = CRIDataBus.newCondition(strQual, args);

		NSArray occupations = fetchOccupations(
				ec, qual, LRSort.newSort(_EOOccupation.DATE_DEBUT_KEY));

		return EOOccupation.filtrerOccupationsVisiblesInMemory(occupations);
	}

	/**
	 * Obtenir une liste d'occupation ayant en commun tous les parametre. Cette
	 * methode est utilisee pour effectuer des operations sur des occupations
	 * "a cheval" sur plusieurs affectations annuelles, donc n fois presentes dans
	 * la base.
	 * 
	 * Cette methode elimine les doublons (cas plusieurs periodes).
	 * 
	 * @return une liste d'object <code>Occupation</code>
	 */
	public static NSArray findOccupationsInContext(EOEditingContext ec, EOIndividu individu,
			EOStructure structure, EOGenericRecord typeOccupation, String nature, NSTimestamp dateDebut,
			NSTimestamp dateFin, String status) {
		String stringQualifier = STRUCTURE_KEY + " = %@ AND " +
						INDIVIDU_KEY + " = %@ AND " + TYPE_OCCUPATION_KEY + " = %@ AND " +
						FLAG_NATURE_KEY + "=%@ AND " + _EOOccupation.DATE_DEBUT_KEY + " = %@ AND " + _EOOccupation.DATE_FIN_KEY + " = %@ AND " + STATUS_KEY + "=%@";
		NSArray args = new NSArray(new Object[] { structure, individu, typeOccupation, nature, dateDebut, dateFin, status });
		EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(stringQualifier, args);
		NSArray records = ec.objectsWithFetchSpecification(new EOFetchSpecification(ENTITY_NAME, qual, null));
		return ArrayCtrl.removeDoublons(records);
	}

	/**
	 * Le libelle d'un etat selon son code
	 * 
	 * @param etatCode
	 * @return
	 */
	public static String libelleForEtatValidation(String etatCode) {
		String libelle = null;
		if (!StringCtrl.isEmpty(etatCode)) {
			if (etatCode.equals(ConstsOccupation.CODE_VALIDEE)) {
				libelle = "Validée";
			} else if (etatCode.equals(ConstsOccupation.CODE_REFUSEE)) {
				libelle = "Refusée";
			} else if (etatCode.equals(ConstsOccupation.CODE_EN_COURS_DE_VALIDATION)) {
				libelle = "En cours de validation";
			} else if (etatCode.equals(ConstsOccupation.CODE_SUPPRIMEE)) {
				libelle = "Supprimée";
			} else if (etatCode.equals(ConstsOccupation.CODE_VISEE)) {
				libelle = "Visée";
			} else if (etatCode.equals(ConstsOccupation.CODE_EN_COURS_DE_SUPPRESSION)) {
				libelle = "En cours de suppression";
			} else if (etatCode.equals(ConstsOccupation.CODE_EN_COURS_DE_SUPPRESSION_VISEE)) {
				libelle = "En cours de suppression (visée)";
			}
		}
		return libelle;
	}

	private Number oid;

	/**
	 * En lecture seule, pour les noms de fichier
	 * 
	 * @return -1 si erreur
	 */
	public Number oid() {
		if (oid == null) {
			oid = new Integer(-1);
			try {
				Number pk = (Number) EOUtilities.primaryKeyForObject(editingContext(), this).valueForKey("oid");
				if (pk != null)
					oid = pk;
			} catch (Exception e) {

			}
		}
		return oid;
	}
}
