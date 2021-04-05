package br.com.astrosoft.framework.view

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.vaadin.flow.component.grid.ColumnTextAlign.END
import com.vaadin.flow.component.grid.Grid
import com.github.mvysny.karibudsl.v10.getAll
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider
import com.vaadin.flow.data.provider.Query

inline fun <reified T: Any> (@VaadinDsl Grid<T>).addColumnSeq(label: String, crossinline lista:() -> List<T>) {
  addColumn {
    lista().indexOf(it) + 1
  }.apply {
    this.textAlign = END
    isAutoWidth = false
    setHeader(label)
    width = "90px"
  }
}