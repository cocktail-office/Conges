// EOPeriodeAffectationAnnuelle.java
// Created on Thu Jun 17 08:48:26  2004 by Apple EOModeler Version 5.2

package fr.univlr.cri.conges.eos.modele.planning;

import java.util.Enumeration;
import java.util.GregorianCalendar;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.grhum.EOAffectation;
import fr.univlr.cri.conges.objects.HoraireHebdomadaire;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRSort;

public class EOPeriodeAffectationAnnuelle extends _EOPeriodeAffectationAnnuelle {

	public EOPeriodeAffectationAnnuelle() {
		super();
	}

	public Number quotite() {
		return affectation().numQuotation();
	}

	private final static int NB_JOURS_PAR_MOIS_COMPLET 	= 30;
	private final static int NB_JOURS_PAR_AN_COMPLET 		= 360;
	private final static long NB_MS_PAR_JOUR						= (1000 * 60 * 60 * 24);

	/**
	 * Determiner la valeur des minutes dues sur la période
	 * 
	 * @param shouldCalculAuto
	 *          : faut-il faire un calcul avec les jour de fractionnement
	 * @param minutesDuesSiPasCalculAuto
	 *          : la valeur de references (pour 1 100% sur toute l'année) utilisé
	 *          uniquement si shouldCalculAuto est a <em>false</em>
	 * @return
	 */
	protected int minutesDues100Pourcent(boolean shouldCalculAuto, int minutesDuesSiPasCalculAuto) {
		// Calcul horaire annuel
		int minutesDues100Pourcent = 0;
		if (shouldCalculAuto) {
			minutesDues100Pourcent = (1600 * 60) - (14 * 60); // 1600h - 14h de jours de
																							// fractionnement
			long delta = dateFin().getTime() - dateDebut().getTime();
			delta /= 1000; // Transfo en secondes
			delta /= 60; // Transfo en minutes
			delta /= 60; // Transfo en heures
			delta /= 24; // Transfo en jours
			if (delta < 360) {
				minutesDues100Pourcent = new Long((minutesDues100Pourcent * delta) / 360).intValue();
				int modulo60 = minutesDues100Pourcent % 60;
				if (modulo60 < 30) {
					minutesDues100Pourcent -= modulo60;
				} else {
					minutesDues100Pourcent += modulo60;
				}
			}
		} else {
			minutesDues100Pourcent = minutesDuesSiPasCalculAuto;
		}
		return minutesDues100Pourcent;
	}

	
	/**
	 * Calcule la valeur pondérée par la quotité lié à la période
	 * @return
	 */
	public float valeurPonderee(int minutes, NSTimestamp dateDebut, NSTimestamp dateFin) {
		
		int nombreJoursPeriode = 0;
		NSTimestamp dateDebutMoisEnCours = DateCtrlConges.dateToDebutMois(dateDebut);
		NSTimestamp dateFinMoisEnCours = DateCtrlConges.dateToFinMois(dateDebut);
		
		while (DateCtrlConges.isBeforeEq(dateDebutMoisEnCours, dateFin)) {

			boolean isIncludingDebutMois = true;
			boolean isIncludingFinMois = true;
			
			// recadrage du debut et de la fin sur la periode
			if (DateCtrlConges.isBefore(dateDebutMoisEnCours, dateDebut)) {
				dateDebutMoisEnCours = dateDebut();
				isIncludingDebutMois = false;
			}
			if (DateCtrlConges.isAfter(dateFinMoisEnCours, dateFin)) {
				dateFinMoisEnCours = dateFin();
				isIncludingFinMois = false;
			}

			// si le mois est complet, on compte 30 jours
			if (isIncludingDebutMois && isIncludingFinMois) {
				nombreJoursPeriode += NB_JOURS_PAR_MOIS_COMPLET; 
			} else {
				int totalJoursMois = (int)((dateFinMoisEnCours.getTime() - dateDebutMoisEnCours.getTime())	/ NB_MS_PAR_JOUR + 1);
				// on va regarder si avec les periodes voisines on arrive pas a faire un mois complet
				if (!isIncludingDebutMois && isMoisCompletAvecPeriodePrecedenteMemeQuotite()) {
					// periode precedente ?
					// on nivelle comme s'il s'agissait d'un mois a 30 jours
					GregorianCalendar gcMois = new GregorianCalendar();
					gcMois.setTime(dateFinMoisEnCours);
					int joursDuMois = gcMois.get(GregorianCalendar.DAY_OF_MONTH);
					nombreJoursPeriode += totalJoursMois-(joursDuMois-NB_JOURS_PAR_MOIS_COMPLET);
	
				} else {
					// periode suivante ?
					// ajout de la valeur du 1 a J
					nombreJoursPeriode += totalJoursMois;
				}
			}
	
			// avance au mois suivant
			dateDebutMoisEnCours = DateCtrlConges.dateToDebutMois(dateDebutMoisEnCours).timestampByAddingGregorianUnits(0, 1, 0, 0, 0,		0);
			dateFinMoisEnCours = dateDebutMoisEnCours.timestampByAddingGregorianUnits(0, 1, -1, 0, 0, 0);
		}

		// prorata sur une annee de 360 jours
		float minutesDues = (new Float((float)(nombreJoursPeriode * minutes) / NB_JOURS_PAR_AN_COMPLET)).floatValue();
		// prorata sur la quotite d'affectation
		minutesDues = (quotite().intValue() * minutesDues) / 100;
		
		
		return minutesDues;
	}
	
