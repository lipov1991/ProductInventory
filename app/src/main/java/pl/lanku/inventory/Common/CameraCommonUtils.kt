package pl.lanku.inventory.common

import com.journeyapps.barcodescanner.ScanOptions
import pl.lanku.inventory.R
import pl.lanku.inventory.common.utils.QrUtils
import pl.lanku.inventory.presentation.products.ProductsActivity

open class CameraCommonUtils(private val qrUtils:QrUtils, productsActivity: ProductsActivity,
                             cameraCommonUtils: CameraCommonUtils?
):
    QrUtils(productsActivity, cameraCommonUtils) {
    companion object {
        private const val MAIN_CAMERA_ID = 0
    }

    private fun scanBarcodeStart(options: ScanOptions) {
        qrUtils.scanBarcode(options)
    }

    open fun buttonStartCamera() {
        val options = ScanOptions().apply {
            setPrompt(getString(R.string.qr_scanner_prompt))
            setCameraId(MAIN_CAMERA_ID)
            setBeepEnabled(false)
            setBarcodeImageEnabled(true)
        }
        scanBarcodeStart(options)
    }
}
