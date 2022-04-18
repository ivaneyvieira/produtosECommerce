package br.com.astrosoft.produtosECommerce.view.diferencas

import br.com.astrosoft.framework.view.FilterBar
import br.com.astrosoft.framework.view.PainelGrid
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnString
import br.com.astrosoft.produtosECommerce.model.beans.FiltroProdutoConferencia
import br.com.astrosoft.produtosECommerce.model.beans.ProdutoConferencia
import br.com.astrosoft.produtosECommerce.model.planilha.PlanilhaConferencia
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryProdutoConferencia
import br.com.astrosoft.produtosECommerce.viewmodel.IDiferencasView
import com.github.mvysny.karibudsl.v10.textField
import com.github.mvysny.karibudsl.v10.tooltip
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridMultiSelectionModel
import com.vaadin.flow.component.grid.GridMultiSelectionModel.SelectAllCheckboxVisibility
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.upload.FileRejectedEvent
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider
import org.vaadin.stefan.LazyDownloadButton
import java.io.ByteArrayInputStream
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PainelGridProdutoConferencia(
  val view: IDiferencasView, serviceQuery: ServiceQueryProdutoConferencia,
                                  ) : PainelGrid<ProdutoConferencia, FiltroProdutoConferencia>(serviceQuery) {
  override fun gridPanel(dataProvider: ConfigurableFilterDataProvider<ProdutoConferencia, Void, FiltroProdutoConferencia>): Grid<ProdutoConferencia> {
    val grid = Grid(ProdutoConferencia::class.java, false)
    grid.dataProvider = dataProvider
    return grid
  }

  override fun filterBar(): FilterBar<FiltroProdutoConferencia> {
    return FilterConferencia()
  }

  inner class FilterConferencia : FilterBar<FiltroProdutoConferencia>() {
    private lateinit var edtCodigo: TextField

    override fun FilterBar<FiltroProdutoConferencia>.contentBlock() { //this.selectAll()

      val (buffer, upload) = uploadFileXls()
      upload.addSucceededListener {
        val fileName = "/tmp/${it.fileName}"
        val bytes = buffer.getInputStream(it.fileName).readBytes()
        val file = File(fileName)
        file.writeBytes(bytes)
        (serviceQuery as? ServiceQueryProdutoConferencia)?.readExcel(fileName)
        updateGrid()
      }

      this.downloadExcel()

      edtCodigo = textField {
        addValueChangeListener { updateGrid() }
      }
    }

    private fun HasComponents.uploadFileXls(): Pair<MultiFileMemoryBuffer, Upload> {
      val buffer = MultiFileMemoryBuffer()
      val upload = Upload(buffer)
      val uploadButton = Button("Planilha")
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

    override fun filtro(): FiltroProdutoConferencia {
      return FiltroProdutoConferencia(codigo = edtCodigo.value ?: "")
    }
  }

  override fun Grid<ProdutoConferencia>.gridConfig() {
    this.setSelectionMode(Grid.SelectionMode.MULTI)
    val multiModel = this.selectionModel as GridMultiSelectionModel<ProdutoConferencia>
    multiModel.selectAllCheckboxVisibility = SelectAllCheckboxVisibility.VISIBLE
    addColumnString(ProdutoConferencia::refid) {
      setHeader("Código Site")
      isResizable = true
      isAutoWidth = true
    }
    addColumnString(ProdutoConferencia::prdno) {
      setHeader("Código Saci")
      isResizable = true
      isAutoWidth = true
    }
    addColumnString(ProdutoConferencia::grade) {
      setHeader("Grade")
      isResizable = true
      isAutoWidth = true
    }
    addColumnString(ProdutoConferencia::descricaoSite) {
      setHeader("Descricao Site")
      width = "100px"
      isResizable = true
      isAutoWidth = true
    }
    addColumnString(ProdutoConferencia::descricaoSaci) {
      setHeader("Descricao Saci")
      isResizable = true
      isAutoWidth = true
    }
    addColumnDouble(ProdutoConferencia::listPrice) {
      setHeader("Preço Site")
      isResizable = true
      isAutoWidth = true
    }
    addColumnDouble(ProdutoConferencia::precoRef) {
      setHeader("Preço Referência")
      isResizable = true
      isAutoWidth = true
    }
    addColumnDouble(ProdutoConferencia::precoPromo) {
      setHeader("Preço Promocional")
      isResizable = true
      isAutoWidth = true
    }
  }

  private fun HasComponents.downloadExcel() {
    val button = LazyDownloadButton(VaadinIcon.TABLE.create(), { filename() }, {
      val planilha = PlanilhaConferencia()
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