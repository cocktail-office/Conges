/*
 * Copyright Universit� de La Rochelle 2005
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
package fr.univlr.cri.conges.objects.occupations;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.planning.EOHoraire;
import fr.univlr.cri.conges.eos.modele.planning.EOOccupation;
import fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation;
import fr.univlr.cri.conges.objects.*;
import fr.univlr.cri.conges.utils.TimeCtrl;

public class AutorisationdAbsence extends Occupation {

  public AutorisationdAbsence(EOTypeOccupation unType, Planning unPlanning, NSTimestamp debutTS, NSTimestamp finTS, String unMotif, EOEditingContext ec) {
    super(unType, unPlanning, debutTS, finTS, unMotif, ec);
  }
  
  public AutorisationdAbsence(EOOccupation uneOccupation, Planning unPlanning, EOEditingContext ec) {
    super(uneOccupation, unPlanning, ec);
  }
  
  public AutorisationdAbsence(EOOccupation uneOccupation, EOEditingContext ec) {
    super(uneOccupation, ec);
  }
  
  public boolean isValide() {
    boolean isValide = super.isValide();

    if (isValide) {
      // recuperation de l'horaire associ� au jour de l'absence
      EOHoraire horaire = lePlanning.leJour(dateDebut()).semaine().horaire();
      // le cong�s compensateur doit etre inclus dans une plage horaire
      NSArray listDebutFin = NSArray.componentsSeparatedByString(horaire.horaires(), ",");
      GregorianCalendar calendar = new GregorianCalendar();
      calendar.setTime(dateDebut());
      int jourSemaine = calendar.get(Calendar.DAY_OF_WEEK);
      // recuperation des minutes du jour pour cet horaire
      // -2 car dimanche = 1, lundi = 2 ...
      NSArray horaireJournalier = NSArray.componentsSeparatedByString((String) listDebutFin.objectAtIndex(jourSemaine - 2), ";");

      int h1, h2, h3, h4;
      h1 = h2 = h3 = h4 = 0;

      if (horaireJournalier.count() > 0) {
        String matin = (String) horaireJournalier.objectAtIndex(0);
        NSArray matinDebutFin = NSArray.componentsSeparatedByString(matin, "|");
        if (matinDebutFin.count() > 0) {
          h1 = TimeCtrl.getMinutes((String) matinDebutFin.objectAtIndex(0));
          h2 = TimeCtrl.getMinutes((String) matinDebutFin.objectAtIndex(1));
        }
      }
      if (horaireJournalier.count() > 1) {
        String aprem = (String) horaireJournalier.objectAtIndex(1);
        NSArray apremDebutFin = NSArray.componentsSeparatedByString(aprem, "|");
        if (apremDebutFin.count() > 0) {
          h3 = TimeCtrl.getMinutes((String) apremDebutFin.objectAtIndex(0));
          h4 = TimeCtrl.getMinutes((String) apremDebutFin.objectAtIndex(1));
        }
      }

      // minutes debut fin du conges
      int m1 = TimeCtrl.getMinutesOfDay(dateDebut());
      int m2 = TimeCtrl.getMinutesOfDay(dateFin());

      boolean isInclusMatin = ((m1 >= h1) && (m2 <= h2));
      boolean isInclusAprem = ((m1 >= h3) && (m2 <= h4));

      // si le cong�s saisi est dans la tranche des heures de travail : OK
      if (isInclusMatin || isInclusAprem) {
        isValide = true;
      } else {
        setErrorMsg("La saisie de votre autorisation d'absence ne correspond pas à" + " un horaire de travail !\nCes congés doivent etre compris entre "
            + TimeCtrl.stringForMinutes(h1) + " et " + TimeCtrl.stringForMinutes(h2) + " ou entre " + TimeCtrl.stringForMinutes(h3) + " et "
            + TimeCtrl.stringForMinutes(h4) + ".");
        isValide = false;
      }

      if (isValide) {

        // on ne peut pas prendre de conges compensateur sur un JFerie, JChome et une periode de conges
        NSArray lesJoursOccupes = lePlanning.lesJours(TimeCtrl.dateToMinuit(dateDebut()), TimeCtrl.dateToMinuit(dateFin()));
        if (lesJoursOccupes.count() > 0) {
          Jour leJourOccupe = (Jour) lesJoursOccupes.lastObject();

          isValide = !leJourOccupe.isChome() && !leJourOccupe.isFerie();
          if (isValide) {
            if (isInclusMatin) {
              isValide = leJourOccupe.isTravailleAM() || leJourOccupe.isTravaille()
                  || leJourOccupe.isCongeLegalPM() || leJourOccupe.isCongePM();
            }
            if (isInclusAprem) {
              isValide = leJourOccupe.isTravaillePM() || leJourOccupe.isTravaille()
                  || leJourOccupe.isCongeLegalAM() || leJourOccupe.isCongeAM();
            }
            if (!isValide) {
              setErrorMsg("Vous devez prendre votre autorisation d'absence pendant un jour travaillé.");
            }
          } else {
            setErrorMsg("Vous ne pouvez pas prendre votre autorisation d'absence pendant un jour férié ou chomé.");
          }
        }
      }

    }
    return isValide;
  }

}
