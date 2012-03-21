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
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.constantes.ConstsOccupation;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.planning.EOOccupation;
import fr.univlr.cri.conges.objects.occupations.HeuresSupplementaires;
import fr.univlr.cri.planning.PartagePlanning;
import fr.univlr.cri.planning.SPConstantes;
import fr.univlr.cri.planning.SPOccupation;
import fr.univlr.cri.util.wo5.DateCtrl;
import fr.univlr.cri.webapp.LRRecord;

/**
 * Classe dediees au DirectAction utilises par l'application Agenda.
 * 
 * @author Cyril TARADE <cyril.tarade at univ-lr.fr>
 */

public class DAAgenda extends DirectAction {

  public DAAgenda(WORequest request) {
    super(request);
  }

  /**
   * donne la liste des occupations d'un individu entre 2 date
   */
  public WOActionResults occupationsPourPeriodeAction() {
    //Session uneSession = cngSession();
    //WORequest request = request();
    
//    Number noIndividu = request.numericFormValueForKey("noIndividu", new NSNumberFormatter("#"));
//    NSTimestampFormatter dateFormatter = new NSTimestampFormatter("%d/%m/%Y %H:%M");
//    NSTimestamp debut = request.dateFormValueForKey("debut", dateFormatter);
//    NSTimestamp fin = request.dateFormValueForKey("fin", dateFormatter);
    
    NSDictionary dicoParams = PartagePlanning.dicoParams(request());
    
    Number noIndividu   = (Number) dicoParams.valueForKey("noIndividu");
    NSTimestamp debut   = (NSTimestamp) dicoParams.valueForKey("debut");
    NSTimestamp fin     = (NSTimestamp) dicoParams.valueForKey("fin");
    
    
    //StringBuffer buffer = new StringBuffer();

    NSArray spOccupations = new NSArray();
    int status = 0;
    String errMessage = "";
    
    if (noIndividu != null) {
      if (debut != null) {
        if (fin != null) {
          NSMutableDictionary bindings = new NSMutableDictionary();
          bindings.setObjectForKey(noIndividu, "oid");
          EOEditingContext edc = new EOEditingContext();
          EOIndividu individu = (EOIndividu) EOUtilities.objectWithFetchSpecificationAndBindings(
          		edc, EOIndividu.ENTITY_NAME, "individuWithOID", bindings);
          if (individu != null) {

            if (DateCtrl.isBeforeEq(debut, fin)) {
              NSArray recsOccupation = EOOccupation.findRecordsFromVOccupationInPeriod(edc,
                  noIndividu, debut, fin, "R", ConstsOccupation.CODE_VALIDEE);

              for (int i = 0; i < recsOccupation.count(); i++) {
                LRRecord recVOcc = (LRRecord) recsOccupation.objectAtIndex(i);
                //
                SPOccupation spOccupation = new SPOccupation(recVOcc.dateForKey("dteDebutReel"), recVOcc.dateForKey("dteFinReel"),
                    SPConstantes.SPOCC_TYPE_ABSENCE, recVOcc.stringForKey("motif"));
                
                if (recVOcc.stringForKey("toccLibelleCourt").equals(HeuresSupplementaires.LIBELLE_COURT)) {
                	spOccupation.setTypeTemps(SPConstantes.SPOCC_TYPE_PRESENCE);
                }
                
                spOccupations = spOccupations.arrayByAddingObject(spOccupation);
              }
              
              status = 1;

            } else {
              // erreurs de dates
              errMessage = "Date debut après la date de fin";
            }
          } else {
            // individu non trouv�
            errMessage = "Individu non trouvé";
          }
        } else {
          // fin absent
          errMessage = "Date de fin absente";
       }
      } else {
        // debut absent
        errMessage = "Date de début absente";
     }
    } else {
      // noIndividu absent
      errMessage = "noIndividu absent";
   }

    // creation Reponse a retourner
    
    WOResponse resultat = new WOResponse();
    resultat = PartagePlanning.reponsePlanning(spOccupations, status, errMessage);
  
    return resultat;
  }

