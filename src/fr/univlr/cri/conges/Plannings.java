package fr.univlr.cri.conges;

import java.util.Hashtable;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.ParentPlannings;
import fr.univlr.cri.conges.YCRIWebPage;
import fr.univlr.cri.conges.constantes.ConstsJour;
import fr.univlr.cri.conges.constantes.ConstsMenu;
import fr.univlr.cri.conges.constantes.ConstsOccupation;
import fr.univlr.cri.conges.eos.modele.planning.*;
import fr.univlr.cri.conges.objects.*;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.util.wo5.DateCtrl;
/*
 * Copyright Consortium Coktail, 12 avr. 07
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
 * Page affichant un planning annuel
 * 
 *  @author Emmanuel Geze <egeze at univ-lr.fr>
 *  @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */
public class Plannings 
	extends YCRIWebPage {
	
  // erreur 
	public String txtErreur;
 
  // variables entrantes
  public Planning lePlanning;
  public boolean showMenu;
  
  // checkboxes pour choisir l'affichage
  public Boolean isVisuOccupation;
  public Boolean isVisuHoraire;
  
  // backup binding
  private Planning lePlanningPrecedent;
  
  // menu
  public NSArray menuItems = null;
  public String unItemMenu = null;
  public int indexItemMenu = 0;
  public String selectedItemMenu = null;
  
  // elements pour les boucles
  public Mois leMois, unMois = null;
  private Semaine _laSemaine;
  private Jour leJour, leJourPrecedent;
	
  public I_Absence uneAbsence;	
	
  public String lesDureesJournalieresItem;

  // 2 visualisation du planning
  public String leTypeVisu;
	
  private Boolean isSemaineModifiable;
	
  public boolean isDisabled = false;
	
  public ParentPlannings parentPage;
    
  /** id du span contenant les conges restants en jours */
  public final String LABEL_DIV_CONGES_RESTANTS_JOURS 			= "divCongesRestantsJours";
  /** id du span contenant les reliquats restants en jours */
  public final String LABEL_DIV_RELIQUATS_RESTANTS_JOURS 		= "divReliquatsRestantsJours";
  /** id du span contenant les droits a conges initiaux en jours */
  public final String LABEL_DIV_CONGES_INITIAUX_JOURS 		= "divCongesInitiauxJours";
  /** nom de l'input HTML dans lequel est saisit la duree d'une journee en heure */
  public final String INPUT_DUREE_JOURNEE 		= "document.forms['FormCalculs'].inputDureeJournee";

  public static String dureeJourneeApp =  Parametre.PARAM_DUREE_JOUR_CONVERSION.getParamValueString();
  /** valeur initiale de la duree d'une journee */
  public String dureeJournee = dureeJourneeApp;
  
  /** patterns du code javascript genere pour les calculs de conversion */
  private final String ON_BLUR_INPUT_DUREE = 
  	"formatter(document.forms['FormCalculs'].inputDureeJournee,true);" +
  		"convertirHeuresToJoursFromElement(" +
  		"'%"+KEY_CONGES_RESTANTS+"%'," +
  		INPUT_DUREE_JOURNEE+"," +
  		"'"+LABEL_DIV_CONGES_RESTANTS_JOURS+"')" +
  		";" +
  		"convertirHeuresToJoursFromElement(" +
  		"'%"+KEY_RELIQUATS_RESTANTS+"%'," +
  		INPUT_DUREE_JOURNEE+"," +
  		"'"+LABEL_DIV_RELIQUATS_RESTANTS_JOURS+"')" +
  		";" +
  		"convertirHeuresToJoursFromElement(" +
  		"'%"+KEY_CONGES_INITIAUX+"%'," +
  		INPUT_DUREE_JOURNEE+"," +
  		"'"+LABEL_DIV_CONGES_INITIAUX_JOURS+"')" +
  		";";
  private final String ON_KEY_DOWN_INPUT_DUREE = "if(event.keyCode==13){" +
  	ON_BLUR_INPUT_DUREE + "}";
  /** le nom des variable utilisees par le dico de conversion */
  private final static String KEY_CONGES_RESTANTS = "CONGES_RESTANTS";
  private final static String KEY_RELIQUATS_RESTANTS = "RELIQUATS_RESTANTS";
  private final static String KEY_CONGES_INITIAUX = "CONGES_INITIAUX";
  
  /** les valeurs concretes de gestion du code javascript pour le planning en cours*/
  public String onBlurInputDuree;
  public String onKeyDownInputDuree;
  /** dico de conversion utilises pour la creation du code JS par replaceWithDico*/
  private Hashtable dicoConversion;
  
  public Plannings(WOContext context) {
    super(context);
   // 2 types de parent possible : PagePlannings et PageDetailDemande
    parentPage = (ParentPlannings) context().page();
    parentPage.setPlanningsComponent(this);
  }
  
  public void appendToResponse(WOResponse response, WOContext arg1) {
  	// generer le code js de conversion
  	generateCodeJavascriptConversion();
  	//
    super.appendToResponse(response, arg1);
    // ajouter la css du planning
    addLocalCss(response, "css/Planning.css"); 
    // le texte de la css des horaires
    if (!StringCtrl.isEmpty(lePlanning().textCssHoraire())) {
      addTextCss(response, lePlanning().textCssHoraire());
    }
    // le js pour manipuler les classes
    addLocalJScript(response, "js/cssUtils.js");
    // le code javascript utilise pour la manipulation de durees
    addLocalJScript(response, "jscript/CRITimeField.js", "CRIWebExt3");
  }

  /**
   * @see I_ClasseMetierNotificationParametre
   * @param parametre
   */
  public static void initStaticField(
  		Parametre parametre) {
  	if (parametre == Parametre.PARAM_DUREE_JOUR_CONVERSION) {
  		dureeJourneeApp = parametre.getParamValueString();
  	} 
  }
  
  /**
   * Genere le code javascript utilise dans la conversion des jours
   * en heures. Ce code est specifique a chaque planning. On reinstancie
   * les variables concretes <code>dicoConversion</code>, 
   * <code>onBlurInputDuree</code> et <code>onKeyDownInputDuree</code>
   */
  private void generateCodeJavascriptConversion() {
  	// base de conversion
  	dicoConversion = new Hashtable();
  	dicoConversion.put(KEY_CONGES_RESTANTS, lePlanning().congesGlobalRestants());
  	dicoConversion.put(KEY_RELIQUATS_RESTANTS, lePlanning().reliquatRestant());
  	dicoConversion.put(KEY_CONGES_INITIAUX, lePlanning().congesInitiauxEnHeures());
    	// creation du code a partir des patterns
  	onBlurInputDuree 		= StringCtrl.replaceWithDico(ON_BLUR_INPUT_DUREE, dicoConversion);
  	onKeyDownInputDuree = StringCtrl.replaceWithDico(ON_KEY_DOWN_INPUT_DUREE, dicoConversion);
  }
  
  /**
   * Au chargement de la page, on fixe a l'avance la classe
   * de chaque semaine selon les filtres, pour eviter 
   * l'execution du code JS des CSS un peu rameux ...
   * Sous IE, si pas d'horaire, on affiche le pt d'interrogation
   * en fond.
   */
  public String classeSemaineInit() {
    String classeSemaine = null;
    if (isVisuHoraire == Boolean.TRUE)
      classeSemaine = laSemaine().classeCssShowHoraire();
    else
      classeSemaine = laSemaine().classeCssHideHoraire();
    // verif horaire vide sous IE
    if (isUsingNavigatorIE && classeSemaine == null)
      classeSemaine = laSemaine().classeCssNoHoraire();
    return classeSemaine;
  }
      
  public void reset() {
    super.reset();
    txtErreur = null;
  }
		
  public boolean hasErreur() {
    return txtErreur != null;
  }
    
  public Planning lePlanning() {
    return lePlanning;
  }

  /**
   * Mise a jour de la selection du menu d'apres 
   * l'etat du planning
   */
  public void setLePlanning(Planning value) {
    lePlanningPrecedent = lePlanning;
    lePlanning = value;
    if (lePlanning != null) {
      if (lePlanningPrecedent == null ||
          (lePlanningPrecedent != lePlanning && !lePlanningPrecedent.type().equals(lePlanningPrecedent))) {
        // selection du menu par defaut
        if (StringCtrl.isEmpty(selectedItemMenu())) {
          if (lePlanning.isPReel())           setSelectedItemMenu(ConstsMenu.MENU_PERSO_PLA_REEL);
          else if (lePlanning.isPTest())      setSelectedItemMenu(ConstsMenu.MENU_PERSO_PLA_TEST);
          else if (lePlanning.isPPrev())      setSelectedItemMenu(ConstsMenu.MENU_PERSO_PLA_PREV);
          else if (lePlanning.isPBack())       setSelectedItemMenu(ConstsMenu.MENU_PERSO_PLA_BACK);        
        } else {
          // verification de la concordance menu<->etat
          if (lePlanning.isPReel()) {
            if (!selectedItemMenu().equals(ConstsMenu.MENU_PERSO_FIC_ROSE) && 
                !selectedItemMenu().equals(ConstsMenu.MENU_PERSO_PLA_REEL) &&
                !selectedItemMenu().equals(ConstsMenu.MENU_PERSO_HORAIRES) &&
                !selectedItemMenu().equals(ConstsMenu.MENU_PERSO_CET) &&
                !selectedItemMenu().equals(ConstsMenu.MENU_PERSO_HISTO))
              setSelectedItemMenu(ConstsMenu.MENU_PERSO_PLA_REEL);
          } else if (lePlanning.isPPrev()) {
            if (!selectedItemMenu().equals(ConstsMenu.MENU_PERSO_PLA_PREV) && 
                !selectedItemMenu().equals(ConstsMenu.MENU_PERSO_HORAIRES) &&
                !selectedItemMenu().equals(ConstsMenu.MENU_PERSO_CET) &&
                !selectedItemMenu().equals(ConstsMenu.MENU_PERSO_HISTO))
              setSelectedItemMenu(ConstsMenu.MENU_PERSO_PLA_PREV);
          } else if (lePlanning.isPTest()) {
            if (!selectedItemMenu().equals(ConstsMenu.MENU_PERSO_PLA_TEST) && 
                !selectedItemMenu().equals(ConstsMenu.MENU_PERSO_HORAIRES) &&
                !selectedItemMenu().equals(ConstsMenu.MENU_PERSO_CET) &&
                !selectedItemMenu().equals(ConstsMenu.MENU_PERSO_HISTO))
              setSelectedItemMenu(ConstsMenu.MENU_PERSO_PLA_TEST);
          } else if (lePlanning.isPBack()) {
            if (!selectedItemMenu().equals(ConstsMenu.MENU_PERSO_PLA_BACK))
              setSelectedItemMenu(ConstsMenu.MENU_PERSO_PLA_BACK);
          }
        }
        // affichage par defaut
        if (lePlanning.isPReel()) {
          // selon l'appelant : on affiche les occupation
          // que pour un planning valide quand l'appelant
          // n'est pas PageDetailDemande
          isVisuHoraire     = Boolean.FALSE;
          isVisuOccupation  = Boolean.TRUE;
          if (parentPage instanceof PageDetailDemande) {
            if (lePlanning().isEnCoursDeValidation()) {
              isVisuHoraire     = Boolean.TRUE;
              isVisuOccupation  = Boolean.FALSE;
            } 
          }
        } else if (lePlanning.isPPrev()) {
          isVisuHoraire     = Boolean.TRUE;
          isVisuOccupation  = Boolean.TRUE;       
        } else if (lePlanning.isPTest()) {
          isVisuHoraire     = Boolean.TRUE;
          isVisuOccupation  = Boolean.TRUE;       
        } else if (lePlanning.isPBack()) {
          isVisuHoraire     = Boolean.TRUE;
          isVisuOccupation  = Boolean.FALSE;       
        }
      }
    }
  }
  
  public String selectedItemMenu() {
    return selectedItemMenu;
  }
   
  public void setSelectedItemMenu(String value) {
  	selectedItemMenu = value;
  	// on effectue la selection et les traitements sous jacents
  	// que si le planning entre page conteneur et cette page
  	// est bien le meme (pas de prob de synchronisation de binding) 
  	boolean isSynchronizedWithParent = 
  		(parentPage.planningBinding() != null && lePlanning() == parentPage.planningBinding());
  	if (!StringCtrl.isEmpty(selectedItemMenu()) && lePlanning() != null && isSynchronizedWithParent) {
      if (selectedItemMenu().equals(ConstsMenu.MENU_PERSO_FIC_ROSE))
        selectFicheRose();
      else if (selectedItemMenu().equals(ConstsMenu.MENU_PERSO_PLA_PREV))
        selectPlanningPrev();
      else if (selectedItemMenu().equals(ConstsMenu.MENU_PERSO_PLA_REEL))
        selectPlanningReel();
      else if (selectedItemMenu().equals(ConstsMenu.MENU_PERSO_PLA_TEST))
        selectPlanningTest();
      else if (selectedItemMenu().equals(ConstsMenu.MENU_PERSO_PLA_BACK))
        selectPlanningBack();
      else if (selectedItemMenu().equals(ConstsMenu.MENU_PERSO_HORAIRES))
        selectHoraires();
      else if (selectedItemMenu().equals(ConstsMenu.MENU_PERSO_CET))
        selectCET();
      else if (selectedItemMenu().equals(ConstsMenu.MENU_PERSO_HISTO))
        selectHistorique();
    }
  }
    
	public String idTabMenu() {
	  String idTabMenu = "tab"+indexItemMenu;
	  return idTabMenu;
	}
    
	public String classTabMenu() {
		String classTabMenu = "tab";
		
		if (unItemMenu.equals(selectedItemMenu)) {
			classTabMenu = "selectedTab";
		}
		
		return classTabMenu;
	}

	public void setLeJour(Jour value) {	
	    leJourPrecedent = leJour();	
	    leJour = value; 
 	}
	
	public Jour leJour() {
  	    return leJour;
	}

	public boolean isSemaineOuverteALaSaisieDuPrevisionnel() {
		boolean isSemaineOuverteALaSaisieDuPrevisionnel = false;
		EOAffectationAnnuelle affectationAnnuelle = lePlanning().affectationAnnuelle();	
		
		// Pour permettre l'association d'un horaire a une semaine 
		// sur le planning previsionnel, il faut que:
		// - P.Prev individu visualise = individu connecte
		// - Status P.Prev = NON VALIDE ou STATUS_EN_COURS_DE_MODIFICATION
		// - Semaine concernee active
		// - Il existe au moins un horaire associable
		if (isDisabled()==false &&
				cngUserInfo().isAllowedModifyPlanning(lePlanning.affectationAnnuelle()) &&
			lePlanning().isNonValide() && 
            laSemaine().isActive() &&
			affectationAnnuelle.horaires().count()>0 ) {
				isSemaineOuverteALaSaisieDuPrevisionnel = true;
		}
		return isSemaineOuverteALaSaisieDuPrevisionnel;
	}
	
	/*
	public boolean isSemainePasOuverteALaSaisieDuPrevisionnel() {
		return !isSemaineOuverteALaSaisieDuPrevisionnel();
	}
	
	public boolean isSemaineOuverteALaSaisieDuReel() {
		boolean isSemaineOuverteALaSaisieDuReel = false;
		EOAffectationAnnuelle affectationAnnuelle = lePlanning().affectationAnnuelle();
		
		// Pour permettre l'association d'un horaire a une semaine 
		// sur le planning previsionnel, il faut que:
		// - P.Prev individu visualise = individu connecte
		// - Status P = NON VALIDE
		// - Semaine concernee active
		// - Il existe au moins un horaire associable
		if (cngUserInfo().isAllowedModifyPlanning(lePlanning.affectationAnnuelle()) &&
		    lePlanning().isEnCoursDeModification() && 
            laSemaine().isActive() &&
			affectationAnnuelle.horaires().count()>0 &&
			DateCtrl.isAfter(laSemaine().debut(),DateCtrl.now())) {
				isSemaineOuverteALaSaisieDuReel = true;
		}
		return isSemaineOuverteALaSaisieDuReel;
	}
*/
	public PageAssocierHoraire associerHoraire() {
	  PageAssocierHoraire nextPage = null;
      
	  nextPage = (PageAssocierHoraire)laSession.getSavedPageWithName(PageAssocierHoraire.class.getName());
	  nextPage.reset();
	  nextPage.externeSetLePlanning(lePlanning());
	  nextPage.setLaPremiereSemaine(laSemaine());
	  nextPage.initialise();
		
	  return nextPage;
	}
	

	/**
	 * accessibilite lien numero de semaine sur le planning reel
	 * @return
	 */
	public boolean isSemaineModifiable() {
		if (isSemaineModifiable == null) {
			isSemaineModifiable = new Boolean(false);
			
			/*
			if (lePlanning().isPReel()) {
				if (isDisabled()==false &&
						lePlanning().isEnCoursDeModification() && 
						cngUserInfo().isAllowedModifyPlanning(lePlanning.affectationAnnuelle()) &&
						laSemaine().isActive() &&
						DateCtrl.isAfterEq(laSemaine().debut(),DateCtrlConges.toLundi(DateCtrlConges.now()))) {
					isSemaineModifiable = new Boolean(true);
				}
			}
	    	
			if (lePlanning().isPTest()) {
				if (isDisabled()==false &&
						cngUserInfo().isAllowedModifyPlanning(lePlanning.affectationAnnuelle()) &&
						laSemaine().isActive()) {
					isSemaineModifiable = new Boolean(true);
				}
			}*/
			
			if (isDisabled() == false) {
				isSemaineModifiable = lePlanning().isSemaineModifiable(laSemaine(), cngUserInfo());
			}
			
		}
		return isSemaineModifiable.booleanValue();	
	}
	
	public boolean isSemainePasModifiable() {
	    return !isSemaineModifiable();
	}
	
	
	public PageSaisieOccupation saisirOccupation() {
	    return saisirOccupationPourTypeEtDate(laSession.typeCongeAnnuel(), leJour().date());
	}
	
	/**
	 * methodes generique de saisie d'occupation
	 * @param unTypeOccupation
	 * @param uneDate
	 * @return
	 */
	public PageSaisieOccupation saisirOccupationPourTypeEtDate(EOTypeOccupation unTypeOccupation, NSTimestamp uneDate) {
		PageSaisieOccupation nextPage = (PageSaisieOccupation)pageWithName(PageSaisieOccupation.class.getName());
		nextPage.externeSetLePlanning(lePlanning());
		nextPage.setDateDebut(uneDate);
		nextPage.setDateFin(uneDate);
		nextPage.setDateDebutAMPMSelection(ConstsOccupation.OCC_MATIN);
		nextPage.setDateFinAMPMSelection(ConstsOccupation.OCC_APREM);		
		nextPage.setLeTypeOccupationSelectionne(unTypeOccupation);
		return nextPage;
	}
	
	public WOComponent detaillerUneAbsence() {
		PageDetailAbsence nextPage = (PageDetailAbsence) laSession.getSavedPageWithName(PageDetailAbsence.class.getName());
	  nextPage.reset();
      nextPage.externeSetLePlanning(lePlanning());
	  if (isFicheRose()) {
	    nextPage.setUneAbsence(uneAbsence);
	  } else {
	  	nextPage.setUneAbsence((I_Absence)lePlanning().absencesForDay(leJour().date()).objectAtIndex(0));
	  }
	  return nextPage;
	}
	
	/**
	 * Le message d'information qui s'affiche sur l'icone help a
	 * cote du reliquat. Il precise la date maximum d'utilisation
	 * du reliquat.
	 */
	public String htmlTextToolTipReliquat() {
		return "<center>Les reliquats de cong&eacute;s ne sont utilisables que<br>" +
				"sur les absences qui contiennent des jours avant<br>" +
				"le <b>" + DateCtrlConges.dateToString(lePlanning().affectationAnnuelle().dateFinReliquat())+ "</b>" +
				"<br><br>Les reliquats non utilis&eacute;s peuvent etre bascul&eacute; dans<br>" +
				"votre compte &eacute;pargne temps a partir du " + 
				DateCtrlConges.dateToString(lePlanning().affectationAnnuelle().dateDebutDemandeCet())+
				" et avant la date limite du " +
				DateCtrlConges.dateToString(lePlanning().affectationAnnuelle().dateFinDemandeCet())+
				"</center>";
	}
	
	/**
	 * Le message d'information qui s'affiche sur l'icone help a
	 * cote du bilan heures sup / conges compensateurs.
	 */
	public String htmlTextToolTipBilanHSCC() {
		String message = "<center>Balance entre heures suppl&eacute;mentaires et cong&eacute;s compensateurs (<u>valid&eacute;s</u>)";
		if (lePlanning().bilanHSCCEnMinutes() > 0) {
			message += "<br>Pensez &agrave; d&eacute;poser d'autres cong&eacute;s compensateurs <b>avant le " + 
				DateCtrlConges.dateToString(lePlanning().affectationAnnuelle().dateFinAnnee()) + "</b><br/>" +
				" (le reliquat d'heures suppl&eacute;mentaires est perdu d'une ann&eacute;e sur l'autre!) ";
		} else if (lePlanning().bilanHSCCEnMinutes() < 0){
			message += "<br>Celle ci est automatiquement d&eacute;compt&eacute;e de vos droits &agrave; cong&eacute;s.<br/>" +
				"(vos cong&eacute;s restants de <b>"+lePlanning().congesGlobalRestants()+"</b> int&egrave;gre d&eacute;j&agrave; ce &quot;malus&quot;)";
		}
		message += "</center>";
		return message;
	}
	
	// PAGE FICHE ROSE

	public Planning unPlanning = null; 
	
	public WOComponent changeService() {
	  return null;
	}

	public void visualiserUneFiche() {
		reset();
	}

	// METHODES DE NAVIGATION
	
	/**
	 * gestion de l'affichage du '?' pour le popup de l'horaire
	 * il est affiche si :
	 * - dans une meme semaine, le jour precedent porte 
     *   le numero de semaine (que j-1 != dimanche)
	 * - sinon, dans une meme semaine, si le jour suivant porte le numero de 
     *   semaine et que le jour suivant du suivant n'est pas dispo
	 */
	public boolean showToolTipHoraire() {
  	  boolean showToolTipHoraire = false;
	  if (laSemaine() != null && laSemaine().isActive()) {
	    if (leJourPrecedent != null && 
            leJourPrecedent.semaine().equals(leJour().semaine()) && 
            !leJourPrecedent.isDimanche() && 
            !"".equals(leJourPrecedent.numeroSemaine())) {
	      showToolTipHoraire = true;
	    } else {
	      Jour leJourSuivant = null;
	      if (!leJour().isDimanche()) {
	        int indexJourSuivant = laSemaine().jours().indexOfIdenticalObject(leJour()) + 1;
	        if (laSemaine().jours().count() == indexJourSuivant+1) {
	          leJourSuivant = (Jour) laSemaine().jours().objectAtIndex(indexJourSuivant);
	          if (!"".equals(leJourSuivant.numeroSemaine())) {
	            showToolTipHoraire = true;
	          }
	        }
	      }
	    }
	  }
 	  return showToolTipHoraire;
	}


	
	public boolean isPlanningValide() {
		boolean isPlanningValide = false;
		
		if (lePlanning().isValide()) {
			isPlanningValide = true;
		}
		return isPlanningValide;
	}

	/**
	 * ouvrir la page des details des calculs des droits a conges
	 * @return
	 */
	public PageDetailCalculs detaillerCalculs() {
      PageDetailCalculs nextPage = (PageDetailCalculs)pageWithName(
      		PageDetailCalculs.class.getName());
      nextPage.externeSetLePlanning(lePlanning());
      return nextPage;
	}

	public boolean isDisabled() {
	  return isDisabled;
	}
	public void setIsDisabled(boolean value) {
	  isDisabled = value;
	}
	
	/**
	 * popup legende couleurs
	 * @return
	 */
	public WOComponent ouvrirLegende() {
	  return pageWithName(PageLegende.class.getName());
	}
	
	/**
	 * disponibilite du lien d'affiche de la legende
	 * @return
	 */
	public boolean isVisibleLienLegende() {
	  return isPagePlanningPrevisionnel() || isPagePlanningReel() || isPagePlanningTest();
    }

	// MENU

	public WOComponent selectMenu() {
	  setSelectedItemMenu(unItemMenu);
	  return null;
	}
    
    
	// message d'avertissement pour un menu
	public String unToolTipMenu;
    
	/**
	 * faut-il afficher un avertissment a cote d'un menu
	 * - ex : periode de cet et cet non bascule
	 * @return
	 */
	public boolean isToolTipMenuVisible() {
		
		unToolTipMenu = StringCtrl.emptyString();
		
		// l'agent n'a pas d'horaires ...
		if (unItemMenu.equals(ConstsMenu.MENU_PERSO_HORAIRES)) {
			if (lePlanning().affectationAnnuelle().horaires().count() == 0) {
				unToolTipMenu = 
					"<CENTER>" +
					"Vous n avez pas d horaires de saisi ...<BR>" +
					"Cliquez ici pour les entrer." +
					"</CENTER>"
					;
			}
		}
      
		// l'agent a un reliquat non bascule pdt la periode du cet
		if (unItemMenu.equals(ConstsMenu.MENU_PERSO_CET)) {
			
			CetFactory cetFactory = lePlanning().affectationAnnuelle().cetFactory();
			
			// le message pour la décision a prendre sur l'ancien CET
			if (cetFactory.isDemandePassageRegimePerenneFaisable(false)) {
				unToolTipMenu = "&raquo; Vous devez statuer sur votre CET &quot;ancien r&eacute;gime&quot;<br/>";
			}
			
			// le message pour la demande d'épargne
			if (cetFactory.isEpargneFaisable(false)) {
				unToolTipMenu += 
					"&raquo; Vous pouvez &eacute;pargner jusqu'&agrave; <b>"  + 
					cetFactory.reliquatPourBlocageCetMaxEnJour7h00Arrondi() + "j. &agrave; 7h00" +
					"</b> de vos reliquats !<br/>";
					;
			} else {
				// le message pour l'option a faire sans épargne
				
				if (cetFactory.isOptionFaisable(0, false)) {
					int excedent20jEnJour = cetFactory.depassementSeuil20JoursPourEpargneCetEnMinutes(0) / ConstsJour.DUREE_JOUR_7H00;
					unToolTipMenu += "&raquo; Vous devez exercer votre droit d'option sur l'excedent de 20j. de votre solde CET " + 
					 	", soit <b>" + excedent20jEnJour + "j. &agrave; 7h00)</b><br/>";
				}
			}

			// ajouter des infos communes s'il y a un message
			// dates d'ouverture de la période
			if (!StringCtrl.isEmpty(unToolTipMenu)) {
				unToolTipMenu = 
						"La p&eacute;riode d'alimentation et d'exercice du droit d'option sur le CET est ouverte du <b>" + 
						DateCtrlConges.dateToString(lePlanning().affectationAnnuelle().dateDebutDemandeCet()) + 
					"</b> au <b>" + DateCtrlConges.dateToString(lePlanning().affectationAnnuelle().dateFinDemandeCet()) + "</b><br/><br/>" + 
						unToolTipMenu +
					"<br/><b><i>N'oubliez pas de faire votre demande avant la date limite ...</i></b>";
			}
			

			
		}
		
		// l'agent possede un excedent d'heures supplementaires
		if (unItemMenu.equals(ConstsMenu.MENU_PERSO_FIC_ROSE)) {
			int minutesBilan = lePlanning().bilanHSCCEnMinutes();
			if (minutesBilan > 0) {
				String heuresBilan = TimeCtrl.stringHeureToDuree(TimeCtrl.stringForMinutes(minutesBilan));
				String jourMoisFinAnnee = DateCtrlConges.dateToString(lePlanning().affectationAnnuelle().dateFinAnnee()).substring(0,5);
				unToolTipMenu = 
					"Vous poss&eacute;dez un exc&eacute;dent d'heures suppl&eacute;mentaires de <b>" + heuresBilan + "</b><br/><br/>" +
					"Les heures suppl&eacute;mentaires peuvent &ecirc;tre r&eacute;cup&eacute;r&eacute;es par des absences du "+
					"type <b>cong&eacute;s compensateurs</b>.<br/><br/>" + 
					//TODO a rajouter quand les CC seront a la demi journee
					/*"Ces cong&eacute;s compensateurs peuvent &ecirc;tre d&eacute;compt&eacute;s en heures/minutes, demi-journ&eacute;e (AM-AM) " +
					"et journ&eacute;e compl&egrave;te (AM-PM). </font><br>"+*/
					"<b>La r&eacute;cup&eacute;ration doit &ecirc;tre effectu&eacute;e avant le "+ jourMoisFinAnnee + "  " +
					" de l'ann&eacute;e universitaire</b> au titre de laquelle les heures "+
					"suppl&eacute;mentaires ont &eacute;t&eacute; effectu&eacute;es.<br>" +
					"<b><font color=red>Pass&eacute; ce terme,&nbsp; aucune r&eacute;cup&eacute;ration ne sera possible.</b></font><br/><br/>"+
					"Sous r&eacute;serve de contraintes exceptionnelles et d'une demande clairement "+
					"motiv&eacute;e par votre responsable de service, une d&eacute;rogation <br>"+
					"pourra le cas &eacute;ch&eacute;ant &ecirc;tre&nbsp; appliqu&eacute;e. Les heures non prises seront "+
					"alors ajout&eacute;es au solde de cong&eacute; de la nouvelle ann&eacute;e<br>"+
					"universitaire par votre gestionnaire par g&eacute;n&eacute;ration d'une ligne "+
					"<b>r&eacute;gulation solde</b> sur votre fiche rose.";
			}
		}

		
		boolean isVisible = false;

		if (!StringCtrl.isEmpty(unToolTipMenu)) {
			isVisible = true;
		}
		
		return isVisible;
	}
	
	public boolean isPagePlanningPrevisionnel(){    return selectedItemMenu.equals(ConstsMenu.MENU_PERSO_PLA_PREV);  }
	public boolean isPagePlanningReel(){            return selectedItemMenu.equals(ConstsMenu.MENU_PERSO_PLA_REEL);  }
	public boolean isPagePlanningTest(){            return selectedItemMenu.equals(ConstsMenu.MENU_PERSO_PLA_TEST);  }
	public boolean isPagePlanningBackup(){          return selectedItemMenu.equals(ConstsMenu.MENU_PERSO_PLA_BACK);  }
	public boolean isHoraire(){				    					return selectedItemMenu.equals(ConstsMenu.MENU_PERSO_HORAIRES);	}
	public boolean isFicheRose(){		    	   				return selectedItemMenu.equals(ConstsMenu.MENU_PERSO_FIC_ROSE);	}	
	public boolean isImprimesDivers(){		    	  	return selectedItemMenu.equals(ConstsMenu.MENU_PERSO_IMP_DIVERS);	}	
	public boolean isCET(){		    	     						return selectedItemMenu.equals(ConstsMenu.MENU_PERSO_CET);	}
	public boolean isHistorique(){		        			return selectedItemMenu.equals(ConstsMenu.MENU_PERSO_HISTO);	}
	
	// SETTERS GETTERS
	
    //FIXME bug : laSemaine=null si on fait actu->reel, histo->reel, actu->previsionnel
	public Semaine laSemaine()						{	return _laSemaine; }
	public void setLaSemaine(Semaine value) 		{	
		isSemaineModifiable = null;
	  _laSemaine = value; 
	}

  /**
   * doit on afficher un planning
   */
  public boolean showPlanning() {
    return isPagePlanningPrevisionnel()  || isPagePlanningReel() || 
    isPagePlanningTest() || isPagePlanningBackup();
  }

  /**
   * L'affichage est le meme pour le reel 
   * et le planning de comparaison
   */
  public boolean isPlanningReelOuBack() {
    return isPagePlanningReel() || isPagePlanningBackup();
  }
    
  /**
   * Si le reliquat est negatif, on affiche "dont reliquat"
   * sinon on affiche (+reliquat)
   */
  public boolean isReliquatNegatif() {
    return lePlanning.reliquatInitial().startsWith("-");
  }
  
  // GESTION DE L'APPUI SUR CHAQUE ITEM DU MENU
  
  private void selectFicheRose()     {   lePlanning().setType("R");}
  private void selectPlanningPrev()  {   lePlanning().setType("P");}
  private void selectPlanningReel()  {   lePlanning().setType("R");}
  private void selectPlanningTest()  {   lePlanning().setType("T");}
  private void selectPlanningBack()  {   lePlanning().setType("S");}
  private void selectHoraires()      {   }
  private void selectCET()           {   }
  private void selectHistorique()    {   }
  
  // les div
  
  /**
   * Si la semaine n'a pas d'horaire, on affiche la div
   * contenant le pt d'interrogation, en avant plan.
   * <b>ne marche pas sous IE</b>
   */
  public boolean showDivNoHoraire() {
    return laSemaine().isActive() && !isUsingNavigatorIE && laSemaine().horaire() == null;
  }
}