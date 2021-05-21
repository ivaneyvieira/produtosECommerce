package br.com.astrosoft.produtosECommerce.model.beans

import br.com.astrosoft.framework.util.format
import br.com.astrosoft.produtosECommerce.model.saci
import java.time.LocalDate

class Promocao(
  val promoNo: Int, val descricao: String, val vencimento: LocalDate
              ) {
  val vencimentoFormat
    get() = vencimento.format()

  companion object {
    fun findPromocoesViergentes() = saci.findPromocoesViergentes()
  }
}