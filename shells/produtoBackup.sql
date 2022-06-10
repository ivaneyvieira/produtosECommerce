SELECT current_date * 1                       as data,
       P.no                                   as prdno,
       CAST(LPAD(TRIM(P.no), 6, '0') AS CHAR) AS codigo,
       TRIM(IFNULL(B.barcode, P.barcode))     AS barcode,
       TRIM(MID(P.name, 1, 37))               AS descricao,
       IFNULL(V.val, '')                      AS grade,
       TRIM(MID(P.name, 37, 3))               AS unid,
       P.mfno                                 AS vendno,
       P.typeno                               AS typeno,
       CAST(LPAD(P.clno, 6, '0') AS CHAR)     AS clno,
       R.refprice / 100                       AS precoRef,
       S.ncm                                  AS ncm
FROM sqldados.prd AS P
         left join sqldados.prp AS R
                   ON P.no = R.prdno AND R.storeno = 10
         LEFT JOIN sqldados.grade AS G
                   ON G.no = MID(P.grade_l, 1, 10) * 1
         LEFT JOIN sqldados.grdval AS V
                   ON V.gradeno = G.no AND (V.bits & POW(2, 0)) = 0
         LEFT JOIN sqldados.prdbar AS B
                   ON P.no = B.prdno AND V.val = B.grade
         LEFT JOIN sqldados.prdloc AS L
                   ON P.no = L.prdno AND L.storeno = 4
         LEFT JOIN sqldados.spedprd AS S
                   ON P.no = S.prdno
GROUP BY codigo, grade;
