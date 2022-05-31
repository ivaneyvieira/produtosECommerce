UPDATE produtoEcomerce.vtex
SET preco     = IF(:preco IS NULL, preco, :preco),
    precoList = IF(:precoList IS NULL, precoList, :precoList)
WHERE skuId = :skuId