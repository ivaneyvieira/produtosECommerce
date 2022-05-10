package br.com.astrosoft.produtosECommerce.model.beans

import br.com.astrosoft.framework.util.toSaciDate
import java.time.LocalDate

class PrecoSaci(
  val codigo: Int,
  val descricao: String,
  val refprice: Double,
  val validade: Int,
  val promoprice: Double?,
  val precoCompor: Double?,
  val preco: Double?,
               ) {
  val promopricev
    get() = if (LocalDate.now().toSaciDate() <= validade) promoprice else null
}