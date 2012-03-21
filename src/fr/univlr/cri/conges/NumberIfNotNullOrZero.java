package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOComponent;

/**
 * Composant n'affichant le nombre en binding
 * uniquement s'il est non null et différent de 0
 * 
 * @author ctarade
 *
 */
public class NumberIfNotNullOrZero extends WOComponent {
  
	public Number value;
	public String numberformat;
	
	public NumberIfNotNullOrZero(WOContext context) {
		super(context);
	}
	
	/**
	 * @return
	 */
	public boolean showValue() {
		return value != null && ((Float) value).floatValue() != (float) 0;
	}
}