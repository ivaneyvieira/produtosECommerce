DO @PRDNO := LPAD(:prdno, 16, ' ');
DO @GRADE := :grade;
DO @PROMO := :promo;

DROP TEMPORARY TABLE IF EXISTS T_PROMO;
CREATE TEMPORARY TABLE T_PROMO
SELECT @PROMO         AS promono,
       prdno,
       @GRADE         AS grade,
       promo_price    AS price,
       promo_validate AS validade
FROM sqldados.prp p
WHERE storeno = 10
  AND prdno = @PRDNO;

INSERT IGNORE INTO sqldados.promoprd(price, auxLong1, auxLong2, auxLong3, auxLong4, auxLong5,
				     auxLong6, auxLong7, auxLong8, auxMy1, auxMy2, auxMy3, auxMy4,
				     promono, prazomedio, bits, padbyte, auxShort1, auxShort2,
				     auxShort3, auxShort4, auxShort5, auxShort6, auxShort7, auxShort8,
				     prdno, grade, auxString)
SELECT price,
       0,
       0,
       0,
       0,
       0,
       0,
       0,
       0,
       0,
       0,
       0,
       0,
       promono,
       0,
       0,
       0,
       0,
       0,
       0,
       0,
       0,
       0,
       0,
       0,
       prdno,
       grade,
       ''
FROM T_PROMO
WHERE validade >= CURRENT_DATE;

UPDATE sqldados.promoprd AS P INNER JOIN T_PROMO AS T USING (promono, prdno, grade)
SET P.price = T.price
WHERE validade >= CURRENT_DATE;

DELETE
FROM sqldados.promoprd
WHERE promono = @PROMO
  AND prdno = @PRDNO
  AND grade = @GRADE
  AND EXISTS(SELECT *
	     FROM T_PROMO TP
	     WHERE validade < CURRENT_DATE);