package fr.univlr.cri.conges.databus;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.Session;
import fr.univlr.cri.conges.constantes.ConstsOccupation;
import fr.univlr.cri.conges.eos.modele.conges.EOAlerte;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOOccupation;
import fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation;
import fr.univlr.cri.conges.objects.Planning;
import fr.univlr.cri.conges.objects.finder.FinderClasse;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.webapp.LRLog;
import fr.univlr.cri.ycrifwk.utils.UtilDb;

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
 * Bus de gestion des occupations : les appels sont faits depuis le planning
 * ou la fiche rose.
 * 
 * @author Cyril TARADE <cyril.tarade at univ-lr.fr>
 */

public class CngOccupationBus extends CngDataBus {

  public CngOccupationBus(EOEditingContext editingContext) {
    super(editingContext);
  }
  
  // la liste des plannings impactes par l'occupation
  private NSArray plannings;
  
  /**
   * Methode pour remettre a zero les varaibles
   */
  private void resetBus() {
    plannings = new NSArray();
  }
  
  
  /**
   * Effectue les traitements communs qui vont remplir les
   * variables reutilises dans toutes les methodes
   * 
   * - la liste des plannings impactes par la saisie d'une absence (sur une seule structure)
   */
  private void fillVariables(NSTimestamp dateDebut, NSTimestamp dateFin, EOStructure structure) {
    // on cale la date de fin a 0:00 pour les tests 
    // pour regler le cas particulier d'un conge pose le dernier jour de l'annee univ
    NSTimestamp dateFinDebutJournee = TimeCtrl.dateToMinuit(dateFin);
    
    if (dateDebut != null && dateFin != null) {
      EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(
          "((dateDebutAnnee>=%@ AND dateDebutAnnee<=%@) OR" +
          "(dateFinAnnee>=%@ AND dateFinAnnee<=%@) OR" +
          "(dateDebutAnnee<=%@ AND dateFinAnnee>=%@) OR" +
          "(dateDebutAnnee>=%@ AND dateFinAnnee<=%@)) AND structure = %@",
          new NSArray( new Object[] { 
              dateDebut, dateFinDebutJournee, 
              dateDebut, dateFinDebutJournee, 
              dateDebut, dateFinDebutJournee, 
              dateDebut, dateFinDebutJournee, structure}));
      NSArray affectationsAnnuelles = EOQualifier.filteredArrayWithQualifier(cngSession().affAnnsPerso(), qual);
      // on sauvegarde la selection de la session
      EOAffectationAnnuelle backupSelectionAffSAnnPerso = cngSession().selectedAffAnnPerso();
      for (int i = 0; i < affectationsAnnuelles.count(); i++) {
        // on fixe 1 par 1 l'aff annuelle de la session pour recuperer tous les plannings, sans forcement tout recalculer
        cngSession().setSelectedAffAnnPerso((EOAffectationAnnuelle) affectationsAnnuelles.objectAtIndex(i));
        plannings = plannings.arrayByAddingObject(cngSession().getLePlanningSelectionne());
      }

      // retribution de la selection
      cngSession().setSelectedAffAnnPerso(backupSelectionAffSAnnPerso);
    }


  }
  
