<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xsl:stylesheet
[
<!ENTITY  nbsp  "&#160;">
<!ENTITY  space  "&#x20;">
<!ENTITY  br  "&#x2028;">
<!ENTITY  eaigu  "&#233;">
<!ENTITY  egrave  "&#232;">
<!ENTITY  ecircon  "&#234;">
<!ENTITY  ugrave  "&#249;">
<!ENTITY agrave "&#224;">

]>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <xsl:template match="/">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="main" margin="10mm 5mm 5mm 5mm" page-width="210mm" page-height="297mm">
                    <fo:region-before extent="5mm" margin="0pt"/>
                    <fo:region-after extent="5mm" margin="0pt"/>
                    <fo:region-start extent="5mm" margin="0pt"/>
                    <fo:region-end extent="5mm" margin="0pt"/>
                    <fo:region-body margin="5mm 10mm 10mm 10mm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="main">
                <!-- ENTETE de page -->
                <fo:static-content flow-name="xsl-region-before"/>
                <!-- ENTETE de page : END -->
                <!-- BAS de page -->
                <fo:static-content flow-name="xsl-region-after"/>
                <!-- BAS de page : END -->
                <!--AVANT -->
                <fo:static-content flow-name="xsl-region-start"/>
                <!--AVANT: END -->
                <!--APRES -->
                <fo:static-content flow-name="xsl-region-end"/>
                <!--APRES: END -->
                <!-- PAGE CONTENTS -->
                <fo:flow flow-name="xsl-region-body" font-family="Arial, Helvetica, sans-serif"
                    font-size="10pt" font-color="#000000">
                    <fo:block>
                        <xsl:apply-templates select="demande"/>
                    </fo:block>
                </fo:flow>
                <!-- PAGE CONTENTS : END-->
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    
    
    
    <!-- -->
    <!-- -->
    <!-- -->
    <xsl:template match="demande">
    
    	<!-- decision d'epargne cet -->
		
		<fo:table>
		   <fo:table-column/>
		   <fo:table-body>
			
				<!--titre -->
				<xsl:call-template name="zoneTitre">
					<xsl:with-param name="titre" select='"DÉCISION D&apos;ÉPARGNE CET"'/>
				</xsl:call-template>
		
				<!--contenu -->
				<xsl:call-template name="zoneContenuDecision">
							
				</xsl:call-template>
				
			</fo:table-body>
		</fo:table>
                
        
        <!-- tableau de droit d'option manuscrit -->
        
		<!-- demande sur le régime pérenne : transfert / épargne / droit d'option -->
        <xsl:if test="/demande/isExerciceDroitOption = 'true'">
			
			
	        <!-- saut de page -->
	       	<xsl:call-template name="sautPage"/>
			
			<fo:table>
				<fo:table-column/>
				<fo:table-body>
				
					<!--titre -->
					<xsl:call-template name="zoneTitre">
						<xsl:with-param name="titre" select="/demande/titre"/>
					</xsl:call-template>
				
					<!--contenu -->
					<xsl:call-template name="zoneContenuDroitOption">
					
					</xsl:call-template>
				
				</fo:table-body>
			</fo:table>
       
       	</xsl:if>
        
        
        
                
                
    </xsl:template>
    
    

    
    
    <!-- -->
    <!-- -->
    <!-- -->
    <xsl:template name="zoneTitre">
  	    <xsl:param name="titre"/>
  	    <fo:table-row>
		<fo:table-cell>
        <fo:table>
            <fo:table-column column-width="20mm"/>
            <fo:table-column column-width="10mm"/>
            <fo:table-column/>
            <fo:table-body>
                <fo:table-row>
                    <fo:table-cell display-align="top" text-align="left">
                        <fo:block>
                            <!-- Affichage du logo de l'universite joint au fichier xsl et xml-->
                            <fo:external-graphic  height="20mm" width="20mm">
                            	<xsl:attribute name="src"><xsl:value-of select="/demande/mainLogoUrl"/></xsl:attribute>
                            </fo:external-graphic>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell/>
                    <fo:table-cell border-width="0.5pt" border-style="solid" text-align="center" display-align="center">
                    	<fo:table>
							<fo:table-column/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell text-align="center" font-size="15pt">
										<!--titre L1-->
										<fo:block>
											<xsl:value-of select="$titre"/>
				       	         		</fo:block>
                 	 		 		 </fo:table-cell>
				                </fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
		</fo:table-cell>
		</fo:table-row>
		<!--saut de ligne -->
        <xsl:call-template name="sautLigne"/>
    </xsl:template>
    
    
    
    <!-- -->
    <!-- -->
    <!-- contenu droit d'option -->
    <xsl:template name="zoneContenuDroitOption">
    	<fo:table-row>
        	<fo:table-cell>
				<fo:table>
					<fo:table-column column-width="15mm"/>
					<fo:table-column column-width="30mm"/>
					<fo:table-column/>
					<fo:table-column column-width="50mm"/>
					<fo:table-column column-width="15mm"/>
					<fo:table-body>
	
	
						<!--sauts de ligne -->
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>

						<!-- décret -->
					  	<fo:table-row>
					  		<fo:table-cell/>
							<fo:table-cell number-columns-spanned="3">
								<fo:block>
									vu le décret 2002-634 du 29 avril 2002 modifié ;&br;
									vu le décret n°2009-1065 du 28 août 2009 ;
								</fo:block>
							</fo:table-cell>
					  		<fo:table-cell/>
						</fo:table-row>
						
						<!--sauts de ligne -->
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						
						<!-- je soussigné -->
						<fo:table-row>
					  		<fo:table-cell/>
							<fo:table-cell number-columns-spanned="3">
								<fo:block text-align="left">
									Je soussigné
								</fo:block>
							</fo:table-cell>
					  		<fo:table-cell/>
						</fo:table-row>
						<xsl:call-template name="identiteDemandeur"/>
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						<fo:table-row>
					  		<fo:table-cell/>
							<fo:table-cell number-columns-spanned="3">
								<fo:block>
									détenteur d'un compte épargne temps crédité à la date du <xsl:value-of select="dateValeurCet"/>,
									de <xsl:value-of select="soldeCetApresDecisionTotalEnJours7h00"/> jours à 7h00, souhaite exercer mon droit d'option sur les jours dépassant
									des 20 jours mentionnées aux articles 5 et 6 du décret 2002-634 dans les conditions suivantes :
								</fo:block>
							</fo:table-cell>
					  		<fo:table-cell/>
						</fo:table-row>
						
						<!--sauts de ligne -->
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						
						<!-- tableau -->
						<fo:table-row>
					  		<fo:table-cell/>
							<fo:table-cell number-columns-spanned="3"> 
								<fo:table font-size="12pt" text-align="center" display-align="center" border-width="0.5pt" border-style="solid">
									<fo:table-column/>
									<fo:table-column/>
									<fo:table-column/>
									<fo:table-column/>
									<fo:table-column/>
									<fo:table-body>
						
										<fo:table-row background-color="#DDDDDD">
											<fo:table-cell border-top-width="0.5pt" border-top-style="solid" border-left-width="0.5pt" border-left-style="solid" border-right-width="0.5pt" border-right-style="solid"><fo:block/></fo:table-cell>
											<fo:table-cell border-top-width="0.5pt" border-top-style="solid" border-right-width="0.5pt" border-right-style="solid"><fo:block/></fo:table-cell>
											<fo:table-cell border-top-width="0.5pt" border-top-style="solid" border-right-width="0.5pt" border-right-style="solid" number-columns-spanned="3">
												<fo:block>
													Usage de jours à maintenir
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										
										
										<fo:table-row background-color="#DDDDDD">
											<fo:table-cell border-left-width="0.5pt" border-left-style="solid" border-right-width="0.5pt" border-right-style="solid">
												<fo:block>
													Nombre de jours crédités
												</fo:block>
											</fo:table-cell>
											<fo:table-cell border-right-width="0.5pt" border-right-style="solid">
												<fo:block>
													Nombre de jours à maintenir
												</fo:block>
											</fo:table-cell>
											<fo:table-cell border-top-width="0.5pt" border-top-style="solid" border-right-width="0.5pt" border-right-style="solid">
												<fo:block>
													Indemnisation
												</fo:block>
											</fo:table-cell>
											<fo:table-cell border-top-width="0.5pt" border-top-style="solid" border-right-width="0.5pt" border-right-style="solid">
												<fo:block>
													Transfert dans le RAFP (fonctionnaire uniquement)
												</fo:block>
											</fo:table-cell>
											<fo:table-cell border-right-width="0.5pt" border-right-style="solid" border-top-width="0.5pt" border-top-style="solid">
												<fo:block>
													Maintien pour une utilisation ultérieure en congés
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
											
										<fo:table-row height="20mm">
											<fo:table-cell font-size="14pt" border-top-width="0.5pt" border-top-style="solid" border-right-width="0.5pt" border-right-style="solid">
												<fo:block>
													<xsl:value-of select="/demande/decisionTotalEnJours7h00"/>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell font-size="14pt" border-top-width="0.5pt" border-top-style="solid" border-right-width="0.5pt" border-right-style="solid">
												<fo:block>
													20
												</fo:block>
											</fo:table-cell>
											<fo:table-cell border-top-width="0.5pt" border-top-style="solid" border-right-width="0.5pt" border-right-style="solid">
												<fo:block>
												
												</fo:block>
											</fo:table-cell>
											<fo:table-cell border-top-width="0.5pt" border-top-style="solid" border-right-width="0.5pt" border-right-style="solid">
												<fo:block>
													
												</fo:block>
											</fo:table-cell>
											<fo:table-cell border-top-width="0.5pt" border-top-style="solid">
												<fo:block>
													
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										</fo:table-body>
								</fo:table>
							</fo:table-cell>
					  		<fo:table-cell/>
						</fo:table-row>
						
						<!--sauts de ligne -->
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						
						<!-- signature -->
						<fo:table-row>
					  		<fo:table-cell/>
							<fo:table-cell/>
							<fo:table-cell/>
							<fo:table-cell>
								<fo:block>
									<xsl:value-of select="ville"/>, le ..............
								</fo:block>
							</fo:table-cell>
					  		<fo:table-cell/>
						</fo:table-row>
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						<fo:table-row>
					  		<fo:table-cell/>
							<fo:table-cell/>
							<fo:table-cell/>
							<fo:table-cell>
								<fo:block>
									Signature du demandeur
								</fo:block>
							</fo:table-cell>
					  		<fo:table-cell/>
						</fo:table-row>
						
						
						<!--saut de ligne -->
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>

						<!-- decision -->
						<xsl:call-template name="encartDecision"/>
												
					</fo:table-body>
				</fo:table>
        	</fo:table-cell>
        </fo:table-row>
    </xsl:template>

     <!-- -->
    <!-- -->
    <!-- contenu decision -->
    <xsl:template name="zoneContenuDecision">
    	<fo:table-row>
        	<fo:table-cell>
				<fo:table>
					<fo:table-column column-width="15mm"/>
					<fo:table-column column-width="30mm"/>
					<fo:table-column/>
					<fo:table-column column-width="40mm"/>
					<fo:table-column column-width="15mm"/>
					<fo:table-body>
	
						<!-- le président ... -->
					  	<fo:table-row>
					  		<fo:table-cell/>
							<fo:table-cell number-columns-spanned="3">
								<fo:block text-align="center" font-size="14pt">
									<xsl:value-of select="grhumPresident"/>
								</fo:block>
							</fo:table-cell>
					  		<fo:table-cell/>
						</fo:table-row>
						
						<!--sauts de ligne -->
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						
						<!-- décret -->
					  	<fo:table-row>
					  		<fo:table-cell/>
							<fo:table-cell number-columns-spanned="3">
								<fo:block>
									vu le décret 2002-634 du 29 avril 2002 modifié ;&br;
									vu le décret n°2009-1065 du 28 août 2009 ;&br;
									vu l'arrêté du 28 août 2009 pris pour l'application du décret 2002-634 ;&br;
									vu la demande de l'intéressé(e) en date du <xsl:value-of select="dateDemande"/> sollicitant le versement sur un CET de
									<xsl:value-of select="demandeEpargneEnJours7h00"/> jour(s) à 7h00 de congés annuels acquis au titre de l'année universitaire <xsl:value-of select="anneeUnivNm1"/>
								</fo:block>
							</fo:table-cell>
					  		<fo:table-cell/>
						</fo:table-row>
						
						<!--sauts de ligne -->
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						
						<!-- décide -->
					  	<fo:table-row>
					  		<fo:table-cell/>
							<fo:table-cell/>
							<fo:table-cell>
								<fo:block text-align="center" font-size="14pt">
									<fo:inline text-decoration="underline">
										DÉCIDE
									</fo:inline>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell/>
					  		<fo:table-cell/>
						</fo:table-row>
						
						<!--sauts de ligne -->
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						
						<!-- article 1 -->
						<fo:table-row>
					  		<fo:table-cell/>
							<fo:table-cell number-columns-spanned="3">
								<fo:block text-align="left" font-size="12pt">
									<fo:inline text-decoration="underline">
										Article 1
									</fo:inline>
								</fo:block>
							</fo:table-cell>
					  		<fo:table-cell/>
						</fo:table-row>
						<xsl:call-template name="identiteDemandeur"/>
						<xsl:call-template name="sautLigne"/>
						<fo:table-row>
					  		<fo:table-cell/>
							<fo:table-cell number-columns-spanned="3">
								<fo:block>
									bénéficie du versement de <xsl:value-of select="decisionEpargneEnJours7h00"/> jour(s) à 7h00 de congés annuels acquis au titre
									de l'année universitaire <xsl:value-of select="anneeUnivNm1"/>
								</fo:block>
							</fo:table-cell>
					  		<fo:table-cell/>
						</fo:table-row>
						
						<!--sauts de ligne -->
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						
						<!-- article 2 -->
						<xsl:if test="/demande/isExerciceDroitOption = 'true'">
						
							<fo:table-row>
						  		<fo:table-cell/>
								<fo:table-cell number-columns-spanned="3">
									<fo:block text-align="left" font-size="12pt">
										<fo:inline text-decoration="underline">
											Article 2
										</fo:inline>
									</fo:block>
								</fo:table-cell>
						  		<fo:table-cell/>
							</fo:table-row>
							<fo:table-row>
						  		<fo:table-cell/>
								<fo:table-cell number-columns-spanned="3">
									<fo:block>
										L'intéressé(e) devra opter à l'aide du document joint dans les proportions qu'il souhaite concernant <xsl:value-of select="decisionTotalOptionsEnJours7h00"/>
										jour(s) à 7h00 de congés épargnés dépassant le seuil fixé par les articles 5 et 6 du décret 2002-634.&br;
										- pour le maintien des congés sur le CET (maintien annuel limité à 10 jours)&br;
										- pour l'indemnisation selon sa catégorie d'emploi&br;
										- pour la prise en compte au sein de la RAFP (fonctionnaire uniquement)
									</fo:block>
								</fo:table-cell>
						  		<fo:table-cell/>
							</fo:table-row>
							
							<!--sauts de ligne -->
							<xsl:call-template name="sautLigne"/>
							<xsl:call-template name="sautLigne"/>
						
						</xsl:if>
						
						<!-- article 3 -->
						<fo:table-row>
					  		<fo:table-cell/>
							<fo:table-cell number-columns-spanned="3">
								<fo:block text-align="left" font-size="12pt">
									<fo:inline text-decoration="underline">
										Article
										<xsl:if test="/demande/isExerciceDroitOption = 'false'">2</xsl:if>
										<xsl:if test="/demande/isExerciceDroitOption = 'true'">3</xsl:if>
									</fo:inline>
								</fo:block>
							</fo:table-cell>
					  		<fo:table-cell/>
						</fo:table-row>
						<fo:table-row>
					  		<fo:table-cell/>
							<fo:table-cell number-columns-spanned="3">
								<fo:block>
									Le Directeur général des services de <xsl:value-of select="grhumEtab"/> est chargé de l'exécution du présent arrêté.
								</fo:block>
							</fo:table-cell>
					  		<fo:table-cell/>
						</fo:table-row>
						
						<!--sauts de ligne -->
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						
						<!-- signature -->
						<fo:table-row>
					  		<fo:table-cell/>
							<fo:table-cell/>
							<fo:table-cell/>
							<fo:table-cell>
								<fo:block>
									<xsl:value-of select="ville"/> le ...........
								</fo:block>
							</fo:table-cell>
					  		<fo:table-cell/>
						</fo:table-row>
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						<fo:table-row>
					  		<fo:table-cell/>
							<fo:table-cell/>
							<fo:table-cell/>
							<fo:table-cell>
								<fo:block>
									<fo:inline font-weight="bold">
										<xsl:value-of select="signaturePresident"/>
									</fo:inline>
								</fo:block>
							</fo:table-cell>
					  		<fo:table-cell/>
						</fo:table-row>
						
						<xsl:call-template name="sautLigne"/>
	
						<!-- destinataires -->
						<fo:table-row>
					  		<fo:table-cell/>
							<fo:table-cell number-columns-spanned="3">
								<fo:block font-size="10pt">
									<fo:inline text-decoration="underline">Destinataires</fo:inline> : Intéréssé(e) - Etablissement - Rectorat
								</fo:block>
							</fo:table-cell>
					  		<fo:table-cell/>
						</fo:table-row>
								
								
						<xsl:call-template name="sautLigne"/>
						
						<fo:table-row>
							<fo:table-cell number-columns-spanned="5">
								<fo:block>
               					<fo:table border-width="0.5pt" border-style="solid" text-align="left" background-color="#EEEEEE">
										<fo:table-column/>
											<fo:table-body>
											<fo:table-row>
												<fo:table-cell>
													<fo:block font-size="8pt">
														<fo:inline text-decoration="underline">Voies et délais de recours</fo:inline>&br;
														Si vous estimez que la décision prise par l'administration est contestable, vous pouvez former :&br;
														- un recours gracieux devant l'auteur de la décision ;&br;
														- un recours hiérarchique devant le ministre chargé de l'éducation nationale ;&br;
														- un recours contentieux devant le tribunal administratif dans le ressort duquel se trouve votre lieu d'affectation ;&br;
														Le recours gracieux et le recours hiérarchique peuvent être faits sans conditions de délais. En revanche, le recours
														contentieux doit intervenir dans un délai de deux mois à compter de la notification de la décision.&br;
														Toutefois, si vous souhaitez, en cas de rejet du recours gracieux ou du recours hiérarchique, former un recours contentieux,
														ce recours gracieux ou hiérarchique devra avoir été introduit dans un délai de deux mois à compter de la notification de 
														la décision initiale.&br;
														Vous conservez ainsi la possibilité de former un recours contentieux dans un délai de deux mois à compter de la notification
														de la décision intervenue sur ledit recours gracieux ou hiérarchique. Cette décision peut être explicite ou implicite (absence
														de réponse de l'administration pendant deux mois).&br;
														Dans les cas très exceptionnels où une décision explicite intervient dans un délai de deux mois après la décision implicite
														(c'est-à-dire dans un délai de quatre mois à compter de la date du recours gracieux ou hiérarchique), vous disposez à nouveau
														d'un délai de deux mois à compter de la notification de cette décision explicite pour former un recours contentieux.
													</fo:block>
												</fo:table-cell>
											</fo:table-row>								
										</fo:table-body>
									</fo:table>
             				   	</fo:block>
							</fo:table-cell>
						</fo:table-row>								
												
					</fo:table-body>
				</fo:table>
        	</fo:table-cell>
        </fo:table-row>
    </xsl:template>

    
    
    <!-- -->
    <!-- -->
    <!-- -->
    <!--saut de ligne -->
    <xsl:template name="sautLigne">
        <fo:table-row height="4mm">
            <fo:table-cell/>
        </fo:table-row>
    </xsl:template>
    <!-- -->
    
    <!-- -->
    <!-- -->
    <!-- -->
    <!--saut de page -->
    <xsl:template name="sautPage">
	    <fo:block break-after="page"/>
    </xsl:template>
    
    
    <!-- -->
    <!-- -->
    <!-- -->
    <!-- zone identité demandeur -->
    <xsl:template name="identiteDemandeur">
  		<!-- identite -->
        <fo:table-row>
	  		<fo:table-cell/>
        	<fo:table-cell/>
            <fo:table-cell number-columns-spanned="2">
            	<fo:block>
	            	<xsl:value-of select="civilite"/>&nbsp;
	                <xsl:value-of select="nomDemandeur"/>&nbsp;
	                <xsl:value-of select="prenomDemandeur"/>&nbsp;
              	</fo:block>
                <fo:block>
                	Grade : <xsl:value-of select="grade"/>
                </fo:block>
            </fo:table-cell>
	  		<fo:table-cell/>
       </fo:table-row>
       <!--affectations -->
       <fo:table-row>
	  		<fo:table-cell/>
       		<fo:table-cell/>
            <fo:table-cell font-size="9pt" number-columns-spanned="2">
            	<fo:block>Affecté à : <xsl:value-of select="listeAffectation"/></fo:block>
            </fo:table-cell>
	  		<fo:table-cell/>
       	</fo:table-row>
    </xsl:template>
                
        
    <!-- -->
    <!-- -->
	<!--checkbox -->
    <xsl:template name="checkbox">
		<fo:table-cell display-align="center">
			<fo:block>
				<fo:table border-width="0.5pt" border-style="solid">
					<fo:table-column width="4mm"/>
					<fo:table-column/>
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell>
								<fo:block>&nbsp;</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:block>
		</fo:table-cell>
    </xsl:template>


	<!-- -->
    <!-- -->
	<!-- encart décision -->
    <xsl:template name="encartDecision">
    	<fo:table-row>
			<fo:table-cell number-columns-spanned="5"> 
				<fo:table border-width="0.5pt" border-style="solid" padding="5pt">
					<fo:table-column column-width="130mm"/>
					<fo:table-column/>
					<fo:table-body>
						<xsl:call-template name="accordPresident"/>
						<xsl:call-template name="observationSoldeFinalSignature"/>
					</fo:table-body>
				</fo:table>
			</fo:table-cell>
		</fo:table-row>    
    </xsl:template> 

    <!-- -->
    <!-- -->
	<!-- accord du president -->
	<xsl:template name="accordPresident">
		<fo:table-row height="14mm">
			<fo:table-cell number-columns-spanned="2">
				<fo:table border-bottom-width="0.5pt" border-bottom-style="solid" padding="5pt">
					<fo:table-column/>
					<fo:table-column column-width="15mm"/>
					<fo:table-column column-width="4mm"/>
					<fo:table-column column-width="30mm"/>
					<fo:table-column column-width="4mm"/>
					<fo:table-column column-width="25mm"/>
					<fo:table-body>
						<fo:table-row>	
							<fo:table-cell>
								<fo:block>
									Accord du pr&eaigu;sident de l'universit&eaigu;
								</fo:block>
							</fo:table-cell>
							<fo:table-cell/>
							<xsl:call-template name="checkbox"/>
							<fo:table-cell font-size="14pt" display-align="center"><fo:block>&nbsp;&nbsp; OUI</fo:block></fo:table-cell>
							<xsl:call-template name="checkbox"/>
							<fo:table-cell font-size="14pt" display-align="center"><fo:block>&nbsp;&nbsp; NON</fo:block></fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:table-cell>
		</fo:table-row>
    </xsl:template>
    
    <!-- -->
    <!-- -->
	<!-- observation + solde final + signature -->
	<xsl:template name="observationSoldeFinalSignature">
			
		<fo:table-row height="40mm">
			<fo:table-cell number-columns-spanned="2">
				<fo:table display-align="center">
					<fo:table-column/>
					<fo:table-column column-width="70mm"/>
					<fo:table-body>
						<fo:table-row height="15mm">	
							<fo:table-cell><fo:block>&nbsp;&nbsp;<fo:inline text-decoration="underline">Observations :</fo:inline> </fo:block></fo:table-cell>
							<fo:table-cell>
								<fo:block text-align="center">
									Solde du CET apr&egrave;s maintien et exercice du droit d'option
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row height="10mm">	
							<fo:table-cell><fo:block><xsl:value-of select="ville"/>, le ...........................</fo:block></fo:table-cell>
							<fo:table-cell><fo:block text-align="center">
								<fo:inline font-size="16pt" font-weight="bold">..... jours &agrave; 7h00</fo:inline>
							</fo:block></fo:table-cell>
						</fo:table-row>
						<fo:table-row height="15mm"><fo:table-cell><fo:block>Signature :</fo:block></fo:table-cell></fo:table-row>
					</fo:table-body>
				</fo:table>
			 </fo:table-cell>
		</fo:table-row>
    </xsl:template>
    
    
</xsl:stylesheet>
