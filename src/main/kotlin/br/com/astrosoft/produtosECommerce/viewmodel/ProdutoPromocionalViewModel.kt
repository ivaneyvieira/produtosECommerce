package br.com.astrosoft.produtosECommerce.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.produtosECommerce.model.beans.FiltroProdutosPromocional
import br.com.astrosoft.produtosECommerce.model.beans.ProdutoPromocao
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryProdutoPromocional
import com.vaadin.flow.data.provider.CallbackDataProvider

class ProdutoPromocionalViewModel(view: IProdutoPromocionalView) :
  ViewModel<IProdutoPromocionalView>(view) {
  fun serviceQueryProdutoPromocional() = ServiceQueryProdutoPromocional()

  fun savePromocao(list: List<ProdutoPromocao>) = exec {
    list.ifEmpty { fail("Não há nenhum produto selecionado") }
    val filtro = view.filtroSemPromocao
    val promocao = filtro.promocao ?: fail("Não existe promoção selecionada")
    ProdutoPromocao.savePromocao(promocao, list)
    view.updateGridSemPromocao()
  }

  fun removePromocao(list: List<ProdutoPromocao>) {
    list.ifEmpty { fail("Não há nenhum produto selecionado") }
    ProdutoPromocao.removePromocao(list)
    view.updateGridComPromocao()
  }
}

interface IProdutoPromocionalView : IView {
  fun savePromocao(list: List<ProdutoPromocao>)
  fun updateGridSemPromocao()
  fun updateGridComPromocao()

  fun removePromocao(list: List<ProdutoPromocao>)

  val filtroSemPromocao: FiltroProdutosPromocional
  val filtroComPromocao: FiltroProdutosPromocional
}