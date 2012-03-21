<?xml version="1.0" encoding="ISO-8859-1"?>
<!DOCTYPE xsl:stylesheet
[
<!ENTITY  nbsp  "&#160;">
<!ENTITY  space  "&#x20;">
<!ENTITY  br  "&#x2028;">
<!ENTITY  euro  "&#x20AC;">
]>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

<xsl:template match="/">
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
	
	<fo:layout-master-set>
		<fo:simple-page-master master-name="main" margin="5mm" margin-top="10mm" page-width="210mm" page-height="297mm">
			<fo:region-before extent="10mm" margin="0pt"/>
		    <fo:region-after extent="5mm" margin="0pt"/>
			<fo:region-start extent="5mm" margin="0pt"/>
			<fo:region-end extent="5mm" margin="0pt"/>
			<fo:region-body margin-bottom="10mm" margin-top="5mm" margin-left="10mm" margin-right="10mm"/>
		</fo:simple-page-master>
	</fo:layout-master-set>

	<fo:page-sequence master-reference="main">
	<!-- ENTETE de page -->
	    <fo:static-content flow-name="xsl-region-before" font-family="Arial, Helvetica, sans-serif" font-size="8pt">
	      <fo:table width="100%" table-layout="fixed" space-before="0pt" padding="0pt" border-after-style="solid" border-after-width="0.5pt">
	        <fo:table-column/>
	        <fo:table-column/>
	        <fo:table-body>
	          <fo:table-row font-weight="bold">
	            <fo:table-cell text-align="left"><fo:block>Service : <xsl:value-of select="agents/service"/></fo:block></fo:table-cell>
	            <fo:table-cell text-align="right"><fo:block>Horaires <xsl:value-of select="agents/annee-univ"/></fo:block></fo:table-cell>
	          </fo:table-row>
	        </fo:table-body>
	      </fo:table>
	    </fo:static-content>
	<!-- ENTETE de page : END -->
	<!-- BAS de page -->
    <fo:static-content flow-name="xsl-region-after" font-family="Arial, Helvetica, sans-serif" font-size="7pt">
		<fo:table width="100%" table-layout="fixed" space-before="0pt" padding="0pt">
	        <fo:table-column/>
	        <fo:table-column/>
	        <fo:table-body>
	          <fo:table-row display-align="center">
	            <fo:table-cell text-align="left"><fo:block>Page : <fo:page-number/> </fo:block></fo:table-cell>
	            <fo:table-cell text-align="right"><fo:block>Date d'impression : <xsl:value-of select="agents/date-impression"/></fo:block></fo:table-cell>
	          </fo:table-row>
	        </fo:table-body>
	    </fo:table>
    </fo:static-content>
	<!-- BAS de page : END -->
    <fo:static-content flow-name="xsl-region-start">
    </fo:static-content>
    <fo:static-content flow-name="xsl-region-end">
    </fo:static-content>
    <!-- PAGE CONTENTS -->
    <fo:flow flow-name="xsl-region-body" font-family="Arial, Helvetica, sans-serif" font-size="7pt">
		<xsl:apply-templates/>
    </fo:flow>
    <!-- PAGE CONTENTS : END-->
</fo:page-sequence>
  
  
</fo:root>
</xsl:template>

<!-- TPL SAUT DE PAGE-->
<xsl:template name="saut-de-page" match="saut-de-page">
	<fo:table>
    <fo:table-column/>
	<fo:table-body>
	<fo:table-row><fo:table-cell><fo:block break-after="page"></fo:block></fo:table-cell></fo:table-row>
	</fo:table-body>
	</fo:table>
</xsl:template>
<!-- TPL /SAUT DE PAGE-->

<xsl:template match="agents">
	<!-- on s'assure qu'il y a au moins un agent a imprimer -->
	<xsl:if test="count(agent) &gt; 0">
		<xsl:apply-templates/>
	</xsl:if>
	<!-- pas d'agent / horaires / associations : afficher un message -->
	<xsl:if test="count(agent) = 0">
		<fo:table width="100%" table-layout="fixed" space-before="5mm" padding="1pt 4pt 1pt 4pt" border-style="solid" border-width="0.5pt" font-size="10pt">
	   		<fo:table-column/>
			<fo:table-body>
			   	<fo:table-row>
					<fo:table-cell padding="0pt">
						<fo:block>rien a imprimer : pas d'horaire(s) associé(s) au(x) planning(s)</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:if>
</xsl:template>

<xsl:template match="agent">
	<xsl:apply-templates/>
</xsl:template>

