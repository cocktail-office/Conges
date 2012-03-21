<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xsl:stylesheet
[
<!ENTITY  nbsp  "&#160;">
<!ENTITY  space  "&#x20;">
<!ENTITY  br  "&#x2028;">
<!ENTITY  eaigu  "&#233;">
<!ENTITY  egrave  "&#232;">
<!ENTITY  ecircon  "&#234;">
<!ENTITY  ucircon  "&#251;">
<!ENTITY  ugrave  "&#249;">
<!ENTITY  agrave  "&#224;">
<!ENTITY  numero  "&#176;">

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
                    <fo:region-body margin="5mm 5mm 10mm 5mm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="main">
                <!-- ENTETE de page -->
                <fo:static-content flow-name="xsl-region-before"/>
                <!-- ENTETE de page : END -->
                <!-- BAS de page -->
                <fo:static-content flow-name="xsl-region-after"
                    font-family="Arial, Helvetica, sans-serif" font-size="10pt">
                    </fo:static-content>
                <!-- BAS de page : END -->
                <!--AVANT -->
                <fo:static-content flow-name="xsl-region-start"/>
                <!--AVANT: END -->
                <!--APRES -->
                <fo:static-content flow-name="xsl-region-end"/>
                <!--APRES: END -->
                <!-- PAGE CONTENTS -->
                <fo:flow flow-name="xsl-region-body" font-family="Arial, Helvetica, sans-serif" font-size="10pt" font-color="#000000">
                	<xsl:apply-templates select="situations"/>
                </fo:flow>
                <!-- PAGE CONTENTS : END-->
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
  	<!-- -->
    <!-- -->
    <!-- -->
    <xsl:template name="situations" match="situations">
		<xsl:apply-templates select="situation"/>
  	</xsl:template>
    <!-- -->
    <!-- -->
    <!-- -->
    <xsl:template match="situation">
        <fo:table>
            <fo:table-column/>
            <fo:table-body>
                <!--Entete de la lettre -->
				<fo:table-row><fo:table-cell><xsl:call-template name="enteteLettre"/></fo:table-cell></fo:table-row>
				<!--Separation -->
                <fo:table-row height="5mm"><fo:table-cell><fo:block> &nbsp; </fo:block></fo:table-cell></fo:table-row>
                <!--contenu -->
				<fo:table-row>
                    <fo:table-cell>
                        <xsl:call-template name="contenu"/>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
        <!-- saut de page except&eaigu; pour la derniere -->
        <xsl:if test="position()!=last()">
	    	<fo:block break-after="page"/>
	    </xsl:if>
	</xsl:template>
    <!-- -->
    <!-- -->
    <!-- -->
    <xsl:template name="enteteLettre">
        <fo:table>
            <fo:table-column column-width="40mm"/>
            <fo:table-column/>
            <fo:table-body>
                <fo:table-row>
                    <fo:table-cell display-align="top" text-align="left">
                        <fo:block>
                            <!-- Affichage du logo de l'universite joint au fichier xsl et xml-->
                            <fo:external-graphic  height="30mm" width="30mm">
                           		
                            	<xsl:attribute name="src">
                           			
                            		<xsl:value-of select="/situations/mainLogoUrl"/>
                           		
                            	</xsl:attribute>
                           	
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
											SITUATION D'UN COMPTE EPARGNE TEMPS
										</fo:block>
                 	 		 		 </fo:table-cell>
				                </fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
    </xsl:template>
    <!-- -->
    <!-- -->
    <!-- tableau -->
    <xsl:template name="situationTableau">
     	<xsl:param name="titreColonne1"/>
	    <xsl:param name="titreColonne6"/>
	    <xsl:param name="racineLignes"/>
		<fo:table-row>
			<fo:table-cell number-columns-spanned="3">
				<fo:table text-align="center" display-align="center" padding="10pt">
					<fo:table-column/>
					<fo:table-column/>
					<fo:table-column/>
					<fo:table-column/>
					<fo:table-column/>
					<fo:table-column/>
					<fo:table-header display-align="center">
						<fo:table-row  height="7mm" background-color="#DDDDDD">
							<fo:table-cell><fo:block><xsl:value-of select="$titreColonne1"/></fo:block></fo:table-cell>
							<fo:table-cell><fo:block>Nombre de jours &eaigu;pargn&eaigu;s (base de 7h00/j.)</fo:block></fo:table-cell>
							<fo:table-cell><fo:block>Total jours &eaigu;pargn&eaigu;s</fo:block></fo:table-cell>
							<fo:table-cell><fo:block>Utilisation des jours &eaigu;pargn&eaigu;s</fo:block></fo:table-cell>
							<fo:table-cell><fo:block>Nombre de jours utilis&eaigu;s (base de 7h00/j.)</fo:block></fo:table-cell>
							<fo:table-cell><fo:block>Solde</fo:block></fo:table-cell>
						</fo:table-row>
							
					</fo:table-header>
					<fo:table-body>
								
						<xsl:for-each select="$racineLignes/transaction">
							<fo:table-row>
								<fo:table-cell border-width="0.5pt" border-style="solid">
									<xsl:attribute name="number-rows-spanned"><xsl:value-of select="count(ligneDebitable) + 1"/></xsl:attribute>
									<fo:block><xsl:value-of select="transactionLibelle"/>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid">
									<xsl:attribute name="number-rows-spanned"><xsl:value-of select="count(ligneDebitable) + 1"/></xsl:attribute>
									<fo:block><xsl:value-of select="transactionValeurEnJours7h00"/></fo:block>
								</fo:table-cell>
								<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid">
									<xsl:attribute name="number-rows-spanned"><xsl:value-of select="count(ligneDebitable) + 1"/></xsl:attribute>
									<fo:block><xsl:value-of select="transactionSoldeInitialEnJours7h00"/></fo:block>
								</fo:table-cell>
							</fo:table-row>
							<xsl:for-each select="ligneDebitable">
								<fo:table-row>
									<fo:table-cell border-width="0.5pt" border-style="solid"><fo:block><xsl:value-of select="debitableLibelle"/></fo:block></fo:table-cell>
									<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid"><fo:block><xsl:value-of select="debitableValeurEnJours7h00"/></fo:block></fo:table-cell>
									<fo:table-cell font-size="12pt" border-width="0.5pt" border-style="solid"><fo:block><xsl:value-of select="debitableSoldeFinalEnJours7h00"/></fo:block></fo:table-cell>
								</fo:table-row>
							</xsl:for-each>
								
						</xsl:for-each>
								
					</fo:table-body>
				</fo:table>
			</fo:table-cell>
		</fo:table-row>	
	</xsl:template>  
    <!-- -->
    <!-- -->
    <!-- -->
    <!-- -->
    <!-- -->
    <xsl:template name="contenu">
        <fo:table>
            <fo:table-column column-width="20mm"/>
            <fo:table-column column-width="3mm"/>
            <fo:table-column/>
            <fo:table-body>
         		<fo:table-row height="5mm"/>
				<fo:table-row>
					<fo:table-cell><fo:block>R&eaigu;f&eaigu;rences :</fo:block></fo:table-cell>
               	    <fo:table-cell/>
               	    <fo:table-cell>
<fo:list-block>

    <fo:list-item>
     <fo:list-item-label end-indent="label-end()">
      <fo:block font-weight="bold">
       <fo:character character="&#x2022;"/>
      </fo:block>
     </fo:list-item-label>
     <fo:list-item-body start-indent="body-start()">
      <fo:block>D&eaigu;cret n&numero; 2002-634 du 29 avril 2002 portant cr&eaigu;ation du 
               	        compte &eaigu;pargne temps dans la fonction publique de l'&eaigu;tat et dans la magistrature ;</fo:block>
     </fo:list-item-body>
    </fo:list-item>
    
    <fo:list-item>
     <fo:list-item-label end-indent="label-end()">
      <fo:block font-weight="bold">
       <fo:character character="&#x2022;"/>
      </fo:block>
     </fo:list-item-label>
     <fo:list-item-body start-indent="body-start()">
      <fo:block>D&eaigu;cret n&numero; 2009-1065 modifiant certaines dispositions relatives au compte &eaigu;pargne
               	        temps dans la fonction publique de l'&eaigu;tat et dans la magistrature ;</fo:block>
     </fo:list-item-body>
    </fo:list-item>
    
    <fo:list-item>
     <fo:list-item-label end-indent="label-end()">
      <fo:block font-weight="bold">
       <fo:character character="&#x2022;"/>
      </fo:block>
     </fo:list-item-label>
     <fo:list-item-body start-indent="body-start()">
      <fo:block>Arr&ecircon;t&eaigu; du 28 juillet 2004 portant application dans les services d&eaigu;concentr&eaigu;s et 
               	        &eaigu;tablissements relevant du minist&egrave;re de l'&eaigu;ducation nationale et dans les &eaigu;tablissements relevant
               	        du minist&egrave;re charg&eaigu; de l'enseignement sup&eaigu;rieur du d&eaigu;cret n&numero; 2002-634 ;</fo:block>
     </fo:list-item-body>
    </fo:list-item>
    
    <fo:list-item>
     <fo:list-item-label end-indent="label-end()">
      <fo:block font-weight="bold">
       <fo:character character="&#x2022;"/>
      </fo:block>
     </fo:list-item-label>
     <fo:list-item-body start-indent="body-start()">
      <fo:block>Arr&ecircon;t&eaigu; du 28 ao&ucircon;t 2009 pris pour l'application du d&eaigu;cret n&numero; 2002-634 du 29 avril 2002
               	        modifi&eaigu; portant cr&eaigu;ation du compte &eaigu;pargne-temps dans la fonction publique de l'Etat et dans la magistrature ;</fo:block>
     </fo:list-item-body>
    </fo:list-item>
    
    <fo:list-item>
     <fo:list-item-label end-indent="label-end()">
      <fo:block font-weight="bold">
       <fo:character character="&#x2022;"/>
      </fo:block>
     </fo:list-item-label>
     <fo:list-item-body start-indent="body-start()">
      <fo:block>Arr&ecircon;t&eaigu; du 8 juillet 2010 relatif &agrave; la mise en oeuvre du compte &eaigu;pargne-temps au sein de l'administration 
               	        centrale du minist&egrave;re charg&eaigu; de l'&eaigu;ducation nationale et du minist&egrave;re charg&eaigu; de l'enseignement sup&eaigu;rieur et de la 
               	        recherche ainsi qu'au sein des organismes qui leur sont rattach&eaigu;s ;</fo:block>
     </fo:list-item-body>
    </fo:list-item>
    
   </fo:list-block>

 
               	    </fo:table-cell>
            	</fo:table-row>
            </fo:table-body>
        </fo:table>
        <fo:table>
            <fo:table-column column-width="20mm"/>
            <fo:table-column/>
            <fo:table-column column-width="20mm"/>
            <fo:table-body>
	         	<fo:table-row height="5mm"/>
                <fo:table-row>
                    <fo:table-cell/>
                    <fo:table-cell>
                       	<fo:block>
                       		<fo:inline font-weight="bold">
		                       	<xsl:value-of select="civilite"/>&nbsp;
        	               		<xsl:value-of select="nomDemandeur"/>&nbsp;
           	            		<xsl:value-of select="prenomDemandeur"/>&nbsp;
           	            	</fo:inline>
                       	</fo:block>
                       	<fo:block>
                       		<fo:inline font-weight="bold">
                       			<xsl:value-of select="grade"/>
                       		</fo:inline>
                       	</fo:block>
                    </fo:table-cell>
                    <fo:table-cell/>
                </fo:table-row>
				<!-- adresse ULR -->
                <fo:table-row><fo:table-cell/><fo:table-cell><fo:block><xsl:value-of select="/situations/adrAdresse1"/></fo:block></fo:table-cell></fo:table-row>
                <fo:table-row><fo:table-cell/><fo:table-cell><fo:block><xsl:value-of select="/situations/adrAdresse2"/></fo:block></fo:table-cell></fo:table-row>
                <fo:table-row><fo:table-cell/><fo:table-cell><fo:block><xsl:value-of select="/situations/codePostalVille"/></fo:block></fo:table-cell></fo:table-row>
				<fo:table-row height="5mm"/>
				
				<!-- ancien regime -->
				<xsl:if test="blocAncienRegime">
					<xsl:call-template name="situationTableau">
						<xsl:with-param name="titreColonne1" select="'Ancien R&eaigu;gime'"/>
						<xsl:with-param name="racineLignes"  select="blocAncienRegime"/>
					</xsl:call-template>
				</xsl:if>
				
				<!-- regime perenne -->
				<xsl:if test="blocRegimePerenne">
					<fo:table-row height="5mm"/>
					<xsl:call-template name="situationTableau">
						<xsl:with-param name="titreColonne1" select="'R&eaigu;gime P&eaigu;renne'"/>
						<xsl:with-param name="racineLignes"  select="blocRegimePerenne"/>
					</xsl:call-template>
				</xsl:if>
				
				<fo:table-row height="5mm"/>
	  		</fo:table-body>
 		</fo:table>
   
 		

        <fo:table>
            <fo:table-column column-width="90mm"/>
            <fo:table-column/>
            <fo:table-body>
         	   <fo:table-row>
			
					<fo:table-cell/>
			
					<fo:table-cell>
						<fo:block text-align="right">
							Solde total <xsl:if test="(blocAncienRegime) and (blocRegimePerenne)">(ancien + p&eaigu;renne)</xsl:if>:
						</fo:block>
					</fo:table-cell>
				</fo:table-row>    
				<fo:table-row height="2mm"/>
         	   <fo:table-row>
			
					<fo:table-cell/>
			
					<fo:table-cell>
						<fo:block text-align="right">
							<fo:inline font-size="16pt" font-weight="bold">
								<xsl:value-of select="soldeFinalEnJours7h00"/> jours &agrave; 7h00
							</fo:inline>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>    
				<fo:table-row height="5mm"/>
	         	<fo:table-row>
			
					<fo:table-cell/>
			
					<fo:table-cell>
						<fo:block text-align="right">Situation arr&ecircon;t&eaigu;e a la date du <xsl:value-of select="/situations/dateArret"/></fo:block>
					</fo:table-cell>
				</fo:table-row>    				
	  		</fo:table-body>
 		</fo:table>
  
            
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


</xsl:stylesheet>
