package br.com.astrosoft.produtosEComerce.model.beans

import br.com.astrosoft.produtosEComerce.model.beans.Categoria.Companion
import br.com.astrosoft.produtosEComerce.model.local

data class Marca(var marcaNo : Int=0, var name : String="") {
  companion object {
    fun findAll(): List<Marca> {
      return local.findAllMarca()
    }
  
    fun findById(id : Int) = findAll()
      .firstOrNull{it.marcaNo == id}
  
  
    fun add(marca: Marca) {
      local.addMarca(marca)
    }
    
    fun update(marca: Marca) {
      local.updateMarca(marca)
    }
    
    fun delete(marca: Marca) {
      local.deleteMarca(marca)
    }
  }
}