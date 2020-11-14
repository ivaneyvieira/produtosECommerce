package br.com.astrosoft.produtosECommerce.view.cruds

import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.colorPick
import br.com.astrosoft.produtosECommerce.model.beans.GradeCor
import br.com.astrosoft.produtosECommerce.model.beans.Marca
import br.com.astrosoft.produtosECommerce.view.layout.ProdutoECommerceLayout
import br.com.astrosoft.produtosECommerce.view.user.UserCrudFormFactory.Companion.TITLE
import br.com.astrosoft.produtosECommerce.viewmodel.CorViewModel
import br.com.astrosoft.produtosECommerce.viewmodel.ICorView
import br.com.astrosoft.produtosECommerce.viewmodel.IMarcaView
import br.com.astrosoft.produtosECommerce.viewmodel.MarcaViewModel
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
import com.vaadin.flow.component.textfield.TextFieldVariant.LUMO_SMALL
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.renderer.ComponentRenderer
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
import java.util.function.*

@Route(layout = ProdutoECommerceLayout::class, value = "cor")
@PageTitle(TITLE)
class CorView: ViewLayout<CorViewModel>(), ICorView {
  override val viewModel = CorViewModel(this)
  
  override fun isAccept() = true
  
  init {
    form("Editor de cores")
    setSizeFull()
    val crud: GridCrud<GradeCor> = gridCrud()
    // layout configuration
    setSizeFull()
    this.add(crud)
    // logic configuration
    setOperation(crud)
  }
  
  private fun gridCrud(): GridCrud<GradeCor> {
    val crud: GridCrud<GradeCor> = GridCrud(GradeCor::class.java, HorizontalSplitCrudLayout())
    crud.grid
      .setColumns(GradeCor::descricao.name,
                  GradeCor::codigoCor.name)
    crud.grid.getColumnBy(GradeCor::descricao)
      .setHeader("Descrição")
    crud.grid.getColumnBy(GradeCor::codigoCor)
      .setHeader("Código Cor")
    crud.grid.addColumn(ComponentRenderer{produto->
      VerticalLayout().apply {
        if(produto.codigoCor.isBlank()) {
          this.element.style.remove("backgroundColor")
        }
        else {
          this.element.style.set("backgroundColor", produto.codigoCor)
        }
      }
    }).apply {
      setHeader("Cor")
      isAutoWidth = false
      width = "3em"
    }
    
    crud.grid.addThemeVariants(LUMO_COMPACT, LUMO_ROW_STRIPES, LUMO_COLUMN_BORDERS)
    
    crud.crudFormFactory = CorCrudFormFactory()
    
    crud.setSizeFull()
    (crud.crudLayout as? WindowBasedCrudLayout)?.setFormWindowWidth("30em")
    return crud
  }
  
  private fun setOperation(crud: GridCrud<GradeCor>) {
    crud.setOperations(
      {viewModel.findAll()},
      {user: GradeCor -> viewModel.add(user)},
      {user: GradeCor? -> viewModel.update(user)},
      {user: GradeCor? -> viewModel.delete(user)})
  }
}

class CorCrudFormFactory: AbstractCrudFormFactory<GradeCor>() {
  private var _newInstanceSupplier: Supplier<GradeCor?>? = null
  
  override fun buildNewForm(operation: CrudOperation?,
                            domainObject: GradeCor?,
                            readOnly: Boolean,
                            cancelButtonClickListener: ComponentEventListener<ClickEvent<Button>>?,
                            operationButtonClickListener: ComponentEventListener<ClickEvent<Button>>?): Component {
    val binder = Binder(GradeCor::class.java)
    
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
        textField("Descrição") {
          binder.bind(this, GradeCor::descricao.name)
          this.isAutofocus = true
          this.isAutoselect = true
          this.isReadOnly = readOnly
          colspan = 4
        }
        colorPick("Código Cor") {
          binder.bind(this, GradeCor::codigoCor.name)
          this.isReadOnly = readOnly
          colspan = 4
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
  
  override fun buildCaption(operation: CrudOperation?, domainObject: GradeCor?) = null
  
  override fun showError(operation: CrudOperation?, e: Exception?) {
    ConfirmDialog.createError()
      .withCaption("Erro do aplicativo")
      .withMessage(e?.message ?: "Erro desconhecido")
      .open()
  }
  
  override fun setNewInstanceSupplier(newInstanceSupplier: Supplier<GradeCor?>) {
    this._newInstanceSupplier = newInstanceSupplier
  }
  
  override fun getNewInstanceSupplier(): Supplier<GradeCor?>? {
    if(_newInstanceSupplier == null) {
      _newInstanceSupplier = Supplier {
        try {
          return@Supplier GradeCor()
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
    const val TITLE = "Cor"
  }
}