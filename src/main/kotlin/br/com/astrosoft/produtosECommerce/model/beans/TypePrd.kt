package br.com.astrosoft.produtosECommerce.model.beans

import br.com.astrosoft.framework.model.ILookup

class TypePrd(
  val typeno: Int, val typeName: String
             ) : ILookup {
  override val lookupValue: String
    get() = typeName
}