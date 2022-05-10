package br.com.astrosoft.produtosECommerce.model.services

import br.com.astrosoft.framework.model.IServiceQuery
import br.com.astrosoft.framework.model.SortOrder
import br.com.astrosoft.produtosECommerce.model.beans.*
import br.com.astrosoft.produtosECommerce.model.local
import br.com.astrosoft.produtosECommerce.model.xlsx.PrecosEcomerce
import br.com.astrosoft.produtosECommerce.model.xlsx.PrecosVtex

class ServiceQueryVtex : IServiceQuery<Vtex, FiltroVtex> {
  override fun count(filter: FiltroVtex): Int {
    return local.countVtex(filter)
  }

  override fun fetch(
    filter: FiltroVtex, offset: Int, limit: Int, sortOrders: List<SortOrder>
  ): List<Vtex> {
    val lista =  local.fetchVtex(filter, offset, limit, sortOrders)
    lista.forEachIndexed { index, vtex ->
      vtex.seq = offset + index + 1
    }
    return lista
  }

  fun readExcel(fileName: String) {
    val precos = PrecosVtex.readExcel(fileName)
    precos.forEach { preco ->
      local.updatePrecoVtex(preco)
    }
  }
}