package br.com.astrosoft.produtosECommerce.view.diferencas

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.tabGrid
import br.com.astrosoft.produtosECommerce.model.beans.EEditor
import br.com.astrosoft.produtosECommerce.model.beans.EEditor.*
import br.com.astrosoft.produtosECommerce.model.beans.Produto
import br.com.astrosoft.produtosECommerce.model.beans.UserSaci
import br.com.astrosoft.produtosECommerce.view.layout.ProdutoECommerceLayout
import br.com.astrosoft.produtosECommerce.view.main.ProdutosEComerceView.Companion.TAB_BASE
import br.com.astrosoft.produtosECommerce.view.main.ProdutosEComerceView.Companion.TAB_CORRECAO
import br.com.astrosoft.produtosECommerce.view.main.ProdutosEComerceView.Companion.TAB_EDITADO
import br.com.astrosoft.produtosECommerce.view.main.ProdutosEComerceView.Companion.TAB_EDITAR
import br.com.astrosoft.produtosECommerce.view.main.ProdutosEComerceView.Companion.TAB_ENVIADO
import br.com.astrosoft.produtosECommerce.view.main.ProdutosEComerceView.Companion.TAB_ENVIAR
import br.com.astrosoft.produtosECommerce.view.main.ProdutosEComerceView.Companion.TAB_IMPORTADO
import br.com.astrosoft.produtosECommerce.view.main.categoriaField
import br.com.astrosoft.produtosECommerce.view.main.marcaField
import br.com.astrosoft.produtosECommerce.viewmodel.DiferencasViewModel
import br.com.astrosoft.produtosECommerce.viewmodel.IDiferencasView
import br.com.astrosoft.produtosECommerce.viewmodel.ProcessaBean
import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextFieldVariant.LUMO_SMALL
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = ProdutoECommerceLayout::class, value = "diferencas")
@PageTitle(AppConfig.title)
@HtmlImport("frontend://styles/shared-styles.html")
class DiferencasView : ViewLayout<DiferencasViewModel>(), IDiferencasView {
  private var tabMain: TabSheet
  override val viewModel: DiferencasViewModel = DiferencasViewModel(this)

  private val gridConferencia = PainelGridProdutoConferencia(this, viewModel.serviceQueryProdutoConferencia())

  override fun isAccept() = true

  init {
    val user = AppConfig.userSaci as? UserSaci
    tabMain = tabSheet {
      setSizeFull()
      tabGrid(TAB_CONFERENCIA, gridConferencia)
    }
    viewModel.updateGrid()
  }

  override fun updateGridConferencia() {
    gridConferencia.updateGrid()
  }

  override fun panelStatus(): EEditor {
    val id = tabMain.selectedTab?.id?.orElseGet { "" } ?: ""
    return when (id) {
      TAB_BASE        -> BASE
      TAB_EDITAR      -> EDITAR
      TAB_EDITADO     -> EDITADO
      TAB_IMPORTADO   -> IMPORTADO
      TAB_ENVIAR      -> ENVIAR
      TAB_ENVIADO     -> ENVIADO
      TAB_CORRECAO    -> CORRECAO
      TAB_CONFERENCIA -> CONFERENCIA
      else            -> BASE
    }
  }

  override fun marcaProdutos(itens: List<Produto>, marca: EEditor) {
    viewModel.marcaProdutos(itens, marca)
  }

  companion object {
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