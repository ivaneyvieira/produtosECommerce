#!/bin/bash

TMP_FILE="/tmp/produtonovos.txt"

cat /root/containers/produtosEcomerce/producao/shells/prd.sql | mysql -h172.20.47.5 -papp1237 -uapp sqldados  > $TMP_FILE
mysqlimport --ignore-lines=1 --local --delete produtoEcomerce $TMP_FILE

cat /root/containers/produtosEcomerce/producao/shells/updateNovos.sql | mysql -hlocalhost -uroot produtoEcomerce
