package br.com.astrosoft.produtosECommerce.view.promocao

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.tabGrid
import br.com.astrosoft.produtosECommerce.model.beans.ProdutoPromocao
import br.com.astrosoft.produtosECommerce.model.beans.UserSaci
import br.com.astrosoft.produtosECommerce.view.layout.ProdutoECommerceLayout
import br.com.astrosoft.produtosECommerce.viewmodel.IFiltroPromocao
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
  private val gridProdutoPromocao = PainelGridProdutoPromocao(this) { viewModel.updateGrid() }
  override val viewModel: ProdutoPromocionalViewModel = ProdutoPromocionalViewModel(this)

  override fun isAccept(): Boolean {
    val user = AppConfig.userSaci as? UserSaci
    return user?.admin == true
  }

  override fun updateGrid(itens: List<ProdutoPromocao>) {
    gridProdutoPromocao.updateGrid(itens)
  }

  override fun updatePromo(list: List<ProdutoPromocao>) {
    viewModel.updatePromo(list)
  }

  override val filtro: IFiltroPromocao
    get() = gridProdutoPromocao.filterBar as IFiltroPromocao

  init {
    val user = AppConfig.userSaci as? UserSaci
    tabMain = tabSheet {
      setSizeFull()
      tabGrid("Produtos Promocionais", gridProdutoPromocao)
    }
    viewModel.updateGrid()
  }
}