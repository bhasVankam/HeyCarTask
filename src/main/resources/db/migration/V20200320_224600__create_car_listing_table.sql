drop table car_listings if exists;

CREATE TABLE car_listings
  (
     id         uuid default random_uuid(),
     code       VARCHAR(255) NOT NULL,
     make       VARCHAR(255) NOT NULL,
     model      VARCHAR(255) NOT NULL,
     year       INTEGER,
     power_in_kw INTEGER,
     color      VARCHAR(30),
     price      DECIMAL NOT NULL,
     created_by  VARCHAR(255) NOT NULL ,
     created_on  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
     modified_on TIMESTAMP WITHOUT TIME ZONE,
     PRIMARY KEY (id)
  );

ALTER TABLE car_listings
  ADD CONSTRAINT code_and_createdBy_unique UNIQUE(code, created_by);


ALTER TABLE car_listings ALTER COLUMN created_on SET DEFAULT CURRENT_TIMESTAMP;
