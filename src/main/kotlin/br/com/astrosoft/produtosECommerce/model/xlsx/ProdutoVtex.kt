package br.com.astrosoft.produtosECommerce.model.xlsx

import java.text.Normalizer
import java.util.regex.Pattern

data class ProdutoVtex(
  val skuId: Int,
  val idProd: Int,
  val nomeSku: String,
  val ativarSku: String,
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

    private fun Map<String, String>.getString(colname: String, pos: Int = 0): String? {
      val key = if (mapKey.keys.contains(colname)) mapKey[colname]
      else {
        val resultList = this.keys.mapNotNull { key ->
          val localizado = deAccent(key).toUpperCase().replace(" +".toRegex(), "").contains(colname, ignoreCase = true)
          if (localizado) key
          else null
        }
        val result = resultList.getOrNull(pos)
        if (result != null) {
          mapKey[colname] = result
        }
        result
      }
      key ?: throw EColunaNaoEncontrada(colname)
      return this[key]?.trim()
    }

    private fun Map<String, String>.getInt(colname: String, pos: Int = 0): Int? {
      val strInt = getString(colname, pos)
      return strInt?.toDoubleOrNull()?.toInt()
    }

    fun readExcel(filename: String): List<ProdutoVtex> {
      val dataFrame = readXlsx(filename, 0)
      val lista = dataFrame.map {
        val skuId = it.getInt("SkuId") ?: 0
        val idProd = it.getInt("IdPro") ?: 0
        val nomeSku = it.getString("NomeSku") ?: ""
        val ativarSku = it.getString("AtivarSku") ?: ""
        val referenciaSKU = it.getString("ReferenciaSKU") ?: ""
        val idDep = it.getInt("IdDep") ?: 0
        val nomeDepartamento = it.getString("NomeDep") ?: ""
        val idCat = it.getInt("IdCat") ?: 0
        val nomeCategoria = it.getString("NomeCat") ?: ""
        val idMarca = it.getInt("IdMar") ?: 0
        val nomeMarca = it.getString("Marca", pos = 1) ?: ""
        ProdutoVtex(
          skuId = skuId,
          idProd = idProd,
          nomeSku = nomeSku,
          ativarSku = ativarSku,
          referenciaSKU = referenciaSKU,
          idDep = idDep,
          nomeDepartamento = nomeDepartamento,
          idCat = idCat,
          nomeCategoria = nomeCategoria,
          idMarca = idMarca,
          nomeMarca = nomeMarca,
                   )
      }
      return lista
    }
  }
}