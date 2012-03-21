package fr.univlr.cri.conges;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.constantes.ConstsJour;
import fr.univlr.cri.conges.eos.modele.conges.EOCETTransaction;
import fr.univlr.cri.conges.eos.modele.grhum.EOAdresse;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;
import fr.univlr.cri.conges.objects.I_DebitableSurCET;
import fr.univlr.cri.conges.print.CngPdfBoxCtrl;
import fr.univlr.cri.conges.print.ConstsPrint;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.util.ArrayCtrl;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.CRIDataBus;

/**
 * Edition situation d'un ou plusieurs agents
 */
public abstract class A_PdfBoxSituationCETNewCtrl extends CngPdfBoxCtrl {

	private NSTimestamp dateArret;
	
	public A_PdfBoxSituationCETNewCtrl(
			Class genericSixPrintClass,	EOEditingContext anEc, NSTimestamp aDateArret) {
		super(genericSixPrintClass, anEc);
		dateArret = aDateArret;
	}
		
	public abstract String fileName();
	
	/** la liste des dictionnaires (1 par situation a imprimer) */
	public abstract NSArray arraySituationDico();
	
	public NSDictionary buildDico() {
		NSMutableDictionary dico = new NSMutableDictionary();
		
		dico.setObjectForKey(
				DateCtrlConges.dateToString(dateArret), ConstsPrint.XML_KEY_SITUATION_DATE_ARRET);

		// adresse de l'etablissement
		EOStructure etablissement = EOStructure.getStructureRacineInContext(getEc());
		dico.setObjectForKey(etablissement.libelleLong(),		ConstsPrint.XML_KEY_ETAB_ADRESSE_LIBELLE);
		EOAdresse adresse = EOAdresse.getAdresseRacineInContext(getEc());
		String adrAdresse1 = "";
		if (!StringCtrl.isEmpty(adresse.adrAdresse1())) {
			adrAdresse1 = adresse.adrAdresse1();
		}
		String adrAdresse2 = "";
		if (!StringCtrl.isEmpty(adresse.adrAdresse2())) {
			adrAdresse2 = adresse.adrAdresse2();
		}
		String codePostalVille = "";
		if (!StringCtrl.isEmpty(adresse.codePostal()) && !StringCtrl.isEmpty(adresse.ville())) {
			codePostalVille = adresse.codePostal() + " " + adresse.ville();
		}
		dico.setObjectForKey(adrAdresse1,				ConstsPrint.XML_KEY_ETAB_ADRESSE_ADRESSE_1);
		dico.setObjectForKey(adrAdresse2,				ConstsPrint.XML_KEY_ETAB_ADRESSE_ADRESSE_2);
		dico.setObjectForKey(codePostalVille,		ConstsPrint.XML_KEY_ETAB_ADRESSE_CP_VILLE);
		
		// URL du logo
		Application app = (Application) Application.application();
		dico.setObjectForKey(app.mainLogoUrl(), ConstsPrint.XML_KEY_MAIN_LOGO_URL);	

		// un seul dico car une seule situatino
		dico.setObjectForKey(arraySituationDico(), ConstsPrint.SITUATION_ARRAY_DICO);
		
		return dico;
	}

