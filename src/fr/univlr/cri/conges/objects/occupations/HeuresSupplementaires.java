/*
 * Copyright Universit� de La Rochelle 2004
 *
 * ctarade@univ-lr.fr
 *
 * Ce logiciel est un programme informatique servant � g�rer les comptes
 * informatiques des utilisateurs. 
 * 
 * Ce logiciel est r�gi par la licence CeCILL soumise au droit fran�ais et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffus�e par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".

 * En contrepartie de l'accessibilit� au code source et des droits de copie,
 * de modification et de redistribution accord�s par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limit�e.  Pour les m�mes raisons,
 * seule une responsabilit� restreinte p�se sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les conc�dants successifs.

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

 * Le fait que vous puissiez acc�der � cet en-t�te signifie que vous avez 
 * pris connaissance de la licence CeCILL, et que vous en avez accept� les
 * termes.
 */

/**
 * @author ctarade
 */

package fr.univlr.cri.conges.objects.occupations;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.planning.EOHoraire;
import fr.univlr.cri.conges.eos.modele.planning.EOOccupation;
import fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation;
import fr.univlr.cri.conges.eos.modele.planning.I_ClasseMetierNotificationParametre;
import fr.univlr.cri.conges.objects.Jour;
import fr.univlr.cri.conges.objects.Parametre;
import fr.univlr.cri.conges.objects.Planning;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.StringCtrl;

/**
 * Classe de gestion des heures supplementaires. Ce type d'occupation ne peut
 * �tre saisi qu'a la journ�e (i.e. de 00h00 � 23h59 d'un meme jour). Selon le
 * positionnement de ces dernieres dans la journ�e, un coefficient de
 * bonification est appliqu�. La valeur la plus avantageuse est toujours choisie
 * en cas de bonification multiple a un instant t.
 * 
 * @author ctarade
 * 
 */
