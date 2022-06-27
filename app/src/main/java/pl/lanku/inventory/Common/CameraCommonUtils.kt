package pl.lanku.inventory.common

import android.content.res.Resources
import com.journeyapps.barcodescanner.ScanOptions
import pl.lanku.inventory.R
import pl.lanku.inventory.common.utils.QrUtils

class CameraCommonUtils(private val qrUtils:QrUtils){
    companion object {
        private const val MAIN_CAMERA_ID = 0
    }

    private fun scanBarcodeStart(options: ScanOptions) {
        qrUtils.scanBarcode(options)
    }

    fun buttonStartCamera() {
        val options = ScanOptions().apply {
            setPrompt(Resources.getSystem().getString(R.string.qr_scanner_prompt))
            setCameraId(MAIN_CAMERA_ID)
            setBeepEnabled(false)
            setBarcodeImageEnabled(true)
        }
        scanBarcodeStart(options)
    }
}
