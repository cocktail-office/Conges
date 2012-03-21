package fr.univlr.cri.conges.objects.occupations;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.planning.EOOccupation;
import fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation;
import fr.univlr.cri.conges.objects.*;

/*
 * Created on 23 ao�t 04
 *
 */

/**
 * @author egeze
 *
 */
public class DecesMaladieTresGrave extends Occupation {

	public DecesMaladieTresGrave(EOTypeOccupation unType, Planning unPlanning, NSTimestamp debutTS, NSTimestamp finTS, String unMotif, EOEditingContext ec) {
	    super(unType, unPlanning, debutTS, finTS, unMotif, ec);
	}
	
	public DecesMaladieTresGrave(EOOccupation uneOccupation, Planning unPlanning, EOEditingContext ec) {
		super(uneOccupation, unPlanning, ec);
	}

  public DecesMaladieTresGrave(EOOccupation uneOccupation, EOEditingContext ec) {
    super(uneOccupation, ec);
  }
	
	public boolean isValide() {
		boolean isValide = super.isValide();
		if (isValide) {
			long laDuree = (dateFin().getTime()-dateDebut().getTime())/(1000*60*60*24);
			
			if (laDuree<=5) {
				//laValeur = valeur();
			} else {
				isValide=false;
				setErrorMsg("La durée de cette absence ne peut-être superieure à 5 jours.");
			}
		}

		return isValide;
	}

}
