/*
 * Copyright Consortium Coktail, 7 juin 07
 * 
 * cyril.tarade at univ-lr.fr
 * 
 * Ce logiciel est un programme informatique servant � [rappeler les
 * caract�ristiques techniques de votre logiciel]. 
 * 
 * Ce logiciel est r�gi par la licence CeCILL soumise au droit fran�ais et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffus�e par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".
 * 
 * En contrepartie de l'accessibilit� au code source et des droits de copie,
 * de modification et de redistribution accord�s par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limit�e.  Pour les m�mes raisons,
 * seule une responsabilit� restreinte p�se sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les conc�dants successifs.
 * 
 * A cet �gard  l'attention de l'utilisateur est attir�e sur les risques
 * associ�s au chargement,  � l'utilisation,  � la modification et/ou au
 * d�veloppement et � la reproduction du logiciel par l'utilisateur �tant 
 * donn� sa sp�cificit� de logiciel libre, qui peut le rendre complexe � 
 * manipuler et qui le r�serve donc � des d�veloppeurs et des professionnels
 * avertis poss�dant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invit�s � charger  et  tester  l'ad�quation  du
 * logiciel � leurs besoins dans des conditions permettant d'assurer la
 * s�curit� de leurs syst�mes et ou de leurs donn�es et, plus g�n�ralement, 
 * � l'utiliser et l'exploiter dans les m�mes conditions de s�curit�. 
 * 
 * Le fait que vous puissiez acc�der � cet en-t�te signifie que vous avez 
 * pris connaissance de la licence CeCILL, et que vous en avez accept� les
 * termes.
 */

package fr.univlr.cri.conges.objects;

import fr.univlr.cri.conges.eos.modele.planning.EOHoraire;

/**
 * Les methodes liees a l'utilisation du planning
 * sur une interface HTML.
 * 
 * @author Cyril Tarade <cyril.tarade at univ-lr.fr>
 */
public class PlanningHtml extends A_GenericPlanningExtension {
  
  public PlanningHtml(Planning planning) {
		super(planning);
  }

	/**
   * Le code css associe aux horaires declares sur ce planning
   */
  public String textCssHoraire() {
    StringBuffer text = new StringBuffer();
    if (planning().horairesHebdomadaires() != null) {
      for (int i = 0; i < planning().horairesHebdomadaires().count(); i++) {
        EOHoraire h = ((HoraireHebdomadaire) planning().horairesHebdomadaires().objectAtIndex(i)).horaire();
        text.append(".H_").append(h.oid()).append("_ {\n");
        text.append("width: 75px;\n");  
        text.append("height: 15px;\n");
        text.append("left: 0;\n");
        text.append("top: 0px;\n");
        text.append("border-bottom: 1px solid #123fa9;\n");
        text.append("background-color: ").append(h.couleur()).append(" ;\n}\n");
      }
    }
    return text.toString();
  }
  
  /**
   * Le code JS permettant de cacher la couleur des horaires
   * sur le planning
   */
  private String textJScriptHideHoraire() {
    StringBuffer text = new StringBuffer("");
    text.append("SwitchObjectsForClass('H_','").append(Semaine.classeCssSemaineActive).append("');");
    return text.toString();
  }

  /**
   * Le code JS permettant de montrer la couleur des horaires
   * sur le planning
   */
  private String textJScriptShowHoraire() {
    StringBuffer text = new StringBuffer("");
    if (planning().horairesHebdomadaires() != null) {
      for (int i = 0; i < planning().horairesHebdomadaires().count(); i++) {
        EOHoraire h = ((HoraireHebdomadaire) planning().horairesHebdomadaires().objectAtIndex(i)).horaire();
        text.append("SwitchObjectsForClass('_").append(h.oid()).append("_','H_").append(h.oid()).append("_');");
      }
    }
    return text.toString();
  }

  /**
   * Le code JS permettant de cacher les occupations
   * sue le planning
   */
  private String textJScriptHideOccupation() {
    StringBuffer text = new StringBuffer("");
    
    text.append("SwitchObjectsForClass('").append(Jour.CSS_ID_JOUR_NORMAL).append("', '").append(Jour.CSS_CLASS_JOUR_NORMAL).append("');");
    // remettre les jours feries - chomes ...
    text.append("SwitchObjectsForClass('").append(Jour.CSS_ID_JOUR_CHOME).append("', '").append(Jour.CSS_CLASS_JOUR_CHOME).append("');");
    text.append("SwitchObjectsForClass('").append(Jour.CSS_ID_JOUR_HORS_AFFECTATION).append("', '").append(Jour.CSS_CLASS_JOUR_HORS_AFFECTATION).append("');");
    text.append("SwitchObjectsForClass('").append(Jour.CSS_ID_JOUR_SANS_STATUT).append("', '").append(Jour.CSS_CLASS_JOUR_SANS_STATUT).append("');");
 
    return text.toString();
  }
  
