DO @PRDNO := CAST(LPAD(:codigo, 16, ' ') AS CHAR);
DO @NUM_PROMO := :promono;

DELETE
FROM sqldados.promoprd
WHERE promono = :promono
  AND prdno = @PRDNO