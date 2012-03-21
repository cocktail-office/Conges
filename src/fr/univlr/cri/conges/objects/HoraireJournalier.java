package fr.univlr.cri.conges.objects;

import com.webobjects.eocontrol.EOCustomObject;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.planning.I_ClasseMetierNotificationParametre;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.planning.SPOccupation;
import fr.univlr.cri.util.StringCtrl;

/*
 * Created on 3 juil. 04
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author egeze
 */
public class HoraireJournalier
		extends EOCustomObject
		implements I_ClasseMetierNotificationParametre {

	private String heureDebutPause;
	private String heureDebutAM;
	private String heureFinAM;
	private String heureDebutPM;
	private String heureFinPM;
	private String HoraireJournalierTotal;
	private String libelleJour;
	public static final NSArray libellesJours = new NSArray(
			new String[] { "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche" });
	private static int aMDebutMini;
	private static int aMDebutMaxi;
	private static int pMFinMini;
	private static int pMFinMaxi;
	private static int pauseMeridienneDureeMini;
	private static int pauseMeridienneDebMini;
	private static int pauseMeridienneFinMaxi;
	private static int pauseRTTDuree;
	private static int minutesTravailleesMini;
	private static int debutJourneeBonus;
	private static int finJourneeBonus;
	private static float coefSamediMatin;
	private static float coefDebordement;
	private static float coefHsupSamApremDimJf;
	private static int demiJourneeDureeMaxi;

	/** la reference vers l'horaire hebdomadaire associe */
	private HoraireHebdomadaire horaireHebdo;

	/**
	 * @see I_ClasseMetierNotifiableParametre
	 */
	public static void initStaticField(Parametre parametre) {
		if (parametre == Parametre.PARAM_AM_DEBUT_MINI) {
			aMDebutMini = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_AM_DEBUT_MAXI) {
			aMDebutMaxi = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_PM_FIN_MINI) {
			pMFinMini = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_PM_FIN_MAXI) {
			pMFinMaxi = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_PAUSE_MERIDIENNE_DUREE_MINI) {
			pauseMeridienneDureeMini = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_PAUSE_MERIDIENNE_DEBUT_MINI) {
			pauseMeridienneDebMini = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_PAUSE_MERIDIENNE_FIN_MAXI) {
			pauseMeridienneFinMaxi = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_PAUSE_RTT_DUREE) {
			pauseRTTDuree = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_HEURES_TRAVAILLEES_MINI_HOR_BONUS) {
			minutesTravailleesMini = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_DEBUT_JOURNEE_BONUS) {
			debutJourneeBonus = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_FIN_JOURNEE_BONUS) {
			finJourneeBonus = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_COEF_SAMEDI_MATIN_BONUS) {
			coefSamediMatin = parametre.getParamValueFloat().floatValue();
		} else if (parametre == Parametre.PARAM_COEF_DEBORDEMENT_BONUS) {
			coefDebordement = parametre.getParamValueFloat().floatValue();
		} else if (parametre == Parametre.PARAM_COEF_HSUP_SAM_APREM_DIM_JF) {
			coefHsupSamApremDimJf = parametre.getParamValueFloat().floatValue();
		} else if (parametre == Parametre.PARAM_DEMI_JOURNEE_DUREE_MAXI) {
			demiJourneeDureeMaxi = TimeCtrl.getMinutes(parametre.getParamValueString());
		}
	}

	/**
	 * 
	 */
	public HoraireJournalier(int indexJour, HoraireHebdomadaire anHoraireHebdo) {
		horaireHebdo = anHoraireHebdo;
		if (horaireHebdo != null && horaireHebdo.horaire() != null) {
			NSArray heures = horaireHebdo.horaire().horairesJournaliers();
			NSArray pauses = horaireHebdo.horaire().pausesJournalieres();
			String heuresJour = (String) heures.objectAtIndex(indexJour);

			setLibelleJour((String) libellesJours.objectAtIndex(indexJour));
			if (heuresJour != null && !(heuresJour.equals(""))) {
				NSArray array = NSArray.componentsSeparatedByString(heuresJour, ";");
				String horaireAM = "";
				String horairePM = "";
				if (array.count() == 2) {
					horaireAM = (String) array.objectAtIndex(0);
					horairePM = (String) array.lastObject();
				}

				array = NSArray.componentsSeparatedByString(horaireAM, "|");
				if (array.count() == 2) {
					setHeureDebutAM((String) array.objectAtIndex(0));
					setHeureFinAM((String) array.lastObject());
				} else {
					setHeureDebutAM("");
					setHeureFinAM("");
				}
				array = NSArray.componentsSeparatedByString(horairePM, "|");
				if (array.count() == 2) {
					setHeureDebutPM((String) array.objectAtIndex(0));
					setHeureFinPM((String) array.lastObject());
				} else {
					setHeureDebutPM("");
					setHeureFinPM("");
				}
				if (pauses != null && pauses.count() > 0) {
					setHeureDebutPause((String) pauses.objectAtIndex(indexJour));
				} else {
					setHeureDebutPause("");
				}
			} else {
				setHeureDebutAM("");
				setHeureFinAM("");
				setHeureDebutPM("");
				setHeureFinPM("");
				setHeureDebutPause("");
			}
		} else {
			setLibelleJour((String) libellesJours.objectAtIndex(indexJour));
			setHeureDebutAM("");
			setHeureFinAM("");
			setHeureDebutPM("");
			setHeureFinPM("");
			setHeureDebutPause("");
		}
		recalculerTotal();
	}

	/**
	 * @param value
	 */
	private void setHoraireJournalierTotal(String value) {
		HoraireJournalierTotal = value;
	}

	/**
	 * @param value
	 */
	public void setHeureFinPM(String value) {
		heureFinPM = value;
	}

	/**
	 * @param value
	 */
	public void setHeureDebutPM(String value) {
		heureDebutPM = value;
	}

	/**
	 * @param value
	 */
	public void setHeureFinAM(String value) {
		heureFinAM = value;
	}

	/**
	 * @param value
	 */
	public void setHeureDebutAM(String value) {
		heureDebutAM = value;
	}

	/**
	 * @param value
	 */
	public void setHeureDebutPause(String value) {
		heureDebutPause = value;
	}

	/**
	 * @param value
	 */
	private void setLibelleJour(String value) {
		libelleJour = value;

	}

	/**
	 * @return
	 */
	public String getHeureDebutAM() {
		return heureDebutAM;
	}

	/**
	 * @return
	 */
	public String getHeureDebutPause() {
		// le debut de la pause est positionn� que si au moins une demi journ�e est
		// saisie
		if (!"".equals(getHeureDebutAM()) && !"".equals(getHeureFinAM())
				||
				(!"".equals(getHeureDebutPM()) && !"".equals(getHeureFinPM())))
			return heureDebutPause;
		else
			return "";
		// return TimeCtrl.stringForMinutes(0).replace('.', 'h');
	}

	/**
	 * @return
	 */
	public String getHeureDebutPM() {
		return heureDebutPM;
	}

	/**
	 * @return
	 */
	public String getHeureFinAM() {
		return heureFinAM;
	}

	/**
	 * @return
	 */
	public String getHeureFinPM() {
		return heureFinPM;
	}

	/**
	 * @return
	 */
	public String getHoraireJournalierTotal() {
		return HoraireJournalierTotal;
	}

	/**
	 * @return
	 */
	public String libelleJour() {
		return libelleJour;
	}

	/**
	 * 
	 */
	public void recalculerTotal() {
		String horaireTotal = "00h00";
		int total = 0;
		String heureDebAM = getHeureDebutAM();
		String heureFinAM = getHeureFinAM();
		String heureDebPM = getHeureDebutPM();
		String heureFinPM = getHeureFinPM();
		NSArray heureDebArray = null;
		NSArray heureFinArray = null;
		int minutesAM = 0;
		int minutesPM = 0;

		try {

			if ((StringCtrl.isEmpty(heureDebAM) == false && StringCtrl.isEmpty(heureFinAM) == false) ||
					(StringCtrl.isEmpty(heureDebPM) == false && StringCtrl.isEmpty(heureFinPM) == false)) {
				if (StringCtrl.isEmpty(heureDebAM) == false && StringCtrl.isEmpty(heureFinAM) == false) {
					Integer heures = null;
					Integer minutes = null;

					heureDebArray = NSArray.componentsSeparatedByString(heureDebAM, ":");
					heureFinArray = NSArray.componentsSeparatedByString(heureFinAM, ":");
					heures = Integer.valueOf((String) heureDebArray.objectAtIndex(0));
					minutes = Integer.valueOf((String) heureDebArray.lastObject());
					minutesAM = heures.intValue() * 60 + minutes.intValue();
					heures = Integer.valueOf((String) heureFinArray.objectAtIndex(0));
					minutes = Integer.valueOf((String) heureFinArray.lastObject());
					minutesAM = heures.intValue() * 60 + minutes.intValue() - minutesAM;
				}

				if (StringCtrl.isEmpty(heureDebPM) == false && StringCtrl.isEmpty(heureFinPM) == false) {
					Integer heures = null;
					Integer minutes = null;
					heureDebArray = NSArray.componentsSeparatedByString(heureDebPM, ":");
					heureFinArray = NSArray.componentsSeparatedByString(heureFinPM, ":");
					heures = Integer.valueOf((String) heureDebArray.objectAtIndex(0));
					minutes = Integer.valueOf((String) heureDebArray.lastObject());
					minutesPM = heures.intValue() * 60 + minutes.intValue();
					heures = Integer.valueOf((String) heureFinArray.objectAtIndex(0));
					minutes = Integer.valueOf((String) heureFinArray.lastObject());
					minutesPM = heures.intValue() * 60 + minutes.intValue() - minutesPM;
				}
				total = minutesAM + minutesPM;
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

		if (total > 0) {
			int heuresTotal = total / 60;
			int minutesTotal = total % 60;

			horaireTotal = String.valueOf(heuresTotal) + "h";
			if (heuresTotal < 10) {
				horaireTotal = "0" + horaireTotal;
			}
			if (minutesTotal < 10) {
				horaireTotal = horaireTotal + "0";
			}
			horaireTotal = horaireTotal + minutesTotal;
		}

		setHoraireJournalierTotal(horaireTotal);
	}

	public String toString() {
		String toString = null;

		toString = toStringHoraire() + "/" + toStringPause();

		return toString;
	}

	public String toStringHoraire() {
		String toString = "";
		String heureDebAM = getHeureDebutAM();
		String heureFinAM = getHeureFinAM();
		String heureDebPM = getHeureDebutPM();
		String heureFinPM = getHeureFinPM();

		if (StringCtrl.isEmpty(heureDebAM) || StringCtrl.isEmpty(heureFinAM)) {
			toString = "";
		} else {
			toString = heureDebAM + "|" + heureFinAM;
		}
		toString += ";";
		if (StringCtrl.isEmpty(heureDebPM) || StringCtrl.isEmpty(heureFinPM)) {
			toString += "";
		} else {
			toString += heureDebPM + "|" + heureFinPM;
		}

		return toString;
	}

	public String toStringPause() {
		String toString = getHeureDebutPause();

		if (StringCtrl.isEmpty(toString)) {
			toString = "";
		}

		return toString;
	}

	public String toStringDureeAM() {
		String heureDebAM = getHeureDebutAM();
		String heureFinAM = getHeureFinAM();
		NSArray heuresMinutes = null;
		int duree = 0;

		if (StringCtrl.isEmpty(heureDebAM) == false &&
				StringCtrl.isEmpty(heureFinAM) == false) {
			heuresMinutes = NSArray.componentsSeparatedByString(heureFinAM, ":");
			duree = Integer.valueOf((String) heuresMinutes.lastObject()).intValue();
			duree += Integer.valueOf((String) heuresMinutes.objectAtIndex(0)).intValue() * 60;
			heuresMinutes = NSArray.componentsSeparatedByString(heureDebAM, ":");
			duree -= Integer.valueOf((String) heuresMinutes.lastObject()).intValue();
			duree -= Integer.valueOf((String) heuresMinutes.objectAtIndex(0)).intValue() * 60;
		}

		return String.valueOf(duree);
	}

	public String toStringDureePM() {
		String heureDebPM = getHeureDebutPM();
		String heureFinPM = getHeureFinPM();
		NSArray heuresMinutes = null;
		int duree = 0;

		if (StringCtrl.isEmpty(heureDebPM) == false &&
				StringCtrl.isEmpty(heureFinPM) == false) {
			heuresMinutes = NSArray.componentsSeparatedByString(heureFinPM, ":");
			duree = Integer.valueOf((String) heuresMinutes.lastObject()).intValue();
			duree += Integer.valueOf((String) heuresMinutes.objectAtIndex(0)).intValue() * 60;
			heuresMinutes = NSArray.componentsSeparatedByString(heureDebPM, ":");
			duree -= Integer.valueOf((String) heuresMinutes.lastObject()).intValue();
			duree -= Integer.valueOf((String) heuresMinutes.objectAtIndex(0)).intValue() * 60;
		}

		return String.valueOf(duree);
	}

	public String toStringDuree() {
		String toStringDureeAM = toStringDureeAM();
		String toStringDureePM = toStringDureePM();
		int duree = 0;

		duree = Integer.valueOf(toStringDureeAM).intValue() + Integer.valueOf(toStringDureePM).intValue();

		return String.valueOf(duree);
	}

	public String toStringDureeAvecBonus() {
		int duree = 0;
		int bonus = to_minutes(getBonus());

		duree = Integer.valueOf(toStringDuree()).intValue();
		duree += bonus;

		return String.valueOf(duree);
	}

	private int to_minutes(String horaire) {
		int to_minutes = 0;
		if (StringCtrl.isEmpty(horaire) == false) {
			String heures = horaire.substring(0, 2);
			String minutes = horaire.substring(3, 5);

			to_minutes += Integer.valueOf(heures).intValue() * 60;
			to_minutes += Integer.valueOf(minutes).intValue();
		}

		return to_minutes;
	}

	private String erreur;

	/**
	 * @return
	 */
	public boolean isValide(boolean passeDroit) {
		boolean isValide = true;

		setErreur(null);

		int heureDebAM = to_minutes(getHeureDebutAM());
		int heureFinAM = to_minutes(getHeureFinAM());
		int heureDebPM = to_minutes(getHeureDebutPM());
		int heureFinPM = to_minutes(getHeureFinPM());

		if (heureDebAM != 0 && heureFinAM != 0 && heureDebPM != 0 && heureFinPM != 0) {
			if ((heureDebAM < heureFinAM) && (heureDebPM < heureFinPM)) {
				isValide = true;
			} else {
				isValide = false;
				setErreur("les heures d'embauche doivent preceder les heures de debauche");
			}
		} else if ((heureDebAM != 0 && heureFinAM != 0) && (heureDebPM == 0 && heureFinPM == 0)) {
			if (heureDebAM < heureFinAM) {
				isValide = true;
			} else {
				isValide = false;
				setErreur("matin : l'heure d'embauche doit preceder de debauche");
			}
		} else if ((heureDebPM != 0 && heureFinPM != 0) && (heureDebAM == 0 && heureFinAM == 0)) {
			if (heureDebPM < heureFinPM) {
				isValide = true;
			} else {
				isValide = false;
				setErreur("apres-midi : l'heure d'embauche doit preceder de debauche");
			}
		}

		if (passeDroit == false) {

			if (isValide && heureDebAM != 0 && heureDebAM < aMDebutMini) {
				setErreur("matin : l'heure d'embauche intervient trop tot");
				isValide = false;
			}

			if (isValide && heureDebAM != 0 && heureDebAM > aMDebutMaxi) {
				setErreur("matin : l'heure d'embauche intervient trop tard");
				isValide = false;
			}

			if (isValide && heureFinPM != 0 && heureFinPM < pMFinMini) {
				setErreur("apres midi : l'heure de debauche intervient trop tot");
				isValide = false;
			}

			if (isValide && heureFinPM != 0 && heureFinPM > pMFinMaxi) {
				setErreur("apres midi : l'heure de debauche intervient trop tard");
				isValide = false;
			}

			if (isValide && heureFinAM != 0 && heureFinAM < pauseMeridienneDebMini) {
				setErreur("matin : l'heure de debauche intervient trop tot");
				isValide = false;
			}

			if (isValide && heureDebPM != 0 && heureDebPM > pauseMeridienneFinMaxi) {
				setErreur("apres midi : l'heure d'embauche intervient trop tard");
				isValide = false;
			}

			if (isValide && heureDebPM != 0 && heureFinAM != 0 && (heureDebPM - heureFinAM) < pauseMeridienneDureeMini) {
				setErreur("pause meridienne : duree est trop courte");
				isValide = false;
			}

			if (isValide && heureDebAM != 0 && heureFinAM != 0 && (heureFinAM - heureDebAM) > demiJourneeDureeMaxi) {
				setErreur("matin : la duree de la demi-journee depasse le plafond maximum");
				isValide = false;
			}

			if (isValide && heureDebPM != 0 && heureFinPM != 0 && (heureFinPM - heureDebPM) > demiJourneeDureeMaxi) {
				setErreur("apres midi : la duree de la demi-journee depasse le plafond maximum");
				isValide = false;
			}

		}

		return isValide;
	}

	/**
	 * retoune le bonus en minutes pour une journée de travail
	 * 
	 * @param heuresJour
	 * @param jour
	 * @return
	 */
	public String getBonus() {

		int h1, h2, h3, h4;
		h1 = h2 = h3 = h4 = 0;

		// recuperation de l'horaire
		if (!StringCtrl.isEmpty(getHeureDebutAM()))
			h1 = TimeCtrl.getMinutes(getHeureDebutAM());
		if (!StringCtrl.isEmpty(getHeureFinAM()))
			h2 = TimeCtrl.getMinutes(getHeureFinAM());
		if (!StringCtrl.isEmpty(getHeureDebutPM()))
			h3 = TimeCtrl.getMinutes(getHeureDebutPM());
		if (!StringCtrl.isEmpty(getHeureFinPM()))
			h4 = TimeCtrl.getMinutes(getHeureFinPM());

		// on ne verifie que si l'horaire est renseign�
		if ((h1 == 0) && (h2 == 0) && (h3 == 0) && (h4 == 0))
			return TimeCtrl.stringForMinutes(0).replace(':', 'h');

		boolean isSamedi = ((String) libellesJours.objectAtIndex(5)).equals(libelleJour());

		int bonus = 0;

		// samedi
		if (isSamedi) {
			// le bonus ne s'applique que si les 10 1/2 (lundi au vendredi)
			// journees precedentes sont travaillees
			boolean isTravaille10DemiJourn = true;
			NSArray horairesJourn = horaireHebdo.horairesJournaliers();
			for (int i = 0; i < 5; i++) {
				HoraireJournalier aHoraireHebdo = (HoraireJournalier) horairesJourn.objectAtIndex(i);
				isTravaille10DemiJourn = isTravaille10DemiJourn && aHoraireHebdo.isJourneeCompleteTravaillee();
			}
			int bonusSamediMatin = 0;
			if (isTravaille10DemiJourn) {
				bonusSamediMatin = (int) ((h2 - h1) * (coefSamediMatin - (float) 1));
			}
			horaireHebdo.setBonusSamediMatin(bonusSamediMatin);
			bonus += bonusSamediMatin;

			// samedi apres midi travaillé donne bonif de 1,5
			int bonusSamediApresMidi = (int) ((h4 - h3) * (coefHsupSamApremDimJf - (float) 1));
			horaireHebdo.setBonusSamediApresMidi(bonusSamediApresMidi);
			bonus += bonusSamediApresMidi;
		}

		// avant 7h00 ou apr�s 19h00, pour 2h00 travaillees minimum : 1:00 -> 1h12
		int cumul = 0;

		// avant 7h00
		if (h1 != h2 && h1 < debutJourneeBonus) {
			cumul += debutJourneeBonus - h1;
		}

		// apres 19h00
		if (h4 > finJourneeBonus) {
			if (h3 > finJourneeBonus) {
				cumul += h4 - h3;
			} else {
				cumul += h4 - finJourneeBonus;
			}
		}

		if (cumul >= minutesTravailleesMini) {
			bonus += (int) ((float) cumul * (coefDebordement - (float) 1));
		}

		return TimeCtrl.stringHeureToDuree(TimeCtrl.stringForMinutes(bonus));
	}

	/**
	 * retourne l'horaire sous la forme d'occupations ex : 8h00-12h00 et
	 * 14h00-17h00
	 * 
	 * @dateJour : date du jour a attacher a l'horaire
	 * @voirPause : prise en compte de la pause ou non
	 * @return
	 */
	public NSArray occupationsHoraireJournalier(NSTimestamp dateJour, boolean voirPauses) {
		NSArray occupationsHoraireJournalier = new NSArray();

		int h1, h2, h3, h4, p;
		h1 = h2 = h3 = h4 = p = -1;

		// recuperation de l'horaire
		if (!StringCtrl.isEmpty(getHeureDebutAM()))
			h1 = TimeCtrl.getMinutes(getHeureDebutAM());
		if (!StringCtrl.isEmpty(getHeureFinAM()))
			h2 = TimeCtrl.getMinutes(getHeureFinAM());
		if (!StringCtrl.isEmpty(getHeureDebutPM()))
			h3 = TimeCtrl.getMinutes(getHeureDebutPM());
		if (!StringCtrl.isEmpty(getHeureFinPM()))
			h4 = TimeCtrl.getMinutes(getHeureFinPM());
		if (!StringCtrl.isEmpty(getHeureDebutPause()))
			p = TimeCtrl.getMinutes(getHeureDebutPause());

		if (h1 != -1 && h2 != -1) {
			// la pause existe et on veut la voir
			if (p != -1 && voirPauses) {
				// la pause est le matin, et pas en d�but de pause m�ridienne -> 2
				// occupations
				if (p < h2 - pauseRTTDuree) {

					NSTimestamp dateDebut = DateCtrlConges.stringToDate(DateCtrlConges.dateToString(dateJour) + " " + TimeCtrl.stringForMinutes(h1), "%d/%m/%Y %H:%M");
					NSTimestamp dateFin = DateCtrlConges.stringToDate(DateCtrlConges.dateToString(dateJour) + " " + TimeCtrl.stringForMinutes(p), "%d/%m/%Y %H:%M");
					SPOccupation spOccAvtPause = new SPOccupation(dateDebut, dateFin, "horaire", null);
					occupationsHoraireJournalier = occupationsHoraireJournalier.arrayByAddingObject(spOccAvtPause);

					dateDebut = DateCtrlConges.stringToDate(DateCtrlConges.dateToString(dateJour) + " " + TimeCtrl.stringForMinutes(p + pauseRTTDuree), "%d/%m/%Y %H:%M");
					dateFin = DateCtrlConges.stringToDate(DateCtrlConges.dateToString(dateJour) + " " + TimeCtrl.stringForMinutes(h2), "%d/%m/%Y %H:%M");
					SPOccupation spOccAprPause = new SPOccupation(dateDebut, dateFin, "horaire", null);
					occupationsHoraireJournalier = occupationsHoraireJournalier.arrayByAddingObject(spOccAprPause);
				}
				// la pause est le matin, mais en d�but de pause m�ridienne -> 1
				// occupation
				else {
					NSTimestamp dateDebut = DateCtrlConges.stringToDate(DateCtrlConges.dateToString(dateJour) + " " + TimeCtrl.stringForMinutes(h1), "%d/%m/%Y %H:%M");
					NSTimestamp dateFin = DateCtrlConges.stringToDate(DateCtrlConges.dateToString(dateJour) + " " + TimeCtrl.stringForMinutes(h2 - pauseRTTDuree), "%d/%m/%Y %H:%M");
					SPOccupation spOcc = new SPOccupation(dateDebut, dateFin, "horaire", null);
					occupationsHoraireJournalier = occupationsHoraireJournalier.arrayByAddingObject(spOcc);
				}
			}
			// pas de pause
			else {
				NSTimestamp dateDebut = DateCtrlConges.stringToDate(DateCtrlConges.dateToString(dateJour) + " " + TimeCtrl.stringForMinutes(h1), "%d/%m/%Y %H:%M");
				NSTimestamp dateFin = DateCtrlConges.stringToDate(DateCtrlConges.dateToString(dateJour) + " " + TimeCtrl.stringForMinutes(h2), "%d/%m/%Y %H:%M");
				SPOccupation spOcc = new SPOccupation(dateDebut, dateFin, "horaire", null);
				occupationsHoraireJournalier = occupationsHoraireJournalier.arrayByAddingObject(spOcc);
			}
		}

		if (h3 != -1 && h4 != -1) {
			// la pause existe et on veut la voir
			if (p != -1 && voirPauses) {
				// la pause est l'aprem, et pas en fin de pause m�ridienne -> 2
				// occupations
				if (p > h3 + pauseRTTDuree) {
					NSTimestamp dateDebut = DateCtrlConges.stringToDate(DateCtrlConges.dateToString(dateJour) + " " + TimeCtrl.stringForMinutes(h3), "%d/%m/%Y %H:%M");
					NSTimestamp dateFin = DateCtrlConges.stringToDate(DateCtrlConges.dateToString(dateJour) + " " + TimeCtrl.stringForMinutes(p), "%d/%m/%Y %H:%M");
					SPOccupation spOccAvtPause = new SPOccupation(dateDebut, dateFin, "horaire", null);
					occupationsHoraireJournalier = occupationsHoraireJournalier.arrayByAddingObject(spOccAvtPause);

					dateDebut = DateCtrlConges.stringToDate(DateCtrlConges.dateToString(dateJour) + " " + TimeCtrl.stringForMinutes(p + pauseRTTDuree), "%d/%m/%Y %H:%M");
					dateFin = DateCtrlConges.stringToDate(DateCtrlConges.dateToString(dateJour) + " " + TimeCtrl.stringForMinutes(h4), "%d/%m/%Y %H:%M");
					SPOccupation spOccAprPause = new SPOccupation(dateDebut, dateFin, "horaire", null);
					occupationsHoraireJournalier = occupationsHoraireJournalier.arrayByAddingObject(spOccAprPause);

				}
				// la pause est le matin, mais en d�but de pause m�ridienne -> 1
				// occupation
				else {
					NSTimestamp dateDebut = DateCtrlConges.stringToDate(DateCtrlConges.dateToString(dateJour) + " " + TimeCtrl.stringForMinutes(h3/*
																																																																				 * +
																																																																				 * pauseRTTDuree
																																																																				 */), "%d/%m/%Y %H:%M");
					NSTimestamp dateFin = DateCtrlConges.stringToDate(DateCtrlConges.dateToString(dateJour) + " " + TimeCtrl.stringForMinutes(h4), "%d/%m/%Y %H:%M");
					SPOccupation spOcc = new SPOccupation(dateDebut, dateFin, "horaire", null);
					occupationsHoraireJournalier = occupationsHoraireJournalier.arrayByAddingObject(spOcc);
				}
			}
			// pas de pause
			else {
				NSTimestamp dateDebut = DateCtrlConges.stringToDate(DateCtrlConges.dateToString(dateJour) + " " + TimeCtrl.stringForMinutes(h3), "%d/%m/%Y %H:%M");
				NSTimestamp dateFin = DateCtrlConges.stringToDate(DateCtrlConges.dateToString(dateJour) + " " + TimeCtrl.stringForMinutes(h4), "%d/%m/%Y %H:%M");
				SPOccupation spOcc = new SPOccupation(dateDebut, dateFin, "horaire", null);
				occupationsHoraireJournalier = occupationsHoraireJournalier.arrayByAddingObject(spOcc);
			}
		}
		return occupationsHoraireJournalier;
	}

	/**
	 * Indique si l'horaire occupe les 2 demi journee du matin et de l'apres midi.
	 */
	public boolean isJourneeCompleteTravaillee() {
		return !StringCtrl.isEmpty(getHeureDebutAM()) &&
				!StringCtrl.isEmpty(getHeureFinAM()) &&
				!StringCtrl.isEmpty(getHeureDebutPM()) &&
				!StringCtrl.isEmpty(getHeureFinPM());
	}

	protected static final int getAMDebutMini() {
		return aMDebutMini;
	}

	protected static final int getAMDebutMaxi() {
		return aMDebutMaxi;
	}

	protected static final int getPMFinMini() {
		return pMFinMini;
	}

	protected static final int getPMFinMaxi() {
		return pMFinMaxi;
	}

	protected static final int getPauseMeridienneDureeMini() {
		return pauseMeridienneDureeMini;
	}

	protected static final int getPauseMeridienneDebMini() {
		return pauseMeridienneDebMini;
	}

	protected static final int getPauseMeridienneFinMaxi() {
		return pauseMeridienneFinMaxi;
	}

	protected static final int getPauseRTTDuree() {
		return pauseRTTDuree;
	}

	protected static final int getMinutesTravailleesMini() {
		return minutesTravailleesMini;
	}

	protected static final int getDebutJourneeBonus() {
		return debutJourneeBonus;
	}

	protected static final int getFinJourneeBonus() {
		return finJourneeBonus;
	}

	protected static final float getCoefSamediMatin() {
		return coefSamediMatin;
	}

	protected static final float getCoefDebordement() {
		return coefDebordement;
	}

	public final String getErreur() {
		return erreur;
	}

	public final void setErreur(String erreur) {
		this.erreur = erreur;
	}

}
