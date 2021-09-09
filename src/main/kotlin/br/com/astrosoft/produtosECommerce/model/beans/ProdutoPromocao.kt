package br.com.astrosoft.produtosECommerce.model.beans

import br.com.astrosoft.produtosECommerce.model.beans.EEditor.ENVIADO
import br.com.astrosoft.produtosECommerce.model.saci
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryProduto
import java.time.LocalDate

class ProdutoPromocao(
  val codigo: String,
  val descricao: String,
  val promoDateWeb: LocalDate?,
  val validade: LocalDate?,
  val precoRef: Double,
  val perc: Double,
  val precoPromoWeb: Double,
  val precoPromo: Double,
  val vendno: Int,
  val abrev: String,
  val tipo: Int,
  val centLucro: Int,
  val saldo: Int,
  val promono: Int,
  val web: String,
  val promocaoWeb: String,
  val promocaoSaci: String,
  val precoAlinhado: String,
  val dataAlinhada: String,
                     ) {

  companion object {
    fun savePromocao(promocao: Promocao, list: List<ProdutoPromocao>) {
      val produtos = listCodigos(list)
      produtos.forEach { produto ->
        val price = list.firstOrNull { it.codigo == produto.codigo }?.precoPromo
        if (price == null) saci.removePromocao(produto.codigo, produto.grade)
        else saci.savePromocao(promocao, produto.codigo, produto.grade, price)
      }
    }

    private fun listCodigos(list: List<ProdutoPromocao>): List<Produto> {
      val codigos = list.map { it.codigo }
      val produtos = ServiceQueryProduto().fetch(ENVIADO).filter {
        it.codigo in codigos
      }
      return produtos
    }

    fun removePromocao(list: List<ProdutoPromocao>) {
      val produtos = listCodigos(list)
      produtos.forEach {
        saci.removePromocao(it.codigo, it.grade)
      }
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as ProdutoPromocao

    if (codigo != other.codigo) return false

    return true
  }

  override fun hashCode(): Int {
    return codigo.hashCode()
  }
}

data class FiltroProdutosPromocional(
  val promocao: Promocao?,
  val centroLucro: Int,
  val fornecedor: Int,
  val tipo: Int,
  val codigo: String,
  val tipoPainel : ETipoPainel,
                                    )

enum class ETipoPainel {
  BASE, SACI, WEB, INVALIDO
}