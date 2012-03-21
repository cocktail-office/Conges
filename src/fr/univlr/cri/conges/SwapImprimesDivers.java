/*
 * Copyright Consortium Coktail, 2004-2008
 * 
 * cyril.tarade at univ-lr.fr
 * 
 * Ce logiciel est un programme informatique servant � g�rer les cong�s.
 * 
 * Ce logiciel est r�gi par la licence CeCILL soumise au droit fran�ais et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffus�e par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".
 * 
 * En contrepartie de l'accessibilit� au code source et des droits de copie,
 * de modification et de redistribution accord�s par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limit�e.  Pour les m�mes raisons,
 * seule une responsabilit� restreinte p�se sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les conc�dants successifs.
 * 
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
 * 
 * Le fait que vous puissiez acc�der � cet en-t�te signifie que vous avez 
 * pris connaissance de la licence CeCILL, et que vous en avez accept� les
 * termes.
 */

package fr.univlr.cri.conges;

import com.webobjects.appserver.*;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.constantes.ConstsOccupation;
import fr.univlr.cri.conges.print.CngPdfBoxCtrl;
import fr.univlr.cri.conges.print.ConstsPrint;
import fr.univlr.cri.conges.print.PrintDemandeGardeEnfant;
import fr.univlr.cri.util.wo5.DateCtrl;
import fr.univlr.cri.webapp.LRRecord;

/**
 * Ecran de gestion des imprimes "divers", comme par exemple les conges
 * pour garde d'enfant.
 * 
 * @author ctarade
 */
