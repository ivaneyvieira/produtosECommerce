INSERT IGNORE INTO produto(codigo, grade, barcode, descricao, vendno, fornecedor, typeno, typeName, clno, clname, marca,
                           categoria, descricaoCompleta, bitola, imagem, peso, altura, comprimento, largura, editado,
                           textLink, especificacoes, precoCheio, ncm, gradeCompleta, cor, dataHoraMudanca, userno,
                           corStr, modificado, gradeAlternativa)
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
       marca,
       categoria,
       descricaoCompleta,
       bitola,
       imagem,
       peso,
       altura,
       comprimento,
       largura,
       editado,
       textLink,
       especificacoes,
       precoCheio,
       ncm,
       gradeCompleta,
       cor,
       dataHoraMudanca,
       0   AS userno,
       ''  AS corStr,
       'N' AS modificado,
       ''  AS gradeAlternativa
FROM produtonovos;

UPDATE produto AS P INNER JOIN produtonovos AS N USING (codigo, grade)
SET P.barcode = N.barcode
WHERE P.barcode != N.barcode;

UPDATE produto AS P INNER JOIN produtonovos AS N USING (codigo, grade)
SET P.fornecedor = N.fornecedor
WHERE P.fornecedor != N.fornecedor;

UPDATE produto AS P LEFT JOIN produtonovos AS N USING (codigo, grade)
SET P.editado = -1
WHERE N.codigo IS NULL
  AND P.editado >= 0;

UPDATE produto AS P INNER JOIN produtonovos AS N USING (codigo, grade)
SET P.editado = 0
WHERE P.editado = -1;

DROP TEMPORARY TABLE IF EXISTS T;
CREATE TEMPORARY TABLE T (
  PRIMARY KEY (codigo)
)
SELECT codigo, MAX(peso) AS peso, MAX(altura) AS altura, MAX(comprimento) AS comprimento, MAX(largura) AS largura
FROM produto
GROUP BY codigo;

UPDATE produto AS P INNER JOIN T USING (codigo)
SET P.altura = T.altura
WHERE P.altura = 0;

UPDATE produto AS P INNER JOIN T USING (codigo)
SET P.comprimento = T.comprimento
WHERE P.comprimento = 0;

UPDATE produto AS P INNER JOIN T USING (codigo)
SET P.largura = T.largura
WHERE P.largura = 0;

UPDATE produto AS P INNER JOIN T USING (codigo)
SET P.peso = T.peso
WHERE P.peso = 0;
