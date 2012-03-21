package fr.univlr.cri.conges.objects.occupations;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.planning.EOOccupation;
import fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation;
import fr.univlr.cri.conges.objects.*;

/*
 * Created on 23 ao�t 04
 */

/**
 * @author egeze
 */
public class Naissance extends Occupation {
  
	public Naissance(EOTypeOccupation unType, Planning unPlanning, NSTimestamp debutTS, NSTimestamp finTS, String unMotif, EOEditingContext ec) {
    super(unType, unPlanning, debutTS, finTS, unMotif, ec);
  }

  public Naissance(EOOccupation uneOccupation, Planning unPlanning, EOEditingContext ec) {
    super(uneOccupation, unPlanning, ec);
  }

  public Naissance(EOOccupation uneOccupation, EOEditingContext ec) {
    super(uneOccupation, ec);
  }

  public boolean isValide() {
    boolean isValide = super.isValide();

    // 3 jours ouvrables
    if (isValide) {
      NSArray lesJoursOccupes = lePlanning.lesJours(dateDebut(), dateFin());
      int nbJourOuvrable = 0;

      for (int i = 0; i < lesJoursOccupes.count(); i++) {
        Jour unJour = (Jour) lesJoursOccupes.objectAtIndex(i);
        if (unJour.dureeTravailleeEnMinutes() > 0) {
          nbJourOuvrable++;
        }
      }

      if (nbJourOuvrable > 3) {
        isValide = false;
        setErrorMsg("La durée de cette absence ne peut-être superieure à 3 jours ouvrables.");
      }
    }

    return isValide;
  }

}
