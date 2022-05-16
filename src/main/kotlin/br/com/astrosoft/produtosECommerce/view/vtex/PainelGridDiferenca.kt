package br.com.astrosoft.produtosECommerce.view.vtex

import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.*
import br.com.astrosoft.produtosECommerce.model.beans.EDiferenca
import br.com.astrosoft.produtosECommerce.model.beans.FiltroVtexDif
import br.com.astrosoft.produtosECommerce.model.beans.Vtex
import br.com.astrosoft.produtosECommerce.model.planilha.PlanilhaVtexPreco
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryVtexDif
import br.com.astrosoft.produtosECommerce.viewmodel.IVtexView
import com.github.mvysny.karibudsl.v10.comboBox
import com.github.mvysny.karibudsl.v10.isExpand
import com.github.mvysny.karibudsl.v10.textField
import com.github.mvysny.karibudsl.v10.tooltip
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.combobox.ComboBox
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
class PainelGridDiferenca(val view: IVtexView, val serviceQueryDif: ServiceQueryVtexDif) :
        PainelGrid<Vtex, FiltroVtexDif>(serviceQueryDif) {
  private lateinit var edtProduto: TextField
  private lateinit var edtSku: TextField
  private lateinit var cmbDiferenca: ComboBox<EDiferenca>

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
      cmbDiferenca = comboBox<EDiferenca>("Diferença") {
        setItems(EDiferenca.values().toList())
        setItemLabelGenerator {
          it.label
        }
        value = EDiferenca.PROMO
        isAutoOpen = true
        isAllowCustomValue = false
        addValueChangeListener { updateGrid() }
      }
    }

    override fun filtro(): FiltroVtexDif {
      return FiltroVtexDif(produto = edtProduto.value ?: "",
                           sku = edtSku.value ?: "",
                           departamento = "",
                           categoria = "",
                           marca = "",
                           diferenca = cmbDiferenca.value ?: EDiferenca.PROMO)
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
    addColumnDouble(Vtex::precoCompor) {
      setHeader("P Compor")
      isExpand = false
      isResizable = true
      isAutoWidth = false
      width = "100px"
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
      setClassNameGenerator {
        val validade = it.validade.format()
        val validadeVtex = it.validadeVtex.format()
        if (validade != validadeVtex && cmbDiferenca.value == EDiferenca.DATA) "marcaDiferenca"
        else null
      }
    }
    addColumnDouble(Vtex::promoprice) {
      setHeader("Promoção")
      isExpand = false
      isResizable = true
      isAutoWidth = false
      width = "100px"
      setClassNameGenerator {
        val promoprice = it.promoprice ?: 0.00
        val promoVtex = it.promoVtex ?: 0.00
        if (promoprice != promoVtex && cmbDiferenca.value == EDiferenca.PROMO) "marcaDiferenca"
        else null
      }
    }
    addColumnDouble(Vtex::refprice) {
      setHeader("P Ref")
      isExpand = false
      isResizable = true
      isAutoWidth = false
      width = "100px"
      setClassNameGenerator {
        val refprice = it.refprice ?: 0.00
        val preco = it.preco
        if (refprice != preco && cmbDiferenca.value == EDiferenca.PRICE) "marcaDiferenca"
        else null
      }
    }
    addColumnLocalDate(Vtex::validadeVtex) {
      setHeader("Valid Vtex")
      isExpand = false
      isResizable = true
      isAutoWidth = true
      setClassNameGenerator {
        val validade = it.validade.format()
        val validadeVtex = it.validadeVtex.format()
        if (validade != validadeVtex && cmbDiferenca.value == EDiferenca.DATA) "marcaDiferenca"
        else null
      }
    }
    addColumnDouble(Vtex::promoVtex) {
      setHeader("Prom Vtex")
      isExpand = false
      isResizable = true
      isAutoWidth = false
      width = "100px"
      setClassNameGenerator {
        val promoprice = it.promoprice ?: 0.00
        val promoVtex = it.promoVtex ?: 0.00
        if (promoprice != promoVtex && cmbDiferenca.value == EDiferenca.PROMO) "marcaDiferenca"
        else null
      }
    }
    addColumnDouble(Vtex::preco) {
      setHeader("P. Vtex")
      isExpand = false
      isResizable = true
      isAutoWidth = false
      width = "100px"
      setClassNameGenerator {
        val refprice = it.refprice ?: 0.00
        val preco = it.preco
        if (refprice != preco && cmbDiferenca.value == EDiferenca.PRICE) "marcaDiferenca"
        else null
      }
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