/*
 * Copyright COCKTAIL (www.cocktail.org), 1995, 2010 This software
 * is governed by the CeCILL license under French law and abiding by the
 * rules of distribution of free software. You can use, modify and/or
 * redistribute the software under the terms of the CeCILL license as
 * circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * As a counterpart to the access to the source code and rights to copy, modify
 * and redistribute granted by the license, users are provided only with a
 * limited warranty and the software's author, the holder of the economic
 * rights, and the successive licensors have only limited liability. In this
 * respect, the user's attention is drawn to the risks associated with loading,
 * using, modifying and/or developing or reproducing the software by the user
 * in light of its specific status of free software, that may mean that it
 * is complicated to manipulate, and that also therefore means that it is
 * reserved for developers and experienced professionals having in-depth
 * computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling
 * the security of their systems and/or data to be ensured and, more generally,
 * to use and operate it in the same conditions as regards security. The
 * fact that you are presently reading this means that you have had knowledge
 * of the CeCILL license and that you accept its terms.
 */
package fr.univlr.cri.conges;

import com.webobjects.appserver.WOComponent;

import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.*;

import fr.univlr.cri.conges.constantes.ConstsDroit;
import fr.univlr.cri.conges.constantes.ConstsMenu;
import fr.univlr.cri.conges.databus.CngDataCenter;
import fr.univlr.cri.conges.databus.CngDroitBus;
import fr.univlr.cri.conges.databus.CngPlanningBus;
import fr.univlr.cri.conges.databus.CngPreferenceBus;
import fr.univlr.cri.conges.databus.CngUserInfo;
import fr.univlr.cri.conges.eos.modele.conges.*;
import fr.univlr.cri.conges.eos.modele.grhum.*;
import fr.univlr.cri.conges.eos.modele.planning.*;
import fr.univlr.cri.conges.objects.*;
import fr.univlr.cri.conges.objects.finder.FinderResponsabilite;
import fr.univlr.cri.conges.objects.occupations.CongesCompensateurs;
import fr.univlr.cri.conges.objects.occupations.CongespourCet;
import fr.univlr.cri.conges.objects.occupations.DechargeSyndicale;
import fr.univlr.cri.conges.objects.occupations.HeuresSupplementaires;
import fr.univlr.cri.conges.objects.occupations.Occupation;
import fr.univlr.cri.conges.print.CngPdfBoxCtrl;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.util.wo5.DateCtrl;
import fr.univlr.cri.webapp.CRIWebSession;
import fr.univlr.cri.webapp.LRLog;
import fr.univlr.cri.webapp.LRUserInfoDB;
import fr.univlr.cri.webext.CRIAlertPage;

