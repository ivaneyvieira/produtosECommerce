package br.com.astrosoft.produtosEComerce.viewmodel

import br.com.astrosoft.produtosEComerce.model.beans.UserSaci
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.framework.viewmodel.fail

class UsuarioViewModel(view: IUsuarioView): ViewModel<IUsuarioView>(view) {
  fun findAll(): List<UserSaci>? {
    return UserSaci.findAllAtivos()
  }
  
  fun add(user: UserSaci): UserSaci? {
    exec {
      user.ativo = true
      validaUser(user)
      UserSaci.updateUser(user)
    }
    return user
  }
  
  private fun validaUser(user: UserSaci?): UserSaci {
    UserSaci.findUser(user?.login) ?: fail("Usuário não encontrado no saci")
    return user ?: fail("Usuário não selecionado")
  }
  
  fun update(user: UserSaci?): UserSaci? {
    exec {
      UserSaci.updateUser(validaUser(user))
    }
    return user
  }
  
  fun delete(user: UserSaci?) {
    exec {
      val userValid = validaUser(user)
      userValid.ativo = false
      UserSaci.updateUser(userValid)
    }
  }
  
 // fun findLogins() : List<String>{
 //   return UserSaci.findAllUser().map{it.login}.distinct().sorted()
 // }
  
  fun findAllUser(): List<UserSaci> {
    return UserSaci.findAllUser()
  }
}

interface IUsuarioView: IView