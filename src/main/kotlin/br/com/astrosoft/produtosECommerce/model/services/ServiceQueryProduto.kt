package br.com.astrosoft.produtosECommerce.model.services

import br.com.astrosoft.framework.model.IServiceQuery
import br.com.astrosoft.framework.model.SortOrder
import br.com.astrosoft.produtosECommerce.model.beans.EEditor
import br.com.astrosoft.produtosECommerce.model.beans.FiltroProduto
import br.com.astrosoft.produtosECommerce.model.beans.Produto
import br.com.astrosoft.produtosECommerce.model.local

class ServiceQueryProduto : IServiceQuery<Produto, FiltroProduto> {
  override fun count(filter: FiltroProduto): Int {
    return local.countProduto(filter)
  }

  override fun fetch(
    filter: FiltroProduto, offset: Int, limit: Int, sortOrders: List<SortOrder>
  ): List<Produto> {
    return local.fetchProduto(filter, offset, limit, sortOrders)
  }

  fun fetch(editado: EEditor) = fetch(FiltroProduto(editado = editado))
}