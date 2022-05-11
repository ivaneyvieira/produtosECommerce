UPDATE produtoEcomerce.vtex
SET promoprice  = :promoprice,
    refprice    = :refprice,
    precoCompor = :precoCompor,
    codigo      = :codigo
WHERE skuId = :skuId