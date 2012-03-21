package fr.univlr.cri.conges;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eocontrol.EOAndQualifier;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSNumberFormatter;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.constantes.ConstsJour;
import fr.univlr.cri.conges.eos.modele.conges.EOCET;
import fr.univlr.cri.conges.eos.modele.conges.EOCETTransaction;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.grhum.EOVPersonnelActuel;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.print.ConstsPrint;
import fr.univlr.cri.conges.print.PrintSituationCET;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRDataResponse;
import fr.univlr.cri.webapp.LRLog;
/**
 * Ensemble des CET ouverts. 
 * 
 * @author ctarade
 *
 */
public class AdminCETSituation 
	extends A_ComponentAnneeUniv {

	// recherche
	public String searchString;
	
	// personnel actuels
	public boolean isOnlyPersonnelActuel = true;
	
	// liste des cet
	public WODisplayGroup cetDg;
	public EOCET cetItem;
	public EOCET cetSelected;
	
	private boolean shouldRefreshCetDg;
	
	// 
	private EOAffectationAnnuelle firstAffAnnCetItem;
  
  // date a imprimer dans l'edition de la situation
  public NSTimestamp dateArret = DateCtrlConges.now();
	
	public AdminCETSituation(WOContext context) {
		super(context);
		shouldRefreshCetDg = true;
	}
	
  /**
   * forcer les donnees du DisplayGroup a etre rechargees.
   */
	private void refreshCetDg() {
		// qualifier
		NSArray arrayQual = new NSArray();
		
		// recherche par nom
  	if (!StringCtrl.isEmpty(searchString)) {
  		arrayQual = arrayQual.arrayByAddingObject(
  				CRIDataBus.newCondition(
  						EOIndividu.getStringQualifierIndividuForNomPrenom(
  								EOCET.INDIVIDU_KEY,	searchString),
  								null));
  	} 
  	
  	// personnel actuel O/N
  	if (isOnlyPersonnelActuel) {
  		arrayQual = arrayQual.arrayByAddingObject(
  				CRIDataBus.newCondition(
  						EOCET.INDIVIDU_KEY + "." + EOIndividu.TO_V_PERSONNEL_ACTUEL_KEY + "." + EOVPersonnelActuel.NO_DOSSIER_PERS_KEY +" <> nil"));
  	}
  	
  	cetDg.setQualifier(new EOAndQualifier(arrayQual));
  	
		cetDg.qualifyDataSource(); // fetch
  	// classer par nom
		cetDg.setSortOrderings(EOCET.SORT_INDIVIDU);
		cetDg.updateDisplayedObjects();  // sort 
	}

	/**
	 * Remise à zéro de certaines variables
	 */
	public void appendToResponse(WOResponse arg0, WOContext arg1) {
		if (shouldRefreshCetDg) {
			refreshCetDg();
			shouldRefreshCetDg = false;
		}
		
		super.appendToResponse(arg0, arg1);
	}

	/**
	 * 
	 */
	public void onChangeAnneUnivSelectionnee() {
		shouldRefreshCetDg = true;
	}	

	
	// boolean
	
	/**
	 * Indique si le CET en cours de la liste est 
	 * celui selectionné
	 */
	public boolean isTheSelectedCetItem() {
		return cetItem == cetSelected;
	}
	
	
  // actions
  
  /**
   * Demander le rafraichissement de la liste
   */
  public WOComponent doRefreshCetDg() {
  	shouldRefreshCetDg = true;
  	return null;
  }
  
  /**
   * Action de sélectionner un CET
   * @return
   */
  public WOComponent doSelectCetItem() {
  	cetSelected = cetItem;
  	return null;
  }
  
  
  // getters
  
  /**
   * Le solde de l'ancien regime en jours à 7h00
   */
  public float getSoldeAncienRegimeEnJour7h00CetItem() {
  	float solde = 0;
  	 
  	solde = ((float) cetItem.minutesRestantesAncienRegime()) / (float) ConstsJour.DUREE_JOUR_7H00;
  	 
  	return solde;
  }  
  
  /**
   * Le solde de l'ancien regime en heures
   */
  public String getSoldeAncienRegimeEnHeuresCetItem() {
  	String solde = "00:00";
  	 
  	solde = TimeCtrl.stringForMinutes(cetItem.minutesRestantesAncienRegime());
  	 
  	return solde;
  }
  
  /**
   * Le solde de en jours à 7h00 en début d'année
   */
  public float getSoldeRegimePerenneDebutAnneeEnJour7h00CetItem() {
  	float solde = 0;
  	 
  	if (firstAffAnnCetItem != null) {
  		solde = ((float) cetItem.minutesRestantesRegimePerenne(
  				firstAffAnnCetItem.dateDebutAnnee())) / (float) ConstsJour.DUREE_JOUR_7H00;
  	}
  	 
  	 return solde;
  }
  
  /**
   * Le solde de en heures en début d'année
   */
  public String getSoldeRegimePerenneDebutAnneeEnHeuresCetItem() {
  	String solde = "00:00";
	 
  	if (firstAffAnnCetItem != null) {
  		solde = TimeCtrl.stringForMinutes(cetItem.minutesRestantesRegimePerenne(
  				firstAffAnnCetItem.dateDebutAnnee()));
  	}
  	 
  	return solde;
  }
  
  /**
   * Le solde de en jours à 7h00 en fin d'année
   */
  public float getSoldeRegimePerenneFinAnneeEnJour7h00CetItem() {
  	 float solde = 0;
  	 
  	 if (firstAffAnnCetItem != null) {
  		 solde = ((float) cetItem.minutesRestantesRegimePerenne(
  				 firstAffAnnCetItem.dateFinAnnee())) / (float) ConstsJour.DUREE_JOUR_7H00;
  	 }
  	 
  	 return solde;
  }
  
  /**
   * Le solde de en heures en fin d'année
   */
  public String getSoldeRegimePerenneFinAnneeEnHeuresCetItem() {
  	String solde = "00:00";

  	if (firstAffAnnCetItem != null) {
  		solde = TimeCtrl.stringForMinutes(cetItem.minutesRestantesRegimePerenne(
  				firstAffAnnCetItem.dateFinAnnee()));
  	}
  	 
  	return solde;
  }
  
  /**
   * Donne le premier planning pour l'année #selectedAnneeUniv
   * et l'agent 
   * @return
   */
  private EOAffectationAnnuelle getFirstAffAnnCetItem() {
  	EOAffectationAnnuelle affAnn = null;
  	
  	NSArray affAnnList = EOAffectationAnnuelle.findAffectationsAnnuelleInContext(
				edc, cetItem.individu().oid(), null, selectedAnneeUniv);
		
		// on selectionne le premier
		if (affAnnList.count() > 0) {
			affAnn = (EOAffectationAnnuelle) affAnnList.objectAtIndex(0);
		}

  	
  	return affAnn;
  }
  
  // setters
 	
	/**
	 * 
	 * @param value
	 */
	public void setSearchString(String value) {
		searchString = value;
		shouldRefreshCetDg = true;
	}
	
	/**
	 * 
	 */
	public void setIsOnlyPersonnelActuel(boolean value) {
		isOnlyPersonnelActuel = value;
		shouldRefreshCetDg = true;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setCetItem(EOCET value) {
		cetItem = value;
		if (cetItem != null) {
			firstAffAnnCetItem = getFirstAffAnnCetItem();
		}
	}
	
	
  // export CSV

	/**
	 * Affichage des durees
	 */
	private NSNumberFormatter nf;
	
	private NSNumberFormatter nf() {
		if (nf == null) {
			nf = new NSNumberFormatter();
		  nf.setPattern("0.00");
		}
		return nf;
	}
	
	/**
	 * 
	 */
	public WOResponse printCsv() {
  	LRDataResponse resp = new LRDataResponse();
  	StringBuffer sb = new StringBuffer();

  	// liste de tous les cet affichables
  	NSArray arrayCet = getAllCetFromDg();

  	// entete
  	sb.append("agent").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
  	sb.append("ancien regime (j)").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
  	sb.append("ancien regime (h)").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
  	sb.append("regime perenne debut annee (j)").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
  	sb.append("regime perenne debut annee (h)").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
  	sb.append("regime perenne fin annee (j)").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
  	sb.append("regime perenne fin annee (h)").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
   	sb.append(ConstsPrint.CSV_NEW_LINE);
   	
  	//
  	for (int i = 0; i < arrayCet.count(); i++) {
  		setCetItem((EOCET) arrayCet.objectAtIndex(i));
 
  		sb.append(cetItem.individu().nomComplet()).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
   		
   		sb.append(nf().format(getSoldeAncienRegimeEnJour7h00CetItem())).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
   		sb.append(getSoldeAncienRegimeEnHeuresCetItem()).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
   		sb.append(nf().format(getSoldeRegimePerenneDebutAnneeEnJour7h00CetItem())).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
   		sb.append(getSoldeRegimePerenneDebutAnneeEnHeuresCetItem()).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
   		sb.append(nf().format(getSoldeRegimePerenneFinAnneeEnJour7h00CetItem())).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
   		sb.append(getSoldeRegimePerenneFinAnneeEnHeuresCetItem()).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
   		
  		sb.append(ConstsPrint.CSV_NEW_LINE);
  		
		}
  	
  	NSData stream = new NSData(sb.toString(), ConstsPrint.CSV_ENCODING);
  	resp.setContent(stream);
		resp.setContentEncoding(ConstsPrint.CSV_ENCODING);		
  	resp.setHeader(String.valueOf(stream.length()), "Content-Length");
  	resp.setFileName(DateCtrlConges.dateToString(DateCtrlConges.now(), "%Y%m%d") + "_liste_cet.csv");
  	return resp;
	}
	
	/**
	 * La liste de tous les elements affichables par le DG
	 * @return
	 */
	private NSArray getAllCetFromDg() {
  	// configurer le DG pour que tout soit sur la même page
  	int prevNumberOfObjectsPerBatch = cetDg.numberOfObjectsPerBatch();
  	cetDg.setNumberOfObjectsPerBatch(0);
  	NSArray arrayCet = cetDg.displayedObjects();
  	// remettre le DG comme précédemment
  	cetDg.setNumberOfObjectsPerBatch(prevNumberOfObjectsPerBatch);
  	return arrayCet;
	}
	
	// editions
  
  public PdfBoxSituationCETAllCtrl ctrlSituationCETAllCet() {
  	return new PdfBoxSituationCETAllCtrl(PrintSituationCET.class, edc, dateArret);
  }
  
  /**
   * Situation CET d'un agent 
   */
  public class PdfBoxSituationCETAllCtrl extends A_PdfBoxSituationCETNewCtrl {

		public PdfBoxSituationCETAllCtrl(
				Class aGenericSixPrintClass, EOEditingContext anEc, NSTimestamp aDateArret) {
			super(aGenericSixPrintClass, anEc, aDateArret);
		}

		
	  public String fileName() {
	  	// on nomme ainsi : le premier nom et le dernier nom
	  	return "SituationsCET_" + StringCtrl.normalize(lAnneeUnivSelectionnee);
	  }


		@Override
		public NSArray arraySituationDico() {
			NSArray arrayDico = new NSArray();
			
			// seulement la page affichée (l'application freeze au bout de 15 plannings)
	  	NSArray arrayCet = getAllCetFromDg();

	  	LRLog.log("printing situation : " + arrayCet.count());
			for (int i=0; i<arrayCet.count(); i++) {
				EOCET cet = (EOCET) arrayCet.objectAtIndex(i);
				// ne pas traiter les CET qui n'ont pas de transaction
				if (cet.cETTransactions().count() > 0) {
					// il faut charger le planning de l'agent car les debitables ne sont
					// calculer qu'a cette condition
					// on passe par un autre ec pour avoir suffisament de mémoire
					EOEditingContext ec = new EOEditingContext();
					EOIndividu individu = EOIndividu.findIndividuForPersIdInContext(ec, cet.individu().persId());
					EOCET otherCet = individu.toCET();
					otherCet.setCngUserInfo(cngUserInfo());
					otherCet.doRecalculerPlannings();
					LRLog.log(" - " + individu.nomComplet() + " (" + (i+1) + "/" + arrayCet.count() + ")");
					arrayDico = arrayDico.arrayByAddingObject(dicoSituationForIndividu(individu));
					// cette opération permet de faire du ménage dans la mémoire
					ec.dispose();
				}
			}
			
			return arrayDico;
		}
  }

  private void displayMemory() {
    Runtime rt = Runtime.getRuntime();
    long free = rt.freeMemory();
    long total = rt.totalMemory();
    long percent = (100*free)/total;
    LRLog.log( "freemem: "+free+" totalmem: "+total+" ("+percent+" % free)" );
  }
  
  private void cleanMemory() {
  	LRLog.log( "MEMORY-STATUS\n" );
    Runtime rt = Runtime.getRuntime();
    LRLog.log( "BEFORE rt.gc()" );
    long free = rt.freeMemory();
    long total = rt.totalMemory();
    long percent = (100*free)/total;
    LRLog.log( "freemem: "+free+" totalmem: "+total+" ("+percent+" % free)" );
    rt.gc(); // free up some memory
    LRLog.log( "AFTER rt.gc()" );
    free = rt.freeMemory();
    total = rt.totalMemory();
    percent = (100*free)/total;
    LRLog.log( "freemem: "+free+" totalmem: "+total+" ("+percent+" % free)\n" );
  }

}