  /**
   * donne la liste des horaires (sauf forme d'occupations negatives) d'un individu entre 2 date
   */
  public WOActionResults horairesPourPeriodeAction() {
    EOEditingContext edc = new EOEditingContext();
    
    NSDictionary dicoParams = PartagePlanning.dicoParams(request());
    Number noIndividu   = (Number) dicoParams.valueForKey("noIndividu");
    NSTimestamp debut   = (NSTimestamp) dicoParams.valueForKey("debut");
    NSTimestamp fin     = (NSTimestamp) dicoParams.valueForKey("fin");
 
    NSArray spOccupations = new NSArray();
    int status = 0;
    String errMessage = "";
    
 
    
    if (noIndividu != null) {
      if (debut != null) {
        if (fin != null) {
          NSMutableDictionary bindings = new NSMutableDictionary();
          bindings.setObjectForKey(noIndividu, "oid");
          EOIndividu individu = (EOIndividu) EOUtilities.objectWithFetchSpecificationAndBindings(edc, "Individu", "individuWithOID", bindings);
          if (individu != null) {
            if (DateCtrl.isBeforeEq(debut, fin)) {
           
              NSArray recsHoraire = EOOccupation.findRecordsFromVHorairesInPeriod(edc, app(), noIndividu, debut, fin, "R");

              for (int i = 0; i < recsHoraire.count(); i++) {
                NSDictionary recVHor = (NSDictionary) recsHoraire.objectAtIndex(i);
                Object dteDebutReel = recVHor.valueForKey("dteDebutReel");
                if (dteDebutReel != null &&	dteDebutReel != NSKeyValueCoding.NullValue) {
                  SPOccupation spOccupation = new SPOccupation(
                  		DateCtrl.stringToDate((String) recVHor.valueForKey("dteDebutReel"), "%d/%m/%Y %H:%M"), 
                  		DateCtrl.stringToDate((String) recVHor.valueForKey("dteFinReel"), "%d/%m/%Y %H:%M"), 
                  		SPConstantes.SPOCC_TYPE_PRESENCE, "");
                  spOccupations = spOccupations.arrayByAddingObject(spOccupation);
                }
                  
              }
//              boolean voirPauses = false;
//              if (voirLesPauses != null && voirLesPauses.intValue() == 1)
//                voirPauses = true;
//
//              // liste de toutes les horaires sous forme d'occupation
//              NSArray lesAffectationsAnnuelles = new NSArray();
//              NSTimestamp debutAnneeEnCours = DateCtrlConges.dateToDebutAnneeUniv(debut);
//              bindings.setObjectForKey(individu, "individu");
//
//              // doit-on verifier sur plusieurs ann�es ?
//              while (DateCtrlConges.isBeforeEq(debutAnneeEnCours, DateCtrlConges.dateToDebutAnneeUniv(fin))) {
//                bindings.setObjectForKey(debutAnneeEnCours, "dateDebut");
//                bindings.setObjectForKey(debutAnneeEnCours.timestampByAddingGregorianUnits(1, 0, -1, 0, 0, 0), "dateFin");
//                NSArray affectationsAnnuelles = FinderAffectationAnnuelle.findAffectationsAnnuellesInContext(bindings, edc);
//                lesAffectationsAnnuelles = lesAffectationsAnnuelles.arrayByAddingObjectsFromArray(affectationsAnnuelles);
//                debutAnneeEnCours = debutAnneeEnCours.timestampByAddingGregorianUnits(1, 0, 0, 0, 0, 0);
//              }
//
//              lesAffectationsAnnuelles = UtilMisc.removeDoublons(lesAffectationsAnnuelles);
//
//             for (int i = 0; i < lesAffectationsAnnuelles.count(); i++) {
//                EOAffectationAnnuelle uneAffectationAnnuelle = (EOAffectationAnnuelle) lesAffectationsAnnuelles.objectAtIndex(i);
//                for (int j = 0; j < uneAffectationAnnuelle.periodes().count(); j++) {
//                  EOPeriodeAffectationAnnuelle unePeriode = (EOPeriodeAffectationAnnuelle) uneAffectationAnnuelle.periodes().objectAtIndex(j);
//                  spOccupations = spOccupations.arrayByAddingObjectsFromArray(unePeriode.occupationsReellesHorairesPourPeriode(debut, fin,
//                      voirPauses));
//                }
//              }
//             
              
              
             status = 1;

            } else {
              // erreurs de dates
              errMessage = "Date debut après la date de fin";
              
            }
          } else {
            // individu non trouv�
            errMessage = "Individu non trouvé";
            
          }
        } else {
          // fin absent
          errMessage = "Date de fin absente";
        }
      } else {
        // debut absent
        errMessage = "Date de début absente";
             }
    } else {
      // noIndividu absent
      errMessage = "noIndividu absent";
    }

    // creation Reponse a retourner
    WOResponse resultat = new WOResponse();
    resultat = PartagePlanning.reponsePlanning(spOccupations, status, errMessage);

    return resultat;
  }
}
