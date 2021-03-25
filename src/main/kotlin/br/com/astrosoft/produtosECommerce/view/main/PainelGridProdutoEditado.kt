package br.com.astrosoft.produtosECommerce.view.main

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.FilterBar
import br.com.astrosoft.produtosECommerce.model.beans.*
import br.com.astrosoft.produtosECommerce.model.beans.EEditor.*
import br.com.astrosoft.produtosECommerce.model.planilha.PlanilhaEcommerceNova
import br.com.astrosoft.produtosECommerce.viewmodel.IFiltroEditado
import br.com.astrosoft.produtosECommerce.viewmodel.IProdutosEComerceView
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.tooltip
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.ButtonVariant.LUMO_SMALL
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.icon.VaadinIcon.TABLE
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField
import org.vaadin.stefan.LazyDownloadButton
import java.io.ByteArrayInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PainelGridProdutoEditado(view: IProdutosEComerceView, blockUpdate: () -> Unit) :
  PainelGridProdutoAbstract(view, blockUpdate) {
  override fun statusDefault() = EDITADO

  override fun filterBar() = FilterBarEditado()

  inner class FilterBarEditado : FilterBar(), IFiltroEditado {
    private lateinit var edtCategoria: ComboBox<Categoria>
    private lateinit var edtCl: ComboBox<Cl>
    private lateinit var edtTipo: ComboBox<TypePrd>
    private lateinit var edtFornecedor: ComboBox<Fornecedor>
    private lateinit var edtDescricaoF: TextField
    private lateinit var edtDescricaoI: TextField
    private lateinit var edtCodigo: IntegerField

    override fun FilterBar.contentBlock() {
      val user = AppConfig.userSaci as? UserSaci
      button {
        icon = VaadinIcon.ARROW_CIRCLE_LEFT.create()
        addThemeVariants(LUMO_SMALL)
        onLeftClick { view.marcaProdutos(multiSelect(), EDITAR) }
        this.tooltip = "Voltar para o painel editar"
      }
      if (user?.admin == true) {
        button {
          icon = VaadinIcon.ARROW_CIRCLE_RIGHT.create()
          addThemeVariants(LUMO_SMALL)
          onLeftClick { view.marcaProdutos(multiSelect(), ENVIAR) }
          this.tooltip = "Voltar para o painel enviar"
        }
      }
      this.downloadExcel()

      edtCodigo = codigoField {
        addValueChangeListener { blockUpdate() }
      }
      edtDescricaoI = descricaoIField {
        addValueChangeListener { blockUpdate() }
      }
      edtDescricaoF = descricaoFField {
        addValueChangeListener { blockUpdate() }
      }
      edtFornecedor = fornecedorField {
        addValueChangeListener { blockUpdate() }
      }
      edtTipo = tipoField {
        addValueChangeListener { blockUpdate() }
      }
      edtCl = clField {
        addValueChangeListener { blockUpdate() }
      }
      edtCategoria = categoriaField {
        addValueChangeListener { blockUpdate() }
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
    val textTime = LocalDateTime.now().format(sdf)
    val filename = "planilha$textTime.xlsx"
    return filename
  }

  private fun HasComponents.downloadExcel() {
    val button = LazyDownloadButton(TABLE.create(), { filename() }, {
      val planilha = PlanilhaEcommerceNova()
      val bytes = planilha.grava(multiSelect().ifEmpty { allItens() })
      ByteArrayInputStream(bytes)
    })
    button.addThemeVariants(LUMO_SMALL)
    button.tooltip = "Salva a planilha"
    add(button)
  }

}

