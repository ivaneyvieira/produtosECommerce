UPDATE sqldados.users
SET auxLong1 = :storeno
WHERE no = :no;

INSERT INTO sqldados.userApp(userno, appName, bitAcesso)
VALUES (:no, 'produtoECommerce', :bitAcesso)
ON DUPLICATE KEY UPDATE bitAcesso = :bitAcesso