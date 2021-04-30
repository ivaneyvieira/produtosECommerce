package br.com.astrosoft.produtosECommerce.view.main

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.PainelGrid
import br.com.astrosoft.produtosECommerce.model.beans.*
import br.com.astrosoft.produtosECommerce.model.beans.EEditor.*
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryProduto
import br.com.astrosoft.produtosECommerce.viewmodel.IProdutosEComerceView
import com.github.mvysny.karibudsl.v10.getColumnBy
import com.vaadin.flow.component.Focusable
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode.MULTI
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider
import com.vaadin.flow.data.provider.SortDirection

abstract class PainelGridProdutoAbstract(
  val view: IProdutosEComerceView, serviceQuery: ServiceQueryProduto,
                                        ) : PainelGrid<Produto, FiltroProduto>(serviceQuery) {
  override fun Grid<Produto>.gridConfig() {
    setSelectionMode(MULTI)
    val userSaci = AppConfig.userSaci as? UserSaci

    val ativaEditor = statusDefault().canEdit || (userSaci?.admin == true)

    if (ativaEditor) {
      withEditor(Produto::class, openEditor = { binder ->
        binder.bean.editado = statusDefault().value
        (getColumnBy(Produto::descricaoCompleta).editorComponent as? Focusable<*>)?.focus()
      }, closeEditor = { binder ->
        view.salvaProduto(binder.bean)
        grid.dataProvider.refreshItem(binder.bean)
      })
      editor.binder.addValueChangeListener {
        (it.value as? Produto)?.modificado = "S"
      }
    }
    colSequencial()
    if(statusDefault() == ENVIADO) colModificado()
    if (statusDefault() == CORRECAO) colUsuario()
    colDataHoraMudanca()
    colCodigo()
    if (statusDefault() == EDITADO) colFornecedor()
    colBarcode()
    colDescricao()
    colDescricaoCompleta().apply {
      if (ativaEditor) textAreaEditor {
        this.addValueChangeListener { event ->
          val string = event.value ?: ""
          val maxLength = 80
          if (string.length > maxLength && event.isFromClient) {
            Notification.show("Este campo só aceita no máximo $maxLength cartactere")
            event.source.value = string.substring(0, maxLength)
          }
        }
      }
    }
    colBitola().apply {
      if (ativaEditor) comboFieldEditor {
        Bitola.findAll().sortedBy { it.lookupValue }
      }
    }
    colGrade() //colCor()
    colGradeCompleta().apply {
      if (ativaEditor) colorPainelEditor()
    }
    colCorPainel()
    colMarca().apply {
      if (ativaEditor) comboFieldEditor {
        Marca.findAll().sortedBy { it.lookupValue }
      }
    }
    colCategoria().apply {
      if (ativaEditor) comboFieldEditor {
        Categoria.findAll().sortedBy { it.lookupValue }
      }
    }
    colImagem().apply {
      if (ativaEditor) textAreaEditor()
    }
    colTexLink().apply {
      if (ativaEditor) textAreaEditor().apply {
        (this.editorComponent as? TextArea)?.isReadOnly = true
      }
    }
    colEspecificacoes().apply {
      if (ativaEditor) textAreaEditor()
    }
    colPeso().apply {
      if (ativaEditor) decimalFieldEditor()
    }
    colAltura().apply {
      if (ativaEditor) decimalFieldEditor()
    }
    colLargura().apply {
      if (ativaEditor) decimalFieldEditor()
    }
    colComprimento().apply {
      if (ativaEditor) decimalFieldEditor()
    }
    if (statusDefault() == EDITADO) colUsuario()

    this.sort(listOf(GridSortOrder(getColumnBy(Produto::descricao), SortDirection.ASCENDING)))
  }


  override fun gridPanel(
    dataProvider: ConfigurableFilterDataProvider<Produto, Void, FiltroProduto>,
                        ): Grid<Produto> {
    val grid = Grid(Produto::class.java, false)
    grid.dataProvider = dataProvider
    return grid
  }

  abstract fun statusDefault(): EEditor
}


