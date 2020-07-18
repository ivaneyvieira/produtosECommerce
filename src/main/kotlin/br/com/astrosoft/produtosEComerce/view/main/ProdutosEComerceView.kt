package br.com.astrosoft.produtosEComerce.view.main

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.tabGrid
import br.com.astrosoft.produtosEComerce.model.beans.Produto
import br.com.astrosoft.produtosEComerce.view.layout.ProdutoEComerceLayout
import br.com.astrosoft.produtosEComerce.viewmodel.IFiltroEditado
import br.com.astrosoft.produtosEComerce.viewmodel.IFiltroEditar
import br.com.astrosoft.produtosEComerce.viewmodel.IProdutosEComerceView
import br.com.astrosoft.produtosEComerce.viewmodel.ProdutosEComerceViewModel
import com.github.mvysny.karibudsl.v10.TabSheet
import com.github.mvysny.karibudsl.v10.tabSheet
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = ProdutoEComerceLayout::class)
@PageTitle(AppConfig.title)
@HtmlImport("frontend://styles/shared-styles.html")
class ProdutosEComerceView: ViewLayout<ProdutosEComerceViewModel>(), IProdutosEComerceView {
  private var tabMain: TabSheet
  private val gridEditar = PainelGridProdutoEditar(this) {viewModel.updateGridEditar()}
  private val gridEditado = PainelGridProdutoEditado(this) {viewModel.updateGridEditado()}
  override val viewModel: ProdutosEComerceViewModel = ProdutosEComerceViewModel(this)
  
  override fun isAccept() = true
  
  init {
    tabMain = tabSheet {
      setSizeFull()
      tabGrid(TAB_EDITAR, gridEditar)
      tabGrid(TAB_EDITADO, gridEditado)
    }
    viewModel.updateGridEditar()
  }
  
  override fun updateGridEditar(itens: List<Produto>) {
    gridEditar.updateGrid(itens)
  }
  
  override fun updateGridEditado(itens: List<Produto>) {
    gridEditado.updateGrid(itens)
  }
  
  override val filtroEditar: IFiltroEditar
    get() = gridEditar.filterBar as IFiltroEditar
  override val filtroEditado: IFiltroEditado
    get() = gridEditado.filterBar as IFiltroEditado
  
  companion object {
    const val TAB_EDITAR: String = "Editar"
    const val TAB_EDITADO: String = "Editado"
  }
}


