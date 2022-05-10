package br.com.astrosoft.produtosECommerce.view.vtex

import br.com.astrosoft.framework.model.IServiceQuery
import br.com.astrosoft.framework.view.*
import br.com.astrosoft.produtosECommerce.model.beans.FiltroVtex
import br.com.astrosoft.produtosECommerce.model.beans.Vtex
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryProdutoConferencia
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryVtex
import br.com.astrosoft.produtosECommerce.viewmodel.IVtexView
import com.github.mvysny.karibudsl.v10.isExpand
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridMultiSelectionModel
import com.vaadin.flow.component.grid.GridMultiSelectionModel.SelectAllCheckboxVisibility
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.upload.FileRejectedEvent
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider
import com.vaadin.flow.data.value.ValueChangeMode
import java.io.File

class PainelGridPreco(val view: IVtexView, serviceQuery: IServiceQuery<Vtex, FiltroVtex>) :
        PainelGrid<Vtex, FiltroVtex>(serviceQuery) {
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


    override fun FilterBar<FiltroVtex>.contentBlock() {
      val (buffer, upload) = uploadFileXls()
      upload.addSucceededListener {
        val fileName = "/tmp/${it.fileName}"
        val bytes = buffer.getInputStream(it.fileName).readBytes()
        val file = File(fileName)
        file.writeBytes(bytes)
        (serviceQuery as? ServiceQueryVtex)?.readExcel(fileName)
        updateGrid()
      }

      edtSku = textField("SKU ID") {
        valueChangeMode = ValueChangeMode.TIMEOUT
        addValueChangeListener { updateGrid() }
      }
      edtProduto = textField("Produto") {
        valueChangeMode = ValueChangeMode.TIMEOUT
        addValueChangeListener { updateGrid() }
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
    val multiModel = this.selectionModel as GridMultiSelectionModel<Vtex>
    multiModel.selectAllCheckboxVisibility = SelectAllCheckboxVisibility.VISIBLE
    addColumnInt(Vtex::seq) {
      setHeader("Seq")
      isExpand = false
      isResizable = true
      isAutoWidth = true
      isSortable = false
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
    addColumnInt(Vtex::estoque) {
      setHeader("Estoque")
      isExpand = false
      isResizable = true
      isAutoWidth = false
      width = "150px"
    }
    addColumnDouble(Vtex::preco) {
      setHeader("Preco")
      isExpand = false
      isResizable = true
      isAutoWidth = false
      width = "150px"
    }
  }
}