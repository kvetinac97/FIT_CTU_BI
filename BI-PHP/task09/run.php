<?php

use Goutte\Client;
use Symfony\Component\DomCrawler\Crawler;

require __DIR__.'/vendor/autoload.php';

function text(Crawler $crawler, string $selector)
{
    $new = $crawler->filter($selector);
    if (count($new)) {
        return trim($new->text());
    }

    return null;
}
function price_val($x) : int {
    return intval(str_replace([' ', '&nbsp;'], '', htmlentities($x['price'])));
}

const ALZA_HOMEPAGE = "https://www.alza.cz";
const CZC_HOMEPAGE = "https://www.czc.cz";

/**
 * @param string $query - query string e.g. 'Beats Studio 3'
 * @return array
 */
function scrape(string $query)
{
    // Init Alza
    $client = new Client();
    $crawler = $client->request('GET', ALZA_HOMEPAGE . '/search.htm?exps=' . urlencode($query));

    $alzaProducts = $crawler->filter('div.browsingitem a.name.browsinglink')->each(function (Crawler $item) use($client) {
        // Load product page
        $crawler = $client->request('GET', $link = ALZA_HOMEPAGE . $item->attr("href"));
        return [
            'name' => text($crawler, 'h1[itemprop=name]'),
            'price' => text($crawler, 'div#pricec span.price_withVat') ?? // akční cena
                       text($crawler, 'table#prices tr.pricenormal td.c2 > span'), // klasická cena
            'link' => $link,
            'eshop' => ALZA_HOMEPAGE,
            'description' => text($crawler, 'div.nameextc > span')
        ];
    });

    // Init CZC
    $crawler = $client->request('GET', CZC_HOMEPAGE . '/' . str_replace('+', '%20', urlencode($query)) . '/hledat');

    $czcProducts = $crawler->filter('a.tile-link')->each(function (Crawler $item) use($client) {
        // Load product page
        $crawler = $client->request('GET', $link = CZC_HOMEPAGE . $item->attr("href"));
        return [
            'name' => text($crawler, 'div.pd-wrap h1'),
            'price' => text($crawler, 'div.total-price span.price-vatin'),
            'link' => $link,
            'eshop' => CZC_HOMEPAGE,
            'description' => text($crawler, 'p.pd-shortdesc')
        ];
    });

    $result = array_merge($alzaProducts, $czcProducts);
    usort($result, fn($l, $r) => (price_val($l) - price_val($r)) - (price_val($r) - price_val($l)) );
    return $result;
}

/**
 * @param string $query   - query string e.g. 'Beats Studio 3'
 * @param array  $results - input product collection
 * @return array
 */
function filter(string $query, array $results)
{
    return array_filter($results, fn($product) => str_contains(
        mb_strtolower(str_replace(' ', '', $product['name'])),
        mb_strtolower(str_replace(' ', '', $query))
        ) || str_contains(
            mb_strtolower(str_replace(' ', '', $product['description'])),
            mb_strtolower(str_replace(' ', '', $query))
        )
    );
}

/*
 * input array $results show contain following keys
 * - 'name'
 * - 'price'
 * - 'link' - link to product detail page
 * - 'eshop' - eshop identifier e.g. 'alza'
 * - 'description'
 */
function printResults(array $results, bool $includeDescription = false)
{
    $width = [
        'name' => 0,
        'price' => 0,
        'link' => 0,
        'eshop' => 0,
        'description' => 0,
    ];
    foreach ($results as $result) {
        foreach ($result as $key => $value) {
            $width[$key] = max(mb_strlen($value), $width[$key]);
        }
    }
    echo '+'.str_repeat('-', 2 + $width['name']);
    echo '+'.str_repeat('-', 2 + $width['price']);
    echo '+'.str_repeat('-', 2 + $width['link']);
    echo '+'.str_repeat('-', 2 + $width['eshop'])."+\n";
    foreach ($results as $result) {

        echo '| '.$result['name'].str_repeat(' ', $width['name'] - mb_strlen($result['name'])).' ';
        echo '| '.$result['price'].str_repeat(' ', $width['price'] - mb_strlen($result['price'])).' ';
        echo '| '.$result['link'].str_repeat(' ', $width['link'] - mb_strlen($result['link'])).' ';
        echo '| '.$result['eshop'].str_repeat(' ', $width['eshop'] - mb_strlen($result['eshop'])).' ';
        echo "|\n";
        echo '+'.str_repeat('-', 2 + $width['name']);
        echo '+'.str_repeat('-', 2 + $width['price']);
        echo '+'.str_repeat('-', 2 + $width['link']);
        echo '+'.str_repeat('-', 2 + $width['eshop'])."+\n";
        if ($includeDescription) {
            echo '| '.$result['description'].str_repeat(' ',
                    max(0, 7 + $width['name'] + $width['price'] + $width['link'] - mb_strlen($result['description'])));
            echo "|\n";
            echo str_repeat('-', 10 + $width['name'] + $width['price'] + $width['link'])."\n";
        }
    }
}

// MAIN
if (count($argv) !== 2) {
    echo "Usage: php run.php <query>\n";
    exit(1);
}

$query = $argv[1];
$results = scrape($query);
$results = filter($query, $results);
printResults($results);
