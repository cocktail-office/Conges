package fr.univlr.cri.conges;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.constantes.ConstsDroit;
import fr.univlr.cri.conges.eos.modele.conges.EOAlerte;
import fr.univlr.cri.webapp.LRLog;

/*
 * Copyright Consortium Coktail, 6 f�vr. 07
 * 
 * cyril.tarade at univ-lr.fr
 * 
 * Ce logiciel est un programme informatique servant � [rappeler les
 * caract�ristiques techniques de votre logiciel]. 
 * 
 * Ce logiciel est r�gi par la licence CeCILL soumise au droit fran�ais et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffus�e par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".
 * 
 * En contrepartie de l'accessibilit� au code source et des droits de copie,
 * de modification et de redistribution accord�s par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limit�e.  Pour les m�mes raisons,
 * seule une responsabilit� restreinte p�se sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les conc�dants successifs.
 * 
 * A cet �gard  l'attention de l'utilisateur est attir�e sur les risques
 * associ�s au chargement,  � l'utilisation,  � la modification et/ou au
 * d�veloppement et � la reproduction du logiciel par l'utilisateur �tant 
 * donn� sa sp�cificit� de logiciel libre, qui peut le rendre complexe � 
 * manipuler et qui le r�serve donc � des d�veloppeurs et des professionnels
 * avertis poss�dant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invit�s � charger  et  tester  l'ad�quation  du
 * logiciel � leurs besoins dans des conditions permettant d'assurer la
 * s�curit� de leurs syst�mes et ou de leurs donn�es et, plus g�n�ralement, 
 * � l'utiliser et l'exploiter dans les m�mes conditions de s�curit�. 
 * 
 * Le fait que vous puissiez acc�der � cet en-t�te signifie que vous avez 
 * pris connaissance de la licence CeCILL, et que vous en avez accept� les
 * termes.
 */

/**
 * Recherche les demande de visa/validation sur les alertes. 
 * Un traitement est prevu pour chaque cas de figure.
 *
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */

