package br.com.astrosoft.produtosECommerce.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.produtosECommerce.model.beans.FiltroProdutosPromocional
import br.com.astrosoft.produtosECommerce.model.beans.ProdutoPromocao
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryProdutoPromocional

class ProdutoPromocionalViewModel(view: IProdutoPromocionalView) :
  ViewModel<IProdutoPromocionalView>(view) {
  fun serviceQueryProdutoPromocional() = ServiceQueryProdutoPromocional()

  fun savePromocao(list: List<ProdutoPromocao>) = exec {
    list.ifEmpty { fail("Não há nenhum produto selecionado") }
    val filtro = view.filtroPromocaoSaci
    val promocao = filtro.promocao ?: fail("Não existe promoção selecionada")
    ProdutoPromocao.savePromocao(promocao, list)
    view.updateGridPromocaoSaci()
  }

  fun removePromocao(list: List<ProdutoPromocao>) {
    list.ifEmpty { fail("Não há nenhum produto selecionado") }
    ProdutoPromocao.removePromocao(list)
    view.updateGridPromocaoWeb()
  }
}

interface IProdutoPromocionalView : IView {
  fun savePromocao(list: List<ProdutoPromocao>)
  fun updateGridPromocaoSaci()
  fun updateGridPromocaoWeb()
  fun updateGridPromocaoBase()

  fun removePromocao(list: List<ProdutoPromocao>)

  val filtroPromocaoSaci: FiltroProdutosPromocional
  val filtroPromocaoWeb: FiltroProdutosPromocional
  val filtroPromocaoBase: FiltroProdutosPromocional
}