package br.com.astrosoft.produtosECommerce.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.produtosECommerce.model.beans.GradeCor

class CorViewModel(view: ICorView) : ViewModel<ICorView>(view) {
  fun findAll(): List<GradeCor>? {
    return GradeCor.findAll()
  }

  fun add(cor: GradeCor): GradeCor? {
    exec {
      GradeCor.add(cor)
    }
    return cor
  }

  fun update(cor: GradeCor?): GradeCor? {
    exec {
      if (cor != null) GradeCor.update(cor)
    }
    return cor
  }

  fun delete(cor: GradeCor?) {
    exec {
      if (cor != null) GradeCor.delete(cor)
    }
  }
}

interface ICorView : IView