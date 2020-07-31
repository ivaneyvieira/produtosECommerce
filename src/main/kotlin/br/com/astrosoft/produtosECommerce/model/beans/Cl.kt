package br.com.astrosoft.produtosECommerce.model.beans

import br.com.astrosoft.framework.model.ILookup

data class Cl(val clno: String,
              val clname: String): ILookup {
  override val lookupValue: String
    get() = clname
}