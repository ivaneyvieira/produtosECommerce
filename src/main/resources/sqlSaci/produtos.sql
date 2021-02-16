SELECT codigo,
       grade,
       gradeCompleta,
       barcode,
       descricao,
       vendno,
       fornecedor,
       typeno,
       typeName,
       clno,
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
       ncm,
       cor,
       'simples' AS variacao,
       dataHoraMudanca
FROM produtoEcomerce.produto
WHERE (codigo = :codigo OR :codigo = 0)
  AND descricao BETWEEN RPAD(:descricaoI, 37, ' ') AND RPAD(:descricaoF, 37, 'Z')
  AND (vendno = :vendno OR :vendno = 0)
  AND (typeno = :typeno OR :typeno = 0)
  AND (clno BETWEEN :clno1 AND :clno2 OR :clno1 = 0)
  AND (editado = :editado OR :editado = -1)
  AND (categoria BETWEEN :categoria1 AND :categoria2 OR :categoria1 = 0)
