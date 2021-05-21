package br.com.astrosoft.produtosECommerce.model.beans

import br.com.astrosoft.framework.model.ILookup
import br.com.astrosoft.framework.util.lpad
import br.com.astrosoft.produtosECommerce.model.local

data class Categoria(
  var categoriaNo: Int = 0, var grupo: String = "", var departamento: String = "", var secao: String = ""
                    ) : Comparable<Categoria?>, ILookup {
  override val lookupValue: String
    get() = descricao
  val codigo
    get() = categoriaNo.toString().lpad(6, "0")
  val codigoGrupo
    get() = codigo.substring(0, 2).toIntOrNull() ?: 0
  val codigoDepartamento
    get() = codigo.substring(2, 4).toIntOrNull() ?: 0
  val codigoSecao
    get() = codigo.substring(4, 6).toIntOrNull() ?: 0
  val descricao
    get() = when {
      grupo == ""        -> ""
      departamento == "" -> grupo
      secao == ""        -> "$grupo/$departamento"
      else               -> "$grupo/$departamento/$secao"
    }

  companion object {
    fun makeCodigo(grupo: Int, departamento: Int, secao: Int) = "${
      grupo.toString().lpad(2, "0")
    }${
      departamento.toString().lpad(2, "0")
    }${
      secao.toString().lpad(2, "0")
    }"

    private val listCategoria = mutableListOf<Categoria>().apply {
      addAll(local.findAllCategoria())
    }

    fun updateList() {
      listCategoria.clear()
      listCategoria.addAll(local.findAllCategoria())
    }

    fun findAll(): List<Categoria> {
      return listCategoria.toList()
    }

    fun findById(id: Int) = listCategoria.firstOrNull { it.categoriaNo == id }
    fun findByDescricao(grupo: String?, departamento: String?, secao: String?) =
      listCategoria.firstOrNull { categoria ->
        when {
          grupo.isNullOrBlank()        -> false
          departamento.isNullOrBlank() -> categoria.grupo.equals(grupo, ignoreCase = true)
          secao.isNullOrBlank()        -> categoria.grupo.equals(
            grupo, ignoreCase = true
                                                                ) && categoria.departamento.equals(
            departamento, ignoreCase = true
                                                                                                  )
          else                         -> categoria.grupo.equals(
            grupo, ignoreCase = true
                                                                ) && categoria.departamento.equals(
            departamento, ignoreCase = true
                                                                                                  ) && categoria.secao.equals(
            secao, ignoreCase = true
                                                                                                                             )
        }
      }

    fun add(categoria: Categoria) {
      local.addCategoria(categoria)
    }

    fun update(categoria: Categoria) {
      local.updateCategoria(categoria)
    }

    fun delete(categoria: Categoria) {
      local.deleteCategoria(categoria)
    }

    fun listGrupo() = local.findAllCategoria().distinctBy { it.grupo }.sortedBy { it.categoriaNo }

    fun listDepartamento(grupo: String) =
      local.findAllCategoria().filter { it.grupo == grupo }.distinctBy { it.departamento }.sortedBy { it.categoriaNo }

    fun listSecao(grupo: String, departamento: String) =
      local.findAllCategoria().filter { it.grupo == grupo && it.departamento == departamento }.distinctBy { it.secao }
        .sortedBy { it.categoriaNo }
  }

  override fun compareTo(other: Categoria?): Int = descricao.compareTo(other?.descricao ?: "")
}