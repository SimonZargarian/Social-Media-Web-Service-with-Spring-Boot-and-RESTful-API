/*
 * When the application is launched this file with the name data.sql gets called and 
 * the data in this file is used to initialize the database, this is a Spring Boot 
 * auto configuration feature.
 */

INSERT INTO USER VALUES(1001, sysdate(), 'John');
INSERT INTO USER VALUES(1002, sysdate(), 'Jill');
INSERT INTO USER VALUES(1003, sysdate(), 'Clark');
INSERT INTO POST VALUES(1101, 'My First Post', 1001);
INSERT INTO POST VALUES(1102, 'My Second Post', 1001);