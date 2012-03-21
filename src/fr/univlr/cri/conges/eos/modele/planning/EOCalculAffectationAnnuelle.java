// EOCalculAffectationAnnuelle.java
// Created on Thu Jun 17 08:48:18  2004 by Apple EOModeler Version 5.2

package fr.univlr.cri.conges.eos.modele.planning;

import com.webobjects.eocontrol.EOEditingContext;

import fr.univlr.cri.conges.objects.occupations.Occupation;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.webapp.LRLog;

public class EOCalculAffectationAnnuelle 
	extends _EOCalculAffectationAnnuelle {
	
	public EOCalculAffectationAnnuelle() {
		super();
	}
	
	public EOCalculAffectationAnnuelle(String nature) {
	  super();
	  setFlagNature(nature);
	  initValues();
	}

	public void initValues() {
		setMinutesDues(new Integer(0));
		setMinutesRestantes(new Integer(0));
		setMinutesReliquatRestantes(new Integer(0));
		setMinutesTravaillees(new Integer(0));
		setMinutesBilan(new Integer(0));
		setMinutesDecompteLegal(new Integer(0));
		setMinutesCongesConsommees(new Integer(-1));
		setMinutesConsommeesDateReliquat(new Integer(-1));
		setMinutesReliquatConsommeesDateReliquat(new Integer(-1));
		setMinutesDechargeSyndicaleRestantes(new Integer(0));
	}
	

	public Integer minutesTravaillees() {
		Integer minutesTravaillees = 0; 
		
		if (toAffectationAnnuelle().isCalculAutomatique()) {
			minutesTravaillees = super.minutesTravaillees();
		} else {
			int minutesDues 	= minutesDues().intValue();
			int minutesConges	= toAffectationAnnuelle().toMouvementDroitsCongesNonAutomatiques().mouvementMinutes().intValue();
			minutesTravaillees = new Integer(minutesDues + minutesConges);
		}
		
		return minutesTravaillees;
	}

	public void addMinutesDues(int minutesAdded) {
		int newMinutesDues = minutesDues().intValue();
		
		newMinutesDues += minutesAdded;
		setMinutesDues(new Integer(newMinutesDues));
		//substractMinutesRestantes(minutesAdded);
		// modifier les minutes dues n'a pas d'influence sur le reliquat
		int restant = minutesRestantes().intValue() - minutesAdded;
		setMinutesRestantes(new Integer(restant));
	}

	public void addMinutesTravaillees(int minutesAdded) {
		int newMinutesTravaillees = minutesTravaillees().intValue();
		
		newMinutesTravaillees += minutesAdded;
		setMinutesTravaillees(new Integer(newMinutesTravaillees));
		addMinutesRestantes(minutesAdded);		
	}

	public void substractMinutesTravaillees(int minutesSubstrated) {
		int newMinutesTravaillees = minutesTravaillees().intValue();
		
/*		LRLog.setLevel(LRLog.LEVEL_DEBUG);
		LRLog.trace("substractMinutesTravaillees() " + TimeCtrl.stringForMinutes(minutesSubstrated), true);
		LRLog.setLevel(LRLog.LEVEL_BASIC);*/
		
		newMinutesTravaillees -= minutesSubstrated;
		setMinutesTravaillees(new Integer(newMinutesTravaillees));
		//TODO modif pour reliquat : modifier les minutes dues n'a pas d'influence sur le reliquat
		//substractMinutesRestantes(minutesSubstrated);		
		int restant = minutesRestantes().intValue() - minutesSubstrated;
		
		

		setMinutesRestantes(new Integer(restant));
	}
	
	public void addMinutesRestantes(int minutes) {
		// TODO bascule sur reliquat ou CET quand compte courant plein
		int newMinutesRestantes = minutesRestantes().intValue();

		newMinutesRestantes += minutes;
			
		setMinutesRestantes(new Integer(newMinutesRestantes));				

	}
	
	public void substractMinutesRestantes(int minutes) {
		/*int soldeCompteReliquat = minutesReliquatRestantes().intValue();
		int soldeCompteCourant =  minutesRestantes().intValue();
		
		soldeCompteReliquat -= minutes;
		if (soldeCompteReliquat<0) {
			soldeCompteCourant += soldeCompteReliquat;
			soldeCompteReliquat = 0;
		}
		setMinutesRestantes(new Integer(soldeCompteCourant));				
		setMinutesReliquatRestantes(new Integer(soldeCompteReliquat));*/
		
		setMinutesRestantes(new Integer(minutesRestantes().intValue()-minutes));
	}

	/**
	 * suppression d'une absence : on remet les heures au bon endroit
	 * @param absence
	 */
	public void addMinutesRestantesForOccupation(Occupation occupation) {
		int soldeCompteReliquat = minutesReliquatRestantes().intValue();
		//int soldeCompteCourantInit =  minutesTravaillees().intValue() - minutesDues().intValue();
		int soldeCompteCourant =  minutesRestantes().intValue();

		setMinutesRestantes(new Integer(soldeCompteCourant + occupation.leDebitConges()));				
		setMinutesReliquatRestantes(new Integer(soldeCompteReliquat + occupation.leDebitReliquats()));
	}
	
	public void insertInEditingContext(EOEditingContext ec) {
		if (ec != null) {
			if (ec.globalIDForObject(this) == null) {
				ec.insertObject(this);
			}
		}
	}

	
	// TEMPO calcul CRICRI
	
	private Integer _minutesTravaillees3112;
	
	public void setMinutesTravaillees3112(Integer value) {
		_minutesTravaillees3112 = value;
	}
	
	public Integer minutesTravaillees3112() {
		return _minutesTravaillees3112;
	}
	
	private Integer _minutesConges3112;
	
	public void setMinutesConges3112(Integer value) {
		_minutesConges3112 = value;
	}
	
	public Integer minutesConges3112() {
		return _minutesConges3112;
	}
	
}
