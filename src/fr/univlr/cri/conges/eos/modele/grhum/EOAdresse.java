package fr.univlr.cri.conges.eos.modele.grhum;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSValidation;

public class EOAdresse extends _EOAdresse {

  public EOAdresse() {
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
    
  /**
   * Récupérer l'adresse de l'établissement racine
   */
  public static EOAdresse getAdresseRacineInContext(EOEditingContext ec) {
  	EOAdresse adresse = null;

  	EOStructure structureRacine = EOStructure.getStructureRacineInContext(ec);
		NSArray adresses = structureRacine.tosAdresse();
		if (adresses.count() > 0) {
			adresse = (EOAdresse) adresses.lastObject();
		}
		
		return adresse;
  }

}
