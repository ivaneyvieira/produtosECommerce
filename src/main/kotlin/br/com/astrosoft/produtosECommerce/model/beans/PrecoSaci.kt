package br.com.astrosoft.produtosECommerce.model.beans

class PrecoSaci(
  val codigo: Int,
  val descricao: String,
  val refprice: Double,
  val validade: Int,
  val promoprice: Double?,
  val preco: Double?
)