package fr.univlr.cri.conges;

import com.webobjects.appserver.*;
import com.webobjects.eocontrol.EOAndQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

import fr.univlr.cri.conges.constantes.ConstsDroit;
import fr.univlr.cri.conges.eos.modele.conges.EODroit;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRSort;
import fr.univlr.cri.ycrifwk.utils.UtilDb;

/**
 * Page affichant la liste de tous les droits existants.
 * 
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */

public class PageAdminDroitsList extends YCRIWebPage {

	/** le display group contenant les enregistrement <code>EODroit</code> */
	public WODisplayGroup droitDg;
	
	/** un droit dans la liste */
	public EODroit droitItem;

	/** filtre par rapport au type de cible */
	public final NSArray TYPE_CIBLE_LIST = new NSArray(new String[]{
			ConstsDroit.DROIT_CIBLE_INDIVIDU, 
			ConstsDroit.DROIT_CIBLE_CHEF_DE_SERVICE, 
			ConstsDroit.DROIT_CIBLE_SERVICE});
	public String typeCibleItem;
	public String typeCibleSelected;
	
	/** filtre par rapport au niveau du droit accorde */
	public final NSArray NIVEAU_LIST = new NSArray(new Integer[]{
			ConstsDroit.DROIT_NIVEAU_VISU, 
			ConstsDroit.DROIT_NIVEAU_VISA, 
			ConstsDroit.DROIT_NIVEAU_VALIDATION, 
			ConstsDroit.DROIT_NIVEAU_DRH_COMPOSANTE, 
			ConstsDroit.DROIT_NIVEAU_ADM_COMPOSANTE, 
			ConstsDroit.DROIT_NIVEAU_DELEGATION});
	public Integer niveauItem;
	public Integer niveauSelected;
	
	/** les classement possibles du DG*/
	private final static NSArray ARRAY_SORT_TITULAIRE_ASC = 
		LRSort.newSort(EODroit.TO_INDIVIDU_RESP_KEY + "." + EOIndividu.NOM_KEY, 
				LRSort.Ascending);
	private final static NSArray ARRAY_SORT_TITULAIRE_DESC = 
		LRSort.newSort(EODroit.TO_INDIVIDU_RESP_KEY + "." + EOIndividu.NOM_KEY, 
				LRSort.Descending);
	private final static NSArray ARRAY_SORT_CIBLE_ASC = 
		LRSort.newSort(EODroit.TO_INDIVIDU_KEY + "." + EOIndividu.NOM_KEY + "," + EODroit.TO_STRUCTURE_KEY + "." + EOStructure.DISPLAY_KEY, 
				LRSort.Ascending);
	private final static NSArray ARRAY_SORT_CIBLE_DESC = 
			LRSort.newSort(EODroit.TO_INDIVIDU_KEY + "." + EOIndividu.NOM_KEY + "," + EODroit.TO_STRUCTURE_KEY + "." + EOStructure.DISPLAY_KEY, 
					LRSort.Descending);
	private final static NSArray ARRAY_SORT_NIVEAU_ASC = 
		LRSort.newSort(EODroit.CDR_NIVEAU_KEY, 
				LRSort.Ascending);
	private final static NSArray ARRAY_SORT_NIVEAU_DESC = 
		LRSort.newSort(EODroit.CDR_NIVEAU_KEY, 
				LRSort.Descending);
	
	/** le classement en cours */
	private NSArray arraySort;
	
	/** la liste des droits selectionnés pour suppression */
	public NSMutableArray droitListSelected;
	
	// recherche
	public String titulaire;
	public String cible;
	
	public PageAdminDroitsList(WOContext context) {
		super(context);
		initComponent();
	}
	
	/**
	 * 
	 */
	private void initComponent() {
		droitListSelected = new NSMutableArray();
		// classement par defaut
		goSortTitulaire();
	}
	
