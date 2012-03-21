// EODroit.java
// Created on Wed Sep 22 06:44:45  2004 by Apple EOModeler Version 5.2

package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.foundation.NSValidation;

import fr.univlr.cri.conges.constantes.ConstsDroit;

public class EODroit 
	extends _EODroit {

  public EODroit() {
    super();
	}
	
	public void validateForInsert() throws NSValidation.ValidationException {
	    this.validateObjectMetier();
	    validateBeforeTransactionSave();
	    super.validateForInsert();
	}
	
	public void validateForUpdate() throws NSValidation.ValidationException {
	    this.validateObjectMetier();
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
	  
	
	}
	
	/**
	 * A appeler par les validateforsave, forinsert, forupdate.
	 *
	 */
	private final void validateBeforeTransactionSave() throws NSValidation.ValidationException {
	       
	}
	
	
	// ajouts 
	
	
	public final static String DROIT_CIBLE_LIBELLE_KEY = "droitCibleLibelle";

	/**
	 * Le libelle de la cible du droit. 
	 * Depend du type de droit
	 */
	public String droitCibleLibelle() {
		String libelle = "** inconnu **";		
		
		if (cdrType().equals(ConstsDroit.DROIT_CIBLE_SERVICE)) {
			libelle = toStructure().display();
		} else if (cdrType().equals(ConstsDroit.DROIT_CIBLE_CHEF_DE_SERVICE)) { 
			libelle = toStructure().display() + 
			" ("+ 
			(toStructure().responsable() != null ? 
					toStructure().responsable().nomComplet() : "aucun")+
			" )";
		} else if (cdrType().equals(ConstsDroit.DROIT_CIBLE_INDIVIDU)) { 
			libelle = toIndividu().nomComplet();
		}
		return libelle;
	}

	/**
	 * Le libelle du type de la cible
	 * @return
	 */
	public String droitTypeCibleLibelle() {
		String libelle = "** inconnu **";
		
		if (cdrType().equals(ConstsDroit.DROIT_CIBLE_SERVICE)) {
			libelle = ConstsDroit.DROIT_CIBLE_SERVICE_LABEL;
		} else if (cdrType().equals(ConstsDroit.DROIT_CIBLE_CHEF_DE_SERVICE)) { 
			libelle = ConstsDroit.DROIT_CIBLE_CHEF_DE_SERVICE_LABEL;
		} else if (cdrType().equals(ConstsDroit.DROIT_CIBLE_INDIVIDU)) { 
			libelle = ConstsDroit.DROIT_CIBLE_INDIVIDU_LABEL;
		}
		
		return libelle;
	}
}
