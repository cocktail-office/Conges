package fr.univlr.cri.conges.eos.modele.grhum;

import com.webobjects.foundation.NSValidation;

import fr.univlr.cri.webapp.LRSort;

public class EOVPersonnelActuel extends _EOVPersonnelActuel {

    public EOVPersonnelActuel() {
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
    
    public final static LRSort V_PERSONNEL_SORT = LRSort.newSort(
    		INDIVIDU_KEY + "." + EOIndividu.NOM_KEY + "," + INDIVIDU_KEY + "." + EOIndividu.PRENOM_KEY);

}
