package br.com.astrosoft.produtosECommerce.model.beans

import br.com.astrosoft.framework.model.ILookup

data class Fornecedor(val vendno: Int, val fornecedor: String) : ILookup {
  override val lookupValue: String
    get() = fornecedor
}