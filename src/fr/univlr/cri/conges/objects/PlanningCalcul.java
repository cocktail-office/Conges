/*
 * Copyright Consortium Coktail, 7 juin 07
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

import java.util.GregorianCalendar;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSNumberFormatter;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.constantes.ConstsJour;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOCalculAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOMouvement;
import fr.univlr.cri.conges.eos.modele.planning.EOVIndividuConges;
import fr.univlr.cri.conges.eos.modele.planning.I_ClasseMetierNotificationParametre;
import fr.univlr.cri.conges.factory.CngCalculFactory;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.LRLog;

/**
 * Regroupe tous les calculs relatifs � un planning
 * 
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */
public class PlanningCalcul 
	extends A_GenericPlanningExtension 
		implements I_ClasseMetierNotificationParametre {


  public static String dureeJourneeApp = Parametre.PARAM_DUREE_JOUR_CONVERSION.getParamValueString();
  /** valeur initiale de la duree d'une journee */
  public String dureeJournee = dureeJourneeApp;

  public PlanningCalcul(Planning aPlanning) {
  	super(aPlanning);
  }

  /**
   * @see I_ClasseMetierNotificationParametre
   * @param parametre
   */
  public static void initStaticField(
  		Parametre parametre) {
  	if (parametre == Parametre.PARAM_DUREE_JOUR_CONVERSION) {
  		dureeJourneeApp = parametre.getParamValueString();
  	} 
  }
  
  public String dureeJour() {
  	return dureeJournee;
  }

  public void setDureeJour(String string) {
  	dureeJournee = string;
   if (StringCtrl.isEmpty(dureeJournee))
  	 dureeJournee = dureeJourneeApp;
 	}

  /**
   * 
   */
  private EOAffectationAnnuelle affectationAnnuelle() {
  	return planning().affectationAnnuelle();
  }
  
  /**
   * 
   */
  private String type() {
  	return planning().type();
  }
  
  /**
   * Reliquat initiaux pour l'annee. Si cette valeur est renseignee
   * explicitement (<code>minutesReliquatInitial<code> de l'entite
   * <code>CalculAffectationAnnuelle</code>), alors c'est cette valeur,
   * sinon c'est la valeur <code>minutesRestantes<code> de la meme
   * entite, pour l'annee universitaire precedente.
   */
  public int reliquatInitialEnMinutes() {
  	return affectationAnnuelle().reliquatInitialEnMinutes();
  } 
  
  public String reliquatInitial() {
    return DateCtrlConges.to_duree(reliquatInitialEnMinutes());
  }

  public String reliquatInitialEnJours() {
    return TimeCtrl.to_duree_en_jours(reliquatInitialEnMinutes(), dureeJour());
  }

  /**
   * methodes de recuperation du total restant : reliquat + conges restants
   */
  public int totalRestantEnMinutes() {
    return reliquatRestantEnMinutes() + congesRestantsEnMinutes();
  }

  public String totalRestant() {
    return DateCtrlConges.to_duree(totalRestantEnMinutes());
  }

  public String totalRestantEnJours() {
    return TimeCtrl.to_duree_en_jours(totalRestantEnMinutes(), dureeJour());
  }

  /**
   * methodes de recuperation du malus des conges legaux
   * 
   * @return
   */

  public int decompteLegalEnMinutes() {
    return affectationAnnuelle().calculAffAnn(type()).minutesDecompteLegal().intValue();
  }

  public String decompteLegal() {
    return DateCtrlConges.to_duree(decompteLegalEnMinutes());
  }

  public String decompteLegalEnJours() {
    return TimeCtrl.to_duree_en_jours(decompteLegalEnMinutes(), dureeJour());
  }

  /**
   * methodes de recuperation du bilan Heures SUP / Conges compensateurs
   */
  public int bilanHSCCEnMinutes() {
    return affectationAnnuelle().calculAffAnn(type()).minutesBilan().intValue();
  }

  public String bilanHSCCEnHeures() {
    return DateCtrlConges.to_duree(bilanHSCCEnMinutes());
  }

  public String bilanHSCCEnJours() {
    return TimeCtrl.to_duree_en_jours(bilanHSCCEnMinutes(), dureeJour());
  }
  
  /**
   * methodes de recuperation de la regularisation (ex: une mutation)
   */
  public int regularistationEnMinutes() {
  	int regularistationEnMinutes = 0;
  	NSArray mouvementRegularisationSoldeConges = affectationAnnuelle().tosMouvementRegularisationSoldeConges();
  	if (mouvementRegularisationSoldeConges.count() > 0) {
  		regularistationEnMinutes = ((Number) mouvementRegularisationSoldeConges.valueForKey("@sum."+EOMouvement.MOUVEMENT_MINUTES_KEY)).intValue();
  	}
    return regularistationEnMinutes;
  }

  public String regularistationEnHeures() {
    return DateCtrlConges.to_duree(regularistationEnMinutes());
  }

  public String regularistationEnJours() {
    return TimeCtrl.to_duree_en_jours(regularistationEnMinutes(), dureeJour());
  }

  /**
   * methodes de recuperation de la quantite de rachat de RTT
   */
  public int jrtiEnMinutes() {
  	int jrtiEnMinutes = 0;
  	if (affectationAnnuelle().toMouvementJrti() != null) {
  		jrtiEnMinutes = affectationAnnuelle().toMouvementJrti().mouvementMinutes().intValue();
  	}
  	return jrtiEnMinutes;
  }

  public String jrtiEnHeures() {
    return DateCtrlConges.to_duree(jrtiEnMinutes());
  }

  public String jrtiEnJours() {
    return TimeCtrl.to_duree_en_jours(jrtiEnMinutes(), dureeJour());
  }

  
  /**
   * methodes de recuperation de la valeur des reliquats démandés pour
   * une épargne CET
   */
  public int cetDemandeEpargneEnMinutes() {
  	int demandeEpargneCetEnMinutes = 0;
  	if (affectationAnnuelle().toMouvementCetDemandeEpargne() != null) {
  		demandeEpargneCetEnMinutes = 
  			affectationAnnuelle().toMouvementCetDemandeEpargne().mouvementMinutes().intValue();
  	}
  	return demandeEpargneCetEnMinutes;
  }

  public String cetDemandeEpargneEnHeures() {
    return DateCtrlConges.to_duree(cetDemandeEpargneEnMinutes());
  }

  public String cetDemandeEpargneEnJours() {
    return TimeCtrl.to_duree_en_jours(cetDemandeEpargneEnMinutes(), dureeJour());
  }

  
  /**
   * methodes de recuperation de la valeur des reliquats bloqués APRES épargne CET
   */
  public int blocageReliquatsApresBasculeCetEnMinutes() {
  	int blocageReliquatsApresBasculeCetEnMinutes = 0;
  	if (affectationAnnuelle().toMouvementCetDecisionEpargne() != null) {
  		blocageReliquatsApresBasculeCetEnMinutes = 
  			affectationAnnuelle().toMouvementCetDecisionEpargne().mouvementMinutes().intValue();
  	}
  	return blocageReliquatsApresBasculeCetEnMinutes;
  }

  public String blocageReliquatsApresBasculeCetEnHeures() {
    return DateCtrlConges.to_duree(blocageReliquatsApresBasculeCetEnMinutes());
  }

  public String blocageReliquatsApresBasculeCetEnJours() {
    return TimeCtrl.to_duree_en_jours(blocageReliquatsApresBasculeCetEnMinutes(), dureeJour());
  }

  
  /**
   * La valeur du blocage CET a decompter sur le planning.
   * Correspond a la valeur finale, i.e. celle basculée par DRH.
   * Si cette étape n'est pas encore réalisée, alors on prend
   * la valeur de la quantité bloquée par l'agent.
   * @return
   */
  public int blocageReliquatsCetEnMinutes() {
  	int blocageReliquatsCetEnMinutes = 0;
  	if (affectationAnnuelle().toMouvementCetDecisionEpargne() != null) {
  		blocageReliquatsCetEnMinutes = blocageReliquatsApresBasculeCetEnMinutes();
  	} else {
  		blocageReliquatsCetEnMinutes = cetDemandeEpargneEnMinutes();
  	}
  	return blocageReliquatsCetEnMinutes;
  }

  public String blocageReliquatsCetEnHeures() {
    return DateCtrlConges.to_duree(blocageReliquatsCetEnMinutes());
  }

  public String blocageReliquatsCetEnJours() {
    return TimeCtrl.to_duree_en_jours(blocageReliquatsCetEnMinutes(), dureeJour());
  }
  
  
  /**
   * methodes de recuperation des droits a conges 
   * restants (issus de l'association des horaires)
   */
  public int congesRestantsEnMinutes() {
    int congesRestants = affectationAnnuelle().calculAffAnn(type()).minutesRestantes().intValue();
    /*if (reliquatInitialEnMinutes() < 0) {
      congesRestants += reliquatInitialEnMinutes();
    }*/
    // si la regulation est negative, il faut l'ajouter
    /*if (regularistationEnMinutes() < 0)
    	congesRestants += regularistationEnMinutes();*/
    return congesRestants;
  }

  public String congesRestants() {
    return DateCtrlConges.to_duree(congesRestantsEnMinutes());
  }

  public String congesRestantsEnJours() {
    return TimeCtrl.to_duree_en_jours(congesRestantsEnMinutes(), dureeJour());
  }
  
  /**
	 *
   */
  public int congesGlobalRestantsEnMinutes() {
    int congesGlobalRestants = affectationAnnuelle().calculAffAnn(type()).minutesRestantes().intValue();
    // si le reliquat est negatif, il faut l'ajouter au total initial
    if (reliquatInitialEnMinutes() < 0) {
      congesGlobalRestants += reliquatInitialEnMinutes();
    }
    return congesGlobalRestants;
  }

  public String congesGlobalRestants() {
    return DateCtrlConges.to_duree(congesGlobalRestantsEnMinutes());
  }

  public String congesGlobalRestantsEnJours() {
    return TimeCtrl.to_duree_en_jours(congesGlobalRestantsEnMinutes(), dureeJour());
  }

  public String congesGlobalRestantsEnJourA7h00() {
    return TimeCtrl.to_duree_en_jours(congesGlobalRestantsEnMinutes(), TimeCtrl.stringDureeToHeure(TimeCtrl.stringForMinutes(ConstsJour.DUREE_JOUR_7H00)));
  }
  
  
  /**
   * 
   */
  public int congesInitiauxSansDecompteEnMinutes() {
    int congesGlobalInitiaux = congesInitiauxEnMinutes();
    // si le reliquat est positif, on l'ajoute
    if (reliquatInitialEnMinutes() > 0)
      congesGlobalInitiaux += reliquatInitialEnMinutes();
    return congesGlobalInitiaux;
  }

  public String congesInitiauxSansDecompte() {
    return DateCtrlConges.to_duree(congesInitiauxSansDecompteEnMinutes());
  }

  public String congesInitiauxSansDecompteEnJours() {
    return TimeCtrl.to_duree_en_jours(congesInitiauxSansDecompteEnMinutes(), dureeJour());
  }

  /**
   * Methodes de recuperation des droits a conges initiaux total :
   * - association : heures travaillees - heures dues
   * - si reliquat negatif -> += reliquat 
   * - +regulation (minutesCongesSup)
   * - si decompte conge legal -> - decompte
   */
  public int congesInitiauxEnMinutes() {
    EOCalculAffectationAnnuelle compte = affectationAnnuelle().calculAffAnn(type());
    
    int minutesTravaillees = compte.minutesTravaillees().intValue();
    int minutesDues = compte.minutesDues().intValue();
    int minutesCongesSup = regularistationEnMinutes();
    int minutesReliquat = reliquatInitialEnMinutes();

    int congesInitiauxEnMinutes = minutesTravaillees - minutesDues + minutesCongesSup;
    
    if (minutesReliquat < 0)
      congesInitiauxEnMinutes += minutesReliquat;
    
    return congesInitiauxEnMinutes;
  }

  public String congesInitiauxEnHeures() {
    return DateCtrlConges.to_duree(congesInitiauxEnMinutes());
  }

  public String congesInitiauxEnJours() {
    return TimeCtrl.to_duree_en_jours(congesInitiauxEnMinutes(), dureeJour());
  }
  
  /**
   * reliquat de conges pour le CET
   */
  public int reliquatBasculePourCETEnMinutes(NSTimestamp dateOuvertureSaisieCET, NSTimestamp dateFermetureSaisieCET) {
    int reliquatBascule = affectationAnnuelle().calculAffAnn(type()).minutesReliquatRestantes().intValue();
    if (reliquatInitialEnMinutes() < 0) {
      reliquatBascule = 0;
    }
    
    // les reliquats basculables n'existent que pendant la date de la bascule
    if (DateCtrlConges.isBefore(DateCtrlConges.now(), dateOuvertureSaisieCET) ||
        DateCtrlConges.isAfter(DateCtrlConges.now(), dateFermetureSaisieCET)) {
      reliquatBascule = 0;
    }

    return reliquatBascule;
  }
  
  public String reliquatBasculePourCETEnJours(NSTimestamp dateOuvertureSaisieCET, NSTimestamp dateFermetureSaisieCET) {
    return TimeCtrl.to_duree_en_jours(reliquatBasculePourCETEnMinutes(
    		dateOuvertureSaisieCET, dateFermetureSaisieCET), dureeJour());
  }
  
  public String reliquatBasculePourCET(NSTimestamp dateOuvertureSaisieCET, NSTimestamp dateFermetureSaisieCET) {
    return DateCtrlConges.to_duree(reliquatBasculePourCETEnMinutes(
    		dateOuvertureSaisieCET, dateFermetureSaisieCET));
  }

  /**
   * consommation de reliquat
   */
  
  private int consommationReliquatEnMinutes;
  
  private int consommationReliquatEnMinutes() {
    return consommationReliquatEnMinutes;
  }
  
  public String consommationReliquatEnJours() {
    return TimeCtrl.to_duree_en_jours(consommationReliquatEnMinutes(), dureeJour());
  }
  
  public String consommationReliquat() {
    return DateCtrlConges.to_duree(consommationReliquatEnMinutes());
  }
  
  /**
   * consommation de conges
   */
  private int consommationCongesEnMinutes;
  
  public int consommationCongesEnMinutes() {
    return consommationCongesEnMinutes;
  }
  
  public String consommationCongesEnJours() {
    return TimeCtrl.to_duree_en_jours(consommationCongesEnMinutes(), dureeJour());
  }
  
  public String consommationConges() {
    return DateCtrlConges.to_duree(consommationCongesEnMinutes());
  }
  
  /**
   * methodes de recuperation des reliquat restants sur l'annee
   * - c'est la valeur du reliquat utilisable dans les cong�s et uniquement cela (pas de cet)
   */
  public int reliquatRestantEnMinutes() {
    int reliquatRestant = affectationAnnuelle().calculAffAnn(type()).minutesReliquatRestantes().intValue();
    if (reliquatInitialEnMinutes() < 0) {
      reliquatRestant = 0;
    }
    // si la date est > a la date max des reliquats -> 0
    if (DateCtrlConges.isAfter(DateCtrlConges.now(), affectationAnnuelle().dateFinReliquat())) {
      reliquatRestant = 0;
    }

    return reliquatRestant;
  }

  public String reliquatRestant() {
    return DateCtrlConges.to_duree(reliquatRestantEnMinutes());
  }

  public String reliquatRestantEnJours() {
    return TimeCtrl.to_duree_en_jours(reliquatRestantEnMinutes(), dureeJour());
  }

  public String reliquatRestantlEnJours7Heures() {
    return to_duree_en_jours_a_7_heures(reliquatRestantEnMinutes());
  }

  
  /**
   * methodes de recuperation des droits decharge syndicales initiaux
   */
  public int dechargeSyndicaleInitiauxEnMinutes() {
  	int dechargeSyndicaleInitiauxEnMinutes = 0;
  	if (affectationAnnuelle().toMouvementDechargeSyndicale() != null) {
  		dechargeSyndicaleInitiauxEnMinutes = affectationAnnuelle().toMouvementDechargeSyndicale().mouvementMinutes().intValue();
  	}
  	return dechargeSyndicaleInitiauxEnMinutes;
  }

  public String dechargeSyndicaleInitiaux() {
    return DateCtrlConges.to_duree(dechargeSyndicaleInitiauxEnMinutes());
  }

  public String dechargeSyndicaleInitiauxEnJours() {
    return TimeCtrl.to_duree_en_jours(dechargeSyndicaleInitiauxEnMinutes(), dureeJour());
  }

  public String dechargeSyndicaleInitiauxEnJours7Heures() {
    return to_duree_en_jours_a_7_heures(dechargeSyndicaleInitiauxEnMinutes());
  }

  /**
   * methodes de recuperation des droits decharge syndicales restants
   */
  public int dechargeSyndicaleRestantsEnMinutes() {
    return affectationAnnuelle().calculAffAnn(type()).minutesDechargeSyndicaleRestantes().intValue();
  }

  public String dechargeSyndicaleRestants() {
    return DateCtrlConges.to_duree(dechargeSyndicaleRestantsEnMinutes());
  }

  public String dechargeSyndicaleRestantsEnJours() {
    return TimeCtrl.to_duree_en_jours(dechargeSyndicaleRestantsEnMinutes(), dureeJour());
  }

  public String dechargeSyndicaleRestants7Heures() {
    return to_duree_en_jours_a_7_heures(dechargeSyndicaleRestantsEnMinutes());
  }
  
  /**
   * 
   * @param ec
   * @param affAnn
   * @return
   */
  public static int reliquatInitialEnMinutesForAffAnn(EOEditingContext ec, EOAffectationAnnuelle affAnn) {
    Number reliquat = null;
    if (affAnn.toMouvementReliquatsNonAutomatiques() != null) {
    	reliquat = affAnn.toMouvementReliquatsNonAutomatiques().mouvementMinutes();
    }
    // si pas de reliquat manuel, on fouille dans l'annee precedente
    StringBuffer theBuf = new StringBuffer();
    if (reliquat == null) {
      theBuf.append("auto value ... ");
      // si individu invalide ... bug dans la page des logs
      if (affAnn.individu() != null) {
      	reliquat = EOVIndividuConges.getPrevCongesRestants(ec, affAnn.individu().oid(), 
      			affAnn.structure().cStructure(), affAnn.dateDebutAnnee());
      }
      // si pas de reliquat, alors on retourne 0
      if (reliquat == null) {
        reliquat = new Integer(0);
        theBuf.append("no previous value");
      } else
        theBuf.append("using previous value");
    } else
      theBuf.append("manual reliquat");
    theBuf.append(" : ").append(TimeCtrl.stringForMinutes(reliquat.intValue()));
    LRLog.trace(theBuf.toString());
    return reliquat.intValue();
  }
  

  private static String to_duree_en_jours_a_7_heures(int minutes) {
    String to_duree = "";
    double lesJours = (double) minutes / (double) (TimeCtrl.getMinutes(TimeCtrl.stringDureeToHeure("7h00")));
    Double unNombre = new Double(lesJours);
    NSNumberFormatter numberFormat = new NSNumberFormatter("0.00");
    to_duree += numberFormat.format(unNombre) + "j";

    return to_duree;
  }

  /**
   * Nombre de jours travailles a donner aux impots 
	 * Periode 01/09 -> 31/12
   */
  public int nbJourTravaillesImpotsPremierePartie() {
  	return nbJourTravaillesImpotsForPartie(
  			DateCtrlConges.dateDebutAnnee(affectationAnnuelle().dateDebutAnnee()),
  			DateCtrlConges.dateToFinAnneeCivile(affectationAnnuelle().dateDebutAnnee()));
  }

  /**
   * Nombre de jours travailles a donner aux impots 
	 * Periode 01/01 -> 31/08
   */
  public int nbJourTravaillesImpotsDeuxiemePartie() {
  	return nbJourTravaillesImpotsForPartie(
  			DateCtrlConges.date1erJanAnneeUniv(affectationAnnuelle().dateDebutAnnee()),
  			DateCtrlConges.dateToFinAnneeUniv(affectationAnnuelle().dateDebutAnnee()));
  }
  
  /**
   * la formule du calcul des jours travailles a donner
   * aux impots.
   * 
   * @param dateDebut
   * @param dateFin
   * @return
   */
  public int nbJourTravaillesImpotsForPartie(NSTimestamp dateDebut, NSTimestamp dateFin) {
  	return CngCalculFactory.getNbJourWithDureeForStatut(
  			planning(), Jour.STATUS_IS_JOUR_TRAVAILLE_IMPOT, dateDebut, dateFin);
  }
  
  /**
   * Le nombre de jours de conges legaux
   */
  public int nbJourCongesLegal() {
  	return CngCalculFactory.getNbJourWithDureeForStatut(
  			planning(), Jour.STATUS_IS_CONGE_LEGAL_JOURNEE_COMPLETE, planning().affectationAnnuelle().dateDebutAnnee(), 
  			planning().affectationAnnuelle().dateFinAnnee());
  }

  /**
   * TODO ne fonctionne pas car les status IS_TRAVAILLE_AM et IS_TRAVAILLE_PM
   * ne sont pas renseignes ..........
   * Le nombre de demies journees travaillees entre 2 dates.
   * Sert par exemple pour les imprimes de garde d'enfant
   * @param dateDebut
   * @param dateFin
   * @return
   */
  public int nbDemiJourneesTravaillees(NSTimestamp dateDebut, NSTimestamp dateFin) {
  	int result = 0;
  	GregorianCalendar gcDebut = new GregorianCalendar();
  	gcDebut.setTime(dateDebut);
  	GregorianCalendar gcFin = new GregorianCalendar();
  	gcFin.setTime(dateFin);
  	// cas particulier si c'est le meme jour
  	if (DateCtrlConges.isSameDay(dateDebut, dateFin)) {
  		// meme demi journee
  		if (gcDebut.get(GregorianCalendar.AM_PM) == gcFin.get(GregorianCalendar.AM_PM)) {
  			if (gcDebut.get(GregorianCalendar.AM_PM) == GregorianCalendar.AM) {
    			result += CngCalculFactory.getNbJourWithDureeForStatut(
    					planning(), Jour.STATUS_IS_TRAVAILLE_AM, dateDebut, dateDebut);
  			} else {
    			result += CngCalculFactory.getNbJourWithDureeForStatut(
        			planning(), Jour.STATUS_IS_TRAVAILLE_PM, dateDebut, dateDebut);
  			}
  		} else {
  			// journee complete
  			result += CngCalculFactory.getNbJourWithDureeForStatut(
      			planning(), Jour.STATUS_IS_TRAVAILLE_AM, dateDebut, dateDebut);
    		result += CngCalculFactory.getNbJourWithDureeForStatut(
      			planning(), Jour.STATUS_IS_TRAVAILLE_PM, dateDebut, dateDebut);
  		}
  	} else {
  		// plusieurs jours
    	// calcul pour le premier jour
    	if (gcDebut.get(GregorianCalendar.AM_PM) == GregorianCalendar.PM) {
    		result += CngCalculFactory.getNbJourWithDureeForStatut(
      			planning(), Jour.STATUS_IS_TRAVAILLE_PM, dateDebut, dateDebut);
    	} else {
    		result += CngCalculFactory.getNbJourWithDureeForStatut(
      			planning(), Jour.STATUS_IS_TRAVAILLE_AM, dateDebut, dateDebut);
    		result += CngCalculFactory.getNbJourWithDureeForStatut(
      			planning(), Jour.STATUS_IS_TRAVAILLE_PM, dateDebut, dateDebut);
    	}
    	// calcul des jours intermediaires (que s'il ne sont pas communs au debut et a la fin)
    	NSTimestamp dateDebutJPlus1 = dateDebut.timestampByAddingGregorianUnits(0,0,1,0,0,0);
    	if (!DateCtrlConges.isSameDay(dateDebutJPlus1, dateFin)) {
    		NSTimestamp dateFinJMoins1 = dateFin.timestampByAddingGregorianUnits(0,0,-1,0,0,0);
    		result += CngCalculFactory.getNbJourWithDureeForStatut(
      			planning(), Jour.STATUS_IS_TRAVAILLE_AM, dateDebutJPlus1, dateFinJMoins1);
    		result += CngCalculFactory.getNbJourWithDureeForStatut(
      			planning(), Jour.STATUS_IS_TRAVAILLE_PM, dateDebutJPlus1, dateFinJMoins1);
    	}
    	// calcul pour le dernier jour
    	if (gcFin.get(GregorianCalendar.AM_PM) == GregorianCalendar.AM) {
    		result += CngCalculFactory.getNbJourWithDureeForStatut(
      			planning(), Jour.STATUS_IS_TRAVAILLE_AM, dateFin, dateFin);
    	} else {
    		result += CngCalculFactory.getNbJourWithDureeForStatut(
      			planning(), Jour.STATUS_IS_TRAVAILLE_AM, dateFin, dateFin);
    		result += CngCalculFactory.getNbJourWithDureeForStatut(
      			planning(), Jour.STATUS_IS_TRAVAILLE_PM, dateFin, dateFin);
    	}
  	}
  	return result;
  }
}
