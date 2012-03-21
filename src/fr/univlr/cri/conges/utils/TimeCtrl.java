/*
 * Copyright Université de La Rochelle 2004
 *
 * ctarade@univ-lr.fr
 *
 * Ce logiciel est un programme informatique servant à gérer les comptes
 * informatiques des utilisateurs. 
 * 
 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".

 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.

 * A cet égard  l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement,  à l'utilisation,  à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant 
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à 
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement, 
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité. 

 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez 
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 */

package fr.univlr.cri.conges.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSNumberFormatter;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.util.StringCtrl;

/**
 * @author ctarade
 * 
 * To change the template for this generated type comment go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class TimeCtrl {

  // GET MINUTES : Retourne le nombre de minutes correspondant à la chaine string au format %H:%M (l'inverse de stringFor: )
  public static int getMinutes(String chaine) {
    NSArray str = NSArray.componentsSeparatedByString(chaine, ":");
    int nombre = 0;

    if (StringCtrl.isEmpty(chaine) || ("00:00".equals(chaine)) || ("".equals(chaine)) || ("..:..".equals(chaine)))
      return 0;

    if (chaine.length() == 0)
      return 0;

    if (str.count() == 1)
      nombre = ((Number) str.objectAtIndex(0)).intValue() * 60;
    else {
      if ((((Number) new Integer((String) str.objectAtIndex(0))).intValue()) < 0)
        nombre = (-((((Number) new Integer((String) str.objectAtIndex(0))).intValue()) * 60) + ((((Number) new Integer((String) str.objectAtIndex(1)))
            .intValue())));
      else
        nombre = (((((Number) new Integer((String) str.objectAtIndex(0))).intValue()) * 60) + ((((Number) new Integer((String) str.objectAtIndex(1)))
            .intValue())));
    }

    if ((((Number) new Integer((String) str.objectAtIndex(0))).intValue()) < 0)
      nombre = -nombre; // on a passe une valeur negative

    return nombre;
  }

  // STRING FOR MINUTES
  // Formatte le nombre de minutes en une chaine au format %H:%M (l'inverse de numberOfMinutesFor: )
  public static String stringForMinutes(int numberOfMinutes) {
    String formattedString;
    int hours, minutes;
    boolean negatif = false;

    if (numberOfMinutes == 0)
      return "00:00";

    if (numberOfMinutes < 0) {
      negatif = true;
      numberOfMinutes = -numberOfMinutes;
    }

    hours = numberOfMinutes / 60;
    minutes = numberOfMinutes % 60;

    if (hours < 10)
      formattedString = "0" + hours;
    else
      formattedString = "" + hours;

    if (minutes < 10)
      formattedString = formattedString + ":0" + minutes;
    else
      formattedString = formattedString + ":" + minutes;

    if (negatif)
      formattedString = "-" + formattedString;

    return formattedString;
  }

  /**
   * retourne le nombre de minutes ecoulées dans la journée
   * 
   * @param date
   * @return
   */
  public static int getMinutesOfDay(NSTimestamp aDate) {

    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(aDate);
    return calendar.get(Calendar.MINUTE) + calendar.get(Calendar.HOUR_OF_DAY) * 60;

  }

  /**
   * mettre une date a minuit (le jour meme)
   * 
   * @param aDate
   * @return
   */
  public static NSTimestamp dateToMinuit(NSTimestamp aDate) {
    GregorianCalendar nowGC = new GregorianCalendar();
    nowGC.setTime(aDate);
    return aDate.timestampByAddingGregorianUnits(0, 0, 0, -nowGC.get(GregorianCalendar.HOUR_OF_DAY), -nowGC.get(GregorianCalendar.MINUTE), -nowGC
        .get(GregorianCalendar.SECOND));
  }

  /**
   * permet de transformer une heure en durée (remplace le : par un h
   * 
   * @param heure
   * @return
   */
  public static String stringHeureToDuree(String heure) {
    if (heure != null)
      return heure.replace(':', 'h');
    else
      return "00h00";
  }

  /**
   * permet de transformer une durée en heure (remplace le h par un :
   * 
   * @param heure
   * @return
   */
  public static String stringDureeToHeure(String heure) {
    if (heure != null)
      return heure.replace('h', ':');
    else
      return "00:00";
  }

  // TODO controler si null ou po
  public static int getHeuresFromString(String heuresMinutes) {
    return (int) ((float) TimeCtrl.getMinutes(heuresMinutes) / (float) 60);
  }

  public static int getMinutesFromString(String heuresMinutes) {
    return TimeCtrl.getMinutes(heuresMinutes) % 60;
  }

  public static String to_duree_en_jours(int minutes, String dureeJour) {
    String to_duree = "";
    double lesJours = (double) minutes / (double) (TimeCtrl.getMinutes(TimeCtrl.stringDureeToHeure(dureeJour)));
    Double unNombre = new Double(lesJours);
    NSNumberFormatter numberFormat = new NSNumberFormatter("0.00");
    to_duree += numberFormat.format(unNombre) + "j";
    return to_duree;
  }
}
