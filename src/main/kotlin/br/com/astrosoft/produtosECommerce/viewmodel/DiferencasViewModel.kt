package br.com.astrosoft.produtosECommerce.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.produtosECommerce.model.beans.EEditor
import br.com.astrosoft.produtosECommerce.model.beans.EEditor.CONFERENCIA
import br.com.astrosoft.produtosECommerce.model.beans.Produto
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryProduto
import br.com.astrosoft.produtosECommerce.model.services.ServiceQueryProdutoConferencia

class DiferencasViewModel(view: IDiferencasView) : ViewModel<IDiferencasView>(view) {

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
    when (view.panelStatus()) {
      CONFERENCIA -> view.updateGridConferencia()
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

interface IDiferencasView : IView {
  fun updateGridConferencia()

  fun panelStatus(): EEditor
  fun marcaProdutos(itens: List<Produto>, marca: EEditor)
}


