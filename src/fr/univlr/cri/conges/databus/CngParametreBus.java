package fr.univlr.cri.conges.databus;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;

import fr.univlr.cri.conges.eos.modele.conges.EOParametre;
import fr.univlr.cri.webapp.LRRecord;

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

public class CngParametreBus extends CngDataBus {

  public CngParametreBus(EOEditingContext editingContext) {
    super(editingContext);
  }

  /**
   *
   */
  public LRRecord fetchParametre(EOEditingContext ec, String paramKey) {
    return (LRRecord) fetchObject(ec, EOParametre.ENTITY_NAME, 
    		newCondition(EOParametre.PARAM_KEY_KEY+"='"+paramKey+"'"));
  }
  
  /**
  *
  */
 public boolean updateParametre(Integer transId, String paramKey, String paramValue) {
   Integer localTransId = getTransaction(transId);
   EOEditingContext ec = econtextForTransaction(localTransId, true);
   try {
  	 // premier filtre pour ne pas prendre en compte les valeurs null
     if (paramKey == null || paramValue == null) return false;
  	 // recherche du parametre
     EOEnterpriseObject recParametre = fetchParametre(ec, paramKey);
     if (recParametre == null) return false;
     // on fait la maj uniquement si besoin
     if (paramValue.equals((String) recParametre.valueForKey(EOParametre.PARAM_VALUE_KEY))) return true;
     // mise a jour de la valeur
     recParametre.takeValueForKey(paramValue, EOParametre.PARAM_VALUE_KEY);
     if (transId == null) return commitECTransaction(localTransId);
     return true;
    } catch(Throwable ex) {
      throwError(ex);
      return false;
    }
 }
 
  
  /**
   * Creer une enregistrement <code>Parametre</code>.
   */
  public LRRecord addParametre(Integer transId, String paramKey, String paramValue) {
    Integer localTransId = getTransaction(transId);
    EOEditingContext ec = econtextForTransaction(localTransId, true);
    try {
      EOEnterpriseObject recTraitement = newObjectInEntity(EOParametre.ENTITY_NAME, ec);
      recTraitement.takeValueForKey(paramKey,   EOParametre.PARAM_KEY_KEY);
      recTraitement.takeValueForKey(paramValue, EOParametre.PARAM_VALUE_KEY);
      if (transId == null) commitECTransaction(localTransId);
      return (LRRecord) recTraitement;
    } catch(Throwable ex) {
      throwError(ex);
      return null;
    }
  }
  
  
}
