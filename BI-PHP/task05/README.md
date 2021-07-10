# Úkol 5: Faktury

Cílem je vytvořit program, který bude generovat faktury jako PDF dokumenty. Máte připravenou kostru aplikace, kde najdete třídy reprezentující věci vyskytující se na faktuře a třídu `Builder`, která slouží k vytvoření faktury. Do projektu doinstalujte pomocí composeru knihovnu [DomPDF](https://github.com/dompdf/dompdf). Pokud z nějakého důvodu chcete použít knihovnu jinou, po domluvě s cvičícím, kterému úkoly odevzdáváte, je to také možné.

Doplňte chybějící části tříd `Invoice\Item`, `Builder` a `Invoice`.

Dále implementujte třídu `Renderer`, tak aby vytvořila PDF fakturu. K testování můžete použít skript `run.php`:

```
$ php run.php > invoice.pdf
```

Soubor `template.pdf` ukazuje, jak by výsledná faktura měla vypadat. Vaším úkolem je použít knihovnu DomPDF tak, abyste se co nejvíc přiblížili ukázce. Bude potřeba se podívat do dokumentace knihovny, abyste zjistili, jak ji použít. Není potřeba řešit podporu českých znaků.

Bodové hodnocení jednotlivých částí je následující:

- Nainstalování knihovny FPDF a vyprodukování PDF souboru, který jde otevřít **[1 bod]**
- Faktura obsahuje všechna požadovaná data **[2.5 bodu]**
- Vzhled faktury:
	- Dodavatel a odběratel s příslušným formátováním (telefonní číslo a email se zobrazí pouze pokud jsou nastavené) **[1,5 bodu]**
	- Tabulka položek (správné zarámování, tučná hlavička a řádek celkem, zarovnání) **[1,5 bodu]**
	- Formátování cen na dvě desetinná místa, s desetinnou čárkou a tisíce oddělené mezerou **[0,5 bodu]**
