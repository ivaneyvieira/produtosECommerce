package br.com.astrosoft.produtosEComerce.model.beans

import br.com.astrosoft.framework.spring.IUser
import br.com.astrosoft.produtosEComerce.model.saci
import kotlin.math.pow

class UserSaci: IUser {
  var no: Int = 0
  var name: String = ""
  override var login: String = ""
  override var senha: String = ""
  override fun roles(): List<String> {
    val roles = if(admin) listOf("ADMIN") else listOf("USER")

    return roles
  }
  
  var bitAcesso: Int = 0
  var storeno: Int = 0
  
  //Otiros campos
  var ativo
    get() = (bitAcesso and BIT_ATIVO) != 0 || admin
    set(value) {
      bitAcesso = if(value) bitAcesso or BIT_ATIVO
      else bitAcesso and BIT_ATIVO.inv()
    }
  val admin
    get() = login == "ADM"

  
  companion object {
    private val BIT_ATIVO = 2.pow(10)
  
    fun findAllAtivos(): List<UserSaci> {
      return saci.findAllUser()
        .filter {it.ativo}
    }
  
    fun findAllUser(): List<UserSaci> {
      return saci.findAllUser()
    }
  
    fun updateUser(user: UserSaci) {
      saci.updateUser(user)
    }
    
    fun findUser(login: String?): UserSaci? {
      return saci.findUser(login)
        .firstOrNull()
    }
  }
}

fun Int.pow(e: Int): Int = this.toDouble()
  .pow(e)
  .toInt()
