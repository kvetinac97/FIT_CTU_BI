<?php

namespace kvetinac97;

use Goutte\Client;
use Symfony\Component\DomCrawler\Crawler;

class Main {

    public function run() {
        $this->runDumb();
        $this->runSmart();
    }
    private function runDumb() {
        $goutte = new Client();
        $crawler = $goutte->request('GET', 'https://www.kvetinac97.cz');
        var_dump($crawler->filter('article strong')->each(fn($node) => $node->text()));
    }

    // Main tutorial method

    private const BASE_URL = "https://www.zive.cz";
    private const DELAY = 3_000_000; // not to DDOS
    private const OUTPUT_FILE = "output2.html";

    private function runSmart() {
        // Init client
        $client = new Client();
        file_put_contents(self::OUTPUT_FILE, '<!DOCTYPE html>
            <html lang="cs">
            <head>
                <meta charset="utf-8" />
                <title>Articles</title>
            </head>
            <body>
        ');

        // BFS
        $visited = [];
        $toVisit = [self::BASE_URL]; // queue

        while (count($toVisit) > 0) {
            $site = array_shift($toVisit); // return first
            $crawler = $client->request('GET', $site);

            // Scrapping
            $title = $crawler->filter('div.ar-detail h1[itemprop=name]')->text("");
            $content = $crawler->filter('div.ar-content p:not(.ar-tags)')
                ->each(fn($node) => $node->text(""));

            if ($title !== "" && count($content) > 0) {
                $html = "<h1>$title</h1>" . PHP_EOL;
                $html .= join(PHP_EOL, array_map(fn($paragraph) => "<p>$paragraph</p>", $content));
                file_put_contents(self::OUTPUT_FILE, $html, FILE_APPEND);
            }

            $visited[] = $site;

            // Crawling
            $crawler->filter('h2 > a[href^="/clanky"]')
                ->each(function (Crawler $node) use (&$toVisit, &$visited) {
                    if (!in_array($url = self::BASE_URL . $node->attr('href'), $visited))
                        $toVisit[] = $url;
                });

            // Wait
            usleep(self::DELAY);
            echo "Other " . count($toVisit) . PHP_EOL;
        }

        file_put_contents(self::OUTPUT_FILE, '</body></html>', FILE_APPEND);
    }

}