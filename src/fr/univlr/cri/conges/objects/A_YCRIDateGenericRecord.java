/*
 * Copyright Universit� de La Rochelle 2005
 *
 * ctarade@univ-lr.fr
 *
 * Ce logiciel est un programme informatique servant � g�rer les comptes
 * informatiques des utilisateurs. 
 * 
 * Ce logiciel est r�gi par la licence CeCILL soumise au droit fran�ais et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffus�e par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".

 * En contrepartie de l'accessibilit� au code source et des droits de copie,
 * de modification et de redistribution accord�s par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limit�e.  Pour les m�mes raisons,
 * seule une responsabilit� restreinte p�se sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les conc�dants successifs.

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

 * Le fait que vous puissiez acc�der � cet en-t�te signifie que vous avez 
 * pris connaissance de la licence CeCILL, et que vous en avez accept� les
 * termes.
 */
package fr.univlr.cri.conges.objects;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSTimestamp;
import com.webobjects.foundation.NSValidation;

import fr.univlr.cri.conges.eos.modele.conges.EOAlerte;
import fr.univlr.cri.conges.eos.modele.conges.EOCET;
import fr.univlr.cri.conges.eos.modele.conges.EOCETTransaction;
import fr.univlr.cri.conges.eos.modele.conges.EODroit;
import fr.univlr.cri.conges.eos.modele.conges.EODroitPasDeMailCds;
import fr.univlr.cri.conges.eos.modele.conges.EOMessage;
import fr.univlr.cri.conges.eos.modele.conges.EOParametre;
import fr.univlr.cri.conges.eos.modele.conges.EOPreference;
import fr.univlr.cri.conges.eos.modele.conges.EORepartValidation;
import fr.univlr.cri.conges.eos.modele.conges.EOStructureAutorisee;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOAnnulationPeriodeFermeture;
import fr.univlr.cri.conges.eos.modele.planning.EOCalculAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOHoraire;
import fr.univlr.cri.conges.eos.modele.planning.EOMouvement;
import fr.univlr.cri.conges.eos.modele.planning.EOOccupation;
import fr.univlr.cri.conges.eos.modele.planning.EOPeriodeAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOPeriodeFermeture;
import fr.univlr.cri.conges.eos.modele.planning.EOPlanningHebdomadaire;
import fr.univlr.cri.conges.eos.modele.planning.EOTypeHoraire;
import fr.univlr.cri.conges.eos.modele.planning.EOVacanceScolaire;
import fr.univlr.cri.util.wo5.DateCtrl;
import fr.univlr.cri.webapp.LRRecord;

/**
 * Classe permettant de gerer automatiquement les dates de creation
 * et modification des enregistrements.
 * 
 * La liste des entités concernées est données par le tableau {@link #ENTITIES_DATE_SENSITIVE}
 * 
 * @author ctarade
 */
public abstract class A_YCRIDateGenericRecord 
	extends LRRecord {

	private final static NSArray ENTITIES_DATE_SENSITIVE = new NSArray(new String[] {
			// modele conges
			EOAlerte.ENTITY_NAME,
			EOCET.ENTITY_NAME,
			EOCETTransaction.ENTITY_NAME,
			EODroit.ENTITY_NAME,
			EODroitPasDeMailCds.ENTITY_NAME,
			EOMessage.ENTITY_NAME,
			EOParametre.ENTITY_NAME,
			EOPreference.ENTITY_NAME,
			EORepartValidation.ENTITY_NAME,
			EOStructureAutorisee.ENTITY_NAME,
			// modele planning
			EOAffectationAnnuelle.ENTITY_NAME,
			EOAnnulationPeriodeFermeture.ENTITY_NAME,
			EOCalculAffectationAnnuelle.ENTITY_NAME,
			EOHoraire.ENTITY_NAME,
			EOMouvement.ENTITY_NAME,
			EOOccupation.ENTITY_NAME,
			EOPeriodeAffectationAnnuelle.ENTITY_NAME,
			EOPeriodeFermeture.ENTITY_NAME,
			EOPlanningHebdomadaire.ENTITY_NAME,
			EOTypeHoraire.ENTITY_NAME,
			EOVacanceScolaire.ENTITY_NAME });

	private Boolean _shouldProcessDate;
	
	private boolean shouldProcessDate() {
		if (_shouldProcessDate == null) {
			_shouldProcessDate = new Boolean(ENTITIES_DATE_SENSITIVE.containsObject(entityName()));
		}
		return _shouldProcessDate;
	}
	
  public void validateForInsert() throws NSValidation.ValidationException {
  	if (shouldProcessDate()) {
    	setDCreation(DateCtrl.now());
      setDModification(DateCtrl.now());
  	}
    super.validateForInsert();
  }

  public void validateForUpdate() throws NSValidation.ValidationException {
  	if (shouldProcessDate()) {
  		  		
	    boolean shouldChangeDModification = false;
	    // on met a jour la date uniquement si des attributs ont changé
	    NSDictionary dico = changesFromSnapshot(editingContext().committedSnapshotForObject(this));
	    NSArray keys = dico.allKeys();
	    NSArray attributeKeys = attributeKeys();
	    for (int i=0; i<keys.count() && !shouldChangeDModification; i++) {
	    	String key = (String) keys.objectAtIndex(i);
	    	if (attributeKeys.containsObject(key)) {
	    		shouldChangeDModification = true;
	    	}
	    }
	    if (shouldChangeDModification) {
	      setDModification(DateCtrl.now());
	    }
  	}
    super.validateForUpdate();
  }

  public void validateForDelete() throws NSValidation.ValidationException {
  	super.validateForDelete();
  }

  public void validateForSave() throws NSValidation.ValidationException {
  	super.validateForSave();
  }

  private void setDCreation(NSTimestamp value) {
    takeStoredValueForKey(value, "dCreation");
  }
  private void setDModification(NSTimestamp value) {
    takeStoredValueForKey(value, "dModification");
  }

}
