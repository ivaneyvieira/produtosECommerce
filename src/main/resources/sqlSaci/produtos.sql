SELECT codigo,
       grade,
       gradeCompleta,
       barcode,
       P.descricao,
       vendno,
       fornecedor,
       typeno,
       typeName,
       IFNULL(clno, '')                                                AS clno,
       clname,
       marca,
       categoria,
       descricaoCompleta,
       bitola,
       imagem,
       peso,
       altura,
       comprimento,
       largura,
       textLink,
       especificacoes,
       precoCheio,
       IFNULL(ncm, '')                                                 AS ncm,
       cor,
       'simples'                                                       AS variacao,
       IFNULL(IF(corStr = '' || corStr = NULL, codigoCor, corStr), '') AS corStr,
       dataHoraMudanca,
       userno
FROM produtoEcomerce.produto         AS P
  LEFT JOIN produtoEcomerce.gradeCor AS G
	      ON P.grade = G.descricao
WHERE (codigo = :codigo OR :codigo = 0)
  AND P.descricao BETWEEN RPAD(:descricaoI, 37, ' ') AND RPAD(:descricaoF, 37, 'Z')
  AND (vendno = :vendno OR :vendno = 0)
  AND (typeno = :typeno OR :typeno = 0)
  AND (clno BETWEEN :clno1 AND :clno2 OR :clno1 = 0)
  AND (editado = :editado OR :editado = -1)
  AND (categoria BETWEEN :categoria1 AND :categoria2 OR :categoria1 = 0)
