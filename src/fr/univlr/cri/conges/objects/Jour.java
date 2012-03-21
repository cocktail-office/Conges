package fr.univlr.cri.conges.objects;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.webobjects.eocontrol.EOCustomObject;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.constantes.ConstsJour;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.util.wo5.DateCtrl;

/**
 * Classe de gestion d'un jour dans le planning
 * 
 * @author Emmanuel Geze
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */
public class Jour extends EOCustomObject implements ConstsJour {

	public final static String DUREE_TRAVAILLEES_EN_MINUTES_KEY = "dureeTravailleeEnMinutes";

	private int codeStatut;

	private Semaine semaine;
	private NSTimestamp date;
	private String libelle;
	private String numero;
	private int dureeTravailleeEnMinutes, dureeAMTravailleeEnMinutes, dureePMTravailleeEnMinutes;
	private String duree;
	private String dureeAM;
	private String dureePM;
	private String numeroSemaine;

	// vacances scolaires oui non
	private boolean isVacancesScolaires;

	public Jour() {
		super();
	}

	/**
	 * @param currentDayTS
	 */
	public void setDate(NSTimestamp currentDayTS) {
		GregorianCalendar currentDayGC = new GregorianCalendar();
		int numJour = 0;
		String numeroJour = "";
		String libelleJour = null;

		date = currentDayTS;
		currentDayGC.setTime(currentDayTS);
		numJour = currentDayGC.get(Calendar.DAY_OF_MONTH);
		numeroJour = String.valueOf(numJour);
		setNumero(numeroJour);
		libelleJour = (String) ConstsJour.INITIALES_LIBELLES_JOURS.objectAtIndex(
				currentDayGC.get(Calendar.DAY_OF_WEEK) - 1);
		setLibelle(libelleJour);
		if (isDimanche())
			addStatut(STATUS_CHOME);

		// mise en etat today pour le jour d'aujourd'hui
		if (DateCtrlConges.isSameDay(date(), DateCtrlConges.now()))
			addStatut(HIGHLIGHT_TODAY);

	}

	private void setLibelle(String libelleJour) {
		libelle = libelleJour;
	}

	private void setNumero(String numeroJour) {
		numero = numeroJour;
	}

	public String libelle() {
		return libelle;
	}

	public String libelleComplet() {
		return libelle() + " " + numero();
	}

	public String numero() {
		return numero;
	}

	public NSTimestamp date() {
		return date;
	}

	/**
	 * Ajouter un etat. On utiliser le & logique pour avoir N etats.
	 */
	public void addStatut(int value) {
		if ((codeStatut & value) == 0)
			codeStatut += value;
	}

	/**
	 * forcer un statut en supprimant tous les autres
	 */
	public void setStatut(int value) {
		codeStatut = value;
	}

	/**
	 * Conges toute le journee : - journee complete ou - journee AM et journee PM
	 */
	public boolean isCongeJourneeComplete() {
		return hasStatutConge() || (hasStatutCongeAM() && hasStatutCongePM());
	}

	/**
	 * Conges Legal toute le journee : - journee complete legal ou - journee legal
	 * AM et journee legal PM
	 */
	public boolean isCongeLegalJourneeComplete() {
		return hasStatutCongeLegal() || (hasStatutCongeLegalAM() && hasStatutCongeLegalPM());
	}

	/**
	 * Conges DRH toute le journee : - journee complete DRH ou - journee DRH AM et
	 * journee DRH PM
	 */
	public boolean isCongeDrhJourneeComplete() {
		return hasStatutCongeDrh() || (hasStatutCongeDrhAM() &&
				hasStatutCongeDrhPM());
	}

	/**
	 * Pour les impots, il faut donner le nombre de jours travailles. Cette
	 * methode indique si ce jour est concerne.
	 */
	public boolean isJourTravailleImpot() {
		return dureeTravailleeEnMinutes > 0 && isTravaille() && !isCongeJourneeComplete() && !isCongeLegalJourneeComplete() && !isFerme();
	}

	public boolean isFerie() {
		return hasStatutFerie();
	}

	public boolean isChome() {
		return hasStatutChome();
	}

	public boolean isFerme() {
		return hasStatutFerme();
	}

