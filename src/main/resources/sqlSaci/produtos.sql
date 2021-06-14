DROP TEMPORARY TABLE IF EXISTS T_RESULT;
CREATE TEMPORARY TABLE T_RESULT
SELECT codigo,
       grade,
       gradeCompleta,
       barcode,
       P.descricao,
       vendno,
       fornecedor,
       typeno,
       typeName,
       IFNULL(clno, '')   AS clno,
       clname,
       marca,
       IFNULL(M.name, '') AS marcaNome,
       categoria,
       CASE
	 WHEN C.categoriaNo IS NULL
	   THEN ''
	 WHEN C.grupo = ''
	   THEN ''
	 WHEN C.departamento = ''
	   THEN C.grupo
	 WHEN C.secao = ''
	   THEN CAST(CONCAT(C.grupo, '/', C.departamento) AS CHAR)
	 ELSE CAST(CONCAT(C.grupo, '/', C.departamento, '/', C.secao) AS CHAR)
       END                AS categoriaNome,
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
       IFNULL(ncm, '')    AS ncm,
       cor,
       'simples'          AS variacao,
       corStr             AS corStr,
       P.dataHoraMudanca,
       P.userno,
       modificado,
       gradeAlternativa
FROM produtoEcomerce.produto          AS P
  LEFT JOIN produtoEcomerce.marca     AS M
	      ON P.marca = M.marcaNo
  LEFT JOIN produtoEcomerce.categoria AS C
	      ON P.categoria = C.categoriaNo
WHERE (codigo = :codigo OR :codigo = 0)
  AND P.descricao BETWEEN RPAD(:descricaoI, 37, ' ') AND RPAD(:descricaoF, 37, 'Z')
  AND (vendno = :vendno OR :vendno = 0)
  AND (typeno = :typeno OR :typeno = 0)
  AND (clno BETWEEN :clno1 AND :clno2 OR :clno1 = 0)
  AND (editado = :editado OR :editado = -1)
  AND (categoria BETWEEN :categoria1 AND :categoria2 OR :categoria1 = 0)
  AND (codigo IN (:listaProduto) OR :listaProdutoVazia = 'S')
