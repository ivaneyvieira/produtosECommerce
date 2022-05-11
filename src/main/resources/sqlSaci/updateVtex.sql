UPDATE produtoEcomerce.vtex
SET promoprice  = :promoprice,
    refprice    = :refprice,
    precoCompor = :precoCompor,
    codigo      = :codigo,
    validade    = :validade
WHERE skuId = :skuId