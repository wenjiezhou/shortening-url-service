-- Sequence: shortening_url_service.url_mapping_id_seq

-- DROP SEQUENCE shortening_url_service.url_mapping_id_seq;

CREATE SEQUENCE url_mapping_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

-- Table: shortening_url_service.url_mapping

-- DROP TABLE shortening_url_service.url_mapping;

CREATE TABLE url_mapping
(
  id bigint NOT NULL,
  long_url character varying(255) NOT NULL,
  short_url character varying(255),
  CONSTRAINT url_mapping_pkey PRIMARY KEY (id),
  CONSTRAINT uk_luxssvi0fl5akdeuachjtgvgg UNIQUE (short_url)
)
WITH (
  OIDS=FALSE
);
