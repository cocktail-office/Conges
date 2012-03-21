/*
 * Copyright COCKTAIL (www.cocktail.org), 1995, 2010 This software
 * is governed by the CeCILL license under French law and abiding by the
 * rules of distribution of free software. You can use, modify and/or
 * redistribute the software under the terms of the CeCILL license as
 * circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * As a counterpart to the access to the source code and rights to copy, modify
 * and redistribute granted by the license, users are provided only with a
 * limited warranty and the software's author, the holder of the economic
 * rights, and the successive licensors have only limited liability. In this
 * respect, the user's attention is drawn to the risks associated with loading,
 * using, modifying and/or developing or reproducing the software by the user
 * in light of its specific status of free software, that may mean that it
 * is complicated to manipulate, and that also therefore means that it is
 * reserved for developers and experienced professionals having in-depth
 * computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling
 * the security of their systems and/or data to be ensured and, more generally,
 * to use and operate it in the same conditions as regards security. The
 * fact that you are presently reading this means that you have had knowledge
 * of the CeCILL license and that you accept its terms.
 */

package fr.univlr.cri.conges.constantes;

import com.webobjects.foundation.NSArray;

/**
 * Les constantes relatives a tous les objets de type <code>Jour</code>
 * 
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */

public interface ConstsJour {

	/** les etats possible d'un jour */
	public final static int STATUS_SANS_STATUS = 0;
	public final static int STATUS_TRAVAILLE = (int) Math.pow(2, 0);
	public final static int STATUS_TRAVAILLE_AM = (int) Math.pow(2, 1);
	public final static int STATUS_TRAVAILLE_PM = (int) Math.pow(2, 2);
	public final static int STATUS_CONGE = (int) Math.pow(2, 3);
	public final static int STATUS_CONGE_AM = (int) Math.pow(2, 4);
	public final static int STATUS_CONGE_PM = (int) Math.pow(2, 5);
	public final static int STATUS_CONGE_LEGAL = (int) Math.pow(2, 6);
	public final static int STATUS_CONGE_LEGAL_AM = (int) Math.pow(2, 7);
	public final static int STATUS_CONGE_LEGAL_PM = (int) Math.pow(2, 8);
	public final static int STATUS_CONGE_DRH = (int) Math.pow(2, 22);
	public final static int STATUS_CONGE_DRH_AM = (int) Math.pow(2, 23);
	public final static int STATUS_CONGE_DRH_PM = (int) Math.pow(2, 24);
	public final static int STATUS_FERMETURE = (int) Math.pow(2, 9);
	public final static int STATUS_FERIE = (int) Math.pow(2, 10);
	public final static int STATUS_CHOME = (int) Math.pow(2, 11);
	public final static int STATUS_HORS_AFFECTATION = (int) Math.pow(2, 12);
	public final static int STATUS_HORAIRE_FORCE = (int) Math.pow(2, 13);
	public final static int STATUS_CONGES_COMP = (int) Math.pow(2, 14);

	/**
	 * etats pour la mise en valeur des occupations sur le planning pour
	 * validation
	 */
	public final static int HIGHLIGHT_OCC_FIRST_DAY = (int) Math.pow(2, 15);
	public final static int HIGHLIGHT_OCC_MIDDLE_DAY = (int) Math.pow(2, 16);
	public final static int HIGHLIGHT_OCC_LAST_DAY = (int) Math.pow(2, 17);
	public final static int HIGHLIGHT_OCC_ONLY_DAY = (int) Math.pow(2, 18);
	public final static int HIGHLIGHT_TODAY = (int) Math.pow(2, 19);

	/** etats de validation de l'occupation */
	public final static int STATUS_EN_COURS_DE_VALIDATION = (int) Math.pow(2, 20);
	public final static int STATUS_EN_COURS_DE_SUPPRESSION = (int) Math.pow(2, 21);

