package br.com.astrosoft.produtosEComerce.view.main

import br.com.astrosoft.framework.view.PainelGrid
import br.com.astrosoft.produtosEComerce.model.beans.Cl
import br.com.astrosoft.produtosEComerce.model.beans.Fornecedor
import br.com.astrosoft.produtosEComerce.model.beans.Produto
import br.com.astrosoft.produtosEComerce.model.beans.TypePrd
import br.com.astrosoft.produtosEComerce.viewmodel.IFiltroEditar
import br.com.astrosoft.produtosEComerce.viewmodel.IProdutosEComerceView
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.refresh
import com.vaadin.flow.component.HasSize
import com.vaadin.flow.component.HasValue
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.textfield.TextFieldVariant
import com.vaadin.flow.component.textfield.TextFieldVariant.LUMO_ALIGN_RIGHT
import com.vaadin.flow.data.binder.Binder

class PainelGridProdutoEditar(view: IProdutosEComerceView, blockUpdate: () -> Unit):
  PainelGrid<Produto>(view, blockUpdate) {
  override fun Grid<Produto>.gridConfig() {
    colCodigo()
    colBarcode()
    colDescricao()
    colGrade()
    val colMarca = colMarca()
    val colCategoria = colCategoria()
    val colDescricaoCompleta = colDescricaoCompleta()
    val colBitola = colBitola()
    val colImagem = colImagem()
    colPeso()
    colAltura()
    colComprimento()
    colLargura()
    val binder = Binder(Produto::class.java)
    editor.binder = binder
    editor.isBuffered = false
    val marcaField = TextField().setKeys(this) as TextField
    val categoriaField = IntegerField().setKeys(this) as IntegerField
    val descricaoCompletaField = TextField().setKeys(this) as TextField
    val bitolaField = TextField().setKeys(this) as TextField
    val imagemField = TextField().setKeys(this) as TextField
    
    editor.binder.forField(marcaField)
      .bind(Produto::marca.name)
    editor.binder.forField(categoriaField)
      .bind(Produto::categoria.name)
    editor.binder.forField(descricaoCompletaField)
      .bind(Produto::descricaoCompleta.name)
    editor.binder.forField(bitolaField)
      .bind(Produto::bitola.name)
    editor.binder.forField(imagemField)
      .bind(Produto::imagem.name)
    
    colMarca.editorComponent = marcaField
    colCategoria.editorComponent = categoriaField
    colDescricaoCompleta.editorComponent = descricaoCompletaField
    colBitola.editorComponent = bitolaField
    colImagem.editorComponent = imagemField
    
    addItemDoubleClickListener {event ->
      editor.editItem(event.item)
      marcaField.focus()
    }
    editor.addCloseListener {
      refresh()
    }
  }
  
  private fun HasValue<*, *>.setKeys(grid: Grid<Produto>): HasValue<*, *> {
    element.addEventListener("keydown") {_ ->
      grid.editor.cancel()
    }.filter = "event.key === 'Tab' && event.shiftKey"
    when(this) {
      is TextField    -> this.addThemeVariants(TextFieldVariant.LUMO_SMALL)
      is IntegerField -> {
        this.addThemeVariants(TextFieldVariant.LUMO_SMALL, LUMO_ALIGN_RIGHT)
      }
    }
    (this as? HasSize)?.setSizeFull()
    return this
  }
  
  override fun filterBar() = FilterBarEditar()
  
  inner class FilterBarEditar: FilterBar(), IFiltroEditar {
    private lateinit var edtCl: ComboBox<Cl>
    private lateinit var edtTipo: ComboBox<TypePrd>
    private lateinit var edtFornecedor: ComboBox<Fornecedor>
    private lateinit var edtDescricaoF: TextField
    private lateinit var edtDescricaoI: TextField
    private lateinit var edtCodigo: IntegerField
    
    override fun FilterBar.contentBlock() {
      button("Processar") {
        icon = VaadinIcon.COG.create()
        addThemeVariants(ButtonVariant.LUMO_SMALL)
        onLeftClick {
          val item = selectionItem()
          view.processaProdutos(item)
        }
      }
      edtCodigo = codigoField {
        addValueChangeListener {blockUpdate()}
      }
      edtDescricaoI = descricaoIField {
        addValueChangeListener {blockUpdate()}
      }
      edtDescricaoF = descricaoFField {
        addValueChangeListener {blockUpdate()}
      }
      edtFornecedor = fornecedorField {
        addValueChangeListener {blockUpdate()}
      }
      edtTipo = tipoField {
        addValueChangeListener {blockUpdate()}
      }
      edtCl = clField {
        addValueChangeListener {blockUpdate()}
      }
    }
    
    override val codigo: Int
      get() = edtCodigo.value ?: 0
    override val descricaoI: String
      get() = edtDescricaoI.value ?: ""
    override val descricaoF: String
      get() = edtDescricaoF.value ?: ""
    override val fornecedor: Fornecedor?
      get() = edtFornecedor.value
    override val type: TypePrd?
      get() = edtTipo.value
    override val cl: Cl?
      get() = edtCl.value
  }
}



