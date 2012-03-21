<?xml version="1.0" encoding="ISO-8859-1"?>
<!DOCTYPE xsl:stylesheet
[
<!ENTITY  nbsp  "&#160;">
<!ENTITY  space  "&#x20;">
<!ENTITY  br  "&#x2028;">
]>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

   <!-- définition des patterns pour la fonction format-number -->
   <xsl:decimal-format decimal-separator="," grouping-separator=" "/>

	<!-- TPL une transaction -->
   <xsl:template name="transaction" match="transaction">
		<fo:table-row font-size="8pt" text-align="center">
			<fo:table-cell border-left-width="0.5pt" border-left-style="solid"><fo:block><xsl:value-of select="nom"/></fo:block></fo:table-cell>
			<fo:table-cell border-left-width="0.5pt" border-left-style="solid"><fo:block><xsl:value-of select="credit"/></fo:block></fo:table-cell>
			<fo:table-cell border-left-width="0.5pt" border-left-style="solid"><fo:block><xsl:value-of select="debit"/></fo:block></fo:table-cell>
			<fo:table-cell border-left-width="0.5pt" border-left-style="solid"><fo:block><xsl:value-of select="restant"/></fo:block></fo:table-cell>
			<fo:table-cell border-left-width="0.5pt" border-left-style="solid"><fo:block><xsl:value-of select="etat"/></fo:block></fo:table-cell>
		</fo:table-row>	
	</xsl:template>
	
   <!-- TPL les tableau -->
	<xsl:template name="liste" match="liste">
		<fo:table border-width="0.5pt" border-style="solid">
			<fo:table-column column-width="60mm"/>
			<fo:table-column/>
			<fo:table-column/>
			<fo:table-column/>
			<fo:table-column/>
			<fo:table-body>
	     	<xsl:apply-templates select="transaction"/>
			</fo:table-body>
		</fo:table>
   </xsl:template>
   
   
   
   <!-- TPL entete -->
   <xsl:template name="entete">
		<fo:table>
			<fo:table-column column-width="1in"/>
			<fo:table-column/>
			<fo:table-column/>
			<fo:table-column column-width="1in"/>
			<fo:table-body>
	     	<fo:table-row>
					<fo:table-cell text-align="left" number-columns-spanned="2"><fo:block font-size="8pt">Liste des CET</fo:block></fo:table-cell>
					<fo:table-cell text-align="right" number-columns-spanned="2"><fo:block font-size="8pt"><xsl:value-of select="dateImpression"/></fo:block></fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell number-columns-spanned="4" border-top-style="solid" border-top-width="0.5pt"><fo:block font-size="8pt"></fo:block></fo:table-cell>
				</fo:table-row>
				</fo:table-body>
			</fo:table>
			<fo:table border-width="0.5pt" border-style="solid" space-before="5mm" font-size="9pt">
			<fo:table-column column-width="60mm"/>
			<fo:table-column/>
			<fo:table-column/>
			<fo:table-column/>
			<fo:table-column/>
			<fo:table-body>
				<fo:table-row border-width="0.5pt solid" background-color="silver" border-style="solid" font-size="10pt" text-align="center">
					<fo:table-cell><fo:block>Nom</fo:block></fo:table-cell>
					<fo:table-cell><fo:block>Credit</fo:block></fo:table-cell>
					<fo:table-cell><fo:block>Debit</fo:block></fo:table-cell>
					<fo:table-cell><fo:block>Restant</fo:block></fo:table-cell>
					<fo:table-cell><fo:block>Etat</fo:block></fo:table-cell>
				</fo:table-row>	
			</fo:table-body>
		</fo:table>
   </xsl:template>
   
   
   
   <!-- TPL piedDePage -->
   <xsl:template name="piedDePage" match="piedDePage">
   		<fo:table width="100%" table-layout="fixed" space-before="0pt" padding="0pt">
	    <fo:table-column/>
		  <fo:table-body>
		    <fo:table-row text-align="center" display-align="center">
		    	<fo:table-cell>
    				<fo:block font-size="8pt" text-align="center">- <fo:page-number/> -</fo:block>
     	  	</fo:table-cell>
     	  </fo:table-row>
     	</fo:table-body>
    </fo:table>
	</xsl:template>
   
  
      
   <!-- TPL total -->
   <xsl:template name="total" match="total">
   		<fo:table width="100%" table-layout="fixed" space-before="0pt" padding="0pt">
	    <fo:table-column/>
		  <fo:table-body>
		    <fo:table-row height="10mm"/>
		    <fo:table-row text-align="center" display-align="center">
		    	<fo:table-cell>
    				<fo:block font-size="12pt" text-align="center" font-weight="bold">Total : <xsl:value-of select="total"/></fo:block>
     	  	</fo:table-cell>
     	  </fo:table-row>
     	</fo:table-body>
    </fo:table>
	</xsl:template>
   
  
   
   
   <!-- TPL CongesListeCet -->
   <xsl:template name="CongesListeCet" match="CongesListeCet">
		<fo:static-content flow-name="xsl-region-end">		
		</fo:static-content>
		
		<fo:static-content flow-name="xsl-region-after">
			<xsl:call-template name="piedDePage"/>
		</fo:static-content>
	
		<fo:static-content flow-name="xsl-region-before">
			<xsl:call-template name="entete"/>
		</fo:static-content>

		<fo:static-content flow-name="xsl-region-start">
		</fo:static-content>
	
		<fo:flow flow-name="xsl-region-body">	
			<xsl:apply-templates select="liste"/>
			<xsl:call-template name="total"/>
		</fo:flow>
  </xsl:template>
	

  <xsl:template name="toto" match="/">
  	<fo:root>
      <fo:layout-master-set>
    	  <fo:simple-page-master master-name="page" margin-top="1cm" margin-bottom="1cm" page-width="21cm" page-height="29.7cm" margin-left="1cm" margin-right="1cm">
          <fo:region-before extent="45.1mm" margin="0pt" />
          <fo:region-after extent="1mm" />
      	 	<fo:region-start />
          <fo:region-end />
          <fo:region-body margin-bottom="10mm" margin-top="12.5mm" margin-left="0cm" margin-right="0cm" />
        </fo:simple-page-master>
      </fo:layout-master-set>		 
		 	<fo:page-sequence master-reference="page" >
			 	<xsl:apply-templates select="CongesListeCet"/>
      </fo:page-sequence>		 
    </fo:root>
  </xsl:template>	

</xsl:stylesheet>
