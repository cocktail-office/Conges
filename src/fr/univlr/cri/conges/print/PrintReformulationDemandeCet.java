package fr.univlr.cri.conges.print;

import java.io.IOException;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

import fr.univlr.cri.util.LRXMLWriter;

/**
 * Edition de la re-formulation de la demande CET
 * 
 * @author ctarade
 *
 */
public class PrintReformulationDemandeCet 
	extends GenericSixPrint {

	
	public PrintReformulationDemandeCet(EOEditingContext editingContext, 
			NSDictionary dico, CngPdfBoxCtrl ctrl) {
		super(editingContext, 
				ConstsPrint.PDF_CONGES_REFORM_DEMANDE_CET,
				ConstsPrint.ID_CONGES_REFORM_DEMANDE_CET, 
				ConstsPrint.XML_CONGES_REFORM_DEMANDE_CET,
				dico, ctrl);
	}
	
	/**
	 * 
	 * @param anEcToNest
	 * @param resultFile
	 * @param maquetteId
	 * @param xmlFileName
	 * @param dico
	 * @param ctrl
	 */
	public PrintReformulationDemandeCet(EOEditingContext anEcToNest,
			String resultFile, String maquetteId, String xmlFileName,
			NSDictionary dico, CngPdfBoxCtrl ctrl) {
		super(anEcToNest, resultFile, maquetteId, xmlFileName, dico, ctrl);
	}

	/**
	 * 
	 * @param w
	 * @throws IOException
	 */
	protected void generateXml(LRXMLWriter w) 
		throws IOException {

		w.setEscapeSpecChars(true);
 		w.startDocument();
 		w.writeComment("Edition de la reformulation de la demande CET");
 		w.startElement("demande");
 		{
 			
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DATE_IMPRESSION);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DATE_DEMANDE);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_MAIN_LOGO_URL);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_GRHUM_ETAB);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_GRHUM_PRESIDENT);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_SIGNATURE_PRESIDENT);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_VILLE);

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
		
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DEMANDE_EPARGNE_EN_JOURS_7H00);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DECISION_EPARGNE_EN_JOURS_7H00);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_ANNEE_UNIV_N_M_1);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_IS_EXERCICE_DROIT_OPTION);
 		
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DECISION_TOTAL_OPTIONS_EN_JOURS_7H00);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DATE_VALEUR_CET);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_TITRE);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_DECISION_TOTAL_EN_JOURS_7H00);
 			writeElementSameKeyForPrintDico(w, ConstsPrint.XML_KEY_SOLDE_CET_APRES_DECISION_EPARGNE_EN_JOURS_7H00);
 				 		
 		}
 		w.endElement(); // "demande"
 		w.endDocument();
 		w.close();
		
	}

}
