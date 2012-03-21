package fr.univlr.cri.conges;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.woextensions.WOLongResponsePage;

import fr.univlr.cri.util.StringCtrl;


/**
 * Page de traitement (long) de le export CSV des plannings
 * 
 * @author Cyril TARADE <cyril.tarade at cocktail.org>
 */
public class AdminSuiviPlanningWorking extends WOLongResponsePage {

	private static final double REFRESH_INTERVAL = 2.0;
	
	private AdminSuiviPlanningWorkingCtrl ctrl;
	
	private int refreshCount = 0;
	private String strTexteEvolution;
	
	public AdminSuiviPlanningWorking(WOContext context) {	
		super(context);
		setRefreshInterval(REFRESH_INTERVAL);
		setCachingEnabled(false);
		strTexteEvolution = "Cr&eacute;ation du fichier CSV en cours<br><br>";
	}
	
	/**
	 * Override of appendToResponse - this method increments the count (the total
	 * number of refreshes) and sets the refresh interval to the passed in
	 * value.
	 */
	public void appendToResponse(WOResponse aResponse, WOContext aContext) {
		setRefreshInterval(REFRESH_INTERVAL);
		
		if (refreshCount%5 == 0) {
			strTexteEvolution = StringCtrl.replace(
					strTexteEvolution, "<br>||||", "<br><s>||||</s><br>");
		} else {
			strTexteEvolution += "|";
		}
		
		refreshCount++;
		
		super.appendToResponse( aResponse, aContext );
	}


	/**
	 * Override of performAction method - this is where the main computation is done
	 * for the example.  By placing the computation in invokeAction is is automatically
	 * performed when the component loads.  Here we use the current values of the
	 * start and stop values to calculate all of the primes.
	 */
	public Object performAction() {
		PrintFactory printFactory = new PrintFactory();
		/* This class does a setStatus: on the refresh page as it is computing
		 * another way of setting the status would be to implement
		 * -returnStatusPage, and in there, to have the main thread pole this object
		 * (first keep this object as an ivar) for the status.
		 * but then pnc needs a thread safe -status method.
		 */
		printFactory.doPrint();
		return null;
	}


	
	/**
	 * La classe qui fait le boulot
	 */
	private class PrintFactory {
		public void doPrint() {
			isWorking = true;
			ctrl.generateCsvResponse();
			isWorking = false;
		}		
  }
	
	/**
	 * Method to return the result page when the computation is complete.  This
	 * methods sets the result page, passes all of the computation information,
	 * and then returns the page.
	 */
	public WOComponent pageForResult(Object result) {
		return ctrl.getCaller();
	}
	
	private static boolean isWorking = false;
	
	public boolean isWorking() {
		return isWorking;
	}

	public final void setCtrl(AdminSuiviPlanningWorkingCtrl ctrl) {
		this.ctrl = ctrl;
	}

	public final String getStrTexteEvolution() {
		return strTexteEvolution;
	}
}