use sqldados;

drop temporary table if exists T_PRODUTO_BAKUP;
create temporary table T_PRODUTO_BAKUP
(
    primary key (data, prdno, grade)
)
SELECT current_date * 1                       as data,
       P.no                                   as prdno,
       CAST(LPAD(TRIM(P.no), 6, '0') AS CHAR) AS codigo,
       TRIM(IFNULL(B.barcode, P.barcode))     AS barcode,
       TRIM(MID(P.name, 1, 37))               AS descricao,
       IFNULL(V.val, '')                      AS grade,
       TRIM(MID(P.name, 37, 3))               AS unid,
       P.mfno                                 AS vendno,
       P.typeno                               AS typeno,
       CAST(LPAD(P.clno, 6, '0') AS CHAR)     AS clno,
       R.refprice / 100                       AS precoRef,
       S.ncm                                  AS ncm
FROM sqldados.prd AS P
         left join sqldados.prp AS R
                   ON P.no = R.prdno AND R.storeno = 10
         LEFT JOIN sqldados.grade AS G
                   ON G.no = MID(P.grade_l, 1, 10) * 1
         LEFT JOIN sqldados.grdval AS V
                   ON V.gradeno = G.no AND (V.bits & POW(2, 0)) = 0
         LEFT JOIN sqldados.prdbar AS B
                   ON P.no = B.prdno AND V.val = B.grade
         LEFT JOIN sqldados.prdloc AS L
                   ON P.no = L.prdno AND L.storeno = 4
         LEFT JOIN sqldados.spedprd AS S
                   ON P.no = S.prdno
GROUP BY codigo, grade;


show create table T_PRODUTO_BAKUP;

CREATE TEMPORARY TABLE `T_PRODUTO_BAKUP`
(
    `data`      int(10)     NOT NULL             DEFAULT '0',
    `prdno`     char(16)    NOT NULL             DEFAULT '',
    `codigo`    varchar(6) CHARACTER SET utf8mb4 DEFAULT NULL,
    `barcode`   varchar(16)                      DEFAULT NULL,
    `descricao` varchar(37)                      DEFAULT NULL,
    `grade`     varchar(8)  NOT NULL             DEFAULT '',
    `unid`      varchar(3)                       DEFAULT NULL,
    `vendno`    int(10)     NOT NULL,
    `typeno`    smallint(5) NOT NULL             DEFAULT '0',
    `clno`      varchar(6) CHARACTER SET utf8mb4 DEFAULT NULL,
    `precoRef`  decimal(23, 4)                   DEFAULT NULL,
    `ncm`       char(12)                         DEFAULT '',
    PRIMARY KEY (`data`, `prdno`, `grade`)
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1