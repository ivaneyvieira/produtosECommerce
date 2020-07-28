package br.com.astrosoft.produtosECommerce.view.main

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.tabGrid
import br.com.astrosoft.produtosECommerce.model.beans.Produto
import br.com.astrosoft.produtosECommerce.view.layout.ProdutoECommerceLayout
import br.com.astrosoft.produtosECommerce.viewmodel.IFiltroEditado
import br.com.astrosoft.produtosECommerce.viewmodel.IFiltroEditar
import br.com.astrosoft.produtosECommerce.viewmodel.IFiltroImportado
import br.com.astrosoft.produtosECommerce.viewmodel.IProdutosEComerceView
import br.com.astrosoft.produtosECommerce.viewmodel.ProcessaBean
import br.com.astrosoft.produtosECommerce.viewmodel.ProdutosEComerceViewModel
import com.github.mvysny.karibudsl.v10.TabSheet
import com.github.mvysny.karibudsl.v10.bind
import com.github.mvysny.karibudsl.v10.checkBox
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

@Route(layout = ProdutoECommerceLayout::class, value = "produto")
@PageTitle(AppConfig.title)
@HtmlImport("frontend://styles/shared-styles.html")
class ProdutosEComerceView: ViewLayout<ProdutosEComerceViewModel>(), IProdutosEComerceView {
  private var tabMain: TabSheet
  private val gridEditar = PainelGridProdutoEditar(this) {viewModel.updateGridEditar()}
  private val gridEditado = PainelGridProdutoEditado(this) {viewModel.updateGridEditado()}
  private val gridImportado = PainelGridProdutoImportado(this) {viewModel.updateGridImportado()}
  override val viewModel: ProdutosEComerceViewModel = ProdutosEComerceViewModel(this)
  
  override fun isAccept() = true
  
  init {
    tabMain = tabSheet {
      setSizeFull()
      tabGrid(TAB_EDITAR, gridEditar)
      tabGrid(TAB_EDITADO, gridEditado)
      tabGrid(TAB_IMPORTADO, gridImportado)
    }
    viewModel.updateGridEditar()
  }
  
  override fun updateGridEditar(itens: List<Produto>) {
    gridEditar.updateGrid(itens)
  }
  
  override fun updateGridEditado(itens: List<Produto>) {
    gridEditado.updateGrid(itens)
  }
  
  override fun updateGridImportado(itens: List<Produto>) {
    gridImportado.updateGrid(itens)
  }
  
  override fun processaProdutos(itens: List<Produto>) {
    if(itens.isEmpty())
      showError("Nenhum produto selecionado")
    else {
      val form = FormProcessamento()
      form.bean = ProcessaBean()
      
      showForm("Processamento de Produto", form) {
        viewModel.processaProduto(form.bean, itens)
      }
    }
  }
  
  override fun desProcessaProdutos(itens: List<Produto>) {
    if(itens.isEmpty())
      showError("Nenhum produto selecionado")
    else
      viewModel.desProcessaProduto(itens)
  }
  
  override fun salvaProduto(bean: Produto?) {
    viewModel.salvaProduto(bean)
  }
  
  override val filtroEditar: IFiltroEditar
    get() = gridEditar.filterBar as IFiltroEditar
  override val filtroEditado: IFiltroEditado
    get() = gridEditado.filterBar as IFiltroEditado
  override val filtroImportado: IFiltroImportado
    get() = gridImportado.filterBar as IFiltroImportado
  
  companion object {
    const val TAB_EDITAR: String = "Editar"
    const val TAB_EDITADO: String = "Editado"
    const val TAB_IMPORTADO: String = "Importado"
  }
}

class FormProcessamento: VerticalLayout() {
  private val binder = Binder<ProcessaBean>(ProcessaBean::class.java)
  
  init {
    width = "100%"
    horizontalLayout {
      width = "100%"
      checkBox {
        this.bind(binder)
          .bind(ProcessaBean::marcaCheck)
      }
      marcaField {
        this.bind(binder)
          .bind(ProcessaBean::marca)
      }
      checkBox {
        this.bind(binder)
          .bind(ProcessaBean::categoriaCheck)
      }
      categoriaField {
        this.bind(binder)
          .bind(ProcessaBean::categoria)
        isExpand = true
      }
    }
    horizontalLayout {
      width = "100%"
      checkBox {
        this.bind(binder)
          .bind(ProcessaBean::descricaoCompletaCheck)
      }
      textField("Descricação Completa") {
        width = "100%"
        this.bind(binder)
          .bind(ProcessaBean::descricaoCompleta)
        addThemeVariants(LUMO_SMALL)
        isExpand = true
      }
    }
    horizontalLayout {
      width = "100%"
      checkBox {
        this.bind(binder)
          .bind(ProcessaBean::bitolaCheck)
      }
      textField("Bitola") {
        this.bind(binder)
          .bind(ProcessaBean::bitola)
        addThemeVariants(LUMO_SMALL)
      }
      checkBox {
        this.bind(binder)
          .bind(ProcessaBean::imagemCheck)
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