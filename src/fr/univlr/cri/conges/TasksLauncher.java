package fr.univlr.cri.conges;
/**
 * Classe permettant de programmer et gerer le
 * lancement de toutes les taches planifiees.
 */

import java.util.Timer;

import fr.univlr.cri.conges.Application;
import fr.univlr.cri.conges.constantes.ConstsApplication;
import fr.univlr.cri.conges.eos.modele.conges.EODbTodo;
import fr.univlr.cri.conges.eos.modele.conges.EOMailTodo;
import fr.univlr.cri.webapp.LRLog;

/**
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */

public class TasksLauncher extends Thread {

  private Application app;
  
  public TasksLauncher() {
    super();
    app = (Application)Application.application();
  }

  public void run() {
    Timer timer = new Timer(true); 
    // demon de recherche de modification de la table d'affectations
   	if (app.extNotifCode() == ConstsApplication.EXT_NOTIF_CODE_TRIGGER) {
      int delayScanAffectation = app.config().intForKey("DELAY_SCAN_AFFECTATION");
      LRLog.rawLog(">> Notification par TRIGGER : la table " + EODbTodo.ENTITY_TABLE_NAME + " sera verifiee toutes les "+delayScanAffectation+" secondes.");
      timer.schedule(new TaskCheckTableChangesAffectation(), 0, 1000*delayScanAffectation);
   	}
    if (app.appUseSam()) {
      // demon de recherche des ordres par mails      
      int delayScanMailTodo = app.config().intForKey("DELAY_SCAN_MAIL_TODO");
      LRLog.rawLog(">> Activation du service SAM : la table  " + EOMailTodo.ENTITY_TABLE_NAME + " sera verifiee toutes les "+delayScanMailTodo+" secondes.");
      timer.schedule(new TaskCheckMailOrdersAlerts(), 0, 1000*delayScanMailTodo);    	
    }
  }

}