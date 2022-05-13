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
  this.setClassNameGenerator {
    if (it.promocaoSaci == "N" && it.promocaoWeb == "S") "marcaDiferenca" else null
  }
}

fun Grid<ProdutoPromocao>.colValidade() = addColumnLocalDate(ProdutoPromocao::validade) {
  setHeader("Validade")
  this.setClassNameGenerator {
    if (it.dataAlinhada == "N") "marcaDiferenca" else null
  }
}

fun Grid<ProdutoPromocao>.colValidadeWeb() = addColumnLocalDate(ProdutoPromocao::promoDateWeb) {
  setHeader("Validade Web")
  this.setClassNameGenerator {
    if (it.dataAlinhada == "N") "marcaDiferenca" else null
  }
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
  setHeader("Pro Saci")
  width = "8em"
  this.setClassNameGenerator {
    if (it.precoAlinhado == "N") "marcaDiferenca" else null
  }
}

fun Grid<ProdutoPromocao>.colPrecoPromoWeb() = addColumnDouble(ProdutoPromocao::precoPromoWeb) {
  setHeader("Promo Web")
  width = "8em"
  this.setClassNameGenerator {
    if (it.precoAlinhado == "N") "marcaDiferenca" else null
  }
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
