UPDATE produtoEcomerce.produto
SET marca             = :marca,
    categoria         = :categoria,
    descricaoCompleta = :descricaoCompleta,
    bitola            = :bitola,
    imagem            = :imagem,
    editado           = :editado
WHERE codigo = :codigo
  AND grade = :grade