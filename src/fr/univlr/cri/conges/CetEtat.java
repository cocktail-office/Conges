package fr.univlr.cri.conges;


import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;
import com.webobjects.foundation.NSValidation;

import fr.univlr.cri.conges.constantes.ConstsJour;
import fr.univlr.cri.conges.eos.modele.conges.EOCETTransaction;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOMouvement;
import fr.univlr.cri.conges.objects.I_DebitableSurCET;
import fr.univlr.cri.conges.objects.Planning;
import fr.univlr.cri.conges.print.PrintSituationCET;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webext.CRIAlertPage;
import fr.univlr.cri.webext.CRIAlertResponder;
import fr.univlr.cri.ycrifwk.utils.UtilDb;

/**
 * Détail du CET d'un agent
 * 
 * @author ctarade
 */
public class CetEtat 
	extends YCRIWebPage {

	// bindings
	public EOIndividu individu;
	public boolean isModificationAutorisee = false;
	public NSTimestamp dateDebutAnnee;
  /**
   * Indiquer explicitement au composant de se recharger
   * @see AdminCETRegularisation
   */
  public boolean shouldReloadForced = false;

  // liste des transactions
	private NSArray transactionList;
  public EOCETTransaction transactionItem;
  public EOCETTransaction transactionSelected;
  
  // liste des debits CET par transaction
  public I_DebitableSurCET debitable;
  public int debitableIndex;

  // faut-il recharger les plannings de l'agent 
  // pour avoir les bons calculs et debits
  private boolean shouldReload = false;
  
  // date a imprimer dans l'edition de la situation
  public NSTimestamp dateArret = DateCtrlConges.now();
  
	public CetEtat(WOContext context) {
		super(context);
	}

	public void appendToResponse(WOResponse arg0, WOContext arg1) {
		if (shouldReload || shouldReloadForced) {
			transactionList = null;
  		NSArray affAnnList = EOAffectationAnnuelle.findAffectationsAnnuelleInContext(
  				edc, individu.oid(), null, dateDebutAnnee);
  		// on ne prend que le premier planning ... ce sera suffisant 
  		// lors du passage en mode planning unique
  		if (affAnnList.count() > 0) {
  			EOAffectationAnnuelle affAnn = (EOAffectationAnnuelle) affAnnList.objectAtIndex(0);
  			Planning p = Planning.newPlanning(affAnn, cngUserInfo(), dateDebutAnnee);
  			p.setType("R");
  		}
			shouldReload = false;
			shouldReloadForced = false;
		}
		super.appendToResponse(arg0, arg1);
	}
	
  /**
   * La liste des transactions
   * @return
   */
  public NSArray lesTransactions() {
  	if (transactionList == null) {
  		transactionList =  EOCETTransaction.findAllTransactionForIndividuInContext(edc, individu, false);
  	}
    return transactionList;
  }
  
  public String totalDebits() {
    int total = 0;

    if (lesTransactions() != null && lesTransactions().count() > 0) {
      total = ((Number) lesTransactions().valueForKeyPath(
      		"@sum." + EOCETTransaction.MINUTES_DEBITEES_KEY)).intValue();
    }

    return TimeCtrl.stringForMinutes(total);
  }

  public String totalCredits() {
    int total = 0;

    if (lesTransactions() != null && lesTransactions().count() > 0) {
      total = ((Number) lesTransactions().valueForKeyPath(
      		"@sum." + EOCETTransaction.VALEUR_KEY)).intValue();
    }

    return TimeCtrl.stringForMinutes(total);
  }

  /**
   * Total des transactions en heures
   * @return
   */
  public String totalTransactionsEnHeures() {
    int total = 0;

    if (lesTransactions() != null && lesTransactions().count() > 0) {
    	int sommeValeurs = ((Number) lesTransactions().valueForKeyPath("@sum." + EOCETTransaction.VALEUR_KEY)).intValue();
    	int sommeDebits = ((Number) lesTransactions().valueForKeyPath("@sum." + EOCETTransaction.MINUTES_DEBITEES_KEY)).intValue();
    	total = sommeValeurs - sommeDebits;
    }

    return TimeCtrl.stringForMinutes(total);
  }

  /**
   * Total des transactions en jours à 7h00
   * @return
   */
  public float totalTransactionsEnJours7h00() {
    float total = 0;

    if (lesTransactions() != null && lesTransactions().count() > 0) {
    	int sommeValeurs = ((Number) lesTransactions().valueForKeyPath("@sum." + EOCETTransaction.VALEUR_KEY)).intValue();
    	int sommeDebits = ((Number) lesTransactions().valueForKeyPath("@sum." + EOCETTransaction.MINUTES_DEBITEES_KEY)).intValue();
    	total = ((float) (sommeValeurs - sommeDebits))/(float)ConstsJour.DUREE_JOUR_7H00;
    }

    return total;
  }

  //
  
  /**
   * 
   */
  public String getDebitForDebitable() {
  	return TimeCtrl.stringForMinutes(
  			((Integer)transactionItem.getDebits().objectAtIndex(debitableIndex)).intValue());
  }

  /**
   * 
   * @return
   */
  public boolean getIsVidageIndemnisation() {
  	return individu.toCET().tosMouvementVidageIndemnisation().count() > 0;
  }
  
  /**
   * 
   * @return
   */
  public boolean getIsVidageTransfertRafp() {
  	return individu.toCET().tosMouvementVidageTransfertRafp().count() > 0;
  }
  
  /**
   * 
   * @return
   */
  public boolean getIsVidageIndemnisationAncienRegime() {
  	return individu.toCET().tosMouvementVidageIndemnisationAncienRegime().count() > 0;
  }
  
  /**
   * 
   * @return
   */
  public boolean getIsVidageTransfertRafpAncienRegime() {
  	return individu.toCET().tosMouvementVidageTransfertRafpAncienRegime().count() > 0;
  }
  
  // setters
  
  /**
   * On force le recalcul dernier planning et ses transactions
   * Le recalcul des transactions est nécéssaire pour remplir
   * la liste des debitables
   */
  public void setIndividu(EOIndividu value) {
  	EOIndividu prevIndividu = individu;
  	individu = value;
  	if (individu != prevIndividu) {
  		transactionList = null;
  		transactionSelected = null;
  		shouldReload = true;
  	}
  }
  
  /**
   * 
   * @param isVidageIndemnisation
   */
  public void setIsVidageIndemnisation(boolean isVidageIndemnisation) {
  	try {
    	if (isVidageIndemnisation) {
    		individu.toCET().vider(EOMouvement.TYPE_CET_VIDAGE_INDEMNISATION);
    	} else {
    		individu.toCET().supprimerMouvementVidage(EOMouvement.TYPE_CET_VIDAGE_INDEMNISATION);
    	}
    	UtilDb.save(edc, true);
  		shouldReload = true;
  	} catch (NSValidation.ValidationException e) {
  		setErrorMessage(e.getMessage());
		} catch (Throwable e) {
			e.printStackTrace();
		}
  }
  
  /**
   * 
   * @param isVidageTransfertRafp
   */
  public void setIsVidageTransfertRafp(boolean isVidageTransfertRafp) {
  	try {
  		if (isVidageTransfertRafp) {
  			individu.toCET().vider(EOMouvement.TYPE_CET_VIDAGE_TRANSFERT_RAFP);
  		} else {
  			individu.toCET().supprimerMouvementVidage(EOMouvement.TYPE_CET_VIDAGE_TRANSFERT_RAFP);
  		}
  		UtilDb.save(edc, true);
  		shouldReload = true;
  	} catch (NSValidation.ValidationException e) {
  		setErrorMessage(e.getMessage());
		} catch (Throwable e) {
			e.printStackTrace();
		}
  }
  
  /**
   * 
   * @param isVidageIndemnisationAncienRegime
   */
  public void setIsVidageIndemnisationAncienRegime(boolean isVidageIndemnisationAncienRegime) {
  	try {
  		if (isVidageIndemnisationAncienRegime) {
  			individu.toCET().vider(EOMouvement.TYPE_CET_VIDAGE_INDEMNISATION_ANCIEN_REGIME);
  		} else {
  			individu.toCET().supprimerMouvementVidage(EOMouvement.TYPE_CET_VIDAGE_INDEMNISATION_ANCIEN_REGIME);
  		}
  		UtilDb.save(edc, true);
  		shouldReload = true;
  	} catch (NSValidation.ValidationException e) {
  		setErrorMessage(e.getMessage());
		} catch (Throwable e) {
			e.printStackTrace();
		}
  }
  
  /**
   * 
   * @param isVidageTransfertRafpAncienRegime
   */
  public void setIsVidageTransfertRafpAncienRegime(boolean isVidageTransfertRafpAncienRegime) {
  	try {
  		if (isVidageTransfertRafpAncienRegime) {
  			individu.toCET().vider(EOMouvement.TYPE_CET_VIDAGE_TRANSFERT_RAFP_ANCIEN_REGIME);
  		} else {
  			individu.toCET().supprimerMouvementVidage(EOMouvement.TYPE_CET_VIDAGE_TRANSFERT_RAFP_ANCIEN_REGIME);
  		}
  		UtilDb.save(edc, true);
  		shouldReload = true;
  	} catch (NSValidation.ValidationException e) {
  		setErrorMessage(e.getMessage());
		} catch (Throwable e) {
			e.printStackTrace();
		}
  }
  
  
  // boolean interface

  /**
   * Acces au bouton de modification
   */
  public boolean isShowBtnToModificationTransactionItem() {
  	boolean show = true;
  	
  	// s'assurer que le composant autorise la modification
  	if (!isModificationAutorisee) {
  		show = false;
  	}
  	
  	// il ne faut pas déjà etre en train de modifier quelque chose
  	if (show &&
  			transactionSelected != null) {
  		show = false;
  	}

  	// on ne modifie que les lignes non automatiques
  	if (show &&
  			transactionItem.isTransactionAutomatique()) {
  		show = false;
  	}
  	
  	return show;
  }
  
  /**
   * Acces au bouton d'enregistrement
   */
  public boolean isShowBtnDoModificationTransactionItem() {
  	boolean show = true;
  	
  	// s'assurer que le composant autorise la modification
  	if (!isModificationAutorisee) {
  		show = false;
  	}
  	
  	// elle doit être la ligne en cours de modification
  	if (show &&
  			!isTheTransactionSelected()) {
  		show = false;
  	}
  	
  	return show;
  }
  
  /**
   * Acces au bouton de suppression
   */
  public boolean isShowBtnDoSuppressionTransactionItem() {
  	boolean show = true;
  	
  	// s'assurer que le composant autorise la modification
  	if (!isModificationAutorisee) {
  		show = false;
  	}
  	
  	// rien ne doit être en modification
  	if (show &&
  			transactionSelected != null) {
  		show = false;
  	}
  	
  	// la suppression doit être possible
  	if (show &&
  			!transactionItem.isSuppressionAutorisee()) {
  		show = false;
  	}
  	
  	return show;
  }
 
  /**
   * Disponibilité des zones de texte uniquement pour 
   * la ligne selectionée
   * @return
   */
  public boolean isTheTransactionSelected() {
  	boolean show = true;
  	
  	if (transactionItem != transactionSelected) {
  		show = false;
  	}
  	
  	return show;
  }
  
  /**
   * On ne peut vider l'ancien régime en RAFP si le CET
   * n'a été vidé d'aucune autre façon
   * @return
   */
  public boolean isDisabledChkIsVidageIndemnisationAncienRegime() {
  	return getIsVidageTransfertRafpAncienRegime() ||
  		getIsVidageIndemnisation() ||
  		getIsVidageTransfertRafp();
  }
  
  /**
   * On ne peut vider l'ancien régime en indemnisation si le CET
   * n'a été vidé d'aucune autre façon
   * @return
   */
  public boolean isDisabledChkIsVidageTransfertRafpAncienRegime() {
  	return getIsVidageIndemnisationAncienRegime() ||
  		getIsVidageIndemnisation() ||
  		getIsVidageTransfertRafp();
  }
  
  
  // navigation
  
  /**
   * Passer en mode modification de la transaction
   * {@link #transactionItem}
   */
  public WOComponent toModificationTransactionItem() {
  	transactionSelected = transactionItem;
  	
  	return null;
  }
  
  /**
   * Annuler la saisie en cours
   */
  public WOComponent toAnnulerModificationTransactionSelected() {
  	transactionSelected = null;
  	
  	return null;
  }
  
  
  // action
  
   
  //TODO RAZ des changed des transaction
  
  /**
   * Enregister et appliquer les modifications de valeur
   * la transaction {@link #transactionSelected}
   * @return
   * @throws Throwable
   */
  public WOComponent doModificationTransactionSelected() throws Throwable {
  	
  	boolean isSaveOk = false;
  	
  	try {
  		isSaveOk = UtilDb.save(edc, false);

  		// verification s'il y a eu un changement qui implique un recalcul des plannings
    	if (transactionSelected.isDateValeurChanged() || 
    			transactionSelected.isValeurChanged()) {
    		// on positionne la variable CngUserInfo nécéssaire au recalcul des plannings
    		individu.toCET().setCngUserInfo(cngUserInfo());
    		// recalcul nécéssaire
    		individu.toCET().doRecalculerPlannings();
    		//
				shouldReload = true;
    	}

  	} catch (Exception e) {
  	
  		setErrorMessage(e.getMessage());
  
  	} finally {

  		// remise à zéro des témoins de changement
  		transactionSelected.setDateValeurChanged(false);
  		transactionSelected.setValeurChanged(false);

  	}
  	
		// raz de la selection si pas d'erreur
  	if (isSaveOk) {
  		transactionSelected = null;
  	}
  		
 		return null;
  }
  
	/**
	 * La classe interne
	 */	 
	public class SupprimerResponder implements CRIAlertResponder {
		
		private WOComponent parentComponent;
		private EOCETTransaction transactionToDelete;
		
		public SupprimerResponder(
				WOComponent aParentComponent, EOCETTransaction aTransactionToDelete) {
			parentComponent = aParentComponent;
			transactionToDelete = aTransactionToDelete;
		}
		
		public WOComponent respondToButton(int buttonNo) {
			switch (buttonNo) {
				case 1: 
					try {
						EOCETTransaction.delete(transactionToDelete);
						shouldReload = true;
					} catch (Throwable e) {
						e.printStackTrace();
					}
					return parentComponent;
				case 2:  
					return parentComponent;
				default: return neFaitRien();
			}
		}
	} 
  
	/**
	 * Suppression de la transaction
	 * @return
	 */
  public WOComponent doSuppressionTransactionItem() {
  	// page de confirmation
  	SupprimerResponder responder = new SupprimerResponder(
  			this.parent().parent().parent(), transactionItem);
  	return CRIAlertPage.newAlertPageWithResponder(this, "Suppression de demande<br>",
  			"<center>Confirmation de l'op&eacute;ration:<br><br>"+
  			"Etes vous sur de vouloir supprimer la transaction CET de '"+ transactionItem.cet().individu().nomComplet()+"'<br>" +
  			"- Date de valeur :" + DateCtrlConges.dateToString(transactionItem.dateValeur())	+	"<br>" +
  			"- Valeur : " + transactionItem.creditEnHeures()	+ " ?",
  			"OUI SUPPRIMER", "Annuler et conserver la transaction", null, CRIAlertPage.QUESTION, responder);
  }
  
  

	
	// editions
  
  public PdfBoxSituationCETNewCtrl ctrlSituationCETAgent() {
  	return new PdfBoxSituationCETNewCtrl(PrintSituationCET.class, edc, dateArret);
  }
  
  /**
   * Situation CET d'un agent 
   */
  public class PdfBoxSituationCETNewCtrl extends A_PdfBoxSituationCETNewCtrl {

		public PdfBoxSituationCETNewCtrl(
				Class aGenericSixPrintClass, EOEditingContext anEc, NSTimestamp aDateArret) {
			super(aGenericSixPrintClass, anEc, aDateArret);
		}

		
	  public String fileName() {
	  	return "SituationCET_"+
	  	StringCtrl.toBasicString(individu.nom())+"_"+
	  	StringCtrl.toBasicString(individu.prenom());
	  }


		@Override
		public NSArray arraySituationDico() {
			return new NSArray(dicoSituationForIndividu(individu));
		}
  }
}