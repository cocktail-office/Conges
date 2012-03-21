package fr.univlr.cri.conges;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;

import fr.univlr.cri.conges.constantes.ConstsOccupation;
import fr.univlr.cri.conges.databus.CngDroitBus;
import fr.univlr.cri.conges.databus.CngPlanningBus;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOOccupation;
import fr.univlr.cri.conges.eos.modele.planning.I_ClasseMetierNotificationParametre;
import fr.univlr.cri.conges.objects.Parametre;
import fr.univlr.cri.conges.objects.Planning;
import fr.univlr.cri.conges.print.CngPdfBoxCtrl;
import fr.univlr.cri.conges.print.ConstsPrint;
import fr.univlr.cri.conges.print.PrintDemandeCongePapier;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.util.wo5.DateCtrl;

/**
 * Classe qui rassemble tout ce que est nécéssaire pour faire une edition de
 * demande de congé.
 * 
 * @author ctarade
 */
public abstract class A_PagePrintDemCongePapier
		extends YCRIWebPage
		implements I_ClasseMetierNotificationParametre {

	// afficher l'edition papier pour toute demande de conge
	protected static boolean isShowDemandeCongesPapier;

	public A_PagePrintDemCongePapier(WOContext context) {
		super(context);
	}

	/**
	 * @see I_ClasseMetierNotifiableParametre
	 */
	public static void initStaticField(Parametre parametre) {
		if (parametre == Parametre.PARAM_SHOW_DEMANDE_CONGES_PAPIER) {
			isShowDemandeCongesPapier = parametre.getParamValueBoolean().booleanValue();
		}
	}

	/**
	 * L'occupation a imprimer
	 */
	public abstract EOOccupation recOccupationAImprimer();

	/**
	 * On affiche l'edition uniquement sur les absences en cours de validation,
	 * mais pas les conges legaux / fermeture
	 */
	public boolean isShowPrintDemandeCongePapier() {
		return isShowPrintDemandeCongePapier(recOccupationAImprimer());
	}

	private NSArray occupationAImprimerArray() {
		return laSession.getLePlanningSelectionne().affectationAnnuelle().occupations();
	}

	/**
	 * On affiche l'edition uniquement sur les absences en cours de validation,
	 * mais pas les conges legaux / fermeture
	 */
	private boolean isShowPrintDemandeCongePapier(EOOccupation occ) {
		boolean show = false;
		if (occ != null &&
				isShowDemandeCongesPapier &&
				!occ.isCongeLegal() &&
				!occ.isFermeture()) {
			// on ignore les occupations deja validees ou en cours de suppression
			if (!occ.isValidee() &&
					!occ.isEnCoursDeSuppression()) {
				show = true;
			}
		}
		return show;
	}

	/**
	 * On affiche l'edition uniquement sur les absences en cours de validation,
	 * mais pas les conges legaux / fermeture
	 */
	private boolean isShowPrintDemandeCongePapierJMC(EOOccupation occ) {
		boolean show = false;
		if (occ != null &&
				isShowDemandeCongesPapier &&
				!occ.isCongeLegal() &&
				!occ.isFermeture()) {
			// on ignore les occupations deja validees ou en cours de suppression
			if (!occ.isSupprimee() && !occ.isEnCoursDeSuppression()) {
				show = true;
			}
		}
		return show;
	}

	/**
	 * Classe controleur pour l'impression de la demande papier
	 */
	public class PdfBoxDemandeCongePapierCtrl extends CngPdfBoxCtrl {
		public PdfBoxDemandeCongePapierCtrl(Class aGenericSixPrintClass, EOEditingContext anEc) {
			super(aGenericSixPrintClass, anEc);
		}

		public NSDictionary buildDico() {
			NSMutableDictionary dico = new NSMutableDictionary();

			EOAffectationAnnuelle recAffAnn = laSession.selectedAffAnnPerso();
			// date impression
			dico.setObjectForKey(DateCtrl.dateToString(DateCtrl.now()), ConstsPrint.XML_KEY_DATE_IMPRESSION);
			// service
			if (!StringCtrl.isEmpty(recAffAnn.structure().libelleCourt())) {
				dico.setObjectForKey(recAffAnn.structure().libelleCourt(), ConstsPrint.XML_KEY_SERVICE_LIBELLE_COURT);
			} else {
				dico.setObjectForKey("", ConstsPrint.XML_KEY_SERVICE_LIBELLE_COURT);
			}
			dico.setObjectForKey(recAffAnn.structure().libelleLong(), ConstsPrint.XML_KEY_SERVICE_LIBELLE_LONG);
			// nom prenom
			dico.setObjectForKey(recAffAnn.individu().nom(), ConstsPrint.XML_KEY_NOM_DEMANDEUR);
			dico.setObjectForKey(recAffAnn.individu().prenom(), ConstsPrint.XML_KEY_PRENOM_DEMANDEUR);
			// grade
			String grade = recAffAnn.individu().getLibelleGradeForIndividu(recAffAnn);
			dico.setObjectForKey(grade, ConstsPrint.XML_KEY_GRADE_DEMANDEUR);
			// infos sur l'absence
			dico.setObjectForKey(recOccupationAImprimer().type(), ConstsPrint.XML_KEY_TYPE_OCCUPATION);

			// calcul conges restants avant et apres
			Planning planning = laSession.getLePlanningSelectionne();
			// int minutesApresDemande = planning.congesGlobalRestantsEnMinutes();
			// int minutesAvantDemande = minutesApresDemande -
			// recOccupationAImprimer().valeurMinutes().intValue();

			int minutesApresDemande = planning.congesGlobalRestantsEnMinutes();
			int minutesAvantDemande = minutesApresDemande + recOccupationAImprimer().valeurMinutes().intValue();

			String joursApresDemande = TimeCtrl.to_duree_en_jours(minutesApresDemande, planning.dureeJour());
			String joursAvantDemande = TimeCtrl.to_duree_en_jours(minutesAvantDemande, planning.dureeJour());
			String heuresApresDemande = DateCtrlConges.to_duree(minutesApresDemande);
			String heuresAvantDemande = DateCtrlConges.to_duree(minutesAvantDemande);
			String dureeJournee = TimeCtrl.stringHeureToDuree(planning.dureeJour());

			dico.setObjectForKey(joursAvantDemande, ConstsPrint.XML_KEY_CONGES_RESTANTS_AVANT_DEMANDE_EN_JOURS);
			dico.setObjectForKey(joursApresDemande, ConstsPrint.XML_KEY_CONGES_RESTANTS_APRES_DEMANDE_EN_JOURS);
			dico.setObjectForKey(heuresAvantDemande, ConstsPrint.XML_KEY_CONGES_RESTANTS_AVANT_DEMANDE_EN_HEURES);
			dico.setObjectForKey(heuresApresDemande, ConstsPrint.XML_KEY_CONGES_RESTANTS_APRES_DEMANDE_EN_HEURES);
			dico.setObjectForKey(dureeJournee, ConstsPrint.XML_KEY_DUREE_JOURNEE);

			dico.setObjectForKey(recOccupationAImprimer().duree(), ConstsPrint.XML_KEY_DUREE_OCCUPATION);
			String motif = "";
			if (!StringCtrl.isEmpty(recOccupationAImprimer().motif())) {
				motif = recOccupationAImprimer().motif();
			}
			dico.setObjectForKey(motif, ConstsPrint.XML_KEY_MOTIF_OCCUPATION);

			// selon le type d'occupation, on affiche soit le detail des heures, soit
			// AM/PM
			String dateDebutStr = DateCtrl.dateToString(recOccupationAImprimer().dateDebut());
			String dateFinStr = DateCtrl.dateToString(recOccupationAImprimer().dateFin());

			GregorianCalendar dateDebutGC = new GregorianCalendar();
			GregorianCalendar dateFinGC = new GregorianCalendar();
			dateDebutGC.setTime(recOccupationAImprimer().dateDebut());
			dateFinGC.setTime(recOccupationAImprimer().dateFin());

			if (recOccupationAImprimer().isOccupationJour()) {
				if (dateDebutGC.get(GregorianCalendar.AM_PM) == GregorianCalendar.AM) {
					dateDebutStr += " " + ConstsOccupation.OCC_MATIN;
				} else {
					dateDebutStr += " " + ConstsOccupation.OCC_APREM;
				}
				if (dateFinGC.get(GregorianCalendar.AM_PM) == GregorianCalendar.AM) {
					dateFinStr += " " + ConstsOccupation.OCC_MATIN;
				} else {
					dateFinStr += " " + ConstsOccupation.OCC_APREM;
				}
			} else {
				// recuperation des heures
				dateDebutStr += " " + dateDebutGC.get(Calendar.HOUR_OF_DAY) + ":" + (dateDebutGC.get(Calendar.MINUTE) < 10 ? "0" : "") + dateDebutGC.get(Calendar.MINUTE);
				dateFinStr += " " + dateFinGC.get(Calendar.HOUR_OF_DAY) + ":" + (dateFinGC.get(Calendar.MINUTE) < 10 ? "0" : "") + dateFinGC.get(Calendar.MINUTE);
			}
			dico.setObjectForKey(dateDebutStr, ConstsPrint.XML_KEY_DATE_DEBUT_OCCUPATION);
			dico.setObjectForKey(dateFinStr, ConstsPrint.XML_KEY_DATE_FIN_OCCUPATION);

			// responsables (on affiche aussi ceux qui ne veulent pas recevoir les
			// mails)
			try {
				NSArray viseurs = planningBus().viseurs(droitBus(), recAffAnn, recOccupationAImprimer(), true);
				dico.setObjectForKey(viseurs.valueForKey("nomComplet"), ConstsPrint.XML_KEY_VISEURS);
				NSArray responsables = planningBus().responsables(droitBus(), recAffAnn, recOccupationAImprimer(), true);
				dico.setObjectForKey(responsables.valueForKey("nomComplet"), ConstsPrint.XML_KEY_RESPONSABLES);
			} catch (Exception e) {
				e.printStackTrace();
			}

			NSMutableDictionary dicoAllDemande = new NSMutableDictionary();
			NSArray arrayAllDico = new NSArray(dico.immutableClone());
			dicoAllDemande.setObjectForKey(arrayAllDico, ConstsPrint.DEMANDE_ARRAY_DICO);

			// URL du logo
			dicoAllDemande.setObjectForKey(app.mainLogoUrl(), ConstsPrint.XML_KEY_MAIN_LOGO_URL);

			return dicoAllDemande.immutableClone();
		}

		/**
		 * Le nom de fichier. On ajoute l'oid de l'occupation pour différencier deux
		 * occupations du même type intervenant le même jour
		 */
		public String fileName() {
			EOAffectationAnnuelle recAffAnn = laSession.selectedAffAnnPerso();
			return "DemandeCongePapier_" + StringCtrl.toBasicString(
					recAffAnn.individu().nomComplet() + "_" +
							recOccupationAImprimer().type() + "_" +
							Integer.toString(recOccupationAImprimer().oid().intValue()));
		}
	}

	/** */
	public PdfBoxDemandeCongePapierCtrl ctrlDemandeCongePapierCtrl() {
		return new PdfBoxDemandeCongePapierCtrl(PrintDemandeCongePapier.class, edc);
	}

	// raccourcis bus de donnees

	private CngPlanningBus planningBus() {
		return laSession.cngDataCenter().planningBus();
	}

	private CngDroitBus droitBus() {
		return laSession.cngDataCenter().droitBus();
	}
}
