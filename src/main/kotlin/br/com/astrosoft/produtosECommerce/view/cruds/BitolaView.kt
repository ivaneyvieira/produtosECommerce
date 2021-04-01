package br.com.astrosoft.produtosECommerce.view.cruds

import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.produtosECommerce.model.beans.Bitola
import br.com.astrosoft.produtosECommerce.view.layout.ProdutoECommerceLayout
import br.com.astrosoft.produtosECommerce.view.user.UserCrudFormFactory.Companion.TITLE
import br.com.astrosoft.produtosECommerce.viewmodel.BitolaViewModel
import br.com.astrosoft.produtosECommerce.viewmodel.IBitolaView
import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant.LUMO_ERROR
import com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY
import com.vaadin.flow.component.grid.GridVariant.*
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.END
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextFieldVariant.LUMO_ALIGN_RIGHT
import com.vaadin.flow.component.textfield.TextFieldVariant.LUMO_SMALL
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import org.claspina.confirmdialog.ConfirmDialog
import org.vaadin.crudui.crud.CrudOperation
import org.vaadin.crudui.crud.CrudOperation.*
import org.vaadin.crudui.crud.impl.GridCrud
import org.vaadin.crudui.form.AbstractCrudFormFactory
import org.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout
import org.vaadin.crudui.layout.impl.WindowBasedCrudLayout
import java.util.function.*

@Route(layout = ProdutoECommerceLayout::class, value = "bitola")
@PageTitle(TITLE)
class BitolaView : ViewLayout<BitolaViewModel>(), IBitolaView {
  override val viewModel = BitolaViewModel(this)

  override fun isAccept() = true

  init {
    form("Editor de bitolas")
    setSizeFull()
    val crud: GridCrud<Bitola> = gridCrud() // layout configuration
    setSizeFull()
    this.add(crud) // logic configuration
    setOperation(crud)
  }

  private fun gridCrud(): GridCrud<Bitola> {
    val crud: GridCrud<Bitola> = GridCrud(Bitola::class.java, HorizontalSplitCrudLayout())
    crud.grid.setColumns(
      Bitola::bitolaNo.name, Bitola::name.name
    )
    crud.grid.getColumnBy(Bitola::bitolaNo).setHeader("Número")
    crud.grid.getColumnBy(Bitola::name).setHeader("Bitola")

    crud.grid.addThemeVariants(LUMO_COMPACT, LUMO_ROW_STRIPES, LUMO_COLUMN_BORDERS)

    crud.crudFormFactory = BitolaCrudFormFactory()
    crud.setSizeFull()
    (crud.crudLayout as? WindowBasedCrudLayout)?.setFormWindowWidth("30em")
    return crud
  }

  private fun setOperation(crud: GridCrud<Bitola>) {
    crud.setOperations({ viewModel.findAll() },
      { user: Bitola -> viewModel.add(user) },
      { user: Bitola? -> viewModel.update(user) },
      { user: Bitola? -> viewModel.delete(user) })
  }
}

class BitolaCrudFormFactory : AbstractCrudFormFactory<Bitola>() {
  private var _newInstanceSupplier: Supplier<Bitola?>? = null

  override fun buildNewForm(
    operation: CrudOperation?,
    domainObject: Bitola?,
    readOnly: Boolean,
    cancelButtonClickListener: ComponentEventListener<ClickEvent<Button>>?,
    operationButtonClickListener: ComponentEventListener<ClickEvent<Button>>?
  ): Component {
    val binder = Binder<Bitola>(Bitola::class.java)
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
        integerField("Número") {
          binder.bind(this, Bitola::bitolaNo.name)
          addThemeVariants(LUMO_ALIGN_RIGHT)
          this.isAutofocus = true
          this.isAutoselect = true
          this.addThemeVariants(LUMO_SMALL)
          width = "10em"
          this.isReadOnly = readOnly
        }
        textField("Bitola") {
          binder.bind(this, Bitola::name.name)
          colspan = 4
          this.isReadOnly = readOnly
          this.addThemeVariants(LUMO_SMALL)
          this.isAutofocus = true
        }
      }
      if (operation != READ) {
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
    return operation?.let { crudOperation ->
      when (crudOperation) {
        READ -> "Consulta"
        ADD -> "Adiciona"
        UPDATE -> "Atualiza"
        DELETE -> "Remove"
      }
    } ?: "Erro"
  }

  override fun buildCaption(operation: CrudOperation?, domainObject: Bitola?) = null

  override fun showError(operation: CrudOperation?, e: Exception?) {
    ConfirmDialog.createError()
      .withCaption("Erro do aplicativo")
      .withMessage(e?.message ?: "Erro desconhecido")
      .open()
  }

  override fun setNewInstanceSupplier(newInstanceSupplier: Supplier<Bitola?>) {
    this._newInstanceSupplier = newInstanceSupplier
  }

  override fun getNewInstanceSupplier(): Supplier<Bitola?>? {
    if (_newInstanceSupplier == null) {
      _newInstanceSupplier = Supplier {
        try {
          return@Supplier Bitola().apply {
            this.bitolaNo = Bitola.nextNo()
          }
        } catch (e: InstantiationException) {
          e.printStackTrace()
          return@Supplier null
        } catch (e: IllegalAccessException) {
          e.printStackTrace()
          return@Supplier null
        }
      }
    }
    return _newInstanceSupplier
  }

  companion object {
    const val TITLE = "Bitola"
  }
}