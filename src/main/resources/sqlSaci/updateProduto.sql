INSERT IGNORE INTO produtoEcomerce.produto(codigo, grade, barcode, descricao, vendno, fornecedor,
					   typeno, typeName, clno, clname, marca, categoria,
					   descricaoCompleta, bitola, imagem, peso, altura,
					   comprimento, largura, editado, textLink, especificacoes,
					   precoCheio, ncm, gradeCompleta, cor, dataHoraMudanca,
					   userno, corStr, modificado, gradeAlternativa)
VALUES (:codigo, :grade, :barcode, :descricao, :vendno, :fornecedor, :typeno, :typeName, :clno,
	:clname, 0 /*marca*/, 0 /*categoria*/, :descricao /*descricaoCompleta*/, 0 /*bitola*/,
	'' /*imagem*/, 0 /*peso*/, 0 /*altura*/, 0 /*comprimento*/, 0 /*largura*/, 0 /*editado*/,
	'' /*textLink*/, '' /*especificacoes*/, :precoCheio, :ncm, '' /*gradeCompleta*/, '' /*cor*/,
	CURRENT_TIMESTAMP /*dataHoraMudanca*/, 1 /*userno*/, '' /*corStr*/, 'N' /*modificado*/,
	'' /*gradeAlternativa*/)
ON DUPLICATE KEY UPDATE barcode    = :barcode,
			descricao  = :descricao,
			vendno     = :vendno,
			fornecedor = :fornecedor,
			typeno     = :typeno,
			typeName   = :typeName,
			clno       = :clno,
			clname     = :clname,
			precoCheio = :precoCheio,
			ncm        = :ncm