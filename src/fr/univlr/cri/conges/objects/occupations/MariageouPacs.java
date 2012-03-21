package fr.univlr.cri.conges.objects.occupations;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.planning.EOOccupation;
import fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation;
import fr.univlr.cri.conges.objects.*;

/*
 * Created on 23 ao�t 04
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author egeze
 */
public class MariageouPacs extends Occupation {
  
	public MariageouPacs(EOTypeOccupation unType, Planning unPlanning, NSTimestamp debutTS, NSTimestamp finTS, String unMotif, EOEditingContext ec) {
    super(unType, unPlanning, debutTS, finTS, unMotif, ec);
  }
  
  public MariageouPacs(EOOccupation uneOccupation, Planning unPlanning, EOEditingContext ec) {
    super(uneOccupation, unPlanning, ec);
  }

  public MariageouPacs(EOOccupation uneOccupation, EOEditingContext ec) {
    super(uneOccupation, ec);
  }

  public boolean isValide() {
    boolean isValide = super.isValide();

    // 5 jours ouvrables
    if (isValide) {
      NSArray lesJoursOccupes = lePlanning.lesJours(dateDebut(), dateFin());
      int nbJourOuvrable = 0;

      for (int i = 0; i < lesJoursOccupes.count(); i++) {
        Jour unJour = (Jour) lesJoursOccupes.objectAtIndex(i);
        if (unJour.dureeTravailleeEnMinutes() > 0) {
          nbJourOuvrable++;
        }
      }

      if (nbJourOuvrable > 5) {
        isValide = false;
        setErrorMsg("La durée de cette absence ne peut-être superieure à 5 jours ouvrables.");
      }
    }

    return isValide;
  }

}
