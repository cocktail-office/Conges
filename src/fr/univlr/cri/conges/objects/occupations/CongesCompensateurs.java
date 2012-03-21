package fr.univlr.cri.conges.objects.occupations;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.planning.*;
import fr.univlr.cri.conges.objects.*;
import fr.univlr.cri.conges.utils.TimeCtrl;

/**
 * @author ctarade
 */
public class CongesCompensateurs extends Occupation {

  public final static String LIBELLE_COURT = "C_COMP";

  public CongesCompensateurs(EOTypeOccupation unType, Planning unPlanning, NSTimestamp debutTS, NSTimestamp finTS, String unMotif, EOEditingContext ec) {
    super(unType, unPlanning, debutTS, finTS, unMotif, ec);
  }
  
  public CongesCompensateurs(EOOccupation uneOccupation, Planning unPlanning, EOEditingContext ec) {
    super(uneOccupation, unPlanning, ec);
  }

  public CongesCompensateurs(EOOccupation uneOccupation, EOEditingContext ec) {
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
        setErrorMsg("La saisie de vos congés compensateurs ne correspond pas à" + " un horaire de travail !\nCes congés doivent etre compris entre "
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
              setErrorMsg("Vous devez prendre vos congés compensateurs pendant un jour travaillé.");
            }
          } else {
            setErrorMsg("Vous ne pouvez pas prendre vos congés compensateurs pendant un jour férié ou chomé.");
          }
        }
      }

    }
    return isValide;
  }

  public void calculerValeur() {
    // Le debit de cette occupation est egal
    // a la duree de travail prevue pendant la duree de l'absence

    super.calculerValeur();
    setLaValeur(-laValeur());

  }

  public void confirmer() {

  }

}
