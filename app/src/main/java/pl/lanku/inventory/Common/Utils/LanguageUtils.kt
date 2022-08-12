package pl.lanku.inventory.common.utils

import android.graphics.Color
import android.view.View

class LanguageUtils {
    fun changeBackgroundColor(lan: String, engButton: View, polButton: View, gerButton: View) {
        when (lan) {
            "en" -> {
                engButton.setBackgroundColor(Color.GREEN)
                polButton.setBackgroundColor(Color.alpha(0))
                gerButton.setBackgroundColor(Color.alpha(0))
            }
            "pl" -> {
                engButton.setBackgroundColor(Color.alpha(0))
                polButton.setBackgroundColor(Color.GREEN)
                gerButton.setBackgroundColor(Color.alpha(0))
            }
            "de" -> {
                engButton.setBackgroundColor(Color.alpha(0))
                polButton.setBackgroundColor(Color.alpha(0))
                gerButton.setBackgroundColor(Color.GREEN)
            }
        }
    }

    fun setApplicationLanguage(language:String){
        //TODO()
        
//        when(language){
//            "en" ->
//            "pl" ->
//            "de" ->
//            else ->
//        }
    }
}