<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20211123135232 extends AbstractMigration
{
    public function getDescription(): string
    {
        return 'Version 2 - add roles & fix reservations';
    }

    public function up(Schema $schema): void
    {
        $this->addSql('CREATE TABLE reservation_user (reservation_id INTEGER NOT NULL, user_id INTEGER NOT NULL, PRIMARY KEY(reservation_id, user_id))');
        $this->addSql('CREATE INDEX ix_reservation_user_reservation_id ON reservation_user (reservation_id)');
        $this->addSql('CREATE INDEX ix_reservation_user_user_id ON reservation_user (user_id)');
        $this->addSql('DROP INDEX uix_building_name');
        $this->addSql('CREATE TEMPORARY TABLE __temp__building AS SELECT id, name FROM building');
        $this->addSql('DROP TABLE building');
        $this->addSql('CREATE TABLE building (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name VARCHAR(128) NOT NULL COLLATE BINARY)');
        $this->addSql('INSERT INTO building (id, name) SELECT id, name FROM __temp__building');
        $this->addSql('DROP TABLE __temp__building');
        $this->addSql('CREATE UNIQUE INDEX uix_building_name ON building (name)');
        $this->addSql('DROP INDEX uix_parent_group_id');
        $this->addSql('DROP INDEX uix_group_name');
        $this->addSql('CREATE TEMPORARY TABLE __temp__group AS SELECT id, parent_group_id, name FROM "group"');
        $this->addSql('DROP TABLE "group"');
        $this->addSql('CREATE TABLE "group" (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, parent_group_id INTEGER DEFAULT NULL, name VARCHAR(128) NOT NULL COLLATE BINARY, CONSTRAINT uix_parent_group_id FOREIGN KEY (parent_group_id) REFERENCES "group" (id) NOT DEFERRABLE INITIALLY IMMEDIATE)');
        $this->addSql('INSERT INTO "group" (id, parent_group_id, name) SELECT id, parent_group_id, name FROM __temp__group');
        $this->addSql('DROP TABLE __temp__group');
        $this->addSql('CREATE UNIQUE INDEX uix_group_name ON "group" (name)');
        $this->addSql('CREATE UNIQUE INDEX uix_group_parent_group_id ON "group" (parent_group_id)');
        $this->addSql('DROP INDEX ix_member_group_id');
        $this->addSql('DROP INDEX ix_member_user_id');
        $this->addSql('CREATE TEMPORARY TABLE __temp__member AS SELECT id, user_id, group_id, manager FROM member');
        $this->addSql('DROP TABLE member');
        $this->addSql('CREATE TABLE member (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, user_id INTEGER NOT NULL, group_id INTEGER NOT NULL, manager BOOLEAN NOT NULL, CONSTRAINT ix_member_user_id FOREIGN KEY (user_id) REFERENCES "user" (id) NOT DEFERRABLE INITIALLY IMMEDIATE, CONSTRAINT ix_member_group_id FOREIGN KEY (group_id) REFERENCES "group" (id) NOT DEFERRABLE INITIALLY IMMEDIATE)');
        $this->addSql('CREATE UNIQUE INDEX uix_member_user_group ON member (user_id, group_id)');
        $this->addSql('INSERT INTO member (id, user_id, group_id, manager) SELECT id, user_id, group_id, manager FROM __temp__member');
        $this->addSql('DROP TABLE __temp__member');
        $this->addSql('CREATE INDEX ix_member_user_id ON member (user_id)');
        $this->addSql('CREATE INDEX ix_member_group_id ON member (group_id)');
        $this->addSql('DROP INDEX ix_reservation_group_id');
        $this->addSql('DROP INDEX ix_reservation_room_id');
        $this->addSql('DROP INDEX ix_reservation_user_id');
        $this->addSql('CREATE TEMPORARY TABLE __temp__reservation AS SELECT id, user_id, room_id, group_id, approved, cancelled, start, until FROM reservation');
        $this->addSql('DROP TABLE reservation');
        $this->addSql('CREATE TABLE reservation (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, author_id INTEGER NOT NULL, room_id INTEGER NOT NULL, group_id INTEGER DEFAULT NULL, approved BOOLEAN NOT NULL, cancelled BOOLEAN NOT NULL, start DATETIME NOT NULL, until DATETIME NOT NULL, CONSTRAINT ix_reservation_author_id FOREIGN KEY (author_id) REFERENCES "user" (id) NOT DEFERRABLE INITIALLY IMMEDIATE, CONSTRAINT ix_reservation_room_id FOREIGN KEY (room_id) REFERENCES room (id) NOT DEFERRABLE INITIALLY IMMEDIATE, CONSTRAINT ix_reservation_group_id FOREIGN KEY (group_id) REFERENCES "group" (id) NOT DEFERRABLE INITIALLY IMMEDIATE)');
        $this->addSql('INSERT INTO reservation (id, author_id, room_id, group_id, approved, cancelled, start, until) SELECT id, user_id, room_id, group_id, approved, cancelled, start, until FROM __temp__reservation');
        $this->addSql('DROP TABLE __temp__reservation');
        $this->addSql('CREATE INDEX ix_reservation_author_id ON reservation (author_id)');
        $this->addSql('CREATE INDEX ix_reservation_room_id ON reservation (room_id)');
        $this->addSql('CREATE INDEX ix_reservation_group_id ON reservation (group_id)');
        $this->addSql('DROP INDEX ix_room_building_id');
        $this->addSql('DROP INDEX ix_room_group_id');
        $this->addSql('DROP INDEX uix_room_name');
        $this->addSql('CREATE TEMPORARY TABLE __temp__room AS SELECT id, group_id, building_id, name, public FROM room');
        $this->addSql('DROP TABLE room');
        $this->addSql('CREATE TABLE room (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, group_id INTEGER NOT NULL, building_id INTEGER NOT NULL, name VARCHAR(128) NOT NULL COLLATE BINARY, public BOOLEAN NOT NULL, CONSTRAINT ix_room_group_id FOREIGN KEY (group_id) REFERENCES "group" (id) NOT DEFERRABLE INITIALLY IMMEDIATE, CONSTRAINT ix_room_building_id FOREIGN KEY (building_id) REFERENCES building (id) NOT DEFERRABLE INITIALLY IMMEDIATE)');
        $this->addSql('INSERT INTO room (id, group_id, building_id, name, public) SELECT id, group_id, building_id, name, public FROM __temp__room');
        $this->addSql('DROP TABLE __temp__room');
        $this->addSql('CREATE UNIQUE INDEX uix_room_name ON room (name)');
        $this->addSql('CREATE INDEX ix_room_group_id ON room (group_id)');
        $this->addSql('CREATE INDEX ix_room_building_id ON room (building_id)');
        $this->addSql('DROP INDEX ix_room_user_user_id');
        $this->addSql('DROP INDEX ix_room_user_room_id');
        $this->addSql('CREATE TEMPORARY TABLE __temp__room_user AS SELECT id, room_id, user_id, manager FROM room_user');
        $this->addSql('DROP TABLE room_user');
        $this->addSql('CREATE TABLE room_user (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, room_id INTEGER NOT NULL, user_id INTEGER NOT NULL, manager BOOLEAN NOT NULL, CONSTRAINT ix_room_user_room_id FOREIGN KEY (room_id) REFERENCES room (id) NOT DEFERRABLE INITIALLY IMMEDIATE, CONSTRAINT ix_room_user_user_id FOREIGN KEY (user_id) REFERENCES "user" (id) NOT DEFERRABLE INITIALLY IMMEDIATE)');
        $this->addSql('INSERT INTO room_user (id, room_id, user_id, manager) SELECT id, room_id, user_id, manager FROM __temp__room_user');
        $this->addSql('DROP TABLE __temp__room_user');
        $this->addSql('CREATE INDEX ix_room_user_room_id ON room_user (room_id)');
        $this->addSql('CREATE INDEX ix_room_user_user_id ON room_user (user_id)');
        $this->addSql('DROP INDEX uix_user_email');
        $this->addSql('CREATE TEMPORARY TABLE __temp__user AS SELECT id, email, password, name, tel, admin FROM user');
        $this->addSql('DROP TABLE user');
        $this->addSql('CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, email VARCHAR(255) NOT NULL COLLATE BINARY, password VARCHAR(255) NOT NULL COLLATE BINARY, name VARCHAR(128) NOT NULL COLLATE BINARY, tel VARCHAR(16) DEFAULT NULL COLLATE BINARY, admin BOOLEAN NOT NULL, roles CLOB NOT NULL --(DC2Type:json)
        )');
        $this->addSql('INSERT INTO user (id, email, password, name, tel, admin) SELECT id, email, password, name, tel, admin FROM __temp__user');
        $this->addSql('DROP TABLE __temp__user');
        $this->addSql('CREATE UNIQUE INDEX uix_user_email ON user (email)');
    }

    public function down(Schema $schema): void
    {
        $this->addSql('DROP TABLE reservation_user');
        $this->addSql('DROP INDEX uix_building_name');
        $this->addSql('CREATE TEMPORARY TABLE __temp__building AS SELECT id, name FROM building');
        $this->addSql('DROP TABLE building');
        $this->addSql('CREATE TABLE building (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name VARCHAR(128) NOT NULL)');
        $this->addSql('INSERT INTO building (id, name) SELECT id, name FROM __temp__building');
        $this->addSql('DROP TABLE __temp__building');
        $this->addSql('CREATE UNIQUE INDEX uix_building_name ON building (name)');
        $this->addSql('DROP INDEX uix_group_name');
        $this->addSql('DROP INDEX uix_group_parent_group_id');
        $this->addSql('CREATE TEMPORARY TABLE __temp__group AS SELECT id, parent_group_id, name FROM "group"');
        $this->addSql('DROP TABLE "group"');
        $this->addSql('CREATE TABLE "group" (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, parent_group_id INTEGER DEFAULT NULL, name VARCHAR(128) NOT NULL)');
        $this->addSql('INSERT INTO "group" (id, parent_group_id, name) SELECT id, parent_group_id, name FROM __temp__group');
        $this->addSql('DROP TABLE __temp__group');
        $this->addSql('CREATE UNIQUE INDEX uix_parent_group_id ON "group" (parent_group_id)');
        $this->addSql('CREATE UNIQUE INDEX uix_group_name ON "group" (name)');
        $this->addSql('DROP INDEX ix_member_user_id');
        $this->addSql('DROP INDEX ix_member_group_id');
        $this->addSql('CREATE TEMPORARY TABLE __temp__member AS SELECT id, user_id, group_id, manager FROM member');
        $this->addSql('DROP INDEX uix_member_user_group');
        $this->addSql('DROP TABLE member');
        $this->addSql('CREATE TABLE member (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, user_id INTEGER NOT NULL, group_id INTEGER NOT NULL, manager BOOLEAN NOT NULL)');
        $this->addSql('INSERT INTO member (id, user_id, group_id, manager) SELECT id, user_id, group_id, manager FROM __temp__member');
        $this->addSql('DROP TABLE __temp__member');
        $this->addSql('CREATE INDEX ix_member_group_id ON member (group_id)');
        $this->addSql('CREATE INDEX ix_member_user_id ON member (user_id)');
        $this->addSql('DROP INDEX ix_reservation_author_id');
        $this->addSql('DROP INDEX ix_reservation_room_id');
        $this->addSql('DROP INDEX ix_reservation_group_id');
        $this->addSql('CREATE TEMPORARY TABLE __temp__reservation AS SELECT id, author_id, room_id, group_id, approved, cancelled, start, until FROM reservation');
        $this->addSql('DROP TABLE reservation');
        $this->addSql('CREATE TABLE reservation (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, user_id INTEGER NOT NULL, room_id INTEGER NOT NULL, group_id INTEGER DEFAULT NULL, approved BOOLEAN NOT NULL, cancelled BOOLEAN NOT NULL, start DATETIME NOT NULL, until DATETIME NOT NULL)');
        $this->addSql('INSERT INTO reservation (id, user_id, room_id, group_id, approved, cancelled, start, until) SELECT id, author_id, room_id, group_id, approved, cancelled, start, until FROM __temp__reservation');
        $this->addSql('DROP TABLE __temp__reservation');
        $this->addSql('CREATE INDEX ix_reservation_user_id ON reservation (user_id)');
        $this->addSql('CREATE INDEX ix_reservation_group_id ON reservation (group_id)');
        $this->addSql('CREATE INDEX ix_reservation_room_id ON reservation (room_id)');
        $this->addSql('DROP INDEX uix_room_name');
        $this->addSql('DROP INDEX ix_room_group_id');
        $this->addSql('DROP INDEX ix_room_building_id');
        $this->addSql('CREATE TEMPORARY TABLE __temp__room AS SELECT id, group_id, building_id, name, public FROM room');
        $this->addSql('DROP TABLE room');
        $this->addSql('CREATE TABLE room (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, group_id INTEGER NOT NULL, building_id INTEGER NOT NULL, name VARCHAR(128) NOT NULL, public BOOLEAN NOT NULL)');
        $this->addSql('INSERT INTO room (id, group_id, building_id, name, public) SELECT id, group_id, building_id, name, public FROM __temp__room');
        $this->addSql('DROP TABLE __temp__room');
        $this->addSql('CREATE INDEX ix_room_building_id ON room (building_id)');
        $this->addSql('CREATE INDEX ix_room_group_id ON room (group_id)');
        $this->addSql('CREATE UNIQUE INDEX uix_room_name ON room (name)');
        $this->addSql('DROP INDEX ix_room_user_user_id');
        $this->addSql('DROP INDEX ix_room_user_room_id');
        $this->addSql('CREATE TEMPORARY TABLE __temp__room_user AS SELECT id, room_id, user_id, manager FROM room_user');
        $this->addSql('DROP TABLE room_user');
        $this->addSql('CREATE TABLE room_user (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, room_id INTEGER NOT NULL, user_id INTEGER NOT NULL, manager BOOLEAN NOT NULL)');
        $this->addSql('INSERT INTO room_user (id, room_id, user_id, manager) SELECT id, room_id, user_id, manager FROM __temp__room_user');
        $this->addSql('DROP TABLE __temp__room_user');
        $this->addSql('CREATE INDEX ix_room_user_user_id ON room_user (user_id)');
        $this->addSql('CREATE INDEX ix_room_user_room_id ON room_user (room_id)');
        $this->addSql('DROP INDEX uix_user_email');
        $this->addSql('CREATE TEMPORARY TABLE __temp__user AS SELECT id, email, password, name, tel, admin FROM "user"');
        $this->addSql('DROP TABLE "user"');
        $this->addSql('CREATE TABLE "user" (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, email VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, name VARCHAR(128) NOT NULL, tel VARCHAR(16) DEFAULT NULL, admin BOOLEAN NOT NULL)');
        $this->addSql('INSERT INTO "user" (id, email, password, name, tel, admin) SELECT id, email, password, name, tel, admin FROM __temp__user');
        $this->addSql('DROP TABLE __temp__user');
        $this->addSql('CREATE UNIQUE INDEX uix_user_email ON "user" (email)');
    }
}
