
package fr.univlr.cri.conges.databus;

import java.util.Enumeration;

import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.Application;
import fr.univlr.cri.conges.constantes.ConstsAlerte;
import fr.univlr.cri.conges.constantes.ConstsApplication;
import fr.univlr.cri.conges.constantes.ConstsDroit;
import fr.univlr.cri.conges.constantes.ConstsPlanning;
import fr.univlr.cri.conges.eos.modele.conges.EOAlerte;
import fr.univlr.cri.conges.eos.modele.conges.EODroit;
import fr.univlr.cri.conges.eos.modele.conges.EOStructureAutorisee;
import fr.univlr.cri.conges.eos.modele.grhum.EOAffectation;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOCalculAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOHoraire;
import fr.univlr.cri.conges.eos.modele.planning.EOOccupation;
import fr.univlr.cri.conges.eos.modele.planning.EOPeriodeAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOPlanningHebdomadaire;
import fr.univlr.cri.conges.eos.modele.planning.EOVIndividuConges;
import fr.univlr.cri.conges.eos.modele.planning.I_ClasseMetierNotificationParametre;
import fr.univlr.cri.conges.objects.Parametre;
import fr.univlr.cri.conges.objects.Planning;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.ArrayCtrl;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.util.wo5.DateCtrl;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRLog;
import fr.univlr.cri.ycrifwk.utils.UtilDb;
import fr.univlr.cri.ycrifwk.utils.UtilException;

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
 * Gestionnaire des informations sur le planning de l'agent dans la base de donnees de l'application Conges.
 * 
 * @author Cyril TARADE <cyril.tarade at univ-lr.fr>
 */