public class TaskCheckMailOrdersAlerts 
	extends TaskCheckMailOrders
		implements ConstsDroit {

	/**
	 * Les nom des attributs de recherche de hashcode 
	 * possibles dans la base de donnees.
	 */
	private final static String DB_ATTR_HASHCODE_VAL = "CAL_HASHCODE_VAL";
	private final static String DB_ATTR_HASHCODE_VIS = "CAL_HASHCODE_VIS";

	/**
	 * Tous les codes d'ordres connus
	 */
	private final static String CODE_OP_ACCEPT = "ACCEPT";
	private final static String CODE_OP_REFUSE = "REFUSE";
	
	// exemples :
	// CONGES.CNG_ALERTE=CAL_HASHCODE_VAL;yThdK15DSs6;francis.forbeau@univ-lr.fr;ACCEPT
	// CONGES.CNG_ALERTE=CAL_HASHCODE_VIS;OdruzZj485D;fabienne.bourmaud@univ-lr.fr;ACCEPT
	protected synchronized void doTask() {
		
    NSArray tasksMailAlert = checkTasks();
    if (tasksMailAlert.count() > 0) {
      //LRLog.rawLog("tasks.count : " + tasksMailAlert.count(), 2);
      LRLog.log("tasks.count : " + tasksMailAlert.count());
    }
    
    for (int i = 0; i < tasksMailAlert.count(); i++) {
  	  beginTask();
  	  
      StringBuffer txtLog = new StringBuffer();
      EOGenericRecord recordTask = (EOGenericRecord) tasksMailAlert.objectAtIndex(i);
      
      
      synchronized (recordTask) {
			  setCurrentRecord(recordTask);
	      extractData();
	      if (hasErrors()) {
	      	// erreur : poubelle
	        cngDataCenter().mailTodoBus().moveToCrash(recordTask, getErrorMessage());
	        txtLog.append(getErrorMessage());
	        sendMailError();
	      } else {
	        txtLog.append("codeOp="+codeOperation);
	        txtLog.append(", emailFrom=").append(emailFrom);
	        txtLog.append(", hashCodeValue=").append(hashCodeValue);
	        txtLog.append(", dbHashCodeAttr=").append(dbHashcodeAttr).append("\n");
	        boolean success = false;
	        
	        boolean isVisa = !dbHashcodeAttr.equals(DB_ATTR_HASHCODE_VAL);
	        boolean isAccept = codeOperation.equals(CODE_OP_ACCEPT);
	        
	        // faire une sauvegarde des infos de l'alerte pour
	        // l'envoi de notification en cas de succes
	        String libelleAlert = alerte().libelle();
	        String nomPrenom = alerte().affectationAnnuelle().individu().nomComplet();
	        
	        // traitement selon la demande
	        if (!isVisa) {
	        	if (isAccept) {
	        		success = cngDataCenter().alerteBus().accepteValid(editingContext(), alerte(), userInfo);
	        	} else {
	        		success = cngDataCenter().alerteBus().refuse(editingContext(), alerte(), userInfo, DROIT_NIVEAU_VALIDATION);
	        	}
	        } else {
	        	if (isAccept) {
	        		success = cngDataCenter().alerteBus().accepteVisa(editingContext(), alerte(), userInfo);
	        	} else {
	        		success = cngDataCenter().alerteBus().refuse(editingContext(), alerte(), userInfo, DROIT_NIVEAU_VISA);
	        	}
	        }
	        
	        txtLog.append("success : ").append(success);
	
	        if (success) {
	        	// operation reussie : on informe le demandeur
	          cngDataCenter().alerteBus().sendMailOperationSuccessfull(
	          		libelleAlert, nomPrenom, emailFrom, isAccept, isVisa);
	          // operation reussie : on efface la requete
	          cngDataCenter().mailTodoBus().deleteRecord(recordTask);
	          txtLog.append(" - deleting task ...");
	        } else {
	        	// ca n'a pas marche, on previent l'utilisateur et l'administrateur
	        	setErrorMessage(cngDataCenter().alerteBus().getErrorMessage() + 
	        			"\n----\ndetails\n\n"+ txtLog.toString());
	          cngDataCenter().mailTodoBus().moveToCrash(recordTask, getErrorMessage());
	          txtLog.append(" - error : " + getErrorMessage());
	          sendMailError();
	        }
	        LRLog.rawLog(txtLog.toString(), 2);
	    	}
      }
      
      endTask();
    }
	}

	/**
	 * Concerne la table des alertes
	 */
	public String dbTargetTableName() {
		return "CONGES.CNG_ALERTE";
	}

	private NSArray _validCodeOperation;
	
	/**
	 * Les operations d'acceptation et de refus
	 */
	public NSArray validCodeOperation() {
		if (_validCodeOperation == null)
			_validCodeOperation = new NSArray(
					new String[]{CODE_OP_ACCEPT, CODE_OP_REFUSE});
		return _validCodeOperation;
	}

	private NSArray _validDbHashCodeAttr;

	/**
	 * Les attributs de visa et de validation
	 */
	public NSArray validDbHashCodeAttr() {
		if (_validDbHashCodeAttr == null)
			_validDbHashCodeAttr = new NSArray(
					new String[]{DB_ATTR_HASHCODE_VAL, DB_ATTR_HASHCODE_VIS});
		return _validDbHashCodeAttr;
	}

	/**
	 * Retrouver l'alerte associee
	 */
	public Object fetchObjectInModel() {
		return cngDataCenter().alerteBus().findAlerteForHashCode(editingContext(), modelHashcodeAttr, hashCodeValue);
	}

	/**
	 * Recuperer directement l'alerte
	 */
	private EOAlerte alerte() {
		return (EOAlerte) objectInModel();
	}
	
	private EOEditingContext _editingContext;

	/**
	 * Le pointeur vers un editing context qui  peut etre utilise en ecriture.
	 * (Celui des CRIDataBus est un ROEditingContext)
	 */
	private EOEditingContext editingContext() {
		if (_editingContext == null)
			_editingContext = new EOEditingContext();
		return _editingContext;
	}
}
