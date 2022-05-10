package br.com.astrosoft.produtosECommerce.model.xlsx

import com.github.nwillc.poink.workbook
import java.text.Normalizer
import java.util.regex.Pattern

data class PrecosEcomerce(val codigo: String, val preco: Double, val descricao: String) {
  companion object {
    private val mapKey = mutableMapOf<String, String>()
    private fun deAccent(str: String): String {
      val nfdNormalizedString: String = Normalizer.normalize(str, Normalizer.Form.NFD)
      val pattern: Pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
      return pattern.matcher(nfdNormalizedString).replaceAll("")
    }

    private fun Map<String, String>.getString(colname: String): String? {
      val key = if (mapKey.keys.contains(colname)) mapKey[colname]
      else {
        val result = this.keys.firstOrNull { key ->
          deAccent(key).toUpperCase().contains(colname, ignoreCase = true)
        }
        mapKey[colname] = result ?: ""
        result
      }
      key ?: return null
      return this[key]?.trim()
    }

    fun readExcel(filename: String): List<PrecosEcomerce> {
      val dataFrame = readXlsx(filename, 0)
      val lista = dataFrame.mapNotNull {
        val refID = it.getString("Ref ID") ?: return@mapNotNull null
        val listPrice = it.getString("List Price")?.toDoubleOrNull() ?: return@mapNotNull null
        val descricao = it.getString("Product Name") ?: return@mapNotNull null
        PrecosEcomerce(
          codigo = refID,
          preco = listPrice,
          descricao = descricao,
        )
      }
      return lista
    }
  }
}


fun readXlsx(filename: String, rowIndexCol : Int): List<Map<String, String>> {
  val listMap = mutableListOf<Map<String, String>>()
  workbook(filename) {
    sheet(0) {
      val colunas = mutableListOf<String>()
      var rowIndex = 0
      this.rowIterator().forEach { row ->
        if (rowIndex == rowIndexCol) {
          row.iterator().forEach { cell ->
            colunas.add(cell.stringCellValue)
          }
        } else if (rowIndex > rowIndexCol) {
          val map = mutableMapOf<String, String>()
          var colIndex = 0
          colunas.forEachIndexed { index, col ->
            val value = row.getCell(index)?.toString() ?: ""
            map[col] = value
            colIndex++
          }
          listMap.add(map.toMap())
        }
        rowIndex++
      }
    }
  }
  return listMap
}


