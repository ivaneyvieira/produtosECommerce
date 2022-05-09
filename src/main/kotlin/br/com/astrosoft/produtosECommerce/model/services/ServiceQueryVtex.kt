package br.com.astrosoft.produtosECommerce.model.services

import br.com.astrosoft.framework.model.IServiceQuery
import br.com.astrosoft.framework.model.SortOrder
import br.com.astrosoft.produtosECommerce.model.beans.*
import br.com.astrosoft.produtosECommerce.model.local

class ServiceQueryVtex : IServiceQuery<Vtex, FiltroVtex> {
  override fun count(filter: FiltroVtex): Int {
    return local.countVtex(filter)
  }

  override fun fetch(
    filter: FiltroVtex, offset: Int, limit: Int, sortOrders: List<SortOrder>
  ): List<Vtex> {
    return local.fetchVtex(filter, offset, limit, sortOrders)
  }
}