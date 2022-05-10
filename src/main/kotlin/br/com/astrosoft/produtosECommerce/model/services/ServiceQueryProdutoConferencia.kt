package br.com.astrosoft.produtosECommerce.model.services

import br.com.astrosoft.framework.model.IServiceQuery
import br.com.astrosoft.framework.model.SortOrder
import br.com.astrosoft.produtosECommerce.model.beans.FiltroProdutoConferencia
import br.com.astrosoft.produtosECommerce.model.beans.ProdutoBarcode
import br.com.astrosoft.produtosECommerce.model.beans.ProdutoCodigo
import br.com.astrosoft.produtosECommerce.model.beans.ProdutoConferencia
import br.com.astrosoft.produtosECommerce.model.local
import br.com.astrosoft.produtosECommerce.model.saci
import br.com.astrosoft.produtosECommerce.model.xlsx.PrecosEcomerce

fun ProdutoBarcode?.toProdutoCodigo(): ProdutoCodigo? {
  val produto = this ?: return null
  return ProdutoCodigo(codigo = produto.codigo.toString(), grade = produto.grade, barcode = produto.barcode)
}

class ServiceQueryProdutoConferencia : IServiceQuery<ProdutoConferencia, FiltroProdutoConferencia> {
  override fun count(filter: FiltroProdutoConferencia): Int {
    return local.countProdutoConferencia(filter)
  }

  override fun fetch(filter: FiltroProdutoConferencia,
                     offset: Int,
                     limit: Int,
                     sortOrders: List<SortOrder>): List<ProdutoConferencia> {
    return local.fetchProdutoConferencia(filter, offset, limit, sortOrders)
  }

  fun readExcel(fileName: String) {
    val precos = PrecosEcomerce.readExcel(fileName)
    local.apagaPrecos()
    val precosSaci = saci.precoSaci().groupBy { it.codigo }.mapValues { it.value.firstOrNull() }
    val produtosLocal = local.produtosBarcode()
    val produtoBarcodeSaci = saci.produtoBarcodeSaci().groupBy { it.barcode }.mapValues { it.value.firstOrNull() }
    val produtosBarcode = produtosLocal.groupBy { it.barcode }.mapValues { it.value.firstOrNull()?.toProdutoCodigo() }
    val produtosCodigo = produtosLocal.groupBy { it.codigo }.mapValues { it.value.firstOrNull()?.toProdutoCodigo() }
    precos.forEach {
      val produto =
              produtoBarcodeSaci[it.codigo] ?: produtosBarcode[it.codigo] ?: produtosCodigo[it.codigo.toIntOrNull()]
      val prdSaci = precosSaci[produto?.codigo?.toIntOrNull()] ?: precosSaci[it.codigo.toIntOrNull()]

      local.addPrecoConferencia(
        refid = it.codigo,
        listPrice = it.preco,
        prdno = prdSaci?.codigo?.toString() ?: "",
        grade = produto?.grade ?: "",
        descricaoSite = it.descricao,
        descricaoSaci = prdSaci?.descricao ?: "",
        precoPromo = prdSaci?.preco,
        precoRef = prdSaci?.refprice,
                               )
    }
  }
}