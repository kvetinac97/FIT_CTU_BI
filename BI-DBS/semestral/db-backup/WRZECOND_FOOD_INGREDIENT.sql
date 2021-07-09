create table FOOD_INGREDIENT
(
    INGREDIENT_COUNT NUMBER      not null,
    UNIT             VARCHAR2(8) not null,
    INGREDIENT_ID    NUMBER      not null
        constraint FOOD_INGREDIENT_FK1
            references INGREDIENT,
    FOOD_ID          NUMBER      not null
        constraint FOOD_INGREDIENT_FK2
            references FOOD,
    constraint FOOD_INGREDIENT_PK
        primary key (INGREDIENT_ID, FOOD_ID)
)
/

INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (200, 'ml', 1, 1);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (50, 'g', 2, 1);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (30, 'g', 3, 1);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (5, 'kg', 4, 1);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (200, 'ml', 1, 2);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (80, 'g', 5, 2);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (200, 'ml', 1, 3);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (80, 'g', 6, 3);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (30, 'g', 7, 3);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (150, 'g', 8, 4);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (50, 'g', 9, 4);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (5, 'kg', 4, 4);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (80, 'g', 11, 5);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (150, 'ml', 10, 5);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (2, 'ks', 12, 6);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (10, 'dkg', 13, 6);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (10, 'dkg', 14, 6);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (25, 'g', 15, 6);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (79, 'g', 16, 6);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (50, 'g', 9, 7);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (5, 'kg', 4, 7);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (150, 'g', 17, 7);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (200, 'g', 18, 8);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (10, 'dkg', 13, 8);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (7, 'dkg', 14, 8);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (15, 'g', 19, 8);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (80, 'g', 20, 9);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (50, 'g', 16, 9);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (2, 'ks', 15, 9);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (80, 'g', 21, 10);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (20, 'g', 22, 10);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (120, 'g', 23, 10);
INSERT INTO WRZECOND.FOOD_INGREDIENT (INGREDIENT_COUNT, UNIT, INGREDIENT_ID, FOOD_ID) VALUES (100, 'g', 18, 10);