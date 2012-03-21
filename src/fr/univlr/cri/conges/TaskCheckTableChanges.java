package fr.univlr.cri.conges;

import java.util.StringTokenizer;

import com.webobjects.eoaccess.EOAttribute;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableDictionary;

import fr.univlr.cri.util.StringCtrl;


/*
 * Copyright Université de La Rochelle 2006
 *
 * ctarade@univ-lr.fr
 *
 * Ce logiciel est un programme informatique servant à gérer les comptes
 * informatiques des utilisateurs. 
 * 
 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".

 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.

 * A cet égard  l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement,  à l'utilisation,  à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant 
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à 
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement, 
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité. 

 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez 
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 */

/**
 * Classe permettant de gerer la lecture de donnees 
 * de la table CONGES.DB_TODO. Elle extrait les
 * informations d'enregistrements.
 *
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */

public abstract class TaskCheckTableChanges extends TaskGeneric {
  
  // exemple :
  // GRHUM.AFFECTATION=U;2215;D_FIN_AFFECTATION=30/06/2003$17/02/2006
  
  private final static String KEYWORD_OP_INSERT = "I";
  private final static String KEYWORD_OP_DELETE = "D";
  private final static String KEYWORD_OP_UPDATE = "U";

  private final static String LABEL_INSERT  = "INSERT";
  private final static String LABEL_DELETE  = "DELETE";
  private final static String LABEL_UPDATE  = "UPDATE";
  private final static String LABEL_UNKNOWN = "UNKNOWN";

  public final static int CODE_OP_INSERT   = 0;
  public final static int CODE_OP_DELETE   = 1;
  public final static int CODE_OP_UPDATE   = 2;
  public final static int CODE_OP_UNKNOWN  = 3;
  
  protected int codeOperation;
  
  // Les valeurs lues dans la requete
  private Object primaryKeyValue;
  // tableau de dictionnaires contenant les information sur les donnes modifiees
  private NSArray dataValues;
  // structure d'un dico :
  // "valueClass" : classe java de l'objet
  // "attributeColumn"
  // "attributeName"
  // "oldValue"
  // "newValue"
  
  /**
   * Le nom oracle de la table a surveiller.
   */
  public abstract String dBtableName();
    
