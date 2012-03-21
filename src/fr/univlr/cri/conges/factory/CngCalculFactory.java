package fr.univlr.cri.conges.factory;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.databus.CngDataBus;
import fr.univlr.cri.conges.eos.modele.planning.EOPeriodeAffectationAnnuelle;
import fr.univlr.cri.conges.objects.Planning;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.webapp.CRIDataBus;

/*
 * Copyright Consortium Coktail, 6 juin 07
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

/**
 * Classe rassemblant l'ensemble des calculs complexes
 * 
 * @author Cyril TARADE <cyril.tarade at univ-lr.fr>
 */
public class CngCalculFactory extends CngDataBus {

	/** le premier palier pour le declenchement de reduction de la duree travaillee */
	private final static int N_JOUR_PALIER_1 = 90;
	/** le second palier pour le declenchement de reduction de la duree travaillee */
	private final static int N_JOUR_PALIER_2 = 180;
	/** la duree fixe d un jour en minutes (ex conges legaux ...) -> 7h00 */
	private final static int N_MINUTES_JOUR = 420;
	
  public CngCalculFactory(EOEditingContext editingContext) {
    super(editingContext);
  }

  /**
   * A partir d'un certain nombre de conges legaux, le total
   * travaille par l'agent sur l'annee est reduit.
   * 
   * - si total jours légaux > 90j  : reduction de 10 jours a 7h00 (pour un 100%) 
   * - si total jours légaux > 180j : reduction de 20 jours a 7h00 (pour un 100%)
	 *
	 * @param planning : l'objet <code>Planning</code> concerne
	 *
   * @return le decompte en minutes.
   */
  public static int getMinutesDecompteLegal(Planning planning) {
  		
  	int totalJourCongesLegaux = getNbJourWithDureeForStatut(
  			planning, "isCongeLegalJourneeComplete", planning.affectationAnnuelle().dateDebutAnnee(), 
  			planning.affectationAnnuelle().dateFinAnnee());
  			
  	int minutesDecompteLegal = 0;
  	if (totalJourCongesLegaux > N_JOUR_PALIER_1) {
  		minutesDecompteLegal = 10 * N_MINUTES_JOUR;
      if (totalJourCongesLegaux > N_JOUR_PALIER_2) {
      	minutesDecompteLegal += 10 * N_MINUTES_JOUR;
      } 
  	}
  	
  	if (minutesDecompteLegal > 0) {
    	// on applique le prorata de(s) quotite(s) de travail 
    	// sur l'annee universitaire
  		double ratio = 0.0;
  		NSArray periodes = planning.affectationAnnuelle().periodes();
      for (int i = 0; i < periodes.count(); i++) {
      	EOPeriodeAffectationAnnuelle periode = (EOPeriodeAffectationAnnuelle) 
      		planning.affectationAnnuelle().periodes().objectAtIndex(i);
      	int nJours = getTotalJours360(periode.dateDebut(), periode.dateFin());
      	ratio += (((double)nJours)/360.0)*(periode.quotite().doubleValue()/100.0);
      }
  		minutesDecompteLegal = (int) ((double)minutesDecompteLegal*ratio);
  	}
  
  	return minutesDecompteLegal;
  }

  /**
   * TODO traiter le cas d'une periode unique a 1 jour qui tombe le 31 ...
   * 
	 * Retourne la duree d'une periode sur la base de
   * 1 mois = 30 jours
   * 1 an = 12 mois = 360 jours
   * 
   * @param dateDebut
   * @param dateDebut
   * 
   * @return le nombre de jours de cette periode sur cette base
   */
  public static int getTotalJours360(NSTimestamp dateDebut, NSTimestamp dateFin) {
		int dureePeriode = 0;
		NSTimestamp dateDebutMoisEnCours 	= DateCtrlConges.dateToDebutMois(dateDebut);
		NSTimestamp dateFinMoisEnCours 		= DateCtrlConges.dateToFinMois(dateDebut);
		while (DateCtrlConges.isBeforeEq(dateDebutMoisEnCours, dateFin)) {
			boolean moisComplet = true;
		    
			// recadrage du debut et de la fin sur la periode
			if (DateCtrlConges.isBefore(dateDebutMoisEnCours, dateDebut)) {
				dateDebutMoisEnCours = dateDebut;
				moisComplet = false;
			}
		  
			if (DateCtrlConges.isAfter(dateFinMoisEnCours, dateFin)) {
				dateFinMoisEnCours = dateFin;
				moisComplet = false;
			}

			// si le mois est complet, on compte 30 jours
			if (moisComplet) {
				dureePeriode += 30;
			} else {
				// +1 car de j à j+i, il y a i+1 jours complets
				dureePeriode += (dateFinMoisEnCours.getTime()-dateDebutMoisEnCours.getTime())/(1000*60*60*24) + 1;
			}
		        
			// avance au mois suivant
			dateDebutMoisEnCours 	= DateCtrlConges.dateToDebutMois(dateDebutMoisEnCours).timestampByAddingGregorianUnits(0,1,0,0,0,0);
			dateFinMoisEnCours 		= dateDebutMoisEnCours.timestampByAddingGregorianUnits(0,1,-1,0,0,0);
		}
  	return dureePeriode;
  }
  
  /**
   * Connaitre le total de jour ayant le statut <code>strStatut</code>.
   * 
   * Cette methode se base sur la nom d'une methode de la classe <code>Jour</code>.
   *  
   * @see Jour
   */
  public static int getNbJourWithDureeForStatut(Planning planning, String strStatut, 
  		NSTimestamp dateDebut, NSTimestamp dateFin) {
  	EOQualifier qual = CRIDataBus.newCondition(
  			strStatut + " = %@ AND date >= %@ AND date <= %@", 
  			new NSArray(new Object[] {
  					Boolean.TRUE, dateDebut, dateFin}));
  	return EOQualifier.filteredArrayWithQualifier(planning.jours(), qual).count();
  }
}
