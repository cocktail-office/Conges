package fr.univlr.cri.conges;
/*
 * Copyright CRI - Universite de La Rochelle, 1995-2004 
 * 
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software. You can use, 
 * modify and/or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 * 
 * As a counterpart to the access to the source code and rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty and the software's author, the holder of the
 * economic rights, and the successive licensors have only limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading, using, modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean that it is complicated to manipulate, and that also
 * therefore means that it is reserved for developers and experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and, more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableDictionary;

import fr.univlr.cri.conges.YCRIWebPage;
import fr.univlr.cri.conges.eos.modele.planning.EOTypeHoraire;
import fr.univlr.cri.conges.eos.modele.planning.I_ClasseMetierNotificationParametre;
import fr.univlr.cri.conges.objects.Parametre;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.webext.*;
import fr.univlr.cri.ycrifwk.utils.UtilDb;

public class PageAdminHoraires 
	extends YCRIWebPage
		implements I_ClasseMetierNotificationParametre {

	public EOTypeHoraire unTypeHoraire;
	public NSArray lesTypesHorairesSelectionnes;
	public boolean isModeCreation;
	public boolean isHorsNormeNewHoraire;
	public boolean isSemHauteNewHoraire;
	public String totalNewHoraire;   
	public static boolean appLockHorairesTypes = Parametre.PARAM_LOCK_HORAIRES_TYPES.getParamValueBoolean().booleanValue();
    
    public PageAdminHoraires(WOContext context) {
        super(context);
    }
    
    /**
     * @see I_ClasseMetierNotificationParametre
     * @param parametre
     */
    public static void initStaticField(
    		Parametre parametre) {
    	if (parametre == Parametre.PARAM_LOCK_HORAIRES_TYPES) {
    		appLockHorairesTypes = parametre.getParamValueBoolean().booleanValue();
    	} 
    }
	
    public void reset() {
        isModeCreation = false;
        isHorsNormeNewHoraire = false;
        isSemHauteNewHoraire = false;
        totalNewHoraire = TimeCtrl.stringForMinutes(0);
    }
    
	public NSArray lesTypesHoraires() {
	    NSArray lesTypesHoraires = new NSArray();
	    EOEditingContext edc = laSession.ec();
		NSMutableDictionary bindings = new NSMutableDictionary();
		bindings.setObjectForKey(new Integer(0), "horsNormes");
		lesTypesHoraires = lesTypesHoraires.arrayByAddingObjectsFromArray(EOUtilities.objectsWithFetchSpecificationAndBindings(edc, "TypeHoraire", "TypesHoraireNormaux", bindings));
		bindings.setObjectForKey(new Integer(1), "horsNormes");
		lesTypesHoraires = lesTypesHoraires.arrayByAddingObjectsFromArray(EOUtilities.objectsWithFetchSpecificationAndBindings(edc, "TypeHoraire", "TypesHoraireNormaux", bindings));
		NSArray orderings = new NSArray(EOSortOrdering.sortOrderingWithKey("total", EOSortOrdering.CompareAscending));
		return EOSortOrdering.sortedArrayUsingKeyOrderArray(lesTypesHoraires, orderings);		
	}

	public EOTypeHoraire unTypeHoraireSelectionne() {
	    if (lesTypesHorairesSelectionnes != null && lesTypesHorairesSelectionnes.count() > 0)
	        return (EOTypeHoraire)lesTypesHorairesSelectionnes.objectAtIndex(0);
	    else
	        return null;
	}
	
	// METHODES DE DISPLAY
	/**
	 * affichage de d�tail de l'horaire dans le popup
	 */
	public String displayUnTypeHoraire() {
	    return unTypeHoraire.dureeHeures();
	}
	
	/**
	 * Affichage de la conf pour APP_LOCK_HORAIRES_TYPES
	 * @return
	 */
	public String strParamAppLockHorairesTypes() {
		return "LOCK_HORAIRES_TYPES=" + (appLockHorairesTypes ? "YES" : "NO");
	}
	
	// METHODES DE NAVIGATION
	public WOComponent ajouter() {
	    isModeCreation = true;
	    return null;
	}
	
	public WOComponent enregistrer() throws Throwable {
	    if (isModeCreation) {
	        EOTypeHoraire typeHoraire = EOTypeHoraire.newDefaultRecordInContext(edc);
	        int flagHorsNorme =  0;
	        if (isHorsNormeNewHoraire) {
	            flagHorsNorme = 1;
	        }
	        int flagHoraireSemaineHaute = 0;
	        if (isSemHauteNewHoraire) {
	            flagHoraireSemaineHaute = 1;
	        }
	        typeHoraire.setFlagHorsNorme(new Integer(flagHorsNorme));
	        typeHoraire.setFlagHoraireSemaineHaute(new Integer(flagHoraireSemaineHaute));
	        typeHoraire.setTotal(new Integer(TimeCtrl.getMinutes(totalNewHoraire)));
	    }
	    UtilDb.save(edc, true);

	    reset();
	    return null;
	}
	
	public WOComponent annuler() {
	    edc.revert();
	    reset();
	    return null;
	}
	
	public WOComponent supprimer() throws Throwable {
	
		int nbHoraires = laSession.cngDataCenter().horaireBus().horairesForHoraireType(
            edc, unTypeHoraireSelectionne()).count();
		
		if (nbHoraires > 0) {
			// interdiction de suppresion de l'horaire type si des horaires associ�s 
			  CRIAlertPage page = (CRIAlertPage)criApp.pageWithName(CRIAlertPage.class.getName(), context());
			  page.showMessage(
			          laSession.getSavedPageWithName(PageAdministration.class.getName()),
			          "Suppression impossible",
			          "Vous ne pouvez pas supprimer cet horaire type ...<br><br>" +
			          "Il existe <b>" + nbHoraires + " horaires</b> reli&eacute;s &agrave; cet horaire type !<br><br>",
			          "Retour", 
			          null, 
			          null, 
			          CRIAlertPage.ERROR, 
			          null
			  	);
			  return page;
		}
		else {
		    edc.deleteObject(unTypeHoraireSelectionne());
		    UtilDb.save(edc, true);

			return null;
		}
	}
	


}