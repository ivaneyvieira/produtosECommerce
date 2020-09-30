package br.com.astrosoft.produtosECommerce.view.main

import br.com.astrosoft.framework.view.FilterBar
import br.com.astrosoft.produtosECommerce.model.beans.Categoria
import br.com.astrosoft.produtosECommerce.model.beans.Cl
import br.com.astrosoft.produtosECommerce.model.beans.EEditor.EDITADO
import br.com.astrosoft.produtosECommerce.model.beans.EEditor.EDITAR
import br.com.astrosoft.produtosECommerce.model.beans.Fornecedor
import br.com.astrosoft.produtosECommerce.model.beans.TypePrd
import br.com.astrosoft.produtosECommerce.model.planilha.PlanilhaEcommerceNova
import br.com.astrosoft.produtosECommerce.viewmodel.IFiltroEditado
import br.com.astrosoft.produtosECommerce.viewmodel.IProdutosEComerceView
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.tooltip
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant.LUMO_SMALL
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.icon.VaadinIcon.TABLE
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.server.InputStreamFactory
import com.vaadin.flow.server.StreamResource
import org.vaadin.olli.FileDownloadWrapper
import org.vaadin.stefan.LazyDownloadButton
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PainelGridProdutoEditado(view: IProdutosEComerceView, blockUpdate: () -> Unit):
  PainelGridProdutoAbstract(view, blockUpdate) {
  override fun statusDefault() = EDITADO
  
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
        icon = VaadinIcon.ARROW_CIRCLE_LEFT.create()
        addThemeVariants(LUMO_SMALL)
        onLeftClick {view.marcaProdutos(multiSelect(), EDITAR)}
        this.tooltip = "Voltar para o painel editar"
      }
      this.buttonDownloadLazy()
      
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
  
  private fun filename(): String {
    val sdf = DateTimeFormatter.ofPattern("yyMMddHHmmss")
    val textTime =
      LocalDateTime.now()
        .format(sdf)
    val filename = "planilha$textTime.xlsx"
    return filename
  }
  
  private fun HasComponents.buttonDownloadLazy() {
    val button = LazyDownloadButton(TABLE.create(),
                                    {filename()},
                                    {
                                      val planilha = PlanilhaEcommerceNova()
                                      val bytes = planilha.grava(allItens())
                                      ByteArrayInputStream(bytes)
                                    }
                                   )
    button.addThemeVariants(LUMO_SMALL)
    button.tooltip = "Salva a planilha"
    add(button)
  }
  
  private fun HasComponents.buttonDownload() {
    val button = Button().apply {
      icon = TABLE.create()
      addThemeVariants(LUMO_SMALL)
      this.tooltip = "Salva a planilha"
    }
    val stream = StreamResource(filename(), ConverteByte {
      val planilha = PlanilhaEcommerceNova()
      planilha.grava(allItens())
    })
    val buttonWrapper = FileDownloadWrapper(stream)
    buttonWrapper.wrapComponent(button)
    this.add(buttonWrapper)
  }
}

class ConverteByte(val bytesBoletos: () -> ByteArray): InputStreamFactory {
  override fun createInputStream(): InputStream {
    return ByteArrayInputStream(bytesBoletos())
  }
}