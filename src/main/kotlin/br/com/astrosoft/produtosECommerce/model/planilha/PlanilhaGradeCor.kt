package br.com.astrosoft.produtosECommerce.model.planilha

import br.com.astrosoft.framework.util.format
import br.com.astrosoft.produtosECommerce.model.beans.GradeCor
import com.github.nwillc.poink.workbook
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment
import java.io.ByteArrayOutputStream

class PlanilhaGradeCor {
  private val campos: List<Campo<*, GradeCor>> =
    listOf(
      CampoString("Descrição") { descricao },
      CampoString("Código") { codigoCor.toUpperCase() },
      CampoString("Modificação") { dataHoraMudanca.format() },
          )

  fun grava(listaBean: List<GradeCor>): ByteArray {
    val wb = workbook {
      val headerStyle = cellStyle("Header") {
        fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
        fillPattern = FillPatternType.SOLID_FOREGROUND
        this.verticalAlignment = VerticalAlignment.TOP
      }
      val rowStyle = cellStyle("Row") {
        this.verticalAlignment = VerticalAlignment.TOP
      }
      val sheet = sheet("Grade Cor") {
        val headers = campos.map { it.header }
        row(headers, headerStyle)
        listaBean.forEach { bean ->
          val valores = campos.map { it.produceVakue(bean) }
          row(valores, rowStyle)
        }
      }

      campos.forEachIndexed { index, _ ->
        sheet.autoSizeColumn(index)
      }
    }
    val outBytes = ByteArrayOutputStream()
    wb.write(outBytes)
    return outBytes.toByteArray()
  }
}