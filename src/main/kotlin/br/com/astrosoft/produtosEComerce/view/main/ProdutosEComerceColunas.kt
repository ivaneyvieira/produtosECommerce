package br.com.astrosoft.produtosEComerce.view.main

import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnString
import br.com.astrosoft.produtosEComerce.model.beans.Categoria
import br.com.astrosoft.produtosEComerce.model.beans.Cl
import br.com.astrosoft.produtosEComerce.model.beans.Fornecedor
import br.com.astrosoft.produtosEComerce.model.beans.Produto
import br.com.astrosoft.produtosEComerce.model.beans.TypePrd
import br.com.astrosoft.produtosEComerce.model.saci
import com.github.mvysny.karibudsl.v10.comboBox
import com.github.mvysny.karibudsl.v10.integerField
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.combobox.ComboBox.ItemFilter
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.textfield.TextFieldVariant
import com.vaadin.flow.data.renderer.TemplateRenderer

fun Grid<Produto>.colCodigo() = addColumnString(Produto::codigo) {
  setHeader("Código")
}

fun Grid<Produto>.colGrade() = addColumnString(Produto::grade) {
  setHeader("Grade")
}

fun Grid<Produto>.colBarcode() = addColumnString(Produto::barcode) {
  setHeader("Código de barras")
}

fun Grid<Produto>.colDescricao() = addColumnString(Produto::descricao) {
  setHeader("Descrição")
}

fun Grid<Produto>.colMarca() = addColumnString(Produto::marca) {
  setHeader("Marca")
}

fun Grid<Produto>.colFornecedor() = addColumnString(Produto::fornecedor) {
  setHeader("Fornecedor")
}

fun Grid<Produto>.colTipo() = addColumnString(Produto::typeName) {
  setHeader("Tipo")
}

fun Grid<Produto>.colCl() = addColumnString(Produto::clname) {
  setHeader("Cl")
}

fun Grid<Produto>.colCategoria() = addColumnInt(Produto::categoria) {
  setHeader("Categoria")
}

fun Grid<Produto>.colDescricaoCompleta() = addColumnString(Produto::descricaoCompleta) {
  setHeader("Descrição Completa")
}

fun Grid<Produto>.colBitola() = addColumnString(Produto::bitola) {
  setHeader("Bitola")
}

fun Grid<Produto>.colImagem() = addColumnString(Produto::imagem) {
  setHeader("Imagem")
}

fun Grid<Produto>.colPeso() = addColumnDouble(Produto::peso) {
  setHeader("Peso")
}

fun Grid<Produto>.colAltura() = addColumnDouble(Produto::altura) {
  setHeader("Altura")
}

fun Grid<Produto>.colComprimento() = addColumnDouble(Produto::comprimento) {
  setHeader("Comprimento")
}

fun Grid<Produto>.colLargura() = addColumnDouble(Produto::largura) {
  setHeader("Largura")
}

//
fun HasComponents.codigoField(block: IntegerField.() -> Unit = {}) = integerField("Código") {
  addThemeVariants(TextFieldVariant.LUMO_SMALL)
  block()
}

fun HasComponents.descricaoIField(block: TextField.() -> Unit = {}) = textField("Descrição Inicial") {
  addThemeVariants(TextFieldVariant.LUMO_SMALL)
  block()
}

fun HasComponents.descricaoFField(block: TextField.() -> Unit = {}) = textField("Descrição Final") {
  addThemeVariants(TextFieldVariant.LUMO_SMALL)
  block()
}

fun HasComponents.fornecedorField(block: ComboBox<Fornecedor>.() -> Unit = {}) = comboBox<Fornecedor>("Fornecedor") {
  val filter = ItemFilter {element: Fornecedor, filterString: String? ->
    filterString ?: return@ItemFilter true
    element.fornecedor.contains(filterString, ignoreCase = true) ||
    element.vendno.toString() == filterString
  }
  isClearButtonVisible = true
  this.setItems(filter, saci.listaFornecedores())
  setItemLabelGenerator {
    "${it.vendno} ${it.fornecedor}"
  }
  setRenderer(TemplateRenderer.of<Fornecedor>(
    "<div>[[item.vendno]]<br><small>[[item.fornecedor]]</small></div>")
                .withProperty("vendno", Fornecedor::vendno)
                .withProperty("fornecedor", Fornecedor::fornecedor))
  block()
  element.setAttribute("theme", "small")
}

fun HasComponents.tipoField(block: ComboBox<TypePrd>.() -> Unit = {}) = comboBox<TypePrd>("Tipo") {
  val filter = ItemFilter {element: TypePrd, filterString: String? ->
    filterString ?: return@ItemFilter true
    element.typeName.contains(filterString, ignoreCase = true) ||
    element.typeno.toString() == filterString
  }
  isClearButtonVisible = true
  this.setItems(filter, saci.listaType())
  setItemLabelGenerator {
    "${it.typeno} ${it.typeName}"
  }
  setRenderer(TemplateRenderer.of<TypePrd>(
    "<div>[[item.typeno]]<br><small>[[item.typeName]]</small></div>")
                .withProperty("typeno", TypePrd::typeno)
                .withProperty("typeName", TypePrd::typeName))
  width = "10em"
  element.setAttribute("theme", "small")
  block()
}

fun HasComponents.clField(block: ComboBox<Cl>.() -> Unit = {}) = comboBox<Cl>("Cl") {
  val filter = ItemFilter {element: Cl, filterString: String? ->
    filterString ?: return@ItemFilter true
    element.clname.contains(filterString, ignoreCase = true) ||
    element.clno.toString() == filterString
  }
  isClearButtonVisible = true
  this.setItems(filter, saci.listaCl())
  setItemLabelGenerator {
    "${it.clno} ${it.clname}"
  }
  setRenderer(TemplateRenderer.of<Cl>(
    "<div>[[item.clno]]<br><small>[[item.clname]]</small></div>")
                .withProperty("clno", Cl::clno)
                .withProperty("clname", Cl::clname))
  width="18em"
  element.setAttribute("theme", "small")
  block()
}

fun HasComponents.categoriaField(block: ComboBox<Categoria>.() -> Unit = {}) = comboBox<Categoria>("Categoria") {
  val filter = ItemFilter {element: Categoria, filterString: String? ->
    filterString ?: return@ItemFilter true
    element.descricao.contains(filterString, ignoreCase = true) ||
    element.categoriaNo.toString() == filterString
  }
  isClearButtonVisible = true
  this.setItems(filter, saci.listaCategoria())
  setItemLabelGenerator {
    "${it.categoriaNo} ${it.descricao}"
  }
  setRenderer(TemplateRenderer.of<Categoria>(
    "<div>[[item.categoriaNo]]<br><small>[[item.clname]]</small></div>")
                .withProperty("categoriaNo", Categoria::categoriaNo)
                .withProperty("descricao", Categoria::descricao))
  element.setAttribute("theme", "small")
  block()
}

