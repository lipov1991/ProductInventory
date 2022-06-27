package pl.lanku.inventory.common.utils

import android.widget.Toast
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import pl.lanku.inventory.common.CameraCommonUtils
import pl.lanku.inventory.presentation.products.ProductsActivity

open class QrUtils(private val productsActivity:ProductsActivity,cameraCommonUtils: CameraCommonUtils) : ProductsActivity(cameraCommonUtils) {
    private val barcodeLauncher =
        registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
            if (result.contents.isNullOrBlank()) {
                productsActivity.setFormFieldsEnabled(false)
                Toast.makeText(this@QrUtils, "Skanowanie anulowane", Toast.LENGTH_LONG).show()
            } else {
                productsActivity.setFormFieldsEnabled(true)
                barcodeContent = result.contents
                productsActivity.barcodeCheck()
            }
        }

    fun scanBarcode(options: ScanOptions){
        barcodeLauncher.launch(options)
    }
}