package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;
import com.webobjects.foundation.NSValidation;

import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;
import fr.univlr.cri.util.wo5.DateCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRRecord;
import fr.univlr.cri.ycrifwk.utils.UtilDb;

public class EOAnnulationPeriodeFermeture extends _EOAnnulationPeriodeFermeture {

    public EOAnnulationPeriodeFermeture() {
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
    	// une annulation doit etre sur une cible maximum
    	if ((toAffectationAnnuelle() != null && toStructure() != null) ||
    			(toAffectationAnnuelle() != null && toComposante() != null) ||
    			(toStructure() != null && toComposante() != null)) {
    		throw new NSValidation.ValidationException(
    				"Une fermeture ne peut être partagé par 2 types d'objets suivants : planning, service et/ou composante");
    	}
    	// une annulation ne peut être globale
    	if (toAffectationAnnuelle() == null && toStructure() == null && toComposante() == null) {
    		throw new NSValidation.ValidationException(
    			"Une fermeture ne peut pas être annulée globalement.");
    	}
    }
    
    /**
     * A appeler par les validateforsave, forinsert, forupdate.
     *
     */
    private final void validateBeforeTransactionSave() throws NSValidation.ValidationException {
           
    }

    
    // methodes ajoutees
    
    /**
		 * liste des fermetures originales qui ont ete supprimees d'un service
		 * @param ec
		 * @param unService
		 * @param dateDebutAnnee
		 * @param dateFinAnnee
		 * @return
		 */
		static NSArray findPeriodesFermeturesAnnuleesForService(EOEditingContext ec, EOStructure unService) {
		  EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(TO_STRUCTURE_KEY + " = %@", new NSArray(unService));
		  return (NSArray) UtilDb.fetchArray(ec, ENTITY_NAME, qual, null).valueForKey(TO_PERIODE_FERMETURE_KEY);
		}

		/**
     * retourne vrai si une fermeture a deja ete annulee pour une affectation annuelle
     * @param fermeture
     * @param affectation
     * @return
     */
    public static boolean isFermetureAnnuleeForAffectationAnnuelle(EOPeriodeFermeture fermeture, EOAffectationAnnuelle affectation) {
        NSArray fermetures = (NSArray) affectation.annulationPeriodeFermetures().valueForKey(TO_PERIODE_FERMETURE_KEY);
        return fermetures.containsObject(fermeture);
    }
    
    /**
     * retourne vrai si une fermeture a deja ete annulee pour un service
     * @param fermeture
     * @param affectation
     * @return
     */
    public static boolean isFermetureAnnuleeForService(EOPeriodeFermeture fermeture, EOStructure service) {
      EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(TO_PERIODE_FERMETURE_KEY+" =%@ AND "+TO_STRUCTURE_KEY+" = %@",
          new NSArray(new Object[] {fermeture, service}));      
      return UtilDb.fetchArray(fermeture.editingContext(), ENTITY_NAME, qual, null).count() > 0;
    }
   
    public static EOAnnulationPeriodeFermeture findAnnulationFermetureForAffectationAnnuelleInContext(EOEditingContext ec, EOPeriodeFermeture fermeture, EOAffectationAnnuelle affAnn) {
      EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(TO_PERIODE_FERMETURE_KEY+" =%@ AND "+TO_AFFECTATION_ANNUELLE_KEY+" = %@",
          new NSArray(new Object[] {fermeture, affAnn}));      
      NSArray records = UtilDb.fetchArray(fermeture.editingContext(), ENTITY_NAME, qual, null);
      EOAnnulationPeriodeFermeture record = null;
      if (records.count() > 0) {
        record = (EOAnnulationPeriodeFermeture) records.lastObject();
      }
      return record;
    }
    
    public static EOAnnulationPeriodeFermeture findAnnulationFermetureForServiceInContext(EOEditingContext ec, EOPeriodeFermeture fermeture, EOStructure service) {
      EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(TO_PERIODE_FERMETURE_KEY+" =%@ AND "+TO_STRUCTURE_KEY+" = %@",
          new NSArray(new Object[] {fermeture, service}));      
      NSArray records = UtilDb.fetchArray(fermeture.editingContext(), ENTITY_NAME, qual, null);
      EOAnnulationPeriodeFermeture record = null;
      if (records.count() > 0) {
        record = (EOAnnulationPeriodeFermeture) records.lastObject();
      }
      return record;
    }
    
    
    public static NSArray findPeriodesFermeturesSupprimeesForService(EOEditingContext ec, 
        EOStructure unService, NSTimestamp dateDebutAnnee, NSTimestamp dateFinAnnee) {
      EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(
      		TO_STRUCTURE_KEY + " = %@ AND " +
      		TO_PERIODE_FERMETURE_KEY +"."+_EOPeriodeFermeture.DATE_DEBUT_KEY + " >= %@ AND " +
      		TO_PERIODE_FERMETURE_KEY +"."+_EOPeriodeFermeture.DATE_FIN_KEY + " <= %@", 
          new NSArray(new Object[] {unService, dateDebutAnnee, dateFinAnnee}));
  		NSArray orderDate = new NSArray(EOSortOrdering.sortOrderingWithKey(
  				TO_PERIODE_FERMETURE_KEY + "." + _EOPeriodeFermeture.DATE_DEBUT_KEY, EOSortOrdering.CompareAscending));
      return (NSArray) UtilDb.fetchArray(ec, ENTITY_NAME, qual, orderDate).valueForKey(TO_PERIODE_FERMETURE_KEY);
     }
    
