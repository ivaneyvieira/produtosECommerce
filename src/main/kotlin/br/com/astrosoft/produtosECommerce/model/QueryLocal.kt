package br.com.astrosoft.produtosECommerce.model

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.model.QueryDB
import br.com.astrosoft.framework.util.DB
import br.com.astrosoft.framework.util.lpad
import br.com.astrosoft.produtosECommerce.model.beans.*
import java.time.LocalDateTime

class QueryLocal : QueryDB("local", driver, url, username, password) {
  fun listaProdutos(
    codigo: Int,
    descricaoI: String,
    descricaoF: String,
    vendno: Int,
    typeno: Int,
    clno: String,
    editado: Int,
    categoria: Int
                   ): List<Produto> {
    val sql = "/sqlSaci/produtos.sql"
    return query(sql, Produto::class) {
      addOptionalParameter(
        "codigo", codigo.toString().lpad(6, "0")
                          )
      addOptionalParameter("descricaoI", descricaoI)
      addOptionalParameter("descricaoF", descricaoF)
      addOptionalParameter("vendno", vendno)
      addOptionalParameter("typeno", typeno)
      addOptionalParameter("clno1", clno)
      addOptionalParameter("clno2", clno.max())
      addOptionalParameter("editado", editado)
      addOptionalParameter("categoria1", categoria)
      addOptionalParameter("categoria2", categoria.max())
    }
  }

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
      addOptionalParameter("gradeCompleta", bean.gradeCompleta)
      addOptionalParameter("editado", bean.editado ?: 0)
      bean.dataHoraMudanca= LocalDateTime.now()
      addOptionalParameter("dataHoraMudanca", bean.dataHoraMudanca)
      val userSaci = AppConfig.userSaci as? UserSaci
      bean.userName = userSaci?.name
      bean.userno = userSaci?.no
      addOptionalParameter("userno", bean.userno ?: 0)
    }
  }

  fun findAllCategoria(): List<Categoria> {
    return query(
      """select categoriaNo, grupo, departamento, secao
      |             from produtoEcomerce.categoria
      |             order by categoriaNo
      |             """.trimMargin(), Categoria::class
                )
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
    return query(
      """select marcaNo, name
      |             from produtoEcomerce.marca""".trimMargin(), Marca::class
                )
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
    return query(
      """select bitolaNo, name
      |             from produtoEcomerce.bitola""".trimMargin(), Bitola::class
                )
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
    return query(
      """select descricao, codigoCor
      |             from produtoEcomerce.gradeCor""".trimMargin(), GradeCor::class
                )
  }

  fun addCor(cor: GradeCor) {
    val sql = """INSERT INTO produtoEcomerce.gradeCor(descricao, codigoCor)
           |  VALUES(:descricao, :codigoCor)""".trimMargin()
    script(sql) {
      addOptionalParameter("descricao", cor.descricao)
      addOptionalParameter("codigoCor", cor.codigoCor)
    }
  }

  fun updateCor(cor: GradeCor) {
    val sql = """UPDATE produtoEcomerce.gradeCor
                        |  SET codigoCor   = :codigoCor,
                        |  descricao       = :descricao
                        | WHERE descricao   = :descricaoOriginal""".trimMargin()
    script(sql) {
      addOptionalParameter("codigoCor", cor.codigoCor)
      addOptionalParameter("descricao", cor.descricao)
      addOptionalParameter("descricaoOriginal", cor.descricaoOriginal)
    }
  }

  fun deleteCor(cor: GradeCor) {
    val sql = "DELETE FROM  produtoEcomerce.gradeCor WHERE descricao = :descricaoOriginal"
    script(sql) {
      addOptionalParameter("descricaoOriginal", cor.descricaoOriginal)
    }
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