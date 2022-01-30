<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

final class Version20211102212952 extends AbstractMigration {

    public function getDescription() : string {
        return 'Version 2 - Add name column to account';
    }

    public function up (Schema $schema) : void {
        $this->addSql('CREATE TEMPORARY TABLE __temp__account AS SELECT id, employee_id, expiration FROM account');
        $this->addSql('DROP TABLE account');
        $this->addSql('CREATE TABLE account (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, employee_id INTEGER NOT NULL, expiration DATETIME NOT NULL, name VARCHAR(64) NOT NULL)');
        $this->addSql('INSERT INTO account (id, name, employee_id, expiration) SELECT id, cast(id AS VARCHAR(64)), employee_id, expiration FROM __temp__account');
        $this->addSql('DROP TABLE __temp__account');
        $this->addSql('CREATE UNIQUE INDEX uix_account_name ON account (name)');
    }

    public function down (Schema $schema) : void {
        $this->addSql('DROP INDEX uix_account_name');
        $this->addSql('CREATE TEMPORARY TABLE __temp__account AS SELECT id, employee_id, expiration FROM account');
        $this->addSql('DROP TABLE account');
        $this->addSql('CREATE TABLE account (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, employee_id INTEGER NOT NULL, expiration DATETIME NOT NULL)');
        $this->addSql('INSERT INTO account (id, employee_id, expiration) SELECT id, employee_id, expiration FROM __temp__account');
        $this->addSql('DROP TABLE __temp__account');
    }

}
