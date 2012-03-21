/*
 * Copyright Universit� de La Rochelle 2004
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
package fr.univlr.cri.conges.objects;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.conges.EOCETTransaction;
import fr.univlr.cri.conges.objects.occupations.Occupation;
import fr.univlr.cri.conges.utils.DateCtrlConges;

/**
 * @author ctarade
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JourReliquatCongesNode {

	// nom des methodes pour effectuer des fetch 
	public final static String DEBIT_DECHARGE_SYNDICALE_KEY 	= "debitDechargeSyndicale";
	public final static String DEBIT_RELIQUATS_KEY 						= "debitReliquats";
	public final static String DEBIT_CONGES_KEY 							= "debitConges";	
	public final static String DEBIT_CET_KEY 									= "debitCet";	
	public final static String MINUTES_JOUR_KEY 							= "minutesJour";	
	public final static String DATE_KEY												=	"date";

	//
  private Planning planning;
  private Occupation occupation;
  private Jour unJour;
  private int debitReliquats, debitConges, debitCet, debitDechargeSyndicale, minutesJour;

  /**
   * Constructeur pour une décompte sans occupation (exemple JRTI)
   * TODO les periodes de fermeture normales passent encore ici ...
   * @param unUnJour
   * @param unPlanning
   */
  public JourReliquatCongesNode(Jour unUnJour, Planning unPlanning) {
    super();
    unJour = unUnJour;
    planning = unPlanning;
  }
  
  /**
   * Constructeur pour une décompte avec occupation 
   * @param unUnJour
   * @param uneAffectationAnnuelle
   * @param uneOccupation
   */
  public JourReliquatCongesNode(Jour unUnJour, Planning unPlanning, Occupation uneOccupation) {
    super();
    unJour = unUnJour;
    planning = unPlanning;
    occupation = uneOccupation;
  }
  
  public int debitConges() {
     return debitConges;
  }

  public int debitReliquats() {
    return debitReliquats;
  }

  public int debitDechargeSyndicale() {
    return debitDechargeSyndicale;
  }

  public int debitCet() {
  	return debitCet;
  }
  
  
  public void setMinutesJour(int value) {
    minutesJour = value;
  }

  public int minutesJour() {
    return minutesJour;
  }
  
  
  public NSTimestamp date() {
  	return unJour.date();
  }

  /**
   * permet de repartir dans le bon reservoir de conges selon sa date et l'etat des reliquats
   * calcul des valeurs intermediaire de reliquat restant
   */
  public void confirmer(String typePlanning) {

  	int totalADebiter = minutesJour();

    if (isDechargeSyndicale()) {
    	
    	// pour les conges syndicaux, on utilise le reservoir decharge syndicale
    	int soldeDechargeSyndicale = planning.affectationAnnuelle().calculAffAnn(typePlanning).minutesDechargeSyndicaleRestantes().intValue();
    	int aDebiterDechargeSyndicale = totalADebiter;
    	if (soldeDechargeSyndicale < totalADebiter) {
    		aDebiterDechargeSyndicale = soldeDechargeSyndicale;
    	}
    	debitDechargeSyndicale = aDebiterDechargeSyndicale;
    	totalADebiter -= debitDechargeSyndicale;
    	planning.affectationAnnuelle().calculAffAnn(typePlanning).setMinutesDechargeSyndicaleRestantes(
    			new Integer(soldeDechargeSyndicale - debitDechargeSyndicale));

  		// baisser la valeur de l'occupation a conges de la quantite debitee en decharge syndicale
  		occupation.setLaValeur(occupation.laValeur()-debitDechargeSyndicale);
    }

    debiterReliquatConges(typePlanning, totalADebiter);
  }
  
  
  /**
   * Accepter : pour le CET, on décrémente d'abord le compte CET
   */
  public void accepter(String typePlanning) {
  	if (isAbsenceCET()) {
  		
   		int totalADebiter = minutesJour();

  		NSArray transactions = occupation.tosTransactionCet();
  		
  		// on essaye de debiter sur les transactions CET ayant des minutes restantes
  		int i = 0;
  		debitCet = 0;
  		while (totalADebiter > 0 && i < transactions.count()) {
  			EOCETTransaction transaction = (EOCETTransaction) transactions.objectAtIndex(i);
      	int aDebiterCet = totalADebiter;
      	if (transaction.minutesRestantes().intValue() < totalADebiter) {
      		aDebiterCet = transaction.minutesRestantes().intValue();
      	}
       	debitCet += aDebiterCet;
       	totalADebiter -= aDebiterCet;
      	transaction.debiter(aDebiterCet);
      	transaction.addDebitable((I_DebitableSurCET) occupation.recOccupation(), aDebiterCet);
      	i++;
  		}

  		// baisser la valeur de l'occupation a conges de la quantite debitee en cet
  		occupation.setLaValeur(occupation.laValeur()-debitCet);
  		// recrediter les droits a conges et reliquats
    	recrediter(typePlanning);
    	// les redebiter par rapport au restant (minutesJours-debitCet)
      debiterReliquatConges(typePlanning, totalADebiter);
    } 
  }


  /**
   * Effectuer le debit d'une quantit� sur les reliquats en priorit�
   * puis sur les cong�s
   * @param typePlanning
   * @param totalADebiter
   */
  private void debiterReliquatConges(String typePlanning, int totalADebiter) {
    // le restant sur les reliquats puis conges 
    int soldeCompteReliquat = planning.affectationAnnuelle().calculAffAnn(typePlanning).minutesReliquatRestantes().intValue();
    int soldeCompteCourant = planning.affectationAnnuelle().calculAffAnn(typePlanning).minutesRestantes().intValue();

    // avt la date de fin du reliquat ?
    if (soldeCompteReliquat > 0 && 
    		DateCtrlConges.isBeforeEq(unJour.date(), planning.affectationAnnuelle().dateFinReliquat())) {
      // decompte dans le reliquat si existe, sinon dans les conges
      soldeCompteReliquat -= totalADebiter;
      debitReliquats += totalADebiter;
      if (soldeCompteReliquat < 0) {
        debitConges -= soldeCompteReliquat;
        debitReliquats += soldeCompteReliquat;
        soldeCompteReliquat = 0;
      }
    } else {
      // decompte dans les conges
      debitConges += totalADebiter;
    }

    soldeCompteCourant -= debitConges;
    
    planning.affectationAnnuelle().calculAffAnn(typePlanning).setMinutesReliquatRestantes(new Integer(soldeCompteReliquat));
    planning.affectationAnnuelle().calculAffAnn(typePlanning).setMinutesRestantes(new Integer(soldeCompteCourant));
  }
  
  
  /**
   * accepter un conge DRH : recredit la valeur du debit de 
   * conges et de reliquats dans le solde
  */
  public void recrediter(String typePlanning) {
    int soldeCompteReliquat = planning.affectationAnnuelle().calculAffAnn(typePlanning).minutesReliquatRestantes().intValue();
    int soldeCompteCourant = planning.affectationAnnuelle().calculAffAnn(typePlanning).minutesRestantes().intValue();
    planning.affectationAnnuelle().calculAffAnn(typePlanning).setMinutesReliquatRestantes(new Integer(soldeCompteReliquat + debitReliquats));
    planning.affectationAnnuelle().calculAffAnn(typePlanning).setMinutesRestantes(new Integer(soldeCompteCourant + debitConges));
    setMinutesJour(0);
    debitConges = debitReliquats = 0;
  }

  /**
   * trouver un node d'apres le jour
   */
  public static JourReliquatCongesNode findNodeInArray(Jour jourATrouver, NSArray unArray) {
    JourReliquatCongesNode leNode = null;
    if (unArray != null && unArray.count() != 0) {
      for (int i = 0; i < unArray.count(); i++) {
        JourReliquatCongesNode unNode = (JourReliquatCongesNode) unArray.objectAtIndex(i);
        if (jourATrouver.equals(unNode.unJour)) {
          leNode = unNode;
          break;
        }
      }
    }
    return leNode;
  }
  
  
  // methodes interne
  
  private boolean isAbsenceCET() {
  	return occupation != null && occupation.isAbsenceCET();
  }
  
  private boolean isDechargeSyndicale() {
  	return occupation != null && occupation.isDechargeSyndicale();
  }

}
