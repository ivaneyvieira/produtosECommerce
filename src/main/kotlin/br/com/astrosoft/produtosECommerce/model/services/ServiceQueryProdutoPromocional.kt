package br.com.astrosoft.produtosECommerce.model.services

import br.com.astrosoft.framework.model.IServiceQuery
import br.com.astrosoft.framework.model.SortOrder
import br.com.astrosoft.produtosECommerce.model.beans.FiltroProdutosPromocional
import br.com.astrosoft.produtosECommerce.model.beans.ProdutoPromocao
import br.com.astrosoft.produtosECommerce.model.saci

class ServiceQueryProdutoPromocional : IServiceQuery<ProdutoPromocao, FiltroProdutosPromocional> {
  override fun count(filter: FiltroProdutosPromocional): Int {
    return saci.countProduto(filter)
  }

  override fun fetch(
    filter: FiltroProdutosPromocional, offset: Int, limit: Int, sortOrders: List<SortOrder>
                    ): List<ProdutoPromocao> {
    return saci.fetchProduto(filter, offset, limit, sortOrders)
  }
}