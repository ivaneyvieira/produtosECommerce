package br.com.astrosoft.produtosECommerce.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.produtosECommerce.model.beans.Categoria
import br.com.astrosoft.produtosECommerce.model.beans.Cl
import br.com.astrosoft.produtosECommerce.model.beans.EEditor
import br.com.astrosoft.produtosECommerce.model.beans.EEditor.BASE
import br.com.astrosoft.produtosECommerce.model.beans.EEditor.EDITADO
import br.com.astrosoft.produtosECommerce.model.beans.EEditor.EDITAR
import br.com.astrosoft.produtosECommerce.model.beans.Fornecedor
import br.com.astrosoft.produtosECommerce.model.beans.Marca
import br.com.astrosoft.produtosECommerce.model.beans.Produto
import br.com.astrosoft.produtosECommerce.model.beans.TypePrd

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
                                 categoria = filtro.categoria)
  }
  
  fun updateGridBase() {
    view.updateGridBase(listBase())
  }
  
  private fun listBase(): List<Produto> {
    val filtro = view.filtroBase
    return Produto.listaProdutos(codigo = filtro.codigo,
                                 descricaoI = filtro.descricaoI,
                                 descricaoF = filtro.descricaoF,
                                 fornecedor = filtro.fornecedor,
                                 type = filtro.type,
                                 cl = filtro.cl,
                                 editado = BASE,
                                 categoria = filtro.categoria)
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
  
  fun salvaProduto(produto: Produto?) {
    if(produto != null) {
      Produto.save(produto)
      updateGridEditar()
      updateGridEditado()
    }
  }
  
  fun marcaProdutos(itens: List<Produto>, marca: EEditor) {
    itens.forEach {produto ->
      produto.editado = marca.value
      Produto.save(produto)
    }
    updateGrid()
  }
  
  fun updateGrid() {
    when(view.panelStatus()) {
      BASE    -> updateGridBase()
      EDITAR  -> updateGridEditar()
      EDITADO -> updateGridEditado()
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
  val categoria: Categoria?
  
  fun isEmpty() = codigo == 0 && descricaoI == "" && descricaoF == "" && fornecedor == null
                  && type == null && cl == null && categoria == null
}

interface IFiltroEditado {
  val codigo: Int
  val descricaoI: String
  val descricaoF: String
  val fornecedor: Fornecedor?
  val type: TypePrd?
  val cl: Cl?
  val categoria: Categoria?
  
  fun isEmpty() = codigo == 0 && descricaoI == "" && descricaoF == "" && fornecedor == null
                  && type == null && cl == null && categoria == null
}

interface IFiltroBase {
  val codigo: Int
  val descricaoI: String
  val descricaoF: String
  val fornecedor: Fornecedor?
  val type: TypePrd?
  val cl: Cl?
  val categoria: Categoria?
  
  fun isEmpty() = codigo == 0 && descricaoI == "" && descricaoF == "" && fornecedor == null
                  && type == null && cl == null && categoria == null
}

interface IProdutosEComerceView: IView {
  fun updateGridEditar(itens: List<Produto>)
  fun updateGridEditado(itens: List<Produto>)
  fun updateGridBase(itens: List<Produto>)
  fun panelStatus(): EEditor
  
  fun marcaProdutos(itens: List<Produto>, marca: EEditor)
  
  fun salvaProduto(bean: Produto?)
  
  val filtroEditar: IFiltroEditar
  val filtroEditado: IFiltroEditado
  val filtroBase: IFiltroBase
}

data class ProcessaBean(var marca: Marca? = null,
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