public class Session 
	extends CRIWebSession 
		implements I_ClasseMetierNotificationParametre {
	
  // remplissage du popup de selection de la structure
  private NSArray structuresDispoParNiveau;
  // remplissage du popup de selection de la structure (selection)
  private Responsabilite structuresDispoParNiveauSelection;
  
  // verif version du navigateur
  private boolean isBadNavigator;
  	 
  // desactivation du menu du haut (ex : saisie d'un horaire)
  private boolean isDisabledMenu = false;
  
  // cache des plannings deja charges
  private NSMutableDictionary dicoLoadedPlannings;
  
  // afficher le menu CET 
  private static boolean isShowCet;
  
  // afficher le menu d'édition garde d'enfant
  private static boolean isShowDemandeGardeEnfant;
  
  public Session() {
    super();
    app = (Application)Application.application();    
    setDateRef(DateCtrlConges.now());
    dicoLoadedPlannings = new NSMutableDictionary();
    log("open session");
  }
  
	/**
	 * @see I_ClasseMetierNotifiableParametre
	 */
	public static void initStaticField(Parametre parametre) {
		if (parametre == Parametre.PARAM_SHOW_CET) {
			isShowCet = parametre.getParamValueBoolean().booleanValue();
		} else if (parametre == Parametre.PARAM_SHOW_DEMANDE_GARDE_ENFANT) {
			isShowDemandeGardeEnfant = parametre.getParamValueBoolean().booleanValue();
		}
  }
      
  private Application app = null;
  public Application app()      {   return app;}

  // toutes les affectation annuelles de l'individu connecte
  private NSArray _persoAffAnnsPerso;
  public void setAffAnnsPerso(NSArray value) 		{  	_persoAffAnnsPerso = value;  }  
  public NSArray affAnnsPerso()                 {  	return _persoAffAnnsPerso;  }
	
	//TODO forcer le recalcul des occupations a cause des congés
	// a cheval sur 2 années universitaire. La valeur prend celle
	// du dernier planning chargé ... voir pour separer ces 2 valeurs
	// en base
  private boolean shouldRecalculerOccupation = false;
  // l'affectation annuelle en cours
  private EOAffectationAnnuelle _selectedAffAnnPerso;
  public void setSelectedAffAnnPerso(EOAffectationAnnuelle value)  	{   
  	_selectedAffAnnPerso = value;
  	// indiquer que le planning changer et donc on doit recalculer les debits des occupations
  	shouldRecalculerOccupation = true;
  }
  public EOAffectationAnnuelle selectedAffAnnPerso()   							{   return _selectedAffAnnPerso; }
  
  /**
   * La cle de stockage d'un planning dans le
   * dictionnaire du cache : <noIndividu>-<cStructure>-<anneeUniv>
   */
  private String getSelectedAffAnnPersoDicoKey(EOAffectationAnnuelle affAnn) {
    return affAnn.individu().oid() + "-" + affAnn.structure().cStructure() + "-" +	affAnn.annee();
  }
  
  /**
   * Obtenir le planning en cours (d'apres l'affectation annuelle en cours)
   * on charge le planning a la demande. S'il est deja charge, on le retourne
   */
  public Planning getLePlanningSelectionne() {    
    return getCachePlanningForAffAnn(selectedAffAnnPerso());
  }
  
  /**
   * Obtenir un planning a partir d'une affectation annuelle
   * @param affAnn
   * @return
   */
  public Planning getCachePlanningForAffAnn(EOAffectationAnnuelle affAnn) {
    Planning planning = (Planning) dicoLoadedPlannings.objectForKey(
    		getSelectedAffAnnPersoDicoKey(affAnn));
    if (planning == null) {
      planning = Planning.newPlanning(affAnn, cngUserInfo(), dateRef());
      dicoLoadedPlannings.setObjectForKey(planning, getSelectedAffAnnPersoDicoKey(affAnn));
      shouldRecalculerOccupation = false;
    } else {
    	//TODO forcer le recalcul des occupations a cause des congés
    	// a cheval sur 2 années universitaire. La valeur prend celle
    	// du dernier planning chargé ... voir pour separer ces 2 valeurs
    	// en base
    	if (shouldRecalculerOccupation) {
      	if (!StringCtrl.isEmpty(planning.type())) {
      		try {
      			planning.calculerOccupationChronologiques();
      		} catch (Throwable e) {
      			e.printStackTrace();
      		}
      		shouldRecalculerOccupation = false;
      	}
    	}
    }
    return planning;  
  }
  
  /**
   * Indique si un planning est dans le cache ou pas
   * @param affAnn
   * @return
   */
  public boolean isPlanningInCache(EOAffectationAnnuelle affAnn) {
    Planning planning = (Planning) dicoLoadedPlannings.objectForKey(
    		getSelectedAffAnnPersoDicoKey(affAnn));
    return planning != null;
  }

  /**
   * Forcer le rechargement d'un planning correspondant
   * a l'affectation annuelle passee en parametre. Le 
   * cache de ce dernier est supprimé. Il sera rechargé
   * a sa prochaine sollicitation.
   */
  public void clearCachePlanning(Planning planningToReload) {
  	dicoLoadedPlannings.removeObjectForKey(getSelectedAffAnnPersoDicoKey(planningToReload.affectationAnnuelle()));
  	planningToReload.affectationAnnuelle().clearCache();
		//TODO gestion du cache des tables : le faire pour toutes les pages personnelles et faire l'opération ailleurs
		try {
  		PageCET pageCET = (PageCET) getSavedPageWithName(PageCET.class.getName());
  		pageCET.reset();
		} catch (Throwable e) {
			e.printStackTrace();
		}
  }
 
  /**
   * Forcer le rechargement d'une affectation annuelle passee en parametre.
   * Le planning associé en cache est supprimé. Il sera rechargé
   * a sa prochaine sollicitation.
   */
  public void clearCachePlanningForAffectationAnnuelle(
  		EOAffectationAnnuelle affectationAnnuelleToReload) {
  	if (affectationAnnuelleToReload != null) {
      dicoLoadedPlannings.removeObjectForKey(getSelectedAffAnnPersoDicoKey(affectationAnnuelleToReload));
      affectationAnnuelleToReload.clearCache();
  		//TODO gestion du cache des tables : le faire pour toutes les pages personnelles et faire l'opération ailleurs
  		try {
    		PageCET pageCET = (PageCET) getSavedPageWithName(PageCET.class.getName());
    		pageCET.reset();
  		} catch (Throwable e) {
  			e.printStackTrace();
  		}
  	}
  }
  
  /**
   * Remplacer un planning par un autre en cache. C'est utile
   * pour ne pas refaire tous les calculs nécéssaires pour son
   * rechargement automatique, seuls quelques parties ont été 
   * modifiées.
   */
  public void setPlanningForce(Planning value) {
    dicoLoadedPlannings.setObjectForKey(value, getSelectedAffAnnPersoDicoKey(value.affectationAnnuelle()));
  }

  public EOIndividu individuConnecte() {    return cngUserInfo().recIndividu();  }

  public EOEditingContext ec() {
    return defaultEditingContext();
  }


  public void setStructuresDispoParNiveau(NSArray array) 	{	structuresDispoParNiveau = array;	}
  public void setStructuresDispoParNiveauSelection(Responsabilite structure) 	{	structuresDispoParNiveauSelection = structure;	}
  public void setBadNavigator(boolean b) {		isBadNavigator = b;}
  
  
  public NSArray structuresDispoParNiveau() 				{	return structuresDispoParNiveau;	}
  public Responsabilite structuresDispoParNiveauSelection() 		{	return structuresDispoParNiveauSelection;	}
  public boolean isBadNavigator() {	return isBadNavigator;	}
  
  /**
   * Indique si la personne connectée est administrateur global
   */
  public boolean isAdministrateur() {
  	return cngUserInfo().isAdmin();
  }
 
  /**
   * Indique si la personne connectée fait partie du groupe
   * des DRH globaux a l'etablissment
   */
  public boolean isDrh() {
  	return cngUserInfo().isDrh();
  }
	
  /**
   * construire le tableau des droits de l'individu sur les structures
   * @return true si l'individu peut se logger (s'il a au moins une structure visible)	
   */
   public boolean setDroitsIndividuConnecte() {
  	 NSMutableArray responsabilites = new NSMutableArray();
	
  	 // s'il est administrateur, il est niveau 3 sur tous les services autorisés
		 if (isAdministrateur() || isDrh()) {
		   for (int i=0; i < lesStructureAutorisee().count(); i++) {
		     EOStructure service = (EOStructure)lesStructureAutorisee().objectAtIndex(i);
		     responsabilites.addObject(new Responsabilite(service, ConstsDroit.DROIT_NIVEAU_VALIDATION));
		   }
		 } else {
		   // pas admin, on met les droits selon
		   NSArray niveaux = new NSArray(new Object[] { ConstsDroit.DROIT_NIVEAU_DRH_COMPOSANTE, ConstsDroit.DROIT_NIVEAU_ADM_COMPOSANTE,
		  		 ConstsDroit.DROIT_NIVEAU_VALIDATION, ConstsDroit.DROIT_NIVEAU_VISA, ConstsDroit.DROIT_NIVEAU_VISU});
		   
		   NSArray niveauxAll = new NSArray(new Object[] { ConstsDroit.DROIT_NIVEAU_DRH_COMPOSANTE, ConstsDroit.DROIT_NIVEAU_ADM_COMPOSANTE,
		  		 ConstsDroit.DROIT_NIVEAU_VALIDATION, ConstsDroit.DROIT_NIVEAU_VISA, ConstsDroit.DROIT_NIVEAU_VISU});
		 	
		   // ** recuperation des structure desquelles il est chef de service **
		   NSArray services = EOStructure.findStructureForChefDeService(ec(), individuConnecte());
		   for (int i = 0; i < services.count(); i++) {
		     // on s'assure que le service est un service autoris�
		     EOStructure service = (EOStructure)services.objectAtIndex(i);
		     if (lesStructureAutorisee().containsObject(service))
		       responsabilites.addObject(new Responsabilite(service, ConstsDroit.DROIT_NIVEAU_VALIDATION));
		   }

		
		   // ** recuperation des structure desquelles il est adm composante / drh composante / validation / visa / visu **
		   for (int j = 0; j<niveauxAll.count(); j++) {
		     Integer niveau = (Integer)niveauxAll.objectAtIndex(j);
		     
		     NSArray servicesNiveau = EOStructure.findStructureForIndividuAndNiveau(ec(), individuConnecte(), niveau);
		     servicesNiveau = droitBus().processServiceListForNiveau(servicesNiveau, niveau);
		  
		     for (int i = 0; i < servicesNiveau.count(); i++) {
		    	 EOStructure serviceNiveau = (EOStructure) servicesNiveau.objectAtIndex(i);
		    	 // recherche du droit associee
		    	 // pour les adm et drh de composante, le droit n'existe pas dans la base puisqu'il est hérité
		    	 NSArray droitsSurService =	droitBus().findDroitsForStructureAndIndividuForCdrTypeAndNiveau(
		    			 ec(), serviceNiveau, null, ConstsDroit.DROIT_CIBLE_SERVICE, niveau);
		    	 if (droitsSurService.count() > 0) {
			    	 EODroit droitSurService = (EODroit) droitsSurService.objectAtIndex(0);
			    	 addResponsabiliteOuRemplaceSiNiveauSuperieur(responsabilites, serviceNiveau, droitSurService, niveau);
		    	 }
		     }
		   }
		
		   // ** recuperation des structures ou il est resp. du chef de service validation / visa / visu **
		   for (int j = 0; j<niveaux.count(); j++) {
		     Integer niveau = (Integer)niveaux.objectAtIndex(j);
		     NSArray servicesNiveau = EOStructure.findStructureWithChefDeServiceForIndividuResponsableAndNiveau(ec(), individuConnecte(), niveau);
		     for (int i = 0; i < servicesNiveau.count(); i++) {
		       EOStructure serviceNiveau = (EOStructure)servicesNiveau.objectAtIndex(i);
		       EODroit droit = (EODroit)droitBus().findDroitsForStructureAndIndividuForCdrTypeAndNiveau(
			      		 ec(), serviceNiveau, null, ConstsDroit.DROIT_CIBLE_CHEF_DE_SERVICE, niveau).objectAtIndex(0);
		       addResponsabiliteOuRemplaceSiNiveauSuperieur(responsabilites, serviceNiveau, droit, niveau);
		     }
		   }
	
		   // ** recuperation des individus dont il est resp. validation / visa / visu **
		   for (int j = 0; j<niveaux.count(); j++) {
		     Integer niveau = (Integer)niveaux.objectAtIndex(j);
		     NSArray droitsIndividuels = droitBus().findDroitsForResponsableAndCdrTypeAndNiveau(
		    		 ec(), individuConnecte(), ConstsDroit.DROIT_CIBLE_INDIVIDU, niveau);
		     for (int i = 0; i < droitsIndividuels.count(); i++) {
		       EOIndividu individuCible = ((EODroit)droitsIndividuels.objectAtIndex(i)).toIndividu();
		       EOStructure service = ((EODroit)droitsIndividuels.objectAtIndex(i)).toStructure();
		       // recherche du droit associe
		       EODroit droit = (EODroit)droitBus().findDroitsForStructureAndIndividuForCdrTypeAndNiveau(
		      		 ec(), service, individuCible, ConstsDroit.DROIT_CIBLE_INDIVIDU, niveau).objectAtIndex(0);
		       addResponsabiliteOuRemplaceSiNiveauSuperieur(responsabilites, service, droit, niveau);
		     }
		   }
	
		
			// ** recuperation des affectations du gars (rajout�es que si pas pr�sente dans la liste des reponsablilit�s)
			NSMutableDictionary bindings = new NSMutableDictionary();
			bindings.setObjectForKey(individuConnecte(), "individu");
			bindings.setObjectForKey(debutAnnee(), "dateDebut");
			bindings.setObjectForKey(finAnnee(), "dateFin");
			NSArray affectations = EOUtilities.objectsWithFetchSpecificationAndBindings(ec(), "Affectation", "AffectationsPourIndividu", bindings);
			services = (NSArray)affectations.valueForKey("structure");
			for (int i = 0; i < services.count(); i++) {
			  EOStructure service = (EOStructure)services.objectAtIndex(i);
			  boolean structureExistante = false;
			  for (int j = 0; j < responsabilites.count(); j++) {
			    Responsabilite responsabilite = (Responsabilite)responsabilites.objectAtIndex(j);
			    // ici la verif que la structure est pas deja dedans
			    if (service.equals(responsabilite.getStructure()))
			      structureExistante = true;
			  }
			  if (!structureExistante)
			    responsabilites.addObject(new Responsabilite(service, ConstsDroit.DROIT_NIVEAU_RIEN));
			}
		 }
		 
		 // construction termin�e, on met �a dans la session
		 setStructuresDispoParNiveau(responsabilites);
	   
		 // selection de la premiere structure
		 if (isAdministrateur() || responsabilites.count() > 0) {
		   if (responsabilites.count()>0)
		     setStructuresDispoParNiveauSelection((Responsabilite)responsabilites.objectAtIndex(0));
		   return true;
		 } else
			 return false;
   }
   
   /**
    * Allegement du code
    * @param responsabilites
    * @param service
    * @param droitAAjouterSiSuperieur
    * @param niveauDeReference
    */
   private void addResponsabiliteOuRemplaceSiNiveauSuperieur(
  		 NSMutableArray responsabilites, 
  		 EOStructure service, 
  		 EODroit droitAAjouterSiSuperieur, 
  		 Integer niveauDeReference) {
  	 // recherche de l'objet responsabilite pour le service s'il n'existe pas deja
     Responsabilite responsabiliteTrouvee = FinderResponsabilite.findResponsabiliteForStructureInArray(responsabilites, service);
     if (responsabiliteTrouvee != null) {
       // si droit est superieur au niveau par defaut de la responsabilite, on ajoute le droit a la responsabilite
       if (responsabiliteTrouvee.getNiveauParDefautSurService().intValue() < niveauDeReference.intValue()) {
         responsabilites.removeIdenticalObject(responsabiliteTrouvee);
         responsabiliteTrouvee.addDroit(droitAAjouterSiSuperieur);
         responsabilites.addObject(responsabiliteTrouvee);
       }
     } else {
       // elle n'existe pas, il faut la creer et ajouter le droit, en initialisant
    	 // la responsabilite "hote" a rien, auquel on ajoute <code>eodroitAAjouter</code>
    	 Responsabilite responsabiliteNouvelle = new Responsabilite(service, ConstsDroit.DROIT_NIVEAU_RIEN);
    	 responsabiliteNouvelle.addDroit(droitAAjouterSiSuperieur);
       responsabilites.addObject(responsabiliteNouvelle);
     }	
   }
   
	 
	 /**
	  * Les sous menus de la rubrique "Planning Personnel". 
	  * Ils dependent de l'etat du planning :
	  * - disabled ou pas
	  * - precedent ou pas
	  */
	 public NSArray menuItemsPersonnel() {
		 NSArray menu = new NSArray();
		 Planning planning = getLePlanningSelectionne();
		 
		 if (planning.isDisabled()) {
			 if (planning.isPreviousAnneeUniv()) {
				 menu = ConstsMenu.MENU_ITEMS_PLANNING_PREVIOUS;
			 } else {
				 menu = ConstsMenu.MENU_ITEMS_PLANNING_OTHER_CURRENT;
				 // ajout du menu CET 
				 menu = addMenuCetIfAllowed(menu);
			 }
		 } else {
			 if (planning.isConnectedOwner()) {
				 menu = ConstsMenu.MENU_ITEMS_PLANNING_SELF_CURRENT;
				 // si imprimes divers on rajoute 
				 if (isShowDemandeGardeEnfant) {
					 // s'il a des enfants seulement
					 if (individuConnecte().tosEnfant().count() > 0) {
						 menu = menu.arrayByAddingObject(ConstsMenu.MENU_PERSO_IMP_DIVERS);
					 }
				 }
				 // si gestion du CET on le rajoute
				 if (isShowCet) {
					 menu = menu.arrayByAddingObject(ConstsMenu.MENU_PERSO_CET);
				 }
			 } else {
				 menu = ConstsMenu.MENU_ITEMS_PLANNING_OTHER_CURRENT;
				 // ajout du menu CET 
				 menu = addMenuCetIfAllowed(menu);
			 }
		 }
		 return menu;
	 }

	 /**
	  * Gestion de l'ajout de l'onglet CET, selon le profil de 
	  * la personne connectée
	  * @param initialMenu
	  * @return
	  */
	 private NSArray addMenuCetIfAllowed(NSArray initialMenu) {
		 
		 NSArray menu = initialMenu;
		 
		 // ajout du menu CET 
		 if (isShowCet) {
			 boolean shouldAddMenuCet = false;
			 
			 // l'administrateur/DRH peut voir l'onglet CET de n'importe qui
			 if (isAdministrateur() || isDrh()) {
				 shouldAddMenuCet = true;
			 }

			 EOAffectationAnnuelle affAnn = getLePlanningSelectionne().affectationAnnuelle();

			 // responsable aussi (cf demande UPMF)
			 try {
				 if (!shouldAddMenuCet) {
					 NSArray persIdsResp = (NSArray) planningBus().responsables(droitBus(), affAnn, null, true).valueForKey(EOIndividu.PERS_ID_KEY);
					 if (persIdsResp.containsObject(individuConnecte().persId())) {
						 shouldAddMenuCet = true;
					 }
				 }
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
			 
			 // viseur aussi (cf demande UPMF)
			 if (!shouldAddMenuCet) {
				 NSArray persIdsViseurs = (NSArray) planningBus().viseurs(droitBus(), affAnn, null, true).valueForKey(EOIndividu.PERS_ID_KEY);
				 if (persIdsViseurs.containsObject(individuConnecte().persId())) {
					 shouldAddMenuCet = true;
				 }
			 }
					 
			 if (shouldAddMenuCet) {
				 menu = menu.arrayByAddingObject(ConstsMenu.MENU_PERSO_CET);
			 }
		 }

		 return menu;
	 }
	 
	/**
	 * Menu pour le detail d'une demande (chgt de planning)
	 * Ce menu n'est pas visible.
	 */
	public NSArray menuItemsDetailDemande() {
		return ConstsMenu.MENU_ITEMS_DETAIL_DEMANDE;
	}
    
	private Boolean isGriseMenuPersonnnel;
	
	public boolean isGriseMenuPersonnel() {	
		if (isGriseMenuPersonnnel == null) {	
			isGriseMenuPersonnnel = new Boolean(false);
		}	
		return isGriseMenuPersonnnel.booleanValue();
	}
	
	public void setIsGriseMenuPersonnel(boolean value) {	
		isGriseMenuPersonnnel = new Boolean(value);
	}
	
	/**
	 * Remise a zero de tous les caches
	 */
	public void initSession() {
    super.initSession();
    cngUserInfo = null;
   }
   
	
	/**
	 * gestion de la connection et du settage des droits d'un individu
	 * @param individu
	 * @return
	 */
	public WOComponent loginRedirect(Number persId) {
	  WOComponent page = null;
	  // raz des cache
	  initSession();
		
	  if (persId != null) {
	  	cngUserInfo = new CngUserInfo(droitBus(), preferenceBus(), ec(), persId);
	  	cngUserInfo.compteForPersId(persId, true);
	  	
	  	if (cngUserInfo != null && cngUserInfo.persId() != null) {
	  		setConnectedUserInfo(cngUserInfo);         
		  	LRLog.log("user login : "+cngUserInfo().login()+ " - OK");
		  	envoyerMailConnexion(cngUserInfo.persId());
	      
		  	// fixer les droits de la personne
		  	if (setDroitsIndividuConnecte() == true) {
		  		if (individuHasPlanning(cngUserInfo.recIndividu()) == true) {
		  			if (isAdministrateur()) {
		  				page = getSavedPageWithName(PageAccueil.class.getName());
		  			} else {
		  	 			Integer niveauMax = (Integer)structuresDispoParNiveau().valueForKeyPath("@max.niveauMax");
			  			if (niveauMax.intValue()>ConstsDroit.DROIT_NIVEAU_VISU.intValue()) {
			  				if (structuresDispoParNiveau().count() > 1)
			  					page = getSavedPageWithName(PageAccueil.class.getName());                           
			  				else {
			  					page = getSavedPageWithName(PageDemandes.class.getName());
			  				}
			  			} else {
			  				preparerPlanningForIndividu(individuConnecte());
			  				page = (PagePlannings) getSavedPageWithName(PagePlannings.class.getName());
			  				((PagePlannings)page).initialiserVisualisation();
			  			}
		  			}
		  		} else {
		  			if (structuresDispoParNiveau().count() == 0) {
		  				// Utilisateur administrateur exclusivement
		  				page = getSavedPageWithName(PageAdministration.class.getName());
		  			} else {
		  				// Utilisateur responsable pour au moins une structure de la validation et/ou du visa
		  				setStructuresDispoParNiveauSelection((Responsabilite)structuresDispoParNiveau().objectAtIndex(0));
		  				page = getSavedPageWithName(PageAccueil.class.getName());
		  			}
		  		} 
		  	} else {
		  		// pas d'acces
		  		String msg = 
		  			"Attention <b>" + individuConnecte().nomComplet() + "</b><br><br>" +
		  			"Vous n'avez pas les droits de vous connecter à l'application des congés ...<br><br>" +
		  			"Faire une <a href=\"" + app().config().stringForKey("DT_APPLIW3") +"\">Demande de Travaux</a><br><br>" +
		  			"Retour au site de l'<a href=\"" + app().config().stringForKey("MAIN_WEB_SITE_URL") + "\">Université</a>";
		  		page = showErrorPage(msg);
		  	}
	  	} else {
		  	// message d'erreur si le user info est vide => pas de compte
	  		String msg = 
	  			"Attention <b>" + individuConnecte().nomComplet() + "</b><br><br>" +
	  			"Vous n'avez pas de <b>compte informatique</b> à l'application des congés ...<br><br>" +
	  			"Ce compte informatique est utilisé pour l'envoi des courriers électroniques<br><br>"+
	  			"Faire une <a href=\"" + app().config().stringForKey("DT_APPLIW3") +"\">Demande de Travaux</a><br><br>" +
	  			"Retour au site de l'<a href=\"" + app().config().stringForKey("MAIN_WEB_SITE_URL") + "\">Université</a>";
	  		page = showErrorPage(msg);
		  	
	  	}
	  }
	  
	  return page;
	}
	
	/**
	 * 
	 * @param errMessage
	 * @return
	 */
	private WOComponent showErrorPage(String errMessage) {
		WOComponent page = criApp.pageWithName(CRIAlertPage.class.getName(), context());
		((CRIAlertPage)page).showMessage(null, "ERREUR", errMessage, null, null, null, CRIAlertPage.ERROR, null);
		return page;
	}

	/**
	 * individu possede une affectation sur la periode en cours
	 */
	public boolean individuHasPlanning(EOIndividu individu) {
	  return EOVIndividuConges.findRecords(ec(), individu.oid(), null, null).count() > 0;
	}
    
	/**
	 * preparer les affectations annuelles et preselection
     * celle qui est active pour affichage
	 */
	public void preparerPlanningForIndividu(EOIndividu individu) {
	  // recherche des plannings de la personne
	  // liste de toutes les affectaions annuelles connues de l'individu
	  NSArray affAnns = EOAffectationAnnuelle.findSortedAffectationsAnnuellesForOidsInContext(ec(),
	      (NSArray) EOVIndividuConges.findRecords(ec(), individu.oid(), null, null).valueForKey("oidAffAnn"));
	  setAffAnnsPerso(affAnns);
	  // on prend la premiere affectation annuelle actuelle "active" : en cours ou apres l'annee univ en cours
	  EOQualifier qual = EOQualifier.qualifierWithQualifierFormat("dateFinAnnee>=%@", new NSArray(dateRef()));
	  NSArray affAnnsActives = EOQualifier.filteredArrayWithQualifier(affAnnsPerso(), qual);

    // prendre celle qui est en cours de préférence
	  EOAffectationAnnuelle eoAffectationAnnuelle = EOAffectationAnnuelle.getPremiereCourante(
	  		affAnnsActives);
	  	
	  if (eoAffectationAnnuelle == null) {
		  if (affAnnsActives.count() > 0) {
		  	// la premiere activie
		  	eoAffectationAnnuelle = (EOAffectationAnnuelle) affAnnsActives.objectAtIndex(0);
		  }
		  // pas d'affectation annuelle active, on prend la derniere
		  else if (affAnnsPerso().count() > 0) {
		  	eoAffectationAnnuelle = (EOAffectationAnnuelle) affAnnsPerso().lastObject();
		  }
	  }
	  
	  setSelectedAffAnnPerso(eoAffectationAnnuelle);
	}
	
	/**
	 * Preparer les affectation, et préselectionner l'une d'entre elle
	 * @param eoAffectationAnnuelle
	 */
	public void preparerPlanningForAffectationAnnuelle(EOAffectationAnnuelle eoAffectationAnnuelle) {
		preparerPlanningForIndividu(eoAffectationAnnuelle.individu());
		setSelectedAffAnnPerso(eoAffectationAnnuelle);
	}
	

	/**
	 * savoir si la personne surlaquelle on travaille est la personne connectee
	 */
	public boolean isIndividuConnecte() {
	  boolean isIndividuConnecte = false;
	  if (getLePlanningSelectionne() != null) {
	    isIndividuConnecte = getLePlanningSelectionne().isConnectedOwner();
	  }
	  return isIndividuConnecte;	
	}
    
	/**
	 * l'individu connect� a une affectation sur l'ann�e
	 * @return
	 */
	public boolean individuConnecteHasAffectation() {
	  boolean hasAffectation = false;
	  
	  if (EOVIndividuConges.findRecords(
	  		ec(), individuConnecte().oid(), null, null).count() > 0) {
	  	hasAffectation = true;
	  }
	  
	  return hasAffectation;
	}

	
	public String pageEnCours() { return pageEnCours;}
	public void setPageEnCours(String value) { pageEnCours = value;}
	
	/**
	 * titre de la page selon son nom (variable "pageEnCours") et d'autres donnees
	 */
	public String pageTitle() {
		String pageTitle = "Gestion des conges ";
		// rajout de la date du jour
		pageTitle += DateCtrl.dateToString(dateRef());    
		return pageTitle;
	}	

	
	public boolean isDisabledMenu() {		return isDisabledMenu;	}
	public void setIsDisabledMenu(boolean value) {		isDisabledMenu = value;	}
	
	
	// METHODES LIEES AUX DONNEES EN CACHE DE LA SESSION
	
	private NSArray typesOccupationsEditables;
	private NSArray individusVPersonnelActuel;
	private NSArray individusAll;
	
	public NSArray typesOccupationsEditables() {
	  if (typesOccupationsEditables == null) 
	  	typesOccupationsEditables = EOTypeOccupation.findTypesOccupationsEditablesInContext(ec());
	  return typesOccupationsEditables;
	}

	public NSArray individusVPersonnelActuel() {
	  if (individusVPersonnelActuel == null)
	    individusVPersonnelActuel = EOIndividu.findIndividuInVPersonnelActuel(ec());
	  return individusVPersonnelActuel;
	}

	public NSArray individusAll() {
		if (individusAll == null)
			individusAll = EOIndividu.findIndividuAll(ec());
		return individusAll;
	}

    
	private NSTimestamp dateRef, debutAnnee, finAnnee;
    
	public NSTimestamp debutAnnee() 	{  	return debutAnnee;}
	public NSTimestamp finAnnee() 		{	return finAnnee;}

	/**
	 * La date de reference de la session. Concretemment, il
	 * s'agit de la date a laquelle a ete ouverte la session.
	 */
	public NSTimestamp dateRef() {	    return dateRef;	}
	
	/**
	 * Lors de la mise a jour de la date de ref, on recalcule
	 * les valeurs debutAnnee et finAnnee
	 */
	public void setDateRef(NSTimestamp value) {
		dateRef = value;
		debutAnnee 	= DateCtrlConges.dateToDebutAnneeUniv(dateRef());
		finAnnee	= debutAnnee.timestampByAddingGregorianUnits(1,0,-1,23,59,59);
	}
	
	// liste des structures autorisees
	private NSArray lesStructureAutorisee;
	
	public void setLesStructureAutorisee(NSArray value) {	lesStructureAutorisee = value; }
	
	public NSArray lesStructureAutorisee() {
		if (lesStructureAutorisee == null) {
			lesStructureAutorisee = EOStructure.servicesAutorisesInContext(ec(), dateRef());
			// classement alpha
			EOSortOrdering libelleLongOrdering = EOSortOrdering.sortOrderingWithKey("libelleLong", EOSortOrdering.CompareAscending);
			lesStructureAutorisee = EOSortOrdering.sortedArrayUsingKeyOrderArray(lesStructureAutorisee,new NSArray(libelleLongOrdering)); 
		}
		return lesStructureAutorisee;
	}
    
    
	// cache des types d'occupation les les plus utilises
    
	private EOTypeOccupation typeCongeAnnuel, typeHeuresSupplementaires, typeCongesCompensateurs, 
			typeCongesCET, typeDechargeSyndicaleJour, typeDechargeSyndicaleMinute;
    
	public EOTypeOccupation typeCongeAnnuel() {
		if (typeCongeAnnuel == null) {
			typeCongeAnnuel = EOTypeOccupation.getTypeOccupationForLibelleCourtInContext(ec(), Occupation.LIBELLE_COURT_CONGES_ANNUEL);
		}
		return typeCongeAnnuel;
	}
    
  public EOTypeOccupation typeHeuresSupplementaires() {
    if (typeHeuresSupplementaires == null) {
      typeHeuresSupplementaires = EOTypeOccupation.getTypeOccupationForLibelleCourtInContext(ec(), HeuresSupplementaires.LIBELLE_COURT);
    }
    return typeHeuresSupplementaires;
  }
  
  public EOTypeOccupation typeCongesCompensateurs() {
    if (typeCongesCompensateurs == null) {
      typeCongesCompensateurs = EOTypeOccupation.getTypeOccupationForLibelleCourtInContext(ec(), CongesCompensateurs.LIBELLE_COURT);
    }
    return typeCongesCompensateurs;
  }
  
  public EOTypeOccupation typeCongesCET() {
    if (typeCongesCET == null) {
      typeCongesCET = EOTypeOccupation.getTypeOccupationForLibelleCourtInContext(ec(), CongespourCet.LIBELLE_COURT);
    }
    return typeCongesCET;
  }
  
  public EOTypeOccupation typeDechargeSyndicaleJour() {
    if (typeDechargeSyndicaleJour == null) {
    	typeDechargeSyndicaleJour = EOTypeOccupation.getTypeOccupationForLibelleCourtInContext(ec(), DechargeSyndicale.LIBELLE_COURT_JOUR);
    }
    return typeDechargeSyndicaleJour;
  }
  
  public EOTypeOccupation typeDechargeSyndicaleMinute() {
    if (typeDechargeSyndicaleMinute == null) {
    	typeDechargeSyndicaleMinute = EOTypeOccupation.getTypeOccupationForLibelleCourtInContext(ec(), DechargeSyndicale.LIBELLE_COURT_MINUTE);
    }
    return typeDechargeSyndicaleMinute;
  }
    
    
    
  // ---------------------
  // RECUP YCRISESSION
  // ---------------------
  
  private String pageEnCours;

  public void terminate() {
  	String userLogin = null;
  	if (connectedUserInfo() != null)
      userLogin = connectedUserInfo().login();
  	StringBuffer buffer = new StringBuffer("close session");
  	if (userLogin != null)
  		buffer.append(", user : ").append(userLogin);
  	log(buffer.toString());
  	super.terminate();
  }
  
  public void envoyerMailConnexion(Number persId) {
  	if (app().appLogConnexion() && persId != null) {
      String domaine = criApp.config().stringForKey("GRHUM_DOMAINE_PRINCIPAL");
      String from = "appli_" + criApp.name().toLowerCase() + "@";
      if (domaine == null)
        from += "ERREUR_PAS_DE_DOMAINE";
      else
          from += (String) domaine;
      LRUserInfoDB userInfo = new LRUserInfoDB(criApp.dataBus());
      userInfo.compteForPersId(persId, true);
      String body = userInfo.toString();
      criApp.mailBus().sendMail(from, app().appAdminMail(), null,
          ("[" + app.name() + "] Connexion de "
              + userInfo.nomEtPrenom()),
              body);
    }
  }
      
  private String logPrefix() {
    return "<" + sessionID() + " - ";
  }

  private void log(String value) {
    LRLog.log(logPrefix() + value + ">");
  }
   
  //
  // LISTE DES OIDS DE TOUTES LES ALERTES
  //
  
  private NSArray recordsVAlerte;

  public NSArray recordsVAlerte()                         {   return recordsVAlerte;}
  
  public void doRefreshViewAlerts()     {  
    recordsVAlerte = cngDataCenter().alerteBus().fetchViewAlerts(ec());;
  }
  
  // ---------------------
  // DEBUT NANASSIFICATION
  // ---------------------
  
  private CngDataCenter _cngDataCenter;
  
  /**
   * Pointeur vers le gestionnaire de donnees.
   */
  public CngDataCenter cngDataCenter() {
    if (_cngDataCenter == null)
      _cngDataCenter = new CngDataCenter(this);
    return _cngDataCenter;
  }
  
  private CngUserInfo cngUserInfo;
  
  public CngUserInfo cngUserInfo() {
    return cngUserInfo;
  }
  
  // bus de donnees
  
  private CngDroitBus droitBus() {
  	return cngDataCenter().droitBus();
  }
  
  private CngPreferenceBus preferenceBus() {
  	return cngDataCenter().preferenceBus();
  }
  
  private CngPlanningBus planningBus() {
  	return cngDataCenter().planningBus();
  }

  
  // ----------------
  // EDITIONS SIX
  // ----------------

  private NSData lastPdfData;
  private CngPdfBoxCtrl lastPdfCtrl;
  
  public NSData getLastPdfData() {
		return lastPdfData;
	}

  public void setCurrentPdfData(NSData value) {
		lastPdfData= value;
	}
  
  public CngPdfBoxCtrl getLastPdfCtrl() {
		return lastPdfCtrl;
	}

  public void setCurrentPdfCtrl(CngPdfBoxCtrl value) {
  	lastPdfCtrl= value;
	}
  

  // ------------------------------
  // TEMPS D'AFFICHAGE DES PAGES //
  // ------------------------------

  private long startTime;
  
  public void awake() {
  	startTime = System.currentTimeMillis();
  	super.awake();
  }
  
  public long getStartTime() {
  	return startTime;
  }
  
}