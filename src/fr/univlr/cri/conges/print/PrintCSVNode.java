/**
 * 
 */
package fr.univlr.cri.conges.print;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSMutableArray;



/**
 * Classe interne pour la gestion des exports qui permet
 * de s'affranchir des {@link EOEditingContext}
 * 
 * @author Cyril TARADE <cyril.tarade at cocktail.org>
 */
public class PrintCSVNode
	extends Object {
	
	private String nomPrenom;
	private String composante;
	private String service;
	private NSMutableArray presenceArray;
	
	public PrintCSVNode(String aNomPrenom, String aComposante, String aService) {
		super();
		nomPrenom = aNomPrenom;
		composante = aComposante;
		service = aService;
		presenceArray = new NSMutableArray();
	}

	public final NSMutableArray getPresenceArray() {
		return presenceArray;
	}

	public final void addPresence(String presence) {
		presenceArray.addObject(presence);
	}

	public final String getNomPrenom() {
		return nomPrenom;
	}
	
	public String toString() {
		String disp = nomPrenom + " : ";
		for (int i=0; i<getPresenceArray().count(); i++) {
			disp += (String) getPresenceArray().objectAtIndex(i);
			if (i < getPresenceArray().count()-1) {
				disp += ", ";
			}
		}
		return disp + "\n";
	}

	public final String getComposante() {
		return composante;
	}

	public final String getService() {
		return service;
	}
}
