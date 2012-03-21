package fr.univlr.cri.conges;


import java.util.Enumeration;

import com.webobjects.appserver.*;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOClassDescription;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.YCRIWebPage;
import fr.univlr.cri.conges.constantes.ConstsDroit;
import fr.univlr.cri.conges.databus.CngDroitBus;
import fr.univlr.cri.conges.eos.modele.conges.EODroit;
import fr.univlr.cri.conges.eos.modele.conges.EODroitPasDeMailCds;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOMouvement;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.ycrifwk.utils.UtilDb;

/**
 * Ecran de gestion des typages des plannings (droits 
 * a conges, depassement...)
 * 
 *  + Ecran de gestion des droits d'acces et d'utilisation
 * de l'application pour l'administrateur global.
 * 
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */
public class AdminDroitGestion extends YCRIWebPage {

	public NSArray lesAffectationsAnnuelles;
	public EOAffectationAnnuelle uneAffectationAnnuelle;
	public EOAffectationAnnuelle uneAffectationAnnuelleSelectionnee;
	
	public NSArray lesIndividus;
	public EOIndividu unIndividu;
	public NSArray lesIndividusSelectionnes;

  // liste des droits affich�s
  public EODroit unDroit;
  public NSArray lesDroitsSelectionnes;
  
  // popup des types de droits (init avec le type service)
  public NSArray cibleList = new NSArray(new String[]{
  		ConstsDroit.DROIT_CIBLE_SERVICE, ConstsDroit.DROIT_CIBLE_CHEF_DE_SERVICE, ConstsDroit.DROIT_CIBLE_INDIVIDU});
  public String cibleSelected = ConstsDroit.DROIT_CIBLE_SERVICE;
  public String cibleItem;
  
  // popup des niveau des droits (init avec le niveau valideur)
  public NSArray niveauList = new NSArray(new Integer[]{
  		ConstsDroit.DROIT_NIVEAU_VISU, ConstsDroit.DROIT_NIVEAU_VISA, ConstsDroit.DROIT_NIVEAU_VALIDATION, 
  		ConstsDroit.DROIT_NIVEAU_ADM_COMPOSANTE, ConstsDroit.DROIT_NIVEAU_DRH_COMPOSANTE, ConstsDroit.DROIT_NIVEAU_DELEGATION});
  public Integer niveauSelected = ConstsDroit.DROIT_NIVEAU_VALIDATION;
  public Integer niveauItem;

  
  /** le choix du type de cible est autorise ou non */
  public Boolean isDisabledRadioCible = Boolean.FALSE;
   
  public AdminDroitGestion(WOContext context) {
    super(context);
  }
 
	public boolean isUneAffectationSelected(){
		return uneAffectationAnnuelleSelectionnee != null;
	}
	
	/**
	 * @return
	 * @throws Throwable 
	 */
	public WOComponent validerProfil() throws Throwable {
		if (isUneAffectationSelected()) {
			UtilDb.save(edc, true);
			// on force le rechargement du planning s'il était en session
			laSession.clearCachePlanningForAffectationAnnuelle(uneAffectationAnnuelleSelectionnee);
		}
	  return null;
	}
	
	/**
	 * methode qui revert
	 * @return
	 */
	public WOComponent revert() {
		edc.revert();
		return null;		
	}
	
	/**
	 * methode qui refresh
	 */
	public WOComponent refresh() {
		return null;		
	}
	
	/**
	 * Savoir si un service a �t� selectionn� dans le browser
	 * @return
	 */
	public boolean isUnServiceAutoriseSelectionne() {
		return leServiceAutoriseSelectionne != null;
	}

	public WOComponent gererLesValidations() {
		return null;
	}

	public EOStructure leServiceAutoriseSelectionne;
  
	
	//
	
	/**
	 * checkbox d'envoi de mail au CDS
	 */ 
	public boolean envoiMail() {
		return EODroitPasDeMailCds.findRecordForStructure(edc, leServiceAutoriseSelectionne) == null;
	}

	/**
	 * checkbox d'envoi de mail au CDS
	 */ 
	public void setEnvoiMail(boolean value) {
	  EODroitPasDeMailCds droit = EODroitPasDeMailCds.findRecordForStructure(edc, leServiceAutoriseSelectionne);
	  if (value == true) {
	    if (droit != null)
	      edc.deleteObject(droit);
	  } else {
	    if (droit == null)
	      droit = EODroitPasDeMailCds.newDroitPasDeMail(edc, leServiceAutoriseSelectionne);
	  }
	}

		
	public String nomPrenom;
	public EOIndividu lIndividuSelectionne;
  
