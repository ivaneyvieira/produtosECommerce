SELECT LPAD(prdno * 1, 6, '0') AS codigo,
       grade                   AS grade,
       qtty_varejo / 1000      AS saldo
FROM sqldados.stk
WHERE storeno = 4