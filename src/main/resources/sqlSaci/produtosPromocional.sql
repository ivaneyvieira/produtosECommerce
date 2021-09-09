DO @CL := :centroLucro;
DO @VD := :fornecedor;
DO @TP := :tipo;
DO @CD := LPAD(:codigo * 1, 16, ' ');
DO @VENCIMENTO := 0;
DO @TIPO_PAINEL := :tipoPainel;
DO @DT := CAST(CURRENT_DATE * 1 AS UNSIGNED);

DROP TEMPORARY TABLE IF EXISTS T_PRDNO_VALIDADE;
CREATE TEMPORARY TABLE T_PRDNO_VALIDADE (
  PRIMARY KEY (prdno)
)
SELECT prdno,
       MAX(promono)   AS promono,
       MAX(P.enddate) AS promoDate,
       MAX(I.price)   AS preco
FROM sqldados.promo            AS P
  INNER JOIN sqldados.promoprd AS I
	       ON P.no = I.promono
WHERE P.enddate >= @DT
  AND P.begdate <= @DT
GROUP BY prdno;

DROP TEMPORARY TABLE IF EXISTS T_PROMO;
CREATE TEMPORARY TABLE T_PROMO (
  PRIMARY KEY (prdno)
)
SELECT prp.prdno                                                 AS prdno,
       prd.name                                                  AS name,
       vend.no                                                   AS vendno,
       vend.sname                                                AS abrev,
       prd.typeno,
       prd.clno,
       prp.refprice,
       (prp.refprice - prp.promo_price) * 100 / prp.refprice     AS perc,
       prp.promo_price,
       prp.promo_validate,
       IF(V.prdno IS NULL, 'N', 'S')                             AS promocaoWeb,
       IF(prp.promo_validate >= @DT, 'S', 'N')                   AS promocaoSaci,
       IFNULL(V.preco, 0)                                        AS precoPromoWeb,
       IF(prp.promo_price = IFNULL(V.preco, 0), 'S', 'N')        AS precoAlinhado,
       V.promoDate                                               AS promoDateWeb,
       IF(prp.promo_validate = IFNULL(V.promoDate, 0), 'S', 'N') AS dataAlinhada,
       IFNULL(V.promono, 0)                                      AS promono
FROM sqldados.prd
  INNER JOIN sqldados.prp
	       ON (prd.no = prp.prdno AND prp.storeno = 10)
  LEFT JOIN  T_PRDNO_VALIDADE AS V
	       ON V.prdno = prp.prdno
  INNER JOIN sqldados.vend
	       ON (prd.mfno = vend.no)
WHERE (prd.groupno = @CL OR prd.deptno = @CL OR prd.clno = @CL OR @CL = 0)
  AND (prd.mfno = @VD OR @VD = 0)
  AND (prd.typeno = @TP OR @TP = 0)
  AND (prd.no LIKE @CD OR @CD * 1 = 0)
  AND (prp.promo_validate = @VENCIMENTO OR @VENCIMENTO = 0)
  AND (prp.prdno IN (:codigos));


DROP TEMPORARY TABLE IF EXISTS T_STK;
CREATE TEMPORARY TABLE T_STK (
  PRIMARY KEY (prdno)
)
SELECT prdno, SUM(qtty_varejo + qtty_atacado) AS qt
FROM sqldados.stk
  INNER JOIN sqldados.store
	       ON store.no = storeno
  INNER JOIN T_PROMO
	       USING (prdno)
GROUP BY prdno;

DROP TEMPORARY TABLE IF EXISTS T_RESULT;
CREATE TEMPORARY TABLE T_RESULT
SELECT CAST(LPAD(prdno * 1, 6, '0') AS CHAR)                            AS codigo,
       TRIM(MID(name, 1, 37))                                           AS descricao,
       CAST(IF(promo_validate = 0, NULL, promo_validate) AS date)       AS validade,
       CAST(promoDateWeb AS DATE)                                       AS promoDateWeb,
       refprice / 100                                                   AS precoRef,
       ROUND(perc, 2)                                                   AS perc,
       promo_price / 100                                                AS precoPromo,
       precoPromoWeb / 100                                              AS precoPromoWeb,
       vendno                                                           AS vendno,
       abrev                                                            AS abrev,
       typeno                                                           AS tipo,
       clno                                                             AS centLucro,
       qt / 1000                                                        AS saldo,
       promono                                                          AS promono,
       CAST(IF(promocaoWeb = 'S', CONCAT('WEB ', promono), '') AS CHAR) AS web,
       promocaoWeb,
       promocaoSaci,
       precoAlinhado,
       dataAlinhada
FROM T_PROMO
  INNER JOIN T_STK
	       USING (prdno)
WHERE CASE @TIPO_PAINEL
	WHEN 'BASE'
	  THEN TRUE
	WHEN 'SACI'
	  THEN promocaoWeb = 'N' AND promocaoSaci = 'S'
	WHEN 'WEB'
	  THEN promocaoWeb = 'S' AND promocaoSaci = 'S'
	WHEN 'INVALIDO'
	  THEN (promocaoWeb = 'S' AND promocaoSaci = 'N')
	  OR ((promocaoWeb = 'S' AND promocaoSaci = 'S')
	    AND (precoAlinhado = 'N' OR dataAlinhada = 'N'))
	ELSE FALSE
      END

