package br.com.astrosoft.produtosECommerce.model.beans

data class Cl(val clno: String,
              val clname: String): ILookup {
  override val lookupValue: String
    get() = clname
}