package br.com.astrosoft.produtosECommerce.model.planilha

import br.com.astrosoft.produtosECommerce.model.beans.Vtex
import com.github.nwillc.poink.workbook
import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment

class PlanilhaVtexPreco {
  private val campos: List<Campo<*, Vtex>> =
    listOf(
      CampoString("Sku ID") { skuId.toString() },
      CampoString("Id Prod") { idProd.toString() },
      CampoString("Nome SKU") { nomeSku },
      CampoString("Referencia SKU") { referenciaSKU },
      CampoNumber("Preço Compor") { precoCompor ?: 0.00 },
      CampoNumber("Preço Ref") { refprice ?: 0.00 },
      CampoNumber("Preço Promo") { promoprice ?: 0.00 },
      CampoNumber("Preço Vtex") { preco },
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
          val valores = campos.map { it.produceVakue(produto) }
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

