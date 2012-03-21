/*
 * Copyright Consortium Coktail, 8 juin 07
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

package fr.univlr.cri.conges.objects;

import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.planning.EOCalculAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOPeriodeAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.I_ClasseMetierNotificationParametre;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;

/**
 * L'ensemble des contraintes de validites du planning
 * (max. heures, max sem. hautes ...)
 * 
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */
public class PlanningContraintes 
	extends A_GenericPlanningExtension 
		implements I_ClasseMetierNotificationParametre {

  /** bornes max des heures pour un planning (390) */
  private static int heuresCongesMaxi;

  /** nbre max de semaines hautes a mettre sur le planning */
  private static int nbreSemainesHautesMaxi;

  /** nbre max de semaines hautes a mettre sur le planning pour ce qui beneficient du depassement */
  private static int nbreSemainesHautesMaxiDepassement;


	public PlanningContraintes(Planning aPlanning) {
		super(aPlanning);
	}
  
  /**
   * @see I_ClasseMetierNotificationParametre
   * @param parametre
   */
  public static void initStaticField(
  		Parametre parametre) {
  	if (parametre == Parametre.PARAM_HEURES_CONGES_MAXI) {
  		heuresCongesMaxi = parametre.getParamValueInteger().intValue();
  	} else if (parametre == Parametre.PARAM_NBRE_SEMAINES_HAUTES_MAXI) {
  		nbreSemainesHautesMaxi = parametre.getParamValueInteger().intValue();
  	} else if (parametre == Parametre.PARAM_NBRE_SEMAINES_HAUTES_MAXI_DEPASSEMENT) {
  		nbreSemainesHautesMaxiDepassement = parametre.getParamValueInteger().intValue();
  	} 
  }

	/**
   * + de 8 semaines "hautes"
   * 
   * @return
   */
  public boolean limiteSemainesHautesDepassee() {
    int nb = 0;
    NSArray semaines = planning().semaines();
    NSMutableArray lesDebutsDeSemainesPassees = new NSMutableArray();
    NSArray lesHorairesTypes = null;// FinderTypeHoraire.findAllTypeHoraire(new EOEditingContext(), !affectationAnnuelle().isHorsNorme());
    if (planning().affectationAnnuelle().isHorsNorme()) {
      lesHorairesTypes = planning().lesTypesHorairesHorsNormes();
    } else {
      lesHorairesTypes = planning().lesTypesHorairesNormaux();
    }
    EOQualifier qual = EOQualifier.qualifierWithQualifierFormat("flagHoraireSemaineHaute = '1'", null);
    NSArray lesHorairesTypesHauts = EOQualifier.filteredArrayWithQualifier(lesHorairesTypes, qual);
    NSArray orderings = new NSArray(EOSortOrdering.sortOrderingWithKey("total", EOSortOrdering.CompareAscending));
    lesHorairesTypesHauts = EOSortOrdering.sortedArrayUsingKeyOrderArray(lesHorairesTypesHauts, orderings);
    if (lesHorairesTypesHauts.count() > 0) {
      for (int i = 0; i < semaines.count(); i++) {
        Semaine semaine = (Semaine) semaines.objectAtIndex(i);
        if (lesDebutsDeSemainesPassees.containsObject(semaine.debut()) == false) {
          lesDebutsDeSemainesPassees.addObject(semaine.debut());
          if (semaine.horaire() != null && semaine.horaire().isSemaineHaute()) {
            nb++;
          }
          int maxSemHautes = nbreSemainesHautesMaxi;
          if (planning().affectationAnnuelle().isDepSemHautes()) {
            maxSemHautes = nbreSemainesHautesMaxiDepassement;
          }
          if (nb > maxSemHautes) {
            return true;
          }
        }
      }
    }
    return false;
  }

  public int limiteCongesEnMinutes() {
    float limiteCongesEnMinutes = 0; // heuresCongesMaxi*60;
    NSArray periodes = planning().affectationAnnuelle().periodes();

    for (int i = 0; i < periodes.count(); i++) {
      EOPeriodeAffectationAnnuelle unePeriode = (EOPeriodeAffectationAnnuelle) periodes.objectAtIndex(i);
     float ratioQuotite = (unePeriode.quotite().floatValue() / (float) 100);
      NSTimestamp debutAnnee = unePeriode.affectationAnnuelle().dateDebutAnnee();
      NSTimestamp finAnnee = unePeriode.affectationAnnuelle().dateFinAnnee();
      // on cale a la fin de la journ�e, comme pour la fin de l'affectation annuelle
      NSTimestamp finPeriode = unePeriode.dateFin().timestampByAddingGregorianUnits(0,0,0,23,59,59);
      float ratioAnnee = (float) (finPeriode.getTime() - unePeriode.dateDebut().getTime()) / (float) (finAnnee.getTime() - debutAnnee.getTime());
      float limiteCongesEnMinutesPourPeriode = (ratioQuotite * ratioAnnee * (float) heuresCongesMaxi * 60);
      limiteCongesEnMinutes += limiteCongesEnMinutesPourPeriode;
    }

    return Math.round(limiteCongesEnMinutes);
  }

  /**
   * Indique s'il y eu depassement des heures maximum de conges
   * autorisees (390h00 a LR). Pas de controle si le depassement
   * est autorise.
   */
  public boolean limiteCongesDepassee() {
    boolean limiteCongesDepassee = false;
    if (planning().affectationAnnuelle().isDepassementCongesAutorise() == false) {
      EOCalculAffectationAnnuelle calculs = planning().affectationAnnuelle().calculAffAnn(planning().type());
      return (calculs.minutesTravaillees().intValue() - calculs.minutesDues().intValue()) > limiteCongesEnMinutes();
      //return TimeCtrl.getMinutes(TimeCtrl.stringDureeToHeure(heuresAssociees())) > heuresCongesMaxi;
    }
    return limiteCongesDepassee;
  }

  /**
   * valeur de l'excedent au dela des 390h00 max
   * 
   * @return
   */
  public int congesDepassementEnMinutes() {
    EOCalculAffectationAnnuelle calculs = planning().affectationAnnuelle().calculAffAnn(planning().type());
    int depassement = calculs.minutesTravaillees().intValue() - calculs.minutesDues().intValue() - limiteCongesEnMinutes();
    if (depassement < 0) {
      depassement = 0;
    }
    return depassement;
  }

  public String congesDepassement() {
    return DateCtrlConges.to_duree(congesDepassementEnMinutes());
  }

  public String heuresDues() {
    int minutesDues = planning().affectationAnnuelle().calculAffAnn(planning().type()).minutesDues().intValue();
    String heures = String.valueOf(minutesDues / 60);
    int minutesRestantes = minutesDues % 60;
    String minutes = String.valueOf(minutesRestantes);
    if (minutesRestantes < 10) {
      minutes = "0" + minutes;
    }
    return heures + "h" + minutes;
  }

  public String heuresMax() {
    return DateCtrlConges.to_duree(TimeCtrl.getMinutes(TimeCtrl.stringDureeToHeure(heuresDues())) + limiteCongesEnMinutes());
  }

  public String limiteCongesDepasseeMsg() {
    return "Vous ne pouvez pas dépasser " +
    	TimeCtrl.stringDureeToHeure(TimeCtrl.stringForMinutes(limiteCongesEnMinutes())) + 
    	" heures de congés par an.";
  }

  public String limiteSemainesHautesDepasseeMsg() {
    String max = Integer.toString(nbreSemainesHautesMaxi);
    if (planning().affectationAnnuelle().isDepSemHautes()) {
      max = Integer.toString(nbreSemainesHautesMaxiDepassement);
    }
    return "Vous ne pouvez pas avoir plus de " + max + " semaines hautes dans l année.";
  }
}
