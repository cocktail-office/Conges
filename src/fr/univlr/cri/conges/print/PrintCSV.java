/**
 * 
 */
package fr.univlr.cri.conges.print;

import com.webobjects.appserver.WOResponse;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.webapp.LRDataResponse;

/**
 * Classe de gestion des exports csv
 * 
 * @author Cyril TARADE <cyril.tarade at cocktail.org>
 */
public class PrintCSV {
	
	/**
	 * Constuire le fichier CSV associé à liste de plannings pour une période
	 * 
	 */
	public static WOResponse printCsvPlanningPourPeriode(
			String nomFichier, 
			NSTimestamp debut, 
			NSTimestamp fin, 
			NSArray affAnnArray, 
			String typePlanning) {
		
		NSArray moisList = DateCtrlConges.getListeDebutMoisEntre(debut, fin);
		
  	LRDataResponse resp = new LRDataResponse();
  	StringBuffer sb = new StringBuffer();
  
  	// liste des dates
  	NSMutableArray dateArray = new NSMutableArray();
  	for (int i=0; i<moisList.count(); i++) {
  		NSTimestamp moisDebut = (NSTimestamp) moisList.objectAtIndex(i);
  		NSTimestamp moisFin = DateCtrlConges.dateToFinMois(moisDebut);
  		
  		// cadrer par rapport à la période d'interrogation
  		if (i == 0 && DateCtrlConges.isAfter(debut, moisDebut)) {
  			moisDebut = debut;
  		}
  		if (i == moisList.count()-1  && DateCtrlConges.isBefore(fin, moisFin)) {
  			moisFin = fin;
  		}
  		
  		NSTimestamp jourEnCours = moisDebut;
		
			while (DateCtrlConges.isBeforeEq(jourEnCours, moisFin)) {
				dateArray.addObject(jourEnCours);
				jourEnCours = jourEnCours.timestampByAddingGregorianUnits(0,0,1,0,0,0);
			}
  	}
  	
  	// liste des oids des plannings (pour alleger la memoire)
  	NSArray oidArray = new NSArray();
  	for (int i=0; i<affAnnArray.count(); i++) {
  		EOAffectationAnnuelle affAnn = (EOAffectationAnnuelle) affAnnArray.objectAtIndex(i);
  		oidArray = oidArray.arrayByAddingObject(affAnn.oid());
  	}
  	
  	NSMutableArray nodes = new NSMutableArray();
  	
  	
  	for (int indexOid = 0; indexOid<oidArray.count() ; indexOid++)  {
  		Number oid = (Number) oidArray.objectAtIndex(indexOid);
  		EOEditingContext ec = new EOEditingContext();
  		EOAffectationAnnuelle affAnn = EOAffectationAnnuelle.findAffectationAnnuellesForOidInContext(ec, oid);

  		PrintCSVNode node = new PrintCSVNode(
  				affAnn.individu().nomComplet(), 
  				affAnn.structure().toComposante().libelleCourt(), 
  				affAnn.structure().libelleCourt());
  		
  		NSArray presenceArray = affAnn.presencePourPeriode(typePlanning, debut, fin);
  		
  		for (int indexPresence = 0; indexPresence < presenceArray.count(); indexPresence++) {
  			String presenceItem = (String) presenceArray.objectAtIndex(indexPresence);
  			for (int indexAmPm = 0; indexAmPm < 2; indexAmPm++)  {
  				String presenceDemiJ = null;
  				if (indexAmPm == 0) {
  					presenceDemiJ = codePourPresenceAM(presenceItem, false);
	        } else {
	        	presenceDemiJ = codePourPresencePM(presenceItem, false);
	        }
  		     node.addPresence(presenceDemiJ);
  			}
  		}
  		
  		// liberation de memoire
  		ec.dispose();
  		
  		nodes.addObject(node);
  	}
  	
  	
  	// construction du fichier

  	// entete
  	sb.append("Agent").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
  	sb.append("Composante").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
  	sb.append("Service").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
  	for (int indexDate = 0; indexDate < dateArray.count() ; indexDate++) {
  		NSTimestamp date = (NSTimestamp) dateArray.objectAtIndex(indexDate);
  		for (int indexAmPm = 0; indexAmPm < 2; indexAmPm++)  {
  			sb.append(DateCtrlConges.dateToString(date)).append(" ");
  			if (indexAmPm % 2 == 0) {
  				sb.append("00:00:00");
  			} else {
  				sb.append("12:00:00");
  			}
  			sb.append(ConstsPrint.CSV_COLUMN_SEPARATOR);
  		}
  	}
		sb.append(ConstsPrint.CSV_NEW_LINE);
  	
  	// agents
  	for (int indexNode = 0 ; indexNode < nodes.count() ; indexNode++) {
  		PrintCSVNode node = (PrintCSVNode) nodes.objectAtIndex(indexNode);
  		
  		sb.append(node.getNomPrenom()).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
  		sb.append(node.getComposante()).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
  		sb.append(node.getService()).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
  		  				
  		for (int indexDate = 0; indexDate < dateArray.count() ; indexDate++) {
  			sb.append(node.getPresenceArray().objectAtIndex(2*indexDate));
  			sb.append(ConstsPrint.CSV_COLUMN_SEPARATOR);
    		sb.append(node.getPresenceArray().objectAtIndex((2*indexDate)+1));
    		sb.append(ConstsPrint.CSV_COLUMN_SEPARATOR);
  		}
  		sb.append(ConstsPrint.CSV_NEW_LINE);
  	}
  	
  	// 
  	NSData stream = new NSData(sb.toString(), ConstsPrint.CSV_ENCODING);
  	resp.setContent(stream);
		resp.setContentEncoding(ConstsPrint.CSV_ENCODING);
  	resp.setHeader(String.valueOf(stream.length()), "Content-Length");
  	resp.setFileName(
  			DateCtrlConges.dateToString(DateCtrlConges.now(), "%Y%m%d") + "_"+ nomFichier + ".csv");
  	return resp;
	}
	


