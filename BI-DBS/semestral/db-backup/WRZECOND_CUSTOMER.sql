create table CUSTOMER
(
    ID            NUMBER       not null
        constraint CUSTOMER_PK
            primary key,
    AGE           NUMBER       not null,
    FIRST_NAME    VARCHAR2(64) not null,
    LAST_NAME     VARCHAR2(64) not null,
    TABLE_ID      NUMBER       not null,
    RESTAURANT_ID NUMBER       not null,
    constraint CUSTOMER_FK1
        foreign key (TABLE_ID, RESTAURANT_ID) references TTABLE
)
/

INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (1, 39, 'Zdeněk', 'Vomáčka', 10, 3);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (2, 89, 'Zikmund', 'Konvalinka', 33, 6);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (3, 98, 'Karel', 'Novotný', 1, 1);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (4, 67, 'Karel', 'Tříska', 3, 1);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (5, 1, 'Erik', 'Konvalinka', 26, 5);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (6, 71, 'Josef', 'Novák', 26, 5);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (7, 45, 'Hugo', 'Novotný', 3, 1);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (8, 53, 'Patrik', 'Novotný', 28, 6);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (9, 94, 'Filip', 'Janoušek', 20, 5);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (10, 31, 'Karel', 'Konvalinka', 26, 5);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (11, 15, 'Zdeněk', 'Novák', 10, 3);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (12, 3, 'Zdeněk', 'Vyskočil', 38, 8);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (13, 15, 'Hugo', 'Konvalinka', 22, 5);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (14, 81, 'Hugo', 'Novák', 10, 3);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (15, 27, 'Zikmund', 'Vyskočil', 29, 6);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (16, 67, 'Jan', 'Konvalinka', 12, 3);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (17, 76, 'Zikmund', 'Janoušek', 9, 3);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (18, 91, 'Hugo', 'Vyskočil', 33, 6);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (19, 18, 'Josef', 'Skřivánek', 25, 5);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (20, 67, 'Jan', 'Vyskočil', 50, 10);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (21, 18, 'Josef', 'Tříska', 12, 3);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (22, 40, 'Zdeněk', 'Novotný', 6, 2);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (23, 35, 'Filip', 'Vyskočil', 48, 9);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (24, 52, 'Zdeněk', 'Konvalinka', 2, 1);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (25, 75, 'Vojtěch', 'Novotný', 11, 3);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (26, 92, 'Hugo', 'Janoušek', 32, 6);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (27, 47, 'Vojtěch', 'Ševčík', 46, 9);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (28, 64, 'Jan', 'Novák', 18, 4);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (29, 89, 'Hugo', 'Janoušek', 26, 5);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (30, 38, 'Filip', 'Vyskočil', 10, 3);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (31, 19, 'Zikmund', 'Janoušek', 28, 6);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (32, 59, 'Patrik', 'Vomáčka', 1, 1);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (33, 84, 'Jan', 'Skřivánek', 24, 5);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (34, 67, 'Jan', 'Vomáčka', 18, 4);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (35, 76, 'Erik', 'Vomáčka', 29, 6);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (36, 31, 'Filip', 'Vyskočil', 28, 6);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (37, 39, 'Zdeněk', 'Ševčík', 10, 3);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (38, 65, 'Erik', 'Janoušek', 10, 3);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (39, 65, 'Erik', 'Skřivánek', 19, 5);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (40, 50, 'Filip', 'Spěvák', 46, 9);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (41, 54, 'Patrik', 'Janoušek', 10, 3);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (42, 65, 'Patrik', 'Spěvák', 38, 8);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (43, 16, 'Josef', 'Spěvák', 52, 10);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (44, 8, 'Filip', 'Janoušek', 19, 5);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (45, 52, 'Zikmund', 'Novák', 20, 5);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (46, 96, 'Vojtěch', 'Novotný', 24, 5);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (47, 55, 'Erik', 'Spěvák', 28, 6);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (48, 27, 'Karel', 'Skřivánek', 39, 8);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (49, 52, 'Zikmund', 'Novotný', 3, 1);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (50, 39, 'Zdeněk', 'Tříska', 12, 3);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (51, 35, 'Erik', 'Ševčík', 27, 6);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (52, 86, 'Jan', 'Novotný', 5, 1);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (53, 23, 'Jan', 'Vomáčka', 24, 5);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (54, 31, 'Karel', 'Konvalinka', 44, 9);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (55, 39, 'Hugo', 'Konvalinka', 6, 2);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (56, 57, 'Filip', 'Vyskočil', 8, 2);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (57, 10, 'Filip', 'Konvalinka', 20, 5);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (58, 34, 'Erik', 'Skřivánek', 19, 5);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (59, 91, 'Hugo', 'Spěvák', 16, 4);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (60, 90, 'Zdeněk', 'Ševčík', 19, 5);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (61, 27, 'Jan', 'Skřivánek', 13, 4);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (62, 32, 'Karel', 'Skřivánek', 16, 4);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (63, 77, 'Karel', 'Novotný', 43, 8);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (64, 12, 'Jan', 'Konvalinka', 45, 9);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (65, 48, 'Zdeněk', 'Skřivánek', 14, 4);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (66, 31, 'Erik', 'Vomáčka', 2, 1);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (67, 10, 'Josef', 'Vomáčka', 34, 7);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (68, 69, 'Zikmund', 'Ševčík', 19, 5);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (69, 75, 'Josef', 'Novotný', 7, 2);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (70, 54, 'Filip', 'Ševčík', 4, 1);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (71, 50, 'Vojtěch', 'Vomáčka', 31, 6);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (72, 63, 'Josef', 'Spěvák', 2, 1);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (73, 71, 'Josef', 'Vomáčka', 28, 6);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (74, 71, 'Jan', 'Ševčík', 40, 8);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (75, 80, 'Filip', 'Spěvák', 3, 1);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (76, 44, 'Josef', 'Ševčík', 29, 6);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (77, 0, 'Zikmund', 'Janoušek', 44, 9);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (78, 24, 'Hugo', 'Novotný', 34, 7);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (79, 23, 'Josef', 'Skřivánek', 5, 1);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (80, 62, 'Jan', 'Novotný', 16, 4);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (81, 18, 'Hugo', 'Vomáčka', 14, 4);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (82, 51, 'Zikmund', 'Novák', 24, 5);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (83, 83, 'Zdeněk', 'Janoušek', 24, 5);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (84, 60, 'Patrik', 'Novotný', 13, 4);
INSERT INTO WRZECOND.CUSTOMER (ID, AGE, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID) VALUES (85, 35, 'Jan', 'Skřivánek', 13, 4);