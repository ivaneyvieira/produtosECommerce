package br.com.astrosoft.produtosECommerce.model.planilha

import br.com.astrosoft.framework.util.format
import br.com.astrosoft.produtosECommerce.model.beans.Produto
import br.com.astrosoft.produtosECommerce.model.beans.ProdutoPromocao
import com.github.nwillc.poink.workbook
import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment

class PlanilhaPromocao {
  private val campos: List<Campo<*, ProdutoPromocao>> = listOf(
    CampoString("codigo") { codigo.replace("^0+".toRegex(), "") },
    CampoString("descrição") { descricao },
    CampoString("Nº") { promono.toString() },
    CampoString("Validade") { validade.format() },
    CampoNumber("Preço Ref") { precoRef },
    CampoNumber("Preço %") { perc },
    CampoNumber("Preço Promo") { precoPromo },
    CampoString("Fornecedor") { vendno.toString() },
    CampoString("Abrev") { abrev },
    CampoString("Tipo") { tipo.toString() },
    CampoString("CL") { centLucro.toString() },
    CampoInt("Saldo") { saldo }
                                                      )

  fun grava(listaProdutos: List<ProdutoPromocao>): ByteArray {
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
        listaProdutos.sortedBy { it.codigo }.forEach { produto ->
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

