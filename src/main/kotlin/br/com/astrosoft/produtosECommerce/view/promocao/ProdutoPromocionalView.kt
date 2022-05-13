package br.com.astrosoft.produtosECommerce.view.promocao

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.tabGrid
import br.com.astrosoft.produtosECommerce.model.beans.FiltroProdutosPromocional
import br.com.astrosoft.produtosECommerce.model.beans.ProdutoPromocao
import br.com.astrosoft.produtosECommerce.model.beans.UserSaci
import br.com.astrosoft.produtosECommerce.view.layout.ProdutoECommerceLayout
import br.com.astrosoft.produtosECommerce.viewmodel.IProdutoPromocionalView
import br.com.astrosoft.produtosECommerce.viewmodel.ProdutoPromocionalViewModel
import com.github.mvysny.karibudsl.v10.TabSheet
import com.github.mvysny.karibudsl.v10.tabSheet
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = ProdutoECommerceLayout::class, value = "promocao")
@PageTitle("Promoção")
@HtmlImport("frontend://styles/shared-styles.html")
class ProdutoPromocionalView : ViewLayout<ProdutoPromocionalViewModel>(), IProdutoPromocionalView {
  private var tabMain: TabSheet
  override val viewModel: ProdutoPromocionalViewModel = ProdutoPromocionalViewModel(this)
  private val gridProdutoPromocaoSaci = PainelGridProdutoPromocaoSaci(this, viewModel.serviceQueryProdutoPromocional())
  private val gridProdutoPromocaoWeb = PainelGridProdutoPromocaoWeb(this, viewModel.serviceQueryProdutoPromocional())
  private val gridProdutoPromocaoBase = PainelGridProdutoPromocaoBase(this, viewModel.serviceQueryProdutoPromocional())
  private val gridProdutoPromocaoWebInvalida =
    PainelGridProdutoPromocaoWebInvalida(this, viewModel.serviceQueryProdutoPromocional())

  override fun isAccept(): Boolean {
    val user = AppConfig.userSaci as? UserSaci
    return user?.admin == true
  }

  override fun savePromocao(list: List<ProdutoPromocao>) {
    viewModel.savePromocao(list)
  }

  override fun updateGridPromocaoSaci() {
    gridProdutoPromocaoSaci.updateGrid()
  }

  override fun updateGridPromocaoWeb() {
    gridProdutoPromocaoWeb.updateGrid()
  }

  override fun updateGridPromocaoBase() {
    gridProdutoPromocaoBase.updateGrid()
  }

  override fun removePromocao(list: List<ProdutoPromocao>) {
    viewModel.removePromocao(list)
  }

  override val filtroPromocaoWeb: FiltroProdutosPromocional
    get() = gridProdutoPromocaoWeb.filterBar.filtro()
  override val filtroPromocaoWebInvalida: FiltroProdutosPromocional
    get() = gridProdutoPromocaoWebInvalida.filterBar.filtro()
  override val filtroPromocaoBase: FiltroProdutosPromocional
    get() = gridProdutoPromocaoBase.filterBar.filtro()
  override val filtroPromocaoSaci: FiltroProdutosPromocional
    get() = gridProdutoPromocaoSaci.filterBar.filtro()

  init {
    tabMain = tabSheet {
      setSizeFull()
      tabGrid("Promoção Base", gridProdutoPromocaoBase)
      tabGrid("Promoção Saci", gridProdutoPromocaoSaci)
      tabGrid("Promoção Web", gridProdutoPromocaoWeb)
      tabGrid("Promoção Web Inválida", gridProdutoPromocaoWebInvalida)
    }
    updateGridPromocaoBase()
  }
}