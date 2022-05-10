package br.com.astrosoft.produtosECommerce.model.xlsx

import java.text.Normalizer
import java.util.regex.Pattern

data class PrecosVtex(val skuId: Int, val preco: Double) {
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

    fun readExcel(filename: String): List<PrecosVtex> {
      val dataFrame = readXlsx(filename, 0)
      val lista = dataFrame.mapNotNull {
        val skuId = it.getString("SKU ID")?.toIntOrNull() ?: return@mapNotNull null
        val listPrice = it.getString("Price")?.toDoubleOrNull() ?: return@mapNotNull null
        PrecosVtex(
          skuId = skuId,
          preco = listPrice
                  )
      }
      return lista
    }
  }
}