	public void appendToResponse(WOResponse arg0, WOContext arg1) {
		// appliquer les filtres et classements
		NSArray arrayQualFilter = new NSArray();
		if (!StringCtrl.isEmpty(typeCibleSelected)) {
			arrayQualFilter = arrayQualFilter.arrayByAddingObject(
					CRIDataBus.newCondition(EODroit.CDR_TYPE_KEY+"=%@", new NSArray(typeCibleSelected)));
		}
		if (niveauSelected != null) {
			arrayQualFilter = arrayQualFilter.arrayByAddingObject(
					CRIDataBus.newCondition(EODroit.CDR_NIVEAU_KEY+"=%@", new NSArray(niveauSelected)));
		}
		if (!StringCtrl.isEmpty(titulaire)) {
			arrayQualFilter = arrayQualFilter.arrayByAddingObject(
					CRIDataBus.newCondition(
							EOIndividu.getStringQualifierIndividuForNomPrenom(EODroit.TO_INDIVIDU_RESP_KEY, titulaire), null));
		}
		if (!StringCtrl.isEmpty(cible)) {
			arrayQualFilter = arrayQualFilter.arrayByAddingObject(
					CRIDataBus.newCondition(EODroit.DROIT_CIBLE_LIBELLE_KEY+" caseInsensitiveLike '*" + cible+ "*'", null));
		}
		droitDg.setQualifier(new EOAndQualifier(arrayQualFilter));
		droitDg.setSortOrderings(arraySort);
		droitDg.updateDisplayedObjects();  // qual & sort
		super.appendToResponse(arg0, arg1);
	}
	
	
	/**
	 * Affiche d'un element de type de cible dans la combobox
	 */
	public String typeCibleItemDisplay() {
		if (typeCibleItem.equals(ConstsDroit.DROIT_CIBLE_INDIVIDU)) {
			return ConstsDroit.DROIT_CIBLE_INDIVIDU_LABEL;
		} else if (typeCibleItem.equals(ConstsDroit.DROIT_CIBLE_CHEF_DE_SERVICE)) {
			return ConstsDroit.DROIT_CIBLE_CHEF_DE_SERVICE_LABEL;
		} else if (typeCibleItem.equals(ConstsDroit.DROIT_CIBLE_SERVICE)) {
			return ConstsDroit.DROIT_CIBLE_SERVICE_LABEL;
		}
		return "** erreur **";
	}
	
	/**
	 * Affiche d'un element de niveau de droiit dans la combobox
	 */
	public String niveauItemDisplay() {
		String disp = "** erreur **";
		if (niveauItem == ConstsDroit.DROIT_NIVEAU_VISU) {
			disp = ConstsDroit.DROIT_NIVEAU_VISU_LABEL;
		} else if (niveauItem == ConstsDroit.DROIT_NIVEAU_VISA) {
			disp = ConstsDroit.DROIT_NIVEAU_VISA_LABEL;
		} else if (niveauItem == ConstsDroit.DROIT_NIVEAU_VALIDATION) {
			disp = ConstsDroit.DROIT_NIVEAU_VALIDATION_LABEL;
		} else if (niveauItem == ConstsDroit.DROIT_NIVEAU_DRH_COMPOSANTE) {
			disp = ConstsDroit.DROIT_NIVEAU_DRH_COMPOSANTE_LABEL;
		} else if (niveauItem == ConstsDroit.DROIT_NIVEAU_ADM_COMPOSANTE) {
			disp = ConstsDroit.DROIT_NIVEAU_ADM_COMPOSANTE_LABEL;
		} else if (niveauItem == ConstsDroit.DROIT_NIVEAU_DELEGATION) {
			disp = ConstsDroit.DROIT_NIVEAU_DELEGATION_LABEL;
		}
		return niveauItem + " - " + disp;
	}
	
	/**
	 * Forcer le classement par le libelle de la cible
	 */
	public WOComponent goSortTitulaire() {
		if (arraySort != null && arraySort.equals(ARRAY_SORT_TITULAIRE_ASC)) {
			arraySort = ARRAY_SORT_TITULAIRE_DESC;
		} else {
			arraySort = ARRAY_SORT_TITULAIRE_ASC;
		}
		return null;
	}
	
	/**
	 * Forcer le classement par le libelle de la cible
	 */
	public WOComponent goSortCible() {
		if (arraySort != null && arraySort.equals(ARRAY_SORT_CIBLE_ASC)) {
			arraySort = ARRAY_SORT_CIBLE_DESC;
		} else {
			arraySort = ARRAY_SORT_CIBLE_ASC;
		}
		return null;
	}
	
	/**
	 * Forcer le classement par le libelle de la niveau
	 */
	public WOComponent goSortNiveau() {
		if (arraySort != null && arraySort.equals(ARRAY_SORT_NIVEAU_ASC)) {
			arraySort = ARRAY_SORT_NIVEAU_DESC;
		} else {
			arraySort = ARRAY_SORT_NIVEAU_ASC;
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getIsDroitItemChecked() {
		return droitListSelected.containsObject(droitItem);
	}
	
	/**
	 * 
	 * @return
	 */
	public void setIsDroitItemChecked(boolean value) {
		if (value == true) {
			if (!droitListSelected.containsObject(droitItem)) {
				droitListSelected.addObject(droitItem);
			}
		} else {
			if (droitListSelected.containsObject(droitItem)) {
				droitListSelected.removeObject(droitItem);
			}
		}
	}
	
	/**
	 * Suppression de l'ensemble des droits selectionnés {@link #droitListSelected}
	 * @return
	 * @throws Throwable 
	 */
	public WOComponent doDeleteSelectedDroitList() throws Throwable {
		if (droitListSelected.count() > 0) {
			for (int i = 0; i < droitListSelected.count(); i++) {
				EODroit droit = (EODroit) droitListSelected.objectAtIndex(i);
				edc.deleteObject(droit);
			}
			UtilDb.save(edc, true);
			droitListSelected = new NSMutableArray();
		}
		return null;
	}
}