  /**
   * Demande de suppression d'une occupation. Si elle est 
   * en cours de validation : suppression immediate, sinon
   * envoi d'une demande aux responsables.
   * 
   * @return <code>true</code> si l'op. s'est bien passee
   */
  public boolean deleteOccupation(EOEditingContext ec, Session session, Planning planning, EOOccupation occupation) {
    boolean transactionOK = false;
    NSTimestamp dateDebut = occupation.dateDebut();
    NSTimestamp dateFin = occupation.dateFin();
    // on recupere la liste de toutes les occupations associees en base
    NSArray recsOccupation = EOOccupation.findOccupationsInContext(ec, planning.affectationAnnuelle().individu(),
        planning.affectationAnnuelle().structure(), occupation.typeOccupation(), planning.type(),
        occupation.dateDebut(), occupation.dateFin(), occupation.status());
    if (!planning.isPReel() || occupation.isSupprimableSansValidation()) {
      // on applique les modifications sur tous ces enregistrement
      for (int i = 0; i < recsOccupation.count(); i++) {
        EOOccupation recOccupation = (EOOccupation) recsOccupation.objectAtIndex(i);
        // on degage l'eventuelle alerte associee
        alerteBus().deleteAlertsForOccupation(ec, recOccupation);
        planning.removeAbsence(recOccupation);
        recOccupation.setStatus(ConstsOccupation.CODE_SUPPRIMEE);
        // on trace le demandeur s'il n'est pas la personne connectee
        if (userInfo().noIndividu().intValue() != recOccupation.affectationAnnuelle().individu().oid().intValue()) {
        	recOccupation.addObjectToBothSidesOfRelationshipWithKey(
        			EOIndividu.findIndividuForPersIdInContext(ec, userInfo().persId()), "toIndividuDemandeur");
        }
      }
    } else {
      // on applique les modifications sur tous ces enregistrement
      for (int i = 0; i < recsOccupation.count(); i++) {
        EOOccupation recOccupation = (EOOccupation) recsOccupation.objectAtIndex(i);
        // suppression alerte precedemment associees
        alerteBus().deleteAlertsForOccupation(ec, recOccupation);
        recOccupation.setStatus(ConstsOccupation.CODE_EN_COURS_DE_SUPPRESSION);
        // on trace le demandeur s'il n'est pas la personne connectee
        if (userInfo().noIndividu().intValue() != recOccupation.affectationAnnuelle().individu().oid().intValue()) {
        	recOccupation.addObjectToBothSidesOfRelationshipWithKey(
        			EOIndividu.findIndividuForPersIdInContext(ec, userInfo().persId()), "toIndividuDemandeur");
        }
      }
      // create de l'alerte indiquant la suppression
      EOAlerte newAlerte = EOAlerte.newEOAlerteInContext(occupation, ec, planning);
      newAlerte.insertInEditingContext(ec);
      try {
        newAlerte.sendMailsNouvelleAlerte(session);
      } catch (Exception e) {
        setErrorMessage(e.getMessage());
        ec.revert();
        return false;
      }
    }
    try {
	    UtilDb.save(ec, true);
      transactionOK = true;
    } catch (Throwable e) {
      transactionOK = false;
      setErrorMessage(e.getMessage());
    }
    if (transactionOK) {
      // on ne trace que les opérations sur le planning réel
      if (planning.isPReel()) {
      	LRLog.log(planning.affectationAnnuelle().individu().nomComplet() + " : suppression occupation : du " +
      			DateCtrlConges.dateToString(dateDebut,    "%d/%m/%Y %H:%M") + " au " + 
      			DateCtrlConges.dateToString(dateFin,      "%d/%m/%Y %H:%M") + logSuffixDelegation());
      }
    }
    return transactionOK;
  }
  
