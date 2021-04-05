package br.com.astrosoft.produtosECommerce.view.promocao

import br.com.astrosoft.framework.view.FilterBar
import br.com.astrosoft.framework.view.PainelGrid
import br.com.astrosoft.produtosECommerce.model.beans.*
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryProdutoPromocional
import br.com.astrosoft.produtosECommerce.view.main.*
import br.com.astrosoft.produtosECommerce.viewmodel.IProdutoPromocionalView
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.tooltip
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon.MONEY_DEPOSIT
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider

class PainelGridProdutoComPromocao(
  val view: IProdutoPromocionalView,
  serviceQuery: ServiceQueryProdutoPromocional
) : PainelGrid<ProdutoPromocao, FiltroProdutosPromocional>(serviceQuery) {
  override fun gridPanel(
    dataProvider: ConfigurableFilterDataProvider<ProdutoPromocao,
            Void, FiltroProdutosPromocional>
  ): Grid<ProdutoPromocao> {
    val grid = Grid(ProdutoPromocao::class.java, false)
    grid.dataProvider = dataProvider
    return grid
  }

  override fun filterBar() = FilterBarBase()

  inner class FilterBarBase : FilterBar<FiltroProdutosPromocional>() {
    private lateinit var edtPromocao: ComboBox<Promocao>
    private lateinit var edtCl: ComboBox<Cl>
    private lateinit var edtTipo: ComboBox<TypePrd>
    private lateinit var edtFornecedor: ComboBox<Fornecedor>
    private lateinit var edtCodigo: IntegerField

    override fun FilterBar<FiltroProdutosPromocional>.contentBlock() {
      this.selectAll()
      button {
        icon = MONEY_DEPOSIT.create()
        addThemeVariants(ButtonVariant.LUMO_SMALL)
        onLeftClick { view.removePromocao(multiSelect()) }
        this.tooltip = "Remover os preços promocionais"
      }

      edtPromocao = promocaoField {
        addValueChangeListener { updateGrid() }
      }

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
        promocao = edtPromocao.value,
        centroLucro = edtCl.value?.clno?.toIntOrNull() ?: 0,
        tipo = edtTipo.value?.typeno ?: 0,
        fornecedor = edtFornecedor.value?.vendno ?: 0,
        codigo = edtCodigo.value?.toString() ?: "",
        temPromocao = true
      )
    }
  }

  override fun Grid<ProdutoPromocao>.gridConfig() {
    setSelectionMode(Grid.SelectionMode.MULTI)

    colCodigo()
    colDescricao()
    colValidade()
    colPrecoRef()
    colPerc()
    colPrecoPromo()
    colVendno()
    colAbrev()
    colTipo()
    colCentLucro()
    colSaldo()
  }
}