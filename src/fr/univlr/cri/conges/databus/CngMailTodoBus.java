package fr.univlr.cri.conges.databus;


import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.webapp.LRSort;

/**
 * Classe de gestion de la table MAIL_TODO
 * 
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */

public class CngMailTodoBus extends CngDataBus {

  private NSArray _dbMailSort;
  
  public CngMailTodoBus(EOEditingContext ec) {
    super(ec);
  }

  /**
   * Retourne la definition de tri pour les todo. 
   * On les trie par la date de creation.
   */
  private NSArray dbMailSort() {
    if (_dbMailSort == null)
      _dbMailSort = LRSort.newSort("mtoKey", LRSort.Ascending);
    return _dbMailSort;
  }
  
  /**
   * Retourne les prochaines operations a faire suite a la reception
   * d'un message email. Classement chronologique.
   * 
   * @param tableName Le nom oracle de la table concernee
   */
  public NSArray findAllMailTodo(String tableName) {
    return fetchArray(getEc(), "MailTodo", 
        newCondition("mtoReq like '" + tableName + "*'"), dbMailSort());
  }

  /**
   * Supprimer un enregistrement apres traitement
   * @param record
   */
  public void deleteRecord(EOGenericRecord record) {
    Integer transId = beginECTransaction();
    archiveRecord(transId, record);
    deleteFromTable(transId, "MailTodo", 
        newCondition("mtoKey = %@", new NSArray(record.valueForKey("mtoKey"))));
    commitECTrancsacition(transId);
  }
  
  /**
   * Deplacer un enregistrement dans la table d'erreurs
   * @param record 
   * @param errorMessage Le message d'erreur
   */
  public boolean moveToCrash(EOGenericRecord record, String errorMessage) {
    Integer transId = beginECTransaction();
    // suppression de la table DB_TODO
    deleteFromTable(transId, "MailTodo", 
        newCondition("mtoKey = %@", new NSArray(record.valueForKey("mtoKey"))));
    // creation du record dans la table DB_TODO_CRASH avec le message d'erreur
    EOEnterpriseObject newRecord = newObjectInEntity("MailTodoCrash", 
        econtextForTransaction(transId));
    newRecord.takeStoredValueForKey(record.valueForKey("mtoReq"), "mtcReq");
    newRecord.takeStoredValueForKey(getNewPrimaryKeyForEntity("MailTodoCrash"), "mtcKey");
    newRecord.takeStoredValueForKey(errorMessage, "mtcMessage");
    return commitECTrancsacition(transId);
  }

  /**
   * Sauvegarder un enregistrement apres traitement reussit
   * dans la table MAIL_TODO_DONE
   * @param record
   * @return
   */
  private void archiveRecord(Integer transId, EOGenericRecord record) {
    EOEnterpriseObject newRecord = newObjectInEntity("MailTodoDone", 
        econtextForTransaction(transId));
    newRecord.takeStoredValueForKey(record.valueForKey("mtoReq"), "mtdReq");
  }
}
