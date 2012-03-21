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

import com.webobjects.foundation.NSArray;

/**
 * Les constantes relatives a tous les objets de 
 * type <code>Occupation</code>
 * 
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */
public interface ConstsOccupation {

	/** code occupation validee */
  public static final String CODE_VALIDEE = "0";
	/** code occupation refusee */
  public static final String CODE_REFUSEE = "1";
	/** code occupation en cours de validation */
  public static final String CODE_EN_COURS_DE_VALIDATION = "2";
	/** code occupation supprimee */
  public static final String CODE_SUPPRIMEE = "3";
	/** code occupation visee */
  public static final String CODE_VISEE = "4";
	/** code occupation en cours de suppression */
  public static final String CODE_EN_COURS_DE_SUPPRESSION = "5";
	/** code occupation en cours de suppression visee */
  public static final String CODE_EN_COURS_DE_SUPPRESSION_VISEE = "6";
  
	/** libelle occupation validee */
  public static final String LIBELLE_VALIDEE = "Validée";
	/** libelle occupation refusee */
  public static final String LIBELLE_REFUSEE = "Refusée";
	/** libelle occupation en cours de validation */
  public static final String LIBELLE_EN_COURS_DE_VALIDATION = "En cours de validation";
	/** libelle occupation supprimee */
  public static final String LIBELLE_SUPPRIMEE = "Supprimée";
	/** libelle occupation visee */
  public static final String LIBELLE_VISEE = "Visée";
	/** libelle occupation en cours de validation */
  public static final String LIBELLE_EN_COURS_DE_SUPPRESSION = "En cours de suppression";

  public static final NSArray LIBELLES_STATUTS = new NSArray(new String[] { 
  		LIBELLE_VALIDEE, LIBELLE_REFUSEE, LIBELLE_EN_COURS_DE_VALIDATION, 
  		LIBELLE_SUPPRIMEE, LIBELLE_VISEE, LIBELLE_EN_COURS_DE_SUPPRESSION,
  		LIBELLE_EN_COURS_DE_SUPPRESSION});

  public static final String OCC_MATIN = "AM";
  public static final String OCC_APREM = "PM";
  
  /** key value coding */
  public static final String ACCEPTER_KEY 	= "accepter";
  public static final String CONFIRMER_KEY 	= "confirmer";
  
  // codes issus de la méthode presence de EOAffectationAnnuelle
  public final static String PRESENCE_NON_TRAVAIL					 														= "0";
  public final static String PRESENCE_TRAVAIL					 																= "1";
  public final static String PRESENCE_OCCUPATION_DEMI_JOURNEE_VALIDEE 								= "2";
  public final static String PRESENCE_OCCUPATION_DEMI_JOURNEE_EN_COURS_DE_VALIDATION 	= "3";
  public final static String PRESENCE_OCCUPATION_MINUTE_VALIDEE 											= "4";
  public final static String PRESENCE_OCCUPATION_MINUTE_EN_COURS_DE_VALIDATION 				= "5";
  
  // format d'affichage des dates 
  public final static String DATE_FORMAT_OCCUPATION_DEMI_JOURNEE 	= "%d/%m/%Y %p";
  public final static String DATE_FORMAT_OCCUPATION_MINUTE				= "%d/%m/%Y %H:%M";
}
