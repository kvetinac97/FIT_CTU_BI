# Jednoduchý spreadsheet

<p>Vytvořte aplikaci implementující jednoduchý tabulkový editor.</p>

<p>
    Buňky pro váš spreadsheet musí podporovat text nebo výrazy (začínající znakem '='). \n
    <i>(Poznámka: text v buňce MUSÍ začínat na znak =, pokud před ním budou bílé znaky, buňka nebude považována za výraz.)</i>
</p>

<p>
    Podporovanými výrazy budou logický výraz IF a dále vybrané aritmetické výrazy, \n
    které však podporují pouze buňky s ČÍSELNÝMI hodnotami. Jediný výraz, který se vyhodnotí \n
    správně i v případě buňky s nečíselnou hodnotou je <code>=BUŇKA</code>.
</p>

<p>
    <strong>Logický výraz IF</strong> NEpodporuje žádné složité řetězení, vícenásobné podmínky ani argumenty. \n
    Podporuje však aritmetický výraz jako argument ( např. <code>=IF(A1 > (5 + 3);A2 + A3;A4)</code> ). \n
</p>

<h4>Syntaxe logického výrazu IF je následující:</h4>

<pre>
input := IF ( math_expr comparator math_expr ; { math_expr | "string" } ; { math_expr | "string" } )
comparator := { '>' | '<' | '==' | '!=' | '<=' | '>=' }
</pre>

