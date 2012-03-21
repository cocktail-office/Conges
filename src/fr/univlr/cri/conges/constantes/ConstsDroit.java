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

import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

/**
 * Regroupe l'ensemble des constantes relatives aux droits.
 * L'entite associee du modele est <code>Droit</code>.
 * 
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 *
 */

public interface ConstsDroit {

	// la nature du droit
	
	/** CDR_NIVEAU pour aucun droit */
	public final static Integer DROIT_NIVEAU_RIEN 											= new Integer(0);
	/** CDR_NIVEAU pour droit de visualisation */
	public final static Integer DROIT_NIVEAU_VISU 											= new Integer(1);
	public final static String DROIT_NIVEAU_VISU_LABEL 									= "Visualisation";
	public final static String DROIT_NIVEAU_VISU_COMMENT								= "Droit de consultation du planning et des occupations";
	/** CDR_NIVEAU pour droit de visa */
	public final static Integer DROIT_NIVEAU_VISA 											= new Integer(2);
	public final static String DROIT_NIVEAU_VISA_LABEL 									= "Visa";
	public final static String DROIT_NIVEAU_VISA_COMMENT 								= "Droit de visa : acceptation partielle, d&eacute;finitive uniquement apr&egrave;s validation";
	/** CDR_NIVEAU pour droit de validation */
	public final static Integer DROIT_NIVEAU_VALIDATION 								= new Integer(3);
	public final static String DROIT_NIVEAU_VALIDATION_LABEL 						= "Validation";
	public final static String DROIT_NIVEAU_VALIDATION_COMMENT 					= "Droit de validation d'absences, d'occupation et de plannings";
	/** CDR_NIVEAU pour droit de DRH local a une composante */
	public final static Integer DROIT_NIVEAU_DRH_COMPOSANTE 						= new Integer(4);
	public final static String DROIT_NIVEAU_DRH_COMPOSANTE_LABEL 				= "DRH Composante";
	public final static String DROIT_NIVEAU_DRH_COMPOSANTE_COMMENT			= "Droit DRH sur la composante et sur tous ses sous-services (validation des occupations DRH niveau 2 et gestion des CET)";
	/** CDR_NIVEAU pour droit d'administrateur local a une composante */
	public final static Integer DROIT_NIVEAU_ADM_COMPOSANTE 						= new Integer(5);
	public final static String DROIT_NIVEAU_ADM_COMPOSANTE_LABEL 				= "ADM Composante";
	public final static String DROIT_NIVEAU_ADM_COMPOSANTE_COMMENT 		= "Droit d'administration sur la composante et tous sur ses sous-services (reliquats, r&eacute;gularisation de solde et gestion des profils)";
	/** CDR_NIVEAU pour droit d'une personne faisant la saisie par délégation */
	public final static Integer DROIT_NIVEAU_DELEGATION 								= new Integer(6);
	public final static String DROIT_NIVEAU_DELEGATION_LABEL 						= "D&eacute;l&eacute;gation";
	public final static String DROIT_NIVEAU_DELEGATION_COMMENT 					= "Droit de saisie des planning et des absences pour le compte d'autres agents";
	
	// le type de cible
	
	/** CDR_TYPE pour le service */
	public final static String DROIT_CIBLE_SERVICE 									= "S";
	public final static String DROIT_CIBLE_SERVICE_LABEL 						= "Service";
	public final static String DROIT_CIBLE_SERVICE_COMMENT 					= "Cibler tous les plannings &eacute;tablis sur ce service";
	/** CDR_TYPE pour le chef de service */
	public final static String DROIT_CIBLE_CHEF_DE_SERVICE 					= "C";
	public final static String DROIT_CIBLE_CHEF_DE_SERVICE_LABEL 		= "Chef du service";
	public final static String DROIT_CIBLE_CHEF_DE_SERVICE_COMMENT	= "Cibler la personne declar&eacute;e comme chef de service dans l'annuaire";
	/** CDR_TYPE pour l'agent */
	public final static String DROIT_CIBLE_INDIVIDU 								= "P";
	public final static String DROIT_CIBLE_INDIVIDU_LABEL 					= "Agent";
	public final static String DROIT_CIBLE_INDIVIDU_COMMENT 				= "Cibler uniquement l'agent selectionn&eacute;";
	
	public static NSArray orderingsIndividu = 	new NSArray( new Object[] {
			EOSortOrdering.sortOrderingWithKey("toIndividuResp.nom", EOSortOrdering.CompareAscending),
			EOSortOrdering.sortOrderingWithKey("toIndividuResp.prenom", EOSortOrdering.CompareAscending)});
	
	/** les niveau de droits qui accorde le droit de visa */
	public final static Integer[] LIST_NIVEAU_DROIT_ALERTE_VISA = {
		DROIT_NIVEAU_VISA, DROIT_NIVEAU_VALIDATION, 
		DROIT_NIVEAU_ADM_COMPOSANTE, DROIT_NIVEAU_DRH_COMPOSANTE};
	
	/** les niveau de droits qui accorde le droit de validation */
	public final static Integer[] LIST_NIVEAU_DROIT_ALERTE_VALIDATION = {
		DROIT_NIVEAU_VALIDATION, DROIT_NIVEAU_ADM_COMPOSANTE, 
		DROIT_NIVEAU_DRH_COMPOSANTE};
	
	/** les niveau de droits qui permette de visualiser un service */
	public final static Integer[] LIST_NIVEAU_DROIT_VISU = {
		DROIT_NIVEAU_VISU, DROIT_NIVEAU_VISA, 
		DROIT_NIVEAU_VALIDATION, DROIT_NIVEAU_ADM_COMPOSANTE, 
		DROIT_NIVEAU_DRH_COMPOSANTE, DROIT_NIVEAU_DELEGATION};

}
