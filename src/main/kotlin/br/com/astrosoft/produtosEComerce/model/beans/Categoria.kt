package br.com.astrosoft.produtosEComerce.model.beans

import br.com.astrosoft.produtosEComerce.model.local
import br.com.astrosoft.produtosEComerce.model.saci

data class Categoria(var categoriaNo: Int = 0,
                     var grupo: String = "",
                     var departamento: String = "",
                     var secao: String = "") {
  val descricao
    get() = "$grupo/$departamento/$secao"
  
  companion object {
    fun findAll(): List<Categoria> {
      return local.findAllCategoria()
    }
    
    fun findById(id : Int) = findAll().firstOrNull{it.categoriaNo == id}
    
    fun add(categoria: Categoria) {
      local.addCategoria(categoria)
    }
    
    fun update(categoria: Categoria) {
      local.updateCategoria(categoria)
    }
    
    fun delete(categoria: Categoria) {
      local.deleteCategoria(categoria)
    }
  }
}