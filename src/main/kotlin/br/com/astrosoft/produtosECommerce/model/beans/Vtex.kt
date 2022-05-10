package br.com.astrosoft.produtosECommerce.model.beans

import br.com.astrosoft.produtosECommerce.model.local

data class Vtex(
  var seq: Int?,
  val skuId: Int,
  val idProd: Int,
  val nomeSku: String,
  val referenciaSKU: String,
  val idDep: Int,
  val nomeDepartamento: String,
  val idCat: Int,
  val nomeCategoria: String,
  val idMarca: Int,
  val nomeMarca: String,
  val estoque: Int,
  val preco: Double,
  var promoprice: Double?,
  var refprice: Double?,
  var precoCompor: Double?,
               ) {
  var priceSaci: PrecoSaci? = null

  fun promoprice() = priceSaci?.promopricev
  fun refprice() = priceSaci?.refprice
  fun precoCompor() = priceSaci?.precoCompor

  fun update() {
    if (promoprice != promoprice() || refprice != refprice() || precoCompor != precoCompor()) {
      local.updateVtex(this)
      promoprice = promoprice()
      refprice = refprice()
      precoCompor = precoCompor()
    }
  }
}

data class FiltroVtex(
  val produto: String = "",
  val sku: String = "",
  val departamento: String = "",
  val categoria: String = "",
  val marca: String = "",
  val promocao: Boolean = false
                     )