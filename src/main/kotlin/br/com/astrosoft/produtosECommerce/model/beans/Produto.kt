package br.com.astrosoft.produtosECommerce.model.beans

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.model.ILookup
import br.com.astrosoft.framework.util.normalize
import br.com.astrosoft.produtosECommerce.model.local
import br.com.astrosoft.produtosECommerce.model.saci

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
  var bitola: Int,
  var imagem: String,
  var peso: Double,
  var altura: Double,
  var comprimento: Double,
  var largura: Double,
  var textLink: String,
  var especificacoes: String,
  var editado: Int,
  val precoCheio: Double,
  val ncm: String
             ): ILookup {
  val marcaDesc
    get() = marcaBean?.name ?: ""
  var categoriaBean
    get() = Categoria.findById(categoria)
    set(value) {
      categoria = value?.categoriaNo ?: 0
    }
  var marcaBean
    get() = Marca.findById(marca)
    set(value) {
      marca = value?.marcaNo ?: 0
    }
  var bitolaBean
    get() = Bitola.findById(bitola)
    set(value) {
      bitola = value?.bitolaNo ?: 0
    }
  val categoriaDesc
    get() = categoriaBean?.descricao ?: ""
  
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
        .map {
          it.textLink = it.descricaoCompleta.normalize()
          it
        }
    }
    
    fun save(bean: Produto) {
      bean.textLink = bean.descricaoCompleta.normalize()
      local.salvaProduto(bean)
    }
  }
  
  override val lookupValue: String
    get() = "$codigo $descricao"
  
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
  
  fun descricaoCompletaPlanilha(): String {
    return if(bitolaBean == null)
      "$descricaoCompleta - $marcaDesc"
    else
      "$descricaoCompleta ${bitolaBean?.name}- $marcaDesc"
  }
  
  fun imagem1(): String {
    return imagem.split(" +".toRegex())
             .toList()
             .getOrNull(0) ?: ""
  }
  
  fun imagem2(): String {
    return imagem.split(" +".toRegex())
             .toList()
             .getOrNull(1) ?: ""
  }
  
  fun imagem3(): String {
    return imagem.split(" +".toRegex())
             .toList()
             .getOrNull(2) ?: ""
  }
  
  fun imagem4(): String {
    return imagem.split(" +".toRegex())
             .toList()
             .getOrNull(3) ?: ""
  }
  
  fun imagem5(): String {
    return imagem.split(" +".toRegex())
             .toList()
             .getOrNull(4) ?: ""
  }
  
  fun saldoLoja4(): Double {
    val saldo = saci.saldoLoja4(codigo, grade)
    return saldo.firstOrNull()?.saldo ?: 0.00
  }
  
  fun price(): Double {
    val price = saci.price(codigo)
    return price.firstOrNull()?.price ?: 0.00
  }
  
  fun grupo(): String {
    return categoriaBean?.grupo ?: ""
  }
  
  fun departamento(): String {
    val departamento = categoriaBean?.departamento ?: ""
    return if(departamento == "")
      grupo()
    else
      "${grupo()}/$departamento"
  }
  
  fun secao(): String {
    val secao = categoriaBean?.secao ?: ""
    return if(secao == "")
      departamento()
    else
      "${departamento()}/$secao"
  }
}

enum class EEditor(val value: Int) {
  BASE(0),
  EDITAR(1),
  EDITADO(2),
  IMPORTADO(3)
}

