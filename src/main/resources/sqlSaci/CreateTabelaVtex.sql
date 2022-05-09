USE produtoEcomerce;

DROP TABLE IF EXISTS vtex;

CREATE TABLE vtex (
  skuId            INT,
  idProd           INT,
  nomeSku          varchar(100),
  referenciaSKU    varchar(20),
  idDep            INT,
  nomeDepartamento varchar(100),
  idCat            INT,
  nomeCategoria    varchar(100),
  idMarca          INT,
  nomeMarca        varchar(100)
);

SELECT * FROM vtex;


