package br.com.astrosoft.produtosECommerce.model.xlsx

import br.com.astrosoft.framework.util.parserDate
import java.text.Normalizer
import java.util.regex.Pattern

data class ProdutoVtex(
  val skuId: Int,
  val idProd: Int,
  val nomeSku: String,
  val referenciaSKU: String,
  val idDep: Int,
  val nomeDepartamento: String,
  val idCat: Int,
  val nomeCategoria: String,
  val idMarca: Int,
  val nomeMarca: String,
                      ) {
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

    fun readExcel(filename: String): List<ProdutoVtex> {
      val dataFrame = readXlsx(filename, 0)
      val lista = dataFrame.mapNotNull {
        val skuId = it.getString("SKU ID")?.toIntOrNull() ?: return@mapNotNull null
        val precoPromo = it.getString("Price")?.toDoubleOrNull() ?: 0.00
        val validade = it.getString("Date To")?.split(" ")?.get(0)
        val data = validade.parserDate("M/d/yyyy")
        ProdutoVtex(
          skuId = skuId,
          idProd = it.getString("SKU ID")?.toIntOrNull() ?: 0,
          nomeSku = it.getString("SKU ID") ?: "",
          referenciaSKU = it.getString("SKU ID") ?: "",
          idDep = it.getString("SKU ID")?.toIntOrNull() ?: 0,
          nomeDepartamento = it.getString("SKU ID") ?: "",
          idCat = it.getString("SKU ID")?.toIntOrNull() ?: 0,
          nomeCategoria = it.getString("SKU ID") ?: "",
          idMarca = it.getString("SKU ID")?.toIntOrNull() ?: 0,
          nomeMarca = it.getString("SKU ID") ?: "",
                   )
      }
      return lista
    }
  }
}