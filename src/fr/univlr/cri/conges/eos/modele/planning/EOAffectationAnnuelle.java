package fr.univlr.cri.conges.eos.modele.planning;

import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;

import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSRange;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.Application;
import fr.univlr.cri.conges.PageAdminDroits;
import fr.univlr.cri.conges.PageAdminJrti;
import fr.univlr.cri.conges.constantes.ConstsDroit;
import fr.univlr.cri.conges.constantes.ConstsJour;
import fr.univlr.cri.conges.constantes.ConstsOccupation;
import fr.univlr.cri.conges.constantes.ConstsPlanning;
import fr.univlr.cri.conges.eos.modele.conges.EODroit;
import fr.univlr.cri.conges.eos.modele.conges.EORepartValidation;
import fr.univlr.cri.conges.eos.modele.grhum.EOAbsenceGepeto;
import fr.univlr.cri.conges.eos.modele.grhum.EOAffectation;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;
import fr.univlr.cri.conges.objects.CetFactory;
import fr.univlr.cri.conges.objects.Parametre;
import fr.univlr.cri.conges.objects.PlanningCalcul;
import fr.univlr.cri.conges.objects.Responsabilite;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.ArrayCtrl;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.util.wo5.DateCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRLog;
import fr.univlr.cri.webapp.LRSort;
import fr.univlr.cri.ycrifwk.utils.UtilDb;

