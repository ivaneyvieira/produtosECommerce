package br.com.astrosoft

import br.com.astrosoft.framework.spring.IUser
import br.com.astrosoft.framework.spring.SecurityUtils
import br.com.astrosoft.framework.view.ViewUtil
import br.com.astrosoft.produtosECommerce.model.saci

object AppConfig {
  val version = ViewUtil.versao
  const val commpany = "Engecopi"
  const val title = "Produtos E-Commerce"
  const val shortName = title
  const val iconPath = "icons/logo.png"
  val test: Boolean?
    get() = false
  val userDetails
    get() = SecurityUtils.userDetails
  val userSaci
    get() = userDetails?.user

  fun findUser(username: String?): IUser? {
    val list = saci.findUser(username)
    return list.firstOrNull { user ->
      user.ativo
    }
  }
}
