<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

final class Version20211110123026 extends AbstractMigration {

    public function getDescription() : string {
        return 'Version 3 - Add relations';
    }

    public function up (Schema $schema) : void {
        $this->addSql('CREATE TABLE employee_role (employee_id INTEGER NOT NULL, role_id INTEGER NOT NULL, PRIMARY KEY(employee_id, role_id))');
        $this->addSql('CREATE INDEX ix_employee_role_employee_id ON employee_role (employee_id)');
        $this->addSql('CREATE INDEX ix_employee_role_role_id ON employee_role (role_id)');
        $this->addSql('DROP INDEX uix_account_name');
        $this->addSql('CREATE TEMPORARY TABLE __temp__account AS SELECT id, employee_id, expiration, name FROM account');
        $this->addSql('DROP TABLE account');
        $this->addSql('CREATE TABLE account (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, employee_id INTEGER NOT NULL, expiration DATETIME NOT NULL, name VARCHAR(64) NOT NULL COLLATE BINARY, CONSTRAINT fk_employee_id FOREIGN KEY (employee_id) REFERENCES employee (id) NOT DEFERRABLE INITIALLY IMMEDIATE)');
        $this->addSql('INSERT INTO account (id, employee_id, expiration, name) SELECT id, employee_id, expiration, name FROM __temp__account');
        $this->addSql('DROP TABLE __temp__account');
        $this->addSql('CREATE UNIQUE INDEX uix_account_name ON account (name)');
        $this->addSql('CREATE INDEX ix_account_employee_id ON account (employee_id)');
    }

    public function down (Schema $schema) : void {
        $this->addSql('DROP TABLE employee_role');
        $this->addSql('DROP INDEX uix_account_name');
        $this->addSql('DROP INDEX ix_account_employee_id');
        $this->addSql('CREATE TEMPORARY TABLE __temp__account AS SELECT id, employee_id, name, expiration FROM account');
        $this->addSql('DROP TABLE account');
        $this->addSql('CREATE TABLE account (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, employee_id INTEGER NOT NULL, name VARCHAR(64) NOT NULL, expiration DATETIME NOT NULL)');
        $this->addSql('INSERT INTO account (id, employee_id, name, expiration) SELECT id, employee_id, name, expiration FROM __temp__account');
        $this->addSql('DROP TABLE __temp__account');
        $this->addSql('CREATE UNIQUE INDEX uix_account_name ON account (name)');
    }

}
