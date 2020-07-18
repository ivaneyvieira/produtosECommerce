SELECT codigo,
       grade,
       barcode,
       descricao,
       vendno,
       fornecedor,
       typeno,
       typeName,
       clno,
       clname,
       categoria,
       descricaoCompleta,
       bitola,
       imagem,
       peso,
       altura,
       comprimento,
       largura
FROM produtoEcomerce.produto
WHERE (codigo = :codigo OR :codigo = 0)
  AND descricao BETWEEN RPAD(:descricaoI, 'A', 37) AND RPAD(:descricaoF, 'Z', 37)
  AND (vendno = :vendno OR :vendno = 0)
  AND (typeno = :typeno OR :typeno = 0)
  AND (clno = :clno OR :clno = 0)
  AND (editado = :editado OR :editado = -1)
  AND (categoria = :categoria OR :categoria = 0)
