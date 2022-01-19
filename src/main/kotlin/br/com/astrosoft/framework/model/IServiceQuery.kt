package br.com.astrosoft.framework.model

interface IServiceQuery<T : Any, F : Any> {
  fun count(filter: F): Int
  fun fetch(
    filter: F, offset: Int, limit: Int, sortOrders: List<SortOrder>
  ): List<T>

  fun fetch(filter: F) = fetch(
    filter, offset = 0, limit = Int.MAX_VALUE, sortOrders = emptyList()
  )
}

data class SortOrder(val field: String, val desc: Boolean) {
  fun sql() = if (desc) "$field DESC" else "$field ASC"
}