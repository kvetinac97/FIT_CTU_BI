create table TTABLE
(
    ID            NUMBER not null,
    CAPACITY      NUMBER not null,
    TYPE          NUMBER not null,
    RESTAURANT_ID NUMBER not null
        constraint TTABLE_FK2
            references RESTAURANT,
    WAITER_ID     NUMBER not null
        constraint TTABLE_FK1
            references EMPLOYEE,
    constraint TTABLE_PK
        primary key (ID, RESTAURANT_ID)
)
/

INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (1, 2, 0, 1, 8);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (2, 6, 1, 1, 2);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (3, 4, 0, 1, 6);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (4, 9, 1, 1, 5);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (5, 5, 1, 1, 1);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (6, 7, 1, 2, 13);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (7, 6, 0, 2, 9);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (8, 1, 1, 2, 10);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (9, 1, 0, 3, 18);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (10, 7, 0, 3, 24);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (11, 1, 0, 3, 19);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (12, 3, 1, 3, 22);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (13, 8, 1, 4, 25);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (14, 2, 1, 4, 25);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (15, 9, 1, 4, 25);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (16, 4, 0, 4, 26);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (17, 10, 1, 4, 27);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (18, 2, 1, 4, 29);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (19, 5, 0, 5, 30);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (20, 3, 1, 5, 30);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (21, 11, 0, 5, 30);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (22, 10, 0, 5, 30);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (23, 11, 1, 5, 30);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (24, 11, 1, 5, 30);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (25, 1, 1, 5, 30);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (26, 4, 1, 5, 30);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (27, 1, 1, 6, 32);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (28, 5, 0, 6, 33);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (29, 10, 1, 6, 31);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (30, 4, 0, 6, 31);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (31, 7, 0, 6, 36);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (32, 7, 0, 6, 32);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (33, 3, 0, 6, 34);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (34, 4, 0, 7, 41);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (35, 11, 1, 7, 41);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (36, 7, 0, 7, 41);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (37, 5, 1, 8, 49);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (38, 5, 0, 8, 44);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (39, 11, 0, 8, 44);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (40, 8, 1, 8, 44);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (41, 4, 1, 8, 47);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (42, 6, 1, 8, 50);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (43, 3, 0, 8, 44);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (44, 10, 1, 9, 57);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (45, 8, 1, 9, 53);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (46, 6, 1, 9, 52);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (47, 10, 1, 9, 53);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (48, 1, 1, 9, 52);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (49, 3, 0, 10, 60);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (50, 11, 1, 10, 60);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (51, 7, 0, 10, 58);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (52, 4, 1, 10, 61);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (53, 5, 0, 11, 63);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (54, 9, 0, 11, 63);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (55, 8, 1, 11, 63);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (56, 12, 1, 12, 64);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (57, 10, 1, 12, 64);
INSERT INTO WRZECOND.TTABLE (ID, CAPACITY, TYPE, RESTAURANT_ID, WAITER_ID) VALUES (58, 15, 1, 12, 64);