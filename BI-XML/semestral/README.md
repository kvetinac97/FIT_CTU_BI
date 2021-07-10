# XML projekt (wrzecond)

### Použití:
 - kontrola souhrnného souboru vůči DTD schématu: <code>xmllint --noout --dtdvalid schema/schema.dtd data.xml</code>
 - kontrola jednotlivých souborů vůči DTD schématu: <code>xmllint --noout --dtdvalid schema/schema.dtd data/*.xml</code>
 - kontrola všech souborů vůči RelaxNG schématu: <code>xmllint --noout --relaxng schema/schema.rng data.xml</code>
 - transformace data.xml souboru do HTML výstupu: <code>java -jar libs/bin/saxon.jar -o:html/output.html -s:data.xml -xsl:xslt/html.xslt</code>
 - transformace data.xml souboru do PDF výstupu: <code>java -jar libs/bin/saxon.jar -o:pdf/output.fo -s:data.xml -xsl:xslt/pdf.xslt && libs/fop -fo pdf/output.fo -pdf pdf/output.pdf</code>

Případně (jednodušeji) přímo použitím Dockeru <code>docker build .</code>

### Postup při návrhu jednotlivých částí:
 - <a href="#prevod">Převod informací z World factbook do XML souborů</a>
 - <a href="#validace">Validace pomocí DTD a RelaxNG</a>
 - <a href="#xslt-html">Vytvoření HTML souboru pomocí XSLT</a>
 - <a href="#xsl-fo">Vytvoření PDF souboru pomocí XSL-FO</a>
 - <a href="#docker-ci">Nasazení do Dockeru a Gitlab CI</a>

<div id="prevod">
<h6>Převod informací z World factbook</h6>
Převod jsem prováděl především manuálně. Nejprve jsem si na základě dat z Afghanistánu vytvořil soubor
se strukturou a následně jsem pro každou zemi vykopíroval po jednotlivých částech text kapitol a pomocí
regulárních výrazů (IDE IntelliJ - zkratka Ctrl+R, regex mód) převedl na strukturu.

Pro různé kapitoly jsem taky využil různý způsob struktury (&lt;tag attr1="a" attr2="b"&gt; 
oproti &lt;tag&gt; &lt;attr1&gt;a&lt;/attr1&gt; &lt;attr2&gt;b&lt;/attr1&gt; &lt;/tag&gt; ... ).
</div>

<div id="validace">
<h6>Validace pomocí DTD a RelaxNG</h6> 
Kostru validačních schémat jsem vytvořil opět pomocí IDE IntelliJ (Tools > XML Actions > Generated DTD from XML file)
a následně (Tools > XML Actions > Convert schema) pro RelaxNG.
Následně jsem provedl úpravy tak, aby se ve schématu neopakovaly zbytečně různé prvky a byly využity všechny možnosti:

V DTD šlo především o správné nastavení #IMPLIED a #REQUIRED 
V RelaxNG jsem již využil pokročilé schopnosti validace dat a proto jsem sloučil 
opakující se prvky, atributy... ( &lt;define&gt;) a využil možnost validace typů daných prvků
pomocí dataTypeLibrary.
</div>



<div id="xslt-html">
<h6>Vytvoření HTML souboru pomocí XSLT</h6>
Další fází bylo vytvoření HTML souboru. K tomu jsem použil XSLT. Kromě základní struktury (xsl:value-of)
jsem použil také podmíněné větve (xsl:choose, xsl:if), cykly pro vyhnutí se opakování (xsl:for-each).
Celý XSLT soubor pro tvorbu HTML jsem vytvářel manuálně.
Jako "pokročilejší formátování HTML" jsem zvolil pár koláčových grafů, obrázek vlajky, mapu zobrazovaného státu
a CSS formátování přes Bootstrap pro lepší navigaci a vzhled.
</div>



<div id="xsl-fo">
<h6>Vytvoření PDF souboru pomocí XSL-FO</h6>
Poslední fází bylo vytvoření PDF souboru, k čemuž jsem využil XSL-FO. 
Zde jsem využil toho, že jsem již měl k dispozici XSLT soubor pro tvorbu HTML a pomocí regulárních výrazů v IntelliJ
jsem pro každou sekci převedl například &lt;b&gt; na &lt;fo:inline style font-weight="bold"&gt; apod...
Nejprve se vytvoří output.fo soubor, ze kterého se následně pomocí fop vytvoří výsledný PDF soubor.
Opět je zde využito větvení a cykly (xsl:if, xsl:choose, xsl:for-each) a jako "pokročilejší formátování PDF" 
je voleno vložení vlajky státu, seznamy určitých částí v dílčích kapitolách a v neposlední řadě tabulka.
</div>

<div id="docker-ci">
<h6>Nasazení do Dockeru a Gitlab CI</h6>
Abych měl dobrou zpětnou vazbu na "funkčnost" schémat a XSLT skriptů, 
rozhodl jsem se nasadit vše do Dockeru (obraz pro práci s XML, Saxon/FOP ve složce libs/) 
Continuos Integration na Gitlabu následně umožňuje jednak zobrazit "neúspěšný test" v případě 
chyby validace a jednak vygenerovanou HTML a PDF stránku nabídnout ke stažení v sekci "Artifacts".
</div>