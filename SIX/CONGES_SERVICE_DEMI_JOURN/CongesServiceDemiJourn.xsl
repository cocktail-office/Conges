<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xsl:stylesheet
[
<!ENTITY  nbsp  "&#160;">
<!ENTITY  space  "&#x20;">
<!ENTITY  br  "&#x2028;">
]>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <xsl:template match="/">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="main" margin="5mm 5mm 5mm 5mm" page-width="297mm" page-height="210mm">
                    <fo:region-before extent="36mm" margin="0pt"></fo:region-before>
                    <fo:region-after extent="21mm" margin="0pt"></fo:region-after>
                    <fo:region-start extent="5mm" margin="0pt"></fo:region-start>
                    <fo:region-end extent="5mm" margin="0pt"></fo:region-end>
                    <fo:region-body margin="20.9mm 5mm 25mm 5mm"></fo:region-body>
                    <!--<fo:region-body margin-top="20.9mm" margin-bottom="25mm"></fo:region-body>-->
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="main">
                <!-- ENTETE de page -->
                <fo:static-content flow-name="xsl-region-before" font-family="Arial, Helvetica, sans-serif" font-size="6pt">
                    <xsl:apply-templates select="planning/entetePlanning"></xsl:apply-templates>
                </fo:static-content>
                <!-- ENTETE de page : END -->
                <!-- BAS de page (legende + no page + date impresion) -->
                <fo:static-content flow-name="xsl-region-after" font-family="Arial, Helvetica, sans-serif" font-size="7pt">
                    <fo:block text-align="right">
                        <fo:table>
                            <fo:table-column column-width="4cm"></fo:table-column>
                            <fo:table-column></fo:table-column>
                            <fo:table-column></fo:table-column>
                            <fo:table-column></fo:table-column>
                            <fo:table-column></fo:table-column>
                            <fo:table-column></fo:table-column>
                            <fo:table-column column-width="4cm"></fo:table-column>
                            <fo:table-body>
                                <fo:table-row>
                                    <fo:table-cell></fo:table-cell>
                                    <fo:table-cell>
                                        <fo:table>
                                            <fo:table-column column-width="2mm"></fo:table-column>
                                            <fo:table-column></fo:table-column>
                                            <fo:table-body>
                                                <fo:table-row>
                                                    <xsl:if test="/planning/enCouleur = 'true'">
                                                        <xsl:variable name="couleur" select="//planning/couleurAbsenceR"></xsl:variable>
                                                        <fo:table-cell background-color="{$couleur}">
                                                            <fo:block>&nbsp;</fo:block>
                                                        </fo:table-cell>
                                                    </xsl:if>
                                                    <xsl:if test="/planning/enCouleur = 'false'">
                                                        <fo:table-cell>
                                                            <fo:block>
                                                                <fo:instream-foreign-object>
                                                                    <svg:svg width="4mm" height="4mm" viewBox="0 0 10 10" xmlns:svg="http://www.w3.org/2000/svg">
                                                                        <svg:line x1="0" y1="0" x2="5" y2="10" style="stroke-width:1; stroke: black; stroke-linecap: butt"></svg:line>
                                                                        <svg:line x1="0" y1="10" x2="5" y2="0" style="stroke-width:1; stroke: black; stroke-linecap: butt"></svg:line>
                                                                    </svg:svg>
                                                                </fo:instream-foreign-object>
                                                            </fo:block>
                                                        </fo:table-cell>
                                                    </xsl:if>
                                                    <fo:table-cell text-align="left" display-align="center">
                                                        <fo:block>=&nbsp;Absence Réelle</fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                            </fo:table-body>
                                        </fo:table>
                                    </fo:table-cell>
                                    <fo:table-cell>
                                        <fo:table>
                                            <fo:table-column column-width="2mm"></fo:table-column>
                                            <fo:table-column></fo:table-column>
                                            <fo:table-body>
                                                <fo:table-row>
                                                    <xsl:if test="/planning/enCouleur = 'true'">
                                                        <xsl:variable name="couleur" select="//planning/couleurAbsenceP"></xsl:variable>
                                                        <fo:table-cell background-color="{$couleur}">
                                                            <fo:block>&nbsp;</fo:block>
                                                        </fo:table-cell>
                                                    </xsl:if>
                                                    <xsl:if test="/planning/enCouleur = 'false'">
                                                        <fo:table-cell>
                                                            <fo:block>
                                                                <fo:instream-foreign-object>
                                                                    <svg:svg width="4mm" height="4mm" viewBox="0 0 10 10" xmlns:svg="http://www.w3.org/2000/svg">
                                                                        <svg:line x1="0" y1="0" x2="5" y2="10" style="stroke-width:1; stroke: grey; stroke-linecap: butt"></svg:line>
                                                                        <svg:line x1="0" y1="10" x2="5" y2="0" style="stroke-width:1; stroke: grey; stroke-linecap: butt"></svg:line>
                                                                    </svg:svg>
                                                                </fo:instream-foreign-object>
                                                            </fo:block>
                                                        </fo:table-cell>
                                                    </xsl:if>
                                                    <fo:table-cell text-align="left" display-align="center">
                                                        <fo:block>=&nbsp;Absence Prévisionnelle</fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                            </fo:table-body>
                                        </fo:table>
                                    </fo:table-cell>
									<fo:table-cell>
                                        <fo:table>
                                            <fo:table-column column-width="2mm"></fo:table-column>
                                            <fo:table-column></fo:table-column>
                                            <fo:table-body>
                                                <fo:table-row>
                                                    <xsl:if test="/planning/enCouleur = 'true'">
                                                        <xsl:variable name="couleur" select="//planning/couleurAbsenceREnCoursVal"></xsl:variable>
                                                        <fo:table-cell background-color="{$couleur}">
                                                            <fo:block>&nbsp;</fo:block>
                                                        </fo:table-cell>
                                                    </xsl:if>
                                                    <xsl:if test="/planning/enCouleur = 'false'">
                                                        <fo:table-cell>
                                                            <fo:block>
                                                                <fo:instream-foreign-object>
                                                                    <svg:svg width="4mm" height="4mm" viewBox="0 0 10 10" xmlns:svg="http://www.w3.org/2000/svg">
                                                                        <svg:line x1="0" y1="10" x2="5" y2="0" style="stroke-width:1; stroke: black; stroke-linecap: butt"></svg:line>
                                                                    </svg:svg>
                                                                </fo:instream-foreign-object>
                                                            </fo:block>
                                                        </fo:table-cell>
                                                    </xsl:if>
                                                    <fo:table-cell text-align="left" display-align="center">
                                                        <fo:block>=&nbsp;Absence Réelle en cours de validation</fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                            </fo:table-body>
                                        </fo:table>
                                    </fo:table-cell>
                                    <fo:table-cell>
                                        <fo:table>
                                            <fo:table-column column-width="2mm"></fo:table-column>
                                            <fo:table-column></fo:table-column>
                                            <fo:table-body>
                                                <fo:table-row>
                                                    <xsl:if test="/planning/enCouleur = 'true'">
                                                        <xsl:variable name="couleur" select="//planning/couleurTravailR"></xsl:variable>
                                                        <fo:table-cell background-color="{$couleur}" >
                                                            <fo:block>&nbsp;</fo:block>
                                                        </fo:table-cell>
                                                    </xsl:if>
                                                    <xsl:if test="/planning/enCouleur = 'false'">
                                                        <fo:table-cell>
                                                            <fo:block font-size="10pt" font-family="courier">
                                                                <fo:inline color="black" font-weight="bold">o</fo:inline>
                                                            </fo:block>
                                                        </fo:table-cell>
                                                    </xsl:if>
                                                    <fo:table-cell text-align="left" display-align="center">
                                                        <fo:block>=&nbsp;Travail Réel</fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                            </fo:table-body>
                                        </fo:table>
                                    </fo:table-cell>
                                    <fo:table-cell>
                                        <fo:table>
                                            <fo:table-column column-width="2mm"></fo:table-column>
                                            <fo:table-column></fo:table-column>
                                            <fo:table-body>
                                                <fo:table-row>
                                                    <xsl:if test="/planning/enCouleur = 'true'">
                                                        <xsl:variable name="couleur" select="//planning/couleurTravailP"></xsl:variable>
                                                        <fo:table-cell background-color="{$couleur}">
                                                            <fo:block>&nbsp;</fo:block>
                                                        </fo:table-cell>
                                                    </xsl:if>
                                                    <xsl:if test="/planning/enCouleur = 'false'">
                                                        <fo:table-cell>
                                                            <fo:block font-size="10pt" font-family="courier">
                                                                <fo:inline color="grey" font-weight="bold">o</fo:inline>
                                                            </fo:block>
                                                        </fo:table-cell>
                                                    </xsl:if>
                                                    <fo:table-cell text-align="left" display-align="center">
                                                        <fo:block>=&nbsp;Travail Prévisionnel</fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                            </fo:table-body>
                                        </fo:table>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell number-columns-spanned="6" text-align="right">
                                        <fo:block>
                                            <xsl:value-of select="/planning/noteBasDePage"></xsl:value-of> &nbsp; &nbsp; <fo:page-number></fo:page-number> / <fo:page-number-citation ref-id="last-page"></fo:page-number-citation>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                </fo:static-content>
                <!-- BAS de page : END -->
                <!--AVANT -->
                <fo:static-content flow-name="xsl-region-start"></fo:static-content>
                <!--AVANT: END -->
                <!--APRES -->
                <fo:static-content flow-name="xsl-region-end"></fo:static-content>
                <!--APRES: END -->
                <!-- PAGE CONTENTS -->
                <fo:flow flow-name="xsl-region-body" font-family="Arial, Helvetica, sans-serif" font-size="8pt" font-color="#000000">
                    <fo:table border-left="0.1pt solid" border-bottom="0.1pt solid" border-right="0.1pf solid">
                        <fo:table-column column-width="25mm"></fo:table-column>
                        <fo:table-column></fo:table-column>
                        <fo:table-column></fo:table-column>
                        <fo:table-body>
                            <xsl:apply-templates select="planning/Individu"></xsl:apply-templates>
                        </fo:table-body>
                    </fo:table>
                    <fo:block id="last-page"></fo:block>
                </fo:flow>
                <!-- PAGE CONTENTS : END-->
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    <!-- -->
    <!-- -->
    <!-- -->
    <xsl:template match="entetePlanning" name="entetePlanning">
        <fo:block>
            <fo:table>
                <fo:table-column column-width="25mm"></fo:table-column>
                <fo:table-column></fo:table-column>
                <fo:table-column></fo:table-column>
                <fo:table-body>
                    <fo:table-row height="12mm">
                        <fo:table-cell number-rows-spanned="2" text-align="center" display-align="center" border-bottom="0.1pt solid" border-right="0.1pt solid">
                            <fo:block>
                                <!-- Affichage du logo de l'université joint au fichier xsl et xml-->
                                <fo:external-graphic  height="20mm">
                           			<xsl:attribute name="src">
                           				<xsl:value-of select="/planning/mainLogoUrl"/>
                           			</xsl:attribute>
                          	 	</fo:external-graphic>
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell number-columns-spanned="2" text-align="center" display-align="center" border-top="0.1pt solid" border-right="0.1pt solid">
                            <fo:block font-size="12pt"> Planning du service : <fo:inline font-weight="bold">
                                    <xsl:value-of select="service"></xsl:value-of>
                                </fo:inline>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row height="8.5mm">
                        <xsl:for-each select="periode">
                            <fo:table-cell>
                                <fo:table>
                                    <fo:table-column></fo:table-column>
                                    <fo:table-body>
                                        <fo:table-row>
                                            <fo:table-cell text-align="center" border-right="0.1pt solid" border-top="0.1pt solid" border-bottom="0.1pt solid">
                                                <fo:block font-size="16pt">
                                                    <fo:inline font-size="12pt">
                                                        <xsl:value-of select="nomPeriode"></xsl:value-of>
                                                    </fo:inline>
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                        <fo:table-row>
                                            <fo:table-cell>
                                                <xsl:call-template name="jour"></xsl:call-template>
                                            </fo:table-cell>
                                        </fo:table-row>
                                    </fo:table-body>
                                </fo:table>
                            </fo:table-cell>
                        </xsl:for-each>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
        </fo:block>
    </xsl:template>
    <!-- -->
    <!-- -->
    <!-- -->
    <xsl:template match="Individu">
        <fo:table-row>
            <fo:table-cell display-align="center">
                <fo:block font-size="5pt" font-weight="bold"> &nbsp;<xsl:value-of select="nom"></xsl:value-of>
                </fo:block>
            </fo:table-cell>
            <xsl:for-each select="periode">
                <fo:table-cell>
                    <xsl:apply-templates select="."></xsl:apply-templates>
                </fo:table-cell>
            </xsl:for-each>
        </fo:table-row>
        <!--  <fo:table-row height="0.5mm" border-top="0.1pt solid">
            <fo:table-cell/>
        </fo:table-row>-->
    </xsl:template>
    <!-- -->
    <!-- -->
    <!-- -->
    <xsl:template match="periode">
        <fo:table>
            <!--Affichage dynamique des colonnes -->
            <xsl:call-template name="recursiveCols">
                <xsl:with-param name="nb" select="nbJour"></xsl:with-param>
            </xsl:call-template>
            <fo:table-body>
                <fo:table-row>
                    <xsl:for-each select="R">
                        <xsl:call-template name="listeAmPm">
                            <xsl:with-param name="premierJour" select="1"></xsl:with-param>
                            <xsl:with-param name="dernierJour" select="../nbJour"></xsl:with-param>
                            <xsl:with-param name="type" select="'R'"></xsl:with-param>
                        </xsl:call-template>
                    </xsl:for-each>
                </fo:table-row>
                <fo:table-row>
					<xsl:for-each select="P">
                        <xsl:call-template name="listeAmPm">
                            <xsl:with-param name="premierJour" select="1"></xsl:with-param>
                            <xsl:with-param name="dernierJour" select="../nbJour"></xsl:with-param>
                            <xsl:with-param name="type" select="'P'"></xsl:with-param>
                        </xsl:call-template>
                    </xsl:for-each>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
    </xsl:template>
    <!-- template recursive permettant de cree un nombre préci de colonne-->
    <xsl:template name="recursiveCols">
        <xsl:param name="nb" select="0"></xsl:param>
        <xsl:choose>
            <xsl:when test="$nb &gt; 0">
                <fo:table-column></fo:table-column>
                <xsl:call-template name="recursiveCols">
                    <xsl:with-param name="nb" select="($nb)-1"></xsl:with-param>
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!-- -->
    <!-- -->
    <!-- -->
    <xsl:template name="listeAmPm">
        <xsl:param name="premierJour"></xsl:param>
        <xsl:param name="dernierJour"></xsl:param>
        <xsl:param name="type"></xsl:param>
        <xsl:if test="$dernierJour &gt;= $premierJour">
            <fo:table-cell>
                <fo:table>
                    <fo:table-column></fo:table-column>
                    <fo:table-column></fo:table-column>
                    <fo:table-body>
                        <fo:table-row>
                            <xsl:call-template name="block">
                                <xsl:with-param name="type">
                                    <xsl:value-of select="$type"></xsl:value-of>
                                </xsl:with-param>
                                <xsl:with-param name="occupation">
                                    <xsl:choose>
                                        <xsl:when test="$premierJour = 1">
                                            <xsl:value-of select="./@am1"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 2">
                                            <xsl:value-of select="./@am2"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 3">
                                            <xsl:value-of select="./@am3"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 4">
                                            <xsl:value-of select="./@am4"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 5">
                                            <xsl:value-of select="./@am5"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 6">
                                            <xsl:value-of select="./@am6"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 7">
                                            <xsl:value-of select="./@am7"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 8">
                                            <xsl:value-of select="./@am8"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 9">
                                            <xsl:value-of select="./@am9"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 10">
                                            <xsl:value-of select="./@am10"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 11">
                                            <xsl:value-of select="./@am11"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 12">
                                            <xsl:value-of select="./@am12"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 13">
                                            <xsl:value-of select="./@am13"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 14">
                                            <xsl:value-of select="./@am14"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 15">
                                            <xsl:value-of select="./@am15"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 16">
                                            <xsl:value-of select="./@am16"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 17">
                                            <xsl:value-of select="./@am17"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 18">
                                            <xsl:value-of select="./@am18"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 19">
                                            <xsl:value-of select="./@am19"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 20">
                                            <xsl:value-of select="./@am20"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 21">
                                            <xsl:value-of select="./@am21"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 22">
                                            <xsl:value-of select="./@am22"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 23">
                                            <xsl:value-of select="./@am23"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 24">
                                            <xsl:value-of select="./@am24"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 25">
                                            <xsl:value-of select="./@am25"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 26">
                                            <xsl:value-of select="./@am26"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 27">
                                            <xsl:value-of select="./@am27"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 28">
                                            <xsl:value-of select="./@am28"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 29">
                                            <xsl:value-of select="./@am29"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 30">
                                            <xsl:value-of select="./@am30"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 31">
                                            <xsl:value-of select="./@am31"></xsl:value-of>
                                        </xsl:when>
                                    </xsl:choose>
                                </xsl:with-param>
                            </xsl:call-template>
                            <xsl:call-template name="block">
                                <xsl:with-param name="type">
                                    <xsl:value-of select="$type"></xsl:value-of>
                                </xsl:with-param>
                                <xsl:with-param name="occupation">
                                    <xsl:choose>
                                        <xsl:when test="$premierJour = 1">
                                            <xsl:value-of select="./@pm1"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 2">
                                            <xsl:value-of select="./@pm2"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 3">
                                            <xsl:value-of select="./@pm3"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 4">
                                            <xsl:value-of select="./@pm4"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 5">
                                            <xsl:value-of select="./@pm5"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 6">
                                            <xsl:value-of select="./@pm6"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 7">
                                            <xsl:value-of select="./@pm7"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 8">
                                            <xsl:value-of select="./@pm8"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 9">
                                            <xsl:value-of select="./@pm9"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 10">
                                            <xsl:value-of select="./@pm10"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 11">
                                            <xsl:value-of select="./@pm11"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 12">
                                            <xsl:value-of select="./@pm12"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 13">
                                            <xsl:value-of select="./@pm13"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 14">
                                            <xsl:value-of select="./@pm14"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 15">
                                            <xsl:value-of select="./@pm15"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 16">
                                            <xsl:value-of select="./@pm16"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 17">
                                            <xsl:value-of select="./@pm17"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 18">
                                            <xsl:value-of select="./@pm18"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 19">
                                            <xsl:value-of select="./@pm19"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 20">
                                            <xsl:value-of select="./@pm20"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 21">
                                            <xsl:value-of select="./@pm21"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 22">
                                            <xsl:value-of select="./@pm22"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 23">
                                            <xsl:value-of select="./@pm23"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 24">
                                            <xsl:value-of select="./@pm24"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 25">
                                            <xsl:value-of select="./@pm25"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 26">
                                            <xsl:value-of select="./@pm26"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 27">
                                            <xsl:value-of select="./@pm27"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 28">
                                            <xsl:value-of select="./@pm28"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 29">
                                            <xsl:value-of select="./@pm29"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 30">
                                            <xsl:value-of select="./@pm30"></xsl:value-of>
                                        </xsl:when>
                                        <xsl:when test="$premierJour = 31">
                                            <xsl:value-of select="./@pm31"></xsl:value-of>
                                        </xsl:when>
                                    </xsl:choose>
                                </xsl:with-param>
                            </xsl:call-template>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:table-cell>
            <xsl:call-template name="listeAmPm">
                <xsl:with-param name="premierJour" select="($premierJour)+1"></xsl:with-param>
                <xsl:with-param name="dernierJour" select="($dernierJour)"></xsl:with-param>
                <xsl:with-param name="type" select="$type"></xsl:with-param>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
    <!-- -->
    <!-- -->
    <!-- -->
    <xsl:template name="block">
        <xsl:param name="type"></xsl:param>
        <xsl:param name="occupation"></xsl:param>
        <xsl:if test="/planning/enCouleur = 'false'">
            <fo:table-cell border-left="0.1pt solid" border-top="0.1pt solid">
                <xsl:if test="$occupation = 'A'">
                    <fo:instream-foreign-object>
                        <svg:svg width="4mm" height="4mm" viewBox="0 0 10 10" xmlns:svg="http://www.w3.org/2000/svg">
                            <xsl:if test="$type = 'P'">
                                <svg:line x1="0" y1="0" x2="5" y2="10" style="stroke-width:1; stroke: grey; stroke-linecap: butt"></svg:line>
                                <svg:line x1="0" y1="10" x2="5" y2="0" style="stroke-width:1; stroke: grey; stroke-linecap: butt"></svg:line>
                            </xsl:if>
                            <xsl:if test="$type = 'R'">
                                <svg:line x1="0" y1="0" x2="5" y2="10" style="stroke-width:1; stroke: black; stroke-linecap: butt"></svg:line>
                                <svg:line x1="0" y1="10" x2="5" y2="0" style="stroke-width:1; stroke: black; stroke-linecap: butt"></svg:line>
                            </xsl:if>
                        </svg:svg>
                    </fo:instream-foreign-object>
                </xsl:if>
                <xsl:if test="$occupation ='T'">
                    <fo:block font-size="10pt" font-family="courier">
                        <xsl:if test="$type = 'P'">
                            <fo:inline color="grey" font-weight="bold">o</fo:inline>
                        </xsl:if>
                        <xsl:if test="$type = 'R'">
                            <fo:inline color="black" font-weight="bold">o</fo:inline>
                        </xsl:if>
                    </fo:block>
                </xsl:if>
				<xsl:if test="$occupation ='V'">
                    <fo:block font-size="10pt" font-family="courier">
						<fo:instream-foreign-object>
							<svg:svg width="4mm" height="4mm" viewBox="0 0 10 10" xmlns:svg="http://www.w3.org/2000/svg">
                                <svg:line x1="0" y1="10" x2="5" y2="0" style="stroke-width:1; stroke: black; stroke-linecap: butt"></svg:line>
                            </svg:svg>
						</fo:instream-foreign-object>
                    </fo:block>
                </xsl:if>
                <xsl:if test="$occupation = 'N'">
                    <fo:block> &nbsp; </fo:block>
                </xsl:if>
            </fo:table-cell>
        </xsl:if>
        <!--
                    <fo:instream-foreign-object>
                        <xsl:if test="$occupation = 'A'">
                            <svg:svg width="4mm" height="4mm" viewBox="0 0 10 10" xmlns:svg="http://www.w3.org/2000/svg">
                                <xsl:if test="$type = 'P'">
                                    <svg:line x1="0" y1="0" x2="5" y2="10" style="stroke-width:1; stroke: grey; stroke-linecap: butt"></svg:line>
                                    <svg:line x1="0" y1="10" x2="5" y2="0" style="stroke-width:1; stroke: grey; stroke-linecap: butt"></svg:line>
                                </xsl:if>
                                <xsl:if test="$type = 'R'">
                                    <svg:line x1="0" y1="0" x2="5" y2="10" style="stroke-width:1; stroke: black; stroke-linecap: butt"></svg:line>
                                    <svg:line x1="0" y1="10" x2="5" y2="0" style="stroke-width:1; stroke: black; stroke-linecap: butt"></svg:line>
                                </xsl:if>
                            </svg:svg>
                        </xsl:if>
                        <xsl:if test="$occupation = 'T'">
                            <svg:svg width="4mm" height="4mm" xmlns:svg="http://www.w3.org/2000/svg">
                                <xsl:if test="$type = 'P'">
                                    <svg:circle cx="3pt" cy="5pt" r="3pt" style="fill:grey;"></svg:circle>
                                </xsl:if>
                                <xsl:if test="$type = 'R'">
                                    <svg:circle cx="3pt" cy="5pt" r="3pt" style="fill:black;"></svg:circle>
                                </xsl:if>
                            </svg:svg>
                        </xsl:if>
                        <xsl:if test="$occupation = 'N'">
                            <svg:svg width="4mm" height="4mm" xmlns:svg="http://www.w3.org/2000/svg">
                                <svg:circle cx="3pt" cy="5pt" r="1pt" style="fill:white;"></svg:circle>
                            </svg:svg>
                        </xsl:if>
                    </fo:instream-foreign-object>
                    -->
        <xsl:if test="/planning/enCouleur = 'true'">
            <xsl:if test="$type = 'R'">
                <xsl:if test="$occupation = 'A'">
                    <xsl:variable name="couleur" select="//planning/couleurAbsenceR"></xsl:variable>
                    <fo:table-cell background-color="{$couleur}" border-left="0.1pt solid" border-top="0.1pt solid">
                        <fo:block>&nbsp;</fo:block>
                    </fo:table-cell>
                </xsl:if>
                <xsl:if test="$occupation = 'T'">
                    <xsl:variable name="couleur" select="//planning/couleurTravailR"></xsl:variable>
                    <fo:table-cell background-color="{$couleur}" border-left="0.1pt solid" border-top="0.1pt solid">
                        <fo:block>&nbsp;</fo:block>
                    </fo:table-cell>
                </xsl:if>
                <xsl:if test="$occupation = 'N'">
                    <xsl:variable name="couleur" select="//planning/couleurNonTravail"></xsl:variable>
                    <fo:table-cell background-color="{$couleur}" border-left="0.1pt solid" border-top="0.1pt solid">
                        <fo:block>&nbsp;</fo:block>
                    </fo:table-cell>
                </xsl:if>
                <xsl:if test="$occupation = 'V'">
                    <xsl:variable name="couleur" select="//planning/couleurAbsenceREnCoursVal"></xsl:variable>
                    <fo:table-cell background-color="{$couleur}" border-left="0.1pt solid" border-top="0.1pt solid">
                        <fo:block>&nbsp;</fo:block>
                    </fo:table-cell>
                </xsl:if>
           </xsl:if>
            <xsl:if test="$type = 'P'">
                <xsl:if test="$occupation = 'A'">
                    <xsl:variable name="couleur" select="//planning/couleurAbsenceP"></xsl:variable>
                    <fo:table-cell background-color="{$couleur}" border-left="0.1pt solid" border-top="0.1pt solid">
                        <fo:block>&nbsp;</fo:block>
                    </fo:table-cell>
                </xsl:if>
                <xsl:if test="$occupation = 'T'">
                    <xsl:variable name="couleur" select="//planning/couleurTravailP"></xsl:variable>
                    <fo:table-cell background-color="{$couleur}" border-left="0.1pt solid" border-top="0.1pt solid">
                        <fo:block>&nbsp;</fo:block>
                    </fo:table-cell>
                </xsl:if>
                <xsl:if test="$occupation = 'N'">
                    <xsl:variable name="couleur" select="//planning/couleurNonTravail"></xsl:variable>
                    <fo:table-cell background-color="{$couleur}" border-left="0.1pt solid" border-top="0.1pt solid">
                        <fo:block>&nbsp;</fo:block>
                    </fo:table-cell>
                </xsl:if>
            </xsl:if>
        </xsl:if>
    </xsl:template>
    <!-- -->
    <!-- -->
    <!-- -->
    <xsl:template name="jour">
        <fo:table>
            <xsl:call-template name="recursiveCols">
                <xsl:with-param name="nb" select="nbJour"></xsl:with-param>
            </xsl:call-template>
            <fo:table-body>
                <fo:table-row>
                    <xsl:for-each select="jour">
                        <fo:table-cell width="4mm" text-align="center" border-right="0.1pt solid" border-bottom="0.1pt solid" margin-top="1mm" margin-bottom="1mm" margin-left="1mm" margin-right="1mm" display-align="center">
                            <fo:block>
                                <xsl:value-of select="."></xsl:value-of>
                            </fo:block>
                        </fo:table-cell>
                    </xsl:for-each>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
    </xsl:template>
</xsl:stylesheet>