  /**
   * pour impressions
   * 
   * @param unePresence
   * @param isCodeXsl
   * @return
   */
	public static String codePourPresenceAM(String unePresence, boolean isCodeXsl) {
    return codePourPresence(unePresence, isCodeXsl, true);
  }

  /**
   * pour impressions
   * 
   * @param unePresence
   * @param isCodeXsl
   * @return
   */
  public static String codePourPresencePM(String unePresence, boolean isCodeXsl) {
    return codePourPresence(unePresence, isCodeXsl, false);
  }

  /**
   * 
   * @param unePresence
   * @param isCodeXsl
   * @param isAm
   * @return
   */
  private static String codePourPresence(String unePresence, boolean isCodeXsl, boolean isAm) {
    String presence = null;
    
    if (isAm) {
    	presence = unePresence.substring(0, 1);
    } else {
    	presence = unePresence.substring(1);
    }
    
  	String code = null;
  	if (presence.equals("1")) {
    	code = isCodeXsl ? ConstsPrint.XSL_CONGES_SERVICE_CODE_TRAVAIL : ConstsPrint.CSV_CONGES_SERVICE_CODE_TRAVAIL;
    } else if (presence.equals("2")) {
    	code = isCodeXsl ? ConstsPrint.XSL_CONGES_SERVICE_CODE_ABSENCE : ConstsPrint.CSV_CONGES_SERVICE_CODE_ABSENCE;
    } else if (presence.equals("3")) {
    	code = isCodeXsl ? ConstsPrint.XSL_CONGES_SERVICE_CODE_ABSENCE_EN_COURS_VAL : ConstsPrint.CSV_CONGES_SERVICE_CODE_ABSENCE_EN_COURS_VAL;
    } else {
    	code = isCodeXsl ? ConstsPrint.XSL_CONGES_SERVICE_CODE_NON_TRAVAIL : ConstsPrint.CSV_CONGES_SERVICE_CODE_NON_TRAVAIL;
    }
    
  	return code;
  }

  /**
   * 
   * @param affAnn
   * @param typePlanningEnCours
   * @param debutMois
   * @return
   */
  public static NSArray presencesHorairesPourAffectation(
  		EOAffectationAnnuelle affAnn, String typePlanningEnCours, NSTimestamp debutMois) {
    NSArray presencesHoraires = affAnn.presenceMensuelle(typePlanningEnCours, debutMois);
    return presencesHoraires;
  }
	
}
