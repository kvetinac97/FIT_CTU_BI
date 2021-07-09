create view CUSTOMER_VIEW as
( SELECT cst.ID, cst.FIRST_NAME, cst.LAST_NAME, tbl.ID AS TABLE_ID, rst.ID AS RESTAURANT_ID, rst.NAME AS RESTAURANT_NAME, COUNT(food.ID) AS FOOD_COUNT FROM CUSTOMER cst
JOIN TTABLE tbl ON tbl.ID = cst.TABLE_ID
JOIN RESTAURANT rst ON rst.ID = tbl.RESTAURANT_ID
JOIN ORDER_FOOD orf ON ( orf.RESTAURANT_ID = rst.ID AND orf.TABLE_ID = tbl.ID )
JOIN FOOD food ON food.ID = orf.FOOD_ID
GROUP BY cst.ID, cst.FIRST_NAME, cst.LAST_NAME, tbl.ID, rst.ID, rst.NAME )
/

INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (4, 'Karel', 'Tříska', 3, 1, 'Ostravská', 3);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (6, 'Josef', 'Novák', 26, 5, 'Síla chuti', 2);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (9, 'Filip', 'Janoušek', 20, 5, 'Síla chuti', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (14, 'Hugo', 'Novák', 10, 3, 'Divinis', 2);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (19, 'Josef', 'Skřivánek', 25, 5, 'Síla chuti', 2);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (29, 'Hugo', 'Janoušek', 26, 5, 'Síla chuti', 2);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (35, 'Erik', 'Vomáčka', 29, 6, 'Červené jablko', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (44, 'Filip', 'Janoušek', 19, 5, 'Síla chuti', 10);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (57, 'Filip', 'Konvalinka', 20, 5, 'Síla chuti', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (73, 'Josef', 'Vomáčka', 28, 6, 'Červené jablko', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (84, 'Patrik', 'Novotný', 13, 4, 'Harmony', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (22, 'Zdeněk', 'Novotný', 6, 2, 'Café Imperial', 2);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (55, 'Hugo', 'Konvalinka', 6, 2, 'Café Imperial', 2);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (70, 'Filip', 'Ševčík', 4, 1, 'Ostravská', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (76, 'Josef', 'Ševčík', 29, 6, 'Červené jablko', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (5, 'Erik', 'Konvalinka', 26, 5, 'Síla chuti', 2);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (21, 'Josef', 'Tříska', 12, 3, 'Divinis', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (46, 'Vojtěch', 'Novotný', 24, 5, 'Síla chuti', 10);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (52, 'Jan', 'Novotný', 5, 1, 'Ostravská', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (85, 'Jan', 'Skřivánek', 13, 4, 'Harmony', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (7, 'Hugo', 'Novotný', 3, 1, 'Ostravská', 3);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (16, 'Jan', 'Konvalinka', 12, 3, 'Divinis', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (34, 'Jan', 'Vomáčka', 18, 4, 'Harmony', 3);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (53, 'Jan', 'Vomáčka', 24, 5, 'Síla chuti', 10);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (59, 'Hugo', 'Spěvák', 16, 4, 'Harmony', 2);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (79, 'Josef', 'Skřivánek', 5, 1, 'Ostravská', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (11, 'Zdeněk', 'Novák', 10, 3, 'Divinis', 2);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (31, 'Zikmund', 'Janoušek', 28, 6, 'Červené jablko', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (51, 'Erik', 'Ševčík', 27, 6, 'Červené jablko', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (65, 'Zdeněk', 'Skřivánek', 14, 4, 'Harmony', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (72, 'Josef', 'Spěvák', 2, 1, 'Ostravská', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (80, 'Jan', 'Novotný', 16, 4, 'Harmony', 2);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (8, 'Patrik', 'Novotný', 28, 6, 'Červené jablko', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (24, 'Zdeněk', 'Konvalinka', 2, 1, 'Ostravská', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (25, 'Vojtěch', 'Novotný', 11, 3, 'Divinis', 3);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (28, 'Jan', 'Novák', 18, 4, 'Harmony', 3);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (33, 'Jan', 'Skřivánek', 24, 5, 'Síla chuti', 10);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (39, 'Erik', 'Skřivánek', 19, 5, 'Síla chuti', 10);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (45, 'Zikmund', 'Novák', 20, 5, 'Síla chuti', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (50, 'Zdeněk', 'Tříska', 12, 3, 'Divinis', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (58, 'Erik', 'Skřivánek', 19, 5, 'Síla chuti', 10);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (60, 'Zdeněk', 'Ševčík', 19, 5, 'Síla chuti', 10);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (82, 'Zikmund', 'Novák', 24, 5, 'Síla chuti', 10);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (15, 'Zikmund', 'Vyskočil', 29, 6, 'Červené jablko', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (36, 'Filip', 'Vyskočil', 28, 6, 'Červené jablko', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (37, 'Zdeněk', 'Ševčík', 10, 3, 'Divinis', 2);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (62, 'Karel', 'Skřivánek', 16, 4, 'Harmony', 2);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (66, 'Erik', 'Vomáčka', 2, 1, 'Ostravská', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (68, 'Zikmund', 'Ševčík', 19, 5, 'Síla chuti', 10);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (81, 'Hugo', 'Vomáčka', 14, 4, 'Harmony', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (83, 'Zdeněk', 'Janoušek', 24, 5, 'Síla chuti', 10);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (1, 'Zdeněk', 'Vomáčka', 10, 3, 'Divinis', 2);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (10, 'Karel', 'Konvalinka', 26, 5, 'Síla chuti', 2);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (30, 'Filip', 'Vyskočil', 10, 3, 'Divinis', 2);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (38, 'Erik', 'Janoušek', 10, 3, 'Divinis', 2);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (41, 'Patrik', 'Janoušek', 10, 3, 'Divinis', 2);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (47, 'Erik', 'Spěvák', 28, 6, 'Červené jablko', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (49, 'Zikmund', 'Novotný', 3, 1, 'Ostravská', 3);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (56, 'Filip', 'Vyskočil', 8, 2, 'Café Imperial', 2);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (61, 'Jan', 'Skřivánek', 13, 4, 'Harmony', 1);
INSERT INTO WRZECOND.CUSTOMER_VIEW (ID, FIRST_NAME, LAST_NAME, TABLE_ID, RESTAURANT_ID, RESTAURANT_NAME, FOOD_COUNT) VALUES (75, 'Filip', 'Spěvák', 3, 1, 'Ostravská', 3);