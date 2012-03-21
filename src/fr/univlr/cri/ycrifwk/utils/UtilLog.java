/*
 * Copyright Université de La Rochelle 2005
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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.LRLog;

/**
 * @author ctarade 9 mai 2005
 *
 */
public class UtilLog {
    
    private static class StackTrace {
    
	    public static void displayStack(int nbLignes) {

	        if (nbLignes == 0) {
	            return;
	        }
	        
	        StringWriter sw = new StringWriter();
	        new Throwable("").printStackTrace(new PrintWriter(sw));
	        String stackTrace = sw.toString();
	        
	        // to clean up the stacktrace
	        StringTokenizer st = new StringTokenizer(stackTrace, "\n");

	        String uneLigne = "";
	        // on vire les 3 premieres lignes ...
	        //	       java.lang.Throwable: 
			//	        	at UtilLog$StackTrace.displayStack(UtilLog.java:58)
			//	        	at UtilLog.log(UtilLog.java:75)
;
			uneLigne = st.nextToken();
			uneLigne = st.nextToken();
	        uneLigne = st.nextToken();
	        
	        // on est sur le premier ligne interssante
	        uneLigne = st.nextToken();
	        
	        // on affiche nbLigne
	        while (nbLignes > 0) {
	            uneLigne = StringCtrl.replace(uneLigne, "\r", "");
		        LRLog.log(uneLigne);
		        try {
		            uneLigne = st.nextToken();
			        nbLignes--;    
		        }
		        catch (NoSuchElementException e) {
		            nbLignes = 0;
		        }
	        }
	    }
    }
	    
    public static void log(Object o) {
        log(0, o);
    }
    
    public static void log(int taille, Object o) {
        StackTrace.displayStack(taille);
        String chaine = "null";
        if (o != null) {
            chaine = (String) o;
        }
        LRLog.log(chaine);
        LRLog.log("-----------------------------------------");
    }
    
}
