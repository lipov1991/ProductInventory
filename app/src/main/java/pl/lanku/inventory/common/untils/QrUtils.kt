package pl.lanku.inventory.common.untils

import com.journeyapps.barcodescanner.ScanOptions
import pl.lanku.inventory.R
import pl.lanku.inventory.common.QrCommon

object QrUtils : QrCommon() {

    private const val MAIN_CAMERA_ID = 0
    fun startCamera() {
        val options = ScanOptions().apply {
            this.setPrompt(getString(R.string.qr_scanner_prompt))
            this.setCameraId(MAIN_CAMERA_ID)
            this.setBeepEnabled(false)
            this.setBarcodeImageEnabled(true)
        }
        qrCommonBarcodeLauncher().launch(options)
    }
    
}