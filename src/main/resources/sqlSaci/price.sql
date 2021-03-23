SELECT CAST(LPAD(D.prdno * 1, 6, '0') AS CHAR) AS codigo,
       D.refprice / 100                        AS price,
       IFNULL(P.gtin, '')                      AS gtin,
       R.mfno_ref                              AS prdRef
FROM sqldados.prp         AS D
  LEFT JOIN sqldados.prd2 AS P
	      ON P.prdno = D.prdno
  LEFT JOIN sqldados.prd  AS R
	      ON R.no = D.prdno
WHERE D.storeno = 10