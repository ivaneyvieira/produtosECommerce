package br.com.astrosoft.produtosECommerce.model.beans

import br.com.astrosoft.produtosECommerce.model.saci
import java.time.LocalDate

class ProdutoPromocao(
  val codigo: String,
  val descricao: String,
  val validade: LocalDate,
  val precoRef: Double,
  val perc: Double,
  val precoPromo: Double,
  val vendno: Int,
  val abrev: String,
  val tipo: Int,
  val centLucro: Int,
  val saldo: Double
) {
  fun updatePromo() {
    saci.updatePromo(codigo)
  }

  companion object {
    fun findProdutosPromocional(filtro: FiltroProdutosPromocional): List<ProdutoPromocao> {
      return saci.findProdutosPromocional(filtro)
    }
  }
}

data class FiltroProdutosPromocional(
  val centroLucro: Int,
  val fornecedor: Int,
  val tipo: Int,
  val codigo: String
)