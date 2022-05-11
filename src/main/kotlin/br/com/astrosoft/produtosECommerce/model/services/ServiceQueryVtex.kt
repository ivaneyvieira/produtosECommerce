package br.com.astrosoft.produtosECommerce.model.services

import br.com.astrosoft.framework.model.IServiceQuery
import br.com.astrosoft.framework.model.SortOrder
import br.com.astrosoft.produtosECommerce.model.beans.*
import br.com.astrosoft.produtosECommerce.model.local
import br.com.astrosoft.produtosECommerce.model.saci
import br.com.astrosoft.produtosECommerce.model.xlsx.PromoVtex

class ServiceQueryVtex : IServiceQuery<Vtex, FiltroVtex> {
  private val precosSaci = saci.precoSaci().groupBy { it.codigo }.mapValues { it.value.firstOrNull() }
  private val produtosLocal = local.produtosBarcode()
  private val produtoBarcodeSaci = saci.produtoBarcodeSaci().groupBy { it.barcode }.mapValues { it.value.firstOrNull() }
  private val produtosBarcode = produtosLocal.groupBy { it.barcode }.mapValues { it.value.firstOrNull()?.toProdutoCodigo() }
  private val produtosCodigo = produtosLocal.groupBy { it.codigo }.mapValues { it.value.firstOrNull()?.toProdutoCodigo() }

  private fun findPrice(codigo : String): PrecoSaci? {
    val produto =
      produtoBarcodeSaci[codigo] ?: produtosBarcode[codigo] ?: produtosCodigo[codigo.toIntOrNull()]
    val prdSaci = precosSaci[produto?.codigo?.toIntOrNull()] ?: precosSaci[codigo.toIntOrNull()]
    return prdSaci
  }

  override fun count(filter: FiltroVtex): Int {
    return local.countVtex(filter)
  }

  override fun fetch(
    filter: FiltroVtex, offset: Int, limit: Int, sortOrders: List<SortOrder>
  ): List<Vtex> {
    val lista =  local.fetchVtex(filter, offset, limit, sortOrders)
    lista.forEachIndexed { index, vtex ->
      vtex.seq = offset + index + 1
      vtex.priceSaci = findPrice(vtex.referenciaSKU)
      vtex.update()
    }
    return lista
  }

  fun readExcel(fileName: String) {
    val precos = PromoVtex.readExcel(fileName)
    precos.forEach { preco ->
      local.updatePrecoVtex(preco)
    }
  }
}