
package fr.univlr.cri.ycrifwk.utils;

import fr.univlr.cri.util.StringCtrl;

public class UtilString {

	
	/**
	 * permet de tester qu'il s'agit d'un nombre
	 * @param chaine
	 * @return
	 */
	public static boolean checkNumber(String chaine) {
		if (!StringCtrl.isEmpty(chaine)) {
			try {Integer.parseInt(chaine);}
			catch (Throwable e) { return false;}
			return true;
		}
		else return false;
	}
	
	/**
	 * ne conserve que les caracteres de goodChar de la chaine. reduit la chaine ne consequence
	 * @param chaine
	 * @param goodChar
	 * @return
	 */
	public static String onlyKeep(String chaine, String goodChar) {
		String newChaine = "";
		for (int i=0;i<chaine.length();i++) {
			String currentChar = Character.toString(chaine.charAt(i));
			if (StringCtrl.like(goodChar,"*"+currentChar+"*") == true)
				newChaine += currentChar;
		}
		return newChaine;
	}

	/**
	 * retourne si la chaine de type nom est acceptable par la base (pas de chiffre, que des lettres ou des -)
	 * @param chaine
	 * @return
	 */
	public static String toValideNom(String chaine) {
		return onlyKeep(chaine.toUpperCase(),"ABCDEFGHIJKLMNOPQRSTUVWXYZÀÁÂÃÄÇÈÉÊËÌÍÎÏÑÒÓÔÕÖÙÚÛÜÝ-");
	}
	
	/**
	 * retourne si la chaine de type ville est acceptable par la base (pas de chiffre, que des lettres des espace ou des -)
	 * @param chaine
	 * @return
	 */
	public static String toValideVille(String chaine) {
		return onlyKeep(chaine.toUpperCase(),"ABCDEFGHIJKLMNOPQRSTUVWXYZÀÁÂÃÄÇÈÉÊËÌÍÎÏÑÒÓÔÕÖÙÚÛÜÝ- ");
	}
	
	/**
	 * permet de savoir si le nom a un format correct
	 * @param chaine
	 * @return
	 */
	public static boolean isValideNom(String chaine) {
		return (chaine.equals(toValideNom(chaine)));
	}
	
	
	/**
	 * permet de valider un code postal FRANCAIS
	 */
	public static boolean isValideCodePostalFR(String codePostal) {
		boolean isNumber = false;
		int value = 0;
		try {value = Integer.parseInt(codePostal); isNumber = true; }
		catch (Throwable e) { }
		return (isNumber && (value > 0) && (value < 100000))	;
	}
	
	
	/**
	 * permet de savoir si une chaine ne contient que des chiffres
	 * @param chaine
	 * @param goodChar
	 * @return
	 */
	public static boolean containsOnlyNumbers(String chaine) {
		for (int i=0;i<chaine.length();i++) {
		  if (!StringCtrl.isBasicDigit((char)chaine.charAt(i)))
				return false;
		}
		return true;
	}
}
