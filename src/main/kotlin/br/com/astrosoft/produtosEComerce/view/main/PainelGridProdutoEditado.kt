package br.com.astrosoft.produtosEComerce.view.main

import br.com.astrosoft.framework.view.PainelGrid
import br.com.astrosoft.produtosEComerce.model.beans.Categoria
import br.com.astrosoft.produtosEComerce.model.beans.Cl
import br.com.astrosoft.produtosEComerce.model.beans.Fornecedor
import br.com.astrosoft.produtosEComerce.model.beans.Produto
import br.com.astrosoft.produtosEComerce.model.beans.TypePrd
import br.com.astrosoft.produtosEComerce.viewmodel.IFiltroEditado
import br.com.astrosoft.produtosEComerce.viewmodel.IFiltroEditar
import br.com.astrosoft.produtosEComerce.viewmodel.IProdutosEComerceView
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField

class PainelGridProdutoEditado(view: IProdutosEComerceView, blockUpdate: () -> Unit):
  PainelGrid<Produto>(view, blockUpdate) {
  override fun Grid<Produto>.gridConfig() {
    colCodigo()
    colBarcode()
    colDescricao()
    colGrade()
    colMarca()
    colCategoria()
    colDescricaoCompleta()
    colBitola()
    colImagem()
    colPeso()
    colAltura()
    colComprimento()
    colLargura()
  }
  
  override fun filterBar() = FilterBarEditado()
  
  inner class FilterBarEditado: FilterBar(), IFiltroEditado {
    private lateinit var edtCategoria:  ComboBox<Categoria>
    private lateinit var edtCl: ComboBox<Cl>
    private lateinit var edtTipo: ComboBox<TypePrd>
    private lateinit var edtFornecedor: ComboBox<Fornecedor>
    private lateinit var edtDescricaoF: TextField
    private lateinit var edtDescricaoI: TextField
    private lateinit var edtCodigo: IntegerField
    
    override fun FilterBar.contentBlock() {
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
      edtCategoria = categoriaField {
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
    override val categoria: Categoria?
      get() = edtCategoria.value
  }
}