	/**
	 * 
	 * @param individu
	 * @return
	 */
	public NSDictionary dicoSituationForIndividu(EOIndividu individuSituation) {
		
    NSMutableDictionary dicoSituation = new NSMutableDictionary();
    dicoSituation.setObjectForKey(individuSituation.cCivilite(), ConstsPrint.XML_KEY_CIVILITE);
    dicoSituation.setObjectForKey(individuSituation.prenom(), ConstsPrint.XML_KEY_PRENOM_DEMANDEUR);
    dicoSituation.setObjectForKey(individuSituation.nom(), ConstsPrint.XML_KEY_NOM_DEMANDEUR);
    
    // grade
    dicoSituation.setObjectForKey(
    		individuSituation.getLibelleGradeForIndividu(
            DateCtrlConges.now(),
            DateCtrlConges.now()),
        ConstsPrint.XML_KEY_GRADE_DEMANDEUR);

    
    // la liste de toutes les transactions
    NSArray transactions = EOCETTransaction.findAllTransactionForIndividuInContext(
    		individuSituation.editingContext(), individuSituation, false);
    
    // ancien regime
    feedDicoForRegime(dicoSituation, false, individuSituation, transactions);
    
    // regime perenne
    feedDicoForRegime(dicoSituation, true, individuSituation, transactions);
    
    return dicoSituation;
	}
  
  
  /**
   * 
   * @param inDico
   * @param isRegimePerenne
   */
  private void feedDicoForRegime(
  		NSMutableDictionary inDico, boolean isRegimePerenne, EOIndividu individu, NSArray allTransaction) {
    NSArray transactions = EOQualifier.filteredArrayWithQualifier(
    		allTransaction,
	  		CRIDataBus.newCondition(EOCETTransaction.IS_TRANSACTION_REGIME_PERENNE_KEY+"=%@", 
	  				new NSArray(new Boolean(isRegimePerenne))));
    if (transactions.count() > 0) {
    	// construire l'objet qui contiendra toutes les infos pour la classe d'impression
    	feedDicoForTransactions(inDico, isRegimePerenne, individu, transactions);
    }
  }
  
  /**
   * Ajouter les entrées au dico 
   */
  private void feedDicoForTransactions(
  		NSMutableDictionary inDico, boolean isRegimePerenne, EOIndividu individu, NSArray transactions) {
  	NSMutableArray array = new NSMutableArray();

  	float soldeIntermediaireEnJour7h00 = (float) 0;
	
  	for (int i=0; i<transactions.count(); i++) {
  		EOCETTransaction transaction = (EOCETTransaction) transactions.objectAtIndex(i);

  		// premiere transaction : le solde est le credit initial
  		if (i == 0) {
  			soldeIntermediaireEnJour7h00 = transaction.creditEnJour7h00();
  		} else {
    		// transactions suivantes : on ajoute la valeur du crédit
  			soldeIntermediaireEnJour7h00 += transaction.creditEnJour7h00();
  		}
  		
  		NSMutableDictionary dico = new NSMutableDictionary();
  		dico.setObjectForKey(transaction.motif(false) , 						
  				ConstsPrint.XML_KEY_SITUATION_TRANSACTION_LIBELLE);
  		dico.setObjectForKey(new Float(soldeIntermediaireEnJour7h00),					
  				ConstsPrint.XML_KEY_SITUATION_TRANSACTION_SOLDE_INITIAL_EN_JOURS_7H00);
  		dico.setObjectForKey(transaction.creditEnJour7h00() , 
  				ConstsPrint.XML_KEY_SITUATION_TRANSACTION_VALEUR_EN_JOURS_7H00);
  		NSDictionary dicoDebitables = getDicoDebitablesForTransaction(
						transaction, isRegimePerenne, i == 0, soldeIntermediaireEnJour7h00);
  		// recuperation du solde intermediaire
  		soldeIntermediaireEnJour7h00 = ((Float) dicoDebitables.objectForKey(ConstsPrint.DICO_KEY_SITUATION_SOLDE_INTERMEDIAIRE)).floatValue();
  		dico.setObjectForKey(dicoDebitables, 
  				ConstsPrint.DICO_KEY_SITUATION_DICO_DEBITABLES);
  		array.addObject(dico);
  	
  	}
  	
  	// solde final
  	int soldeFinalMinutes = 
  		individu.toCET().minutesRestantesAncienRegime() +
  		individu.toCET().minutesRestantesRegimePerenne(DateCtrlConges.now());
  	float soldeFinalEnJours7h00 = getJours7h00ForMinutes(soldeFinalMinutes);
  	inDico.setObjectForKey(new Float(soldeFinalEnJours7h00), ConstsPrint.XML_KEY_SITUATION_SOLDE_FINAL_EN_JOURS_7H00);
  	
  	// le array contenant les transactions
  	String keyArray = ConstsPrint.DICO_KEY_SITUATION_ARRAY_LIGNE_CREDIT_ANCIEN_REGIME;
  	if (isRegimePerenne) {
  		keyArray = ConstsPrint.DICO_KEY_SITUATION_ARRAY_LIGNE_CREDIT_REGIME_PERENNE;
  	}
  	inDico.setObjectForKey(array, keyArray);
  }
  
