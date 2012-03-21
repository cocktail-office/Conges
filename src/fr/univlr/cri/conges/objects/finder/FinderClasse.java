/*
 * Copyright Université de La Rochelle 2005
 *
 * ctarade@univ-lr.fr
 *
 * Ce logiciel est un programme informatique servant à gérer les comptes
 * informatiques des utilisateurs. 
 * 
 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".

 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.

 * A cet égard  l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement,  à l'utilisation,  à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant 
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à 
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement, 
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité. 

 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez 
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 */
package fr.univlr.cri.conges.objects.finder;

import java.lang.reflect.InvocationTargetException;

import com.webobjects.foundation.NSArray;

import fr.univlr.cri.conges.eos.modele.planning.EOTypeOccupation;
import fr.univlr.cri.util.StringCtrl;

public class FinderClasse {

  private final static String OCCUPATION_PACKAGE    = "fr.univlr.cri.conges.objects.occupations.";
  private final static String DEFAULT_CLASS         = "Occupation";
  
  /**
   * retourne une instance du constructeur de la classe metier d'un type d'absence
   * @param absence
   * @return
   */
  public static Object findClassForType(EOTypeOccupation type, Object[] arguments, Class[] argumentTypes) {
    String className = StringCtrl.capitalizeWords(type.libelle());
    className = (NSArray.componentsSeparatedByString(className, " ")).componentsJoinedByString("");
    Class theClass = null;
    try {
      theClass = Class.forName(OCCUPATION_PACKAGE + className);
    } catch (ClassNotFoundException e) {
      // class non trouvee : on prend la classe Occupation
      try {
        theClass = Class.forName(OCCUPATION_PACKAGE + DEFAULT_CLASS);
      } catch (ClassNotFoundException e1) {
        e1.printStackTrace();
      }
    }
    try {
      return theClass.getConstructor(argumentTypes).newInstance(arguments);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }

    return null;
  }
  
}
