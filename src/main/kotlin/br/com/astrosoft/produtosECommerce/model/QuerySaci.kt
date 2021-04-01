package br.com.astrosoft.produtosECommerce.model

import br.com.astrosoft.framework.model.QueryDB
import br.com.astrosoft.framework.util.DB
import br.com.astrosoft.produtosECommerce.model.beans.*

class QuerySaci : QueryDB("saci", driver, url, username, password) {
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

  fun saldoLoja4(): List<SaldoLoja4> {
    val sql = "/sqlSaci/saldoLoja4.sql"
    return query(sql, SaldoLoja4::class)
  }

  fun price(): List<Price> {
    val sql = "/sqlSaci/price.sql"
    return query(sql, Price::class)
  }

  fun listaType(): List<TypePrd> {
    return query(
      "select no as typeno, name as typeName from sqldados.type group by no;", TypePrd::class
    )
  }

  fun listaCl(): List<Cl> {
    return query(
      "select CAST(LPAD(no, 6, '0') AS CHAR) as clno, name as clname from sqldados.cl group by no",
      Cl::class
    )
  }

  fun listaFornecedores(): List<Fornecedor> {
    return query(
      "select no as vendno, name as fornecedor from sqldados.vend group by no",
      Fornecedor::class
    )
  }

  fun updatePromo(prdno: String) {
    return
    val sql = "/sqlSaci/insertPromoPrd.sql"
    val promono = 21
    script(sql) {
      addOptionalParameter("promo", promono)
      addOptionalParameter("prdno", prdno.toIntOrNull() ?: 0)
    }
  }

  fun findProdutosPromocional(filtro: FiltroProdutosPromocional): List<ProdutoPromocao> {
    val sql = "/sqlSaci/produtosPromocional.sql"
    return query(sql, ProdutoPromocao::class) {
      addOptionalParameter("centroLucro", filtro.centroLucro)
      addOptionalParameter("fornecedor", filtro.fornecedor)
      addOptionalParameter("tipo", filtro.tipo)
      addOptionalParameter("codigo", filtro.codigo)
    }
  }

  companion object {
    private val db = DB("saci")
    internal val driver = db.driver
    internal val url = db.url
    internal val username = db.username
    internal val password = db.password
    internal val test = db.test
    val ipServer = url.split("/").getOrNull(2)
  }
}

val saci = QuerySaci()