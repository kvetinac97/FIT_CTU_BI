<?php

use Books\Middleware\JsonBodyParserMiddleware;
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;
use Psr\Http\Server\RequestHandlerInterface as RequestHandler;
use Books\BookUtility;
use Books\Model\Book;
use Slim\Factory\AppFactory;

require __DIR__ . '/../vendor/autoload.php';

$app = AppFactory::create();
$bookUtility = new BookUtility();

$app->addRoutingMiddleware();
$app->addErrorMiddleware(true, true, true);
$app->add(new JsonBodyParserMiddleware());

// Authentication
$securityMiddleware = function (Request $request, RequestHandler $handler) {
    if ( !$request->hasHeader('Authorization') || !isset($_SERVER['PHP_AUTH_USER'])
        || $_SERVER['PHP_AUTH_USER'] != 'admin' || !isset($_SERVER['PHP_AUTH_PW']) ||
        $_SERVER['PHP_AUTH_PW'] != 'pas$word' ) {
        $response = new \Slim\Psr7\Response();
        return $response->withStatus(401);
    }

    $response = $handler->handle($request);
    return $response;
};

// Get list of all books
$app->get('/books', function (Request $request, Response $response, $args) use($bookUtility) {
    $payload = json_encode($bookUtility->getBooks());
    $response->getBody()->write($payload);
    return $response->withHeader('Content-Type', 'application/json');
});

// Get detail of one book with specific id
$app->get('/books/{id}', function (Request $request, Response $response, $args) use ($bookUtility) {
    $book = $bookUtility->getBookById(intval($args['id']));
    if ($book === null)
        return $response->withStatus(404);

    $payload = json_encode($book);
    $response->getBody()->write($payload);
    return $response->withHeader('Content-Type', 'application/json');
});

// (authorized) Create one specific book
$app->post('/books', function (Request $request, Response $response, $args) use ($bookUtility) {
    // Create book and try to set data
    $book = new Book();
    $body = $request->getParsedBody();
    $body['id'] = count($bookUtility->getBooks()) + 1;

    if ($book->setData($body) !== true)
        return $response->withStatus(400);

    // Persist and return
    $bookUtility->addBook($book);
    return $response
        ->withAddedHeader('Location', "/books/{$book->id}")
        ->withStatus(201);
})->add($securityMiddleware);

// (authorized) Update one specific book
$app->put('/books', function (Request $request, Response $response, $args) use ($bookUtility) {
    // Create book and try to set data
    $book = new Book();
    if ($book->setData($request->getParsedBody()) !== true)
        return $response->withStatus(400);

    // Try to find whether the book exists
    if ($bookUtility->getBookById($book->id) === null)
        return $response->withStatus(404);

    // Update, persist and return
    $bookUtility->addBook($book);
    return $response->withStatus(204);
})->add($securityMiddleware);

// (authorized) Delete one specific book
$app->delete('/books/{id}', function (Request $request, Response $response, $args) use ($bookUtility) {
    return $response->withStatus($bookUtility->removeBook($args['id']) ? 204 : 404);
})->add($securityMiddleware);

$app->run();
