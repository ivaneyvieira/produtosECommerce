package br.com.astrosoft.produtosECommerce.view.layout

import br.com.astrosoft.AppConfig
import br.com.astrosoft.produtosECommerce.view.cruds.BitolaView
import br.com.astrosoft.produtosECommerce.view.cruds.CategoriaView
import br.com.astrosoft.produtosECommerce.view.main.ProdutosEComerceView
import br.com.astrosoft.produtosECommerce.view.cruds.MarcaView
import br.com.astrosoft.produtosECommerce.view.user.UsuarioView
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
class ProdutoECommerceLayout: AppLayout() {
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
          routerLink(text = "Produtos", viewType = ProdutosEComerceView::class)
        }
        tab {
          this.isEnabled = AppConfig.userSaci?.roles()?.contains("ADMIN") ?: false
          this.icon(VaadinIcon.CUBES)
          routerLink(text = "Categoria", viewType = CategoriaView::class)
        }
        tab {
          this.isEnabled = AppConfig.userSaci?.roles()?.contains("ADMIN") ?: false
          this.icon(VaadinIcon.CUBE)
          routerLink(text = "Marca", viewType = MarcaView::class)
        }
        tab {
          this.isEnabled = AppConfig.userSaci?.roles()?.contains("ADMIN") ?: false
          this.icon(VaadinIcon.CIRCLE_THIN)
          routerLink(text = "Bitola", viewType = BitolaView::class)
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