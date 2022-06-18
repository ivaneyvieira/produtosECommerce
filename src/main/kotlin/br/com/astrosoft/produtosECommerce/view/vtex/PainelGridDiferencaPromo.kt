package br.com.astrosoft.produtosECommerce.view.vtex

import br.com.astrosoft.framework.view.*
import br.com.astrosoft.produtosECommerce.model.beans.EDiferenca
import br.com.astrosoft.produtosECommerce.model.beans.FiltroVtexDif
import br.com.astrosoft.produtosECommerce.model.beans.Promocao
import br.com.astrosoft.produtosECommerce.model.beans.Vtex
import br.com.astrosoft.produtosECommerce.model.planilha.PlanilhaVtexPreco
import br.com.astrosoft.produtosECommerce.model.saci
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryVtexDif
import br.com.astrosoft.produtosECommerce.view.main.promocaoField
import br.com.astrosoft.produtosECommerce.viewmodel.IVtexView
import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.textfield.NumberField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.ListDataProvider
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
  private lateinit var edtPromocao: ComboBox<Promocao>
  private lateinit var edtPreco: NumberField

  override fun gridPanel(dataProvider: ListDataProvider<Vtex>): Grid<Vtex> {
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
      edtPreco = numberField("Preco") {
        valueChangeMode = ValueChangeMode.TIMEOUT
        addValueChangeListener { updateGrid() }
      }
      button("Remover") {
        icon = VaadinIcon.DOWNLOAD.create()
        onLeftClick {
          val itens = grid.selectedItems.toList()
          if (itens.isEmpty()) {
            Notification.show("Não tem nenhum item selecionado")
          }
          else {
            val promocaaARemover = serviceQueryDif.promocaaARemover(itens)

            if (promocaaARemover.isEmpty()) {
              Notification.show("Não tem nenhum item selecionado")
            }
            else {
              promocaaARemover.forEach { vtex ->
                saci.removerPromocao(vtex)
              }

              serviceQueryDif.updateSaci(itens)

              updateGrid()
            }
          }
        }
      }
      edtPromocao = promocaoField {
        addValueChangeListener { updateGrid() }
      }
      button("Adicionar") {
        icon = VaadinIcon.UPLOAD.create()
        onLeftClick {
          val itens = grid.selectedItems.toList()
          if (itens.isEmpty()) {
            Notification.show("Não tem nenhum item selecionado")
          }
          else {
            val promocaaAAdicionar = serviceQueryDif.promocaaAAdicionar(itens)

            if (promocaaAAdicionar.isEmpty()) {
              Notification.show("Não tem nenhum item selecionado")
            }
            else {
              val promocao = edtPromocao.value

              if (promocao != null) {
                promocaaAAdicionar.forEach { vtex ->
                  saci.adicionarPromocao(vtex, promocao)
                }

                serviceQueryDif.updateSaci(itens)

                updateGrid()
              }
              else {
                Notification.show("Não tem nenhum promoção selecionada")
              }
            }
          }
        }
      }
    }

    override fun filtro(): FiltroVtexDif {
      return FiltroVtexDif(produto = edtProduto.value ?: "",
                           preco = edtPreco.value ?: 0.00,
                           sku = edtSku.value ?: "",
                           departamento = "",
                           categoria = "",
                           marca = "",
                           diferenca = EDiferenca.EDITOR)
    }
  }

  override fun Grid<Vtex>.gridConfig() {
    this.setSelectionMode(Grid.SelectionMode.MULTI)
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
      setHeader("Ctrl+P")
      isExpand = false
      isResizable = true
      isAutoWidth = false
      width = "100px"
      setClassNameGenerator {
        "marcaDiferenca"
      }
    }
    addColumnDouble(Vtex::precoPromoEditor) {
      setHeader("Editor")
      isExpand = false
      isResizable = true
      isAutoWidth = false
      width = "100px"
      setClassNameGenerator {
        "marcaDiferenca"
      }
    }
    addColumnDouble(Vtex::refprice) {
      setHeader("Referência")
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