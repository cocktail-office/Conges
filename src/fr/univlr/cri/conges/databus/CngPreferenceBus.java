package fr.univlr.cri.conges.databus;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.eos.modele.conges.EOPreference;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.util.StringCtrl;

/**
 * Gestionnaire des informations sur les preferences utilisateurs.
 * 
 * @author Cyril TARADE <cyril.tarade at univ-lr.fr>
 */
public class CngPreferenceBus 
	extends CngDataBus {

	private final static String PRF_KEY_IS_MAIL_RECIPISSE = "isMailRecipisse";
	private final static String PRF_KEY_IS_MAIL_RECIPISSE_DELEGATION = "isMailRecipisseDelegation";
	private final static String PRF_SEP = ";";
	private final static String PRF_EQ = "=";
	
	public CngPreferenceBus(EOEditingContext editingContext) {
		super(editingContext);
	}
	
  /**
   * Mets a jour les preferences application de l'utilisateur <code>userInfo</code>.
   * @param transId
   * @param userInfo
   * @param isMailRecipisse
   * @param isMailRecipisseDelegation
   */
  public boolean updatePrefAppli(
  		Integer transId, CngUserInfo userInfo, Boolean isMailRecipisse, Boolean isMailRecipisseDelegation) {
    Integer localTransId = getTransaction(transId);
    EOEditingContext ec = econtextForTransaction(localTransId, true);
    try {
    	EOIndividu recIndividu = EOIndividu.findIndividuForPersIdInContext(ec, userInfo.persId());
    	EOPreference rec = (EOPreference) EOPreference.fetchPreference(
          ec, newCondition(
          		EOPreference.TO_INDIVIDU_KEY+"=%@", new NSArray(recIndividu)));
      // on cree l'enregistrement s'il n'existe pas
      if (rec == null) {
      	rec = EOPreference.createPreference(ec, recIndividu);
      }
      // la chaine de preference apres modification
      String prfPreference = "";
      
      // recipisse
      if (isMailRecipisse != null) {
      	// construire pour stockage
      	prfPreference += PRF_KEY_IS_MAIL_RECIPISSE + PRF_EQ + Boolean.toString(isMailRecipisse.booleanValue());
      } else {
      	// recuperer la valeur de la base
      	String strIsMailRecipisse = getPrfPreference(rec.prfPreference(), PRF_KEY_IS_MAIL_RECIPISSE);
      	if (strIsMailRecipisse != null) {
      		prfPreference += PRF_KEY_IS_MAIL_RECIPISSE + PRF_EQ + strIsMailRecipisse;
  			}
      }
      prfPreference += PRF_SEP;
      
      // recipisse delegation
      if (isMailRecipisseDelegation != null) {
      	// construire pour stockage
      	prfPreference += PRF_KEY_IS_MAIL_RECIPISSE_DELEGATION + PRF_EQ + Boolean.toString(isMailRecipisseDelegation.booleanValue());
      } else {
      	// recuperer la valeur de la base
      	String strIsMailRecipisseDelegation = getPrfPreference(rec.prfPreference(), PRF_KEY_IS_MAIL_RECIPISSE_DELEGATION);
      	if (strIsMailRecipisseDelegation != null) {
      		prfPreference += PRF_KEY_IS_MAIL_RECIPISSE_DELEGATION + PRF_EQ + strIsMailRecipisseDelegation;
  			}
      }
      prfPreference += PRF_SEP;
      
      rec.setPrfPreference(prfPreference);
      if (transId == null) commitECTransaction(localTransId);
      // mise a jour des donnees dans la session
      loadPreferences(userInfo);
      return true;
    } catch(Throwable ex) {
      throwError(ex);
      return false;
    }
  }
  
  /**
   * Charger les preferences application de l'utilisateur <code>persId</code>
   * dans la structure de donnee utilisateur <code>userInfo</code>.
   * @param userInfo
   */
  public boolean loadPreferences(CngUserInfo userInfo) {
    Integer localTransId = getTransaction(null);
    EOEditingContext ec = econtextForTransaction(localTransId, true);
    try {
    	EOIndividu recIndividu = EOIndividu.findIndividuForPersIdInContext(ec, userInfo.persId());
    	EOPreference rec = (EOPreference) EOPreference.fetchPreference(
        ec, newCondition(
        		EOPreference.TO_INDIVIDU_KEY+"=%@", new NSArray(recIndividu)));
    	if (rec != null) {
    		String strIsMailRecipisse = getPrfPreference(rec.prfPreference(), PRF_KEY_IS_MAIL_RECIPISSE);
      	if (strIsMailRecipisse != null) {
      		boolean isMailRecipisse = valueOf(strIsMailRecipisse);
  				userInfo.setIsMailRecipisse(isMailRecipisse);
  			}
      	String strIsMailRecipisseDelegation = getPrfPreference(rec.prfPreference(), PRF_KEY_IS_MAIL_RECIPISSE_DELEGATION);
      	if (strIsMailRecipisseDelegation != null) {
      		boolean isMailRecipisseDelegation = valueOf(strIsMailRecipisseDelegation);
  				userInfo.setIsMailRecipisseDelegation(isMailRecipisseDelegation);
  			}
    	}
    	return true;
    } catch(Throwable ex) {
      throwError(ex);
      return false;
    }
  }
  
  /**
   * Pour java 1.4.2 compliance ...
   * @param value
   * @return
   */
  private boolean valueOf(String value) {
  	if (!StringCtrl.isEmpty(value) && value.toUpperCase().equals("TRUE")) {
  		return true;
  	}
  	return false;
  }
  
  /**
   * Obtenir la valeur String d'un element
   * @param isMailRecipisse
   * @return
   */
  private String getPrfPreference(String strPrefList, String aKey) {
  	if (!StringCtrl.isEmpty(strPrefList)) {
    	NSArray prefList = NSArray.componentsSeparatedByString(strPrefList, PRF_SEP);
    	for (int i=0; i<prefList.count(); i++) {
    		String keyValueStr = (String) prefList.objectAtIndex(i);
    		if (!StringCtrl.isEmpty(keyValueStr)) {
        	NSArray keyValueList = NSArray.componentsSeparatedByString(keyValueStr, PRF_EQ);
        	String key = (String) keyValueList.objectAtIndex(0);
        	if (key.equals(aKey)) {
    				return (String) keyValueList.objectAtIndex(1);
    			}
    		}
    	}
  	}
  	return null;
  }
}
