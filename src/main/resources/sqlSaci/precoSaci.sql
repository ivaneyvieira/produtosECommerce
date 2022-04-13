SELECT TRIM(P.prdno) * 1                                                   AS codigo,
       TRIM(MID(I.name, 1, 37))                                            AS descricao,
       P.refprice / 100                                                    AS refprice,
       P.promo_validate                                                    as validade,
       P.promo_price / 100                                                 AS promoprice,
       IF(CURRENT_DATE * 1 <= P.promo_validate, P.promo_price, 0.00) / 100 AS preco
FROM sqldados.prp                AS P
         INNER JOIN sqldados.prd AS I
                    ON I.no = P.prdno
WHERE P.storeno = 10
GROUP BY codigo