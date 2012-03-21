package fr.univlr.cri.conges;
import java.util.TimerTask;

import com.webobjects.eoaccess.EOAttribute;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.Application;
import fr.univlr.cri.conges.databus.CngDataCenter;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.util.wo5.DateCtrl;

/*
 * Copyright Universit� de La Rochelle 2006
 *
 * ctarade@univ-lr.fr
 *
 * Ce logiciel est un programme informatique servant � g�rer les comptes
 * informatiques des utilisateurs. 
 * 
 * Ce logiciel est r�gi par la licence CeCILL soumise au droit fran�ais et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffus�e par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".

 * En contrepartie de l'accessibilit� au code source et des droits de copie,
 * de modification et de redistribution accord�s par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limit�e.  Pour les m�mes raisons,
 * seule une responsabilit� restreinte p�se sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les conc�dants successifs.

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

 * Le fait que vous puissiez acc�der � cet en-t�te signifie que vous avez 
 * pris connaissance de la licence CeCILL, et que vous en avez accept� les
 * termes.
 */

/**
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */

public abstract class TaskGeneric extends TimerTask {

  // L'objet sur lequel on travaille
  private EOGenericRecord record;
  
  // gestion des erreurs
  private String errorMessage;

  /**
   * La tache a effectuer a intervalle regulier.
   */
  protected abstract void doTask();

  /**
   * Recherche les taches a executer
   */
  public abstract NSArray checkTasks();
  
  /**
   * methode appelee a chaque intervalle temps
   */
  public void run() {
    try{
      doTask();
    } catch (Exception e){
      e.printStackTrace();
    }
  }   

  
  public void beginTask() {
//    getNewSession();
  	setErrorMessage(null);
  }

  public void endTask() {
//    terminateSession();
  }

  private Application _criApp;
  
  protected Application criApp() {
    if (_criApp == null)
      _criApp = (Application) Application.application();
    return _criApp;
  }
  
//  private Session _cngSession;
//
//  private void getNewSession() {
//    _cngSession = new Session();
//  }
//  
//  private void terminateSession() {
//    _cngSession.terminate();
//  }
//
//  /**
//   * Pointeur vers le gestionnaire de donnees.
//   */
//  public Session cngSession() {
//    return _cngSession;
//  }
  
  private CngDataCenter _cngDataCenter;
  
  /**
   * Pointeur vers le gestionnaire de donnees.
   */
  public CngDataCenter cngDataCenter() {
    if (_cngDataCenter == null)
      _cngDataCenter = new CngDataCenter();
    return _cngDataCenter;
  }

  
  /**
   * Extraire toutes les donnes de la requete.
   */
  public abstract void extractData();
  
  
  /**
   * Definir la requete en cours
   */
  public void setCurrentRecord(EOGenericRecord value) {
    record = value;
  }

  public EOGenericRecord currentRecord() {
  	return record;
  }	
  

  /**
   * Le message d'erreur
   */
  public void setErrorMessage(String value) {
  	errorMessage = value;
  }
  
  public String getErrorMessage() {
    return errorMessage;
  }

  // erreurs

  public boolean hasErrors() {
    return !StringCtrl.isEmpty(getErrorMessage());
  }
  
  
  //
  // METHODES COMMUNES
  //
  
  /**
   * Retourner le nom d'un attribut dans le modele
   * @param dbColumnName le champ de la base de donnees
   */
	protected String nameForAttributeColumn(EOEntity entity, String dbColumnName) {
    String name = null;
    NSArray attributes = entity.attributes();
    int i=0;
    while (i < attributes.count() -1 && name == null) {
      EOAttribute anAttribute = (EOAttribute) attributes.objectAtIndex(i);
      if (anAttribute.columnName().equals(dbColumnName)) {
        name = anAttribute.name();
      }
      i++;
    }
    return name;
  }
  
  
  /**
   * L'entite du modele associe au nom d'une table
   * @param tableName
   * @return
   */
  protected EOEntity findEntityForTable(String tableName) {
  	EOEntity result = null;
  	EOModelGroup mg = EOModelGroup.defaultGroup();
    for (int i = 0; i < mg.models().count(); i++) {
      EOModel model = (EOModel) mg.models().objectAtIndex(i);
      for (int j = 0; j < model.entities().count(); j++) {
        EOEntity entity = (EOEntity) model.entities().objectAtIndex(j);
        if (entity.externalName().equals(tableName)) {
        	result = entity;
          break;
        }
      }
    }
    return result;
  }
  
  
  /**
   * Retrouver la classe Java associee au nom d'un attribut du modele
   */
  protected Class classForAttributeName(EOEntity entity, String attributeName) {
    Class aClass = null;
    if (currentRecord() != null) {
      EOAttribute attribute = entity.attributeNamed(attributeName);
      try {
        aClass = Class.forName(attribute.className());
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
    return aClass;
  }
  
  /**
   * Retourner une instance de l'attribut en correspondance avec sa classe
   * dans le modele. La valeur brute est un String.
   * 
   * @param aClass La classe de l'objet dans le modele
   * @param value La valeur de l'objet sous forme String
   */
  protected Object instancelObjectForClassAndStringValue(Class aClass, String value) {
    Object object = null;
    if (aClass == Number.class) {
      object = new Integer(Integer.parseInt(value));
    } else if (aClass == NSTimestamp.class) {
      object = DateCtrl.stringToDate(value);
    } else if (aClass == String.class) {
      object = value;
    }
    return object;
  }
  
 

}
