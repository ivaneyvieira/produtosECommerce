package br.com.astrosoft.produtosECommerce.model.beans

import br.com.astrosoft.framework.spring.IUser
import br.com.astrosoft.produtosECommerce.model.saci
import kotlin.math.pow
import kotlin.reflect.KProperty

class UserSaci : IUser {
  var no: Int = 0
  var name: String = ""
  override var login: String = ""
  override var senha: String = ""
  override fun roles(): List<String> {
    val roles = if (admin) listOf("ADMIN") else listOf("USER")

    return roles
  }

  var bitAcesso: Int = 0
  var storeno: Int = 0

  //Otiros campos
  var ativo by DelegateAuthorized(0)
  var produto by DelegateAuthorized(1)
  var categoria by DelegateAuthorized(2)
  var marca by DelegateAuthorized(3)
  var bitola by DelegateAuthorized(4)
  var cor by DelegateAuthorized(5)

  val admin
    get() = login == "ADM" || login == "YASMINE"

  companion object {
    fun findAllAtivos(): List<UserSaci> {
      return saci.findAllUser().filter { it.ativo }
    }

    fun findAllUser(): List<UserSaci> {
      return saci.findAllUser()
    }

    fun updateUser(user: UserSaci) {
      saci.updateUser(user)
    }

    fun findUser(login: String?): UserSaci? {
      return saci.findUser(login).firstOrNull()
    }
  }
}

fun Int.pow(e: Int): Int = this.toDouble().pow(e).toInt()

class DelegateAuthorized(numBit: Int) {
  private val bit = 2.toDouble().pow(numBit).toInt()

  operator fun getValue(thisRef: UserSaci?, property: KProperty<*>): Boolean {
    thisRef ?: return false
    return (thisRef.bitAcesso and bit) != 0 || thisRef.admin
  }

  operator fun setValue(thisRef: UserSaci?, property: KProperty<*>, value: Boolean?) {
    thisRef ?: return
    val v = value ?: false
    thisRef.bitAcesso = when {
      v    -> thisRef.bitAcesso or bit
      else -> thisRef.bitAcesso and bit.inv()
    }
  }
}