  /**
   * trouver tous les individus qui match le critere de recherche
   */
  public WOComponent rechercher() {
    if (!StringCtrl.isEmpty(nomPrenom))
      lesIndividus = EOIndividu.findIndividuForNomOrPrenom(edc, nomPrenom, false);
    return neFaitRien();
  }
 
  
  /**
   * Lors de la selection du type de cible selectionnee :
   * - composante (deduction du niveau)
   * - tout le service
   * - seulement le chef
   * - uniquement la personne selectionnee
   * il faut afficher l'appelation de cette cible
   */
  public String cdrCibleTypeDisplay() {
  	if (isNivAdmComposante() || isNivDrhComposante()) {
  		return "Composante : " + leServiceAutoriseSelectionne.toComposante().display();
  	}
  	
  	if (isCibleService())
  		return "Service : " + leServiceAutoriseSelectionne.display();
  	else if (isCibleChef()) {
  		if (leServiceAutoriseSelectionne.responsable() != null) {
  			return "Chef de service : " + leServiceAutoriseSelectionne.responsable().nomComplet();
  		} else {
  			return "** pas de chef de service déclaré ! **";
  		}
  	} else if (isCibleIndividu()) {
  		if (isUneAffectationSelected()) {
  			return "Agent ciblé : " + uneAffectationAnnuelleSelectionnee.individu().nomComplet();
  		} else {
  			return "** aucun agent selectionné **";
  		}
  	}
  	return "** impossible a déterminer **";
  }
  
  /**
   * Affichage du libelle complet du type de cible dans le popup
   * @return
   */
  public String cibleItemDisplay() {
  	if (ConstsDroit.DROIT_CIBLE_CHEF_DE_SERVICE.equals(cibleItem)) {
  		return ConstsDroit.DROIT_CIBLE_CHEF_DE_SERVICE_LABEL; 
  	} else if (ConstsDroit.DROIT_CIBLE_INDIVIDU.equals(cibleItem)) {
  		return ConstsDroit.DROIT_CIBLE_INDIVIDU_LABEL; 
  	} else {
  		return ConstsDroit.DROIT_CIBLE_SERVICE_LABEL;
  	} 
  }   
  
  /**
   * Affichage du commentaire complet du type cible dans le popup
   * @return
   */
  public String cibleSelectedComment() {
  	if (ConstsDroit.DROIT_CIBLE_CHEF_DE_SERVICE.equals(cibleSelected)) {
  		return ConstsDroit.DROIT_CIBLE_CHEF_DE_SERVICE_COMMENT; 
  	} else if (ConstsDroit.DROIT_CIBLE_INDIVIDU.equals(cibleSelected)) {
  		return ConstsDroit.DROIT_CIBLE_INDIVIDU_COMMENT; 
  	} else {
  		return ConstsDroit.DROIT_CIBLE_SERVICE_COMMENT;
  	} 
  }  
  
