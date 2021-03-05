package br.com.astrosoft.produtosECommerce.model.beans

data class Price(
  val storeno: Int,
  val prdno: String,
  val price: Double,
  val gtin: String,
  val prdRef: String
                )