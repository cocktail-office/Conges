package fr.univlr.cri.conges.objects;

import java.util.Enumeration;

import com.webobjects.eocontrol.EOCustomObject;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.planning.EOHoraire;
import fr.univlr.cri.conges.eos.modele.planning.EOOccupation;
import fr.univlr.cri.conges.eos.modele.planning.EOPlanningHebdomadaire;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.util.wo5.DateCtrl;

/*
 * Created on 21 juin 04
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author egeze
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Semaine extends EOCustomObject {

	private EOPlanningHebdomadaire planningHebdo;
	private NSMutableArray jours;
	private Mois mois;
	private String numero;
	private String etat;
	private EOHoraire horaire;
	private NSTimestamp debut;
	private NSMutableArray occupations;
	private Boolean isActive;
	private String idAssociation;

	// les classes css associees au semaines
	public final static String classeCssSemaineActive = "divSemaine";
	private final static String classeCssSemaineInactive = "divSemaineInactive";
	private final static String classeCssSemaineNoHoraire = "divSemaineNoHoraire";

	// noms des methodes appeles via NSKeyValueCoding
	public final static String CHECK_BONUS_SAMEDI_MATIN_KEY = "checkBonusSamediMatin";
	public final static String CHECK_BONUS_SAMEDI_APRES_MIDI_KEY = "checkBonusSamediApresMidi";
	public final static String DETERMINER_JOUR_CLIQUABLE_HTML_KEY = "determinerJourCliquableHTML";

	public Semaine() {
		super();
		isActive = Boolean.FALSE;
	}

	public String numero() {
		return numero;
	}

	/**
	 * L'identifiant dans la semaine d'association. Deux semaines de meme numéro
	 * contigues dans le planning sont les mêmes (à cheval sur un mois).
	 * 
	 * @return
	 */
	public String idAssociation() {
		if (idAssociation == null) {

			// on met le numéro de l'année pour différencier
			// excepté pour les semaines 52 voir 53 qui sont toujours
			// celles candidates à etre à cheval sur 2 années
			idAssociation = numero();

			int num = Integer.parseInt(numero());
			if (num != 52 && num != 53) {
				idAssociation += "_" + DateCtrlConges.dateToString(mois().firstDay(), "%Y");
			}

		}

		return idAssociation;
	}

	/**
	 */
	public void setNumero(String numWeekStr) {
		numero = numWeekStr;
	}

	/**
	 */
	public void setNumero(int numWeek) {
		String leNumero = String.valueOf(numWeek);
		if (numWeek < 10)
			leNumero = "0" + leNumero;
		setNumero(leNumero);
	}

	/**
	 */
	public void setMois(Mois unMois) {
		mois = unMois;
	}

	public Mois mois() {
		return mois;
	}

	/**
	 * La classe de la semaine selon son etat : active ou pas et horaires lors de
	 * l'initialisation de la page
	 */
	public String classeCssShowHoraire() {
		String classe = null;
		if (isActive()) {
			if (horaire() != null)
				classe = "H_" + horaire().oid().intValue() + "_";
		} else
			classe = classeCssSemaineInactive;
		return classe;
	}

	/**
	 * La classe de la semaine selon son etat : active ou pas lors de
	 * l'initialisation de la page en masquat les horaires associes
	 */
	public String classeCssHideHoraire() {
		String classe = null;
		if (isActive()) {
			if (horaire() != null)
				classe = classeCssSemaineActive;
		} else
			classe = classeCssSemaineInactive;
		return classe;
	}

	/**
	 * taille de la semaine en pixels
	 */
	public String styleCssSize() {
		StringBuffer style = new StringBuffer();
		// les pixel pour le decalage du calque et sa hauteur
		int offset = jours().count() * 15;
		// la hauteur est 15x le nombre de jours
		style.append("height:").append(offset).append("px;");
		return style.toString();
	}

	/**
	 * positionnement et taille de la semaine calque (horaire non associe)
	 */
	public String styleCssSizeAndOffset() {
		StringBuffer style = new StringBuffer();
		// les pixel pour le decalage du calque et sa hauteur
		int offset = jours().count() * 15;
		// on recule du nombre de jour de la semaine
		style.append("top:-").append(offset).append("px;");
		// la hauteur est 15x le nombre de jours
		style.append("height:").append(offset).append("px;");
		// si moins de 7 jours et qu'elle contient le lundi
		// alors on positionne la div dans l'image differement
		if (jours().count() < 7) {
			if (((NSArray) jours().valueForKey("libelle")).containsObject("L"))
				style.append("background-position:top;");
		}
		return style.toString();
	}

	/**
	 * La css a un fond commun qu'on va pas remettre a chaque fois dans la balise
	 * style
	 */
	public String classeCssNoHoraire() {
		return classeCssSemaineNoHoraire;
	}

	/**
	 * L'id css de la semaine qui correspond a l'horaire associe On prends la cle
	 * primaire de la table HORAIRE precede du numero de la semaine
	 */
	public String idCss() {
		String idCss = numero();
		if (horaire() != null)
			idCss += "-H_" + horaire().oid().intValue() + "_";
		return idCss;
	}

	/**
	 * @param unJour
	 */
	public void add(Jour unJour) {
		jours().addObject(unJour);
	}

	public void add(NSArray array) {
		jours().addObjectsFromArray(array);
	}

	public void remove(NSArray array) {
		jours().removeObjectsInArray(array);
	}

	public void replace(Jour value) {
		for (int i = 0; i < jours().count(); i++) {
			Jour unJour = (Jour) jours().objectAtIndex(i);
			if (unJour.numero().equals(value.numero())) {
				jours().replaceObjectAtIndex(value, i);
				break;
			}
		}

	}

	public NSMutableArray jours() {
		if (jours == null) {
			jours = new NSMutableArray();
		}
		return jours;
	}

	/**
	 * @param unEtat
	 */
	public void setEtat(String unEtat) {
		etat = unEtat;
	}

	public String etat() {
		return etat;
	}

	public boolean isActive() {
		return isActive.booleanValue();
	}

	public void activer() {
		isActive = Boolean.TRUE;
	}

	public boolean isInactive() {
		return !isActive();
	}

	public Number quotite() {
		return planningHebdo().periodeAffectationAnnuelle().quotite();
	}

	public NSTimestamp firstDay() {
		NSTimestamp laDate = null;
		if (jours() != null && jours().count() > 0) {
			Jour leJour = (Jour) jours().objectAtIndex(0);
			laDate = leJour.date();
		}
		return laDate;
	}

	public NSTimestamp lastDay() {
		NSTimestamp laDate = null;
		if (jours() != null && jours().count() > 0) {
			Jour leJour = (Jour) jours().lastObject();
			laDate = leJour.date();
		}
		return laDate;
	}

	/**
	 * @param horaire
	 */
	public void setHoraire(EOHoraire unHoraire) {
		NSArray lesJours = jours();
		Enumeration enumLesJours = lesJours.objectEnumerator();
		NSArray durees = null;
		NSArray dureesAM = null;
		NSArray dureesPM = null;
		String laDuree = "";

		if (unHoraire != null) {
			durees = unHoraire.dureesJournalieres();
			dureesAM = unHoraire.dureesJournalieresAM();
			dureesPM = unHoraire.dureesJournalieresPM();
		}

		while (enumLesJours.hasMoreElements()) {

			Jour unJour = (Jour) enumLesJours.nextElement();

			if (unHoraire != null &&
					planningHebdo() != null &&
					DateCtrl.isAfterEq(unJour.date(), planningHebdo().periodeAffectationAnnuelle().dateDebut()) &&
					DateCtrl.isBeforeEq(unJour.date(), planningHebdo().periodeAffectationAnnuelle().dateFin()) &&
					unJour.isSansStatus() == false &&
					unJour.isFerie() == false) {
				int index = 0;
				if (unJour.isLundi()) {
					index = 0;
				} else if (unJour.isMardi()) {
					index = 1;
				} else if (unJour.isMercredi()) {
					index = 2;
				} else if (unJour.isJeudi()) {
					index = 3;
				} else if (unJour.isVendredi()) {
					index = 4;
				} else if (unJour.isSamedi()) {
					index = 5;
				} else if (unJour.isDimanche()) {
					index = 6;
				}
				laDuree = (String) durees.objectAtIndex(index);
				String laDureeAM = (String) dureesAM.objectAtIndex(index);
				String laDureePM = (String) dureesPM.objectAtIndex(index);

				// XXX mega bidouille DRH : le 16 mai 2005 est compte � 7h00 a 100%
				NSTimestamp dateBidouille = DateCtrl.stringToDate("16/05/2005");
				if (DateCtrl.isAfterEq(dateBidouille, unJour.date()) && DateCtrl.isBeforeEq(dateBidouille, unJour.date())) {
					// retrouver la quotite de la periode pour cette semaine
					int quotite = planningHebdo().periodeAffectationAnnuelle().quotite().intValue();
					int duree7h00 = 7 * 60 * quotite / 100;
					unJour.setDureeTravailleeEnMinutes(duree7h00);
					unJour.setDureeAMTravailleeEnMinutes(duree7h00 / 2);
					unJour.setDureePMTravailleeEnMinutes(duree7h00 / 2);
					unJour.setDuree(Integer.toString(duree7h00));
					unJour.setDureeAM(Integer.toString(duree7h00 / 2));
					unJour.setDureePM(Integer.toString(duree7h00 / 2));
				} else {
					unJour.setDuree(laDuree);
					unJour.setDureeAM(laDureeAM);
					unJour.setDureePM(laDureePM);
					if (StringCtrl.isEmpty(laDureeAM)) {
						unJour.setDureeAMTravailleeEnMinutes(0);
					} else {
						unJour.setDureeAMTravailleeEnMinutes(Integer.valueOf(laDureeAM).intValue());
					}
					if (StringCtrl.isEmpty(laDureePM)) {
						unJour.setDureePMTravailleeEnMinutes(0);
					} else {
						unJour.setDureePMTravailleeEnMinutes(Integer.valueOf(laDureePM).intValue());
					}
					if (StringCtrl.isEmpty(laDuree)) {
						unJour.setDureeTravailleeEnMinutes(0);
					} else {
						unJour.setDureeTravailleeEnMinutes(Integer.valueOf(laDuree).intValue());
					}
				}
			} else {
				if (StringCtrl.isEmpty(laDuree)) {
					unJour.setDuree("");
					unJour.setDureeAM("");
					unJour.setDureePM("");
					unJour.setDureeTravailleeEnMinutes(0);
					unJour.setDureeAMTravailleeEnMinutes(0);
					unJour.setDureePMTravailleeEnMinutes(0);
				}
			}
		}

		horaire = unHoraire;

		if (planningHebdo() != null) {

			EOHoraire prevHoraire = planningHebdo().horaire();

			if (prevHoraire != horaire) {
				// planningHebdo().setHoraire(horaire);
				// la addToBothSide pour ne pas avoir d'erreur oracle
				// si suppression de l'horaire immediate
				planningHebdo().addObjectToBothSidesOfRelationshipWithKey(
						horaire, EOPlanningHebdomadaire.HORAIRE_KEY);
				// mise à jour de la date de dernière modification ...
				planningHebdo().setDModification(DateCtrlConges.now());
			}
		}

	}

	public EOHoraire horaire() {
		return horaire;
	}

	public boolean isSemaineHaute() {
		boolean isSemaineHaute = false;

		if (horaire() != null) {
			isSemaineHaute = horaire.isSemaineHaute();
		}
		return isSemaineHaute;
	}

	public boolean isSemaineBasse() {
		boolean isSemaineBasse = false;

		if (horaire() != null) {
			isSemaineBasse = !(horaire.isSemaineHaute());
		}
		return isSemaineBasse;
	}

	/**
	 * @return
	 */
	public NSTimestamp debut() {
		return debut;
	}

	/**
	 * @param timestamp
	 */
	public void setDebut(NSTimestamp timestamp) {
		debut = timestamp;
	}

	/**
	 * @param unPlanningHebdo
	 */
	public void setPlanningHebdo(EOPlanningHebdomadaire unPlanningHebdo) {
		planningHebdo = unPlanningHebdo;
		planningHebdo.setSemaine(this);
	}

	/**
	 * @return
	 */
	public EOPlanningHebdomadaire planningHebdo() {
		return planningHebdo;
	}

	/**
	 * @param uneOccupation
	 */
	public void addOccupation(EOOccupation uneOccupation) {
		if (occupations == null)
			occupations = new NSMutableArray();
		occupations.addObject(uneOccupation);
	}

	public int dureeTravailleeEnMinutes() {
		Number duree = (Number) (jours().valueForKeyPath("@sum.dureeTravailleeEnMinutes"));

		return duree.intValue();
	}

	public NSTimestamp fin() {
		return debut().timestampByAddingGregorianUnits(0, 0, 6, 0, 0, 0);
	}

	public String libelleLong() {
		String libelleLong = "";
		libelleLong += numero() + " (" + DateCtrl.dateToString(debut()) + ")";

		return libelleLong;
	}

	/**
	 * @return
	 */
	public int minutesTravaillees() {
		int minutesTravaillees = 0;
		NSArray lesJours = jours();
		Enumeration enumLesJours = lesJours.objectEnumerator();

		while (enumLesJours.hasMoreElements()) {
			Jour unJour = (Jour) enumLesJours.nextElement();
			if (unJour.isTravaille()) {
				minutesTravaillees += unJour.dureeTravailleeEnMinutes();
			} else if (unJour.isCongeAM() || unJour.isTravaillePM()) {
				minutesTravaillees += unJour.dureePMTravailleeEnMinutes();
			} else if (unJour.isCongePM() || unJour.isTravailleAM()) {
				minutesTravaillees += unJour.dureeAMTravailleeEnMinutes();
			}
		}

		return minutesTravaillees;
	}

	/*
	 * public String toString() { String string = "semaine n�" + numero() +
	 * " horaire:" + (horaire == null ? "aucun" : horaire().nom()) + " jours:" +
	 * (jours() == null ? "aucun" : ""+jours().count()) + " debut:" + (firstDay()
	 * == null ? "null" : DateCtrl.dateToString(firstDay())); return string; }
	 */

	public boolean equals(Semaine semaine) {
		return libelleLong().equals(semaine.libelleLong());
	}

	/**
	 * sur le planning on affiche le lien associe horaire par defaut sur le jeudi
	 * si pas de jeudi, alors on le met sur le premier jour de la semaine.
	 * 
	 * Attention, cette methode n'est appelee que par
	 * <code>NSKeyValueCoding</code>
	 */
	public void determinerJourCliquableHTML() {
		EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(
				"isJeudi = %@", new NSArray(new Boolean(true)));
		NSArray records = EOQualifier.filteredArrayWithQualifier(jours(), qual);
		if (records.count() > 0) {
			Jour leJeudi = (Jour) records.lastObject();
			leJeudi.setNumeroSemaine(numero());
			replace(leJeudi);
		} else {
			if (jours().count() > 0) {
				Jour lePremierJour = (Jour) jours().objectAtIndex(0);
				lePremierJour.setNumeroSemaine(numero());
				replace(lePremierJour);
			}
		}
	}

	// gestion de la bonification du samedi matin

	/**
	 * Le samedi matin, la bonification ne s'applique que si les 10 demi journees
	 * sont travaillees.
	 * 
	 * Cette methode effectue l'imputation ou pas du bonus
	 */
	public void checkBonusSamediMatin() {
		// on ne fait le traitement que si le samedi matin est travaille
		if (horaire() != null && horaire().isTravailleSamediMatin()) {
			for (int i = 0; i < jours().count(); i++) {
				Jour unJour = (Jour) jours().objectAtIndex(i);
				if (unJour.isSamedi() &&
						unJour.dureeAMTravailleeEnMinutes() > 0) {
					HoraireHebdomadaire horaireHedbo = new HoraireHebdomadaire();
					horaireHedbo.setHoraire(horaire());
					horaireHedbo.recalculerTotaux();
					// retrouver la duree initiale de l'horaire du samedi
					HoraireJournalier horairJourn = (HoraireJournalier) horaireHedbo.horairesJournaliers().objectAtIndex(5);
					int dureeAm = Integer.valueOf(horairJourn.toStringDureeAM()).intValue();
					int dureePm = Integer.valueOf(horairJourn.toStringDureePM()).intValue();
					int dureeAmSansBonus = dureeAm;
					int dureeAmAvecBonus = dureeAm + horaireHedbo.getBonusSamediMatin();
					if (nbDemiJourTravaillee() < 11) {
						// retrouver la valeur du bonus pour le soustraire
						unJour.setDureeTravailleeEnMinutes(dureeAmSansBonus + dureePm);
						unJour.setDureeAMTravailleeEnMinutes(dureeAmSansBonus);
						unJour.setDuree(Integer.toString(dureeAmSansBonus + dureePm));
						unJour.setDureeAM(Integer.toString(dureeAmSansBonus));
					} else {
						unJour.setDureeTravailleeEnMinutes(dureeAmAvecBonus + dureePm);
						unJour.setDureeAMTravailleeEnMinutes(dureeAmAvecBonus);
						unJour.setDuree(Integer.toString(dureeAmAvecBonus + dureePm));
						unJour.setDureeAM(Integer.toString(dureeAmAvecBonus));
					}
				}
			}
		}
	}

	// gestion de la bonification du samedi apres midi

	/**
	 * Le samedi après midi, la bonification si l'après midi est travaillé
	 * 
	 * Cette methode effectue l'imputation ou pas du bonus
	 */
	public void checkBonusSamediApresMidi() {
		// on ne fait le traitement que si le samedi matin est travaille
		if (horaire() != null && horaire().isTravailleSamediApresMidi()) {
			for (int i = 0; i < jours().count(); i++) {
				Jour unJour = (Jour) jours().objectAtIndex(i);
				if (unJour.isSamedi() &&
						unJour.dureePMTravailleeEnMinutes() > 0 &&
						isTravailleSamediApresMidi(unJour)) {
					HoraireHebdomadaire horaireHedbo = new HoraireHebdomadaire();
					horaireHedbo.setHoraire(horaire());
					horaireHedbo.recalculerTotaux();
					unJour.setDureeTravailleeEnMinutes(unJour.dureeTravailleeEnMinutes() + horaireHedbo.getBonusSamediApresMidi());
					unJour.setDureePMTravailleeEnMinutes(unJour.dureePMTravailleeEnMinutes() + horaireHedbo.getBonusSamediApresMidi());
					unJour.setDuree(Integer.toString(unJour.dureeTravailleeEnMinutes()));
					unJour.setDureePM(Integer.toString(unJour.dureePMTravailleeEnMinutes()));
				}
			}
		}
	}

	private Semaine semainePrecedente;

	/**
	 * Memorise la <code>Semaine</code> precedente. Cette derniere sera utilisee
	 * pour trouver le nombre total de demi journee travaillees du lundi au
	 * vendredi.
	 */
	public void setSemainePrecedente(Semaine value) {
		semainePrecedente = value;
	}

	/**
	 * Le nombre total de demi journee travaillees dans la semaine, du lundi au
	 * vendredi. Si la semaine n'est pas complete, alors on regarde les jours
	 * manquants sur la semaine precedente. EDIT : le samedi matin doit lui aussi
	 * être travaillé
	 * 
	 * Cette methode est toujours appelee un samedi.
	 */
	private int nbDemiJourTravaillee() {
		int total = 0;
		int endIndex = 6;

		Jour jourLun = (Jour) jours().objectAtIndex(0);
		if (!jourLun.isLundi()) {
			// pas de lundi dans la semaine, on va chercher le chercher dans la
			// semaine precedente
			if (semainePrecedente != null && semainePrecedente.jours().count() > 0) {
				jourLun = (Jour) semainePrecedente.jours().objectAtIndex(0);
				total += nbDemiJourneePourJour(jourLun);
			} else {
				// pas de semaine precedente, arret
				return 0;
			}

			Jour jourMar = (Jour) jours().objectAtIndex(0);
			if (!jourMar.isMardi()) {
				if (semainePrecedente.jours().count() > 1) {
					jourMar = (Jour) semainePrecedente.jours().objectAtIndex(1);
					total += nbDemiJourneePourJour(jourMar);
				} else {
					// pas trouvable dans la semaine precedente, arrete
					return 0;
				}

				Jour jourMer = (Jour) jours().objectAtIndex(0);
				if (!jourMer.isMercredi()) {
					if (semainePrecedente.jours().count() > 2) {
						jourMer = (Jour) semainePrecedente.jours().objectAtIndex(2);
						total += nbDemiJourneePourJour(jourMer);
					} else {
						// pas trouvable dans la semaine precedente, arrete
						return 0;
					}

					Jour jourJeu = (Jour) jours().objectAtIndex(0);
					if (!jourJeu.isJeudi()) {
						if (semainePrecedente.jours().count() > 3) {
							jourJeu = (Jour) semainePrecedente.jours().objectAtIndex(3);
							total += nbDemiJourneePourJour(jourJeu);
						} else {
							// pas trouvable dans la semaine precedente, arrete
							return 0;
						}

						Jour jourVen = (Jour) jours().objectAtIndex(0);
						if (!jourVen.isVendredi()) {
							if (semainePrecedente.jours().count() > 4) {
								jourVen = (Jour) semainePrecedente.jours().objectAtIndex(4);
								total += nbDemiJourneePourJour(jourVen);
							} else {
								// pas trouvable dans la semaine precedente, arrete
								return 0;
							}

							// cas particulier si c'est un samedi le premier
							// jour de la semaine, on force endIndex a 0
							// (pas de traitement de la semaine en cours ..)
							Jour jourSam = (Jour) jours().objectAtIndex(0);
							if (jourSam.isSamedi()) {
								endIndex = 1;
							}

						} else {
							// c'est bien le vendredi, on passe au traitement de la semaine
							// actuelle
							endIndex = 2;
						}
					} else {
						// c'est bien le jeudi, on passe au traitement de la semaine
						// actuelle
						endIndex = 3;
					}
				} else {
					// c'est bien le mercredi, on passe au traitement de la semaine
					// actuelle
					endIndex = 4;
				}
			} else {
				// c'est bien le mardi, on passe au traitement de la semaine actuelle
				endIndex = 5;
			}
		}

		// traitement pour la semaine en cours
		for (int i = 0; i < endIndex; i++) {
			Jour jourItem = (Jour) jours().objectAtIndex(i);
			if (jourItem.isSamedi() == false) {
				total += nbDemiJourneePourJour(jourItem);
			} else {
				// le samedi on ne regarde que le matin
				if (isTravailleSamediMatin(jourItem)) {
					total += 1;
				}
			}
		}

		return total;
	}

	/**
	 * Une methode interne qui permet de connaitre le nombre de demi journee
	 * travaillees pour un jour donne.
	 */
	private int nbDemiJourneePourJour(Jour jour) {
		if (jour.isTravaille()) {
			if (jour.isFerme() || jour.isCongeJourneeComplete() || jour.isCongeLegalJourneeComplete()) {
				return 0;
			} else if (jour.isCongeAM() || jour.isCongePM() || jour.isCongeLegalAM() || jour.isCongeLegalPM()) {
				return 1;
			}
			return 2;
		}
		return 0;
	}

	/**
	 * 
	 * @param jour
	 * @return
	 */
	private boolean isTravailleSamediMatin(Jour jour) {
		boolean isTravailleSamediMatin = false;

		if (jour.isSamedi() &&
				jour.isTravaille() &&
				jour.isCongeJourneeComplete() == false &&
				jour.isCongeLegalJourneeComplete() == false &&
				jour.isCongeAM() == false &&
				jour.isCongeLegalAM() == false &&
				jour.isFerme() == false) {
			isTravailleSamediMatin = true;
		}

		return isTravailleSamediMatin;
	}

	/**
	 * 
	 * @param jour
	 * @return
	 */
	private boolean isTravailleSamediApresMidi(Jour jour) {
		boolean isTravailleSamediApresMidi = false;

		if (jour.isSamedi() &&
				jour.isTravaille() &&
				jour.isCongeJourneeComplete() == false &&
				jour.isCongeLegalJourneeComplete() == false &&
				jour.isCongePM() == false &&
				jour.isCongeLegalPM() == false &&
				jour.isFerme() == false) {
			isTravailleSamediApresMidi = true;
		}

		return isTravailleSamediApresMidi;
	}
}