<xsl:template match="semaines">
	<!-- les associations des horaires aux semaines  -->
	<fo:table width="100%" table-layout="fixed" space-before="5mm" padding="1pt 4pt 1pt 4pt" border-style="solid" border-width="0.5pt" font-size="10pt">
    <fo:table-column/>
		<fo:table-body>
		   	<fo:table-row keep-with-next="always">
				<fo:table-cell padding="0pt">
					<!-- agent et libelle  -->
					<fo:table table-layout="fixed" space-before="0pt" padding="3pt" background-color="#EEEEEE">
						<fo:table-column/>
						<fo:table-body>
				          <fo:table-row keep-with-next="always">
				            <fo:table-cell text-align="center"><fo:block><fo:inline font-weight="bold">Association Horaires-Semaines : </fo:inline><xsl:value-of select="../nom"/></fo:block></fo:table-cell>
				          </fo:table-row>
				        </fo:table-body>
					</fo:table>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row keep-with-next="always">
				<fo:table-cell padding="0pt">
					<fo:table table-layout="fixed" space-before="0pt" padding="2pt">
					<fo:table-column/>
					<fo:table-body>
						<fo:table-row keep-with-next="always">
							<fo:table-cell number-columns-spanned="6" padding="1mm 3mm 1mm 3mm">
								<fo:table border-width="0.2pt 0pt 0pt 0pt" border-style="solid" border-color="#BBBBBB" padding="2pt 10pt 2pt 10pt">
									<fo:table-column column-width="proportional-column-width(1)"/>
									<fo:table-column column-width="proportional-column-width(1)"/>
									<fo:table-column column-width="proportional-column-width(1)"/>
									<fo:table-column column-width="proportional-column-width(3)"/>
									<fo:table-body>
									<!-- Titre du tableau -->
										<fo:table-row keep-with-next="always">
											<fo:table-cell text-align="center"><fo:block font-weight="bold">Numéro</fo:block></fo:table-cell>
											<fo:table-cell text-align="center"><fo:block font-weight="bold">Début</fo:block></fo:table-cell>
											<fo:table-cell text-align="center"><fo:block font-weight="bold">Fin</fo:block></fo:table-cell>
											<fo:table-cell text-align="center"><fo:block font-weight="bold">Horaire</fo:block></fo:table-cell>
										</fo:table-row>						  
										<xsl:apply-templates/>
								  </fo:table-body>
							  </fo:table>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
		    </fo:table-cell>
			</fo:table-row>
		</fo:table-body>
	</fo:table>
	<!-- pas de saut de page a la derniere page -->
	<xsl:if test="agent != agent[last()]">
		<xsl:call-template name="saut-de-page"/>
	</xsl:if>
</xsl:template>

<!-- une semaine -->
<xsl:template match="semaine">
	<!-- alterner les fonds pair / impair -->
	<xsl:variable name="fond-semaine">
		<xsl:if test="position() mod 4=2">#EEEEEE</xsl:if>
		<xsl:if test="position() mod 4!=2">#FFFFFF</xsl:if>
	</xsl:variable>
	<fo:table-row font-size="10pt" text-align="center" background-color="{$fond-semaine}">
	    <fo:table-cell text-align="center">
			<fo:block><xsl:value-of select="numero"/></fo:block>
		</fo:table-cell>
	    <fo:table-cell text-align="center">
			<fo:block><xsl:value-of select="debut"/></fo:block>
		</fo:table-cell>
		<fo:table-cell text-align="center">
			<fo:block><xsl:value-of select="fin"/></fo:block>
		</fo:table-cell>
		<fo:table-cell text-align="center">
			<fo:block><xsl:value-of select="horaire"/></fo:block>
		</fo:table-cell>
	</fo:table-row>
</xsl:template>

<xsl:template match="horaires">
	<xsl:apply-templates/>
	<xsl:call-template name="saut-de-page"/>
</xsl:template>

