package fr.univlr.cri.conges.databus;


import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.webapp.LRSort;

/**
 * Classe de gestion de la table DB_TODO
 * 
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */

public class CngDbTodoBus extends CngDataBus {

  private NSArray _dbTodoSort;
  
  public CngDbTodoBus(EOEditingContext ec) {
    super(ec);
  }

  /**
   * Retourne la definition de tri pour les todo. 
   * On les trie par la date de creation.
   */
  private NSArray dbTodoSort() {
    if (_dbTodoSort == null)
      _dbTodoSort = LRSort.newSort("dtoKey", LRSort.Ascending);
    return _dbTodoSort;
  }
  
  /**
   * Retourne les prochaines operations a faire suite a la modification
   * du contenu de table surveillees par un trigger de la
   * base de donnees. Classement chronologique.
   * 
   * @param tableName Le nom oracle de la table
   */
  public NSArray findAllDbTodo(String tableName) {
    return fetchArray(getEc(), "DbTodo", 
        newCondition("dtoReq like '" + tableName + "*'"), dbTodoSort());
  }

  /**
   * Supprimer un enregistrement apres traitement
   * @param record
   */
  public void deleteRecord(EOGenericRecord record) {
    Integer transId = beginECTransaction();
    archiveRecord(transId, record);
    deleteFromTable(transId, "DbTodo", 
        newCondition("dtoKey = %@", new NSArray(record.valueForKey("dtoKey"))));
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
    deleteFromTable(transId, "DbTodo", 
        newCondition("dtoKey = %@", new NSArray(record.valueForKey("dtoKey"))));
    // creation du record dans la table DB_TODO_CRASH avec le message d'erreur
    EOEnterpriseObject newRecord = newObjectInEntity("DbTodoCrash", 
        econtextForTransaction(transId));
    newRecord.takeStoredValueForKey(record.valueForKey("dtoReq"), "dtcReq");
    newRecord.takeStoredValueForKey(getNewPrimaryKeyForEntity("DbTodoCrash"), "dtcKey");
    newRecord.takeStoredValueForKey(errorMessage, "dtcMessage");
    return commitECTrancsacition(transId);
  }

  /**
   * Sauvegarder un enregistrement apres traitement reussit
   * dans la table DB_TODO_DONE
   * @param record
   * @return
   */
  private void archiveRecord(Integer transId, EOGenericRecord record) {
  	EOEnterpriseObject newRecord = newObjectInEntity("DbTodoDone", 
        econtextForTransaction(transId));
    newRecord.takeStoredValueForKey(record.valueForKey("dtoReq"), "dtdReq");
  }
}
