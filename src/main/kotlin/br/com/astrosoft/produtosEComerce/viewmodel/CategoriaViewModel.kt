package br.com.astrosoft.produtosEComerce.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.produtosEComerce.model.beans.Categoria

class CategoriaViewModel(view: ICategoriaView): ViewModel<ICategoriaView>(view) {
  fun findAll(): List<Categoria>? {
    return Categoria.findAll()
  }
  
  fun add(categoria: Categoria): Categoria? {
    exec {
      Categoria.add(categoria)
    }
    return categoria
  }
  
  fun update(categoria: Categoria?): Categoria? {
    exec {
      if(categoria != null)
        Categoria.update(categoria)
    }
    return categoria
  }
  
  fun delete(categoria: Categoria?) {
    exec {
      if(categoria != null)
        Categoria.delete(categoria)
    }
  }
}

interface ICategoriaView: IView