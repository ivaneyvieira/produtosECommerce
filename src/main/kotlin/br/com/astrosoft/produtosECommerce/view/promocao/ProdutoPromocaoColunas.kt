package br.com.astrosoft.produtosECommerce.view.promocao

import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import br.com.astrosoft.produtosECommerce.model.beans.ProdutoPromocao
import com.vaadin.flow.component.grid.Grid


fun Grid<ProdutoPromocao>.colWeb() = addColumnString(ProdutoPromocao::web) {
  setHeader("Web")
}

fun Grid<ProdutoPromocao>.colPromoNo() = addColumnInt(ProdutoPromocao::promono) {
  setHeader("Nº")
  width = "4em"
}

fun Grid<ProdutoPromocao>.colCodigo() = addColumnString(ProdutoPromocao::codigo) {
  setHeader("Código")
}

fun Grid<ProdutoPromocao>.colDescricao() = addColumnString(ProdutoPromocao::descricao) {
  setHeader("Descrição")
}

fun Grid<ProdutoPromocao>.colValidade() = addColumnLocalDate(ProdutoPromocao::validade) {
  setHeader("Validade")
}

fun Grid<ProdutoPromocao>.colPrecoRef() = addColumnDouble(ProdutoPromocao::precoRef) {
  setHeader("Preço Ref")
  width = "7em"
}

fun Grid<ProdutoPromocao>.colPerc() = addColumnDouble(ProdutoPromocao::perc) {
  setHeader("Perc %")
  width = "5em"
}

fun Grid<ProdutoPromocao>.colPrecoPromo() = addColumnDouble(ProdutoPromocao::precoPromo) {
  setHeader("Preço Promo")
  width = "8em"
}

fun Grid<ProdutoPromocao>.colVendno() = addColumnInt(ProdutoPromocao::vendno) {
  setHeader("Fornecedor")
}

fun Grid<ProdutoPromocao>.colAbrev() = addColumnString(ProdutoPromocao::abrev) {
  setHeader("Abrev")
}

fun Grid<ProdutoPromocao>.colTipo() = addColumnInt(ProdutoPromocao::tipo) {
  setHeader("Tipo")
  width = "5em"
}

fun Grid<ProdutoPromocao>.colCentLucro() = addColumnInt(ProdutoPromocao::centLucro) {
  setHeader("CL")
  width = "5em"
}

fun Grid<ProdutoPromocao>.colSaldo() = addColumnInt(ProdutoPromocao::saldo) {
  setHeader("Saldo")
  width = "5em"
}
