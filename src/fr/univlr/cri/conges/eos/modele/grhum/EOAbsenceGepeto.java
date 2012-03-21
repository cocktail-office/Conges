package fr.univlr.cri.conges.eos.modele.grhum;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;
import com.webobjects.foundation.NSValidation;

import fr.univlr.cri.conges.constantes.ConstsOccupation;
import fr.univlr.cri.conges.eos.modele.conges.EOTypeAbsenceGepeto;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.objects.A_Absence;
import fr.univlr.cri.conges.objects.I_Absence;
import fr.univlr.cri.conges.objects.Planning;

public class EOAbsenceGepeto
		extends _EOAbsenceGepeto
		implements I_Absence {

	public EOAbsenceGepeto() {
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

	}

	/**
	 * A appeler par les validateforsave, forinsert, forupdate.
	 * 
	 */
	private final void validateBeforeTransactionSave() throws NSValidation.ValidationException {

	}

	// methodes ajoutees

	/**
	 * Obtenir les absences issues du logiciel de GRH ayant une correspondance
	 * avec les types de congés de HAmAC
	 * 
	 * @return {@link NSArray} de {@link EOAbsenceGepeto}
	 */
	public static NSArray findAbsencesGepetoForIndividu(
				EOEditingContext ec, EOIndividu individu, NSTimestamp dateDebut, NSTimestamp dateFin) {
		EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(
					TO_INDIVIDU_KEY + " =%@ and " +
							TO_TYPE_ABSENCE_GEPETO_KEY + "." + EOTypeAbsenceGepeto.TO_TYPE_OCCUPATION_KEY + " <> nil and ((" +
							ABS_DEBUT_KEY + ">=%@ and " + ABS_DEBUT_KEY + "<=%@) or (" + ABS_FIN_KEY + ">=%@ and " + ABS_FIN_KEY + "<=%@))", new NSArray(
							new Object[] { individu, dateDebut, dateFin, dateDebut, dateFin }));
		return ec.objectsWithFetchSpecification(new EOFetchSpecification(ENTITY_NAME, qual, null));
	}

	// interface I_Absence

	private class _AbsenceGepetoAbsence
			extends A_Absence {

		private NSTimestamp _dateDebut;

		/**
		 * Dans cette table, les dates sont toujours à 00:00 Il faut regarder
		 * l'attribut absAmpm pour savoir si ca finit a midi ou a minuit
		 */
		public NSTimestamp dateDebut() {
			if (_dateDebut == null) {
				_dateDebut = absDebut();
				if (absAmpmDebut().equals("pm")) {
					_dateDebut = _dateDebut.timestampByAddingGregorianUnits(0, 0, 0, 12, 0, 0);
				}
			}
			return _dateDebut;
		}

		private NSTimestamp _dateFin;

		/**
		 * Dans cette table, les dates sont toujours à 00:00 Il faut regarder
		 * l'attribut absAmpm pour savoir si ca finit a midi ou a minuit
		 */
		public NSTimestamp dateFin() {
			if (_dateFin == null) {
				_dateFin = absFin();
				if (absAmpmFin().equals("pm")) {
					_dateFin = _dateFin.timestampByAddingGregorianUnits(0, 0, 0, 12, 0, 0);
				}
			}
			return _dateFin;
		}

		public NSTimestamp dateCreation() {
			return EOAbsenceGepeto.this.dCreation();
		}

		public NSTimestamp dateModification() {
			return EOAbsenceGepeto.this.dModification();
		}

		public NSTimestamp dateValidation() {
			return null;
		}

		public NSTimestamp dateVisa() {
			return null;
		}

		public String debit() {
			return "00h00";
		}

		public EOIndividu delegue() {
			return null;
		}

		public String dureeComptabilisee() {
			return "0.0";
		}

		public int dureeEnMinutes() {
			return 0;
		}

		public boolean isAbsenceBilan() {
			return false;
		}

		public boolean isCongeDRH() {
			return false;
		}

		public boolean isCongeDRHComposante() {
			return false;
		}

		public boolean isCongeLegal() {
			return true;
		}

		public boolean isFermeture() {
			return false;
		}

		public boolean isFermetureOriginale() {
			return false;
		}

		public boolean isOccupationJour() {
			return true;
		}

		public boolean isOccupationMinute() {
			return false;
		}

		public int leDebitCET() {
			return 0;
		}

		public int leDebitConges() {
			return 0;
		}

		public int leDebitDechargeSyndicale() {
			return 0;
		}

		public int leDebitReliquats() {
			return 0;
		}

		public NSArray lesNodesJours() {
			return new NSArray();
		}

		public String libelleStatut() {
			return ConstsOccupation.LIBELLE_VALIDEE;
		}

		public String motif() {
			return "[Congé légal issus de l'application de GRH]";
		}

		public void setDateDebut(NSTimestamp date) {
		}

		public void setDateFin(NSTimestamp date) {
		}

		public String type() {
			return EOAbsenceGepeto.this.toTypeAbsenceGepeto().toTypeOccupation().libelle();
		}

		public EOIndividu valideur() {
			return null;
		}

		public EOIndividu viseur() {
			return null;
		}

		public boolean isHoraireForce() {
			return EOAbsenceGepeto.this.toTypeAbsenceGepeto().toTypeOccupation().isHoraireForce(dateDebut(), dateFin());
		}

		public int ordrePriorite() {
			// TODO Auto-generated method stub
			return 0;
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

	private _AbsenceGepetoAbsence _absence = new _AbsenceGepetoAbsence();

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

	public NSArray lesNodesJours() {
		return _absence.lesNodesJours();
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
		return ORDRE_PRIORITE_1;
	}

	public int sousOrdrePriorite() {
		return ORDRE_PRIORITE_1;
	}
}
