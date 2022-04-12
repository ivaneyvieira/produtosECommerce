package br.com.astrosoft.produtosECommerce.model.beans

class ProdutoConferencia(
  val refid: String,
  val listPrice: Double,
  val prdno: String? = null,
  val grade: String? = null,
  val descricao: String? = null,
  val precoSaci: Double? = null
)

data class FiltroProdutoConferencia(
  val codigo: String
)