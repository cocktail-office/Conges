/*
 * Copyright (C) 2004 Université de La Rochelle
 *
 * This file is part of SpoolClient.
 *
 * ComptesWeb is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * ComptesWeb is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
  
package fr.univlr.cri.ycrifwk.utils;
import java.io.FileWriter;
import java.io.PrintWriter;


import com.webobjects.eocontrol._EOCheapCopyMutableArray;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

/**
 * @author ctarade
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class UtilMisc {


	/**
	 * enleve les doublons d'un array
	 * @param array
	 * @return
	 */  
	public static NSArray removeDoublons(NSArray array) {
		NSMutableArray unique = new NSMutableArray();
		for (int i=0;i<array.count();i++)
			if (!unique.containsObject(array.objectAtIndex(i)))
				unique.addObject(array.objectAtIndex(i));
		return unique.immutableClone();
	}
	

	/**
	 * ecrire un flux dans un fichier
	 */
	public static void writeStreamToFile(String chemin,String contenu) {
		PrintWriter pw = null;
		try{
			pw =  new PrintWriter( new FileWriter(new String(chemin)));
			pw.print(contenu);
			pw.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * conversion d'un tableau de int en un String
	 */
	public static String arrayIntToString(int[] arrayInt) {
		int sizeArray = arrayInt.length;
		char arrayChar[] = new char[sizeArray];
		for (int i=0 ; i<sizeArray ; i++) {
			arrayChar[i] = (char)arrayInt[i];
		}
	 	return String.valueOf(arrayChar);
	}
	
	/**
	 * conversion d'un tableau de int en un binary char
	 */
	public static String arrayIntToBinaryString(int[] arrayInt) {
		String chaine = "";
		int size = arrayInt.length;
		for (int i=0;i<size;i++)
			chaine = chaine + (char)arrayInt[i];
		return chaine;
	}

	/**
	 * savoir si un tableau d'entier contient un entier
	 * @param arrayInt
	 * @param value
	 * @return
	 */
	public static boolean arrayIntContainsInt(int[] arrayInt, int value) {
		for (int i=0; i<arrayInt.length ;i++)
			if (arrayInt[i] == value)
				return true;
		return false;
	}

	  
	// METHODES UTILITAIRES
    

    /**
     * remettre a plat un array et ote les tableaux vides
     */
	public static NSArray applatirNSArrays(NSArray arrays) {
	    NSArray arrayPlat = new NSArray();
	    for (int i = 0; i < arrays.count(); i++) {
            Object unObjet = arrays.objectAtIndex(i);
            if (unObjet.getClass() == NSArray.class || unObjet.getClass() == NSMutableArray.class || unObjet.getClass() == _EOCheapCopyMutableArray.class) {
                if ( ((NSArray) unObjet).count() > 0) {
                    arrayPlat = arrayPlat.arrayByAddingObjectsFromArray(applatirNSArrays((NSArray)unObjet));
                }
            }
            else {
                arrayPlat = arrayPlat.arrayByAddingObject(unObjet);
            }
        }
	    return arrayPlat;
	}
	
}
