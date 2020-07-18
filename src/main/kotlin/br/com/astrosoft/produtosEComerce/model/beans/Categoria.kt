package br.com.astrosoft.produtosEComerce.model.beans

data class Categoria(val categoriaNo: Int,
                     val grupo: String,
                     val departamento: String,
                     val secao: String) {
  val descricao
    get() = "$grupo/$departamento/$secao"
}