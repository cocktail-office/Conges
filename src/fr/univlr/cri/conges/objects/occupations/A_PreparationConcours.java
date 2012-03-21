/**
 * 
 */
package fr.univlr.cri.conges.objects.occupations;

import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.planning.EOOccupation;
import fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation;
import fr.univlr.cri.conges.objects.Planning;

/**
 * @author Cyril TARADE <cyril.tarade at cocktail.org>
 *
 */
public abstract class A_PreparationConcours
	extends Occupation {

	public final static int CATEGORIE_A = 1;
	public final static int CATEGORIE_B = 2;
	public final static int CATEGORIE_C = 3;

	public A_PreparationConcours(
			EOTypeOccupation unType, Planning unPlanning, NSTimestamp debutTS, NSTimestamp finTS, String unMotif, EOEditingContext ec) {
		super(unType, unPlanning, debutTS, finTS, unMotif, ec);
	}

	public A_PreparationConcours(
			EOOccupation uneOccupation, Planning unPlanning, EOEditingContext ec) {
		super(uneOccupation, unPlanning, ec);
	}
	
	public A_PreparationConcours(
			EOOccupation uneOccupation, EOEditingContext ec) {
		super(uneOccupation, ec);
	}
	
	/**
	 * La catégorie de personnel concernée
	 * @return
	 */
	public abstract int categorie();

	/**
	 * 
	 * @return
	 */
	private boolean isCategorieA() {
		return categorie() == CATEGORIE_A;
	}

	/**
	 * 
	 * @return
	 */
	private boolean isCategorieB() {
		return categorie() == CATEGORIE_B;
	}

	/**
	 * 
	 * @return
	 */
	private boolean isCategorieC() {
		return categorie() == CATEGORIE_C;
	}

	private final static int NB_PREPARATION_MAX_PAR_AN_FONCTIONNAIRE 	= 1;
	private final static int NB_PREPARATION_MAX_PAR_AN_CONTRACTUEL 		= 2;
	
	
	/**
	 * 
	 * @return
	 */
	private int nbPreparationMaxParAn() {
		int nbPreparationMaxParAn = NB_PREPARATION_MAX_PAR_AN_FONCTIONNAIRE;
		
		if (lePlanning.affectationAnnuelle().individu().isContractuel(lePlanning.affectationAnnuelle())) {
			nbPreparationMaxParAn = NB_PREPARATION_MAX_PAR_AN_CONTRACTUEL;
		}
		
		return nbPreparationMaxParAn;
	}
	
	private final static float DUREE_MAX_ABSENCE_EN_JOUR_CATEGORIE_A = (float) 2;
	private final static float DUREE_MAX_ABSENCE_EN_JOUR_CATEGORIE_B = (float) 1;
	private final static float DUREE_MAX_ABSENCE_EN_JOUR_CATEGORIE_C = (float) 0.5;

	/**
	 * 
	 * @return
	 */
	private float dureeMaxAbsenceEnJour() {
		float dureeMaxAbsenceEnJour = 0;
		
		if (isCategorieA()) {
			dureeMaxAbsenceEnJour = DUREE_MAX_ABSENCE_EN_JOUR_CATEGORIE_A;
		} else if (isCategorieB()) {
			dureeMaxAbsenceEnJour = DUREE_MAX_ABSENCE_EN_JOUR_CATEGORIE_B;
		} else if (isCategorieC()) {
			dureeMaxAbsenceEnJour = DUREE_MAX_ABSENCE_EN_JOUR_CATEGORIE_C;
		} 

		return dureeMaxAbsenceEnJour;
	}
	
	/* (non-Javadoc)
	 * @see fr.univlr.cri.conges.objects.occupations.Occupation#isValide()
	 */
	@Override
	public boolean isValide() {
    boolean isValide = super.isValide();

    if (isValide) {
      EOIndividu unIndividu = lePlanning.affectationAnnuelle().individu();
      NSTimestamp debut = lePlanning.affectationAnnuelle().dateDebutAnnee();
      NSTimestamp fin =		lePlanning.affectationAnnuelle().dateFinAnnee();
      NSMutableDictionary bindings = new NSMutableDictionary();
      bindings.setObjectForKey(unIndividu, "individu");
      bindings.setObjectForKey(debut, "debut");
      bindings.setObjectForKey(fin, "fin");
      bindings.setObjectForKey(leType, "type");
      NSArray occupationsDeCeType = EOUtilities.objectsWithFetchSpecificationAndBindings(
      		edc, EOOccupation.ENTITY_NAME, "OccupationsPourIndividu", bindings);

      if (occupationsDeCeType.count() >= nbPreparationMaxParAn()) {
        isValide = false;
        setErrorMsg("Absence limitée à " + nbPreparationMaxParAn() + " préparation(s) par année.");
      }
      
      if (isValide) {
        long l_duree = dateFin().getTime() - dateDebut().getTime() + (12 * 60 * 60 * 1000);
        float dureeEnJours = (float) (l_duree * 1.0) / (1000 * 60 * 60 * 24);
        
        if ((float) dureeEnJours <= dureeMaxAbsenceEnJour()) {
          // laValeur = valeur();
        } else {
          isValide = false;
          setErrorMsg("La durée de cette absence ne peut-être supérieure à " + dureeMaxAbsenceEnJour() + " jours.");
        }
      }
    }

    return isValide;
	}
	
}
