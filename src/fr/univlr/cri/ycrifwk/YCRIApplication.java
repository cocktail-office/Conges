package fr.univlr.cri.ycrifwk;
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


import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSTimeZone;

import fr.univlr.cri.webapp.CRIWebApplication;


/**
 * @author ctarade
 */
public abstract class YCRIApplication extends CRIWebApplication {

  private EOEditingContext ec;
    
  public void initApplication() {
    super.initApplication();
/*        
    if (debugLevel() >= 2) {		
      NSLog.setAllowedDebugLevel(NSLog.DebugLevelDetailed);
      NSLog.allowDebugLoggingForGroups(NSLog.DebugGroupSQLGeneration);
      NSLog.allowDebugLoggingForGroups(NSLog.DebugGroupDatabaseAccess);
    }
  */      
    // settage du timezone 
        
    java.util.TimeZone tz = java.util.TimeZone.getTimeZone(appTimeZone());
    java.util.TimeZone.setDefault(tz);
    NSTimeZone.setDefault(tz);		
		
  }
  
  // DEFINITIONS OBLIGATOIRES
    
  /**
   * table de config "custom" pour l'appli
   */
  public abstract String customConfigTableName();

	/**
	 * editingcontext local a la classe Application
	 * @return
	 */
	public EOEditingContext ec() {
		if (ec == null) {
			ec = new EOEditingContext();		  
		}
		return ec;
	}

	@Deprecated
	protected final static String APP_URL_KEY = "APP_URL";
	
  public final static String APP_USE_CAS_KEY = "APP_USE_CAS";
  public final static String MAIN_LOGO_URL_KEY = "MAIN_LOGO_URL";

	private Boolean appUseCas, appLogConnexion;	
	private String appAdminMail, appTimeZone, mainLogoUrl, mainWebSiteUrl;
	private Integer debugLevel;

	public boolean appLogConnexion() 	{	if (appLogConnexion == null) 	{	appLogConnexion = new Boolean("YES".equals(config().stringForKey("APP_LOG_CONNEXION")));} return appLogConnexion.booleanValue();}
	public boolean appUseCas() 			{	if (appUseCas == null) 			{	appUseCas = new Boolean("YES".equals(config().stringForKey(APP_USE_CAS_KEY)));} return appUseCas.booleanValue();}
	public String appAdminMail()		{	if (appAdminMail == null) 		{   appAdminMail = config().stringForKey("APP_ADMIN_MAIL");   };	return appAdminMail; }
	public String appTimeZone()			{	if (appTimeZone == null) 		{   appTimeZone = config().stringForKey("APP_TIME_ZONE");   };	return appTimeZone; }
	public int debugLevel() 			{	if (debugLevel == null) 		{	debugLevel = new Integer(Integer.parseInt(config().stringForKey("DEBUG_LEVEL")));} return debugLevel.intValue();}
	public String mainLogoUrl()         {   if (mainLogoUrl == null)        {   mainLogoUrl = config().stringForKey(MAIN_LOGO_URL_KEY);} return mainLogoUrl; }
	public String mainWebSiteUrl()      {   if (mainWebSiteUrl == null)     {   mainWebSiteUrl = config().stringForKey("MAIN_WEB_SITE_URL");  }  return mainWebSiteUrl;     }
      
}