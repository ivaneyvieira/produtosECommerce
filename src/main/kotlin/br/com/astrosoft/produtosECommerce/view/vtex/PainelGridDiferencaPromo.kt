package br.com.astrosoft.produtosECommerce.view.vtex

import br.com.astrosoft.framework.view.*
import br.com.astrosoft.produtosECommerce.model.beans.EDiferenca
import br.com.astrosoft.produtosECommerce.model.beans.FiltroVtexDif
import br.com.astrosoft.produtosECommerce.model.beans.Vtex
import br.com.astrosoft.produtosECommerce.model.planilha.PlanilhaVtexPreco
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryVtexDif
import br.com.astrosoft.produtosECommerce.viewmodel.IVtexView
import com.github.mvysny.karibudsl.v10.isExpand
import com.github.mvysny.karibudsl.v10.textField
import com.github.mvysny.karibudsl.v10.tooltip
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridMultiSelectionModel
import com.vaadin.flow.component.grid.GridMultiSelectionModel.SelectAllCheckboxVisibility
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider
import com.vaadin.flow.data.value.ValueChangeMode
import org.vaadin.stefan.LazyDownloadButton
import java.io.ByteArrayInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@CssImport(value = "./styles/gridmark.css", themeFor = "vaadin-grid")
class PainelGridDiferencaPromo(val view: IVtexView, val serviceQueryDif: ServiceQueryVtexDif) :
        PainelGrid<Vtex, FiltroVtexDif>(serviceQueryDif) {
  private lateinit var edtProduto: TextField
  private lateinit var edtSku: TextField

  override fun gridPanel(dataProvider: ConfigurableFilterDataProvider<Vtex, Void, FiltroVtexDif>): Grid<Vtex> {
    val grid = Grid(Vtex::class.java, false)
    grid.dataProvider = dataProvider
    return grid
  }

  override fun filterBar(): FilterBar<FiltroVtexDif> {
    return FilterConferencia()
  }

  inner class FilterConferencia : FilterBar<FiltroVtexDif>() {
    override fun FilterBar<FiltroVtexDif>.contentBlock() {
      this.downloadExcel()

      edtSku = textField("SKU ID") {
        valueChangeMode = ValueChangeMode.TIMEOUT
        addValueChangeListener { updateGrid() }
      }
      edtProduto = textField("Produto") {
        valueChangeMode = ValueChangeMode.TIMEOUT
        addValueChangeListener { updateGrid() }
      }
    }

    override fun filtro(): FiltroVtexDif {
      return FiltroVtexDif(produto = edtProduto.value ?: "",
                           sku = edtSku.value ?: "",
                           departamento = "",
                           categoria = "",
                           marca = "",
                           diferenca = EDiferenca.EDITOR)
    }
  }

  override fun Grid<Vtex>.gridConfig() {
    this.setSelectionMode(Grid.SelectionMode.MULTI)
    val multiModel = this.selectionModel as GridMultiSelectionModel<Vtex>
    multiModel.selectAllCheckboxVisibility = SelectAllCheckboxVisibility.VISIBLE
    addColumnInt(Vtex::seq) {
      setHeader("Seq")
      isExpand = false
      isSortable = false
      isResizable = true
      isAutoWidth = false
      width = "70px"
    }
    addColumnInt(Vtex::skuId) {
      setHeader("Sku ID")
      isExpand = false
      isResizable = true
      isAutoWidth = true
    }
    addColumnInt(Vtex::idProd) {
      setHeader("Id Prod")
      isExpand = false
      isResizable = true
      isAutoWidth = true
    }
    addColumnString(Vtex::nomeSku) {
      setHeader("Nome SKU")
      isExpand = true
      isResizable = true
      isAutoWidth = true
    }
    addColumnString(Vtex::referenciaSKU) {
      setHeader("Referencia SKU")
      isExpand = false
      isResizable = true
      isAutoWidth = true
    }
    addColumnString(Vtex::codigo) {
      setHeader("Cód Saci")
      isExpand = false
      isResizable = true
      isAutoWidth = true
    }
    addColumnInt(Vtex::promono) {
      setHeader("Nº Prom")
      isExpand = false
      isResizable = true
      isAutoWidth = true
    }
    addColumnLocalDate(Vtex::validade) {
      setHeader("Validade")
      isExpand = false
      isResizable = true
      isAutoWidth = true
    }
    addColumnDouble(Vtex::promoprice) {
      setHeader("Promoção")
      isExpand = false
      isResizable = true
      isAutoWidth = false
      width = "100px"
    }
    addColumnDouble(Vtex::refprice) {
      setHeader("P Ref")
      isExpand = false
      isResizable = true
      isAutoWidth = false
      width = "100px"
    }
    addColumnDouble(Vtex::precoPromoEditor) {
      setHeader("P Ref")
      isExpand = false
      isResizable = true
      isAutoWidth = false
      width = "100px"
    }
  }

  private fun HasComponents.downloadExcel() {
    val button = LazyDownloadButton(VaadinIcon.TABLE.create(), { filename() }, {
      val planilha = PlanilhaVtexPreco()
      val bytes = planilha.grava(allItens())
      ByteArrayInputStream(bytes)
    })
    button.addThemeVariants(ButtonVariant.LUMO_SMALL)
    button.tooltip = "Salva a planilha"
    add(button)
  }

  private fun filename(): String {
    val sdf = DateTimeFormatter.ofPattern("yyMMddHHmmss")
    val textTime = LocalDateTime.now().format(sdf)
    val filename = "planilha$textTime.xlsx"
    return filename
  }
}