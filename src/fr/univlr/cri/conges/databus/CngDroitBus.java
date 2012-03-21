/*
 * Copyright Consortium Coktail, 13 juin 07
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

package fr.univlr.cri.conges.databus;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.constantes.ConstsDroit;
import fr.univlr.cri.conges.eos.modele.conges.EODroitPasDeMailCds;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;

/**
 * Classe de manipulation des droits. Entite centrale : <code>Droit</code>
 * 
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 *
 */

public class CngDroitBus extends CngDataBus {

  public CngDroitBus(EOEditingContext editingContext) {
    super(editingContext);
  }
  
  /**
   * Donner la liste des DRH locaux d'une composante.
   * Le filtrage se fait sur le niveau NIVEAU_DRH_COMPOSANTE
   * @param composante
   * @return un <code>NSArray</code> de <code>EOIndividu</code>
   */
  public NSArray findDRHForComposante(EOEditingContext ec, EOStructure composante) {
  	EOQualifier qual = newCondition(
  			"toStructure = %@ AND cdrNiveau = %@", 
  			new NSArray(new Object[]{
  					composante, ConstsDroit.DROIT_NIVEAU_DRH_COMPOSANTE}));
  	return (NSArray) fetchArray(ec, "Droit", qual, null).valueForKey("toIndividuResp");
  }

	/**
	 * rechercher les droits pour un responsable
	 * @param ec
	 * @param individuResp
	 * @param cdrType
	 * @param cdrNiveau
	 * @return
	 */
	public NSArray findDroitsForResponsableAndCdrTypeAndNiveau(EOEditingContext ec, EOIndividu individuResp, String cdrType, Number cdrNiveau) {
		EOQualifier qual = newCondition(
				"toIndividuResp = %@ AND cdrType = %@ AND cdrNiveau = %@", 
				new NSArray(new Object[] {individuResp, cdrType, cdrNiveau}));
		return fetchArray(ec, "Droit", qual, null);
	}

	/**
	 * rechercher les droits sur une structure (et enventuellement un individu)
	 * 
	 * @param ec
	 * @param structure
	 * @param individu (facultatif)
	 * @param CdrType
	 * @param cdrNiveau
	 * @return
	 */
	public NSArray findDroitsForStructureAndIndividuForCdrTypeAndNiveau(EOEditingContext ec, EOStructure structure, 
			EOIndividu individu, String cdrType, Number cdrNiveau) {
		EOStructure recService = structure;
		// cas particuler au niveau de la composante, on filtre sur la composante du service
		if (cdrNiveau.intValue() == ConstsDroit.DROIT_NIVEAU_ADM_COMPOSANTE.intValue() ||
				cdrNiveau.intValue() == ConstsDroit.DROIT_NIVEAU_DRH_COMPOSANTE.intValue()) {
			recService = structure.toComposante();
		}
		EOQualifier qual = newCondition("toStructure = %@ AND cdrType = %@ AND cdrNiveau = %@", 
			new NSArray(new Object[] {recService, cdrType, cdrNiveau}));
		if (individu != null)
			qual = newCondition("toStructure = %@ AND cdrType = %@ AND cdrNiveau = %@ AND toIndividu = %@", 
				new NSArray(new Object[] {recService, cdrType, cdrNiveau, individu}));
		return fetchArray(ec, "Droit", qual, null);
	}

	/**
	 * Indique si l'individu possede les droits de validation de DRH
	 * sur la composante du service passe en parametre
	 * 
	 * @param individu
	 * @param structure
	 * @return
	 */
	public boolean isDrhComposante(EOEditingContext ec, EOIndividu individu, EOStructure structure) {
		EOQualifier qual = newCondition("toStructure = %@ AND toIndividuResp = %@ AND cdrNiveau = %@",
				new NSArray(new Object[]{structure.toComposante(), individu, ConstsDroit.DROIT_NIVEAU_DRH_COMPOSANTE}));
		return fetchArray(ec, "Droit", qual, null).count() > 0;
	}
	
	/**
	 * Donne la liste des responsables du planning concerne.
	 * @param edc
	 * @param affAnn : l'enregistrement <code>EOAffectationAnnuelle</code> concerne.
	 * @param shouldIgnoreCdsNoMail : indique s'il ne faut pas traiter les information de la table <code>DroitPasDeMailCds</code>.
	 * 																Si cette valeur est a <code>true</code>, alors on retourne toujours le chef de service,
	 * 																meme si ce dernier est parametre comme ne recevant pas les mails.
	 * @return un <code>NSArray</code> de <code>EOIndividu</code>.
	 */
	public NSArray findResponsablePlanning(EOEditingContext edc, EOAffectationAnnuelle affAnn, boolean shouldIgnoreCdsNoMail) {
	  EOStructure structure = affAnn.structure();
	  EOIndividu chefDeService = affAnn.structure().responsable();
		EOIndividu individu = affAnn.individu();
		NSArray recsResponsables = new NSArray();
  	if (chefDeService != null && individu.persId().intValue() == chefDeService.persId().intValue()) {
    	recsResponsables = (NSArray) findDroitsForStructureAndIndividuForCdrTypeAndNiveau(
          edc, structure, null, ConstsDroit.DROIT_CIBLE_CHEF_DE_SERVICE,
          ConstsDroit.DROIT_NIVEAU_VALIDATION).valueForKey("toIndividuResp");
    } else {
    	recsResponsables = (NSArray) findDroitsForStructureAndIndividuForCdrTypeAndNiveau(
          edc, structure, null, ConstsDroit.DROIT_CIBLE_SERVICE,
          ConstsDroit.DROIT_NIVEAU_VALIDATION).valueForKey("toIndividuResp");
    }
    // ajout de toute personne qui a la validation sur l'individu
  	recsResponsables = recsResponsables.arrayByAddingObjectsFromArray(
    		(NSArray) (findDroitsForStructureAndIndividuForCdrTypeAndNiveau(
    				edc, structure, individu, ConstsDroit.DROIT_CIBLE_INDIVIDU,
    				ConstsDroit.DROIT_NIVEAU_VALIDATION).valueForKey("toIndividuResp")));
    // on ajoute le chef de service que s'il faut lui envoyer un mail
  	if (!shouldIgnoreCdsNoMail) {
    	if (chefDeService != null && individu.persId().intValue() != chefDeService.persId().intValue()) {
    		if (EODroitPasDeMailCds.findRecordForStructure(edc, structure) == null)
    			recsResponsables = recsResponsables.arrayByAddingObject(chefDeService);
      }
  	} else {
  		// sinon on l'ajoute quoi qu'il en soit
  		if (chefDeService != null) {
    		recsResponsables = recsResponsables.arrayByAddingObject(chefDeService);
  		}
  	}
  	return recsResponsables;
	}
	
