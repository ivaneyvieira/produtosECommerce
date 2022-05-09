package br.com.astrosoft.produtosECommerce.model

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.model.QueryDB
import br.com.astrosoft.framework.model.SortOrder
import br.com.astrosoft.framework.util.DB
import br.com.astrosoft.framework.util.SystemUtils
import br.com.astrosoft.framework.util.lpad
import br.com.astrosoft.produtosECommerce.model.beans.*
import org.sql2o.Query
import java.time.LocalDateTime

class QueryLocal : QueryDB("local", driver, url, username, password) {
  fun String.max(): String {
    val str = this.toString().lpad(6, "0")
    var gru = str.substring(0, 2)
    var dep = str.substring(2, 4)
    var sec = str.substring(4, 6)
    if (sec == "00") {
      sec = "99"
      if (dep == "00") dep = "99"
    }
    return "$gru$dep$sec"
  }

  fun Int.max(): Int {
    return this.toString().max().toIntOrNull() ?: 0
  }

  fun salvaProduto(bean: Produto) {
    val sql = "/sqlSaci/salvaProduto.sql"
    script(sql) {
      addOptionalParameter("marca", bean.marca ?: 0)
      addOptionalParameter("categoria", bean.categoria ?: 0)
      addOptionalParameter("descricaoCompleta", bean.descricaoCompleta)
      addOptionalParameter("bitola", bean.bitola ?: 0)
      addOptionalParameter("imagem", bean.imagem)
      addOptionalParameter("codigo", bean.codigo)
      addOptionalParameter("grade", bean.grade)
      addOptionalParameter("peso", bean.peso ?: 0.00)
      addOptionalParameter("comprimento", bean.comprimento ?: 0.00)
      addOptionalParameter("largura", bean.largura ?: 0.00)
      addOptionalParameter("altura", bean.altura ?: 0.00)
      addOptionalParameter("textLink", bean.textLink)
      addOptionalParameter("especificacoes", bean.especificacoes)
      addOptionalParameter("modificado", bean.modificado)
      addOptionalParameter("gradeAlternativa", bean.gradeAlternativa)

      bean.corStr = findCores(bean.gradeCompleta).firstOrNull()?.codigoCor ?: ""

      addOptionalParameter("gradeCompleta", bean.gradeCompleta)
      addOptionalParameter("editado", bean.editado ?: 0)
      addOptionalParameter("corStr", bean.corStr)
      bean.dataHoraMudanca = LocalDateTime.now()
      addOptionalParameter("dataHoraMudanca", bean.dataHoraMudanca)
      val userSaci = AppConfig.userSaci as? UserSaci
      if (bean.editado in EEditor.values().filter { it.canEdit }.map { it.value } || userSaci?.admin == true) {
        bean.userno = userSaci?.no
      }
      addOptionalParameter("userno", bean.userno ?: 0)
    }
  }

  fun findAllCategoria(): List<Categoria> {
    return query("""select categoriaNo, grupo, departamento, secao
      |             from produtoEcomerce.categoria
      |             order by categoriaNo
      |             """.trimMargin(), Categoria::class)
  }

  fun addCategoria(categoria: Categoria) {
    val sql = """INSERT INTO produtoEcomerce.categoria(categoriaNo, grupo, departamento, secao)
           |  VALUES(:categoriaNo, :grupo, :departamento, :secao)""".trimMargin()
    script(sql) {
      addOptionalParameter("categoriaNo", categoria.categoriaNo)
      addOptionalParameter("grupo", categoria.grupo)
      addOptionalParameter("secao", categoria.secao)
      addOptionalParameter("departamento", categoria.departamento)
    }
  }

  fun updateCategoria(categoria: Categoria) {
    val sql = """UPDATE produtoEcomerce.categoria
                        |  SET grupo       = :grupo,
                        |  departamento    = :departamento,
                        |  secao           = :secao,
                        |  categoriaNo     = :categoriaNo
                        |WHERE categoriaNo = :categoriaNo""".trimMargin()
    script(sql) {
      addOptionalParameter("categoriaNo", categoria.categoriaNo)
      addOptionalParameter("grupo", categoria.grupo)
      addOptionalParameter("secao", categoria.secao)
      addOptionalParameter("departamento", categoria.departamento)
    }
  }