	public boolean isCongeAM() {
		return hasStatutCongeAM();
	}

	public boolean isCongePM() {
		return hasStatutCongePM();
	}

	public boolean isCongeLegalAM() {
		return hasStatutCongeLegalAM();
	}

	public boolean isCongeLegalPM() {
		return hasStatutCongeLegalPM();
	}

	public boolean isCongeDrhAM() {
		return hasStatutCongeDrhAM();
	}

	public boolean isCongeDrhPM() {
		return hasStatutCongeDrhPM();
	}

	public boolean isCongesComp() {
		return hasStatutCongesComp();
	}

	public boolean isSansStatus() {
		return hasStatutSansStatut();
	}

	public boolean isHorsAffectation() {
		return hasStatutHorsAffectation();
	}

	public boolean isTravaille() {
		return hasStatutTravaille();
	}

	public boolean isTravailleAM() {
		return hasStatutTravailleAM();
	}

	public boolean isTravaillePM() {
		return hasStatutTravaillePM();
	}

	public boolean isHoraireForce() {
		return hasStatutHoraireForce();
	}

	public boolean isHighlightOccFirstDay() {
		return hasStatutHighlightOccFirstDay();
	}

	public boolean isHighlightOccMiddleDay() {
		return hasStatutHighlightOccMiddleDay();
	}

	public boolean isHighlightOccLastDay() {
		return hasStatutHighlightOccLastDay();
	}

	public boolean isHighlightOccOnlyDay() {
		return hasStatutHighlightOccOnlyDay();
	}

	public boolean isHighlightToday() {
		return hasStatutHighlightToday();
	}

	public boolean isEnCoursDeValidation() {
		return hasStatutEnCoursDeValidation();
	}

	public boolean isEnCoursDeSuppression() {
		return hasStatutEnCoursDeSuppression();
	}

	private boolean hasStatutSansStatut() {
		return codeStatut == 0;
	}

	private boolean hasStatutTravaille() {
		return (codeStatut & STATUS_TRAVAILLE) != 0;
	}

	private boolean hasStatutTravailleAM() {
		return (codeStatut & STATUS_TRAVAILLE_AM) != 0;
	}

	private boolean hasStatutTravaillePM() {
		return (codeStatut & STATUS_TRAVAILLE_PM) != 0;
	}

	private boolean hasStatutConge() {
		return (codeStatut & STATUS_CONGE) != 0;
	}

	private boolean hasStatutCongeAM() {
		return (codeStatut & STATUS_CONGE_AM) != 0;
	}

	private boolean hasStatutCongePM() {
		return (codeStatut & STATUS_CONGE_PM) != 0;
	}

	private boolean hasStatutCongeLegal() {
		return (codeStatut & STATUS_CONGE_LEGAL) != 0;
	}

	private boolean hasStatutCongeLegalAM() {
		return (codeStatut & STATUS_CONGE_LEGAL_AM) != 0;
	}

	private boolean hasStatutCongeLegalPM() {
		return (codeStatut & STATUS_CONGE_LEGAL_PM) != 0;
	}

	private boolean hasStatutCongeDrh() {
		return (codeStatut & STATUS_CONGE_DRH) != 0;
	}

	private boolean hasStatutCongeDrhAM() {
		return (codeStatut & STATUS_CONGE_DRH_AM) != 0;
	}

	private boolean hasStatutCongeDrhPM() {
		return (codeStatut & STATUS_CONGE_DRH_PM) != 0;
	}

	private boolean hasStatutCongesComp() {
		return (codeStatut & STATUS_CONGES_COMP) != 0;
	}

	private boolean hasStatutFerme() {
		return (codeStatut & STATUS_FERMETURE) != 0;
	}

	private boolean hasStatutFerie() {
		return (codeStatut & STATUS_FERIE) != 0;
	}

	private boolean hasStatutChome() {
		return (codeStatut & STATUS_CHOME) != 0;
	}

	private boolean hasStatutHorsAffectation() {
		return (codeStatut & STATUS_HORS_AFFECTATION) != 0;
	}

	private boolean hasStatutHoraireForce() {
		return (codeStatut & STATUS_HORAIRE_FORCE) != 0;
	}

