package fr.univlr.cri.conges;
import java.util.StringTokenizer;

import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.databus.CngUserInfo;
import fr.univlr.cri.util.StringCtrl;

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
 * Classe permettant de gerer la lecture de donnees 
 * de la table CONGES.MAIL_TODO. Elle extrait les
 * informations d'enregistrements. 
 * 
 * Ces donnees vont impacter des donnees de la table <code>dbTargetTableName()</code>.
 *
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */

public abstract class TaskCheckMailOrders extends TaskGeneric {

	// format :
	// <USER.TABLE>=<ATTRIBUT_HASHCODE>;<VALEUR_HASHCODE>;<EMAIL_FROM>;<CODE_OPERATION>
	//
	// exemples :
	// CONGES.CNG_ALERTE=CAL_HASCODE_VAL;yThdK15DSs6;francis.forbeau@univ-lr.fr;ACCEPT
	// CONGES.CNG_ALERTE=CAL_HASCODE_VIS;OdruzZj485D;fabienne.bourmaud@univ-lr.fr;ACCEPT
	
	/**
	 * La table impactee, en respectant le format <code>USER.TABLE</code>
	 */
	public abstract String dbTargetTableName();
	
  /**
   * La liste de toutes les MAIL_TODO de la table 
   */
  public NSArray checkTasks() {
  	return cngDataCenter().mailTodoBus().findAllMailTodo(dbTargetTableName());
  }
  
  /**
   * Extraire toutes les donnes de la requete.
   */
  public void extractData() {
    if (currentRecord() == null) {
      setErrorMessage("Pas d'enregistrement a analyser ...");
      return;
    }
    try {
    	// raz des donnees de la derniere operation
    	dbHashcodeAttr = hashCodeValue = emailFrom = codeOperation = null;
    	userInfo = null;
    	objectInModel = null;
    	// extraction des infos
    	StringTokenizer stGlobal = new StringTokenizer(
    			(String) currentRecord().valueForKey("mtoReq"), ";");
      int noToken = 0;
      while (stGlobal.hasMoreTokens()) {
        StringTokenizer stLocal = new StringTokenizer(stGlobal.nextToken(), "=");
        // table
        if (noToken == 0) {
        	// le nom de la table
          stLocal.nextToken();
          // l'attribut dans la DB
          dbHashcodeAttr = stLocal.nextToken();
          if (StringCtrl.isEmpty(dbHashcodeAttr)) {
          	setErrorMessage("Aucun nom d'attribut trouve.");
          	return;
          } else if (!validDbHashCodeAttr().containsObject(dbHashcodeAttr)) {
          	setErrorMessage("Nom d'attribut inconnu : " + dbHashcodeAttr);
          	return;
          }
          // l'attribut dans le modele
          modelHashcodeAttr = nameForAttributeColumn(
          		findEntityForTable(dbTargetTableName()), dbHashcodeAttr);
          if (StringCtrl.isEmpty(modelHashcodeAttr)) {
           	setErrorMessage("L'attribut \""+dbHashcodeAttr+
           			"\" n'a pas de correspondant dans le modele.");
          	return;
          }
        } else if (noToken == 1) {
        	// hashCode
        	hashCodeValue = stLocal.nextToken();
          if (StringCtrl.isEmpty(hashCodeValue)) {
          	setErrorMessage("Aucune valeur de hashcode trouvee.");
          	return;
          }
        } else if (noToken == 2) {
          // email
        	emailFrom = stLocal.nextToken();
          if (StringCtrl.isEmpty(emailFrom)) {
          	setErrorMessage("Aucun email trouve.");      
          	return;
          }
          // userInfo : retrouver la personne associee
          userInfo = cngDataCenter().alerteBus().findUserForEmail(emailFrom);
          if (userInfo == null) {
          	setErrorMessage(
          			"Vous n'etes pas reconnu en tant qu'utilisateur valide des congés.\n" +
          			"Verifiez que votre adresse e-mail  <"+emailFrom+"> est la meme avec laquelle vous avez recu le message.");      
          	return;
          }
        } else if (noToken == 3) {
        	// code operation
        	codeOperation = stLocal.nextToken();
        	if (StringCtrl.isEmpty(codeOperation)) {
        		setErrorMessage("pas de code d'operation trouve.");      
          	return;
          } else if (!validCodeOperation().containsObject(codeOperation)) {
          	setErrorMessage("Code d'operation inconnu : " + codeOperation);
          	return;
          }
        }
        noToken++;
      }
      // on va tenter de recuperer l'objet dans la base
      if (objectInModel() == null) {
      	setErrorMessage("L'objet designe par le code \""+
      			hashCodeValue+"\" est introuvable dans la base de donnees.\n\n"+
      			"Il se peut qu'un email précedent a déjà réalisé l'opération.");
      	return;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Le nom de l'attribut hashCode dans la base
   * de donnees. ex : <code>CAL_HASHCODE_VIS</code>
   */
  protected String dbHashcodeAttr;
  
  /**
   * Le nom de l'attribut hashCode dans la base
   * de donnees. ex : <code>calHashcodeVis</code>
   */
  protected String modelHashcodeAttr;
  
  /**
   * Le valeur du hashCode
   */
  protected String hashCodeValue;
  
  /**
   * L'adresse email d'ou provient l'ordre
   */
  protected String emailFrom;
  
  /**
   * Le code de l'operation en cours
   */
  public String codeOperation;

  /**
   * La structure <code>CngUserInfo</code> associee
   * a l'adresse <code>emailFrom</code>.
   */
  public CngUserInfo userInfo;
  
  /**
   * La liste des codes d'operation valides. Elle doit
   * retourne un <code>NSArray</code> de <code>String</code>.
   * 
   * <code>codeOperation</code> doit faire partie
   * de cette liste pour etre traitee.
   */
  public abstract NSArray validCodeOperation();
  
  /**
   * La liste des codes des attributs valides. Elle doit
   * retourne un <code>NSArray</code> de <code>String</code>.
   *
   * <code>dbHashcodeAttr</code> doit faire partie
   * de cette liste pour etre traitee.
   */
  public abstract NSArray validDbHashCodeAttr();
  
  /**
   * C'est la methode qui retourne l'objet sur lequel
   * l'operation va etre realisee. Il s'agit d'un fetch
   * qui retourne generalement un <code>EOGenericRecord</code>.
   * 
   * Ne pas l'appeler directement, c'est la methode accesseur
   * <code>objectInModel()</code> qui s'en charge.
   */
  public abstract Object fetchObjectInModel();
  
  private Object objectInModel;
  
  public Object objectInModel() {
  	if (objectInModel == null) {
  		objectInModel = fetchObjectInModel();
  	}
  	return objectInModel;
  }
  
  /**
   * Envoyer un email au demandeur et a l'administrateur en copie cachee,
   * indiquantl'erreur survenue.
   */
  public void sendMailError() {
    String title = "[Conges] Erreur de traitement de votre message";
    String content = getErrorMessage() + "\nVotre message ne sera pas traite.";
    if (criApp().debug()) {
      title = "MODE TEST: " + title;
      content = "email a destination de : " + emailFrom + "\n\n" + content;
    } else {
    	// on envoi en plus au demandeur en mode normal
      criApp().mailBus().sendMail(criApp().appAdminMail(), emailFrom, null, title, content);	
    }
    // quoi qu'il en soit, on envoi a l'admin
    criApp().mailBus().sendMail(criApp().appAdminMail(), criApp().appAdminMail(), null, title, 
    		"Le message suivant a ete envoye a <"+ emailFrom + ">\n-----\n"+content);
  }
}
