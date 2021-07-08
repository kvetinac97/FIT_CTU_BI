create table FOOD
(
    ID    NUMBER        not null
        constraint FOOD_PK
            primary key,
    NAME  VARCHAR2(128) not null
        constraint FOOD_UK1
            unique,
    PRICE NUMBER        not null
)
/

INSERT INTO WRZECOND.FOOD (ID, NAME, PRICE) VALUES (1, 'Vývar s nudlemi a játrovým knedlíčkem', 40);
INSERT INTO WRZECOND.FOOD (ID, NAME, PRICE) VALUES (2, 'Hovězí vývar s celestýnskými nudlemi', 50);
INSERT INTO WRZECOND.FOOD (ID, NAME, PRICE) VALUES (3, 'Hráškový krém s opečeným chlebíčkem', 50);
INSERT INTO WRZECOND.FOOD (ID, NAME, PRICE) VALUES (4, 'Kapr s nivou', 200);
INSERT INTO WRZECOND.FOOD (ID, NAME, PRICE) VALUES (5, 'Rumcajsovy koule', 150);
INSERT INTO WRZECOND.FOOD (ID, NAME, PRICE) VALUES (6, 'Burger podle Pata a Mata', 160);
INSERT INTO WRZECOND.FOOD (ID, NAME, PRICE) VALUES (7, 'Zlatá česká trojkombinace', 250);
INSERT INTO WRZECOND.FOOD (ID, NAME, PRICE) VALUES (8, 'Zapékané rozmarýnové brambory', 300);
INSERT INTO WRZECOND.FOOD (ID, NAME, PRICE) VALUES (9, 'Filipínské kebaby', 179);
INSERT INTO WRZECOND.FOOD (ID, NAME, PRICE) VALUES (10, 'Steak s wasabi špenátem', 349);