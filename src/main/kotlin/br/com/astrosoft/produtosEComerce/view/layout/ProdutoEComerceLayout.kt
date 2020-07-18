package br.com.astrosoft.produtosEComerce.view.layout

import br.com.astrosoft.AppConfig
import br.com.astrosoft.produtosEComerce.view.main.ProdutosEComerceView
import br.com.astrosoft.produtosEComerce.view.user.UsuarioView
import com.github.mvysny.karibudsl.v10.anchor
import com.github.mvysny.karibudsl.v10.drawer
import com.github.mvysny.karibudsl.v10.drawerToggle
import com.github.mvysny.karibudsl.v10.h3
import com.github.mvysny.karibudsl.v10.horizontalLayout
import com.github.mvysny.karibudsl.v10.hr
import com.github.mvysny.karibudsl.v10.icon
import com.github.mvysny.karibudsl.v10.isExpand
import com.github.mvysny.karibudsl.v10.label
import com.github.mvysny.karibudsl.v10.navbar
import com.github.mvysny.karibudsl.v10.routerLink
import com.github.mvysny.karibudsl.v10.tab
import com.github.mvysny.karibudsl.v10.tabs
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo

@Theme(value = Lumo::class, variant=Lumo.DARK)
@Push
@PWA(name = AppConfig.title,
     shortName = AppConfig.shortName,
     iconPath = AppConfig.iconPath,
     enableInstallPrompt = false)
class ProdutoEComerceLayout: AppLayout() {
  init {
    isDrawerOpened = false
    navbar {
      drawerToggle()
      h3(AppConfig.title)
      horizontalLayout {
        isExpand = true
      }
      anchor("logout", "Sair")
    }
    drawer {
      verticalLayout {
        label("Versão ${AppConfig.version}")
        label(AppConfig.userSaci?.login)
      }
      hr()
      tabs {
        orientation = Tabs.Orientation.VERTICAL
        tab {
          this.icon(VaadinIcon.FORM)
          routerLink(text = "Pedidos", viewType = ProdutosEComerceView::class)
        }
        tab {
          this.isEnabled = AppConfig.userSaci?.roles()?.contains("ADMIN") ?: false
          this.icon(VaadinIcon.USER)
          routerLink(text = "Usuário", viewType = UsuarioView::class)
        }
      }
    }
  }
}