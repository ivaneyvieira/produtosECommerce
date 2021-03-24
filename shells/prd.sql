SELECT cast(LPAD(TRIM(P.no), 6, '0') AS CHAR) AS codigo,
       IFNULL(V.val, '')                      AS grade,
       TRIM(IFNULL(B.barcode, P.barcode))     AS barcode,
       TRIM(MID(P.name, 1, 37))               AS descricao,
       P.mfno                                 AS vendno,
       F.name                                 AS fornecedor,
       P.typeno,
       T.name                                 AS typeName,
       CAST(LPAD(P.clno, 6, '0') AS CHAR)     AS clno,
       cl.name                                AS clname,
       0 as marca, 0 as categoria, '' AS descricaoCompleta, 0 as bitola, '' as imagem, 0 as peso, 0 as altura, 0 as comprimento, 
  0 as largura, 0 as editado, '' as textLink, '' as especificacoes, P.sp/100 as precoCheio, S.ncm, '' AS gradeCompleta, '' as cor, current_timestamp as dataHoraMudanca 
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
  LEFT JOIN sqldados.spedprd AS S
    ON P.no = S.prdno
GROUP BY codigo, grade;
