package br.com.astrosoft.produtosEComerce.viewmodel

object ProcessaDescricaoProduto {
  fun findPrefixo(descricoes: List<String>): String {
    val listaDescricao = descricoes.map {descricao ->
      Descricao(descricao)
    }
    val resultFold = listaDescricao.reduce {descricao1, descricao2 ->
      val result = descricao2.prefixo(descricao1)
      result
    }
    return resultFold.toStr()
  }
  
  fun findSulfixo(descricao: String, descricoes: List<String>): String {
    val prefixo = findPrefixo(descricoes)
    return descricao.substring(prefixo.length)
      .trim()
  }
}

data class Descricao(val text: String) {
  val palavras get() = text.split(" +".toRegex())
  fun toStr() = palavras.joinToString(" ")
  fun startsWith(outra: Descricao) = this.toStr()
    .startsWith(outra.toStr())
  
  fun prefixo(outra: Descricao): Descricao {
    palavras.forEachIndexed {index, palavra ->
      val palavraOutra = outra.palavras.getOrNull(index) ?: ""
      if(palavra != palavraOutra) {
        return@prefixo if(index == 0)
          Descricao("")
        else
          Descricao(palavras.subList(0, index)
                      .joinToString(" "))
      }
    }
    return Descricao("")
  }
}
