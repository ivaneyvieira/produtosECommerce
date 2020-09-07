package br.com.astrosoft.produtosECommerce.view.cruds


import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.listOrder
import br.com.astrosoft.produtosECommerce.model.beans.Categoria
import br.com.astrosoft.produtosECommerce.model.planilha.PlanilhaCategoria
import br.com.astrosoft.produtosECommerce.view.layout.ProdutoECommerceLayout
import br.com.astrosoft.produtosECommerce.view.user.UserCrudFormFactory.Companion.TITLE
import br.com.astrosoft.produtosECommerce.viewmodel.CategoriaViewModel
import br.com.astrosoft.produtosECommerce.viewmodel.ICategoriaView
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome
import com.github.mvysny.karibudsl.v10.alignSelf
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.formLayout
import com.github.mvysny.karibudsl.v10.getColumnBy
import com.github.mvysny.karibudsl.v10.h3
import com.github.mvysny.karibudsl.v10.horizontalLayout
import com.github.mvysny.karibudsl.v10.hr
import com.github.mvysny.karibudsl.v10.integerField
import com.github.mvysny.karibudsl.v10.responsiveSteps
import com.github.mvysny.karibudsl.v10.textField
import com.github.mvysny.karibudsl.v10.tooltip
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant.LUMO_ERROR
import com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.GridVariant.LUMO_COLUMN_BORDERS
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.grid.GridVariant.LUMO_ROW_STRIPES
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.END
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.textfield.TextFieldVariant.LUMO_ALIGN_RIGHT
import com.vaadin.flow.component.textfield.TextFieldVariant.LUMO_SMALL
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import org.claspina.confirmdialog.ConfirmDialog
import org.vaadin.crudui.crud.CrudOperation
import org.vaadin.crudui.crud.CrudOperation.ADD
import org.vaadin.crudui.crud.CrudOperation.DELETE
import org.vaadin.crudui.crud.CrudOperation.READ
import org.vaadin.crudui.crud.CrudOperation.UPDATE
import org.vaadin.crudui.crud.impl.GridCrud
import org.vaadin.crudui.form.AbstractCrudFormFactory
import org.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout
import org.vaadin.crudui.layout.impl.WindowBasedCrudLayout
import org.vaadin.stefan.LazyDownloadButton
import java.io.ByteArrayInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.function.*

@Route(layout = ProdutoECommerceLayout::class, value = "categoria")
@PageTitle(TITLE)
class CategoriaView: ViewLayout<CategoriaViewModel>(), ICategoriaView {
  private val crud: GridCrud<Categoria>
  override val viewModel = CategoriaViewModel(this)
  
  override fun isAccept() = true
  
  init {
    form("Editor de categorias")
    setSizeFull()
    crud = gridCrud()
    // layout configuration
    setSizeFull()
    this.add(crud)
    // logic configuration
    setOperation(crud)
  }
  
  private fun gridCrud(): GridCrud<Categoria> {
    val crud: GridCrud<Categoria> = GridCrud(Categoria::class.java, HorizontalSplitCrudLayout())
    crud.grid
      .setColumns(Categoria::categoriaNo.name,
                  Categoria::grupo.name,
                  Categoria::departamento.name,
                  Categoria::secao.name)
    crud.grid.getColumnBy(Categoria::categoriaNo)
      .apply {
        this.setHeader("Número")
        this.setTextAlign(ColumnTextAlign.END)
      }
    
    crud.grid.getColumnBy(Categoria::grupo)
      .setHeader("Grupo")
    crud.grid.getColumnBy(Categoria::departamento)
      .setHeader("Departamento")
    crud.grid.getColumnBy(Categoria::secao)
      .setHeader("Seção")
    
    crud.grid.addThemeVariants(LUMO_COMPACT, LUMO_ROW_STRIPES, LUMO_COLUMN_BORDERS)
    
    crud.crudFormFactory = CategoriaCrudFormFactory()
    crud.setSizeFull()
    (crud.crudLayout as? WindowBasedCrudLayout)?.setFormWindowWidth("30em")
    crud.crudLayout.addToolbarComponent(buttonDownloadLazy())
    return crud
  }
  
  private fun setOperation(crud: GridCrud<Categoria>) {
    crud.setOperations(
      {viewModel.findAll()},
      {user: Categoria -> viewModel.add(user)},
      {user: Categoria? -> viewModel.update(user)},
      {user: Categoria? -> viewModel.delete(user)})
  }
  
