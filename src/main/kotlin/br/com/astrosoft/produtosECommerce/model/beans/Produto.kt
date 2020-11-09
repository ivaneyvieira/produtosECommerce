package br.com.astrosoft.produtosECommerce.model.beans

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.model.ILookup
import br.com.astrosoft.framework.util.normalize
import br.com.astrosoft.produtosECommerce.model.beans.EVariacao.COM_VARIACAO
import br.com.astrosoft.produtosECommerce.model.beans.EVariacao.SIMPLES
import br.com.astrosoft.produtosECommerce.model.beans.EVariacao.VARIACAO
import br.com.astrosoft.produtosECommerce.model.local
import br.com.astrosoft.produtosECommerce.model.saci

class Produto(
  val codigo: String,
  val grade: String,
  var gradeCompleta: String?,
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
  val ncm: String,
  val variacao: EVariacao? = SIMPLES
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
          it.textLink = it.descricaoCompleta.normalize("-")
          it
        }
    }
    
    fun save(bean: Produto) {
      bean.textLink = bean.descricaoCompleta.normalize("-")
      local.salvaProduto(bean)
    }
  }
  
  override val lookupValue: String
    get() = "$codigo $descricao"
  
  fun descricaoCompletaPlanilha(): String {
    val parteBitola = when(bitolaBean) {
      null -> ""
      else -> "${bitolaBean?.name} - "
    }
    val parteGrade = when {
      !gradeCompleta.isNullOrBlank() -> "$gradeCompleta - "
      else                           -> ""
    }
    return "$descricaoCompleta - $parteBitola $parteGrade $marcaDesc"
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
    val saldo = if(variacao == COM_VARIACAO) saci.saldoLoja4(codigo, "") else saci.saldoLoja4(codigo, grade)
    return saldo.firstOrNull()?.saldo ?: 0.00
  }
  
  fun price(): Double {
    val price = saci.price(codigo)
    return price.firstOrNull()?.price ?: 0.00
  }
  
  fun grupo() = if(variacao == VARIACAO) ""
  else categoriaBean?.grupo ?: ""
  
  fun departamento() = if(variacao == VARIACAO) ""
  else categoriaBean?.departamento ?: ""
  
  fun secao() = if(variacao == VARIACAO) ""
  else categoriaBean?.secao ?: ""
  
  fun tipoVariacao() = variacao?.descricao ?: SIMPLES.descricao
  
  fun ean(): String {
    val price = saci.price(codigo)
    return price.firstOrNull()?.gtin ?: ""
  }
  
  fun palavrasChave() = if(variacao == VARIACAO) ""
  else listOf(grupo(), departamento(), secao(), marcaDesc).filter {it.trim() != ""}
    .joinToString(",")
  
  fun nomeProduto() = if(variacao == VARIACAO) "" else "${descricaoCompleta} - ${marcaDesc}"
  
  fun descricaoDetalhada() = if(variacao == VARIACAO) "" else "${descricaoCompleta} ${marcaDesc}"
  fun descricao() = if(variacao == VARIACAO) "" else especificacoes
  
  fun skuPai() = if(variacao == COM_VARIACAO) codigo else ""
  fun sku() = when(variacao) {
    COM_VARIACAO -> ""
    SIMPLES -> codigo
    VARIACAO -> barcode
    else         -> codigo
  }
  
  fun slugProduto() = if(variacao == VARIACAO) "" else descricaoCompleta.normalize(" ")
  fun marca() = if(variacao == VARIACAO) "" else marcaDesc
  fun tituloMarca() = if(variacao == VARIACAO) "" else textLink
  fun descricaoPagina() = if(variacao == VARIACAO) "" else descricaoCompleta
  fun gradeCor() = if(variacao == VARIACAO) gradeCompleta ?: "" else ""
  fun cor() = if(variacao == VARIACAO) "Cor" else ""
  
  fun chave() = ChaveProduto(codigo, grade)
  fun copy(variacaoNova: EVariacao) = Produto(codigo,
                                              grade,
                                              gradeCompleta,
                                              barcode,
                                              descricao,
                                              vendno,
                                              fornecedor,
                                              typeno,
                                              typeName,
                                              clno,
                                              clname,
                                              marca,
                                              categoria,
                                              descricaoCompleta,
                                              bitola,
                                              imagem,
                                              peso,
                                              altura,
                                              comprimento,
                                              largura,
                                              textLink,
                                              especificacoes,
                                              editado,
                                              precoCheio,
                                              ncm, variacaoNova)
  
  override fun equals(other: Any?): Boolean {
    if(this === other) return true
    if(javaClass != other?.javaClass) return false
    
    other as Produto
    
    if(codigo != other.codigo) return false
    if(grade != other.grade) return false
    if(variacao != other.variacao) return false
    
    return true
  }
  
  override fun hashCode(): Int {
    var result = codigo.hashCode()
    result = 31 * result + grade.hashCode()
    result = 31 * result + variacao.hashCode()
    return result
  }
}

data class ChaveProduto(val codigo: String, val grade: String)

enum class EEditor(val value: Int) {
  BASE(0),
  EDITAR(1),
  EDITADO(2),
  IMPORTADO(3)
}

enum class EVariacao(val descricao: String) {
  SIMPLES("simples"),
  VARIACAO("variacao"),
  COM_VARIACAO("com-variacao")
}

fun List<Produto>.explodeGrade(): List<Produto> {
  this.distinctBy {}
  val comVariacao =
    this.distinctBy {it.codigo}
      .map {it.copy(COM_VARIACAO)}
  val variacao = this.map {it.copy(VARIACAO)}
  return comVariacao + variacao
  /*
  return this.groupBy {it.chave()}
    .flatMap {entry ->
      val list = entry.value
      val comVariacao = list.firstOrNull()?.copy(COM_VARIACAO)
      val variacao = list.map {it.copy(VARIACAO)}
      listOfNotNull(comVariacao) +  variacao
    }
    
   */
}

