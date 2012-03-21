package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSTimestamp;
import com.webobjects.foundation.NSValidation;

import fr.univlr.cri.conges.eos.modele.planning.I_ClasseMetierNotificationParametre;
import fr.univlr.cri.conges.objects.Parametre;
import fr.univlr.cri.conges.utils.DateCtrlConges;

public class EOParametre extends _EOParametre {
	
	public EOParametre() {
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
   * Methode permettant de reconstruire un dictionnaire issu
   * de la table de parametre. Les cles commencent toutes 
   * avec le meme prefix, et sont suivies d'une annee de 
   * 4 chiffres.
   * 
   * @param prefixParamKey
   * @param paramValueType
   */
  public static NSDictionary retrieveDicoParam(EOEditingContext ec, String prefixParamKey, int paramValueType) {
    EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(
    		PARAM_KEY_KEY + " like '"+prefixParamKey+"*'", null);
    NSArray records = fetchParametres(ec, qual, null);
    NSMutableDictionary dico = new NSMutableDictionary();
    for (int i = 0; i < records.count(); i++) {
      EOParametre record = (EOParametre) records.objectAtIndex(i);
      String strDate = record.stringForKey(PARAM_KEY_KEY);
      NSTimestamp debutAnnee = DateCtrlConges.dateDebutAnneePourStrAnnee(
      		strDate.subSequence(prefixParamKey.length(), prefixParamKey.length()+4).toString());
      if (paramValueType == Parametre.PARAM_VALUE_TYPE_INTEGER) {
      	dico.setObjectForKey(
      			new Integer(record.intForKey(PARAM_VALUE_KEY)),
      			DateCtrlConges.dateToString(debutAnnee));
      } else if (paramValueType == Parametre.PARAM_VALUE_TYPE_STRING) {
    		dico.setObjectForKey(
    				record.stringForKey(PARAM_VALUE_KEY), 
    				DateCtrlConges.dateToString(debutAnnee));
      } else if (paramValueType == Parametre.PARAM_VALUE_TYPE_BOOLEAN) {
    		dico.setObjectForKey(
    				new Boolean(record.boolForKey(PARAM_VALUE_KEY)),
    				DateCtrlConges.dateToString(debutAnnee));
      }
    }

    return dico.immutableClone();
  }

  /**
   * Recuperer la valeur d'un parametre depuis la base de données
   * @param ec
   * @param paramKey
   * @param paramValueType
   * @return
   */
  public static Object retrieveParam(EOEditingContext ec, String paramKey, int paramValueType) {
  	Object paramValue = null;
  	EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(
    		PARAM_KEY_KEY + "='"+paramKey+"'", null);
  	EOParametre record = fetchParametre(ec, qual);
  	if (paramValueType == Parametre.PARAM_VALUE_TYPE_INTEGER) {
  		paramValue = new Integer(record.intForKey(PARAM_VALUE_KEY));
  	} else if (paramValueType == Parametre.PARAM_VALUE_TYPE_STRING) {
  		paramValue = record.stringForKey(PARAM_VALUE_KEY);
  	} else if (paramValueType == Parametre.PARAM_VALUE_TYPE_BOOLEAN) {
  		paramValue = new Boolean(record.boolForKey(PARAM_VALUE_KEY));
  	}

  	return paramValue;
  }
}
