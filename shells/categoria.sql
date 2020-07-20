

CREATE TABLE categoria  (
  categoriaNo 	int(11) NOT NULL,
  grupo       	varchar(40) NOT NULL,
  departamento	varchar(40) NOT NULL,
  secao       	varchar(40) NOT NULL,
  PRIMARY KEY(categoriaNo) USING BTREE
);

