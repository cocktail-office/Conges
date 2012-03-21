/*
 * Copyright Universit� de La Rochelle 2007
 *
 * Ce logiciel est un programme informatique servant � g�rer les conges.
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

import fr.univlr.cri.conges.utils.DateCtrlConges;

/**
 * Classe descriptive des jours de conges 
 * bloqué pour débit sur le planning :
 * - JRTI 
 * - blocage de reliquats pour bascule vers CET
 * 
 * @author ctarade
 */
public class DebitableSurCongesReliquats {
	// le planning concerne
	private Planning planning;
	// le nombre de minutes
	private int minutes;
	// le debit total en reliquats
	private int leDebitReliquats;
	// le debit total en conges
	private int leDebitConges;
	
	public DebitableSurCongesReliquats(Planning aPlanning, int aMinutes) {
		super();
		planning = aPlanning;
		minutes = aMinutes;
	}

	public void confirmer() {
		// on creer un node pour forcer la prise en compte des reliquats
		// on prend le premier jour du planning
		Jour firstJour = (Jour) planning.jours().objectAtIndex(0); 
		NSArray lesNodesJours = new NSArray();
		
		JourReliquatCongesNode unNode = new JourReliquatCongesNode(firstJour, planning);
		unNode.setMinutesJour(minutes);
		unNode.confirmer(planning.type());
		lesNodesJours = lesNodesJours.arrayByAddingObject(unNode);
	
	leDebitReliquats = ((Number) (lesNodesJours.valueForKeyPath("@sum."+JourReliquatCongesNode.DEBIT_RELIQUATS_KEY))).intValue();
		leDebitConges = ((Number) (lesNodesJours.valueForKeyPath("@sum."+JourReliquatCongesNode.DEBIT_CONGES_KEY))).intValue();
  }

  public int leDebitReliquats() {
    return leDebitReliquats;
  }

  public int leDebitConges() {
    return leDebitConges;
  }
  
  public String debit() {
  	return DateCtrlConges.to_duree(leDebitReliquats + leDebitConges);
  }
}
