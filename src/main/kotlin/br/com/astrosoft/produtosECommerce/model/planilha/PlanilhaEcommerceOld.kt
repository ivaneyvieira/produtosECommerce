package br.com.astrosoft.produtosECommerce.model.planilha

import br.com.astrosoft.produtosECommerce.model.beans.Produto
import com.github.nwillc.poink.workbook
import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment

class PlanilhaEcommerceOld {
  private val campos: List<Campo<*, Produto>> = listOf(
    CampoString("id"),
    CampoString("tipo") { "sem-variacao" },
    CampoString("sku-pai"),
    CampoString("sku") { codigo.trim() },
    CampoString("ativo") { "S" },
    CampoString("usado") { "N" },
    CampoString("ncm") { ncm },
    CampoString("gtin") { barcode ?: "" },
    CampoString("nome") { descricaoCompletaPlanilha() },
    CampoString("descricao-completa") { especificacoes ?: "" },
    CampoString("url-video-youtube"),
    CampoString("estoque-gerenciado") { "N" },
    CampoNumber("estoque-quantidade") { saldoLoja4() },
    CampoString("estoque-situacao-em-estoque") { "imediata" },
    CampoString("estoque-situacao-sem-estoque") { "indisponivel" },
    CampoString("preco-sob-consulta") { "N" },
    CampoString("preco-custo"),
    CampoNumber("preco-cheio") { price() },
    CampoNumber("preco-promocional") { price() },
    CampoString("marca") { marcaDesc },
    CampoNumber("peso-em-kg") { peso ?: 0.00 },
    CampoNumber("altura-em-cm") { altura ?: 0.00 },
    CampoNumber("largura-em-cm") { largura ?: 0.00 },
    CampoNumber("comprimento-em-cm") { comprimento ?: 0.00 },
    CampoString("categoria-nome-nivel-1") { grupo() },
    CampoString("categoria-nome-nivel-2") { departamento() },
    CampoString("categoria-nome-nivel-3") { secao() },
    CampoString("categoria-nome-nivel-4"),
    CampoString("categoria-nome-nivel-5"),
    CampoString("imagem-1") { imagem1() },
    CampoString("imagem-2") { imagem2() },
    CampoString("imagem-3") { imagem3() },
    CampoString("imagem-4") { imagem4() },
    CampoString("imagem-5") { imagem5() },
    CampoString("grade-genero"),
    CampoString("grade-tamanho-de-anelalianca"),
    CampoString("grade-tamanho-de-calca"),
    CampoString("grade-tamanho-de-camisacamiseta"),
    CampoString("grade-tamanho-de-capacete"),
    CampoString("grade-tamanho-de-tenis"),
    CampoString("grade-voltagem"),
    CampoString("grade-tamanho-juvenil-infantil"),
    CampoString("grade-produto-com-uma-cor"),
    CampoString("grade-produto-com-duas-cores"),
    CampoString(""),
    CampoString("url-antiga")
                                                      )

  fun grava(listaProdutos: List<Produto>): ByteArray {
    val wb = workbook {
      val headerStyle = cellStyle("Header") {
        fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
        fillPattern = FillPatternType.SOLID_FOREGROUND
        this.verticalAlignment = VerticalAlignment.TOP
      }
      val rowStyle = cellStyle("Row") {
        this.verticalAlignment = VerticalAlignment.TOP
      }
      val stSemGrade = sheet("Produtos Sem Grade") {
        val headers = campos.map { it.header }
        row(headers, headerStyle)
        listaProdutos.filter { it.grade == "" }.sortedBy { it.codigo + it.grade }.forEach { produto ->
          val valores = campos.map { it.produceVakue(produto) }
          row(valores, rowStyle)
        }
      }
      val stComGrade = sheet("Produtos Com Grade") {
        val headers = campos.map { it.header }
        row(headers, headerStyle)
        listaProdutos.filter { it.grade != "" }.sortedBy { it.codigo + it.grade }.forEach { produto ->
          val valores = campos.map { it.produceVakue(produto) }
          row(valores, rowStyle)
        }
      }
      campos.forEachIndexed { index, _ ->
        stSemGrade.autoSizeColumn(index)
        stComGrade.autoSizeColumn(index)
      }
    }
    val outBytes = ByteArrayOutputStream()
    wb.write(outBytes)
    return outBytes.toByteArray()
  }
}

open class Campo<T : Any, B>(val header: String, val produceVakue: (B) -> T)

class CampoString<B>(header: String, produceVakue: B.() -> String = { "" }) : Campo<String, B>(
  header, produceVakue
                                                                                              )

class CampoNumber<B>(header: String, produceVakue: B.() -> Double = { 0.00 }) : Campo<Double, B>(
  header, produceVakue
                                                                                                )

class CampoInt<B>(header: String, produceVakue: B.() -> Int = { 0 }) : Campo<Int, B>(
  header, produceVakue
                                                                                    )
