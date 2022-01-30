<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * First version of database
 */
final class Version20211102142332 extends AbstractMigration {

    public function getDescription () : string {
        return 'Version 1 - creates database (no data)';
    }

    public function up (Schema $schema) : void {
        $this->addSql('CREATE TABLE account (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, employee_id INTEGER NOT NULL, expiration DATETIME NOT NULL)');
        $this->addSql('CREATE TABLE employee (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name VARCHAR(64) NOT NULL, username VARCHAR(8) NOT NULL, phone VARCHAR(16) NOT NULL, email VARCHAR(255) NOT NULL, website VARCHAR(255) NOT NULL, description CLOB NOT NULL)');
        $this->addSql('CREATE UNIQUE INDEX ix_employee_username ON employee (username)');
        $this->addSql('CREATE TABLE role (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name VARCHAR(64) NOT NULL)');
    }

    public function down (Schema $schema) : void {
        $this->addSql('DROP TABLE account');
        $this->addSql('DROP TABLE employee');
        $this->addSql('DROP TABLE role');
    }

}
