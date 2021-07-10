<?php

namespace App;

use GuzzleHttp\Client;
use GuzzleHttp\Exception\GuzzleException;
use GuzzleHttp\Psr7\Request;
use Psr\Http\Client\ClientExceptionInterface;

class RestClient {

    public static function callRequest () {
        $guzzle = new Client();
        $headers = [
            'Accept' => 'application/json'
        ];
        $request = new Request('GET', 'https://ackeeedu.000webhostapp.com/api.php/records/posts/1', $headers);
        try {
            $response = $guzzle->sendRequest($request);
            var_dump($response->getBody()->getContents());

            $resp1 = $guzzle->get('https://jsonplaceholder.typicode.com/todos/1');
            var_dump($resp1->getBody()->getContents());
        }
        catch ( GuzzleException | ClientExceptionInterface $exception ) {
            var_dump($exception->getCode());
        }
    }

}