  fun deleteCategoria(categoria: Categoria) {
    val sql = "DELETE FROM produtoEcomerce.categoria WHERE categoriaNo = :categoriaNo"
    script(sql) {
      addOptionalParameter("categoriaNo", categoria.categoriaNo)
    }
  }

  fun findAllMarca(): List<Marca> {
    return query("""select marcaNo, name
      |             from produtoEcomerce.marca""".trimMargin(), Marca::class)
  }

  fun addMarca(marca: Marca) {
    val sql = """INSERT INTO produtoEcomerce.marca(marcaNo, name)
           |  VALUES(:marcaNo, :name)""".trimMargin()
    script(sql) {
      addOptionalParameter("marcaNo", marca.marcaNo)
      addOptionalParameter("name", marca.name)
    }
  }

  fun updateMarca(marca: Marca) {
    val sql = """UPDATE produtoEcomerce.marca
                        |  SET name       = :name
                        |WHERE marcaNo = :marcaNo""".trimMargin()
    script(sql) {
      addOptionalParameter("marcaNo", marca.marcaNo)
      addOptionalParameter("name", marca.name)
    }
  }

  fun deleteMarca(marca: Marca) {
    val sql = "DELETE FROM  produtoEcomerce.marca WHERE marcaNo = :marcaNo"
    script(sql) {
      addOptionalParameter("marcaNo", marca.marcaNo)
    }
  }

  fun findAllBitola(): List<Bitola> {
    return query("""select bitolaNo, name
      |             from produtoEcomerce.bitola""".trimMargin(), Bitola::class)
  }

  fun addBitola(bitola: Bitola) {
    val sql = """INSERT INTO produtoEcomerce.bitola(bitolaNo, name)
           |  VALUES(:bitolaNo, :name)""".trimMargin()
    script(sql) {
      addOptionalParameter("bitolaNo", bitola.bitolaNo)
      addOptionalParameter("name", bitola.name)
    }
  }

  fun updateBitola(bitola: Bitola) {
    val sql = """UPDATE produtoEcomerce.bitola
                        |  SET name       = :name
                        |WHERE bitolaNo   = :bitolaNo""".trimMargin()
    script(sql) {
      addOptionalParameter("bitolaNo", bitola.bitolaNo)
      addOptionalParameter("name", bitola.name)
    }
  }

  fun deleteBitola(bitola: Bitola) {
    val sql = "DELETE FROM  produtoEcomerce.bitola WHERE bitolaNo = :bitolaNo"
    script(sql) {
      addOptionalParameter("bitolaNo", bitola.bitolaNo)
    }
  }

  /*Cores*/
  fun findAllCor(): List<GradeCor> {
    return query("""select descricao, codigoCor, userno, dataHoraMudanca, enviado
      |             from produtoEcomerce.gradeCor""".trimMargin(), GradeCor::class)
  }

  fun addCor(cor: GradeCor) {
    val sql = """INSERT INTO produtoEcomerce.gradeCor(descricao, codigoCor, userno, dataHoraMudanca, enviado)
           |  VALUES(:descricao, :codigoCor, :userno, :dataHoraMudanca, :enviado)""".trimMargin()
    cor.userno = (AppConfig.userSaci as? UserSaci)?.no ?: 0
    script(sql) {
      addOptionalParameter("descricao", cor.descricao)
      addOptionalParameter("codigoCor", cor.codigoCor)
      addOptionalParameter("userno", cor.userno ?: 0)
      addOptionalParameter("dataHoraMudanca", cor.dataHoraMudanca)
      addOptionalParameter("enviado", cor.enviado ?: 0)
    }
  }

