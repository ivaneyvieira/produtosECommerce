package br.com.astrosoft.produtosECommerce.model.services

import br.com.astrosoft.framework.model.IServiceQuery
import br.com.astrosoft.framework.model.SortOrder
import br.com.astrosoft.produtosECommerce.model.beans.FiltroProdutoConferencia
import br.com.astrosoft.produtosECommerce.model.beans.ProdutoConferencia
import br.com.astrosoft.produtosECommerce.model.local
import br.com.astrosoft.produtosECommerce.model.saci
import br.com.astrosoft.produtosECommerce.model.xlsx.PrecosEcomerce

class ServiceQueryProdutoConferencia : IServiceQuery<ProdutoConferencia, FiltroProdutoConferencia> {
  override fun count(filter: FiltroProdutoConferencia): Int {
    return local.countProdutoConferencia(filter)
  }

  override fun fetch(filter: FiltroProdutoConferencia,
                     offset: Int,
                     limit: Int,
                     sortOrders: List<SortOrder>): List<ProdutoConferencia> {
    return local.fetchProdutoConferencia(filter, offset, limit, sortOrders)
  }

  fun readExcel(fileName: String) {
    val precos = PrecosEcomerce.readExcel(fileName)
    local.apagaPrecos()
    val precosSaci = saci.precoSaci().groupBy { it.codigo }.mapValues { it.value.firstOrNull() }
    val produtosLocal = local.produtosBarcode()
    val produtosBarcode = produtosLocal.groupBy { it.barcode }.mapValues { it.value.firstOrNull() }
    val produtosCodigo = produtosLocal.groupBy { it.codigo }.mapValues { it.value.firstOrNull() }
    precos.forEach {
      val produto = produtosBarcode[it.codigo] ?: produtosCodigo[it.codigo.toIntOrNull()]
      val prdSaci = precosSaci[produto?.codigo]

      local.addPrecoConferencia(
        refid = it.codigo,
        listPrice = it.preco,
        prdno = prdSaci?.codigo?.toString() ?: "",
        grade = produto?.grade ?: "",
        descricaoSite = it.descricao,
        descricaoSaci = prdSaci?.descricao ?: "",
        precoPromo = prdSaci?.promoprice ?: 0.00,
        precoRef = prdSaci?.preco ?: 0.00,
                               )
    }
  }
}