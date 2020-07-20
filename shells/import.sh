#!/usr/bin/env bash

SQL_IMPORT=`cat prd.sql categoria.sql`;
MYOPC_SACI="-h${HOST_MYSQL} -p${PASS_MYSQL} -u${USER_MYSQL}"
MYOPC_LOCAL="--protocol=TCP"

mysql_exec() {
  SQL="$1"
  echo "$SQL" | mysql -NB ${MYOPC_SACI} sqldados
}

mysql_local() {
  local FILE="/tmp/temp.$2.sql"
  local SQL="$1"
  local DATABASE=$2
  echo "$SQL"
  mysql --verbose -NB ${MYOPC_LOCAL} --execute="$SQL" --database=${DATABASE}
}
TABLE=produto
create_table() {
  local TABLE=$1
  echo "DROP TABLE IF EXISTS $TABLE;"
  mysql_exec "
  CREATE TEMPORARY TABLE T
  (PRIMARY KEY(codigo, grade))
  $SQL_IMPORT;
  SHOW CREATE TABLE T;
  " | cut -f2 |
  tr -d '`' |
  sed "s:\\\\n::g" |
  sed "s:TEMPORARY TABLE T:TABLE $TABLE:g"
}

TABLE=produto

DATABASE=produtoEcomerce

mysql_local "CREATE DATABASE IF NOT EXISTS $DATABASE;" mysql
mysql_local "$(create_table ${TABLE});" ${DATABASE}
mysql_local "ALTER TABLE $TABLE ENGINE=InnoDB;" ${DATABASE}

FILE_IMPORT="/tmp/${TABLE}.txt"

mysql_exec "$SQL_IMPORT" > ${FILE_IMPORT}

#mysqlimport  ${MYOPC_LOCAL} --replace ${DATABASE} ${FILE_IMPORT}
mysql_local "LOAD DATA LOCAL INFILE '${FILE_IMPORT}' INTO TABLE $TABLE" ${DATABASE}

mysql_local "ALTER TABLE $TABLE ADD marca VARCHAR(40) DEFAULT '';" ${DATABASE}
mysql_local "ALTER TABLE $TABLE ADD categoria INT DEFAULT 0;" ${DATABASE}
mysql_local "ALTER TABLE $TABLE ADD descricaoCompleta VARCHAR(120) DEFAULT '';" ${DATABASE}
mysql_local "ALTER TABLE $TABLE ADD bitola VARCHAR(30) DEFAULT '';" ${DATABASE}
mysql_local "ALTER TABLE $TABLE ADD imagem VARCHAR(80) DEFAULT '';" ${DATABASE}
mysql_local "ALTER TABLE $TABLE ADD peso DECIMAL(15, 4) DEFAULT 0;" ${DATABASE}
mysql_local "ALTER TABLE $TABLE ADD altura DECIMAL(15, 4) DEFAULT 0;" ${DATABASE}
mysql_local "ALTER TABLE $TABLE ADD comprimento DECIMAL(15, 4) DEFAULT 0;" ${DATABASE}
mysql_local "ALTER TABLE $TABLE ADD largura DECIMAL(15, 4) DEFAULT 0;" ${DATABASE}
mysql_local "ALTER TABLE $TABLE ADD editado INT DEFAULT 0;" ${DATABASE}


