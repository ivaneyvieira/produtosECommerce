package br.com.astrosoft.produtosECommerce.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryVtex
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryVtexDif

class VtexViewModel(view: IVtexView) : ViewModel<IVtexView>(view) {
  fun servicoVtexProduto(): ServiceQueryVtex = ServiceQueryVtex()
  fun servicoVtexDiferenca(): ServiceQueryVtexDif = ServiceQueryVtexDif()

  private fun updateGridProduto() {
    view.updateGridProduto()
  }
}

interface IVtexView : IView {
  fun updateGridProduto()
}


