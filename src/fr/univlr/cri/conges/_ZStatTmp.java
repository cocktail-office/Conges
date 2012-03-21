package fr.univlr.cri.conges;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import fr.univlr.cri.conges.databus.CngDroitBus;
import fr.univlr.cri.conges.databus.CngPreferenceBus;
import fr.univlr.cri.conges.databus.CngUserInfo;
import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.planning.EOAffectationAnnuelle;
import fr.univlr.cri.conges.eos.modele.planning.EOCalculAffectationAnnuelle;
import fr.univlr.cri.conges.objects.Planning;
import fr.univlr.cri.conges.print.ConstsPrint;
import fr.univlr.cri.conges.utils.DateCtrlConges;
import fr.univlr.cri.conges.utils.TimeCtrl;
import fr.univlr.cri.webapp.LRLog;
import fr.univlr.cri.webapp.LRSort;

public class _ZStatTmp {

	
	public static void doStats() {
  	// 0 à 567
		// a decommenter, ligne par ligne (1 seule ligne a chaque fois)
  	//statACricri(0, 100);
  	//statACricri(100, 200);
  	//statACricri(200, 300);
  	//statACricri(300, 400);
  	//statACricri(400, 500);
  	//statACricri(500, 567);
		statACricri();
	}
	
  private static void statACricri() {
  	// la stat a son cricri le retour
  	Session session = new Session();
  	NSTimestamp dateRef = session.debutAnnee();
  	NSArray listAffAnn = EOAffectationAnnuelle.findAffectationsAnnuelleInContext(session.ec(), null, null, dateRef);
  	LRLog.log(">> listAffAnn="+listAffAnn.count() + " au " + DateCtrlConges.dateToString(dateRef));
  	listAffAnn = LRSort.sortedArray(listAffAnn, 
  			EOAffectationAnnuelle.INDIVIDU_KEY + "." + EOIndividu.NOM_KEY);
  	
  	EOEditingContext ec = new EOEditingContext();
  	CngUserInfo ui = new CngUserInfo(new CngDroitBus(ec), new CngPreferenceBus(ec), ec, new Integer(3065));
  	StringBuffer sb = new StringBuffer();
  	sb.append("service").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
  	sb.append("agent").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
  	sb.append("contractuel").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
   	sb.append("travaillees").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
   	sb.append("conges").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
   	sb.append("dues").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
   	sb.append("restant");
   	sb.append(ConstsPrint.CSV_NEW_LINE);
   	
   
  	for (int i = 0; i < listAffAnn.count(); i++) {
  		EOAffectationAnnuelle itemAffAnn = (EOAffectationAnnuelle) listAffAnn.objectAtIndex(i);
  		//
  		EOEditingContext edc = new EOEditingContext();
  		//
  		NSArray array = EOAffectationAnnuelle.findSortedAffectationsAnnuellesForOidsInContext(
  				edc, new NSArray(itemAffAnn.oid()));
  		// charger le planning pour forcer le calcul
  		Planning p = Planning.newPlanning((EOAffectationAnnuelle) array.objectAtIndex(0), ui, dateRef);
  		// quel les contractuels
  		//if (p.affectationAnnuelle().individu().isContractuel(p.affectationAnnuelle())) {
  		try {p.setType("R");
  		EOCalculAffectationAnnuelle calcul = p.affectationAnnuelle().calculAffAnn("R");
  		int minutesTravaillees3112 = calcul.minutesTravaillees3112().intValue();
  		int minutesConges3112 = calcul.minutesConges3112().intValue();
  		
  		// calcul des minutes dues
  		int minutesDues3112 = /*0*/ 514*60;
  		/*	NSArray periodes = p.affectationAnnuelle().periodes();
  		for (int j=0; j<periodes.count(); j++) {
  			EOPeriodeAffectationAnnuelle periode = (EOPeriodeAffectationAnnuelle) periodes.objectAtIndex(j);
    		minutesDues3112 += periode.valeurPonderee(p.affectationAnnuelle().minutesDues(), septembre01, decembre31);
  		}*/
  		sb.append(p.affectationAnnuelle().structure().libelleCourt()).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
  		sb.append(p.affectationAnnuelle().individu().nomComplet()).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
  		sb.append(p.affectationAnnuelle().individu().isContractuel(p.affectationAnnuelle())?"O":"N").append(ConstsPrint.CSV_COLUMN_SEPARATOR);
  		sb.append(TimeCtrl.stringForMinutes(minutesTravaillees3112)).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
  		sb.append(TimeCtrl.stringForMinutes(minutesConges3112)).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
  		sb.append(TimeCtrl.stringForMinutes(minutesDues3112)).append(ConstsPrint.CSV_COLUMN_SEPARATOR);
  		sb.append(TimeCtrl.stringForMinutes(minutesTravaillees3112 - minutesConges3112 - minutesDues3112)).append(ConstsPrint.CSV_NEW_LINE);
  		LRLog.log((i+1)+"/"+listAffAnn.count() + " (" + p.affectationAnnuelle().individu().nomComplet() + ")");
  		} catch (Throwable e) {
  			e.printStackTrace();
  		}
  		edc.dispose();
  		//}
  	}
  	
		String fileName = "/tmp/stat_000_"+listAffAnn.count()+".csv";
  	try {
			BufferedWriter fichier = new BufferedWriter(new FileWriter(fileName));
			fichier.write(sb.toString());
			fichier.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			LRLog.log("writing " + fileName);
		}
  }
  
	
}
