USE produtoEcomerce;

CREATE TEMPORARY TABLE T
SELECT :skuId AS skuId;


REPLACE produtoEcomerce.vtex(skuId, idProd, nomeSku, referenciaSKU, idDep, nomeDepartamento, idCat,
			     nomeCategoria, idMarca, nomeMarca, ativarSku, estoque, preco,
			     promoprice, refprice, precoCompor, codigo, validade, promoVtex,
			     validadeVtex, promono)
SELECT :skuId,
       :idProd,
       MID(:nomeSku, 1, 100) AS nomeSku,
       :referenciaSKU,
       :idDep,
       :nomeDepartamento,
       :idCat,
       :nomeCategoria,
       :idMarca,
       :nomeMarca,
       :ativarSku,
       estoque,
       preco,
       promoprice,
       refprice,
       precoCompor,
       codigo,
       validade,
       promoVtex,
       validadeVtex,
       promono
FROM T
  LEFT JOIN produtoEcomerce.vtex
	      USING (skuId)
