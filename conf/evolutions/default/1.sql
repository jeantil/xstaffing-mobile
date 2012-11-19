# Users schema

# --- !Ups

CREATE TABLE Locations (
    userId varchar(255) NOT NULL,
    clientName varchar(255) NOT NULL,
    latitude varchar(255) NOT NULL,
    longitude varchar(255) NOT NULL,
    PRIMARY KEY (userId,clientName)
);

# --- !Downs

DROP TABLE Locations;