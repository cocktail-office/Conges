package fr.univlr.cri.conges.objects;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;

public interface I_Absence {

	public NSTimestamp dateDebut();

	void setDateDebut(NSTimestamp date);

	public NSTimestamp dateFin();

	void setDateFin(NSTimestamp date);

	public String duree();

	public String dureeComptabilisee();

	public String type();

	String libelleStatut();

	public String debit();

	int dureeEnMinutes();

	EOIndividu valideur();

	EOIndividu viseur();

	NSTimestamp dateValidation();

	NSTimestamp dateVisa();

	NSTimestamp dateCreation();

	NSTimestamp dateModification();

	public String motif();

	boolean isCongeLegal();

	boolean isFermeture();

	boolean isFermetureOriginale();

	boolean isAbsenceBilan();

	boolean isHoraireForce();

	int leDebitReliquats();

	int leDebitConges();

	int leDebitDechargeSyndicale();

	int leDebitCET();

	/** l'absence est-elle typee DRH */
	public boolean isCongeDRH();

	/** l'absence est-elle typee DRH au niveau composante */
	public boolean isCongeDRHComposante();

	public boolean isOccupationMinute();

	public boolean isOccupationJour();

	/** absence posee par delegation ? */
	EOIndividu delegue();

	/** methodes pour forcer automatiquement les jours en horaire forcé */
	public void doForceJour();

	/** le planning associé à l'absence */
	public void setPlanning(Planning p);

	/** l'affectation annuelle attachée */
	public EOAffectationAnnuelle affectationAnnuelle();

	/** le details des jours occupés par l'absence */
	public NSArray lesNodesJours();

	// le nom des methodes pour acces via NSKeyValueCoding
	public final static String IS_HORAIRE_FORCE_KEY = "isHoraireForce";
	public final static String DATE_DEBUT_KEY = "dateDebut";
	public final static String DATE_FIN_KEY = "dateFin";
	public final static String LES_NODES_JOURS_KEY = "lesNodesJours";
	public final static String ORDRE_PRIORITE_KEY = "ordrePriorite";
	public final static String SOUS_ORDRE_PRIORITE_KEY = "sousOrdrePriorite";

	// classement des absences par nature (d'abord les congés légaux)
	public final static int ORDRE_PRIORITE_1 = 1;
	public final static int ORDRE_PRIORITE_2 = 2;

	public int ordrePriorite();

	public int sousOrdrePriorite();
}