	private boolean hasStatutHighlightOccFirstDay() {
		return (codeStatut & HIGHLIGHT_OCC_FIRST_DAY) != 0;
	}

	private boolean hasStatutHighlightOccMiddleDay() {
		return (codeStatut & HIGHLIGHT_OCC_MIDDLE_DAY) != 0;
	}

	private boolean hasStatutHighlightOccLastDay() {
		return (codeStatut & HIGHLIGHT_OCC_LAST_DAY) != 0;
	}

	private boolean hasStatutHighlightOccOnlyDay() {
		return (codeStatut & HIGHLIGHT_OCC_ONLY_DAY) != 0;
	}

	private boolean hasStatutHighlightToday() {
		return (codeStatut & HIGHLIGHT_TODAY) != 0;
	}

	private boolean hasStatutEnCoursDeValidation() {
		return (codeStatut & STATUS_EN_COURS_DE_VALIDATION) != 0;
	}

	private boolean hasStatutEnCoursDeSuppression() {
		return (codeStatut & STATUS_EN_COURS_DE_SUPPRESSION) != 0;
	}

	public String dureeAffichee() {
		String dureeAffichee = "";
		String dureeStr = duree();

		if (StringCtrl.isEmpty(dureeStr) == false) {
			int duree = Integer.valueOf(dureeStr).intValue();

			if (duree > 0) {
				int heures = duree / 60;
				int minutes = duree % 60;
				String heuresAffichees = String.valueOf(heures);
				String minutesAffichees = String.valueOf(minutes);

				if (minutes < 10) {
					minutesAffichees = "0" + minutesAffichees;
				}

				dureeAffichee = heuresAffichees + "h" + minutesAffichees;
			}
		}

		return dureeAffichee;
	}

	/**
	 * @param laDuree
	 */
	public void setDuree(String laDuree) {
		duree = laDuree;
	}

	public String duree() {
		return duree;
	}

	public void setDureeAM(String laDuree) {
		dureeAM = laDuree;
	}

	public String dureeAM() {
		return dureeAM;
	}

	public void setDureePM(String laDuree) {
		dureePM = laDuree;
	}

	public String dureePM() {
		return dureePM;
	}

	/**
	 * Construction de l'id du jour d'apres son etat
	 */
	public String idCss() {
		String id = StringCtrl.replace(DateCtrlConges.dateToString(date()), "/", "_") + "-";

		if (isSansStatus())
			id += CSS_ID_JOUR_SANS_STATUT;
		else
			id += CSS_ID_JOUR_NORMAL;
		if (isFerie() || isChome())
			id += CSS_ID_JOUR_CHOME;
		if (isHorsAffectation())
			id += CSS_ID_JOUR_HORS_AFFECTATION;
		if (isFerme())
			id += CSS_ID_JOUR_FERME;
		if (isCongeJourneeComplete()) {
			if (isEnCoursDeValidation())
				id += CSS_ID_JOUR_CONGE_V;
			else if (isEnCoursDeSuppression())
				id += CSS_ID_JOUR_CONGE_S;
			else
				id += CSS_ID_JOUR_CONGE;
		}
		if (isCongeAM()) {
			if (isEnCoursDeValidation())
				id += CSS_ID_JOUR_CONGE_AM_V;
			else if (isEnCoursDeSuppression())
				id += CSS_ID_JOUR_CONGE_AM_S;
			else
				id += CSS_ID_JOUR_CONGE_AM;
		}
		if (isCongePM()) {
			if (isEnCoursDeValidation())
				id += CSS_ID_JOUR_CONGE_PM_V;
			else if (isEnCoursDeSuppression())
				id += CSS_ID_JOUR_CONGE_PM_S;
			else
				id += CSS_ID_JOUR_CONGE_PM;
		}
		if (isCongeLegalJourneeComplete())
			id += CSS_ID_JOUR_CONGE_LEGAL;
		if (isCongeLegalAM())
			id += CSS_ID_JOUR_CONGE_LEGAL_AM;
		if (isCongeLegalPM())
			id += CSS_ID_JOUR_CONGE_LEGAL_PM;
		if (isCongesComp()) {
			if (isEnCoursDeValidation())
				id += CSS_ID_JOUR_CONGE_COMP_V;
			else if (isEnCoursDeSuppression())
				id += CSS_ID_JOUR_CONGE_COMP_S;
			else
				id += CSS_ID_JOUR_CONGE_COMP;
		}

		return id;
	}

