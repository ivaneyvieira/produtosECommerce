package br.com.astrosoft.produtosECommerce.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.produtosECommerce.model.beans.*
import br.com.astrosoft.produtosECommerce.model.beans.EEditor.*
import java.time.LocalDateTime

class ProdutosEComerceViewModel(view: IProdutosEComerceView) : ViewModel<IProdutosEComerceView>(view) {
  fun updateGridEditar() {
    view.updateGridEditar(listEditar())
  }

  private fun listEditar(): List<Produto> {
    val filtro = view.filtroEditar
    return Produto.listaProdutos(
      codigo = filtro.codigo,
      descricaoI = filtro.descricaoI,
      descricaoF = filtro.descricaoF,
      fornecedor = filtro.fornecedor,
      type = filtro.type,
      cl = filtro.cl,
      editado = EDITAR,
      categoria = filtro.categoria
                                )
  }

  fun updateGridBase() {
    view.updateGridBase(listBase())
  }

  private fun listBase(): List<Produto> {
    val filtro = view.filtroBase
    return Produto.listaProdutos(
      codigo = filtro.codigo,
      descricaoI = filtro.descricaoI,
      descricaoF = filtro.descricaoF,
      fornecedor = filtro.fornecedor,
      type = filtro.type,
      cl = filtro.cl,
      editado = BASE,
      categoria = filtro.categoria
                                )
  }

  fun updateGridEditado() {
    view.updateGridEditado(listEditado())
  }

  private fun listEditado(): List<Produto> {
    val filtro = view.filtroEditado
    return Produto.listaProdutos(
      codigo = filtro.codigo,
      descricaoI = filtro.descricaoI,
      descricaoF = filtro.descricaoF,
      fornecedor = filtro.fornecedor,
      type = filtro.type,
      cl = filtro.cl,
      editado = EDITADO,
      categoria = filtro.categoria
                                )
  }

  fun salvaProduto(produto: Produto?) {
    if (produto != null) {
      Produto.save(produto)
      updateGridEditar()
      updateGridEditado()
    }
  }

  fun updateGridImportado() {
    view.updateGridImportado(listImportado())
  }

  private fun listImportado(): List<Produto> {
    val filtro = view.filtroImportado
    return Produto.listaProdutos(
      codigo = filtro.codigo,
      descricaoI = filtro.descricaoI,
      descricaoF = filtro.descricaoF,
      fornecedor = filtro.fornecedor,
      type = filtro.type,
      cl = filtro.cl,
      editado = IMPORTADO,
      categoria = filtro.categoria
                                )
  }

  fun marcaProdutos(itens: List<Produto>, marca: EEditor) {
    itens.forEach { produto ->
      produto.editado = marca.value
      produto.dataHoraMudanca = LocalDateTime.now()
      Produto.save(produto)
    }
    updateGrid()
  }

  fun updateGrid() {
    when (view.panelStatus()) {
      BASE      -> updateGridBase()
      EDITAR    -> updateGridEditar()
      EDITADO   -> updateGridEditado()
      IMPORTADO -> updateGridImportado()
    }
  }

  fun replicarProdutos(itens: List<Produto>, marca: EEditor) = exec {
    val modelo =
      itens.sortedBy { it.descricao }.firstOrNull { it.descricaoCompleta.isNullOrBlank() } ?: fail(
        "Nenhum produto " + "selecionado"
                                                                                               )
    itens.forEach { produto ->
      produto.marca = modelo.marca
      produto.categoria = modelo.categoria
      produto.descricaoCompleta = modelo.descricaoCompleta
      produto.bitola = modelo.bitola
      produto.imagem = modelo.imagem
      produto.peso = modelo.peso
      produto.comprimento = modelo.comprimento
      produto.largura = modelo.largura
      produto.altura = modelo.altura
      produto.textLink = modelo.textLink
      produto.especificacoes = modelo.especificacoes
      produto.editado = marca.value
      Produto.save(produto)
    }
    updateGrid()
  }
}

interface IFiltroEditar {
  val codigo: Int
  val descricaoI: String
  val descricaoF: String
  val fornecedor: Fornecedor?
  val type: TypePrd?
  val cl: Cl?
  val categoria: Categoria?

  fun isEmpty() =
    codigo == 0 && descricaoI == "" && descricaoF == "" && fornecedor == null && type == null && cl == null && categoria == null
}

interface IFiltroEditado {
  val codigo: Int
  val descricaoI: String
  val descricaoF: String
  val fornecedor: Fornecedor?
  val type: TypePrd?
  val cl: Cl?
  val categoria: Categoria?

  fun isEmpty() =
    codigo == 0 && descricaoI == "" && descricaoF == "" && fornecedor == null && type == null && cl == null && categoria == null
}

interface IFiltroBase {
  val codigo: Int
  val descricaoI: String
  val descricaoF: String
  val fornecedor: Fornecedor?
  val type: TypePrd?
  val cl: Cl?
  val categoria: Categoria?

  fun isEmpty() =
    codigo == 0 && descricaoI == "" && descricaoF == "" && fornecedor == null && type == null && cl == null && categoria == null
}

interface IFiltroImportado {
  val codigo: Int
  val descricaoI: String
  val descricaoF: String
  val fornecedor: Fornecedor?
  val type: TypePrd?
  val cl: Cl?
  val categoria: Categoria?

  fun isEmpty() =
    codigo == 0 && descricaoI == "" && descricaoF == "" && fornecedor == null && type == null && cl == null && categoria == null
}

interface IProdutosEComerceView : IView {
  fun updateGridEditar(itens: List<Produto>)
  fun updateGridEditado(itens: List<Produto>)
  fun updateGridBase(itens: List<Produto>)
  fun updateGridImportado(itens: List<Produto>)
  fun panelStatus(): EEditor

  fun marcaProdutos(itens: List<Produto>, marca: EEditor)

  fun salvaProduto(bean: Produto?)
  fun replicarProdutos(itens: List<Produto>, marca: EEditor)

  val filtroEditar: IFiltroEditar
  val filtroEditado: IFiltroEditado
  val filtroBase: IFiltroBase
  val filtroImportado: IFiltroImportado
}

data class ProcessaBean(
  var marca: Marca? = null,
  var marcaCheck: Boolean = true,
  var categoria: Categoria? = null,
  var categoriaCheck: Boolean = true,
  var descricaoCompleta: String? = "",
  var descricaoCompletaCheck: Boolean = true,
  var bitola: Int? = 0,
  var bitolaCheck: Boolean = true,
  var imagem: String? = "",
  var imagemCheck: Boolean = true
                       )
