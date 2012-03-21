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
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

import fr.univlr.cri.util.LRXMLWriter;
/**
 * Impression de la demande de CET par l'agent
 * 
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */
public class PrintDemandeCET extends GenericSixPrint {
	
	public PrintDemandeCET(EOEditingContext editingContext, 
			NSDictionary dico, CngPdfBoxCtrl ctrl) {
		super(editingContext, 
				ConstsPrint.PDF_CONGES_DEMANDE_CET, 
				ConstsPrint.ID_CONGES_DEMANDE_CET, 
				ConstsPrint.XML_CONGES_DEMANDE_CET,
				dico, ctrl);
	}
	
	/**
	 * 
	 */
	protected void generateXml(LRXMLWriter w) throws IOException {
 		w.setEscapeSpecChars(true);
 		w.startDocument();
 		w.writeComment("Edition de la demande de bascule de CET");
 		
 		w.startElement("demande");
 		{
 			
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_MAIN_LOGO_URL);
 		
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_NOM_DEMANDEUR);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_PRENOM_DEMANDEUR);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_CIVILITE);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_QUALITE);
 			
 			NSArray affectationLabelList = (NSArray) dico().objectForKey(ConstsPrint.XML_KEY_AFFECTATION_LISTE);
 			String strAffectationLabel = "";
 			for (int i=0; i<affectationLabelList.count(); i++) {
 				strAffectationLabel += (String) affectationLabelList.objectAtIndex(i);
 				if (i < affectationLabelList.count() - 1) {
 					strAffectationLabel += ", ";
 				}
 			}
 			w.writeElement(ConstsPrint.XML_KEY_AFFECTATION_LISTE, strAffectationLabel);

 			
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_GRADE_DEMANDEUR);
 			
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_ETAB_ADRESSE_LIBELLE);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_ETAB_ADRESSE_ADRESSE_1);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_ETAB_ADRESSE_ADRESSE_2);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_ETAB_ADRESSE_CP_VILLE);
 			
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_ANNEE_CIVILE_N);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_ANNEE_UNIV_N_M_1);
 	 		
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_IS_MAINTIEN_ANCIEN_CET);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_IS_RENONCEMENT);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_IS_EPARGNE);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_IS_EXERCICE_DROIT_OPTION);

 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DEMANDE_TOTAL_OPTIONS_EN_JOURS_7H00_ANCIEN_SYSTEME);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DEMANDE_TRANSFERT_RAFP_EN_JOURS_7H00_ANCIEN_SYSTEME);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DEMANDE_INDEMNISATION_EN_JOURS_7H00_ANCIEN_SYSTEME);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DEMANDE_MAINTIEN_CET_EN_JOURS_7H00_ANCIEN_SYSTEME);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DEMANDE_TOTAL_OPTIONS_EN_HEURES_ANCIEN_SYSTEME);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DEMANDE_TRANSFERT_RAFP_EN_HEURES_ANCIEN_SYSTEME);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DEMANDE_INDEMNISATION_EN_HEURES_ANCIEN_SYSTEME);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DEMANDE_MAINTIEN_CET_EN_HEURES_ANCIEN_SYSTEME);
 	
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_SOLDE_CET_AVANT_DEMANDE_EN_JOURS_7H00);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_TOTAL_EPARGNE_DEMANDEE_EN_JOURS_7H00);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DROIT_A_CONGES_N_M_1_EN_JOURS_7H00);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DROIT_A_CONGES_N_M_1_EN_HEURES);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_CONSOMMATION_CONGES_N_M_1_EN_JOURS_7H00);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_CONSOMMATION_CONGES_N_M_1_EN_HEURES);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_RELIQUATS_N_M_1_EN_JOURS_7H00);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_RELIQUATS_N_M_1_EN_HEURES);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DEMANDE_EPARGNE_EN_JOURS_7H00);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DEMANDE_EPARGNE_EN_HEURES);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_TRANSFERT_EN_JOURS_7H00);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_TRANSFERT_EN_HEURES);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_SOLDE_CET_APRES_DEMANDE_EN_JOURS_7H00);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_SOLDE_CET_APRES_DEMANDE_EN_HEURES);

 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DATE_ANNEE_CIVILE_N);
 			
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DEMANDE_TOTAL_OPTIONS_EN_JOURS_7H00);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DEMANDE_TRANSFERT_RAFP_EN_JOURS_7H00);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DEMANDE_INDEMNISATION_EN_JOURS_7H00);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DEMANDE_MAINTIEN_CET_EN_JOURS_7H00);
 			
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_SOLDE_CET_APRES_EPARGNE_ET_DECISION_EN_JOURS_7H00);
 			
 	 	 		
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_MAIN_LOGO_URL);
 					 
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_VILLE);

 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DATE_IMPRESSION);
 			
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_TITRE);
 			
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_SUFFIXE_PHRASE_ACCORD_PRESIDENT);
 			
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_SUFFIXE_PHRASE_SOLDE_CET_FINAL);
 		 	
 		}    
 		
 		w.endElement(); // "demande"
 		w.endDocument();
 		w.close();
 		
	}
 
}
