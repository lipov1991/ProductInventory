package pl.lanku.inventory.common

import android.content.res.Resources
import com.journeyapps.barcodescanner.ScanOptions
import pl.lanku.inventory.R

class CameraCommonUtils{
    companion object {
        private const val MAIN_CAMERA_ID = 0
        fun buttonStartCamera(): ScanOptions {
            val options = ScanOptions().apply {
                setPrompt(Resources.getSystem().getString(R.string.qr_scanner_prompt))
                setCameraId(MAIN_CAMERA_ID)
                setBeepEnabled(false)
                setBarcodeImageEnabled(true)
            }
            return options
        }
    }
}
