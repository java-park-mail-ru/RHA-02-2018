-- DROP TABLE IF EXISTS "users";
-- DROP TABLE IF EXISTS "SPRING_SESSION";
-- DROP TABLE IF EXISTS "SPRING_SESSION_ATTRIBUTES";
-- DROP INDEX IF EXISTS SPRING_SESSION_IX1;
-- DROP INDEX IF EXISTS SPRING_SESSION_IX2;
-- DROP INDEX IF EXISTS SPRING_SESSION_IX3;
-- DROP INDEX IF EXISTS SPRING_SESSION_ATTRIBUTES_IX1;
CREATE EXTENSION IF NOT EXISTS citext;

CREATE TABLE IF NOT EXISTS "users" (
  id SERIAL NOT NULL PRIMARY KEY ,
  username CITEXT NOT NULL UNIQUE ,
  email CITEXT NOT NULL UNIQUE ,
  password TEXT NOT NULL,
  rating INTEGER DEFAULT 0,
  avatar TEXT DEFAULT 'def.png'
);

CREATE TABLE IF NOT EXISTS SPRING_SESSION (
  PRIMARY_ID CHAR(36) NOT NULL,
  SESSION_ID CHAR(36) NOT NULL,
  CREATION_TIME BIGINT NOT NULL,
  LAST_ACCESS_TIME BIGINT NOT NULL,
  MAX_INACTIVE_INTERVAL INT NOT NULL,
  EXPIRY_TIME BIGINT NOT NULL,
  PRINCIPAL_NAME VARCHAR(100),
  CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
);


CREATE UNIQUE INDEX IF NOT EXISTS SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX IF NOT EXISTS SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX IF NOT EXISTS SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE IF NOT EXISTS SPRING_SESSION_ATTRIBUTES (
  SESSION_PRIMARY_ID CHAR(36) NOT NULL,
  ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
  ATTRIBUTE_BYTES BYTEA NOT NULL,
  CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
  CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
);


CREATE INDEX IF NOT EXISTS SPRING_SESSION_ATTRIBUTES_IX1 ON SPRING_SESSION_ATTRIBUTES (SESSION_PRIMARY_ID);