public class HeuresSupplementaires
		extends Occupation
		implements I_ClasseMetierNotificationParametre {

	public final static String LIBELLE_COURT = "H_SUP";

	private static float coefHsupDebordement;
	private static float coefHsupSamMat5j;
	private static float coefHsupSamApremDimJf;

	// durees
	private final static int MINUTES_05H00 = 5 * 60;
	private final static int MINUTES_07H00 = 7 * 60;
	private final static int MINUTES_12H30 = 12 * 60 + 30;
	private final static int MINUTES_19H00 = 19 * 60;
	private final static int MINUTES_22H00 = 22 * 60;
	private final static int MINUTES_23H59 = 23 * 60 + 59;

	/** TODO duree parametrable */
	private final static int MINUTES_MINI_HSUP = 30;
	private static float coefHsup22h00_05h00 = (float) 1.5;

	/**
	 * @see I_ClasseMetierNotificationParametre
	 */
	public static void initStaticField(
			Parametre parametre) {
		if (parametre == Parametre.PARAM_COEF_HSUP_DEBORDEMENT) {
			coefHsupDebordement = parametre.getParamValueFloat().floatValue();
		} else if (parametre == Parametre.PARAM_COEF_HSUP_SAM_MAT_5J) {
			coefHsupSamMat5j = parametre.getParamValueFloat().floatValue();
		} else if (parametre == Parametre.PARAM_COEF_HSUP_SAM_APREM_DIM_JF) {
			coefHsupSamApremDimJf = parametre.getParamValueFloat().floatValue();
		}
	}

	/**
	 * @param unType
	 * @param unPlanning
	 * @param debutTS
	 * @param finTS
	 * @param ec
	 */
	public HeuresSupplementaires(EOTypeOccupation unType, Planning unPlanning, NSTimestamp debutTS, NSTimestamp finTS, String unMotif, EOEditingContext ec) {
		super(unType, unPlanning, debutTS, finTS, unMotif, ec);
	}

	public HeuresSupplementaires(EOOccupation uneOccupation, Planning unPlanning, EOEditingContext ec) {
		super(uneOccupation, unPlanning, ec);
	}

	public HeuresSupplementaires(EOOccupation uneOccupation, EOEditingContext ec) {
		super(uneOccupation, ec);
	}

	public boolean isValide() {
		boolean isValide = super.isValide();

		if (isValide) {
			// le motif est obligatoire
			if (StringCtrl.isEmpty(leMotif())) {
				setErrorMsg("La saisie d'un motif est obligatoire pour une heure supplémentaire");
				return false;
			}

			// recuperation de l'horaire associ� au jour de l'absence
			EOHoraire horaire = lePlanning.leJour(dateDebut()).semaine().horaire();
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(dateDebut());
			// minutes debut fin du conges
			int m1 = TimeCtrl.getMinutesOfDay(dateDebut());
			int m2 = TimeCtrl.getMinutesOfDay(dateFin());

			// TODO mettre la duree minimale d'heures sup en parametres
			// la duree minimale est de 30 minutes
			if ((m2 - m1) < MINUTES_MINI_HSUP) {
				setErrorMsg("La durée minimale pour la saisie d'heures supplémentaires est de " + MINUTES_MINI_HSUP + " minutes");
				return false;
			}

			Jour jour = lePlanning.leJour(dateDebut());

			// pas de verif pour les dimanche, jours feries, jours chomes, jours de
			// conges
			// jours de fermeture
			if (!jour.isDimanche() &&
					!jour.isFerie() &&
					!jour.isChome() &&
					!jour.isFerme() &&
					!jour.isCongeJourneeComplete() &&
					!jour.isCongeLegalJourneeComplete()) {

				int h1, h2, h3, h4;
				h1 = h2 = h3 = h4 = 0;

				int jourSemaine = calendar.get(Calendar.DAY_OF_WEEK);
				// recuperation des minutes du jour pour cet horaire
				// -2 car dimanche = 1, lundi = 2 ...
				NSArray listDebutFin = NSArray.componentsSeparatedByString(horaire.horaires(), ",");
				String coupleMatinAprem = (String) listDebutFin.objectAtIndex(jourSemaine - 2);

				if (";".equals(coupleMatinAprem) == false) {

					NSArray horaireJournalier = NSArray.componentsSeparatedByString(coupleMatinAprem, ";");

					if (horaireJournalier.count() > 0) {
						String matin = (String) horaireJournalier.objectAtIndex(0);
						NSArray matinDebutFin = NSArray.componentsSeparatedByString(matin, "|");
						if (matinDebutFin.count() > 0) {
							h1 = TimeCtrl.getMinutes((String) matinDebutFin.objectAtIndex(0));
							h2 = TimeCtrl.getMinutes((String) matinDebutFin.objectAtIndex(1));
						}
					}
					if (horaireJournalier.count() > 1) {
						String aprem = (String) horaireJournalier.objectAtIndex(1);
						NSArray apremDebutFin = NSArray.componentsSeparatedByString(aprem, "|");
						if (apremDebutFin.count() > 0) {
							h3 = TimeCtrl.getMinutes((String) apremDebutFin.objectAtIndex(0));
							h4 = TimeCtrl.getMinutes((String) apremDebutFin.objectAtIndex(1));
						}
					}
				}

				// l'heure supp ne doit pas etre inclus dans une plage horaire

				// travaille le matin
				if ((h1 != 0) && !jour.isCongeAM() && !jour.isCongeLegalAM()) {
					if (((m1 == h1) && (m2 == h2)) || ((m1 > h1) && (m1 < h2)) || ((m2 > h1) && (m2 < h2)) || ((m1 < h2) && (m2 > h2)) || ((m1 < h1) && (m2 > h1))) {
						setErrorMsg("La saisie de vos heures supplémentaires correspond à un horaire de travail.\n " +
								"Vous travaillez le matin de " + TimeCtrl.stringForMinutes(h1) + " à " + TimeCtrl.stringForMinutes(h2) + ".");
						return false;
					}
				}

				// travaille l'apres-midi
				if ((h3 != 0) && !jour.isCongePM() && !jour.isCongeLegalPM()) {
					if (((m1 == h3) && (m2 == h4)) || ((m1 > h3) && (m1 < h4)) || ((m2 > h3) && (m2 < h4)) || ((m1 < h3) && (m2 > h3)) || ((m1 < h4) && (m2 > h4))) {
						setErrorMsg("La saisie de vos heures supplémentaires correspond à un horaire de travail.\n" +
								"Vous travaillez l'après-midi de " + TimeCtrl.stringForMinutes(h3) + " à " + TimeCtrl.stringForMinutes(h4) + ".");
						return false;
					}
				}
			}
		}

		return isValide;
	}

	public String dureeComptabilisee() {
		calculerValeur();
		return TimeCtrl.stringDureeToHeure(TimeCtrl.stringForMinutes(laValeur()));
	}

	public void calculerValeur() {
		// Le debit de cette occupation est egal
		// fonction de la periode de l'absence
		// - coef "normal" : 1.1
		// - samedi matin, 1.2 si les 5 j de la semaine sont travaill�s
		// - samedi aprem, dimanche et jour feries : 1.5

		Jour jour = lePlanning.leJour(dateDebut());
		laValeur = calculValeurChronologique(jour);
	}

	/**
	 * Calculer la valeur comptabilisee d'une periode d'heures supplementaires. On
	 * avance progressivement dans le temps en appliquant les bonifications : -
	 * 00h00 -> 04h59 : 1,5 - 05h00 -> 06h59 : 1,1 - 07h00 -> 19h00 : 1 - 19h01 ->
	 * 22h00 : 1,1 - 22h01 -> 23h59 : 1,5
	 * 
	 * @return
	 */
	private int calculValeurChronologique(Jour jour) {
		int minutes = 0;

		int mDebut = TimeCtrl.getMinutesOfDay(dateDebut());
		int mFin = TimeCtrl.getMinutesOfDay(dateFin());

		// variables locales a chaque intervalle
		int mLocalDebut, mLocalFin = 0;

		// indique s'il faut continuer d'avancer dans la journee pour faire les
		// calculs
		boolean shouldContinue = true;

		// coefficient a appliquer en bonification
		float coef = (float) 0;

		// determiner si on est dans le cas 10 1/2 journ�e travaill�es un samedi
		boolean isTravail10DemiJournees = false;
		if (jour.isSamedi()) {
			isTravail10DemiJournees = isTravail10DemiJournees(jour);
		}

		// plage 00h00 -> 4h59
		if (mDebut < MINUTES_05H00) {
			//
			mLocalDebut = mDebut;
			if (mFin <= MINUTES_05H00) {
				shouldContinue = false;
				mLocalFin = mFin;
			} else {
				mLocalFin = MINUTES_05H00;
			}
			// determiner le coefficient le plus avantageux entre
			// celui 22h00-05h00 et celui du samedi matin 10 1/2 journee et celui des
			// dimanche / JF
			coef = coefHsup22h00_05h00;
			if (jour.isSamedi() && isTravail10DemiJournees && coefHsupSamMat5j > coef) {
				coef = coefHsupSamMat5j;
			} else if (jour.isDimanche() || jour.isFerie()) {
				coef = coefHsupSamApremDimJf;
			}
			//
			minutes += (int) ((float) (mLocalFin - mLocalDebut) * coef);
		}

		// plage 05h00 -> 6h59
		if (shouldContinue && mDebut < MINUTES_07H00) {
			//
			if (mDebut <= MINUTES_05H00) {
				mLocalDebut = MINUTES_05H00;
			} else {
				mLocalDebut = mDebut;
			}
			if (mFin <= MINUTES_07H00) {
				shouldContinue = false;
				mLocalFin = mFin;
			} else {
				mLocalFin = MINUTES_07H00;
			}
			// determiner le coefficient le plus avantageux entre
			// celui 05h00-07h00 et celui du samedi matin 10 1/2 journee et celui des
			// dimanche / JF
			coef = coefHsupDebordement;
			if (jour.isSamedi() && isTravail10DemiJournees && coefHsupSamMat5j > coef) {
				coef = coefHsupSamMat5j;
			} else if (jour.isDimanche() || jour.isFerie()) {
				coef = coefHsupSamApremDimJf;
			}
			//
			minutes += (int) (((float) (mLocalFin - mLocalDebut)) * coef);
		}
		/*
		 * // plage 07h00 -> 19h00 if (shouldContinue && mDebut <= MINUTES_19H00) {
		 * if (mDebut <= MINUTES_07H00) { mLocalDebut = MINUTES_07H00; } else {
		 * mLocalDebut = mDebut; } if (mFin <= MINUTES_19H00) { shouldContinue =
		 * false; mLocalFin = mFin; } else { mLocalFin = MINUTES_19H00; } minutes +=
		 * (int)(float)(mLocalFin-mLocalDebut);
		 * LRLog.log(TimeCtrl.stringForMinutes(mLocalDebut) + "-" +
		 * TimeCtrl.stringForMinutes(mLocalFin) + " : " +
		 * (int)((float)(mLocalFin-mLocalDebut)*1)); }
		 */

		// plage 07h00 -> 12h30
		if (shouldContinue && mDebut <= MINUTES_12H30) {
			if (mDebut <= MINUTES_07H00) {
				mLocalDebut = MINUTES_07H00;
			} else {
				mLocalDebut = mDebut;
			}
			if (mFin <= MINUTES_12H30) {
				shouldContinue = false;
				mLocalFin = mFin;
			} else {
				mLocalFin = MINUTES_12H30;
			}
			// determiner le coefficient le plus avantageux entre
			// celui 07h00-19h00 et celui du samedi matin 10 1/2 journee et celui des
			// dimanche / JF
			coef = (float) 1;
			if (jour.isSamedi() && isTravail10DemiJournees && coefHsupSamMat5j > coef) {
				coef = coefHsupSamMat5j;
			} else if (jour.isDimanche() || jour.isFerie()) {
				coef = coefHsupSamApremDimJf;
			}
			minutes += (int) ((float) (mLocalFin - mLocalDebut) * coef);
		}

		// plage 12h31 -> 19h30
		if (shouldContinue && mDebut <= MINUTES_19H00) {
			if (mDebut <= MINUTES_12H30) {
				mLocalDebut = MINUTES_12H30;
			} else {
				mLocalDebut = mDebut;
			}
			if (mFin <= MINUTES_19H00) {
				shouldContinue = false;
				mLocalFin = mFin;
			} else {
				mLocalFin = MINUTES_19H00;
			}
			// determiner le coefficient le plus avantageux entre
			// celui 07h00-19h00 et celui des dimanche / JF
			coef = (float) 1;
			if (jour.isSamedi() || jour.isDimanche() || jour.isFerie()) {
				coef = coefHsupSamApremDimJf;
			}
			minutes += (int) ((float) (mLocalFin - mLocalDebut) * coef);
		}

		// plage 19h01 -> 22h00
		if (shouldContinue && mDebut <= MINUTES_22H00) {
			//
			if (mDebut <= MINUTES_19H00) {
				mLocalDebut = MINUTES_19H00;
			} else {
				mLocalDebut = mDebut;
			}
			if (mFin <= MINUTES_22H00) {
				shouldContinue = false;
				mLocalFin = mFin;
			} else {
				mLocalFin = MINUTES_22H00;
			}
			// determiner le coefficient le plus avantageux entre
			// celui 19h00-22h00 et celui des dimanche / JF
			coef = coefHsupDebordement;
			if (jour.isSamedi() || jour.isDimanche() || jour.isFerie()) {
				coef = coefHsupSamApremDimJf;
			}
			//
			minutes += (int) ((float) (mLocalFin - mLocalDebut) * coef);
		}

		// plage 22h01 -> 23h59
		if (shouldContinue) {
			//
			if (mDebut <= MINUTES_22H00) {
				mLocalDebut = MINUTES_22H00;
			} else {
				mLocalDebut = mDebut;
			}
			// on rajoute le bout de 1 minutes si la saisie est 23h59
			if (mFin == MINUTES_23H59) {
				mLocalFin = MINUTES_23H59 + 1;
			} else {
				mLocalFin = mFin;
			}

			// determiner le coefficient le plus avantageux entre
			// celui 19h00-22h00 et celui des dimanche / JF
			coef = coefHsup22h00_05h00;
			if (jour.isSamedi() || jour.isDimanche() || jour.isFerie()) {
				coef = coefHsupSamApremDimJf;
			}
			minutes += (int) ((float) (mLocalFin - mLocalDebut) * coef);
		}

		return minutes;
	}

	/**
	 * Indique si pour un samedi, toutes les journees de travail de la semaine
	 * (lundi au vendredi) sont travaillée sur leur 2 demi journées.
	 * 
	 * TODO faire le contrôle sur {@link Jour#isTravailleAM()} et
	 * {@link Jour#isTravaillePM()}
	 * 
	 * @return
	 */
	private boolean isTravail10DemiJournees(Jour jour) {
		boolean result = true;
		NSArray jours = lePlanning.lesJours(dateDebut().timestampByAddingGregorianUnits(0, 0, -5, 0, 0, 0), dateDebut());
		for (int i = 0; i < jours.count(); i++) {
			Jour unJour = (Jour) jours.objectAtIndex(i);
			if ((unJour.isLundi() || unJour.isMardi() || unJour.isMercredi() || unJour.isJeudi() || unJour.isVendredi())) {
				if (unJour.isChome() ||
						unJour.isCongeAM() || unJour.isCongePM() || unJour.isCongeJourneeComplete() ||
						unJour.isFerie() ||
						unJour.isCongeLegalAM() || unJour.isCongeLegalPM() || unJour.isCongeLegalJourneeComplete() ||
						unJour.isFerme()) {
					result = false;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Obtenir la valeur en minutes d'une fin d'heure sup. On ajoute 24h autant de
	 * fois qu'il y a de jours d'ecart
	 * 
	 * @return
	 */
	/*
	 * private int getEnd(NSTimestamp debut, NSTimestamp fin) { int nbJours =
	 * DateCtrlConges.nbJoursEntre(debut, fin); int minutes =
	 * TimeCtrl.getMinutesOfDay(fin); return nbJours*MINUTES_24H00 + minutes; }
	 */

	public void confirmer() {

	}

}
