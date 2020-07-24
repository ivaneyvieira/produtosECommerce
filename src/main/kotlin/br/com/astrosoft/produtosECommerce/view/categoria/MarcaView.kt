package br.com.astrosoft.produtosECommerce.view.marca

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.produtosECommerce.model.beans.Marca
import br.com.astrosoft.produtosECommerce.view.layout.ProdutoEComerceLayout
import br.com.astrosoft.produtosECommerce.view.user.UserCrudFormFactory.Companion.TITLE
import br.com.astrosoft.produtosECommerce.viewmodel.MarcaViewModel
import br.com.astrosoft.produtosECommerce.viewmodel.IMarcaView
import com.github.mvysny.karibudsl.v10.alignSelf
import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.formLayout
import com.github.mvysny.karibudsl.v10.getColumnBy
import com.github.mvysny.karibudsl.v10.horizontalLayout
import com.github.mvysny.karibudsl.v10.hr
import com.github.mvysny.karibudsl.v10.integerField
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant.LUMO_ERROR
import com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY
import com.vaadin.flow.component.grid.GridVariant.LUMO_COLUMN_BORDERS
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.grid.GridVariant.LUMO_ROW_STRIPES
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.END
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextFieldVariant.LUMO_ALIGN_RIGHT
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
import org.vaadin.crudui.layout.impl.WindowBasedCrudLayout
import java.util.function.*

@Route(layout = ProdutoEComerceLayout::class)
@PageTitle(TITLE)
class MarcaView: ViewLayout<MarcaViewModel>(), IMarcaView {
  override val viewModel = MarcaViewModel(this)
  
  override fun isAccept() = AppConfig.userSaci?.roles()
    ?.contains("ADMIN") == true
  
  init {
    form("Editor de marcas")
    setSizeFull()
    val crud: GridCrud<Marca> = gridCrud()
    // layout configuration
    setSizeFull()
    this.add(crud)
    // logic configuration
    setOperation(crud)
  }
  
  private fun gridCrud(): GridCrud<Marca> {
    val crud: GridCrud<Marca> = GridCrud(Marca::class.java)
    crud.grid
      .setColumns(Marca::marcaNo.name,
                  Marca::name.name)
    crud.grid.getColumnBy(Marca::marcaNo)
      .setHeader("Número")
    crud.grid.getColumnBy(Marca::name)
      .setHeader("Grupo")
    
    crud.grid.addThemeVariants(LUMO_COMPACT, LUMO_ROW_STRIPES, LUMO_COLUMN_BORDERS)
    
    crud.crudFormFactory = MarcaCrudFormFactory()
    crud.setSizeFull()
    (crud.crudLayout  as? WindowBasedCrudLayout)?.setFormWindowWidth("30em")
    return crud
  }
  
  private fun setOperation(crud: GridCrud<Marca>) {
    crud.setOperations(
      {viewModel.findAll()},
      {user: Marca -> viewModel.add(user)},
      {user: Marca? -> viewModel.update(user)},
      {user: Marca? -> viewModel.delete(user)})
  }
}

class MarcaCrudFormFactory: AbstractCrudFormFactory<Marca>() {
  private var _newInstanceSupplier: Supplier<Marca?>? = null
  
  override fun buildNewForm(operation: CrudOperation?,
                            domainObject: Marca?,
                            readOnly: Boolean,
                            cancelButtonClickListener: ComponentEventListener<ClickEvent<Button>>?,
                            operationButtonClickListener: ComponentEventListener<ClickEvent<Button>>?): Component {
    val binder = Binder<Marca>(Marca::class.java)
    return VerticalLayout().apply {
      isSpacing = false
      isMargin = false
      formLayout {
        this.responsiveSteps {
          "0px"(1, top)
          "10em"(4, aside)
        }
        integerField("Número") {
          binder.bind(this, Marca::marcaNo.name)
          addThemeVariants(LUMO_ALIGN_RIGHT)
          this.isAutofocus=true
          this.isAutoselect=true
          width = "10em"
        }
        textField("Grupo") {
          binder.bind(this, Marca::name.name)
          colspan = 4
        }
      }
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
      
      binder.readBean(domainObject)
    }
  }
  
  override fun buildCaption(operation: CrudOperation?, domainObject: Marca?): String {
    return operation?.let {crudOperation ->
      when(crudOperation) {
        READ   -> "Consulta"
        ADD    -> "Adiciona"
        UPDATE -> "Atualiza"
        DELETE -> "Remove"
      }
    } ?: "Erro"
  }
  
  override fun showError(operation: CrudOperation?, e: Exception?) {
    ConfirmDialog.createError()
      .withCaption("Erro do aplicativo")
      .withMessage(e?.message ?: "Erro desconhecido")
      .open()
  }
  
  override fun setNewInstanceSupplier(newInstanceSupplier: Supplier<Marca?>) {
    this._newInstanceSupplier = newInstanceSupplier
  }
  
  override fun getNewInstanceSupplier(): Supplier<Marca?>? {
    if(_newInstanceSupplier == null) {
      _newInstanceSupplier = Supplier {
        try {
          return@Supplier Marca()
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
  
  companion object {
    const val TITLE = "Marca"
  }
}