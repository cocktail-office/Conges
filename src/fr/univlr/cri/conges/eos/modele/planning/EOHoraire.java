// EOHoraire.java
// Created on Thu Jun 24 14:50:02  2004 by Apple EOModeler Version 5.2

package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.appserver.WOMessage;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.objects.Parametre;
import fr.univlr.cri.conges.objects.Semaine;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.StringCtrl;

public class EOHoraire
		extends _EOHoraire
		implements I_ClasseMetierNotificationParametre {

	public static final NSArray LIBELLES_JOURS = new NSArray(new String[] { "Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim" });

	// duree maximum d'un horaire hebdo pour un 100%
	private static int dureeHoraireHebdoMaxi;

	// duree maximum d'un horaire hebdo pour un 100% en hors normes
	private static int dureeHoraireHebdoMaxiHorsNorme;

	// faut-il afficher la pause RTT
	private static boolean isShowPause;

	/**
	 * @deprecated
	 * @see #initStaticField(Parametre)
	 */
	public static void initStaticFields(String aDureeHoraireHebdoMaxi, String aDureeHoraireHebdoMaxiHorsNorme) {
		dureeHoraireHebdoMaxi = TimeCtrl.getMinutes(aDureeHoraireHebdoMaxi);
		dureeHoraireHebdoMaxiHorsNorme = TimeCtrl.getMinutes(aDureeHoraireHebdoMaxiHorsNorme);
	}

	/**
	 * @see I_ClasseMetierNotificationParametre
	 * @param parametre
	 */
	public static void initStaticField(
			Parametre parametre) {
		if (parametre == Parametre.PARAM_DUREE_HORAIRE_HEBDO_MAXI) {
			dureeHoraireHebdoMaxi = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_DUREE_HORAIRE_HEBDO_MAXI_HORS_NORMES) {
			dureeHoraireHebdoMaxiHorsNorme = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_SHOW_PAUSE) {
			isShowPause = parametre.getParamValueBoolean().booleanValue();
		}
	}

	public EOHoraire() {
		super();
	}

	public int minutesDureeHebdo() {
		NSArray lesMinutesDureeJournalieres = NSArray.componentsSeparatedByString(durees(), ",");
		int minutes = 0;
		for (int i = 0; i < lesMinutesDureeJournalieres.count(); i++) {
			String minutesJournalieresStr = (String) lesMinutesDureeJournalieres.objectAtIndex(i);
			if (!StringCtrl.isEmpty(minutesJournalieresStr)) {
				minutes += (Integer.valueOf((String) lesMinutesDureeJournalieres.objectAtIndex(i))).intValue();
			}
		}
		return minutes;
	}

	//
	// public EOTypeHoraire typeHoraire() {
	// return (EOTypeHoraire)storedValueForKey("typeHoraire");
	// }
	//
	// public void setTypeHoraire(EOTypeHoraire value) {
	// takeStoredValueForKey(value, "typeHoraire");
	// }

	public EOAffectationAnnuelle affectationAnnuelle() {
		return (EOAffectationAnnuelle) storedValueForKey("affectationAnnuelle");
	}

	public void setAffectationAnnuelle(EOAffectationAnnuelle value) {
		takeStoredValueForKey(value, "affectationAnnuelle");
	}

	/**
	 * @return
	 */
	public NSArray horairesJournaliers() {
		NSArray horairesJournaliers = null;

		horairesJournaliers = NSArray.componentsSeparatedByString(horaires(), ",");

		return horairesJournaliers;
	}

	/**
	 * @return
	 */
	public NSArray pausesJournalieres() {
		NSArray pausesJournalieres = null;

		pausesJournalieres = NSArray.componentsSeparatedByString(pauses(), ",");

		return pausesJournalieres;
	}

	/**
	 * @return
	 */
	public NSArray dureesJournalieres() {
		NSArray dureesJournalieres = null;

		dureesJournalieres = NSArray.componentsSeparatedByString(durees(), ",");

		return dureesJournalieres;
	}

	/**
	 * @return
	 */
	public NSArray dureesJournalieresAM() {
		NSArray dureesJournalieres = null;

		dureesJournalieres = NSArray.componentsSeparatedByString(dureesAM(), ",");

		return dureesJournalieres;
	}

	/**
	 * @return
	 */
	public NSArray dureesJournalieresPM() {
		NSArray dureesJournalieres = null;

		dureesJournalieres = NSArray.componentsSeparatedByString(dureesPM(), ",");

		return dureesJournalieres;
	}

	/**
	 * @param ec
	 */
	public void insertInEditingContext(EOEditingContext ec) {
		if (ec != null) {

			if (ec.globalIDForObject(this) == null) {
				ec.insertObject(this);
			}
		}
	}

	/**
	 * horaire hebdo sous la forme de 1/2 journ�es prends en compte les jours
	 * feries
	 * 
	 * @return
	 */
	public NSArray presenceHoraireJF(NSTimestamp dateLundi) {
		NSArray presenceHoraire = new NSArray();
		NSArray horairesJournaliers = horairesJournaliers();
		// int indexJour = 0;

		for (int i = 0; i < horairesJournaliers.count(); i++) {

			// Jour ferie -> on passe au jour suivant
			// affectationAnnuelle().joursFeries();
			// if (false)
			// {//affectationAnnuelle().joursFeries().containsObject(dateLundi.timestampByAddingGregorianUnits(0,0,indexJour++,0,0,0)))
			// {
			/*
			 * if
			 * (affectationAnnuelle().joursFeries().indexOfIdenticalObject(dateLundi
			 * .timestampByAddingGregorianUnits(0,0,indexJour++,0,0,0)) != -1) {
			 * presenceHoraire = presenceHoraire.arrayByAddingObject("00"); } else {
			 */
			String unHoraire = (String) horairesJournaliers.objectAtIndex(i);
			NSArray unHoraireAMPM = NSArray.componentsSeparatedByString(unHoraire, ";");
			String unHoraireAM = "";
			String unHorairePM = "";

			if (unHoraireAMPM.count() > 0) {
				unHoraireAM = (String) unHoraireAMPM.objectAtIndex(0);
				unHorairePM = (String) unHoraireAMPM.lastObject();
			}

			if (!StringCtrl.isEmpty(unHoraireAM) && !StringCtrl.isEmpty(unHorairePM)) {
				presenceHoraire = presenceHoraire.arrayByAddingObject("11");
			} else if (StringCtrl.isEmpty(unHoraireAM) && StringCtrl.isEmpty(unHorairePM)) {
				presenceHoraire = presenceHoraire.arrayByAddingObject("00");
			} else if (!StringCtrl.isEmpty(unHoraireAM)) {
				presenceHoraire = presenceHoraire.arrayByAddingObject("10");
			} else {
				presenceHoraire = presenceHoraire.arrayByAddingObject("01");
			}
			// }

		}

		return presenceHoraire;
	}

	public String toStringHtml() {
		String toString = "";
		toString += "<CENTER><TABLE border=1><TR><TH colspan=4 bgcolor='#aabbff' align=center>" +
				WOMessage.stringByEscapingHTMLString(nomQuotite()) + "</TH></TR>";
		toString += "<TR><TH>Jour</TH><TH><CENTER>Matin</CENTER></TH><TH><CENTER>Apr&egrave;s-midi</CENTER></TH>";
		if (isShowPause) {
			toString += "<TH>Pause</TH></TR>";
		}
		NSArray horaires = horairesJournaliers();
		NSArray pauses = pausesJournalieres();
		for (int i = 0; i < horaires.count(); i++) {
			toString += "<TR><TH>" + (String) LIBELLES_JOURS.objectAtIndex(i) + "</TH>";
			String unHoraire = (String) horaires.objectAtIndex(i);
			if (!StringCtrl.isEmpty(unHoraire)) {
				NSArray unHoraireAMPM = NSArray.componentsSeparatedByString(unHoraire, ";");
				String unHoraireAM = (String) unHoraireAMPM.objectAtIndex(0);
				toString += "<TD>";
				if (!StringCtrl.isEmpty(unHoraireAM)) {
					NSArray lesHorairesAMPM = NSArray.componentsSeparatedByString(unHoraireAM, "|");
					String debAM = (String) lesHorairesAMPM.objectAtIndex(0);
					String finAM = (String) lesHorairesAMPM.lastObject();
					toString += debAM + "-" + finAM;
				} else {
					toString += "<CENTER> / </CENTER>";
				}
				toString += "</TD><TD>";

				String unHorairePM = (String) unHoraireAMPM.lastObject();
				if (!StringCtrl.isEmpty(unHorairePM)) {
					NSArray lesHorairesAMPM = NSArray.componentsSeparatedByString(unHorairePM, "|");
					String debPM = (String) lesHorairesAMPM.objectAtIndex(0);
					String finPM = (String) lesHorairesAMPM.lastObject();
					toString += debPM + "-" + finPM;
				} else {
					toString += "<CENTER> / </CENTER>";
				}
				toString += "</TD>";

				if (isShowPause) {
					toString += "<TD>";
					String unePause = (String) pauses.objectAtIndex(i);
					if (!StringCtrl.isEmpty(unePause)) {
						toString += unePause;
					} else {
						toString += "<CENTER> / </CENTER>";
					}
					toString += "</TD>";
				}

			} else {
				toString += "<TD><CENTER> / </CENTER></TD><TD><CENTER> / </CENTER></TD>";
				if (isShowPause) {
					toString += "<TD><CENTER> / </CENTER></TD>";
				}
			}
			toString += "</TR>";
		}

		return toString + "</TABLE></CENTER>";

	}

	/**
	 * donne la valeur en minutes du debut de la pause pour le jour choisit :
	 * indexDay : 0=Lun, 1=Mar ...
	 */
	public int minutesPauseAtIndexDayAndPosInDay(int indexDay) {
		if (indexDay < 6)
			return TimeCtrl.getMinutes((String) pausesJournalieres().objectAtIndex(indexDay));
		return 0;
	}

	/**
	 * donne la valeur en minutes pour le jour et l'instant de la journ�e choisit
	 * : indexDay : 0=Lun, 1=Mar ... posDay : 0=embaucheAM, 1=debaucheAM ...
	 */
	public int minutesAtIndexDayAndPosInDay(int indexDay, int posDay) {
		if (indexDay < 6) {

			NSArray horairesMatinAprem = NSArray.componentsSeparatedByString(
					(String) NSArray.componentsSeparatedByString(horaires(), ",").objectAtIndex(indexDay),
					";");

			if (posDay < 2) {
				if (horairesMatinAprem.count() > 0) {
					NSArray horairesMatin = NSArray.componentsSeparatedByString(
							(String) horairesMatinAprem.objectAtIndex(0), "|");
					if (horairesMatin.count() > 0) {
						return TimeCtrl.getMinutes((String) horairesMatin.objectAtIndex(posDay));
					}
				}
			} else {
				if (horairesMatinAprem.count() > 1) {
					NSArray horairesAprem = NSArray.componentsSeparatedByString(
							(String) horairesMatinAprem.objectAtIndex(1), "|");
					if (horairesAprem.count() > 0)
						return TimeCtrl.getMinutes((String) horairesAprem.objectAtIndex(posDay - 2));
				}
			}
		}
		return 0;
	}

	public boolean isSemaineHaute() {
		int minutesDureeHebdoMaxi = (affectationAnnuelle().isHorsNorme() ? dureeHoraireHebdoMaxiHorsNorme : dureeHoraireHebdoMaxi);
		minutesDureeHebdoMaxi = (quotite() == null || affectationAnnuelle().isTempsPartielAnnualise() ? minutesDureeHebdoMaxi : minutesDureeHebdoMaxi
				* quotite().intValue() / 100);
		return minutesDureeHebdo() == minutesDureeHebdoMaxi;
	}

	private Number oid;

	/**
	 * Cle primaire (lecture seule) -1 si non trouvee
	 */
	public Number oid() {
		if (oid == null) {
			oid = new Integer(-1);
			Number pk = (Number) EOUtilities.primaryKeyForObject(editingContext(), this).valueForKey("oid");
			if (pk != null)
				oid = pk;
		}
		return oid;
	}

	/**
	 * Indique si un horaire est saisi sur la demi journee du matin du samedi
	 */
	public boolean isTravailleSamediMatin() {
		return minutesAtIndexDayAndPosInDay(5, 0) != 0 && minutesAtIndexDayAndPosInDay(5, 1) != 0;
	}

	/**
	 * Indique si un horaire est saisi sur la demi journee de l'apres midi du
	 * samedi
	 */
	public boolean isTravailleSamediApresMidi() {
		return minutesAtIndexDayAndPosInDay(5, 2) != 0 && minutesAtIndexDayAndPosInDay(5, 3) != 0;
	}

	/**
	 * Indique si l'horaire peut être associé à la semaine. Oui si le planning est
	 * en TPA ou si la quotité est identique
	 * 
	 * @param semaine
	 * @return
	 */
	public boolean isAssociableALaSemaine(Semaine semaine) {
		boolean isAssociableSemaine = false;

		if (semaine.planningHebdo() != null && (
				affectationAnnuelle().isTempsPartielAnnualise() || (
						!affectationAnnuelle().isTempsPartielAnnualise() &&
						semaine.planningHebdo().periodeAffectationAnnuelle().quotite().floatValue() == quotite().floatValue()))) {
			isAssociableSemaine = true;
		}

		return isAssociableSemaine;
	}

	/**
	 * Affichage de l'horaire + sa quotité
	 * 
	 * @return
	 */
	public String nomQuotite() {
		String nomQuotite = nom();

		nomQuotite += " <";

		if (quotite() != null) {
			nomQuotite += Integer.toString(quotite().intValue()) + "%";
		} else {
			nomQuotite += "TPA";
		}

		nomQuotite += ">";

		return nomQuotite;
	}
}
