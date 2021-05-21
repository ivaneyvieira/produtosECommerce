package br.com.astrosoft.framework.view

import com.github.mvysny.karibudsl.v10.content
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

abstract class FilterBar<F : Any> : HorizontalLayout() {
  init {
    isMargin = false
    isPadding = false
    isSpacing = true
    content { align(left, baseline) }
    width = "100%"
    addComponents()
  }

  private fun addComponents() {
    contentBlock()
  }

  protected abstract fun FilterBar<F>.contentBlock()

  abstract fun filtro(): F
}


