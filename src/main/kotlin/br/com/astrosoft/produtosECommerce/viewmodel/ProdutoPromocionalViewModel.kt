package br.com.astrosoft.produtosECommerce.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.produtosECommerce.model.beans.FiltroProdutosPromocional
import br.com.astrosoft.produtosECommerce.model.beans.ProdutoPromocao

class ProdutoPromocionalViewModel(view: IProdutoPromocionalView) :
  ViewModel<IProdutoPromocionalView>(view) {
  fun updateGrid() {
    val filtro = view.filtro.filtro()
    val list = ProdutoPromocao.findProdutosPromocional(filtro)
    view.updateGrid(list)
  }

  fun updatePromo(list: List<ProdutoPromocao>) {

  }
}

interface IFiltroPromocao {
  fun filtro(): FiltroProdutosPromocional
}

interface IProdutoPromocionalView : IView {
  fun updateGrid(itens: List<ProdutoPromocao>)
  fun updatePromo(list: List<ProdutoPromocao>)

  val filtro: IFiltroPromocao
}