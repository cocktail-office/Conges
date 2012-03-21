package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;
import com.webobjects.foundation.NSValidation;

import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.webapp.CRIDataBus;

public class EOTypeOccupationParametre extends _EOTypeOccupationParametre {

    public EOTypeOccupationParametre() {
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
      
    	if (dDebValidite() != null && 
    			dFinValidite() != null &&
    			DateCtrlConges.isAfter(dDebValidite(), dFinValidite())) {
    		throw new NSValidation.ValidationException(
    				"La date de début doit être avant la date de fin");
    	}

    	//TODO controles de chevauchement 
    }
    
    /**
     * A appeler par les validateforsave, forinsert, forupdate.
     *
     */
    private final void validateBeforeTransactionSave() throws NSValidation.ValidationException {
           
    }

    
    // ajouts
    
    private final static String CONTRAINTE_HORAIRE_FORCE_VALUE = "HORAIRE_FORCE";
    
    private final static String STR_QUAL_HORAIRE_FORCE = 
    		CONTRAINTE_KEY + "='" + CONTRAINTE_HORAIRE_FORCE_VALUE + "' and " +
    		"(" + D_DEB_VALIDITE_KEY + "=nil and " + D_FIN_VALIDITE_KEY + "=nil) or "+
    		"(" + D_FIN_VALIDITE_KEY + ">=%@ and " + D_FIN_VALIDITE_KEY + "<=%@) or " +
    		"(" + D_DEB_VALIDITE_KEY + ">=%@ and " + D_DEB_VALIDITE_KEY + "<=%@) or " +
    		"(" + D_DEB_VALIDITE_KEY + "<=%@ and " + D_FIN_VALIDITE_KEY + "=nil) or " +
    		"(" + D_DEB_VALIDITE_KEY + "=nil and " + D_FIN_VALIDITE_KEY + ">=%@) or " +
    		"(" + D_DEB_VALIDITE_KEY + "<=%@ and " + D_FIN_VALIDITE_KEY + ">=%@) or " +
    		"(" + D_DEB_VALIDITE_KEY + ">=%@ and " + D_FIN_VALIDITE_KEY + "<=%@)";
		
    /** 
     * Le qualifier pour connaitre les horaires forcés sur une période 
     */
    public final static EOQualifier getQualifierHoraireForcePourPeriode(
    		NSTimestamp dDebut, NSTimestamp dFin) {
    	EOQualifier qual = null;
    	
    	NSArray args = new NSArray(
    			new NSTimestamp[] {
    					dDebut, dFin,
    					dDebut, dFin,
    					dDebut, dFin,
    					dDebut, dFin,
    					dDebut, dFin});
    	
    	qual = CRIDataBus.newCondition(STR_QUAL_HORAIRE_FORCE, args);
    	
    	return qual;
    }
    	
    /**
     * 
     */
    public String toString() {
    	String toString = "";
    	
    	if (dDebValidite() == null && dFinValidite() == null) {
    		toString = "permanent";
    	} else {
    		if (dDebValidite() != null && dFinValidite() != null) {
    			toString += 
    				"du " + DateCtrlConges.dateToString(dDebValidite()) + " au " + DateCtrlConges.dateToString(dFinValidite());
    		} else if (dDebValidite() != null && dFinValidite() == null) {
     			toString += 
    				"à partir du " + DateCtrlConges.dateToString(dDebValidite());
    		} else if (dDebValidite() == null && dFinValidite() != null) {
     			toString += 
    				"jusqu'au " + DateCtrlConges.dateToString(dFinValidite());
    		}
    	}
    	
    	return toString;
    }
    
    /**
     * 
     * @param ec
     * @param dDebut
     * @param dFin
     * @param toTypeOccupation
     * @return
     */
    public static EOTypeOccupationParametre nouvelleContrainteForcee(
    		EOEditingContext ec, NSTimestamp dDebut, NSTimestamp dFin, EOTypeOccupation toTypeOccupation) {
    	NSTimestamp now = DateCtrlConges.now();
    	EOTypeOccupationParametre rec = createTypeOccupationParametre(
    			ec, CONTRAINTE_HORAIRE_FORCE_VALUE, now, now, toTypeOccupation);
    	rec.setDDebValidite(dDebut);
    	rec.setDFinValidite(dFin);
    	return rec;
    }
}
