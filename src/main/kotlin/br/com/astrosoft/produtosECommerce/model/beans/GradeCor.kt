package br.com.astrosoft.produtosECommerce.model.beans

import br.com.astrosoft.produtosECommerce.model.local
import br.com.astrosoft.produtosECommerce.model.saci
import java.time.LocalDateTime

@Suppress("UNUSED_PARAMETER")
class GradeCor(var descricao: String = "",
               var codigoCor: String = "",
               var userno: Int? = 0,
               var dataHoraMudanca: LocalDateTime? = LocalDateTime.now(),
               var enviado: Int? = 0) {
  var descricaoOriginal: String = ""
  var enviadoBool: Boolean?
    get() = enviado != 0
    set(value) {
      this.enviado = if (value == true) 1 else 0
    }
  var userName: String?
    get() = if (userno == null) null
    else saci.findAllUser().firstOrNull {
      it.no == userno
    }?.name
    set(value) {}

  companion object {
    private val listGradeCor = mutableListOf<GradeCor>().apply {
      addAll(local.findAllCor().map {
        it.descricaoOriginal = it.descricao
        it
      })
    }

    fun updateList() {
      listGradeCor.clear()
      listGradeCor.addAll(local.findAllCor().map {
        it.descricaoOriginal = it.descricao
        it
      })
    }

    fun findAll(): List<GradeCor> {
      return listGradeCor
    }

    fun add(cor: GradeCor) {
      local.addCor(cor)
    }

    fun update(cor: GradeCor) {
      local.updateCor(cor)
    }

    fun delete(cor: GradeCor) {
      local.deleteCor(cor)
    }
  }
}