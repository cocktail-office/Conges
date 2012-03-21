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
<!ENTITY  agrave  "&#224;">
<!ENTITY  fleche "&#187;">

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
                <fo:flow flow-name="xsl-region-body" font-family="Arial, Helvetica, sans-serif"
                    font-size="12pt" font-color="#000000">
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
    </xsl:template>
    <!-- -->
    <!-- -->
    <!-- -->
    <xsl:template name="enteteLettre">
        <fo:table>
            <fo:table-column column-width="20mm"/>
            <fo:table-column/>
            <fo:table-column column-width="20mm"/>
         <fo:table-body>
                <fo:table-row>
                    <fo:table-cell display-align="top" text-align="left">
                        <fo:block>
                            <!-- Affichage du logo de l'universite joint au fichier xsl et xml-->
                            <!--<fo:external-graphic src="url('logo_univ.jpg')" height="30mm" width="30mm"/> -->
                            <!-- <fo:external-graphic src="url('http://www.univ-lr.fr/image_partagee/ULRLogo90.gif')" height="30mm" width="30mm"/> -->
		                  	<fo:external-graphic  height="30mm" width="30mm">
                           		<xsl:attribute name="src">
                           			<xsl:value-of select="/demande/mainLogoUrl"/>
                           		</xsl:attribute>
                           	</fo:external-graphic>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block font-size="16pt" text-align="center">
                            <fo:inline text-decoration="underline">
                                DEMANDE D’AUTORISATION D’ABSENCE&br;POUR GARDE D’ENFANT
                            </fo:inline>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell/>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
    </xsl:template>
    <!-- -->
    <!-- -->
    <!-- -->
    <xsl:template name="contenu">
        <fo:table>
            <fo:table-column column-width="20mm"/>
            <fo:table-column/>
            <fo:table-column column-width="20mm"/>
            <fo:table-body>
	        <fo:table-row height="10mm"/>
		    <fo:table-row>
		        <fo:table-cell/>
                <fo:table-cell>
				    <fo:block>
				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				    Je soussign&eaigu;<xsl:value-of select="feminin"/>, <fo:inline font-weight="bold"><xsl:value-of select="prenomNomQualite"/></fo:inline>
					sollicite une autorisation d’absence pour garder mon enfant <fo:inline font-weight="bold"><xsl:value-of select="prenomNeLeDNaissance"/></fo:inline>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell/>
            </fo:table-row>
            <fo:table-row height="10mm"/>
		    <fo:table-row>
		        <fo:table-cell/>
                <fo:table-cell>
				    <fo:block>
				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&fleche;&nbsp;du <fo:inline font-weight="bold"><xsl:value-of select="dateDebutAMPM"/></fo:inline>
				    </fo:block>
				</fo:table-cell>
				<fo:table-cell/>
            </fo:table-row>
            <fo:table-row height="5mm"/>
		    <fo:table-row>
		        <fo:table-cell/>
                <fo:table-cell>
				    <fo:block>
				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&fleche;&nbsp;au <fo:inline font-weight="bold"><xsl:value-of select="dateFinAMPM"/></fo:inline>
				    </fo:block>
				</fo:table-cell>
				<fo:table-cell/>
            </fo:table-row>
            <fo:table-row height="10mm"/>
		    <fo:table-row>
		        <fo:table-cell/>
                <fo:table-cell>
				    <fo:block>
					&br;correspondant &agrave; <fo:inline font-weight="bold"><xsl:value-of select="nbDemiJourneesTravail"/></fo:inline> demi-journ&eaigu;e<xsl:value-of select="pluriel"/>
					pour la raison suivante <fo:inline font-size="8pt">(1)</fo:inline> :
					</fo:block>
				</fo:table-cell>
				<fo:table-cell/>
            </fo:table-row>
            <fo:table-row height="10mm"/>
		    <fo:table-row>
		        <fo:table-cell/>
                <fo:table-cell>
				    <fo:block>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<fo:inline font-weight="bold"><xsl:value-of select="motif"/></fo:inline>
				    </fo:block>
				</fo:table-cell>
				<fo:table-cell/>
            </fo:table-row>
            <fo:table-row height="10mm"/>
		    <fo:table-row>
		        <fo:table-cell/>
                <fo:table-cell>
				    <fo:block>
				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					Je suis inform&eaigu;<xsl:value-of select="feminin"/> que cette demande ne constitue pas un droit mais 
					rel&egrave;ve d'une mesure de bienveillance relevant de mon sup&eaigu;rieur hi&eaigu;rarchique.
				    </fo:block>
				</fo:table-cell>
				<fo:table-cell/>
            </fo:table-row>
            <fo:table-row height="10mm"/>
		    <fo:table-row>
		        <fo:table-cell/>
                <fo:table-cell>
				    <fo:block>
				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					J'ai &eaigu;galement pris connaissance du fait que si le quota d’autorisations 
					susceptibles de m'&ecircon;tre autoris&eaigu;es &agrave; ce titre a &eaigu;t&eaigu; d&eaigu;pass&eaigu;, cette dur&eaigu;e d’absence sera 
					d&eaigu;compt&eaigu;e de mes droits &agrave; cong&eaigu;s annuels de l'ann&eaigu;e en cours (ou de l'ann&eaigu;e suivante si 
			 		ceux-ci ont &eaigu;t&eaigu; &eaigu;puis&eaigu;s).
		          </fo:block>
				</fo:table-cell>
				<fo:table-cell/>
            </fo:table-row>
            <fo:table-row height="10mm"/>
		    <fo:table-row>
		        <fo:table-cell/>
                <fo:table-cell>
				    <fo:block>
				        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 	        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 	        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 	        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	       			    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <xsl:value-of select="ville"/>, le .................
    	           </fo:block>
				</fo:table-cell>
				<fo:table-cell/>
            </fo:table-row>
            <fo:table-row height="5mm"/>
		    <fo:table-row>
		        <fo:table-cell/>
                <fo:table-cell>
				    <fo:block>
				        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 	        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 	        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 	        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	       			    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 	        <fo:inline text-decoration="underline">Signature de l'agent</fo:inline>
			 	   </fo:block>
				</fo:table-cell>
				<fo:table-cell/>
            </fo:table-row>
            <fo:table-row height="10mm"/>
		    <fo:table-row>
		        <fo:table-cell number-columns-spanned="3">
				    <fo:block>
                        <fo:inline text-decoration="underline">Avis du sup&eaigu;rieur hi&eaigu;rarchique</fo:inline>&nbsp;<fo:inline font-size="8pt">(2)</fo:inline>
                    </fo:block>
				</fo:table-cell>
				<fo:table-cell/>
            </fo:table-row>
            <fo:table-row height="5mm"/>
		    <fo:table-row>
		        <fo:table-cell number-columns-spanned="3">
				    <fo:block>
                        &nbsp;<fo:inline font-weight="bold" font-size="14pt">O</fo:inline>&nbsp;Favorable, &agrave; d&eaigu;compter dans la limite du quota de demi-journ&eaigu;es allou&eaigu;es pour garde d'enfant.
                    </fo:block>
				</fo:table-cell>
				<fo:table-cell/>
            </fo:table-row>
            <fo:table-row height="3mm"/>
		    <fo:table-row>
		        <fo:table-cell number-columns-spanned="3">
				    <fo:block>
                        &nbsp;<fo:inline font-weight="bold" font-size="14pt">O</fo:inline>&nbsp;D&eaigu;favorable, &agrave; d&eaigu;compter sur les cong&eaigu;s annuels de l'agent.
                    </fo:block>
				</fo:table-cell>
				<fo:table-cell/>
            </fo:table-row>
            <fo:table-row height="5mm"/>
		    <fo:table-row>
		        <fo:table-cell/>
                <fo:table-cell>
				    <fo:block>
				        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 	        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 	        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 	        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	       			    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 	        <fo:inline text-decoration="underline">Signature</fo:inline>
			         </fo:block>
				</fo:table-cell>
				<fo:table-cell/>
            </fo:table-row>
            <fo:table-row height="23mm"/>
        </fo:table-body>
    </fo:table>
    <fo:table>
        <fo:table-column column-width="40mm"/>
        <fo:table-column/>
        <fo:table-body>
            <fo:table-row>
                <fo:table-cell number-columns-spanned="3">
	                <fo:block>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 	        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 	        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 	        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    			        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    				    Pris connaissance de l'avis, le .................
    				</fo:block>
                </fo:table-cell>
            </fo:table-row>
            <fo:table-row height="5mm"/>
            <fo:table-row>
                <fo:table-cell>
                   <fo:block font-size="8pt">
                        (1) joindre le justificatif&br;
                        (2) cocher l'avis retenu 
                    </fo:block>
                </fo:table-cell>
                <fo:table-cell number-columns-spanned="2">
	                <fo:block>
    				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 	        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 	        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <fo:inline text-decoration="underline">Signature de l'agent</fo:inline>
                     </fo:block>
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
