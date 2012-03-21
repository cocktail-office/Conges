package fr.univlr.cri.conges.objects;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;

/**
 * Definit le motif des objets qui peuvent être sujet 
 * a venir debiter un compte CET
 * 
 * @author ctarade
 */
public interface I_DebitableSurCET {

	public final static String DATE_VALEUR_KEY 										= "dateValeur";
	public final static String DUREE_A_DEBITER_KEY						 		= "dureeADebiter";
	public final static String MINUTES_RESTANTES_A_DEBITER_KEY 		= "minutesRestantesADebiter";
	public final static String CLEAR_CACHE_KEY 										= "clearCache";

	/**
	 * La date de valeur de l'objet
	 * @return
	 */
	public abstract NSTimestamp dateValeur();	
	
	/**
	 * La duree totale a debiter sur CET
	 * @return
	 */
	public abstract Number dureeADebiter();
	
	/**
	 * Total de minutes restant a debiter pour cet objet.
	 * Chaque objet est traité lors du chargement du planning,
	 * et cette valeur est recalculée a chaque fois.
	 */
	public int minutesRestantesADebiter();
	
	public void debiter(int minutesADebiter);
	
	/**
	 * Effacer les objets en cache
	 */
	public void clearCache();
	
	public EOAffectationAnnuelle toAffectationAnnuelle();
	
	/**
	 * La liste des transactions CET succeptibles d'etre 
	 * debitees par ce debitable (date de valeur antérieure et
	 * avec des minutes restantes)
	 * @return
	 */
	public NSArray tosTransaction();
	
	/**
	 * Doit retourner une chaine qui permet d'afficher l'objet
	 * dans la liste des details de tous les debitables
	 * @return
	 */
	public String displayCet();
	
}
