package br.com.astrosoft.produtosECommerce.model.beans

import br.com.astrosoft.produtosECommerce.model.beans.EEditor.EDITADO
import br.com.astrosoft.produtosECommerce.model.beans.EEditor.ENVIADO
import br.com.astrosoft.produtosECommerce.model.saci
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryProduto
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

  companion object {
    fun savePromocao(promocao: Promocao, list: List<ProdutoPromocao>) {
      val produtos = listCodigos(list)
      produtos.forEach { produto ->
        val price = list.firstOrNull { it.codigo == it.codigo }?.precoPromo
        if (price == null)
          saci.removePromocao(produto.codigo, produto.grade)
        else
          saci.savePromocao(promocao, produto.codigo, produto.grade, price)
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
}

data class FiltroProdutosPromocional(
  val promocao: Promocao?,
  val centroLucro: Int,
  val fornecedor: Int,
  val tipo: Int,
  val codigo: String,
  val temPromocao: Boolean
)