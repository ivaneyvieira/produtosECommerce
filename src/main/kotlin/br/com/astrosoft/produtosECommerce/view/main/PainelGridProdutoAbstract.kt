package br.com.astrosoft.produtosECommerce.view.main

import br.com.astrosoft.framework.view.PainelGrid
import br.com.astrosoft.produtosECommerce.model.beans.Bitola
import br.com.astrosoft.produtosECommerce.model.beans.Categoria
import br.com.astrosoft.produtosECommerce.model.beans.ILookup
import br.com.astrosoft.produtosECommerce.model.beans.Marca
import br.com.astrosoft.produtosECommerce.model.beans.Produto
import br.com.astrosoft.produtosECommerce.viewmodel.IProdutosEComerceView
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode.MULTI
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.textfield.BigDecimalField
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextAreaVariant
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.textfield.TextFieldVariant
import com.vaadin.flow.component.textfield.TextFieldVariant.LUMO_ALIGN_RIGHT
import com.vaadin.flow.component.textfield.TextFieldVariant.LUMO_SMALL
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.Result
import com.vaadin.flow.data.binder.ValueContext
import com.vaadin.flow.data.converter.Converter
import com.vaadin.flow.data.provider.SortDirection
import com.vaadin.flow.data.value.ValueChangeMode.ON_CHANGE
import com.vaadin.flow.dom.DomEvent
import java.math.BigDecimal
import java.util.*

abstract class PainelGridProdutoAbstract(view: IProdutosEComerceView, blockUpdate: () -> Unit):
  PainelGrid<Produto>(view, blockUpdate) {
  override fun Grid<Produto>.gridConfig() {
    setSelectionMode(MULTI)
    colCodigo()
    colBarcode()
    val colDescricao = colDescricao()
    val colDescricaoCompleta = colDescricaoCompleta()
    val colBitola = colBitola()
    colGrade()
    val colMarca = colMarca()
    //
    val colCategoria = colCategoria()
    //
    val colPeso = colPeso()
    val colAltura = colAltura()
    val colLargura = colLargura()
    val colComprimento = colComprimento()
    val colImagem = colImagem()
    val colTexLink = colTexLink()
    val colEspecificacoes = colEspecificacoes()
    //
    val binder = Binder(Produto::class.java)
    editor.binder = binder
    //
    val descricaoCompletaField = textAreaComponente()
    val bitolaField = comboFieldComponente {
      Bitola.findAll()
        .sortedBy {it.lookupValue}
    }
    val imagemField = textFieldComponente()
    val marcaField = comboFieldComponente {
      Marca.findAll()
        .sortedBy {it.lookupValue}
    }
    val categoriaField = comboFieldComponente {
      Categoria.findAll()
        .sortedBy {it.lookupValue}
    }
    val pesoField = decimalFieldComponent()
    val alturaField = decimalFieldComponent()
    val comprimentoField = decimalFieldComponent()
    val larguraField = decimalFieldComponent()
    val textLinkField = textFieldComponente()
    val especificacoesField = textFieldComponente()
    
    binder.forField(descricaoCompletaField)
      .bind(Produto::descricaoCompleta.name)
    binder.forField(bitolaField)
      .bind(Produto::bitolaBean.name)
    binder.forField(marcaField)
      .bind(Produto::marcaBean.name)
    binder.forField(categoriaField)
      .bind(Produto::categoriaBean.name)
    binder.forField(imagemField)
      .bind(Produto::imagem.name)
    binder.forField(pesoField)
      .withConverter(BigDecimalToDoubleConverter())
      .bind(Produto::peso.name)
    binder.forField(comprimentoField)
      .withConverter(BigDecimalToDoubleConverter())
      .bind(Produto::comprimento.name)
    binder.forField(larguraField)
      .withConverter(BigDecimalToDoubleConverter())
      .bind(Produto::largura.name)
    binder.forField(alturaField)
      .withConverter(BigDecimalToDoubleConverter())
      .bind(Produto::altura.name)
    binder.forField(textLinkField)
      .bind(Produto::textLink.name)
    binder.forField(especificacoesField)
      .bind(Produto::especificacoes.name)
    
    colDescricaoCompleta.editorComponent = descricaoCompletaField
    colBitola.editorComponent = bitolaField
    colImagem.editorComponent = imagemField
    colMarca.editorComponent = marcaField
    colCategoria.editorComponent = categoriaField
    colPeso.editorComponent = pesoField
    colComprimento.editorComponent = comprimentoField
    colLargura.editorComponent = larguraField
    colAltura.editorComponent = alturaField
    colTexLink.editorComponent = textLinkField
    colEspecificacoes.editorComponent = especificacoesField
    
    addItemDoubleClickListener {event ->
      event.item.editado = statusDefault()
      editor.editItem(event.item)
      descricaoCompletaField.focus()
    }
    editor.addCloseListener {
      editor.refresh()
    }
    editor.addCloseListener {_ ->
      view.salvaProduto(binder.bean)
    }
    element.addEventListener("keyup") {_: DomEvent? -> editor.cancel()}.filter =
      "event.key === 'Escape' || event.key === 'Esc'"
    
    this.sort(listOf(GridSortOrder(colDescricao, SortDirection.ASCENDING)))
  }
  
  abstract fun statusDefault(): Int
  
  fun <T: ILookup> comboFieldComponente(itens: () -> List<T>): ComboBox<T> {
    return ComboBox<T>().apply {
      this.setSizeFull()
      this.setDataProvider({filter: String, offset: Int, limit: Int ->
                             itens().filter {
                               it.lookupValue.contains(filter)
                             }
                               .stream()
                           }, {filter ->
                             itens().filter {
                               it.lookupValue.contains(filter)
                             }.size
                           })
      this.setItemLabelGenerator {bean ->
        bean.lookupValue
      }
      this.element.setAttribute("theme", "small")
    }
  }
  
  private fun textAreaComponente() = TextArea().apply {
    this.valueChangeMode = ON_CHANGE
    style.set("maxHeight", "50em");
    style.set("minHeight", "2em");
    addThemeVariants(TextAreaVariant.LUMO_SMALL)
    setSizeFull()
  }
  
  private fun decimalFieldComponent(): BigDecimalField {
    return BigDecimalField().apply {
      this.addThemeVariants(LUMO_ALIGN_RIGHT)
      this.setSizeFull()
      addThemeVariants(TextFieldVariant.LUMO_SMALL)
      this.valueChangeMode = ON_CHANGE
      this.locale = Locale.forLanguageTag("pt-BR")
    }
  }
  
  private fun textFieldComponente() = TextField().apply {
    addThemeVariants(LUMO_SMALL)
    setSizeFull()
  }
}

class BigDecimalToDoubleConverter: Converter<BigDecimal, Double> {
  override fun convertToPresentation(value: Double?, context: ValueContext?): BigDecimal {
    value ?: return BigDecimal.valueOf(0.00)
    return BigDecimal.valueOf(value)
  }
  
  override fun convertToModel(value: BigDecimal?, context: ValueContext?): Result<Double> {
    return Result.ok(value?.toDouble() ?: 0.00)
  }
}