package br.com.astrosoft.framework.view

import com.github.juchar.colorpicker.ColorPickerFieldI18n
import com.github.juchar.colorpicker.ColorPickerFieldRaw
import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.init
import com.vaadin.flow.component.HasComponents
import org.vaadin.gatanaso.MultiselectComboBox

@VaadinDsl
fun <T> (@VaadinDsl HasComponents).multiselectComboBox(block: (@VaadinDsl MultiselectComboBox<T>).() -> Unit = {}) =
  init(MultiselectComboBox(), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).colorPick(
  label: String? = null, block: (@VaadinDsl ColorPickerFieldRaw).() -> Unit = {}
                                        ): @VaadinDsl ColorPickerFieldRaw {
  val compnent = ColorPickerFieldRaw(label).apply {
    setPinnedPalettes(true)
    isHexEnabled = true
    isAlphaEnabled = false
    isRgbEnabled = false
    isHslEnabled = false/*
    setPalette("#ff0000", "#bb0000", "#770000", "#330000",
               "#00ff00", "#00bb00", "#007700", "#003300",
               "#0000ff", "#0000bb", "#000077", "#000033",
               "#ffff00", "#bbbb00", "#777700", "#333300",
               "#ff00ff", "#bb00bb", "#770077", "#330033",
               "#00ffff", "#00bbbb", "#007777", "#003333",
               "#ffffff", "#bbbbbb", "#777777", "#333333",
               "#000000"
              )
              
     */
    this.i18n = ColorPickerFieldI18n().apply {
      select = "Seleciona"
      cancel = "Cancela"
    }
    this.textField.setSizeFull() //this.setSizeFull()
    isChangeFormatButtonVisible = false
    setCssCustomPropertiesEnabled(true)
    this.setPinnedPalettes(true) //setSizeFull()
  }
  return init(compnent, block)
}
