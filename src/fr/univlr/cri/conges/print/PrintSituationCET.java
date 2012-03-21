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

import fr.univlr.cri.util.ArrayCtrl;
import fr.univlr.cri.util.LRXMLWriter;

/**
 * Impression de la situation CET 
 * 
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */
public class PrintSituationCET extends GenericSixPrint {

	public PrintSituationCET(EOEditingContext ec, 
			NSDictionary dico, CngPdfBoxCtrl ctrl) {
		super(ec, 
				ConstsPrint.PDF_CONGES_SITUATION_CET, 
				ConstsPrint.ID_CONGES_SITUATION_CET, 
				ConstsPrint.XML_CONGES_SITUATION_CET, 
				dico, ctrl);
	}


	protected void generateXml(LRXMLWriter w) throws IOException {
		
		w.setEscapeSpecChars(true);
		w.startDocument();
		w.writeComment("Edition de la situation CET");
    
		w.startElement("situations");
		{
 			
 			// date de l'arret
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_SITUATION_DATE_ARRET);
	   
 			// logo
			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_MAIN_LOGO_URL);
			
			// adresse de l'établissement
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_ETAB_ADRESSE_LIBELLE);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_ETAB_ADRESSE_ADRESSE_1);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_ETAB_ADRESSE_ADRESSE_2);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_ETAB_ADRESSE_CP_VILLE);

			
			// on boucle sur autant de dicos qu'il y a de situations a sortir
			NSArray arrayDico = (NSArray) dico().objectForKey(ConstsPrint.SITUATION_ARRAY_DICO);
			
			for (int i=0; i<arrayDico.count(); i++) {
				
				NSDictionary dico = (NSDictionary) arrayDico.objectAtIndex(i);
				
				w.startElement("situation");
				{
					
					// identité
					writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_CIVILITE, dico);
					writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_NOM_DEMANDEUR, dico);
					writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_PRENOM_DEMANDEUR, dico);
					writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_GRADE_DEMANDEUR, dico);
	                 
	 				// ancien regime
					writeLignesCredit(w, false, dico);
	 				
					// régime pérenne
					writeLignesCredit(w, true, dico);
	 			
					// solde final 
					writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_SITUATION_SOLDE_FINAL_EN_JOURS_7H00, dico);
				}
				
				w.endElement(); // "situation"

			}
				
		}
		w.endElement(); // "situations"
		w.endDocument();
		w.close();
	}
	
	/**
	 *
	 * @param w
	 * @param isRegimePerenne
	 * @throws IOException 
	 */
	private void writeLignesCredit(LRXMLWriter w, boolean isRegimePerenne, NSDictionary dico) throws IOException {
		NSArray lignes = null;
		if (isRegimePerenne) {
			lignes = (NSArray) dico.objectForKey(ConstsPrint.DICO_KEY_SITUATION_ARRAY_LIGNE_CREDIT_REGIME_PERENNE);
		} else {
			lignes = (NSArray) dico.objectForKey(ConstsPrint.DICO_KEY_SITUATION_ARRAY_LIGNE_CREDIT_ANCIEN_REGIME);
		}
		if (!ArrayCtrl.isEmpty(lignes)) {
			String bloc = ConstsPrint.XML_KEY_SITUATION_BLOC_ANCIEN_REGIME;
			if (isRegimePerenne) {
				bloc = ConstsPrint.XML_KEY_SITUATION_BLOC_REGIME_PERENNE;
			}
			w.startElement(bloc);
			{
				for (int i=0; i<lignes.count(); i++) {
					NSDictionary dicoLigne = (NSDictionary) lignes.objectAtIndex(i);
					w.startElement(ConstsPrint.XML_KEY_SITUATION_TRANSACTION);
 					{
 						writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_SITUATION_TRANSACTION_LIBELLE, 																					dicoLigne);
 						writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_SITUATION_TRANSACTION_SOLDE_INITIAL_EN_JOURS_7H00,		dicoLigne);
 						writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_SITUATION_TRANSACTION_VALEUR_EN_JOURS_7H00, 								dicoLigne);
 						
 						NSDictionary dicoDebitableLigne = (NSDictionary) dicoLigne.valueForKey(ConstsPrint.DICO_KEY_SITUATION_DICO_DEBITABLES);
 						
 						writeDebitables(w, dicoDebitableLigne);
 					}
 					w.endElement(); // ConstsPrint.XML_KEY_SITUATION_TRANSACTION
				}
			}
			w.endElement(); // element
		}
	}
	
	/**
	 * Methode interne permettant d'alimenter le fichier XML avec une liste 
	 * de debitables (cette methode est appelée 2 fois)
	 * @param w
	 * @param dico
	 * @throws IOException 
	 */
	private void writeDebitables(LRXMLWriter w, NSDictionary dico) throws IOException {
		// les debitables
		NSArray arrayDicoDebitable =  (NSArray) dico.objectForKey(ConstsPrint.DICO_KEY_SITUATION_ARRAY_DEBITABLES);
		for (int i=0; i<arrayDicoDebitable.count(); i++) {
			NSDictionary dicoDebitable = (NSDictionary) arrayDicoDebitable.objectAtIndex(i);
			w.startElement(ConstsPrint.XML_KEY_SITUATION_LIGNE_DEBITABLE);
			{
				writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_SITUATION_DEBITABLE_LIBELLE, 																		dicoDebitable);
				writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_SITUATION_DEBITABLE_VALEUR_EN_JOURS_7H00, 					dicoDebitable);
				writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_SITUATION_DEBITABLE_SOLDE_FINAL_EN_JOURS_7H00,	dicoDebitable);
			}
			w.endElement(); // ConstsPrint.XML_KEY_SITUATION_LIGNE_DEBITABLE
		}
	}
    
}
