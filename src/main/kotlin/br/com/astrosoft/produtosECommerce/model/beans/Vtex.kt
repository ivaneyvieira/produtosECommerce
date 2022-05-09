package br.com.astrosoft.produtosECommerce.model.beans

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
               )

data class FiltroVtex(
  val produto: String,
  val sku: String,
  val departamento: String,
  val categoria: String,
  val marca: String,
                     )