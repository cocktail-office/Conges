package fr.univlr.cri.conges.objects;

import java.util.Enumeration;

import com.webobjects.eocontrol.EOCustomObject;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOHoraire;
import fr.univlr.cri.conges.eos.modele.planning.I_ClasseMetierNotificationParametre;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.StringCtrl;

/**
 * @author egeze
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class HoraireHebdomadaire extends EOCustomObject {

	private String nom;
	private String total;
	private String totalBonus;

	private NSMutableArray horairesJournaliers;

	private EOHoraire horaire;

	/** duree minimum d'un horaire hebdo pour un 100% */
	private static int dureeHoraireHebdoMini;
	/** duree maximum d'un horaire hebdo pour un 100% */
	private static int dureeHoraireHebdoMaxi;
	/** duree maximum d'un horaire hebdo pour un 100% en hors normes */
	private static int dureeHoraireHebdoMaxiHorsNorme;
	/** duree maximum travail demi journée */
	private static int demiJourneeDureeMaxi;

	private String erreur;

	/** le nom par defaut d'un nouvel horaire */
	private final static String DEFAULT_NAME = "Nouvel horaire";

	public HoraireHebdomadaire() {
		super();
	}

	/**
	 * @see I_ClasseMetierNotificationParametre
	 * @param parametre
	 */
	public static void initStaticField(
			Parametre parametre) {
		if (parametre == Parametre.PARAM_DUREE_HORAIRE_HEBDO_MINI) {
			dureeHoraireHebdoMini = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_DUREE_HORAIRE_HEBDO_MAXI) {
			dureeHoraireHebdoMaxi = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_DUREE_HORAIRE_HEBDO_MAXI_HORS_NORMES) {
			dureeHoraireHebdoMaxiHorsNorme = TimeCtrl.getMinutes(parametre.getParamValueString());
		} else if (parametre == Parametre.PARAM_DEMI_JOURNEE_DUREE_MAXI) {
			demiJourneeDureeMaxi = TimeCtrl.getMinutes(parametre.getParamValueString());
		}
	}

	/**
	 * @param unHoraire
	 */
	public void setHoraire(EOHoraire unHoraire) {
		horaire = unHoraire;
		if (unHoraire != null) {
			setNom(unHoraire.nom());
		} else {
			setNom(DEFAULT_NAME);
		}
		horairesJournaliers = null;
		for (int index = 0; index < 6; index++) {
			HoraireJournalier unHoraireJournalier = null;
			unHoraireJournalier = new HoraireJournalier(index, this);
			addHoraireJournalier(unHoraireJournalier);
		}
	}

	public EOHoraire horaire() {
		return horaire;
	}

	/**
	 * @param unHoraireJournalier
	 */
	private void addHoraireJournalier(HoraireJournalier unHoraireJournalier) {
		if (horairesJournaliers == null)
			horairesJournaliers = new NSMutableArray();
		horairesJournaliers.addObject(unHoraireJournalier);

	}

	public NSArray horairesJournaliers() {
		return horairesJournaliers;
	}

	/**
	 * @param string
	 */
	public void setNom(String value) {
		nom = value;

	}

	public String erreur() {
		return erreur;
	}

	/**
	 * @param string
	 */
	public void setErreur(String value) {
		erreur = value;

	}

	/**
	 * @return
	 */
	public String nom() {
		return nom;
	}

	/**
	 * @return
	 */
	public EOHoraire getHoraire() {
		return horaire;
	}

	/**
	 * @param horaireSource
	 * @param horaireDest
	 */
	public void recopierHoraire(HoraireJournalier horaireSource, HoraireJournalier horaireDest) {
		horaireDest.setHeureDebutAM(horaireSource.getHeureDebutAM());
		horaireDest.setHeureFinAM(horaireSource.getHeureFinAM());
		horaireDest.setHeureDebutPM(horaireSource.getHeureDebutPM());
		horaireDest.setHeureFinPM(horaireSource.getHeureFinPM());
		horaireDest.setHeureDebutPause(horaireSource.getHeureDebutPause());
		recalculerTotaux();

	}

	/**
	 * 
	 */
	public void recalculerTotaux() {
		int totalHebdomadaire = 0;
		int totalBonus = 0;

		NSArray horairesJournaliers = horairesJournaliers();
		Enumeration enumHorairesJournaliers = horairesJournaliers.objectEnumerator();
		HoraireJournalier unHoraireJournalier = null;

		while (enumHorairesJournaliers.hasMoreElements()) {
			unHoraireJournalier = (HoraireJournalier) enumHorairesJournaliers.nextElement();
			unHoraireJournalier.recalculerTotal();
			totalHebdomadaire += minutes(unHoraireJournalier.getHoraireJournalierTotal());
			totalBonus += minutes(unHoraireJournalier.getBonus());
		}
		setTotal(horaire(totalHebdomadaire));
		setTotalBonus(horaire(totalBonus));
	}

	/**
	 * @param horaire
	 */
	public void effacerHoraire(HoraireJournalier horaire) {
		horaire.setHeureDebutAM("");
		horaire.setHeureFinAM("");
		horaire.setHeureDebutPM("");
		horaire.setHeureFinPM("");
		horaire.setHeureDebutPause("");
		recalculerTotaux();
	}

	/**
	 * @return
	 */
	public String getTotal() {
		return total;
	}

	/**
	 * @param string
	 */
	private void setTotal(String string) {
		total = string;
	}

	/**
	 * @return
	 */
	public String getTotalBonus() {
		return totalBonus;
	}

	/**
	 * @param string
	 */
	private void setTotalBonus(String string) {
		totalBonus = string;
	}

	public int minutes(String uneDuree) {
		int lesMinutes = 0;
		NSArray temps = NSArray.componentsSeparatedByString(uneDuree, "h");
		if (temps != null && temps.count() == 2) {
			int heures = (Integer.valueOf((String) temps.objectAtIndex(0))).intValue();
			int minutes = (Integer.valueOf((String) temps.lastObject())).intValue();

			lesMinutes = heures * 60 + minutes;
		}

		return lesMinutes;
	}

	public String horaire(int duree) {
		String horaire = "";
		int heures = duree / 60;
		int minutes = duree % 60;

		horaire = String.valueOf(heures) + "h";
		if (heures < 10)
			horaire = "0" + horaire;
		if (minutes < 10)
			horaire = horaire + "0";
		horaire = horaire + String.valueOf(minutes);

		return horaire;
	}

	/**
	 * @return
	 */
	public boolean isValide(
			EOAffectationAnnuelle affectationAnnuelle,
			String dureeARealisee,
			Number quotite,
			String libelle) {
		boolean isValide = true;

		setErreur("");

		int totalHebdo = minutes(getTotal());
		int totalARealiser = minutes(dureeARealisee);
		if (totalHebdo != totalARealiser) {
			setErreur("La duree de l'horaire n'est pas bonne, a realiser : " + dureeARealisee + " et actuel : " + getTotal());
			isValide = false;
		}

		NSArray horaires = horairesJournaliers();

		for (int i = 0; isValide && i < horaires.count(); i++) {
			HoraireJournalier unHoraireJournalier = (HoraireJournalier) horaires.objectAtIndex(i);
			isValide = unHoraireJournalier.isValide(affectationAnnuelle.isPasseDroit());
			if (!isValide) {
				setErreur("Un des horaire journalier n'est pas valide : " + unHoraireJournalier.libelleJour() + " => " + unHoraireJournalier.getErreur());
			}
		}

		if (isValide) {
			if (!affectationAnnuelle.isTempsPartielAnnualise() && quotite == null) {
				isValide = false;
				setErreur("La quotite n'est pas renseignee");
			}
		}

		if (isValide) {
			// verification des durees
			int laDureeHoraireHebdoMaxiHorsNorme = (affectationAnnuelle.isTempsPartielAnnualise() ? dureeHoraireHebdoMaxiHorsNorme : dureeHoraireHebdoMaxiHorsNorme * quotite.intValue() / 100);
			if (affectationAnnuelle.isHorsNorme() && totalARealiser > laDureeHoraireHebdoMaxiHorsNorme) {
				isValide = false;
				String laDureeHoraireHebdoMaxiHorsNormeHeures = TimeCtrl.stringForMinutes(laDureeHoraireHebdoMaxiHorsNorme);
				setErreur("Vous depassez le maximum d'heures autorisees pour un horaire hebdomadaire (" +
						laDureeHoraireHebdoMaxiHorsNormeHeures + ")");
			}
		}

		if (isValide) {
			int laDureeHoraireHebdoMaxi = (affectationAnnuelle.isTempsPartielAnnualise() ? dureeHoraireHebdoMaxi : dureeHoraireHebdoMaxi * quotite.intValue() / 100);
			if (!affectationAnnuelle.isHorsNorme() && totalARealiser > laDureeHoraireHebdoMaxi) {
				isValide = false;
				String laDureeHoraireHebdoMaxiHeures = TimeCtrl.stringForMinutes(laDureeHoraireHebdoMaxi);
				setErreur("Vous depassez le maximum d'heures autorisees pour un horaire hebdomadaire ("
						+ laDureeHoraireHebdoMaxiHeures + ")");
			}
		}

		if (isValide) {
			int laDureeHoraireHebdoMini = (affectationAnnuelle.isTempsPartielAnnualise() ? dureeHoraireHebdoMini : dureeHoraireHebdoMini * quotite.intValue() / 100);
			if (!affectationAnnuelle.isTempsPartielAnnualise() && totalARealiser < laDureeHoraireHebdoMini) {
				isValide = false;
				String laDureeHoraireHebdoMiniHeures = TimeCtrl.stringForMinutes(laDureeHoraireHebdoMini);
				setErreur("Vous ne faites pas le minimum d'heures pour un horaire hebdomadaire (" +
						laDureeHoraireHebdoMiniHeures + ")");
			}
		}

		// verification de l'unicite du nom
		if (isValide) {
			if (StringCtrl.isEmpty(libelle)) {
				isValide = false;
				setErreur("Le libelle n'est pas renseigne");
			}
		}

		if (isValide) {
			EOQualifier qual = EOQualifier.qualifierWithQualifierFormat("nom = %@", new NSArray(libelle));
			NSArray horairesNomIdentique = EOQualifier.filteredArrayWithQualifier(affectationAnnuelle.horaires(), qual);
			if (horairesNomIdentique.count() > 0) {
				// verification qu'il ne s'agit pas de lui meme
				if (horairesNomIdentique.count() == 1) {
					EOHoraire lHoraire = (EOHoraire) horairesNomIdentique.lastObject();
					if (lHoraire != horaire()) {
						isValide = false;
						setErreur("Le libelle \"" + libelle + "\" existe deja, veuillez en saisir un autre.");
					}
				}
			}
		}

		return isValide;
	}

	/**
	 * @return
	 */
	public String toStringHoraires() {
		String toStringHoraires = "";
		NSArray horaires = horairesJournaliers();
		Enumeration enumHorairesJournaliers = horaires.objectEnumerator();
		HoraireJournalier unHoraireJournalier = null;

		while (enumHorairesJournaliers.hasMoreElements()) {
			unHoraireJournalier = (HoraireJournalier) enumHorairesJournaliers.nextElement();
			toStringHoraires += unHoraireJournalier.toStringHoraire();
			toStringHoraires += ",";
		}

		return toStringHoraires;
	}

	/**
	 * @return
	 */
	public String toStringPauses() {
		String toStringPauses = "";
		NSArray horaires = horairesJournaliers();
		Enumeration enumHorairesJournaliers = horaires.objectEnumerator();
		HoraireJournalier unHoraireJournalier = null;

		while (enumHorairesJournaliers.hasMoreElements()) {
			unHoraireJournalier = (HoraireJournalier) enumHorairesJournaliers.nextElement();
			toStringPauses += unHoraireJournalier.toStringPause();
			toStringPauses += ",";
		}

		return toStringPauses;
	}

	public String toStringDurees() {
		String toStringDurees = "";
		NSArray horaires = horairesJournaliers();
		Enumeration enumHorairesJournaliers = horaires.objectEnumerator();
		HoraireJournalier unHoraireJournalier = null;

		while (enumHorairesJournaliers.hasMoreElements()) {
			unHoraireJournalier = (HoraireJournalier) enumHorairesJournaliers.nextElement();
			// toStringDurees += unHoraireJournalier.toStringDuree();
			toStringDurees += unHoraireJournalier.toStringDureeAvecBonus();
			toStringDurees += ",";
		}
		return toStringDurees;
	}

	public String toStringDureesAM() {
		String toStringDureesAM = "";
		NSArray horaires = horairesJournaliers();
		Enumeration enumHorairesJournaliers = horaires.objectEnumerator();
		HoraireJournalier unHoraireJournalier = null;

		while (enumHorairesJournaliers.hasMoreElements()) {
			unHoraireJournalier = (HoraireJournalier) enumHorairesJournaliers.nextElement();
			toStringDureesAM += unHoraireJournalier.toStringDureeAM();
			toStringDureesAM += ",";
		}
		return toStringDureesAM;
	}

	public String toStringDureesPM() {
		String toStringDureesPM = "";
		NSArray horaires = horairesJournaliers();
		Enumeration enumHorairesJournaliers = horaires.objectEnumerator();
		HoraireJournalier unHoraireJournalier = null;

		while (enumHorairesJournaliers.hasMoreElements()) {
			unHoraireJournalier = (HoraireJournalier) enumHorairesJournaliers.nextElement();
			toStringDureesPM += unHoraireJournalier.toStringDureePM();
			toStringDureesPM += ",";
		}
		return toStringDureesPM;
	}

	/**
	 * retourne l'horaire sous la forme d'occupations ex : 8h00-12h00 et
	 * 14h00-17h00
	 * 
	 * @return
	 */
	public NSArray occupationsHoraireHebdomadaire(NSTimestamp dateLundi, boolean voirPauses) {
		NSArray occupationsHoraireHebdomadaire = new NSArray();
		NSArray horaires = horairesJournaliers();
		Enumeration enumHorairesJournaliers = horaires.objectEnumerator();
		int index = 0;

		while (enumHorairesJournaliers.hasMoreElements()) {
			HoraireJournalier unHoraireJournalier = (HoraireJournalier) enumHorairesJournaliers.nextElement();
			NSTimestamp dateJour = dateLundi.timestampByAddingGregorianUnits(0, 0, index++, 0, 0, 0);
			occupationsHoraireHebdomadaire = occupationsHoraireHebdomadaire.arrayByAddingObjectsFromArray(unHoraireJournalier.occupationsHoraireJournalier(dateJour, voirPauses));
		}
		return occupationsHoraireHebdomadaire;
	}

	/**
	 * Est-ce qu'une couleur est renseignee.
	 */
	public boolean hasNotCouleur() {
		if (horaire() == null)
			return false;
		return StringCtrl.isEmpty(horaire().couleur()) ||
				horaire().couleur().toUpperCase().equals("#FFFFFF") ||
				horaire().couleur().toUpperCase().equals("WHITE");
	}

	private int bonusSamediMatin;

	public void setBonusSamediMatin(int value) {
		bonusSamediMatin = value;
	}

	public int getBonusSamediMatin() {
		return bonusSamediMatin;
	}

	private int bonusSamediApresMidi;

	private final static String LI_START = "<li style=\"display:list-item\">";
	private final static String LI_END = "</li>";

	/**
	 * Message d'information relative a un horaire
	 * 
	 * @return
	 */
	public String getStrHtmlContraintesEmbaucheDebauche(EOAffectationAnnuelle affAnn, Number quotite) {

		// barrer les contraintes si l'agent est passe droit
		String strStrikeStart = "";
		String strStrikeEnd = "";
		if (affAnn.isPasseDroit()) {
			strStrikeStart = "<s>";
			strStrikeEnd = "</s>";
		}

		StringBuffer sb = new StringBuffer("<ul>");
		sb.append(LI_START).append(strStrikeStart).append("Matin, embauche entre ").append(TimeCtrl.stringDureeToHeure(
					TimeCtrl.stringForMinutes(HoraireJournalier.getAMDebutMini()))).append(" et ").
				append(TimeCtrl.stringDureeToHeure(TimeCtrl.stringForMinutes(HoraireJournalier.getAMDebutMaxi()))).append(strStrikeEnd).append(LI_END);
		sb.append(LI_START).append(strStrikeStart).append("Matin, d&eacute;bauche entre ").
				append(TimeCtrl.stringDureeToHeure(TimeCtrl.stringForMinutes(HoraireJournalier.getPauseMeridienneDebMini()))).append(" et ").
				append(TimeCtrl.stringDureeToHeure(TimeCtrl.stringForMinutes(HoraireJournalier.getPauseMeridienneFinMaxi()))).append(strStrikeEnd).append(LI_END);
		sb.append(LI_START).append(strStrikeStart).append("Apr&egrave;s-midi, embauche entre ").
				append(TimeCtrl.stringDureeToHeure(TimeCtrl.stringForMinutes(
						HoraireJournalier.getPauseMeridienneDebMini() + HoraireJournalier.getPauseMeridienneDureeMini()))).append(" et ").
				append(TimeCtrl.stringDureeToHeure(TimeCtrl.stringForMinutes(HoraireJournalier.getPauseMeridienneFinMaxi()))).append(strStrikeEnd).append(LI_END);
		sb.append(LI_START).append(strStrikeStart).append("Apr&egrave;s-midi, d&eacute;bauche entre ").
				append(TimeCtrl.stringDureeToHeure(TimeCtrl.stringForMinutes(HoraireJournalier.getPMFinMini()))).append(" et ").
				append(TimeCtrl.stringDureeToHeure(TimeCtrl.stringForMinutes(HoraireJournalier.getPMFinMaxi()))).append(strStrikeEnd).append(LI_END);
		sb.append(LI_START).append(strStrikeStart).append("Dur&eacute;e minimum de la pause m&eacute;ridienne : ").append(
				HoraireJournalier.getPauseMeridienneDureeMini()).append(" min").append(strStrikeEnd).append(LI_END);

		// contraintes sur la duree
		if (affAnn.isTempsPartielAnnualise() || (!affAnn.isTempsPartielAnnualise() && quotite != null)) {
			// minimum
			int laDureeHoraireHebdoMini = affAnn.isTempsPartielAnnualise() ? dureeHoraireHebdoMini : dureeHoraireHebdoMini * quotite.intValue() / 100;
			sb.append(LI_START).append("Dur&eacute;e totale minimum de l'horaire : ").append(
					TimeCtrl.stringForMinutes(laDureeHoraireHebdoMini)).append(LI_END);

			// maximum
			if (!affAnn.isHorsNorme()) {
				int laDureeHoraireHebdoMaxi = affAnn.isTempsPartielAnnualise() ? dureeHoraireHebdoMaxi : dureeHoraireHebdoMaxi * quotite.intValue() / 100;
				sb.append(LI_START).append("Dur&eacute;e totale maximum de l'horaire : ").append(
						TimeCtrl.stringForMinutes(laDureeHoraireHebdoMaxi)).append(LI_END);
			} else {
				int laDureeHoraireHebdoMaxi = affAnn.isTempsPartielAnnualise() ? dureeHoraireHebdoMaxiHorsNorme : dureeHoraireHebdoMaxiHorsNorme * quotite.intValue() / 100;
				sb.append(LI_START).append("Dur&eacute;e totale maximum de l'horaire : ").append(
						TimeCtrl.stringForMinutes(laDureeHoraireHebdoMaxi)).append(LI_END);
			}
		}

		// duree de la demi journée
		sb.append(LI_START).append(strStrikeStart).append("Dur&eacute;e maximum d'une demi-journ&eacute;e de travail : ").append(
				TimeCtrl.stringForMinutes(demiJourneeDureeMaxi)).append(strStrikeEnd).append(LI_END);

		sb.append("</ul>");
		return sb.toString();
	}

	/**
	 * Dupliquer un horaire
	 * 
	 * @param horaireHebdomadaire
	 * @return
	 */
	public static HoraireHebdomadaire dupliquer(HoraireHebdomadaire horaireHebdomadaire) {
		HoraireHebdomadaire nouvelHoraireHebdomadaire = new HoraireHebdomadaire();
		nouvelHoraireHebdomadaire.setNom(horaireHebdomadaire.nom() + " - copie");

		// les horaires journaliers le composant
		for (int i = 0; i < horaireHebdomadaire.horairesJournaliers().count(); i++) {
			HoraireJournalier horaireJournalier = (HoraireJournalier) horaireHebdomadaire.horairesJournaliers().objectAtIndex(i);
			HoraireJournalier nouvelHoraireJournalier = new HoraireJournalier(i, nouvelHoraireHebdomadaire);
			nouvelHoraireJournalier.setHeureDebutAM(horaireJournalier.getHeureDebutAM());
			nouvelHoraireJournalier.setHeureDebutPause(horaireJournalier.getHeureDebutPause());
			nouvelHoraireJournalier.setHeureDebutPM(horaireJournalier.getHeureDebutPM());
			nouvelHoraireJournalier.setHeureFinAM(horaireJournalier.getHeureFinAM());
			nouvelHoraireJournalier.setHeureFinPM(horaireJournalier.getHeureFinPM());
			nouvelHoraireJournalier.recalculerTotal();

			nouvelHoraireHebdomadaire.addHoraireJournalier(nouvelHoraireJournalier);
		}

		nouvelHoraireHebdomadaire.recalculerTotaux();

		return nouvelHoraireHebdomadaire;
	}

	public final int getBonusSamediApresMidi() {
		return bonusSamediApresMidi;
	}

	public final void setBonusSamediApresMidi(int bonusSamediApresMidi) {
		this.bonusSamediApresMidi = bonusSamediApresMidi;
	}

}
