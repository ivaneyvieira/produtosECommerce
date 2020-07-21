

DROP TABLE IF EXISTS marca;
CREATE TABLE marca  (
  marcaNo 	int(11) NOT NULL,
  name       	varchar(40) NOT NULL,
  PRIMARY KEY(marcaNo) USING BTREE
);
