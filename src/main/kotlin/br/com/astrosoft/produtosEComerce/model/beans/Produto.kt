package br.com.astrosoft.produtosEComerce.model.beans

import br.com.astrosoft.AppConfig
import br.com.astrosoft.produtosEComerce.model.local

class Produto(
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
  var marca: Int,
  var categoria: Int,
  var descricaoCompleta: String,
  var bitola: String,
  var imagem: String,
  val peso: Double,
  val altura: Double,
  val comprimento: Double,
  val largura: Double,
  var editado: Int
                  ) {
  
  
  val categoriaDesc
    get() = Categoria.findById(categoria)?.descricao ?: ""
  val marcaDesc
    get() = Marca.findById(marca)?.name ?: ""
  
  companion object {
    private val userSaci: UserSaci
      get() = AppConfig.userSaci as UserSaci
    
    fun listaProdutos(codigo: Int, descricaoI: String, descricaoF: String, fornecedor: Fornecedor?,
                      type: TypePrd?, cl: Cl?, editado: EEditor?, categoria: Categoria?): List<Produto> {
      return local.listaProdutos(codigo = codigo,
                                 descricaoI = descricaoI,
                                 descricaoF = descricaoF,
                                 vendno = fornecedor?.vendno ?: 0,
                                 typeno = type?.typeno ?: 0,
                                 clno = cl?.clno ?: "",
                                 editado = editado?.value ?: 0,
                                 categoria = categoria?.categoriaNo ?: 0)
    }
    
    fun save(bean: Produto) {
      local.salvaProduto(bean)
    }
  }
  
  override fun equals(other: Any?): Boolean {
    if(this === other) return true
    if(javaClass != other?.javaClass) return false
    
    other as Produto
    
    if(codigo != other.codigo) return false
    if(grade != other.grade) return false
    
    return true
  }
  
  override fun hashCode(): Int {
    var result = codigo.hashCode()
    result = 31 * result + grade.hashCode()
    return result
  }
}

enum class EEditor(val value: Int) {
  TODOS(-1),
  EDITAR(0),
  EDITADO(1)
}

