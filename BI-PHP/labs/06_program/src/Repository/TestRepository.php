<?php

namespace Repository;

use Entity\Entity;
use Entity\TestEntity;
use PDO;

class TestRepository extends AbstractRepository {

    public function findAll(): array {
        $query = "SELECT * FROM `test` ORDER BY `id`";

        $statement = self::$connection->prepare($query);
        $statement->execute();
        $rows = $statement->fetchAll(PDO::FETCH_ASSOC);

        return array_map( function($row) {
            return new TestEntity($row);
        }, $rows );
    }

    public function getByID(int $id) : ?Entity {
        return null; // todo
    }

    public function persist(Entity $entity): bool {
        // todo
        return false;
    }


}