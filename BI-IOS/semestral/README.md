# RestSys
Chytrý restaurační systém - semestrální práce (nejen) do předmětu IOS

<h3>Backstory</h3>
<hr>
V letním semestru 2019/2020 jsem absolvoval předmět BI-DBS a výstupem
tohoto předmětu byla semestrální práce na téma "Restaurace Zdeňka Pohlreicha".
<br><br>
V zimním semestru 2020/2021 přišel čas posunout toto téma na vyšší, nový level - restaurační systém složený
ze serverové části obsluhující status objednávek a klientských částí - baru a kuchyně.
<br><br>
Prvním krokem bylo vytvoření Rest API v rámci předmětu BI-TJV.
API je napsáno v Javě (framework Spring) a obsahuje CRUD metody pro Alergeny, Jídla, Stoly, Objednávky a Jídlo v objednávce.
Toto API by samo o sobě za moc nestálo, proto jsem k němu vytvořil i <a href="https://youtu.be/8idZruMo8Gs">podpůrnou Android aplikaci se základními metodami (preview zde)</a>
<br><br>

<h3>Back to present</h3>
<hr>
Tato iOS aplikace slouží jak k obsluze chytrého restauračního systému, tak pro veřejnost.

Veřejnost si může zobrazit <strong>seznam jídel</strong>, které restaurace daný den nabízí, včetně alergenů v nich obsažených.
Alergeny se samozřejmě řídí přísnými Evropskými nařízeními.

Pro majitele restaurace je zde možnost <strong>vytváření a úpravy jídel</strong> v Jídelníčku, včetně názvu, ceny a alergenů.
Následně může majitel také <strong>vytvářet a odebírat stoly</strong> třech typů - Barový, Vnitřní a Venkovní.

Pro číšníky je zde jednoduché <strong>rozhraní pro vytváření objednávek</strong>. Číšník přijde ke stolu a zadá objednávku
do systému intuitivně výběrem z jídelníčku. Číšník následně načepuje dle objednávky pití.

Další částí je <strong>kuchyňská tabule</strong>, kde se objeví veškeré jídlo (nikoliv pití), které je potřeba připravit.
Jakmile je jídlo připraveno, číšník jej odnese a odklikne z kuchyňské tabule. V případě, že by se spletl, je zde možnost
otevřít historii a "missclick" vrátit zpět.

Ve chvíli, kdy je veškeré pití a jídlo v objednávce doneseno a snězeno, číšník <strong>vytiskne účtenku</strong> (v aplikaci
je realizováno pomocí vytvoření PDF), zkasíruje zákazníky a objednávku potvrdí. Pokud došlo k chybě, do potvrzení objednávky
lze objednávku jak zrušit, tak změnit. Nutno ale podotknout, že pokud bude z objednávky odstraněno již odnesené jídlo
(odkliknuté v kuchyni), propadá ve prospěch zákazníka a škoda bude číšníkovi stržena z prémií.

<i>(Poznámka: pro přihlášení jako majitel restaurace použijte login "wrzecond" a heslo "password". Pro přihlášení jako
číšník použijte login "bliznjan" a heslo "bliznjan")</i>

<h3>Použité technologie, realizace</h3>
<hr>
Aplikace je napsána ve <strong>Swift 5</strong> především s pomocí <strong>SwiftUI 2</strong> (2020). Využívá <strong>View - ViewModel</strong> architekturu
Jelikož je SwiftUI pořád poměrně nová technologie, semestrálka se neobešla také bez využití některých knihoven (instalovány přes Swift Package Manager)


 - <a href="https://github.com/timbersoftware/SwiftUIRefresh.git">SwiftUIRefresh</a> (potáhnutí pro refresh seznamu)
 - <a href="https://github.com/yonat/MultiSelectSegmentedControl">MultiSelectSegmentControl</a> (výběr alergenů při vytváření jídla)
 
a také využití UIKitu (pro generování PDF).

V semestrální práci jsou použity všechny <strong>požadované technologie</strong>, tedy:


 - pokročilá navigace (<strong>TabView</strong> pro navigaci mezi stoly, jídelníčkem a kuchyňským panelem), <strong>NavigationView</strong> pro seznam alergenů a
   objednávek na stole, <strong>Modální prezentace</strong> pro vytváření stolů / jídel)
 - networking (komunikace s vlastním <strong>Rest API</strong> - celá aplikace, propojení s <strong>Firebase Realtime Database</strong> - kuchyňský panel)
 - seznam / grid (<strong>List</strong> pro většinu seznamů jídel, <strong>LazyVGrid</strong> pro stoly)
 - jako zajímavou feature bych bral jednak systém přidávání jídel do objednávek (na kuchyňském panelu se opravdu objeví pouze to, co má, i při odebírání...)
   a také <strong>generování PDF účtenky</strong> (zajímavé je využití SwiftUI pro layout PDF)
 - <strong>slušné UI/UX</strong> je zaručeno správným použitím SwiftUI, které už samo o sobě řeší AutoLayout apod.
 - <strong>slušný kód</strong> je dán snahou o dodržení architektury i principů SwiftUI (neopakující se kód, správné rozdělení...)
<hr>

&copy; Ondřej Wrzecionko 2021
