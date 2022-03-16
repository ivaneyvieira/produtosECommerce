package br.com.astrosoft.produtosECommerce.model.planilha

import br.com.astrosoft.produtosECommerce.model.beans.EVariacao.SIMPLES
import br.com.astrosoft.produtosECommerce.model.beans.Produto
import br.com.astrosoft.produtosECommerce.model.beans.explodeGrade
import com.github.nwillc.poink.workbook
import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment

class PlanilhaEcommerceNovaEAC {
  private val campos: List<Campo<*, Produto>> = listOf( // CampoString("codigo") {codigo},
    CampoString("código produto") { codigo },
    CampoString("código de barras") { barcode ?: "" },
    CampoString("grade") { grade },
    CampoString("grade do aplicativo") { gradeCor() },
    CampoString("descricao completa") { nomeProduto() },
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
      val produtos = sheet("Produtos") {
        val headers = campos.map { it.header }
        row(headers, headerStyle)
        listaProdutos.sortedBy { it.codigo + it.grade }.forEach { produto ->
          val valores = campos.map { it.produceVakue(produto) }
          row(valores, rowStyle)
        }
      }
      campos.forEachIndexed { index, _ ->
        produtos.autoSizeColumn(index)
      }
    }
    val outBytes = ByteArrayOutputStream()
    wb.write(outBytes)
    return outBytes.toByteArray()
  }
}

