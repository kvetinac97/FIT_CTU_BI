# Úkol 8: REST API

Vaším úkolem je vytvořit jednoduché REST API pro správu knih. Pomocí API lze prohlížet existující knihy, vytvářet nové a upravovat a mazat existující.

Zvolte si persistentní úložitě, které chcete použít (např. Sqlite, nebo soubor) a do něj ukládejte informace o knihách. Každá kniha sestává z následujících položek:

- `id`
- `name`
- `author`
- `publisher`
- `isbn`
- `pages`

Prohlížení existujících záznamů může dělat kdokoliv. Operace, které záznamy upravují mohou dělat pouze autorizovaní uživatelé, kdy ověření probíhá pomocí HTTP Basic Auth. Pro účely tohoto úkolu stačí "zahardcodovat" uživatele `admin` s heslem `pas$word`.

Máte připravenou kostru aplikace v `public/index.php`.
Není potřeba implementovat vše v jednom scriptu, máte nastavený namespace `Books` do složky `src`, vytvořte si další třídy, které budete potřebovat, aby byl kód přehledný.

## Spuštění

Používáte-li [předpřipravený docker](https://gitlab.fit.cvut.cz/BI-PHP/bi-php-docker), aktualizujte si jej prosím na nejnovější verzi (stačí git pull a restart kontejnerů). V něm je připravena example aplikace. Abyste na její místo dali HW-08, upravte buďto cesty v konfiguraci (viz [Návod na konci README](https://gitlab.fit.cvut.cz/BI-PHP/bi-php-docker#kde-se-mohu-pod%C3%ADvat-na-uk%C3%A1zkovou-aplikaci)), nebo zdrojáky HW08 vložte přímo do `/src/` dockeru

Pokud nepoužíváte Docker, můžete aplikaci spustit pomocí PHP build-in serveru: `$ php -S localhost:8080 -t public public/index.php`.

---

## Seznam uložených knih (1 bod)

**Request**

```
> GET /books

```

**Success Response**

Vrátí seznam uložených knih. V případě, že žádné knihy uložené nejsou, vrátí prázdný seznam. Seznam knih obsahuje pouze `id`, `name` a `author`.

```
< 200 OK

[{
    "id": 1,
    "name": "The Little Prince",
    "author": "Antoine de Saint-Exupéry"  
}, {
    ...
}]
```

---

## Detail knihy (1 bod)

**Request**

```
> GET /books/:id

```

**Success Response**

Vrátí detail knihy, který obsahuje všechna pole.

```
< 200 OK

{
    "id": 1,
    "name": "The Little Prince",
    "author": "Antoine de Saint-Exupéry",
    "publisher": "Mariner Books",
    "isbn": "978-0156012195",
    "pages": 96
}
```

**Error Response**

V případě neexistujícího `id` vártí HTTP chybu 404.

```
< 404 Not Found

```

---

## Vytvoření nové knihy 🔐 (2 bodu)

**Request**

Novou knihu může vytvořit pouze autorizovaný uživatel. To je ověřeno pomocí HTTP Basic Auth. Je potřeba poslat všechny informace o knize.

```
> POST /books

Authorization: Basic <token>
Content-Type: application/json


{
    "name": "The Little Prince",
    "author": "Antoine de Saint-Exupéry",
    "publisher": "Mariner Books",
    "isbn": "978-0156012195",
    "pages": 96
}
```

**Success Response**

Server automaticky vygeneruje `id` nové knihy a vrátí hlavičku `Location`, která obsahuje URL nové knihy.

```
< 201 Created

Location: /books/:id
```

**Unauthorized Error Response**

Pokud uživatel nepošle správný token nebo ho nepošle vůbec, vrátí server HTTP chybu 401.

```
< 401 Unauthorized

```

**Bad Request Error Response**

Pokud request neobsahuje všechny informace o knize, vrátí server HTTP chybu 400. Pokud chcete, můžete v odpovědi vrátit i informace o chybějících datech (ve formátu JSON).

```
< 400 Bad Request

```

---

## Aktualizace existující knihy 🔐 (2 bodu)

**Request**

Aktualizovat existující knihu může opět pouze autorizovaný uživatel. Pošle všechny informace znovu a existující záznam je jimi zcela nahrazen, `id` zůstává stejné.

```
> PUT /books/:id

Authorization: Basic <token>
Content-Type: application/json


{
    "name": "The Little Prince",
    "author": "Antoine de Saint-Exupéry",
    "publisher": "Mariner Books",
    "isbn": "978-0156012195",
    "pages": 96
}
```

**Success Response**

V případě úspěchu server nic nevrací a odpoví HTTP statusem 204.

```
< 204 No Content

```

**Unauthorized Error Response**

Pokud uživatel nepošle správný token nebo ho nepošle vůbec, vrátí server HTTP chybu 401.

```
< 401 Unauthorized

```

**Not Found Error Response**

Pokud je uživatel správně autorizovaný, ale snaží se aktualizovat neexistující záznam, vrátí server HTTP chybu 404.

```
< 404 Not Found

```

**Bad Request Error Response**

Stejně jako v případě vytváření nové knihy, je i zde potřeba ověřit, že jsou odeslaná všechna data.

```
< 400 Bad Request

```

---

## Smazání knihy 🔐 (1 bod)

**Request**

Knihu může smazat pouze autorizovaný uživatel.

```
> DELETE /books/:id

Authorization: Basic <token>

```

**Success Response**

V případě úspěchu server nevrací nic, odpoví HTTP statusem 204.

```
< 204 No Content

```

**Unauthorized Error Response**

Pokud uživatel nepošle správný token nebo ho nepošle vůbec, vrátí server HTTP chybu 401.

```
< 401 Unauthorized

```

**Not Found Error Response**

Pokud je uživatel správně autorizovaný, ale snaží se smazat neexistující záznam, vrátí server HTTP chybu 404.

```
< 404 Not Found

```
