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
                <fo:static-content flow-name="xsl-region-after" font-family="Arial, Helvetica, sans-serif" font-size="10pt">
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
                    <fo:block>
                        <xsl:apply-templates select="demandes"/>
                    </fo:block>
                </fo:flow>
                <!-- PAGE CONTENTS : END-->
            </fo:page-sequence>
        </fo:root>
    </xsl:template>	<!-- -->
    <!-- -->
    <!-- -->
    <xsl:template name="demandes" match="demandes">
		<xsl:apply-templates select="demande"/>
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
				<!--titre L1-->
                <fo:table-row>
                    <fo:table-cell text-align="center" font-size="14pt">
                        <fo:block>DEMANDE D'AUTORISATION D'ABSENCE</fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <!--Separation -->
                <fo:table-row height="5mm"><fo:table-cell><fo:block> &nbsp; </fo:block></fo:table-cell></fo:table-row>
                <!--contenu -->
				<fo:table-row>
                    <fo:table-cell>
                        <xsl:call-template name="contenu"/>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                    <fo:table-cell>
                        <!--Le bas de page
                        <xsl:call-template name="basDePage"/> -->
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
            <fo:table-column/>
            <fo:table-column/>
            <fo:table-body>
                <fo:table-row>
                    <fo:table-cell display-align="top" text-align="left">
                        <fo:block>
                            <!-- Affichage du logo de l'universite joint au fichier xsl et xml-->
                            <fo:external-graphic  height="30mm" width="30mm">
                           		<xsl:attribute name="src">
                           			<xsl:value-of select="/demande/mainLogoUrl"/>
                           		</xsl:attribute>
                           	</fo:external-graphic>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
    </xsl:template>
    <!-- -->
    <!-- -->
    <!-- -->
    <xsl:template name="contenu">
        <xsl:variable name="anneeUniv" select="anneeUniv"/>
        <fo:table>
            <fo:table-column column-width="20mm"/>
            <fo:table-column/>
            <fo:table-column column-width="20mm"/>
            <fo:table-body>
                <!--saut de ligne -->
                <xsl:call-template name="sautLigne"/>
                <fo:table-row>
                    <fo:table-cell/>
                    <fo:table-cell>
                        <fo:block>Type d'absence : <xsl:value-of select="typeOccupation"/></fo:block>
                    </fo:table-cell>
                    <fo:table-cell/>
                </fo:table-row>
                <!--saut de ligne -->
                <xsl:call-template name="sautLigne"/>
                <fo:table-row>
                    <fo:table-cell/>
                    <fo:table-cell>
                        <fo:block>
                        	Nom : <xsl:value-of select="nomDemandeur"/>&nbsp; 
                        	Prénom : <xsl:value-of select="prenomDemandeur"/>&nbsp;
                        	Grade : <xsl:value-of select="gradeDemandeur"/>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell/>
                </fo:table-row>
				<!--saut de ligne -->
                <xsl:call-template name="sautLigne"/>
                <fo:table-row>
                    <fo:table-cell/>
                    <fo:table-cell>
						<fo:block>
							Date : <xsl:value-of select="dateImpression"/>&nbsp;&nbsp;&nbsp;&nbsp;
							Signature de l'intéressé(e) : ........................................
						</fo:block>
                    </fo:table-cell>
                    <fo:table-cell/>
                </fo:table-row>
                <!--saut de ligne -->
                <xsl:call-template name="sautLigne"/>
                <fo:table-row>
                    <fo:table-cell/>
                    <fo:table-cell>
                        <fo:block>
                        	Congés restant avant la présente demande : <xsl:value-of select="congesRestantsAvantDemandeEnHeures"/>
                        	&nbsp;( soit <xsl:value-of select="congesRestantsAvantDemandeEnJours"/> à <xsl:value-of select="dureeJournee"/>)&br; 
                        	Durée et date(s) du Congé sollicité : du <xsl:value-of select="dateDebutOccupation"/> au <xsl:value-of select="dateFinOccupation"/> (durée <xsl:value-of select="dureeOccupation"/>)&br;
                     		Congés restant après la présente demande : <xsl:value-of select="congesRestantsApresDemandeEnHeures"/>
                        	&nbsp;( soit <xsl:value-of select="congesRestantsApresDemandeEnJours"/> à <xsl:value-of select="dureeJournee"/>)
                     	</fo:block>
                     </fo:table-cell>
                    <fo:table-cell/>
                </fo:table-row>
                <!--saut de ligne -->
                <xsl:call-template name="sautLigne"/>
                <fo:table-row>
                    <fo:table-cell/>
                    <fo:table-cell>
                        <fo:block>
                        	Viseur(s) :  <xsl:apply-templates select="viseurs"/>&nbsp; - 
                        	Valideur(s) : <xsl:value-of select="responsables"/>
                     	</fo:block>
                     </fo:table-cell>
                    <fo:table-cell/>
                </fo:table-row>
                <!--saut de ligne -->
                <xsl:call-template name="sautLigne"/>
			</fo:table-body>
        </fo:table>
    </xsl:template>
	<!-- -->
    <!-- -->
    <!-- -->
  	<xsl:template name="viseur" match="viseur">
  		<xsl:value-of select="."/>
   	</xsl:template>
   	<!-- -->
    <!-- -->
    <!-- -->
    <xsl:template name="viseurs" match="viseurs">
 		<xsl:apply-templates select="viseur"/>
    </xsl:template>
	<!-- -->
    <!-- -->
    <!-- -->
  	<xsl:template name="responsable" match="responsable">
  		<xsl:value-of select="."/>
   	</xsl:template>
   	<!-- -->
    <!-- -->
    <!-- -->
    <xsl:template name="responsables" match="responsables">
 		<xsl:apply-templates select="responsable"/>
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