public class EOAffectationAnnuelle
		extends _EOAffectationAnnuelle
		implements I_ClasseMetierNotificationParametre {

	// ordering des objets
	public final static NSArray SORT_INDIVIDU = LRSort.newSort(
				EOAffectationAnnuelle.INDIVIDU_KEY + "." + EOIndividu.NOM_KEY + "," +
						EOAffectationAnnuelle.INDIVIDU_KEY + "." + EOIndividu.PRENOM_KEY);
	public final static NSArray SORT_STRUCTURE_ET_INDIVIDU = LRSort.newSort(
				EOAffectationAnnuelle.STRUCTURE_KEY + "." + EOStructure.LIBELLE_LONG_KEY + "," +
						EOAffectationAnnuelle.INDIVIDU_KEY + "." + EOIndividu.NOM_KEY + "," +
						EOAffectationAnnuelle.INDIVIDU_KEY + "." + EOIndividu.PRENOM_KEY);
	public final static NSArray SORT_DATE_DEBUT_ANNEE = LRSort.newSort(
			DATE_DEBUT_ANNEE_KEY);

	// liens
	public final static String INDIVIDU_KEY = "individu";
	public final static String STRUCTURE_KEY = "structure";

	//
	private static NSDictionary volumeHoraireAnnuel;
	private static NSDictionary dateMaxReliquat;
	private static NSDictionary dateDebutDemandeCet;
	private static NSDictionary dateFinDemandeCet;
	private static NSDictionary seuilCongesConsommesJour7h00Nm1PourEpargneCet;
	private static NSDictionary seuilReliquatHeuresPourEpargneCet;
	private static NSDictionary plafondEpargneCet;

	// tips HTML
	private final static String HTML_TIP_DEP_SEM_HAUTES =
			"L'agent peut associer plus de 8 semaines<br>tr&egrave;s hautes, et " +
					"au maximum 10 (si non<br>coch&eacute;, ce maximum est ramen&eacute; &agrave; 8)";
	private final static String HTML_TIP_HORS_NORME =
			"L'agent a acc&egrave;s aux horaires<br/>typ&eacute;s <i>hors normes</i>, ex:45h00";
	private final static String HTML_TIP_PASSE_DROIT =
			"L'agent n'est pas soumis aux contraintes sur les plages journali&egrave;res<br/>des " +
					"horaires<br> ex: il pourra embaucher avant 8h00 et d&eacute;baucher avant 16h45 ...";
	private final static String HTML_TIP_CALC_AUTO =
			"D&eacute;termine le mode de calcul des cong&eacute;s de l'agent:<br>- soit la " +
					"diff&eacute;rence entre heures associ&eacute;es au planning et les heures dues " +
					"(case coch&eacute;e)<br>- soit fixer une valeur d&eacute;finitive (case non coch&eacute;e)";
	private final static String HTML_TIP_TPA =
			"L'agent peut saisir des horaires de n'importe quelle dur&eacute;e et quotit&eacute;";
	private final static String HTML_TIP_DEP_CONGES =
			"L'agent n'est pas limit&eacute; en droit maximum &agrave; cong&eacute;s. Il peut<br>" +
					"d&eacute;passer le plafond initial fix&eacute; dans HEURES_CONGES_MAXI.";
	private final static String HTML_TIP_REL_AUTO =
			"Reliquat automatique = cong&eacute;s non consomm&eacute;s sur<br>" +
					"l'ann&eacute;e pr&eacute;c&eacute;dente, <u>sur le m&ecirc;me service</u>.";
	private final static String HTML_TIP_DECHARGE_SYNDICALE =
			"&lt;&agrave; compl&eacute;ter&gt;";

	//
	public final static String IS_CALCUL_AUTOMATIQUE_KEY = "isCalculAutomatique";
	public final static String IS_RELIQUAT_AUTOMATIQUE_KEY = "isReliquatAutomatique";
	public final static String IS_DECHARGE_SYNDICALE_KEY = "isDechargeSyndicale";

	// fetch spec
	public final static String AFFECTATIONS_ANNUELLES_POUR_MOIS_FETCH_SPEC = "AffectationsAnnuellesPourMois";

	/**
	 * Le tip associé à la colonne column
	 * 
	 * @param column
	 * @return
	 */
	public static String getHtmlTipForKey(String key) {
		String tip = "&lt;non document&eacute;&gt;";

		if (key.equals(FLAG_DEP_SEM_HAUTES_KEY)) {
			tip = HTML_TIP_DEP_SEM_HAUTES;
		} else if (key.equals(FLAG_HORS_NORME_KEY)) {
			tip = HTML_TIP_HORS_NORME;
		} else if (key.equals(FLAG_PASSE_DROIT_KEY)) {
			tip = HTML_TIP_PASSE_DROIT;
		} else if (key.equals(IS_CALCUL_AUTOMATIQUE_KEY)) {
			tip = HTML_TIP_CALC_AUTO;
		} else if (key.equals(FLAG_TEMPS_PARTIEL_ANNUALISE_KEY)) {
			tip = HTML_TIP_TPA;
		} else if (key.equals(FLAG_DEPASSEMENT_CONGES_AUTORISE_KEY)) {
			tip = HTML_TIP_DEP_CONGES;
		} else if (key.equals(IS_RELIQUAT_AUTOMATIQUE_KEY)) {
			tip = HTML_TIP_REL_AUTO;
		} else if (key.equals(IS_DECHARGE_SYNDICALE_KEY)) {
			tip = HTML_TIP_DECHARGE_SYNDICALE;
		}

		return tip;
	}

	/**
	 * @deprecated
	 * @see #initStaticField(Parametre)
	 */
	public static void initStaticFields(
			NSDictionary unDicoVolumeHoraireAnnuel,
			NSDictionary unDicoDateMaxReliquat,
			NSDictionary unDicoDateDebutDemandeCet,
			NSDictionary unDicoDateFinDemandeCet,
			NSDictionary unDicoSeuilCongesConsommesJour7h00Nm1PourEpargneCet,
			NSDictionary unDicoSeuilReliquatHeuresPourEpargneCet,
			NSDictionary unDicoPlafondEpargneCet) {
		volumeHoraireAnnuel = unDicoVolumeHoraireAnnuel;
		dateMaxReliquat = unDicoDateMaxReliquat;
		dateDebutDemandeCet = unDicoDateDebutDemandeCet;
		dateFinDemandeCet = unDicoDateFinDemandeCet;
		seuilCongesConsommesJour7h00Nm1PourEpargneCet = unDicoSeuilCongesConsommesJour7h00Nm1PourEpargneCet;
		seuilReliquatHeuresPourEpargneCet = unDicoSeuilReliquatHeuresPourEpargneCet;
		plafondEpargneCet = unDicoPlafondEpargneCet;
	}

	/**
	 * @see I_ClasseMetierNotificationParametre
	 * @param parametre
	 */
	public static void initStaticField(
			Parametre parametre) {
		Application app = ((Application) Application.application());
		if (parametre == Parametre.PARAM_HEURES_DUES) {
			volumeHoraireAnnuel = app.retrieveDicoHeuresDues();
		} else if (parametre == Parametre.PARAM_DATE_MAX_RELIQUAT) {
			dateMaxReliquat = app.retrieveDicoDateMaxReliquat();
		} else if (parametre == Parametre.PARAM_DEBUT_DEMANDE_CET) {
			dateDebutDemandeCet = app.retrieveDicoDateDebutDemandeCet();
		} else if (parametre == Parametre.PARAM_FIN_DEMANDE_CET) {
			dateFinDemandeCet = app.retrieveDicoDateFinDemandeCet();
		} else if (parametre == Parametre.PARAM_SEUIL_CONGES_CONSOMMES_JOUR_7H00_NM1_POUR_EPARGNE_CET) {
			seuilCongesConsommesJour7h00Nm1PourEpargneCet = app.retrieveDicoSeuilCongesConsommesJour7h00Nm1PourEpargneCet();
		} else if (parametre == Parametre.PARAM_SEUIL_RELIQUAT_HEURES_POUR_EPARGNE_CET) {
			seuilReliquatHeuresPourEpargneCet = app.retrieveDicoSeuilReliquatHeuresPourEpargneCet();
		} else if (parametre == Parametre.PARAM_PLAFOND_EPARGNE_CET) {
			plafondEpargneCet = app.retrieveDicoPlafondEpargneCet();
		}
	}

	/**
	 * Remettre à zéro toutes les données stockées en cache
	 */
	public void clearCache() {
		_fermetures = null;
		_joursFeries = null;
		_vacancesScolaires = null;
		cetFactory().clearCache();
	}

	/**
	 * Retourne les heures a faire sur l'annee en cours (pour une affectation a
	 * 100%) Cette valeur est lue depuis le dictionnaire statique
	 * <code>volumeHoraireAnnuel</code> qui contient les parametres etablissement
	 * pour chaque annee universitaire
	 * 
	 * MODIF : les heures sont converties en minutes
	 */
	public int volumeHoraireAnnuel() {
		String heures = dicoStringForDateDebutAnnee(volumeHoraireAnnuel);
		return TimeCtrl.getMinutes(heures);
	}

	public EOAffectationAnnuelle() {
		super();
	}

	public EOCalculAffectationAnnuelle calculAffAnn(String nature) {
		EOCalculAffectationAnnuelle calculAffAnn = null;
		EOQualifier qualifier = EOQualifier.qualifierWithQualifierFormat(
				EOCalculAffectationAnnuelle.FLAG_NATURE_KEY + "=%@", new NSArray(nature));
		calculAffAnn = (EOCalculAffectationAnnuelle) (EOQualifier.filteredArrayWithQualifier(calculAffAnns(), qualifier)).lastObject();

		return calculAffAnn;
	}

	private EOIndividu _individu;

	public EOIndividu individu() {
		// correction bug si l'une des affectations a le temoin TEM_VALIDE = 'N'
		if (_individu == null) {
			int i = 0;
			while (_individu == null && i < periodes().count()) {
				EOPeriodeAffectationAnnuelle periode = (EOPeriodeAffectationAnnuelle) periodes().objectAtIndex(i);
				if (periode.affectation() != null) {
					_individu = periode.affectation().individu();
				}
				i++;
			}
			if (_individu == null) {
				LRLog.log("test " + oid());
			}
		}
		return _individu;
		// return ((EOPeriodeAffectationAnnuelle)
		// periodes().lastObject()).affectation().individu();
	}

	private EOStructure _structure;

	public EOStructure structure() {
		// correction bug si l'une des affectations a le temoin TEM_VALIDE = 'N'
		if (_structure == null) {
			int i = 0;
			while (_structure == null && i < periodes().count()) {
				EOPeriodeAffectationAnnuelle periode = (EOPeriodeAffectationAnnuelle) periodes().objectAtIndex(i);
				if (periode.affectation() != null) {
					_structure = periode.affectation().structure();
				}
				i++;
			}
		}
		return _structure;
		// return ((EOPeriodeAffectationAnnuelle)
		// periodes().lastObject()).affectation().structure();
	}

	/**
	 * Creation d'un groupes d'affectations annuelles a partir d'affectations
	 * Methode modifier pour pouvoir traiter toutes les affectations sans pour
	 * autant faire de controle si l'individu ou la structure precedente est la
	 * meme (on se demande pourquoi d'ailleurs ce test etait fait ...). On
	 * maintiendra plutot une liste d'affectations traitees pour garantir
	 * l'unicite.
	 * 
	 * @param ec
	 * @param affectations
	 *          . La liste des affectation MANGUE. Attention, elle ne doit pas
	 *          contenir de doublons !
	 * @param debutAnnee
	 * @return
	 */
	public static NSArray newEOAffectationsAnnuelles(EOEditingContext ec, NSArray affectations, NSTimestamp debutAnnee) {

		NSArray affectationsAnnuelles = new NSArray();
		Enumeration<?> enumAffectations = affectations.objectEnumerator();

		while (enumAffectations.hasMoreElements()) {
			// l'affectation MANGUE en cours
			EOAffectation uneAffectation = (EOAffectation) enumAffectations.nextElement();
			// l'affectation annuelle HAMAC attendue
			EOAffectationAnnuelle existingAffAnn = EOAffectationAnnuelle.findAffectationAnnuelleInContext(ec, uneAffectation, debutAnnee);
			if (existingAffAnn == null) {
				// pas trouvee dans la base, elle vient peut etre d'etre juste inseree,
				// on regarde
				// donc dans celles toutes fraiches (affectationsAnnuelles)
				for (int i = 0; (existingAffAnn == null) && i < affectationsAnnuelles.count(); i++) {
					EOAffectationAnnuelle processedAffAnn = (EOAffectationAnnuelle) affectationsAnnuelles.objectAtIndex(i);
					// on fait pas la verif sur debutAnnee car toutes celles traitees sont
					// la meme
					for (int j = 0; (existingAffAnn == null) && j < processedAffAnn.periodes().count(); j++) {
						EOPeriodeAffectationAnnuelle processedAffAnnPeriode = (EOPeriodeAffectationAnnuelle) processedAffAnn.periodes().objectAtIndex(j);
						if (processedAffAnnPeriode.affectationAnnuelle().structure() == uneAffectation.structure() &&
								processedAffAnnPeriode.affectationAnnuelle().individu() == uneAffectation.individu()) {
							// ouf, on l'a enfin trouvee ...
							existingAffAnn = processedAffAnn;
						}
					}
				}
			}
			// est-ce que l'affectation annuelle est disponible
			if (existingAffAnn == null) {
				// elle n'existe pas, on la cree
				EOAffectationAnnuelle newAffAnn = newEOAffectationAnnuelle(ec, uneAffectation, debutAnnee);
				if (newAffAnn != null) {
					affectationsAnnuelles = affectationsAnnuelles.arrayByAddingObject(newAffAnn);
					LRLog.log("  > creating EOAffectationAnnuelle (noSeqAffectation=" + uneAffectation.oid().intValue() + ") - " + uneAffectation.individu().nomComplet());
				}
			} else {
				// elle existe, on va donc voir s'il faut lui ajouter une periode
				// liste des periodes attendues pour cette affectation
				NSArray existingsPeriode = EOPeriodeAffectationAnnuelle.findPeriodesForAffectation(ec, uneAffectation);
				// si aucune d'elle n'est pluggee sur existingAffAnn, alors il faut la
				// creer
				boolean shouldAddPeriode = true;
				for (int i = 0; shouldAddPeriode && i < existingsPeriode.count(); i++) {
					EOPeriodeAffectationAnnuelle existingPeriode = (EOPeriodeAffectationAnnuelle) existingsPeriode.objectAtIndex(i);
					if (existingPeriode.affectationAnnuelle() == existingAffAnn) {
						// elle est deja connectee, on a pas besoin de la recreer
						shouldAddPeriode = false;
					}
				}
				if (shouldAddPeriode) {
					existingAffAnn.addPeriode(uneAffectation);
					LRLog.log("      + adding EOPeriodeAffectationAnnuelle (" +
							(existingAffAnn.oid().intValue() != -1 ?
									"oidAffAnn=" + existingAffAnn.oid().intValue() :
									"noSeqAffectation=" + uneAffectation.oid().intValue()) + ") - " +
							uneAffectation.individu().nomComplet());
				}
			}
		}

		return affectationsAnnuelles;
	}

	// TODO Ajouter en parametre si calcul auto et la duree sinon
	/**
	 * ajout d'une affectation annuelle sachant que les dates dDebAffectaion et
	 * dFinAffectation sont correctes
	 * 
	 * @param affectation
	 * @return
	 */
	public static EOAffectationAnnuelle newEOAffectationAnnuelle(EOEditingContext ec, EOAffectation affectation, NSTimestamp debutAnnee) {
		return newEOAffectationAnnuelle(ec, affectation, debutAnnee, affectation.dDebAffectation(), affectation.dFinAffectation());
	}

	/**
	 * ajout d'une affectation annuelle sachant que les dates dDebAffectaion et
	 * dFinAffectation ne sont pas correctes utilise par les DirectAction, alors
	 * que GEPETO / MANGUE n'a pas fait le commit() de modif d'affectation
	 * 
	 * @param affectation
	 * @param dDebAffectation
	 * @param dFinAffectation
	 * @return
	 */
	public static EOAffectationAnnuelle newEOAffectationAnnuelle(EOEditingContext ec, EOAffectation affectation, NSTimestamp debutAnnee,
			NSTimestamp dDebAffectation, NSTimestamp dFinAffectation) {

		StringBuffer sbInfo = new StringBuffer("newEOAffectationAnnuelle() ");
		sbInfo.append(affectation.individu().nomComplet());
		sbInfo.append(", debutAnnee=").append(debutAnnee != null ? DateCtrlConges.dateToString(debutAnnee) : "null");
		sbInfo.append(", affectation.dDebAffectation()=").append(affectation.dDebAffectation() != null ? DateCtrlConges.dateToString(affectation.dDebAffectation()) : "null");
		sbInfo.append(", affectation.dFinAffectation()=").append(affectation.dFinAffectation() != null ? DateCtrlConges.dateToString(affectation.dFinAffectation()) : "null");
		sbInfo.append(", dDebAffectation=").append(dDebAffectation != null ? DateCtrlConges.dateToString(dDebAffectation) : "null");
		sbInfo.append(", dFinAffectation=").append(dFinAffectation != null ? DateCtrlConges.dateToString(dFinAffectation) : "null");

		EOAffectationAnnuelle newEOAffectationAnnuelle = null;

		// verifier que l'affectation doit bien donner lieu a une creation d'un
		// planning sur l'annee
		// pointee par debutAnnee
		NSTimestamp finAnnee = DateCtrlConges.dateToFinAnneeUniv(debutAnnee);
		if ((affectation.dDebAffectation() != null && DateCtrlConges.isAfter(affectation.dDebAffectation(), finAnnee)) ||
				(affectation.dFinAffectation() != null && DateCtrlConges.isBefore(affectation.dFinAffectation(), debutAnnee))) {
			sbInfo.append(" => off period");
		} else {
			// on ne cree pas d'enregistrement en double
			NSArray existingAffAnns = EOAffectationAnnuelle.findAffectationsAnnuelleInContext(ec, affectation.individu().oid(),
					affectation.structure().cStructure(), debutAnnee);

			if (existingAffAnns.count() == 0) {
				newEOAffectationAnnuelle = new EOAffectationAnnuelle();
				newEOAffectationAnnuelle.setStatusPlanning("0");
				newEOAffectationAnnuelle.setDateDebutAnnee(TimeCtrl.dateToMinuit(DateCtrlConges.dateDebutAnnee(debutAnnee)));
				newEOAffectationAnnuelle.setAnnee(DateCtrlConges.anneeUnivForDate(debutAnnee));
				newEOAffectationAnnuelle.addObjectToBothSidesOfRelationshipWithKey(new EOCalculAffectationAnnuelle("T"), CALCUL_AFF_ANNS_KEY);
				newEOAffectationAnnuelle.addObjectToBothSidesOfRelationshipWithKey(new EOCalculAffectationAnnuelle("P"), CALCUL_AFF_ANNS_KEY);
				newEOAffectationAnnuelle.addObjectToBothSidesOfRelationshipWithKey(new EOCalculAffectationAnnuelle("R"), CALCUL_AFF_ANNS_KEY);

				newEOAffectationAnnuelle.addPeriode(affectation, dDebAffectation, dFinAffectation);

				// par defaut on reprend les droits de l'affectation precedente
				NSArray prevAffAnns = EOAffectationAnnuelle.findAffectationsAnnuelleInContext(ec, affectation.individu().oid(),
						affectation.structure().cStructure(), debutAnnee.timestampByAddingGregorianUnits(-1, 0, 0, 0, 0, 0));
				if (prevAffAnns.count() > 0) {
					EOAffectationAnnuelle prevAffAnn = (EOAffectationAnnuelle) prevAffAnns.lastObject();
					newEOAffectationAnnuelle.setFlagHorsNorme(prevAffAnn.flagHorsNorme());
					newEOAffectationAnnuelle.setFlagPasseDroit(prevAffAnn.flagPasseDroit());
					// newEOAffectationAnnuelle.setFlagCalculHoraireAutomatique(prevAffAnn.flagCalculHoraireAutomatique());
					newEOAffectationAnnuelle.setFlagDepSemHautes(prevAffAnn.flagDepSemHautes());
					newEOAffectationAnnuelle.setFlagTempsPartielAnnualise(prevAffAnn.flagTempsPartielAnnualise());
					newEOAffectationAnnuelle.setFlagDepassementCongesAutorise(prevAffAnn.flagDepassementCongesAutorise());
				} else {
					// pas trouve, on affecte les droits par defaut
					newEOAffectationAnnuelle.setFlagHorsNorme("0");
					newEOAffectationAnnuelle.setFlagPasseDroit("0");
					newEOAffectationAnnuelle.setFlagDepSemHautes("0");
					newEOAffectationAnnuelle.setFlagTempsPartielAnnualise("0");
					newEOAffectationAnnuelle.setFlagDepassementCongesAutorise("0");
				}

				sbInfo.append(" => creating record");
			} else {
				sbInfo.append(" => record already exists");
			}
		}

		LRLog.log(sbInfo.toString());

		return newEOAffectationAnnuelle;
	}

	/**
	 * ajout d'une periode sachant que les dates dDebAffectaion et dFinAffectation
	 * sont correctes
	 * 
	 * @param affectation
	 * @return
	 */
	public EOPeriodeAffectationAnnuelle addPeriode(EOAffectation affectation) {
		return addPeriode(affectation, affectation.dDebAffectation(), affectation.dFinAffectation());
	}

	/**
	 * ajout d'une periode sachant que les dates dDebAffectaion et dFinAffectation
	 * ne sont pas correctes utilise par les DirectAction, alors que GEPETO /
	 * MANGUE n'a pas fait le commit() de modif d'affectation
	 * 
	 * @param affectation
	 * @param dDebAffectation
	 * @param dFinAffectation
	 * @return
	 */
	public EOPeriodeAffectationAnnuelle addPeriode(EOAffectation affectation, NSTimestamp dDebAffectation, NSTimestamp dFinAffectation) {
		// Ajout d'une periode a une affectation
		EOPeriodeAffectationAnnuelle newPeriode = null;
		NSTimestamp dateDebut = null;
		NSTimestamp dateFin = null;
		GregorianCalendar lundiPrecedent = null;
		int dayOfWeek = 0;

		newPeriode = new EOPeriodeAffectationAnnuelle();
		newPeriode.insertInEditingContext(editingContext());
		dateDebut = dDebAffectation;
		dateFin = dFinAffectation;
		if (DateCtrl.isBefore(dateDebut, dateDebutAnnee())) {
			dateDebut = dateDebutAnnee();
		}
		newPeriode.setDateDebut(dateDebut);
		if (dateFin == null || DateCtrl.isAfter(dateFin, dateFinAnnee())) {
			dateFin = dateFinAnnee();
			// attention, la date de fin doit etre a 00:00 !!!
			dateFin = dateFin.timestampByAddingGregorianUnits(0, 0, 0, -23, -59, -59);
		}
		newPeriode.setDateFin(dateFin);
		newPeriode.setAffectationRelationship(affectation);

		lundiPrecedent = new GregorianCalendar();
		lundiPrecedent.setTime(dateDebut);
		dayOfWeek = lundiPrecedent.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1)
			dateDebut = dateDebut.timestampByAddingGregorianUnits(0, 0, 1, 0, 0, 0);
		else
			dateDebut = dateDebut.timestampByAddingGregorianUnits(0, 0, -((dayOfWeek - 2) % 7), 0, 0, 0);

		// Creation des plannings hebdo associes a la periode
		while (DateCtrl.isBeforeEq(dateDebut, dateFin)) {

			// planning previsionnel
			EOPlanningHebdomadaire newPlanningHebdoReel = new EOPlanningHebdomadaire();
			newPlanningHebdoReel.insertInEditingContext(editingContext());
			newPlanningHebdoReel.setDateDebutSemaine(dateDebut);
			newPlanningHebdoReel.setFlagNature("P");
			newPeriode.addObjectToBothSidesOfRelationshipWithKey(newPlanningHebdoReel, EOPeriodeAffectationAnnuelle.PLANNING_HEBDOMADAIRES_KEY);

			// planning de test
			EOPlanningHebdomadaire newPlanningHebdoTest = new EOPlanningHebdomadaire();
			newPlanningHebdoTest.insertInEditingContext(editingContext());
			newPlanningHebdoTest.setDateDebutSemaine(dateDebut);
			newPlanningHebdoTest.setFlagNature("T");
			newPeriode.addObjectToBothSidesOfRelationshipWithKey(newPlanningHebdoTest, EOPeriodeAffectationAnnuelle.PLANNING_HEBDOMADAIRES_KEY);

			dateDebut = dateDebut.timestampByAddingGregorianUnits(0, 0, 7, 0, 0, 0);
		}
		addObjectToBothSidesOfRelationshipWithKey(newPeriode, PERIODES_KEY);

		EOCalculAffectationAnnuelle compte = calculAffAnn("P");
		compte.insertInEditingContext(editingContext());
		compte.addMinutesDues(Math.round(newPeriode.valeurPonderee(newPeriode.minutesDues100Pourcent(false, volumeHoraireAnnuel()))));
		compte = calculAffAnn("R");
		compte.addMinutesDues(Math.round(newPeriode.valeurPonderee(newPeriode.minutesDues100Pourcent(false, volumeHoraireAnnuel()))));

		return newPeriode;
	}

	/**
	 * Donne le totale des heures dues sur l'ensemble de l'affectation annuelle
	 * (somme des heures dues de chaque sous periode)
	 * 
	 * @return
	 */
	public int minutesDues() {
		float minutesDues = 0;

		// on classe les periodes chronologiquement
		NSArray periodes = periodes();

		for (int i = 0; i < periodes.count(); i++) {
			EOPeriodeAffectationAnnuelle curPeriode = (EOPeriodeAffectationAnnuelle) periodes.objectAtIndex(i);
			minutesDues += curPeriode.valeurPonderee(curPeriode.minutesDues100Pourcent(false, volumeHoraireAnnuel()));
		}

		return Math.round(minutesDues);
	}

	/**
	 * 
	 * @param typePlanning
	 * @param uneDate
	 * @return
	 */
	/*
	 * public EOPlanningHebdomadaire planningHebdo( String typePlanning,
	 * NSTimestamp uneDate) { EOPlanningHebdomadaire unPlanningHebdo = null;
	 * NSArray periodes = periodes(); EOQualifier qualPeriodePourDate =
	 * EOQualifier.qualifierWithQualifierFormat(
	 * EOPeriodeAffectationAnnuelle.DATE_DEBUT_KEY + "<=%@ AND " +
	 * EOPeriodeAffectationAnnuelle.DATE_FIN_KEY + ">=%@", new NSArray(new
	 * NSTimestamp[] { uneDate, uneDate }));
	 * 
	 * periodes = EOQualifier.filteredArrayWithQualifier(periodes,
	 * qualPeriodePourDate); EOPeriodeAffectationAnnuelle unePeriode =
	 * (EOPeriodeAffectationAnnuelle) periodes.lastObject(); if (unePeriode !=
	 * null) { NSArray lesPlanningsHebdos = unePeriode.planningHebdomadaires();
	 * EOQualifier qualPlanningHebdoPourDate =
	 * EOQualifier.qualifierWithQualifierFormat(
	 * EOPlanningHebdomadaire.FLAG_NATURE_KEY+"=%@ AND " +
	 * EOPlanningHebdomadaire.DATE_DEBUT_SEMAINE_KEY+"=%@", new NSArray(new
	 * Object[] { typePlanning, uneDate })); lesPlanningsHebdos =
	 * EOQualifier.filteredArrayWithQualifier(lesPlanningsHebdos,
	 * qualPlanningHebdoPourDate); unPlanningHebdo = (EOPlanningHebdomadaire)
	 * lesPlanningsHebdos.lastObject(); } return unPlanningHebdo; }
	 */

	/**
	 * Obtenir le premier enregistrement associé à une semaine Ceci pour trouver
	 * par exemple le planning associé à la semaine du 30/08/xxxx alors que
	 * planning commence le 01/09/xxxx
	 * 
	 * @param typePlanning
	 * @param uneDateDebut
	 * @param uneDateFin
	 * @return
	 */
	public EOPlanningHebdomadaire planningHebdo(
			String typePlanning, NSTimestamp uneDate) {

		EOPlanningHebdomadaire planningHebdo = null;

		NSArray planningsHebdo = planningsHebdo(typePlanning, uneDate);

		if (planningsHebdo.count() > 0) {
			planningHebdo = (EOPlanningHebdomadaire) planningsHebdo.lastObject();
		}

		return planningHebdo;
	}

	/**
	 * Obtenir les enregistrements associé à une semaine Ceci pour trouver par
	 * exemple le planning associé à la semaine du 30/08/xxxx alors que planning
	 * commence le 01/09/xxxx
	 * 
	 * @param typePlanning
	 * @param uneDateDebut
	 * @param uneDateFin
	 * @return
	 */
	public NSArray planningsHebdo(
			String typePlanning, NSTimestamp uneDate) {

		NSArray planningsHebdo = (NSArray) periodes().valueForKey(EOPeriodeAffectationAnnuelle.PLANNING_HEBDOMADAIRES_KEY);
		planningsHebdo = ArrayCtrl.applatirArray(planningsHebdo);

		EOQualifier qualPlanningHebdoPourDate = EOQualifier.qualifierWithQualifierFormat(
				EOPlanningHebdomadaire.FLAG_NATURE_KEY + "=%@ AND " +
						EOPlanningHebdomadaire.DATE_DEBUT_SEMAINE_KEY + "=%@", new NSArray(new Object[] {
						typePlanning, uneDate }));

		planningsHebdo = EOQualifier.filteredArrayWithQualifier(planningsHebdo, qualPlanningHebdoPourDate);

		return planningsHebdo;
	}

	/**
	 * Parmi un {@link NSArray} de {@link EOPlanningHebdomadaire}, indique le
	 * nombre d'enregistrement dont {@link EOPlanningHebdomadaire#horaire()}
	 * contient quelque chose
	 * 
	 * @param planningsHebdo
	 * @return
	 */
	public static int nbPlanningHebdoAssocies(NSArray planningsHebdo) {
		int nb = 0;

		for (int i = 0; i < planningsHebdo.count(); i++) {
			EOPlanningHebdomadaire planningHebdo = (EOPlanningHebdomadaire) planningsHebdo.objectAtIndex(i);
			if (planningHebdo.horaire() != null) {
				nb++;
			}
		}

		return nb;
	}

	/**
	 * liste des planning qui chevauchent plusieurs periodes
	 * 
	 * @return
	 */
	public NSArray planningsHebdoMultiPeriodes() {
		NSArray planningsHebdo = new NSArray();
		for (int i = 0; i < periodes().count(); i++) {
			EOPeriodeAffectationAnnuelle unePeriode = (EOPeriodeAffectationAnnuelle) periodes().objectAtIndex(i);
			planningsHebdo = planningsHebdo.arrayByAddingObjectsFromArray(unePeriode.planningHebdomadaires());
		}

		// on ne prend que les previsionnel (identiques au reel !)
		EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(
				EOPlanningHebdomadaire.FLAG_NATURE_KEY + "='P'", null);
		planningsHebdo = EOQualifier.filteredArrayWithQualifier(planningsHebdo, qual);

		NSArray planningsHebdoMulti = new NSArray();
		for (int i = 0; i < planningsHebdo.count(); i++) {
			EOPlanningHebdomadaire unPlanningHebdo = (EOPlanningHebdomadaire) planningsHebdo.objectAtIndex(i);
			qual = EOQualifier.qualifierWithQualifierFormat(
					EOPlanningHebdomadaire.DATE_DEBUT_SEMAINE_KEY + "=%@", new NSArray(unPlanningHebdo.dateDebutSemaine()));
			NSArray planningsHebdoPourDate = EOQualifier.filteredArrayWithQualifier(planningsHebdo, qual);

			if (planningsHebdoPourDate.count() > 1) {
				planningsHebdoMulti = planningsHebdoMulti.arrayByAddingObject(unPlanningHebdo);
			}
		}

		return planningsHebdoMulti;
	}

	/**
	 * @param ec
	 */
	public void insertInEditingContext(EOEditingContext ec) {
		if (ec != null) {

			if (ec.globalIDForObject(this) == null) {
				ec.insertObject(this);
			}
			// Insertion des periodes
			NSArray lesPeriodes = periodes();
			Enumeration<?> enumLesPeriodes = lesPeriodes.objectEnumerator();
			EOPeriodeAffectationAnnuelle unePeriode = null;

			while (enumLesPeriodes.hasMoreElements()) {
				unePeriode = (EOPeriodeAffectationAnnuelle) enumLesPeriodes.nextElement();
				if (ec.globalIDForObject(unePeriode) == null) {
					unePeriode.insertInEditingContext(ec);
				}
			}

			// Insertion des calculs
			NSArray lesComptes = calculAffAnns();
			Enumeration<?> enumLesComptes = lesComptes.objectEnumerator();
			EOCalculAffectationAnnuelle unCompte = null;

			while (enumLesComptes.hasMoreElements()) {
				unCompte = (EOCalculAffectationAnnuelle) enumLesComptes.nextElement();
				if (ec.globalIDForObject(unCompte) == null) {
					unCompte.insertInEditingContext(ec);
				}
			}

			// Insertion des occupations
			NSArray lesOccupations = occupations();
			if (lesOccupations != null) {
				Enumeration<?> enumLesOccupations = lesOccupations.objectEnumerator();
				EOOccupation uneOccupation = null;

				while (enumLesOccupations.hasMoreElements()) {
					uneOccupation = (EOOccupation) enumLesOccupations.nextElement();
					if (ec.globalIDForObject(uneOccupation) == null) {
						uneOccupation.insertInEditingContext(ec);
					}
				}
			}

		}
	}

	/**
	 * retourne l'index d'un jour sur un tableau de jours (debutPeriode,
	 * finPeriode) si l'index est <0, la methode retourne 0
	 * 
	 * @param debut
	 * @param fin
	 * @return
	 */
	public int nbJoursEntre(NSTimestamp debutPeriode, NSTimestamp dateJour) {
		long indexJour = (dateJour.getTime() - debutPeriode.getTime()) / (1000 * 60 * 60 * 24);
		if (indexJour < 0) {
			indexJour = 0;
		}
		return (new Long(indexJour)).intValue();
	}

	// FIXME 05/01/2005 : bug si conge hors periode
	// FIXME 05/01/2005 : remettre le decompte legal a 0 si pas d'absence
	// (+gestion suppression cng legal)
	public NSArray presence(String typePlanning) {

		NSMutableArray presence = null;
		NSArray periodes = periodes();
		EOPeriodeAffectationAnnuelle unePeriode = null;

		int location = nbJoursEntre(dateDebutAnnee(), dateFinAnnee());
		presence = new NSMutableArray();
		for (int i = 0; i <= location; i++) {
			presence.addObject("00");
		}

		for (int numeroPeriode = 0; numeroPeriode < periodes.count(); numeroPeriode++) {
			unePeriode = (EOPeriodeAffectationAnnuelle) periodes.objectAtIndex(numeroPeriode);
			NSArray planningsHebdos = unePeriode.planningHebdomadaires();

			// classement chronologique par le debut de semaine
			LRSort sortOrderings = LRSort.newSort(EOPlanningHebdomadaire.DATE_DEBUT_SEMAINE_KEY);
			planningsHebdos = EOSortOrdering.sortedArrayUsingKeyOrderArray(planningsHebdos, sortOrderings);

			// filtrage selon la nature du planning (13/01/2005 - DT 34373)
			EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(
					EOPlanningHebdomadaire.FLAG_NATURE_KEY + "=%@", new NSArray(typePlanning));
			planningsHebdos = EOQualifier.filteredArrayWithQualifier(planningsHebdos, qual);

			for (int j = 0; j < planningsHebdos.count(); j++) {
				EOPlanningHebdomadaire unPlanningHebdo = (EOPlanningHebdomadaire) planningsHebdos.objectAtIndex(j);
				EOHoraire unHoraire = unPlanningHebdo.horaire();
				if (unHoraire != null) {

					NSArray presenceHoraire = unHoraire.presenceHoraireJF(unPlanningHebdo.dateDebutSemaine());
					location = nbJoursEntre(dateDebutAnnee(), unPlanningHebdo.dateDebutSemaine());

					// cas du debut de l'année univ, on avance sur le premier jour de
					// l'annee dans l'horaire
					int debut = 0;
					if (DateCtrl.isBefore(unPlanningHebdo.dateDebutSemaine(), dateDebutAnnee())) {
						debut += nbJoursEntre(unPlanningHebdo.dateDebutSemaine(), dateDebutAnnee());
					}

					// cas de la fin de l'année univ, on termine sur le dernier jour de
					// l'annee dans l'horaire
					int fin = presenceHoraire.count();
					if (DateCtrl.isAfter(unPlanningHebdo.dateFinSemaine(), dateFinAnnee())) {
						fin -= nbJoursEntre(dateFinAnnee(), unPlanningHebdo.dateFinSemaine());
					}

					// on ne depasse pas la taille du tableau
					for (int k = location + debut; k < location + fin && k < presence.count(); k++) {
						String unePresenceQuotidienne = (String) presenceHoraire.objectAtIndex(k - location);
						presence.replaceObjectAtIndex(unePresenceQuotidienne, k - debut);
					}

					// on marque comme jour non travaille les jours hors affectation (hors
					// periode)

					// debut de semaine hors affectation
					if (DateCtrl.isAfter(unePeriode.dateDebut(), unPlanningHebdo.dateDebutSemaine())
							&& DateCtrl.isBefore(unePeriode.dateDebut(), unPlanningHebdo.dateFinSemaine())) {
						// y a-t-il une periode precedente ?
						if (numeroPeriode != 0) {
							// oui
							EOPeriodeAffectationAnnuelle periodePrecedente = (EOPeriodeAffectationAnnuelle) periodes.objectAtIndex(numeroPeriode - 1);
							int debutPeriodeEnCours = nbJoursEntre(unPlanningHebdo.dateDebutSemaine(), unePeriode.dateDebut());
							int finPeriodePrec = nbJoursEntre(unPlanningHebdo.dateDebutSemaine(), periodePrecedente.dateFin());
							for (int k = location + finPeriodePrec; k < location + debutPeriodeEnCours; k++) {
								presence.replaceObjectAtIndex("00", k);
							}
						} else {
							// pas de periode precedente, on met les jour comme non travailles
							int debutCourantSemaine = nbJoursEntre(unPlanningHebdo.dateDebutSemaine(), unePeriode.dateDebut());
							// cas particulier de la premiere semaine, location=0 ne tombe pas
							// forcement un lundi --> pas de traitement
							if (j > 0) {
								for (int k = location; k < location + debutCourantSemaine; k++) {
									presence.replaceObjectAtIndex("00", k);
								}
							}
						}
					}

					// fin de semaine hors affectation
					if (DateCtrl.isAfterEq(unePeriode.dateFin(), unPlanningHebdo.dateDebutSemaine())
							&& DateCtrl.isBefore(unePeriode.dateFin(), unPlanningHebdo.dateFinSemaine())) {
						// y a-t-il une periode suivante ?
						if (numeroPeriode < periodes.count() - 1) {
							// oui
							EOPeriodeAffectationAnnuelle periodeSuivante = (EOPeriodeAffectationAnnuelle) periodes.objectAtIndex(numeroPeriode + 1);
							int finPeriodeEnCours = nbJoursEntre(unPlanningHebdo.dateDebutSemaine(), unePeriode.dateFin());
							int debutPeriodeSuiv = nbJoursEntre(unPlanningHebdo.dateDebutSemaine(), periodeSuivante.dateDebut());
							for (int k = location + finPeriodeEnCours + 1; k < location + debutPeriodeSuiv; k++) {
								// cas d'une occupation hors periode (ex: congé à cheval sur 2
								// années universitaire)
								if (k < presence.count()) {
									presence.replaceObjectAtIndex("00", k);
								}
							}
						} else {
							// pas de periode suivante, on met les jour comme non travailles
							int finCourantSemaine = nbJoursEntre(unPlanningHebdo.dateDebutSemaine(), unePeriode.dateFin()) + 1;

							int dernierJourSemaine = (location + 7 >= presence.count()) ? presence.count() : location + 7;

							for (int k = location + finCourantSemaine; k < dernierJourSemaine; k++) {
								presence.replaceObjectAtIndex("00", k);
							}
						}
					}

				}
			}

			NSArray lesOccupations = occupations();
			lesOccupations = EOOccupation.filtrerOccupationsVisiblesInMemory(lesOccupations);

			EOOccupation uneOccupation = null;

			for (int j = 0; j < lesOccupations.count(); j++) {

				uneOccupation = (EOOccupation) lesOccupations.objectAtIndex(j);

				// on ignore les occupations hors periode
				if (DateCtrl.isBefore(uneOccupation.dateFin(), dateDebutAnnee()) ||
						DateCtrl.isBefore(dateFinAnnee(), uneOccupation.dateDebut())) {
					continue;
				}

				// on ne garde que les occupations de la nature du planning
				if (!typePlanning.equals(uneOccupation.flagNature())) {
					continue;
				}

				// uniquement les occupations validees (11/01/2005 - DT 34373) pour le
				// reel
				// toutes les occupations non supprimees pour le previsionnel
				if ("P".equals(typePlanning) && uneOccupation.isSupprimee()) {
					continue;
				}

				// les absences valide : "2" et pas encore valide : "3"
				String codeAbsence = ConstsOccupation.PRESENCE_OCCUPATION_DEMI_JOURNEE_EN_COURS_DE_VALIDATION;
				// pour le previsionnel, toujours valides
				if (uneOccupation.isValidee() || "P".equals(uneOccupation.flagNature())) {
					codeAbsence = ConstsOccupation.PRESENCE_OCCUPATION_DEMI_JOURNEE_VALIDEE;
				}

				// on ignore les HSUP mais pas les conges compensateurs et aut.
				// d'absence
				if (uneOccupation.isOccupationMinute()) {
					if (uneOccupation.isHeureSup()) {
						continue;
					} else {
						// "4" pour celles validées, "5" pour en cours de validation
						codeAbsence = ConstsOccupation.PRESENCE_OCCUPATION_MINUTE_EN_COURS_DE_VALIDATION;
						if (uneOccupation.isValidee() || "P".equals(uneOccupation.flagNature())) {
							codeAbsence = ConstsOccupation.PRESENCE_OCCUPATION_MINUTE_VALIDEE;
						}
					}
				}

				NSTimestamp debutTS = uneOccupation.dateDebut();
				// on ramene le debut de la periode si date < debut periode
				if (DateCtrl.isBefore(debutTS, dateDebutAnnee())) {
					debutTS = dateDebutAnnee();
				}

				NSTimestamp finTS = uneOccupation.dateFin();
				// on ramene le fin de la periode si date > fin periode
				if (DateCtrl.isAfter(finTS, dateFinAnnee())) {
					// fin d'annee + 12:00 pour avoir toutes les demies journees
					finTS = dateFinAnnee().timestampByAddingGregorianUnits(0, 0, 0, 12, 0, 0);
				}

				GregorianCalendar debutGC = new GregorianCalendar();
				GregorianCalendar finGC = new GregorianCalendar();

				// TODO exclure les occupations qui ne sont pas des absences
				location = nbJoursEntre(dateDebutAnnee(), debutTS);
				int length = nbJoursEntre(debutTS, finTS);

				int index = new Long(location).intValue();
				debutGC.setTime(debutTS);
				finGC.setTime(finTS);

				// cas d'une occupation debut PM et fin AM : il manque 1 jour !
				if (debutGC.get(Calendar.AM_PM) == Calendar.PM &&
						finGC.get(Calendar.AM_PM) == Calendar.AM) {
					length++;
				}

				if (debutGC.get(Calendar.AM_PM) == Calendar.PM) {
					// on met les absences uniquement sur le temps de travail
					String jour = (String) presence.objectAtIndex(index);
					// if ("01".equals(jour) || "10".equals(jour) || "11".equals(jour)) {
					if (StringCtrl.containsIgnoreCase(jour, "1")) {
						if ("1".equals(jour.substring(1))) {
							jour = jour.substring(0, 1) + codeAbsence;
							presence.replaceObjectAtIndex(jour, index);
						}
					}
					index++;
				}
				while (index <= presence.count() && index <= location + length && index < presence.count()) {
					// on met les absences uniquement sur le temps de travail
					String jour = (String) presence.objectAtIndex(index);
					// if ("01".equals(jour) || "10".equals(jour) || "11".equals(jour)) {
					if (StringCtrl.containsIgnoreCase(jour, "1")) {
						// rajout bug le matin only apparait pas
						if (index == location + length && finGC.get(Calendar.AM_PM) == Calendar.AM) {
							jour = codeAbsence + jour.charAt(1);
						} else {
							jour = StringCtrl.replace(jour, "1", codeAbsence);
						}
						presence.replaceObjectAtIndex(jour, index);
					}
					index++;
				}

			}

			// on met les jours feries en non travail
			for (int j = 0; j < joursFeries().count(); j++) {
				NSTimestamp unJourFerie = (NSTimestamp) joursFeries().objectAtIndex(j);
				location = nbJoursEntre(dateDebutAnnee(), unJourFerie);
				int index = new Long(location).intValue();
				presence.replaceObjectAtIndex("00", index);
			}

			// on met les fermetures en conges
			for (int j = 0; j < fermetures().count(); j++) {
				EOPeriodeFermeture uneFermeture = (EOPeriodeFermeture) fermetures().objectAtIndex(j);
				location = nbJoursEntre(dateDebutAnnee(), uneFermeture.dateDebut());
				// recadrer les dates de fermeture par rapport au planning
				NSTimestamp dDebutFermeture = uneFermeture.dateDebut();
				if (DateCtrl.isBefore(dDebutFermeture, dateDebutAnnee())) {
					dDebutFermeture = dateDebutAnnee();
				}
				NSTimestamp dFinFermeture = uneFermeture.dateFin();
				if (DateCtrl.isAfter(dFinFermeture, dateFinAnnee())) {
					dFinFermeture = dateFinAnnee();
				}
				int length = nbJoursEntre(dDebutFermeture, dFinFermeture);
				int index = new Long(location).intValue();
				// suppression du <=
				while (index <= presence.count() && index <= location + length) {
					// on met les fermetures uniquement sur le temps de travail
					if ("01".equals(presence.objectAtIndex(index)) ||
							"10".equals(presence.objectAtIndex(index)) ||
							"11".equals(presence.objectAtIndex(index))) {
						presence.replaceObjectAtIndex("22", index);
					}
					index++;
				}
			}

			// les congés légaux
			NSArray congesLegaux = EOAbsenceGepeto.findAbsencesGepetoForIndividu(
					editingContext(), individu(), dateDebutAnnee(), dateFinAnnee());
			for (int j = 0; j < congesLegaux.count(); j++) {
				EOAbsenceGepeto congeLegal = (EOAbsenceGepeto) congesLegaux.objectAtIndex(j);
				location = nbJoursEntre(dateDebutAnnee(), congeLegal.dateDebut());
				// recadrer les dates de fermeture par rapport au planning
				NSTimestamp dDebutCongeLegal = congeLegal.dateDebut();
				if (DateCtrl.isBefore(dDebutCongeLegal, dateDebutAnnee())) {
					dDebutCongeLegal = dateDebutAnnee();
				}
				NSTimestamp dFinCongeLegal = congeLegal.dateFin();
				if (DateCtrl.isAfter(dFinCongeLegal, dateFinAnnee())) {
					dFinCongeLegal = dateFinAnnee();
				}
				int length = nbJoursEntre(dDebutCongeLegal, dFinCongeLegal);
				int index = new Long(location).intValue();
				// suppression du <=
				while (index <= presence.count() && index <= location + length) {
					// on met les conges légaux uniquement sur le temps de travail
					if ("01".equals(presence.objectAtIndex(index)) ||
							"10".equals(presence.objectAtIndex(index)) ||
							"11".equals(presence.objectAtIndex(index))) {
						presence.replaceObjectAtIndex("22", index);
					}
					index++;
				}
			}

		}

		return presence;
	}

	/**
	 * @param timestamp
	 * @return
	 */
	public NSArray presenceMensuelle(String typePlanning, NSTimestamp firstDayOfMonth) {
		NSArray presenceMensuelle = presence(typePlanning);
		NSTimestamp lastDayOfMonth = firstDayOfMonth.timestampByAddingGregorianUnits(0, 1, -1, 0, 0, 0);
		int location = nbJoursEntre(dateDebutAnnee(), firstDayOfMonth);
		int length = nbJoursEntre(firstDayOfMonth, lastDayOfMonth);
		NSRange aRange = new NSRange(new Long(location).intValue(), new Long(length).intValue() + 1);

		presenceMensuelle = presenceMensuelle.subarrayWithRange(aRange);

		return presenceMensuelle;
	}

	public NSArray presencePourPeriode(String typePlanning, NSTimestamp debut, NSTimestamp fin) {
		NSArray presence = presence(typePlanning);
		// remettage a minuit des dates
		debut = TimeCtrl.dateToMinuit(debut);
		fin = TimeCtrl.dateToMinuit(fin);
		// recadrage sur l'ann�e univ
		if (DateCtrl.isBefore(debut, dateDebutAnnee())) {
			debut = dateDebutAnnee();
		}
		if (DateCtrl.isAfter(fin, dateFinAnnee())) {
			fin = dateFinAnnee();
		}
		int location = nbJoursEntre(dateDebutAnnee(), debut);
		int length = nbJoursEntre(debut, fin);

		NSRange aRange = new NSRange(location, length + 1);

		NSArray presencePourPeriode = presence.subarrayWithRange(aRange);

		return presencePourPeriode;
	}

	/**
	 * @param timestamp
	 * @return
	 */
	public NSArray presenceJournaliere(String typePlanning, NSTimestamp day) {
		NSTimestamp dayAtMinuit = TimeCtrl.dateToMinuit(day);
		NSArray presenceJournaliere = presence(typePlanning);
		NSTimestamp daySuivant = dayAtMinuit.timestampByAddingGregorianUnits(0, 0, 1, 0, 0, 0);
		int location = nbJoursEntre(dateDebutAnnee(), dayAtMinuit);
		int length = nbJoursEntre(dayAtMinuit, daySuivant);
		NSRange aRange = new NSRange(new Long(location).intValue(), new Long(length).intValue() + 1);

		presenceJournaliere = presenceJournaliere.subarrayWithRange(aRange);

		return presenceJournaliere;
	}

	/**
	 * @return
	 */
	public boolean isPasseDroit() {
		boolean isPasseDroit = false;

		if (flagPasseDroit() != null && flagPasseDroit().equals("1")) {
			isPasseDroit = true;
		}

		return isPasseDroit;
	}

	public void setIsPasseDroit(boolean value) {
		if (value == true)
			setFlagPasseDroit("1");
		else
			setFlagPasseDroit("0");
	}

	/**
	 * @return
	 */
	public boolean isHorsNorme() {
		boolean isHorsNorme = false;

		if (flagHorsNorme().equals("1")) {
			isHorsNorme = true;
		}
		return isHorsNorme;
	}

	public void setIsHorsNorme(boolean value) {
		if (value == true)
			setFlagHorsNorme("1");
		else
			setFlagHorsNorme("0");
	}

	/**
	 * @return
	 */
	public boolean isDepSemHautes() {
		boolean isDepSemHautes = false;

		if (flagDepSemHautes() != null && flagDepSemHautes().equals("1")) {
			isDepSemHautes = true;
		}
		return isDepSemHautes;
	}

	public void setIsDepSemHautes(boolean value) {
		if (value == true)
			setFlagDepSemHautes("1");
		else
			setFlagDepSemHautes("0");
	}

	/**
	 * Indique si le droit a congés est calculé automatiquement (via la somme des
	 * heures associées au planning) Appelé par directement l'interface
	 * {@link PageAdminDroits}
	 */
	public boolean isCalculAutomatique() {
		boolean isCalculAutomatique = true;

		if (toMouvementDroitsCongesNonAutomatiques() != null) {
			isCalculAutomatique = false;
		}

		return isCalculAutomatique;
	}

	/**
	 * Forcer le droit a congés en mode automatique ou non. Appelé par directement
	 * l'interface {@link PageAdminDroits}
	 */
	public void setIsCalculAutomatique(boolean shouldBeCalculAutomatique) {
		if (shouldBeCalculAutomatique) {
			if (!isCalculAutomatique()) {
				editingContext().deleteObject(toMouvementDroitsCongesNonAutomatiques());
			}
		} else {
			if (isCalculAutomatique()) {
				EOMouvement.newMouvement(this, EOMouvement.TYPE_DROITS_CONGES_NON_AUTOMATIQUES, 0);
			}
		}
	}

	/**
	 * Indique si le reliquat est manuel ou bien s'il est automatique (c'est alors
	 * la valeur des congés restant du planning précédent) Appelé par directement
	 * l'interface {@link PageAdminDroits}
	 */
	public boolean isReliquatAutomatique() {
		boolean isReliquatAutomatique = true;

		if (toMouvementReliquatsNonAutomatiques() != null) {
			isReliquatAutomatique = false;
		}

		return isReliquatAutomatique;
	}

	/**
	 * Forcer le reliquat en bascule automatique ou non. Appelé par directement
	 * l'interface {@link PageAdminDroits}
	 */
	public void setIsReliquatAutomatique(boolean shouldBeReliquatAutomatique) {
		if (shouldBeReliquatAutomatique) {
			if (!isReliquatAutomatique()) {
				editingContext().deleteObject(toMouvementReliquatsNonAutomatiques());
			}
		} else {
			if (isReliquatAutomatique()) {
				EOMouvement.newMouvement(this, EOMouvement.TYPE_RELIQUATS_NON_AUTOMATIQUES, 0);
			}
		}
	}

	/**
	 * Indique si le planning beneficie d'un droit de decharge syndicale Appelé
	 * par directement l'interface {@link PageAdminDroits}
	 */
	public boolean isDechargeSyndicale() {
		boolean isDechargeSyndicale = false;

		if (toMouvementDechargeSyndicale() != null) {
			isDechargeSyndicale = true;
		}

		return isDechargeSyndicale;
	}

	/**
	 * Accorder ou non une decharge syndicale pour ce planning. Appelé par
	 * directement l'interface {@link PageAdminDroits}
	 */
	public void setIsDechargeSyndicale(boolean shouldBeDechargeSyndicale) {
		if (shouldBeDechargeSyndicale) {
			if (!isDechargeSyndicale()) {
				EOMouvement.newMouvement(this, EOMouvement.TYPE_DECHARGE_SYNDICALE, 0);
			}
		} else {
			if (isDechargeSyndicale()) {
				editingContext().deleteObject(toMouvementDechargeSyndicale());
			}
		}
	}

	/**
	 * @return
	 */
	public boolean isTempsPartielAnnualise() {
		boolean isTempsPartielAnnualise = false;

		if (flagTempsPartielAnnualise().equals("1")) {
			isTempsPartielAnnualise = true;
		}
		return isTempsPartielAnnualise;
	}

	public void setIsTempsPartielAnnualise(boolean value) {
		if (value == true)
			setFlagTempsPartielAnnualise("1");
		else
			setFlagTempsPartielAnnualise("0");
	}

	/**
	 * Peut-il depasser le maximum de conges autorise (390h00)
	 */
	public boolean isDepassementCongesAutorise() {
		boolean isDepassementCongesAutorise = false;

		if (flagDepassementCongesAutorise().equals("1")) {
			isDepassementCongesAutorise = true;
		}
		return isDepassementCongesAutorise;
	}

	public void setIsDepassementCongesAutorise(boolean value) {
		if (value == true)
			setFlagDepassementCongesAutorise("1");
		else
			setFlagDepassementCongesAutorise("0");
	}

	public NSTimestamp dateFinAnnee() {
		return dateDebutAnnee().timestampByAddingGregorianUnits(1, 0, -1, 23, 59, 59);
	}

	// OBJETS EN CACHE

	private NSArray _joursFeries, _fermetures, _vacancesScolaires;

	public NSArray joursFeries() {
		if (_joursFeries == null)
			_joursFeries = DateCtrlConges.joursFeriesEntre2Dates(dateDebutAnnee(), dateFinAnnee());
		return _joursFeries;
	}

	/**
	 * Liste des fermetures liées à ce planning - fermetures établissement -
	 * fermetures composante - fermetures service - fermetures personnelle
	 * 
	 * @return
	 */
	public NSArray fermetures() {
		if (_fermetures == null) {
			NSArray allFermetures = EOPeriodeFermeture.findSortedPeriodeFermeturesInContext(
					editingContext(), dateDebutAnnee(), dateFinAnnee());

			NSArray fermeturesEtablissement = EOPeriodeFermeture.filterPeriodesFermeturesEtablissement(allFermetures, dateDebutAnnee(), dateFinAnnee());
			NSArray fermeturesComposante = EOPeriodeFermeture.filterPeriodesFermeturesComposante(allFermetures, dateDebutAnnee(), dateFinAnnee(), structure().toComposante());
			NSArray fermeturesService = EOPeriodeFermeture.filterPeriodesFermeturesService(allFermetures, dateDebutAnnee(), dateFinAnnee(), structure());
			NSArray fermeturesPlanning = EOPeriodeFermeture.filtereriodesFermeturesPlanning(allFermetures, dateDebutAnnee(), dateFinAnnee(), this);

			// on exclut les fermetures annulees
			NSMutableArray fermeturesMutable = new NSMutableArray();
			fermeturesMutable.addObjectsFromArray(fermeturesEtablissement);
			fermeturesMutable.addObjectsFromArray(fermeturesComposante);
			fermeturesMutable.addObjectsFromArray(fermeturesService);
			fermeturesMutable.addObjectsFromArray(fermeturesPlanning);

			// pour la composante
			fermeturesMutable.removeObjectsInArray(
					(NSArray) structure().toComposante().tosComposanteAnnulationPeriodeFermeture().valueForKey(
							EOAnnulationPeriodeFermeture.TO_PERIODE_FERMETURE_KEY));
			// pour le service
			fermeturesMutable.removeObjectsInArray(
					(NSArray) structure().tosServiceAnnulationPeriodeFermeture().valueForKey(
							EOAnnulationPeriodeFermeture.TO_PERIODE_FERMETURE_KEY));
			// pour le planning
			fermeturesMutable.removeObjectsInArray(
					(NSArray) annulationPeriodeFermetures().valueForKey(EOAnnulationPeriodeFermeture.TO_PERIODE_FERMETURE_KEY));

			//
			_fermetures = fermeturesMutable.immutableClone();
		}
		return _fermetures;
	}

	public NSArray vacancesScolaires() {
		if (_vacancesScolaires == null) {
			_vacancesScolaires = new NSArray();
			_vacancesScolaires = EOVacanceScolaire.findVacancesScolairesForPeriode(editingContext(), dateDebutAnnee(), dateFinAnnee());
		}
		return _vacancesScolaires;
	}

	/**
	 * Retourne la date butoire de perenite des reliquat des conges de l'annee
	 * precedente. Au dela, c'est du CET ou c'est perdu. Cette valeur est lue
	 * depuis le dictionnaire statique <code>dateMaxReliquat</code> qui contient
	 * les parametres etablissement pour chaque annee universitaire
	 */
	public NSTimestamp dateFinReliquat() {
		return DateCtrlConges.stringToDate(dicoStringForDateDebutAnnee(dateMaxReliquat));
	}

	/**
	 * Retourne la date de debut de demande de bascule en CET des reliquat des
	 * conges de l'annee precedente. Cette valeur est lue depuis le dictionnaire
	 * statique <code>dateDebutDemandeCet</code> qui contient les parametres
	 * etablissement pour chaque annee universitaire
	 */
	public NSTimestamp dateDebutDemandeCet() {
		return DateCtrlConges.stringToDate(dicoStringForDateDebutAnnee(dateDebutDemandeCet));
	}

	/**
	 * Retourne la date butoir de demande de bascule en CET des reliquat des
	 * conges de l'annee precedente. Au dela, c'est definitivement perdu. Cette
	 * valeur est lue depuis le dictionnaire statique
	 * <code>dateFinDemandeCet</code> qui contient les parametres etablissement
	 * pour chaque annee universitaire
	 */
	public NSTimestamp dateFinDemandeCet() {
		return DateCtrlConges.stringToDate(dicoStringForDateDebutAnnee(dateFinDemandeCet));
	}

	/**
	 * Retourne le nombre de jours à 7h00 minimum pour pouvoir faire une épargne
	 * CET.
	 * 
	 * Cette valeur est lue depuis le dictionnaire statique
	 * <code>seuilCongesConsommesJour7h00Nm1PourEpargneCet</code>
	 */
	public int seuilCongesConsommesJour7h00Nm1PourEpargneCet() {
		return Integer.parseInt(dicoStringForDateDebutAnnee(seuilCongesConsommesJour7h00Nm1PourEpargneCet));
	}

	/**
	 * Retourne le nombre minimum de reliquats de congés pour pouvoir faire une
	 * épargne CET.
	 * 
	 * Cette valeur est lue depuis le dictionnaire statique
	 * <code>seuilReliquatHeuresPourEpargneCet</code>
	 */
	public String seuilReliquatHeuresPourEpargneCet() {
		return dicoStringForDateDebutAnnee(seuilReliquatHeuresPourEpargneCet);
	}

	/**
	 * Retourne le nombre de jours à 7h00 de reliquats de congés maximum
	 * épargnable lors d'une demande d'épargne CET.
	 * 
	 * Cette valeur est lue depuis le dictionnaire statique
	 * <code>plafondEpargneCet</code>
	 */
	public int plafondEpargneCet() {
		return Integer.parseInt(dicoStringForDateDebutAnnee(plafondEpargneCet));
	}

	/**
	 * Donner la valeur d'un dictionnaire pour la clé donnée par l'année
	 * universitaire en cours
	 * 
	 * @param dico
	 * @return
	 */
	private String dicoStringForDateDebutAnnee(NSDictionary dico) {
		return (String) dico.objectForKey(DateCtrl.dateToString(dateDebutAnnee()));
	}

	private Number oid;

	/**
	 * En lecture seule, pour les logs
	 * 
	 * @return -1 si erreur
	 */
	public Number oid() {
		if (oid == null) {
			oid = new Integer(-1);
			try {
				Number pk = (Number) EOUtilities.primaryKeyForObject(editingContext(), this).valueForKey("oid");
				if (pk != null)
					oid = pk;
			} catch (Exception e) {

			}
		}
		return oid;
	}

	/**
	 * Affichage de ce type de donnees dans les interface
	 */
	public String toString() {
		try {
			String str = individu().nomComplet() + " - " + structure().libelleCourt() + " - " + annee();
			return str;
		} catch (Exception e) {
			e.printStackTrace();
			return super.toString();
		}
	}

	// etats du planning

	public boolean isPlanningValide() {
		return statusPlanning().equals(ConstsPlanning.PLANNING_STATUT_VALIDE);
	}

	public boolean isPlanningNonValide() {
		return statusPlanning().equals(ConstsPlanning.PLANNING_STATUT_INVALIDE);
	}

	public boolean isPlanningEnCoursDeValidation() {
		return statusPlanning().equals(ConstsPlanning.PLANNING_STATUT_EN_COURS_DE_VALIDATION) || isPlanningEnCoursDeValidationVise();
	}

	public boolean isPlanningEnCoursDeModification() {
		return statusPlanning().equals(ConstsPlanning.PLANNING_STATUT_EN_COURS_DE_MODIFICATION) || isPlanningEnCoursDeModificationVise();
	}

	public boolean isPlanningEnCoursDeValidationVise() {
		return statusPlanning().equals(ConstsPlanning.PLANNING_STATUT_EN_COURS_DE_VALIDATION_VISE);
	}

	public boolean isPlanningEnCoursDeModificationVise() {
		return statusPlanning().equals(ConstsPlanning.PLANNING_STATUT_EN_COURS_DE_MODIFICATION_VISE);
	}

	public boolean isPlanningVise() {
		return isPlanningEnCoursDeValidationVise() || isPlanningEnCoursDeModificationVise();
	}

	// suivit des visa / validations

	public EORepartValidation repartValidation() {
		EORepartValidation record = null;
		NSArray records = EORepartValidation.findIndividuValidantForPlanning(
				editingContext(), this, EORepartValidation.FLAG_VALIDATION_VALUE);
		if (records.count() > 0)
			record = (EORepartValidation) records.lastObject();
		return record;
	}

	public EORepartValidation repartVisa() {
		EORepartValidation record = null;
		NSArray records = EORepartValidation.findIndividuValidantForPlanning(
				editingContext(), this, EORepartValidation.FLAG_VISEE_VALUE);
		if (records.count() > 0)
			record = (EORepartValidation) records.lastObject();
		return record;
	}

	/**
	 * Effacer l'enregistrement <code>EORepartValidation</code> de validation du
	 * planning
	 */
	public void removeRepartValidation() {
		EORepartValidation repartValidation = repartValidation();
		if (repartValidation != null) {
			repartValidation.removeObjectFromBothSidesOfRelationshipWithKey(
					repartValidation, EORepartValidation.AFFECTATION_ANNUELLE_KEY);
			editingContext().deleteObject(repartValidation);
		}
	}

	/**
	 * Effacer l'enregistrement <code>EORepartValidation</code> de visa du
	 * planning
	 */
	public void removeRepartVisa() {
		EORepartValidation repartVisa = repartVisa();
		if (repartVisa != null) {
			repartVisa.removeObjectFromBothSidesOfRelationshipWithKey(
					repartVisa, EORepartValidation.AFFECTATION_ANNUELLE_KEY);
			editingContext().deleteObject(repartVisa);
		}
	}

	// methode pour forcer le rechargement de l'integralit� du planning

	public static void invalidateEOAffectationAnnuelle(EOEditingContext edc, EOAffectationAnnuelle affAnn) {
		boolean hasCet = (affAnn.individu().toCET() != null);
		if (hasCet) {
			// XXX faire le rafraichissement
			// NSArray transactions = affAnn.individu().toCET().cETTransactions();
			// for (int i=0; i<transactions.count(); i++) {
			// EOCETTransaction transaction = (EOCETTransaction)
			// transactions.objectAtIndex(i);
			// NSArray debitableList = transaction.tosDebitableSurCET();
			// for (int j=0; j<debitableList.count(); j++) {
			// A_DebitableSurCET debitable = (A_DebitableSurCET)
			// debitableList.objectAtIndex(j);
			// debitable.clearCache();
			// edc.invalidateObjectsWithGlobalIDs(new
			// NSArray(edc.globalIDForObject(debitable)));
			// }
			// edc.invalidateObjectsWithGlobalIDs(new
			// NSArray(edc.globalIDForObject(transaction)));
			// }
			edc.invalidateObjectsWithGlobalIDs(new NSArray(edc.globalIDForObject(affAnn.individu().toCET())));
		}
		edc.invalidateObjectsWithGlobalIDs(new NSArray(edc.globalIDForObject(affAnn.individu())));
	}

	//

	/**
	 * Obtenir les enregistrements de mouvement {@link EOMouvement} d'un type
	 * donné
	 */
	public NSArray tosMouvements(int type) {
		NSArray mouvements = null;

		mouvements = tosMouvement(
				CRIDataBus.newCondition(EOMouvement.MOUVEMENT_TYPE_KEY + "=%@", new NSArray(new Integer(type))));

		return mouvements;
	}

	/**
	 * Obtenir l'enregistrement de mouvement {@link EOMouvement} d'un type donné.
	 * S'il y en a plusieurs, alors on prend le premier
	 */
	public EOMouvement toMouvement(int type) {
		EOMouvement mouvement = null;

		NSArray mouvements = tosMouvements(type);

		if (mouvements.count() > 0) {
			mouvement = (EOMouvement) mouvements.objectAtIndex(0);
		}

		return mouvement;
	}

	/**
	 * Raccourci vers l'enregistrement lié à des droits à congés non automatiques
	 * 
	 * @return
	 */
	public EOMouvement toMouvementDroitsCongesNonAutomatiques() {
		return toMouvement(EOMouvement.TYPE_DROITS_CONGES_NON_AUTOMATIQUES);
	}

	/**
	 * Raccourci vers l'enregistrement lié à des reliquats non automatiques
	 * 
	 * @return
	 */
	public EOMouvement toMouvementReliquatsNonAutomatiques() {
		return toMouvement(EOMouvement.TYPE_RELIQUATS_NON_AUTOMATIQUES);
	}

	/**
	 * Raccourci vers l'enregistrement lié à la decharge syndicale
	 * 
	 * @return
	 */
	public EOMouvement toMouvementDechargeSyndicale() {
		return toMouvement(EOMouvement.TYPE_DECHARGE_SYNDICALE);
	}

	/**
	 * Raccourci vers l'enregistrement lié au JRTI
	 * 
	 * @return
	 */
	public EOMouvement toMouvementJrti() {
		return toMouvement(EOMouvement.TYPE_JRTI);
	}

	/**
	 * Raccourci vers l'enregistrement lié au rachat du CET (decret 2008-1136)
	 * 
	 * @return
	 */
	public EOMouvement toMouvementCetIndemnisationDecret20081136() {
		return toMouvement(EOMouvement.TYPE_CET_INDEMNISATION_DECRET_2008_1136);
	}

	/**
	 * Raccourci vers l'enregistrement contenant la quantité de reliquats demandés
	 * en épargne CET
	 * 
	 * @return
	 */
	public EOMouvement toMouvementCetDemandeEpargne() {
		return toMouvement(EOMouvement.TYPE_CET_DEMANDE_EPARGNE);
	}

	/**
	 * Raccourci vers la quantité de CET ancien régime à maintenir dans l'ancien
	 * régime
	 * 
	 * @return
	 */
	public EOMouvement toMouvementCetDemandeMaintienCetAncienRegime() {
		return toMouvement(EOMouvement.TYPE_CET_DEMANDE_MAINTIEN_CET_ANCIEN_REGIME);
	}

	/**
	 * Raccourci vers la quantité de l'épargne demandée à maintenir en CET
	 * 
	 * @return
	 */
	public EOMouvement toMouvementCetDemandeMaintienCet() {
		return toMouvement(EOMouvement.TYPE_CET_DEMANDE_MAINTIEN_CET);
	}

	/**
	 * Raccourci vers la quantité de l'épargne décidée à maintenir en CET
	 * 
	 * @return
	 */
	public EOMouvement toMouvementCetDecisionMaintienCet() {
		return toMouvement(EOMouvement.TYPE_CET_DECISION_MAINTIEN_CET);
	}

	/**
	 * Raccourci vers la quantité de l'épargne demandée à maintenir en CET (la
	 * valeur forcée)
	 * 
	 * @return
	 */
	public EOMouvement toMouvementCetDemandeMaintienCetForce() {
		return toMouvement(EOMouvement.TYPE_CET_DEMANDE_MAINTIEN_CET_FORCE);
	}

	/**
	 * Raccourci vers la quantité de l'épargne décidée à maintenir en CET (la
	 * valeur forcée)
	 * 
	 * @return
	 */
	public EOMouvement toMouvementCetDecisionMaintienCetForce() {
		return toMouvement(EOMouvement.TYPE_CET_DECISION_MAINTIEN_CET_FORCE);
	}

	/**
	 * Raccourci vers la quantité de l'épargne demandée à transferer en RAFP
	 * 
	 * @return
	 */
	public EOMouvement toMouvementCetDemandeTransfertRafp() {
		return toMouvement(EOMouvement.TYPE_CET_DEMANDE_TRANSFERT_RAFP);
	}

	/**
	 * Raccourci vers la quantité de l'épargne décidée à transferer en RAFP
	 * 
	 * @return
	 */
	public EOMouvement toMouvementCetDecisionTransfertRafp() {
		return toMouvement(EOMouvement.TYPE_CET_DECISION_TRANSFERT_RAFP);
	}

	/**
	 * Raccourci vers la quantité de l'épargne demandée à indemniser
	 * 
	 * @return
	 */
	public EOMouvement toMouvementCetDemandeIndemnisation() {
		return toMouvement(EOMouvement.TYPE_CET_DEMANDE_INDEMNISATION);
	}

	/**
	 * Raccourci vers la quantité de l'épargne décidée à indemniser
	 * 
	 * @return
	 */
	public EOMouvement toMouvementCetDecisionIndemnisation() {
		return toMouvement(EOMouvement.TYPE_CET_DECISION_INDEMNISATION);
	}

	/**
	 * Raccourci vers la demande a transferer en RAFP depuis l'ancien systeme
	 * 
	 * @return
	 */
	public EOMouvement toMouvementCetDemandeTransfertRafpAncienRegime() {
		return toMouvement(EOMouvement.TYPE_CET_DEMANDE_TRANSFERT_RAFP_ANCIEN_REGIME);
	}

	/**
	 * Raccourci vers la décision a transferer en RAFP depuis l'ancien systeme
	 * 
	 * @return
	 */
	public EOMouvement toMouvementCetDecisionTransfertRafpAncienRegime() {
		return toMouvement(EOMouvement.TYPE_CET_DECISION_TRANSFERT_RAFP_ANCIEN_REGIME);
	}

	/**
	 * Raccourci vers la quantité à indemniser depuis l'ancien systeme
	 * 
	 * @return
	 */
	public EOMouvement toMouvementCetDemandeIndemnisationAncienRegime() {
		return toMouvement(EOMouvement.TYPE_CET_DEMANDE_INDEMNISATION_ANCIEN_REGIME);
	}

	/**
	 * Raccourci vers la quantité à indemniser depuis l'ancien systeme
	 * 
	 * @return
	 */
	public EOMouvement toMouvementCetDecisionIndemnisationAncienRegime() {
		return toMouvement(EOMouvement.TYPE_CET_DECISION_INDEMNISATION_ANCIEN_REGIME);
	}

	/**
	 * Raccourci vers l'enregistrement contenant la quantité de reliquats
	 * réellement décidé pour l'épargne CET
	 * 
	 * @return
	 */
	public EOMouvement toMouvementCetDecisionEpargne() {
		return toMouvement(EOMouvement.TYPE_CET_DECISION_EPARGNE);
	}

	/**
	 * Raccourci vers la quantité de CET ancien régime à maintenir par décision
	 * dans l'ancien régime
	 * 
	 * @return
	 */
	public EOMouvement toMouvementCetDecisionMaintienCetAncienRegime() {
		return toMouvement(EOMouvement.TYPE_CET_DECISION_MAINTIEN_CET_ANCIEN_REGIME);
	}

	/**
	 * Raccouci vers l'enregistrement contenant la quantité d'heures CET demandées
	 * pour être transféreés vers le régime pérenne
	 * 
	 * @return
	 */
	public EOMouvement toMouvementCetDemandeTransfertRegimePerenne() {
		return toMouvement(EOMouvement.TYPE_CET_DEMANDE_TRANSFERT_REGIME_PERENNE);
	}

	/**
	 * Raccouci vers l'enregistrement contenant la quantité d'heures CET
	 * réellement décidé pour être transféreés vers le régime pérenne
	 * 
	 * @return
	 */
	public EOMouvement toMouvementCetDecisionTransfertRegimePerenne() {
		return toMouvement(EOMouvement.TYPE_CET_DECISION_TRANSFERT_REGIME_PERENNE);
	}

	/**
	 * Raccourci vers le ou les enregistrements de régularisation de solde de
	 * congé (des congés en plus)
	 * 
	 * @return
	 */
	public NSArray tosMouvementRegularisationSoldeConges() {
		return super.tosMouvement(
				CRIDataBus.newCondition(
						EOMouvement.MOUVEMENT_TYPE_KEY + "=%@", new NSArray(EOMouvement.TYPE_REGULARISATION_SOLDE_CONGES)));
	}

	/**
	 * Nombre de jours rachetes sur conges, par bindage direct sur l'interface
	 * d'administration {@link PageAdminJrti}
	 */
	public Integer joursJrti() {
		return getJour7h00ForMinutesMouvementValue(EOMouvement.TYPE_JRTI);
	}

	/**
	 * Fixer la valeur de rachat de congés, par bindage direct sur l'interface
	 * d'administration {@link PageAdminJrti}
	 * 
	 * @param joursJrti
	 */
	public void setJoursJrti(Integer joursJrti) {
		setMinutesForJour7h00MouvementValue(EOMouvement.TYPE_JRTI, joursJrti);
	}

	/**
	 * Nombre de jours rachetes sur CET, par bindage direct sur l'interface
	 * d'administration {@link PageAdminJrti}
	 */
	public Integer joursRachatCetApresDecision() {
		return getJour7h00ForMinutesMouvementValue(EOMouvement.TYPE_CET_INDEMNISATION_DECRET_2008_1136);
	}

	/**
	 * Fixer la valeur de rachat de CET, par bindage direct sur l'interface
	 * d'administration {@link PageAdminJrti}
	 * 
	 * @param joursJrti
	 */
	public void setJoursRachatCetApresDecision(Integer joursRachatCet) {
		setMinutesForJour7h00MouvementValue(EOMouvement.TYPE_CET_INDEMNISATION_DECRET_2008_1136, joursRachatCet);
	}

	/**
	 * Nombre de jours bascule sur le RAFP, par bindage direct sur l'interface
	 * d'administration {@link PageAdminJrti}
	 */
	public Integer joursBasculeCetVersRafpApresDecision() {
		return getJour7h00ForMinutesMouvementValue(EOMouvement.TYPE_CET_DECISION_TRANSFERT_RAFP);
	}

	/**
	 * Fixer la valeur basculee sur le RAFP, par bindage direct sur l'interface
	 * d'administration {@link PageAdminJrti}
	 * 
	 * @param joursJrti
	 */
	public void setJoursBasculeRafpApresDecision(Integer joursBasculeRafp) {
		setMinutesForJour7h00MouvementValue(EOMouvement.TYPE_CET_DECISION_TRANSFERT_RAFP, joursBasculeRafp);
	}

	/**
	 * Obtenir la valeur en jour à 7h00 d'une valeur stockée en minutes pour un
	 * type de mouvement
	 * 
	 * @param mouvementType
	 * @return
	 */
	private Integer getJour7h00ForMinutesMouvementValue(int mouvementType) {
		Integer jours = new Integer(0);
		EOMouvement mouvement = toMouvement(mouvementType);
		if (mouvement != null) {
			jours = new Integer(mouvement.mouvementMinutes().intValue() / ConstsJour.DUREE_JOUR_7H00);
		}
		return jours;
	}

	/**
	 * Enregistrer la valeur en minutes d'une valeur donnée en jours à 7h00 pour
	 * un type de mouvement
	 * 
	 * @param mouvementType
	 * @param jours7h00
	 */
	private void setMinutesForJour7h00MouvementValue(int mouvementType, Integer jours7h00) {
		boolean isValueNotNull = (jours7h00 != null && jours7h00.intValue() > 0);
		EOMouvement mouvement = toMouvement(mouvementType);
		if (isValueNotNull) {
			Integer minutes = new Integer(jours7h00.intValue() * ConstsJour.DUREE_JOUR_7H00);
			// creation si non existant
			if (mouvement != null) {
				mouvement.setMouvementMinutes(minutes);
			} else {
				EOMouvement.newMouvement(this, mouvementType, minutes);
			}
		} else {
			if (mouvement != null) {
				editingContext().deleteObject(mouvement);
			}
		}
	}

	/**
	 * Effectuer la recherche d'enregistrement d'apres une chaine de caractere
	 * 
	 * @param ec
	 * @param searString
	 * @param dteDebutAnnee
	 * @param withinServiceList
	 * @return
	 */
	public static NSArray findRecordsLike(EOEditingContext ec, String searchString, String dteDebutAnnee, NSArray withinServiceList) {
		NSArray recsV = EOVIndividuConges.findRecordsLike(ec, searchString, dteDebutAnnee, withinServiceList, false, true);
		return findSortedAffectationsAnnuellesForOidsInContext(
				ec, (NSArray) recsV.valueForKey(EOVIndividuConges.OID_AFF_ANN_KEY));
	}

	/**
	 * Recuperer des enregistrements de l'entite <code>AffectationAnnuelle</code>
	 * filtrer d'apres les parametres :
	 * 
	 * @param noIndividu
	 * @param cStructure
	 * @param dateDebutAnnee
	 */
	public static NSArray findAffectationsAnnuelleInContext(EOEditingContext ec, Number noIndividu,
			String cStructure, NSTimestamp dateDebutAnnee) {
		NSArray recsV = EOVIndividuConges.findRecords(ec, noIndividu, cStructure, dateDebutAnnee);
		return findSortedAffectationsAnnuellesForOidsInContext(ec, (NSArray) recsV.valueForKey("oidAffAnn"));
	}

	/**
	 * Recuperer les enregistrements de la table <code>AffectationAnnuelle</code>
	 * dont les cles primaires des objets sont dans le tableau <code>oids</code>.
	 * Les enregistrements sont retournes du plus ancien au plus recent (champ
	 * <code>dteDebutAnnee</code>)
	 */
	public static NSArray findSortedAffectationsAnnuellesForOidsInContext(
			EOEditingContext ec, NSArray oids) {
		StringBuffer strQual = new StringBuffer();
		if (oids.count() == 0) {
			// si rien a filtrer, on met le qualifier restrictif
			strQual.append("oid=-1");
		} else {
			for (int i = 0; i < oids.count(); i++) {
				Number oid = (Number) oids.objectAtIndex(i);
				strQual.append("oid=").append(oid.intValue());
				if (i < oids.count() - 1)
					strQual.append(" OR ");
			}
		}
		return ec.objectsWithFetchSpecification(new EOFetchSpecification(
				ENTITY_NAME,
				EOQualifier.qualifierWithQualifierFormat(strQual.toString(), null),
				LRSort.newSort(DATE_DEBUT_ANNEE_KEY)));
	}

	/**
	 *
	 */
	public static EOAffectationAnnuelle findAffectationAnnuellesForOidInContext(
			EOEditingContext ec, Number oid) {
		EOAffectationAnnuelle result = null;

		NSArray array = findSortedAffectationsAnnuellesForOidsInContext(ec, new NSArray(oid));
		if (array.count() > 0) {
			result = (EOAffectationAnnuelle) array.objectAtIndex(0);
		}

		return result;
	}

	/**
	 * Recuperer un enregistrement <code>EOAffectationAnnuelle</code> a partir
	 * d'une affectation et une date de debut d'ann�e
	 * 
	 * @param edc
	 * @param affectation
	 * @param dateDebutAnnee
	 * 
	 * @return <em>null</em> si non trouv�.
	 */
	public static EOAffectationAnnuelle findAffectationAnnuelleInContext(
			EOEditingContext edc, EOAffectation affectation, NSTimestamp dateDebutAnnee) {
		NSMutableDictionary bindings = new NSMutableDictionary();
		bindings.setObjectForKey(affectation.individu(), "individu");
		bindings.setObjectForKey(affectation.structure(), "service");
		String annee = DateCtrlConges.anneeUnivForDate(dateDebutAnnee);
		bindings.setObjectForKey(annee, "annee");
		NSArray localAffectationsAnnuelles = findAffectationsAnnuellesInContext(bindings, edc);
		if (localAffectationsAnnuelles.count() > 0) {
			return (EOAffectationAnnuelle) localAffectationsAnnuelles.lastObject();
		} else {
			return null;
		}
	}

	/**
	 * @param bindings
	 * @param edc
	 * @return
	 */
	public static NSArray findAffectationsAnnuellesInContext(NSMutableDictionary bindings, EOEditingContext edc) {
		NSArray affectationsAnnuelles = null;
		affectationsAnnuelles = EOUtilities.objectsWithFetchSpecificationAndBindings(edc,
				ENTITY_NAME, "AffectationsAnnuellesPourIndividuEtPourService",
				bindings);
		return affectationsAnnuelles;
	}

	/**
	 * liste de toutes les affectations annuelles associees a une affectation
	 * 
	 * @param ec
	 * @param individu
	 * @return
	 */
	public static NSArray findAffectationsAnnuellesForAffectationInContext(EOEditingContext ec, EOAffectation affectation) {
		EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(
					PERIODES_KEY + "." + EOPeriodeAffectationAnnuelle.AFFECTATION_KEY + "=%@", new NSArray(affectation));
		return UtilDb.fetchArray(ec, ENTITY_NAME, qual, SORT_DATE_DEBUT_ANNEE);
	}

	/**
	 * liste de toutes les affectations annuelles d'un individu
	 * 
	 * @param ec
	 * @param individu
	 * @return
	 */
	public static NSArray findAffectationsAnnuellesForIndividuInContext(EOEditingContext ec, EOIndividu individu) {
		EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(
					PERIODES_KEY + "." + EOPeriodeAffectationAnnuelle.AFFECTATION_KEY + "." + EOAffectation.INDIVIDU_KEY + "=%@", new NSArray(individu));
		return UtilDb.fetchArray(ec, ENTITY_NAME, qual, SORT_DATE_DEBUT_ANNEE);
	}

	/**
	 * idem que {@link filteredAffectationsForResponsabilite}, mais avec une liste
	 * de plusieurs objets {@link Responsabilite}
	 * 
	 * @param ec
	 * @param affectations
	 * @param responsabilite
	 * @return
	 */
	public static NSArray filteredAffectationsForResponsabilites(NSArray affectations, NSArray responsabilites) {
		NSArray affectationsFiltrees = new NSArray();
		if (affectations != null) {
			for (int i = 0; i < responsabilites.count(); i++) {
				Responsabilite responsabilite = (Responsabilite) responsabilites.objectAtIndex(i);
				// ne garder que les affectation du services concernés
				NSArray affectationsService = EOQualifier.filteredArrayWithQualifier(
						affectations, CRIDataBus.newCondition(
								EOAffectationAnnuelle.STRUCTURE_KEY + "=%@", new NSArray(responsabilite.getStructure())));
				affectationsFiltrees = affectationsFiltrees.arrayByAddingObjectsFromArray(
						filteredAffectationsForResponsabilite(affectationsService, responsabilite));
			}
		}
		return affectationsFiltrees;
	}

	/**
	 * retourne la liste filtrée des affectations apres verification des droits de
	 * visibilite
	 * 
	 * @param ec
	 * @param affectations
	 * @param responsabilite
	 * @return
	 */
	public static NSArray filteredAffectationsForResponsabilite(NSArray affectations, Responsabilite responsabilite) {

		if (affectations == null) {
			return new NSArray();
		}
		// cas de la visibilite sur tout le service
		if (responsabilite.getNiveauParDefautSurService().intValue() > ConstsDroit.DROIT_NIVEAU_RIEN.intValue()) {
			return affectations;
		}
		NSArray affectationsFiltrees = new NSArray();
		for (int i = 0; i < responsabilite.getDroitList().count(); i++) {

			EODroit droit = (EODroit) responsabilite.getDroitList().objectAtIndex(i);

			if (droit.cdrNiveau().intValue() > ConstsDroit.DROIT_NIVEAU_RIEN.intValue()) {

				EOIndividu individu = null;

				if (droit.cdrType().equals(ConstsDroit.DROIT_CIBLE_CHEF_DE_SERVICE)) {
					individu = droit.toStructure().responsable();
				} else {
					individu = droit.toIndividu();
				}

				if (individu != null) {
					affectationsFiltrees = affectationsFiltrees.arrayByAddingObjectsFromArray(
							filteredAffectationsForIndividu(affectations, individu));
				}
			}
		}
		return affectationsFiltrees;
	}

	/**
	 * trouver les affectations d'un individu parmi un tableau d'affectations
	 * 
	 * @param affectationsAnnuelles
	 * @param individu
	 */
	public static NSArray filteredAffectationsForIndividu(NSArray affectationsAnnuelles, EOIndividu individu) {
		NSMutableArray affectationsFiltrees = new NSMutableArray();
		for (int i = 0; i < affectationsAnnuelles.count(); i++) {
			EOAffectationAnnuelle affectationAnnuelle = (EOAffectationAnnuelle) affectationsAnnuelles.objectAtIndex(i);
			if (affectationAnnuelle.individu() == individu)
				affectationsFiltrees.addObject(affectationAnnuelle);
		}
		return affectationsFiltrees.immutableClone();
	}

	/**
	 * La valeur des reliquats de congés initiaux en minutes. N'est utilisé que
	 * par les écrans de gestion du CET, on ne retourne donc que le reliquat du
	 * planning réel
	 * 
	 * @return
	 */
	public int reliquatInitialEnMinutes() {
		return PlanningCalcul.reliquatInitialEnMinutesForAffAnn(editingContext(), this);
	}

	/**
	 * La valeur des reliquats de congés initiaux en heures. N'est utilisé que par
	 * les écrans de gestion du CET, on ne retourne donc que le reliquat du
	 * planning réel
	 * 
	 * @return
	 */
	public String reliquatInitialEnHeures() {
		String reliquatInitialEnHeures = TimeCtrl.stringForMinutes(reliquatInitialEnMinutes());

		return reliquatInitialEnHeures;
	}

	/**
	 * #reliquatInitialEnMinutes() converties en jours a 7h00
	 * 
	 * @return
	 */
	public float reliquatInitialEnJour7h00() {
		float reliquatInitialEnJour7h00 = (float) reliquatInitialEnMinutes() / (float) ConstsJour.DUREE_JOUR_7H00;

		return reliquatInitialEnJour7h00;
	}

	// données relatives au CET

	private CetFactory _cetFactory;

	/**
	 * acces aux traitments liés au CET
	 * 
	 * @return
	 */
	public CetFactory cetFactory() {
		if (_cetFactory == null) {
			_cetFactory = new CetFactory(this);
		}
		return _cetFactory;
	}

	/**
	 * Indique s'il existe un mouvement concernant l'ancien regime CET (demande ou
	 * décision)
	 * 
	 * @return
	 */
	public boolean hasMouvementCetAncienRegime() {
		boolean hasMouvementCetAncienRegime = false;

		hasMouvementCetAncienRegime =
				toMouvementCetDemandeMaintienCetAncienRegime() != null ||
						toMouvementCetDemandeTransfertRafpAncienRegime() != null ||
						toMouvementCetDemandeIndemnisationAncienRegime() != null ||
						toMouvementCetDemandeTransfertRegimePerenne() != null ||
						toMouvementCetDecisionMaintienCetAncienRegime() != null ||
						toMouvementCetDecisionTransfertRafpAncienRegime() != null ||
						toMouvementCetDecisionIndemnisationAncienRegime() != null ||
						toMouvementCetDecisionTransfertRegimePerenne() != null;

		return hasMouvementCetAncienRegime;
	}

	/**
	 * Cette méthode donne la quotité moyenne constatée pour un agent s'il avait
	 * un planning unique, c'est à dire un planning avec une unique quotité par
	 * jour, résultant de la somme des quotités journalières de tous ses plannings
	 * (si multi-affectation).
	 * 
	 * Il est possible de choisir s'il faut prendre en compte ou non les jours de
	 * non affectation (i.e. jours avec une quotité de 0).
	 * 
	 * Cette méthode sert entre autres pour déterminer le plafond maximum d'une
	 * épargne CET.
	 * 
	 * Retourne un float entre 0 et 1
	 * 
	 * @return
	 */
	public float quotiteMoyennePlanningUnique(
			boolean priseEnCompteJoursSansAffectation) {

		NSMutableDictionary dicoQuotiteJour = new NSMutableDictionary();

		// pour tous les jours de l'année universitaire
		NSTimestamp dateJour = dateDebutAnnee();

		// parmi tous les plannings
		NSArray affAnnList = EOAffectationAnnuelle.findAffectationsAnnuellesForIndividuInContext(editingContext(), individu());

		// construire la liste des quotités uniques
		while (DateCtrlConges.isBeforeEq(dateJour, dateFinAnnee())) {

			float quotiteJour = (float) 0;

			for (int i = 0; i < affAnnList.count(); i++) {
				EOAffectationAnnuelle affAnn = (EOAffectationAnnuelle) affAnnList.objectAtIndex(i);
				for (int j = 0; j < affAnn.periodes().count(); j++) {
					EOPeriodeAffectationAnnuelle periode = (EOPeriodeAffectationAnnuelle) affAnn.periodes().objectAtIndex(j);
					if (periode.contientDate(dateJour)) {
						quotiteJour += periode.quotite().floatValue();
					}
				}
			}

			dicoQuotiteJour.setObjectForKey(new Float(quotiteJour), DateCtrlConges.dateToString(dateJour));

			dateJour = dateJour.timestampByAddingGregorianUnits(0, 0, 1, 0, 0, 0);
		}

		// calculer la quotité moyenne
		NSArray keys = dicoQuotiteJour.allKeys();
		int nbJours = 0;
		float sommeQuotite = (float) 0;
		for (int i = 0; i < keys.count(); i++) {
			String key = (String) keys.objectAtIndex(i);
			Float quotiteJour = (Float) dicoQuotiteJour.objectForKey(key);
			if (quotiteJour.floatValue() == (float) 0) {
				if (priseEnCompteJoursSansAffectation) {
					nbJours++;
				}
			} else {
				sommeQuotite += quotiteJour.floatValue();
				nbJours++;
			}
		}

		// moyenne
		float moyenneQuotite = (float) 0;
		if (nbJours > 0) {
			moyenneQuotite = sommeQuotite / (float) nbJours;
		}

		return moyenneQuotite / (float) 100;
	}

	/**
	 * Retourne la premiere {@link EOAffectationAnnuelle} courant trouvée
	 * 
	 * @return
	 */
	public static EOAffectationAnnuelle getPremiereCourante(
			NSArray eoAffectationAnnuelleArray) {
		EOAffectationAnnuelle eoAffectationAnnuelle = null;

		NSTimestamp dateCourante = DateCtrlConges.now();

		for (int i = 0; i < eoAffectationAnnuelleArray.count() && eoAffectationAnnuelle == null; i++) {
			EOAffectationAnnuelle eoAffectationAnnuelleItem = (EOAffectationAnnuelle) eoAffectationAnnuelleArray.objectAtIndex(i);
			NSArray eoPeriodeArray = eoAffectationAnnuelleItem.periodes();
			for (int j = 0; j < eoPeriodeArray.count() && eoAffectationAnnuelle == null; j++) {
				EOPeriodeAffectationAnnuelle eoPeriodeItem = (EOPeriodeAffectationAnnuelle) eoPeriodeArray.objectAtIndex(j);
				if (DateCtrlConges.isAfterEq(dateCourante, eoPeriodeItem.dateDebut()) &&
						DateCtrlConges.isBeforeEq(dateCourante, eoPeriodeItem.dateFin())) {
					eoAffectationAnnuelle = eoAffectationAnnuelleItem;
				}
			}
		}

		return eoAffectationAnnuelle;
	}

	/**
	 * Affichage de la liste des périodes (dates + quotité)
	 * 
	 * @return
	 */
	public String affichageListePeriode() {
		String str = "";

		NSArray eoPeriodeArray = periodes();

		for (int i = 0; i < eoPeriodeArray.count(); i++) {
			EOPeriodeAffectationAnnuelle eoPeriodeItem = (EOPeriodeAffectationAnnuelle) eoPeriodeArray.objectAtIndex(i);
			str += DateCtrlConges.dateToString(eoPeriodeItem.dateDebut()) + " - " +
					DateCtrlConges.dateToString(eoPeriodeItem.dateFin()) + " @ " +
					eoPeriodeItem.quotite().intValue() + "%";
			if (i < eoPeriodeArray.count() - 1) {
				str += ", ";
			}
		}

		return str;
	}
}
