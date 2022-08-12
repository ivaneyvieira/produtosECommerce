USE sqldados;

DO @DT := CURRENT_DATE * 1;

DROP TEMPORARY TABLE IF EXISTS T_PRDNO_VALIDADE;
CREATE TEMPORARY TABLE T_PRDNO_VALIDADE (
  PRIMARY KEY (prdno)
)
SELECT prdno,
       type,
       P.auxLong1 / 100 AS perce,
       MAX(promono)     AS promono
FROM sqldados.promo            AS P
  INNER JOIN sqldados.promoprd AS I
	       ON P.no = I.promono
WHERE P.enddate >= @DT
  AND P.begdate <= @DT
GROUP BY prdno;

DROP TEMPORARY TABLE IF EXISTS T_PRDNO_VALIDADE_PRODUTO;
CREATE TEMPORARY TABLE T_PRDNO_VALIDADE_PRODUTO (
  PRIMARY KEY (prdno, grade)
)
SELECT promono,
       prdno,
       grade,
       type,
       perce,
       price / 100 AS price
FROM sqldados.promoprd        AS I
  INNER JOIN T_PRDNO_VALIDADE AS V
	       USING (prdno, promono)
GROUP BY promono, prdno, grade;

SELECT TRIM(P.prdno) * 1                                                   AS codigo,
       IFNULL(grade, '')                                                   AS grade,
       TRIM(MID(I.name, 1, 37))                                            AS descricao,
       P.refprice / 100                                                    AS refprice,
       P.promo_validate                                                    AS validade,
       P.promo_price / 100                                                 AS promoprice,
       P.l8 / 100                                                          AS precoCompor,
       IF(CURRENT_DATE * 1 <= P.promo_validate, P.promo_price, NULL) / 100 AS preco,
       V.promono                                                           AS promono,
       CASE
	 WHEN type = 0
	   THEN (P.refprice / 100) * (1 - perce / 100)
	 WHEN type = 3
	   THEN V.price / 100
	 ELSE 0.00
       END                                                                 AS precoPromoEditor
FROM sqldados.prp                     AS P
  INNER JOIN sqldados.prd             AS I
	       ON I.no = P.prdno
  LEFT JOIN  T_PRDNO_VALIDADE_PRODUTO AS V
	       ON P.prdno = V.prdno
WHERE P.storeno = 10
GROUP BY codigo, grade