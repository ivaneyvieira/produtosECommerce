package br.com.astrosoft.produtosECommerce.view.promocao

import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import br.com.astrosoft.produtosECommerce.model.beans.ProdutoPromocao
import com.vaadin.flow.component.grid.Grid


fun Grid<ProdutoPromocao>.colCodigo() = addColumnString(ProdutoPromocao::codigo) {
  setHeader("Código")
}

fun Grid<ProdutoPromocao>.colDescricao() = addColumnString(ProdutoPromocao::descricao) {
  setHeader("Descrição")
}

fun Grid<ProdutoPromocao>.colValidade()= addColumnLocalDate(ProdutoPromocao::validade){
  setHeader("Validação")
}

fun Grid<ProdutoPromocao>.colPrecoRef()= addColumnDouble(ProdutoPromocao::precoRef){
  setHeader("Preço Ref")
}

fun Grid<ProdutoPromocao>.colPerc()= addColumnDouble(ProdutoPromocao::perc){
  setHeader("Perc %")
}

fun Grid<ProdutoPromocao>.colPrecoPromo()= addColumnDouble(ProdutoPromocao::precoPromo){
  setHeader("Preço Promo")
}

fun Grid<ProdutoPromocao>.colVendno()= addColumnInt(ProdutoPromocao::vendno){
  setHeader("Fornecedor")
}

fun Grid<ProdutoPromocao>.colAbrev()= addColumnString(ProdutoPromocao::abrev){
  setHeader("Abrev")
}

fun Grid<ProdutoPromocao>.colTipo()= addColumnInt(ProdutoPromocao::tipo){
  setHeader("Tipo")
}

fun Grid<ProdutoPromocao>.colCentLucro()= addColumnInt(ProdutoPromocao::centLucro){
  setHeader("CL")
}

fun Grid<ProdutoPromocao>.colSaldo()= addColumnDouble(ProdutoPromocao::saldo){
  setHeader("Saldo")
}
