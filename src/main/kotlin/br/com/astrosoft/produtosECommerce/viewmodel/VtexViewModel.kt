package br.com.astrosoft.produtosECommerce.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.produtosECommerce.model.beans.*
import br.com.astrosoft.produtosECommerce.model.beans.EEditor.*
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryProduto
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryProdutoConferencia
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryVtex

class VtexViewModel(view: IVtexView) : ViewModel<IVtexView>(view) {
  fun servicoBase() : ServiceQueryVtex = ServiceQueryVtex()

  fun updateGridBase() {
    view.updateGridBase()
  }
}

interface IVtexView : IView {
  fun updateGridBase()
}


