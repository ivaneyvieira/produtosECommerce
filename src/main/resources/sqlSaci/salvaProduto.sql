UPDATE produtoEcomerce.produto
SET marca             = :marca,
    categoria         = :categoria,
    descricaoCompleta = :descricaoCompleta,
    bitola            = :bitola,
    imagem            = :imagem
WHERE codigo = :codigo
  AND grade = :grade