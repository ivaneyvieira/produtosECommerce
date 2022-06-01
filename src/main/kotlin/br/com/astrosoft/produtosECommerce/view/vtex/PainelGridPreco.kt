package br.com.astrosoft.produtosECommerce.view.vtex

import br.com.astrosoft.framework.view.*
import br.com.astrosoft.produtosECommerce.model.beans.FiltroVtex
import br.com.astrosoft.produtosECommerce.model.beans.Promocao
import br.com.astrosoft.produtosECommerce.model.beans.Vtex
import br.com.astrosoft.produtosECommerce.model.planilha.PlanilhaVtexPreco
import br.com.astrosoft.produtosECommerce.model.saci
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryVtex
import br.com.astrosoft.produtosECommerce.view.main.promocaoField
import br.com.astrosoft.produtosECommerce.viewmodel.IVtexView
import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.upload.FileRejectedEvent
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider
import com.vaadin.flow.data.value.ValueChangeMode
import org.vaadin.stefan.LazyDownloadButton
import java.io.ByteArrayInputStream
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PainelGridPreco(val view: IVtexView, val serviceQueryVtex: ServiceQueryVtex) :
        PainelGrid<Vtex, FiltroVtex>(serviceQueryVtex) {
  private lateinit var edtPromocao: ComboBox<Promocao>

  override fun gridPanel(dataProvider: ConfigurableFilterDataProvider<Vtex, Void, FiltroVtex>): Grid<Vtex> {
    val grid = Grid(Vtex::class.java, false)
    grid.dataProvider = dataProvider
    return grid
  }

  override fun filterBar(): FilterBar<FiltroVtex> {
    return FilterConferencia()
  }

  inner class FilterConferencia : FilterBar<FiltroVtex>() {
    private lateinit var edtProduto: TextField
    private lateinit var edtSku: TextField

    private fun HasComponents.uploadFileXls(): Pair<MultiFileMemoryBuffer, Upload> {
      val buffer = MultiFileMemoryBuffer()
      val upload = Upload(buffer)
      val uploadButton = Button(VaadinIcon.MONEY.create())
      upload.uploadButton = uploadButton
      upload.isAutoUpload = true
      upload.isDropAllowed = false
      upload.maxFileSize = 1024 * 1024 * 1024
      upload.addFileRejectedListener { event: FileRejectedEvent ->
        println(event.errorMessage)
      }
      upload.addFailedListener { event ->
        println(event.reason.message)
      }
      add(upload)
      return Pair(buffer, upload)
    }

    override fun FilterBar<FiltroVtex>.contentBlock() {
      this.selectAll()

      val (buffer, upload) = uploadFileXls()
      upload.addSucceededListener {
        val fileName = "/tmp/${it.fileName}"
        val bytes = buffer.getInputStream(it.fileName).readBytes()
        val file = File(fileName)
        file.writeBytes(bytes)
        serviceQueryVtex.readExcelPrecoBase(fileName)
        serviceQueryVtex.readExcelPrecoList(fileName)
        updateGrid()
      }

      this.downloadExcel()

      edtSku = textField("SKU ID") {
        valueChangeMode = ValueChangeMode.TIMEOUT
        addValueChangeListener { updateGrid() }
      }
      edtProduto = textField("Produto") {
        valueChangeMode = ValueChangeMode.TIMEOUT
        addValueChangeListener { updateGrid() }
      }

      button("Atualiza dados Saci") {
        icon = VaadinIcon.DISC.create()
        onLeftClick {
          val filter =
            FiltroVtex(produto = edtProduto.value ?: "",
                       sku = edtSku.value ?: "",
                       departamento = "",
                       categoria = "",
                       marca = "")
          serviceQueryVtex.updateSaci(filter)
          updateGrid()
        }
      }
      button("Remover") {
        icon = VaadinIcon.DOWNLOAD.create()
        onLeftClick {
          val itens = grid.selectedItems.toList()
          if (itens.isEmpty()) {
            Notification.show("Não tem nenhum item selecionado")
          }
          else {
            val promocaaARemover = serviceQueryVtex.promocaaARemover(itens)

            if (promocaaARemover.isEmpty()) {
              Notification.show("Não tem nenhum item selecionado")
            }
            else {
              promocaaARemover.forEach { vtex ->
                saci.removerPromocao(vtex)
              }

              serviceQueryVtex.updateSaci(itens)

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
            val promocaaAAdicionar = serviceQueryVtex.promocaaAAdicionar(itens)

            if (promocaaAAdicionar.isEmpty()) {
              Notification.show("Não tem nenhum item selecionado")
            }
            else {
              val promocao = edtPromocao.value

              if (promocao != null) {
                promocaaAAdicionar.forEach { vtex ->
                  saci.adicionarPromocao(vtex, promocao)
                }

                serviceQueryVtex.updateSaci(itens)

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

    override fun filtro(): FiltroVtex {
      return FiltroVtex(
        produto = edtProduto.value ?: "",
        sku = edtSku.value ?: "",
        departamento = "",
        categoria = "",
        marca = "",
                       )
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
    addColumnLocalDate(Vtex::validadeVtex) {
      setHeader("Valid Vtex")
      isExpand = false
      isResizable = true
      isAutoWidth = true
    }
    addColumnDouble(Vtex::promoVtex) {
      setHeader("Prom Vtex")
      isExpand = false
      isResizable = true
      isAutoWidth = false
      width = "100px"
    }
    addColumnDouble(Vtex::preco) {
      setHeader("P. Vtex")
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