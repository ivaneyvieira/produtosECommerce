package br.com.astrosoft.framework.spring

import br.com.astrosoft.AppConfig
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class UserSaciDetailsService: UserDetailsService {
 
  override fun loadUserByUsername(username: String?): UserDetails {
    val user = AppConfig.findUser(username) ?: throw UsernameNotFoundException("Usuário inválido")
    return UserSaciDetails(user)
  }
}

class UserSaciDetails(val user : IUser) : UserDetails{
  override fun getAuthorities(): List<GrantedAuthority> = emptyList()
  
  override fun isEnabled(): Boolean = true
  
  override fun getUsername(): String = user.login
  
  override fun isCredentialsNonExpired(): Boolean = true
  
  override fun getPassword(): String = user.senha
  
  override fun isAccountNonExpired(): Boolean = true
  
  override fun isAccountNonLocked(): Boolean = true
}