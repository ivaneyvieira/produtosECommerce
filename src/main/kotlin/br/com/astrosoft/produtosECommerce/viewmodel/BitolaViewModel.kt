package br.com.astrosoft.produtosECommerce.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.produtosECommerce.model.beans.Bitola

class BitolaViewModel(view: IBitolaView) : ViewModel<IBitolaView>(view) {
  fun findAll(): List<Bitola>? {
    Bitola.updateList()
    return Bitola.findAll()
  }

  fun add(bitola: Bitola): Bitola? {
    exec {
      Bitola.add(bitola)
    }
    return bitola
  }

  fun update(bitola: Bitola?): Bitola? {
    exec {
      if (bitola != null) Bitola.update(bitola)
    }
    return bitola
  }

  fun delete(bitola: Bitola?) {
    exec {
      if (bitola != null) Bitola.delete(bitola)
    }
  }
}

interface IBitolaView : IView