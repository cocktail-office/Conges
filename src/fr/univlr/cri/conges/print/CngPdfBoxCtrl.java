package fr.univlr.cri.conges.print;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;

import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.LRLog;


/*
 * Copyright Consortium Coktail, 23 mai 07
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
 * Classe de controle d'affichage d'une edition SIX
 * dans un WOComponent
 */
public abstract class CngPdfBoxCtrl {
	
	/** la classe a instancier pour l'impression */
	private Class genericSixPrintClass;
	
	/** */
	private EOEditingContext ec;
	
	/** les temps de realisation de toutes les operations */
	private Number timeDico, timeXml, timeSix;
	
	/** Le debut d'execution */
	private Number timeStart;
	
	/** messages d'erreur */
	private String errDico, errXml, errSix;

	/** message a afficher des que l'impression est terminee */
	private String endingMessage;
	
	public CngPdfBoxCtrl(Class aGenericSixPrintClass, EOEditingContext anEc) {
		super();
		genericSixPrintClass = aGenericSixPrintClass;
		ec = anEc;
		timeDico = timeXml = timeSix = null;
		errDico = errXml = errSix = null;
	}
	
	/** 
	 * Creation du dico de donnees consomme
	 * par la classe GenericSixPrint
	 * Classe a surcharger.
	 */
	public abstract NSDictionary buildDico();

	/**
	 * Le nom du fichier PDF (sans l'extension)
	 */
	public abstract String fileName();
	
	/**
	 * Prefixer le {@link #fileName()} par la date du jour
	 * @return
	 */
	public String dateFileName() {
		return DateCtrlConges.dateToString(DateCtrlConges.now(), "%Y%m%d_") + fileName();
	}
	
	/** 
	 * Methode de generation du flux PDF
	 * a partir de la classe <code>genericSixPrintClass</code>
	 */
	public NSData generatePdfData() throws 
		SecurityException, NoSuchMethodException, IllegalArgumentException, 
		InstantiationException, IllegalAccessException, InvocationTargetException {
		Constructor constructor = genericSixPrintClass.getConstructor (
				new Class [] {EOEditingContext.class, NSDictionary.class, CngPdfBoxCtrl.class});
	
		setTimeStart();
		long tStartDico = System.currentTimeMillis();
		NSDictionary dico = buildDico();
		setTimeDico(new Long(System.currentTimeMillis() - tStartDico));
		
		GenericSixPrint print = (GenericSixPrint)constructor.newInstance(
				new Object[]{ec, dico, this});
		return print.builtPdfData();
	}

	public Number getTimeDico() {
		return timeDico;
	}

	public void setTimeDico(Number timeDico) {
		this.timeDico = timeDico;
		LRLog.log("#"+hashCode() + " - building dictionary    : " + timeDico.intValue() + "ms");
	}

	public Number getTimeSix() {
		return timeSix;
	}

	public void setTimeSix(Number timeSix) {
		this.timeSix = timeSix;
		LRLog.log("#"+hashCode() + " - SIX server conversion  : " + timeSix.intValue() + "ms");
	}

	public Number getTimeXml() {
		return timeXml;
	}

	public void setTimeXml(Number timeXml) {
		this.timeXml = timeXml;
		LRLog.log("#"+hashCode() + " - building XML File      : " + timeXml.intValue() + "ms");
	}

	public Number getTimeStart() {
		return timeStart;
	}

	private void setTimeStart() {
		timeStart = new Long(System.currentTimeMillis());
	}

	public boolean isErrDico() {
		return !StringCtrl.isEmpty(errDico);
	}

	public String getErrDico() {
		return errDico;
	}
	
	public void setErrDico(String errDico) {
		this.errDico = errDico;
	}

	public boolean isErrSix() {
		return !StringCtrl.isEmpty(errSix);
	}

	public String getErrSix() {
		return errSix;
	}

	public void setErrSix(String errSix) {
		this.errSix = errSix;
	}

	public boolean isErrXml() {
		return !StringCtrl.isEmpty(errXml);
	}

	public String getErrXml() {
		return errXml;
	}

	public void setErrXml(String errXml) {
		this.errXml = errXml;
	}
	
	public String getEndingMessage() {
		return endingMessage;
	}
	
	public void setEndingMessage(String endingMessage) {
		this.endingMessage = endingMessage;
	}

	public final EOEditingContext getEc() {
		return ec;
	}
}