<xsl:template match="horaire">
	<!-- un horaire -->
	<fo:table width="100%" table-layout="fixed" space-before="5mm" padding="1pt 4pt 1pt 4pt" border-style="solid" border-width="0.5pt" font-size="10pt">
    <fo:table-column/>
		<fo:table-body>
		   	<fo:table-row keep-with-next="always">
				<fo:table-cell padding="0pt">
					<!-- agent et libelle  -->
					<fo:table table-layout="fixed" space-before="0pt" padding="3pt" background-color="#EEEEEE">
						<fo:table-column/>
						<fo:table-column/>
						<fo:table-column/>
				        <fo:table-body>
				          <fo:table-row keep-with-next="always">
				            <fo:table-cell text-align="center"><fo:block><fo:inline font-weight="bold">Agent : </fo:inline><xsl:value-of select="../../nom"/></fo:block></fo:table-cell>
				            <fo:table-cell text-align="center"><fo:block><fo:inline font-weight="bold">Libellé : </fo:inline><xsl:value-of select="libelle"/></fo:block></fo:table-cell>
							<fo:table-cell text-align="center"><fo:block><fo:inline font-weight="bold">Quotité : </fo:inline><xsl:value-of select="quotite"/></fo:block></fo:table-cell>
				          </fo:table-row>
				        </fo:table-body>
					</fo:table>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row keep-with-next="always">
				<fo:table-cell padding="0pt">
					<fo:table table-layout="fixed" space-before="0pt" padding="2pt">
					<fo:table-column/>
					<fo:table-body>
						<fo:table-row keep-with-next="always">
							<fo:table-cell number-columns-spanned="6" padding="1mm 3mm 1mm 3mm">
								<fo:table border-width="0.2pt 0pt 0pt 0pt" border-style="solid" border-color="#BBBBBB" padding="2pt 10pt 2pt 10pt">
									<fo:table-column column-width="proportional-column-width(1)"/>
									<fo:table-column column-width="proportional-column-width(4)"/>
									<fo:table-column column-width="proportional-column-width(4)"/>
									<fo:table-column column-width="proportional-column-width(2)"/>
									<fo:table-column column-width="proportional-column-width(2)"/>
									<fo:table-column column-width="proportional-column-width(2)"/>
									<fo:table-body>
									<!-- Titre du tableau -->
										<fo:table-row keep-with-next="always">
											<fo:table-cell text-align="center"><fo:block font-weight="bold">Jour</fo:block></fo:table-cell>
											<fo:table-cell text-align="center"><fo:block font-weight="bold">Matin</fo:block></fo:table-cell>
											<fo:table-cell text-align="center"><fo:block font-weight="bold">Après-midi</fo:block></fo:table-cell>
											<fo:table-cell text-align="center"><fo:block font-weight="bold">Total</fo:block></fo:table-cell>
											<fo:table-cell text-align="center"><fo:block font-weight="bold">Pause</fo:block></fo:table-cell>
											<fo:table-cell text-align="center"><fo:block font-weight="bold">Bonus</fo:block></fo:table-cell>
										</fo:table-row>						  
										<xsl:apply-templates/>
										<xsl:call-template name="totaux"/>
								  </fo:table-body>
							  </fo:table>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
		    </fo:table-cell>
			</fo:table-row>
		</fo:table-body>
	</fo:table>	
</xsl:template>

<!-- un horaire journalier -->
<xsl:template match="jour">
  <fo:table-row font-size="10pt" text-align="center">
    <fo:table-cell text-align="center">
		<fo:block font-style="italic"><xsl:value-of select="libelle"/></fo:block>
	</fo:table-cell>
    <fo:table-cell text-align="center">
		<fo:block>
			<xsl:if test="debut-am != ''"><xsl:value-of select="debut-am"/>-<xsl:value-of select="fin-am"/></xsl:if>	
		</fo:block>
	</fo:table-cell>
	<fo:table-cell text-align="center">
		<fo:block>
			<xsl:if test="debut-pm != ''"><xsl:value-of select="debut-pm"/>-<xsl:value-of select="fin-pm"/></xsl:if>
		</fo:block>
	</fo:table-cell>
    <fo:table-cell text-align="center">
		<fo:block>
			<xsl:if test="total != '00h00'"><xsl:value-of select="total"/></xsl:if>
		</fo:block>
	</fo:table-cell>
    <fo:table-cell text-align="center">
		<fo:block><xsl:value-of select="pause"/></fo:block>
	</fo:table-cell>
    <fo:table-cell text-align="center">
		<fo:block>
			<xsl:if test="debut-am != '' or debut-pm != ''">
				<xsl:if test="bonus != '00h00'"><xsl:value-of select="bonus"/></xsl:if>
				<xsl:if test="bonus = '00h00'">-</xsl:if>
			</xsl:if>
		</fo:block>
	</fo:table-cell>
  </fo:table-row>
</xsl:template>

<!-- totaux -->
<xsl:template name="totaux">
  <fo:table-row font-size="10pt" text-align="center" font-weight="bold">
    <fo:table-cell/>
    <fo:table-cell/>
	<fo:table-cell/>
    <fo:table-cell text-align="center" border-width="0.2pt 0pt 0pt 0pt" border-style="dotted" border-color="#BBBBBB" padding-top="3pt">
		<fo:block><xsl:value-of select="total"/></fo:block>
	</fo:table-cell>
    <fo:table-cell/>
    <fo:table-cell text-align="center" border-width="0.2pt 0pt 0pt 0pt" border-style="dotted" border-color="#BBBBBB" padding-top="3pt">
		<fo:block><xsl:value-of select="bonus"/></fo:block>
	</fo:table-cell>
  </fo:table-row>
</xsl:template>

</xsl:stylesheet>
