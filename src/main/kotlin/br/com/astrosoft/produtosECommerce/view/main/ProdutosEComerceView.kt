package br.com.astrosoft.produtosECommerce.view.main

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.tabGrid
import br.com.astrosoft.produtosECommerce.model.beans.EEditor
import br.com.astrosoft.produtosECommerce.model.beans.EEditor.*
import br.com.astrosoft.produtosECommerce.model.beans.Produto
import br.com.astrosoft.produtosECommerce.model.beans.UserSaci
import br.com.astrosoft.produtosECommerce.view.layout.ProdutoECommerceLayout
import br.com.astrosoft.produtosECommerce.viewmodel.*
import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextFieldVariant.LUMO_SMALL
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = ProdutoECommerceLayout::class, value = "produto")
@PageTitle(AppConfig.title)
@HtmlImport("frontend://styles/shared-styles.html")
class ProdutosEComerceView : ViewLayout<ProdutosEComerceViewModel>(), IProdutosEComerceView {
  private var tabMain: TabSheet
  private val gridBase = PainelGridProdutoBase(this) { viewModel.updateGridBase() }
  private val gridEditar = PainelGridProdutoEditar(this) { viewModel.updateGridEditar() }
  private val gridImportado = PainelGridProdutoImportado(this) { viewModel.updateGridImportado() }
  private val gridEditado = PainelGridProdutoEditado(this) { viewModel.updateGridEditado() }
  private val gridEnviar = PainelGridProdutoEnviar(this) { viewModel.updateGridEnviar() }
  private val gridEnviado = PainelGridProdutoEnviado(this) { viewModel.updateGridEnviado() }
  override val viewModel: ProdutosEComerceViewModel = ProdutosEComerceViewModel(this)

  override fun isAccept() = true

  init {
    val user = AppConfig.userSaci as? UserSaci
    tabMain = tabSheet {
      setSizeFull()
      tabGrid(TAB_BASE, gridBase)
      tabGrid(TAB_EDITAR, gridEditar)
      tabGrid(TAB_IMPORTADO, gridImportado)
      tabGrid(TAB_EDITADO, gridEditado)
      if (user?.admin == true) {
        tabGrid(TAB_ENVIAR, gridEnviar)
        tabGrid(TAB_ENVIADO, gridEnviado)
      }
    }
    viewModel.updateGrid()
  }

  override fun updateGridEditar(itens: List<Produto>) {
    gridEditar.updateGrid(itens)
  }

  override fun updateGridEditado(itens: List<Produto>) {
    gridEditado.updateGrid(itens)
  }

  override fun updateGridBase(itens: List<Produto>) {
    gridBase.updateGrid(itens)
  }

  override fun updateGridImportado(itens: List<Produto>) {
    gridImportado.updateGrid(itens)
  }

  override fun updateGridEnviar(itens: List<Produto>) {
    gridEnviar.updateGrid(itens)
  }

  override fun updateGridEnviado(itens: List<Produto>) {
    gridEnviado.updateGrid(itens)
  }

  override fun panelStatus(): EEditor {
    val id = tabMain.selectedTab?.id?.orElseGet { "" } ?: ""
    return when (id) {
      TAB_BASE      -> BASE
      TAB_EDITAR    -> EDITAR
      TAB_EDITADO   -> EDITADO
      TAB_IMPORTADO -> IMPORTADO
      TAB_ENVIAR    -> ENVIAR
      TAB_ENVIADO   -> ENVIADO
      else          -> BASE
    }
  }

  override fun marcaProdutos(itens: List<Produto>, marca: EEditor) {
    viewModel.marcaProdutos(itens, marca)
  }

  override fun salvaProduto(bean: Produto?) {
    viewModel.salvaProduto(bean)
  }

  override fun replicarProdutos(itens: List<Produto>, marca: EEditor) {
    viewModel.replicarProdutos(itens, marca)
  }

  override fun updatePromo(multiSelect: List<Produto>) {
    viewModel.updatePromo(multiSelect)
  }

  override val filtroEditar: IFiltroEditar
    get() = gridEditar.filterBar as IFiltroEditar
  override val filtroEditado: IFiltroEditado
    get() = gridEditado.filterBar as IFiltroEditado
  override val filtroBase: IFiltroBase
    get() = gridBase.filterBar as IFiltroBase
  override val filtroImportado: IFiltroImportado
    get() = gridImportado.filterBar as IFiltroImportado
  override val filtroEnviar: IFiltroEnviar
    get() = gridEnviar.filterBar as IFiltroEnviar
  override val filtroEnviado: IFiltroEnviado
    get() = gridEnviado.filterBar as IFiltroEnviado

  companion object {
    const val TAB_EDITAR: String = "Editar"
    const val TAB_EDITADO: String = "Editado"
    const val TAB_IMPORTADO: String = "Importado"
    const val TAB_BASE: String = "Base"
    const val TAB_ENVIAR: String = "Enviar"
    const val TAB_ENVIADO: String = "Enviado"
  }
}

class FormProcessamento : VerticalLayout() {
  private val binder = Binder<ProcessaBean>(ProcessaBean::class.java)

  init {
    width = "100%"
    horizontalLayout {
      width = "100%"
      checkBox {
        this.bind(binder).bind(ProcessaBean::marcaCheck)
      }
      marcaField {
        this.bind(binder).bind(ProcessaBean::marca)
      }
      checkBox {
        this.bind(binder).bind(ProcessaBean::categoriaCheck)
      }
      categoriaField {
        this.bind(binder).bind(ProcessaBean::categoria)
        isExpand = true
      }
    }
    horizontalLayout {
      width = "100%"
      checkBox {
        this.bind(binder).bind(ProcessaBean::descricaoCompletaCheck)
      }
      textField("Descricação Completa") {
        width = "100%"
        this.bind(binder).bind(ProcessaBean::descricaoCompleta)
        addThemeVariants(LUMO_SMALL)
        isExpand = true
      }
    }
    horizontalLayout {
      width = "100%"
      checkBox {
        this.bind(binder).bind(ProcessaBean::bitolaCheck)
      }
      integerField("Bitola") {
        this.bind(binder).bind(ProcessaBean::bitola)
        addThemeVariants(LUMO_SMALL)
      }
      checkBox {
        this.bind(binder).bind(ProcessaBean::imagemCheck)
      }
      textField("Imagem") {
        this.bind(binder).bind(ProcessaBean::imagem)
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