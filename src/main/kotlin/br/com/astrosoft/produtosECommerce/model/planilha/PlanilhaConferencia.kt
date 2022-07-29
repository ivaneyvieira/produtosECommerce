package br.com.astrosoft.produtosECommerce.model.planilha

import br.com.astrosoft.produtosECommerce.model.beans.ProdutoConferencia
import com.github.nwillc.poink.workbook
import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment

class PlanilhaConferencia {
  private val campos: List<Campo<*, ProdutoConferencia>> =
    listOf(
      CampoString("Código Site") { refid },
      CampoString("Código Saci") { prdno ?: "" },
      CampoString("Grade") { grade ?: "" },
      CampoString("Descricao Site") { descricaoSite ?: "" },
      CampoString("Descricao Saci") { descricaoSaci ?: "" },
      CampoNumber("Preço Site") { listPrice },
      CampoNumber("Preço Referência") { precoRef ?: 0.00 },
      CampoNumber("Preço Promocional") { precoPromo ?: 0.00 },
          )

  fun grava(listaProdutos: List<ProdutoConferencia>): ByteArray {
    val wb = workbook {
      val headerStyle = cellStyle("Header") {
        fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
        fillPattern = FillPatternType.SOLID_FOREGROUND
        this.verticalAlignment = VerticalAlignment.TOP
      }
      val rowStyle = cellStyle("Row") {
        this.verticalAlignment = VerticalAlignment.TOP
      }
      val stSemGrade = sheet("Produtos") {
        val headers = campos.map { it.header }
        row(headers, headerStyle)
        listaProdutos.sortedBy { it.prdno + it.grade }.forEach { produto ->
          val valores = campos.map { it.produceValue(produto) ?: "" }
          row(valores, rowStyle)
        }
      }

      campos.forEachIndexed { index, _ ->
        stSemGrade.autoSizeColumn(index)
      }
    }
    val outBytes = ByteArrayOutputStream()
    wb.write(outBytes)
    return outBytes.toByteArray()
  }
}