  fun updateCor(cor: GradeCor) {
    val sql = """UPDATE produtoEcomerce.gradeCor
                        |  SET codigoCor   = :codigoCor,
                        |  descricao       = :descricao,
                        |  userno          = :userno, 
                        |  dataHoraMudanca = :dataHoraMudanca, 
                        |  enviado         = :enviado
                        | WHERE descricao   = :descricaoOriginal""".trimMargin()
    cor.dataHoraMudanca = LocalDateTime.now()
    script(sql) {
      addOptionalParameter("codigoCor", cor.codigoCor)
      addOptionalParameter("descricao", cor.descricao)
      addOptionalParameter("descricaoOriginal", cor.descricaoOriginal)
      addOptionalParameter("userno", cor.userno ?: 0)
      addOptionalParameter("dataHoraMudanca", cor.dataHoraMudanca)
      addOptionalParameter("enviado", cor.enviado ?: 0)
    }
  }

  fun deleteCor(cor: GradeCor) {
    val sql = "DELETE FROM  produtoEcomerce.gradeCor WHERE descricao = :descricaoOriginal"
    script(sql) {
      addOptionalParameter("descricaoOriginal", cor.descricaoOriginal)
    }
  }

  fun findCores(descricao: String?): List<GradeCor> {
    return query("""select DISTINCT TRIM(UPPER(MID(TRIM(codigoCor), 1, 7))) AS codigoCor, TRIM(descricao) AS 
        descricao 
from produtoEcomerce.gradeCor
HAVING descricao =  '$descricao' OR '$descricao' = ''""".trimMargin(), GradeCor::class)
  }

  private fun <R : Any> filtroProduto(
    filter: FiltroProduto,
    complemento: String,
    result: (Query) -> R,
                                     ): R {
    val sql = "/sqlSaci/produtos.sql"
    return querySerivce(sql, complemento, lambda = {
      val listaProduto = filter.listaProduto.split(" +".toRegex()).filter { it.trim() != "" }.map {
        it.trim().lpad(6, "0")
      }
      addOptionalParameter("codigo", filter.codigo.toString().lpad(6, "0"))
      addOptionalParameter("listaProduto", listaProduto)
      addOptionalParameter("listaProdutoVazia", if (listaProduto.isEmpty()) "S" else "N")
      addOptionalParameter("descricaoI", filter.descricaoI)
      addOptionalParameter("descricaoF", filter.descricaoF)
      addOptionalParameter("vendno", filter.fornecedor?.vendno ?: 0)
      addOptionalParameter("typeno", filter.type?.typeno ?: 0)
      addOptionalParameter("clno1", filter.cl?.clno ?: "")
      addOptionalParameter("clno2", filter.cl?.clno?.max() ?: "")
      addOptionalParameter("editado", filter.editado.value)
      addOptionalParameter("categoria1", filter.categoria?.categoriaNo ?: 0)
      addOptionalParameter("categoria2", filter.categoria?.categoriaNo?.max() ?: 0)
    }, result = result)
  }

  fun countProduto(filter: FiltroProduto): Int {
    val complemento = "SELECT COUNT(*) FROM T_RESULT"
    return filtroProduto(filter, complemento) {
      it.executeScalar(Int::class.java)
    }
  }

  fun countProdutoConferencia(filter: FiltroProdutoConferencia): Int {
    val codigo = filter.codigo
    val sql = """SELECT COUNT(*) FROM produtoEcomerce.produtoConferencia 
      |WHERE prdno = LPAD('$codigo', 16, ' ') OR '$codigo' = '' 
      |  AND precoRef != listPrice
      |""".trimMargin()

    return querySerivce(sql) {
      it.executeScalar(Int::class.java)
    }
  }

  fun fetchProduto(
    filter: FiltroProduto,
    offset: Int,
    limit: Int,
    sortOrders: List<SortOrder>,
                  ): List<Produto> {
    val orderBy = if (sortOrders.isEmpty()) ""
    else "ORDER BY " + sortOrders.joinToString(separator = ", ") { it.sql() }
    val complemento = """DO @OFFSET := $offset;
      |SELECT @OFFSET := @OFFSET + 1 AS seq, R.* 
      |FROM T_RESULT AS R 
      |$orderBy 
      |LIMIT $limit OFFSET $offset""".trimMargin()
    return filtroProduto(filter, complemento) {
      it.executeAndFetch(Produto::class.java)
    }
  }