  /**
   * Affichage du libelle complet du niveau de droit dans le popup
   * @return
   */
  public String niveauItemDisplay() {
  	if (ConstsDroit.DROIT_NIVEAU_VISU.equals(niveauItem)) {
  		return ConstsDroit.DROIT_NIVEAU_VISU_LABEL; 
  	} else if (ConstsDroit.DROIT_NIVEAU_VISA.equals(niveauItem)) {
  		return ConstsDroit.DROIT_NIVEAU_VISA_LABEL; 
  	} else if (ConstsDroit.DROIT_NIVEAU_VALIDATION.equals(niveauItem)) {
  		return ConstsDroit.DROIT_NIVEAU_VALIDATION_LABEL; 
  	} else if (ConstsDroit.DROIT_NIVEAU_ADM_COMPOSANTE.equals(niveauItem)) {
  		return ConstsDroit.DROIT_NIVEAU_ADM_COMPOSANTE_LABEL; 
  	} else if (ConstsDroit.DROIT_NIVEAU_DRH_COMPOSANTE.equals(niveauItem)) {
  		return ConstsDroit.DROIT_NIVEAU_DRH_COMPOSANTE_LABEL; 
  	} else {
  		return ConstsDroit.DROIT_NIVEAU_DELEGATION_LABEL;
  	} 
  }
  
  
  /**
   * Affichage du commentaire complet du niveau de droit dans le popup
   * @return
   */
  public String niveauSelectedComment() {
  	if (ConstsDroit.DROIT_NIVEAU_VISU.equals(niveauSelected)) {
  		return ConstsDroit.DROIT_NIVEAU_VISU_COMMENT; 
  	} else if (ConstsDroit.DROIT_NIVEAU_VISA.equals(niveauSelected)) {
  		return ConstsDroit.DROIT_NIVEAU_VISA_COMMENT; 
  	} else if (ConstsDroit.DROIT_NIVEAU_VALIDATION.equals(niveauSelected)) {
  		return ConstsDroit.DROIT_NIVEAU_VALIDATION_COMMENT; 
  	} else if (ConstsDroit.DROIT_NIVEAU_ADM_COMPOSANTE.equals(niveauSelected)) {
  		return ConstsDroit.DROIT_NIVEAU_ADM_COMPOSANTE_COMMENT; 
  	} else if (ConstsDroit.DROIT_NIVEAU_DRH_COMPOSANTE.equals(niveauSelected)) {
  		return ConstsDroit.DROIT_NIVEAU_DRH_COMPOSANTE_COMMENT; 
  	} else {
  		return ConstsDroit.DROIT_NIVEAU_DELEGATION_COMMENT; 
  	} 
  }
  
  // ** partie ajout des valideurs - viseurs - visualisateur **
	
  /**
   * Ajouter le droit selectionne. Les parametres <code>niveauDroit</code>,
   * <code>lIndividuSelectionne</code>, <code>leServiceAutoriseSelectionn</code>
   * et <code>cdrType</code> sont utilises dans cette methode.
   * @throws Throwable 
   */
  public WOComponent ajoutDroit() throws Throwable {
	  if (lIndividuSelectionne != null) {
	    EODroit newDroit = new EODroit();
	    newDroit.addObjectToBothSidesOfRelationshipWithKey(lIndividuSelectionne, "toIndividuResp");
	    newDroit.setNoIndividuResp(new Integer(lIndividuSelectionne.oid().intValue()));
	    // attention si c'est un droit pour une composante
	    EOStructure recService = leServiceAutoriseSelectionne;
	    if (isNivAdmComposante() || isNivDrhComposante()) {
	    	recService = leServiceAutoriseSelectionne.toComposante();
	    }
	    newDroit.setCStructure((String)(EOUtilities.primaryKeyForObject(edc, recService)).objectForKey("oid"));
	    newDroit.setToStructureRelationship(recService);
	    newDroit.setCdrNiveau(niveauSelected);
	    newDroit.setCdrType(cibleSelected);

	    if (isCibleIndividu()) {
	      EOIndividu individuCible = uneAffectationAnnuelleSelectionnee.individu();
	      newDroit.addObjectToBothSidesOfRelationshipWithKey(individuCible, "toIndividu");
	      newDroit.setNoIndividu(new Integer(individuCible.oid().intValue()));
	    } else {
		    // bidouille pour les primary key : si pas individuelle, on met l'individu connecte 
		    // (c juste pour avoir un lien, ce ne sera jamais utilise)
	      newDroit.addObjectToBothSidesOfRelationshipWithKey(laSession.individuConnecte(), "toIndividu");
	      newDroit.setNoIndividu(new Integer(laSession.individuConnecte().oid().intValue()));
	    }
	    
	    edc.insertObject(newDroit);
	  }
    UtilDb.save(edc, true);

	  return null;
  }
  
	
	/**
	 * La liste des enregistrement <code>EODroit</code> associes
	 * a la selection du formulaire
	 */
	public NSArray lesDroitsExistants() {
	  EOIndividu individu = null;
	  if (isUneAffectationSelected() && isCibleIndividu())
	    individu = uneAffectationAnnuelleSelectionnee.individu();
	  NSArray recsDroit = droitBus().findDroitsForStructureAndIndividuForCdrTypeAndNiveau(
	      edc, 
	      leServiceAutoriseSelectionne,
	      individu,
	      cibleSelected, 
	      niveauSelected);
	  return EOSortOrdering.sortedArrayUsingKeyOrderArray(recsDroit, ConstsDroit.orderingsIndividu);
	}
  
