package br.com.astrosoft.produtosECommerce.view.main

import br.com.astrosoft.AppConfig
import br.com.astrosoft.produtosECommerce.model.beans.Categoria
import br.com.astrosoft.produtosECommerce.model.beans.Cl
import br.com.astrosoft.produtosECommerce.model.beans.EEditor.EDITADO
import br.com.astrosoft.produtosECommerce.model.beans.EEditor.EDITAR
import br.com.astrosoft.produtosECommerce.model.beans.Fornecedor
import br.com.astrosoft.produtosECommerce.model.beans.TypePrd
import br.com.astrosoft.produtosECommerce.model.beans.UserSaci
import br.com.astrosoft.produtosECommerce.viewmodel.IFiltroEditado
import br.com.astrosoft.produtosECommerce.viewmodel.IProdutosEComerceView
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.button.ButtonVariant.LUMO_SMALL
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField

class PainelGridProdutoEditado(view: IProdutosEComerceView, blockUpdate: () -> Unit):
  PainelGridProdutoAbstract(view, blockUpdate) {
  override fun statusDefault(): Int = EDITADO.value
  
  override fun filterBar() = FilterBarEditado()
  
  inner class FilterBarEditado: FilterBar(), IFiltroEditado {
    private lateinit var edtCategoria: ComboBox<Categoria>
    private lateinit var edtCl: ComboBox<Cl>
    private lateinit var edtTipo: ComboBox<TypePrd>
    private lateinit var edtFornecedor: ComboBox<Fornecedor>
    private lateinit var edtDescricaoF: TextField
    private lateinit var edtDescricaoI: TextField
    private lateinit var edtCodigo: IntegerField
    
    override fun FilterBar.contentBlock() {
      button {
        isVisible = (AppConfig.userSaci as? UserSaci)?.admin ?: false
        icon = VaadinIcon.ARROW_CIRCLE_LEFT.create()
        addThemeVariants(LUMO_SMALL)
        onLeftClick {view.marcaProdutos(multiSelect(), EDITAR)}
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

