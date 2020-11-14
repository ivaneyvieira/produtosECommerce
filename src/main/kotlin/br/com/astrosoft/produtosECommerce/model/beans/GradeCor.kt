package br.com.astrosoft.produtosECommerce.model.beans

import br.com.astrosoft.produtosECommerce.model.local

class GradeCor(
  var descricao: String = "",
  var codigoCor: String = ""
              ) {
  var descricaoOriginal : String = ""
  companion object {
    fun findAll(): List<GradeCor> {
      return local.findAllCor().map {
        it.descricaoOriginal = it.descricao
        it
      }
    }
    
    fun add(cor: GradeCor) {
      local.addCor(cor)
    }
    
    fun update(cor: GradeCor) {
      local.updateCor(cor)
    }
    
    fun delete(cor: GradeCor) {
      local.deleteCor(cor)
    }
  }
}