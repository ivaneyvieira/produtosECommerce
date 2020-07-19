package br.com.astrosoft.produtosEComerce.model.beans

import br.com.astrosoft.produtosEComerce.model.saci

data class Categoria(var categoriaNo: Int = 0,
                     var grupo: String = "",
                     var departamento: String = "",
                     var secao: String = "") {
  val descricao
    get() = "$grupo/$departamento/$secao"
  
  companion object {
    fun findAll(): List<Categoria> {
      return saci.findAllCategoria()
    }
    
    fun add(categoria: Categoria) {
      saci.addCategoria(categoria)
    }
    
    fun update(categoria: Categoria) {
      saci.updateCategoria(categoria)
    }
    
    fun delete(categoria: Categoria) {
      saci.deleteCategoria(categoria)
    }
  }
}