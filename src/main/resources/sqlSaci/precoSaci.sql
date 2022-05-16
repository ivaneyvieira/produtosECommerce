DO
@DT := current_date*1;

DROP
TEMPORARY TABLE IF EXISTS T_PRDNO_VALIDADE;
CREATE
TEMPORARY TABLE T_PRDNO_VALIDADE (
  PRIMARY KEY (prdno)
)
SELECT prdno,
       MAX(promono) AS promono
FROM sqldados.promo                   AS P
         INNER JOIN sqldados.promoprd AS I
                    ON P.no = I.promono
WHERE P.enddate >= @DT AND
      P.begdate <= @DT
GROUP BY prdno;

SELECT TRIM(P.prdno) * 1                                                   AS codigo,
       TRIM(MID(I.name, 1, 37))                                            AS descricao,
       P.refprice / 100                                                    AS refprice,
       P.promo_validate                                                    as validade,
       P.promo_price / 100                                                 AS promoprice,
       P.l8 / 100                                                          AS precoCompor,
       IF(CURRENT_DATE * 1 <= P.promo_validate, P.promo_price, null) / 100 AS preco,
       V.promono                                                           AS promono
FROM sqldados.prp                    AS P
         INNER JOIN sqldados.prd     AS I
                    ON I.no = P.prdno
         LEFT JOIN  T_PRDNO_VALIDADE AS V
                    ON P.prdno = V.prdno
WHERE P.storeno = 10
GROUP BY codigo