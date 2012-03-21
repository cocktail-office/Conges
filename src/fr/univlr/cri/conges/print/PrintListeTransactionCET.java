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

import fr.univlr.cri.conges.eos.modele.conges.EOCETTransaction;
import fr.univlr.cri.util.LRXMLWriter;

/**
 * Liste des l'�dition en masse des transactions du CET 
 * 
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */
public class PrintListeTransactionCET extends GenericSixPrint {

	public PrintListeTransactionCET(
			EOEditingContext editingContext, 
			NSDictionary dico, CngPdfBoxCtrl ctrl) {
		super(editingContext, 
				ConstsPrint.PDF_CONGES_LISTE_TRANSACTION_CET, 
				ConstsPrint.ID_CONGES_LISTE_TRANSACTION_CET, 
				ConstsPrint.XML_CONGES_LISTE_TRANSACTION_CET,
				dico, ctrl);
	}

	protected void generateXml(LRXMLWriter w) throws IOException {
		w.setEscapeSpecChars(true);
		w.startDocument();
		w.writeComment("Edition de la liste de transactions CET");
		
		w.startElement("CongesListeCet"); 
		
		{
			w.writeElement("dateImpression", 	(String) dico().objectForKey("dateImpression"));
			w.writeElement("titre", 					(String) dico().objectForKey("titre"));
			NSArray recs = (NSArray) dico().objectForKey("recs");
					
			// la liste de toutes les demandes de cet
			w.startElement("liste");
			{
				for (int i = 0; i < recs.count(); i++) {
					EOCETTransaction rec = (EOCETTransaction) recs.objectAtIndex(i);
					w.startElement("transaction");
					w.writeElement("nom", rec.cet().individu().nomComplet());
					w.writeElement("solde", rec.soldeEnHeures());
					w.writeElement("debit", rec.debitEnHeures());
					w.writeElement("credit", rec.creditEnHeures());
					w.writeElement("motif", rec.motif(false) != null ? rec.motif(false) : "");
					w.endElement(); // transaction
				}
			}
			w.endElement(); //"liste"
			
			w.writeElement("total", Integer.toString(recs.count()));
		}    
		
		w.writeElement(ConstsPrint.XML_KEY_MAIN_LOGO_URL,	(String) dico().objectForKey(ConstsPrint.XML_KEY_MAIN_LOGO_URL));
				
		w.endElement(); // "CongesListeCet"
		w.endDocument();
		w.close();
	}

}
