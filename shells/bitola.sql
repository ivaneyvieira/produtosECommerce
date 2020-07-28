DROP TABLE IF EXISTS bitola;
CREATE TABLE bitola (
  bitolaNo INT(11)     NOT NULL,
  name     VARCHAR(40) NOT NULL,
  PRIMARY KEY (bitolaNo) USING BTREE
);