  /**
   * Methode de mise a jour des occupations. L'envoi de mail
   * est fait par la meme occasion.
   *
   * @return <code>true</code> si l'op. s'est bien passee
   */
  public boolean updateOccupation(EOEditingContext ec, Session session, Planning planning, EOOccupation occupation,
      NSTimestamp newDateDebut, NSTimestamp newDateFin, String newMotif) {
    boolean transactionOK = false;
    NSTimestamp dateDebutPrev, dateFinPrev;
    dateDebutPrev = dateFinPrev = null;
    // on recupere la liste de toutes les occupations associees en base
    NSArray recsOccupation = EOOccupation.findOccupationsInContext(ec, planning.affectationAnnuelle().individu(),
        planning.affectationAnnuelle().structure(), occupation.typeOccupation(), planning.type(), 
        occupation.dateDebut(), occupation.dateFin(), occupation.status());

    // on applique les modifications sur tous ces enregistrement  
    for (int i = 0; i < recsOccupation.count(); i++) {
      EOOccupation recOccupation = (EOOccupation) recsOccupation.objectAtIndex(i);
      
      planning.removeAbsence(recOccupation);
      
      
      Object arguments[] = { recOccupation.typeOccupation(), planning, newDateDebut, newDateFin, newMotif, ec };
      Class argumentTypes[] = { EOTypeOccupation.class, Planning.class, NSTimestamp.class, NSTimestamp.class, String.class, EOEditingContext.class };
      Object aTypeOccupation = FinderClasse.findClassForType(occupation.typeOccupation(), arguments, argumentTypes);
      Boolean isValide = (Boolean) (((NSKeyValueCoding) aTypeOccupation).valueForKey("isValide"));

      if (isValide.booleanValue()) {
        
        // supprimer l'alerte associee
        alerteBus().deleteAlertsForOccupation(ec, occupation);
     
        // backup anciennes valeurs
        dateDebutPrev = recOccupation.dateDebut();
        dateFinPrev = recOccupation.dateFin();
        
        // Mise a jour des comptes et du planning si necessaire
        recOccupation.setDateDebut(newDateDebut);
        recOccupation.setDateFin(newDateFin);
        recOccupation.setMotif(newMotif);
        recOccupation.setStatus(ConstsOccupation.CODE_EN_COURS_DE_VALIDATION);

        // recalculer la valeur pour que la durée dans l'alerte soit mis à jour
        ((NSKeyValueCoding) aTypeOccupation).valueForKey("calculerValeur");
        Number laValeur = (Number) ((NSKeyValueCoding) aTypeOccupation).valueForKey("laValeur");
        recOccupation.setValeurMinutes(new Integer(laValeur.intValue()));
        
        // on trace le demandeur s'il n'est pas la personne connectee
        if (userInfo().noIndividu().intValue() != recOccupation.affectationAnnuelle().individu().oid().intValue()) {
        	recOccupation.setToIndividuDemandeurRelationship(
        			EOIndividu.findIndividuForPersIdInContext(ec, userInfo().persId()));
        }

      } else {
      	setErrorMessage((String) (((NSKeyValueCoding) aTypeOccupation).valueForKey("errorMsg")));
      }
      // recalcul des donnees cache interne a l'occupation
      recOccupation.resetCache();
      planning.addAbsenceOccupation(recOccupation);
      occupation = recOccupation;
    }
    
    // recreer une alerte pour la nouvelle version de l'occupation
    if (occupation.flagNature().equals("R")) {
      EOAlerte uneAlerte = EOAlerte.newEOAlerteInContext(
      		occupation, ec, planning);
      uneAlerte.insertInEditingContext(ec);
      try {
        uneAlerte.sendMailsNouvelleAlerte(session);
        transactionOK = true;
      } catch (Exception e) {
        setErrorMessage(e.getMessage());
        ec.revert();
        transactionOK = false;
      }
    }
    
    // enregistrement global
    if (transactionOK) {
      try {
  	    UtilDb.save(ec, true);
        transactionOK = true;
      } catch (Throwable e) {
        setErrorMessage(e.getMessage());
        ec.revert();
        transactionOK = false;
      }
    }
    
    if (transactionOK) {
      // on ne trace que les opérations sur le planning réel
      if (planning.isPReel()) {
      	LRLog.log(planning.affectationAnnuelle().individu().nomComplet() + " : modification occupation : du " +
      			DateCtrlConges.dateToString(dateDebutPrev,    "%d/%m/%Y %H:%M") + " au " + 
      			DateCtrlConges.dateToString(dateFinPrev,      "%d/%m/%Y %H:%M") + " -> du " + 
      			DateCtrlConges.dateToString(newDateDebut,    "%d/%m/%Y %H:%M") + " au " + 
      			DateCtrlConges.dateToString(newDateFin,      "%d/%m/%Y %H:%M") + logSuffixDelegation());
      }
    }
    return transactionOK;
  }
  
