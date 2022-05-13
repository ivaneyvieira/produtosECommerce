package br.com.astrosoft.produtosECommerce.view.main

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.FilterBar
import br.com.astrosoft.produtosECommerce.model.beans.*
import br.com.astrosoft.produtosECommerce.model.beans.EEditor.*
import br.com.astrosoft.produtosECommerce.model.planilha.PlanilhaEcommerceNova
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryProduto
import br.com.astrosoft.produtosECommerce.viewmodel.IProdutosEComerceView
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.tooltip
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.ButtonVariant.LUMO_SMALL
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.icon.VaadinIcon.*
import com.vaadin.flow.component.textfield.TextField
import org.vaadin.stefan.LazyDownloadButton
import java.io.ByteArrayInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PainelGridProdutoEnviar(view: IProdutosEComerceView, serviceQuery: ServiceQueryProduto) :
        PainelGridProdutoAbstract(view, serviceQuery) {
  override fun statusDefault() = ENVIAR

  override fun filterBar() = FilterBarEnviar()

  inner class FilterBarEnviar : FilterBar<FiltroProduto>() {
    private lateinit var edtCategoria: ComboBox<Categoria>
    private lateinit var edtCl: ComboBox<Cl>
    private lateinit var edtTipo: ComboBox<TypePrd>
    private lateinit var edtFornecedor: ComboBox<Fornecedor>
    private lateinit var edtDescricaoF: TextField
    private lateinit var edtDescricaoI: TextField
    private lateinit var edtCodigo: TextField

    override fun FilterBar<FiltroProduto>.contentBlock() {
      val user = AppConfig.userSaci as? UserSaci
      this.selectAll()

      if (user?.admin == true) {
        button {
          icon = ARROW_CIRCLE_LEFT.create()
          addThemeVariants(LUMO_SMALL)
          onLeftClick { view.marcaProdutos(multiSelect(), EDITADO) }
          this.tooltip = "Voltar para o painel editado"
        }
      }
      if (user?.admin == true) {
        button {
          icon = ARROW_CIRCLE_RIGHT.create()
          addThemeVariants(LUMO_SMALL)
          onLeftClick { view.marcaProdutos(multiSelect(), ENVIADO) }
          this.tooltip = "Enviar para o painel enviado"
        }
      }
      this.downloadExcel()

      edtCodigo = listaProdutoField {
        addValueChangeListener { updateGrid() }
      }
      edtDescricaoI = descricaoIField {
        addValueChangeListener { updateGrid() }
      }
      edtDescricaoF = descricaoFField {
        addValueChangeListener { updateGrid() }
      }
      edtFornecedor = fornecedorField {
        addValueChangeListener { updateGrid() }
      }
      edtTipo = tipoField {
        addValueChangeListener { updateGrid() }
      }
      edtCl = clField {
        addValueChangeListener { updateGrid() }
      }
      edtCategoria = categoriaField {
        addValueChangeListener { updateGrid() }
      }
    }

    override fun filtro() = FiltroProduto(codigo = 0,
                                          listaProduto = edtCodigo.value ?: "",
                                          descricaoI = edtDescricaoI.value ?: "",
                                          descricaoF = edtDescricaoF.value ?: "",
                                          fornecedor = edtFornecedor.value,
                                          type = edtTipo.value,
                                          cl = edtCl.value,
                                          categoria = edtCategoria.value,
                                          editado = statusDefault())
  }

  private fun filename(): String {
    val sdf = DateTimeFormatter.ofPattern("yyMMddHHmmss")
    val textTime = LocalDateTime.now().format(sdf)
    return "planilha$textTime.xlsx"
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

