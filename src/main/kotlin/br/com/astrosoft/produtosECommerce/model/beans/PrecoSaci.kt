package br.com.astrosoft.produtosECommerce.model.beans

import br.com.astrosoft.framework.util.toSaciDate
import java.time.LocalDate

class PrecoSaci(val codigo: Int,
                var grade: String,
                val descricao: String,
                val refprice: Double,
                val validade: Int,
                val promoprice: Double?,
                val precoCompor: Double?,
                val preco: Double?,
                val promono: Int?,
                val precoPromoEditor: Double?) {
  val promopricev
    get() = if (LocalDate.now().toSaciDate() <= validade) promoprice else null
  val validadev
    get() = if (LocalDate.now().toSaciDate() <= validade) validade else null
}