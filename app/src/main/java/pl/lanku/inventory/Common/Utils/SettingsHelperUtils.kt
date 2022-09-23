package pl.lanku.inventory.common.utils

import android.view.View

class SettingsHelperUtils {
    fun onClickSettingsShowHide(view: View, settingButton: View){
        if (view.visibility == View.INVISIBLE) {
//            view.visibility = View.VISIBLE
            settingButton.animate().setDuration(800).rotation(180F).start()
        } else {
//            view.visibility = View.INVISIBLE
            settingButton.animate().setDuration(800).rotation(-180F).start()
        }
    }
}