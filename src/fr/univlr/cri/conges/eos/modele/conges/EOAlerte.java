// EOAlerte.java
// Created on Sat Aug 14 05:42:39  2004 by Apple EOModeler Version 5.2

package fr.univlr.cri.conges.eos.modele.conges;

import java.util.*;

import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.*;

import fr.univlr.cri.conges.Session;
import fr.univlr.cri.conges.constantes.ConstsAlerte;
import fr.univlr.cri.conges.constantes.ConstsOccupation;
import fr.univlr.cri.conges.databus.CngDroitBus;
import fr.univlr.cri.conges.databus.CngPlanningBus;
import fr.univlr.cri.conges.databus.CngPreferenceBus;
import fr.univlr.cri.conges.databus.CngUserInfo;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.planning.EOOccupation;
import fr.univlr.cri.conges.objects.Planning;
import fr.univlr.cri.conges.utils.*;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.util.wo5.DateCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.CRIMailBus;
import fr.univlr.cri.webapp.LRUserInfo;
import fr.univlr.cri.webapp.LRUserInfoDB;

public class EOAlerte 
	extends _EOAlerte {

  private static boolean modeDebug;
  private static CRIMailBus mailBus;
  private static CRIDataBus dataBus;
  @Deprecated
  private static String appUrl;
  private static boolean appUseSam;
  private static String appAdminMail;
  private static String appSamMail;

  public static int validationPlanningNiveau;
  
	public static void initStaticFields(boolean aModeDebug,
      CRIMailBus aMailBus, CRIDataBus aDataBus, String anAppUrl, 
      boolean anAppUseSam, String anAppAdminMail, String anAppSamMail) {
    modeDebug = aModeDebug;
    mailBus = aMailBus;
    dataBus = aDataBus;
    appUrl = anAppUrl;
    appUseSam = anAppUseSam;
    appAdminMail = anAppAdminMail;
    appSamMail = anAppSamMail;
  }

  /**
   * Le pointeur vers le planning est disponible lors de sa creation.
   * On l'utilise pour connaitre la valeur des conges restants lors
   * de l'envoi du mail au valideur.
   */
  private Planning planning;
  
  public EOAlerte() {
    super();
  }

  /**
   * La boite a random
   */
  private static Random randommer = new Random();
  
  /**
   * Generation aleatoire d'un hashcode
   */
  private static String generateRandomHashcode() {
  	String hashcode = "";
		for (int i=0;i<32;i++)
			hashcode = hashcode.concat((new Character(ConstsAlerte.HASH_DICTIONARY.charAt(
					randommer.nextInt(ConstsAlerte.HASH_DICTIONARY.length())))).toString()); 
		return hashcode;
  }
  
  /**
   * @param uneOccupation
   * @return
   */
  public static EOAlerte newEOAlerteInContext(
  		EOOccupation uneOccupation, EOEditingContext edc, Planning aPlanning) {
    EOAlerte newEOAlerte = new EOAlerte();
    newEOAlerte.planning = aPlanning;
    // certains champ se construisent automatique si c'est une occupation
    if (uneOccupation != null) {
      newEOAlerte.setAffectationAnnuelleRelationship(uneOccupation.affectationAnnuelle());
      newEOAlerte.setOccupationRelationship(uneOccupation);
      String libelleAlerte = "";

      // on determine la position dans la demi journee
      String dateDebutAmPm = ConstsOccupation.OCC_MATIN;
      String dateFinAmPm = ConstsOccupation.OCC_MATIN;
      if (uneOccupation.isOccupationMinute() == false) {
        if (DateCtrl.isAfterEq(uneOccupation.dateDebut(), 
            TimeCtrl.dateToMinuit(
                uneOccupation.dateDebut()).timestampByAddingGregorianUnits(0, 0, 0, 12, 0, 0)))
          dateDebutAmPm = ConstsOccupation.OCC_APREM;
        if (DateCtrl.isAfterEq(uneOccupation.dateFin(), 
            TimeCtrl.dateToMinuit(
                uneOccupation.dateFin()).timestampByAddingGregorianUnits(0, 0, 0, 12, 0, 0)))
          dateFinAmPm = ConstsOccupation.OCC_APREM;
      } else {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(uneOccupation.dateDebut());
        String heures = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minutes = String.valueOf(calendar.get(Calendar.MINUTE));
        if (Integer.parseInt(heures) < 10)
          heures = "0" + heures;
        if (Integer.parseInt(minutes) < 10)
          minutes = "0" + minutes;
        dateDebutAmPm = heures + ":" + minutes;
        calendar.setTime(uneOccupation.dateFin());
        heures = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        minutes = String.valueOf(calendar.get(Calendar.MINUTE));
        if (Integer.parseInt(heures) < 10)
          heures = "0" + heures;
        if (Integer.parseInt(minutes) < 10)
          minutes = "0" + minutes;
        dateFinAmPm = heures + ":" + minutes;
      }

      if (ConstsOccupation.CODE_EN_COURS_DE_VALIDATION.equals(uneOccupation.status()) || 
      		ConstsOccupation.CODE_VALIDEE.equals(uneOccupation.status())) {
        libelleAlerte = 
          uneOccupation.typeOccupation().libelle() + " du " + 
          DateCtrl.dateToString(uneOccupation.dateDebut()) + " " + dateDebutAmPm + " au "  + 
          DateCtrl.dateToString(uneOccupation.dateFin()) + " " + dateFinAmPm  + 
          " (" + uneOccupation.getValeurEnHeures()+")"+ "\n";
        if ((uneOccupation.motif() != null) && (!"".equals(uneOccupation.motif())))
          libelleAlerte += " Motif : " + uneOccupation.motif();
      } else {
        libelleAlerte = 
          "Demande de suppression : " + uneOccupation.typeOccupation().libelle() + 
          " du " + DateCtrl.dateToString(uneOccupation.dateDebut()) + " " + 
          dateDebutAmPm + " au " + DateCtrl.dateToString(uneOccupation.dateFin()) + " " + dateFinAmPm + 
          " (" + uneOccupation.getValeurEnHeures()+")";
      }
      newEOAlerte.setLibelle(libelleAlerte);
      // mise a jour de la relation alertes
      uneOccupation.addObjectToBothSidesOfRelationshipWithKey(newEOAlerte, EOOccupation.ALERTES_KEY);
    }

    // on genere les hashcode
    newEOAlerte.setCalHashcodeVal(generateRandomHashcode());
    newEOAlerte.setCalHashcodeVis(generateRandomHashcode());
    return newEOAlerte;
  }


  public void insertInEditingContext(EOEditingContext ec) {
    if (ec != null) {

      if (ec.globalIDForObject(this) == null) {
        ec.insertObject(this);
      }
    }
  }

  /**
   * retourne la liste de tous les email des individu
   * passes en parametre : liste de <code>EOIndividu</code>
   * a passer directement en parametre a la methode
   * d'envoi de message. Si l'individu est celui concerne
   * par l'alerte, alors son adresse sera ignoree.
   */
  private String formatedEmailsIgnore(NSArray individus, EOIndividu individuToIgnore) {
    StringBuffer strMails = new StringBuffer();
    //EOIndividu individuSelf = affectationAnnuelle().individu();
    Enumeration enumIndividus = individus.objectEnumerator();
    
    // recup des responsables
    while (enumIndividus.hasMoreElements()) {
      EOIndividu unIndividu = (EOIndividu) enumIndividus.nextElement();
      // on enleve l'individu cibl� des destinataires des mail si c'est lui meme
      if (unIndividu == individuToIgnore) {
        continue;
      }
      NSArray comptes = (NSArray) unIndividu.valueForKeyPath(EOIndividu.REPART_COMPTES_KEY);
      if ((comptes != null) && (comptes.count() > 0)) {
        //String email = FinderCompte.findEmailPourIndividuInContext(editingContext(), unIndividu);
        userInfo().compteForPersId(unIndividu.persId(), true);
        String email = userInfo().email();
        strMails.append(email).append(",");
      }
    }

    // on enleve la derniere virgule
    if (strMails.length() > 0) {
      strMails.deleteCharAt(strMails.length()-1);
    }
    return strMails.toString();
  }
  
  
  /**
   * Pour utiliser StringCtrl.replaceWithDico(),
   * il faut recuperer les noms de variable sans 
   * les caraceteres '%'. Cette methode enleve ce
   * caractere avant et apres.
   * @param varName
   * @return
   */
  private String clean(String varName) {
  	return StringCtrl.replace(varName, "%", "");
  }
  
  /**
   * Determiner si l'alerte concernant une occupation
   * ou un planning modifie / cree par delegation
   * @return
   */
  private boolean isAlerteDelegation() {
  	return 
  		(occupation() == null && affectationAnnuelle().toIndividuDemandeur() != null) ||
  		(occupation() != null && occupation().toIndividuDemandeur() != null);
  }

  /**
   * L'enregistrement <code>EOIndividu</code> de la personne
   * deleguee qui a effectue la demande
   * @return
   */
  public EOIndividu individuDelegue() {
  	EOIndividu delegue = null;
  	if (isAlerteDelegation()) {
  		if (occupation() == null) {
  			delegue = affectationAnnuelle().toIndividuDemandeur();
  		} else {
  			delegue = occupation().toIndividuDemandeur();
  		}
  	}
  	return delegue;
  }
  
  /**
   * Retourne le lien de connexion à l'application, tenant
   * de
   */
  private String appURLConnexionDirecte(Session session) {
  	return session.app().getApplicationURL(session.context()) + "/wa/connexion";
  }
    
  /**
   * Permet d'envoyer a tous les responsables et valideurs un mail 
   * pour les avertir d'un NOUVEL evenement
   */
  public void sendMailsNouvelleAlerte(Session session) throws Exception {
    try {
      EOIndividu individu = affectationAnnuelle().individu();
      String emailIndividu = "";
      String emailsResponsables = formatedEmailsIgnore(
      		planningBus().responsables(droitBus(), affectationAnnuelle(), occupation(), false), individu);
      String emailsViseurs = formatedEmailsIgnore(
      		planningBus().viseurs(droitBus(), affectationAnnuelle(), occupation(), false), individu);
   
      // construire le dico de validation
      Hashtable dico = new Hashtable();
      dico.put(clean(ConstsAlerte.VAR_DEMANDEUR), 				individu.nomComplet());
      dico.put(clean(ConstsAlerte.VAR_LIBELLE_ALERTE), 		libelle());
      //dico.put(clean(ConstsAlerte.VAR_APP_URL), 					appUrl);
      dico.put(clean(ConstsAlerte.VAR_APP_URL),						appURLConnexionDirecte(session));
      dico.put(clean(ConstsAlerte.VAR_VERBE), 						"valider");

      // les specificites liees a SAM
      if (appUseSam) {
        dico.put(clean(ConstsAlerte.VAR_SAM_MAIL), 				appSamMail);
        dico.put(clean(ConstsAlerte.VAR_ATTR), 						"VAL");
        dico.put(clean(ConstsAlerte.VAR_HASH), 						calHashcodeVal());
      }
      
      // construire le dico specifiques a la nature de l'alerte
      Hashtable dicoSpec = new Hashtable();
      // -- rajouter les specificites selon la nature de l'alerte --
      boolean hasSpecConges = !isValidationPrev() && ! isModificationReel() && 
      	!ConstsOccupation.CODE_EN_COURS_DE_SUPPRESSION.equals(occupation().status()) &&
      	!occupation().isAbsenceBilan() &&
      	!occupation().isAbsenceCET();
      	
      if (hasSpecConges) {
      	// specificite de nouvelle occupation
				dico.put(clean(ConstsAlerte.VAR_SPEC), 						ConstsAlerte.VAR_SPEC_NOUVELLE_OCCUPATION);
        dicoSpec.put(clean(ConstsAlerte.VAR_CONGES_RESTANTS),	planning.congesGlobalRestants());
      } else {
      	// aucune specificite
      	dico.put(clean(ConstsAlerte.VAR_SPEC), "");
      }
      
      // construire le dico specifique a la la delegation
      Hashtable dicoDelegation = new Hashtable();
      if (isAlerteDelegation()) {
      	// 
      	dico.put(clean(ConstsAlerte.VAR_CONTENT_DELEGATION), ConstsAlerte.CONTENT_DELEGATION);
      	dicoDelegation.put(clean(ConstsAlerte.VAR_DELEGUE),	individuDelegue().nomComplet());
      } else {
      	// aucune specificite
      	dico.put(clean(ConstsAlerte.VAR_CONTENT_DELEGATION), "");
      }
         
      // envoi aux responsables
      if (emailsResponsables.length() > 0) {

        String titre = ConstsAlerte.PREFIX_MAIL_TITLE_DEMANDE + "validation : " + libelle();
        titre = StringCtrl.replace(titre, "\n", " "); // on met titre a plat

        String contentValidation = 
        	StringCtrl.replaceWithDico(
        			StringCtrl.replaceWithDico(
      						StringCtrl.replaceWithDico(appUseSam ? ConstsAlerte.PATTERN_NEW_ALERT_SAM : ConstsAlerte.PATTERN_NEW_ALERT, dico),
  						dicoSpec),
					dicoDelegation);

        // en mode test, on prefixe le message et le destinataire devient l'administrateur
        if (modeDebug) {
          titre = "MODE TEST: " + titre;
          contentValidation = StringCtrl.replace(StringCtrl.replace(ConstsAlerte.PREFIX_CONTENT_DEBUG, 
          		ConstsAlerte.VAR_ADMIN_MAIL, appAdminMail), ConstsAlerte.VAR_MAIL_TO, emailsResponsables)
              + contentValidation;
          emailsResponsables = appAdminMail;
        }

        userInfo().compteForPersId(individu.persId(), true);
        emailIndividu = userInfo().email();
        mailBus.sendMail(emailIndividu, emailsResponsables, null, titre, contentValidation);
        
        // si c'est une demande par delegation, on previent la personne
        // concernee (le from etant le demandeur)
        if (isAlerteDelegation()) {
        	titre = ConstsAlerte.PREFIX_MAIL_TITLE_DEMANDE + "validation : " + libelle() + " par delegation";
        	titre = StringCtrl.replace(titre, "\n", " "); // on met titre a plat
        	
        	contentValidation = "";
        	
        	// le mail du delegue
        	userInfo().compteForPersId(individuDelegue().persId(), true);
        	String emailDelegue = userInfo().email();
        	
        	// le mail de l'agent concerne
          userInfo().compteForPersId(individu.persId(), true);
          String emailTo = userInfo().email();
          
          contentValidation = individuDelegue().nomComplet() + " a fait une demande en votre nom :\n" + titre;
          
          // en mode test, on prefixe le message et le destinataire devient l'administrateur
          if (modeDebug) {
            titre = "MODE TEST: " + titre;
            contentValidation = StringCtrl.replace(StringCtrl.replace(ConstsAlerte.PREFIX_CONTENT_DEBUG, 
            		ConstsAlerte.VAR_ADMIN_MAIL, appAdminMail), ConstsAlerte.VAR_MAIL_TO, emailIndividu)
                + contentValidation;
            emailTo = appAdminMail;
          }

          mailBus.sendMail(emailDelegue, emailTo, null, titre, contentValidation);
          
        }
      }

      // envoi aux viseurs
      if (emailsViseurs.length() > 0) {
        String titre = ConstsAlerte.PREFIX_MAIL_TITLE_DEMANDE + "visa : " + libelle();
        titre = StringCtrl.replace(titre, "\n", " "); // on met titre a plat

        // remplacer le verbe et les codes pour la validation
        dico.put(clean(ConstsAlerte.VAR_ATTR), 	"VIS");
        dico.put(clean(ConstsAlerte.VAR_VERBE), 	"viser");
        dico.put(clean(ConstsAlerte.VAR_HASH), 	calHashcodeVis());

        String contentVisa = 
        	StringCtrl.replaceWithDico(
        			StringCtrl.replaceWithDico(
        					StringCtrl.replaceWithDico(appUseSam ? ConstsAlerte.PATTERN_NEW_ALERT_SAM : ConstsAlerte.PATTERN_NEW_ALERT, dico),
    					dicoSpec),
					dicoDelegation);
        
        if (modeDebug) {
          titre = "MODE TEST: " + titre;
          contentVisa = StringCtrl.replace(StringCtrl.replace(ConstsAlerte.PREFIX_CONTENT_DEBUG, 
          		ConstsAlerte.VAR_ADMIN_MAIL, appAdminMail), ConstsAlerte.VAR_MAIL_TO, emailsViseurs) + contentVisa;
          emailsViseurs = appAdminMail;
        }
        mailBus.sendMail(emailIndividu, emailsViseurs, null, titre, contentVisa);
      }
    } catch (Exception e) {
    	e.printStackTrace();
      sendMailResponsableIntrouvable();
      throw e;
    }

  }

  /**
   * Envoi de mail au demandeur que l'operation est terminee.
   * On envoi egalement le mail au(x) valideur(s) ainsi qu'aux eventuels autres viseurs
   */
  public void sendMailAlerteTraitee(LRUserInfo from, String unType) {
    userInfo().compteForPersId(affectationAnnuelle().individu().persId(), true);
    String emailTo = userInfo().email();
    userInfo().compteForPersId(from.persId(), true);
    String emailFrom = userInfo().email();
    String emailCc = null;
    
    // on previent les viseurs et les valideurs
    // on catch l'exception des responsables, s'agissant d'un mail d'un viseur, le probleme
  	// des responsable aura forcement eu lieu lors de la demande par l'agent
  	try {
  		NSArray destList = planningBus().
  			viseurs(droitBus(), affectationAnnuelle(), occupation(), false).
  			arrayByAddingObjectsFromArray(planningBus().
  			responsables(droitBus(), affectationAnnuelle(), occupation(), false));
  		boolean shouldAddDelegue = false;
  		// ajouter le demandeur delegue dans les destinataires
  		if (isAlerteDelegation()) {
  			CngUserInfo destUi = new CngUserInfo(droitBus(), preferenceBus(), editingContext(), individuDelegue().persId());
  			// on l'ajoute pas si ces preferences disent qu'il ne le faut pas
  			if (destUi.isMailRecipisseDelegation()) {
  				shouldAddDelegue = true;
  			}
  		}
  		// on enleve les responsables qui ne veulent pas du mail de recipisse
  		NSArray destMailList = new NSArray();
  		for (int i=0; i<destList.count(); i++) {
  			EOIndividu dest = (EOIndividu) destList.objectAtIndex(i);
  			CngUserInfo destUi = new CngUserInfo(droitBus(), preferenceBus(), editingContext(), dest.persId());
  			if (destUi.isMailRecipisse()) {
  				destMailList = destMailList.arrayByAddingObject(dest);
  			}
  		}
  		// ajout du delegue
  		if (shouldAddDelegue) {
  			destMailList = destMailList.arrayByAddingObject(individuDelegue());
  		}
  		// enlever celui qui envoi l'alerte
   		EOIndividu individuFrom = EOIndividu.findIndividuForPersIdInContext(editingContext(), from.persId());
   		emailCc = formatedEmailsIgnore(destMailList, individuFrom);
  	} catch (Throwable e) {
    	e.printStackTrace();
    	return;
  	}
    
    String title = ConstsAlerte.ALERT_MAIL_SUBJECT_PREFIX + "Demande " + unType + " : " + libelle();
    String content = "Votre demande :\n" + libelle() + "\na ete " + unType + " par " + from.nom() + " " + from.prenom();
    if (!StringCtrl.isEmpty(commentaire())) {
      content += "\n\nCommentaire du responsable :\n" + commentaire();
    }
    if (modeDebug) {
      title = "MODE TEST: " + title;
      content = 
      	StringCtrl.replace(
      			StringCtrl.replace(
      					StringCtrl.replace(
      							ConstsAlerte.PREFIX_CONTENT_DEBUG, ConstsAlerte.VAR_ADMIN_MAIL, appAdminMail), 
      							ConstsAlerte.VAR_MAIL_TO, emailTo),
      							ConstsAlerte.VAR_MAIL_CC, emailCc) + content;
      emailTo = appAdminMail;
      emailCc = null;
    }
    mailBus.sendMail(emailFrom, emailTo, emailCc, title, content);
  }

  /**
   * Envoi de mail d'erreur au demandeur car aucun responsable n'a ete trouve
   * Copie a l'administrateur
   */
  private void sendMailResponsableIntrouvable() {
    String emailFrom = appAdminMail;
    userInfo().compteForPersId(affectationAnnuelle().individu().persId(), true);
    String emailTo = userInfo().email();
    String emailCc = appAdminMail;
    String title = ConstsAlerte.ALERT_MAIL_SUBJECT_PREFIX + "Erreur de l'application";
    String content = ConstsAlerte.TXT_ERREUR_RESPONSABLE;
    if (modeDebug) {
      title = "MODE TEST: " + title;
      content = StringCtrl.replace(StringCtrl.replace(ConstsAlerte.PREFIX_CONTENT_DEBUG, 
      		ConstsAlerte.VAR_ADMIN_MAIL, appAdminMail), ConstsAlerte.VAR_MAIL_TO, emailTo) + content;
      emailTo = appAdminMail;
      emailCc = null;
    }
    mailBus.sendMail(emailFrom, emailTo, emailCc, title, content);
    // envoi a l'admin avec la trace de l'exception
  }

  public boolean isAcceptee() {
    boolean isAcceptee = false;
    String reponse = flagReponse();
    if (reponse != null && "1".equals(reponse)) {
      isAcceptee = true;
    }

    return isAcceptee;
  }

  public boolean isRefusee() {
    boolean isRefusee = false;
    String reponse = flagReponse();
    if (reponse != null && "0".equals(reponse)) {
      isRefusee = true;
    }

    return isRefusee;
  }

  /**
   * est-ce une alerte concernant une demande de validation du planning previsionnel ?
   */
  public boolean isValidationPrev() {
    return libelle() != null && libelle().startsWith(ConstsAlerte.ALERT_PREFIX_LIBELLE_VALID_P_PREV);
  }

  /**
   * est-ce une alerte concernant une demande de modification du planning reel ?
   */
  public boolean isModificationReel() {
    return libelle() != null && libelle().startsWith(ConstsAlerte.ALERT_PREFIX_LIBELLE_MODIF_P_REEL);
  }
  
  /** 
   * instance de LRUserInfoDB pour effectuer
   * les recherches d'email 
   */
  private LRUserInfoDB _userInfo;
  
  private LRUserInfoDB userInfo() {
    if (_userInfo == null)
      _userInfo = new LRUserInfoDB(dataBus);
    return _userInfo;
  }
  
  private Number oidOccupation;
  
  /**
   * En lecture seule, pour les logs
   * @return -1 si erreur
   */
  public Number oidOccupation() {
    if (oidOccupation == null) {
      oidOccupation = new Integer(-1);
      if (occupation() != null) {
        Number pk = (Number) EOUtilities.primaryKeyForObject(editingContext(), occupation()).valueForKey("oid");
        if (pk != null)
          oidOccupation = pk;
      }
    }
    return oidOccupation;
  }

  // les bus de donnees
  
  private CngDroitBus _droitBus;
  
  public CngDroitBus droitBus() {
  	if (_droitBus == null) {
  		_droitBus = new CngDroitBus(editingContext());
  	}
  	return _droitBus;
  }
  
  private CngPreferenceBus _preferenceBus;
  
  public CngPreferenceBus preferenceBus() {
  	if (_preferenceBus == null) {
  		_preferenceBus = new CngPreferenceBus(editingContext());
  	}
  	return _preferenceBus;
  }  
  
  private CngPlanningBus _planningBus;
  
  public CngPlanningBus planningBus() {
  	if (_planningBus == null) {
  		_planningBus = new CngPlanningBus(editingContext());
  	}
  	return _planningBus;
  }
  

}
