UPDATE produtoEcomerce.vtex
SET preco = :preco,
    promoVtex = :promoVtex,
    validadeVtex = :validadeVtex
WHERE skuId = :skuId