	/**
	 * Donne la liste des delegues du planning concerne.
	 * @param edc
	 * @param affAnn : l'enregistrement <code>EOAffectationAnnuelle</code> concerne.
	 * @return un <code>NSArray</code> de <code>EOIndividu</code>.
	 */
	public NSArray findDeleguePlanning(EOEditingContext edc, EOAffectationAnnuelle affAnn) {
	  EOStructure structure = affAnn.structure();
	  EOIndividu chefDeService = affAnn.structure().responsable();
		EOIndividu individu = affAnn.individu();
		NSArray recsDelegues = new NSArray();
  	if (chefDeService != null && individu.persId().intValue() == chefDeService.persId().intValue()) {
    	recsDelegues = (NSArray) findDroitsForStructureAndIndividuForCdrTypeAndNiveau(
          edc, structure, null, ConstsDroit.DROIT_CIBLE_CHEF_DE_SERVICE,
          ConstsDroit.DROIT_NIVEAU_DELEGATION).valueForKey("toIndividuResp");
    } else {
    	recsDelegues = (NSArray) findDroitsForStructureAndIndividuForCdrTypeAndNiveau(
          edc, structure, null, ConstsDroit.DROIT_CIBLE_SERVICE,
          ConstsDroit.DROIT_NIVEAU_DELEGATION).valueForKey("toIndividuResp");
    }
    // ajout de toute personne qui a la delegation sur l'individu
  	recsDelegues = recsDelegues.arrayByAddingObjectsFromArray(
    		(NSArray) (findDroitsForStructureAndIndividuForCdrTypeAndNiveau(
    				edc, structure, individu, ConstsDroit.DROIT_CIBLE_INDIVIDU,
    				ConstsDroit.DROIT_NIVEAU_DELEGATION).valueForKey("toIndividuResp")));
  	return recsDelegues;
	}
	

  
  // methodes de reconnaissance du niveau de droit par rapport
  // a ce qui est possible en terme de niveau de validation
  
  /**
   * Indique si le niveau passe en parametre est au moins comme un niveau de valideur
   */
  public boolean isMinimumNiveauValidation(Number niveau) {
  	boolean result = false;
  	for (int i=0; i<ConstsDroit.LIST_NIVEAU_DROIT_ALERTE_VALIDATION.length; i++) {
  		if (niveau.intValue() == ConstsDroit.LIST_NIVEAU_DROIT_ALERTE_VALIDATION[i].intValue()) {
  			result = true;
  			break;
  		}
  	}
  	return result;
  }

  /**
   * Indique si le niveau passe en parametre est au moins comme un niveau de viseur
   */
  public boolean isMinimumNiveauVisa(Number niveau) {
  	boolean result = false;
  	for (int i=0; i<ConstsDroit.LIST_NIVEAU_DROIT_ALERTE_VISA.length; i++) {
  		if (niveau.intValue() == ConstsDroit.LIST_NIVEAU_DROIT_ALERTE_VISA[i].intValue()) {
  			result = true;
  			break;
  		}
  	}
  	return result;
  }

  /**
   * Indique si le niveau passe en parametre est au moins comme un niveau de visualiseur
   */
  public boolean isMinimumNiveauVisu(Number niveau) {
  	boolean result = false;
  	for (int i=0; i<ConstsDroit.LIST_NIVEAU_DROIT_VISU.length; i++) {
  		if (niveau.intValue() == ConstsDroit.LIST_NIVEAU_DROIT_VISU[i].intValue()) {
  			result = true;
  			break;
  		}
  	}
  	return result;
  }
  
  /**
   * Donne la liste des services cibles par un droit. Tout dépend du niveau passé
   * en paramètre, s'il est héritable ou non. Pour les niveau adm de composante ou
   * drh de composante, on doit aussi traiter les services fils
   * @return
   */
  public NSArray processServiceListForNiveau(NSArray serviceList, Number niveau) {
  	NSArray result = new NSArray();

  	if (niveau == ConstsDroit.DROIT_NIVEAU_ADM_COMPOSANTE || niveau == ConstsDroit.DROIT_NIVEAU_DRH_COMPOSANTE) {
  		for (int i=0; i<serviceList.count(); i++) {
  			EOStructure service = (EOStructure) serviceList.objectAtIndex(i);
  			result = result.arrayByAddingObject(service);
  			result = result.arrayByAddingObjectsFromArray(service.tosServiceFils());
  		}
  	} else {
  		result = new NSArray(serviceList);
  	}  	
		
  	return result;
  }
  
}
