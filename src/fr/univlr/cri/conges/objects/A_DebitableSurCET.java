package fr.univlr.cri.conges.objects;

import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.conges.EOCETTransaction;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRSort;

/**
 * Definit le motif des objets qui peuvent être sujet 
 * a venir debiter un compte CET
 * 
 * @author ctarade
 */
public abstract class A_DebitableSurCET
	implements I_DebitableSurCET {

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

	private Number minutesRestantesADebiter;

	/**
	 * Total de minutes restant a debiter pour cet objet.
	 * Chaque objet est traité lors du chargement du planning,
	 * et cette valeur est recalculée a chaque fois.
	 */
	public int minutesRestantesADebiter() {
		if (minutesRestantesADebiter == null) {
			// on l'initialise par defaut par la valeur du rachat
			minutesRestantesADebiter = dureeADebiter();
		}
		return minutesRestantesADebiter.intValue();
	}
	
	/**
	 * 
	 * @param minutesADebiter
	 */
	public void debiter(int minutesADebiter) {
		minutesRestantesADebiter = new Integer(minutesRestantesADebiter() - minutesADebiter);
	}
	
	
	/**
	 * Effacer les objets en cache
	 */
	public void clearCache(){
		minutesRestantesADebiter = null;
	}
	
	/**
	 * 
	 */
	public abstract EOAffectationAnnuelle toAffectationAnnuelle();
	
	/**
	 * La liste des transactions CET succeptibles d'etre 
	 * debitees par ce debitable (date de valeur antérieure et
	 * avec des minutes restantes).
	 * Classement par date de valeur.
	 * @return
	 */
	public NSArray tosTransaction() {
		NSArray transactionList = toAffectationAnnuelle().individu().toCET().cETTransactions();
		
		transactionList = EOQualifier.filteredArrayWithQualifier(
				transactionList,
				CRIDataBus.newCondition(
						EOCETTransaction.DATE_VALEUR_KEY + "<=%@ and " +
						EOCETTransaction.MINUTES_RESTANTES_KEY + ">0",
						new NSArray(dateValeur())));
		
		transactionList = LRSort.sortedArray(transactionList, EOCETTransaction.DATE_VALEUR_KEY);
		
		return transactionList;
	}

	/**
	 * Doit retourner une chaine qui permet d'afficher l'objet
	 * dans la liste des details de tous les debitables
	 * @return
	 */
	public abstract String displayCet();
	
}
