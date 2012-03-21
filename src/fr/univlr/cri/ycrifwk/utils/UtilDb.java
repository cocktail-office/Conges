/*
 * Copyright (C) 2004 Universit� de La Rochelle
 *
 * This file is part of SpoolClient.
 *
 * ComptesFwk is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * ComptesFwk is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */


package fr.univlr.cri.ycrifwk.utils;

import com.webobjects.eocontrol.*;
import com.webobjects.foundation.NSArray;

/**
 * @author ctarade
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class UtilDb {

	/**
	 * permet de faire un fetch en une seule ligne
	 * @param ec
	 * @param entity
	 * @param qual
	 * @param arraySort
	 * @return NSArray des objets issus du fetch
	  */
	public static NSArray fetchArray(EOEditingContext ec, String entity, EOQualifier qual, NSArray arraySort) {
		EOFetchSpecification fetchSpec = new EOFetchSpecification(entity, qual, arraySort);
		return ec.objectsWithFetchSpecification(fetchSpec);   
	}
	
	/**
	 * permet de faire un fetch en une seule ligne
	 * @param ec
	 * @param entity
	 * @param qual
	 * @param arraySort
	 * @param refresh
	 * @return NSArray des objets issus du fetch
	 */
	public static NSArray fetchArray(EOEditingContext ec, String entity, EOQualifier qual, NSArray arraySort, boolean refresh) {
		EOFetchSpecification fetchSpec = new EOFetchSpecification(entity, qual, arraySort);
		fetchSpec.setRefreshesRefetchedObjects(refresh);
		return ec.objectsWithFetchSpecification(fetchSpec);   
	}

	
	/**
	 * permet de sauvegarder un editing context
	 * 
	 * @param ec
	 * @param shouldPrintStackTrace
	 * 	indique s'il faut tracer l'erreur dans la console
	 */
	public static boolean save(EOEditingContext ec, boolean shouldPrintStackTrace) throws Throwable {
		ec.lock();
		try {
			ec.saveChanges(); 
			ec.unlock(); 
			return true;
		}
		catch (Throwable e) {
			ec.revert();
			ec.unlock(); 
			if (shouldPrintStackTrace) {
				e.printStackTrace();
			}
			throw e;
		}
	}
}
