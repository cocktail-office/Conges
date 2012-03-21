package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSValidation;

import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.util.wo5.DateCtrl;

public class EOPreference extends _EOPreference {

    public EOPreference() {
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

    // methodes ajoutees
    
    /**
     * 
     */
		public static EOPreference createPreference(EOEditingContext ec,
				EOIndividu recIndividu) {
			return createPreference(ec, DateCtrl.now(), DateCtrl.now(), recIndividu);
		}

}
