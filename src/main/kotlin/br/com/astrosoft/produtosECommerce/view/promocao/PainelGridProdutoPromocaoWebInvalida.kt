package br.com.astrosoft.produtosECommerce.view.promocao

import br.com.astrosoft.framework.view.FilterBar
import br.com.astrosoft.framework.view.PainelGrid
import br.com.astrosoft.produtosECommerce.model.beans.*
import br.com.astrosoft.produtosECommerce.model.planilha.PlanilhaPromocao
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryProdutoPromocional
import br.com.astrosoft.produtosECommerce.view.main.clField
import br.com.astrosoft.produtosECommerce.view.main.codigoField
import br.com.astrosoft.produtosECommerce.view.main.fornecedorField
import br.com.astrosoft.produtosECommerce.view.main.tipoField
import br.com.astrosoft.produtosECommerce.viewmodel.IProdutoPromocionalView
import com.github.mvysny.karibudsl.v10.tooltip
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon.TABLE
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider
import org.vaadin.stefan.LazyDownloadButton
import java.io.ByteArrayInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@CssImport("./styles/gridmark.css")
class PainelGridProdutoPromocaoWebInvalida(
  val view: IProdutoPromocionalView, serviceQuery: ServiceQueryProdutoPromocional
                                          ) : PainelGrid<ProdutoPromocao, FiltroProdutosPromocional>(serviceQuery) {
  override fun gridPanel(
    dataProvider: ConfigurableFilterDataProvider<ProdutoPromocao, Void, FiltroProdutosPromocional>
                        ): Grid<ProdutoPromocao> {
    val grid = Grid(ProdutoPromocao::class.java, false)
    grid.dataProvider = dataProvider
    return grid
  }

  override fun filterBar() = FilterBarBase()

  inner class FilterBarBase : FilterBar<FiltroProdutosPromocional>() {
    private lateinit var edtCl: ComboBox<Cl>
    private lateinit var edtTipo: ComboBox<TypePrd>
    private lateinit var edtFornecedor: ComboBox<Fornecedor>
    private lateinit var edtCodigo: IntegerField

    override fun FilterBar<FiltroProdutosPromocional>.contentBlock() {
      this.selectAll()

      this.downloadExcel()

      edtCodigo = codigoField {
        addValueChangeListener { updateGrid() }
      }

      edtFornecedor = fornecedorField {
        addValueChangeListener { updateGrid() }
      }
      edtTipo = tipoField {
        addValueChangeListener { updateGrid() }
      }
      edtCl = clField {
        addValueChangeListener { updateGrid() }
      }
    }

    override fun filtro(): FiltroProdutosPromocional {
      return FiltroProdutosPromocional(
        promocao = null,
        centroLucro = edtCl.value?.clno?.toIntOrNull() ?: 0,
        tipo = edtTipo.value?.typeno ?: 0,
        fornecedor = edtFornecedor.value?.vendno ?: 0,
        codigo = edtCodigo.value?.toString() ?: "",
        tipoPainel = ETipoPainel.INVALIDO,
                                      )
    }
  }

  override fun Grid<ProdutoPromocao>.gridConfig() {
    setSelectionMode(Grid.SelectionMode.MULTI)

    colCodigo()
    colDescricao()
    colPromoNo()
    colValidade()
    colValidadeWeb()
    colPrecoRef()
    colPerc()
    colPrecoPromo()
    colPrecoPromoWeb()
    colVendno()
    colAbrev()
    colTipo()
    colCentLucro()
    colSaldo()
  }

  private fun filename(): String {
    val sdf = DateTimeFormatter.ofPattern("yyMMddHHmmss")
    val textTime = LocalDateTime.now().format(sdf)
    val filename = "planilha$textTime.xlsx"
    return filename
  }

  private fun HasComponents.downloadExcel() {
    val button = LazyDownloadButton(TABLE.create(), { filename() }, {
      val planilha = PlanilhaPromocao()
      val bytes = planilha.grava(allItens())
      ByteArrayInputStream(bytes)
    })
    button.addThemeVariants(ButtonVariant.LUMO_SMALL)
    button.tooltip = "Salva a planilha"
    add(button)
  }
}