DO @PRDNO := CAST(LPAD(:prdno, 16, ' ') AS CHAR);
DO @PROMO := :promo;
DO @GRADE := :grade;
DO @PRICE := :price;
/*
INSERT INTO sqldados.promoprd(price, auxLong1, auxLong2, auxLong3, auxLong4, auxLong5,
			      auxLong6, auxLong7, auxLong8, auxMy1, auxMy2, auxMy3, auxMy4,
			      promono, prazomedio, bits, padbyte, auxShort1, auxShort2,
			      auxShort3, auxShort4, auxShort5, auxShort6, auxShort7,
			      auxShort8, prdno, grade, auxString) VALUE (@PRICE,
									 0,
									 0,
									 0,
									 0,
									 0,
									 0,
									 0,
									 0,
									 0,
									 0,
									 0,
									 0,
									 @PROMO,
									 0,
									 0,
									 0,
									 0,
									 0,
									 0,
									 0,
									 0,
									 0,
									 0,
									 0,
									 @PRDNO,
									 @GRADE,
									 '')
ON DUPLICATE KEY UPDATE price = @PRICE

 */