  /**
   * Extraire toutes les donnes de la requete.
   */
  public void extractData() {
    if (currentRecord() == null) {
      setErrorMessage("Pas d'enregistrement a analyser ...");
      return;
    }
    try {
      _attributesNames = null;
      dataValues = new NSArray();
      StringTokenizer stGlobal = new StringTokenizer(
          (String) currentRecord().valueForKey("dtoReq"), ";");
      int noToken = 0;
      while (stGlobal.hasMoreTokens()) {
        StringTokenizer stLocal = new StringTokenizer(stGlobal.nextToken(), "=");
        // table
        if (noToken == 0) {
          String tableName = stLocal.nextToken();
          if (entityDbTable() == null) {
          	setErrorMessage("Aucune entite n'a ete trouve pour la table '" + tableName + "'");
            return;
          }
          String codeOp = stLocal.nextToken();
          if (codeOp.equals(KEYWORD_OP_DELETE)) 
            codeOperation = CODE_OP_DELETE;
          else if (codeOp.equals(KEYWORD_OP_INSERT))
            codeOperation = CODE_OP_INSERT;
          else if (codeOp.equals(KEYWORD_OP_UPDATE))
            codeOperation = CODE_OP_UPDATE;
          else 
            codeOperation = CODE_OP_UNKNOWN;
        }
        // primary key
        else if (noToken == 1) {
          primaryKeyValue = instancelObjectForClassAndStringValue(primaryKeyClass(), stLocal.nextToken());
        }
        // datas
        else {
          if (codeOperation == CODE_OP_DELETE) {
            // pas de datas pour delete
          } else if (codeOperation == CODE_OP_INSERT) {
            // pas de datas pour insert
          } else if (codeOperation == CODE_OP_UPDATE) {
            // 2 valeurs
            // <attributeColumn>=<oldValue>$<newValue>
            String attributeColumn = stLocal.nextToken();
            String attributeName = nameForAttributeColumn(entityDbTable(), attributeColumn);
            Class valueClass = classForAttributeName(entityDbTable(), attributeName);
            NSArray arrayValues = NSArray.componentsSeparatedByString(stLocal.nextToken(), "$");
            Object oldValue = instancelObjectForClassAndStringValue(valueClass, (String) arrayValues.objectAtIndex(0));
            Object newValue = instancelObjectForClassAndStringValue(valueClass, (String) arrayValues.objectAtIndex(1));
            NSMutableDictionary dico = new NSMutableDictionary();
            dico.setObjectForKey(attributeColumn, "attributeColumn");
            dico.setObjectForKey(attributeName, "attributeName");
            dico.setObjectForKey(valueClass, "valueClass");
            dico.setObjectForKey((oldValue == null ? NSKeyValueCoding.NullValue : oldValue), "oldValue");
            dico.setObjectForKey((newValue == null ? NSKeyValueCoding.NullValue : newValue), "newValue");
            dataValues = dataValues.arrayByAddingObject(dico);
          }
        }
        noToken++;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  

  private String _primaryKeyName;
  
  /**
   * Le nom de l'attribut de la cle primaire dans le modele.
   */
  public String primaryKeyName() {
    if (StringCtrl.isEmpty(_primaryKeyName)) {
      if (currentRecord() != null) {
        EOAttribute attribute = (EOAttribute) entityDbTable().primaryKeyAttributes().objectAtIndex(0);
        _primaryKeyName = attribute.name();
      }
    }
    return _primaryKeyName;
  }
  
  private Class _primaryKeyClass;
  
  /**
   * Classe d'un objet cle primaire
   */
  public Class primaryKeyClass() {
    if (_primaryKeyClass == null) {
      if (!StringCtrl.isEmpty(primaryKeyName())) {
        _primaryKeyClass = classForAttributeName(entityDbTable(), primaryKeyName());
      }
    }
    return _primaryKeyClass;
  }


  private EOEntity _entityDbTable;
  
  /**
   * L'entité du modele representant la table a surveiller
   */
  private EOEntity entityDbTable() {
    if (_entityDbTable == null)
    	_entityDbTable = findEntityForTable(dBtableName());
    return _entityDbTable;
  }
  
  // getters
  
  public Object getPrimaryKeyValue() {
    return primaryKeyValue;
  }

  private NSArray _attributesNames;
  
  /**
   * liste de tous les attributs du modele qui
   * doivent etre modifies.
   */
  public NSArray attributesNames() {
    if (_attributesNames == null) {
      _attributesNames = new NSArray();
      for (int i = 0; i < getDataValues().count(); i++) {
        NSDictionary dico = (NSDictionary) getDataValues().objectAtIndex(i);
        _attributesNames = _attributesNames.arrayByAddingObject(
            dico.valueForKey("attributeName"));
      }
    }
    return _attributesNames;
  }
  
  /**
   * Retourne la nouvelle valeur de l'attribut
   * @param name
   */
  public Object getOldAttributeValueForName(String name) {
    for (int i = 0; i < getDataValues().count(); i++) {
      NSDictionary dico = (NSDictionary) getDataValues().objectAtIndex(i);
      if (((String)dico.valueForKey("attributeName")).equals(name)) {
        Object value = dico.valueForKey("oldValue");
        if (value == NSKeyValueCoding.NullValue) {  
          return null; 
        } else {
          return value;
        }
      }
    }
    return null;
  }

  /**
   * Retourne la nouvelle valeur de l'attribut
   * @param name
   */
  public Object getNewAttributeValueForName(String name) {
    for (int i = 0; i < getDataValues().count(); i++) {
      NSDictionary dico = (NSDictionary) getDataValues().objectAtIndex(i);
      if (((String)dico.valueForKey("attributeName")).equals(name)) {
        Object value = dico.valueForKey("newValue");
        if (value == NSKeyValueCoding.NullValue) {  
          return null; 
        } else {
          return value;
        }
      }
    }
    return null;
  }

  /**
   * Retourne la liste des valeurs des parametres
   */
  public NSArray getDataValues() {
    return dataValues;
  }
  
  /**
   * Le libelle d'une operation selon son code
   */
  public String getLabelForOperation(int codeOp) {
    String label = LABEL_UNKNOWN;
    switch (codeOp) {
      case CODE_OP_INSERT: label = LABEL_INSERT; break;
      case CODE_OP_UPDATE: label = LABEL_UPDATE; break;
      case CODE_OP_DELETE: label = LABEL_DELETE; break;
      default: break;
    }
    return label;
  }
  
  /**
   * La liste de toutes les DB_TODO de la table 
   */
  public NSArray checkTasks() {
  	return cngDataCenter().dbTodoBus().findAllDbTodo(dBtableName());
  }
}
