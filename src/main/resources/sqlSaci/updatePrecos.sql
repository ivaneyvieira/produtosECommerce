UPDATE produtoEcomerce.vtex
SET preco = :preco
WHERE skuId = :skuId
  AND :tipo = 'BASE';

UPDATE produtoEcomerce.vtex
SET precoList = :preco
WHERE skuId = :skuId
  AND :tipo = 'LIST'