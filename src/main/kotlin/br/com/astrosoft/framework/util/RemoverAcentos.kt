package br.com.astrosoft.framework.util

import java.text.Normalizer

private val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()
fun CharSequence.normalize(sep: String): String {
  val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
  return REGEX_UNACCENT.replace(temp, "")
    .replace("[^A-Za-z0-9 ]".toRegex(), "")
    .replace(" +".toRegex(), sep)
    .toLowerCase()
}
  
