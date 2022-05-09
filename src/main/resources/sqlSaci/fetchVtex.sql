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
       nomeMarca
FROM produtoEcomerce.vtex
WHERE (referenciaSKU = @SKU OR @SKU = '')
  AND (idProd = @ID_PRODUTO OR @ID_PRODUTO = 0 OR nomeSku = @PRODUTO OR @PRODUTO = '')
  AND (idDep = @ID_DEP OR @ID_DEP = 0 OR nomeDepartamento = @DEP OR @DEP = '')
  AND (idCat = @ID_CAT OR @ID_CAT = 0 OR nomeCategoria = @CAT OR @CAT = '')
  AND (idMarca = @ID_MARCA OR @ID_MARCA = 0 OR nomeMarca = @MARCA OR @MARCA = '')


