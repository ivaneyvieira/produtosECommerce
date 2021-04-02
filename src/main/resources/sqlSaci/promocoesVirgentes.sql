SELECT no                    AS promoNo,
       name                  AS descricao,
       CAST(enddate AS date) AS vencimento
FROM sqldados.promo
WHERE enddate >= CURRENT_DATE * 1
  AND type = 3
ORDER BY enddate, no