package br.com.astrosoft.produtosECommerce.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.produtosECommerce.model.beans.*
import br.com.astrosoft.produtosECommerce.model.beans.EEditor.*
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryProduto
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryProdutoConferencia

class ProdutosEComerceViewModel(view: IProdutosEComerceView) : ViewModel<IProdutosEComerceView>(view) {

  fun serviceQueryProduto() = ServiceQueryProduto()

  fun serviceQueryProdutoConferencia() = ServiceQueryProdutoConferencia()

  fun salvaProduto(produto: Produto?) {
    if (produto != null) {
      Produto.save(produto)
      updateGrid()
    }
  }

  fun marcaProdutos(itens: List<Produto>, marca: EEditor) {
    itens.forEach { produto ->
      produto.editado = marca.value
      produto.modificado = "N"
      Produto.save(produto)
    }
    updateGrid()
  }

  fun updateGrid() {
    Marca.updateList()
    Bitola.updateList()
    Categoria.updateList()
    Marca.updateList()
    when (view.panelStatus()) {
      BASE      -> view.updateGridBase()
      EDITAR    -> view.updateGridEditar()
      EDITADO   -> view.updateGridEditado()
      IMPORTADO -> view.updateGridImportado()
      ENVIAR    -> view.updateGridEnviar()
      ENVIADO   -> view.updateGridEnviado()
      CORRECAO  -> view.updateGridCorrecao()
    }
  }

  fun replicarProdutos(itens: List<Produto>, marca: EEditor) = exec {
    val modelo =
      itens.sortedBy { it.descricao }.firstOrNull { it.descricaoCompleta.isNullOrBlank() }
      ?: fail("Nenhum produto " + "selecionado")
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

interface IProdutosEComerceView : IView {
  fun updateGridBase()
  fun updateGridEditar()
  fun updateGridEditado()
  fun updateGridImportado()
  fun updateGridEnviar()
  fun updateGridEnviado()
  fun updateGridCorrecao()

  fun panelStatus(): EEditor
  fun marcaProdutos(itens: List<Produto>, marca: EEditor)
  fun salvaProduto(bean: Produto?)
  fun replicarProdutos(itens: List<Produto>, marca: EEditor)

  val filtroEditar: FiltroProduto
  val filtroEditado: FiltroProduto
  val filtroBase: FiltroProduto
  val filtroImportado: FiltroProduto
  val filtroEnviar: FiltroProduto
  val filtroEnviado: FiltroProduto
  val filtroCorrecao: FiltroProduto
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
  var imagemCheck: Boolean = true,
                       )
