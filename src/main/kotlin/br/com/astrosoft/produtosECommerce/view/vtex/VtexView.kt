package br.com.astrosoft.produtosECommerce.view.vtex

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.tabGrid
import br.com.astrosoft.produtosECommerce.model.beans.EEditor.*
import br.com.astrosoft.produtosECommerce.model.beans.FiltroVtex
import br.com.astrosoft.produtosECommerce.model.beans.UserSaci
import br.com.astrosoft.produtosECommerce.view.layout.ProdutoECommerceLayout
import br.com.astrosoft.produtosECommerce.viewmodel.*
import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = ProdutoECommerceLayout::class, value = "vtex")
@PageTitle(AppConfig.title)
@HtmlImport("frontend://styles/shared-styles.html")
class VtexView : ViewLayout<VtexViewModel>(), IVtexView {
  private var tabMain: TabSheet
  override val viewModel: VtexViewModel = VtexViewModel(this)

  private val gridConferencia = PainelGridVtex(this, viewModel.servicoBase())

  override fun isAccept() = true

  init {
    val user = AppConfig.userSaci as? UserSaci
    tabMain = tabSheet {
      setSizeFull()
      tabGrid("Base", gridConferencia)
    }
    viewModel.updateGridBase()
  }

  override fun updateGridBase() {
    gridConferencia.updateGrid()
  }
}
