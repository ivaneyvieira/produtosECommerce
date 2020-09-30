package br.com.astrosoft.produtosECommerce.model.planilha

import br.com.astrosoft.produtosECommerce.model.beans.Categoria
import br.com.astrosoft.produtosECommerce.model.beans.Produto
import com.github.nwillc.poink.workbook
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment
import java.io.ByteArrayOutputStream

class PlanilhaCategoria {
  private val campos: List<Campo<*, Categoria>> = listOf(
    CampoInt("Número") {categoriaNo},
    CampoString("Grupo") {grupo},
    CampoString("Departamento") {departamento},
    CampoString("Seção") {secao}
                                                        )

    fun grava(listaBean: List<Categoria>): ByteArray {
      val wb = workbook {
        val headerStyle = cellStyle("Header") {
          fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
          fillPattern = FillPatternType.SOLID_FOREGROUND
          this.verticalAlignment = VerticalAlignment.TOP
        }
        val rowStyle = cellStyle("Row") {
          this.verticalAlignment = VerticalAlignment.TOP
        }
        val sheet = sheet("Categorias") {
          val headers = campos.map {it.header}
          row(headers, headerStyle)
          listaBean.forEach {bean ->
              val valores = campos.map {it.produceVakue(bean)}
              row(valores, rowStyle)
            }
        }

        campos.forEachIndexed {index, campo ->
          sheet.autoSizeColumn(index)
        }
      }
      val outBytes = ByteArrayOutputStream()
      wb.write(outBytes)
      return outBytes.toByteArray()
    }
}