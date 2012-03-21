package fr.univlr.cri.conges;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.eos.modele.grhum.EOIndividu;
import fr.univlr.cri.conges.eos.modele.grhum.EORepartition;
import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;
import fr.univlr.cri.ycrifwk.utils.UtilDb;

/**
 * Gestion de l'appartenance de personnes à un groupe de l'annuaire
 * 
 * @author ctarade
 */
public abstract class A_MembreGroupe 
	extends YCRIWebPage {

	/** le display group affichant la liste des membres du groupe */
	public WODisplayGroup repartStructureDg;
	
	/** item de la repetition */
	public EORepartition repartStructureItem;
	
	/** nouvel membre */
	public EOIndividu individu;
	
	public A_MembreGroupe(WOContext context) {
		super(context);
		initComponent();
	}

	/**
	 * Initialisation 
	 * - binder le groupe d'appartenant a celui designe dans la
	 * 	configuration de l'application
	 */
	private void initComponent() {
		repartStructureDg.queryBindings().setObjectForKey(
				cStructureGroupe(), "cStructure");
		repartStructureDg.fetch();
	}

	/**
	 * Suppression d'un membre
	 * @return
	 * @throws Throwable 
	 */
	public WOComponent doDeleteRepartStructureItem() throws Throwable {
		edc.deleteObject(repartStructureItem);
		try {
			UtilDb.save(edc, false);
			// rafraichir
			repartStructureDg.fetch();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Ajout d'un nouveau membre
	 * @return
	 * @throws Throwable 
	 */
	public WOComponent doAddMembre() throws Throwable {
		// individu selectionné ?
		if (individu != null) {
			// s'assurer qu'il n'est pas deja membre
			if (!((NSArray) repartStructureDg.displayedObjects().valueForKeyPath(
					EORepartition.INDIVIDU_KEY + "." + EOIndividu.PERS_ID_KEY)).containsObject(individu.persId())) {
				EORepartition.createRepartition(
						edc, individu, getStructure());
				try {
					UtilDb.save(edc, false);
					// rafraichir
					repartStructureDg.fetch();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * L'enregistrement {@link EOStructure} associé à {@link #cStructureGroupe()}
	 * @return
	 */
	private EOStructure getStructure() {
		return EOStructure.findStructureForCstructureInContext(edc, cStructureGroupe());
	}
	
	// methodes a surcharger par la superclasse
	
	/**
	 * le code du groupe pour lequel on doit faire la gestion
	 */
	public abstract String cStructureGroupe();
	
}
