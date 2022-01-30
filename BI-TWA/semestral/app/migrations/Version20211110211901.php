<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

final class Version20211110211901 extends AbstractMigration
{
    public function getDescription(): string
    {
        return 'Version 1 - Initial relations';
    }

    public function up(Schema $schema): void
    {
        $this->addSql('CREATE TABLE building (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name VARCHAR(128) NOT NULL)');
        $this->addSql('CREATE UNIQUE INDEX uix_building_name ON building (name)');
        $this->addSql('CREATE TABLE "group" (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, parent_group_id INTEGER DEFAULT NULL, name VARCHAR(128) NOT NULL)');
        $this->addSql('CREATE UNIQUE INDEX uix_group_name ON "group" (name)');
        $this->addSql('CREATE UNIQUE INDEX uix_parent_group_id ON "group" (parent_group_id)');
        $this->addSql('CREATE TABLE member (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, user_id INTEGER NOT NULL, group_id INTEGER NOT NULL, manager BOOLEAN NOT NULL)');
        $this->addSql('CREATE INDEX ix_member_user_id ON member (user_id)');
        $this->addSql('CREATE INDEX ix_member_group_id ON member (group_id)');
        $this->addSql('CREATE TABLE reservation (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, user_id INTEGER NOT NULL, room_id INTEGER NOT NULL, group_id INTEGER DEFAULT NULL, approved BOOLEAN NOT NULL, cancelled BOOLEAN NOT NULL, start DATETIME NOT NULL, until DATETIME NOT NULL)');
        $this->addSql('CREATE INDEX ix_reservation_user_id ON reservation (user_id)');
        $this->addSql('CREATE INDEX ix_reservation_room_id ON reservation (room_id)');
        $this->addSql('CREATE INDEX ix_reservation_group_id ON reservation (group_id)');
        $this->addSql('CREATE TABLE room (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, group_id INTEGER NOT NULL, building_id INTEGER NOT NULL, name VARCHAR(128) NOT NULL, public BOOLEAN NOT NULL)');
        $this->addSql('CREATE UNIQUE INDEX uix_room_name ON room (name)');
        $this->addSql('CREATE INDEX ix_room_group_id ON room (group_id)');
        $this->addSql('CREATE INDEX ix_room_building_id ON room (building_id)');
        $this->addSql('CREATE TABLE room_user (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, room_id INTEGER NOT NULL, user_id INTEGER NOT NULL, manager BOOLEAN NOT NULL)');
        $this->addSql('CREATE INDEX ix_room_user_room_id ON room_user (room_id)');
        $this->addSql('CREATE INDEX ix_room_user_user_id ON room_user (user_id)');
        $this->addSql('CREATE TABLE "user" (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, email VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, name VARCHAR(128) NOT NULL, tel VARCHAR(16) DEFAULT NULL, admin BOOLEAN NOT NULL)');
        $this->addSql('CREATE UNIQUE INDEX uix_user_email ON "user" (email)');
    }

    public function down(Schema $schema): void
    {
        $this->addSql('DROP TABLE building');
        $this->addSql('DROP TABLE "group"');
        $this->addSql('DROP TABLE member');
        $this->addSql('DROP TABLE reservation');
        $this->addSql('DROP TABLE room');
        $this->addSql('DROP TABLE room_user');
        $this->addSql('DROP TABLE "user"');
    }
}