  /**
   * Methode de creation d'une nouvelle occupation. Elle enregistre la
   * demande dans la base de donnees, autant de fois qu'il y a de planning
   * impactes. Elle se charge egalement des alertes.
   * 
   * @return la liste des plannings impactes par l'ajout de l'occupation
   * @return <code>null</code> si erreur
   */
  public NSArray addOccupation(EOEditingContext ec, Session session, Planning planning, EOTypeOccupation recTypeOcc, 
      NSTimestamp dateDebut, NSTimestamp dateFin, String motif) {
    boolean transactionOK = false;
    resetBus();
    fillVariables(dateDebut, dateFin, planning.affectationAnnuelle().structure());
    
    EOOccupation recOccupation = null;
    
    for (int i = 0; i < plannings.count(); i++) {
      Planning planningToChange = (Planning) plannings.objectAtIndex(i);
      // le meme type de planning que l'original
      planningToChange.setType(planning.type());

      // ignore les planning non concernes
      if (DateCtrlConges.isBefore(dateFin, planningToChange.affectationAnnuelle().dateDebutAnnee())
          || DateCtrlConges.isAfter(dateDebut, planningToChange.affectationAnnuelle().dateFinAnnee().timestampByAddingGregorianUnits(0,0,0,23,59,59))) {
        continue;
      }
      
      try {
        Object arguments[] = { recTypeOcc, planningToChange, dateDebut, dateFin, motif, ec };
        Class argumentTypes[] = { EOTypeOccupation.class, Planning.class, NSTimestamp.class, NSTimestamp.class, String.class, EOEditingContext.class };
        Object aTypeOccupation = FinderClasse.findClassForType(recTypeOcc, arguments, argumentTypes);
        Boolean isValide = (Boolean) (((NSKeyValueCoding) aTypeOccupation).valueForKey("isValide"));
        if (isValide.booleanValue()) {

          ((NSKeyValueCoding) aTypeOccupation).valueForKey("calculerValeur");
          Number laValeur = (Number) ((NSKeyValueCoding) aTypeOccupation).valueForKey("laValeur");
          Number laValeurPrev = laValeur;
          
          // Mise a jour des comptes et du planning si necessaire
          ((NSKeyValueCoding) aTypeOccupation).valueForKey(ConstsOccupation.CONFIRMER_KEY);
          // Creation de l'occupation
          EOOccupation uneOccupation = EOOccupation.newEOOccupationInContext(recTypeOcc, planningToChange, ec);
          uneOccupation.setDateDebut(dateDebut);
          uneOccupation.setDateFin(dateFin);
          uneOccupation.setValeurMinutes(new Integer(laValeur.intValue()));
          uneOccupation.setDureeReelle(new Integer(laValeurPrev.intValue()));
          uneOccupation.setMotif(motif);
          
          // on trace le demandeur s'il n'est pas la personne connectee
          if (userInfo().noIndividu().intValue() != planningToChange.affectationAnnuelle().individu().oid().intValue()) {
          	uneOccupation.addObjectToBothSidesOfRelationshipWithKey(
          			EOIndividu.findIndividuForPersIdInContext(ec, userInfo().persId()), "toIndividuDemandeur");
          }
          
          if (!recTypeOcc.isOccupationMinute())
            planningToChange.addAbsenceOccupation(uneOccupation);
          else
            planningToChange.addOccupationJournaliere(uneOccupation);

          planningToChange.affectationAnnuelle().addObjectToBothSidesOfRelationshipWithKey(uneOccupation, "occupations");
          uneOccupation.insertInEditingContext(ec);
          
          recOccupation = uneOccupation;

        } else {
          setErrorMessage((String) ((NSKeyValueCoding) aTypeOccupation).valueForKey("errorMsg"));
          return null;
        }
      } catch (Throwable e) {
        e.printStackTrace();
      }
    }
    
    // enregistrement d'1 seule alerte pour cette occupation
    if (recOccupation != null && plannings.count() > 0) {
      Planning aPlanning = (Planning) plannings.lastObject();
      if (aPlanning.isPReel()) {
        EOAlerte uneAlerte = EOAlerte.newEOAlerteInContext(recOccupation, ec, aPlanning);
        uneAlerte.insertInEditingContext(ec);
        try {
          uneAlerte.sendMailsNouvelleAlerte(session);
        } catch (Exception e) {
          setErrorMessage(e.getMessage());
          ec.revert();
          return null;
        }
     }  
      try {
  	    UtilDb.save(ec, true);
        transactionOK = true;
      } catch (Throwable e) {
        setErrorMessage(e.getMessage());
      }
      // pas de sauvegarde si pas de changement
    } else {
      ec.revert();
    }
    
    // log si tout est ok
    if (transactionOK) {
      StringBuffer logBuffer = new StringBuffer();
      logBuffer.append(planning.affectationAnnuelle().individu().nomComplet()).append(" : nouvelle occupation : du ").
        append(DateCtrlConges.dateToString(dateDebut, "%d/%m/%Y %H:%M")).append(" au ").
        append(DateCtrlConges.dateToString(dateFin,   "%d/%m/%Y %H:%M")).
        append(" (").append(recTypeOcc.libelleCourt()).append(") plannings :");
      for (int i = 0; i < plannings.count(); i++) {
        EOAffectationAnnuelle affAnn = ((Planning) plannings.objectAtIndex(i)).affectationAnnuelle();
        logBuffer.append(" ").append(affAnn.annee()).append(" : ").append(affAnn.structure().libelleCourt());
        if (i < plannings.count() -1 )
          logBuffer.append(",");
      }
      
      // on ne trace que les opérations sur le planning réel
      if (planning.isPReel()) {
        LRLog.log(logBuffer.toString() + logSuffixDelegation());
      }

    }
    return plannings;
  }

  /**
   * Pour les lignes de log, on affiche le nom du delegue qui
   * a fait la demande si besoin
   * @return
   */
  private String logSuffixDelegation() {
  	String result = "";
  	if (!cngSession().isIndividuConnecte()) {
  		return " (" + cngSession().individuConnecte().nomComplet() + " par delegation)";
  	}
  	return result;
  }
}
