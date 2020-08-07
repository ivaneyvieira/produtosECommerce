SELECT storeno,
       prdno,
       refprice / 100 AS price
FROM sqldados.prp
WHERE storeno = 10
  AND prdno = :prdno