UPDATE produtoEcomerce.produto
SET marca             = :marca,
    categoria         = :categoria,
    descricaoCompleta = :descricaoCompleta,
    bitola            = :bitola,
    imagem            = :imagem,
    peso              = :peso,
    comprimento       = :comprimento,
    largura           = :largura,
    altura            = :altura,
    textLink          = :textLink,
    especificacoes    = :especificacoes,
    editado           = :editado,
    gradeCompleta     = :gradeCompleta
WHERE codigo = :codigo
  AND grade = :grade