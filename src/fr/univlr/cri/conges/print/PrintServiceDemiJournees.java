/*
 * Copyright Universit� de La Rochelle 2005
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
import java.util.Hashtable;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSTimestamp;
import com.webobjects.foundation.NSTimestampFormatter;

import fr.univlr.cri.conges.objects.Planning;
import fr.univlr.cri.util.LRXMLWriter;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.util.wo5.DateCtrl;

/**
 * Impression du planning de service sur 2 mois
 * 
 * @author Cyril TARADE <cyril.tarade at univ-lr.fr>
 */
public class PrintServiceDemiJournees extends GenericSixPrint {

	public PrintServiceDemiJournees(
			EOEditingContext editingContext, 
			NSDictionary dico, CngPdfBoxCtrl ctrl) {
		super(editingContext, 
				ConstsPrint.PDF_CONGES_SERVICE_DEMI_JOURN, 
				ConstsPrint.ID_CONGES_SERVICE_DEMI_JOURN, 
				ConstsPrint.XML_CONGES_SERVICE_DEMI_JOURN,
				dico, ctrl);
	}
	
	protected void generateXml(LRXMLWriter w) throws IOException {
		w.setEscapeSpecChars(true);
		w.startDocument();
		w.writeComment("Edition du planning du service par demi journees");
		
		w.startElement("planning");
		{
			w.writeElement("enCouleur", Boolean.toString(((Boolean)dico().objectForKey("enCouleur")).booleanValue()));
			w.startElement("entetePlanning");
			{
				w.writeElement("service", 		StringCtrl.replace((String) dico().objectForKey("leService"), "&", "et"));
				NSArray lesMois = (NSArray) dico().objectForKey("lesMois");
				NSArray lesJoursDesMois = (NSArray) dico().objectForKey("lesJoursDesMois");
				for (int i = 0; i < lesMois.count(); i++) {
					NSTimestamp unMois = (NSTimestamp) lesMois.objectAtIndex(i);
					NSArray lesJours = (NSArray) lesJoursDesMois.objectAtIndex(i);
					w.startElement("periode");
					{
						NSTimestampFormatter f = new NSTimestampFormatter("%B %Y");                                       
						w.writeElement("nomPeriode", f.format(unMois));
						w.writeElement("nbJour", Integer.toString(lesJours.count()));
						for (int j = 0; j < lesJours.count(); j++) {
							NSTimestamp unJourTS = (NSTimestamp) lesJours.objectAtIndex(j);
							NSTimestampFormatter f1 = new NSTimestampFormatter("%A");                                       
							NSTimestampFormatter f2 = new NSTimestampFormatter("%d");                                       
							String unJour = f1.format(unJourTS).substring(0,1).toUpperCase() + f2.format(unJourTS);
							w.writeElement("jour", unJour);
						}
					}
					w.endElement(); // "periode"
				}
			}
			w.endElement(); // "entetePlanning"
			
			// recup des couleurs
			NSArray lesCouleurs = (NSArray) dico().objectForKey("lesCouleurs");
			w.writeElement("couleurNonTravail",	(String)lesCouleurs.objectAtIndex(0));
			w.writeElement("couleurTravailR",	(String)lesCouleurs.objectAtIndex(1));
			w.writeElement("couleurAbsenceR",	(String)lesCouleurs.objectAtIndex(2));
			w.writeElement("couleurTravailP",	(String)lesCouleurs.objectAtIndex(3));
			w.writeElement("couleurAbsenceP",   (String)lesCouleurs.objectAtIndex(4));
			w.writeElement("couleurAbsenceREnCoursVal",   (String)lesCouleurs.objectAtIndex(5));
			
			NSArray lesIndividus = (NSArray) dico().objectForKey("lesIndividus");
			NSArray lesCouleursDesIndividus = (NSArray) dico().objectForKey("lesCodesCouleurs");
			for (int i = 0; i < lesIndividus.count(); i++) {
				String unIndividu = (String) lesIndividus.objectAtIndex(i);
				
				NSArray lesCouleursPEtR = (NSArray) lesCouleursDesIndividus.objectAtIndex(i);
				
				w.startElement("Individu");
				{
					w.writeElement("nom", unIndividu);
					NSArray lesMois = (NSArray) dico().objectForKey("lesMois");
					for (int j = 0; j < lesMois.count(); j++) {
						w.startElement("periode");
						{                                    
							NSArray lesCouleursDuMois = (NSArray) lesCouleursPEtR.objectAtIndex(j);
							for (int l = 0; l < Planning.TYPES_PLANNINGS_VISIBLES.count(); l++) {
								String typePlanning = (String) Planning.TYPES_PLANNINGS_VISIBLES.objectAtIndex(l);
								NSArray lesCodesCouleurs = (NSArray) lesCouleursDuMois.objectAtIndex(l);
								if (l == 0) {
									w.writeElement("nbJour", Integer.toString(lesCodesCouleurs.count()/2));
								}
								Hashtable dicoAmPm = new Hashtable();
								for (int k = 0; k < lesCodesCouleurs.count(); k++) {
									String unCodeCouleur =(String) lesCodesCouleurs.objectAtIndex(k);
									if (k % 2 == 1) {
										dicoAmPm.put("pm" + (((k-1)/2)+1), unCodeCouleur);
										//w.writeElement("h", dicoAmPm);
									}
									else {
										dicoAmPm.put("am" + ((k/2)+1), unCodeCouleur);
									}  
								}
								w.writeElement(typePlanning, dicoAmPm);
							} 
						}
						w.endElement(); // "periode
					}
				}
				w.endElement(); // "Individu"
			}
			
			w.writeElement("noteBasDePage", "Imprimé le " + DateCtrl.dateToString(DateCtrl.now()));
			
		}    
		
		w.writeElement(ConstsPrint.XML_KEY_MAIN_LOGO_URL,	(String) dico().objectForKey(ConstsPrint.XML_KEY_MAIN_LOGO_URL));
					
		w.endElement(); // "planning"
		w.endDocument();
		w.close();

	}
}
