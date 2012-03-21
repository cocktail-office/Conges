/*
 * Copyright Université de La Rochelle 2008
 *
 * ctarade@univ-lr.fr
 *
 * Ce logiciel est un programme informatique servant à gérer les conges
 * 
 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".

 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.

 * A cet égard  l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement,  à l'utilisation,  à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant 
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à 
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement, 
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité. 

 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez 
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 */
package fr.univlr.cri.conges.print;

import java.io.IOException;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSDictionary;

import fr.univlr.cri.util.LRXMLWriter;
/**
 * Impression de la demande de conges pour garde d'enfant par l'agent
 * 
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */
public class PrintDemandeGardeEnfant extends GenericSixPrint {
	
	public PrintDemandeGardeEnfant(EOEditingContext editingContext, 
			NSDictionary dico, CngPdfBoxCtrl ctrl) {
		super(editingContext, 
				ConstsPrint.PDF_CONGES_DEM_GARDE_ENFANT, 
				ConstsPrint.ID_CONGES_DEM_GARDE_ENFANT, 
				ConstsPrint.XML_CONGES_DEM_GARDE_ENFANT,
				dico, ctrl);
	}
	
	/**
	 * 
	 */
	protected void generateXml(LRXMLWriter w) throws IOException {
 		w.setEscapeSpecChars(true);
 		w.startDocument();
 		w.writeComment(ConstsPrint.XML_COMMENT_CONGES_DEM_CONGE_PAPIER);
 		
 		w.startElement(ConstsPrint.XML_ELEMENT_DEMANDE);
 		{
 			w.writeElement("dateImpression",             			(String) dico().objectForKey("dateImpression"));
 			w.writeElement("feminin",             						(String) dico().objectForKey("feminin"));
 			w.writeElement("prenomNomQualite",           		 	(String) dico().objectForKey("prenomNomQualite"));
 			w.writeElement("prenomNeLeDNaissance",      		 	(String) dico().objectForKey("prenomNeLeDNaissance"));
 			w.writeElement("dateDebutAMPM",             			(String) dico().objectForKey("dateDebutAMPM"));
 			w.writeElement("dateFinAMPM",            	 				(String) dico().objectForKey("dateFinAMPM"));
 			w.writeElement("nbDemiJourneesTravail",       		(String) dico().objectForKey("nbDemiJourneesTravail"));
 			w.writeElement("pluriel",             						(String) dico().objectForKey("pluriel"));
 			w.writeElement("motif",             							(String) dico().objectForKey("motif"));
 			w.writeElement("ville",             							(String) dico().objectForKey("ville"));
 			w.writeElement(ConstsPrint.XML_KEY_MAIN_LOGO_URL,	(String) dico().objectForKey(ConstsPrint.XML_KEY_MAIN_LOGO_URL));
 		}    
 		
 		w.endElement(); // "demande"
 		w.endDocument();
 		w.close();
 		
	}
    
    
    
}
