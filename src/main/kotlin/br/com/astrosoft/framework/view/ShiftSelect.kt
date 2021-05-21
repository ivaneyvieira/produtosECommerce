package br.com.astrosoft.framework.view

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.vaadin.flow.component.grid.ColumnTextAlign.END
import com.vaadin.flow.component.grid.Grid

inline fun <reified T : Any> (@VaadinDsl Grid<T>).addColumnSeq(label: String, crossinline lista: () -> List<T>) {
  addColumn {
    lista().indexOf(it) + 1
  }.apply {
    this.textAlign = END
    isAutoWidth = false
    setHeader(label)
    width = "90px"
  }
}