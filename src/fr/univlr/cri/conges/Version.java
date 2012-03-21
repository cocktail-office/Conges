/*
 * Copyright COCKTAIL (www.cocktail.org), 1995, 2010 This software
 * is governed by the CeCILL license under French law and abiding by the
 * rules of distribution of free software. You can use, modify and/or
 * redistribute the software under the terms of the CeCILL license as
 * circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * As a counterpart to the access to the source code and rights to copy, modify
 * and redistribute granted by the license, users are provided only with a
 * limited warranty and the software's author, the holder of the economic
 * rights, and the successive licensors have only limited liability. In this
 * respect, the user's attention is drawn to the risks associated with loading,
 * using, modifying and/or developing or reproducing the software by the user
 * in light of its specific status of free software, that may mean that it
 * is complicated to manipulate, and that also therefore means that it is
 * reserved for developers and experienced professionals having in-depth
 * computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling
 * the security of their systems and/or data to be ensured and, more generally,
 * to use and operate it in the same conditions as regards security. The
 * fact that you are presently reading this means that you have had knowledge
 * of the CeCILL license and that you accept its terms.
 */
package fr.univlr.cri.conges;

import fr.univlr.cri.webapp.VersionCocktail;

/**
 * Classe descriptive de la version et dependances de l'application HAmAC
 * 
 * @author ctarade
 */
public class Version extends VersionCocktail {

	// nom de l'application
	public String name() {
		return "HAmAC";
	}

	// date de publication
	public String date() {
		return "15/03/2012";
	}

	// numéro majeur
	public int versionNumMaj() {
		return 2;
	}

	// numéro mineur
	public int versionNumMin() {
		return 10;
	}

	// numéro de patch
	public int versionNumPatch() {
		return 6;
	}

	// numéro de build
	public int versionNumBuild() {
		return 9;
	}

	// commentaire
	public String comment() {
		return "";
	}

	// liste des dependances
	public VersionCocktailRequirements[] dependencies() {
		return new VersionCocktailRequirements[] {
				new VersionCocktailRequirements(new fr.univlr.cri.webapp.Version(), "3.1", "3", true),
				new VersionCocktailRequirements(new fr.univlr.cri.webext.Version(), "3.1", "3", true),
				new VersionCocktailRequirements(new fr.univlr.cri.webapp.VersionCocktailWebObjects(), "5.3", null, false),
				new VersionCocktailRequirements(new VersionOracleDbConges(), "2.10.6", null, true),
				new VersionCocktailRequirements(new fr.univlr.cri.webapp.VersionCocktailJava(), "1.5", "1.6", false) /*
																																																							 * ,
																																																							 * new
																																																							 * VersionCocktailRequirements
																																																							 * (
																																																							 * new
																																																							 * fr
																																																							 * .
																																																							 * univlr
																																																							 * .
																																																							 * cri
																																																							 * .
																																																							 * planning
																																																							 * .
																																																							 * Version
																																																							 * (
																																																							 * )
																																																							 * ,
																																																							 * "0.8"
																																																							 * ,
																																																							 * null
																																																							 * ,
																																																							 * false
																																																							 * )
																																																							 */};
	}
}