	/**
	 * Connaitre le status supplementaire pour le jour en cours. ex : le jour
	 * d'aujourd'hui, le debut de l'absence ... C'est utilise pour les mettre en
	 * surbrillance dans le planning en utilisant des div transparents.
	 * 
	 * @return null pour ne pas afficher la div transparente
	 */
	public String classeCssDivJour() {
		String classe = null;
		if (isHighlightToday()) {
			classe = CSS_CLASS_HIGHLIGHT_TODAY;
		} else if (isHighlightOccFirstDay()) {
			classe = CSS_CLASS_HIGHLIGHT_OCC_FIRST_DAY;
		} else if (isHighlightOccMiddleDay()) {
			classe = CSS_CLASS_HIGHLIGHT_OCC_MIDDLE_DAY;
		} else if (isHighlightOccLastDay()) {
			classe = CSS_CLASS_HIGHLIGHT_OCC_LAST_DAY;
		} else if (isHighlightOccOnlyDay()) {
			classe = CSS_CLASS_HIGHLIGHT_OCC_ONLY_DAY;
		}
		return classe;
	}

	public String classeCssOccupation() {
		String classe = CSS_CLASS_JOUR_NORMAL;
		if (isSansStatus()) {
			classe = CSS_CLASS_JOUR_SANS_STATUT;
		} else if (isFerie() || isChome()) {
			classe = CSS_CLASS_JOUR_CHOME;
		} else if (isHorsAffectation()) {
			classe = CSS_CLASS_JOUR_HORS_AFFECTATION;
		} else if (isCongeLegalJourneeComplete()) {
			classe = CSS_CLASS_JOUR_CONGE_LEGAL;
		} else if (isCongeLegalAM()) {
			classe = CSS_CLASS_JOUR_CONGE_LEGAL_AM;
		} else if (isCongeLegalPM()) {
			classe = CSS_CLASS_JOUR_CONGE_LEGAL_PM;
		} else if (isFerme()) {
			classe = CSS_CLASS_JOUR_FERME;
		} else if (isCongeJourneeComplete()) {
			if (isEnCoursDeValidation()) {
				classe = CSS_CLASS_JOUR_CONGE_V;
			} else if (isEnCoursDeSuppression()) {
				classe = CSS_CLASS_JOUR_CONGE_S;
			} else {
				classe = CSS_CLASS_JOUR_CONGE;
			}
		} else if (isCongeAM()) {
			if (isEnCoursDeValidation()) {
				classe = CSS_CLASS_JOUR_CONGE_AM_V;
			} else if (isEnCoursDeSuppression()) {
				classe = CSS_CLASS_JOUR_CONGE_AM_S;
			} else {
				classe = CSS_CLASS_JOUR_CONGE_AM;
			}
		} else if (isCongePM()) {
			if (isEnCoursDeValidation()) {
				classe = CSS_CLASS_JOUR_CONGE_PM_V;
			} else if (isEnCoursDeSuppression()) {
				classe = CSS_CLASS_JOUR_CONGE_PM_S;
			} else {
				classe = CSS_CLASS_JOUR_CONGE_PM;
			}
		} else if (isCongesComp()) {
			if (isEnCoursDeValidation()) {
				classe = CSS_CLASS_JOUR_CONGE_COMP_V;
			} else if (isEnCoursDeSuppression()) {
				classe = CSS_CLASS_JOUR_CONGE_COMP_S;
			} else {
				classe = CSS_CLASS_JOUR_CONGE_COMP;
			}
		}

		return classe;
	}

	public String classeCssNoOccupation() {
		String classCss = CSS_CLASS_JOUR_NORMAL;
		if (isSansStatus()) {
			classCss = CSS_CLASS_JOUR_SANS_STATUT;
		} else if (isFerie() || isChome()) {
			classCss = CSS_CLASS_JOUR_CHOME;
		} else if (isHorsAffectation()) {
			classCss = CSS_CLASS_JOUR_HORS_AFFECTATION;
		}
		return classCss;
	}

