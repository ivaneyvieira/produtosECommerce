SELECT D.storeno,
       D.prdno,
       D.refprice / 100 AS price,
       IFNULL(P.gtin, '') AS gtin
FROM sqldados.prp         AS D
  LEFT JOIN sqldados.prd2 AS P
	      ON P.prdno = D.prdno
WHERE D.storeno = 10
  AND D.prdno = :prdno