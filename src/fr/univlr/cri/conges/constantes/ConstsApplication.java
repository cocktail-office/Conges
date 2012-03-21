/*
 * Copyright COCKTAIL (www.cocktail.org), 1995, 2010 This software
 * is governed by the CeCILL license under French law and abiding by the
 * rules of distribution of free software. You can use, modify and/or
 * redistribute the software under the terms of the CeCILL license as
 * circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * As a counterpart to the access to the source code and rights to copy, modify
 * and redistribute granted by the license, users are provided only with a
 * limited warranty and the software's author, the holder of the economic
 * rights, and the successive licensors have only limited liability. In this
 * respect, the user's attention is drawn to the risks associated with loading,
 * using, modifying and/or developing or reproducing the software by the user
 * in light of its specific status of free software, that may mean that it
 * is complicated to manipulate, and that also therefore means that it is
 * reserved for developers and experienced professionals having in-depth
 * computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling
 * the security of their systems and/or data to be ensured and, more generally,
 * to use and operate it in the same conditions as regards security. The
 * fact that you are presently reading this means that you have had knowledge
 * of the CeCILL license and that you accept its terms.
 */
package fr.univlr.cri.conges.constantes;

/**
 * Les constantes relative a l'application
 *
 * @author ctarade
 */
public interface ConstsApplication {

	// 
	public final static String CONFIG_OTHER_TABLE_NAME_AFFECTATION_KEY 			= "OTHER_TABLE_NAME_AFFECTATION";
	// noms des parametres jours feries
	public final static String CONFIG_APP_DATECTRL_ADDITIONAL_HOLIDAY_KEY = "APP_DATECTRL_ADDITIONAL_HOLIDAY";
	public final static String CONFIG_APP_DATECTRL_IGNORING_HOLIDAY_KEY 		= "APP_DATECTRL_IGNORING_HOLIDAY";
	// code structure DRH
	public final static String CONFIG_C_STRUCTURE_DRH_KEY = "C_STRUCTURE_DRH";
	// notes cet
	public final static String CONFIG_NOTE_CET_URL_KEY	=	"NOTE_CET_URL";
	
	// editions
	public final static String CONFIG_GRHUM_PRESIDENT_KEY 	= "GRHUM_PRESIDENT";
	public final static String CONFIG_GRHUM_ETAB_KEY 				= "GRHUM_ETAB";
	
	/**
   * Gestion des triggers et des directAction pour la modif de donnees
   * par les autres applications 
   */    
	public final static String KEY_CONFIG_EXT_NOTIF = "EXT_NOTIF";
	public final static String EXT_NOTIF_LABEL_DIRECT_ACTION = "DA";
  public final static String EXT_NOTIF_LABEL_TRIGGER       = "TRIGGER";
  public final static int EXT_NOTIF_CODE_UNDEFINED    		= -1;
  public final static int EXT_NOTIF_CODE_DIRECT_ACTION    = 0;
  public final static int EXT_NOTIF_CODE_TRIGGER          = 1;
  public final static int EXT_NOTIF_CODE_NONE             = 2;
  
  
}
