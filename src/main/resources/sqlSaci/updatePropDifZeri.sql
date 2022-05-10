create table sqldados.prd_bakl8
select storeno, prdno, l8
from sqldados.prp
where storeno = 10
  and l8 != 0;

UPDATE sqldados.prp
set l8 = 0
where storeno = 10
  and l8 != 0;

select storeno, prdno, l8
from sqldados.prp
where l8 != 0;
