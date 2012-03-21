package fr.univlr.cri.conges.objects.occupations;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.planning.EOOccupation;
import fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation;
import fr.univlr.cri.conges.objects.Planning;

public class PreparationConcoursCatb 
	extends A_PreparationConcours {

	public PreparationConcoursCatb(
			EOTypeOccupation unType, Planning unPlanning, NSTimestamp debutTS, NSTimestamp finTS, String unMotif, EOEditingContext ec) {
		super(unType, unPlanning, debutTS, finTS, unMotif, ec);
	}

	public PreparationConcoursCatb(
			EOOccupation uneOccupation, Planning unPlanning, EOEditingContext ec) {
		super(uneOccupation, unPlanning, ec);
	}
	
	public PreparationConcoursCatb(
			EOOccupation uneOccupation, EOEditingContext ec) {
		super(uneOccupation, ec);
	}

	/* (non-Javadoc)
	 * @see fr.univlr.cri.conges.objects.occupations.A_PreparationConcours#categorie()
	 */
	@Override
	public int categorie() {
		return CATEGORIE_B;
	}

}
