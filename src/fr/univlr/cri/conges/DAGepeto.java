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
import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSNumberFormatter;
import com.webobjects.foundation.NSTimestamp;
import com.webobjects.foundation.NSTimestampFormatter;

import fr.univlr.cri.conges.constantes.ConstsApplication;
import fr.univlr.cri.conges.eos.modele.grhum.EOAffectation;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.ArrayCtrl;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.util.wo5.DateCtrl;
import fr.univlr.cri.webapp.LRDataResponse;

/**
 * Classe dediees au DirectAction utilises par 
 * l'application de gestion de personnel.
 * @author Cyril TARADE <cyril.tarade at univ-lr.fr>
 */

public class DAGepeto extends DirectAction {

  public DAGepeto(WORequest request) {
    super(request);
  }
  
  private int extNotifCode() {
  	return ((Number)app().valueForKey("extNotifCode")).intValue();
  }

  /**
   * nouvelle affectation
   */
  public WOActionResults nouvelleAffectationAction() {
    WOResponse resultat = new WOResponse();
    if (extNotifCode() == ConstsApplication.EXT_NOTIF_CODE_DIRECT_ACTION) {
      Number oidAffectation = context().request().numericFormValueForKey("oidAffectation",new NSNumberFormatter("#"));
      boolean success = cngSession().cngDataCenter().
        planningBus().nouvelleAffectation(oidAffectation);
      resultat.setHeader(success ? "1" : "0", "statut");
      String message = cngSession().cngDataCenter().planningBus().getErrorMessage();
      cngSession().terminate();
      if (!StringCtrl.isEmpty(message)) 
        resultat.setHeader(message, "message");
    } else {
      resultat.setHeader("1", "statut");
    }
    return resultat;
  }

  /**
   * changement de la date de d�but ou de la date 
   * de fin de l'affectation (gepeto)
   */
  public WOActionResults changeDatesAffectationAction() {
    WOResponse resultat = new WOResponse();
    if (extNotifCode() == ConstsApplication.EXT_NOTIF_CODE_DIRECT_ACTION) {
      WORequest request = request();
      Number oidAffectation = request.numericFormValueForKey("oidAffectation",new NSNumberFormatter("#"));
      NSTimestampFormatter dateFormatter = new NSTimestampFormatter("%d/%m/%Y");
      NSTimestamp dDebAffectation = request.dateFormValueForKey("dDebAffectation",dateFormatter);
      NSTimestamp dFinAffectation = request.dateFormValueForKey("dFinAffectation",dateFormatter);
      // recuperation des anciennes dates d'affectation (gepeto n'a pas fait le commit)
      EOAffectation lAffectationGepeto = EOAffectation.
        findAffectationGepetoForOidInContext(oidAffectation, 
            cngSession().cngDataCenter().planningBus().editingContext());
      if (lAffectationGepeto != null) {
        // le commit n'a pas encore �t� fait par gepeto
        NSTimestamp prevDDebAffectation = lAffectationGepeto.dDebAffectation();
        NSTimestamp prevDFinAffectation = lAffectationGepeto.dFinAffectation();
        boolean success = cngSession().cngDataCenter().
        planningBus().changeDatesAffectation(oidAffectation, 
            prevDDebAffectation, dDebAffectation, prevDFinAffectation, dFinAffectation);
        resultat.setHeader(success ? "1" : "0", "statut");
        String message = cngSession().cngDataCenter().planningBus().getErrorMessage();
        if (!StringCtrl.isEmpty(message)) {
          resultat.setHeader(message, "message");
        }
      } else {
        resultat.setHeader("0","statut");
        resultat.setHeader("Affectation introuvable pour oid="+oidAffectation, "message");
      }
      cngSession().terminate();      
    } else {
      resultat.setHeader("1", "statut");
    }
    return resultat;
  }

  /**
   * changement de la quotite : on efface tous les
   * plannings associ�s � l'affectation modifi�e
   */
  public WOActionResults changeQuotiteAffectationAction() {
    WOResponse resultat = new WOResponse();
    if (extNotifCode() == ConstsApplication.EXT_NOTIF_CODE_DIRECT_ACTION) {
      Number oidAffectation = context().request().numericFormValueForKey("oidAffectation",new NSNumberFormatter("#"));
      boolean success = cngSession().cngDataCenter().
        planningBus().changeQuotiteAffectation(oidAffectation);
      resultat.setHeader(success ? "1" : "0", "statut");
      String message = cngSession().cngDataCenter().planningBus().getErrorMessage();
      cngSession().terminate();
      if (!StringCtrl.isEmpty(message)) {
        resultat.setHeader(message, "message");
      }
    } else {
      resultat.setHeader("1", "statut");
    }
    return resultat;
  }

