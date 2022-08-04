package br.com.astrosoft.produtosECommerce.view.vtex

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.tabGrid
import br.com.astrosoft.produtosECommerce.model.beans.UserSaci
import br.com.astrosoft.produtosECommerce.view.layout.ProdutoECommerceLayout
import br.com.astrosoft.produtosECommerce.viewmodel.IVtexView
import br.com.astrosoft.produtosECommerce.viewmodel.VtexViewModel
import com.github.mvysny.karibudsl.v10.TabSheet
import com.github.mvysny.karibudsl.v10.tabSheet
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = ProdutoECommerceLayout::class, value = "vtex")
@PageTitle(AppConfig.title)
@HtmlImport("frontend://styles/shared-styles.html")
class VtexView : ViewLayout<VtexViewModel>(), IVtexView {
  private var tabMain: TabSheet
  override val viewModel: VtexViewModel = VtexViewModel(this)

  private val gridProduto = PainelGridProduto(this, viewModel.servicoVtexProduto())
  private val gridPreco = PainelGridPreco(this, viewModel.servicoVtexProduto())
  private val gridPromocao = PainelGridPromocao(this, viewModel.servicoVtexProduto())
  private val gridDiferenca = PainelGridDiferenca(this, viewModel.servicoVtexDiferenca())
  private val gridDiferencaPromo = PainelGridDiferencaPromo(this, viewModel.servicoVtexDiferenca())
  private val gridDiferencaRefList = PainelGridDiferencaRefList(this, viewModel.servicoVtexDiferenca())
  private val gridDiferencaPromoBase = PainelGridDiferencaPromoBase(this, viewModel.servicoVtexDiferenca())
  private val gridDiferencaBaseRef = PainelGridDiferencaBaseRef(this, viewModel.servicoVtexDiferenca())
  private val gridDiferencaPrice = PainelGridDiferencaPrice(this, viewModel.servicoVtexDiferenca())
  private val gridDiferencaCtrlD = PainelGridDiferencaCtrlD(this, viewModel.servicoVtexDiferenca())

  override fun isAccept() = true

  init {
    val user = AppConfig.userSaci as? UserSaci
    tabMain = tabSheet {
      setSizeFull()
      tabGrid("Produto", gridProduto)
      tabGrid("Preco", gridPreco)
      tabGrid("Promoção", gridPromocao) //tabGrid("Diferença", gridDiferenca)
      tabGrid("Diferença Promo", gridDiferencaPromo)
      tabGrid("Ref x Lista", gridDiferencaRefList)
      tabGrid("Base x Ref", gridDiferencaBaseRef)
      tabGrid("Promo x Base", gridDiferencaPromoBase) //tabGrid("Price x Base", gridDiferencaPrice)
      tabGrid("Ctrl+D", gridDiferencaCtrlD)
    }
    updateGridProduto()
  }

  override fun updateGridProduto() {
    gridProduto.updateGrid()
  }
}
