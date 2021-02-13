package br.com.astrosoft.produtosECommerce.model.planilha

import br.com.astrosoft.produtosECommerce.model.beans.EVariacao.SIMPLES
import br.com.astrosoft.produtosECommerce.model.beans.Produto
import br.com.astrosoft.produtosECommerce.model.beans.explodeGrade
import com.github.nwillc.poink.workbook
import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment

class PlanilhaEcommerceNova {
  private val campos: List<Campo<*, Produto>> = listOf( // CampoString("codigo") {codigo},
    // CampoString("grade") {grade},
    CampoString("nome_produto") {nomeProduto()},
    CampoString("tipo") {tipoVariacao()},
    CampoString("sku_pai") {skuPai().replace("^0+".toRegex(), "")},
    CampoString("sku") {sku().replace("^0+".toRegex(), "")},
    CampoString("slug_produto") {slugProduto()},
    CampoString("descricao_detalhada") {descricaoDetalhada()},
    CampoString("descricao") {descricao()},
    CampoNumber("estoque") {saldoLoja4()},
    CampoString("ean") {ean()},
    CampoString("marca") {marca()},
    CampoNumber("preco_de") {price()},
    CampoNumber("preco_por") {price()},
    CampoNumber("peso_kg") {peso},
    CampoNumber("comprimento_metros") {comprimento / 100},
    CampoNumber("largura_metros") {largura / 100},
    CampoNumber("altura_metros") {altura / 100},
    CampoString("titulo_meta") {tituloMarca()},
    CampoString("palavras_chave") {palavrasChave()},
    CampoString("descricao_pagina") {descricaoPagina()},
    CampoString("categoria-nivel-1") {grupo()},
    CampoString("categoria-nivel-2") {departamento()},
    CampoString("categoria-nivel-3") {secao()},
    CampoString("categoria-nivel-4"),
    CampoString("url_imagem_1") {imagem1()},
    CampoString("url_imagem_2") {imagem2()},
    CampoString("url_imagem_3") {imagem3()},
    CampoString("url_imagem_4") {imagem4()},
    CampoString("url_imagem_5") {imagem5()},
    CampoString("url_imagem_6"),
    CampoString("url_imagem_7"),
    CampoString("url_imagem_8"),
    CampoString("url_imagem_9"),
    CampoString("url_imagem_10"),
    CampoString("nome_variacao_1") {cor()},
    CampoString("variacao_1") {gradeCor()},
    CampoString("nome_variacao2"),
    CampoString("variacao_2"))
  
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
        val headers = campos.map {it.header}
        row(headers, headerStyle)
        listaProdutos.filter {it.grade == ""}.map {
          it.copy(SIMPLES)
        }.sortedBy {it.codigo + it.grade}.forEach {produto ->
          val valores = campos.map {it.produceVakue(produto)}
          row(valores, rowStyle)
        }
      }
      val stComGrade = sheet("Produtos Com Grade") {
        val headers = campos.map {it.header}
        row(headers, headerStyle)
        listaProdutos.filter {it.grade != ""}.explodeGrade()
          .sortedWith(compareBy({it.codigo}, {it.variacao}, {it.grade}))
          .forEach {produto ->
            val valores = campos.map {it.produceVakue(produto)}
            row(valores, rowStyle)
          }
      }
      campos.forEachIndexed {index, _ ->
        stSemGrade.autoSizeColumn(index)
        stComGrade.autoSizeColumn(index)
      }
    }
    val outBytes = ByteArrayOutputStream()
    wb.write(outBytes)
    return outBytes.toByteArray()
  }
}

