package br.com.astrosoft.framework.view

import br.com.astrosoft.framework.model.ILookup
import com.github.mvysny.karibudsl.v10.getAll
import com.github.mvysny.karibudsl.v10.grid
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.Column
import com.vaadin.flow.component.grid.GridVariant.LUMO_COLUMN_BORDERS
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.grid.GridVariant.LUMO_ROW_STRIPES
import com.vaadin.flow.component.orderedlayout.VerticalLayout
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
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.value.ValueChangeMode.ON_CHANGE
import java.math.BigDecimal
import java.util.*
import kotlin.reflect.KClass

abstract class PainelGrid<T: Any>(val blockUpdate: () -> Unit): VerticalLayout() {
  protected var grid: Grid<T>
  private val dataProvider = ListDataProvider<T>(mutableListOf())
  val filterBar: FilterBar by lazy {
    filterBar()
  }
  
  init {
    this.setSizeFull()
    isMargin = false
    isPadding = false
    filterBar.also {add(it)}
    grid = this.grid(dataProvider = dataProvider) {
      addThemeVariants(LUMO_COMPACT, LUMO_COLUMN_BORDERS, LUMO_ROW_STRIPES)
      this.gridConfig()
    }
  }
  
  fun singleSelect(): T? = grid.asSingleSelect().value
  fun multiSelect() = grid.asMultiSelect().value.toList()
  fun allItens() = dataProvider.getAll()
  
  protected abstract fun filterBar(): FilterBar
  
  open fun updateGrid(itens: List<T>) {
    grid.deselectAll()
    dataProvider.updateItens(itens)
  }
  
  protected abstract fun Grid<T>.gridConfig()
  
  fun Grid<T>.withEditor(classBean: KClass<T>, openEditor: (T) -> Unit, closeEditor: (T) -> Unit) {
    val binder = Binder(classBean.java)
    editor.binder = binder
    addItemDoubleClickListener {event ->
      editor.editItem(event.item)
    }
    editor.addCloseListener {event ->
      editor.refresh()
      openEditor(event.item)
    }
    editor.addCloseListener {_ ->
      closeEditor(binder.bean)
    }
    element.addEventListener("keyup") {editor.cancel()}.filter =
      "event.key === 'Escape' || event.key === 'Esc'"
  }
  
  private fun <T: ILookup> comboFieldComponente(itens: () -> List<T>): ComboBox<T> {
    return ComboBox<T>().apply {
      this.setSizeFull()
      /*
      this.setDataProvider({filter: String, offset: Int, limit: Int ->
                             itens().filter {
                               it.lookupValue.contains(filter, ignoreCase = true)
                             }
                               .subList(offset, offset + limit)
                               .stream()
                           }, {filter ->
                             itens().filter {
                               it.lookupValue.contains(filter)
                             }.size
                           })
       */
      this.setDataProvider({ item : T,  filterText: String ->
        item.lookupValue.contains(filterText, ignoreCase = true)
                           }, ListDataProvider(itens()))
      this.setItemLabelGenerator {bean ->
        bean.lookupValue
      }
      this.element.setAttribute("theme", "small")
    }
  }
  
  private fun textAreaComponente() = TextArea().apply {
    this.valueChangeMode = ON_CHANGE
    style.set("maxHeight", "50em")
    style.set("minHeight", "2em")
    addThemeVariants(TextAreaVariant.LUMO_SMALL)
    this.isAutoselect = true
    setSizeFull()
  }
  
  private fun decimalFieldComponent(): BigDecimalField {
    return BigDecimalField().apply {
      this.addThemeVariants(LUMO_ALIGN_RIGHT)
      this.setSizeFull()
      addThemeVariants(TextFieldVariant.LUMO_SMALL)
      this.valueChangeMode = ON_CHANGE
      this.isAutoselect = true
      this.locale = Locale.forLanguageTag("pt-BR")
    }
  }
  
  private fun textFieldComponente() = TextField().apply {
    addThemeVariants(LUMO_SMALL)
    this.isAutoselect = true
    setSizeFull()
  }
  
  //***********************************************************************************************
  //Editores de colunas
  //***********************************************************************************************
  protected fun Column<T>.decimalFieldEditor(): Column<T> {
    val component = decimalFieldComponent()
    grid.editor.binder.forField(component)
      .withConverter(BigDecimalToDoubleConverter())
      .bind(this.key)
    this.editorComponent = component
    return this
  }
  
  protected fun Column<T>.textAreaEditor(): Column<T> {
    val component = textAreaComponente()
    grid.editor.binder.forField(component)
      .bind(this.key)
    this.editorComponent = component
    return this
  }
  
  protected fun Column<T>.textFieldEditor(): Column<T> {
    val component = textFieldComponente()
    grid.editor.binder.forField(component)
      .bind(this.key)
    this.editorComponent = component
    return this
  }
  
  protected fun <B: ILookup> Column<T>.comboFieldEditor(itensSupplier: () -> List<B>): Column<T> {
    val component = comboFieldComponente(itensSupplier)
    grid.editor.binder.forField(component)
      .bind(this.key)
    this.editorComponent = component
    return this
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