	/**
	 * @param uneSemaine
	 */
	public void setSemaine(Semaine uneSemaine) {
		semaine = uneSemaine;
	}

	public Semaine semaine() {
		return semaine;
	}

	/**
	 * @param jourTS
	 * @return
	 */
	private int dayOfWeek(NSTimestamp jourTS) {
		int day;
		GregorianCalendar jourGC = new GregorianCalendar();

		jourGC.setTime(jourTS);
		day = jourGC.get(GregorianCalendar.DAY_OF_WEEK);

		return day;
	}

	public boolean isLundi() {
		return dayOfWeek(date()) == GregorianCalendar.MONDAY ? true : false;
	}

	public boolean isMardi() {
		return dayOfWeek(date()) == GregorianCalendar.TUESDAY ? true : false;
	}

	public boolean isMercredi() {
		return dayOfWeek(date()) == GregorianCalendar.WEDNESDAY ? true : false;
	}

	public boolean isJeudi() {
		return dayOfWeek(date()) == GregorianCalendar.THURSDAY ? true : false;
	}

	public boolean isVendredi() {
		return dayOfWeek(date()) == GregorianCalendar.FRIDAY ? true : false;
	}

	public boolean isSamedi() {
		return dayOfWeek(date()) == GregorianCalendar.SATURDAY ? true : false;
	}

	public boolean isDimanche() {
		return dayOfWeek(date()) == GregorianCalendar.SUNDAY ? true : false;
	}

	/**
	 * @param numero
	 */
	public void setNumeroSemaine(String numero) {
		numeroSemaine = numero;
	}

	public String numeroSemaine() {
		return numeroSemaine;
	}

	public boolean isOuvertALaSaisie() {
		boolean isOuvertALaSaisie = false;

		if (!isHorsAffectation() && !isChome() && !isFerie()) {
			isOuvertALaSaisie = true;
		}

		return isOuvertALaSaisie;
	}

	/**
	 * @param i
	 */
	public void setDureeTravailleeEnMinutes(int dureeEnMinutes) {
		dureeTravailleeEnMinutes = dureeEnMinutes;
	}

	public int dureeTravailleeEnMinutes() {
		return dureeTravailleeEnMinutes;
	}

	public void setDureeAMTravailleeEnMinutes(int dureeEnMinutes) {
		dureeAMTravailleeEnMinutes = dureeEnMinutes;
	}

	public int dureeAMTravailleeEnMinutes() {
		return dureeAMTravailleeEnMinutes;
	}

	public void setDureePMTravailleeEnMinutes(int dureeEnMinutes) {
		dureePMTravailleeEnMinutes = dureeEnMinutes;
	}

	public int dureePMTravailleeEnMinutes() {
		return dureePMTravailleeEnMinutes;
	}

	public String classeDuree() {
		String classe = "dJour";
		if (isHoraireForce())
			classe = "dfJour";
		return classe;
	}

	public boolean isVacancesScolaires() {
		return isVacancesScolaires;
	}

	public void setIsVacancesScolaires(boolean value) {
		isVacancesScolaires = value;
	}

	public boolean equals(Jour unJour) {
		return DateCtrlConges.isSameDay(date(), unJour.date());
	}

	// durees de travail effectue

	/**
	 * On compte la duree comme effectuee si la date du jour est avant ou egale la
	 * date de reference DateCtrlConges.now()
	 */
	public int dureeTravailEffectueEnMinutes(NSTimestamp dateReference) {
		int dureeEffectuee = 0;
		if (DateCtrlConges.isBeforeEq(date(), dateReference)) {
			if (isTravaille()) {
				dureeEffectuee = dureeTravailleeEnMinutes();
			} else if (isCongeAM() && isTravaillePM()) {
				dureeEffectuee = dureePMTravailleeEnMinutes();
			} else if (isCongePM() && isTravailleAM()) {
				dureeEffectuee = dureeAMTravailleeEnMinutes();
			}
		}
		return dureeEffectuee;
	}

	public String dureeTravailEffectueEnHeures() {
		return TimeCtrl.stringForMinutes(dureeTravailEffectueEnMinutes(DateCtrl.now()));
	}

