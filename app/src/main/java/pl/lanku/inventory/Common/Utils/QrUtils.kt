package pl.lanku.inventory.common.utils

import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import pl.lanku.inventory.presentation.products.ProductsActivity

open class QrUtils(private val productsActivity:ProductsActivity){
    private val barcodeLauncher =
        productsActivity.registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
            if (result.contents.isNullOrBlank()) {
                productsActivity.setFormFieldsEnabled(false)
                productsActivity.cancelScanCode()
            } else {
                productsActivity.setFormFieldsEnabled(true)
                productsActivity.barcodeContent = result.contents
                productsActivity.barcodeCheck()
            }
        }

    fun scanBarcode(options: ScanOptions){
        barcodeLauncher.launch(options)
    }
}