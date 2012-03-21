/*
 * Copyright Universit� de La Rochelle 2008
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
 * L'edition papier d'une demande de conge
 * 
 * @author ctarade
 */
public class PrintDemandeCongePapier extends GenericSixPrint {
	
	public PrintDemandeCongePapier(EOEditingContext editingContext, 
			NSDictionary dico, CngPdfBoxCtrl ctrl) {
		super(editingContext, 
				ConstsPrint.PDF_CONGES_DEM_CONGE_PAPIER, 
				ConstsPrint.ID_CONGES_DEM_CONGE_PAPIER, 
				ConstsPrint.XML_CONGES_DEM_CONGE_PAPIER,
				dico, ctrl);
	}
	
	protected void generateXml(LRXMLWriter w) throws IOException {
 		w.setEscapeSpecChars(true);
 		w.startDocument();
 		w.writeComment(ConstsPrint.XML_COMMENT_CONGES_DEM_CONGE_PAPIER);
 		
 		// on boucle sur autant de dicos qu'il y a de demande a sortir
		NSArray arrayDico = (NSArray) dico().objectForKey(ConstsPrint.DEMANDE_ARRAY_DICO);
 		
		w.startElement(ConstsPrint.XML_ELEMENT_DEMANDES);
 		{
			for (int j=0; j<arrayDico.count(); j++) {
				
				NSDictionary dico = (NSDictionary) arrayDico.objectAtIndex(j);
				
		 		w.startElement(ConstsPrint.XML_ELEMENT_DEMANDE);
		 		{
		 			writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_DATE_IMPRESSION, dico);
		 			writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_SERVICE_LIBELLE_COURT, dico);
		 			writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_SERVICE_LIBELLE_LONG, dico);
		 			writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_NOM_DEMANDEUR, dico);
		 			writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_PRENOM_DEMANDEUR, dico);
		 			writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_GRADE_DEMANDEUR, dico);
		 			writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_CONGES_RESTANTS_AVANT_DEMANDE_EN_JOURS, dico);
		 			writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_CONGES_RESTANTS_APRES_DEMANDE_EN_JOURS, dico);
		 			writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_CONGES_RESTANTS_AVANT_DEMANDE_EN_HEURES, dico);
		 			writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_CONGES_RESTANTS_APRES_DEMANDE_EN_HEURES, dico);
		 			writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_DUREE_JOURNEE, dico);
		 			writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_DUREE_OCCUPATION, dico);
		 			writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_DATE_DEBUT_OCCUPATION, dico);
		 			writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_DATE_FIN_OCCUPATION, dico);
		 			writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_TYPE_OCCUPATION, dico);
		 			writeElementSameKeyForDico(w, ConstsPrint.XML_KEY_MOTIF_OCCUPATION, dico);
		 				
		 			NSArray viseurs = (NSArray) dico.objectForKey(ConstsPrint.XML_KEY_VISEURS);
		 			w.startElement(ConstsPrint.XML_KEY_VISEURS);
		 			for (int i=0; i<viseurs.count(); i++) {
		 				w.writeElement(ConstsPrint.XML_KEY_VISEUR, (String) viseurs.objectAtIndex(i));
		 			}
		 			w.endElement();
		
		 			NSArray responsables = (NSArray) dico.objectForKey(ConstsPrint.XML_KEY_RESPONSABLES);
		 			w.startElement(ConstsPrint.XML_KEY_RESPONSABLES);
		 			for (int i=0; i<responsables.count(); i++) {
		 				w.writeElement(ConstsPrint.XML_KEY_RESPONSABLE, (String) responsables.objectAtIndex(i));
		 			}
		
		 			w.endElement();
		 		}    
		 		w.endElement(); // "demande"
			}
	
	 		w.writeElement(ConstsPrint.XML_KEY_MAIN_LOGO_URL,	(String) dico().objectForKey(ConstsPrint.XML_KEY_MAIN_LOGO_URL));
 		}
 		w.endElement(); // "demandes"
 		w.endDocument();
 		w.close();
	}

}
