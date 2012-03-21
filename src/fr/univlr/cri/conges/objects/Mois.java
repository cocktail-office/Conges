package fr.univlr.cri.conges.objects;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;

import com.webobjects.eocontrol.EOCustomObject;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.utils.DateCtrlConges;

/*
 * Created on 21 juin 04
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author egeze
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Mois extends EOCustomObject {

  private NSMutableArray semaines;
  private String libelle, libelleLong, libelleAvecAnnee, annee;
  private NSTimestamp firstDay;
  NSArray moisStr = new NSArray(
  		new String[]{"JAN","FEV","MAR","AVR","MAI","JUI","JUI","AOU","SEP","OCT","NOV","DEC"});
  NSArray moisStrLong = new NSArray(
  		new String[]{"JANVIER","FEVRIER","MARS","AVRIL","MAI","JUIN","JUILLET","AOUT","SEPTEMBRE","OCTOBRE","NOVEMBRE","DECEMBRE"});

  public Mois() {
	super();       
  }

  public NSTimestamp firstDay() {
	return firstDay;
  }

  public void setFirstDay(NSTimestamp aDayTS) {
    GregorianCalendar aDayGC = new GregorianCalendar();
	int month = 0;
	
	aDayGC.setTime(aDayTS);	
	month = aDayGC.get(Calendar.MONTH);
	annee = String.valueOf(aDayGC.get(Calendar.YEAR));
	libelle = (String)moisStr.objectAtIndex(month);
	libelleLong = (String)moisStrLong.objectAtIndex(month);
	libelleAvecAnnee = libelle+" "+annee;		

	firstDay = aDayTS;
  }

  public String annee() {
  	return annee;
  }

  public String libelleCourt() {
  	return libelleLong.substring(0,1);
  }
  
  public String libelle() {
  	return libelle;
  }

  public String libelleLong() {
  	return libelleLong;
  }

  public String libelleAvecAnnee() {
    return libelleAvecAnnee;
  }


  public NSMutableArray semaines() {
    if (semaines == null) {
      semaines = new NSMutableArray();
    }
    return semaines;
  }

  /**
   * @param uneSemaine
   */
  public void add(Semaine uneSemaine) {
    semaines().addObject(uneSemaine);		
  }

  public void addSemaineAtIndex(Semaine uneSemaine, int index) {
	semaines().insertObjectAtIndex(uneSemaine,index);		
  }

  public int minutesTravaillees() {
	int minutesTravaillees = 0;
	NSArray lesSemaines = semaines();
	Enumeration enumLesSemaines = lesSemaines.objectEnumerator();
	while (enumLesSemaines.hasMoreElements()) {
	  Semaine uneSemaine = (Semaine) enumLesSemaines.nextElement();
	  minutesTravaillees += uneSemaine.minutesTravaillees();
	}
	return minutesTravaillees;
  }

  public String heuresTravaillees() {
	String heuresTravaillees="";
	heuresTravaillees = DateCtrlConges.to_duree(minutesTravaillees());
	return heuresTravaillees;
  }
  
}
