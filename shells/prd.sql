SELECT cast(LPAD(TRIM(P.no), 6, '0') AS CHAR) AS codigo,
       IFNULL(V.val, '')                      AS grade,
       TRIM(IFNULL(B.barcode, P.barcode))     AS barcode,
       TRIM(MID(P.name, 1, 37))               AS descricao,
       P.mfno                                 AS vendno,
       F.name                                 AS fornecedor,
       P.typeno,
       T.name                                 AS typeName,
       P.clno,
       cl.name                                AS clname
FROM sqldados.prd           AS P
  LEFT JOIN sqldados.grade  AS G
	      ON G.no = MID(P.grade_l, 1, 10) * 1
  LEFT JOIN sqldados.grdval AS V
	      ON V.gradeno = G.no AND (V.bits & POW(2, 0)) = 0
  LEFT JOIN sqldados.prdbar AS B
	      ON P.no = B.prdno AND V.val = B.grade
  LEFT JOIN sqldados.vend   AS F
	      ON F.no = P.mfno
  LEFT JOIN sqldados.type   AS T
	      ON T.no = P.typeno
  LEFT JOIN sqldados.cl
	      ON cl.no = P.clno
GROUP BY codigo, grade