// EOTypeOccupation.java
// Created on Thu Jun 17 08:48:38  2004 by Apple EOModeler Version 5.2

package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.constantes.ConstsTypeOccupation;
import fr.univlr.cri.conges.objects.occupations.CongesCompensateurs;
import fr.univlr.cri.conges.objects.occupations.CongespourCet;
import fr.univlr.cri.conges.objects.occupations.DechargeSyndicale;
import fr.univlr.cri.conges.objects.occupations.HeuresSupplementaires;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRSort;

public class EOTypeOccupation extends _EOTypeOccupation implements ConstsTypeOccupation{

  public static NSArray ARRAY_SORT = LRSort.newSort(LIBELLE_KEY);
  
  public final static String LIBELLE_COURT_FERMETURE = "C_PFD";
  
  public EOTypeOccupation() {
    super();
  }

 
  // methodes rajoutees

  public boolean isCongeLegal() {
    return congeLegal() != null && congeLegal().intValue() == 1;
  }

  /** typage des absences DRH */
  public boolean isCongeDRH() {
    return flagDRH() != null && flagDRH().intValue() >= FLAG_DRH_GLOBAL.intValue();
  }
  
  /** typage des absences DRH Composantes = validable par les DRH Locaux */
  public boolean isCongeDRHComposante() {
    return flagDRH() != null && flagDRH().intValue() == FLAG_DRH_COMPOSANTE.intValue();
  }
  
  /** typage des absences DRH Composantes = validable par les DRH globaux */
  public boolean isCongeDRHGlobal() {
    return flagDRH() != null && flagDRH().intValue() == FLAG_DRH_GLOBAL.intValue();
  }

  /*public boolean isHoraireForce() {
    return horaireForce() != null && horaireForce().intValue() == 1;
  }*/
  
  
  /**
   * Indique si un horaire doit être forcé sur la période indiquée.
   * Si un seule partie de la période est forcé, alors l'horaire
   * est considéré comme à forcé sur toute l'occupation
   */
  public boolean isHoraireForce(
  		NSTimestamp dDebut, NSTimestamp dFin) {
  	boolean isHoraireForce = false;
  	
  	EOQualifier qual = EOTypeOccupationParametre.getQualifierHoraireForcePourPeriode(dDebut, dFin);
  	if (tosTypeOccupationParametre(qual).count() > 0) {
  		isHoraireForce = true;
  	}
  	
  	return isHoraireForce;
  }
  
  public boolean isOccupationMinute() {
    return !StringCtrl.isEmpty(quantum()) && CODE_QUANTUM_MINUTE.equals(quantum());
  }

  public boolean isOccupationJour() {
    return !StringCtrl.isEmpty(quantum()) && CODE_QUANTUM_JOUR.equals(quantum());
  }

  public boolean isAbsenceBilan() {
    return isHeuresSupplementaires() || isCongesCompensateurs();
  }
  
  public boolean isHeuresSupplementaires() {
    return libelleCourt().equals(HeuresSupplementaires.LIBELLE_COURT) ;
  }

  public boolean isCongesCompensateurs() {
    return libelleCourt().equals(CongesCompensateurs.LIBELLE_COURT);
  }
  
  public boolean isFermeture() {
    return libelleCourt().equals(EOTypeOccupation.LIBELLE_COURT_FERMETURE);
  }
  
  public boolean isAbsenceCET() {
    return libelleCourt().equals(CongespourCet.LIBELLE_COURT);
  }
  
  public boolean isDechargeSyndicale() {
    return libelleCourt().equals(DechargeSyndicale.LIBELLE_COURT_JOUR) || libelleCourt().equals(DechargeSyndicale.LIBELLE_COURT_MINUTE);
  }


	public static NSArray findTypesOccupationsEditablesInContext(EOEditingContext ec) {
		
		
		/*
		
		NSArray occupationsEditables = EOUtilities.objectsWithFetchSpecificationAndBindings(
				ec, EOTypeOccupation.ENTITY_NAME, "OccupationsEditables", null);*/
		NSMutableArray occupationsEditables = new NSMutableArray();
		
		//TODO le temps de remettre la fetch spec fonctionnelle dans le modele
		NSMutableArray occupationsEditablesMutable = new NSMutableArray(
				EOUtilities.objectsWithFetchSpecificationAndBindings(
						ec, EOTypeOccupation.ENTITY_NAME, "OccupationsToutes", null));
		
		// oter celles les conges légaux, les horaires forcé et les fermetures
		for (int i=0; i<occupationsEditablesMutable.count(); i++) {
			EOTypeOccupation typOcc = (EOTypeOccupation) occupationsEditablesMutable.objectAtIndex(i);
			if (typOcc.congeLegal().intValue() == 0 &&
					typOcc.tosTypeOccupationParametre().count() == 0 &&
					!typOcc.isFermeture()) {
				occupationsEditables.addObject(typOcc);
			}
		}
		
		
		return occupationsEditables.immutableClone();
	}


	/**
	 * @param libelleCourt
	 * @return
	 */
	public static EOTypeOccupation getTypeOccupationForLibelleCourtInContext(EOEditingContext edc, String libelleCourt) {
		return EOTypeOccupation.fetchTypeOccupation(edc, 
				CRIDataBus.newCondition(LIBELLE_COURT_KEY+"='"+libelleCourt+"'"));
	}
	
	/**
	 * Filtrer des types d'occupation selon leur caractéristiques
	 * @param typeOccupationList
	 * @param isOccupationMinute
	 * @param isOccupationDRH
	 * @return
	 */
	public static NSArray filterTypeOccupation(NSArray typeOccupationList, boolean isOccupationMinute, boolean isOccupationDRH) {
		EOQualifier qual = null;
		if (!isOccupationMinute && !isOccupationDRH) {
			qual = CRIDataBus.newCondition(EOTypeOccupation.QUANTUM_KEY +"=%@ and "+EOTypeOccupation.FLAG_DRH_KEY+"=%@", new NSArray(new Object[] {
	    		ConstsTypeOccupation.CODE_QUANTUM_JOUR, ConstsTypeOccupation.FLAG_NON_DRH}));
		} else if (isOccupationMinute && !isOccupationDRH) {
			qual = CRIDataBus.newCondition(EOTypeOccupation.QUANTUM_KEY +"=%@ and "+EOTypeOccupation.FLAG_DRH_KEY+"=%@", new NSArray(new Object[] {
	    		ConstsTypeOccupation.CODE_QUANTUM_MINUTE, ConstsTypeOccupation.FLAG_NON_DRH}));
		} else if (!isOccupationMinute && isOccupationDRH) {
			qual = CRIDataBus.newCondition(EOTypeOccupation.QUANTUM_KEY +"=%@ and "+EOTypeOccupation.FLAG_DRH_KEY+"<>%@", new NSArray(new Object[] {
	    		ConstsTypeOccupation.CODE_QUANTUM_JOUR, ConstsTypeOccupation.FLAG_NON_DRH}));
		} else if (isOccupationMinute && isOccupationDRH) {
			qual = CRIDataBus.newCondition(EOTypeOccupation.QUANTUM_KEY +"=%@ and "+EOTypeOccupation.FLAG_DRH_KEY+"<>%@", new NSArray(new Object[] {
	    		ConstsTypeOccupation.CODE_QUANTUM_MINUTE, ConstsTypeOccupation.FLAG_NON_DRH}));
		} 
		return EOQualifier.filteredArrayWithQualifier(typeOccupationList, qual);
	}
}
