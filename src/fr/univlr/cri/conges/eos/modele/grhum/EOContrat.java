package fr.univlr.cri.conges.eos.modele.grhum;

import com.webobjects.foundation.*;
import com.webobjects.eocontrol.*;

import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRSort;

public class EOContrat extends _EOContrat {

    public EOContrat() {
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
    
    private final static LRSort CONTRAT_SORT = LRSort.newSort(D_FIN_CONTRAT_TRAV_KEY);
    
		/**
		 * liste des contrats d'un individu pour une periode donnee
		 * @param ec
		 * @param individu
		 * @param date
		 * @return
		 */
		public static NSArray findContratForIndividuAndDateInContext(EOEditingContext ec, EOIndividu individu, NSTimestamp dateDebut, NSTimestamp dateFin) {
		
		  String strQual = null;
		  NSArray args = null;
		  if (dateFin != null) {
		    strQual = 
		    	TO_INDIVIDU_KEY + "=%@ AND (" +
		        "("+D_DEB_CONTRAT_TRAV_KEY+" <= %@ AND "+D_FIN_CONTRAT_TRAV_KEY+" = nil) OR" +    
		        "("+D_DEB_CONTRAT_TRAV_KEY+" >= %@ AND "+D_FIN_CONTRAT_TRAV_KEY+" <= %@) OR" +
		        "("+D_DEB_CONTRAT_TRAV_KEY+" <= %@ AND "+D_FIN_CONTRAT_TRAV_KEY+" >= %@) OR" +
		        "("+D_DEB_CONTRAT_TRAV_KEY+" <= %@ AND "+D_FIN_CONTRAT_TRAV_KEY+" >= %@ AND "+D_FIN_CONTRAT_TRAV_KEY+" <= %@) OR" +
		        "("+D_DEB_CONTRAT_TRAV_KEY+" >= %@ AND "+D_DEB_CONTRAT_TRAV_KEY+" <= %@ AND "+D_FIN_CONTRAT_TRAV_KEY+" >= %@) OR" +
		        "("+D_DEB_CONTRAT_TRAV_KEY+" >= %@ AND "+D_DEB_CONTRAT_TRAV_KEY+" <= %@ AND "+D_FIN_CONTRAT_TRAV_KEY+" = nil)" +
		      ")";
		    args = new NSArray(new Object[]{
		        individu, 
		        dateDebut, 
		        dateDebut, dateFin, 
		        dateDebut, dateFin, 
		        dateDebut, dateDebut, dateFin,
		        dateDebut, dateFin, dateFin,
		        dateDebut, dateFin});
		    } else {
		      strQual = 
		        "toIndividu = %@ AND (" +
		          "("+D_FIN_CONTRAT_TRAV_KEY+" = nil) OR" +
		          "("+D_DEB_CONTRAT_TRAV_KEY+" <= %@ AND "+D_FIN_CONTRAT_TRAV_KEY+" >= %@) OR" +
		          "("+D_DEB_CONTRAT_TRAV_KEY+" >= %@ AND "+D_FIN_CONTRAT_TRAV_KEY+" >= %@)" +
		        ")";
		      args = new NSArray(new Object[]{ individu, dateDebut, dateDebut, dateDebut, dateDebut});
		    }
		
		  return fetchContrats(ec, CRIDataBus.newCondition(strQual, args), CONTRAT_SORT);
		}
}
