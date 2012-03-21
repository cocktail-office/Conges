package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;

/**
 * Page de gestion des DRH
 * 
 * @author ctarade
 */
public class AdminDRH 
	extends A_MembreGroupe {
  
	public AdminDRH(WOContext context) {
		super(context);
	}
	

	@Override
	public String cStructureGroupe() {
		return app.cStructureDrh();
	}
}