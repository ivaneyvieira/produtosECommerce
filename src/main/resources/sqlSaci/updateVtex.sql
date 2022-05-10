UPDATE produtoEcomerce.vtex
SET promoprice  = :promoprice,
    refprice    = :refprice,
    precoCompor = :precoCompor
WHERE skuId = :skuId