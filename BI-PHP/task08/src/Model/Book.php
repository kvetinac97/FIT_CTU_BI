<?php

namespace Books\Model;

class Book {

    // Book info
    public int $id;
    public string $name;
    public string $isbn;
    public int $pages;

    // People info
    public string $author;
    public string $publisher;

    public function setData (?array $data = null) : bool {
        if ($data === null)
            return false;

        foreach ( ['id', 'name', 'isbn', 'pages', 'author', 'publisher'] as $property ) {
            if (!isset($data[$property]))
                return false;
            $this->{$property} = $data[$property];
        }
        return true;
    }

}