	/**
	 * Calcule la valeur pondérée par la quotité lié à la période
	 * @return
	 */
	public float valeurPonderee(int minutes) {
		float valeurPonderee = valeurPonderee(minutes, dateDebut(), dateFin());
		return valeurPonderee;
	}
	
	/**
	 * Indique si la periode actuelle ainsi que la periode precedente forme un mois
	 * complet sur leur mois charniere
	 * @param prevPeriode
	 * @return
	 */
	private boolean isMoisCompletAvecPeriodePrecedenteMemeQuotite() {
		boolean result = false;
		EOPeriodeAffectationAnnuelle prevPeriode = periodePrecedente();
		if (prevPeriode != null && prevPeriode.quotite().intValue() == quotite().intValue()) {
			if (DateCtrlConges.isSameDay(prevPeriode.dateFin().timestampByAddingGregorianUnits(0,0,1,0,0,0), dateDebut())) {
				NSTimestamp dateDebutMois 	= DateCtrlConges.dateToDebutMois(dateDebut());
				result = DateCtrlConges.isBeforeEq(prevPeriode.dateDebut(), dateDebutMois);
			}
		}
		return result;
	}
	
	/**
	 * La periode precedente chronologiquement
	 * @return
	 */
	private EOPeriodeAffectationAnnuelle periodePrecedente() {
		NSArray sortedPeriodes = LRSort.sortedArray(affectationAnnuelle().periodes(), EOPeriodeAffectationAnnuelle.DATE_DEBUT_KEY);
		int indexCurrentPeriode = sortedPeriodes.indexOfIdenticalObject(this);
		if (indexCurrentPeriode > 0) {
			return (EOPeriodeAffectationAnnuelle) sortedPeriodes.objectAtIndex(indexCurrentPeriode-1);
		}
		return null;
	}
	

	public void insertInEditingContext(EOEditingContext ec) {
		if (ec != null) {
			if (ec.globalIDForObject(this) == null) {
				ec.insertObject(this);
			}
			// Insertion des planning hebdomadaires
			NSArray lesPlanningsHebdo = planningHebdomadaires();
			Enumeration enumLesPlanningsHebdo = lesPlanningsHebdo.objectEnumerator();
			EOPlanningHebdomadaire unPlanningHebdo = null;

			while (enumLesPlanningsHebdo.hasMoreElements()) {
				unPlanningHebdo = (EOPlanningHebdomadaire) enumLesPlanningsHebdo.nextElement();
				unPlanningHebdo.insertInEditingContext(ec);
			}

		}
	}

	/**
	 * TODO faire marcher a la minute retourne les horaires sous la forme
	 * d'occupations ex : 8h00-12h00 et 14h00-17h00
	 * 
	 * @return
	 */
	public NSArray occupationsReellesHorairesPourPeriode(NSTimestamp debut,
			NSTimestamp fin, boolean voirPauses) {
		NSArray occupationsHorairesPourPeriode = new NSArray();

		EOQualifier qual = CRIDataBus.newCondition(
				EOPlanningHebdomadaire.DATE_DEBUT_SEMAINE_KEY +">=%@ and " +
				EOPlanningHebdomadaire.DATE_DEBUT_SEMAINE_KEY +"<=%@ and " +
				EOPlanningHebdomadaire.FLAG_NATURE_KEY +"='R'",
				new NSArray(new Object[] { debut, fin }));
		
		NSArray semainesFiltrees = EOQualifier.filteredArrayWithQualifier(planningHebdomadaires(), qual);

		for (int i = 0; i < semainesFiltrees.count(); i++) {
			EOPlanningHebdomadaire uneSemaine = (EOPlanningHebdomadaire) semainesFiltrees.objectAtIndex(i);
			HoraireHebdomadaire horaireHebdo = new HoraireHebdomadaire();
			horaireHebdo.setHoraire(uneSemaine.horaire());
			occupationsHorairesPourPeriode = occupationsHorairesPourPeriode.arrayByAddingObjectsFromArray(
					horaireHebdo.occupationsHoraireHebdomadaire(
							uneSemaine.dateDebutSemaine(),voirPauses));
		}

		return occupationsHorairesPourPeriode;
	}

	/**
	 * trouve la periode associée a une affectation gepeto pour une periode donnee
	 * @param ec
	 * @param affectation
	 * @return
	 */
	public static NSArray findPeriodesForAffectation(EOEditingContext ec, EOAffectation affectation) {
		return fetchPeriodeAffectationAnnuelles(ec, 
				CRIDataBus.newCondition(AFFECTATION_KEY + "=%@", new NSArray(affectation)),
				LRSort.newSort(DATE_DEBUT_KEY));
	}

	/**
	 * Indique si la date passee en parametre est inclue dans la periode
	 * @param date
	 * @return
	 */
	public boolean contientDate(NSTimestamp date) {
		boolean contientDate = false;
		
		if (date != null && 
				DateCtrlConges.isAfterEq(date, dateDebut()) && 
				DateCtrlConges.isBeforeEq(date, dateFin())) {
			contientDate = true;
		}
		
		return contientDate;
	}
}
