<?php

namespace App;

use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface;
use Psr\Http\Message\ServerRequestInterface as Request;
use Psr\Http\Server\RequestHandlerInterface;
use Slim\Factory\AppFactory;

class RestServer {

    public static function startServer () {
        // Create Slim app
        $app = AppFactory::create();

        // Security
        $securityMiddleware = function ( ServerRequestInterface $request, RequestHandlerInterface $handler ) {
            if ( !$request->hasHeader('Authorization') ||
                $_SERVER['PHP_AUTH_USER'] !== "marek" || $_SERVER['PHP_AUTH_PW'] !== "pass" ) {
                $response = $handler->handle($request);
                return $response->withStatus(401); // Unauthorized
            }

            $request = $request->withAttribute('customer', [
                'username' => 'Josko',
                'name' => 'Pepa Pig'
            ]);
            return $handler->handle($request);
        };

        // Define app routes
        $app->get('/', function ( Request $request, Response $response, $args ) {
            $response->getBody()->write("Hello world!");
            return $response;
        })->add($securityMiddleware); // zabezpečení
        $app->get('/{id}', function ( Request $request, Response $response, array $args ) {
            if ( $args["id"] == -1 )
                return $response->withStatus(404);

            $response->getBody()->write($args["id"]);
            return $response;
        });

        // Run app
        $app->run();
    }

}