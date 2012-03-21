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
                <fo:simple-page-master master-name="main" margin="10mm 5mm 5mm 5mm"
                    page-width="210mm" page-height="297mm">
                    <fo:region-before extent="5mm" margin="0pt"/>
                    <fo:region-after extent="5mm" margin="0pt"/>
                    <fo:region-start extent="5mm" margin="0pt"/>
                    <fo:region-end extent="5mm" margin="0pt"/>
                    <fo:region-body margin="5mm 5mm 10mm 5mm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="main">
                <!-- ENTETE de page -->
                <fo:static-content flow-name="xsl-region-before"/>
                <!-- ENTETE de page : END -->
                <!-- BAS de page -->
                <fo:static-content flow-name="xsl-region-after" font-family="Arial, Helvetica, sans-serif" font-size="10pt">
                    <fo:block>
                		<fo:table border-width="0.5pt" border-style="solid" text-align="center" background-color="#EEEEEE">
							<fo:table-column/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell><fo:block>Un agent ne peut ouvrir plusieurs CET simultan&eaigu;ment dans la fonction publique de l'&eaigu;tat</fo:block></fo:table-cell>
								</fo:table-row>								
							</fo:table-body>
						</fo:table>
                	</fo:block>
                 </fo:static-content>
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
    
    	<!-- demande de maintien sur l'ancien CET + droit d'option associé -->
		<xsl:if test="/demande/isMaintienAncienCet = 'true'">
    
			<fo:table>
			   <fo:table-column/>
			   <fo:table-body>
				
					<!--titre -->
					<xsl:call-template name="zoneTitre">
						<xsl:with-param name="titre" select='"DEMANDE DE MAINTIEN D&apos;UN CET OUVERT AU 31/12/2008"'/>
					</xsl:call-template>
			
					<!--contenu -->
					<xsl:call-template name="zoneContenuAncienRegime">
						<xsl:with-param name="suffixePhraseAccordPresident" select="'la demande de jours inscrits au CET crédité au 31/12/2008'"/>
						<xsl:with-param name="suffixePhraseSoldeCetFinal" select='"maintien et exercice du droit d&apos;option (CET ancien r&eaigu;gime)"'/>
						<xsl:with-param name="soldeCetFinal" select="/demande/demandeMaintienCetEnJours7h00AncienSysteme"/>				
					</xsl:call-template>
					
				</fo:table-body>
			</fo:table>
        
        	<!-- saut de page si necessaire -->
    	    <xsl:if test="(/demande/isEpargne = 'true') or (/demande/isRenoncement = 'true') or (/demande/isExerciceDroitOption = 'true')">
	        	<xsl:call-template name="sautPage"/>
	        </xsl:if>
        	
       	</xsl:if>
        
		<!-- demande sur le régime pérenne : transfert / épargne / droit d'option -->
        <xsl:if test="(/demande/isEpargne = 'true') or (/demande/isRenoncement = 'true') or (/demande/isExerciceDroitOption = 'true')">
			
			<fo:table>
				<fo:table-column/>
				<fo:table-body>
				
					<!--titre -->
					<xsl:call-template name="zoneTitre">
						<xsl:with-param name="titre" select="/demande/titre"/>
					</xsl:call-template>
				
					<!--contenu -->
					<xsl:call-template name="zoneContenu">
						<xsl:with-param name="suffixePhraseAccordPresident" select="/demande/suffixePhraseAccordPresident"/>
						<xsl:with-param name="suffixePhraseSoldeCetFinal" select='/demande/suffixePhraseSoldeCetFinal'/>
						<xsl:with-param name="soldeCetFinal" select="/demande/soldeCetApresEpargneEtDecisionEnJours7h00"/>				
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
            <fo:table-column column-width="40mm"/>
            <fo:table-column/>
            <fo:table-body>
                <fo:table-row>
                    <fo:table-cell display-align="top" text-align="left">
                        <fo:block>
                            <!-- Affichage du logo de l'universite joint au fichier xsl et xml-->
                            <fo:external-graphic  height="30mm" width="30mm">
                            	<xsl:attribute name="src"><xsl:value-of select="/demande/mainLogoUrl"/></xsl:attribute>
                            </fo:external-graphic>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell border-width="0.5pt" border-style="solid" text-align="center" display-align="center">
                    	<fo:table>
							<fo:table-column/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell text-align="center" font-size="14pt">
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
    <!-- contenu pour les demandes sur l'ancien régime -->
    <xsl:template name="zoneContenuAncienRegime">
   	    <xsl:param name="suffixePhraseAccordPresident"/>
	    <xsl:param name="suffixePhraseSoldeCetFinal"/>
	    <xsl:param name="soldeCetFinal"/>
    	<fo:table-row>
        	<fo:table-cell>
				<fo:table>
					<fo:table-column column-width="20mm"/>
					<fo:table-column/>
					<fo:table-column column-width="20mm"/>
					<fo:table-body>
						<!-- identite -->
						<xsl:call-template name="identiteDemandeur"/>
						
						<!--sauts de ligne -->
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						
					  	<fo:table-row>
							<fo:table-cell number-columns-spanned="2"><fo:block>- Titulaire d'un CET cr&eaigu;dit&eaigu; de <fo:inline font-weight="bold">
								<xsl:value-of select="demandeTotalOptionsEnJours7h00AncienSysteme"/> jours &agrave; 7h00</fo:inline>
								au 31 d&eaigu;cembre 2008, demande le maintien de tout ou partie des jours en stock sur mon CET pour une utilisation
								conform&eaigu;ment au tableau ci-dessous</fo:block></fo:table-cell>
						</fo:table-row>
						
						<!--sauts de ligne -->
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						
						<fo:table-row>
							<fo:table-cell number-columns-spanned="3" font-size="12pt"> 
								<fo:block>
									<fo:inline text-decoration="underline">Exercice du droit d'option du CET au 31/12/2008</fo:inline>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell number-columns-spanned="3"> 
								<fo:table font-size="9pt" text-align="center" display-align="center">
									<fo:table-column column-width="10mm"/>
									<fo:table-column/>
									<fo:table-column/>
									<fo:table-column/>
									<fo:table-column/>
									<fo:table-body>
										<fo:table-row  height="7mm" background-color="#DDDDDD">
											<fo:table-cell/>
											<fo:table-cell><fo:block>Jours concern&eaigu;s</fo:block></fo:table-cell>
											<fo:table-cell><fo:block>Jours &agrave; prendre en compte au titre de la RAFP</fo:block></fo:table-cell>
											<fo:table-cell><fo:block>Jours &agrave; indemniser</fo:block></fo:table-cell>
											<fo:table-cell><fo:block>Jours &agrave; maintenir sur le CET pour une utilisation ult&eaigu;rieure en cong&eaigu;</fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row height="7mm">
											<fo:table-cell border-width="0.5pt" border-style="solid"><fo:block>Jours (7h00/j)</fo:block></fo:table-cell>
											<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid" font-weight="bold"><fo:block><xsl:value-of select="demandeTotalOptionsEnJours7h00AncienSysteme"/></fo:block></fo:table-cell>
											<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid"><fo:block><xsl:value-of select="demandeTransfertRafpEnJours7h00AncienSysteme"/></fo:block></fo:table-cell>
											<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid"><fo:block><xsl:value-of select="demandeIndemnisationEnJours7h00AncienSysteme"/></fo:block></fo:table-cell>
											<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid"><fo:block><xsl:value-of select="demandeMaintienCetEnJours7h00AncienSysteme"/></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row height="7mm">
											<fo:table-cell border-width="0.5pt" border-style="solid"><fo:block>Heures</fo:block></fo:table-cell>
											<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid" font-weight="bold"><fo:block><xsl:value-of select="demandeTotalOptionsEnHeuresAncienSysteme"/></fo:block></fo:table-cell>
											<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid"><fo:block><xsl:value-of select="demandeTransfertRafpEnHeuresAncienSysteme"/></fo:block></fo:table-cell>
											<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid"><fo:block><xsl:value-of select="demandeIndemnisationEnHeuresAncienSysteme"/></fo:block></fo:table-cell>
											<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid"><fo:block><xsl:value-of select="demandeMaintienCetEnHeuresAncienSysteme"/></fo:block></fo:table-cell>
										</fo:table-row>
									</fo:table-body>
								</fo:table>
							</fo:table-cell>
						</fo:table-row>
						
						<!--saut de ligne -->
						<xsl:call-template name="sautLigne"/>
						
						<!-- signature agent + avis responsable -->
						<xsl:call-template name="signatureAgentAvisResponsable"/>
						<!--sauts de ligne -->
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						<!-- decision -->
						<xsl:call-template name="encartDecision">
						    <xsl:with-param name="suffixePhraseAccordPresident" select="$suffixePhraseAccordPresident"/>
						    <xsl:with-param name="suffixePhraseSoldeCetFinal" select="$suffixePhraseSoldeCetFinal"/>
							<xsl:with-param name="soldeCetFinal" select="$soldeCetFinal"/>
						</xsl:call-template>
					</fo:table-body>
				</fo:table>
        	</fo:table-cell>
        </fo:table-row>
    </xsl:template>

    
    
    <!-- -->
    <!-- -->
    <!-- contenu pour les demandes du régime pérenne -->
    <xsl:template name="zoneContenu">
   	    <xsl:param name="suffixePhraseAccordPresident"/>
	    <xsl:param name="suffixePhraseSoldeCetFinal"/>
	    <xsl:param name="soldeCetFinal"/>
        <fo:table-row>
        	<fo:table-cell>
				<fo:table>
					<fo:table-column column-width="20mm"/>
					<fo:table-column/>
					<fo:table-column column-width="20mm"/>
					<fo:table-body>
						<!-- identite -->
						<xsl:call-template name="identiteDemandeur"/>
						
						<!--sauts de ligne -->
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
				
						<!-- renoncement à l'ancien CET : phrase + tableau d'exercice du droit d'option -->
						<xsl:if test="/demande/isRenoncement = 'true'">
						   <fo:table-row>
								<fo:table-cell number-columns-spanned="3"><fo:block>- Renonce au CET actif le 31 d&eaigu;cembre 2008 et demande le versement des jours concern&eaigu;s
								(<fo:inline font-weight="bold"><xsl:value-of select="transfertEnJours7h00"/> jours &agrave; 7h00</fo:inline>) 
								dans le r&eaigu;gime p&eaigu;renne par application du d&eaigu;cret 2009-1065</fo:block></fo:table-cell>
							</fo:table-row>
							<!--saut de ligne -->
							<xsl:call-template name="sautLigne"/>
						</xsl:if>
						
						<!-- demande d'épargne -->
						<xsl:if test="/demande/isEpargne = 'true'">
						   <fo:table-row>
								<fo:table-cell number-columns-spanned="3"><fo:block>- Demande le versement de jours de cong&eaigu;s sur son CET
									(<fo:inline font-weight="bold"><xsl:value-of select="totalEpargneDemandeeEnJours7h00"/> jours &agrave; 7h00</fo:inline>)</fo:block></fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell number-columns-spanned="3">
									<fo:block>
										Ann&eaigu;e civile de r&eaigu;f&eaigu;rence : <xsl:value-of select="anneeCivileN"/> -
										Ann&eaigu;e universitaire de r&eaigu;f&eaigu;rence : <xsl:value-of select="anneeUnivNm1"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!--saut de ligne -->
							<xsl:call-template name="sautLigne"/>
						</xsl:if>
						
						
						<!--situation du CET avant decision -->
						<fo:table-row>
							<fo:table-cell/>
							<fo:table-cell>
								<fo:block>
									<fo:table>
										<fo:table-column/>
										<fo:table-body>
											<fo:table-row>
												<fo:table-cell>
													<fo:block text-align="center">
														Situation du CET au <xsl:value-of select="dateImpression"/>&nbsp;
														<xsl:if test="(/demande/isEpargne = 'true') or (/demande/isRenoncement = 'true') or (/demande/isExerciceDroitOption = 'true')">avant&nbsp;</xsl:if>
														<xsl:if test="(/demande/isEpargne = 'true') or (/demande/isRenoncement = 'true')">
															versement <xsl:if test="/demande/isExerciceDroitOption= 'true'">et&nbsp;</xsl:if>
														</xsl:if>
														<xsl:if test="/demande/isExerciceDroitOption= 'true'">exercice du droit d'option</xsl:if>
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell font-size="16pt">
													<fo:block text-align="center">
														<fo:inline font-weight="bold"><xsl:value-of select="soldeCetAvantEpargneEnJours7h00"/> jours &agrave; 7h00</fo:inline>
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</fo:table-body>
									</fo:table>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell/>
						</fo:table-row>
						

						<!--sauts de ligne -->
						<xsl:call-template name="sautLigne"/>
						<xsl:call-template name="sautLigne"/>
						
						<!-- tableau de demande d'epargne -->
						<xsl:if test="/demande/isEpargne = 'true'">
							<fo:table-row>
								<fo:table-cell number-columns-spanned="3" font-size="12pt"> 
									<fo:block>
										<fo:inline text-decoration="underline">D&eaigu;tail de la demande</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell number-columns-spanned="3"> 
									<fo:table font-size="9pt" text-align="center" display-align="center">
										<fo:table-column column-width="10mm"/>
										<fo:table-column/>
										<fo:table-column/>
										<fo:table-column/>
										<!-- renoncement-->
										<xsl:if test="/demande/isRenoncement = 'true'">
											<fo:table-column/>
										</xsl:if>
										<!-- epargne-->
										<xsl:if test="/demande/isEpargne = 'true'">
											<fo:table-column/>
										</xsl:if>
										<fo:table-column/>
										<fo:table-body>
											<fo:table-row  height="7mm" background-color="#DDDDDD">
												<fo:table-cell><fo:block></fo:block></fo:table-cell>
												<fo:table-cell><fo:block>Droits &agrave; cong&eaigu;s au titre de l'ann&eaigu;e <xsl:value-of select="anneeUnivNm1"/>  (cong&eaigu;s et ARTT inclus)</fo:block></fo:table-cell>
												<fo:table-cell><fo:block>Cong&eaigu;s utilis&eaigu;s au cours de <xsl:value-of select="anneeUnivNm1"/> (doit &ecircon;tre sup&eaigu;rieur &agrave; 20 jours)</fo:block></fo:table-cell>
												<fo:table-cell><fo:block>Cong&eaigu;s non pris au titre de l'ann&eaigu;e de r&eaigu;f&eaigu;rence</fo:block></fo:table-cell>
												<!-- renoncement-->
												<xsl:if test="/demande/isRenoncement = 'true'">
													<fo:table-cell><fo:block>Cong&eaigu;s issus du CET actif au 31/12/2008</fo:block></fo:table-cell>
												</xsl:if>
												<!-- epargne-->
												<xsl:if test="/demande/isEpargne = 'true'">
													<fo:table-cell><fo:block>Cong&eaigu;s <xsl:value-of select="anneeUnivNm1"/> dont le versement au CET est demand&eaigu;</fo:block></fo:table-cell>
												</xsl:if>
												<fo:table-cell><fo:block>Situation CET apr&egrave;s versement(s)</fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row height="7mm">
												<fo:table-cell border-width="0.5pt" border-style="solid"><fo:block>Jours (7h00/j)</fo:block></fo:table-cell>
												<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid"><fo:block><xsl:value-of select="droitACongesNm1EnJours7h00"/></fo:block></fo:table-cell>
												<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid"><fo:block><xsl:value-of select="consommationCongesNm1EnJours7h00"/></fo:block></fo:table-cell>
												<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid"><fo:block><xsl:value-of select="reliquatsNm1EnJours7h00"/></fo:block></fo:table-cell>
												<!-- renoncement-->
												<xsl:if test="/demande/isRenoncement = 'true'">
													<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid"><fo:block><xsl:value-of select="transfertEnJours7h00"/></fo:block></fo:table-cell>
												</xsl:if>
												<!-- epargne-->
												<xsl:if test="/demande/isEpargne = 'true'">
													<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid"><fo:block><xsl:value-of select="demandeEpargneEnJours7h00"/></fo:block></fo:table-cell>
												</xsl:if>
												<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid"><fo:block><xsl:value-of select="soldeCetApresEpargneEnJours7h00"/></fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row height="7mm">
												<fo:table-cell border-width="0.5pt" border-style="solid"><fo:block>Heures</fo:block></fo:table-cell>
												<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid"><fo:block><xsl:value-of select="droitACongesNm1EnHeures"/></fo:block></fo:table-cell>
												<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid"><fo:block><xsl:value-of select="consommationCongesNm1EnHeures"/></fo:block></fo:table-cell>
												<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid"><fo:block><xsl:value-of select="reliquatsNm1EnHeures"/></fo:block></fo:table-cell>
												<!-- renoncement-->
												<xsl:if test="/demande/isRenoncement = 'true'">
													<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid"><fo:block><xsl:value-of select="transfertEnHeures"/></fo:block></fo:table-cell>
												</xsl:if>
												<!-- epargne-->
												<xsl:if test="/demande/isEpargne = 'true'">
													<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid"><fo:block><xsl:value-of select="demandeEpargneEnHeures"/></fo:block></fo:table-cell>
												</xsl:if>
												<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid"><fo:block><xsl:value-of select="soldeCetApresEpargneEnHeures"/></fo:block></fo:table-cell>
											</fo:table-row>
										</fo:table-body>
									</fo:table>
								</fo:table-cell>
							</fo:table-row>
						</xsl:if>
						
						<!-- exercice du droit d'option sur le régime pérenne -->
						<xsl:if test="/demande/isExerciceDroitOption = 'true'">
							<xsl:call-template name="sautLigne"/>
							<fo:table-row>
								<fo:table-cell number-columns-spanned="3" font-size="12pt"> 
									<fo:block>
										<fo:inline text-decoration="underline">Exercice du droit d'option du CET au <xsl:value-of select="dateAnneeCivileN"/></fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell number-columns-spanned="3"> 
									<fo:table font-size="9pt" text-align="center" display-align="center">
										<fo:table-column column-width="10mm"/>
										<fo:table-column/>
										<fo:table-column/>
										<fo:table-column/>
										<fo:table-column/>
										<fo:table-body>
											<fo:table-row  height="7mm" background-color="#DDDDDD">
												<fo:table-cell/>
												<fo:table-cell><fo:block>Jours concern&eaigu;s par le droit d'option (jours sup&eaigu;rieurs &agrave; 20)</fo:block></fo:table-cell>
												<fo:table-cell><fo:block>Jours &agrave; prendre en compte au titre de la RAFP</fo:block></fo:table-cell>
												<fo:table-cell><fo:block>Jours &agrave; indemniser</fo:block></fo:table-cell>
												<fo:table-cell><fo:block>Jours &agrave; maintenir sur le CET pour une utilisation ult&eaigu;rieure en cong&eaigu;</fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row height="7mm">
												<fo:table-cell border-width="0.5pt" border-style="solid"><fo:block>Jours (7h00/j)</fo:block></fo:table-cell>
												<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid" font-weight="bold"><fo:block><xsl:value-of select="demandeTotalOptionsEnJours7h00"/></fo:block></fo:table-cell>
												<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid"><fo:block><xsl:value-of select="demandeTransfertRafpEnJours7h00"/></fo:block></fo:table-cell>
												<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid"><fo:block><xsl:value-of select="demandeIndemnisationEnJours7h00"/></fo:block></fo:table-cell>
												<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid"><fo:block><xsl:value-of select="demandeMaintienCetEnJours7h00"/></fo:block></fo:table-cell>
											</fo:table-row>
										</fo:table-body>
									</fo:table>
								</fo:table-cell>
							</fo:table-row>
							<!--saut de ligne -->
							<xsl:call-template name="sautLigne"/>
						</xsl:if>
						<!-- signature agent + avis responsable -->
						<xsl:call-template name="signatureAgentAvisResponsable"/>
						<!--saut de ligne -->
						<xsl:call-template name="sautLigne"/>
						<!-- decision -->
						<xsl:call-template name="encartDecision">
						    <xsl:with-param name="suffixePhraseAccordPresident" select="$suffixePhraseAccordPresident"/>
						    <xsl:with-param name="suffixePhraseSoldeCetFinal" select="$suffixePhraseSoldeCetFinal"/>
							<xsl:with-param name="soldeCetFinal" select="$soldeCetFinal"/>
						</xsl:call-template>
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
            <fo:table-cell>
            	<fo:block>
	            	<xsl:value-of select="civilite"/>&nbsp;
	                <xsl:value-of select="nomDemandeur"/>&nbsp;
	                <xsl:value-of select="prenomDemandeur"/>&nbsp;
              	</fo:block>
                <fo:block>
                	<xsl:value-of select="grade"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell/>
       </fo:table-row>
       <!-- fonction -->
       <fo:table-row>
       		<fo:table-cell/>
            <fo:table-cell font-size="9pt">
            	<fo:block>Fonction exerc&eaigu;e : <xsl:value-of select="qualite"/></fo:block>
            </fo:table-cell>
            <fo:table-cell/>
       </fo:table-row>
       <!--saut de ligne -->
       <xsl:call-template name="sautLigne"/>
       <!--affectations -->
       <fo:table-row>
       		<fo:table-cell/>
            <fo:table-cell font-size="9pt">
            	<fo:block>Affectation : <xsl:value-of select="listeAffectation"/></fo:block>
            </fo:table-cell>
            <fo:table-cell/>
       	</fo:table-row>
		<!-- saut de ligne -->
        <xsl:call-template name="sautLigne"/>
		<!-- adresse etablissement -->
        <fo:table-row><fo:table-cell/><fo:table-cell><fo:block><fo:inline font-style="italic"><xsl:value-of select="adrAdresse1"/></fo:inline></fo:block></fo:table-cell></fo:table-row>
        <fo:table-row><fo:table-cell/><fo:table-cell><fo:block><fo:inline font-style="italic"><xsl:value-of select="adrAdresse2"/></fo:inline></fo:block></fo:table-cell></fo:table-row>
        <fo:table-row><fo:table-cell/><fo:table-cell><fo:block><fo:inline font-style="italic"><xsl:value-of select="codePostalVille"/></fo:inline></fo:block></fo:table-cell></fo:table-row>
    </xsl:template>
        
        
    <!-- -->
    <!-- -->
	<!-- signature agent + avis responsable -->
    <xsl:template name="signatureAgentAvisResponsable">
		<fo:table-row>
			<fo:table-cell number-columns-spanned="3"> 
				<fo:table>
					<fo:table-column column-width="60mm"/>
					<fo:table-column/>
					<fo:table-body>
						<fo:table-row height="7mm">
							<fo:table-cell number-columns-spanned="2"><fo:block><xsl:value-of select="ville"/> le <xsl:value-of select="dateImpression"/></fo:block></fo:table-cell>
						</fo:table-row>
						<fo:table-row height="7mm">
							<fo:table-cell><fo:block text-align="left">
								Signature de l'agent : ...................
							</fo:block></fo:table-cell>
							<fo:table-cell><fo:block text-align="left">
								Avis, date et signature du sup&eaigu;rieur hi&eaigu;rarchique : ........................................................
							</fo:block></fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:table-cell>
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
	    <xsl:param name="suffixePhraseAccordPresident"/>
	    <xsl:param name="suffixePhraseSoldeCetFinal"/>
		<xsl:param name="soldeCetFinal"/>
    	<fo:table-row>
			<fo:table-cell number-columns-spanned="3"> 
				<fo:table border-width="0.5pt" border-style="solid" padding="5pt">
					<fo:table-column column-width="130mm"/>
					<fo:table-column/>
					<fo:table-body>
						<xsl:call-template name="accordPresident">
							<xsl:with-param name="suffixePhraseAccordPresident" select="$suffixePhraseAccordPresident"/>
						</xsl:call-template>
						<xsl:call-template name="observationSoldeFinalSignature">
							<xsl:with-param name="suffixePhraseSoldeCetFinal" select="$suffixePhraseSoldeCetFinal"/>
							<xsl:with-param name="soldeCetFinal" select="$soldeCetFinal"/>
						</xsl:call-template>
					</fo:table-body>
				</fo:table>
			</fo:table-cell>
		</fo:table-row>    
    </xsl:template> 

    <!-- -->
    <!-- -->
	<!-- accord du president -->
	<xsl:template name="accordPresident">
		<xsl:param name="suffixePhraseAccordPresident"/>
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
									Accord du pr&eaigu;sident de l'universit&eaigu; concernant 	<xsl:value-of select="$suffixePhraseAccordPresident"/>
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
		<xsl:param name="suffixePhraseSoldeCetFinal"/>
		<xsl:param name="soldeCetFinal"/>
			
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
									Solde du CET apr&egrave;s <xsl:value-of select="$suffixePhraseSoldeCetFinal"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row height="10mm">	
							<fo:table-cell><fo:block><xsl:value-of select="ville"/>, le ...........................</fo:block></fo:table-cell>
							<fo:table-cell><fo:block text-align="center">
								<fo:inline font-size="16pt" font-weight="bold"><xsl:value-of select="$soldeCetFinal"/> jours &agrave; 7h00</fo:inline>
							</fo:block></fo:table-cell>
						</fo:table-row>
						<fo:table-row height="15mm"><fo:table-cell><fo:block>Signature :</fo:block></fo:table-cell></fo:table-row>
					</fo:table-body>
				</fo:table>
			 </fo:table-cell>
		</fo:table-row>
    </xsl:template>

</xsl:stylesheet>
