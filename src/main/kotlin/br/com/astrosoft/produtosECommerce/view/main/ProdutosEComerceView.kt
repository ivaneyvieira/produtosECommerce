package br.com.astrosoft.produtosECommerce.view.main

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.tabGrid
import br.com.astrosoft.produtosECommerce.model.beans.EEditor
import br.com.astrosoft.produtosECommerce.model.beans.EEditor.*
import br.com.astrosoft.produtosECommerce.model.beans.FiltroProduto
import br.com.astrosoft.produtosECommerce.model.beans.Produto
import br.com.astrosoft.produtosECommerce.model.beans.UserSaci
import br.com.astrosoft.produtosECommerce.view.layout.ProdutoECommerceLayout
import br.com.astrosoft.produtosECommerce.viewmodel.IProdutosEComerceView
import br.com.astrosoft.produtosECommerce.viewmodel.ProcessaBean
import br.com.astrosoft.produtosECommerce.viewmodel.ProdutosEComerceViewModel
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
  override val viewModel: ProdutosEComerceViewModel = ProdutosEComerceViewModel(this)
  private val gridBase = PainelGridProdutoBase(this, viewModel.serviceQueryProduto())
  private val gridEditar = PainelGridProdutoEditar(this, viewModel.serviceQueryProduto())
  private val gridImportado = PainelGridProdutoImportado(this, viewModel.serviceQueryProduto())
  private val gridEditado = PainelGridProdutoEditado(this, viewModel.serviceQueryProduto())
  private val gridEnviar = PainelGridProdutoEnviar(this, viewModel.serviceQueryProduto())
  private val gridEnviado = PainelGridProdutoEnviado(this, viewModel.serviceQueryProduto())
  private val gridCorrecao = PainelGridProdutoCorrecao(this, viewModel.serviceQueryProduto())
  private val gridConferencia = PainelGridProdutoConferencia(this, viewModel.serviceQueryProdutoConferencia())

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
      }
      tabGrid(TAB_ENVIADO, gridEnviado)
      tabGrid(TAB_CORRECAO, gridCorrecao)
      if (user?.admin == true) {
        tabGrid(TAB_CONFERENCIA, gridConferencia)
      }
    }
    viewModel.updateGrid()
  }

  override fun updateGridBase() {
    gridBase.updateGrid()
  }

  override fun updateGridEditar() {
    gridEditar.updateGrid()
  }

  override fun updateGridEditado() {
    gridEditado.updateGrid()
  }

  override fun updateGridImportado() {
    gridImportado.updateGrid()
  }

  override fun updateGridEnviar() {
    gridEnviar.updateGrid()
  }

  override fun updateGridEnviado() {
    gridEnviado.updateGrid()
  }

  override fun updateGridCorrecao() {
    gridCorrecao.updateGrid()
  }

  override fun panelStatus(): EEditor {
    val id = tabMain.selectedTab?.id?.orElseGet { "" } ?: ""
    return when (id) {
      TAB_BASE -> BASE
      TAB_EDITAR -> EDITAR
      TAB_EDITADO -> EDITADO
      TAB_IMPORTADO -> IMPORTADO
      TAB_ENVIAR -> ENVIAR
      TAB_ENVIADO -> ENVIADO
      TAB_CORRECAO -> CORRECAO
      TAB_CONFERENCIA -> CONFERENCIA
      else -> BASE
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

  override val filtroEditar: FiltroProduto
    get() = gridEditar.filterBar.filtro()
  override val filtroEditado: FiltroProduto
    get() = gridEditado.filterBar.filtro()
  override val filtroBase: FiltroProduto
    get() = gridBase.filterBar.filtro()
  override val filtroImportado: FiltroProduto
    get() = gridImportado.filterBar.filtro()
  override val filtroEnviar: FiltroProduto
    get() = gridEnviar.filterBar.filtro()
  override val filtroEnviado: FiltroProduto
    get() = gridEnviado.filterBar.filtro()
  override val filtroCorrecao: FiltroProduto
    get() = gridCorrecao.filterBar.filtro()

  companion object {
    const val TAB_EDITAR: String = "Editar"
    const val TAB_EDITADO: String = "Editado"
    const val TAB_IMPORTADO: String = "Importado"
    const val TAB_BASE: String = "Base"
    const val TAB_ENVIAR: String = "Enviar"
    const val TAB_ENVIADO: String = "Enviado"
    const val TAB_CORRECAO: String = "Correção"
    const val TAB_CONFERENCIA: String = "Conferencia"
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