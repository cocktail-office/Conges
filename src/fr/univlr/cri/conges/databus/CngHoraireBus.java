package fr.univlr.cri.conges.databus;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.constantes.ConstsHoraire;
import fr.univlr.cri.conges.eos.modele.planning.EOHoraire;
import fr.univlr.cri.conges.eos.modele.planning.EOTypeHoraire;
import fr.univlr.cri.conges.objects.HoraireHebdomadaire;
import fr.univlr.cri.conges.objects.Planning;

/*
 * Copyright Universit� de La Rochelle 2006
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

/**
 * Bus de gestion des horaires
 * 
 * @author Cyril TARADE <cyril.tarade at univ-lr.fr>
 */

public class CngHoraireBus extends CngDataBus {

  public CngHoraireBus(EOEditingContext editingContext) {
    super(editingContext);
  }

  /**
   * Liste de tous les horaires associes a un horaire type
   * @param ec
   * @param typeHoraire
   * @return
   */
  public NSArray horairesForHoraireType(EOEditingContext ec, EOTypeHoraire typeHoraire) {
    return fetchArray(ec, "Horaire", newCondition("typeHoraire = %@", new NSArray(typeHoraire)), null);
  }

  /**
   * Copier un objet <code>EOHoraire</code> vers un planning.
   * 
   * @param planning
   * @param prevHoraire
   * @param libelle
   * @return
   */
  public static EOHoraire dupliqueHoraire(Planning planning, EOHoraire prevHoraire, String libelle) {
    EOHoraire newHoraire = new EOHoraire();
    newHoraire.addObjectToBothSidesOfRelationshipWithKey(planning.affectationAnnuelle(), "affectationAnnuelle");
    newHoraire.insertInEditingContext(planning.affectationAnnuelle().editingContext());
    newHoraire.setHoraires(prevHoraire.horaires());
    newHoraire.setPauses(prevHoraire.pauses());
    newHoraire.setDureesAM(prevHoraire.dureesAM());
    newHoraire.setDureesPM(prevHoraire.dureesPM());
    // on limite la taille du libelle
    if (libelle != null && libelle.length() > ConstsHoraire.HORAIRE_LIBELLE_MAX_SIZE) {
    	libelle = libelle.substring(0, ConstsHoraire.HORAIRE_LIBELLE_MAX_SIZE-4) + " ...";
    }
    newHoraire.setNom(libelle);
    newHoraire.setDurees(prevHoraire.durees());
    newHoraire.setQuotite(prevHoraire.quotite());
    newHoraire.setCouleur(prevHoraire.couleur());
    
    // ajout a la classe metier
    HoraireHebdomadaire horaireHebdomadaire = new HoraireHebdomadaire();
    horaireHebdomadaire.setHoraire(newHoraire);
    horaireHebdomadaire.recalculerTotaux();
    planning.addHoraireHebdomadaire(horaireHebdomadaire); 	
    
    return newHoraire;
  }
}