	/**
	 * pour trouver l'index du jour, on l'utilise l'API de Calendar avec dimanche
	 * = 1, lundi = 2, .... samedi = 7 pour correspondre avec notre tableau
	 * horairesJournaliers qui positionne lundi = 0, ... , dimanche = 6
	 * 
	 * @return
	 */
	public int getIndexJour() {
		GregorianCalendar gcJour = new GregorianCalendar();
		gcJour.setTime(date());
		int index = ((gcJour.get(Calendar.DAY_OF_WEEK) + 5) % 7);
		return index;
	}

	// gestion du restant à consommer

	private NSMutableDictionary dicoMinutesConsommees;
	private NSMutableDictionary dicoMinutesConsommeesAM;
	private NSMutableDictionary dicoMinutesConsommeesPM;

	private NSMutableDictionary getDicoMinutesConsommees() {
		if (dicoMinutesConsommees == null) {
			dicoMinutesConsommees = new NSMutableDictionary();
		}
		return dicoMinutesConsommees;
	}

	private NSMutableDictionary getDicoMinutesConsommeesAM() {
		if (dicoMinutesConsommeesAM == null) {
			dicoMinutesConsommeesAM = new NSMutableDictionary();
		}
		return dicoMinutesConsommeesAM;
	}

	private NSMutableDictionary getDicoMinutesConsommeesPM() {
		if (dicoMinutesConsommeesPM == null) {
			dicoMinutesConsommeesPM = new NSMutableDictionary();
		}
		return dicoMinutesConsommeesPM;
	}

	public void resetMinutesConsommes(String typePlanning) {
		getDicoMinutesConsommees().removeObjectForKey(typePlanning);
		getDicoMinutesConsommeesAM().removeObjectForKey(typePlanning);
		getDicoMinutesConsommeesPM().removeObjectForKey(typePlanning);
	}

	public void consommer(String typePlanning, int minutes) {
		Integer minutesConsommees = (Integer) getDicoMinutesConsommees().objectForKey(typePlanning);
		if (minutesConsommees != null) {
			minutesConsommees = new Integer(minutesConsommees.intValue() + minutes);
		} else {
			minutesConsommees = new Integer(minutes);
		}
		getDicoMinutesConsommees().setObjectForKey(minutesConsommees, typePlanning);
	}

	public void consommerAM(String typePlanning, int minutes) {
		Integer minutesConsommeesAM = (Integer) getDicoMinutesConsommeesAM().objectForKey(typePlanning);
		if (minutesConsommeesAM != null) {
			minutesConsommeesAM = new Integer(minutesConsommeesAM.intValue() + minutes);
		} else {
			minutesConsommeesAM = new Integer(minutes);
		}
		getDicoMinutesConsommeesAM().setObjectForKey(minutesConsommeesAM, typePlanning);
	}

	public void consommerPM(String typePlanning, int minutes) {
		Integer minutesConsommeesPM = (Integer) getDicoMinutesConsommeesPM().objectForKey(typePlanning);
		if (minutesConsommeesPM != null) {
			minutesConsommeesPM = new Integer(minutesConsommeesPM.intValue() + minutes);
		} else {
			minutesConsommeesPM = new Integer(minutes);
		}
		getDicoMinutesConsommeesPM().setObjectForKey(minutesConsommeesPM, typePlanning);
	}

	public int dureeTravailleeEnMinutesDisponible(String typePlanning) {
		int minutes = dureeTravailleeEnMinutes();

		Integer consommees = (Integer) getDicoMinutesConsommees().objectForKey(typePlanning);
		if (consommees != null) {
			minutes = minutes - consommees.intValue();
		}

		return minutes;
	}

	public int dureeAMTravailleeEnMinutesDisponible(String typePlanning) {
		int minutes = dureeAMTravailleeEnMinutes();

		Integer consommees = (Integer) getDicoMinutesConsommeesAM().objectForKey(typePlanning);
		if (consommees != null) {
			minutes = minutes - consommees.intValue();
		}

		return minutes;
	}

	public int dureePMTravailleeEnMinutesDisponible(String typePlanning) {
		int minutes = dureePMTravailleeEnMinutes();

		Integer consommees = (Integer) getDicoMinutesConsommeesPM().objectForKey(typePlanning);
		if (consommees != null) {
			minutes = minutes - consommees.intValue();
		}

		return minutes;
	}
}
