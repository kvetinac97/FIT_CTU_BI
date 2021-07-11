# RestSys

Chytrý restaurační systém - semestrální práce do předmětu KOT, TJV

<h3>Backstory</h3>
<hr>
V letním semestru 2019/2020 jsem absolvoval předmět BI-DBS a výstupem
tohoto předmětu byla semestrální práce na téma "Restaurace Zdeňka Pohlreicha".
<br><br>
V zimním semestru 2020/2021 jsem s tímto tématem nadále pokračoval ve předmětech BI-TJV (Spring Java - Rest API + Android Java - aplikace) a BI-IOS (Swift - aplikace).
<br><br>
Příběh pokračuje, a v letním semestru 2020/2021 se RestSys, chytrý restaurační systém, dostává na další úroveň - kromě rozšíření do Linked data a SPARQL (v předmětu VWM) se dostává také do Kotlinu, kde objevuje svět plný nulability, korutin, Kotestu...

<h3>Části</h3>
<hr>
Semestrální práce se skládá ze 3 částí, <b>všechny jsou napsány v Kotlinu</b>:
<ul>
 <li><b>app-model</b>, který představuje společnou vrstvu pro komunikaci mezi klientem a serverem (DTO)</li>
 <li><b>rest-server</b>, který představuje klasické Rest API vytvořené s využitím frameworku <b>Spring</b> (repository - service - controller)</li>
 <li><b>rest-client</b> = klientská část v podobě <b>Android</b> aplikace</li>
</ul>

<h3>Spuštění serveru</h3>
<hr>
Pro zkompilování serveru doporučuji klasicky otevřít v IntelliJ celý projekt (GitHub složku),
následně přejít do složky app-model <code>cd app-model</code> a nainstalovat ho do Mavenu přes
<code>mvn install</code>. Následně přejít do složky rest-server <code>cd ../rest-server</code>
a provést klasické spuštění <code>mvn clean compile exec:java</code> nebo testy <code>mvn test</code>.
Pro usnadnění práce se v Gitlab CI vždy vytvoří <code>server.jar</code>, který lze stáhnout a rovnou spustit.

<h3>Spuštění a nastavení klienta</h3>
<hr>
Klient je Android aplikace, pro jeho zkompilování je tedy potřeba více práce. Nejprve si musíme
nainstalovat <b>Android Studio</b> <a href="https://developer.android.com/studio">(např. zde)</a>.
Provedeme instalaci včetně instalace všech nutných a potřebných SDK a přímo v Android Studiu otevřeme
složku <code>rest-client</code>. Mělo by dojít k automatickému sestavení přes Gradle, s tím, že si nejspíše
hned, či později (po otevření nějakého zdrojáku) bude stěžovat na dvě věci:
<ul>
 <li><b>chybějící SDK location</b> - opravit to lze vytvořením nového souboru <b>local.properties</b> ve složce rest-client
s následujícím obsahem <code>sdk.dir=CESTA_K_ANDROID_SDK</code> (<i>Tento soubor ale, zdá se, Android Studio vytváří automaticky)</i></li>
 <li><b>chybějící model.jar knihovna</b> - opravit to lze vytvořením balíčku <code>cd app-model; mvn package</code>
a zkopírováním vzniklého <b>app-model-1.0.0.jar</b> do složky <b>rest-client/app/libs/model.jar</b> (cestu je nutno vytvořit).
Následně je třeba znovu provést synchronizaci Gradle projektu.</li>
</ul>

Pak by už nic nemělo bránit úspěšnému spuštění projektu na připojeném Android zařízení / emulátoru.
<i>(Obdobně jako v IntelliJ, kliknutím na zelenou šipku "Run" nahoře v IDE)</i>.

Pro usnadnění práce se v Gitlab CI vždy vytvoří
<code>client-apk/debug/app-debug.apk</code>, které lze rovnou nainstalovat na Android zařízení <i>(pozor, je třeba mít zapnutou
instalaci z neznámých zdrojů a přeskočit varování Google Play Protect o neověřené aplikaci)</i>.

Server, ke kterému se aplikace připojuje, běží na adrese http://dev.justtalk.cz:8080/. V případě, že chcete tuto adresu
změnit, dá se tak učinit ve třídě <code>cz.wrzecond.restsys.service.NetworkService</code>, proměnná <code>SERVER_URL</code>.

<h3>Ovládání klienta</h3>
<hr>
Po otevření aplikace se ukáže obrazovka s <b>Jídelníčkem</b> - všemi jídly a nápoji, které jsou v restauraci dostupné.

Po kliknutí na ikonku <b>"i"</b> u seznamu alergenů se ukáže detailní seznam alergenů.

Do aplikace se dá následně přihlásit - kulatým tlačítkem se zámkem vpravo dole. Funkční kombinace username/password jsou
<b>wrzecond</b> / <b>password</b> (admin) a <b>danecek</b> / <b>danecek</b> (číšník).

V přihlášeném rozhraní lze vidět <b>Stoly</b> s možností kliknutím na stůl vytvořit / spravovat objednávku,
která je na něm právě aktivní, případně kulatými tlačítky dole vytvořit nový stůl nebo vytvořit objednávku na všech stolech
venku / uvnitř restaurace. Při podržení prstu na stolu se ukáže rozhraní pro úpravu typu stolu a jeho možné smazání.

Další sekcí přihlášeného rozhraní jsou <b>Jídla</b> - admin má právo tyto jídla přidávat, upravovat cenu (tlačítka dole) apod...
číšník pouze vidí přehled.

Admin dále v menu vidí položku <b>Lidé</b> - slouží k upravování uživatelů a přidání nového uživatele.

Poslední ikonka zámku vpravo aktuálního uživatele <b>odhlásí</b>.
<hr>
&copy; Ondřej Wrzecionko 2020-2021