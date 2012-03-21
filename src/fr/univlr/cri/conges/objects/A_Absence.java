package fr.univlr.cri.conges.objects;

import java.util.GregorianCalendar;

import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOPeriodeAffectationAnnuelle;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.CRIDataBus;

/**
 * Classe commune a tous les types d'occupation
 * 
 * @author ctarade
 */
public abstract class A_Absence
	implements I_Absence {

	public void resetCache() {
		_dureeReelle = null;
		_dureeComptabilisee = null;
	}
	
	//
	private String _dureeReelle;

	/**
	 * La duree reelle de l'absence.
	 * Format 0.0 pour des occupations à la journée
	 * Format 00h00 pour des occupations à la minute
	 */
  public String duree() {
  	if (_dureeReelle == null) {
  		_dureeReelle= "0.0";
      if (isOccupationJour()) {
        long l_duree = dateFin().getTime() - dateDebut().getTime() + (12 * 60 * 60 * 1000);
        float dureeEnJours = (float) (l_duree * 1.0) / (1000 * 60 * 60 * 24);
        _dureeReelle = Float.toString(dureeEnJours);
      } else {
      	_dureeReelle = TimeCtrl.stringHeureToDuree(
      			TimeCtrl.stringForMinutes(
      					TimeCtrl.getMinutesOfDay(dateFin()) - TimeCtrl.getMinutesOfDay(dateDebut())));
      }
    }
    return _dureeReelle;
  }
  
  private String _dureeComptabilisee;
  
  /**
   * Duree comptabilisée en jour de cette absence
   */
  public String dureeComptabilisee() {
    if (_dureeComptabilisee == null) {
      _dureeComptabilisee = "0.0";
      if (isOccupationJour()) {
        NSArray presenceList = affectationAnnuelle().presencePourPeriode("R", dateDebut(), dateFin());
        String presenceString = "";   
        for (int i = 0; i < presenceList.count(); i++) {
        	String presence = (String) presenceList.objectAtIndex(i);
        	// cas des absences qui commencent l'apres midi, on ignore la 
        	// presence sur la matinee
        	if (i == 0) {
        		// cas particulier ou c'est une demi journée unique
        		if (dateDebut().equals(dateFin())) {
        			GregorianCalendar gc = new GregorianCalendar();
        			gc.setTime(dateDebut());
          		if (gc.get(GregorianCalendar.AM_PM) == GregorianCalendar.PM) {
          			presence = presence.substring(1,2);
          		} else if (gc.get(GregorianCalendar.AM_PM) == GregorianCalendar.AM) {
          			presence = presence.substring(0,1);
          		}
        		} else {
          		GregorianCalendar gcDebut = new GregorianCalendar();
          		gcDebut.setTime(dateDebut());
          		if (gcDebut.get(GregorianCalendar.AM_PM) == GregorianCalendar.PM) {
          			presence = presence.substring(1,2);
          		}
        		}
        	} else if (i == presenceList.count() -1) {
          	// cas des absences qui finissent le matin
        		// on ignore la presence sur l'apres midi
        		GregorianCalendar gcFin = new GregorianCalendar();
        		gcFin.setTime(dateFin());
        		if (gcFin.get(GregorianCalendar.AM_PM) == GregorianCalendar.AM) {
        			presence = presence.substring(0,1);
        		}
        	}
          presenceString += presence;
        }
        presenceString = StringCtrl.replace(presenceString, "1", "");
        presenceString = StringCtrl.replace(presenceString, "0", "");
        float dureeFloat = (float) presenceString.length() / (float) 2;
        _dureeComptabilisee = Float.toString(dureeFloat);
      }

    }
    return _dureeComptabilisee;
  }
  
  // le planning associé à l'absence
  private Planning planning;
  
  public void setPlanning(Planning p) {
  	planning = p;
  }
  
  public Planning planning() {
  	return planning;
  }
 
  /**
   * Flagger les jours liés à l'absence au statut forcé pour tous les jours
   * qui doivent l'être. Les durées travaillées sont aussi mises à jour (7h00
   * pour une affectation à 100%, du lundi au vendredi, 0h00 pour le samedi)
   */
  public void doForceJour() {
 		// les autres conges forces
		NSMutableArray occupationHoraireForceList = new NSMutableArray(
				EOQualifier.filteredArrayWithQualifier(
						planning.absences(), CRIDataBus.newCondition(IS_HORAIRE_FORCE_KEY+"=%@", new NSArray(Boolean.TRUE))));
		// on supprime l'absence actuelle
		occupationHoraireForceList.removeIdenticalObject(this);
		
		//
    NSArray jours = planning.lesJours(dateDebut(), dateFin());
    // on passe l'ensemble des jours en revue
		for (int k=0; k<jours.count(); k++) {

			Jour jour = (Jour) jours.objectAtIndex(k);
	  
			// on ne traite ni les samedi ni les dimanche
			if (jour.isSamedi() || jour.isDimanche()) {
				continue;
			}
			
			// on ne traite pas les jours deja forcés par ailleurs
			if (jour.isHoraireForce()) {
				continue;
			}
			
			boolean shouldForcerSemaine = false;
			
			// premier cas : l'occupation englobe la semaine
			// complete du lundi au samedi exclus
			NSTimestamp dateLundi 		= DateCtrlConges.toLundi(jour.date());
			NSTimestamp dateSamedi	 	= dateLundi.timestampByAddingGregorianUnits(0,0,5,0,0,0);
			if (DateCtrlConges.isAfterEq(dateLundi, dateDebut()) && 
					DateCtrlConges.isBefore(dateSamedi, dateFin())) {
				shouldForcerSemaine = true;
			}
			
			// deuxieme cas : la semaine est complete due a la 
			// presence d'autres conges forcés : on doit controler
			// parmi les 5 jours de la semaine
			if (!shouldForcerSemaine) {
				
				for (int j=0; j<5; j++) {
					//
					NSTimestamp date = dateLundi.timestampByAddingGregorianUnits(0,0,j,0,0,0);
					// 
					shouldForcerSemaine = true;
					// on ne traite pas le cas du jour actuel
					if (!DateCtrlConges.isSameDay(date, jour.date())) {
						boolean isJourSurCongeForce = false;
						// verifier d'abord sur l'occupation actuelle
						if (DateCtrlConges.isAfterEq(date, dateDebut()) && 
								DateCtrlConges.isBeforeEq(date, dateFin())) {
							isJourSurCongeForce = true;
						}
						// sinon, on regarde sur les autres conges force
						if (!isJourSurCongeForce) {
							NSArray occupationHoraireForcePourJourList = EOQualifier.filteredArrayWithQualifier(
									occupationHoraireForceList,
									CRIDataBus.newCondition(DATE_DEBUT_KEY + "<=%@ and " + DATE_FIN_KEY + ">=%@",
											new NSArray(new NSTimestamp[]{date, date})));
							if (occupationHoraireForcePourJourList.count() > 0) {
								isJourSurCongeForce = true;
							}
						}
						// pas de congé sur ce jour => on sort de la boucle
						if (!isJourSurCongeForce) {
							shouldForcerSemaine = false;
							break;
						}
					}
				}
			}
			
			
      if (shouldForcerSemaine) {
      	
      	// on traite les cas ou la semaine est a cheval sur plusieurs periodes
      	// on va appliquer l'horaire forcé, mais sur une duree correspondant
      	// a chaque jour en regard de la quotité de leur période
      	NSArray periodes = EOQualifier.filteredArrayWithQualifier(
      			planning.affectationAnnuelle().periodes(),
      			CRIDataBus.newCondition(
      					DATE_DEBUT_KEY + "<=%@ and " + DATE_FIN_KEY + ">=%@",
      					new NSArray(new NSTimestamp[] { jour.date(),jour.date() })));
      	
        	for (int i = 0; i < periodes.count(); i++) {
          EOPeriodeAffectationAnnuelle periode = (EOPeriodeAffectationAnnuelle) periodes.objectAtIndex(i);
      	
          if (periode != null) {

          	int dureeForcee = (int)(((float)(7 * 60)) * ((float)periode.quotite().intValue() / (float)100));
          	
          	// on boucle sur l'ensemble des jours de la semaine
          	NSTimestamp dateVendredi 	= dateLundi.timestampByAddingGregorianUnits(0,0,5,0,0,0);
          	
          	NSArray jourAForcerList = planning.lesJours(dateLundi, dateVendredi);
          			
          	for (int j=0; j<jourAForcerList.count(); j++) {
          		
          		Jour jourAForcer = (Jour) jourAForcerList.objectAtIndex(j);
          		
            	int dureeTravailleeInitiale = jourAForcer.dureeTravailleeEnMinutes();
            	
            	if (!jourAForcer.isSamedi() && 
            			!jourAForcer.isChome() && 
            			!jourAForcer.isFerie() && 
            			!jourAForcer.isDimanche()) {
                // on met a jour la valeur des droits a conges d'apres la difference
            		if (planning.affectationAnnuelle().isCalculAutomatique()) {
              		planning.affectationAnnuelle().calculAffAnn(planning.type()).addMinutesTravaillees(dureeForcee);
              		planning.affectationAnnuelle().calculAffAnn(planning.type()).substractMinutesTravaillees(dureeTravailleeInitiale);
            		}
                // maj des calculs
                jourAForcer.setDureeTravailleeEnMinutes(dureeForcee);
                jourAForcer.setDuree(String.valueOf(dureeForcee));
                // tagger comme jour a horaire force
                jourAForcer.addStatut(Jour.STATUS_HORAIRE_FORCE);
                
              }
              // on force le samedi
              if (jourAForcer.isSamedi()) {
                // et mettre travail à 0h00
              	jourAForcer.setDureeTravailleeEnMinutes(0);
              	jourAForcer.setDuree(String.valueOf(0));
                // tagger comme jour a horaire force
              	jourAForcer.addStatut(Jour.STATUS_HORAIRE_FORCE);
              }
          	}
          }
      	}
      }
    }
  }
  
  /**
   * 
   */
	public EOAffectationAnnuelle affectationAnnuelle() {
		return planning().affectationAnnuelle();
	}

}
