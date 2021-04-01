package br.com.astrosoft.produtosECommerce.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.produtosECommerce.model.beans.Categoria

class CategoriaViewModel(view: ICategoriaView) : ViewModel<ICategoriaView>(view) {
  fun findAll(): List<Categoria>? {
    return Categoria.findAll()
  }

  fun add(categoria: Categoria): Categoria? {
    exec {
      if (categoria.categoriaNo == 0) {
        val codigoGrupo = novoCodigoGrupo(categoria)
        val codigoDepartamento = novoCodigoDepartamento(categoria)
        val codigoSecao = novoCodigoSecao(categoria)
        categoria.categoriaNo =
          Categoria.makeCodigo(codigoGrupo, codigoDepartamento, codigoSecao).toIntOrNull() ?: 0
      }
      Categoria.add(categoria)
    }
    return categoria
  }

  private fun novoCodigoSecao(categoria: Categoria): Int {
    return if (categoria.departamento == "" || categoria.grupo == "" || categoria.secao == "") 0
    else {
      val listSecao = Categoria.listSecao(categoria.grupo, categoria.departamento)
      val catList = listSecao.firstOrNull { it.secao == categoria.secao }
      catList?.codigoSecao ?: (listSecao.map { it.codigoSecao }.maxOrNull() ?: 0) + 1
    }
  }

  private fun novoCodigoDepartamento(categoria: Categoria): Int {
    return if (categoria.departamento == "" || categoria.grupo == "") 0
    else {
      val listDepartamento = Categoria.listDepartamento(categoria.grupo)
      val catList = listDepartamento.firstOrNull { it.departamento == categoria.departamento }
      catList?.codigoDepartamento ?: (listDepartamento.map { it.codigoDepartamento }.maxOrNull()
        ?: 0) + 1
    }
  }

  private fun novoCodigoGrupo(categoria: Categoria): Int {
    return if (categoria.grupo == "") 0
    else {
      val listGrupo = Categoria.listGrupo()
      val catList = listGrupo.firstOrNull { it.grupo == categoria.grupo }
      catList?.codigoGrupo ?: (listGrupo.map { it.codigoGrupo }.maxOrNull() ?: 0) + 1
    }
  }

  fun update(categoria: Categoria?): Categoria? {
    exec {
      if (categoria != null) Categoria.update(categoria)
    }
    return categoria
  }

  fun delete(categoria: Categoria?) {
    exec {
      if (categoria != null) Categoria.delete(categoria)
    }
  }

  fun listGrupo() = Categoria.listGrupo()

  fun listDepartamento(grupo: String) = Categoria.listDepartamento(grupo)

  fun listSecao(grupo: String, departamento: String) = Categoria.listSecao(grupo, departamento)
}

interface ICategoriaView : IView