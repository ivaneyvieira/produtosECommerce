SELECT storeno,
       prdno,
       qtty_varejo / 1000 AS saldo
FROM sqldados.stk
WHERE storeno = 4
  AND prdno = :prdno
  AND (grade = :grade OR :grade = '')