package pl.lanku.inventory.common

import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import pl.lanku.inventory.common.untils.QrUtils
import pl.lanku.inventory.presentation.products.ProductsActivity

open class QrCommon : ProductsActivity() {
    private val barcodeLauncher =
        QrUtils.registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
            if (result.contents.isNullOrBlank()) {
                QrUtils.setFormFieldsEnabled(false)
                Toast.makeText(this@QrCommon, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                QrUtils.setFormFieldsEnabled(true)
                QrUtils.barcodeContent = result.contents
                result.contents
            }
        }
    fun qrCommonBarcodeLauncher(): ActivityResultLauncher<ScanOptions> {
        return this.barcodeLauncher
    }
}