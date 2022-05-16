UPDATE produtoEcomerce.vtex
SET promoprice  = :promoprice,
    refprice    = :refprice,
    precoCompor = :precoCompor,
    codigo      = :codigo,
    validade    = :validade,
    promono     = :promono
WHERE skuId = :skuId