	/**
	 * Les droits qui sont deduits, que l'utilisateur ne peut pas modifier
	 * directement. Ils d�pendent du niveau demande et du type de cible :
	 * - niveau validation
	 * 	 . cible service : responsable dans l'annuaire
	 *   . cible chef : N/A
	 *   . cible individu : responsable annuaire + les droits validation "cible service" existants
	 * - niveau visa 
	 *   . cible service : N/A
	 *   . cible chef : 		les droits visa "cible service" existants
	 *   . cible individu : les droits visa "cible service" existants
	 * @return
	 */
  public NSArray lesDroitsReadOnly() {
  	NSArray lesDroits = new NSArray();
  	// niveau validation
  	if (isNivValidation()) {
  		if (!isCibleChef()) {
	  		// EODroit bidon pour gerer le chef dans l'annuaire
	      EODroit droitAnnuaire = new EODroit();
  			if (leServiceAutoriseSelectionne.responsable() != null) {
  	      droitAnnuaire = new EODroit();
  		    droitAnnuaire.addObjectToBothSidesOfRelationshipWithKey(leServiceAutoriseSelectionne.responsable(), "toIndividuResp");
  		    droitAnnuaire.setNoIndividuResp(new Integer(leServiceAutoriseSelectionne.responsable().oid().intValue()));
  		    droitAnnuaire.setCStructure((String)(EOUtilities.primaryKeyForObject(edc, leServiceAutoriseSelectionne)).objectForKey("oid"));
  		    droitAnnuaire.setCdrNiveau(ConstsDroit.DROIT_NIVEAU_VALIDATION);
  		    droitAnnuaire.setCdrType(cibleSelected);
  		    droitAnnuaire.addObjectToBothSidesOfRelationshipWithKey(leServiceAutoriseSelectionne.responsable(), "toIndividu");
  		    droitAnnuaire.setNoIndividu(new Integer(leServiceAutoriseSelectionne.responsable().oid().intValue()));
  			}
				if (isCibleService()) {
					if (droitAnnuaire != null) {
						lesDroits = lesDroits.arrayByAddingObjectsFromArray(new NSArray(droitAnnuaire));
					}
				} else if (isCibleIndividu()) {
					if (droitAnnuaire != null) {
						lesDroits = lesDroits.arrayByAddingObjectsFromArray(new NSArray(droitAnnuaire));
					}
				 	String prevCrdType = cibleSelected;
				 	cibleSelected = ConstsDroit.DROIT_CIBLE_SERVICE;
					lesDroits = lesDroits.arrayByAddingObjectsFromArray(lesDroitsExistants());
					cibleSelected = prevCrdType;
				} 
  		}
  	}
		// niveau visa / visu
		else if (isNivVisa() || isNivVisu() || isNivDelegation()) {
	    if (isCibleChef() || isCibleIndividu()) {
	    	String prevCrdType = cibleSelected;
	    	cibleSelected = ConstsDroit.DROIT_CIBLE_SERVICE;
        lesDroits = lesDroits.arrayByAddingObjectsFromArray(lesDroitsExistants());
        cibleSelected = prevCrdType;
	    } 	
		}
  	return lesDroits;
  }
  
  /**
   * Quelques methodes pour alleger le code
   */
  public boolean isCibleService() 	{  	return ConstsDroit.DROIT_CIBLE_SERVICE.equals(cibleSelected); }
  public boolean isCibleChef() 			{		return ConstsDroit.DROIT_CIBLE_CHEF_DE_SERVICE.equals(cibleSelected);}
  public boolean isCibleIndividu() 	{  	return ConstsDroit.DROIT_CIBLE_INDIVIDU.equals(cibleSelected); }
  
  public boolean isNivDrhComposante()	{		return niveauSelected == ConstsDroit.DROIT_NIVEAU_DRH_COMPOSANTE; }
  public boolean isNivAdmComposante()	{		return niveauSelected == ConstsDroit.DROIT_NIVEAU_ADM_COMPOSANTE; }
  public boolean isNivValidation()		{		return niveauSelected == ConstsDroit.DROIT_NIVEAU_VALIDATION; }
  public boolean isNivVisa()					{		return niveauSelected == ConstsDroit.DROIT_NIVEAU_VISA; }
  public boolean isNivVisu()					{		return niveauSelected == ConstsDroit.DROIT_NIVEAU_VISU; }
  public boolean isNivDelegation()		{		return niveauSelected == ConstsDroit.DROIT_NIVEAU_DELEGATION; }
    
