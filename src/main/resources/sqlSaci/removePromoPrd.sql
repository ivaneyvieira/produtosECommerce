DO @PRDNO := CAST(LPAD(:prdno, 16, ' ') AS CHAR);
DO @GRADE := :grade;
DO @HOJE := CAST(CURRENT_DATE * 1 AS UNSIGNED);

DROP TEMPORARY TABLE IF EXISTS T_PROMO;
CREATE TEMPORARY TABLE T_PROMO
SELECT no AS promono
FROM sqldados.promo
WHERE enddate >= @HOJE;

DELETE
FROM sqldados.promoprd
WHERE promono IN (SELECT promono
		  FROM T_PROMO)
  AND prdno = @PRDNO
  AND grade = @GRADE