    //

    /**
     * Un raccourci vers le construction sans avoir a gerer les dates de creation / modification
     */
    public static EOAnnulationPeriodeFermeture createAnnulationPeriodeFermeture(
    		EOEditingContext editingContext, EOPeriodeFermeture toPeriodeFermeture) {
      EOAnnulationPeriodeFermeture eo = createAnnulationPeriodeFermeture(
      		editingContext, DateCtrl.now(),  DateCtrl.now(), toPeriodeFermeture);
      return eo;
    }
    
    /**
     * Trouve l'enregistrement
     * @param ec
     * @param periodeFermeture
     * @param composante
     * @return
     */
    public static EOAnnulationPeriodeFermeture findAnnulationPeriodeFermetureForFermetureAndComposante(
    		EOEditingContext ec, EOPeriodeFermeture periodeFermeture, EOStructure composante) {
    	return fetchAnnulationPeriodeFermeture(
    			ec,
    			CRIDataBus.newCondition(
    					TO_PERIODE_FERMETURE_KEY +"=%@ and " + TO_COMPOSANTE_KEY + "=%@",
    					new NSArray(new LRRecord[] {
    							periodeFermeture, composante})));
    }    
    
    /**
     * Trouve l'enregistrement
     * @param ec
     * @param periodeFermeture
     * @param composante
     * @return
     */
    public static EOAnnulationPeriodeFermeture findAnnulationPeriodeFermetureForFermetureAndService(
    		EOEditingContext ec, EOPeriodeFermeture periodeFermeture, EOStructure service) {
    	return fetchAnnulationPeriodeFermeture(
    			ec,
    			CRIDataBus.newCondition(
    					TO_PERIODE_FERMETURE_KEY +"=%@ and " + TO_STRUCTURE_KEY + "=%@",
    					new NSArray(new LRRecord[] {
    							periodeFermeture, service})));
    }   
    
    /**
     * Trouve l'enregistrement
     * @param ec
     * @param periodeFermeture
     * @param composante
     * @return
     */
    public static EOAnnulationPeriodeFermeture findAnnulationPeriodeFermetureForFermetureAndAffAnn(
    		EOEditingContext ec, EOPeriodeFermeture periodeFermeture, EOAffectationAnnuelle affAnn) {
    	return fetchAnnulationPeriodeFermeture(
    			ec,
    			CRIDataBus.newCondition(
    					TO_PERIODE_FERMETURE_KEY +"=%@ and " + TO_AFFECTATION_ANNUELLE_KEY + "=%@",
    					new NSArray(new LRRecord[] {
    							periodeFermeture, affAnn})));
    }    		

    /**
     * Indique si la fermeture passée en parametre a été annulée pour la composante
     * @param ec
     * @param periodeFermeture
     * @param composante
     * @return
     */
    public static boolean isAnnuleePeriodeFermetureForComposante(
    		EOEditingContext ec, EOPeriodeFermeture periodeFermeture, EOStructure composante) {
    	return findAnnulationPeriodeFermetureForFermetureAndComposante(ec, periodeFermeture, composante) != null;
    }
    

    /**
     * Indique si la fermeture passée en parametre a été annulée pour la service
     * @param ec
     * @param periodeFermeture
     * @param structure
     * @return
     */
    public static boolean isAnnuleePeriodeFermetureForService(
    		EOEditingContext ec, EOPeriodeFermeture periodeFermeture, EOStructure service) {
    	return findAnnulationPeriodeFermetureForFermetureAndService(ec, periodeFermeture, service) != null;
    }
    

    /**
     * Indique si la fermeture passée en parametre a été annulée pour le planning
     * @param ec
     * @param periodeFermeture
     * @param affAnn
     * @return
     */
    public static boolean isAnnuleePeriodeFermetureForPlanning(
    		EOEditingContext ec, EOPeriodeFermeture periodeFermeture, EOAffectationAnnuelle affAnn) {
    	return findAnnulationPeriodeFermetureForFermetureAndAffAnn(ec, periodeFermeture, affAnn) != null;
    }

}
