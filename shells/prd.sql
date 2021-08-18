SELECT CAST(LPAD(TRIM(P.no), 6, '0') AS CHAR) AS codigo,
       IFNULL(B.grade, '')                    AS grade,
       TRIM(IFNULL(B.barcode, P.barcode))     AS barcode,
       TRIM(MID(P.name, 1, 37))               AS descricao,
       P.mfno                                 AS vendno,
       F.auxChar1                             AS fornecedor,
       P.typeno,
       T.name                                 AS typeName,
       CAST(LPAD(P.clno, 6, '0') AS CHAR)     AS clno,
       cl.name                                AS clname,
       0                                      AS marca,
       0                                      AS categoria,
       ''                                     AS descricaoCompleta,
       0                                      AS bitola,
       ''                                     AS imagem,
       0                                      AS peso,
       P.m6                                   AS altura,
       P.m4                                   AS comprimento,
       P.m5                                   AS largura,
       0                                      AS editado,
       ''                                     AS textLink,
       ''                                     AS especificacoes,
       P.sp / 100                             AS precoCheio,
       S.ncm,
       ''                                     AS gradeCompleta,
       ''                                     AS cor,
       CURRENT_TIMESTAMP                      AS dataHoraMudanca
FROM sqldados.prd             AS P
  LEFT JOIN sqldados.prdbar  AS B
               ON P.no = B.prdno
  LEFT JOIN  sqldados.vend    AS F
               ON F.no = P.mfno
  LEFT JOIN  sqldados.type    AS T
               ON T.no = P.typeno
  LEFT JOIN  sqldados.cl
               ON cl.no = P.clno
  LEFT JOIN  sqldados.spedprd AS S
               ON P.no = S.prdno
WHERE P.no * 1 > 0
GROUP BY codigo, grade
