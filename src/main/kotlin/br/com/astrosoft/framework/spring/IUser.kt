package br.com.astrosoft.framework.spring

interface IUser {
  val login: String
  val senha: String

  fun roles(): List<String>
}