  /**
   * Effacer les enregistrement <code>EODroit</code> selectionn�s.
   * @throws Throwable 
   */
  public WOComponent supprimerDroit() throws Throwable {
		if (lesDroitsSelectionnes!=null && lesDroitsSelectionnes.count()>0) {
			Enumeration enumLesResponsables = lesDroitsSelectionnes.objectEnumerator();
			while (enumLesResponsables.hasMoreElements()) {
				EODroit unDroit = (EODroit) enumLesResponsables.nextElement();
				edc.deleteObject(unDroit);
			}
	    UtilDb.save(edc, true);

		}
		return null;
  }
  
  /**
   * Surcharge de ce setter pour eventuellement
   * griser certains choses et changer des selections
   */
  public void setNiveauSelected(Integer value) {
  	niveauSelected = value;
  	// si adm ou drh de composante, on permet pas le changement de type de cible
  	if (niveauSelected == ConstsDroit.DROIT_NIVEAU_ADM_COMPOSANTE || 
  			niveauSelected == ConstsDroit.DROIT_NIVEAU_DRH_COMPOSANTE) {
  		isDisabledRadioCible = Boolean.TRUE;
  		// on selectionne la cible service
  		cibleSelected = ConstsDroit.DROIT_CIBLE_SERVICE;
  	} else {
  		isDisabledRadioCible = Boolean.FALSE;
  	}
  }
  
  /**
   * L'administrateur restreint n'a pas acces a la gestion
   * des droits individuels
   */
  public boolean isAdminGlobal() {
  	return laSession.cngUserInfo().isAdmin();
  }
  
  // les bus de donnees
  
  private CngDroitBus droitBus() {
  	return laSession.cngDataCenter().droitBus();
  }

  
  // gestion des régularisations de solde
  
  public EOMouvement regularisationItem;
  public EOMouvement regularisationSelected;

  /**
   * Demander l'inspection de la régularisation {@link #regularisationItem}
   */
  public WOComponent doSelectRegularisationItem() {
  	regularisationSelected = regularisationItem;
    return null;
  }
  
  /**
   * Demander l'ajout d'un nouvelle régularisation de solde
   * @return
   */
  public WOComponent toAddNewRegularisation() {
  	EOClassDescription newMouvementCD = EOClassDescription.classDescriptionForEntityName(EOMouvement.ENTITY_NAME);
  	regularisationSelected = (EOMouvement) newMouvementCD.createInstanceWithEditingContext(null, null);
  	regularisationSelected.setMouvementType(EOMouvement.TYPE_REGULARISATION_SOLDE_CONGES);
  	regularisationSelected.setToAffectationAnnuelleRelationship(uneAffectationAnnuelleSelectionnee);
  	regularisationSelected.setMouvementMinutes(new Integer(0));
  	regularisationSelected.setMouvementLibelle("Régularisation de solde");
  	return null;
  }
  
  /**
   * Enregistrer l'ajout / édition d'une régularisation de solde
   * @return
   * @throws Throwable 
   */
  public WOComponent doSaveRegularisation() throws Throwable {
  	if (isUneNouvelleRegularisation()) {
    	edc.insertObject(regularisationSelected);
  	}
  	validerProfil();
  	regularisationSelected = null;
  	return null;
  }

  /**
   * Indique si on est mode ajout ou edition d'une régularisation
   * @return
   */
  public boolean isUneNouvelleRegularisation() {
    return (edc.globalIDForObject(regularisationSelected) == null);
  }
  
  /**
   * Indique si une régularisation de solde a été selectionnée
   * @return
   */
  public boolean isARegularisationSelected() {
  	return regularisationSelected != null;
  }

  /**
   * Supprimer la régularisation {@link #regularisationSelected}
   * @return
   * @throws Throwable
   */
  public WOComponent doDelRegularisationSelected() throws Throwable {
  	edc.deleteObject(regularisationSelected);
  	regularisationSelected = null;
  	validerProfil();
    return null;
  }

  /**
   * Annuler l'opération d'ajout / modif de régulariation
   * @return
   */
  public WOComponent doCancelRegularisationSelected() {
  	regularisationSelected = null;
  	edc.revert();
    return null;
  }
}