package fr.univlr.cri.conges;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eoaccess.EOAttribute;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eocontrol.EOAndQualifier;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.constantes.ConstsOccupation;
import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;
import fr.univlr.cri.conges.eos.modele.planning.EOOccupation;
import fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation;
import fr.univlr.cri.conges.eos.modele.planning.EOVIndividuConges;
import fr.univlr.cri.conges.eos.modele.planning.EOVOccupationsSuivi;
import fr.univlr.cri.conges.print.ConstsPrint;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.ArrayCtrl;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRDataResponse;
import fr.univlr.cri.webapp.LRLog;
import fr.univlr.cri.webapp.LRSort;

/**
 * Ecran de gestion de suivi des occupations
 * 
 * @author ctarade
 */
public class AdminSuiviOccupation 
	extends YCRIWebPage {
	
	// types d'occupation et leur selection
	public NSArray typeOccupationJourNonDrhList;
	public NSArray typeOccupationMinuteNonDrhList;
	public NSArray typeOccupationJourDrhList;
	public NSArray typeOccupationMinuteDrhList;
	private NSMutableArray typeOccupationSelecteds;
	public EOTypeOccupation typeOccupationItem;
	
	// liste des années universitaires
	public NSArray dateDebutAnneeUnivList;
	public NSTimestamp dateDebutAnneeUnivItem;
	
	// liste des services
	public NSArray serviceList;
	public EOStructure serviceItem;
	
	// liste des composantes
	public NSArray composanteList;
	public EOStructure composanteItem;
	
	// liste des occupations
	public WODisplayGroup vOccupationsSuiviDg;
	public EOVOccupationsSuivi vOccupationSuiviItem;
	
	// liste des etats de validation des occupations
	public NSArray etatList;
	public NSMutableArray etatSelecteds;
	public String etatItem;
	
	// 
	public String nomUsuelSearch;
	public String prenomSearch;
	
	// dates
	public NSTimestamp dateDebut;
	public NSTimestamp dateFin;
	public NSArray dateDebutAmPmList;
	public String dateDebutAmPmItem;
	public String dateDebutAmPmSelected;
	public NSArray dateFinAmPmList;
	public String dateFinAmPmItem;
	public String dateFinAmPmSelected;
	
	// classement par attribut
	public NSArray attributeList;
	public EOAttribute attributeSelected;
	public EOAttribute attributeItem;
	
	// nombre de lignes affichées par page
	public NSArray ligneParPageList;
	public Integer ligneParPageSelected;
	public Integer ligneParPageItem;
	
	public AdminSuiviOccupation(WOContext context) {
		super(context);
		initComponent();
	}
	
	/**
	 * Initialiser les variables du composant
	 */
	private void initComponent() {
		NSArray allVIndividuCongesList = EOVIndividuConges.fetchAllVIndividuCongeses(edc);
		
		//
		dateDebutAnneeUnivList = LRSort.sortedArray(allVIndividuCongesList, EOVIndividuConges.DTE_DEBUT_ANNEE_KEY);
		dateDebutAnneeUnivList = (NSArray) dateDebutAnneeUnivList.valueForKey(EOVIndividuConges.DTE_DEBUT_ANNEE_KEY);
		dateDebutAnneeUnivList = ArrayCtrl.removeDoublons(dateDebutAnneeUnivList);
		
	  // 
		serviceList = (NSArray) allVIndividuCongesList.valueForKey(EOVIndividuConges.TO_STRUCTURE_KEY);
		serviceList = ArrayCtrl.removeDoublons(serviceList);
		serviceList = LRSort.sortedArray(serviceList, EOStructure.DISPLAY_KEY);
		
    // 
		composanteList = (NSArray) serviceList.valueForKey(EOStructure.TO_COMPOSANTE_KEY);
		composanteList = ArrayCtrl.removeDoublons(composanteList);
		composanteList = LRSort.sortedArray(composanteList, EOStructure.DISPLAY_KEY);
		
		// selectionner l'année universitaire en cours par defaut
		NSTimestamp now = DateCtrlConges.now();
		NSTimestamp dateDebutAnneeUnivCurrent = DateCtrlConges.dateToDebutAnneeUniv(now);
		if (dateDebutAnneeUnivCurrent != null) {
			vOccupationsSuiviDg.queryBindings().setObjectForKey(
					dateDebutAnneeUnivCurrent, EOVOccupationsSuivi.DTE_DEBUT_ANNEE_KEY); 
		}
		
		// dates
		dateDebut = dateDebutAnneeUnivCurrent;
		dateFin 	= DateCtrlConges.dateToFinAnneeUniv(dateDebut);
		dateDebutAmPmList = dateFinAmPmList = new NSArray(new String[] { 
				ConstsOccupation.OCC_MATIN, ConstsOccupation.OCC_APREM });
		dateDebutAmPmSelected = ConstsOccupation.OCC_MATIN;
		dateFinAmPmSelected = ConstsOccupation.OCC_APREM;
		
		// etats
		etatList = new NSArray(new String[]{
				ConstsOccupation.CODE_VALIDEE, ConstsOccupation.CODE_VISEE, ConstsOccupation.CODE_EN_COURS_DE_VALIDATION});
		etatSelecteds = new NSMutableArray(etatList);
		
		// types
		/*NSArray typeOccupationList = EOUtilities.objectsWithFetchSpecificationAndBindings(
    		edc, EOTypeOccupation.ENTITY_NAME, "OccupationsEditables", null);*/
		
		NSArray typeOccupationList = EOTypeOccupation.findTypesOccupationsEditablesInContext(edc);
		
    typeOccupationList = EOSortOrdering.sortedArrayUsingKeyOrderArray(typeOccupationList, EOTypeOccupation.ARRAY_SORT);
    typeOccupationJourNonDrhList 		= EOTypeOccupation.filterTypeOccupation(typeOccupationList, false, false);
    typeOccupationMinuteNonDrhList 	= EOTypeOccupation.filterTypeOccupation(typeOccupationList, true, 	false);
    typeOccupationJourDrhList 			= EOTypeOccupation.filterTypeOccupation(typeOccupationList, false, true);
    typeOccupationMinuteDrhList 		= EOTypeOccupation.filterTypeOccupation(typeOccupationList, true, 	true);
    
  	typeOccupationSelecteds = new NSMutableArray();

  	//
    EOEntity entityVIndividuConges = EOModelGroup.defaultGroup().entityNamed(EOVOccupationsSuivi.ENTITY_NAME);
    attributeList = entityVIndividuConges.attributes();
    setAttributeSelected(
    		(EOAttribute) EOQualifier.filteredArrayWithQualifier(attributeList, 
    				CRIDataBus.newCondition("name='"+EOVOccupationsSuivi.NOM_USUEL_KEY+"'")).lastObject());
    //
    ligneParPageList = new NSArray(
    		new Integer[] {
    				new Integer(20),
    				new Integer(50),
    				new Integer(100),
    				new Integer(200),
    				new Integer(500)});
    setLigneParPageSelected(new Integer(50));
	}

	/**
	 * Appliquer les filtres complementaires qui ne sont pas
	 * dans la fetchspec de l'entité
	 */
	public WOComponent doRefreshDg() {
		// appliquer les filtres et classements
		NSArray arrayQualFilter = new NSArray();
		// etats
		if (!ArrayCtrl.isEmpty(etatSelecteds)) {
			String strQualEtat = "";
			for (int i=0; i<etatSelecteds.count(); i++) {
				strQualEtat += EOVOccupationsSuivi.STATUS_KEY + "=%@";
				if (i < etatSelecteds.count() -1) {
					strQualEtat += " or ";
				}
			}
			arrayQualFilter = arrayQualFilter.arrayByAddingObject(CRIDataBus.newCondition(strQualEtat, etatSelecteds));
		}
		// types d'occupation
		if (!ArrayCtrl.isEmpty(typeOccupationSelecteds)) {
			String strQualTypeOcc = "";
			for (int i=0; i<typeOccupationSelecteds.count(); i++) {
				strQualTypeOcc += EOVOccupationsSuivi.TO_TYPE_OCCUPATION_KEY + "=%@";
				if (i < typeOccupationSelecteds.count() -1) {
					strQualTypeOcc += " or ";
				}
			}
			arrayQualFilter = arrayQualFilter.arrayByAddingObject(
					CRIDataBus.newCondition(strQualTypeOcc, typeOccupationSelecteds));
		}

		// dates : on filtre uniquement si les 2 sont saisies
		if (dateDebut != null && dateFin != null) {
			NSTimestamp dateDebutBindings = dateDebut;
			if (dateDebutAmPmSelected.equals(ConstsOccupation.OCC_APREM)) {
				dateDebutBindings = dateDebutBindings.timestampByAddingGregorianUnits(0,0,0,23,59,59);
			}
			NSTimestamp dateFinBindings = dateFin;
			if (dateFinAmPmSelected.equals(ConstsOccupation.OCC_APREM)) {
				dateFinBindings = dateFinBindings.timestampByAddingGregorianUnits(0,0,0,23,59,59);
			}
			vOccupationsSuiviDg.queryBindings().setObjectForKey(dateDebutBindings, EOVOccupationsSuivi.DTE_DEBUT_KEY);
			vOccupationsSuiviDg.queryBindings().setObjectForKey(dateFinBindings, EOVOccupationsSuivi.DTE_FIN_KEY);
		} else {
			vOccupationsSuiviDg.queryBindings().removeObjectForKey(EOVOccupationsSuivi.DTE_DEBUT_KEY);
			vOccupationsSuiviDg.queryBindings().removeObjectForKey(EOVOccupationsSuivi.DTE_FIN_KEY);
		}
		
		LRLog.log(""+vOccupationsSuiviDg.queryBindings());
		
		
		// on oblige a ce que les etats et les types soient selectionnés
		EOQualifier qual /*= null;
		if (arrayQualFilter.count() == 2) {
			qual */= new EOAndQualifier(arrayQualFilter);
		/*} else {
			qual = CRIDataBus.newCondition(EOVOccupationsSuivi.OID_AFF_ANN_KEY + "=-1");
		}*/

		vOccupationsSuiviDg.setQualifier(qual);
		//vOccupationsSuiviDg.setSortOrderings(arraySort);
		vOccupationsSuiviDg.qualifyDataSource();
		vOccupationsSuiviDg.updateDisplayedObjects();  // qual & sort
		
		return null;
	}
	
	// DISPLAY
	
	/**
	 * 
	 */
  public String dateDebutAnneeUnivItemDisplay() {
    return DateCtrlConges.anneeUnivForDate(dateDebutAnneeUnivItem);
  }
  
  /**
   * 
   * @return
   */
  public String vOccupationSuiviLibelleEtatValidation() {
  	return EOOccupation.libelleForEtatValidation(vOccupationSuiviItem.status());
  }

  /**
   * 
   * @return
   */
  public String vOccupationSuiviLibelleTempsSurHoraireHeures() {
  	return TimeCtrl.stringForMinutes(vOccupationSuiviItem.tempsSurHoraire().intValue());
  }
  
  /**
   * 
   * @return
   */
  public String vOccupationSuiviLibelleTempsDecompteHeures() {
  	return TimeCtrl.stringForMinutes(vOccupationSuiviItem.tempsDecompte().intValue());
  }

  /**
   * 
   * @return
   */
  public String vOccupationSuiviDgDisplayedObjectsTempsSurHoraireTotalHeures() {
  	return TimeCtrl.stringForMinutes(
  			((Number) vOccupationsSuiviDg.displayedObjects().valueForKeyPath("@sum."+EOVOccupationsSuivi.TEMPS_SUR_HORAIRE_KEY)).intValue());
  }
  
  /**
   * 
   * @return
   */
  public String vOccupationSuiviDgDisplayedObjectsTempsDecompteTotalHeures() {
   	return TimeCtrl.stringForMinutes(
  			((Number) vOccupationsSuiviDg.displayedObjects().valueForKeyPath("@sum."+EOVOccupationsSuivi.TEMPS_DECOMPTE_KEY)).intValue());
  }

  /**
   * 
   * @return
   */
  public String vOccupationSuiviDgAllObjectsTempsSurHoraireTotalHeures() {
  	return TimeCtrl.stringForMinutes(
  			((Number) vOccupationsSuiviDg.allObjects().valueForKeyPath("@sum."+EOVOccupationsSuivi.TEMPS_SUR_HORAIRE_KEY)).intValue());
  }
  
  /**
   * 
   * @return
   */
  public String vOccupationSuiviDgAllObjectsTempsDecompteTotalHeures() {
   	return TimeCtrl.stringForMinutes(
  			((Number) vOccupationsSuiviDg.allObjects().valueForKeyPath("@sum."+EOVOccupationsSuivi.TEMPS_DECOMPTE_KEY)).intValue());
  }
  
  
  // GETTERS

  /**
   * 
   */
  public boolean isSelectedEtatItem() {
  	return etatSelecteds.containsObject(etatItem);
  }
  
  /**
   * 
   * @return
   */
  public String getEtatItemLibelle() {
  	return EOOccupation.libelleForEtatValidation(etatItem);
  }

  /**
   * 
   */
  public boolean isSelectedTypeOccupationItem() {
  	return typeOccupationSelecteds.containsObject(typeOccupationItem);
  }
  
  
  // SETTERS
  
  /**
   * 
   */
  public void setIsSelectedEtatItem(boolean shouldAddEtatItem) {
  	if (shouldAddEtatItem) {
  		if (!etatSelecteds.containsObject(etatItem)) {
  			etatSelecteds.addObject(etatItem);
  		}
  	} else {
  		if (etatSelecteds.containsObject(etatItem)) {
  			etatSelecteds.removeObject(etatItem);
  		}
  	}
  }
  
  /**
   * 
   */
  public void setIsSelectedTypeOccupationItem(boolean shouldAddTypeOccupationItem) {
  	if (shouldAddTypeOccupationItem) {
  		if (!typeOccupationSelecteds.containsObject(typeOccupationItem)) {
  			typeOccupationSelecteds.addObject(typeOccupationItem);
  		}
  	} else {
  		if (typeOccupationSelecteds.containsObject(typeOccupationItem)) {
  			typeOccupationSelecteds.removeObject(typeOccupationItem);
  		}
  	}
  }
   
  /**
   * 
   */
  public void setNomUsuelSearch(String value) {
  	nomUsuelSearch = value;
    if (value != null) {
      value = "*" + value.toUpperCase() + "*";
      vOccupationsSuiviDg.queryBindings().setObjectForKey(value, EOVOccupationsSuivi.NOM_USUEL_KEY);
    }
    else {
    	vOccupationsSuiviDg.queryBindings().removeObjectForKey(EOVOccupationsSuivi.NOM_USUEL_KEY);
    }
  }
  
  /**
   * 
   */
  public void setPrenomSearch(String value) {
  	prenomSearch = value;
    if (value != null) {
      value = "*" + value.toUpperCase() + "*";
      vOccupationsSuiviDg.queryBindings().setObjectForKey(value, EOVOccupationsSuivi.PRENOM_KEY);
    }
    else {
    	vOccupationsSuiviDg.queryBindings().removeObjectForKey(EOVOccupationsSuivi.PRENOM_KEY);
    }
  }
  
  /**
   * 
   */
  public void setAttributeSelected(EOAttribute value) {
  	attributeSelected = value;
    if (vOccupationsSuiviDg != null) {
		  if (attributeSelected != null) {
		  	vOccupationsSuiviDg.setSortOrderings(new NSArray(EOSortOrdering.sortOrderingWithKey(attributeSelected.name(), EOSortOrdering.CompareAscending)));
		  }
		  else {
		  	vOccupationsSuiviDg.setSortOrderings(new NSArray());		    
		  }
    }
  }
  
  /**
   * 
   */
  public void setLigneParPageSelected(Integer value) {
  	ligneParPageSelected = value;
    if (vOccupationsSuiviDg != null) {
		  if (ligneParPageSelected != null) {
		  	vOccupationsSuiviDg.setNumberOfObjectsPerBatch(ligneParPageSelected);
		  }
		  else {
		  	vOccupationsSuiviDg.setNumberOfObjectsPerBatch(0); 
		  }
    }
  }


  // export CSV
	
	/**
	 * 
	 */
	public WOResponse printCsv() {
  	LRDataResponse resp = new LRDataResponse();
  	StringBuffer sb = new StringBuffer();
  	// configurer le DG pour que tout soit sur la même page
  	int prevNumberOfObjectsPerBatch = vOccupationsSuiviDg.numberOfObjectsPerBatch();
  	vOccupationsSuiviDg.setNumberOfObjectsPerBatch(0);
  	//
  	for (int i = 0; i < vOccupationsSuiviDg.displayedObjects().count(); i++) {
  		EOVOccupationsSuivi vOccupationsSuivi = (EOVOccupationsSuivi) vOccupationsSuiviDg.displayedObjects().objectAtIndex(i);

   		sb.append(vOccupationsSuivi.composante()).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
   		sb.append(vOccupationsSuivi.service()).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
   		sb.append(vOccupationsSuivi.nomUsuel()).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
   		sb.append(vOccupationsSuivi.prenom()).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
   		sb.append(DateCtrlConges.dateToString(vOccupationsSuivi.dteDebut())).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
   		sb.append(DateCtrlConges.dateToString(vOccupationsSuivi.dteFin())).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
   		String motif = "";
   		if (!StringCtrl.isEmpty(vOccupationsSuivi.motif())) {
   			motif = vOccupationsSuivi.motif();
   		}
   		sb.append(motif);
   	 
  		sb.append(ConstsPrint.CSV_NEW_LINE);
  		
		}
  	// remettre le DG comme précédemment
  	vOccupationsSuiviDg.setNumberOfObjectsPerBatch(prevNumberOfObjectsPerBatch);
  	NSData stream = new NSData(sb.toString(), ConstsPrint.CSV_ENCODING);
  	resp.setContent(stream);
		resp.setContentEncoding(ConstsPrint.CSV_ENCODING);		
  	resp.setHeader(String.valueOf(stream.length()), "Content-Length");
  	resp.setFileName("suivi_occupation.csv");
  	return resp;
	}
}