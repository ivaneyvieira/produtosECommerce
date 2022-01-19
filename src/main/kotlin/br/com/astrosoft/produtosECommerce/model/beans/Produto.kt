package br.com.astrosoft.produtosECommerce.model.beans

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.model.ILookup
import br.com.astrosoft.framework.util.normalize
import br.com.astrosoft.produtosECommerce.model.beans.EVariacao.*
import br.com.astrosoft.produtosECommerce.model.local
import br.com.astrosoft.produtosECommerce.model.saci
import java.time.LocalDateTime
import kotlin.concurrent.thread

class Produto(
  val seq: Int,
  val codigo: String,
  val grade: String,
  var gradeCompleta: String?,
  val barcode: String?,
  val descricao: String,
  val vendno: Int,
  val fornecedor: String?,
  val typeno: Int,
  val typeName: String?,
  val clno: String,
  val clname: String?,
  var marca: Int?,
  val marcaNome: String?,
  var categoria: Int?,
  val categoriaNome: String?,
  var descricaoCompleta: String?,
  var bitola: Int?,
  var imagem: String?,
  var peso: Double?,
  var altura: Double?,
  var comprimento: Double?,
  var largura: Double?,
  var textLink: String?,
  var especificacoes: String?,
  var editado: Int?,
  val precoCheio: Double?,
  val ncm: String,
  var cor: String?,
  val variacao: String,
  var corStr: String,
  var dataHoraMudanca: LocalDateTime,
  var userno: Int?,
  var modificado: String,
  var gradeAlternativa: String,
) : ILookup {
  val userName: String?
    get() = if (userno == null) null
    else saci.findAllUser().firstOrNull {
      it.no == userno
    }?.name
  val marcaDesc
    get() = marcaBean?.name ?: ""
  var categoriaBean
    get() = Categoria.findById(categoria ?: 0)
    set(value) {
      categoria = value?.categoriaNo ?: 0
    }
  var marcaBean
    get() = Marca.findById(marca ?: 0)
    set(value) {
      marca = value?.marcaNo ?: 0
    }
  var bitolaBean
    get() = Bitola.findById(bitola ?: 0)
    set(value) {
      bitola = value?.bitolaNo ?: 0
    }
  val categoriaDesc
    get() = categoriaBean?.descricao ?: ""
  val corStrOld
    get() = GradeCor.findAll().firstOrNull { it.descricao == grade && grade != "" }?.codigoCor

  override val lookupValue: String
    get() = "$codigo $descricao"

  fun descricaoCompletaPlanilha(): String {
    val parteBitola = when (bitolaBean) {
      null -> ""
      else -> "${bitolaBean?.name} - "
    }
    val parteGrade = when {
      !gradeCompleta.isNullOrBlank() -> "$gradeCompleta - "
      else -> ""
    }
    return "$descricaoCompleta - $parteBitola $parteGrade $marcaDesc"
  }

  fun imagem1(): String {
    return imagem?.split(" +".toRegex())?.toList()?.getOrNull(0) ?: ""
  }

  fun imagem2(): String {
    return imagem?.split(" +".toRegex())?.toList()?.getOrNull(1) ?: ""
  }

  fun imagem3(): String {
    return imagem?.split(" +".toRegex())?.toList()?.getOrNull(2) ?: ""
  }

  fun imagem4(): String {
    return imagem?.split(" +".toRegex())?.toList()?.getOrNull(3) ?: ""
  }

  fun imagem5(): String {
    return imagem?.split(" +".toRegex())?.toList()?.getOrNull(4) ?: ""
  }

  fun saldoLoja4(): Double {
    val saldo = if (variacao == COM_VARIACAO.descricao) saldoLoja4(codigo, "")
    else saldoLoja4(codigo, grade)
    return saldo?.saldo ?: 0.00
  }

  fun price(): Double {
    val price = price(codigo)
    return price?.price ?: 0.00
  }

  val prdRef: String
    get() {
      val price = price(codigo)
      return price?.prdRef ?: ""
    }

  fun grupo() = if (variacao == VARIACAO.descricao) ""
  else categoriaBean?.grupo ?: ""

  fun departamento() = if (variacao == VARIACAO.descricao) ""
  else categoriaBean?.departamento ?: ""

  fun secao() = if (variacao == VARIACAO.descricao) ""
  else categoriaBean?.secao ?: ""

  fun tipoVariacao() = variacao

  fun ean(): String {
    val price = price(codigo)

    return if (barcode?.trim().isNullOrBlank()) price?.gtin ?: "" else barcode ?: ""
  }

  fun palavrasChave() = if (variacao == VARIACAO.descricao) ""
  else listOf(grupo(), departamento(), secao(), marcaDesc).filter { it.trim() != "" }.joinToString(",")

  fun nomeProduto() = if (variacao == VARIACAO.descricao) "" else "$descricaoCompleta - $marcaDesc"

  fun descricaoDetalhada() = if (variacao == VARIACAO.descricao) "" else especificacoes ?: ""
  fun descricao() = if (variacao == VARIACAO.descricao) ""
  else "$descricaoCompleta $marcaDesc".substring(0, Math.min("$descricaoCompleta $marcaDesc".length, 100))

  fun skuPai() = when (variacao) {
    VARIACAO.descricao -> codigo
    else -> ""
  }

  fun sku() = when (variacao) {
    COM_VARIACAO.descricao -> codigo
    SIMPLES.descricao -> codigo
    VARIACAO.descricao -> barcode ?: ""
    else -> ""
  }

  fun slugProduto() = if (variacao == VARIACAO.descricao) ""
  else descricaoCompleta?.normalize(" " + "") ?: ""

  fun marca() = if (variacao == VARIACAO.descricao) "" else marcaDesc
  fun tituloMarca() = if (variacao == VARIACAO.descricao) "" else textLink ?: ""
  fun descricaoPagina() = if (variacao == VARIACAO.descricao) "" else descricaoCompleta ?: ""
  fun gradeCor() = when (variacao) {
    VARIACAO.descricao -> when (gradeAlternativa) {
      "" -> gradeCompleta ?: ""
      else -> gradeAlternativa.split(":").getOrNull(1) ?: ""
    }
    else -> ""
  }

  fun cor() = if (variacao == VARIACAO.descricao) when (gradeAlternativa) {
    "" -> "Cor"
    else -> gradeAlternativa.split(":").getOrNull(0) ?: ""
  }
  else ""

  fun chave() = ChaveProduto(codigo, grade)
  fun copy(variacaoNova: EVariacao) = Produto(
    seq,
    codigo,
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
    marcaNome,
    categoria,
    categoriaNome,
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
    ncm,
    cor,
    variacaoNova.descricao,
    corStr,
    dataHoraMudanca,
    userno,
    modificado,
    gradeAlternativa
  )

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Produto

    if (codigo != other.codigo) return false
    if (grade != other.grade) return false
    if (variacao != other.variacao) return false

    return true
  }

  override fun hashCode(): Int {
    var result = codigo.hashCode()
    result = 31 * result + grade.hashCode()
    result = 31 * result + variacao.hashCode()
    return result
  }

  companion object {
    private val listSaldos = saci.saldoLoja4().groupBy { Pair(it.codigo, it.grade) }

    private val listPreco = saci.price().groupBy { it.codigo }

    fun saldoLoja4(
      codigo: String,
      grade: String,
    ) = listSaldos[Pair(codigo, grade)].orEmpty().firstOrNull()

    fun price(codigo: String) = listPreco[codigo].orEmpty().firstOrNull()

    private val userSaci: UserSaci
      get() = AppConfig.userSaci as UserSaci

    fun save(bean: Produto) {
      bean.textLink = bean.descricaoCompleta?.normalize("-")
      local.salvaProduto(bean)
    }

    private var datahoraUpdate = LocalDateTime.now().minusDays(1)

    fun updateProduto() {
      val agora = LocalDateTime.now()
      if (agora > datahoraUpdate.plusHours(5)) {
        thread {
          saci.updateProdutos()
          datahoraUpdate = agora
        }
      }
    }

    init {
      updateProduto()
    }
  }
}

data class ChaveProduto(
  val codigo: String,
  val grade: String,
)

enum class EEditor(val value: Int, val canEdit: Boolean) {
  BASE(0, false),
  EDITAR(1, true),
  EDITADO(2, true),
  IMPORTADO(3, false),
  ENVIAR(4, false),
  ENVIADO(5, true),
  CORRECAO(6, false)
}

enum class EVariacao(val descricao: String) {
  SIMPLES("simples"),
  VARIACAO("variacao"),
  COM_VARIACAO("com-variacao")
}

fun List<Produto>.explodeGrade(): List<Produto> {
  val comVariacao = this.distinctBy { it.codigo }.map { it.copy(COM_VARIACAO) }
  val variacao = this.map { it.copy(VARIACAO) }
  return comVariacao + variacao
}

data class FiltroProduto(
  val codigo: Int = 0,
  val listaProduto: String = "",
  val descricaoI: String = "",
  val descricaoF: String = "",
  val fornecedor: Fornecedor? = null,
  val type: TypePrd? = null,
  val cl: Cl? = null,
  val categoria: Categoria? = null,
  val editado: EEditor,
)