  fun produtosBarcode(): List<ProdutoBarcode> {
    val sql = """SELECT barcode, codigo * 1 AS codigo, grade
FROM produtoEcomerce.produto
GROUP BY barcode"""
    return query(sql, ProdutoBarcode::class)
  }

  fun fetchProdutoConferencia(
    filter: FiltroProdutoConferencia,
    offset: Int,
    limit: Int,
    sortOrders: List<SortOrder>,
                             ): List<ProdutoConferencia> {
    val codigo = filter.codigo
    val orderBy = if (sortOrders.isEmpty()) ""
    else "ORDER BY " + sortOrders.joinToString(separator = ", ") { it.sql() }
    val sql = """SELECT * FROM produtoEcomerce.produtoConferencia
      |WHERE prdno = LPAD('$codigo', 16, ' ') OR '$codigo' = '' 
      |  AND precoRef != listPrice
      |$orderBy 
      |LIMIT $limit OFFSET $offset
    """.trimMargin()
    return querySerivce(sql) {
      it.executeAndFetch(ProdutoConferencia::class.java)
    }
  }

  fun apagaPrecos() {
    val sql = "TRUNCATE TABLE produtoEcomerce.produtoConferencia"
    script(sql)
  }

  fun addPrecoConferencia(
    refid: String,
    listPrice: Double,
    prdno: String,
    grade: String,
    descricaoSite: String,
    descricaoSaci: String,
    precoPromo: Double?,
    precoRef: Double?,
                         ) {
    val sql = """INSERT IGNORE produtoEcomerce.produtoConferencia(refid, listPrice, prdno, grade, 
      |descricaoSite, descricaoSaci, precoPromo, precoRef) 
      |VALUES(:refid, :listPrice, :prdno, :grade, :descricaoSite, :descricaoSaci, 
      |:precoPromo, :precoRef)""".trimMargin()
    script(sql) {
      addOptionalParameter("refid", refid)
      addOptionalParameter("listPrice", listPrice)
      addOptionalParameter("prdno", prdno)
      addOptionalParameter("grade", grade)
      addOptionalParameter("descricaoSite", descricaoSite)
      addOptionalParameter("descricaoSaci", descricaoSaci)
      addOptionalParameter("precoPromo", precoPromo)
      addOptionalParameter("precoRef", precoRef)
    }
  }

  fun fetchVtex(filtro: FiltroVtex, offset: Int, limit: Int, sortOrders: List<SortOrder>): List<Vtex> {
    val sqlIncial = SystemUtils.readFile("/sqlSaci/fetchVtex.sql") ?: return emptyList()

    val orderBy = if (sortOrders.isEmpty()) ""
    else "ORDER BY " + sortOrders.joinToString(separator = ", ") { it.sql() }
    val sql = """$sqlIncial
      |$orderBy 
      |LIMIT $limit OFFSET $offset
    """.trimMargin()
    return query(sql, Vtex::class) {
      addOptionalParameter("produto", filtro.produto)
      addOptionalParameter("sku", filtro.sku)
      addOptionalParameter("departamento", filtro.departamento)
      addOptionalParameter("marca", filtro.marca)
      addOptionalParameter("categoria", filtro.categoria)
    }
  }

  fun countVtex(filtro: FiltroVtex): Int {
    val sql = "/sqlSaci/countVtex.sql"
    return querySerivce(sql, lambda = {
      addOptionalParameter("produto", filtro.produto)
      addOptionalParameter("sku", filtro.sku)
      addOptionalParameter("departamento", filtro.departamento)
      addOptionalParameter("marca", filtro.marca)
      addOptionalParameter("categoria", filtro.categoria)
    }, result = {
      it.executeScalar(Int::class.java)
    })
  }

  companion object {
    private val db = DB("local")
    internal val driver = db.driver
    internal val url = db.url
    internal val username = db.username
    internal val password = db.password
    internal val test = db.test
    val ipServer = url.split("/").getOrNull(2)
  }
}

val local = QueryLocal()