  inner class CategoriaCrudFormFactory: AbstractCrudFormFactory<Categoria>() {
    private lateinit var edtSecao: TextField
    private lateinit var edtDepartamento: TextField
    private lateinit var edtGrupo: TextField
    private lateinit var edtNumero: IntegerField
    private var _newInstanceSupplier: Supplier<Categoria?>? = null
    
    override fun buildNewForm(operation: CrudOperation?,
                              domainObject: Categoria?,
                              readOnly: Boolean,
                              cancelButtonClickListener: ComponentEventListener<ClickEvent<Button>>?,
                              operationButtonClickListener: ComponentEventListener<ClickEvent<Button>>?): Component {
      val binder = Binder<Categoria>(Categoria::class.java)
      return VerticalLayout().apply {
        isSpacing = false
        isMargin = false
        isPadding = false
        formLayout {
          this.responsiveSteps {
            "0px"(1, top)
            "10em"(4, aside)
          }
          h3(createCaption(operation)) {
            colspan = 4
          }
          edtNumero = integerField("Número") {
            binder.bind(this, Categoria::categoriaNo.name)
            addThemeVariants(LUMO_ALIGN_RIGHT)
            //this.isEnabled = operation != ADD
            width = "10em"
            this.isReadOnly = readOnly
            this.isAutoselect = true
            this.addThemeVariants(LUMO_SMALL)
            this.focus()
          }
          edtGrupo = textField("Grupo") {
            binder.bind(this, Categoria::grupo.name)
            this.isAutofocus = true
            this.isAutoselect = true
            colspan = 4
            this.isReadOnly = readOnly
            this.element.setAttribute("theme", "small")
          }
          edtDepartamento = textField("Departamento") {
            binder.bind(this, Categoria::departamento.name)
            colspan = 4
            this.isReadOnly = readOnly
            this.isClearButtonVisible = true
            this.element.setAttribute("theme", "small")
          }
          edtSecao = textField("Seção") {
            binder.bind(this, Categoria::secao.name)
            colspan = 4
            this.isReadOnly = readOnly
            this.addThemeVariants(LUMO_SMALL)
          }
        }
        if(operation != READ) {
          hr()
          horizontalLayout {
            this.setWidthFull()
            this.justifyContentMode = JustifyContentMode.END
            button("Confirmar") {
              alignSelf = END
              addThemeVariants(LUMO_PRIMARY)
              addClickListener {
                binder.writeBean(domainObject)
                operationButtonClickListener?.onComponentEvent(it)
              }
            }
            button("Cancelar") {
              alignSelf = END
              addThemeVariants(LUMO_ERROR)
              addClickListener(cancelButtonClickListener)
            }
          }
        }
        binder.readBean(domainObject)
      }
    }
    
    fun createCaption(operation: CrudOperation?): String {
      return operation?.let {crudOperation ->
        when(crudOperation) {
          READ -> "Consulta"
          ADD -> "Adiciona"
          UPDATE -> "Atualiza"
          DELETE -> "Remove"
        }
      } ?: "Erro"
    }
    
    override fun buildCaption(operation: CrudOperation?, domainObject: Categoria?) = null
    
    override fun showError(operation: CrudOperation?, e: Exception?) {
      ConfirmDialog.createError()
        .withCaption("Erro do aplicativo")
        .withMessage(e?.message ?: "Erro desconhecido")
        .open()
    }
    
    override fun setNewInstanceSupplier(newInstanceSupplier: Supplier<Categoria?>) {
      this._newInstanceSupplier = newInstanceSupplier
    }
    
    override fun getNewInstanceSupplier(): Supplier<Categoria?>? {
      if(_newInstanceSupplier == null) {
        _newInstanceSupplier = Supplier {
          try {
            val categoriaSelecionada: Categoria? = crud.grid.asSingleSelect().value
            return@Supplier Categoria().apply {
              if(categoriaSelecionada != null) {
                this.grupo = categoriaSelecionada.grupo
                this.departamento = categoriaSelecionada.departamento
                this.secao = ""
              }
            }
          } catch(e: InstantiationException) {
            e.printStackTrace()
            return@Supplier null
          } catch(e: IllegalAccessException) {
            e.printStackTrace()
            return@Supplier null
          }
        }
      }
      return _newInstanceSupplier
    }
  }
  
  companion object {
    const val TITLE = "Categoria"
  }
  
  private fun filename(): String {
    val sdf = DateTimeFormatter.ofPattern("yyMMddHHmmss")
    val textTime =
      LocalDateTime.now()
        .format(sdf)
    val filename = "categoria$textTime.xlsx"
    return filename
  }
  
  private fun buttonDownloadLazy(): LazyDownloadButton {
    val button = LazyDownloadButton(FontAwesome.Solid.FILE_EXCEL.create(),
                                    {filename()},
                                    {
                                      val planilha = PlanilhaCategoria()
                                      val bytes = planilha.grava(crud.grid.listOrder())
                                      ByteArrayInputStream(bytes)
                                    }
                                   )
   // button.addThemeVariants(ButtonVariant.LUMO_SMALL)
    button.tooltip = "Salva a planilha"
    return button
  }
}

