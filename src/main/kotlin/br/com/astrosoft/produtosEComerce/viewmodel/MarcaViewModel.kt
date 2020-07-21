package br.com.astrosoft.produtosEComerce.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.produtosEComerce.model.beans.Marca

class MarcaViewModel(view: IMarcaView): ViewModel<IMarcaView>(view) {
  fun findAll(): List<Marca>? {
    return Marca.findAll()
  }
  
  fun add(marca: Marca): Marca? {
    exec {
      Marca.add(marca)
    }
    return marca
  }
  
  fun update(marca: Marca?): Marca? {
    exec {
      if(marca != null)
        Marca.update(marca)
    }
    return marca
  }
  
  fun delete(marca: Marca?) {
    exec {
      if(marca != null)
        Marca.delete(marca)
    }
  }
}

interface IMarcaView: IView