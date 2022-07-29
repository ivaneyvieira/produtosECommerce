package br.com.astrosoft.produtosECommerce.view.vtex

import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.*
import br.com.astrosoft.produtosECommerce.model.beans.EDiferenca
import br.com.astrosoft.produtosECommerce.model.beans.FiltroVtexDif
import br.com.astrosoft.produtosECommerce.model.beans.Vtex
import br.com.astrosoft.produtosECommerce.model.planilha.CampoInt
import br.com.astrosoft.produtosECommerce.model.planilha.CampoNumber
import br.com.astrosoft.produtosECommerce.model.planilha.CampoString
import br.com.astrosoft.produtosECommerce.model.planilha.PlanilhaVtexPreco
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryVtexDif
import br.com.astrosoft.produtosECommerce.model.xlsx.EColunaNaoEncontrada
import br.com.astrosoft.produtosECommerce.viewmodel.IVtexView
import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.textfield.NumberField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.upload.FileRejectedEvent
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MemoryBuffer
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.value.ValueChangeMode
import org.vaadin.stefan.LazyDownloadButton
import java.io.ByteArrayInputStream
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@CssImport(value = "./styles/gridmark.css", themeFor = "vaadin-grid")
class PainelGridDiferencaCompor(val view: IVtexView, val serviceQueryDif: ServiceQueryVtexDif) :
        PainelGrid<Vtex, FiltroVtexDif>(serviceQueryDif) {
  private lateinit var edtProduto: TextField
  private lateinit var edtSku: TextField
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
      val (buffer, upload) = uploadFileXls()
      upload.addSucceededListener {
        try {
          val fileName = "/tmp/${it.fileName}"
          val bytes = buffer.inputStream.readBytes()
          val file = File(fileName)
          file.writeBytes(bytes)
          serviceQueryDif.readExcelPromo(fileName)
          file.delete()
          updateGrid()
        } catch (e: EColunaNaoEncontrada) {
          showErro(e.message)
        }
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
      edtPreco = numberField("Preco") {
        valueChangeMode = ValueChangeMode.TIMEOUT
        addValueChangeListener { updateGrid() }
      }
      button("Zerar Compor") {
        icon = VaadinIcon.MINUS_CIRCLE.create()
        onLeftClick {
          val itens = grid.selectedItems.toList()
          if (itens.isEmpty()) {
            Notification.show("Não tem nenhum item selecionado")
          }
          else {
            serviceQueryDif.zeraCompor(itens)
            serviceQueryDif.updateSaci(itens)
            updateGrid()
          }
        }
      }
    }

    private fun HasComponents.uploadFileXls(): Pair<MemoryBuffer, Upload> {
      val buffer = MemoryBuffer()
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

    override fun filtro(): FiltroVtexDif {
      return FiltroVtexDif(produto = edtProduto.value ?: "",
                           preco = edtPreco.value ?: 0.00,
                           sku = edtSku.value ?: "",
                           departamento = "",
                           categoria = "",
                           marca = "",
                           diferenca = EDiferenca.COMPOR)
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
    addColumnDouble(Vtex::precoCompor) {
      setHeader("P. Compor")
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
      setClassNameGenerator {
        if (it.preco != it.promoprice && it.preco != it.precoList) "marcaDiferenca"
        else null
      }
    }
    addColumnDouble(Vtex::preco) {
      setHeader("Base")
      isExpand = false
      isResizable = true
      isAutoWidth = false
      width = "100px"
      setClassNameGenerator {
        if (it.preco != it.promoprice && it.preco != it.precoList) "marcaDiferenca"
        else null
      }
    }
    addColumnDouble(Vtex::refprice) {
      setHeader("Referência")
      isExpand = false
      isResizable = true
      isAutoWidth = false
      width = "100px"
      setClassNameGenerator {
        if (it.precoList != it.refprice) "marcaDiferenca"
        else null
      }
    }
    addColumnDouble(Vtex::precoList) {
      setHeader("Lista")
      isExpand = false
      isResizable = true
      isAutoWidth = false
      width = "100px"
      setClassNameGenerator {
        if (it.precoList != it.refprice) "marcaDiferenca"
        else null
      }
    }
  }

  private fun HasComponents.downloadExcel() {
    val button = LazyDownloadButton(VaadinIcon.TABLE.create(), { filename() }, {
      val planilha = PlanilhaVtexPreco {
        listOf(
          CampoInt("Seq") { seq ?: 0 },
          CampoString("Sku ID") { skuId.toString() },
          CampoString("Id Prod") { idProd.toString() },
          CampoString("Nome SKU") { nomeSku },
          CampoString("Ativar") { ativarSku },
          CampoString("Referencia SKU") { referenciaSKU },
          CampoString("Cód Saci") { codigo },
          CampoNumber("P. Compor") { precoCompor ?: 0.00 },
          CampoInt("Nº Prom") { promono ?: 0 },
          CampoString("Validade") { validade.format() },
          CampoNumber("Promoção") { promoprice ?: 0.00 },
          CampoNumber("Base") { preco },
          CampoNumber("Referência") { refprice ?: 0.00 },
          CampoNumber("Lista") { precoList },
              )
      }
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