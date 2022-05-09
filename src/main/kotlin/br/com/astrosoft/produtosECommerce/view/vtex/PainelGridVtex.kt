package br.com.astrosoft.produtosECommerce.view.vtex

import br.com.astrosoft.framework.model.IServiceQuery
import br.com.astrosoft.framework.view.*
import br.com.astrosoft.produtosECommerce.model.beans.FiltroVtex
import br.com.astrosoft.produtosECommerce.model.beans.ProdutoConferencia
import br.com.astrosoft.produtosECommerce.model.beans.Vtex
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

class PainelGridVtex(val view: IVtexView, serviceQuery: IServiceQuery<Vtex, FiltroVtex>) :
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
    private lateinit var edtDepartamento: TextField
    private lateinit var edtCategoria: TextField
    private lateinit var edtMarca: TextField

    override fun FilterBar<FiltroVtex>.contentBlock() { //this.selectAll()
      edtSku = textField("SKU ID") {
        valueChangeMode = ValueChangeMode.TIMEOUT
        addValueChangeListener { updateGrid() }
      }
      edtProduto = textField("Produto") {
        valueChangeMode = ValueChangeMode.TIMEOUT
        addValueChangeListener { updateGrid() }
      }
      edtDepartamento = textField("Departamento") {
        valueChangeMode = ValueChangeMode.TIMEOUT
        addValueChangeListener { updateGrid() }
      }
      edtCategoria = textField("Categoria") {
        valueChangeMode = ValueChangeMode.TIMEOUT
        addValueChangeListener { updateGrid() }
      }
      edtMarca = textField("Marca") {
        valueChangeMode = ValueChangeMode.TIMEOUT
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

    override fun filtro(): FiltroVtex {
      return FiltroVtex(
        produto = edtProduto.value ?: "",
        sku = edtSku.value ?: "",
        departamento = edtDepartamento.value ?: "",
        categoria = edtCategoria.value ?: "",
        marca = edtMarca.value ?: "",
                       )
    }
  }

  override fun Grid<Vtex>.gridConfig() {
    this.setSelectionMode(Grid.SelectionMode.MULTI)
    val multiModel = this.selectionModel as GridMultiSelectionModel<Vtex>
    multiModel.selectAllCheckboxVisibility = SelectAllCheckboxVisibility.VISIBLE
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
      width = "100px"
      isExpand = false
      isResizable = true
      isAutoWidth = true
    }
    addColumnInt(Vtex::idDep) {
      setHeader("Id Dep")
      isExpand = false
      isResizable = true
      isAutoWidth = true
    }
    addColumnString(Vtex::nomeDepartamento) {
      setHeader("Nome Departamento")
      isExpand = false
      isResizable = true
      isAutoWidth = true
    }
    addColumnInt(Vtex::idCat) {
      setHeader("Id Cat")
      isExpand = false
      isResizable = true
      isAutoWidth = true
    }
    addColumnString(Vtex::nomeCategoria) {
      setHeader("Nome Categoria")
      isExpand = false
      isResizable = true
      isAutoWidth = true
    }
    addColumnInt(Vtex::idMarca) {
      setHeader("Id Marca")
      isExpand = false
      isResizable = true
      isAutoWidth = true
    }
    addColumnString(Vtex::nomeMarca) {
      setHeader("Nome Marca")
      isExpand = false
      isResizable = true
      isAutoWidth = true
    }
  }
}