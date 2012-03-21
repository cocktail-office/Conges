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
package fr.univlr.cri.conges.print;

import java.io.IOException;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSComparator;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSTimestamp;
import com.webobjects.foundation.NSComparator.ComparisonException;

import fr.univlr.cri.conges.constantes.ConstsPlanning;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOHoraire;
import fr.univlr.cri.conges.eos.modele.planning.EOPlanningHebdomadaire;
import fr.univlr.cri.conges.objects.HoraireHebdomadaire;
import fr.univlr.cri.conges.objects.HoraireJournalier;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.util.ArrayCtrl;
import fr.univlr.cri.util.LRXMLWriter;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.CRIDataBus;

/**
 * Impression SIX des horaires
 * 
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */
public class PrintHoraires extends GenericSixPrint {
	
	public PrintHoraires(EOEditingContext editingContext, 
			NSDictionary dico, CngPdfBoxCtrl ctrl) {
		super(editingContext, 
				ConstsPrint.PDF_CONGES_HORAIRES, 
				ConstsPrint.ID_CONGES_HORAIRES, 
				ConstsPrint.XML_CONGES_HORAIRES,
				dico, ctrl);
	}
	
	/**
	 * 
	 */
	protected void generateXml(LRXMLWriter w) throws IOException {
		
		// une premiere boucle sur tous les plannings hebdo
		// pour construire des tableaux asso-semaine de
		// meme taille
		NSArray listDebutSemaine = new NSArray();
		NSArray listFinSemaine = new NSArray();
		
		// tous les plannings a imprimer
		NSArray listAffAnn = (NSArray) dico().valueForKey("listAffectationAnnuelle");
		
		// le type de planning. Si aucun, alors on estime que c'est le reel
		String typePlanning = (String) dico().valueForKey("typePlanning");
		if (StringCtrl.isEmpty(typePlanning)) {
			typePlanning = ConstsPlanning.REEL;
		}
		// le qualifier associe
		EOQualifier qualPlaHebType = null;
		if (typePlanning.equals(ConstsPlanning.REEL)) {
			qualPlaHebType = EOPlanningHebdomadaire.QUAL_REEL;
		} else if (typePlanning.equals(ConstsPlanning.PREV)) {
			qualPlaHebType = EOPlanningHebdomadaire.QUAL_PREV;
		} else if (typePlanning.equals(ConstsPlanning.TEST)) {
			qualPlaHebType = EOPlanningHebdomadaire.QUAL_TEST;
		} 
 		
		for (int i = 0; i < listAffAnn.count(); i++) {
			// pour chaque planning
			EOAffectationAnnuelle itemAffAnn = (EOAffectationAnnuelle) listAffAnn.objectAtIndex(i);
			
			// plannings avec horaires associes sur le reel
			NSArray listPlaHeb = ArrayCtrl.applatirArray(
					(NSArray) itemAffAnn.valueForKeyPath("horaires.planningHebdomadaires"));
			listPlaHeb = EOQualifier.filteredArrayWithQualifier(listPlaHeb, qualPlaHebType);

			// pour toutes les semaines
			for (int j = 0; j < listPlaHeb.count(); j++) {
				EOPlanningHebdomadaire itemPlaHeb = (EOPlanningHebdomadaire) listPlaHeb.objectAtIndex(j);
				if (!listDebutSemaine.containsObject(itemPlaHeb.dateDebutSemaine())) {
					listDebutSemaine = listDebutSemaine.arrayByAddingObject(itemPlaHeb.dateDebutSemaine());
					listFinSemaine = listFinSemaine.arrayByAddingObject(itemPlaHeb.dateFinSemaine());
				}
			}
		}
		
		// classement chronologique des tableaux
		try {
			listDebutSemaine = listDebutSemaine.sortedArrayUsingComparator(NSComparator.AscendingTimestampComparator);
			listFinSemaine = listFinSemaine.sortedArrayUsingComparator(NSComparator.AscendingTimestampComparator);
		} catch (ComparisonException e) {
			e.printStackTrace();
		}
		
		// construction de la liste des numeros de semaine
		NSArray listNoSemaine = new NSArray();
		for (int i = 0; i < listDebutSemaine.count(); i++) {
			NSTimestamp itemDebutSemaine = (NSTimestamp) listDebutSemaine.objectAtIndex(i);
			listNoSemaine = listNoSemaine.arrayByAddingObject(
					new Integer(DateCtrlConges.weekNumber(itemDebutSemaine)));
		}
		
 		w.setEscapeSpecChars(true);
 		w.startDocument();
 		w.writeComment("Edition des horaires hebdomadaires");
 		
 		w.startElement("agents");
 		{
 			w.writeElement("annee-univ",			(String) dico().objectForKey("anneeUniv"));
 			w.writeElement("service",   			(String) dico().objectForKey("service"));
 			w.writeElement("date-impression",	(String) dico().objectForKey("dateImpression"));

 			// tous les plannings
 			for (int i = 0; i < listAffAnn.count(); i++) {
 				// pour chaque planning
 				EOAffectationAnnuelle itemAffAnn = (EOAffectationAnnuelle) listAffAnn.objectAtIndex(i);
				
 				// pas d'horaires cr��s, on passe
 				if (ArrayCtrl.isEmpty(itemAffAnn.horaires())) {
 					continue;
 				}
 				
 				// verifier qu'au moins 1 semaine de ce type est associ�e a un horaire
 				NSArray listPlaHebType = ArrayCtrl.applatirArray(
 						(NSArray) itemAffAnn.valueForKeyPath("horaires.planningHebdomadaires"));
 				listPlaHebType = EOQualifier.filteredArrayWithQualifier(listPlaHebType, qualPlaHebType);
 				if (listPlaHebType.count() == 0) {
 					continue;
 				}
 				
 				w.startElement("agent");
 				{
 					w.writeElement("nom",   	itemAffAnn.individu().nomComplet());
 					
 					// les associations au semaines
 					NSArray listPlaHeb = new NSArray();
 					
 					// tous les horaires
 					w.startElement("horaires");	
 					{
 					
	 					NSArray listHor = itemAffAnn.horaires();
	 					for (int j = 0; j < listHor.count(); j++) {
	 						// pour chaque horaire
	 						EOHoraire itemHor = (EOHoraire) listHor.objectAtIndex(j);
	 						
	 						// la liste des asso
	 						NSArray listPlaHebHor = (NSArray) itemHor.valueForKey("planningHebdomadaires");
	 						listPlaHebHor = EOQualifier.filteredArrayWithQualifier(
	 								listPlaHebHor, qualPlaHebType);
	 						
	 						// l'horaire n'est affect� � aucune semaine, on passe
	 						if (listPlaHebHor.count() == 0)
	 							continue;
	 			
	 						listPlaHeb = listPlaHeb.arrayByAddingObjectsFromArray(listPlaHebHor);
	 						
	 	          HoraireHebdomadaire itemHoraireHebdo = new HoraireHebdomadaire();
	 	          itemHoraireHebdo.setHoraire(itemHor);
	 	          itemHoraireHebdo.recalculerTotaux();
	 	          
	 						w.startElement("horaire");
	 						{
	 							w.writeElement("libelle", StringCtrl.replace(itemHoraireHebdo.nom(), "&", "et"));
	 							w.writeElement("quotite",   
	 									itemHor.quotite() != null ? Integer.toString(itemHor.quotite().intValue()) : "TPA");
	 							w.writeElement("total", 		itemHoraireHebdo.getTotal());
	 							w.writeElement("bonus", 		itemHoraireHebdo.getTotalBonus());
	 							
	 							// tous les jours de la semaine
	 							NSArray listHorairesJournaliers = itemHor.horairesJournaliers();
	 							for (int k = 0; k < listHorairesJournaliers.count(); k++) {
	 								// pour chaque jour
	 								HoraireJournalier itemHoraireJourn = new HoraireJournalier(k, itemHoraireHebdo);
	 								w.startElement("jour");
	 								{
	 									w.writeElement("libelle", (String) EOHoraire.LIBELLES_JOURS.objectAtIndex(k));
	 									w.writeElement("debut-am", itemHoraireJourn.getHeureDebutAM());
	 									w.writeElement("fin-am", 	itemHoraireJourn.getHeureFinAM());
	 									w.writeElement("debut-pm", itemHoraireJourn.getHeureDebutPM());
	 									w.writeElement("fin-pm", 	itemHoraireJourn.getHeureFinPM());
	 									w.writeElement("total", 	itemHoraireJourn.getHoraireJournalierTotal());
	 									w.writeElement("pause", 	itemHoraireJourn.getHeureDebutPause());
	 									w.writeElement("bonus", 	itemHoraireJourn.getBonus());
	 								}
	 								w.endElement(); // jour
	 							}
	 						}
	 				 		w.endElement(); // "horaire"
	 					}
 					}
 					w.endElement(); // "horaires"
		
 					// le tableau des asso horaires-semaines
 					w.startElement("semaines");
 					for (int j = 0; j < listDebutSemaine.count(); j++) {
 						
 						NSTimestamp itemDebutSemaine 	= (NSTimestamp) listDebutSemaine.objectAtIndex(j);
 						NSTimestamp itemFinSemaine 		= (NSTimestamp) listFinSemaine.objectAtIndex(j);
 						
 						// retrouver le planning hebdo associe a ce debut de semaine
 						EOQualifier qualPlaHeb = CRIDataBus.newCondition(
 								"dateDebutSemaine = %@ and dateFinSemaine = %@", 
 								new NSArray(new NSTimestamp[]{itemDebutSemaine, itemFinSemaine}));
 						
 						NSArray recsPlaHeb = EOQualifier.filteredArrayWithQualifier(
 								listPlaHeb, qualPlaHeb);
 						
 						// si plusieurs plannings hebdo pour cette semaine, on 
 						// les faits tous (ex: changement de quotite en cours d'annee)
 						
 						for (int k = 0; k < recsPlaHeb.count(); k++) {
 							EOPlanningHebdomadaire recPlaHeb = (EOPlanningHebdomadaire) recsPlaHeb.objectAtIndex(k);
 				 			
 	 						// un ligne par semaine
 	 						w.startElement("semaine"); 
 	 						{
 	 							w.writeElement("debut", DateCtrlConges.dateToString(itemDebutSemaine));
 		 						w.writeElement("fin", 	DateCtrlConges.dateToString(itemFinSemaine));
 	 							w.writeElement("numero", Integer.toString(((Integer) listNoSemaine.objectAtIndex(j)).intValue()));
 		 						w.writeElement("horaire", (recPlaHeb != null ? recPlaHeb.horaire().nomQuotite() : ""));
 	 						}
 	 	 	 				w.endElement(); // "semaine"		
 						}
 					}
 	 				w.endElement(); // "semaines"
 	 			}
 				w.endElement(); // "agent" 				
 			}
 		}    

 		w.writeElement(ConstsPrint.XML_KEY_MAIN_LOGO_URL,	(String) dico().objectForKey(ConstsPrint.XML_KEY_MAIN_LOGO_URL));

 		w.endElement(); // "agents"
 		w.endDocument();
 		w.close();
 		
	}
    
    
    
}