  /**
	 *
   */
  private NSDictionary getDicoDebitablesForTransaction(
  		EOCETTransaction transaction, boolean isRegimePerenne, boolean isPremiereTransaction, float soldeIntermediaire) {
  	NSMutableDictionary dico = new NSMutableDictionary();
  	
		NSArray debitables = transaction.getDebitables();
  	
  	// alimenter le dico pour chaque debitable
  	if (!ArrayCtrl.isEmpty(debitables)) {
  		NSArray arrayDicoDebitable = new NSArray();
  		// 
  		for (int i=0; i<debitables.count(); i++) {
  			I_DebitableSurCET debitable = (I_DebitableSurCET) debitables.objectAtIndex(i);
  			NSMutableDictionary dicoDebitable = new NSMutableDictionary();
  			dicoDebitable.setObjectForKey(
  					debitable.displayCet(), ConstsPrint.XML_KEY_SITUATION_DEBITABLE_LIBELLE);
  			float valeurEnJours7h00 = getJours7h00ForMinutes(((Integer) transaction.getDebits().objectAtIndex(i)).intValue());
   			dicoDebitable.setObjectForKey(
   					new Float(valeurEnJours7h00), ConstsPrint.XML_KEY_SITUATION_DEBITABLE_VALEUR_EN_JOURS_7H00);
   			// soustraire cette valeur pour connaitre le solde suivant
   			soldeIntermediaire = soldeIntermediaire - valeurEnJours7h00;
   			// ne conserver que la valeur absolue, car les arrondis peuvent faire afficher -0.00
   			if (soldeIntermediaire < (float) 0) {
   				soldeIntermediaire = -soldeIntermediaire;
   			}
   			dicoDebitable.setObjectForKey(
   					new Float(soldeIntermediaire), ConstsPrint.XML_KEY_SITUATION_DEBITABLE_SOLDE_FINAL_EN_JOURS_7H00);
  			arrayDicoDebitable = arrayDicoDebitable.arrayByAddingObject(dicoDebitable.immutableClone());
  		}
  		dico.setObjectForKey(
  				arrayDicoDebitable, ConstsPrint.DICO_KEY_SITUATION_ARRAY_DEBITABLES);
  	} else {
  		// si pas de debitable, alors on indique explicitement une ligne vide
  		NSMutableDictionary dicoDebitableVide = new NSMutableDictionary();
  		dicoDebitableVide.setObjectForKey(
 					"-", ConstsPrint.XML_KEY_SITUATION_DEBITABLE_LIBELLE);
  		dicoDebitableVide.setObjectForKey(
 					"-", ConstsPrint.XML_KEY_SITUATION_DEBITABLE_VALEUR_EN_JOURS_7H00);
  		dicoDebitableVide.setObjectForKey(
  				new Float(soldeIntermediaire), ConstsPrint.XML_KEY_SITUATION_DEBITABLE_SOLDE_FINAL_EN_JOURS_7H00);
  		dico.setObjectForKey(
  				new NSArray(dicoDebitableVide.immutableClone()), ConstsPrint.DICO_KEY_SITUATION_ARRAY_DEBITABLES);
  	}
  	
  	// stockage du solde intermediaire
  	dico.setObjectForKey(new Float(soldeIntermediaire), ConstsPrint.DICO_KEY_SITUATION_SOLDE_INTERMEDIAIRE);
  	
  	return dico.immutableClone();
  }
 
  /**
   * 
   * @param minutes
   * @return
   */
  private float getJours7h00ForMinutes(int minutes) {
  	return (float)minutes / (float)ConstsJour.DUREE_JOUR_7H00;
  }
}