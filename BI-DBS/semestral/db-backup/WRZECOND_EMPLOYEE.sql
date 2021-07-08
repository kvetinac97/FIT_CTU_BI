create table EMPLOYEE
(
    ID            NUMBER       not null
        constraint EMPLOYEE_PK
            primary key,
    COOK          NUMBER       not null,
    FIRST_NAME    VARCHAR2(64) not null,
    LAST_NAME     VARCHAR2(64) not null,
    SALARY        NUMBER       not null,
    RESTAURANT_ID NUMBER       not null
        constraint EMPLOYEE_FK1
            references RESTAURANT,
    PASSWORD      VARCHAR2(255 char),
    USERNAME      VARCHAR2(255 char)
)
/

INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (1, 0, 'Jan', 'Ševčík', 18000, 1, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (2, 1, 'Karel', 'Skřivánek', 34000, 1, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (3, 0, 'Jan', 'Novotný', 21000, 1, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (4, 1, 'Karel', 'Janoušek', 31000, 1, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (5, 1, 'Jan', 'Novotný', 36000, 1, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (6, 0, 'Jan', 'Tříska', 33000, 1, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (7, 0, 'Filip', 'Tříska', 14000, 1, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (8, 0, 'Vojtěch', 'Janoušek', 22000, 1, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (9, 1, 'Hugo', 'Ševčík', 19000, 2, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (10, 0, 'Jan', 'Novák', 39000, 2, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (11, 1, 'Filip', 'Konvalinka', 21000, 2, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (12, 1, 'Josef', 'Novák', 18000, 2, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (13, 0, 'Erik', 'Ševčík', 12000, 2, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (14, 0, 'Josef', 'Novotný', 28000, 2, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (15, 0, 'Hugo', 'Ševčík', 38000, 2, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (16, 1, 'Jan', 'Skřivánek', 13000, 2, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (17, 0, 'Josef', 'Vyskočil', 29000, 3, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (18, 1, 'Erik', 'Vomáčka', 25000, 3, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (19, 1, 'Zdeněk', 'Ševčík', 24000, 3, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (20, 1, 'Jan', 'Vomáčka', 22000, 3, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (21, 1, 'Erik', 'Janoušek', 15000, 3, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (22, 0, 'Zdeněk', 'Novák', 22000, 3, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (23, 0, 'Filip', 'Vomáčka', 14000, 3, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (24, 0, 'Zikmund', 'Konvalinka', 17000, 3, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (25, 0, 'Erik', 'Janoušek', 23000, 4, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (26, 1, 'Patrik', 'Vyskočil', 37000, 4, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (27, 1, 'Zdeněk', 'Skřivánek', 21000, 4, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (28, 0, 'Vojtěch', 'Vomáčka', 15000, 4, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (29, 0, 'Karel', 'Tříska', 28000, 4, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (30, 1, 'Patrik', 'Skřivánek', 32000, 5, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (31, 0, 'Filip', 'Novotný', 26000, 6, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (32, 0, 'Karel', 'Vomáčka', 22000, 6, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (33, 0, 'Patrik', 'Novák', 27000, 6, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (34, 1, 'Erik', 'Novák', 18000, 6, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (35, 0, 'Josef', 'Janoušek', 36000, 6, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (36, 0, 'Hugo', 'Tříska', 31000, 6, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (37, 1, 'Karel', 'Vyskočil', 15000, 6, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (38, 0, 'Zdeněk', 'Ševčík', 13000, 6, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (39, 0, 'Vojtěch', 'Konvalinka', 39000, 6, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (40, 0, 'Hugo', 'Vyskočil', 26000, 7, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (41, 0, 'Zdeněk', 'Konvalinka', 34000, 7, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (42, 0, 'Zdeněk', 'Ševčík', 38000, 7, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (43, 1, 'Hugo', 'Novotný', 15000, 7, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (44, 1, 'Vojtěch', 'Janoušek', 20000, 8, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (45, 1, 'Patrik', 'Vomáčka', 28000, 8, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (46, 0, 'Filip', 'Novotný', 20000, 8, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (47, 0, 'Vojtěch', 'Vomáčka', 13000, 8, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (48, 0, 'Karel', 'Novák', 39000, 8, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (49, 0, 'Zdeněk', 'Vomáčka', 34000, 8, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (50, 1, 'Karel', 'Vomáčka', 15000, 8, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (51, 1, 'Jan', 'Janoušek', 33000, 8, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (52, 0, 'Hugo', 'Spěvák', 19000, 9, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (53, 1, 'Patrik', 'Novák', 13000, 9, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (54, 1, 'Josef', 'Vomáčka', 16000, 9, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (55, 0, 'Jan', 'Skřivánek', 19000, 9, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (56, 0, 'Erik', 'Spěvák', 20000, 9, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (57, 1, 'Zdeněk', 'Vomáčka', 25000, 9, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (58, 1, 'Filip', 'Tříska', 36000, 10, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (59, 0, 'Vojtěch', 'Konvalinka', 35000, 10, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (60, 1, 'Zikmund', 'Vyskočil', 21000, 10, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (61, 1, 'Zdeněk', 'Vyskočil', 12000, 10, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (62, 1, 'Erik', 'Tříska', 27000, 10, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (63, 1, 'Erik', 'Skřivánek', 39000, 11, null, null);
INSERT INTO WRZECOND.EMPLOYEE (ID, COOK, FIRST_NAME, LAST_NAME, SALARY, RESTAURANT_ID, PASSWORD, USERNAME) VALUES (64, 1, 'Erik', 'Vomáčka', 46000, 12, null, null);