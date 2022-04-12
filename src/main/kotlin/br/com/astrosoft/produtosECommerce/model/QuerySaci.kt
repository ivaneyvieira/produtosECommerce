package br.com.astrosoft.produtosECommerce.model

import br.com.astrosoft.framework.model.QueryDB
import br.com.astrosoft.framework.model.SortOrder
import br.com.astrosoft.framework.util.DB
import br.com.astrosoft.framework.util.SystemUtils
import br.com.astrosoft.framework.util.lpad
import br.com.astrosoft.produtosECommerce.model.beans.*
import org.sql2o.Query
import kotlin.math.roundToInt

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

  fun precoSaci(): List<PrecoSaci> {
    val sql = "/sqlSaci/precoSaci.sql"
    return query(sql, PrecoSaci::class)
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
      "select CAST(LPAD(no, 6, '0') AS CHAR) as clno, name as clname from sqldados.cl group by no", Cl::class
    )
  }

  fun listaFornecedores(): List<Fornecedor> {
    return query(
      "select no as vendno, name as fornecedor from sqldados.vend group by no", Fornecedor::class
    )
  }

  fun savePromocao(promocao: Promocao, prdno: String, grade: String, price: Double) {
    val sql = "/sqlSaci/savePromoPrd.sql"
    val promono = promocao.promoNo
    script(sql) {
      addOptionalParameter("promo", promono)
      addOptionalParameter("prdno", prdno.toIntOrNull() ?: 0)
      addOptionalParameter("grade", grade)
      addOptionalParameter("price", (price * 100).roundToInt())
    }
  }

  fun removePromocao(prdno: String, grade: String) {
    val sql = "/sqlSaci/removePromoPrd.sql"
    script(sql) {
      addOptionalParameter("prdno", prdno.toIntOrNull() ?: 0)
      addOptionalParameter("grade", grade)
    }
  }

  fun <R : Any> filtroProdutosPromocional(
    filtro: FiltroProdutosPromocional,
    complemento: String,
    result: (Query) -> R
  ): R {
    val sql = "/sqlSaci/produtosPromocional.sql"
    val codigos = local.fetchProduto(
      FiltroProduto(editado = EEditor.ENVIADO), 0, Int.MAX_VALUE, emptyList()
    ).map { it.codigo }.distinct().map { it.toIntOrNull().toString().lpad(16, " ") }
    return querySerivce(sql, complemento, lambda = {
      addOptionalParameter("centroLucro", filtro.centroLucro)
      addOptionalParameter("fornecedor", filtro.fornecedor)
      addOptionalParameter("tipo", filtro.tipo)
      addOptionalParameter("codigo", filtro.codigo)
      addOptionalParameter("codigos", codigos)
      addOptionalParameter("tipoPainel", filtro.tipoPainel.toString())
    }, result = result)
  }


  fun countProduto(filter: FiltroProdutosPromocional): Int {
    val complemento = "SELECT COUNT(*) FROM T_RESULT"
    return filtroProdutosPromocional(filter, complemento) {
      it.executeScalar(Int::class.java)
    }
  }

  fun fetchProduto(
    filter: FiltroProdutosPromocional, offset: Int, limit: Int, sortOrders: List<SortOrder>
  ): List<ProdutoPromocao> {
    val orderBy = if (sortOrders.isEmpty()) "" else "ORDER BY " + sortOrders.joinToString(
      separator = ", "
    ) { it.sql() }
    val complemento = "SELECT * FROM T_RESULT $orderBy LIMIT $limit OFFSET $offset"
    return filtroProdutosPromocional(filter, complemento) {
      it.executeAndFetch(ProdutoPromocao::class.java)
    }
  }

  fun findPromocoesViergentes(): List<Promocao> {
    val sql = "/sqlSaci/promocoesVirgentes.sql"
    return query(sql, Promocao::class)
  }

  private fun findProdutosSaci(): List<ProdutoSaci> {
    val sql = "/sqlSaci/produtoSaci.sql"
    return query(sql, ProdutoSaci::class)
  }

  fun updateProdutos() {
    val produtoSaci = findProdutosSaci()
    val sql = "/sqlSaci/updateProduto.sql"

    SystemUtils.readFile(sql)?.let { sqlUpdate ->
      sql2o.beginTransaction().use { con ->
        val query = con.createQuery(sqlUpdate)
        produtoSaci.forEach { produtos ->
          query.addOptionalParameter("codigo", produtos.codigo)
          query.addOptionalParameter("grade", produtos.grade)
          query.addOptionalParameter("barcode", produtos.barcode)
          query.addOptionalParameter("descricao", produtos.descricao)
          query.addOptionalParameter("vendno", produtos.vendno)
          query.addOptionalParameter("fornecedor", produtos.fornecedor)
          query.addOptionalParameter("typeno", produtos.typeno)
          query.addOptionalParameter("typeName", produtos.typeName)
          query.addOptionalParameter("clno", produtos.clno)
          query.addOptionalParameter("clname", produtos.clname)
          query.addOptionalParameter("precoCheio", produtos.precoCheio)
          query.addOptionalParameter("ncm", produtos.ncm)
        }
        query.executeBatch()
        print(".")
        con.commit()
      }
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