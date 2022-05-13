package br.com.astrosoft.produtosECommerce.model.beans

class ProdutoConferencia(
  val refid: String,
  val listPrice: Double,
  val prdno: String? = null,
  val grade: String? = null,
  val descricaoSite: String? = null,
  val descricaoSaci: String? = null,
  val precoPromo: Double? = null,
  val precoRef: Double? = null,
                        )

data class FiltroProdutoConferencia(val codigo: String)