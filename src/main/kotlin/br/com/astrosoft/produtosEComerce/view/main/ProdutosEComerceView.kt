package br.com.astrosoft.produtosEComerce.view.main

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.tabGrid
import br.com.astrosoft.produtosEComerce.model.beans.Produto
import br.com.astrosoft.produtosEComerce.view.layout.ProdutoEComerceLayout
import br.com.astrosoft.produtosEComerce.viewmodel.IFiltroEditado
import br.com.astrosoft.produtosEComerce.viewmodel.IFiltroEditar
import br.com.astrosoft.produtosEComerce.viewmodel.IProdutosEComerceView
import br.com.astrosoft.produtosEComerce.viewmodel.ProcessaBean
import br.com.astrosoft.produtosEComerce.viewmodel.ProdutosEComerceViewModel
import com.github.mvysny.karibudsl.v10.TabSheet
import com.github.mvysny.karibudsl.v10.bind
import com.github.mvysny.karibudsl.v10.h3
import com.github.mvysny.karibudsl.v10.horizontalLayout
import com.github.mvysny.karibudsl.v10.isExpand
import com.github.mvysny.karibudsl.v10.tabSheet
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextFieldVariant.LUMO_SMALL
import com.vaadin.flow.data.binder.Binder
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
  
  override fun processaProdutos(produto: Produto?) {
    if(produto == null)
      showError("Não há produto selecionado")
    else {
      val form = FormProcessamento()
      form.bean = ProcessaBean.fromProduto(produto)
      
      showForm("Processamento de Produto", form) {
      }
    }
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

class FormProcessamento: VerticalLayout() {
  private val binder = Binder<ProcessaBean>(ProcessaBean::class.java)
  
  init {
    h3("Processa produtos")
    width = "100%"
    horizontalLayout {
      width = "100%"
      textField("Marca") {
        this.bind(binder)
          .bind(ProcessaBean::marca)
        addThemeVariants(LUMO_SMALL)
      }
      categoriaField {
        this.bind(binder)
          .bind(ProcessaBean::categoria)
        isExpand = true
      }
    }
    textField("Descricação Completa") {
      width = "100%"
      this.bind(binder)
        .bind(ProcessaBean::descricaoCompleta)
      addThemeVariants(LUMO_SMALL)
      isExpand = true
    }
    horizontalLayout {
      width = "100%"
      textField("Bitola") {
        this.bind(binder)
          .bind(ProcessaBean::bitola)
        addThemeVariants(LUMO_SMALL)
      }
      textField("Imagem") {
        this.bind(binder)
          .bind(ProcessaBean::imagem)
        addThemeVariants(LUMO_SMALL)
        isExpand = true
      }
    }
  }
  
  var bean
    get() = binder.bean
    set(value) {
      binder.bean = value
    }
}