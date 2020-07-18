DROP TABLE IF EXISTS produtoEcomerce.categoria;

CREATE TABLE produtoEcomerce.categoria (
  categoriaNo  INT         NOT NULL,
  grupo        VARCHAR(40) NOT NULL,
  departamento VARCHAR(40) NOT NULL,
  secao        VARCHAR(40) NOT NULL,
  PRIMARY KEY (categoriaNo)
);