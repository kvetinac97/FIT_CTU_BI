<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20211222180430 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('DROP INDEX IDX_7D3656A4A76ED395');
        $this->addSql('DROP INDEX UNIQ_7D3656A45E237E06');
        $this->addSql('CREATE TEMPORARY TABLE __temp__account AS SELECT id, user_id, name, password, roles, expiration FROM account');
        $this->addSql('DROP TABLE account');
        $this->addSql('CREATE TABLE account (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, user_id INTEGER NOT NULL, name VARCHAR(64) NOT NULL COLLATE BINARY, password VARCHAR(255) NOT NULL COLLATE BINARY, roles CLOB NOT NULL COLLATE BINARY --(DC2Type:json)
        , expiration DATETIME DEFAULT NULL, CONSTRAINT FK_7D3656A4A76ED395 FOREIGN KEY (user_id) REFERENCES "user" (id) NOT DEFERRABLE INITIALLY IMMEDIATE)');
        $this->addSql('INSERT INTO account (id, user_id, name, password, roles, expiration) SELECT id, user_id, name, password, roles, expiration FROM __temp__account');
        $this->addSql('DROP TABLE __temp__account');
        $this->addSql('CREATE INDEX IDX_7D3656A4A76ED395 ON account (user_id)');
        $this->addSql('CREATE UNIQUE INDEX UNIQ_7D3656A45E237E06 ON account (name)');
        $this->addSql('DROP INDEX UNIQ_6DC044C55E237E06');
        $this->addSql('DROP INDEX IDX_6DC044C561997596');
        $this->addSql('CREATE TEMPORARY TABLE __temp__group AS SELECT id, parent_group_id, name FROM "group"');
        $this->addSql('DROP TABLE "group"');
        $this->addSql('CREATE TABLE "group" (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, parent_group_id INTEGER DEFAULT NULL, name VARCHAR(128) NOT NULL COLLATE BINARY, CONSTRAINT FK_6DC044C561997596 FOREIGN KEY (parent_group_id) REFERENCES "group" (id) NOT DEFERRABLE INITIALLY IMMEDIATE)');
        $this->addSql('INSERT INTO "group" (id, parent_group_id, name) SELECT id, parent_group_id, name FROM __temp__group');
        $this->addSql('DROP TABLE __temp__group');
        $this->addSql('CREATE UNIQUE INDEX UNIQ_6DC044C55E237E06 ON "group" (name)');
        $this->addSql('CREATE INDEX IDX_6DC044C561997596 ON "group" (parent_group_id)');
        $this->addSql('DROP INDEX IDX_70E4FA78A76ED395');
        $this->addSql('DROP INDEX IDX_70E4FA78FE54D947');
        $this->addSql('DROP INDEX uix_member_user_id_group_id');
        $this->addSql('CREATE TEMPORARY TABLE __temp__member AS SELECT id, user_id, group_id, manager FROM member');
        $this->addSql('DROP TABLE member');
        $this->addSql('CREATE TABLE member (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, user_id INTEGER NOT NULL, group_id INTEGER NOT NULL, manager BOOLEAN NOT NULL, CONSTRAINT FK_70E4FA78A76ED395 FOREIGN KEY (user_id) REFERENCES "user" (id) NOT DEFERRABLE INITIALLY IMMEDIATE, CONSTRAINT FK_70E4FA78FE54D947 FOREIGN KEY (group_id) REFERENCES "group" (id) NOT DEFERRABLE INITIALLY IMMEDIATE)');
        $this->addSql('INSERT INTO member (id, user_id, group_id, manager) SELECT id, user_id, group_id, manager FROM __temp__member');
        $this->addSql('DROP TABLE __temp__member');
        $this->addSql('CREATE INDEX IDX_70E4FA78A76ED395 ON member (user_id)');
        $this->addSql('CREATE INDEX IDX_70E4FA78FE54D947 ON member (group_id)');
        $this->addSql('CREATE UNIQUE INDEX uix_member_user_id_group_id ON member (user_id, group_id)');
        $this->addSql('DROP INDEX IDX_42C84955F675F31B');
        $this->addSql('DROP INDEX IDX_42C8495554177093');
        $this->addSql('CREATE TEMPORARY TABLE __temp__reservation AS SELECT id, author_id, room_id, approved, cancelled, start, until FROM reservation');
        $this->addSql('DROP TABLE reservation');
        $this->addSql('CREATE TABLE reservation (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, author_id INTEGER NOT NULL, room_id INTEGER NOT NULL, approved BOOLEAN NOT NULL, cancelled BOOLEAN NOT NULL, start DATETIME NOT NULL, until DATETIME NOT NULL, CONSTRAINT FK_42C84955F675F31B FOREIGN KEY (author_id) REFERENCES "user" (id) NOT DEFERRABLE INITIALLY IMMEDIATE, CONSTRAINT FK_42C8495554177093 FOREIGN KEY (room_id) REFERENCES room (id) NOT DEFERRABLE INITIALLY IMMEDIATE)');
        $this->addSql('INSERT INTO reservation (id, author_id, room_id, approved, cancelled, start, until) SELECT id, author_id, room_id, approved, cancelled, start, until FROM __temp__reservation');
        $this->addSql('DROP TABLE __temp__reservation');
        $this->addSql('CREATE INDEX IDX_42C84955F675F31B ON reservation (author_id)');
        $this->addSql('CREATE INDEX IDX_42C8495554177093 ON reservation (room_id)');
        $this->addSql('DROP INDEX IDX_9BAA1B21B83297E7');
        $this->addSql('DROP INDEX IDX_9BAA1B21A76ED395');
        $this->addSql('CREATE TEMPORARY TABLE __temp__reservation_user AS SELECT reservation_id, user_id FROM reservation_user');
        $this->addSql('DROP TABLE reservation_user');
        $this->addSql('CREATE TABLE reservation_user (reservation_id INTEGER NOT NULL, user_id INTEGER NOT NULL, PRIMARY KEY(reservation_id, user_id), CONSTRAINT FK_9BAA1B21B83297E7 FOREIGN KEY (reservation_id) REFERENCES reservation (id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE, CONSTRAINT FK_9BAA1B21A76ED395 FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE)');
        $this->addSql('INSERT INTO reservation_user (reservation_id, user_id) SELECT reservation_id, user_id FROM __temp__reservation_user');
        $this->addSql('DROP TABLE __temp__reservation_user');
        $this->addSql('CREATE INDEX IDX_9BAA1B21B83297E7 ON reservation_user (reservation_id)');
        $this->addSql('CREATE INDEX IDX_9BAA1B21A76ED395 ON reservation_user (user_id)');
        $this->addSql('DROP INDEX IDX_729F519B4D2A7E12');
        $this->addSql('DROP INDEX IDX_729F519BFE54D947');
        $this->addSql('DROP INDEX UNIQ_729F519B5E237E06');
        $this->addSql('CREATE TEMPORARY TABLE __temp__room AS SELECT id, group_id, building_id, name, public, open FROM room');
        $this->addSql('DROP TABLE room');
        $this->addSql('CREATE TABLE room (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, group_id INTEGER NOT NULL, building_id INTEGER NOT NULL, name VARCHAR(128) NOT NULL COLLATE BINARY, public BOOLEAN NOT NULL, open BOOLEAN NOT NULL, CONSTRAINT FK_729F519BFE54D947 FOREIGN KEY (group_id) REFERENCES "group" (id) NOT DEFERRABLE INITIALLY IMMEDIATE, CONSTRAINT FK_729F519B4D2A7E12 FOREIGN KEY (building_id) REFERENCES building (id) NOT DEFERRABLE INITIALLY IMMEDIATE)');
        $this->addSql('INSERT INTO room (id, group_id, building_id, name, public, open) SELECT id, group_id, building_id, name, public, open FROM __temp__room');
        $this->addSql('DROP TABLE __temp__room');
        $this->addSql('CREATE INDEX IDX_729F519B4D2A7E12 ON room (building_id)');
        $this->addSql('CREATE INDEX IDX_729F519BFE54D947 ON room (group_id)');
        $this->addSql('CREATE UNIQUE INDEX UNIQ_729F519B5E237E06 ON room (name)');
        $this->addSql('DROP INDEX IDX_EE973C2D54177093');
        $this->addSql('DROP INDEX IDX_EE973C2DA76ED395');
        $this->addSql('CREATE TEMPORARY TABLE __temp__room_user AS SELECT id, room_id, user_id, manager FROM room_user');
        $this->addSql('DROP TABLE room_user');
        $this->addSql('CREATE TABLE room_user (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, room_id INTEGER NOT NULL, user_id INTEGER NOT NULL, manager BOOLEAN NOT NULL, CONSTRAINT FK_EE973C2D54177093 FOREIGN KEY (room_id) REFERENCES room (id) NOT DEFERRABLE INITIALLY IMMEDIATE, CONSTRAINT FK_EE973C2DA76ED395 FOREIGN KEY (user_id) REFERENCES "user" (id) NOT DEFERRABLE INITIALLY IMMEDIATE)');
        $this->addSql('INSERT INTO room_user (id, room_id, user_id, manager) SELECT id, room_id, user_id, manager FROM __temp__room_user');
        $this->addSql('DROP TABLE __temp__room_user');
        $this->addSql('CREATE INDEX IDX_EE973C2D54177093 ON room_user (room_id)');
        $this->addSql('CREATE INDEX IDX_EE973C2DA76ED395 ON room_user (user_id)');
        $this->addSql('DROP INDEX UNIQ_8D93D649E7927C74');
        $this->addSql('CREATE TEMPORARY TABLE __temp__user AS SELECT id, email, name, tel, image FROM user');
        $this->addSql('DROP TABLE user');
        $this->addSql('CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, email VARCHAR(255) NOT NULL COLLATE BINARY, name VARCHAR(128) NOT NULL COLLATE BINARY, tel VARCHAR(16) DEFAULT NULL COLLATE BINARY, image VARCHAR(255) DEFAULT NULL COLLATE BINARY)');
        $this->addSql('INSERT INTO user (id, email, name, tel, image) SELECT id, email, name, tel, image FROM __temp__user');
        $this->addSql('DROP TABLE __temp__user');
        $this->addSql('CREATE UNIQUE INDEX UNIQ_8D93D649E7927C74 ON user (email)');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('DROP INDEX UNIQ_7D3656A45E237E06');
        $this->addSql('DROP INDEX IDX_7D3656A4A76ED395');
        $this->addSql('CREATE TEMPORARY TABLE __temp__account AS SELECT id, user_id, name, password, roles, expiration FROM account');
        $this->addSql('DROP TABLE account');
        $this->addSql('CREATE TABLE account (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, user_id INTEGER NOT NULL, name VARCHAR(64) NOT NULL, password VARCHAR(255) NOT NULL, roles CLOB NOT NULL --(DC2Type:json)
        , expiration DATETIME DEFAULT NULL)');
        $this->addSql('INSERT INTO account (id, user_id, name, password, roles, expiration) SELECT id, user_id, name, password, roles, expiration FROM __temp__account');
        $this->addSql('DROP TABLE __temp__account');
        $this->addSql('CREATE UNIQUE INDEX UNIQ_7D3656A45E237E06 ON account (name)');
        $this->addSql('CREATE INDEX IDX_7D3656A4A76ED395 ON account (user_id)');
        $this->addSql('DROP INDEX UNIQ_6DC044C55E237E06');
        $this->addSql('DROP INDEX IDX_6DC044C561997596');
        $this->addSql('CREATE TEMPORARY TABLE __temp__group AS SELECT id, parent_group_id, name FROM "group"');
        $this->addSql('DROP TABLE "group"');
        $this->addSql('CREATE TABLE "group" (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, parent_group_id INTEGER DEFAULT NULL, name VARCHAR(128) NOT NULL)');
        $this->addSql('INSERT INTO "group" (id, parent_group_id, name) SELECT id, parent_group_id, name FROM __temp__group');
        $this->addSql('DROP TABLE __temp__group');
        $this->addSql('CREATE UNIQUE INDEX UNIQ_6DC044C55E237E06 ON "group" (name)');
        $this->addSql('CREATE INDEX IDX_6DC044C561997596 ON "group" (parent_group_id)');
        $this->addSql('DROP INDEX IDX_70E4FA78A76ED395');
        $this->addSql('DROP INDEX IDX_70E4FA78FE54D947');
        $this->addSql('DROP INDEX uix_member_user_id_group_id');
        $this->addSql('CREATE TEMPORARY TABLE __temp__member AS SELECT id, user_id, group_id, manager FROM member');
        $this->addSql('DROP TABLE member');
        $this->addSql('CREATE TABLE member (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, user_id INTEGER NOT NULL, group_id INTEGER NOT NULL, manager BOOLEAN NOT NULL)');
        $this->addSql('INSERT INTO member (id, user_id, group_id, manager) SELECT id, user_id, group_id, manager FROM __temp__member');
        $this->addSql('DROP TABLE __temp__member');
        $this->addSql('CREATE INDEX IDX_70E4FA78A76ED395 ON member (user_id)');
        $this->addSql('CREATE INDEX IDX_70E4FA78FE54D947 ON member (group_id)');
        $this->addSql('CREATE UNIQUE INDEX uix_member_user_id_group_id ON member (user_id, group_id)');
        $this->addSql('DROP INDEX IDX_42C84955F675F31B');
        $this->addSql('DROP INDEX IDX_42C8495554177093');
        $this->addSql('CREATE TEMPORARY TABLE __temp__reservation AS SELECT id, author_id, room_id, approved, cancelled, start, until FROM reservation');
        $this->addSql('DROP TABLE reservation');
        $this->addSql('CREATE TABLE reservation (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, author_id INTEGER NOT NULL, room_id INTEGER NOT NULL, approved BOOLEAN NOT NULL, cancelled BOOLEAN NOT NULL, start DATETIME NOT NULL, until DATETIME NOT NULL)');
        $this->addSql('INSERT INTO reservation (id, author_id, room_id, approved, cancelled, start, until) SELECT id, author_id, room_id, approved, cancelled, start, until FROM __temp__reservation');
        $this->addSql('DROP TABLE __temp__reservation');
        $this->addSql('CREATE INDEX IDX_42C84955F675F31B ON reservation (author_id)');
        $this->addSql('CREATE INDEX IDX_42C8495554177093 ON reservation (room_id)');
        $this->addSql('DROP INDEX IDX_9BAA1B21B83297E7');
        $this->addSql('DROP INDEX IDX_9BAA1B21A76ED395');
        $this->addSql('CREATE TEMPORARY TABLE __temp__reservation_user AS SELECT reservation_id, user_id FROM reservation_user');
        $this->addSql('DROP TABLE reservation_user');
        $this->addSql('CREATE TABLE reservation_user (reservation_id INTEGER NOT NULL, user_id INTEGER NOT NULL, PRIMARY KEY(reservation_id, user_id))');
        $this->addSql('INSERT INTO reservation_user (reservation_id, user_id) SELECT reservation_id, user_id FROM __temp__reservation_user');
        $this->addSql('DROP TABLE __temp__reservation_user');
        $this->addSql('CREATE INDEX IDX_9BAA1B21B83297E7 ON reservation_user (reservation_id)');
        $this->addSql('CREATE INDEX IDX_9BAA1B21A76ED395 ON reservation_user (user_id)');
        $this->addSql('DROP INDEX UNIQ_729F519B5E237E06');
        $this->addSql('DROP INDEX IDX_729F519BFE54D947');
        $this->addSql('DROP INDEX IDX_729F519B4D2A7E12');
        $this->addSql('CREATE TEMPORARY TABLE __temp__room AS SELECT id, group_id, building_id, name, public, open FROM room');
        $this->addSql('DROP TABLE room');
        $this->addSql('CREATE TABLE room (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, group_id INTEGER NOT NULL, building_id INTEGER NOT NULL, name VARCHAR(128) NOT NULL, public BOOLEAN NOT NULL, open BOOLEAN NOT NULL)');
        $this->addSql('INSERT INTO room (id, group_id, building_id, name, public, open) SELECT id, group_id, building_id, name, public, open FROM __temp__room');
        $this->addSql('DROP TABLE __temp__room');
        $this->addSql('CREATE UNIQUE INDEX UNIQ_729F519B5E237E06 ON room (name)');
        $this->addSql('CREATE INDEX IDX_729F519BFE54D947 ON room (group_id)');
        $this->addSql('CREATE INDEX IDX_729F519B4D2A7E12 ON room (building_id)');
        $this->addSql('DROP INDEX IDX_EE973C2D54177093');
        $this->addSql('DROP INDEX IDX_EE973C2DA76ED395');
        $this->addSql('CREATE TEMPORARY TABLE __temp__room_user AS SELECT id, room_id, user_id, manager FROM room_user');
        $this->addSql('DROP TABLE room_user');
        $this->addSql('CREATE TABLE room_user (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, room_id INTEGER NOT NULL, user_id INTEGER NOT NULL, manager BOOLEAN NOT NULL)');
        $this->addSql('INSERT INTO room_user (id, room_id, user_id, manager) SELECT id, room_id, user_id, manager FROM __temp__room_user');
        $this->addSql('DROP TABLE __temp__room_user');
        $this->addSql('CREATE INDEX IDX_EE973C2D54177093 ON room_user (room_id)');
        $this->addSql('CREATE INDEX IDX_EE973C2DA76ED395 ON room_user (user_id)');
        $this->addSql('ALTER TABLE user ADD COLUMN password VARCHAR(255) NOT NULL COLLATE BINARY');
        $this->addSql('ALTER TABLE user ADD COLUMN roles CLOB NOT NULL COLLATE BINARY');
    }
}
