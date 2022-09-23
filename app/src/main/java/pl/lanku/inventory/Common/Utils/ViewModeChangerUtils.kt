package pl.lanku.inventory.common.utils

import android.view.View
import androidx.appcompat.app.AppCompatDelegate

class ViewModeChangerUtils {
    fun toLightModeChange(view_light: View, view_dark: View){
//        view_light.visibility = View.VISIBLE
//        view_dark.visibility = View.GONE
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    fun toDarkModeChange(view_light: View, view_dark: View){
//        view_light.visibility = View.GONE
//        view_dark.visibility = View.VISIBLE
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}