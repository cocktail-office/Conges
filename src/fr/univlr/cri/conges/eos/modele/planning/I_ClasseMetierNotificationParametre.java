package fr.univlr.cri.conges.eos.modele.planning;

/**
 * Motif des classes utilisant des paramètres globaux.
 * Chacune d'entre elle doit implementer une méthode statique :
 * public static void initStaticField(Parametre parametre)
 * et oui ... ça ne marche pas les methodes static en interface :( 
 * 
 * @author ctarade
 */
public interface I_ClasseMetierNotificationParametre {

	public final static String INIT_STATIC_FIELD_STATIC_METHOD_NAME = "initStaticField";
	
}
