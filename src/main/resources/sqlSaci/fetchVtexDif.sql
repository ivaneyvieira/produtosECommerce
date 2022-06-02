DO @PRODUTO := :produto;
DO @ID_PRODUTO := IF(@PRODUTO REGEXP '^[0-9]+$', @PRODUTO * 1, 0);
DO @SKU := :sku;
DO @DEP := :departamento;
DO @ID_DEP := IF(@DEP REGEXP '^[0-9]+$', @DEP * 1, 0);
DO @CAT := :categoria;
DO @ID_CAT := IF(@CAT REGEXP '^[0-9]+$', @CAT * 1, 0);
DO @MARCA := :marca;
DO @ID_MARCA := IF(@MARCA REGEXP '^[0-9]+$', @MARCA * 1, 0);

SELECT skuId,
       idProd,
       nomeSku,
       referenciaSKU,
       idDep,
       nomeDepartamento,
       idCat,
       nomeCategoria,
       idMarca,
       nomeMarca,
       estoque,
       preco,
       precoList,
       promoprice,
       refprice,
       precoCompor,
       promoVtex,
       validadeVtex,
       validade,
       codigo,
       promono,
       precoPromoEditor
FROM produtoEcomerce.vtex
WHERE (skuId LIKE CONCAT(@SKU, '%') OR @SKU = '')
  AND (idProd = @ID_PRODUTO OR nomeSku LIKE CONCAT('%', @PRODUTO, '%') OR
       referenciaSKU LIKE CONCAT(@PRODUTO, '%') OR @PRODUTO = '')
  AND (idDep = @ID_DEP OR nomeDepartamento LIKE CONCAT('%', @DEP, '%') OR @DEP = '')
  AND (idCat = @ID_CAT OR nomeCategoria LIKE CONCAT('%', @CAT, '%') OR @CAT = '')
  AND (idMarca = @ID_MARCA OR nomeMarca LIKE CONCAT('%', @MARCA, '%') OR @MARCA = '')
  AND (promoprice > 0 OR :promocao != 'S')
  AND ((IFNULL(promoprice, 0) != IFNULL(promoVtex, 0) AND :diferenca = 'PROMO') OR
       (IFNULL(precoList, 0) != IFNULL(refprice, 0) AND :diferenca = 'LIST') OR
       (IFNULL(promoVtex, 0) != 0 AND :diferenca = 'PRICEBASE') OR
       (IFNULL(preco, 0) != IFNULL(promoprice, 0) AND :diferenca = 'BASE') OR
       (IFNULL(promoprice, 0) != IFNULL(precoPromoEditor, 0) AND
       (IFNULL(promoprice, 0) != 0 OR IFNULL(precoPromoEditor, 0) != 0) AND :diferenca = 'EDITOR') OR
       (IFNULL(validadeVtex * 1, 0) != IFNULL(validade * 1, 0) AND :diferenca = 'DATA') OR
       (
	   (
	       (
		   (IFNULL(refprice, 0) != IFNULL(preco, 0)) AND
		   ((IFNULL(promoprice, 0) = 0))
		 ) OR
	       (
		   (IFNULL(preco, 0) != IFNULL(promoprice, 0)) AND
		   ((IFNULL(promoprice, 0) != 0))
		 )
	     ) AND (:diferenca = 'PRICE')
	 )
  )