public class CngPlanningBus 
	extends CngDataBus
		implements ConstsPlanning, I_ClasseMetierNotificationParametre {

  public CngPlanningBus(EOEditingContext ec) {
    super(ec);
  }
  
  // niveau de validation des planning
  private static String validationPlanningNiveau;
  
  /**
   * @see I_ClasseMetierNotificationParametre
   * @param parametre
   */
  public static void initStaticField(
  		Parametre parametre) {
  	if (parametre == Parametre.PARAM_VALIDATION_PLANNING_NIVEAU) {
  		validationPlanningNiveau = parametre.getParamValueString();
  	} 
  }
  
  /**
   * Modifier les dates de d�but et/ou de fin d'affectation
   * 
   * @param oidAffectation :
   *          la cle primaire de l'affectation modifiee
   * 
   * @param prevDDebAffectation :
   *          l'ancienne date de debut d'affectation
   * 
   * @param dDebAffectation :
   *          la nouvelle date de debut d'affectation
   * 
   * @param prevDFinAffectation :
   *          l'ancienne date de fin d'affectation
   * 
   * @param dFinAffectation :
   *          la nouvelle date de fin d'affectation
   * 
   * @return true si l'operation est un succes. <code>errorMessage</code> est mis a jour si une erreur est survenue.
   */
  public boolean changeDatesAffectation(Number oidAffectation, NSTimestamp prevDDebAffectation, NSTimestamp dDebAffectation, 
      NSTimestamp prevDFinAffectation, NSTimestamp dFinAffectation) {
    boolean success = false;
    setErrorMessage("");
    Integer transId = null;
    // message d'information pour les logs
    StringBuffer sbInfo = new StringBuffer();
    sbInfo.append("changeDatesAffectation() - oidAffectation=").append(oidAffectation);
    sbInfo.append(", prevDDebAffectation=");
    sbInfo.append(prevDDebAffectation != null ? DateCtrlConges.dateToString(prevDDebAffectation) : "null");
    sbInfo.append(", dDebAffectation=");
    sbInfo.append(dDebAffectation != null ? DateCtrlConges.dateToString(dDebAffectation) : "null");
    sbInfo.append(", prevDFinAffectation=");
    sbInfo.append(prevDFinAffectation != null ? DateCtrlConges.dateToString(prevDFinAffectation) : "null");
    sbInfo.append(", dFinAffectation=");
    sbInfo.append(dFinAffectation != null ? DateCtrlConges.dateToString(dFinAffectation) : "null");
    try {
      if (oidAffectation != null) {
        if (dDebAffectation != null) {
          transId = beginECTransaction();
          EOEditingContext ec = econtextForTransaction(transId);

          EOAffectation lAffectationGepeto = EOAffectation.findAffectationGepetoForOidInContext(oidAffectation,ec);
          if (lAffectationGepeto != null) {

            // le commit n'a pas encore �t� fait par gepeto
            // NSTimestamp prevDDebAffectation = lAffectationGepeto.dDebAffectation();
            // NSTimestamp prevDFinAffectation = lAffectationGepeto.dFinAffectation();

            if (structureBus().isStructureAutoriseForDate(lAffectationGepeto.structure(), DateCtrlConges.now())) {
              NSArray affectationsAnnuelles = EOAffectationAnnuelle.findAffectationsAnnuellesForAffectationInContext(ec, lAffectationGepeto);

              // determiner si c'est un changement de date deb ou fin
              boolean isChangementDeb = !DateCtrlConges.isSameDay(dDebAffectation, prevDDebAffectation);
              boolean isAgrandissement = false; // l'affectation a-t-elle ete agrandie

              // on applique les changements uniquement pour les affectations annuelles concernees
              // determiner les parametres du filtre
              NSArray args = null;
              if (isChangementDeb) {
                isAgrandissement = DateCtrlConges.isBefore(dDebAffectation, prevDDebAffectation);
                if (isAgrandissement) {
                  args = new NSArray(new NSTimestamp[] { DateCtrlConges.dateToDebutAnneeUniv(dDebAffectation),
                      DateCtrlConges.dateToFinAnneeUniv(prevDDebAffectation) });
                } else {
                  args = new NSArray(new NSTimestamp[] { DateCtrlConges.dateToDebutAnneeUniv(prevDDebAffectation),
                      DateCtrlConges.dateToFinAnneeUniv(dDebAffectation) });
                }
              } else {
                isAgrandissement = (dFinAffectation == null && prevDFinAffectation != null) ||
                  (dFinAffectation != null && prevDFinAffectation != null && DateCtrlConges.isAfter(dFinAffectation, prevDFinAffectation));
                if (isAgrandissement) {
                  if (dFinAffectation == null) {
                    args = new NSArray(new NSTimestamp[] { DateCtrlConges.dateToDebutAnneeUniv(prevDFinAffectation),
                        DateCtrlConges.dateToFinAnneeUniv(prevDFinAffectation) });
                  } else {
                    args = new NSArray(new NSTimestamp[] { DateCtrlConges.dateToDebutAnneeUniv(prevDFinAffectation),
                        DateCtrlConges.dateToFinAnneeUniv(dFinAffectation) });
                  }
                } else {
                  if (prevDFinAffectation == null) {
                    NSTimestamp finAnneeUnivNewAff = DateCtrlConges.dateToFinAnneeUniv(dFinAffectation);
                    // cas d'une reduction sur l'annee universitaire precedente : impacter
                    // les affectations annuelles actuelles
                    if (DateCtrlConges.isBefore(finAnneeUnivNewAff, DateCtrlConges.dateToDebutAnneeUniv(DateCtrlConges.now()))) {
                      args = new NSArray(new NSTimestamp[] { DateCtrlConges.dateToDebutAnneeUniv(dFinAffectation),
                          DateCtrlConges.dateToFinAnneeUniv(TimeCtrl.dateToMinuit(DateCtrlConges.now())) });
                    } else {
                      args = new NSArray(new NSTimestamp[] { DateCtrlConges.dateToDebutAnneeUniv(dFinAffectation),
                          DateCtrlConges.dateToFinAnneeUniv(dFinAffectation) });
                    }
                  } else {
                    args = new NSArray(new NSTimestamp[] { DateCtrlConges.dateToDebutAnneeUniv(dFinAffectation),
                        DateCtrlConges.dateToFinAnneeUniv(prevDFinAffectation) });
                  }
                }
              }

              // liste de toutes les affectations annuelles existantes affectees par le changement de dates
              
              // pour fetchage, les AffectationAnnuelles finissent a 23h59, on recale donc la date de fin la dessus
              args = new NSArray(new NSTimestamp[]{
                  (NSTimestamp) args.objectAtIndex(0),
                  ((NSTimestamp) args.lastObject()).timestampByAddingGregorianUnits(0,0,0,23,59,59)});
              
              EOQualifier qual = EOQualifier.qualifierWithQualifierFormat("dateDebutAnnee >= %@ AND dateFinAnnee <= %@", args);
              NSArray affectationsAnnuellesFiltrees = EOQualifier.filteredArrayWithQualifier(affectationsAnnuelles, qual);

              for (int i = 0; i < affectationsAnnuellesFiltrees.count(); i++) {
                EOAffectationAnnuelle uneAffectationAnnuelle = (EOAffectationAnnuelle) affectationsAnnuellesFiltrees.objectAtIndex(i);

                boolean devaliderPlanning = true;

                // on supprime l'affectation annuelle si celle ci n'est plus dans la nouvelle affectation gepeto
                boolean supprimerAffectation = false;
                if (isChangementDeb && !isAgrandissement && DateCtrlConges.isAfter(dDebAffectation, uneAffectationAnnuelle.dateFinAnnee())) {
                  supprimerAffectation = true;
                }

                if (!isChangementDeb && !isAgrandissement && DateCtrlConges.isBefore(dFinAffectation, uneAffectationAnnuelle.dateDebutAnnee())) {
                  supprimerAffectation = true;
                }

                if (supprimerAffectation) {
                  ec.deleteObject(uneAffectationAnnuelle);
                } else {
                  // pas de suppression : on met a jour l'affectation annuelle, la periode et les planning hebdo associes

                  // Sauvegarde temporaire de la periode pour l'affectation gepeto et des planning hebdos associes
                  qual = EOQualifier.qualifierWithQualifierFormat("affectationAnnuelle = %@ AND affectation = %@", new NSArray(new Object[] {
                      uneAffectationAnnuelle, lAffectationGepeto }));
                  EOPeriodeAffectationAnnuelle lAnciennePeriode = (EOPeriodeAffectationAnnuelle) EOQualifier.filteredArrayWithQualifier(
                      uneAffectationAnnuelle.periodes(), qual).lastObject();

                  // on la supprime de l'affectation annuelle
                  uneAffectationAnnuelle.removeObjectFromBothSidesOfRelationshipWithKey(lAnciennePeriode, "periodes");
                  ec.deleteObject(lAnciennePeriode);

                  // creation de la periode associee a la nouvelle affectation gepeto
                  EOPeriodeAffectationAnnuelle laNouvellePeriode = uneAffectationAnnuelle.addPeriode(lAffectationGepeto, dDebAffectation, dFinAffectation);

                  // mise a jour des planning hebdo en fonction de l'ancienne periode
                  NSArray lesNouveauxPlanningsHebdo = laNouvellePeriode.planningHebdomadaires();

                  // creation des plannings hebdomadaires 'R' (non fabriques par addPeriode())) - on se calle sur les plannings 'P'
                  qual = EOQualifier.qualifierWithQualifierFormat("flagNature = %@", new NSArray("P"));
                  NSArray planningsPrevisionnels = EOQualifier.filteredArrayWithQualifier(lesNouveauxPlanningsHebdo, qual);
                  for (int j = 0; j < planningsPrevisionnels.count(); j++) {
                    EOPlanningHebdomadaire unNouveauPlanningPrev = (EOPlanningHebdomadaire) planningsPrevisionnels.objectAtIndex(j);
                    EOPlanningHebdomadaire unNouveauPlanningReel = new EOPlanningHebdomadaire();
                    unNouveauPlanningReel.insertInEditingContext(ec);
                    unNouveauPlanningReel.setDateDebutSemaine(unNouveauPlanningPrev.dateDebutSemaine());
                    unNouveauPlanningReel.setFlagNature("R");
                    laNouvellePeriode.addObjectToBothSidesOfRelationshipWithKey(unNouveauPlanningReel, "planningHebdomadaires");
                  }

                  // recuperation des horaires associes
                  for (int j = 0; j < lesNouveauxPlanningsHebdo.count(); j++) {
                    EOPlanningHebdomadaire unNouveauPlanningHebdo = (EOPlanningHebdomadaire) lesNouveauxPlanningsHebdo.objectAtIndex(j);
                    // recherche de l'equivalent dans la sauvegarde
                    qual = EOQualifier.qualifierWithQualifierFormat("dateDebutSemaine = %@ AND flagNature = %@", new NSArray(new Object[] {
                        unNouveauPlanningHebdo.dateDebutSemaine(), unNouveauPlanningHebdo.flagNature() }));
                    NSArray resultats = EOQualifier.filteredArrayWithQualifier(lAnciennePeriode.planningHebdomadaires(), qual);
                    if (resultats.count() > 0) {
                      EOPlanningHebdomadaire lAncienPlanningHebdo = (EOPlanningHebdomadaire) resultats.lastObject();
                      unNouveauPlanningHebdo.setHoraireRelationship(lAncienPlanningHebdo.horaire());
                    }
                  }

                  // si la date de fin de la periode reste inchangee, on ne devalide pas
                  if (DateCtrlConges.isSameDay(lAnciennePeriode.dateFin(), laNouvellePeriode.dateFin())) {
                    devaliderPlanning = false;
                  }
                }

                // enfin, on d�valide le planning si necessaire
                if (devaliderPlanning) {
                  uneAffectationAnnuelle.setStatusPlanning(Planning.PLANNING_STATUT_INVALIDE);
                }
              }

              // faut-il creer une nouvelle affectation annuelle pour la annee universitaire actuelle
              // cas particulier et recurrent d'un aggrandissement de contrat sur la periode en cours
              // passage de dfin < debut_annee_univ_en_cours a dfin > debut_annee_univ_en_cours
              if (isAgrandissement) {
                NSTimestamp debutAnneeUniv = DateCtrlConges.dateDebutAnnee(DateCtrlConges.now());
                if (//DateCtrlConges.isBefore(lAffectationGepeto.dFinAffectation(), debutAnneeUniv)
                    DateCtrlConges.isBefore(prevDFinAffectation, debutAnneeUniv)
                    && (dFinAffectation == null || DateCtrlConges.isAfter(dFinAffectation, debutAnneeUniv))) {
                  // creation d'une affectation annuelle
                  EOAffectationAnnuelle nouvelleAffectationAnnuelle = EOAffectationAnnuelle.newEOAffectationAnnuelle(
                      ec, lAffectationGepeto, debutAnneeUniv,
                      dDebAffectation, dFinAffectation);
                  nouvelleAffectationAnnuelle.insertInEditingContext(ec);
                  
                  // nouvelleAffectationAnnuelle.addPeriode(lAffectationGepeto);
                }
              }

              success = true;
              
              // sauvegarde general
              commitECTrancsacition(transId);
            } else {
              success = true;
              sbInfo.append(" => Changement de dates d'une affectation pour une personne non concernee par Conges");
              LRLog.rawLog("Changement de dates d'une affectation pour une personne non concernee par Conges", 2);
            }
          } else {
            success = false;
            sbInfo.append(" => Enregistrement dans l'entite Affectation introuvable");
            setErrorMessage("Affectation introuvable pour oid="+oidAffectation);
          }
        } else {
          // Requete incomplete
          success = false;
          sbInfo.append(" => Requete incomplete: veuillez renseigner le champs 'dDebAffectation'");
          setErrorMessage("Requete incomplete: veuillez renseigner le champs 'dDebAffectation'");
        }
      } else {
        // Requete incomplete
        success = false;
        sbInfo.append(" => Requete incomplete: veuillez renseigner le champs 'oidAffectation'");
        setErrorMessage("Requete incomplete: veuillez renseigner le champs 'oidAffectation'");
      }
    } catch (Throwable e) {
      // exception envoyee directement a GEPETO
      if (transId != null)
        rollbackECTrancsacition(transId);
      success = false;
      setErrorMessage(UtilException.stackTraceToString(e, false));
      sbInfo.append(" => ").append(e.getMessage());
    }
    sbInfo.append(" success=").append(success);
    LRLog.log(sbInfo.toString());
    return success;
  }

  /**
   * Suppression affectation : on efface toutes les periodes 
   * associ�es si une affectation annuelle n'a plus de periode, 
   * alors on l'efface �galement
   * 
   * @param oidAffectation : La cle primaire de l'affectation supprimee
   */
  public boolean supprimeAffectation(Number oidAffectation) {
    boolean success = true;
    setErrorMessage("");
    // message d'information pour les logs
    StringBuffer sbInfo = new StringBuffer();
    sbInfo.append("supprimeAffectation() - oidAffectation=").append(oidAffectation);
    Integer transId = null;
    try {
      if (oidAffectation != null) {
        transId = beginECTransaction();
        NSArray periodes = findPeriodesAffectationsAnnuellesForOidAffectation(transId, oidAffectation);
        if (periodes.count() > 0) {
          StringBuffer txtErr = new StringBuffer();
          // on enleve toutes les periodes associees a l'affectation gepeto
          for (int i = 0; i < periodes.count(); i++) {
            EOPeriodeAffectationAnnuelle unePeriode = (EOPeriodeAffectationAnnuelle) periodes.objectAtIndex(i);
            EOAffectationAnnuelle affectationAnnuelle = unePeriode.affectationAnnuelle();
            affectationAnnuelle.removeObjectFromBothSidesOfRelationshipWithKey(unePeriode, "periodes");
            int nbRecords = deleteFromTable(transId, unePeriode);
            sbInfo.append(", delete Periode");
            if (nbRecords < 1) {
              txtErr.append("Erreur lors de la suppression de : ").append(unePeriode);
              sbInfo.append(" => ").append(txtErr.toString());
              success = false;
              rollbackECTrancsacition(transId);
              break;
            }
            // si plus de periode associe, on efface l'affectation annuelle
            if (affectationAnnuelle.periodes().count() == 0) {
              deleteFromTable(transId, affectationAnnuelle);
              sbInfo.append(", delete AffectationAnnuelle");
            }
          }
          commitECTrancsacition(transId);
        } else {
          // rien a effacer
          commitECTrancsacition(transId);
          sbInfo.append(" => Aucun enregistrement de l'entite Periode a effacer");
          success = true;
        }
      } else {
        commitECTrancsacition(transId);
        success = false;
        sbInfo.append(" => Requete incomplete: veuillez renseigner le champs 'oidAffectation'");
        setErrorMessage("Requete incomplete: veuillez renseigner le champs 'oidAffectation'");
      }
    } catch (Throwable e) {
      // exception envoyee directement a GEPETO
      if (transId != null)
        rollbackECTrancsacition(transId);
      success = false;
      setErrorMessage(UtilException.stackTraceToString(e, false));
      sbInfo.append(" => ").append(e.getMessage());
    }
    sbInfo.append(" success=").append(success);
    LRLog.log(sbInfo.toString());
    return success;
  }

  

  /**
   * Suppression affectation annuelle : on efface tous les 
   * enregistrement associes cote conges. Cette methode n'est
   * utilisee que dans la page d'administration des donnees.
   * 
   * @param recAffAnn : L'affectation annuelle a supprimer
   */
  public boolean supprimeAffAnn(EOAffectationAnnuelle recAffAnn) {
    boolean success = true;
    setErrorMessage("");
    EOEditingContext ec = recAffAnn.editingContext();
    try {
      if (recAffAnn != null) {
        ec.deleteObject(recAffAnn);
        UtilDb.save(ec, true);
        success = true;
      } else {
        success = false;
        setErrorMessage("Requete incomplete: veuillez renseigner le champs 'recAffAnn'");
      }
    } catch (Throwable e) {
      ec.undo();
      success = false;
      setErrorMessage(UtilException.stackTraceToString(e, false));
    }
    return success;
  }
  
  /**
   * Nouvelle affectation : on construit tous les planning
   * annuels n�cessaires pour couvrir sa periode.
   *
   * @param oidAffectation : la cle primaire de l'affectation
   */
  public boolean nouvelleAffectation(Number oidAffectation) {
    return nouvelleAffectation(beginECTransaction(), oidAffectation);
  }
  
  /**
   * Nouvelle affectation : on construit tous les planning
   * annuels n�cessaires pour couvrir sa periode.
   *
   * @param oidAffectation : la cle primaire de l'affectation
   * 
   * @param transId : L'identifiant de la transaction en cour
   *  (cette methode peut etre appelee alors qu'une transaction
   *  est deja en cours)
   * 
   * @return
   */
  private boolean nouvelleAffectation(Integer transId, Number oidAffectation) {
    boolean success = false;
    setErrorMessage("");
    // message d'information pour les logs
    StringBuffer sbInfo = new StringBuffer("");
    sbInfo.append("nouvelleAffectation() - oidAffectation=").append(oidAffectation);
    try {
      EOEditingContext ec = econtextForTransaction(transId);
      if (oidAffectation != null) {
        EOAffectation affectationGepeto = EOAffectation.findAffectationGepetoForOidInContext(oidAffectation, ec);

      	sbInfo.append(", dDebAffectation=");
      	sbInfo.append(affectationGepeto.dDebAffectation() != null ? DateCtrlConges.dateToString(affectationGepeto.dDebAffectation()) : "null");
      	sbInfo.append(", dFinAffectation=");
      	sbInfo.append(affectationGepeto.dFinAffectation() != null ? DateCtrlConges.dateToString(affectationGepeto.dFinAffectation()) : "null");
      	
        if (affectationGepeto != null) {
        	NSTimestamp dDebutAnneeUnivNow = DateCtrlConges.dateToDebutAnneeUniv(DateCtrlConges.now());
        	NSTimestamp dFinAnneeUnivNow = DateCtrlConges.dateToFinAnneeUniv(DateCtrlConges.now());
        	sbInfo.append(", dDebutAnneeUnivNow=");
        	sbInfo.append(dDebutAnneeUnivNow != null ? DateCtrlConges.dateToString(dDebutAnneeUnivNow) : "null");
        	sbInfo.append(", dFinAnneeUnivNow=");
        	sbInfo.append(dFinAnneeUnivNow != null ? DateCtrlConges.dateToString(dFinAnneeUnivNow) : "null");
        	
          //  on ignore les demandes pour les agents hors V_PERSONNEL_NON_ENS
        	if (individuBus().isIndividuNonEnsWhilePeriode(affectationGepeto.individu(), dDebutAnneeUnivNow, dFinAnneeUnivNow)) {
            
            if (structureBus().isStructureAutoriseForDate(affectationGepeto.structure(), DateCtrlConges.now())) {

              // liste de toutes les periodes qui doivent exister pour cette affectation
              NSArray datesPeriode = DateCtrlConges.lesPeriodesAnneeUniv(DateCtrlConges.now(), affectationGepeto.dDebAffectation(), affectationGepeto.dFinAffectation());

              // liste de toutes les affectations annuelles conges qu'il possede sur ce service
              NSMutableDictionary bindingsAffAnn = new NSMutableDictionary();
              bindingsAffAnn.setObjectForKey(affectationGepeto.individu(), "individu");
              bindingsAffAnn.setObjectForKey(affectationGepeto.structure(), "service");
              NSArray lesAffectationsAnnuelles = EOAffectationAnnuelle.findAffectationsAnnuellesInContext(bindingsAffAnn, ec);

             
              
              // synchronisation de ce qu'on doit avoir et de ce qu'on a
              for (int i = 0; i < datesPeriode.count(); i += 2) {
                NSTimestamp debutPeriode = (NSTimestamp) datesPeriode.objectAtIndex(i);
                NSTimestamp finPeriode = (NSTimestamp) datesPeriode.objectAtIndex(i + 1);

                boolean creerNouvelleAffectationAnnuelle = true;
                boolean creerNouvellePeriode = true;

                EOAffectationAnnuelle lAffectationAnnuelle = null;

                // recherche d'une affectation annuelle associee a la periode en cours
                if (lesAffectationsAnnuelles != null && lesAffectationsAnnuelles.count() > 0) {
                  EOQualifier qual = EOQualifier.qualifierWithQualifierFormat("annee = %@", new NSArray(DateCtrlConges.anneeUnivForDate(debutPeriode)));
                  NSArray lesAffectationAnnuellesFiltrees = EOQualifier.filteredArrayWithQualifier(lesAffectationsAnnuelles, qual);

                  if (lesAffectationAnnuellesFiltrees.count() > 0) {
                    creerNouvelleAffectationAnnuelle = false;
                    lAffectationAnnuelle = (EOAffectationAnnuelle) lesAffectationAnnuellesFiltrees.lastObject();

                    // on cherche une periode correspondante
                    NSArray periodes = lAffectationAnnuelle.periodes();
                    for (int j = 0; j < periodes.count(); j++) {
                      EOPeriodeAffectationAnnuelle unePeriode = (EOPeriodeAffectationAnnuelle) periodes.objectAtIndex(j);
                      if (DateCtrlConges.isSameDay(unePeriode.dateDebut(), debutPeriode) && DateCtrlConges.isSameDay(unePeriode.dateFin(), finPeriode)) {
                        creerNouvellePeriode = false;
                      }
                    }
                  }
                }

                if (creerNouvelleAffectationAnnuelle) {
                	lAffectationAnnuelle = EOAffectationAnnuelle.newEOAffectationAnnuelle(ec, affectationGepeto, debutPeriode);
                  if (lAffectationAnnuelle != null) {
                  	lAffectationAnnuelle.insertInEditingContext(ec);
                  }
                  sbInfo.append(", new EOAffectationAnnuelle()");
                  creerNouvellePeriode = false;
                }

                if (creerNouvellePeriode) {
                	if (lAffectationAnnuelle != null) {
                		lAffectationAnnuelle.addPeriode(affectationGepeto);
                	}
                  sbInfo.append(", new EOPeriodeAffectationAnnuelle()");
                }

                // on devalide le planning
                if (lAffectationAnnuelle != null) {
                	lAffectationAnnuelle.setStatusPlanning(Planning.PLANNING_STATUT_INVALIDE);
                }

              }
              
              // sauvegarde generale
              commitECTrancsacition(transId);
              
              success = true;
              // TODO Envoi d'un mail pour avertir l'individu de la modif
            } else {
              commitECTrancsacition(transId);
              success = true;
              sbInfo.append(" => Nouvelle affectation pour un service non concernee par Conges (structure non autorisee) cStructure=").append(affectationGepeto.structure().cStructure());
            }
          } else {
            commitECTrancsacition(transId);
            success = true;
            String strInfo = 
            	" => Nouvelle affectation pour une personne non concernee par Conges (pas dans l'intersection des entites VPersonnelNonEns et VPersonnelActuel sur la periode " + 
            	DateCtrlConges.dateToString(dDebutAnneeUnivNow) + "-" + DateCtrlConges.dateToString(dFinAnneeUnivNow);
            sbInfo.append(strInfo);
          }
        } else {
          rollbackECTrancsacition(transId);
          success = false;
          String strInfo = "Affectation introuvable pour oid=" + oidAffectation;
          sbInfo.append(strInfo);
          setErrorMessage(strInfo);
        }
      } else {
        // Requete incomplete
        commitECTrancsacition(transId);
        success = false;
        String strInfo = "Requete incomplete: veuillez renseigner le champs 'oidAffectation'";
        sbInfo.append(strInfo);
        setErrorMessage(strInfo);
      }
    } catch (Throwable e) {
      // exception envoyee directement a GEPETO
      rollbackECTrancsacition(transId);
      success = false;
      setErrorMessage(UtilException.stackTraceToString(e, false));
      e.printStackTrace();
      sbInfo.append(" => ").append(e.getMessage());
    }
    LRLog.rawLog("returning : " + success, 2);
    sbInfo.append(" success=").append(success);
    LRLog.log(sbInfo.toString());
    return success;
  }
  
  
  /**
   * Changement de la quotite : Tout doit etre supprime,
   * on considere qu'il s'agit d'un remplacement total :
   * 1. on efface l'affectation
   * 2. on la recree
   */
  public boolean changeQuotiteAffectation(Number oidAffectation) {
    boolean success = false;
    setErrorMessage("");
    // message d'information pour les logs
    StringBuffer sbInfo = new StringBuffer();
    sbInfo.append("changeQuotiteAffectation() - oidAffectation=").append(oidAffectation);
    try {
      if (oidAffectation != null) {
        EOAffectation affectation = EOAffectation.findAffectationGepetoForOidInContext(
            oidAffectation, editingContext());
        if (affectation != null) {
         if (structureBus().isStructureAutoriseForDate(affectation.structure(), DateCtrlConges.now())) {
           success = supprimeAffectation(oidAffectation) && nouvelleAffectation(oidAffectation);
          } else {
            success = true;
            String strInfo = "Changement de quotite d'une affectation pour une personne non concernee par Conges";
            LRLog.trace(strInfo);
            sbInfo.append(" => ").append(strInfo);
          }
        } else {
          success = false;
          String strInfo = "Affectation introuvable pour oid="+oidAffectation;
          LRLog.trace(strInfo);
          sbInfo.append(" => ").append(strInfo);
          setErrorMessage(strInfo);
        }       
      } else {
        // Requete incomplete
        success = false;
        String strInfo = "Requete incomplete: veuillez renseigner le champs 'oidAffectation'";
        LRLog.trace(strInfo);
        sbInfo.append(" => ").append(strInfo);
      }
    } catch (Throwable e) {
      // exception envoyee directement a GEPETO
      success = false;
      setErrorMessage(UtilException.stackTraceToString(e, false));
      e.printStackTrace();
      sbInfo.append(" => ").append(e.getMessage());
    }
    sbInfo.append(" success=").append(success);
    LRLog.log(sbInfo.toString());
    return success;
  }
  

  /**
   * Liste des enregistrements d'affectation anuelle attaches a une Affectation Gepeto. On recupere
   * 
   * @param transId : L'identifiant de la transaction
   *    
   * @param oidAffectation
   *          La cle primaire de l'affectation
   */
  public NSArray findPeriodesAffectationsAnnuellesForOidAffectation(Integer transId, Number oidAffectation) {
    return fetchArray(transId, "PeriodeAffectationAnnuelle", 
        newCondition("oidAffectation = %@", new NSArray(oidAffectation)), null);
  }
  

  /**
   * Effacer un objet d'une table
   * 
   * @param transId : L'identifiant de la transaction
   * 
   * @param record : L'objet a effacer
   * 
   * @return Le nombre d'enregistrement supprimes. -1 si erreur.
   */
  private int deleteFromTable(Integer transId, EOGenericRecord record) {
    int result = 0;
    String tableName = null;
    if (record instanceof EOPeriodeAffectationAnnuelle) {
      tableName = "PeriodeAffectationAnnuelle";
    } else if (record instanceof EOAffectationAnnuelle) {
      tableName = "AffectationAnnuelle";
    }
    if (!StringCtrl.isEmpty(tableName)) {
      result = deleteFromTable(transId, tableName, 
          newCondition("oid = %@", new NSArray(primaryKey(record))));
    }
    return result;
  }

  /**
   * Retourner la cle primaire d'un objet. Dans les modeles du planning, chaque table a une seule et unique cle : oid.
   * 
   * @param record
   * @return
   */
  private Number primaryKey(EOGenericRecord record) {
    return (Integer) EOUtilities.primaryKeyForObject(record.editingContext(), record).valueForKey("oid");
  }
  
  /**
   * Recopier un planning dans un autre. Utilisé lors de la validation 
   * du planning previonnel et lors de la sauvegarde pendant le changement
   * du planning reel
   * @param affAnn  : l'affectation anuelle concernee
   * @param typeSource : le type de planning de depart (R,T,S,P)
   * @param typeDest : le type de planning d'arrivee (R,T,S,P)
   * @param duplicateAbsences : faut il oui ou non recopier aussi les absences
   * @return
   */
  public void dupliquerPlanning(
  		EOAffectationAnnuelle affAnn, String typeSource, String typeDest, boolean duplicateAbsences) {

  	// pour la recopie prévisionnel vers réel, on ne copie que 
  	// les changements, en se basant sur la date de modification
  	boolean isCopiePrevReel = false;
  	if (typeSource.equals(Planning.PREV) &&
  			typeDest.equals(Planning.REEL)) {
  		isCopiePrevReel = true;
  	}
  			
  	NSArray periodes = affAnn.periodes();
    Enumeration enumPeriodes = periodes.objectEnumerator();
    
    while (enumPeriodes.hasMoreElements()) {
      EOPeriodeAffectationAnnuelle unePeriode = (EOPeriodeAffectationAnnuelle) enumPeriodes.nextElement();
      NSArray planningsHebdo = unePeriode.planningHebdomadaires();

      EOQualifier qualDest = EOQualifier.qualifierWithQualifierFormat(
      		EOPlanningHebdomadaire.FLAG_NATURE_KEY + "=%@", new NSArray(typeDest));
      NSArray planningsHebdoDest = EOQualifier.filteredArrayWithQualifier(planningsHebdo, qualDest);
      EOQualifier qualSource = EOQualifier.qualifierWithQualifierFormat(
      		EOPlanningHebdomadaire.FLAG_NATURE_KEY + "=%@",  new NSArray(typeSource));
      
      NSArray planningsHebdoSource = EOQualifier.filteredArrayWithQualifier(planningsHebdo, qualSource);
      for (int i = 0; i < planningsHebdoSource.count(); i++) {
        
      	EOPlanningHebdomadaire unPlanningHebdoSource = (EOPlanningHebdomadaire) planningsHebdoSource.objectAtIndex(i);
        EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(
        		EOPlanningHebdomadaire.DATE_DEBUT_SEMAINE_KEY + "=%@", 
            new NSArray(unPlanningHebdoSource.dateDebutSemaine()));
        NSArray array = EOQualifier.filteredArrayWithQualifier(planningsHebdoDest, qual);

        
        // recherche de la présence du planning dest correspondant
        boolean isPlanningHebdoExistant = false;
        EOPlanningHebdomadaire lePlanningHebdoDest = null;
        if (array.count() == 0) {
          // il existe pas on le cree
          lePlanningHebdoDest = new EOPlanningHebdomadaire();
          lePlanningHebdoDest.setDateDebutSemaine(unPlanningHebdoSource.dateDebutSemaine());
          lePlanningHebdoDest.setFlagNature(typeDest);
          unePeriode.addToPlanningHebdomadairesRelationship(lePlanningHebdoDest);
          lePlanningHebdoDest.insertInEditingContext(affAnn.editingContext());
        } else {
          // il existe on recup
          lePlanningHebdoDest = (EOPlanningHebdomadaire) array.lastObject();
          isPlanningHebdoExistant = true;
        }
        
        boolean isRecopieAutorisee = true;
        // controle sur les dates de création des enregistrement
        // pour le cas particulier prev vers reel : on ne recopie
        // que ce qui est plus récent 
        if (isPlanningHebdoExistant && isCopiePrevReel) {
        	if (DateCtrl.isBefore(
        			unPlanningHebdoSource.dModification(), lePlanningHebdoDest.dModification())) {
        		isRecopieAutorisee = false;
        	}
        }
        
        // recopie de l'horaire du source dans le dest
        if (isRecopieAutorisee) {
        	EOHoraire prevHoraire = lePlanningHebdoDest.horaire();
        	EOHoraire newHoraire = unPlanningHebdoSource.horaire();
        	
        	if (newHoraire != prevHoraire) {
            lePlanningHebdoDest.setHoraireRelationship(newHoraire);
            // addObjectToBothSidesOfRelationshipWithKey pour que la suppression
            // d'un horaire nouvellement affecte dans la meme session ne 
            // desaffecte pas tous les horaires
            lePlanningHebdoDest.addObjectToBothSidesOfRelationshipWithKey(
            		unPlanningHebdoSource.horaire(), 
            		EOPlanningHebdomadaire.HORAIRE_KEY);
            // maj date de modification
            lePlanningHebdoDest.setDModification(DateCtrlConges.now());
        	}
        }
      }
    }
    
    // calculs si besoin
    if (affAnn.calculAffAnn(typeDest) == null) {
    	EOCalculAffectationAnnuelle calculS = new EOCalculAffectationAnnuelle(typeDest);
    	calculS.insertInEditingContext(affAnn.editingContext());
    	//affAnn.addObjectToBothSidesOfRelationshipWithKey(calculS, "calculAffAnns");
    	affAnn.addToCalculAffAnnsRelationship(calculS);
    }
    
    // absences
    if (duplicateAbsences) {
    	// supprimer les absences de destination
    	NSArray absencesDst = EOQualifier.filteredArrayWithQualifier(
    			affAnn.occupations(), newCondition(EOOccupation.FLAG_NATURE_KEY + "='" + typeDest +"'"));
    	for (int i = 0; i < absencesDst.count(); i++)
    		affAnn.editingContext().deleteObject((EOOccupation) absencesDst.objectAtIndex(i));
    	// recopier les absences sources en modifiant la nature
    	NSArray absencesSrc = EOQualifier.filteredArrayWithQualifier(
    			affAnn.occupations(), newCondition(EOOccupation.FLAG_NATURE_KEY + "='" + typeSource +"'"));
    	for (int i = 0; i < absencesSrc.count(); i++) {
    		EOOccupation occSrc = (EOOccupation) absencesSrc.objectAtIndex(i);
       
    		EOOccupation occDst = new EOOccupation();
    		occDst.insertInEditingContext(affAnn.editingContext());
	       
    		occDst.setDateDebut(occSrc.dateDebut());
    		occDst.setDateFin(occSrc.dateFin());
    		occDst.setMotif(occSrc.motif());
    		occDst.setFlagNature(typeDest);
    		occDst.setStatus(occSrc.status());
    		occDst.setValeurMinutes(occSrc.valeurMinutes());
    		occDst.setDureeReelle(occSrc.dureeReelle());
    		//occDst.addObjectToBothSidesOfRelationshipWithKey(occSrc.typeOccupation(), "typeOccupation");
    		occDst.setTypeOccupationRelationship(occSrc.typeOccupation());
    		if (occSrc.toIndividuDemandeur() != null) {
    			//occDst.addObjectToBothSidesOfRelationshipWithKey(occSrc.toIndividuDemandeur(), "toIndividuDemandeur");
    			occDst.setToIndividuDemandeurRelationship(occSrc.toIndividuDemandeur());
    		}
    		//affAnn.addObjectToBothSidesOfRelationshipWithKey(occDst, "occupations");
    		affAnn.addToOccupationsRelationship(occDst);
    	}
    }
  }

  /**
   * Methode qui remet a zero le planning :
   * - effacer toutes les associations
   * - effacer toutes les alertes
   * - effacer toutes les occupations
   * @throws Throwable 
   */
  public void razPlanning(Planning planning) throws Throwable {
    EOEditingContext ec = planning.affectationAnnuelle().editingContext();
    
    // effacer les associations
    NSArray planningsHebdo = ArrayCtrl.applatirArray((NSArray) planning.affectationAnnuelle().valueForKeyPath("periodes.planningHebdomadaires"));
    for (int i = 0; i < planningsHebdo.count(); i++) {
      EOPlanningHebdomadaire plgHeb = (EOPlanningHebdomadaire) planningsHebdo.objectAtIndex(i);
      plgHeb.setHoraireRelationship(null);
    }
    
    // suppression des alertes 
    NSArray alertes = alerteBus().findAlertesForPlanning(planning);
    for (int i = 0; i < alertes.count(); i++) {
      ec.deleteObject((EOAlerte) alertes.objectAtIndex(i));
    }
    
    // effacer les occupations
    NSArray occupations = planning.affectationAnnuelle().occupations();
    for (int i = 0; i < occupations.count(); i++) {
      ec.deleteObject((EOOccupation) occupations.objectAtIndex(i));
    }
    
    // supprimer les historiques de visa et de validation
    planning.affectationAnnuelle().removeRepartVisa();      	
    planning.affectationAnnuelle().removeRepartValidation();
    
    UtilDb.save(ec, true);
  }
  

  /**
   * Autoriser une liste de service pour une annee universitaire.
   * Cette operation creer autant de plannings qu'il y a de personnes
   * dans la vue <code>V_PERSONNEL_NON_ENS</code> pendant la periode
   * couverte par l'annee universitaire selectionnee.
   * 
   * @param ec
   * @param lesServices
   * @param uneDate
   * @param shouldCreateRecordEOStructureAutorisee : indique si l'enregistrement
   *   doit etre cree. Ce parametre doit etre a <code>false</code> dans le cas
   *   d'une resynchronisation de contractuels.
   * 
   * @return un <code>NSArray</code> contenant :
   * 	1- le nombre d'affectations annuelles creees
   *  2- le nombre de periodes creees
   */
  public NSArray autoriserServicesPourDateInContext(
  		EOEditingContext ec, NSArray lesServices, NSTimestamp uneDate, boolean shouldCreateRecordEOStructureAutorisee) {

 
    StringBuffer messageLog = new StringBuffer();

 
    if (lesServices != null && lesServices.count() > 0) {
    	
      NSTimestamp leDebutAnnee = DateCtrlConges.dateToDebutAnneeUniv(uneDate);
      NSTimestamp laFinAnnee = DateCtrlConges.dateToFinAnneeUniv(uneDate);

      String strAnneeUniv = DateCtrlConges.anneeUnivForDate(uneDate);

      // liste des individus affectes sur l'annee universitaire
      NSArray individusVPersonnelNonEns = individuBus().fetchAllIndividuNonEnsForDateRefInContext(
      		ec, 
      		DateCtrlConges.dateToDebutAnneeUniv(uneDate),
      		DateCtrlConges.dateToFinAnneeUniv(uneDate));

    	
      Enumeration enumLesServices = lesServices.objectEnumerator();
      
      NSArray affectationsAnnuelles = new NSArray();

      while (enumLesServices.hasMoreElements()) {
        EOStructure unServiceAAutoriser = (EOStructure) enumLesServices.nextElement();
        NSArray lesAffectations = EOAffectation.findAffectationsGepetoInContext(ec, null, unServiceAAutoriser, leDebutAnnee, laFinAnnee);

        // on autorise que les gens non enseignants
        NSMutableArray lesAffectationNonEns = new NSMutableArray();
        for (int i = 0; i < lesAffectations.count(); i++) {
          EOAffectation uneAffectation = (EOAffectation) lesAffectations.objectAtIndex(i);
          if (individusVPersonnelNonEns.containsObject(uneAffectation.individu())) {
            lesAffectationNonEns.addObject(uneAffectation);
          }
        }
        lesAffectations = lesAffectationNonEns.immutableClone();

        LRLog.log(" > "+ (shouldCreateRecordEOStructureAutorisee ? "allowing" : "synchronizing")+ 
        		" on " + DateCtrlConges.anneeUnivForDate(leDebutAnnee)+" : " + unServiceAAutoriser.display());
        affectationsAnnuelles = EOAffectationAnnuelle.newEOAffectationsAnnuelles(ec, lesAffectations, leDebutAnnee);
        Enumeration enumAffectationsAnnuelles = affectationsAnnuelles.objectEnumerator();
        while (enumAffectationsAnnuelles.hasMoreElements()) {
          EOAffectationAnnuelle affAnn = null;
          affAnn = (EOAffectationAnnuelle) enumAffectationsAnnuelles.nextElement();
          affAnn.insertInEditingContext(ec);
          //totalAffAnns++;
          //totalPeriodes += affAnn.periodes().count();
          messageLog.append(affAnn.toString()).append("<br>");
          affAnn = null;
        }

        if (shouldCreateRecordEOStructureAutorisee) {
          EOStructureAutorisee aNewServiceAutorise = new EOStructureAutorisee();
          aNewServiceAutorise.setStructureRelationship(unServiceAAutoriser);
          aNewServiceAutorise.setAnnee(strAnneeUniv);
          ec.insertObject(aNewServiceAutorise);
        }
        
      }
      
    }

    int totalAffAnns = 0;
    int totalPeriodes = 0;
    NSArray insertedObjects = ec.insertedObjects();
    for (int i=0; i<insertedObjects.count(); i++) {
    	Object insertedObject = insertedObjects.objectAtIndex(i);
    	if (insertedObject instanceof EOAffectationAnnuelle) {
    		totalAffAnns++;
    	} else if (insertedObject instanceof EOPeriodeAffectationAnnuelle) {
    		totalPeriodes++;
    	} 
    }
    
    return new NSArray(
    		new Object[]{
    				new Integer(totalAffAnns), 
    				new Integer(totalPeriodes),
    				messageLog.toString()});
  }



  /**
   * Individus qui ont les droits de validation sur l'alerte.
   * 
   * @param shouldIgnoreCdsNoMail : indique s'il ne faut pas traiter les information de la table <code>DroitPasDeMailCds</code>.
	 * 																Si cette valeur est a <code>true</code>, alors on retourne toujours le chef de service,
	 * 																meme si ce dernier est parametre comme ne recevant pas les mails.
	 * 
	 * @return un tableau de <code>EOIndividu</code>.
   */
  public NSArray responsables(
  		CngDroitBus droitBus,
  		EOAffectationAnnuelle recAffAnn, 
  		EOOccupation recOcc, 
  		boolean shouldIgnoreCdsNoMail) throws Exception {
    try {
      NSArray responsables = null;
      EOOccupation occupation = recOcc;
      EOEditingContext edc = editingContext();

    	// conges typ�s DRH ou validation planning DRH => viseur = responsable
    	boolean validationDRH = (
    			(occupation == null && validationPlanningNiveau.equals(Parametre.VALIDATION_PLANNING_NIVEAU_DRH_VALUE)) || 
    			(occupation != null && occupation.isCongeDRH()));
      
    	if (validationDRH) {
    	  responsables = new NSArray();
    	  boolean shouldAddResponsableDRHGlobal = false;
    		// cas particulier ou seul le DRH global est habilit� a valider
    		if (occupation != null && occupation.isCongeDRHGlobal()) {
    			shouldAddResponsableDRHGlobal = true;
    		} else {
    			// on localise les DRH locaux a la composante
        	EOStructure composante = recAffAnn.structure().toComposante();
        	responsables = droitBus.findDRHForComposante(edc, composante);
    		}
        // si la liste est vide, on a trouve personne ou conges DRH global-> DRH Globaux
    		if (responsables.count() == 0 || shouldAddResponsableDRHGlobal) {
          EOStructure structureDrh = EOStructure.findStructureForCstructureInContext(edc, cStructureDrh());
          NSArray drh = (NSArray) structureDrh.valueForKey("individus");
          for (int i = 0; i < drh.count(); i++) {
          	EOGenericRecord unIndividuPersId = (EOGenericRecord) drh.objectAtIndex(i);
          	EOIndividu unDrh = EOIndividu.findIndividuForPersIdInContext(
          			edc, (Number) unIndividuPersId.valueForKey("persId"));
          	responsables = responsables.arrayByAddingObject(unDrh);
          }
    		}
    	} else {
    		// sinon c'est les valideurs "normaux"
    		responsables = droitBus.findResponsablePlanning(edc, recAffAnn, shouldIgnoreCdsNoMail);
    	}

    	return responsables;
    } catch (Exception e) {
    	e.printStackTrace();
      throw new Exception(ConstsAlerte.TXT_ERREUR_RESPONSABLE_HTML);
    }
  }


  
  /**
   * Individus qui ont les droits de visa sur la cible (recOcc doit �tre vide si c'est le planning,
   * non vide si c'est l'occupation) 
   * 
   * @param shouldIgnoreCdsNoMail : indique s'il ne faut pas traiter les information de la table <code>DroitPasDeMailCds</code>.
	 * 																Si cette valeur est a <code>true</code>, alors on retourne toujours le chef de service,
	 * 																meme si ce dernier est parametre comme ne recevant pas les mails.
	 * 
   * Retourne un tableau de <code>EOIndividu</code>.
   */
  public NSArray viseurs(
  		CngDroitBus droitBus,
  		EOAffectationAnnuelle recAffAnn, 
  		EOOccupation recOcc,
  		boolean shouldIgnoreCdsNoMail) {
    NSArray viseurs = null;
    EOStructure structure = recAffAnn.structure();
    EOIndividu chefDeService = structure.responsable();
    EOIndividu individu = recAffAnn.individu();
    EOOccupation occupation = recOcc;
    EOEditingContext edc = editingContext();

    
  	// conges typ�s DRH ou validation planning DRH => viseur = responsable
  	boolean validationDRH = (
  			(occupation == null && validationPlanningNiveau.equals(Parametre.VALIDATION_PLANNING_NIVEAU_DRH_VALUE)) || 
  			(occupation != null && occupation.isCongeDRH()));
    if (validationDRH) {
    	viseurs = droitBus.findResponsablePlanning(edc, recAffAnn, shouldIgnoreCdsNoMail);
    } else {
    	// conges normaux ou validation planning niveau responsable => viseur = viseurs normaux
      if (chefDeService != null && individu.persId().intValue() == chefDeService.persId().intValue()) {
        viseurs = droitBus.findDroitsForStructureAndIndividuForCdrTypeAndNiveau(
            edc, structure, null, ConstsDroit.DROIT_CIBLE_CHEF_DE_SERVICE, ConstsDroit.DROIT_NIVEAU_VISA);
        viseurs = (NSArray) viseurs.valueForKey(EODroit.TO_INDIVIDU_RESP_KEY);
      } else {
        viseurs = droitBus.findDroitsForStructureAndIndividuForCdrTypeAndNiveau(
            edc, structure, null, ConstsDroit.DROIT_CIBLE_SERVICE, ConstsDroit.DROIT_NIVEAU_VISA);
        viseurs = (NSArray) viseurs.valueForKey(EODroit.TO_INDIVIDU_RESP_KEY);
      }
      // ajout de toute personne qui a la visa sur l'individu
      NSArray viseursIndividuels = (NSArray) droitBus.findDroitsForStructureAndIndividuForCdrTypeAndNiveau(
          edc, structure, individu, ConstsDroit.DROIT_CIBLE_INDIVIDU, ConstsDroit.DROIT_NIVEAU_VISA).valueForKey(EODroit.TO_INDIVIDU_RESP_KEY);
      viseurs = viseurs.arrayByAddingObjectsFromArray(viseursIndividuels);
    }
    
    return viseurs;
  }
  
  // code de la structure du groupe DRH
  private static String cStructureDrh = null;
  
  private static String cStructureDrh() {
  	if (cStructureDrh == null) {
  		cStructureDrh = ((Application)Application.application()).config().stringForKey(
  				ConstsApplication.CONFIG_C_STRUCTURE_DRH_KEY);
  	}
  	return cStructureDrh;
  }
  
  /**
   * La liste des plannings a supprimer : ceux qui sont orphelins,
   * dont les dates des affectations associées ne sont pas cohérentes.
   * Par exemple, un planning 2009/2010 ne peut être associé à une
   * affectation sur 2003/2004
   * @return
   */
  public NSArray getPlanningASupprimer(EOEditingContext ec) {
  	NSArray records = null;
  	
  	EOQualifier qual = CRIDataBus.newCondition(
  			EOVIndividuConges.D_DEB_AFFECTATION_KEY + ">" + EOVIndividuConges.DTE_FIN_ANNEE_KEY + " or (" +
  			EOVIndividuConges.D_FIN_AFFECTATION_KEY + "<> nil and " +
  			EOVIndividuConges.D_FIN_AFFECTATION_KEY + "<" + EOVIndividuConges.DTE_DEBUT_ANNEE_KEY + ")");

  	records = (NSArray) EOVIndividuConges.fetchVIndividuCongeses(
  			ec, qual, null).valueForKey(EOVIndividuConges.TO_AFFECTATION_ANNUELLE_KEY);
  	
  	return records;
  }
}
