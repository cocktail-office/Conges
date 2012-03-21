// EOMessage.java
// Created on Wed Feb 16 10:06:29  2005 by Apple EOModeler Version 5.2

package fr.univlr.cri.conges.eos.modele.conges;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.webapp.CRIDataBus;
import fr.univlr.cri.webapp.LRSort;


public class EOMessage extends _EOMessage {

	private final static LRSort MESSAGE_SORT = LRSort.newSort(
			MES_DATE_DEBUT_KEY+","+MES_DATE_FIN_KEY, LRSort.Descending);
		
	public EOMessage() {
		super();
	}

	/**
	 * rechercher tous les messages
	 * @param ec
	 * @param personne
	 * @param CdrType
	 * @param niveau
	 * @return
	 */
	public static NSArray findAllMessagesInContext(EOEditingContext ec) {
		return fetchMessages(ec, null, MESSAGE_SORT);
	}

	/**
	 * rechercher les messages en cours
	 * @param ec
	 * @param personne
	 * @param CdrType
	 * @param niveau
	 * @return
	 */
	public static NSArray findCurrentMessagesInContext(EOEditingContext ec) {
		EOQualifier qual = CRIDataBus.newCondition(
				MES_DATE_DEBUT_KEY + "<=%@ and " + MES_DATE_FIN_KEY + ">=%@",
				new NSArray(new Object[] {DateCtrlConges.now(), DateCtrlConges.now()}));
		return fetchMessages(ec, qual, MESSAGE_SORT);
	}
}
