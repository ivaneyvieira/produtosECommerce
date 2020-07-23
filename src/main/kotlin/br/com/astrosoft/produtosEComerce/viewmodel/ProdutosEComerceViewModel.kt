package br.com.astrosoft.produtosEComerce.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.produtosEComerce.model.beans.Categoria
import br.com.astrosoft.produtosEComerce.model.beans.Cl
import br.com.astrosoft.produtosEComerce.model.beans.EEditor.EDITADO
import br.com.astrosoft.produtosEComerce.model.beans.EEditor.EDITAR
import br.com.astrosoft.produtosEComerce.model.beans.Fornecedor
import br.com.astrosoft.produtosEComerce.model.beans.Marca
import br.com.astrosoft.produtosEComerce.model.beans.Produto
import br.com.astrosoft.produtosEComerce.model.beans.TypePrd

class ProdutosEComerceViewModel(view: IProdutosEComerceView): ViewModel<IProdutosEComerceView>(view) {
  fun updateGridEditar() {
    view.updateGridEditar(listEditar())
  }
  
  private fun listEditar(): List<Produto> {
    val filtro = view.filtroEditar
    return Produto.listaProdutos(codigo = filtro.codigo,
                                 descricaoI = filtro.descricaoI,
                                 descricaoF = filtro.descricaoF,
                                 fornecedor = filtro.fornecedor,
                                 type = filtro.type,
                                 cl = filtro.cl,
                                 editado = EDITAR,
                                 categoria = null)
  }
  
  fun updateGridEditado() {
    view.updateGridEditado(listEditado())
  }
  
  private fun listEditado(): List<Produto> {
    val filtro = view.filtroEditado
    return Produto.listaProdutos(codigo = filtro.codigo,
                                 descricaoI = filtro.descricaoI,
                                 descricaoF = filtro.descricaoF,
                                 fornecedor = filtro.fornecedor,
                                 type = filtro.type,
                                 cl = filtro.cl,
                                 editado = EDITADO,
                                 categoria = filtro.categoria)
  }
  
  fun processaProduto(bean: ProcessaBean, itens: List<Produto>) {
    val descricoes =
      itens.map {it.descricao}
        .distinct()
    itens.forEach {produto ->
      if(!bean.bitolaCheck) {
        val descricao = produto.descricao
        val bitola = ProcessaDescricaoProduto.findSulfixo(descricao, descricoes)
        produto.bitola = bitola
      }
      processaProduto(bean, produto)
    }
    updateGridEditar()
  }
  
  private fun processaProduto(bean: ProcessaBean, produto: Produto) {
    if(bean.marcaCheck)
      produto.marca = bean.marca?.marcaNo ?: 0
    if(bean.categoriaCheck)
      produto.categoria = bean.categoria?.categoriaNo ?: 0
    if(bean.descricaoCompletaCheck)
      produto.descricaoCompleta = bean.descricaoCompleta ?: ""
    if(bean.bitolaCheck)
      produto.bitola = bean.bitola ?: ""
    if(bean.imagemCheck)
      produto.imagem = bean.imagem ?: ""
    produto.editado = EDITADO.value
    Produto.save(produto)
  }
  
  fun desProcessaProduto(itens: List<Produto>) {
    itens.forEach {produto ->
      desProcessaProduto(produto)
    }
    updateGridEditado()
  }
  
  private fun desProcessaProduto(produto: Produto) {
    produto.marca = 0
    produto.categoria = 0
    produto.descricaoCompleta = ""
    produto.bitola = ""
    produto.imagem = ""
    produto.editado = EDITAR.value
    Produto.save(produto)
  }
  
  fun salvaProduto(produto: Produto?) {
    if(produto != null) {
      Produto.save(produto)
      updateGridEditar()
      updateGridEditado()
    }
  }
}

interface IFiltroEditar {
  val codigo: Int
  val descricaoI: String
  val descricaoF: String
  val fornecedor: Fornecedor?
  val type: TypePrd?
  val cl: Cl?
  
  fun isEmpty() = codigo == 0 && descricaoI == "" && descricaoF == "" && fornecedor == null
                  && type == null && cl == null
}

interface IFiltroEditado {
  val codigo: Int
  val descricaoI: String
  val descricaoF: String
  val fornecedor: Fornecedor?
  val type: TypePrd?
  val cl: Cl?
  val categoria: Categoria?
  
  fun empty() = codigo == 0 && descricaoI == "" && descricaoF == "" && fornecedor == null
                && type == null && cl == null && categoria == null
}

interface IProdutosEComerceView: IView {
  fun updateGridEditar(itens: List<Produto>)
  fun updateGridEditado(itens: List<Produto>)
  fun processaProdutos(itens: List<Produto>)
  fun desProcessaProdutos(itens: List<Produto>)
  fun salvaProduto(bean: Produto?)
  
  val filtroEditar: IFiltroEditar
  val filtroEditado: IFiltroEditado
}

data class ProcessaBean(var marca: Marca? = null,
                        var marcaCheck: Boolean = true,
                        var categoria: Categoria? = null,
                        var categoriaCheck: Boolean = true,
                        var descricaoCompleta: String? = "",
                        var descricaoCompletaCheck: Boolean = true,
                        var bitola: String? = "",
                        var bitolaCheck: Boolean = true,
                        var imagem: String? = "",
                        var imagemCheck: Boolean = true
                       )
