package pl.lanku.inventory.common.utils

import com.journeyapps.barcodescanner.ScanOptions

class BarcodeScannerUtils {

    companion object {
        private const val MAIN_CAMERA_ID = 0
    }

    fun scanBarcode(scanCaption:String) =
        ScanOptions().apply {
            setPrompt(scanCaption)
            setOrientationLocked(false)
            setCameraId(MAIN_CAMERA_ID)
            setBeepEnabled(false)
            setBarcodeImageEnabled(true)
        }
}