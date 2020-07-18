UPDATE sqldados.users AS U
SET bits2    = :bitAcesso,
    auxLong1 = :storeno
WHERE no = :no