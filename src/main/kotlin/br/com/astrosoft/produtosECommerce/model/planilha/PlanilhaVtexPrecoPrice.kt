package br.com.astrosoft.produtosECommerce.model.planilha

import br.com.astrosoft.framework.util.format
import br.com.astrosoft.produtosECommerce.model.beans.Vtex
import com.github.nwillc.poink.workbook
import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment

class PlanilhaVtexPrecoPrice {
  private val campos: List<Campo<*, Vtex>> =
    listOf(
      CampoString("Sku ID") { skuId.toString() },
      CampoString("Id Prod") { idProd.toString() },
      CampoString("Nome SKU") { nomeSku },
      CampoString("Referencia SKU") { referenciaSKU },
      CampoString("Cód Saci") { codigo },
      CampoString("Validade") { validadeVtex.format() },
      CampoNumber("P. Price") { promoVtex ?: 0.00 },
      CampoNumber("P. Base") { preco },
          )

  fun grava(listaProdutos: List<Vtex>): ByteArray {
    val wb = workbook {
      val headerStyle = cellStyle("Header") {
        fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
        fillPattern = FillPatternType.SOLID_FOREGROUND
        this.verticalAlignment = VerticalAlignment.TOP
      }
      val rowStyle = cellStyle("Row") {
        this.verticalAlignment = VerticalAlignment.TOP
      }
      val stSemGrade = sheet("Preços VTEX") {
        val headers = campos.map { it.header }
        row(headers, headerStyle)
        listaProdutos.forEach { produto ->
          val valores = campos.map { it.produceValue(produto) ?: ""}
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

