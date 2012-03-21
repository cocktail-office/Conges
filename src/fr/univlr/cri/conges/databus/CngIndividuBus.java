package fr.univlr.cri.conges.databus;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.util.ArrayCtrl;
import fr.univlr.cri.ycrifwk.utils.UtilDb;

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
 * Classe permettant d'accerder aux donnees des individus.
 * 
 * @author Cyril TARADE <cyril.tarade at univ-lr.fr>
 */

public class CngIndividuBus extends CngDataBus {

  public CngIndividuBus(EOEditingContext editingContext) {
    super(editingContext);
  }

  /**
   * Determiner l'individu faisait partie du personnel non
   * enseignant durant la periode donnee.
   * 
   * @param individu
   * @param dateDebut
   * @param dateFin
   */
  public boolean isIndividuNonEnsWhilePeriode(EOIndividu individu, NSTimestamp dateDebut, NSTimestamp dateFin) {
    EOQualifier qual = newCondition(
        "(" +
        "(dDebut <= %@ AND dFin = nil) OR" +
        "(dDebut >= %@ AND dFin <= %@) OR" +
        "(dDebut <= %@ AND dFin >= %@) OR" +
        "(dDebut <= %@ AND dFin >= %@ AND dFin <= %@) OR" +
        "(dDebut >= %@ AND dDebut <= %@ AND dFin >= %@) OR" +
        "(dDebut >= %@ AND dDebut <= %@ AND dFin = nil)" +
        ") AND " +
        "noDossierPers = %@",    
        new NSArray(new Object[]{
            dateDebut, 
            dateDebut, dateFin, 
            dateDebut, dateFin, 
            dateDebut, dateDebut, dateFin,
            dateDebut, dateFin, dateFin,
            dateDebut, dateFin,
            individu.valueForKey("oid")
        }));
    return fetchArray("VPersonnelNonEns", qual, null).count() > 0;
  }
  
  /**
  * Donne la liste de 
  * @param ec
  * @return
  */
  public NSArray fetchAllIndividuNonEnsForDateRefInContext(
  		EOEditingContext ec, NSTimestamp dateDebut, NSTimestamp dateFin) {
	 String strQual = 
		 "(dDebut <= %@ AND dFin = nil) OR" +
		 "(dDebut >= %@ AND dFin <= %@) OR" +
		 "(dDebut <= %@ AND dFin >= %@) OR" +
		 "(dDebut <= %@ AND dFin >= %@ AND dFin <= %@) OR" +
		 "(dDebut >= %@ AND dDebut <= %@ AND dFin >= %@) OR" +
		 "(dDebut >= %@ AND dDebut <= %@ AND dFin = nil)" +
		 ") AND " +
		 "toIndividu.toVPersonnelNonEns.toIndividu.nomUsuel <> nil" ;   
	 NSArray args = new NSArray(new Object[]{
			 dateDebut, 
			 dateDebut, dateFin, 
			 dateDebut, dateFin, 
			 dateDebut, dateDebut, dateFin,
			 dateDebut, dateFin, dateFin,
			 dateDebut, dateFin});

	 EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(strQual, args);
	 NSArray arraySort = new NSArray( new Object[] {
			 EOSortOrdering.sortOrderingWithKey("nom", 		EOSortOrdering.CompareAscending),
			 EOSortOrdering.sortOrderingWithKey("prenom", 	EOSortOrdering.CompareAscending)
	 }
	 );
	 NSArray individus = (NSArray) ((NSArray) UtilDb.fetchArray(
			 ec, "VPersonnelNonEns", qual, null)).valueForKey("toIndividu");
  
	 // classement et suppression des doublons
	 individus = ArrayCtrl.removeDoublons(individus);
	 individus = EOSortOrdering.sortedArrayUsingKeyOrderArray(individus, arraySort);
     
   return individus;
 }
  
}
