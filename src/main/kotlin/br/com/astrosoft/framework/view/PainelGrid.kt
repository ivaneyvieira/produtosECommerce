package br.com.astrosoft.framework.view

import br.com.astrosoft.framework.model.ILookup
import br.com.astrosoft.produtosECommerce.model.local
import com.github.juchar.colorpicker.ColorPickerFieldI18n
import com.github.juchar.colorpicker.ColorPickerFieldRaw
import com.github.mvysny.karibudsl.v10.getAll
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.Column
import com.vaadin.flow.component.grid.GridVariant.*
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.*
import com.vaadin.flow.component.textfield.TextFieldVariant.LUMO_ALIGN_RIGHT
import com.vaadin.flow.component.textfield.TextFieldVariant.LUMO_SMALL
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.Result
import com.vaadin.flow.data.binder.ValueContext
import com.vaadin.flow.data.converter.Converter
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.value.ValueChangeMode.ON_CHANGE
import java.math.BigDecimal
import java.util.*
import kotlin.reflect.KClass

abstract class PainelGrid<T : Any>(val blockUpdate: () -> Unit) : VerticalLayout() {
  protected var grid: Grid<T>
  private val dataProvider = ListDataProvider<T>(mutableListOf())

  val filterBar: FilterBar by lazy {
    filterBar()
  }

  abstract fun gridPanel(dataProvider: ListDataProvider<T>): Grid<T>

  init {
    this.setSizeFull()
    isMargin = false
    isPadding = false
    filterBar.also { add(it) }
    grid = this.gridPanel(dataProvider = dataProvider).apply {
      addThemeVariants(LUMO_COMPACT, LUMO_COLUMN_BORDERS, LUMO_ROW_STRIPES)
      this.gridConfig()
    }
    addAndExpand(grid)
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

  fun Grid<T>.withEditor(
    classBean: KClass<T>,
    openEditor: (Binder<T>) -> Unit,
    closeEditor: (Binder<T>) -> Unit
  ) {
    val binder = Binder(classBean.java)
    editor.binder = binder
    addItemDoubleClickListener { event ->
      editor.editItem(event.item)
    }
    editor.addOpenListener { event ->
      openEditor(binder)
    }
    editor.addCloseListener { _ ->
      editor.refresh()
      openEditor(binder)
      closeEditor(binder)
    }
    element.addEventListener("keyup") { editor.cancel() }.filter =
      "event.key === 'Escape' || event.key === 'Esc'"
  }

  private fun <T : ILookup> comboFieldComponente(itens: () -> List<T>): ComboBox<T> {
    return ComboBox<T>().apply {
      this.setSizeFull()
      this.setDataProvider({ item: T, filterText: String ->
        item.lookupValue.contains(filterText, ignoreCase = true)
      }, ListDataProvider(itens()))
      this.setItemLabelGenerator { bean ->
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

  private fun textComponente() = TextField().apply {
    this.valueChangeMode = ON_CHANGE
    addThemeVariants(LUMO_SMALL)
    this.isAutoselect = true
    setSizeFull()
  }

  private fun colorPainelComponente() = ComboBox<String>().apply {
    this.setSizeFull()
    val cores = local.findCores("")
    this.setItems(cores.sortedBy { it.descricao }.map { it.descricao })
    this.isAllowCustomValue = false
    this.isClearButtonVisible = true

    setRenderer(ComponentRenderer { descricao ->
      val cor = local.findCores(descricao).firstOrNull()
      val text = Div()
      text.text = cor?.descricao ?: "Sem cor"

      val box = VerticalLayout().apply {
        if (cor == null) {
          this.element.style.remove("backgroundColor")
        } else {
          this.element.style.set("backgroundColor", cor.codigoCor)
        }
        width = "21px"
        height = "21px"
      }

      val wrapper = FlexLayout()
      text.style.set("margin-left", "0.5em")
      wrapper.add(box, text)
      wrapper
    })

    this.element.setAttribute("theme", "small")
  }

  private fun colorComponente() = ColorPickerFieldRaw().apply {
    setPinnedPalettes(true)
    isHexEnabled = true
    isAlphaEnabled = false
    isRgbEnabled = false
    isHslEnabled = false
    this.i18n = ColorPickerFieldI18n().apply {
      select = "Seleciona"
      cancel = "Cancela"
    }
    this.textField.setSizeFull() //this.setSizeFull()
    isChangeFormatButtonVisible = false
    setCssCustomPropertiesEnabled(true)
    this.setPinnedPalettes(true) //setSizeFull()
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

  protected fun Column<T>.textAreaEditor(block: TextArea.() -> Unit = {}): Column<T> {
    val component = textAreaComponente()
    component.block()
    grid.editor.binder.forField(component).bind(this.key)
    this.editorComponent = component
    return this
  }

  protected fun Column<T>.textEditor(block: TextField.() -> Unit = {}): Column<T> {
    val component = textComponente()
    component.block()
    grid.editor.binder.forField(component).bind(this.key)
    this.editorComponent = component
    return this
  }

  protected fun Column<T>.colorEditor(): Column<T> {
    val component = colorComponente()
    grid.editor.binder.forField(component).bind(this.key)
    this.editorComponent = component
    return this
  }

  protected fun Column<T>.colorPainelEditor(): Column<T> {
    val component = colorPainelComponente()
    grid.editor.binder.forField(component).bind(this.key)
    this.editorComponent = component
    return this
  }

  protected fun Column<T>.textFieldEditor(): Column<T> {
    val component = textFieldComponente()
    grid.editor.binder.forField(component).bind(this.key)
    this.editorComponent = component
    return this
  }

  protected fun <B : ILookup> Column<T>.comboFieldEditor(itensSupplier: () -> List<B>): Column<T> {
    val component = comboFieldComponente(itensSupplier)
    grid.editor.binder.forField(component).bind(this.key)
    this.editorComponent = component
    return this
  }
}

class BigDecimalToDoubleConverter : Converter<BigDecimal, Double> {
  override fun convertToPresentation(
    value: Double?,
    context: ValueContext?
  ): BigDecimal {
    value ?: return BigDecimal.valueOf(0.00)
    return BigDecimal.valueOf(value)
  }

  override fun convertToModel(
    value: BigDecimal?,
    context: ValueContext?
  ): Result<Double> {
    return Result.ok(value?.toDouble() ?: 0.00)
  }
}