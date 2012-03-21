/*
 * Copyright Université de La Rochelle 2004
 *
 * ctarade@univ-lr.fr
 *
 * Ce logiciel est un programme informatique servant à gérer les comptes
 * informatiques des utilisateurs. 
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


package fr.univlr.cri.ycrifwk.utils;

import fr.univlr.cri.webapp.CRIMailBus;

/**
 * @author ctarade
 *
 * classe permettant de gerer les Exception Java
 */
public class UtilException {
	


	/**
	 * permet de recuperer la trace d'une exception au format string
	 * message + trace
	 * @param e
	 * @return
	 */
	public static String stackTraceToString(Throwable e, boolean useHtml) {
		String tagCR = "\n";
		if (useHtml)
			tagCR = "<br>";
		String stackStr = e.getMessage() + tagCR + tagCR;
		StackTraceElement[] stack = e.getStackTrace();
		for (int i=0; i<stack.length; i++)
			stackStr += ((StackTraceElement)stack[i]).toString() + tagCR;
		return stackStr;
	}


	/**
	 * envoi de la trace d'une exception à une personne
	 * @param mailBus
	 * @param appName
	 * @param e
	 * @param from
	 * @param to
	 * @return
	 */
	public static boolean sendExceptionTrace(CRIMailBus mailBus, String appName, String from, String to, String startText, Throwable e) {
		return mailBus.sendMail(
				from,
				to,
				null, 
				"["+appName+"]Exception",
				startText + "\n\nTrace de l'exception : \n\n" + 
				stackTraceToString(e, false) +
				(e != null && e.getCause() != null ? "\nCause by :" + stackTraceToString(e.getCause(), false) : ""));
	}
}
