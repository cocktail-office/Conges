/*
 * Copyright Universit� de La Rochelle 2004
 *
 * ctarade@univ-lr.fr
 *
 * Ce logiciel est un programme informatique servant � g�rer les comptes
 * informatiques des utilisateurs. 
 * 
 * Ce logiciel est r�gi par la licence CeCILL soumise au droit fran�ais et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffus�e par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".

 * En contrepartie de l'accessibilit� au code source et des droits de copie,
 * de modification et de redistribution accord�s par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limit�e.  Pour les m�mes raisons,
 * seule une responsabilit� restreinte p�se sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les conc�dants successifs.

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

 * Le fait que vous puissiez acc�der � cet en-t�te signifie que vous avez 
 * pris connaissance de la licence CeCILL, et que vous en avez accept� les
 * termes.
 */


package fr.univlr.cri.conges.objects;

import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

import fr.univlr.cri.conges.constantes.ConstsDroit;
import fr.univlr.cri.conges.eos.modele.conges.EODroit;
import fr.univlr.cri.conges.eos.modele.grhum.EOStructure;
import fr.univlr.cri.util.ArrayCtrl;
import fr.univlr.cri.webapp.CRIDataBus;

/**
 * Objet representant l'ensemble des droits d'un agent sur 1 service :
 * - planning du service
 * - chef du service
 * - tout le service
 * Il ne doit y avoir qu'au plus 1 objet responsabilite par service.
 * 
 * @author ctarade
 */
public class Responsabilite {

	private EOStructure structure;			// service associ�
	private Number niveauParDefautSurService;			// niveau par defaut sur toutes les affectation du service
	private NSMutableArray droitList;		// liste des droits particuliers, si differents du niveau par defaut

	public Responsabilite(EOStructure aStructure, Number aNiveauParDefautSurService) {
		super();		
		droitList = new NSMutableArray();
		structure = aStructure;
		niveauParDefautSurService = aNiveauParDefautSurService;
	}


	/**
	 * Insertion d'un droit dans la liste.
	 * Si le droit concerne une meme cible, mais pour un niveau
	 * different, on conserve le niveau le plus "fort"
	 * @param droitNouveau
	 * @return true si insertion faite
	 */
	public boolean addDroit(EODroit droitNouveau) {
		if (getStructure() == null) {
			structure = droitNouveau.toStructure();
		}
		structure = droitNouveau.toStructure();
		if (!getStructure().equals(droitNouveau.toStructure())) {
			return false;
		}
		
		// on essaye de trouver un droit dans la liste de meme cible
		EODroit droitCibleIdentique = null;
		if (!ArrayCtrl.isEmpty(droitList)) {
			String strQual = EODroit.TO_STRUCTURE_KEY+"=%@ and "+EODroit.CDR_TYPE_KEY+"=%@";
			NSArray args = new NSArray(new Object[]{droitNouveau.toStructure(), droitNouveau.cdrType()});
			if (droitNouveau.toIndividu() != null) {
				strQual += " and " + EODroit.TO_INDIVIDU_KEY + "=%@";
				args = args.arrayByAddingObject(droitNouveau.toIndividu());
			}
			NSArray recs = EOQualifier.filteredArrayWithQualifier(droitList, 
					CRIDataBus.newCondition(strQual, args));
			if (recs.count() > 0) {
				droitCibleIdentique = (EODroit) recs.objectAtIndex(0);
			}
		}
		
		if (droitCibleIdentique != null) {
			if (droitCibleIdentique.cdrNiveau().intValue() < droitNouveau.cdrNiveau().intValue()) {
				// celui qui existe est inferieur, on remplace
				droitList.removeIdenticalObject(droitCibleIdentique);
				droitList.addObject(droitNouveau);
			}
			// sinon, on ne fait rien
		} else {
			// pas trouve, on ajoute
			droitList.addObject(droitNouveau);
		}
		
		// echanger le niveau par defaut si le droit nouveau concerne
		// le meme service et que son niveau est sup�rieur
		if (droitNouveau.cdrType().equals(ConstsDroit.DROIT_CIBLE_SERVICE) && 
				droitNouveau.cdrNiveau().intValue() > niveauParDefautSurService.intValue()) {
			niveauParDefautSurService = droitNouveau.cdrNiveau();
		}
		
		return true;
	}
	
	/**
	 * niveau max de responsabilite sur tout le service et tous les individus
	 * @return
	 */
	public Number getNiveauMax() {
		Number niveauMax = (Number)getDroitList().valueForKey("@max."+EODroit.CDR_NIVEAU_KEY);
		if (niveauMax == null)
			return niveauParDefautSurService;
		else {
			if (niveauParDefautSurService.intValue() >= niveauMax.intValue())
				return niveauParDefautSurService;
			else
				return niveauMax;
		}
	}
	
	public final static String STRUCTURE_KEY = "structure";
	
	public EOStructure getStructure() 	{	return structure;	}

	public final static String COMPOSANTE_KEY = "composante";
	
	public EOStructure getComposante() 	{	return structure.toComposante();	}

	
	public NSArray getDroitList()			{	return droitList;		}
	public Number getNiveauParDefautSurService() {	return niveauParDefautSurService;	}


	public String toString() {
		String toString = "";
		toString 	+= "STRUCTURE : " + structure.libelleCourt() + " niveau par defaut : " + getNiveauParDefautSurService().intValue() + " niveau max : " + getNiveauMax().intValue();
		for (int i = 0; i < getDroitList().count(); i++) {
			EODroit droit = (EODroit)getDroitList().objectAtIndex(i);
			toString += "\nDroit " + i + " : " ;
			if (ConstsDroit.DROIT_CIBLE_SERVICE.equals(droit.cdrType())) {
				toString += "Service -> " + droit.toStructure().libelleCourt();
			}
			if (ConstsDroit.DROIT_CIBLE_CHEF_DE_SERVICE.equals(droit.cdrType())) {
				toString += "Chef de service -> " + droit.toStructure().display();
			}
			if (ConstsDroit.DROIT_CIBLE_INDIVIDU.equals(droit.cdrType())) {
				toString += "Individu -> " + droit.toIndividu().nomComplet();
			}
			toString += " | niveau = " + droit.cdrNiveau();
		}

		return toString;
	}

}
