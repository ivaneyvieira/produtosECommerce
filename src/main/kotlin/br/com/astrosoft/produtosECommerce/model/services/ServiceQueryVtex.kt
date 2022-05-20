package br.com.astrosoft.produtosECommerce.model.services

import br.com.astrosoft.framework.model.IServiceQuery
import br.com.astrosoft.framework.model.SortOrder
import br.com.astrosoft.produtosECommerce.model.beans.*
import br.com.astrosoft.produtosECommerce.model.local
import br.com.astrosoft.produtosECommerce.model.saci
import br.com.astrosoft.produtosECommerce.model.xlsx.PrecosVtex
import br.com.astrosoft.produtosECommerce.model.xlsx.ProdutoVtex
import br.com.astrosoft.produtosECommerce.model.xlsx.PromoVtex

class ServiceQueryVtex : IServiceQuery<Vtex, FiltroVtex> {
  private val precosSaci: MutableMap<Int, PrecoSaci?> = mutableMapOf()
  private val produtosLocal: MutableList<ProdutoBarcode> = mutableListOf()
  private var produtoBarcodeSaci: MutableMap<String, ProdutoCodigo?> = mutableMapOf()
  private val produtosBarcode: MutableMap<String, ProdutoCodigo?> = mutableMapOf()
  private var produtosCodigo: MutableMap<Int, ProdutoCodigo?> = mutableMapOf()

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

  private fun findPrice(codigo: String): PrecoSaci? {
    val produto = produtoBarcodeSaci[codigo] ?: produtosBarcode[codigo] ?: produtosCodigo[codigo.toIntOrNull()]
    val prdSaci = precosSaci[produto?.codigo?.toIntOrNull()] ?: precosSaci[codigo.toIntOrNull()]
    return prdSaci
  }

  override fun count(filter: FiltroVtex): Int {
    return local.countVtex(filter)
  }

  override fun fetch(filter: FiltroVtex, offset: Int, limit: Int, sortOrders: List<SortOrder>): List<Vtex> {
    val lista = local.fetchVtex(filter, offset, limit, sortOrders)
    lista.forEachIndexed { index, vtex ->
      vtex.seq = offset + index + 1
    }
    return lista
  }

  fun readExcelPreco(fileName: String) {
    val precos = PrecosVtex.readExcel(fileName)
    local.apagaPrecoReferenciaVtex()
    precos.forEach { preco ->
      local.updatePrecoVtex(preco)
    }
  }

  fun readExcelProduto(fileName: String) {
    val produtos = ProdutoVtex.readExcel(fileName)
    produtos.forEach { produto ->
      local.replaceProdutoVtex(produto)
    }
  }

  fun readExcelPromo(fileName: String) {
    val precos = PromoVtex.readExcel(fileName)
    local.apagaPrecoPromocionalVtex()
    precos.forEach { preco ->
      local.updatePromoVtex(preco)
    }
  }

  fun updateSaci(filter: FiltroVtex) {
    updatePricesSaci()
    val list = fetch(filter)
    list.forEach {
      it.priceSaci = findPrice(it.referenciaSKU)
      it.update()
    }
  }

  fun updateSaci(list: List<Vtex>) {
    updatePricesSaci()
    list.forEach {
      it.priceSaci = findPrice(it.referenciaSKU)
      it.update()
    }
  }

  fun promocaaARemover(filter: FiltroVtex): List<Vtex> {
    updatePricesSaci()
    val list = fetch(filter)
    return list.filter {
      it.priceSaci = findPrice(it.referenciaSKU)
      it.validade() == null && (it.promono() > 0)
    }
  }

  fun promocaaARemover(list: List<Vtex>): List<Vtex> {
    updatePricesSaci()
    return list.filter {
      it.priceSaci = findPrice(it.referenciaSKU)
      it.validade() == null && (it.promono() > 0)
    }
  }

  fun promocaaAAdicionar(list: List<Vtex>): List<Vtex> {
    updatePricesSaci()
    return list.filter {
      it.priceSaci = findPrice(it.referenciaSKU)
      it.validade() != null && (it.promono() == 0)
    }
  }
}