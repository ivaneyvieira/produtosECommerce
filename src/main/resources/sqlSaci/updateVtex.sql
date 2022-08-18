UPDATE produtoEcomerce.vtex
SET promoprice       = :promoprice,
    grade            = :grade,
    descricaoSaci    = :descricaoSaci,
    refprice         = :refprice,
    precoCompor      = :precoCompor,
    codigo           = :codigo,
    validade         = :validade,
    promono          = IF(:promono = 0, NULL, :promono),
    precoPromoEditor = :precoPromoEditor
WHERE skuId = :skuId