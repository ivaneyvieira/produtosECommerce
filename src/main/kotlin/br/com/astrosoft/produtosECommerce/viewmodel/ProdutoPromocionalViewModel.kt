package br.com.astrosoft.produtosECommerce.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.produtosECommerce.model.beans.FiltroProdutosPromocional
import br.com.astrosoft.produtosECommerce.model.beans.ProdutoPromocao
import br.com.astrosoft.produtosECommerce.model.beans.Promocao

class ProdutoPromocionalViewModel(view: IProdutoPromocionalView) :
  ViewModel<IProdutoPromocionalView>(view) {
  fun updateGridSemPromocao() {
    val filtro = view.filtroSemPromocao.filtro()
    val list = ProdutoPromocao.findProdutosPromocional(filtro)
    view.updateGridSemPromocao(list)
  }
  fun updateGridComPromocao() {
    val filtro = view.filtroComPromocao.filtro()
    val list = ProdutoPromocao.findProdutosPromocional(filtro)
    view.updateGridComPromocao(list)
  }

  fun savePromocao(list: List<ProdutoPromocao>) = exec {
    list.ifEmpty { fail("Não há nenhum produto selecionado") }
    val filtro = view.filtroSemPromocao.filtro()
    val promocao = filtro.promocao ?: fail("Não existe promoção selecionada")
    ProdutoPromocao.savePromocao(promocao, list)
    updateGridSemPromocao()
  }

  fun removePromocao(list: List<ProdutoPromocao>) {
    list.ifEmpty { fail("Não há nenhum produto selecionado") }
    ProdutoPromocao.removePromocao(list)
    updateGridComPromocao()
  }
}

interface IFiltroPromocao {
  fun filtro(): FiltroProdutosPromocional
}

interface IProdutoPromocionalView : IView {
  fun updateGridSemPromocao(itens: List<ProdutoPromocao>)
  fun savePromocao(list: List<ProdutoPromocao>)

  fun updateGridComPromocao(itens: List<ProdutoPromocao>)
  fun removePromocao(list: List<ProdutoPromocao>)

  val filtroSemPromocao: IFiltroPromocao
  val filtroComPromocao: IFiltroPromocao
}