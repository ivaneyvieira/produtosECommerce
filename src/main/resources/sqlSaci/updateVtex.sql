UPDATE produtoEcomerce.vtex
SET promoprice  = :promoprice,
    refprice    = :refprice,
    precoCompor = :precoCompor,
    codigo      = :codigo,
    validade    = :validade,
    promono     = IF(:promono = 0, NULL, :promono)
WHERE skuId = :skuId