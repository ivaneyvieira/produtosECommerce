package br.com.astrosoft.produtosEComerce.model

import br.com.astrosoft.framework.model.QueryDB
import br.com.astrosoft.framework.util.DB
import br.com.astrosoft.framework.util.lpad
import br.com.astrosoft.produtosEComerce.model.beans.Categoria
import br.com.astrosoft.produtosEComerce.model.beans.Cl
import br.com.astrosoft.produtosEComerce.model.beans.Fornecedor
import br.com.astrosoft.produtosEComerce.model.beans.Produto
import br.com.astrosoft.produtosEComerce.model.beans.TypePrd
import br.com.astrosoft.produtosEComerce.model.beans.UserSaci

class QuerySaci: QueryDB("saci", driver, url, username, password) {
  fun findUser(login: String?): List<UserSaci> {
    login ?: return emptyList()
    val sql = "/sqlSaci/userSenha.sql"
    return query(sql, UserSaci::class) {
      addParameter("login", login)
    }
  }
  
  fun findAllUser(): List<UserSaci> {
    val sql = "/sqlSaci/userSenha.sql"
    return query(sql, UserSaci::class) {
      addParameter("login", "TODOS")
    }
  }
  
  fun updateUser(user: UserSaci) {
    val sql = "/sqlSaci/updateUser.sql"
    script(sql) {
      addOptionalParameter("no", user.no)
      addOptionalParameter("bitAcesso", user.bitAcesso)
      addOptionalParameter("storeno", user.storeno)
    }
  }
  
  companion object {
    private val db = DB("saci")
    internal val driver = db.driver
    internal val url = db.url
    internal val username = db.username
    internal val password = db.password
    internal val test = db.test
    val ipServer =
      url.split("/")
        .getOrNull(2)
  }
}

val saci = QuerySaci()