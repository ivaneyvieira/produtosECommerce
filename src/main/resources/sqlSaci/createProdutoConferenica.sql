drop table if exists produtoEcomerce.produtoConferencia;

create table produtoEcomerce.produtoConferencia
(
    refid     varchar(20),
    listPrice DECIMAL(13, 2),
    prdno     varchar(16),
    grade     varchar(8),
    descricaoSaci varchar(100),
    descricaoSite varchar(150),
    precoSaci decimal(13, 2)
)