package br.com.astrosoft.produtosECommerce.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryVtex

class VtexViewModel(view: IVtexView) : ViewModel<IVtexView>(view) {
  fun servicoVtexProduto() : ServiceQueryVtex = ServiceQueryVtex()

  fun updateGridProduto() {
    view.updateGridProduto()
  }
}

interface IVtexView : IView {
  fun updateGridProduto()
}


