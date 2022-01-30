<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

final class Version20211214171729 extends AbstractMigration {

    public function getDescription() : string {
        return 'Version 6 - non-expiring accounts (expiration NULL)';
    }

    public function up (Schema $schema) : void {
        $this->addSql('DROP INDEX IDX_7D3656A48C03F15C');
        $this->addSql('DROP INDEX UNIQ_7D3656A45E237E06');
        $this->addSql('CREATE TEMPORARY TABLE __temp__account AS SELECT id, employee_id, expiration, name, password, roles FROM account');
        $this->addSql('DROP TABLE account');
        $this->addSql('CREATE TABLE account (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, employee_id INTEGER NOT NULL, expiration DATETIME DEFAULT NULL, name VARCHAR(64) NOT NULL COLLATE BINARY, password VARCHAR(255) NOT NULL COLLATE BINARY, roles CLOB NOT NULL COLLATE BINARY --(DC2Type:json)
        , CONSTRAINT FK_7D3656A48C03F15C FOREIGN KEY (employee_id) REFERENCES employee (id) NOT DEFERRABLE INITIALLY IMMEDIATE)');
        $this->addSql('INSERT INTO account (id, employee_id, expiration, name, password, roles) SELECT id, employee_id, expiration, name, password, roles FROM __temp__account');
        $this->addSql('DROP TABLE __temp__account');
        $this->addSql('CREATE INDEX IDX_7D3656A48C03F15C ON account (employee_id)');
        $this->addSql('CREATE UNIQUE INDEX UNIQ_7D3656A45E237E06 ON account (name)');
        $this->addSql('DROP INDEX IDX_E2B0C02DD60322AC');
        $this->addSql('DROP INDEX IDX_E2B0C02D8C03F15C');
        $this->addSql('CREATE TEMPORARY TABLE __temp__employee_role AS SELECT employee_id, role_id FROM employee_role');
        $this->addSql('DROP TABLE employee_role');
        $this->addSql('CREATE TABLE employee_role (employee_id INTEGER NOT NULL, role_id INTEGER NOT NULL, PRIMARY KEY(employee_id, role_id), CONSTRAINT FK_E2B0C02D8C03F15C FOREIGN KEY (employee_id) REFERENCES employee (id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE, CONSTRAINT FK_E2B0C02DD60322AC FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE)');
        $this->addSql('INSERT INTO employee_role (employee_id, role_id) SELECT employee_id, role_id FROM __temp__employee_role');
        $this->addSql('DROP TABLE __temp__employee_role');
        $this->addSql('CREATE INDEX IDX_E2B0C02DD60322AC ON employee_role (role_id)');
        $this->addSql('CREATE INDEX IDX_E2B0C02D8C03F15C ON employee_role (employee_id)');
    }

    public function down (Schema $schema) : void {
        $this->addSql('DROP INDEX UNIQ_7D3656A45E237E06');
        $this->addSql('DROP INDEX IDX_7D3656A48C03F15C');
        $this->addSql('CREATE TEMPORARY TABLE __temp__account AS SELECT id, employee_id, name, password, roles, expiration FROM account');
        $this->addSql('DROP TABLE account');
        $this->addSql('CREATE TABLE account (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, employee_id INTEGER NOT NULL, name VARCHAR(64) NOT NULL, password VARCHAR(255) NOT NULL, roles CLOB NOT NULL --(DC2Type:json)
        , expiration DATETIME NOT NULL)');
        $this->addSql('INSERT INTO account (id, employee_id, name, password, roles, expiration) SELECT id, employee_id, name, password, roles, expiration FROM __temp__account');
        $this->addSql('DROP TABLE __temp__account');
        $this->addSql('CREATE UNIQUE INDEX UNIQ_7D3656A45E237E06 ON account (name)');
        $this->addSql('CREATE INDEX IDX_7D3656A48C03F15C ON account (employee_id)');
        $this->addSql('DROP INDEX IDX_E2B0C02D8C03F15C');
        $this->addSql('DROP INDEX IDX_E2B0C02DD60322AC');
        $this->addSql('CREATE TEMPORARY TABLE __temp__employee_role AS SELECT employee_id, role_id FROM employee_role');
        $this->addSql('DROP TABLE employee_role');
        $this->addSql('CREATE TABLE employee_role (employee_id INTEGER NOT NULL, role_id INTEGER NOT NULL, PRIMARY KEY(employee_id, role_id))');
        $this->addSql('INSERT INTO employee_role (employee_id, role_id) SELECT employee_id, role_id FROM __temp__employee_role');
        $this->addSql('DROP TABLE __temp__employee_role');
        $this->addSql('CREATE INDEX IDX_E2B0C02D8C03F15C ON employee_role (employee_id)');
        $this->addSql('CREATE INDEX IDX_E2B0C02DD60322AC ON employee_role (role_id)');
    }

}