<p>kde string je jakýkoliv textový řetězec neobsahující znak uvozovka (") a math_expr aritmetický výraz. \n
<i>(Připomínka: logický mód podporuje pouze buňky s číselným obsahem. =IF ( 5 > 3 ; A1 ; 1 ) bude tedy platný výraz pouze pokud A1 je číslo.)</i> \n
<i>(Poznámka: logický výraz MUSÍ začínat znaky =IF, bílé znaky mohou být až před závorkou. = IF() je tedy neplatný výraz.)</i> </p>

<p>
    <strong>Aritmetické výrazy</strong> jsou ze všech nejkomplexnější. \n
    Mohou obsahovat základní operátory <code>(+, -, *, /)</code>
    a unární <code>(abs, sin, cos)</code> či binární <code>(min, max, pow)</code> funkce.
</p>

<p>
    V aritmetických výrazech <b>LZE</b> používat závorky a platí priority operátorů a závorek \n
    ( * má prioritu před + a -, závorky se řeší prvně ).
</p>

<p>
    Aritmetické výrazy musí splňovat následující formát, jinak budou vyhodnoceny \n
    jako neplatné a v buňce se zobrazí hodnota <b>"!#EXPR"</b> :
</p>

<pre>
input              := bracket_expression
bracket_expression := 0_or_more { '+' } 0_or_more { '-' } { '(' expression ')' | expression }
expression         := { number | cell | unary_function | binary_function | binary_operator }
cell               := X { '1' - '9' } 1_or_more { '0' - '9' }
integer            := 0_or_1 { '+' | '-' } 1_or_more { '0' - '9' }
number             := integer 0_or_1 { '.' 1_or_more { '0' - '9' } }
unary_function     := xxx ( expression )
binary_function    := xxx ( expression ';' expression )
binary_operator    := bracket_expression op bracket_expression
op                 := { '+' | '-' | '*' | '/' }
X                  := { 'A' - 'Z' }
x                  := { 'a' - 'z' }
</pre>

<p>
    Aritmetické výrazy detekují cykly a <b>NEDOVOLÍ</b> vyhodnocení výrazu, který by cyklus způsobil. \n
    V případě, že by buňka způsobila cyklus, se v buňce zobrazí hodnota <b>"!#LOOP"</b>. \n
    Při pokusu o získání hodnoty buňky, která neexistuje, se výraz vyhodnotí jako <b>"!#REF"</b>. \n
    Aritmetické výrazy se kvůli (ne)přesnosti datového typu double nemusí vyhodnocovat přesně.
</p>

<p>
    Vytvořená tabulka půjde <b>uložit</b> a znovu <b>načíst</b> a taktéž bude fungovat <b>export</b> do CSV souboru. \n
    Při načítání dojde k <strong>přepsání</strong> původní tabulky (stará data nebudou zachována). \n
    Při uložení se uloží <strong>raw</strong> hodnoty buněk, tedy včetně vzorců v nich obsažených. \n
    Při exportu se uloží pouze <strong>vypočtené</strong> hodnoty buněk (bez vzorců). \n
    Uložení ani export nijak <strong>neolivní</strong> a <strong>nesmaže</strong> obsah tabulky.
</p>

<p><strong>Struktura save souboru</strong> je přesně zadána a musí být dodržena, jinak soubor nebude načten.</p>

<p>
    Soubor obsahuje <strong>buňky</strong>, formát jedné buňky je následující: \n
    <code>[column][space][row][space][content][newline]</code>, \n
    kde space je právě jeden znak mezery (' '), newline právě jeden znak nové řádky (\\n), \n
    column znak A-Z, 0 < row <= 1 000 000 a content libovolný (i prázdný) obsah buňky.
</p>

<p>
    V případě <strong>duplicitních</strong> buněk (více řádků se stejným row a col) \n
    není zajištěno, z kterého řádku bude načten obsah buňky.
</p>

<p>
    Aplikace bude pracovat v konzolovém rozhraní, kdy práce s daty bude probíhat pomocí <b>příkazů</b>:
    <ul>
    <li> <b>set</b> [buňka] [data] - nastaví hodnotu [data] do buňky [buňka] </li>
    <li> <b>clear</b> [buňka] - smaže obsah buňky [buňka] </li>
    <li> <b>print</b> [buňka] - získá raw obsah buňky (vzorec), případně vypíše chybovou hlášku </li>
    <li> <b>get</b> [buňka] - získá obsah buňky (vypočtený), případně vypíše chybovou hlášku </li>
    </ul>
    a práce s tabulkou (uložení, export) pomocí <b>příkazů</b>:
    <ul>
    <li> <b>save</b> [file] - uloží <b>neprázdnou</b> tabulku do souboru [file]. Upozorní na neexistující soubor... </li>
    <li> <b>export</b> [file] - exportuje <b>neprázdnou</b> tabulku do souboru [file] včetně kontroly souboru</li>
    <li> <b>load</b> [file] - pokusí se načíst tabulku ze souboru [file]. Hlásí chybný formát i neplatný soubor. </li>
    </ul>
    Ukončit aplikaci půjde příkazem <strong>stop</strong> či <strong>end</strong>.
</p>

<p>
    Jméno příkazu musí být vždy zadáno přesně tak, jak je, bez uvozovek. \n
    Jednotlivé <b>argumenty</b> budou buď zadány jednoslovně bez uvozovek ( <code>save soubor.txt</code> ), \n
    poslední argument může být zadán víceslovně v uvozovkách. Za druhou uvozovkou pak MUSÍ být znak nové řádky (\\n). \n
    ( <code>export "Víceslovný soubor.csv" \\n load "Hodně dlouhý soubor.data"</code> \\n ).
</p>

<p>
    Pokud bude v posledním argumentu (v jiných není povolen) využit znak uvozovky, bude <b>vynechán</b>. ( <code>set A1 P"e"pa</code> se uloží jako Pepa ) \n
    Výjimku tvoří uvozovky v logických výrazech ( <code>set A1 "=IF ( 1 > 1 ; "test" ; "test2" )"</code> )
</p>

<p>
    Program umožňuje nepovinný argument <b>assert</b> ( ./program assert ). Pokud není nastaven, \n
    program na příkazovou řádku vypisuje veškeré detaily příkazů ( <code>The value of cell xxx set to xxx</code> ). \n
    V opačném případě budou vypsány pouze <b>CHYBOVÁ HLÁŠENÍ</b> a výstup z příkazu PRINT/GET (ve formátu "hodnota").
</p>

<p>
    <strong>Sloupce</strong> budou pojmenovány postupně od 'A' až do 'Z', platí tedy omezení na maximálně 26 sloupců. \n
    <strong>Řádky</strong> budou číslovány klasicky 1, 2, 3, ... a jejich počet je omezen na 1 000 000.
</p>

\section human Lidský popis práce programu:

<p>
Program lze využít k jednoduché správě tabulek. Tabulku lze načíst ze souboru příkazem <b>load</b>, \n
uložit příkazem <b>save</b> a exportovat do CSV příkazem <b>export</b>. S buňkami jde pracovat pomocí \n
příkazů <b>set</b> buňka "hodnota", <b>get/print</b> buňka a clear buňka. Používat jde matematické výrazy, \n
logický výraz IF a klasické textové hodnoty buněk.
</p>

\section examples Ukázka práce programu:

<p>
Ve složce <strong>examples/</strong> je ukázkový příklad práce programu na různých matematických, \n
logických výrazech a při načítání, ukládání a exportování. \n
Celou sadu testů lze spustit pomocí skriptu <b>examples/asserts.sh</b>, \n
manuálně pak lze vyzkoušet načtení/uložení souborů ve složce <b>examples/sheets</b> \n
\n
( <i>Poznámka: Referenční řešení (.ref) pochází z Google Sheets s úpravou formátování. </i> )
</p>

\section polymorph Polymorfismus:

<p>
v programu je užit <strong>polymorfismus</strong>, a to vícenásobně. Mezi příklady užití patří:

<ol>

<li> CCell::getValue() umožňuje získat hodnotu buňky uložené na určité pozici \n
v tabulce. Polymorfismus je zde využit tak, že get příkazu je úplně jedno, jaký typ buňka \n
má - implementace funkce je pokaždé přizpůsobená typu buňky (text, výraz). </li>

<li> CCommand::execute() umožňuje jednoduché vykonávání příkazů přímo v CCommandLoader. \n
Místo toho, aby se na základě přijatého příkazu ze vstupu musely složitě získávat \n
argumenty a následně zjišťovat výsledek příkazu, toto rozhraní to značně zjednodušuje. </li>

<li> CParser::getResult() usnadňuje práci při parsování výrazu v CExpressionCell. \n
Výrazová buňka určí typ parseru pomocí začátečního textu v buňce a následně se už nemusí starat \n
o implementaci parsování. </li>

<li> CBinaryFunction::calculate(), CBinaryOperator::calculate(), CUnaryFunction::calculate() \n
jsou velmi důležité polymorfní metody, jelikož umožňují v kódu (konkrétně CMathParser) \n
pracovat s výsledkem přímo přes jednu metodu a ne přes složitá přetypování. </li>

<li> CBinaryComparator::compare() funguje podobně jako calculate() v bodu 4. </li>

</ol>