public class SwapImprimesDivers 
	extends YCRIWebPage
		implements ConstsOccupation {
	
	// conges garde d'enfant : selections dans la liste d'un enfant
	public LRRecord enfantItem;
	public LRRecord enfantSelected;
	// dates 
	public NSTimestamp dDeb;
	public NSTimestamp dFin;
	public NSArray amPmList = new NSArray(new String[]{OCC_MATIN, OCC_APREM});
	public String amPmItem;
	public String amPmDDeb;
	public String amPmDFin;
	//
	public String motif;
	//
	public String strDemiJournees;
	private int nbDemiJournees;
	
	public SwapImprimesDivers(WOContext context) {
		super(context);
		initComponent();
	}
	
	private void initComponent() {
		dDeb = DateCtrl.now();
		dFin = DateCtrl.now();
		amPmDDeb = OCC_MATIN;
		amPmDFin = OCC_APREM;
		motif = "motif de l'absence";
		nbDemiJournees = -1;
		strDemiJournees = "<inconnu>";
	}
	
	// navigation
	
	/**
	 * TODO voir pour utiliser laSession.getLePlanningSelectionne().getCalcul().nbDemiJourneesTravaillees plutot
	 * Relancer le calcul du nombre de jour de demi journees
	 */
	public WOComponent doRecalcul() {
		if (dDeb != null && dFin != null && DateCtrl.isBeforeEq(dDeb, dFin)) {
			//NSTimestamp dDebAmPm = dDeb.timestampByAddingGregorianUnits(0,0,0,amPmDDeb.equals(D_PM)?12:0,0,0);
			//NSTimestamp dFinAmPm = dFin.timestampByAddingGregorianUnits(0,0,0,amPmDFin.equals(D_PM)?12:0,0,0);
			//nbDemiJournees = laSession.getLePlanningSelectionne().getCalcul().nbDemiJourneesTravaillees(
			//		dDebAmPm, dFinAmPm);
			NSArray presenceList = laSession.getLePlanningSelectionne().affectationAnnuelle().presencePourPeriode(
					"R", dDeb, dFin);
			 // faut-il ignorer certaines valeurs
			boolean shouldIgnoreFirstDemiJournee = amPmDDeb.equals(OCC_APREM);
			boolean shouldIgnoreLastDemiJournee = amPmDFin.equals(OCC_MATIN);
			nbDemiJournees = 0;
			for (int i=0; i<presenceList.count(); i++) {
				String presence = (String) presenceList.objectAtIndex(i);
				if (shouldIgnoreFirstDemiJournee && i==0) {
				} else {
					nbDemiJournees += (!((String)presence.substring(0, 1)).equals("0")?1:0);
				}
				if (shouldIgnoreLastDemiJournee && i==presenceList.count()-1) {
				} else {
					nbDemiJournees += (!((String)presence.substring(1, 2)).equals("0")?1:0);
				}
			}
			strDemiJournees = Integer.toString(nbDemiJournees);
		} else {
			nbDemiJournees = -1;
			strDemiJournees = "<erreur>";
		}
		return null;
	}
	
	// display
	
	/**
	 * Affichage : nom prenom n�(e) le dNaissance
	 */
	public String enfantDisplay() {
		return enfantDisplay(enfantItem);
	}
	
	/**
	 * Affichage : nom prenom né(e) le dNaissance
	 */
	private String enfantDisplay(LRRecord value) {
		return 
		value.stringForKey("nom") + " " + value.stringForKey("prenom") + 
		" né" + (value.stringForKey("sexe").equals("F") ? "e" : "") + " le " +
		DateCtrl.dateToString(value.dateForKey("dNaissance"));
	}
	
	// boolean 
	
	/**
	 * On affiche l'imprime uniquement pour les agents ayant des enfants
	 */
	public boolean showImprimeGardeEnfant() {
		return laSession.individuConnecte().tosEnfant().count() > 0;
	}
	
	/**
	 * On affiche le bouton d'impression uniquement si le formulaire est valide
	 */
	public boolean showCngPdfBoxDemandeGardeEnfant() {
		return dDeb != null && dFin != null && amPmDDeb != null && amPmDFin != null && enfantSelected != null && motif != null && nbDemiJournees != -1;
	}
	
  // gestion des impressions
	 
  /**
   * Classe controleur pour l'impression du planning
   * de service en couleurs
   */
  public class PdfBoxDemandeGardeEnfantCtrl extends CngPdfBoxCtrl {
		public PdfBoxDemandeGardeEnfantCtrl(Class aGenericSixPrintClass, EOEditingContext anEc) {
			super(aGenericSixPrintClass, anEc);
		}
		public NSDictionary buildDico() {
			NSMutableDictionary dico = new NSMutableDictionary();
			// date impression
			dico.setObjectForKey(DateCtrl.dateToString(DateCtrl.now()), ConstsPrint.XML_KEY_DATE_IMPRESSION);
			// sexe du demandeur (pour le je soussigné(e))
			if (!laSession.individuConnecte().cCivilite().equals("M.")) {
				dico.setObjectForKey("e", "feminin");
			} else {
				dico.setObjectForKey("", "feminin");
			}
			// prenom / nom / qualite
			dico.setObjectForKey(laSession.individuConnecte().nomCompletQualite(), "prenomNomQualite");
			// prenom enfant ne le
			dico.setObjectForKey(enfantDisplay(enfantSelected), "prenomNeLeDNaissance");
			// dates
			dico.setObjectForKey(DateCtrl.dateToString(dDeb) + " " + (amPmDDeb.equals(OCC_MATIN) ? "matin" : "après-midi"), "dateDebutAMPM");
			dico.setObjectForKey(DateCtrl.dateToString(dFin) + " " + (amPmDFin.equals(OCC_MATIN) ? "matin" : "après-midi"), "dateFinAMPM");
			// demies journee et pluriel ou pas
			dico.setObjectForKey(strDemiJournees, "nbDemiJourneesTravail");
			if (nbDemiJournees > 1) {
				dico.setObjectForKey("s", "pluriel");
			} else {
				dico.setObjectForKey("", "pluriel");
			}
			// motif
			dico.setObjectForKey(motif, "motif");
			// ville
	    dico.setObjectForKey(app.appGrhumVille(), ConstsPrint.XML_KEY_VILLE);
	    // URL du logo
	    dico.setObjectForKey(app.mainLogoUrl(), ConstsPrint.XML_KEY_MAIN_LOGO_URL);
			return dico.immutableClone();
		}
		public String fileName() {
	  	return "DemandeGardeEnfant_"+enfantSelected.stringForKey("nom")+"_"+enfantSelected.stringForKey("prenom");
	  }	
  }
  
  /** */
  public PdfBoxDemandeGardeEnfantCtrl ctrlDemandeGardeEnfantCtrl() {
  	return new PdfBoxDemandeGardeEnfantCtrl(PrintDemandeGardeEnfant.class, edc);
  }
  

}