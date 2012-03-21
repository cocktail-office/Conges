// EOCETTransaction.java
// Created on Mon Oct 18 07:22:38  2004 by Apple EOModeler Version 5.2

package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.*;

import fr.univlr.cri.conges.CetEtat;
import fr.univlr.cri.conges.constantes.ConstsJour;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOMouvement;
import fr.univlr.cri.conges.objects.I_DebitableSurCET;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRSort;
import fr.univlr.cri.ycrifwk.utils.UtilDb;

public class EOCETTransaction 
	extends _EOCETTransaction {

  public EOCETTransaction() {
  	super();
  }
  
  public void validateForInsert() throws NSValidation.ValidationException {
      this.validateObjectMetier();
      
      // pas de controle particulier sur les transactions automatique
      if (!isTransactionAutomatique()) {

      	// controler que le motif, la valeur et la date de valeur sont renseignées
      	if (super.dateValeur() == null || 
      			super.valeur() == null || 
      			super.valeur().intValue() == 0 || 
      			StringCtrl.isEmpty(super.motif())) {
      		StringBuffer sb = new StringBuffer();
       		if (super.dateValeur() == null) {
      			sb.append("date de valeur");
      		}
       		if (super.valeur() == null || super.valeur().intValue() == 0) {
       			if (sb.length() > 0) {
       				sb.append(", ");
       			}
      			sb.append("valeur");
      		}
       		if (StringCtrl.isEmpty(super.motif())) {
       			if (sb.length() > 0) {
       				sb.append(", ");
       			}
      			sb.append("motif");
      		}
   				sb.insert(0, "Impossible de créer la transaction car les champs suivant sont vides ou nuls : ");

    			throw new NSValidation.ValidationException(sb.toString());
      	}
      	
      	// pas de valeur négative
      	if (super.valeur().intValue() < 0) {
      		throw new NSValidation.ValidationException("La valeur ne peut pas être négative");
      	}
      	
      	// tests spécifiques à l'ancien régime
      	if (!isTransactionRegimePerenne()) {

        	// liste des plannings
      		NSArray affAnnList = EOAffectationAnnuelle.findAffectationsAnnuelleInContext(
      				editingContext(),	cet().individu().oid(), null, null); 
      		
          // pas de saisie sur l'ancien régime alors qu'il y a eu des mouvements
      		// sur l'ancien régime
      		if (!isTransactionRegimePerenne() &&
      				hasMouvementAncienRegimeInAffAnnList(affAnnList)) {
      			throw new NSValidation.ValidationException("Vous ne pouvez pas saisir de transaction sur l'ancien régime " +
      					"s'il existe déjà des demandes ou des décisions faites sur le CET ancien régime");
      		}
      		
          // pas de saisie sur l'ancien régime alors qu'il y a eu un mouvement
      		// de transfert de l'ancien régime vers le régime pérenne
      		if (hasMouvementTransfertRegimePerenneInAffAnnList(affAnnList)) {
      			throw new NSValidation.ValidationException("Vous ne pouvez pas saisir de transaction sur l'ancien régime " +
      					"s'il y a eu un transfert des congés de l'ancien régime vers le régime pérenne");
      		}

      	}
    		
      }
      

      validateBeforeTransactionSave();
      super.validateForInsert();
  }

  public void validateForUpdate() throws NSValidation.ValidationException {
    this.validateObjectMetier();
  	
  	// ne pas autoriser le changement de valeur ou de date de valeur
  	// s'il y a eu décision sur l'ancien régime et que les modifications
  	// portent sur une transaction de l'ancien régime
  	if (!isTransactionRegimePerenne() && (
  			isValeurChanged || isDateValeurChanged)) {
			
  		// liste des plannings
  		NSArray affAnnList = EOAffectationAnnuelle.findAffectationsAnnuelleInContext(
  				editingContext(),	cet().individu().oid(), null, null); 
  		
    	// on autorise pas les modifs sur des rachats de CET 2008-1136
  		if (hasIndemnisationDecret20081136OnAffAnnList(affAnnList)) {
  			throw new NSValidation.ValidationException("Vous ne pouvez pas modifier la valeur ou la date de valeur " +
  					"pour une transaction de l'ancien régime surlequel une indemnisation sur décret 2008-1136 a été faite");
  		}
  		
  		// recherche de mouvements sur l'ancien régime
  		if (hasMouvementAncienRegimeInAffAnnList(affAnnList)) {
  			throw new NSValidation.ValidationException("Vous ne pouvez pas modifier la valeur ou la date de valeur " +
  					"pour une transaction de l'ancien régime surlequel des demandes ont été faites ou des decisions prises");
  		}

  	}
  	
  	validateBeforeTransactionSave();
    super.validateForUpdate();
  }

  public void validateForDelete() throws NSValidation.ValidationException {
    super.validateForDelete();
  }

  /**
   * Apparemment cette methode n'est pas appelee.
   * @see com.webobjects.eocontrol.EOValidation#validateForUpdate()
   */    
  public void validateForSave() throws NSValidation.ValidationException {
      validateObjectMetier();
      validateBeforeTransactionSave();
      super.validateForSave();
  }

  /**
   * Peut etre appele a partir des factories.
   * @throws NSValidation.ValidationException
   */
  public void validateObjectMetier() throws NSValidation.ValidationException {
  	
  	// controler que les transactions automatiques n'ont pas de valeur, date et motif renseigné
  	if (isTransactionAutomatique()) {
  		if (super.valeur() != null) {
  			throw new NSValidation.ValidationException("La valeur n'est pas modifiable pour une transaction automatique");
  		}
  		if (super.dateValeur() != null) {
  			throw new NSValidation.ValidationException("La date de valeur n'est pas modifiable pour une transaction automatique");
  		}
  		if (super.motif() != null) {
  			throw new NSValidation.ValidationException("La motif n'est pas modifiable pour une transaction automatique");
  		}
  	}
  
  }
  
  /**
   * A appeler par les validateforsave, forinsert, forupdate.
   *
   */
  private final void validateBeforeTransactionSave() throws NSValidation.ValidationException {
         
  }
  
  
  // ajouts
  
  
	public final static String VALEUR_KEY 												= "valeur";
	public final static String MINUTES_DEBITEES_KEY 							= "minutesDebitees";
  public final static String DATE_VALEUR_KEY 										= "dateValeur";
  public final static String CET_KEY 														= "cet";
  public final static String MINUTES_RESTANTES_KEY							= "minutesRestantes";
  public final static String CLEAR_CACHE_KEY										=	"clearCache";
  public final static String IS_TRANSACTION_REGIME_PERENNE_KEY 	= "isTransactionRegimePerenne";

  /**
   * Constructeur simplifié
   * @param editingContext
   * @param cet
   * @return
   */
  public static EOCETTransaction createCETTransaction(
  		EOEditingContext editingContext, EOCET cet) {
  	NSTimestamp now = DateCtrlConges.now();
    return createCETTransaction(editingContext, now, now, new Integer(0), cet);
  }

 /**
  * 
  * @return
  */
  public String creditEnHeures() {
    String creditEnHeures = "00:00";

    if (valeur().intValue() > 0) {
      creditEnHeures = TimeCtrl.stringForMinutes(valeur().intValue());
    }

    return creditEnHeures;
  }

  /**
   * 
   * @return
   */
  public float creditEnJour7h00() {
  	float creditEnJour7h00 = (float) 0;
  	
    if (valeur().intValue() > 0) {
    	creditEnJour7h00 = (float) valeur().floatValue() / (float) ConstsJour.DUREE_JOUR_7H00;
    }
  	
  	return creditEnJour7h00;
  }
  
  /**
   * 
   * @param value
   */
  public void setCreditEnHeures(String value) {
  	if (!StringCtrl.isEmpty(value)) {
  		int minutes = TimeCtrl.getMinutes(value);
  		setValeur(new Integer(minutes));
  	}
  }
  
  /**
   * 
   * @return
   */
  public String debitEnHeures() {
    String debitEnHeures = "00:00";

    if (minutesDebitees().intValue() > 0) {
    	debitEnHeures = TimeCtrl.stringForMinutes(minutesDebitees().intValue());
    }

    return debitEnHeures;
  }
  
  /**
   * 
   * @return
   */
  public float debitEnJour7h00() {
  	float debitEnJour7h00 = (float) 0;
  	
    if (valeur().intValue() > 0) {
    	debitEnJour7h00 = (float) minutesDebitees().floatValue() / (float) ConstsJour.DUREE_JOUR_7H00;
    }
  	
  	return debitEnJour7h00;
  }
    
  /**
   * 
   * @return
   */
  public String soldeEnHeures() {
    String soldeEnHeures = "";

    soldeEnHeures = TimeCtrl.stringForMinutes(valeur().intValue()-minutesDebitees().intValue());

    return soldeEnHeures;
  }

  
  /**
   * Minutes restantes sur la transaction
   */
  public Number minutesRestantes() {
  	return new Integer(valeur().intValue()-minutesDebitees().intValue());
  }
  
  /**
   * Effectuer un debit
   * @param minutesADebiter
   */
  public void debiter(int minutesADebiter) {
  	setMinutesDebitees(new Integer(minutesDebitees().intValue() + minutesADebiter));
  }
  
  // la liste des debits sur cette transaction
  
  private NSArray debitables;
  private NSMutableArray debits;
  
  public void addDebitable(I_DebitableSurCET debitable, int aDebiter) {
  	if (debitables == null) {
  		debitables = new NSArray();
  		debits = new NSMutableArray();
  	}
  	if (!debitables.containsObject(debitable)) {
    	debitables = debitables.arrayByAddingObject(debitable);
    	debits.addObject(new Integer(aDebiter));
  	} else {
  		Integer prevADebiter = (Integer) debits.objectAtIndex(debitables.indexOfIdenticalObject(debitable));
  		debits.replaceObjectAtIndex(
  				new Integer(prevADebiter.intValue() + aDebiter),
  				debitables.indexOfIdenticalObject(debitable));
  	}
  }

  public NSArray getDebitables() {
  	return debitables;
  }

  public NSArray getDebits() {
  	return debits;
  }
  
  public void clearCache() {
  	debitables = debits = null;
  }
  
  
  // lien avec les mouvements
 
  /**
   * La valeur de crédit de la transaction.
   * Pour la nouvelle gestion du CET, alors c'est la somme de l'épargne et de l'éventuelle transfert
   * du régime ancien vers le régime pérenne.
   * Pour l'ancienne gestion du CET, c'est celle saisie dans la variable {@link #valeur()}
   */
  public Integer valeur() {
  	int valeurMinutes = 0;
  	
  	// ancienne gestion du CET
  	if (!isTransactionAutomatique()) {
  		if (super.valeur() != null) {
    		valeurMinutes = super.valeur().intValue();
  		}
  	} else {
  		
  		// nouvelle gestion du CET	
  		// la valeur de l'épargne s'il y a
  		if (toAffectationAnnuelle().toMouvementCetDecisionEpargne() != null) {
    		valeurMinutes += toAffectationAnnuelle().toMouvementCetDecisionEpargne().mouvementMinutes();
  		}
  		
  		// la valeur du transfert de l'ancien régime vers le régume pérenne s'il y a
  		if (toAffectationAnnuelle().toMouvementCetDecisionTransfertRegimePerenne() != null) {
  			valeurMinutes += toAffectationAnnuelle().toMouvementCetDecisionTransfertRegimePerenne().mouvementMinutes();
  		}
  		
  	}

  	return new Integer(valeurMinutes);
  }  
  
  /**
   * La date de valeur de la transaction.
   * Pour la nouvelle gestion du CET, c'est le 1er janvier du planning.
   * Pour l'ancienne gestion du CET, c'est celle saisie dans la variable {@link #dateValeur()}
   */
  public NSTimestamp dateValeur() {
  	NSTimestamp dateValeur = null;
  	
  	// ancienne gestion du CET
  	if (!isTransactionAutomatique()) {
  		dateValeur = super.dateValeur();
  	} else {
  		// nouvelle gestion du CET : 01/01 
  		dateValeur = DateCtrlConges.date1erJanAnneeUniv(toAffectationAnnuelle().dateDebutAnnee());
  	}
  	
		return dateValeur;
  }
  
  /**
   * Le motif de la transaction. Si un mouvement type blocage
   * de reliquat est lié, alors c'est un libellé générique du planning, sinon c'est celle
   * saisie dans la variable {@link #motif()}
   */
  public String motif() {
		return motif(true);
  }
  
  public String motif(boolean isHtml) {
  	String motif = StringCtrl.emptyString();
  	// ancienne gestion du CET
  	if (!isTransactionAutomatique()) {
  		motif = super.motif();
  	} else {
  		// nouvelle gestion du CET	

  		// crédit issus d'un transfert ancien régime vers régime pérenne
  		if (isEpargneTransfertDepuisAncienRegime()) {
  			motif += "Transfert de l'ancien r&eacute;gime vers le r&eacute;gime p&eacute;renne (";
				NSNumberFormatter nf = new NSNumberFormatter("0.00");
				motif += nf.format(toAffectationAnnuelle().toMouvementCetDecisionTransfertRegimePerenne().mouvementJoursA7h00());
				motif += "j. à 7h00)";
  		}
  		
  		// crédit issus d'une épargne de reliquats de l'année N-1
  		if (toAffectationAnnuelle().toMouvementCetDecisionEpargne() != null &&
  				toAffectationAnnuelle().toMouvementCetDecisionEpargne().mouvementMinutes().intValue() > 0) {
  			if (!StringCtrl.isEmpty(motif)) {
  				motif += "<br/>+ ";
  			}
  			NSTimestamp tsAnneeNm1 = toAffectationAnnuelle().dateDebutAnnee().timestampByAddingGregorianUnits(-1,0,0,0,0,0);
  			
  			motif += "&Eacute;pargne de reliquats de cong&eacute;s " + DateCtrlConges.anneeUnivForDate(tsAnneeNm1) + " (";
  			motif += toAffectationAnnuelle().toMouvementCetDecisionEpargne().mouvementJoursA7h00Arrondi();
				motif += "j. à 7h00)";
  		}
  		
  	}
  	
  	if (!isHtml) {
  		motif = StringCtrl.replace(motif, "<br/>", "\n");
  		motif = StringCtrl.replace(motif, "&eacute;", "é");
  		motif = StringCtrl.replace(motif, "&Eacute;", "E");
  	}
  	
		return motif;
  }
  
  /**
   * Indique si la transaction est automatiquement gérée
   * par l'application (via une épargne ou un transfert)
   */
  public boolean isTransactionAutomatique() {
  	return toAffectationAnnuelle() != null;
  }

  /**
   * Indique si la transaction concerne une opération sur le régime 
   * pérenne ou non.
   * On se base sur la date de valeur de la transaction, le régime
   * pérenne s'applique après le 31/12/2009
   * @return
   */
  public boolean isTransactionRegimePerenne() {
  	return DateCtrlConges.isAfterEq(dateValeur(), EOCET.DATE_CET_DEBUT_REGIME_PERENNE);
  }
  
  /**
   * Indique si cette transaction correspond a un refus de bascule CET
   * (valable uniquement dans le nouveau systeme)
   * @return
   */
  public boolean isEpargneReliquatRefusee() {
  	boolean isEpargneReliquatRefusee = false;
  	
  	if (isTransactionAutomatique() && 
  			toAffectationAnnuelle().toMouvementCetDecisionEpargne() != null &&
  			toAffectationAnnuelle().toMouvementCetDecisionEpargne().mouvementMinutes().intValue() == 0) {
  		isEpargneReliquatRefusee = true;
  	}
  	
  	return isEpargneReliquatRefusee;
  }
  
  /**
   * Indique que la transaction existe du fait qu'il y ai un
   * transfert de l'ancien régime vers le régime pérenne
   */
  public boolean isEpargneTransfertDepuisAncienRegime() {
  	boolean isEpargneTransfertDepuisAncienRegime = false;
  	
  	if (isTransactionAutomatique() && 
  			toAffectationAnnuelle().toMouvementCetDecisionTransfertRegimePerenne() != null &&
  			toAffectationAnnuelle().toMouvementCetDecisionTransfertRegimePerenne().mouvementMinutes().intValue() > 0) {
  		isEpargneTransfertDepuisAncienRegime = true;
  	}
  	
  	return isEpargneTransfertDepuisAncienRegime;
  }

	/**
	 * retour la liste des CET pour une periode donnee
	 * @param edc
	 * @param individu
	 * @param dateAnneeUniv
	 * @return
	 */
	public static NSArray findTransactionsForPeriodeInContext(
			EOEditingContext ec, NSTimestamp dateDebut, NSTimestamp dateFin) {
		String strQual = (dateFin != null ? 
				DATE_VALEUR_KEY + ">=%@ AND "+DATE_VALEUR_KEY+"<= %@" : DATE_VALEUR_KEY+" < %@");
		NSArray args = (dateFin != null ? 
				new NSArray( new NSTimestamp[] {dateDebut, dateFin}) : new NSArray(dateDebut));
		EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(strQual, args);
		return UtilDb.fetchArray(ec, EOCETTransaction.ENTITY_NAME, qual, null);
	}


	/**
	 * toutes les transaction pour le CET d'individu
	 * @param ec
	 * @param individu
	 * @param showTransactionsRefusees TODO
	 * @return
	 */
	public static NSArray findAllTransactionForIndividuInContext(
			EOEditingContext ec, EOIndividu individu, boolean showTransactionsRefusees) {
		EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(
				EOCETTransaction.CET_KEY + "." + EOCET.INDIVIDU_KEY + "=%@", new NSArray(individu));
		NSArray transactions = fetchCETTransactions(
				ec, qual, LRSort.newSort(EOCETTransaction.DATE_VALEUR_KEY));	
		if (!showTransactionsRefusees) {
			// masquer les transactions refusees
			NSMutableArray transactionsNonRefusees = new NSMutableArray();
			for (int i=0; i<transactions.count(); i++) {
				EOCETTransaction transaction = (EOCETTransaction) transactions.objectAtIndex(i);
				if (!transaction.isEpargneReliquatRefusee()) {
					transactionsNonRefusees.addObject(transaction);
				} else {
					// cas particulier : les épargnes avec une décision à 0 en épargne
					// mais avec un transfert ne sont pas à masquer !
					if (transaction.isEpargneTransfertDepuisAncienRegime()) {
						transactionsNonRefusees.addObject(transaction);
					}
				}
			}
			transactions = transactionsNonRefusees.immutableClone();
		}
		// tempo
		transactions = LRSort.sortedArray(transactions, EOCETTransaction.DATE_VALEUR_KEY);
		return transactions;
	}
	
	
	/**
	 * Efface une transaction. Si c'est la dernière, alors
	 * on efface aussi l'enregistrement {@link EOCET} associé
	 * @param transaction
	 */
	public static void delete(EOCETTransaction transaction) {
		EOEditingContext ec = transaction.editingContext();
		NSArray transactions = fetchCETTransactions(
				ec, CRIDataBus.newCondition(CET_KEY + "=%@", new NSArray(transaction.cet())), null);
		boolean shouldDeleteCet = false;
		if (transactions.count() == 1) {
			shouldDeleteCet = true;
		}	
		EOCET cet = transaction.cet();
		cet.removeFromCETTransactionsRelationship(transaction);
		ec.deleteObject(transaction);
		if (shouldDeleteCet) {
			ec.deleteObject(cet);
		}
	}

	
	private Number oid;
	
	/**
	 * La clé primaire, pour faire des ancres HTML par exemple
	 * @return
	 */
	public Number oid() {
		if (oid == null) {
			oid = (Number) EOUtilities.primaryKeyForObject(editingContext(), this).valueForKey("oid");
		}
		return oid;
	}
	
	
	// gestion des modification des valeurs et dates des transactions
	
	
	private boolean isValeurChanged;
	private boolean isDateValeurChanged;
	
	public final boolean isValeurChanged() {
		return isValeurChanged;
	}

	public final void setValeurChanged(boolean isValeurChanged) {
		this.isValeurChanged = isValeurChanged;
	}

	public final boolean isDateValeurChanged() {
		return isDateValeurChanged;
	}

	public final void setDateValeurChanged(boolean isDateValeurChanged) {
		this.isDateValeurChanged = isDateValeurChanged;
	}
	
	/**
	 * Un changement de la date de valeur entraine le recalcul des plannings de l'agent
	 */
	public void setDateValeur(NSTimestamp value) {
		setDateValeurChanged(false);
		if (value != dateValeur()) {
			setDateValeurChanged(true);
		}
		super.setDateValeur(value);
	}
	
	/**
	 * Un changement de valeur entraine le recalcul des plannings de l'agent
	 */
	public void setValeur(Integer value) {
		setValeurChanged(false);
		if (value != valeur()) {
			setValeurChanged(true);
		}
		super.setValeur(value);
	}
	
	
	// suppression
	
	/**
	 * Indique s'il est possible d'effacer cette transaction
	 * @see CetEtat
	 */
	public boolean isSuppressionAutorisee() {
		boolean isSuppressionAutorisee = true;
		
		// pas de suppression des transactions automatique
		if (isTransactionAutomatique()) {
			isSuppressionAutorisee = false;
		}	
		
		NSArray affAnnList = new NSArray();
		
		// ne pas autoriser la suppression pour des transactions 
		// de l'ancien régime ayant des rachats de CET 2008-1136
		if (isSuppressionAutorisee &&
				!isTransactionRegimePerenne()) {
			
  		// liste des plannings
  		affAnnList = EOAffectationAnnuelle.findAffectationsAnnuelleInContext(
  				editingContext(),	cet().individu().oid(), null, null); 
  		
    	// on autorise pas les modifs sur des rachats de CET 2008-1136
  		if (hasIndemnisationDecret20081136OnAffAnnList(affAnnList)) {
  			isSuppressionAutorisee = false;
  		}
		}
		
		// ne pas autoriser les suppression pour des transactions ayant des 
		// demandes ou décisions sur l'ancien régime
		if (isSuppressionAutorisee &&
				!isTransactionRegimePerenne()) {
  	
			// recherche de mouvements sur l'ancien régime
  		if (hasMouvementAncienRegimeInAffAnnList(affAnnList)) {
  			isSuppressionAutorisee = false;
  		}
			
		}
		
		return isSuppressionAutorisee;
	}
	
	
	// creation
	
	/**
	 * Création d'une transaction manuelle
	 */
	public static EOCETTransaction createTransaction(
			EOIndividu individu, String motif, NSTimestamp dateValeur, String valeurHeures) {
		EOCETTransaction transaction = null;
		
		// création de l'enregistrement CET si l'individu n'en a pas
		EOCET cet = EOCET.doCreationCetSiInexistant(individu);
		
		//
		transaction = createCETTransaction(individu.editingContext(), cet);
		
		transaction.setMotif(motif);
		transaction.setDateValeur(dateValeur);
		transaction.setValeur(new Integer(TimeCtrl.getMinutes(valeurHeures)));
		
		// positionner explicitement la transaction en manuelle
		transaction.setToAffectationAnnuelleRelationship(null);
		
		return transaction;
	}
	
	
	
	
	// methodes internes
	
	
	/**
	 * Indique si parmi une liste d'affectation annuelles, il est trouvé
	 * un {@link EOMouvement} de type {@link EOMouvement#TYPE_CET_INDEMNISATION_DECRET_2008_1136}
	 * @param affAnnList
	 * @return
	 */
	private boolean hasIndemnisationDecret20081136OnAffAnnList(NSArray affAnnList) {
		boolean hasIndemnisationDecret20081136 = false;

		int i = 0;
		while (!hasIndemnisationDecret20081136 && i<affAnnList.count()) {
			EOAffectationAnnuelle affAnn = (EOAffectationAnnuelle) affAnnList.objectAtIndex(i);
			hasIndemnisationDecret20081136 = (affAnn.toMouvementCetIndemnisationDecret20081136() != null);
			i++;
		}

		return hasIndemnisationDecret20081136;
	}
	
	/**
	 * Indique si parmi une liste d'affectation annuelles, il est trouvé au 
	 * moins un {@link EOMouvement} de type décision ou demande sur l'ancien
	 * régime CET
	 * @param affAnnList
	 * @return
	 */
	private boolean hasMouvementAncienRegimeInAffAnnList(NSArray affAnnList) {
		boolean hasOperationAncienRegime = false;
		
		// recherche de mouvements sur l'ancien régime
		int i = 0;
		while (!hasOperationAncienRegime && i<affAnnList.count()) {
			EOAffectationAnnuelle affAnn = (EOAffectationAnnuelle) affAnnList.objectAtIndex(i);
			hasOperationAncienRegime = affAnn.hasMouvementCetAncienRegime();
			i++;
		}
		
		return hasOperationAncienRegime;
	}	
	
	/**
	 * Indique si parmi une liste d'affectation annuelles, il est trouvé
	 * un {@link EOMouvement} de type {@link EOMouvement#TYPE_CET_DECISION_TRANSFERT_REGIME_PERENNE}
	 * ou de type {@link EOMouvement#TYPE_CET_DEMANDE_TRANSFERT_REGIME_PERENNE}
	 * @param affAnnList
	 * @return
	 */
	private boolean hasMouvementTransfertRegimePerenneInAffAnnList(NSArray affAnnList) {
		boolean hasTransfertRegimePerenne = false;
		
		// recherche de mouvements
		int i = 0;
		while (!hasTransfertRegimePerenne && i<affAnnList.count()) {
			EOAffectationAnnuelle affAnn = (EOAffectationAnnuelle) affAnnList.objectAtIndex(i);
			if (affAnn.toMouvementCetDecisionTransfertRegimePerenne() != null ||
					affAnn.toMouvementCetDemandeTransfertRegimePerenne() != null) {
				hasTransfertRegimePerenne = true;
			}
			i++;
		}
		
		return hasTransfertRegimePerenne;
	}
}


