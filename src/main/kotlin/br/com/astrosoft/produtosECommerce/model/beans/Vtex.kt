package br.com.astrosoft.produtosECommerce.model.beans

import br.com.astrosoft.framework.util.localDate
import br.com.astrosoft.produtosECommerce.model.local
import java.time.LocalDate

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
  var codigo: String,
  var validade: LocalDate?,
  val promoVtex: Double?,
  val validadeVtex: LocalDate?,
               ) {
  var priceSaci: PrecoSaci? = null

  fun promoprice() = priceSaci?.promopricev
  fun refprice() = priceSaci?.refprice
  fun precoCompor() = priceSaci?.precoCompor
  fun codigo() = priceSaci?.codigo?.toString() ?: ""
  fun validade() = priceSaci?.validadev?.localDate()

  fun update() {
    if (promoprice != promoprice() || refprice != refprice() || precoCompor != precoCompor() || codigo != codigo() || validade != validade()) {
      local.updateVtex(this)
      promoprice = promoprice()
      refprice = refprice()
      precoCompor = precoCompor()
      codigo = codigo()
      validade = validade()
    }
  }
}

data class FiltroVtex(val produto: String = "",
                      val sku: String = "",
                      val departamento: String = "",
                      val categoria: String = "",
                      val marca: String = "",
                      val promocao: Boolean = false)

data class FiltroVtexDif(val produto: String = "",
                         val sku: String = "",
                         val departamento: String = "",
                         val categoria: String = "",
                         val marca: String = "",
                         val promocao: Boolean = false,
                         val diferenca: EDiferenca)

enum class EDiferenca(val id: String, val label: String) {
  PROMO("PROMO", "Promoção"), DATA("DATA", "Validade"), PRICE("PRICE", "Preço")
}