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

import fr.univlr.cri.webapp.VersionCocktailOracleUser;

/**
 * Classe de controle de la version du user Oracle CONGES
 *
 * @author ctarade
 */
public class VersionOracleDbConges extends VersionCocktailOracleUser {

  // nom de ce qui est controle (pour affichage dans les logs)
  public String name() {
    return "CONGES ORACLE user";
  }

  // la table contenant la liste des versions de user
  public String dbUserTableName() {	
    return "CONGES.DB_VERSION";
  }
  
  // l'attribut contenant la date de la version
  public String dbVersionDateColumnName() {
    return "DB_VERSION_DATE";
  }
  
  // l'attribut contenant l'id de la version
  public String dbVersionIdColumnName(){
    return "DB_VERSION_LIBELLE";
  }

  // mettre ici les dependances du user (version oracle, autres version users ...)
  public VersionCocktailRequirements[] dependencies() {
    return null;
  }
}