package br.com.astrosoft.produtosECommerce.model.services

import br.com.astrosoft.framework.model.IServiceQuery
import br.com.astrosoft.framework.model.SortOrder
import br.com.astrosoft.produtosECommerce.model.beans.FiltroVtexDif
import br.com.astrosoft.produtosECommerce.model.beans.PrecoSaci
import br.com.astrosoft.produtosECommerce.model.beans.Vtex
import br.com.astrosoft.produtosECommerce.model.local
import br.com.astrosoft.produtosECommerce.model.saci

class ServiceQueryVtexDif : IServiceQuery<Vtex, FiltroVtexDif> {
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
}