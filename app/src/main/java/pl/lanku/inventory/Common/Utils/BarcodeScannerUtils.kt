package pl.lanku.inventory.common.utils

import androidx.activity.result.ActivityResultLauncher
import com.journeyapps.barcodescanner.ScanOptions

class BarcodeScannerUtils {

    companion object {
        private const val MAIN_CAMERA_ID = 0
    }

    fun scanBarcode(barcodeLauncher: ActivityResultLauncher<ScanOptions>,scanCaption:String) {
        val options = ScanOptions().apply {
            setPrompt(scanCaption)
            setCameraId(MAIN_CAMERA_ID)
            setBeepEnabled(false)
            setBarcodeImageEnabled(true)
        }
        barcodeLauncher.launch(options)
    }
}