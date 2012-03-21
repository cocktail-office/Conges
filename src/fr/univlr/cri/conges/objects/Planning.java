package fr.univlr.cri.conges.objects;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;

import com.webobjects.eocontrol.EOCustomObject;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOKeyValueQualifier;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSRange;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.constantes.ConstsHoraire;
import fr.univlr.cri.conges.constantes.ConstsOccupation;
import fr.univlr.cri.conges.constantes.ConstsPlanning;
import fr.univlr.cri.conges.databus.CngHoraireBus;
import fr.univlr.cri.conges.databus.CngUserInfo;
import fr.univlr.cri.conges.eos.modele.conges.EOCET;
import fr.univlr.cri.conges.eos.modele.conges.EOCETTransaction;
import fr.univlr.cri.conges.eos.modele.grhum.EOAbsenceGepeto;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOCalculAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOHoraire;
import fr.univlr.cri.conges.eos.modele.planning.EOMouvement;
import fr.univlr.cri.conges.eos.modele.planning.EOOccupation;
import fr.univlr.cri.conges.eos.modele.planning.EOPeriodeAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOPeriodeFermeture;
import fr.univlr.cri.conges.eos.modele.planning.EOPlanningHebdomadaire;
import fr.univlr.cri.conges.eos.modele.planning.EOTypeHoraire;
import fr.univlr.cri.conges.eos.modele.planning.EOVacanceScolaire;
import fr.univlr.cri.conges.factory.CngCalculFactory;
import fr.univlr.cri.conges.objects.finder.FinderClasse;
import fr.univlr.cri.conges.objects.occupations.Occupation;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.ArrayCtrl;
import fr.univlr.cri.util.wo5.DateCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRLog;
import fr.univlr.cri.webapp.LRSort;
import fr.univlr.cri.ycrifwk.utils.UtilDb;
import fr.univlr.cri.ycrifwk.utils.UtilMisc;

/*
 * Copyright Consortium Coktail, 7 juin 07
 * 
 * cyril.tarade at univ-lr.fr
 * 
 * Ce logiciel est un programme informatique servant � [rappeler les
 * caract�ristiques techniques de votre logiciel]. 
 * 
 * Ce logiciel est r�gi par la licence CeCILL soumise au droit fran�ais et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffus�e par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".
 * 
 * En contrepartie de l'accessibilit� au code source et des droits de copie,
 * de modification et de redistribution accord�s par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limit�e.  Pour les m�mes raisons,
 * seule une responsabilit� restreinte p�se sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les conc�dants successifs.
 * 
 * A cet �gard  l'attention de l'utilisateur est attir�e sur les risques
 * associ�s au chargement,  � l'utilisation,  � la modification et/ou au
 * d�veloppement et � la reproduction du logiciel par l'utilisateur �tant 
 * donn� sa sp�cificit� de logiciel libre, qui peut le rendre complexe � 
 * manipuler et qui le r�serve donc � des d�veloppeurs et des professionnels
 * avertis poss�dant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invit�s � charger  et  tester  l'ad�quation  du
 * logiciel � leurs besoins dans des conditions permettant d'assurer la
 * s�curit� de leurs syst�mes et ou de leurs donn�es et, plus g�n�ralement, 
 * � l'utiliser et l'exploiter dans les m�mes conditions de s�curit�. 
 * 
 * Le fait que vous puissiez acc�der � cet en-t�te signifie que vous avez 
 * pris connaissance de la licence CeCILL, et que vous en avez accept� les
 * termes.
 */

