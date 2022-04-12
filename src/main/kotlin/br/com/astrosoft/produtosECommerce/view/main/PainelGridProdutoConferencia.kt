package br.com.astrosoft.produtosECommerce.view.main

import br.com.astrosoft.framework.view.FilterBar
import br.com.astrosoft.framework.view.PainelGrid
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnString
import br.com.astrosoft.produtosECommerce.model.beans.FiltroProdutoConferencia
import br.com.astrosoft.produtosECommerce.model.beans.ProdutoConferencia
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryProdutoConferencia
import br.com.astrosoft.produtosECommerce.viewmodel.IProdutosEComerceView
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.upload.FileRejectedEvent
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider
import java.io.File

class PainelGridProdutoConferencia(
  val view: IProdutosEComerceView, serviceQuery: ServiceQueryProdutoConferencia,
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

    override fun FilterBar<FiltroProdutoConferencia>.contentBlock() {
      val (buffer, upload) = uploadFileXls()
      upload.addSucceededListener {
        val fileName = "/tmp/${it.fileName}"
        val bytes = buffer.getInputStream(it.fileName).readBytes()
        val file = File(fileName)
        file.writeBytes(bytes)
        (serviceQuery as? ServiceQueryProdutoConferencia)?.readExcel(fileName)
        updateGrid()
      }

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
    addColumnDouble(ProdutoConferencia::precoSaci) {
      setHeader("Preço Saci")
      isResizable = true
      isAutoWidth = true
    }
  }
}