  /**
   * changement des dates d'un cong� l�gal suppression
   * de ce cong� puis ajout via la DA enregistreCongeLegalAction()
   */
  public WOActionResults changeCongeLegalAction() {
    WOResponse resultat = new WOResponse();
    resultat.setHeader("1", "statut");
    return resultat;
  }

  /**
   * suppression d'un cong� l�gal
   * 
   * @return
   */
  public WOActionResults supprimeCongeLegalAction() {
    WOResponse resultat = new WOResponse();
    resultat.setHeader("1", "statut");
    return resultat;
  }

  /**
   * suppression affectation : on efface toutes les periodes associ�es
   * si une affectation annuelle n'a plus de periode, alors on l'efface 
   * �galement
   */
  public WOActionResults supprimeAffectationAction() {
    WOResponse resultat = new WOResponse();
    if (extNotifCode() == ConstsApplication.EXT_NOTIF_CODE_DIRECT_ACTION) {
      Number oidAffectation = context().request().numericFormValueForKey("oidAffectation",new NSNumberFormatter("#"));
      boolean success = cngSession().cngDataCenter().
        planningBus().supprimeAffectation(oidAffectation);
      resultat.setHeader(success ? "1" : "0", "statut");
      String message = cngSession().cngDataCenter().planningBus().getErrorMessage();
      cngSession().terminate();
      if (!StringCtrl.isEmpty(message)) {
        resultat.setHeader(message, "message");
      }      
    } else {
      resultat.setHeader("1", "statut");
    }
    return resultat;
  }

  /**
   * 
   */
  public WOActionResults enregistreCongeLegalAction() {
    WOResponse resultat = new WOResponse();
    resultat.setHeader("1", "statut");
    return resultat;
  }

  /**
   * Cette DA ne doit plus etre utilisee, le changement de
   * structure n'a pas besoin de traitement.
   */
  public WOActionResults changeStructureAffectationAction() {
    WOResponse resultat = new WOResponse();
    resultat.setHeader("1", "statut");
    return resultat;
  }

