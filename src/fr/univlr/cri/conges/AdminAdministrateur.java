package fr.univlr.cri.conges;

import com.webobjects.appserver.WOContext;

/**
 * Page de gestion des administrateurs
 * 
 * @author ctarade
 */
public class AdminAdministrateur 
	extends A_MembreGroupe {

	public AdminAdministrateur(WOContext context) {
		super(context);
	}

	@Override
	public String cStructureGroupe() {
		return app.cStructureAdmin();
	}
}