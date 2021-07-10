<?php

namespace Repository;

use Entity\Entity;
use \PDO;

abstract class AbstractRepository {

    private const CONNECTION_STRING = "sqlite:db.sqlite";

    /** @var PDO $connection */
    protected static $connection = null;
    private static $connectionCount = 0;

    public function __construct() {
        if ( self::$connection == null )
            self::$connection = new PDO(self::CONNECTION_STRING);
        self::$connectionCount++;
    }
    public function __destruct() {
        if ( --self::$connectionCount == 0 )
            self::$connection = null;
    }

    /** @return Entity[] */
    abstract public function findAll() : array;
    abstract public function getByID(int $id) : ?Entity;
    abstract public function persist(Entity $entity) : bool;

}