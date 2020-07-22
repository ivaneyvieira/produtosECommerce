package br.com.astrosoft.produtosEComerce.model

import br.com.astrosoft.framework.model.QueryDB
import br.com.astrosoft.framework.util.DB
import br.com.astrosoft.framework.util.lpad
import br.com.astrosoft.produtosEComerce.model.beans.Categoria
import br.com.astrosoft.produtosEComerce.model.beans.Cl
import br.com.astrosoft.produtosEComerce.model.beans.Fornecedor
import br.com.astrosoft.produtosEComerce.model.beans.Marca
import br.com.astrosoft.produtosEComerce.model.beans.Produto
import br.com.astrosoft.produtosEComerce.model.beans.TypePrd

class QueryLocal: QueryDB("local", driver, url, username, password) {
  
  fun listaCategoria(): List<Categoria> {
    return query("""select categoriaNo, grupo, departamento, secao
      |             from produtoEcomerce.categoria""".trimMargin(),
                 Categoria::class)
  }
  
  fun listaMarca(): List<Marca> {
    return query("""select marcaNo, name
      |             from produtoEcomerce.marca""".trimMargin(),
                 Marca::class)
  }
  
  fun listaProdutos(codigo: Int, descricaoI: String, descricaoF: String, vendno: Int, typeno: Int,
                    clno: String, editado: Int, categoria: Int):
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
  
  fun salvaProduto(bean: Produto) {
    val sql = "/sqlSaci/salvaProduto.sql"
    script(sql) {
      addOptionalParameter("marca", bean.marca)
      addOptionalParameter("categoria", bean.categoria)
      addOptionalParameter("descricaoCompleta", bean.descricaoCompleta)
      addOptionalParameter("bitola", bean.bitola)
      addOptionalParameter("imagem", bean.imagem)
      addOptionalParameter("codigo", bean.codigo)
      addOptionalParameter("grade", bean.grade)
      addOptionalParameter("editado", bean.editado)
    }
  }
  
  fun findAllCategoria(): List<Categoria> {
    return listaCategoria()
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
    return listaMarca()
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
  
  companion object {
    private val db = DB("local")
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

val local = QueryLocal()