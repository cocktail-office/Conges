package fr.univlr.cri.conges.utils;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;

import com.sun.tools.javac.comp.Todo;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.planning.I_ClasseMetierNotificationParametre;
import fr.univlr.cri.conges.objects.Parametre;
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
 * Classe ajoutant des methodes a DateCtrl
 * 
 * @author Emmanuel Gere <egeze at univ-lr.fr>
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */
public class DateCtrlConges 
	extends DateCtrl 
		implements I_ClasseMetierNotificationParametre {
 	
  /**
   * @deprecated
   * @see #initStaticField(Parametre)
   */
  public static void initStaticFields(String aDebutAnnee) {
  	debutAnnee = aDebutAnnee;
  }
  
	/**
	 * @see I_ClasseMetierNotificationParametre
	 */
  public static void initStaticField(
  		Parametre parametre) {
  	if (parametre == Parametre.PARAM_DEBUT_ANNEE_UNIVERSITAIRE) {
  		debutAnnee = parametre.getParamValueString();
  	}
  }
	
	private static String debutAnnee;
	/**
	 * Methode qui donne les jours feries entre deux dates
	 * firstDay et lastDay inclus
	 * @param firstDayTS
	 * @param lastDayTS
	 * @return
	 */
	public static NSArray joursFeriesEntre2Dates(NSTimestamp firstDayTS, NSTimestamp lastDayTS) {
		NSMutableArray joursFeriesEntre2Dates = null;
		NSArray joursFeries = null;
		Enumeration enumJoursFeries = null;
		GregorianCalendar firstDayGC, lastDayGC = null;
		int firstDayYear, lastDayYear = 0;
		
		firstDayGC = new GregorianCalendar();
		firstDayGC.setTime(firstDayTS);	
		firstDayYear = firstDayGC.get(Calendar.YEAR);
		lastDayGC = new GregorianCalendar();
		lastDayGC.setTime(lastDayTS);	
		lastDayYear = lastDayGC.get(Calendar.YEAR);

		joursFeries = DateCtrlConges.holidaysFR(firstDayYear);
		while (firstDayYear<lastDayYear) {
			firstDayYear++;
			joursFeries = joursFeries.arrayByAddingObjectsFromArray(
					DateCtrlConges.holidaysFR(firstDayYear));
		}

		joursFeriesEntre2Dates = new NSMutableArray();
		enumJoursFeries = joursFeries.objectEnumerator();
		while (enumJoursFeries.hasMoreElements()) {
			NSTimestamp unJourFerie = (NSTimestamp)enumJoursFeries.nextElement();
			if (isAfterEq(unJourFerie,firstDayTS) && isBeforeEq(unJourFerie,lastDayTS)) {
				joursFeriesEntre2Dates.addObject(unJourFerie);
			}
		}
		
		return joursFeriesEntre2Dates;	
	}

	// Methode qui calcule le nbre de jours ouvres sur une periode donnee
	static public long nbreJoursOuvresEntre(NSTimestamp firstDayTS, NSTimestamp lastDayTS) {
		long nbreJoursOuvres = 0;
		
		if (isAfter(firstDayTS,lastDayTS)) {
			nbreJoursOuvres = -1;
		} else {
			NSArray joursFeries = joursFeriesEntre2Dates(firstDayTS,lastDayTS);
			//Enumeration enumJoursFeries = joursFeries.objectEnumerator();
			GregorianCalendar currentDayGC = null;
			NSTimestamp currentDayTS = null;
			int currentDayNum = 0;

			
			nbreJoursOuvres = lastDayTS.getTime()-firstDayTS.getTime();
			nbreJoursOuvres /= 1000; // Transfo en secondes
			nbreJoursOuvres /= 3600; // Transfo en heures
			nbreJoursOuvres /= 24; // Transfo en jours
			nbreJoursOuvres++;
			currentDayTS = new NSTimestamp(firstDayTS);
			currentDayGC = new GregorianCalendar();
			while (isAfterEq(lastDayTS,currentDayTS)) {
				currentDayGC.setTime(currentDayTS);
				currentDayNum = currentDayGC.get(Calendar.DAY_OF_WEEK);			
				if (joursFeries.containsObject(currentDayTS) ||
					currentDayNum == Calendar.SATURDAY ||
					currentDayNum == Calendar.SUNDAY) {
					nbreJoursOuvres--;
				}
				currentDayTS = currentDayTS.timestampByAddingGregorianUnits(0,0,1,0,0,0);
			}
		}
		return nbreJoursOuvres;	
	}
	
	
	/**
	 * remonte jusqu'au lundi 0:00
	 * @param date
	 */
	public static NSTimestamp toLundi(NSTimestamp date) {
		NSTimestamp lundiEnCours = date;
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(lundiEnCours);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek==GregorianCalendar.SUNDAY) {
			lundiEnCours = lundiEnCours.timestampByAddingGregorianUnits(0,0,1,0,0,0);
		} else {
			lundiEnCours = lundiEnCours.timestampByAddingGregorianUnits(0,0,-((dayOfWeek-2)%7),0,0,0);
		}
		return TimeCtrl.dateToMinuit(lundiEnCours);
	}
	
  /**
   * Donne le numero d'une semaine pour une date 
   * @param date
   * @return
   */
	public static int weekNumber(NSTimestamp date) {
		FixedCalendar fixedCalendar = new FixedCalendar();
		fixedCalendar.setTime(date);
		return fixedCalendar.get(Calendar.WEEK_OF_YEAR); 
	}
		
	/**
	 * retourne le debut de l'annee universitaire d'une date	
	 * ex : 02/02/2005 -> 01/09/2004
	 * @param uneDate
	 * @return
	 */
	public static NSTimestamp dateToDebutAnneeUniv(NSTimestamp uneDate) {	
		// on transltate la date sur l'annee civile : 01/09/2004 -> 01/01/2004
		int jourDebutAnnee = Integer.valueOf(debutAnnee.substring(0,2)).intValue();
	  int moisDebutAnnee = Integer.valueOf(debutAnnee.substring(3,5)).intValue();
      
      NSTimestamp uneDateDecalee = uneDate.timestampByAddingGregorianUnits(
          jourDebutAnnee-1, -(moisDebutAnnee-1), 0, 0, 0, 0); 
      GregorianCalendar nowGC = new GregorianCalendar();
      nowGC.setTime(uneDateDecalee);
      NSTimestamp debutAnneeDecalee = uneDateDecalee.timestampByAddingGregorianUnits(
          0,
          -nowGC.get(GregorianCalendar.MONTH),
          -nowGC.get(GregorianCalendar.DAY_OF_MONTH)+1,
          -nowGC.get(GregorianCalendar.HOUR_OF_DAY),
          -nowGC.get(GregorianCalendar.MINUTE),
          -nowGC.get(GregorianCalendar.SECOND)
      );
      NSTimestamp debutAnnee = debutAnneeDecalee.timestampByAddingGregorianUnits(
          0, (moisDebutAnnee-1), 0, 0, 0, 0);
      return debutAnnee;
  }

    /**
     * retourne la fin de l'annee universitaire d'une date
     * ex : 02/02/2005 -> 31/08/2005
     * @param uneDate
     * @return
     */
     public static NSTimestamp dateToFinAnneeUniv(NSTimestamp uneDate) {
         return dateToDebutAnneeUniv(uneDate).timestampByAddingGregorianUnits(1,0,-1,0,0,0);
     }

     /**
      * retourne le debut de l'annee civile d'une date
      * ex : 02/02/2005 -> 01/01/2005
      * @param uneDate
      * @return
      */
     public static NSTimestamp dateToDebutAnneeCivile(NSTimestamp uneDate) {
         GregorianCalendar nowGC = new GregorianCalendar();
         nowGC.setTime(uneDate);
         NSTimestamp debutAnneeCivile = uneDate.timestampByAddingGregorianUnits(
 		        0,
 		        -nowGC.get(GregorianCalendar.MONTH),
 		        -nowGC.get(GregorianCalendar.DAY_OF_MONTH)+1,
 		        -nowGC.get(GregorianCalendar.HOUR_OF_DAY),
 		        -nowGC.get(GregorianCalendar.MINUTE),
 		        -nowGC.get(GregorianCalendar.SECOND)
 		        );
         return debutAnneeCivile;
     }

     /**
      * retourne le 1er janvier associe a une date sur une annee univ
      * ex : 02/02/2005 -> 01/01/2005
      * ex : 10/10/2004 -> 01/01/2005
      * @param uneDate
      * @return
      */
     public static NSTimestamp date1erJanAnneeUniv(NSTimestamp uneDate) {
       NSTimestamp debutAnneeUniv = dateToDebutAnneeUniv(uneDate);
       int jourDebutAnnee = Integer.valueOf(debutAnnee.substring(0,2)).intValue();
       int moisDebutAnnee = Integer.valueOf(debutAnnee.substring(3,5)).intValue();
       int diffJour = -jourDebutAnnee+1;
       int diffMois = (12-moisDebutAnnee)+1;
       return debutAnneeUniv.timestampByAddingGregorianUnits(
           0, diffMois, -diffJour, 0, 0, 0);
     }

     /**
      * retourne la fin de l'annee civile d'une date
      * @param uneDate
      * @return
      */
      public static NSTimestamp dateToFinAnneeCivile(NSTimestamp uneDate) {
          return dateToDebutAnneeCivile(uneDate).timestampByAddingGregorianUnits(1,0,-1,0,0,0);
      }

    

     /**
      * retourne le debut du mois d'une date
      * @param uneDate
      * @return
      */
      public static NSTimestamp dateToDebutMois(NSTimestamp uneDate) {
          // on transltate la date sur l'annee civile : 01/09/2004 -> 01/01/2004
          GregorianCalendar nowGC = new GregorianCalendar();
          nowGC.setTime(uneDate);
          NSTimestamp debutMois = uneDate.timestampByAddingGregorianUnits(
  		        0,
  		        0,
  		        -nowGC.get(GregorianCalendar.DAY_OF_MONTH)+1,
  		        -nowGC.get(GregorianCalendar.HOUR_OF_DAY),
  		        -nowGC.get(GregorianCalendar.MINUTE),
  		        -nowGC.get(GregorianCalendar.SECOND)
  		        );
          return debutMois;
      }
      
      /**
       * retourne la fin du mois d'une date
       * @param uneDate
       * @return
       */
       public static NSTimestamp dateToFinMois(NSTimestamp uneDate) {
           return dateToDebutMois(uneDate).timestampByAddingGregorianUnits(0,1,-1,0,0,0);
       }

     
     /**
      * savoir si 2 jour sont les meme (ignore les heures)
      * @param uneDate1
      * @param uneDate2
      * @return
      */
     public static boolean isSameDay(NSTimestamp uneDate1, NSTimestamp uneDate2) {
         return dateToString(uneDate1).equals(dateToString(uneDate2));
     }

    /**
     * @param uneDate
     * @return
     */
    public static NSTimestamp dateDebutAnnee(NSTimestamp uneDate) {
    	NSTimestamp dateDebutAnnee = uneDate;
    	GregorianCalendar myCalendar = new GregorianCalendar();
    	
    	int moisDebutAnnee = Integer.valueOf(debutAnnee.substring(3,5)).intValue();
    	myCalendar.setTime(uneDate);
    	int year = myCalendar.get(GregorianCalendar.YEAR);
    	int month = myCalendar.get(GregorianCalendar.MONTH)+1;
    	if ( month < moisDebutAnnee) year--;
        myCalendar.set(GregorianCalendar.DAY_OF_MONTH,1);
        myCalendar.set(GregorianCalendar.MONTH,moisDebutAnnee-1);
        myCalendar.set(GregorianCalendar.YEAR,year);
        myCalendar.set(GregorianCalendar.HOUR_OF_DAY,0);
        myCalendar.set(GregorianCalendar.MINUTE,0);
        myCalendar.set(GregorianCalendar.SECOND,0);
    	
    	dateDebutAnnee = new NSTimestamp(myCalendar.getTimeInMillis());
    	
    	return dateDebutAnnee;
    }

    /**
     * retourne l'annee universitaire d'une date.
     * ex : 10/10/2004 - > 2004/2005
     * @return
     */
    public static String anneeUnivForDate(NSTimestamp aDate) {
    	GregorianCalendar myCalendar = new GregorianCalendar();
    	
    	int moisDebutAnnee = Integer.valueOf(debutAnnee.substring(3,5)).intValue();
    	myCalendar.setTime(aDate);
    	if (myCalendar.get(GregorianCalendar.MONTH)+1 >= moisDebutAnnee)
    		return myCalendar.get(GregorianCalendar.YEAR) + "/" + (myCalendar.get(GregorianCalendar.YEAR)+1);
    	else
    		return (myCalendar.get(GregorianCalendar.YEAR)-1) + "/" + myCalendar.get(GregorianCalendar.YEAR);
    }
    
    /**
     * retourne la date de debut d'annee universitaire pour
     * une periode universitaire donnee
     * ex : (String)"2004/2005" -> (NSTimestamp)01/09/2004
     * @return
     */
    public static NSTimestamp dateDebutAnneePourStrPeriodeAnnee(String aAnneeUniv) {
      return stringToDate(debutAnnee + "/" + aAnneeUniv.substring(0,4));
    }

    /**
     * retourne la date de debut d'annee universitaire pour une annee donnee
     * ex : (String)"2004" -> (NSTimestamp)01/09/2004
     * @return
     */
    public static NSTimestamp dateDebutAnneePourStrAnnee(String aAnnee) {
      return stringToDate(debutAnnee + "/" + aAnnee);
    }
    
	//TODO 05/01/2005 : centraliser la methode (existe dans Planning.java)
	public static String to_duree(int minutes) {
		String to_duree = "";
        
        int lesMinutesAbs = Math.abs(minutes);
        
		int lesHeures = lesMinutesAbs/60;
		int lesMinutes = lesMinutesAbs%60;
		
		to_duree += lesHeures+"h";
		if (lesMinutes<10) to_duree+="0";
		to_duree += lesMinutes;
		
        if (minutes < 0) {
          to_duree = "-" + to_duree;
        }
        
		return to_duree;
	}

	

  
  /**
   * Retourne la liste des periodes <code>EOPeriodeAffectationAnnuelle</code> attendues par l'affectation pour une date de reference
   * Modification de la methode pour tenir compte aussi des periodes qui n'incluent pas la date du jour, mais qui sont dans
   * l'ann�e universitaire en cours.
   * 
   *  Exemples d'utilisation
   * - dateRef = 15/06/2004 ET affectation = debut:30/06/2003 - fin:31/08/2008 >> "01/09/2003"->"31/08/2004"
   * - dateRef = 15/06/2004 ET affectation = debut:30/06/2003 - fin:30/06/2004 >> "01/09/2003"->"30/06/2004"
   * - dateRef = 15/06/2004 ET affectation = debut:15/01/2004 - fin:           >> "15/01/2004"->"31/08/2004"
   * - dateRef = 15/06/2004 ET affectation = debut:30/06/2004 - fin:           >> "30/06/2004"->"31/08/2004"
   * - dateRef = 15/06/2004 ET affectation = debut:01/09/2003 - fin:15/05/2004 >> "01/09/2003"->"15/05/2004"
   * 
   * @return <code>NSArray</code> de couples de dates debut / fin
   */  
  public static NSArray lesPeriodesAnneeUniv(NSTimestamp dateRef, NSTimestamp dateDebutAff, NSTimestamp dateFinAff) {
  	// deduire l'ann�e universitaire associee a la date dateRef
  	NSTimestamp dDebAnneeUniv = dateToDebutAnneeUniv(dateRef);
  	NSTimestamp dFinAnneeUniv = dateToFinAnneeUniv(dateRef);
  	
  	// retailler la periode par rapport a l'annee universitaire
  	NSTimestamp dDebutPeriode = dateDebutAff;
  	if (dateDebutAff == null || isBefore(dateDebutAff, dDebAnneeUniv)) {
  		dDebutPeriode = dDebAnneeUniv;
  	}
  	NSTimestamp dFinPeriode = dateFinAff;
  	if (dateFinAff == null || isAfter(dateFinAff, dFinAnneeUniv)) {
  		dFinPeriode = dFinAnneeUniv;
  	}
  	
  	return new NSArray(new NSTimestamp[]{dDebutPeriode, dFinPeriode});
  }
  

  /**
   * Donne le nombre de jours entre 2 dates
   */
  public static int nbJoursEntre(NSTimestamp debut, NSTimestamp fin) {
    return (int) ((fin.getTime() - debut.getTime()) / (1000 * 60 * 60 * 24));
  }

  /**
   * Transformer une liste de mois (identifiés par leur date du premier jour)
   * en une liste de jours. Le dernier mois est à inclure.
   * @return
   */
  public static NSArray getListeJoursPourMois(NSArray moisArray) {
  	NSMutableArray array = new NSMutableArray();
  	
  	for (int i=0; i<moisArray.count(); i++) {
  		NSTimestamp moisItem = (NSTimestamp) moisArray.objectAtIndex(i);
  		NSTimestamp debutMois = DateCtrlConges.dateToDebutMois(moisItem);
  		NSTimestamp finMois = DateCtrlConges.dateToFinMois(moisItem);
  		NSTimestamp jourEnCours = debutMois;
  		while (isBeforeEq(jourEnCours, finMois)) {
  			array.addObject(jourEnCours);
  			jourEnCours = jourEnCours.timestampByAddingGregorianUnits(0,0,1,0,0,0);
  		}
  	}
  	
  	return array.immutableClone();
  }

  /**
   * La liste des dates comprises entre 2 dates
   * @return
   */
  public static NSArray getListeJoursEntre(NSTimestamp debut, NSTimestamp fin) {
  	NSMutableArray array = new NSMutableArray();
  	
  	NSTimestamp jourEnCours = debut;
  	while (isBeforeEq(jourEnCours, fin)) {
  		array.addObject(array);
  		jourEnCours = jourEnCours.timestampByAddingGregorianUnits(0,0,1,0,0,0);
  	}
  	
  	return array.immutableClone();
  }

  /**
   * La liste des dates comprises entre 2 dates
   * @return
   */
  public static NSArray getListeDebutMoisEntre(NSTimestamp debut, NSTimestamp fin) {
  	NSMutableArray array = new NSMutableArray();
  	
  	NSTimestamp jourEnCours = dateToDebutMois(debut);
  	NSTimestamp dernierMois = dateToDebutMois(fin);
  	while (isBeforeEq(jourEnCours, dernierMois)) {
  		array.addObject(jourEnCours);
  		jourEnCours = jourEnCours.timestampByAddingGregorianUnits(0,1,0,0,0,0);
  	}
  	
  	return array.immutableClone();
  }
}
