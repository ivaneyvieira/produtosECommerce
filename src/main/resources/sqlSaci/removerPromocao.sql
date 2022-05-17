DO @PRDNO := CAST(LPAD(:codigo, 16, ' ') AS CHAR);

DELETE
FROM sqldados.promoprd
WHERE promono = :promono
  AND prdno = @PRDNO