  /**
   * Le code JS permettant de montrer les occupations
   * sue le planning
   */
  private String textJScriptShowOccupation() {
    StringBuffer text = new StringBuffer("");

    text.append("SwitchObjectsForClass('").append(Jour.CSS_ID_JOUR_NORMAL).append("', '").append(Jour.CSS_CLASS_JOUR_NORMAL).append("');");
    text.append("SwitchObjectsForClass('").append(Jour.CSS_ID_JOUR_FERME).append("', '").append(Jour.CSS_CLASS_JOUR_FERME).append("');");
    text.append("SwitchObjectsForClass('").append(Jour.CSS_ID_JOUR_CONGE).append("', '").append(Jour.CSS_CLASS_JOUR_CONGE).append("');");
    text.append("SwitchObjectsForClass('").append(Jour.CSS_ID_JOUR_CONGE_V).append("', '").append(Jour.CSS_CLASS_JOUR_CONGE_V).append("');");
    text.append("SwitchObjectsForClass('").append(Jour.CSS_ID_JOUR_CONGE_S).append("', '").append(Jour.CSS_CLASS_JOUR_CONGE_S).append("');");
    text.append("SwitchObjectsForClass('").append(Jour.CSS_ID_JOUR_CONGE_AM).append("', '").append(Jour.CSS_CLASS_JOUR_CONGE_AM).append("');");
    text.append("SwitchObjectsForClass('").append(Jour.CSS_ID_JOUR_CONGE_AM_V).append("', '").append(Jour.CSS_CLASS_JOUR_CONGE_AM_V).append("');");
    text.append("SwitchObjectsForClass('").append(Jour.CSS_ID_JOUR_CONGE_AM_S).append("', '").append(Jour.CSS_CLASS_JOUR_CONGE_AM_S).append("');");
    text.append("SwitchObjectsForClass('").append(Jour.CSS_ID_JOUR_CONGE_PM).append("', '").append(Jour.CSS_CLASS_JOUR_CONGE_PM).append("');");
    text.append("SwitchObjectsForClass('").append(Jour.CSS_ID_JOUR_CONGE_PM_V).append("', '").append(Jour.CSS_CLASS_JOUR_CONGE_PM_V).append("');");
    text.append("SwitchObjectsForClass('").append(Jour.CSS_ID_JOUR_CONGE_PM_S).append("', '").append(Jour.CSS_CLASS_JOUR_CONGE_PM_S).append("');");
    text.append("SwitchObjectsForClass('").append(Jour.CSS_ID_JOUR_CONGE_LEGAL).append("', '").append(Jour.CSS_CLASS_JOUR_CONGE_LEGAL).append("');");
    text.append("SwitchObjectsForClass('").append(Jour.CSS_ID_JOUR_CONGE_LEGAL_AM).append("', '").append(Jour.CSS_CLASS_JOUR_CONGE_LEGAL_AM).append("');");
    text.append("SwitchObjectsForClass('").append(Jour.CSS_ID_JOUR_CONGE_LEGAL_PM).append("', '").append(Jour.CSS_CLASS_JOUR_CONGE_LEGAL_PM).append("');");
    text.append("SwitchObjectsForClass('").append(Jour.CSS_ID_JOUR_CONGE_COMP).append("', '").append(Jour.CSS_CLASS_JOUR_CONGE_COMP).append("');");
    text.append("SwitchObjectsForClass('").append(Jour.CSS_ID_JOUR_CONGE_COMP_V).append("', '").append(Jour.CSS_CLASS_JOUR_CONGE_COMP_V).append("');");
    text.append("SwitchObjectsForClass('").append(Jour.CSS_ID_JOUR_CONGE_COMP_S).append("', '").append(Jour.CSS_CLASS_JOUR_CONGE_COMP_S).append("');");
    // remettre les jours feries - chomes ...
    text.append("SwitchObjectsForClass('").append(Jour.CSS_ID_JOUR_CHOME).append("', '").append(Jour.CSS_CLASS_JOUR_CHOME).append("');");
    text.append("SwitchObjectsForClass('").append(Jour.CSS_ID_JOUR_HORS_AFFECTATION).append("', '").append(Jour.CSS_CLASS_JOUR_HORS_AFFECTATION).append("');");
    text.append("SwitchObjectsForClass('").append(Jour.CSS_ID_JOUR_SANS_STATUT).append("', '").append(Jour.CSS_CLASS_JOUR_SANS_STATUT).append("');");
    
    return text.toString();
  }
  
  /**
   * Le code JS de gestion du coche d'affichage d'horaire
   * si coche : on affiche sinon on cache
   */
  public String textJScriptCheckboxHoraire() {
    StringBuffer text = new StringBuffer("javascript:");
    text.append("if (checked) {").append(textJScriptShowHoraire()).append("}");
    text.append("else {").append(textJScriptHideHoraire()).append("}");
    return text.toString();
  }
  
  /**
   * Le code JS de gestion du coche d'affichage d'occupations
   * si coche : on affiche sinon on cache
   */
  public String textJScriptCheckboxOccupation() {
    StringBuffer text = new StringBuffer("javascript:");
    text.append("if (checked) {").append(textJScriptShowOccupation()).append("}");
    text.append("else {").append(textJScriptHideOccupation()).append("}");
    return text.toString();
  }
  
  /**
   * Lors du chargement de la page du planning, on doit afficher
   * ou pas les horaires et les conges. Par defaut, la page 
   * contient les horaires et les occupations. On ne fait donc que
   * du masquage eventuel dans cette methode.
   */
  public String textJScriptOnloadBodyPlanning(boolean showHoraire, boolean showOccupation) {
    StringBuffer text = new StringBuffer("\"javascript:");
    
    if (!showHoraire)
      text.append(textJScriptHideHoraire());
    
    if (!showOccupation) 
      text.append(textJScriptHideOccupation());
    
    text.append("\"");
    
    return text.toString();
  }
	
}
