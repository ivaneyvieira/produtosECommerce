package br.com.astrosoft.produtosECommerce.model.services

import br.com.astrosoft.framework.model.IServiceQuery
import br.com.astrosoft.framework.model.SortOrder
import br.com.astrosoft.produtosECommerce.model.beans.*
import br.com.astrosoft.produtosECommerce.model.local
import br.com.astrosoft.produtosECommerce.model.saci
import br.com.astrosoft.produtosECommerce.model.xlsx.PrecosBase
import br.com.astrosoft.produtosECommerce.model.xlsx.PrecosList
import br.com.astrosoft.produtosECommerce.model.xlsx.PromoVtex

class ServiceQueryVtexDif : IServiceQuery<Vtex, FiltroVtexDif> {
  private val precosSaci: MutableMap<Int, PrecoSaci?> = mutableMapOf()
  private val produtosLocal: MutableList<ProdutoBarcode> = mutableListOf()
  private var produtoBarcodeSaci: MutableMap<String, ProdutoCodigo?> = mutableMapOf()
  private val produtosBarcode: MutableMap<String, ProdutoCodigo?> = mutableMapOf()
  private var produtosCodigo: MutableMap<Int, ProdutoCodigo?> = mutableMapOf()

  private fun findPrice(codigo: String): PrecoSaci? {
    val produto = produtoBarcodeSaci[codigo] ?: produtosBarcode[codigo] ?: produtosCodigo[codigo.toIntOrNull()]
    val prdSaci = precosSaci[produto?.codigo?.toIntOrNull()] ?: precosSaci[codigo.toIntOrNull()]
    if(produto != null){
      prdSaci?.grade = produto.grade
    }
    return prdSaci
  }

  override fun count(filter: FiltroVtexDif): Int {
    return local.countVtexDif(filter)
  }

  override fun fetch(filter: FiltroVtexDif, offset: Int, limit: Int, sortOrders: List<SortOrder>): List<Vtex> {
    return local.fetchVtexDif(filter, offset, limit, sortOrders)
  }

  fun readExcelPrecoBase(fileName: String) {
    val precos = PrecosBase.readExcel(fileName)
    local.apagaPrecoReferenciaBase()
    precos.forEach { preco ->
      local.updatePrecoVtex(preco)
    }
  }

  fun readExcelPrecoList(fileName: String) {
    val precos = PrecosList.readExcel(fileName)
    local.apagaPrecoReferenciaList()
    precos.forEach { preco ->
      local.updatePrecoVtex(preco)
    }
  }

  fun readExcelPromo(fileName: String) {
    val precos = PromoVtex.readExcel(fileName)
    local.apagaPrecoPromocionalVtex()
    precos.forEach { preco ->
      local.updatePromoVtex(preco)
    }
  }

  fun updatePricesSaci() {
    precosSaci.clear()
    precosSaci.putAll(saci.precoSaci().groupBy { it.codigo }.mapValues { it.value.firstOrNull() })

    produtosLocal.clear()
    produtosLocal.addAll(local.produtosBarcode())

    produtoBarcodeSaci.clear()
    produtoBarcodeSaci.putAll(saci.produtoBarcodeSaci().groupBy { it.barcode }.mapValues { it.value.firstOrNull() })

    produtosBarcode.clear()
    produtosBarcode.putAll(produtosLocal.groupBy { it.barcode }.mapValues { it.value.firstOrNull()?.toProdutoCodigo() })

    produtosCodigo.clear()
    produtosCodigo.putAll(produtosLocal.groupBy { it.codigo }.mapValues { it.value.firstOrNull()?.toProdutoCodigo() })
  }

  fun promocaaARemover(list: List<Vtex>): List<Vtex> {
    updatePricesSaci()
    return list.filter {
      it.validade == null && ((it.promono ?: 0) > 0)
    }
  }

  fun promocaaAAdicionar(list: List<Vtex>): List<Vtex> {
    updatePricesSaci()
    return list.filter {
      it.validade != null && ((it.promono ?: 0) == 0)
    }
  }

  fun updateSaci(list: List<Vtex>) {
    updatePricesSaci()
    list.forEach {
      val priceSaci = findPrice(it.referenciaSKU)
      it.update(priceSaci)
    }
  }

  fun zeraCompor(itens: List<Vtex>) {
    itens.forEach {vtex ->
      saci.zeraCompor(vtex.codigo)
    }
  }
}