/**
 * Classe de gestion de l'objet central : le planning
 * 
 * @author Emmanuel Geze
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */
public class Planning
		extends EOCustomObject
		implements ConstsPlanning, ConstsOccupation {

	private NSMutableArray horairesHebdomadaires;

	private EOAffectationAnnuelle affectationAnnuelle;

	private NSMutableArray _moisPrev, _moisReel, _moisTest, _moisBack;
	private NSMutableArray _absencesPrev, _absencesReel, _absencesTest, _absencesBack;

	private String type;

	private NSTimestamp firstDay;

	/** la classe qui rassemble tous les calculs associes */
	private PlanningCalcul calcul;

	/** la classe qui rassemble les traitements HTML */
	private PlanningHtml html;

	/** la classe qui rassemble les contraintes */
	private PlanningContraintes contraintes;

	/** les donnees sur la personne qui est connectee */
	private CngUserInfo connectedUserInfo;

	/** le debut de l'annee universitaire en cours */
	private NSTimestamp dateDebutCurrentAnneeUniv;

	public Planning(NSTimestamp aFirstDay, CngUserInfo aConnectedUserInfo, NSTimestamp aDateDebutCurrentAnneeUniv) {
		super();
		calcul = new PlanningCalcul(this);
		html = new PlanningHtml(this);
		contraintes = new PlanningContraintes(this);
		_moisTest = new NSMutableArray();
		_moisPrev = new NSMutableArray();
		_moisReel = new NSMutableArray();
		_moisBack = new NSMutableArray();
		firstDay = aFirstDay;
		connectedUserInfo = aConnectedUserInfo;
		dateDebutCurrentAnneeUniv = aDateDebutCurrentAnneeUniv;
	}

	public boolean isPReel() {
		return REEL.equals(type());
	}

	public boolean isPPrev() {
		return PREV.equals(type());
	}

	public boolean isPTest() {
		return TEST.equals(type());
	}

	public boolean isPBack() {
		return SAVE.equals(type());
	}

	public String type() {
		return type;
	}

	/**
	 * Changement du type de planning. Si le planning de ce type n'est pas deja en
	 * memoire, alors on lance sa construction
	 */
	public void setType(String value) {
		type = value;
		if (isLoadedPlanning(value) == false) {
			try {
				construirePlanning(value, firstDay);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			loadedPlannings().addObject(value);
		}
	}

	/**
	 * Indique si le planning du type passé en paramètre a déjà été chargé
	 * 
	 * @param type
	 * @return
	 */
	private boolean isLoadedPlanning(String type) {
		boolean isLoadedPlanning = false;

		if (loadedPlannings().containsObject(type)) {
			isLoadedPlanning = true;
		}

		return isLoadedPlanning;
	}

	/**
	 * Forcer le rechargement integrale du plannning du type cite.
	 */
	public void forceReloadPlanning(String type) {
		if (loadedPlannings().containsObject(type)) {
			loadedPlannings().removeObject(type);
			if (isPReel()) {
				_moisReel = new NSMutableArray();
				_absencesReel = new NSMutableArray();
			} else if (isPPrev()) {
				_moisPrev = new NSMutableArray();
				_absencesPrev = new NSMutableArray();
			} else if (isPTest()) {
				_moisTest = new NSMutableArray();
				_absencesTest = new NSMutableArray();
			} else if (isPBack()) {
				_moisBack = new NSMutableArray();
				_absencesBack = new NSMutableArray();
			}
		}
	}

	public NSMutableArray mois() {
		NSMutableArray _mois = null;
		if (isPReel()) {
			_mois = _moisReel;
		} else if (isPPrev()) {
			_mois = _moisPrev;
		} else if (isPTest()) {
			_mois = _moisTest;
		} else if (isPBack()) {
			_mois = _moisBack;
		}
		return _mois;
	}

	private void addMois(Mois unMois) {
		mois().addObject(unMois);
	}

	public NSMutableArray semaines() {
		NSMutableArray _semaines = null;
		_semaines = new NSMutableArray(
				ArrayCtrl.applatirArray((NSArray) mois().valueForKey("semaines")));
		return _semaines;
	}

	public NSMutableArray jours() {
		NSMutableArray _jours = null;
		_jours = new NSMutableArray(
				ArrayCtrl.applatirArray((NSArray) semaines().valueForKey("jours")));
		return _jours;
	}

	public NSMutableArray absences() {
		NSMutableArray _lesAbsences = null;
		if (isPReel()) {
			if (_absencesReel == null)
				_absencesReel = new NSMutableArray();
			_lesAbsences = _absencesReel;
		} else if (isPPrev()) {
			if (_absencesPrev == null)
				_absencesPrev = new NSMutableArray();
			_lesAbsences = _absencesPrev;
		} else if (isPTest()) {
			if (_absencesTest == null)
				_absencesTest = new NSMutableArray();
			_lesAbsences = _absencesTest;
		} else if (isPBack()) {
			if (_absencesBack == null)
				_absencesBack = new NSMutableArray();
			_lesAbsences = _absencesBack;
		}
		return _lesAbsences;
	}

	/**
	 * Initialiser le planning de l'agent.
	 * 
	 * @param affectationAnnuelle
	 * @param aConnectedUserInfo
	 *          : les infos sur la personne connectee
	 * @param aDateDebutCurrentAnneeUniv
	 *          : la date de debut de l'anne univ en cours
	 * @return
	 */
	public static Planning newPlanning(EOAffectationAnnuelle affectationAnnuelle, CngUserInfo aConnectedUserInfo, NSTimestamp aDateDebutCurrentAnneeUniv) {
		Planning newPlanning = null;

		if (affectationAnnuelle != null) {
			newPlanning = new Planning(affectationAnnuelle.dateDebutAnnee(), aConnectedUserInfo, aDateDebutCurrentAnneeUniv);
			newPlanning.setAffectationAnnuelle(affectationAnnuelle);

			// Creer les objets HoraireHebdomadaire a partir de EOHoraire
			if (affectationAnnuelle.horaires() != null) {
				for (int i = 0; i < affectationAnnuelle.horaires().count(); i++) {
					EOHoraire horaire = (EOHoraire) affectationAnnuelle.horaires().objectAtIndex(i);
					HoraireHebdomadaire horaireHebdomadaire = new HoraireHebdomadaire();
					horaireHebdomadaire.setHoraire(horaire);
					horaireHebdomadaire.recalculerTotaux();
					newPlanning.addHoraireHebdomadaire(horaireHebdomadaire);
				}
			}
		}
		return newPlanning;
	}

	// liste de tous les plannings charges
	private NSMutableArray _loadedPlannings;

	private NSMutableArray loadedPlannings() {
		if (_loadedPlannings == null)
			_loadedPlannings = new NSMutableArray();
		return _loadedPlannings;
	}

	private EOEditingContext edc() {
		return affectationAnnuelle().editingContext();
	}

	/**
	 * construction du planning generique
	 * 
	 * @param firstDayTS
	 * @throws Throwable
	 */
	private void construirePlanning(String typePlanning, NSTimestamp firstDayTS) throws Throwable {

		long tDebut = System.currentTimeMillis();

		GregorianCalendar firstDayGC, lastDayGC = null;
		NSTimestamp currentDayTS, lastDayTS = null;

		// Initialisation du planning
		// Recuperation jours feries sur une annee a partir d'une date de reference
		currentDayTS = new NSTimestamp(firstDayTS);
		lastDayTS = firstDayTS.timestampByAddingGregorianUnits(1, 0, -1, 0, 0, 0);
		firstDayGC = new GregorianCalendar();
		firstDayGC.setTime(firstDayTS);
		lastDayGC = new GregorianCalendar();
		lastDayGC.setTime(lastDayTS);
		NSArray joursFeries = DateCtrlConges.joursFeriesEntre2Dates(firstDayTS, lastDayTS);

		while (currentDayTS.before(lastDayTS)) {

			GregorianCalendar currentDayGC = new GregorianCalendar();
			currentDayGC.setTime(currentDayTS);
			int previousMonth = currentDayGC.get(Calendar.MONTH);
			int currentMonth = previousMonth;
			Mois unMois = new Mois();
			unMois.setFirstDay(currentDayTS);
			while (currentMonth == previousMonth) {
				Semaine uneSemaine = new Semaine();

				int previousWeek = DateCtrlConges.weekNumber(currentDayTS);
				int currentWeek = previousWeek;

				uneSemaine.setMois(unMois);
				uneSemaine.setNumero(currentWeek);

				while (currentWeek == previousWeek && currentMonth == previousMonth) {
					Jour unJour = new Jour();
					unJour.setSemaine(uneSemaine);
					unJour.setDate(currentDayTS);

					// jour chome
					if (unJour.isDimanche()) {
						unJour.addStatut(Jour.STATUS_CHOME);
					}

					// jour ferie
					if (joursFeries.containsObject(currentDayTS)) {
						unJour.addStatut(Jour.STATUS_FERIE);
					}

					unJour.setNumeroSemaine("");

					currentDayTS = currentDayTS.timestampByAddingGregorianUnits(0, 0, 1, 0, 0, 0);
					currentDayGC.setTime(currentDayTS);
					currentMonth = currentDayGC.get(Calendar.MONTH);
					currentWeek = DateCtrlConges.weekNumber(currentDayTS);
					uneSemaine.add(unJour);
				}

				Jour firstDay = (Jour) uneSemaine.jours().objectAtIndex(0);
				if (firstDay.isLundi()) {
					uneSemaine.setDebut(firstDay.date());
				} else {
					firstDayTS = firstDay.date();
					firstDayGC = new GregorianCalendar();

					firstDayGC.setTime(firstDayTS);
					if (firstDay.isDimanche()) {
						firstDayTS = firstDayTS.timestampByAddingGregorianUnits(0, 0, -6, 0, 0, 0);
					} else {
						firstDayTS = firstDayTS.timestampByAddingGregorianUnits(0, 0, -(firstDayGC.get(GregorianCalendar.DAY_OF_WEEK) - 2), 0, 0, 0);
					}
					uneSemaine.setDebut(firstDayTS);
				}
				unMois.add(uneSemaine);
			}
			addMois(unMois);
		}

		// Activation des periodes d'affectation
		// et remplissage du planning previsionnel
		NSArray periodes = affectationAnnuelle.periodes();
		Enumeration enumPeriodes = periodes.objectEnumerator();

		// conservation du premier planning hebdo et de la
		// permiere semaine pour l'etape
		// copie horaire derniere semaine planning precedent
		// vers premiere semaine planning actuel
		EOPlanningHebdomadaire firstPlanningHebdo = null;
		Semaine firstSemaine = null;

		while (enumPeriodes.hasMoreElements()) {
			EOPeriodeAffectationAnnuelle unePeriode = (EOPeriodeAffectationAnnuelle) enumPeriodes.nextElement();
			NSTimestamp debutPeriode = unePeriode.dateDebut();
			NSTimestamp finPeriode = unePeriode.dateFin();
			NSArray jours = lesJours(debutPeriode, finPeriode);
			for (int i = 0; i < jours.count(); i++) {
				Jour unJour = (Jour) jours.objectAtIndex(i);
				if (unJour.isChome() == false && unJour.isFerie() == false) {
					unJour.addStatut(Jour.STATUS_TRAVAILLE);
					unJour.semaine().activer();
				}
				// memoriser la premier semaine
				if (i == 0) {
					firstSemaine = unJour.semaine();
				}
			}
			NSArray lesPlanningsHebdo = unePeriode.planningHebdomadaires();
			EOQualifier qual = EOQualifier.qualifierWithQualifierFormat("flagNature=%@", new NSArray(type()));
			NSArray sortOrderings = new NSArray(EOSortOrdering.sortOrderingWithKey("dateDebutSemaine", EOSortOrdering.CompareAscending));
			lesPlanningsHebdo = EOQualifier.filteredArrayWithQualifier(lesPlanningsHebdo, qual);
			lesPlanningsHebdo = EOSortOrdering.sortedArrayUsingKeyOrderArray(lesPlanningsHebdo, sortOrderings);
			Enumeration enumLesPlanningsHebdo = lesPlanningsHebdo.objectEnumerator();
			EOPlanningHebdomadaire unPlanningHebdo = null;
			int indexBoucle = 0;
			while (enumLesPlanningsHebdo.hasMoreElements()) {
				unPlanningHebdo = (EOPlanningHebdomadaire) enumLesPlanningsHebdo.nextElement();

				// memoriser le premier planning hebdo
				if (indexBoucle == 0) {
					firstPlanningHebdo = unPlanningHebdo;
				}
				indexBoucle++;

				NSTimestamp dateDebut = unPlanningHebdo.dateDebutSemaine();
				NSTimestamp dateDebutPlus6 = dateDebut.timestampByAddingGregorianUnits(0, 0, 6, 0, 0, 0);
				NSArray semaines = semainesComprisesEntre(dateDebut, dateDebutPlus6);
				Semaine uneSemainePrecedente = null;
				for (int i = 0; i < semaines.count(); i++) {
					Semaine uneSemaine = (Semaine) semaines.objectAtIndex(i);
					// on ne compte pas les semaine a cheval et inactives
					if (uneSemaine.isInactive()) {
						continue;
					}
					if (uneSemaine.planningHebdo() != null) {
						// Cas d'une semaine a cheval sur 2 periodes
						Semaine newSemaine = new Semaine();
						newSemaine.setDebut(uneSemaine.debut());
						newSemaine.setEtat(uneSemaine.etat());
						newSemaine.setMois(uneSemaine.mois());
						newSemaine.setNumero(uneSemaine.numero());
						NSArray lesJours = uneSemaine.jours();
						int index = uneSemaine.mois().semaines().indexOfIdenticalObject(uneSemaine);
						EOQualifier qualLesJours = CRIDataBus.newCondition(
								"date >= %@ AND date <= %@",
								new NSArray(new NSTimestamp[] {
										unPlanningHebdo.periodeAffectationAnnuelle().dateDebut(),
										unPlanningHebdo.periodeAffectationAnnuelle().dateFin() }));
						lesJours = EOQualifier.filteredArrayWithQualifier(lesJours, qualLesJours);

						if (lesJours.count() > 0) {
							newSemaine.add(lesJours);
							uneSemaine.remove(lesJours);
							newSemaine.setPlanningHebdo(unPlanningHebdo);
							EOHoraire unHoraire = unPlanningHebdo.horaire();
							if (unHoraire != null) {
								newSemaine.setHoraire(unHoraire);
							}
							if (uneSemaine.firstDay() != null &&
									newSemaine.firstDay() != null &&
									DateCtrl.isAfter(newSemaine.firstDay(), uneSemaine.firstDay())) {
								newSemaine.mois().addSemaineAtIndex(newSemaine, index + 1);
							} else {
								newSemaine.mois().addSemaineAtIndex(newSemaine, index);
							}
							newSemaine.activer();
						}
					} else {
						uneSemaine.setPlanningHebdo(unPlanningHebdo);
						EOHoraire unHoraire = unPlanningHebdo.horaire();
						if (unHoraire != null) {
							uneSemaine.setHoraire(unHoraire);
						}
					}
					uneSemaine.setSemainePrecedente(uneSemainePrecedente);
					uneSemainePrecedente = uneSemaine;
				}
			}
		}

		// mise en hors affectation des jours non couverts par
		// la/les affectation GEPETO
		NSArray tousLesJours = lesJours(affectationAnnuelle().dateDebutAnnee(), affectationAnnuelle().dateFinAnnee());
		for (int i = 0; i < tousLesJours.count(); i++) {
			Jour unJour = (Jour) tousLesJours.objectAtIndex(i);
			boolean isHorsAffectation = true;
			for (int j = 0; j < periodes.count(); j++) {
				EOPeriodeAffectationAnnuelle unePeriode = (EOPeriodeAffectationAnnuelle) periodes.objectAtIndex(j);
				if (DateCtrlConges.isAfterEq(unJour.date(), unePeriode.dateDebut()) &&
						DateCtrlConges.isBeforeEq(unJour.date(), unePeriode.dateFin())) {
					isHorsAffectation = false;
					break;
				}
			}
			if (isHorsAffectation)
				unJour.addStatut(Jour.STATUS_HORS_AFFECTATION);
		}

		// cas d'un planning previsionnel, si la premiere semaine est non associee
		// et si un planning sur l'annee precedente existe, alors on duplique
		// l'horaire
		// associe sur cette annee et on l'associe a la premiere semaine
		if (isPPrev() && firstPlanningHebdo != null && firstPlanningHebdo.horaire() == null) {
			// on ne fait le traitement que si la premiere semaine est au debut de
			// l'annee
			if (DateCtrlConges.isBeforeEq(firstSemaine.debut(), affectationAnnuelle().dateDebutAnnee())) {
				recupererPremiereSemainePrevisionnel(firstPlanningHebdo, firstSemaine);
			}
		}

		// sur l'interface HTML, on determine les numeros de jours
		// cliquables pour associer un horaire a un semaine
		semaines().valueForKey(Semaine.DETERMINER_JOUR_CLIQUABLE_HTML_KEY);

		EOCalculAffectationAnnuelle oldCompte = affectationAnnuelle().calculAffAnn(type());
		if (oldCompte != null) {

			// gestion du cet : pre calcul (uniquement sur le reel)
			if (toCET() != null && isPReel()) {
				traitementCETPrePlanning();
			}

			// Gestion des occupations
			calculerOccupationChronologiques();

			// gestion du cet : post calcul (uniquement sur le reel)
			if (toCET() != null && isPReel()) {
				traitementCETPostPlanning();
			}

			// Gestion des vacances scolaires
			NSArray vacancesScolaires = affectationAnnuelle.vacancesScolaires();
			for (int i = 0; i < jours().count(); i++) {
				Jour leJour = (Jour) jours().objectAtIndex(i);
				leJour.setIsVacancesScolaires(EOVacanceScolaire.jourIsVacanceInArray(leJour.date(), vacancesScolaires));
			}

		}
		UtilDb.save(edc(), true);

		LRLog.rawLog("loading : [" + type() + "], [" +
				affectationAnnuelle().individu().nomComplet() + "], [" +
				affectationAnnuelle().structure().libelleCourt() + "], [" +
				affectationAnnuelle().annee() + "] - " +
				(System.currentTimeMillis() - tDebut) + "ms",
				2);

	}

	/**
	 * Duplique l'horaire de la derniere semaine du planning reel precedent, le
	 * recopie sur le planning actuel et l'affecte a la premiere semaine.
	 * 
	 * @throws Throwable
	 */
	private void recupererPremiereSemainePrevisionnel(
			EOPlanningHebdomadaire aFirstPlanningHebdo, Semaine aFirstSemaine) throws Throwable {
		EOQualifier qual = CRIDataBus.newCondition(
				"periodeAffectationAnnuelle.affectation.individu = %@ AND " +
						"periodeAffectationAnnuelle.affectation.structure = %@ AND " +
						"dateDebutSemaine < %@ AND dateDebutSemaine > %@ and flagNature = 'R'",
				new NSArray(new Object[] {
						affectationAnnuelle().individu(), affectationAnnuelle().structure(),
						affectationAnnuelle().dateDebutAnnee(),
						affectationAnnuelle().dateDebutAnnee().timestampByAddingGregorianUnits(0, 0, -8, 0, 0, 0)
				}));
		CRIDataBus dataBus = new CRIDataBus(edc());
		NSArray recs = dataBus.fetchArray(edc(), "PlanningHebdomadaire", qual, EOPlanningHebdomadaire.ARRAY_SORT);
		if (recs.count() > 0) {
			EOPlanningHebdomadaire lastPlanningHebdo = (EOPlanningHebdomadaire) recs.lastObject();
			EOHoraire prevHoraire = lastPlanningHebdo.horaire();
			if (prevHoraire != null) {
				EOHoraire newHoraire = CngHoraireBus.dupliqueHoraire(this, prevHoraire, ConstsHoraire.PREFIX_LABEL_HORAIRE_CHARNIERE +
						prevHoraire.affectationAnnuelle().annee() + " et " + affectationAnnuelle().annee());
				// affectation a la premiere semaine
				affecteHorairePourSemainesEntre(newHoraire, aFirstSemaine, aFirstSemaine);
			}
		}
	}

	public EOCET toCET() {
		return affectationAnnuelle().individu().toCET();
	}

	/**
	 * Calculer les debits relatifs au CET avant le planning actuel : - les
	 * rachats annees N-p (p>=0) - les occupations CET annees N-p (p>=1)
	 */
	private void traitementCETPrePlanning() {
		// enlever le cache de tous les debitables
		toCET().valueForKeyPath(
				EOCET.TOS_DEBITABLE_KEY + "." + I_DebitableSurCET.CLEAR_CACHE_KEY);

		// remettre a zero les minutes debitees de toutes les transactions
		toCET().cETTransactions().takeValueForKey(
				new Integer(0), EOCETTransaction.MINUTES_DEBITEES_KEY);

		// virer le cache sur les transactions
		toCET().cETTransactions().valueForKey(
				EOCETTransaction.CLEAR_CACHE_KEY);

		// maintenant, on parcourt tous les debitables anterieurs a ce planning
		NSArray debitables = toCET().tosDebitableBefore(affectationAnnuelle().dateDebutAnnee());

		// pour chacun d'entre eux, on debite les transactions CET
		for (int i = 0; i < debitables.count(); i++) {
			I_DebitableSurCET debitable = (I_DebitableSurCET) debitables.objectAtIndex(i);

			// les transactions CET candidates
			NSArray transactions = debitable.tosTransaction();

			// tant qu'elle n'est pas completement débitée, on avance dans les
			// transactions
			int j = 0;
			while (debitable.minutesRestantesADebiter() > 0 && j < transactions.count()) {
				EOCETTransaction transaction = (EOCETTransaction) transactions.objectAtIndex(j);
				int aDebiter = debitable.minutesRestantesADebiter();
				if (aDebiter > transaction.minutesRestantes().intValue()) {
					aDebiter = transaction.minutesRestantes().intValue();
				}
				debitable.debiter(aDebiter);
				transaction.addDebitable(debitable, aDebiter);
				transaction.debiter(aDebiter);
				j++;
			}
		}
	}

	/**
	 * Meme operation que {@link #traitementCETPrePlanning()}, sauf que la on
	 * traite ; - les rachats N>p (p>=1) - les occupations CET années N>p (p>=1)
	 */
	private void traitementCETPostPlanning() {
		// enlever le cache de tous les debitables
		toCET().valueForKeyPath(
				EOCET.TOS_DEBITABLE_KEY + "." + I_DebitableSurCET.CLEAR_CACHE_KEY);

		// maintenant, on parcourt tous les debitables anterieurs a ce planning
		NSArray debitables = toCET().tosDebitableAfter(affectationAnnuelle().dateFinAnnee());

		// pour chacun d'entre eux, on debite les transactions CET
		for (int i = 0; i < debitables.count(); i++) {
			I_DebitableSurCET debitable = (I_DebitableSurCET) debitables.objectAtIndex(i);

			// les transactions CET candidates
			NSArray transactions = debitable.tosTransaction();

			// tant qu'elle n'est pas completement débitée, on avance dans les
			// transactions
			int j = 0;
			while (debitable.minutesRestantesADebiter() > 0 && transactions.count() < j) {
				EOCETTransaction transaction = (EOCETTransaction) transactions.objectAtIndex(j);
				int aDebiter = debitable.minutesRestantesADebiter();
				if (aDebiter > transaction.minutesRestantes().intValue()) {
					aDebiter = transaction.minutesRestantes().intValue();
				}
				debitable.debiter(aDebiter);
				transaction.addDebitable(debitable, aDebiter);
				transaction.debiter(aDebiter);
				j++;
			}
		}
	}

	/**
	 * lancer le recalcul des droits a conges
	 */
	private void calculerDroitsConges() {

		EOCalculAffectationAnnuelle oldCompte = affectationAnnuelle().calculAffAnn(type());
		NSArray lesJours = jours();

		int minutesDues = affectationAnnuelle().minutesDues();

		// TODO attention, ca doit s'appeller minutesAssociees, car on ne compte pas
		// les conges
		int minutesTravaillees = ((Number) lesJours.valueForKeyPath(
				"@sum." + Jour.DUREE_TRAVAILLEES_EN_MINUTES_KEY)).intValue();

		if (affectationAnnuelle().isCalculAutomatique() == false) {
			minutesTravaillees =
					affectationAnnuelle().toMouvementDroitsCongesNonAutomatiques().mouvementMinutes().intValue() + minutesDues;
			/* LRLog.log("minutesTravaillees en dur"); */
		}

		// si le reliquat initial n'est pas renseigne, on prend
		// les conges restants sur l'annee precedente
		int reliquatInitial = reliquatInitialEnMinutes();
		int minutesRestantes = minutesTravaillees - minutesDues;
		/*
		 * LRLog.log("minutesTravaillees="+TimeCtrl.stringForMinutes(minutesTravaillees
		 * )); LRLog.log("minutesDues="+TimeCtrl.stringForMinutes(minutesDues));
		 * LRLog
		 * .log("minutesRestantes="+TimeCtrl.stringForMinutes(minutesRestantes));
		 */

		oldCompte.setMinutesDues(new Integer(minutesDues));
		oldCompte.setMinutesReliquatRestantes(new Integer(reliquatInitial));
		oldCompte.setMinutesTravaillees(new Integer(minutesTravaillees));
		oldCompte.setMinutesRestantes(new Integer(minutesRestantes));

		// oldCompte.setMinutesDechargeSyndicaleRestantes(oldCompte.minutesDechargeSyndicale());
		Integer minutesDechargeSyndicaleRestantes = new Integer(0);
		if (affectationAnnuelle().toMouvementDechargeSyndicale() != null) {
			minutesDechargeSyndicaleRestantes = affectationAnnuelle().toMouvementDechargeSyndicale().mouvementMinutes();
		}
		oldCompte.setMinutesDechargeSyndicaleRestantes(minutesDechargeSyndicaleRestantes);
	}

	/**
	 * Retourne toutes les absences brutes (sans recalcul, issues directement de
	 * la base de donnees)
	 */
	private NSArray toutesLesAbsencesNonCalculees() {
		// congés légaux
		NSArray congesLegaux = EOAbsenceGepeto.findAbsencesGepetoForIndividu(
				edc(), affectationAnnuelle().individu(), affectationAnnuelle().dateDebutAnnee(), affectationAnnuelle().dateFinAnnee());

		// fermetures
		NSArray fermetures = affectationAnnuelle.fermetures();

		// occupations classiques
		NSArray occupations = affectationAnnuelle.occupations();
		// cas particulier des absences de sauvegarde, on recupere les reelles
		EOQualifier qualNatureType = EOQualifier.qualifierWithQualifierFormat(
				EOOccupation.FLAG_NATURE_KEY + " = %@",
				new NSArray(isPBack() ? REEL : type()));
		occupations = EOQualifier.filteredArrayWithQualifier(occupations, qualNatureType);
		// on ignore les conges legaux de la table PLNG_OCC, on ne prend que celle
		// de GRHUM.ABSENCES
		occupations = EOQualifier.filteredArrayWithQualifier(occupations,
				CRIDataBus.newCondition("isCongeLegal = %@", new NSArray(Boolean.FALSE)));
		// on ignore les fermetures de la table PLNG_OCC, on ne prend que celle de
		// PLNG_PER_FER
		occupations = EOQualifier.filteredArrayWithQualifier(occupations,
				CRIDataBus.newCondition("isFermeture = %@", new NSArray(Boolean.FALSE)));

		// occupations des autres affectations annuelles qui pointent sur le meme
		// service et qui ont une partie commune avec celui ci
		// ex : poser le 22/08/2005 au 03/09/2005 pour l'annee univ 2004-2005 :
		// commun a 2004-2005 et 2005-2006
		NSMutableArray affectationsDeLIndividu = new NSMutableArray(EOAffectationAnnuelle.findAffectationsAnnuellesForIndividuInContext(
				edc(), affectationAnnuelle().individu()));
		EOQualifier qualService = EOQualifier.qualifierWithQualifierFormat("structure = %@", new NSArray(affectationAnnuelle().structure()));
		affectationsDeLIndividu = new NSMutableArray(EOQualifier.filteredArrayWithQualifier(affectationsDeLIndividu, qualService));
		affectationsDeLIndividu.removeObject(affectationAnnuelle());
		NSArray absencesAutresAffAnn = UtilMisc.applatirNSArrays((NSArray) affectationsDeLIndividu.valueForKeyPath("occupations"));
		absencesAutresAffAnn = EOQualifier.filteredArrayWithQualifier(absencesAutresAffAnn, qualNatureType);
		EOQualifier qualCommu = EOQualifier.qualifierWithQualifierFormat(
				"(dateDebut < %@ 	AND dateFin >= %@) OR " +
						"(dateDebut <= %@ AND dateFin > %@) OR " +
						"(dateDebut >= %@ AND dateFin <= %@) OR " +
						"(dateDebut <= %@ AND dateFin >= %@)",
				new NSArray(new NSTimestamp[] {
						affectationAnnuelle().dateDebutAnnee(), affectationAnnuelle().dateDebutAnnee(),
						affectationAnnuelle().dateFinAnnee(), affectationAnnuelle().dateFinAnnee(),
						affectationAnnuelle().dateDebutAnnee(), affectationAnnuelle().dateFinAnnee(),
						affectationAnnuelle().dateDebutAnnee(), affectationAnnuelle().dateFinAnnee() }));
		absencesAutresAffAnn = EOQualifier.filteredArrayWithQualifier(absencesAutresAffAnn, qualCommu);

		// mergeage des 3 + sortage par date de debut
		NSArray toutesLesAbsences = new NSArray();
		toutesLesAbsences = toutesLesAbsences.arrayByAddingObjectsFromArray(congesLegaux);
		toutesLesAbsences = toutesLesAbsences.arrayByAddingObjectsFromArray(occupations);
		toutesLesAbsences = toutesLesAbsences.arrayByAddingObjectsFromArray(fermetures);

		// on ne met pas les absences en double (les absences étaient mises que d'un
		// coté jusqu'au 01/01/2006)
		for (int i = 0; i < absencesAutresAffAnn.count(); i++) {
			EOOccupation absenceAutreAffAnn = (EOOccupation) absencesAutresAffAnn.objectAtIndex(i);
			EOQualifier qualIdentique = EOQualifier.qualifierWithQualifierFormat("dateDebut=%@ AND dateFin=%@",
					new NSArray(new NSTimestamp[] { absenceAutreAffAnn.dateDebut(), absenceAutreAffAnn.dateFin() }));
			if (EOQualifier.filteredArrayWithQualifier(toutesLesAbsences, qualIdentique).count() == 0) {
				toutesLesAbsences = toutesLesAbsences.arrayByAddingObject(absenceAutreAffAnn);
			}
		}

		// classement : d'abord les congés légaux, puis les autres
		// TODO problème potentiel : si une fermeture chevauche une occupation
		// normale
		// alors le débit de l'occupation se fera d'abord, puis la fermeture viendra
		// être comptée par dessus ... A TESTER
		toutesLesAbsences = EOSortOrdering.sortedArrayUsingKeyOrderArray(toutesLesAbsences,
				LRSort.newSort(I_Absence.ORDRE_PRIORITE_KEY + "," + I_Absence.DATE_DEBUT_KEY + "," + I_Absence.SOUS_ORDRE_PRIORITE_KEY));

		return toutesLesAbsences;
	}

	// objet concernant les plannings ayant un JRTI
	private DebitableSurCongesReliquats jrti;

	public DebitableSurCongesReliquats getJrti() {
		return jrti;
	}

	// objet concernant les plannings ayant un blocage de reliquats
	private DebitableSurCongesReliquats blocageReliquatsCet;

	public DebitableSurCongesReliquats getBlocageReliquatsCet() {
		return blocageReliquatsCet;
	}

	/**
	 * lancer le calcul des occupations de la + ancienne a la + recente permet de
	 * faire convenablement le decompte dans les reliquats
	 * 
	 * @throws Throwable
	 */
	public void calculerOccupationChronologiques() throws Throwable {

		// raz liste des absences calculees du planning
		if (isPPrev()) {
			_absencesPrev = null;
		} else if (isPReel()) {
			_absencesReel = null;
		} else if (isPTest()) {
			_absencesTest = null;
		} else if (isPBack()) {
			_absencesBack = null;
		}

		NSArray jours = jours();
		for (int i = 0; i < jours.count(); i++) {
			Jour jour = (Jour) jours.objectAtIndex(i);
			jour.resetMinutesConsommes(type());
		}

		NSArray toutesLesAbsencesNonCalculees = toutesLesAbsencesNonCalculees();

		// 03/10/2011 :
		// Traitement préalable aux calculs de débits, on indique si le jour est une
		// fermeture ou non. Cela pour éviter d'avoir double débit en cas de
		// chevauchement entre occupation et fermeture (si l'occupation commence
		// avant la fermeture)
		for (int i = 0; i < toutesLesAbsencesNonCalculees.count(); i++) {
			I_Absence uneAbsence = (I_Absence) toutesLesAbsencesNonCalculees.objectAtIndex(i);
			if (uneAbsence instanceof EOPeriodeFermeture) {
				remplirStatutJourPeriodeFermeture(uneAbsence);
			} else if (uneAbsence instanceof EOAbsenceGepeto) {
				remplirStatutJourOccupation(uneAbsence);
			} else {
				EOOccupation uneEOOccupation = (EOOccupation) uneAbsence;
				uneEOOccupation.setPlanning(this);

				// on ignore celles qui sont supprimees / refusees (invisibles)
				if (uneEOOccupation.isSupprimee() || uneEOOccupation.isRefusee()) {
					continue;
				}

				if (uneEOOccupation.isOccupationMinute()) {
					remplirStatutJourOccupationJournaliere(uneEOOccupation);
				} else {
					remplirStatutJourOccupation(uneEOOccupation);
				}
			}
		}

		// on verifie si le bonus pour les samedi matin ne doivent pas etre imputes
		semainesActives().valueForKey(Semaine.CHECK_BONUS_SAMEDI_MATIN_KEY);

		// pareil pour les apres midi
		semainesActives().valueForKey(Semaine.CHECK_BONUS_SAMEDI_APRES_MIDI_KEY);

		// recalcul des droits a conges
		calculerDroitsConges();

		EOCalculAffectationAnnuelle oldCompte = affectationAnnuelle().calculAffAnn(type());

		// regularisation (conges supplementaires)
		// modification : la regulation de solde peut etre negative, auquel
		// cas on appelle substract
		NSArray tosMouvementRegularisationSoldeConges = affectationAnnuelle().tosMouvementRegularisationSoldeConges();
		if (tosMouvementRegularisationSoldeConges.count() > 0) {
			for (int i = 0; i < tosMouvementRegularisationSoldeConges.count(); i++) {
				EOMouvement mouvement = (EOMouvement) tosMouvementRegularisationSoldeConges.objectAtIndex(i);
				int minutes = mouvement.mouvementMinutes().intValue();
				if (minutes > 0) {
					oldCompte.addMinutesRestantes(minutes);
				} else {
					oldCompte.substractMinutesRestantes(-minutes);
				}
			}
		}

		// hsup-ccomp -> init de la valeur
		oldCompte.setMinutesBilan(new Integer(0));

		// rachat de RTT (prend sur les reliquats en priorite)
		if (calcul.jrtiEnMinutes() > 0) {
			jrti = new DebitableSurCongesReliquats(this, calcul.jrtiEnMinutes());
			jrti.confirmer();
		}

		// blocage de reliquats avant epargne CET (prend sur les reliquats en
		// priorite)
		if (calcul.blocageReliquatsCetEnMinutes() > 0) {
			blocageReliquatsCet = new DebitableSurCongesReliquats(this, calcul.blocageReliquatsCetEnMinutes());
			blocageReliquatsCet.confirmer();
		}

		// passer 1 par 1 dans cet ordre, et effectuer le calcul des reliquats
		for (int i = 0; i < toutesLesAbsencesNonCalculees.count(); i++) {
			I_Absence uneAbsence = (I_Absence) toutesLesAbsencesNonCalculees.objectAtIndex(i);

			//
			// LRLog.log("uneAbsence="+uneAbsence);

			// fermeture initiale
			if (uneAbsence instanceof EOPeriodeFermeture) {
				EOPeriodeFermeture uneFermeture = (EOPeriodeFermeture) uneAbsence;
				uneFermeture.setPlanning(this);
				if (uneFermeture.isHoraireForce()) {
					uneFermeture.doForceJour();
				}
				uneFermeture.calculerValeur();
				uneFermeture.confirmer();
				uneFermeture.setDebit(uneFermeture.laValeur());
				addAbsenceFermeture(uneFermeture);

			} else if (uneAbsence instanceof EOAbsenceGepeto) {

				// congé légal
				EOAbsenceGepeto unCongeLegal = (EOAbsenceGepeto) uneAbsence;
				unCongeLegal.setPlanning(this);
				if (unCongeLegal.isHoraireForce()) {
					unCongeLegal.doForceJour();
				}
				addAbsenceOccupation(unCongeLegal);

			} else {
				// occupation (utilisation du KeyValueCoding)
				EOOccupation uneEOOccupation = (EOOccupation) uneAbsence;
				uneEOOccupation.setPlanning(this);

				// on ignore celles qui sont supprimees / refusees (invisibles)
				if (uneEOOccupation.isSupprimee() || uneEOOccupation.isRefusee()) {
					continue;
				}

				try {
					Object arguments[] = { uneEOOccupation, this, edc() };
					Class argumentTypes[] = { EOOccupation.class, Planning.class, EOEditingContext.class };
					Object aTypeOccupation = FinderClasse.findClassForType(uneEOOccupation.typeOccupation(), arguments, argumentTypes);

					// genericOccupation est une instance de Occupation ou de ses sous
					// classes
					NSKeyValueCoding genericOccupation = (NSKeyValueCoding) aTypeOccupation;

					genericOccupation.valueForKey(Occupation.CALCULER_VALEUR_KEY);
					Number laValeur = (Number) genericOccupation.valueForKey(Occupation.LA_VALEUR_KEY);
					uneEOOccupation.setDureeReelle(new Integer(laValeur.intValue()));
					genericOccupation.valueForKey(ConstsOccupation.CONFIRMER_KEY);

					// on compte les absences validees et en cours de suppression
					if (ConstsOccupation.CODE_VALIDEE.equals(uneEOOccupation.status()) ||
							ConstsOccupation.CODE_EN_COURS_DE_SUPPRESSION.equals(uneEOOccupation.status()) ||
							ConstsOccupation.CODE_EN_COURS_DE_SUPPRESSION_VISEE.equals(uneEOOccupation.status())) {
						genericOccupation.valueForKey(ConstsOccupation.ACCEPTER_KEY);
					}

					// la valeur a peut etre change suite a la validation (ex: conges drh)
					laValeur = (Number) genericOccupation.valueForKey(Occupation.LA_VALEUR_KEY);

					// hsup-ccomp -> dans le bilan
					if (uneEOOccupation.isAbsenceBilan()) {
						if (ConstsOccupation.CODE_VALIDEE.equals(uneEOOccupation.status())) {
							oldCompte.setMinutesBilan(new Integer(oldCompte.minutesBilan().intValue() + laValeur.intValue()));
						}
					}

					int leDebitConges = ((Number) genericOccupation.valueForKey(Occupation.LE_DEBIT_CONGES_KEY)).intValue();
					int leDebitReliquats = ((Number) genericOccupation.valueForKey(Occupation.LE_DEBIT_RELIQUATS_KEY)).intValue();
					int leDebitDechargeSyndicale = ((Number) genericOccupation.valueForKey(Occupation.LE_DEBIT_DECHARGE_SYNDICALE_KEY)).intValue();
					int leDebitCET = ((Number) genericOccupation.valueForKey(Occupation.LE_DEBIT_CET_KEY)).intValue();
					NSArray lesNodesJours = (NSArray) genericOccupation.valueForKey(Occupation.LES_NODES_JOURS_KEY);

					uneEOOccupation.setValeurMinutes(new Integer(leDebitConges + leDebitReliquats));
					// cas particulier des heures supp. et conges comp.,
					// c'est laValeur qui est la bonne car il n'y a pas de debit
					if (uneEOOccupation.isAbsenceBilan()) {
						uneEOOccupation.setValeurMinutes(laValeur.intValue());
					}

					uneEOOccupation.setLeDebitConges(leDebitConges);
					uneEOOccupation.setLeDebitReliquats(leDebitReliquats);
					uneEOOccupation.setLeDebitDechargeSyndicale(leDebitDechargeSyndicale);
					uneEOOccupation.setLeDebitCET(leDebitCET);
					uneEOOccupation.setLesNodesJours(lesNodesJours);

					if (uneEOOccupation.isOccupationMinute()) {
						addOccupationJournaliere(uneEOOccupation);
					} else {
						addAbsenceOccupation(uneEOOccupation);
					}

				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}

		// decompte sur le temps travaille si conges legaux depasse le seuil
		int decompteLegal = CngCalculFactory.getMinutesDecompteLegal(this);
		oldCompte.setMinutesDecompteLegal(new Integer(decompteLegal));
		oldCompte.substractMinutesRestantes(decompteLegal);

		/*
		 * // regularisation (conges supplementaires) // modification : la
		 * regulation de solde peut etre negative, auquel // cas on appelle
		 * substract NSArray tosMouvementRegularisationSoldeConges =
		 * affectationAnnuelle().tosMouvementRegularisationSoldeConges(); if
		 * (tosMouvementRegularisationSoldeConges.count() > 0) { for (int i=0;
		 * i<tosMouvementRegularisationSoldeConges.count(); i++) { EOMouvement
		 * mouvement = (EOMouvement)
		 * tosMouvementRegularisationSoldeConges.objectAtIndex(i); int minutes =
		 * mouvement.mouvementMinutes().intValue(); if (minutes > 0) {
		 * oldCompte.addMinutesRestantes(minutes); } else {
		 * oldCompte.substractMinutesRestantes(-minutes); } } }
		 */

		// si le bilan heures sup / conges comp est negatif, on l'ote des conges
		if (oldCompte.minutesBilan().intValue() < 0) {
			oldCompte.addMinutesRestantes(oldCompte.minutesBilan().intValue());
		}

		// calculs relatifs aux occupation (utilise pour la demande de cet)
		consommationReliquatEnMinutes = ((BigDecimal) absences().valueForKeyPath("@sum." + Occupation.LE_DEBIT_RELIQUATS_KEY)).intValue();
		consommationCongesEnMinutes = ((BigDecimal) absences().valueForKeyPath("@sum." + Occupation.LE_DEBIT_CONGES_KEY)).intValue();

		// enregistrer la valeur de consommation de congés
		int minutesCongesConsommees = consommationCongesEnMinutes;
		// inclure le bilan s'il est négatif
		if (oldCompte.minutesBilan().intValue() < 0) {
			minutesCongesConsommees += -(oldCompte.minutesBilan().intValue());
		}
		oldCompte.setMinutesCongesConsommees(new Integer(minutesCongesConsommees));

		// consommation des occupations sur les jours de validite des reliquats
		NSArray nodesValiditeReliquat = ArrayCtrl.applatirArray((NSArray) absences().valueForKey(I_Absence.LES_NODES_JOURS_KEY));
		nodesValiditeReliquat = EOQualifier.filteredArrayWithQualifier(nodesValiditeReliquat,
				CRIDataBus.newCondition(JourReliquatCongesNode.DATE_KEY + "<%@",
						new NSArray(DateCtrlConges.date1erJanAnneeUniv(affectationAnnuelle().dateDebutAnnee()))));
		int minutesValiditeReliquat = 0;
		minutesValiditeReliquat += ((Number) nodesValiditeReliquat.valueForKeyPath("@sum." + JourReliquatCongesNode.DEBIT_RELIQUATS_KEY)).intValue();
		oldCompte.setMinutesReliquatConsommeesDateReliquat(new Integer(minutesValiditeReliquat));
		minutesValiditeReliquat += ((Number) nodesValiditeReliquat.valueForKeyPath("@sum." + JourReliquatCongesNode.DEBIT_CONGES_KEY)).intValue();
		oldCompte.setMinutesConsommeesDateReliquat(new Integer(minutesValiditeReliquat));

		// TEMPO calcul crici
		NSArray lesJours3112 = EOQualifier.filteredArrayWithQualifier(jours(),
				CRIDataBus.newCondition("date < %@", new NSArray(DateCtrlConges.date1erJanAnneeUniv(dateDebutCurrentAnneeUniv))));
		int minutesTravaillees3112 = ((Number) lesJours3112.valueForKeyPath(
				"@sum." + Jour.DUREE_TRAVAILLEES_EN_MINUTES_KEY)).intValue();
		oldCompte.setMinutesTravaillees3112(new Integer(minutesTravaillees3112));

		// TEMPO Calcul CRICRI
		oldCompte.setMinutesConges3112(
				new Integer(((Number) nodesValiditeReliquat.valueForKeyPath("@sum." + JourReliquatCongesNode.DEBIT_CONGES_KEY)).intValue()));

		// sauvegarde générale
		UtilDb.save(edc(), true);
	}

	/**
	 * @param affectationAnnuelle
	 */
	private void setAffectationAnnuelle(EOAffectationAnnuelle uneAffectationAnnuelle) {
		affectationAnnuelle = uneAffectationAnnuelle;
	}

	public EOAffectationAnnuelle affectationAnnuelle() {
		return affectationAnnuelle;
	}

	/**
	 * @param unHoraireHebdomadaire
	 */
	public void addHoraireHebdomadaire(HoraireHebdomadaire unHoraireHebdomadaire) {
		if (horairesHebdomadaires == null)
			horairesHebdomadaires = new NSMutableArray();
		horairesHebdomadaires.addObject(unHoraireHebdomadaire);
	}

	public void removeHoraireHebdomadaire(HoraireHebdomadaire unHoraireHebdomadaire) {
		horairesHebdomadaires.removeObject(unHoraireHebdomadaire);
	}

	public NSArray horairesHebdomadaires() {
		return horairesHebdomadaires;
	}

	/**
	 * @param firstDay
	 * @param lastDay
	 * @return
	 */
	public NSArray semainesComprisesEntre(NSTimestamp firstDay, NSTimestamp lastDay) {
		NSArray arguments = new NSArray(new NSTimestamp[] { firstDay, lastDay, firstDay, firstDay, lastDay, lastDay });
		EOQualifier qualSemaines = EOQualifier.qualifierWithQualifierFormat("(firstDay >= %@ AND lastDay =< %@) OR " + "(firstDay <= %@ AND lastDay >= %@) OR "
				+ "(firstDay <= %@ AND lastDay >= %@)", arguments);
		return EOQualifier.filteredArrayWithQualifier(semaines(), qualSemaines);
	}

	/**
   *
   */
	public Jour leJour(NSTimestamp uneDate) {
		Jour leJour = null;
		NSArray lesJours = jours();
		EOQualifier qualJourDontLaDateEst = EOQualifier.qualifierWithQualifierFormat("date=%@", new NSArray(new NSTimestamp[] { TimeCtrl.dateToMinuit(uneDate) }));

		lesJours = EOQualifier.filteredArrayWithQualifier(lesJours, qualJourDontLaDateEst);
		leJour = (Jour) lesJours.lastObject();
		return leJour;
	}

	public NSArray lesJours(NSTimestamp dateDebut, NSTimestamp dateFin) {
		NSArray lesJours = jours();
		GregorianCalendar dateDebutGC = new GregorianCalendar();
		dateDebutGC.setTime(dateDebut);
		dateDebut = dateDebut.timestampByAddingGregorianUnits(0, 0, 0, -(dateDebutGC.get(Calendar.HOUR_OF_DAY)), 0, 0);
		EOQualifier qualLesJours = EOQualifier.qualifierWithQualifierFormat("date>=%@ AND date<=%@", new NSArray(new NSTimestamp[] {
				TimeCtrl.dateToMinuit(dateDebut), TimeCtrl.dateToMinuit(dateFin) }));

		lesJours = EOQualifier.filteredArrayWithQualifier(lesJours, qualLesJours);

		return lesJours;
	}

	public NSArray semainesActives() {
		NSArray semainesActives = null;
		EOKeyValueQualifier qualSemainesActives = null;

		qualSemainesActives = new EOKeyValueQualifier("isActive", EOQualifier.QualifierOperatorEqual, new Integer(1));
		semainesActives = EOQualifier.filteredArrayWithQualifier(semaines(), qualSemainesActives);

		return semainesActives;
	}

	public NSArray semainesLibres() {
		NSArray semainesLibres = semainesActives();
		EOQualifier qualSemainesLibres = EOQualifier.qualifierWithQualifierFormat("horaire = nil", null);

		semainesLibres = EOQualifier.filteredArrayWithQualifier(semainesLibres, qualSemainesLibres);

		return semainesLibres;
	}

	/**
   *
   */
	/*
	 * public NSArray semainesDisponibles(String firstNumWeek) { NSMutableArray
	 * semainesDisponibles = new NSMutableArray(); NSArray semainesActives = null;
	 * Enumeration enumSemainesActives = null; // Semaine laPremiereSemaine =
	 * null; Semaine uneSemaine = null; String numeroSemaine = null; boolean
	 * flagSemaineDispo = false;
	 * 
	 * semainesActives = semainesActives(); enumSemainesActives =
	 * semainesActives.objectEnumerator(); while
	 * (enumSemainesActives.hasMoreElements()) { uneSemaine = (Semaine)
	 * enumSemainesActives.nextElement(); numeroSemaine = uneSemaine.numero(); if
	 * (flagSemaineDispo || numeroSemaine.equals(firstNumWeek)) { flagSemaineDispo
	 * = true; // Distinct sur les semaines if (semainesDisponibles.count() > 0 &&
	 * numeroSemaine.equals(((Semaine)
	 * (semainesDisponibles.lastObject())).numero())) {
	 * semainesDisponibles.removeLastObject(); }
	 * semainesDisponibles.addObject(uneSemaine); } } return semainesDisponibles;
	 * }
	 */

	/**
	 * @deprecated
	 * @see #affecteHorairePourSemaines(EOHoraire, NSArray)
	 * @param horaireSelectionne
	 * @param semaine
	 * @param semaine2
	 * @throws Throwable
	 */
	public String affecteHorairePourSemainesEntre(EOHoraire horaire, Semaine firstSemaine, Semaine lastSemaine) throws Throwable {
		String resultat = null;
		NSArray lesSemaines = semaines();
		NSArray lesSemainesAAffecter = null;
		Enumeration enumLesSemaines = null;
		Semaine uneSemaine = null;
		int indexFirstSemaine = 0, indexLastSemaine = 0;
		int dureeHebdo = 0, oldDureeHebdo = 0;
		int nbreSemainesHautes = 0;
		EOCalculAffectationAnnuelle calculs = affectationAnnuelle().calculAffAnn(type());
		boolean isHoraireTypeSemaineHaute = false;

		indexFirstSemaine = lesSemaines.indexOfIdenticalObject(firstSemaine);
		indexLastSemaine = lesSemaines.indexOfIdenticalObject(lastSemaine);
		isHoraireTypeSemaineHaute = horaire != null && horaire.isSemaineHaute();
		if (isHoraireTypeSemaineHaute) {
			// Verification des limites du nbre de semaines hautes dans l'annee
			// et du maximum d'heures de conges par an
			enumLesSemaines = lesSemaines.objectEnumerator();
			String numSemaine, lastNumSemaine = null;
			while (enumLesSemaines.hasMoreElements()) {
				uneSemaine = (Semaine) enumLesSemaines.nextElement();
				numSemaine = uneSemaine.numero();
				if (numSemaine.equals(lastNumSemaine) == false) {
					if (uneSemaine.isSemaineHaute()) {
						nbreSemainesHautes++;
					}
				}
				lastNumSemaine = numSemaine;
			}
			lesSemainesAAffecter = lesSemaines.subarrayWithRange(new NSRange(indexFirstSemaine, indexLastSemaine - indexFirstSemaine + 1));
			enumLesSemaines = lesSemainesAAffecter.objectEnumerator();
			lastNumSemaine = null;
			while (enumLesSemaines.hasMoreElements()) {
				uneSemaine = (Semaine) enumLesSemaines.nextElement();
				numSemaine = uneSemaine.numero();
				if (numSemaine.equals(lastNumSemaine) == false) {
					if (uneSemaine.horaire() == null || uneSemaine.isSemaineBasse()) {
						nbreSemainesHautes++;
					}
				}
				lastNumSemaine = numSemaine;
			}
		}

		NSArray lesSemainesAAffecterUniques = lesSemaines.subarrayWithRange(new NSRange(indexFirstSemaine, indexLastSemaine - indexFirstSemaine + 1));

		// recuperation de toutes les semaines a affecter (recup via les mois pour
		// avoir les semaines qui chevauchent)
		lesSemainesAAffecter = new NSArray();

		for (int i = 0; i < lesSemainesAAffecterUniques.count(); i++) {
			Semaine semaine = (Semaine) lesSemainesAAffecterUniques.objectAtIndex(i);
			// on prend les semaines consecutives
			EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(
					"numero = %@ AND (debut >= %@ AND debut <= %@)",
					new NSArray(new Object[] {
							semaine.numero(),
							semaine.debut().timestampByAddingGregorianUnits(0, 0, -7, 0, 0, 0),
							semaine.debut().timestampByAddingGregorianUnits(0, 0, 7, 0, 0, 0) }));
			NSArray lesSemainesArray = (NSArray) mois().valueForKey("semaines");
			NSArray toutesLesSemaines = new NSArray();
			for (int j = 0; j < lesSemainesArray.count(); j++) {
				toutesLesSemaines = toutesLesSemaines.arrayByAddingObjectsFromArray((NSArray) lesSemainesArray.objectAtIndex(j));
			}
			NSArray lesSemainesPourNumero = (NSArray) EOQualifier.filteredArrayWithQualifier(toutesLesSemaines, qual);
			lesSemainesAAffecter = lesSemainesAAffecter.arrayByAddingObjectsFromArray(lesSemainesPourNumero);
		}

		NSArray copyLesSemaines = (NSArray) lesSemaines.clone();
		enumLesSemaines = lesSemainesAAffecter.objectEnumerator();

		while (enumLesSemaines.hasMoreElements()) {
			// on affecte les horaires qu'au semaines dont la quotite est la meme que
			// l'horaire
			uneSemaine = (Semaine) enumLesSemaines.nextElement();
			if (uneSemaine.planningHebdo() != null && (
						affectationAnnuelle().isTempsPartielAnnualise() || (
								!affectationAnnuelle().isTempsPartielAnnualise() &&
								uneSemaine.planningHebdo().periodeAffectationAnnuelle().quotite().floatValue() == horaire.quotite().floatValue()))) {
				oldDureeHebdo = uneSemaine.dureeTravailleeEnMinutes();
				uneSemaine.setHoraire(horaire);
				dureeHebdo = uneSemaine.dureeTravailleeEnMinutes();
				if (calculs.toAffectationAnnuelle().isCalculAutomatique()) {
					calculs.substractMinutesTravaillees(oldDureeHebdo);
					calculs.addMinutesTravaillees(dureeHebdo);
				}
			}
		}
		lesSemaines = new NSArray(copyLesSemaines);

		// on lance le recalcul (uniquement pour les semaines completes Fermeture ou
		// cong� l�gal)
		calculerOccupationChronologiques();

		return resultat;
	}

	/**
	 * TODO faire le controle de semaines hautes ici ?
	 * 
	 * @param horaireSelectionne
	 * @param semaines
	 * @throws Throwable
	 */
	public String affecteHorairePourSemaines(
			EOHoraire horaire, NSArray semaines) throws Throwable {

		String resultat = null;
		NSArray lesSemaines = semaines();
		EOCalculAffectationAnnuelle calculs = affectationAnnuelle().calculAffAnn(type());
		NSArray lesSemainesAAffecterUniques = semaines;
		NSArray lesSemainesAAffecter = new NSArray();

		// recuperation de toutes les semaines a affecter (recup via les mois pour
		// avoir les semaines qui chevauchent)
		for (int i = 0; i < lesSemainesAAffecterUniques.count(); i++) {
			Semaine semaine = (Semaine) lesSemainesAAffecterUniques.objectAtIndex(i);
			// on prend les semaines consecutives
			EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(
					"numero = %@ AND (debut >= %@ AND debut <= %@)",
					new NSArray(new Object[] {
							semaine.numero(),
							semaine.debut().timestampByAddingGregorianUnits(0, 0, -7, 0, 0, 0),
							semaine.debut().timestampByAddingGregorianUnits(0, 0, 7, 0, 0, 0) }));
			NSArray lesSemainesArray = (NSArray) mois().valueForKey("semaines");
			NSArray toutesLesSemaines = new NSArray();
			for (int j = 0; j < lesSemainesArray.count(); j++) {
				toutesLesSemaines = toutesLesSemaines.arrayByAddingObjectsFromArray((NSArray) lesSemainesArray.objectAtIndex(j));
			}
			NSArray lesSemainesPourNumero = (NSArray) EOQualifier.filteredArrayWithQualifier(toutesLesSemaines, qual);
			lesSemainesAAffecter = lesSemainesAAffecter.arrayByAddingObjectsFromArray(lesSemainesPourNumero);
		}

		NSArray copyLesSemaines = (NSArray) lesSemaines.clone();

		for (int i = 0; i < lesSemainesAAffecter.count(); i++) {
			Semaine uneSemaine = (Semaine) lesSemainesAAffecter.objectAtIndex(i);
			// on affecte les horaires qu'aux semaines dont la quotite est la meme que
			// l'horaire
			if (horaire.isAssociableALaSemaine(uneSemaine)) {
				int oldDureeHebdo = uneSemaine.dureeTravailleeEnMinutes();
				uneSemaine.setHoraire(horaire);
				if (affectationAnnuelle().isCalculAutomatique()) {
					int dureeHebdo = uneSemaine.dureeTravailleeEnMinutes();
					calculs.substractMinutesTravaillees(oldDureeHebdo);
					calculs.addMinutesTravaillees(dureeHebdo);
				}
			}
		}

		lesSemaines = new NSArray(copyLesSemaines);

		// on lance le recalcul (uniquement pour les semaines completes Fermeture ou
		// congé légal)
		calculerOccupationChronologiques();

		return resultat;
	}

	/**
	 * TODO deplacer les appels a ces methodes depuis les interface vers
	 * contraintes
	 */
	public boolean limiteSemainesHautesDepassee() {
		return contraintes.limiteSemainesHautesDepassee();
	}

	public int limiteCongesEnMinutes() {
		return contraintes.limiteCongesEnMinutes();
	}

	public boolean limiteCongesDepassee() {
		return contraintes.limiteCongesDepassee();
	}

	public int congesDepassementEnMinutes() {
		return contraintes.congesDepassementEnMinutes();
	}

	public String congesDepassement() {
		return contraintes.congesDepassement();
	}

	public String heuresDues() {
		return contraintes.heuresDues();
	}

	public String heuresMax() {
		return contraintes.heuresMax();
	}

	public String limiteCongesDepasseeMsg() {
		return contraintes.limiteCongesDepasseeMsg();
	}

	public String limiteSemainesHautesDepasseeMsg() {
		return contraintes.limiteSemainesHautesDepasseeMsg();
	}

	public String heuresAssociees() {
		return DateCtrlConges.to_duree(affectationAnnuelle().calculAffAnn(type()).
				minutesTravaillees().intValue());
	}

	public String heuresTravaillees() {
		int minutesTravaillees = 0;
		NSArray lesMois = mois();
		Enumeration enumLesMois = lesMois.objectEnumerator();

		while (enumLesMois.hasMoreElements()) {
			Mois unMois = (Mois) enumLesMois.nextElement();
			minutesTravaillees += unMois.minutesTravaillees();
		}

		return DateCtrlConges.to_duree(minutesTravaillees);
	}

	public String heuresTravailleesEnJours() {
		int minutesTravaillees = 0;
		NSArray lesMois = mois();
		Enumeration enumLesMois = lesMois.objectEnumerator();

		while (enumLesMois.hasMoreElements()) {
			Mois unMois = (Mois) enumLesMois.nextElement();
			minutesTravaillees += unMois.minutesTravaillees();
		}

		return TimeCtrl.to_duree_en_jours(minutesTravaillees, dureeJour());
	}

	/**
	 * Effectue le cumul des heures associees au planning depuis le debut de
	 * l'annee a aujourd'hui exclus.
	 * 
	 * @param dateReference
	 * @return
	 */
	public int heuresTravailleesRealiseesEnMinutes(NSTimestamp dateReference) {
		int minutesTravailleesRealisees = 0;
		NSTimestamp laDateDuJour = affectationAnnuelle().dateDebutAnnee();
		GregorianCalendar nowGC = new GregorianCalendar();
		NSArray lesJours = jours();
		Enumeration enumLesJours = lesJours.objectEnumerator();
		nowGC.setTime(dateReference);
		NSTimestamp laDate = dateReference.timestampByAddingGregorianUnits(
				0, 0, -1, -nowGC.get(GregorianCalendar.HOUR_OF_DAY), -nowGC.get(GregorianCalendar.MINUTE), -nowGC.get(GregorianCalendar.SECOND));

		while (DateCtrl.isBefore(laDateDuJour, laDate) && enumLesJours.hasMoreElements()) {
			Jour unJour = (Jour) enumLesJours.nextElement();
			if (unJour.isTravaille()) {
				minutesTravailleesRealisees += unJour.dureeTravailleeEnMinutes();
			} else if (unJour.isCongeAM() || unJour.isTravaillePM()) {
				minutesTravailleesRealisees += unJour.dureePMTravailleeEnMinutes();
			} else if (unJour.isCongePM() || unJour.isTravailleAM()) {
				minutesTravailleesRealisees += unJour.dureePMTravailleeEnMinutes();
			}
			laDateDuJour = unJour.date();
		}
		return minutesTravailleesRealisees;
	}

	public String heuresTravailleesRealisees(NSTimestamp dateReference) {
		return DateCtrlConges.to_duree(heuresTravailleesRealiseesEnMinutes(dateReference));
	}

	public String heuresTravailleesRealiseesEnJours(NSTimestamp dateReference) {
		return TimeCtrl.to_duree_en_jours(heuresTravailleesRealiseesEnMinutes(dateReference), dureeJour());
	}

	/**
	 * TODO deplacer les appels de ces methodes des interfaces vers calcul
	 */
	public int congesRestantsEnMinutes() {
		return calcul.congesRestantsEnMinutes();
	}

	public String congesRestants() {
		return calcul.congesRestants();
	}

	public String congesRestantsEnJours() {
		return calcul.congesRestantsEnJours();
	}

	public int congesGlobalRestantsEnMinutes() {
		return calcul.congesGlobalRestantsEnMinutes();
	}

	public String congesGlobalRestants() {
		return calcul.congesGlobalRestants();
	}

	public String congesGlobalRestantsEnJours() {
		return calcul.congesGlobalRestantsEnJours();
	}

	public String congesGlobalRestantsEnJourA7h00() {
		return calcul.congesGlobalRestantsEnJourA7h00();
	}

	public int congesInitiauxSansDecompteEnMinutes() {
		return calcul.congesInitiauxSansDecompteEnMinutes();
	}

	public String congesInitiauxSansDecompte() {
		return calcul.congesInitiauxSansDecompte();
	}

	public String congesInitiauxSansDecompteEnJours() {
		return calcul.congesInitiauxSansDecompteEnJours();
	}

	public int congesInitiauxEnMinutes() {
		return calcul.congesInitiauxEnMinutes();
	}

	public String congesInitiauxEnHeures() {
		return calcul.congesInitiauxEnHeures();
	}

	public String congesInitiauxEnJours() {
		return calcul.congesInitiauxEnJours();
	}

	/**
	 * reliquat de conges pour le CET
	 */
	public int reliquatBasculePourCETEnMinutes(NSTimestamp dateOuvertureSaisieCET, NSTimestamp dateFermetureSaisieCET) {
		return calcul.reliquatBasculePourCETEnMinutes(dateOuvertureSaisieCET, dateFermetureSaisieCET);
	}

	public String reliquatBasculePourCETEnJours(NSTimestamp dateOuvertureSaisieCET, NSTimestamp dateFermetureSaisieCET) {
		return calcul.reliquatBasculePourCETEnJours(dateOuvertureSaisieCET, dateFermetureSaisieCET);
	}

	public String reliquatBasculePourCET(NSTimestamp dateOuvertureSaisieCET, NSTimestamp dateFermetureSaisieCET) {
		return calcul.reliquatBasculePourCET(dateOuvertureSaisieCET, dateFermetureSaisieCET);
	}

	/**
	 * consommation de reliquat
	 */

	private int consommationReliquatEnMinutes;

	private int consommationReliquatEnMinutes() {
		return consommationReliquatEnMinutes;
	}

	public String consommationReliquatEnJours() {
		return TimeCtrl.to_duree_en_jours(consommationReliquatEnMinutes(), dureeJour());
	}

	public String consommationReliquat() {
		return DateCtrlConges.to_duree(consommationReliquatEnMinutes());
	}

	/**
	 * consommation de conges
	 */

	private int consommationCongesEnMinutes;

	public int consommationCongesEnMinutes() {
		return consommationCongesEnMinutes;
	}

	public String consommationCongesEnJours() {
		return TimeCtrl.to_duree_en_jours(consommationCongesEnMinutes(), dureeJour());
	}

	public String consommationConges() {
		return DateCtrlConges.to_duree(consommationCongesEnMinutes());
	}

	/**
	 * methodes de recuperation des reliquat restants sur l'annee - c'est la
	 * valeur du reliquat utilisable dans les cong�s et uniquement cela (pas de
	 * cet)
	 */
	public int reliquatRestantEnMinutes() {
		return calcul.reliquatRestantEnMinutes();
	}

	public String reliquatRestant() {
		return calcul.reliquatRestant();
	}

	public String reliquatRestantEnJours() {
		return calcul.reliquatRestantEnJours();
	}

	public String reliquatRestantlEnJours7Heures() {
		return calcul.reliquatRestantlEnJours7Heures();
	}

	/**
	 * Reliquat initiaux pour l'annee. Si cette valeur est renseignee
	 * explicitement (<code>minutesReliquatInitial<code> de l'entite
	 * <code>CalculAffectationAnnuelle</code>), alors c'est cette valeur, sinon
	 * c'est la valeur <code>minutesRestantes<code> de la meme
	 * entite, pour l'annee universitaire precedente.
	 */
	public int reliquatInitialEnMinutes() {
		return calcul.reliquatInitialEnMinutes();
	}

	public String reliquatInitial() {
		return calcul.reliquatInitial();
	}

	public String reliquatInitialEnJours() {
		return calcul.regularistationEnJours();
	}

	/**
	 * methodes de recuperation du total restant : reliquat + conges restants
	 */
	public int totalRestantEnMinutes() {
		return calcul.totalRestantEnMinutes();
	}

	public String totalRestant() {
		return calcul.totalRestant();
	}

	public String totalRestantEnJours() {
		return calcul.totalRestantEnJours();
	}

	/**
	 * methodes de recuperation du malus des conges legaux
	 */
	public int decompteLegalEnMinutes() {
		return calcul.decompteLegalEnMinutes();
	}

	public String decompteLegal() {
		return calcul.decompteLegal();
	}

	public String decompteLegalEnJours() {
		return calcul.decompteLegalEnJours();
	}

	/**
	 * methodes de recuperation du bilan hsup / conges comp.
	 */
	public int bilanHSCCEnMinutes() {
		return calcul.bilanHSCCEnMinutes();
	}

	public String bilanHSCCEnHeures() {
		return calcul.bilanHSCCEnHeures();
	}

	public String bilanHSCCEnJours() {
		return calcul.bilanHSCCEnJours();
	}

	/**
	 * methodes de recuperation de la regularisation (du a une mutation)
	 */
	public int regularistationEnMinutes() {
		return calcul.regularistationEnMinutes();
	}

	public String regularistationEnHeures() {
		return calcul.regularistationEnHeures();
	}

	public String regularistationEnJours() {
		return calcul.regularistationEnJours();
	}

	/**
	 * methodes de recuperation des rachats de conges
	 */
	public int jrtiEnMinutes() {
		return calcul.jrtiEnMinutes();
	}

	public String jrtiEnHeures() {
		return calcul.jrtiEnHeures();
	}

	public String jrtiEnJours() {
		return calcul.jrtiEnJours();
	}

	/**
	 * methodes de recuperation des rachats de conges
	 */
	public int blocageReliquatsCetEnMinutes() {
		return calcul.blocageReliquatsCetEnMinutes();
	}

	public String blocageReliquatsCetEnHeures() {
		return calcul.blocageReliquatsCetEnHeures();
	}

	public String blocageReliquatsCetEnJours() {
		return calcul.blocageReliquatsCetEnJours();
	}

	/**
	 * methodes de recuperation des decharges syndicales initiales et restantes
	 */
	public int dechargeSyndicaleInitiauxEnMinutes() {
		return calcul.dechargeSyndicaleInitiauxEnMinutes();
	}

	public String dechargeSyndicaleInitiauxEnHeures() {
		return calcul.dechargeSyndicaleInitiaux();
	}

	public String dechargeSyndicaleInitiauxEnJours() {
		return calcul.dechargeSyndicaleInitiauxEnJours();
	}

	public int dechargeSyndicaleRestantsEnMinutes() {
		return calcul.dechargeSyndicaleRestantsEnMinutes();
	}

	public String dechargeSyndicaleRestantsEnHeures() {
		return calcul.dechargeSyndicaleRestants();
	}

	public String dechargeSyndicaleRestantsEnJours() {
		return calcul.dechargeSyndicaleRestantsEnJours();
	}

	public boolean isValidePourAssociationHoraire() {
		boolean isValidePourAssociationHoraire = false;
		if (isNonValide() && affectationAnnuelle().horaires() != null && affectationAnnuelle().horaires().count() > 0) {
			isValidePourAssociationHoraire = true;
		}
		return isValidePourAssociationHoraire;
	}

	private void addAbsenceFermeture(I_Absence uneFermeture) {
		remplirStatutJourPeriodeFermeture(uneFermeture);
		absences().addObject((I_Absence) uneFermeture);
	}

	public void addOccupationJournaliere(EOOccupation uneOccupation) {
		absences().addObject((I_Absence) uneOccupation);
		remplirStatutJourOccupationJournaliere(uneOccupation);
	}

	/**
	 * Remplir le statut fermeture pour une fermeture
	 * 
	 * @param periodeFermeture
	 */
	private void remplirStatutJourPeriodeFermeture(
			I_Absence periodeFermeture) {
		if (periodeFermeture instanceof EOPeriodeFermeture) {
			NSArray lesJours = lesJours(periodeFermeture.dateDebut(), periodeFermeture.dateFin());
			// on ne ferme que les jour de l'affectaiton
			Enumeration enumLesJours = lesJours.objectEnumerator();
			while (enumLesJours.hasMoreElements()) {
				Jour unJour = (Jour) enumLesJours.nextElement();
				unJour.addStatut(Jour.STATUS_FERMETURE);
			}
		}
	}

	/**
	 * Remplir le statut d'une occupation journaliere.
	 * 
	 * Le type d'absence concernée est : - toutes les occupations à la minutes -
	 * sauf les heures supplémentaires
	 * 
	 * @param uneOccupation
	 */
	private void remplirStatutJourOccupationJournaliere(EOOccupation uneOccupation) {
		// indique l'etat du jour un conges comp ou aut abs
		if (uneOccupation.isOccupationMinute() && !uneOccupation.isHeureSup()) {
			Jour leJour = (Jour) lesJours(
					TimeCtrl.dateToMinuit(uneOccupation.dateDebut()),
					TimeCtrl.dateToMinuit(uneOccupation.dateDebut()).timestampByAddingGregorianUnits(0, 0, 0, 12, 0, 0))
					.objectAtIndex(0);
			leJour.addStatut(Jour.STATUS_CONGES_COMP);

			// etat de validation / suppression
			if (uneOccupation.isEnCoursDeValidation()) {
				leJour.addStatut(Jour.STATUS_EN_COURS_DE_VALIDATION);
			} else if (uneOccupation.isEnCoursDeSuppression()) {
				leJour.addStatut(Jour.STATUS_EN_COURS_DE_SUPPRESSION);
			}
		}
	}

	/**
	 * Mettre à jour les états des jours occupées par l'absence.
	 * 
	 * Le type d'absence concernée est : - {@link EOAbsenceGepeto} : les congés
	 * légaux - {@link EOOccupation} : les occupations non refusées -
	 * 
	 * @param absence
	 */
	private void remplirStatutJourOccupation(I_Absence absence) {
		if (absence instanceof EOAbsenceGepeto ||
				((absence instanceof EOOccupation && ((EOOccupation) absence).isRefusee() == false))) {
			NSTimestamp dateDebutTS = absence.dateDebut();
			NSTimestamp dateFinTS = absence.dateFin();
			NSArray lesJoursOccupes = lesJours(dateDebutTS, dateFinTS);

			Jour premierJour = (Jour) lesJoursOccupes.objectAtIndex(0);
			Jour dernierJour = (Jour) lesJoursOccupes.lastObject();
			GregorianCalendar leDebutGC = new GregorianCalendar();
			GregorianCalendar laFinGC = new GregorianCalendar();
			Enumeration enumLesJoursOccupes = lesJoursOccupes.objectEnumerator();

			// les etats selon si conges legal ou pas
			int etatConge = Jour.STATUS_CONGE;
			int etatCongeAM = Jour.STATUS_CONGE_AM;
			int etatCongePM = Jour.STATUS_CONGE_PM;

			if (absence.isCongeLegal()) {
				etatConge = Jour.STATUS_CONGE_LEGAL;
				etatCongeAM = Jour.STATUS_CONGE_LEGAL_AM;
				etatCongePM = Jour.STATUS_CONGE_LEGAL_PM;
			}

			// TODO Recalcul du quota d'heures dues si calcul automatique
			leDebutGC.setTime(dateDebutTS);
			laFinGC.setTime(dateFinTS);

			while (enumLesJoursOccupes.hasMoreElements()) {
				Jour unJour = (Jour) enumLesJoursOccupes.nextElement();

				// etat de validation / suppression
				if (absence instanceof EOOccupation) {
					EOOccupation occupation = (EOOccupation) absence;
					if (occupation.isEnCoursDeValidation()) {
						unJour.addStatut(Jour.STATUS_EN_COURS_DE_VALIDATION);
					} else if (occupation.isEnCoursDeSuppression()) {
						unJour.addStatut(Jour.STATUS_EN_COURS_DE_SUPPRESSION);
					}
				}

				if (unJour.equals(premierJour) == false &&
						unJour.equals(dernierJour) == false) {
					unJour.addStatut(etatConge);
				}
			}
			if (premierJour.equals(dernierJour)) {
				if (leDebutGC.get(GregorianCalendar.AM_PM) == GregorianCalendar.AM) {
					if (laFinGC.get(GregorianCalendar.AM_PM) == GregorianCalendar.AM) {
						if (premierJour.isCongePM()) {
							premierJour.addStatut(etatConge);
						} else {
							premierJour.addStatut(etatCongeAM);
						}
					} else {
						premierJour.addStatut(etatConge);
					}
				} else {
					if (premierJour.isCongeAM()) {
						premierJour.addStatut(etatConge);
					} else {
						premierJour.addStatut(etatCongePM);
					}
				}
			} else {
				if (leDebutGC.get(GregorianCalendar.AM_PM) == GregorianCalendar.PM) {
					if (premierJour.isCongeAM()) {
						premierJour.addStatut(etatConge);
					} else {
						premierJour.addStatut(etatCongePM);
					}
				} else {
					premierJour.addStatut(etatConge);
				}
				if (laFinGC.get(GregorianCalendar.AM_PM) == GregorianCalendar.AM) {
					if (dernierJour.isCongePM()) {
						dernierJour.addStatut(etatConge);
					} else {
						dernierJour.addStatut(etatCongeAM);
					}
				} else {
					dernierJour.addStatut(etatConge);
				}
			}
		}
	}

	/**
	 * Ajouter une absence
	 * 
	 * @param uneOccupation
	 */
	public void addAbsenceOccupation(I_Absence absence) {
		remplirStatutJourOccupation(absence);
		absences().addObject((I_Absence) absence);
	}

	/**
	 * 
	 * @param uneAbsence
	 */
	public void removeAbsence(I_Absence uneAbsence) {
		NSTimestamp dateDebutTS = uneAbsence.dateDebut();
		NSTimestamp dateFinTS = uneAbsence.dateFin();
		NSArray lesJoursOccupes = lesJours(dateDebutTS, dateFinTS);
		Jour premierJour = (Jour) lesJoursOccupes.objectAtIndex(0);
		Jour dernierJour = (Jour) lesJoursOccupes.lastObject();
		GregorianCalendar leDebutGC = new GregorianCalendar();
		GregorianCalendar laFinGC = new GregorianCalendar();
		Enumeration enumLesJoursOccupes = lesJoursOccupes.objectEnumerator();

		// TODO Recalcul du quota d'heures dues si calcul automatique
		leDebutGC.setTime(dateDebutTS);
		laFinGC.setTime(dateFinTS);

		while (enumLesJoursOccupes.hasMoreElements()) {
			Jour unJour = (Jour) enumLesJoursOccupes.nextElement();
			if ((unJour.equals(premierJour) == false) && (unJour.equals(dernierJour) == false) && (unJour.isCongeJourneeComplete() == true)) {
				unJour.setStatut(Jour.STATUS_TRAVAILLE);
			}
		}
		if (leDebutGC.get(GregorianCalendar.AM_PM) == GregorianCalendar.PM) {
			if (premierJour.isCongeJourneeComplete() == true) {
				premierJour.setStatut(Jour.STATUS_CONGE_AM);
			} else {
				premierJour.setStatut(Jour.STATUS_TRAVAILLE);
			}
		} else {
			premierJour.setStatut(Jour.STATUS_TRAVAILLE);
		}
		if (laFinGC.get(GregorianCalendar.AM_PM) == GregorianCalendar.AM) {
			if (dernierJour.isCongeJourneeComplete() == true) {
				dernierJour.setStatut(Jour.STATUS_CONGE_PM);
			} else {
				dernierJour.setStatut(Jour.STATUS_TRAVAILLE);
			}
		} else {
			dernierJour.setStatut(Jour.STATUS_TRAVAILLE);
		}
		absences().removeIdenticalObject(uneAbsence);
	}

	/**
	 * retourne les absences qui incluent le jour date
	 * 
	 * @param date
	 * @return
	 */
	public NSArray absencesForDay(NSTimestamp date) {
		EOQualifier qualifier = EOQualifier.qualifierWithQualifierFormat("(dateDebut <= %@ OR dateDebut <= %@) AND (dateFin >= %@ OR dateFin >= %@)", new NSArray(
				new Object[] { TimeCtrl.dateToMinuit(date), TimeCtrl.dateToMinuit(date.timestampByAddingGregorianUnits(0, 0, 1, 0, 0, 0)), TimeCtrl.dateToMinuit(date),
						TimeCtrl.dateToMinuit(date.timestampByAddingGregorianUnits(0, 0, 1, 0, 0, 0)) }));
		return EOQualifier.filteredArrayWithQualifier(absences(), qualifier);
	}

	public String dureeJour() {
		return calcul.dureeJour();
	}

	public void setDureeJour(String value) {
		calcul.setDureeJour(value);
	}

	/**
	 * Indique si toutes les semaines ACTIVES du planning poss�de un horaire
	 * associe
	 */
	public boolean isToutesLesSemainesActivesAssociees() {
		boolean result = true;

		for (int i = 0; i < semaines().count(); i++) {
			Semaine uneSemaine = (Semaine) semaines().objectAtIndex(i);
			if (uneSemaine.isActive() && uneSemaine.horaire() == null) {
				result = false;
				break;
			}
		}

		return result;
	}

	/**
	 * La liste de <code>Semaine</code> actives pourlequelles un horaire n'a pas
	 * ete associ�
	 * 
	 * @return
	 */
	public NSArray semainesActivitesNonAssociees() {
		NSArray listSemaine = new NSArray();
		for (int i = 0; i < semaines().count(); i++) {
			Semaine uneSemaine = (Semaine) semaines().objectAtIndex(i);
			if (uneSemaine.isActive() && uneSemaine.horaire() == null) {
				listSemaine = listSemaine.arrayByAddingObject(uneSemaine);
			}
		}
		return listSemaine;
	}

	/**
	 * Affichage du planning dans la console
	 */
	public String toString() {
		StringBuffer str = new StringBuffer();

		str.append("type=[").append(type());
		str.append("], agent=[").append(affectationAnnuelle().individu().nomComplet());
		str.append("], service=[").append(affectationAnnuelle().structure().display()).append("]");
		str.append("], annee=[").append(affectationAnnuelle().annee()).append("]");

		return str.toString();
	}

	private Boolean _isDisabled;

	/**
	 * Le planning n'est pas modifiable selon plusieurs critere : - son etat - la
	 * personne qui le consulte
	 * 
	 * @return
	 */
	public boolean isDisabled() {
		if (_isDisabled == null) {
			// le planning est toujours modifiable par l'administrateur et le DRH
			if (connectedUserInfo.isAdmin() || connectedUserInfo.isDrh()) {
				_isDisabled = Boolean.FALSE;
			} else {
				// on desactive le planning sous plusieurs conditions
				boolean isEnabled = false;
				// inactif sur les annees universitaires passees
				if (isPreviousAnneeUniv() == false) {
					// inactif s'il n'est pas le proprietaire
					if (isConnectedOwner()) {
						isEnabled = true;
					}
					// inactif s'il n'est pas delegue
					if (!isEnabled && connectedUserInfo.isDeleguePlanning(affectationAnnuelle())) {
						isEnabled = true;
					}
				}
				_isDisabled = new Boolean(!isEnabled);
			}
		}
		return _isDisabled.booleanValue();
	}

	// OBJETS EN CACHE

	private NSArray lesTypesHorairesNormaux;
	private NSArray lesTypesHorairesHorsNormes;

	public NSArray lesTypesHorairesNormaux() {
		if (lesTypesHorairesNormaux == null) {
			lesTypesHorairesNormaux = EOTypeHoraire.findAllTypeHoraire(edc(), true);
		}
		return lesTypesHorairesNormaux;
	}

	public NSArray lesTypesHorairesHorsNormes() {
		if (lesTypesHorairesHorsNormes == null)
			lesTypesHorairesHorsNormes = EOTypeHoraire.findAllTypeHoraire(edc(), false);
		return lesTypesHorairesHorsNormes;
	}

	// attribut du planning par rapport a la personne connectee

	private Boolean _isConnectedOwner;

	/**
	 * La personne connectee est-elle le proprietaire
	 */
	public boolean isConnectedOwner() {
		if (_isConnectedOwner == null) {
			_isConnectedOwner = new Boolean(
					affectationAnnuelle().individu().persId().intValue() == connectedUserInfo.persId().intValue());
		}
		return _isConnectedOwner.booleanValue();
	}

	private Boolean _isPreviousAnneeUniv;

	/**
	 * Le planning concerne-t-il une annee universitaire antiereure a celle en
	 * cours
	 */
	public boolean isPreviousAnneeUniv() {
		if (_isPreviousAnneeUniv == null) {
			_isPreviousAnneeUniv = new Boolean(
					DateCtrlConges.isBefore(affectationAnnuelle().dateFinAnnee(), dateDebutCurrentAnneeUniv));
		}
		return _isPreviousAnneeUniv.booleanValue();
	}

	/**
	 * TODO a supprimer, utiliser celle PlanningHtml directement depuis les
	 * interfaces
	 */
	public String textCssHoraire() {
		return html.textCssHoraire();
	}

	public String textJScriptCheckboxHoraire() {
		return html.textJScriptCheckboxHoraire();
	}

	public String textJScriptCheckboxOccupation() {
		return html.textJScriptCheckboxOccupation();
	}

	public String textJScriptOnloadBodyPlanning(boolean showHoraire, boolean showOccupation) {
		return html.textJScriptOnloadBodyPlanning(showHoraire, showOccupation);
	}

	public PlanningCalcul getCalcul() {
		return calcul;
	}

	// les etats du planning
	// connaitre l'etat "principal" d'un planning
	// 2 etats peuvent signifier pour l'utilisateur 1 seul et unique (ex visa)

	public boolean isValide() {
		return affectationAnnuelle().isPlanningValide();
	}

	public boolean isNonValide() {
		return affectationAnnuelle().isPlanningNonValide();
	}

	public boolean isEnCoursDeValidation() {
		return affectationAnnuelle().isPlanningEnCoursDeValidation();
	}

	public boolean isEnCoursDeModification() {
		return affectationAnnuelle().isPlanningEnCoursDeModification();
	}

	// controle si la saisie du réel a commencé a être faite

	private Boolean _isPlanningReelContientHoraires;

	/**
	 * Indique si le planning reel de l'agent contient au moins 1 horaire associé
	 * 
	 * @return
	 */
	public boolean isPlanningReelContientHoraires() {
		if (_isPlanningReelContientHoraires == null) {
			boolean isContientHoraire = false;
			NSArray periodes = affectationAnnuelle().periodes();
			int i = 0;
			while (!isContientHoraire && i < periodes.count()) {
				EOPeriodeAffectationAnnuelle periode = (EOPeriodeAffectationAnnuelle) periodes.objectAtIndex(i);

				if (periode.planningHebdomadaires(
							CRIDataBus.newCondition(EOPlanningHebdomadaire.HORAIRE_KEY + "<> nil", null)).count() > 0) {
					isContientHoraire = true;
				}

				i++;
			}
			_isPlanningReelContientHoraires = new Boolean(isContientHoraire);
		}
		return _isPlanningReelContientHoraires.booleanValue();
	}

	/**
	 * Indique si l'horaire associé à une semaine peut être modifié ou non.
	 * accessibilite lien numero de semaine sur le planning reel
	 * 
	 * @return
	 */
	public boolean isSemaineModifiable(Semaine semaine, CngUserInfo connectedUser) {
		boolean isSemaineModifiable = false;

		// planning reel
		if (isPReel()) {
			if (isEnCoursDeModification() &&
					connectedUser.isAllowedModifyPlanning(affectationAnnuelle()) &&
					semaine.isActive() &&
					DateCtrl.isAfterEq(semaine.debut(), DateCtrlConges.toLundi(DateCtrlConges.now()))) {
				isSemaineModifiable = true;
			}
		}

		// planning de test
		if (!isSemaineModifiable) {
			if (isPTest()) {
				if (connectedUser.isAllowedModifyPlanning(affectationAnnuelle()) &&
						semaine.isActive()) {
					isSemaineModifiable = true;
				}
			}
		}

		// planning previsionnel
		// Pour permettre l'association d'un horaire a une semaine
		// sur le planning previsionnel, il faut que:
		// - individu connecte a le droit de modifier le planning
		// - Statut planning = NON VALIDE
		// - Semaine concernee active
		// - Il existe au moins un horaire associable
		if (!isSemaineModifiable) {

			if (isPPrev()) {

				if (connectedUser.isAllowedModifyPlanning(affectationAnnuelle()) &&
						isNonValide() &&
						semaine.isActive() &&
						affectationAnnuelle().horaires().count() > 0) {
					isSemaineModifiable = true;
				}

				// controles complémentaires pour interdire la modification
				// du planning previsionnel dans le but de changer le planning
				// réel pour les semaines passées
				boolean isBlocageSemainePassee = false;
				if (isSemaineModifiable) {

					// FIXME c'est vraiment pas propre ...
					// le planning réel doit être chargé au préalable afin que les
					// objets Semaine soient correctement affectés
					if (isLoadedPlanning(REEL) == false) {
						setType(REEL);
						setType(PREV);
					}

					// on ne va conserver que les plannings hebdo qui ont une semaine
					// affectée
					// i.e. ceux associés à des semaines actives
					EOQualifier qual = CRIDataBus.newCondition(
							EOPlanningHebdomadaire.HAS_SEMAINE_KEY + "=%@", new NSArray(Boolean.TRUE));

					NSArray planningsReel = affectationAnnuelle().planningsHebdo(
							REEL, semaine.debut());
					planningsReel = EOQualifier.filteredArrayWithQualifier(planningsReel, qual);

					NSArray planningsPrev = affectationAnnuelle().planningsHebdo(
							PREV, semaine.debut());
					planningsPrev = EOQualifier.filteredArrayWithQualifier(planningsPrev, qual);

					// interdire la modification s'il existe un horaire associé dans le
					// réel
					// et que la date est déjà passée
					// ajout : s'il y a des semaines à cheval, alors on interdit si
					// celle ci sont présentes dans le réel et le prévisionnel, et
					// qu'elles
					// ont toutes un horaire associé
					if (planningsReel.count() > 0 &&
							planningsReel.count() == planningsPrev.count() &&
							EOAffectationAnnuelle.nbPlanningHebdoAssocies(planningsReel) == planningsPrev.count() &&
							DateCtrl.isBefore(semaine.debut(), DateCtrlConges.toLundi(DateCtrlConges.now()))) {
						isSemaineModifiable = false;
						isBlocageSemainePassee = true;
					}
				}

				// l'administrateur peut toujours intervenir pour les cas bloquants
				// comme par exemple si dépassement de congés
				// (sauf pour lui même ...)
				if (!isSemaineModifiable &&
						isBlocageSemainePassee) {
					if (connectedUser.isAdmin() &&
							!isConnectedOwner()) {
						isSemaineModifiable = true;
					}
				}

			}
		}

		return isSemaineModifiable;
	}

}