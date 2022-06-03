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
  private lateinit var colValidadeVtex: Grid.Column<Vtex>
  private lateinit var colPromoprice: Grid.Column<Vtex>
  private lateinit var colPromoVtex: Grid.Column<Vtex>
  private lateinit var colValidade: Grid.Column<Vtex>
  private lateinit var colPromono: Grid.Column<Vtex>
  private lateinit var colRefprice: Grid.Column<Vtex>
  private lateinit var colPreco: Grid.Column<Vtex>
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
        setItems(listOf(EDiferenca.PROMO, EDiferenca.DATA, EDiferenca.PRICE))
        setItemLabelGenerator {
          it.label
        }
        value = EDiferenca.PROMO
        isAutoOpen = true
        isAllowCustomValue = false
        addValueChangeListener {
          updateGrid()
          updateCols(it.value)
        }
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
    addColumnString(Vtex::ativarSku) {
      setHeader("Ativar")
      isExpand = false
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
    colPromono = addColumnInt(Vtex::promono) {
      setHeader("Nº Prom")
      isExpand = false
      isResizable = true
      isAutoWidth = true
    }
    colValidade = addColumnLocalDate(Vtex::validade) {
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
    colPromoprice = addColumnDouble(Vtex::promoprice) {
      setHeader("Promoção")
      isExpand = false
      isResizable = true
      isAutoWidth = false
      width = "100px"
      setClassNameGenerator {
        val price = it.preco
        val promoprice = it.promoprice ?: 0.00
        val promoVtex = it.promoVtex ?: 0.00

        val testePromo = promoprice != promoVtex && cmbDiferenca.value == EDiferenca.PROMO
        val testePreco = promoprice != price && cmbDiferenca.value == EDiferenca.PRICE
        if (testePreco || testePromo) "marcaDiferenca"
        else null
      }
    }
    colRefprice = addColumnDouble(Vtex::refprice) {
      setHeader("P Ref")
      isVisible = false
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
    colValidadeVtex = addColumnLocalDate(Vtex::validadeVtex) {
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
    colPromoVtex = addColumnDouble(Vtex::promoVtex) {
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
    colPreco = addColumnDouble(Vtex::preco) {
      setHeader("P. Vtex")
      isExpand = false
      isResizable = true
      isAutoWidth = false
      width = "100px"
      setClassNameGenerator {
        val refprice = it.refprice ?: 0.00
        val preco = it.preco
        val promoprice = it.promoprice ?: 0.00

        val testePromo = promoprice != 0.00 && preco != promoprice
        val testePreco = promoprice == 0.00 && preco != refprice
        if ((testePreco || testePromo) && (cmbDiferenca.value == EDiferenca.PRICE)) "marcaDiferenca"
        else null
      }
    }
    updateCols(cmbDiferenca.value)
  }

  private fun updateCols(value: EDiferenca?) {
    value ?: return
    when (value) {
      EDiferenca.PRICE  -> {
        colValidadeVtex.isVisible = false
        colPromoprice.isVisible = false
        colPromoVtex.isVisible = false
        colValidade.isVisible = false
        colPromono.isVisible = false
        colPreco.isVisible = true
        colRefprice.isVisible = true
      }
      EDiferenca.PROMO  -> {
        colValidadeVtex.isVisible = true
        colPromoprice.isVisible = true
        colPromoVtex.isVisible = true
        colValidade.isVisible = true
        colPromono.isVisible = true
        colPreco.isVisible = false
        colRefprice.isVisible = false
      }
      EDiferenca.DATA   -> {
        colValidadeVtex.isVisible = true
        colPromoprice.isVisible = true
        colPromoVtex.isVisible = true
        colValidade.isVisible = true
        colPromono.isVisible = true
        colPreco.isVisible = false
        colRefprice.isVisible = false
      }
      EDiferenca.EDITOR -> {
        colValidadeVtex.isVisible = true
        colPromoprice.isVisible = true
        colPromoVtex.isVisible = true
        colValidade.isVisible = true
        colPromono.isVisible = true
        colPreco.isVisible = false
        colRefprice.isVisible = false
      }
      EDiferenca.LIST   -> {
        colValidadeVtex.isVisible = true
        colPromoprice.isVisible = true
        colPromoVtex.isVisible = true
        colValidade.isVisible = true
        colPromono.isVisible = true
        colPreco.isVisible = false
        colRefprice.isVisible = false
      }
      else              -> {
        colValidadeVtex.isVisible = true
        colPromoprice.isVisible = true
        colPromoVtex.isVisible = true
        colValidade.isVisible = true
        colPromono.isVisible = true
        colPreco.isVisible = false
        colRefprice.isVisible = false
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