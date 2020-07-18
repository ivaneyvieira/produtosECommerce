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

class QuerySaci: QueryDB(driver, url, username, password) {
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
  
  fun listaFornecedores(): List<Fornecedor> {
    return query("select vendno, fornecedor from produtoEcomerce.produto group by vendno",
                 Fornecedor::class)
  }
  
  fun listaType(): List<TypePrd> {
    return query("select typeno, typeName from produtoEcomerce.produto group by typeno",
                 TypePrd::class)
  }
  
  fun listaCl(): List<Cl> {
    return query("select clno, clname from produtoEcomerce.produto group by clno",
                 Cl::class)
  }
  
  fun listaCategoria(): List<Categoria> {
    return query("""select categoriaNo, grupo, departamento, secao
      |             from produtoEcomerce.produto group by clno""".trimMargin(),
                 Categoria::class)
  }
  
  fun listaProdutos(codigo: Int, descricaoI: String, descricaoF: String, vendno: Int, typeno: Int,
                    clno: Int, editado: Int, categoria: Int):
    List<Produto> {
    val sql = "/sqlSaci/produtos.sql"
    return query(sql, Produto::class) {
      addOptionalParameter("codigo",
                           codigo.toString()
                             .lpad(6, "0"))
      addOptionalParameter("descricaoI", descricaoI)
      addOptionalParameter("descricaoF", descricaoF)
      addOptionalParameter("vendno", vendno)
      addOptionalParameter("typeno", typeno)
      addOptionalParameter("clno", clno)
      addOptionalParameter("editado", editado)
      addOptionalParameter("categoria", categoria)
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