package br.com.astrosoft.produtosECommerce.model.services

import br.com.astrosoft.framework.model.IServiceQuery
import br.com.astrosoft.framework.model.SortOrder
import br.com.astrosoft.produtosECommerce.model.beans.FiltroVtexDif
import br.com.astrosoft.produtosECommerce.model.beans.PrecoSaci
import br.com.astrosoft.produtosECommerce.model.beans.Vtex
import br.com.astrosoft.produtosECommerce.model.local
import br.com.astrosoft.produtosECommerce.model.saci
import br.com.astrosoft.produtosECommerce.model.xlsx.PromoVtex

class ServiceQueryVtexDif : IServiceQuery<Vtex, FiltroVtexDif> {
  private val precosSaci = saci.precoSaci().groupBy { it.codigo }.mapValues { it.value.firstOrNull() }
  private val produtosLocal = local.produtosBarcode()
  private val produtoBarcodeSaci = saci.produtoBarcodeSaci().groupBy { it.barcode }.mapValues { it.value.firstOrNull() }
  private val produtosBarcode =
    produtosLocal.groupBy { it.barcode }.mapValues { it.value.firstOrNull()?.toProdutoCodigo() }
  private val produtosCodigo =
    produtosLocal.groupBy { it.codigo }.mapValues { it.value.firstOrNull()?.toProdutoCodigo() }

  private fun findPrice(codigo: String): PrecoSaci? {
    val produto = produtoBarcodeSaci[codigo] ?: produtosBarcode[codigo] ?: produtosCodigo[codigo.toIntOrNull()]
    val prdSaci = precosSaci[produto?.codigo?.toIntOrNull()] ?: precosSaci[codigo.toIntOrNull()]
    return prdSaci
  }

  override fun count(filter: FiltroVtexDif): Int {
    return local.countVtexDif(filter)
  }

  override fun fetch(filter: FiltroVtexDif, offset: Int, limit: Int, sortOrders: List<SortOrder>): List<Vtex> {
    val lista = local.fetchVtexDif(filter, offset, limit, sortOrders)
    lista.forEachIndexed { index, vtex ->
      vtex.seq = offset + index + 1
    }
    return lista
  }

  fun updateSaci(filter: FiltroVtexDif) {
    val list = fetch(filter)
    list.forEach {
      it.priceSaci = findPrice(it.referenciaSKU)
      it.update()
    }
  }
}