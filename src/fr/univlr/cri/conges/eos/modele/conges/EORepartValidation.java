package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSValidation;

import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOOccupation;
import fr.univlr.cri.webapp.LRUserInfo;

public class EORepartValidation extends _EORepartValidation {

		public EORepartValidation() {
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
    
    
    private final static String INDIVIDU_PERSID_KEY = 
    	EORepartValidation.INDIVIDU_KEY + "." + EOIndividu.PERS_ID_KEY;


		public final static String FLAG_VALIDATION_VALUE 	= "A";
  	public final static String FLAG_VISEE_VALUE 		= "I";
  	

  	/**
		 * Indique si la personne a deja ou non vise le planning
		 */
		public static boolean individuHasVisePlanning(EOEditingContext ec, 
				EOAffectationAnnuelle affAnn, LRUserInfo userInfo) {
			NSArray persIdViseurs = (NSArray)findIndividuValidantForPlanning(
							ec,	affAnn, FLAG_VISEE_VALUE).valueForKeyPath(INDIVIDU_PERSID_KEY);
			return persIdViseurs.containsObject(new Double(userInfo.persId().intValue()));
		}

		/**
		 * Indique si la personne a deja ou non vise l'occupation
		 */
		public static boolean individuHasViseOccupation(EOEditingContext ec, 
				EOOccupation occupation, LRUserInfo userInfo) {
			NSArray persIdViseurs = (NSArray)findIndividuValidantForOccupation(
							ec,	occupation, FLAG_VISEE_VALUE).valueForKeyPath(INDIVIDU_PERSID_KEY);
			return persIdViseurs.containsObject(new Double(userInfo.persId().intValue()));
		}

		/**
		 * Indique si la personne a deja ou non valide le planning
		 */
		public static boolean individuHasValidePlanning(EOEditingContext ec, 
				EOAffectationAnnuelle affAnn, LRUserInfo userInfo) {
			NSArray persIdValideurs = (NSArray)findIndividuValidantForPlanning(
							ec, affAnn, FLAG_VALIDATION_VALUE).valueForKeyPath(INDIVIDU_PERSID_KEY);
			return persIdValideurs.containsObject(new Double(userInfo.persId().intValue()));
		}

		/**
		 * Indique si la personne a deja ou non valide l'occupation
		 */
		public static boolean individuHasValideOccupation(EOEditingContext ec, 
				EOOccupation occupation, LRUserInfo userInfo) {
			NSArray persIdValideurs = (NSArray)findIndividuValidantForOccupation(
							ec, occupation, FLAG_VALIDATION_VALUE).valueForKeyPath(INDIVIDU_PERSID_KEY);
			return persIdValideurs.containsObject(new Double(userInfo.persId().intValue()));
		}

		/**
		 * Enregistrements de l'entite <code>RepartValidation</code> pour 
		 * une <code>affAnn</code> et un type <code>typeValidation</code>.
		 */
		public static NSArray findIndividuValidantForPlanning(EOEditingContext ec, 
				EOAffectationAnnuelle affAnn, String typeValidation) {
			EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(
					EORepartValidation.AFFECTATION_ANNUELLE_KEY + " = %@ AND " +
					EORepartValidation.OCCUPATION_KEY + " = nil AND " + 
					EORepartValidation.TYPE_KEY + " = %@", 
					new NSArray(new Object[] { affAnn, typeValidation} ));
			return EORepartValidation.fetchRepartValidations(ec, qual, null);
		}

		/**
		 * Enregistrements de l'entite <code>RepartValidation</code> pour 
		 * une <code>occupation</code> et un type <code>typeValidation</code>.
		 */
		public static NSArray findIndividuValidantForOccupation(EOEditingContext ec, 
				EOOccupation occupation, String typeValidation) {
			EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(
					EORepartValidation.OCCUPATION_KEY + " = %@ AND " + 
					EORepartValidation.TYPE_KEY + " = %@", 
					new NSArray(new Object[] { occupation, typeValidation} ));
			return EORepartValidation.fetchRepartValidations(ec, qual, null);
		}



		/**
		 * creation d'un enregistrement + mise dans l'editingContext
		 * @param ec
		 * @param individu
		 * @param affAnn
		 * @param occupation
		 * @return
		 */
		public static EORepartValidation newRepartValidation(EOEditingContext ec, 
				LRUserInfo userInfo, EOAffectationAnnuelle affAnn, EOOccupation occupation, String typeValidation) {
			EOIndividu individu = EOIndividu.findIndividuForPersIdInContext(ec, userInfo.persId());
			EORepartValidation repart = createRepartValidation(
					ec, null, null, new Integer(individu.oid().intValue()), typeValidation, affAnn, individu);
			if (occupation != null) {
				repart.setOccupationRelationship(occupation);
			}
			return repart;
		}

}