	/** classe css d'un jour sur le planning selon son etat */
	public final static String CSS_CLASS_JOUR_NORMAL = "divJour";
	public final static String CSS_CLASS_JOUR_CHOME = "divJourW";
	public final static String CSS_CLASS_JOUR_FERME = "divJourF";
	public final static String CSS_CLASS_JOUR_SANS_STATUT = "divJourSS";
	public final static String CSS_CLASS_JOUR_HORS_AFFECTATION = "divJourHA";
	public final static String CSS_CLASS_JOUR_CONGE = "divJourC";
	public final static String CSS_CLASS_JOUR_CONGE_V = "divJourCV";
	public final static String CSS_CLASS_JOUR_CONGE_S = "divJourCS";
	public final static String CSS_CLASS_JOUR_CONGE_AM = "divJourCAM";
	public final static String CSS_CLASS_JOUR_CONGE_AM_V = "divJourCAMV";
	public final static String CSS_CLASS_JOUR_CONGE_AM_S = "divJourCAMS";
	public final static String CSS_CLASS_JOUR_CONGE_PM = "divJourCPM";
	public final static String CSS_CLASS_JOUR_CONGE_PM_V = "divJourCPMV";
	public final static String CSS_CLASS_JOUR_CONGE_PM_S = "divJourCPMS";
	public final static String CSS_CLASS_JOUR_CONGE_LEGAL = "divJourCL";
	public final static String CSS_CLASS_JOUR_CONGE_LEGAL_AM = "divJourCLAM";
	public final static String CSS_CLASS_JOUR_CONGE_LEGAL_PM = "divJourCLPM";
	public final static String CSS_CLASS_JOUR_CONGE_COMP = "divJourCC";
	public final static String CSS_CLASS_JOUR_CONGE_COMP_V = "divJourCCV";
	public final static String CSS_CLASS_JOUR_CONGE_COMP_S = "divJourCCS";

	/** l'alpha des etats pour composer les id des jours */
	public final static String CSS_ID_JOUR_NORMAL = "Ja";
	public final static String CSS_ID_JOUR_CHOME = "Jb";
	public final static String CSS_ID_JOUR_FERME = "Jc";
	public final static String CSS_ID_JOUR_SANS_STATUT = "Jd";
	public final static String CSS_ID_JOUR_HORS_AFFECTATION = "Je";
	public final static String CSS_ID_JOUR_CONGE = "Jf";
	public final static String CSS_ID_JOUR_CONGE_AM = "Jg";
	public final static String CSS_ID_JOUR_CONGE_PM = "Jh";
	public final static String CSS_ID_JOUR_CONGE_LEGAL = "Ji";
	public final static String CSS_ID_JOUR_CONGE_LEGAL_AM = "Jj";
	public final static String CSS_ID_JOUR_CONGE_LEGAL_PM = "Jk";
	public final static String CSS_ID_JOUR_CONGE_COMP = "Jl";
	public final static String CSS_ID_JOUR_CONGE_V = "Jm";
	public final static String CSS_ID_JOUR_CONGE_AM_V = "Jn";
	public final static String CSS_ID_JOUR_CONGE_PM_V = "Jo";
	public final static String CSS_ID_JOUR_CONGE_COMP_V = "Jp";
	public final static String CSS_ID_JOUR_CONGE_S = "Jq";
	public final static String CSS_ID_JOUR_CONGE_AM_S = "Jr";
	public final static String CSS_ID_JOUR_CONGE_PM_S = "Js";
	public final static String CSS_ID_JOUR_CONGE_COMP_S = "Jt";

	/**
	 * classe css d'un jour sur le planning pour mise en valeur lors de la
	 * validation
	 */
	public final static String CSS_CLASS_HIGHLIGHT_TODAY = "divJourToday";
	public final static String CSS_CLASS_HIGHLIGHT_OCC_FIRST_DAY = "divJourOccFirstDay";
	public final static String CSS_CLASS_HIGHLIGHT_OCC_MIDDLE_DAY = "divJourOccMiddleDay";
	public final static String CSS_CLASS_HIGHLIGHT_OCC_LAST_DAY = "divJourOccLastDay";
	public final static String CSS_CLASS_HIGHLIGHT_OCC_ONLY_DAY = "divJourOccOnlyDay";

	public final static NSArray INITIALES_LIBELLES_JOURS = new NSArray(new String[] { "D", "L", "M", "M", "J", "V", "S" });

	/** la duree d'une journee de 7h00 en minutes */
	public final static int DUREE_JOUR_7H00 = 420;

	// les noms des methodes retournant l'etat du jour
	public final static String STATUS_IS_JOUR_TRAVAILLE_IMPOT = "isJourTravailleImpot";
	public final static String STATUS_IS_CONGE_LEGAL_JOURNEE_COMPLETE = "isCongeLegalJourneeComplete";
	public final static String STATUS_IS_TRAVAILLE = "isTravaille";
	public final static String STATUS_IS_TRAVAILLE_AM = "isTravailleAM";
	public final static String STATUS_IS_TRAVAILLE_PM = "isTravaillePM";

}
