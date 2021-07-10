<?php

namespace Books;

use Books\Model\Book;

class BookUtility {

    private const BOOK_FILE_PATH = ( __DIR__ . "/books.data" );

    /** @var Book[] $books */
    private array $books;

    public function __construct () {
        if ( !is_file(self::BOOK_FILE_PATH) )
            file_put_contents(self::BOOK_FILE_PATH, serialize([]));
        $this->books = unserialize(file_get_contents(self::BOOK_FILE_PATH));
    }
    public function __destruct() {
        file_put_contents(self::BOOK_FILE_PATH, serialize($this->books));
    }

    public function addBook ( Book $book ) {
        $this->books[$book->id] = $book;
    }
    public function removeBook ( int $id ) : bool {
        $b = isset($this->books[$id]);
        unset($this->books[$id]);
        return $b;
    }

    /** @return Book[] */
    public function getBooks () : array {
        return array_values($this->books);
    }
    public function getBookById ( int $id ) : ?Book {
        return isset($this->books[$id]) ? $this->books[$id] : null;
    }

}