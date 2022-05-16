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

SELECT *
FROM vtex;

ALTER TABLE vtex
  ADD COLUMN estoque INT DEFAULT 0;

ALTER TABLE vtex
  ADD COLUMN preco decimal(13, 2) DEFAULT 0.00;


ALTER TABLE vtex
  ADD COLUMN promoprice INT DEFAULT 0;

ALTER TABLE vtex
  ADD COLUMN refprice decimal(13, 2) DEFAULT 0.00;

ALTER TABLE vtex
  ADD COLUMN precoCompor INT DEFAULT 0;

ALTER TABLE vtex
  ADD COLUMN codigo VARCHAR(16) DEFAULT '';

ALTER TABLE vtex
  ADD COLUMN validade DATE;



ALTER TABLE vtex
  ADD COLUMN promoVtex INT DEFAULT 0;

ALTER TABLE vtex
  ADD COLUMN validadeVtex DATE;


ALTER TABLE vtex
  MODIFY promoVtex DECIMAL(13, 2) DEFAULT 0.00 NULL;


ALTER TABLE vtex
  MODIFY promoprice DECIMAL(13, 2) DEFAULT 0.00 NULL;


ALTER TABLE vtex
  MODIFY precoCompor DECIMAL(13, 2) DEFAULT 0.00 NULL;

ALTER TABLE vtex
  ADD promono INT NULL;
