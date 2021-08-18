#!/bin/bash

TMP_FILE="/tmp/produtonovos.txt"

cat prd.sql | mysql -h172.20.47.5 -pengecopi2006 -uroot sqldados > $TMP_FILE
mysqlimport --ignore-lines=1 --local --delete produtoEcomerce $TMP_FILE

cat updateNovos.sql | mysql -hlocalhost -uroot produtoEcomerce
