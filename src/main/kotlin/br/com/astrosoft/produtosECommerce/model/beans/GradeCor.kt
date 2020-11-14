package br.com.astrosoft.produtosECommerce.model.beans

import br.com.astrosoft.produtosECommerce.model.local

class GradeCor(
  var descricao: String = "",
  var codigoCor: String = ""
              ) {
  companion object {
    fun findAll(): List<GradeCor> {
      return local.findAllCor()
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