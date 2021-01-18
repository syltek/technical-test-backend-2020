CREATE TABLE wallet
(
    id       SERIAL PRIMARY KEY,
    balance  FLOAT        NOT NULL,
    currency varchar(200) NOT NULL
);