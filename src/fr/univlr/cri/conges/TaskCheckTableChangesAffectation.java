package fr.univlr.cri.conges;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.webapp.LRLog;

/**
 * Classe qui se charge de faire les verification sur 
 * les changement survenus sur la table GRHUM.AFFECTATION
 * 
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */

public class TaskCheckTableChangesAffectation extends TaskCheckTableChanges {

  public TaskCheckTableChangesAffectation() {
    super();
  }

  /**
   * Recherche les modification survenues sur la table 
   * GRHUM.AFFECTATION via la table CONGES.DT_TODO. 
   * Un traitement est prevu pour chaque cas de figure.
   */
  protected synchronized void doTask() {
    beginTask();
    NSArray tasksAffectation = checkTasks();
    if (tasksAffectation.count() > 0) {
      LRLog.rawLog("tasks.count : " + tasksAffectation.count(), 2);
    }
    for (int i = 0; i < tasksAffectation.count(); i++) {
      StringBuffer txtLog = new StringBuffer();
      EOGenericRecord recordTask = (EOGenericRecord) tasksAffectation.objectAtIndex(i);
      setCurrentRecord(recordTask);
      extractData();
      if (hasErrors()) {
        txtLog.append(getErrorMessage());
      } else {
        txtLog.append(getLabelForOperation(codeOperation));
        txtLog.append(", ").append(primaryKeyName()).append("=").append(getPrimaryKeyValue());
        txtLog.append(", params=").append(attributesNames().toString());
        txtLog.append(", values=").append(getDataValues().toString()).append("\n");
        boolean success = false;

        switch (codeOperation) {
        case CODE_OP_UPDATE:
          // changement de dates d'affectation
          if (attributesNames().containsObject("dDebAffectation") && attributesNames().containsObject("dFinAffectation")) {
            NSTimestamp prevDDebAffectation = (NSTimestamp) getOldAttributeValueForName("dDebAffectation");
            NSTimestamp dDebAffectation     = (NSTimestamp) getNewAttributeValueForName("dDebAffectation");
            NSTimestamp prevDFinAffectation = (NSTimestamp) getOldAttributeValueForName("dFinAffectation");
            NSTimestamp dFinAffectation     = (NSTimestamp) getNewAttributeValueForName("dFinAffectation");
            success = cngDataCenter().planningBus().changeDatesAffectation(
                (Integer) getPrimaryKeyValue(), prevDDebAffectation, dDebAffectation,
                prevDFinAffectation, dFinAffectation);
          }
          // changement de quotite
          if (attributesNames().containsObject("numQuotation")) {
            success = cngDataCenter().planningBus().changeQuotiteAffectation(
                (Integer) getPrimaryKeyValue());
          }
          break;
        case CODE_OP_DELETE:
          success = cngDataCenter().planningBus().supprimeAffectation((Integer) getPrimaryKeyValue());
          break;
        case CODE_OP_INSERT:
          success = cngDataCenter().planningBus().nouvelleAffectation((Integer) getPrimaryKeyValue());
          break;
        default:
          break;
        }
        txtLog.append("success : ").append(success);

        // operation reussie : on efface la requete
        if (success) {
          cngDataCenter().dbTodoBus().deleteRecord(recordTask);
          txtLog.append(" - deleting task ...");
        } else {
          cngDataCenter().dbTodoBus().moveToCrash(recordTask, cngDataCenter().planningBus().getErrorMessage());
          txtLog.append(" - error : " + cngDataCenter().planningBus().getErrorMessage());
        }
        LRLog.rawLog(txtLog.toString(), 2);
      }
    }
    endTask();
  }

  public String dBtableName() {
    return "GRHUM.AFFECTATION";
  }

}
