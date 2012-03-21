package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.databus.CngPlanningBus;
import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;

/**
 * Methodes communes aux pages effectuant la creation
 * d'enregistrement d'affectations annuelles.
 * 
 * @author ctarade
 */
public abstract class A_PageAdminCreationAffAnn 
	extends A_ComponentAnneeUniv {

  /** indique le nombre total d'affectation annuelles creees lors de la derniere operation */
  public int lastTotalAffAnn;
  /** indique le nombre total de periodes d'affectation annuelles creees lors de la derniere operation */
  public int lastTotalPeriode;
  /** indique le nombre total de periodes d'affectation annuelles creees lors de la derniere operation */
  public String lastMessage;
  /** on affiche le total apres une operation, mais ensuite on masque le message */
  public boolean shouldShowLastTotalAffAnn = false;

  /** la liste des services autorises pour l'annee universitaire selectionnee */
	public NSArray lesServicesAutorises;

  public A_PageAdminCreationAffAnn(WOContext context) {
		super(context);
 }	

  /**
   * Gestion de l'affichage et masquage du message du nombre total
   * d'affectation annuelles creees
   */
  public void appendToResponse(WOResponse arg0, WOContext arg1) {
  	super.appendToResponse(arg0, arg1);
  	if (shouldShowLastTotalAffAnn) {
    	shouldShowLastTotalAffAnn = false;
  	}
  }
 
  /**
   * changement de la selection de l'annee universitaire : traitement a effectuer
   */
  public abstract void onChangeAnneUnivSelectionnee();
  

  public NSArray lesServicesAutorises() {
    if (lesServicesAutorises == null) {
      if (lAnneeUnivSelectionnee != null) {
        lesServicesAutorises = EOStructure.servicesAutorisesInContext(edc, selectedAnneeUniv);
        // classement alpha
        EOSortOrdering libelleLongOrdering = EOSortOrdering.sortOrderingWithKey(
        		EOStructure.LIBELLE_LONG_KEY, EOSortOrdering.CompareAscending);
        lesServicesAutorises = EOSortOrdering.sortedArrayUsingKeyOrderArray(
        		lesServicesAutorises, new NSArray(libelleLongOrdering));
      }
    }
    return lesServicesAutorises;
  }
  
	
  
  // ** les bus de donnees **
  
  protected CngPlanningBus planningBus() {
    return dataCenter().planningBus();
  }
}
