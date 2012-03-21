/*
 * Copyright Université de La Rochelle 2006
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
package fr.univlr.cri.conges.objects.occupations;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.planning.EOOccupation;
import fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation;
import fr.univlr.cri.conges.objects.JourReliquatCongesNode;
import fr.univlr.cri.conges.objects.Planning;

public class CongespourCet extends Occupation  {

  public final static String LIBELLE_COURT = "C_CET";

  public CongespourCet(EOTypeOccupation unType, Planning unPlanning, NSTimestamp debutTS, NSTimestamp finTS, String unMotif, EOEditingContext ec) {
    super(unType, unPlanning, debutTS, finTS, unMotif, ec);
  }

  public CongespourCet(EOOccupation uneOccupation, Planning unPlanning, EOEditingContext ec) {
    super(uneOccupation, unPlanning, ec);
  }

  public CongespourCet(EOOccupation uneOccupation, EOEditingContext ec) {
    super(uneOccupation, ec);
  }
  
  /**
   * 
   */
  public boolean isValide() {
    boolean isValide = super.isValide();
/*
    // 5 jours ouvrables minimun
    if (isValide) {
      NSArray lesJoursOccupes = lePlanning.lesJours(dateDebut(), dateFin());
      int nbJourOuvrable = 0;

      for (int i = 0; i < lesJoursOccupes.count(); i++) {
        Jour unJour = (Jour) lesJoursOccupes.objectAtIndex(i);
        if (unJour.dureeTravailleeEnMinutes() > 0) {
          nbJourOuvrable++;
        }
      }

      if (nbJourOuvrable < 5) {
        isValide = false;
        setErrorMsg("La durée d'un conges CET ne peut-être inférieure à 5 jours ouvrables.");
      }
    }

    // la duree ne doit pas depasse le total du CET restant
    /*if (isValide) {
      calculerValeur();
      if (laValeur() > lePlanning.toCET().minutesRestantes().intValue()) {
        isValide = false;
        setErrorMsg("La durée de ce congé dépasse votre crédit de CET (" +
            TimeCtrl.stringForMinutes(laValeur()) + " > " + 
            TimeCtrl.stringForMinutes(lePlanning.toCET().minutesRestantes()) + ")"
        );
      }
      
    }*/
    
    
    return isValide;
  }
  
  /**
   * 
   */
  public void calculerValeur() {
  	super.calculerValeur();
  }

  
  /**
   * Cas particulier pour le CET, lors de l'acceptation,
   * le droit a congé n'est pas recredité, c'est juste 
   * un décalage des debits vers le CET (tout se fait dans
   * la methode {@link JourReliquatCongesNode#accepter(String)}
   */
  public void accepter() {
   	for (int i = 0; i < lesNodesJours().count(); i++) {
      JourReliquatCongesNode unNode = (JourReliquatCongesNode) lesNodesJours().objectAtIndex(i);
      unNode.accepter(lePlanning.type());
    }
    // on remet a jour les valeurs de debit reliquats / conges et cet qui ont forcement
   	// change entre la confirmation et l'acceptation
    setLeDebitReliquats(((Number) (lesNodesJours().valueForKeyPath("@sum."+JourReliquatCongesNode.DEBIT_RELIQUATS_KEY))).intValue());
    setLeDebitConges(((Number) (lesNodesJours().valueForKeyPath("@sum."+JourReliquatCongesNode.DEBIT_CONGES_KEY))).intValue());
    setLeDebitCET(((Number) (lesNodesJours().valueForKeyPath("@sum."+JourReliquatCongesNode.DEBIT_CET_KEY))).intValue());

  }
}
