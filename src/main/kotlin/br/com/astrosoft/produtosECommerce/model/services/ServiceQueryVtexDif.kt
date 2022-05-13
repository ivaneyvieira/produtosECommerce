package br.com.astrosoft.produtosECommerce.model.services

import br.com.astrosoft.framework.model.IServiceQuery
import br.com.astrosoft.framework.model.SortOrder
import br.com.astrosoft.produtosECommerce.model.beans.*
import br.com.astrosoft.produtosECommerce.model.local
import br.com.astrosoft.produtosECommerce.model.saci
import br.com.astrosoft.produtosECommerce.model.xlsx.PromoVtex

class ServiceQueryVtexDif : IServiceQuery<Vtex, FiltroVtexDif> {
  override fun count(filter: FiltroVtexDif): Int {
    return local.countVtexDif(filter)
  }

  override fun fetch(filter: FiltroVtexDif, offset: Int, limit: Int, sortOrders: List<SortOrder>): List<Vtex> {
    return local.fetchVtexDif(filter, offset, limit, sortOrders)
  }

  fun readExcel(fileName: String) {
    val precos = PromoVtex.readExcel(fileName)
    precos.forEach { preco ->
      local.updatePrecoVtex(preco)
    }
  }
}