  /**
   * Nombre de demi journ�es de travail entre 2 dates. Utilise
   * par GEPETO pour les conges legaux.
   */
  public WOActionResults presencesDemiJourneesPourPeriodeAction() {
    WORequest request = request();
    EOEditingContext edc = new EOEditingContext();
    Number noIndividu = request.numericFormValueForKey("noIndividu", new NSNumberFormatter("#"));
    NSTimestampFormatter dateFormatter = new NSTimestampFormatter("%d/%m/%Y %H:%M");
    NSTimestamp debut = request.dateFormValueForKey("debut", dateFormatter);
    NSTimestamp fin = request.dateFormValueForKey("fin", dateFormatter);
    StringBuffer buffer = new StringBuffer();
    if (noIndividu != null) {
      if (debut != null) {
        if (fin != null) {
          if (DateCtrl.isBeforeEq(debut, fin)) {
            NSMutableDictionary bindings = new NSMutableDictionary();
            bindings.setObjectForKey(noIndividu, "oid");
            EOIndividu individu = (EOIndividu) EOUtilities.objectWithFetchSpecificationAndBindings(edc, "Individu", "individuWithOID", bindings);
            if (individu != null) {

              NSArray lesAffectationsAnnuelles = new NSArray();

              NSTimestamp debutAnneeEnCours = DateCtrlConges.dateToDebutAnneeUniv(debut);

              bindings.setObjectForKey(individu, "individu");

              // doit-on verifier sur plusieurs ann�es ?
              while (DateCtrlConges.isBeforeEq(debutAnneeEnCours, DateCtrlConges.dateToDebutAnneeUniv(fin))) {

                bindings.setObjectForKey(debutAnneeEnCours, "dateDebut");
                bindings.setObjectForKey(debutAnneeEnCours.timestampByAddingGregorianUnits(1, 0, -1, 0, 0, 0), "dateFin");
                NSArray affectationsAnnuelles = EOAffectationAnnuelle.findAffectationsAnnuellesInContext(bindings, edc);

                /*
                 * 
                 * if (affectationsAnnuelles.count() > 0) { NSArray planningsPourAnnee = Planning.preparerPourAffectationsAnnuelles(
                 * EOAffectationAnnuelle.affectationsAnnuellesInContext(bindings, edc), debutAnneeEnCours, lesServicesAutorises ); lesPlannings =
                 * lesPlannings.arrayByAddingObjectsFromArray(planningsPourAnnee); }
                 */

                lesAffectationsAnnuelles = lesAffectationsAnnuelles.arrayByAddingObjectsFromArray(affectationsAnnuelles);

                debutAnneeEnCours = debutAnneeEnCours.timestampByAddingGregorianUnits(1, 0, 0, 0, 0, 0);
              }

              lesAffectationsAnnuelles = ArrayCtrl.removeDoublons(lesAffectationsAnnuelles);

              int nbDemiJournees = 0;

              // on additionne toutes les presences de tous les plannings
              for (int i = 0; i < lesAffectationsAnnuelles.count(); i++) {
                /*
                 * Planning unPlanning = (Planning)lesPlannings.objectAtIndex(i);
                 * 
                 * NSTimestamp dateDebut = debut; if (DateCtrlConges.isBefore(dateDebut, unPlanning.affectationAnnuelle().dateDebutAnnee())) { dateDebut =
                 * unPlanning.affectationAnnuelle().dateDebutAnnee(); } NSTimestamp dateFin = fin; if (DateCtrlConges.isAfter(dateFin,
                 * unPlanning.affectationAnnuelle().dateFinAnnee())) { dateFin = unPlanning.affectationAnnuelle().dateFinAnnee(); }
                 * 
                 * NSArray presenceHorsConges = unPlanning.affectationAnnuelle().presencePourPeriode(dateDebut, dateFin); String presenceString = ""; for (int
                 * j=0; j<presenceHorsConges.count(); j++) presenceString += presenceHorsConges.objectAtIndex(j); nbDemiJournees +=
                 * StringCtrl.replace(StringCtrl.replace(presenceString, "1", ""), "0", "").length();
                 */

                EOAffectationAnnuelle uneAffectationAnnuelle = (EOAffectationAnnuelle) lesAffectationsAnnuelles.objectAtIndex(i);

                NSTimestamp dateDebut = debut;
                if (DateCtrlConges.isBefore(dateDebut, uneAffectationAnnuelle.dateDebutAnnee())) {
                  dateDebut = uneAffectationAnnuelle.dateDebutAnnee();
                }
                NSTimestamp dateFin = fin;
                if (DateCtrlConges.isAfter(dateFin, uneAffectationAnnuelle.dateFinAnnee())) {
                  dateFin = uneAffectationAnnuelle.dateFinAnnee();
                }

                NSArray presenceHorsConges = uneAffectationAnnuelle.presencePourPeriode("R", dateDebut, dateFin);

                // on retaille la presence si les journ�e de d�but ou fin ne sont pas completes
                if (presenceHorsConges.count() > 0) {
                  if (!dateDebut.equals(TimeCtrl.dateToMinuit(dateDebut))) {
                    // TODO le replacement des pr�sences doit etre fait directement dans la methode presence()
                    String premElt = (String) presenceHorsConges.objectAtIndex(0);
                    premElt = "0" + premElt.substring(1, 2);
                    NSMutableArray presenceMutable = new NSMutableArray(presenceHorsConges);
                    presenceMutable.replaceObjectAtIndex(premElt, 0);
                    presenceHorsConges = presenceMutable.immutableClone();
                  }
                  if (!dateFin.equals(TimeCtrl.dateToMinuit(dateFin).timestampByAddingGregorianUnits(0, 0, 0, 12, 0, 0))) {
                    // TODO le replacement des pr�sences doit etre fait directement dans la methode presence()
                    String derElt = (String) presenceHorsConges.lastObject();
                    derElt = derElt.substring(0, 1) + "0";
                    NSMutableArray presenceMutable = new NSMutableArray(presenceHorsConges);
                    presenceMutable.replaceObjectAtIndex(derElt, presenceHorsConges.count() - 1);
                    presenceHorsConges = presenceMutable.immutableClone();
                  }
                }

                String presenceString = "";
                for (int j = 0; j < presenceHorsConges.count(); j++) {
                  presenceString += presenceHorsConges.objectAtIndex(j);
                }
                nbDemiJournees += StringCtrl.replace(presenceString, "0", "").length();
              }

              buffer.append("n = " + Integer.toString(nbDemiJournees) + "\n");
              buffer.append("statut = 1\n");

            } else {
              // erreurs de dates
              buffer.append("erreur = 'Date debut après la date de fin'\n");
              buffer.append("statut = 0\n");
            }
          } else {
            // individu non trouv�
            buffer.append("erreur = 'Individu non trouvé'\n");
            buffer.append("statut = 0\n");
          }
        } else {
          // fin absent
          buffer.append("erreur = 'Date de fin absente'\n");
          buffer.append("statut = 0\n");
        }
      } else {
        // debut absent
        buffer.append("erreur = 'Date de début absente'\n");
        buffer.append("statut = 0\n");
      }
    } else {
      // noIndividu absent
      buffer.append("erreur = 'noIndividu absent'\n");
      buffer.append("statut = 0\n");
    }

    LRDataResponse resultat = new LRDataResponse();
    resultat.setContent(buffer.toString());

    return resultat;
  }

}
