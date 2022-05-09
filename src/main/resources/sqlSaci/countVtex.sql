DO @PRODUTO := :produto;
DO @ID_PRODUTO := IF(@PRODUTO REGEXP '^[0-9]+$', @PRODUTO * 1, 0);
DO @SKU := :sku;
DO @DEP := :departamento;
DO @ID_DEP := IF(@DEP REGEXP '^[0-9]+$', @DEP * 1, 0);
DO @CAT := :categoria;
DO @ID_CAT := IF(@CAT REGEXP '^[0-9]+$', @CAT * 1, 0);
DO @MARCA := :marca;
DO @ID_MARCA := IF(@MARCA REGEXP '^[0-9]+$', @MARCA * 1, 0);

SELECT COUNT(*)
FROM produtoEcomerce.vtex
WHERE (skuId LIKE CONCAT(@SKU, '%') OR @SKU = '')
  AND (idProd = @ID_PRODUTO OR nomeSku LIKE CONCAT('%', @PRODUTO, '%') OR
       referenciaSKU LIKE CONCAT(@PRODUTO, '%') OR @PRODUTO = '')
  AND (idDep = @ID_DEP OR nomeDepartamento LIKE CONCAT('%', @DEP, '%') OR @DEP = '')
  AND (idCat = @ID_CAT OR nomeCategoria LIKE CONCAT('%', @CAT, '%') OR @CAT = '')
  AND (idMarca = @ID_MARCA OR nomeMarca LIKE CONCAT('%', @MARCA, '%') OR @MARCA = '')


