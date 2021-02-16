package br.com.astrosoft.produtosECommerce.view.main

import br.com.astrosoft.framework.view.PainelGrid
import br.com.astrosoft.produtosECommerce.model.beans.Bitola
import br.com.astrosoft.produtosECommerce.model.beans.Categoria
import br.com.astrosoft.produtosECommerce.model.beans.EEditor
import br.com.astrosoft.produtosECommerce.model.beans.Marca
import br.com.astrosoft.produtosECommerce.model.beans.Produto
import br.com.astrosoft.produtosECommerce.viewmodel.IProdutosEComerceView
import com.github.mvysny.karibudsl.v10.getColumnBy
import com.vaadin.flow.component.Focusable
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode.MULTI
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.provider.SortDirection

abstract class PainelGridProdutoAbstract(val view: IProdutosEComerceView, blockUpdate: () -> Unit):
  PainelGrid<Produto>(blockUpdate) {
  override fun Grid<Produto>.gridConfig() {
    setSelectionMode(MULTI)
    //
    withEditor(Produto::class, openEditor = {produto ->
      produto.editado = statusDefault().value
      (getColumnBy(Produto::descricaoCompleta).editorComponent as? Focusable<*>)?.focus()
    }, closeEditor = {produto ->
      view.salvaProduto(produto)
    })
    //
    colDataHoraMudanca()
    colCodigo()
    colBarcode()
    colDescricao()
    colDescricaoCompleta().textAreaEditor()
    colBitola().comboFieldEditor {
      Bitola.findAll()
        .sortedBy {it.lookupValue}
    }
    colGrade()
    colCor()
    colCorPainel()
    colGradeCompleta().textAreaEditor()
    colMarca().comboFieldEditor {
      Marca.findAll()
        .sortedBy {it.lookupValue}
    }
    //
    colCategoria().comboFieldEditor {
      Categoria.findAll()
        .sortedBy {it.lookupValue}
    }
    //
    colImagem().textAreaEditor()
    colTexLink().textAreaEditor()
      .apply {
        (this.editorComponent as? TextArea)?.isReadOnly = true
      }
    colEspecificacoes().textAreaEditor()
    //
    colPeso().decimalFieldEditor()
    colAltura().decimalFieldEditor()
    colLargura().decimalFieldEditor()
    colComprimento().decimalFieldEditor()
    
    
    
    this.sort(listOf(GridSortOrder(getColumnBy(Produto::descricao), SortDirection.ASCENDING)))
  }
  
  override fun gridPanel(dataProvider : ListDataProvider<Produto>): Grid<Produto>{
    val grid = Grid<Produto>(Produto::class.java, false)
    grid.dataProvider = dataProvider
    return grid
  }
  
  abstract fun statusDefault(): EEditor
}


