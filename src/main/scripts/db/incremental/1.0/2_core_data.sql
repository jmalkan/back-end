-- Populate USER Table
INSERT INTO USER(FIRST_NAME, LAST_NAME, EMAIL, USERNAME, VERSION)
VALUES  ('Admin', 'Admin', 'admin@gmail.com', 'admin', 1);

INSERT INTO USER(FIRST_NAME, LAST_NAME, EMAIL, USERNAME, VERSION) 
VALUES  ('Jignesh', 'Malkan', 'jnmalkan@gmail.com', 'jmalkan', 1);

INSERT INTO USER_CREDENTIALS(ID, PASSWORD, VERSION)
SELECT U.ID, '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 1
FROM USER U
WHERE U.USERNAME = ('admin');

INSERT INTO USER_CREDENTIALS(ID, PASSWORD, VERSION)
SELECT U.ID, '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 1
FROM USER U
WHERE U.USERNAME = ('jmalkan');

-- Populate ROLE Table
INSERT INTO ROLE(NAME, DESCRIPTION, VERSION) VALUES  ('ADMIN', 'Sample Admin', 1);
INSERT INTO ROLE(NAME, DESCRIPTION, VERSION) VALUES  ('MANAGER', 'Sample Manager', 1);
INSERT INTO ROLE(NAME, DESCRIPTION, VERSION) VALUES  ('USER', 'Sample User', 1);

-- Populate PERMISSION Table
INSERT INTO PERMISSION (resource, operation, version) VALUES ('*', '*', 1);
INSERT INTO PERMISSION (resource, operation, version) VALUES ('*', 'read', 1);
INSERT INTO PERMISSION (resource, operation, version) VALUES ('*', 'create', 1);
INSERT INTO PERMISSION (resource, operation, version) VALUES ('*', 'update', 1);
INSERT INTO PERMISSION (resource, operation, version) VALUES ('*', 'delete', 1);

INSERT INTO PERMISSION (resource, operation, version) VALUES ('todos', '*', 1);
INSERT INTO PERMISSION (resource, operation, version) VALUES ('todos', 'read', 1);
INSERT INTO PERMISSION (resource, operation, version) VALUES ('todos', 'create', 1);
INSERT INTO PERMISSION (resource, operation, version) VALUES ('todos', 'update', 1);
INSERT INTO PERMISSION (resource, operation, version) VALUES ('todos', 'delete', 1);


INSERT INTO USER_ROLE
SELECT U.ID, R.ID
FROM USER U, ROLE R
WHERE U.USERNAME = ('admin') AND R.NAME = ('ADMIN');

INSERT INTO USER_ROLE
SELECT U.ID, R.ID
FROM USER U, ROLE R
WHERE U.USERNAME = ('jmalkan') AND R.NAME = ('USER');


INSERT INTO ROLE_PERMISSION 
SELECT R.ID, P.ID
from ROLE R, PERMISSION P 
WHERE R.NAME = ('ADMIN') AND (P.RESOURCE = '*' AND P.OPERATION = '*');


INSERT INTO ROLE_PERMISSION 
SELECT R.ID, P.ID
from ROLE R, PERMISSION P 
WHERE R.NAME = ('USER') AND (P.RESOURCE = 'todos' AND P.OPERATION = 'read');