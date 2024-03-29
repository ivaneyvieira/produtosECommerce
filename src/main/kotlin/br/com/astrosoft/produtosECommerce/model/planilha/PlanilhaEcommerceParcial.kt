package br.com.astrosoft.produtosECommerce.model.planilha

import br.com.astrosoft.produtosECommerce.model.beans.Produto
import com.github.nwillc.poink.workbook
import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment

class PlanilhaEcommerceParcial {
  private val campos: List<Campo<*, Produto>> =
    listOf(
      CampoString("codigo") { codigo.replace("^0+".toRegex(), "") },
      CampoString("codigo de barras") { barcode ?: "" },
      CampoString("descricao") { descricao },
      CampoString("grade") { grade },
      CampoString("referencia do fornecedor") { prdRef },
      CampoInt("codigo do fornecedor") { vendno },
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
      val stSemGrade = sheet("Produtos") {
        val headers = campos.map { it.header }
        row(headers, headerStyle)
        listaProdutos.sortedBy { it.codigo + it.grade }.forEach { produto ->
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

