package br.com.astrosoft.produtosECommerce.model.beans

import br.com.astrosoft.produtosECommerce.model.local

data class Marca(var marcaNo: Int = 0, var name: String = "") : ILookup {
  override val lookupValue: String
    get() = name
  
  companion object {
    private val listMarcas = mutableListOf<Marca>().apply {
      addAll(local.findAllMarca())
    }
    
    fun findAll(): List<Marca> {
      listMarcas.clear()
      listMarcas.addAll(local.findAllMarca())
      return listMarcas
    }
    
    fun findById(id: Int) = listMarcas
      .firstOrNull {it.marcaNo == id}
    
    fun add(marca: Marca) {
      local.addMarca(marca)
    }
    
    fun update(marca: Marca) {
      local.updateMarca(marca)
    }
    
    fun delete(marca: Marca) {
      local.deleteMarca(marca)
    }
    
    fun nextNo(): Int {
      val maxNo =
        findAll().map {it.marcaNo}
          .max() ?: 0
      return maxNo + 1
    }
  }
}


