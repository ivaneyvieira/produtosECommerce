package br.com.astrosoft.produtosECommerce.model.beans

data class Fornecedor(val vendno: Int,
                      val fornecedor: String):  ILookup {
  override val lookupValue: String
    get() = fornecedor
}