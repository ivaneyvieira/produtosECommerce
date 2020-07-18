package br.com.astrosoft.produtosEComerce.view.layout

import br.com.astrosoft.produtosEComerce.view.main.ProdutosEComerceView
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.Route

@Route("")
class ViewEmpty: VerticalLayout(), BeforeEnterObserver {
  override fun beforeEnter(event: BeforeEnterEvent?) {
    if(event?.navigationTarget == ViewEmpty::class.java)
      event.forwardTo(ProdutosEComerceView::class.java)
  }
}