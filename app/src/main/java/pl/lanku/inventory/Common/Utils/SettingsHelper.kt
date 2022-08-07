package pl.lanku.inventory.common.utils

import android.view.View

class SettingsHelper {
    fun onClickSettingsShowHide(view: View){
        if (view.visibility == View.INVISIBLE) {
            view.visibility = View.VISIBLE
            view.animate().setDuration(800).rotation(180F).start()
        } else {
            view.visibility = View.INVISIBLE
            view.animate().setDuration(800).rotation(-180F).start()
        }
    }
}