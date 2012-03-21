/*
 * Copyright Consortium Coktail, 16 mai 07
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

package fr.univlr.cri.conges.print;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSNumberFormatter;

import fr.univlr.cri.conges.Application;
import fr.univlr.cri.print.LRPrinter;
import fr.univlr.cri.util.LRXMLWriter;
import fr.univlr.cri.webapp.CRIWebApplication;
import fr.univlr.cri.webapp.LRLog;

/**
 * La classe dont herite toutes les classes d'impressions SIX.
 * Cette methode herite de thread pour avoir une execution
 * non blocante de l'application globale.
 * 
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */

public abstract class GenericSixPrint {
	
	/** */
	private EOEditingContext ec;
	/** */
	private String resultFile;
	/** */
	private String maquetteId;
	/** */
	private String xmlFileName;
	/** */
	private NSDictionary dico;
	/** */
	private NSData resultData;
	/** */
	private CngPdfBoxCtrl ctrl;
	/** */
	private String endingMessage;

	/** Le buffer utiliser pour afficher les messages dans la console */
	private StringBuffer logBuffer;
	
	/**
	 * Constructeur que chaque classe doit surchager
	 * 
	 * @param anEcToNest : le EOEditingContext pour effectuer d'eventuels fetch
	 * 	a partir duquel on va creer un nested
	 * @param aResultFile : le chemin du fichier PDF genere
	 * @param aCtrl : le controleur d'impression associe
	 * @param aMaquetteId : l'id de la maquette SIX a utiliser
	 * @param aXmlFileName : le chemin du fichier XML genere
	 * @param aDico : les donnees parametres de cette impression
	 */
	public GenericSixPrint(
			EOEditingContext anEcToNest, String aResultFile, 
			String aMaquetteId, String aXmlFileName, 
			NSDictionary aDico, CngPdfBoxCtrl aCtrl) {
		super();
		ec = new EOEditingContext(anEcToNest);
		resultFile = aResultFile;
		maquetteId = aMaquetteId;
		xmlFileName = aXmlFileName;
		dico = aDico;
		ctrl = aCtrl;
		// le message de fin doit etre contenu dans le dictionnaire
		endingMessage = (String) dico.valueForKey(ConstsPrint.DICO_KEY_ENDING_MESSAGE);
		logBuffer = new StringBuffer();
	}
	
	/**
	 * Methode de creation du fichier XML qui sera passe
	 * au moteur SIX pour creation du PDF
	 */
	protected abstract void generateXml(LRXMLWriter w) throws IOException ;
	
	/**
	 * Effectue la generation du fichier XML et la 
	 * generation du fichier PDF associe. Retourne le
	 * flux PDF.
	 * @throws Exception 
	 */
	public NSData builtPdfData() {
		resultData = null;
 		ctrl.setEndingMessage(endingMessage);
		try { 
			long tStartXml = System.currentTimeMillis();
			LRXMLWriter lrXmlWriter = new LRXMLWriter(xmlFileName);
			lrXmlWriter.setEncoding("UTF-8");
			generateXml(lrXmlWriter);
			ctrl.setTimeXml(new Long(System.currentTimeMillis() - tStartXml));
			long tStartSix = System.currentTimeMillis();
			resultData = getPdfFromSixServer();
			ctrl.setTimeSix(new Long(System.currentTimeMillis() - tStartSix));
		}
		catch(Exception ex) {
  		ex.printStackTrace();
  		logBuffer.append(" Erreur d'ecriture d'un document XML :\n"+ex.getMessage());
  		resultData = null;
		} finally {
  		LRLog.log(logBuffer.toString());
  	}
		return resultData;
	}
	
  /** 
   * Retourne le flux pdf de l'edition une fois
   * generee par le moteur SIX
   */
  private NSData getPdfFromSixServer() {
    // On suppose que cette methode existe
    Hashtable params = new Hashtable();

    Application app = (Application) CRIWebApplication.application();
    
    params.put("XML_PRINTER_DRIVER",    app.config().stringForKey("XML_PRINTER_DRIVER"));
    params.put("SIX_SERVICE_HOST",      app.config().stringForKey("SIX_SERVICE_HOST"));
    params.put("SIX_SERVICE_PORT",      app.config().stringForKey("SIX_SERVICE_PORT"));
    params.put("SIX_USE_COMPRESSION",   app.config().stringForKey("SIX_USE_COMPRESSION"));

    logBuffer.append("generating SIX stream "+maquetteId);
    
    // On cree et on initialise le pilote d'impression
    LRPrinter printer = null;
    try {
    	printer = LRPrinter.newDefaultInstance(params);
    } catch (ClassNotFoundException e) {
    	// Le pilote n'a pas ete trouve
    	logBuffer.append(" >> Erreur : Pilote introuvable ").append(e.getMessage());
    	return null;
    }

    if (!printer.checkTemplate(maquetteId)) {
    	String errMessage = " >> Erreur : Maquette SIX \"" +maquetteId + "\" introuvable ";
    	// Le maquette n'a pas ete trouvee
    	ctrl.setErrSix(errMessage);
    	logBuffer.append(errMessage);
    	return null;
    }
    
    printer.printFileImmediate(maquetteId, xmlFileName, resultFile);
    
    // On verifie si l'operation est OK
    logBuffer.append(" - ");
    if (printer.hasSuccess()) {
    	logBuffer.append("OK");
    } else {
    	logBuffer.append("FAIL");
    	ctrl.setErrSix(printer.getMessage());
    }
    logBuffer.append(" - ").append(printer.getMessage());
     
    NSData datas = null;

    try {
    	datas = new NSData(new File(resultFile));
    } catch (Exception e) {
    	datas = null;
    }
    return datas;
  }

  /**
   * 
   */
	public EOEditingContext ec() {
		return ec;
	}

	/**
	 * 
	 */
	public NSDictionary dico() {
		return dico;
	}


  
  
	/**
	 * Ecrire dans le {@link LRXMLWriter} w la valeur du dictionnaire {@link #dico()}
	 * ayant pour la clé key avec la clé identique
	 * @param dico
	 * @param key
	 * @throws IOException 
	 */
	protected void writeElementSameKeyForPrintDico(LRXMLWriter w, String key) throws IOException {
		writeElement(w, key, dico().objectForKey(key));
	}

	/**
	 * 
	 * @param w
	 * @param key
	 * @param dico
	 * @throws IOException
	 */
	protected void writeElementSameKeyForDico(LRXMLWriter w, String key, NSDictionary dico) throws IOException {
		writeElement(w, key, dico.objectForKey(key));
	}
	
	/**
	 * 
	 * @param w
	 * @param key
	 * @param value
	 * @throws IOException
	 */
	protected void writeElement(LRXMLWriter w, String key, Object value) throws IOException {
	
		if (value != null) {
			String strObject = value.toString();
			
			if (value instanceof Float) {
				// pour les float, ce sont des jours à afficher avec 2 virgules
				NSNumberFormatter numberFormat = new NSNumberFormatter("0.00");
				strObject = numberFormat.format((Float) value); 
			} else if (value instanceof Integer) {
				// les integer doivent être affichés normalement
				strObject = ((Integer) value).toString();
			}
			
			w.writeElement(key, strObject);
			
		}
		
	}

}
