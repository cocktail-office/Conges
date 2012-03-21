package fr.univlr.cri.conges.databus;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableDictionary;

import fr.univlr.cri.conges.Application;
import fr.univlr.cri.conges.constantes.ConstsDroit;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.webapp.*;

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
 * Informations sur l'utilisateur de l'application Conges
 * 
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */

public class CngUserInfo extends LRUserInfoDB {

	private boolean isAdmin, isDrh, isAdmComposante, isDrhComposante;
	private Application app;
	private EOIndividu recIndividu;
	private Number persId;

	/** liste des composante dont il est DRH */
	private NSArray listDrhComposante;
	
	/** liste des composante dont il est Admin*/
	private NSArray listAdmComposante;
	
	/** */
	private EOEditingContext ec;
	
	/** 
	 * dico rassemblant la liste des planning pour lequels il est responsable (niveau 3).
	 * Son format est :
	 * - cle : PK de l'entite <code>EOAffectationAnnuelle</code>
	 * - value : <code>Boolean</code> Indique si oui non il est responsable.
	 */
	private NSMutableDictionary dicoValidationPlanning = new NSMutableDictionary();
	
	/** 
	 * dico rassemblant la liste des planning pour lequels il est delegue (niveau 6).
	 * Son format est :
	 * - cle : PK de l'entite <code>EOAffectationAnnuelle</code>
	 * - value : <code>Boolean</code> Indique si oui non il est delegue.
	 */
	private NSMutableDictionary dicoDelegationPlanning = new NSMutableDictionary();
	
	/** */
	private Boolean isMailRecipisse;
	private Boolean isMailRecipisseDelegation;
	
	/**
	 * Constructeur avec un objet <code>EOIndividu</code>
	 * @param aDroitBus
	 * @param aPreferenceBus
	 * @param anEc
	 * @param aPersId : le persId 
	 */
  public CngUserInfo(CngDroitBus aDroitBus, CngPreferenceBus aPreferenceBus, EOEditingContext anEc, Number aPersId) {
   	super(aDroitBus);
   	persId = aPersId;
  	app = (Application)Application.application();
  	ec = anEc;
  	recIndividu = EOIndividu.findIndividuForPersIdInContext(ec, persId);
   	individuForPersId(persId, true);
	  // determiner s'il est admin
  	isAdmin = recIndividu.belongStructureRepartStructure(app.cStructureAdmin());
	  // idem pour les drh
 		isDrh = recIndividu.belongStructureRepartStructure(app.cStructureDrh());
 		// admin de composante
 		listAdmComposante = (NSArray) aDroitBus.findDroitsForResponsableAndCdrTypeAndNiveau(
 				ec, recIndividu(), ConstsDroit.DROIT_CIBLE_SERVICE, 
 				ConstsDroit.DROIT_NIVEAU_ADM_COMPOSANTE).valueForKey("toStructure");
 		isAdmComposante = (listAdmComposante.count() > 0);
 		// drh de composante
 		listDrhComposante = (NSArray) aDroitBus.findDroitsForResponsableAndCdrTypeAndNiveau(
 				ec, recIndividu(), ConstsDroit.DROIT_CIBLE_SERVICE, 
 				ConstsDroit.DROIT_NIVEAU_DRH_COMPOSANTE).valueForKey("toStructure");
 		isDrhComposante = (listAdmComposante.count() > 0);
 		//
 		isMailRecipisse = Boolean.FALSE;
 		isMailRecipisseDelegation = Boolean.TRUE;
 		aPreferenceBus.loadPreferences(this);
  }
  
  public boolean isAdmin() {
  	return isAdmin;
  }

  public boolean isDrh() {
		return isDrh;
	}
  
  public boolean isAdmComposante() {
  	return isAdmComposante;
  }

  public boolean isDrhComposante() {
		return isDrhComposante;
	}

	public EOIndividu recIndividu() {
		return recIndividu;
	}

	/**
	 * La liste des composantes dont il a le droit
	 * de DRH local
	 */
	public NSArray getListDrhComposante() {
		return listDrhComposante;
	}

	/**
	 * La liste des composantes dont il a le droit
	 * d'administration
	 */
	public NSArray getListAdmComposante() {
		return listAdmComposante;
	}
		
	/**
	 * Indique si l'agent connecte possede la delegation sur un planning.
	 * On utilise du cache pour accelerer les traitements
	 * @param affAnn
	 * @return
	 */
	public boolean isDeleguePlanning(EOAffectationAnnuelle affAnn) {
		String pk = String.valueOf(affAnn.oid().intValue());
		Boolean result = (Boolean) dicoDelegationPlanning.objectForKey(pk);
		if (result == null) {
			result = new Boolean(
					((CngDroitBus) dataBus()).findDeleguePlanning(ec, affAnn).containsObject(recIndividu()));
			dicoDelegationPlanning.setObjectForKey(result, pk);
		}
		return result.booleanValue();
	}

	/**
	 * Indique si l'agent connecte a le droit de modifier le planning :
	 * - soit c'est le proprietaire
	 * - soit c'est un delegue
	 * @param affAnn
	 * @return
	 */
	public boolean isAllowedModifyPlanning(EOAffectationAnnuelle affAnn) {
		return affAnn.individu().persId().intValue() == persId().intValue() || isDeleguePlanning(affAnn);
	}
	
	/**
	 * Indique si l'agent possede les droits de DRH de la composante
	 * du planning passe en parametre
	 */
	public boolean isDrhComposanteForPlanning(EOAffectationAnnuelle affAnn) {
		return affAnn.structure().toComposante() != null && 
			getListDrhComposante().containsObject(affAnn.structure().toComposante());
	}
	
	/**
	 * Indique si l'agent possede les droits d'administrateur de la composante
	 * du planning passe en parametre
	 */
	public boolean isAdmComposanteForPlanning(EOAffectationAnnuelle affAnn) {
		return affAnn.structure().toComposante() != null && 
			getListAdmComposante().containsObject(affAnn.structure().toComposante());
	}
	
	/**
	 * Indique si l'agent possede les droits de DRH de la composante
	 * du service passe en parametre
	 */
	public boolean isDrhComposanteForService(EOStructure structure) {
		return structure.toComposante() != null && 
			getListDrhComposante().containsObject(structure.toComposante());
	}
	
	/**
	 * Indique si l'agent possede les droits d'administrateur de la composante
	 * du service passe en parametre
	 */
	public boolean isAdmComposanteForService(EOStructure structure) {
		return structure.toComposante() != null && 
			getListAdmComposante().containsObject(structure.toComposante());
	}
	
	// informations relatives au preferences

	public void setIsMailRecipisse(boolean value) {
		isMailRecipisse = new Boolean(value);
	}
	
	public void setIsMailRecipisseDelegation(boolean value) {
		isMailRecipisseDelegation = new Boolean(value);
	}
	
	/**
	 * Indique si l'utilisateur souhaite recevoir les mails de recipisse
	 * lors d'une validation par un tiers d'un planning / occupation
	 * concernnat un agent dont il a la responsabilit�.
	 */
	public boolean isMailRecipisse() {
		return isMailRecipisse.booleanValue();
	}	
	
	/**
	 * Indique si l'utilisateur souhaite recevoir les mails de recipisse
	 * lors d'une validation par un tiers d'un planning / occupation
	 * pour une demande realisee par delegation (par lui pour un autre)
	 */
	public boolean isMailRecipisseDelegation() {
		return isMailRecipisseDelegation.booleanValue();
	}
}
