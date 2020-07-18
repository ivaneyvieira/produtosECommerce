package br.com.astrosoft.produtosEComerce.model.beans

import br.com.astrosoft.AppConfig
import br.com.astrosoft.produtosEComerce.model.saci

data class Produto(
  val codigo: String,
  val grade: String,
  val barcode: String,
  val descricao: String,
  val vendno: Int,
  val fornecedor: String,
  val typeno: Int,
  val typeName: String,
  val clno: Int,
  val clname: String,
  val marca: String,
  val categoria: Int,
  val descricaoCompleta: String,
  val bitola: String,
  val imagem: String,
  val peso: Double,
  val altura: Double,
  val comprimento: Double,
  val largura: Double,
  val editado: Int
                  ) {
  companion object {
    private val userSaci: UserSaci by lazy {
      AppConfig.userSaci as UserSaci
    }
    
    fun listaProdutos(codigo: Int, descricaoI: String, descricaoF: String, fornecedor: Fornecedor?,
                      type: TypePrd?, cl: Cl?, editado: EEditor?, categoria: Categoria?): List<Produto> {
      return saci.listaProdutos(codigo = codigo,
                                descricaoI = descricaoI,
                                descricaoF = descricaoF,
                                vendno = fornecedor?.vendno ?: 0,
                                typeno = type?.typeno ?: 0,
                                clno = cl?.clno ?: 0,
                                editado = editado?.value ?: 0,
                                categoria = categoria?.categoriaNo ?: 0)
    }
  }
}

enum class EEditor(val value: Int) {
  TODOS(-1),
  EDITAR(0),
  EDITADO(1)
}

