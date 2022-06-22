package pl.lanku.inventory.untils

import android.widget.Toast
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import pl.lanku.inventory.R
import pl.lanku.inventory.presentation.products.ProductsActivity

object CameraStart : ProductsActivity() {

    private const val MAIN_CAMERA_ID = 0
    private val barcodeLauncher =
        registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
            if (result.contents.isNullOrBlank()) {
                setFormFieldsEnabled(false)
                Toast.makeText(this@CameraStart, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                setFormFieldsEnabled(true)
                barcodeContent = result.contents
            }
        }

    fun startCamera()  {
        val options = ScanOptions().apply {
            this.setPrompt(getString(R.string.qr_scanner_prompt))
            this.setCameraId(MAIN_CAMERA_ID)
            this.setBeepEnabled(false)
            this.setBarcodeImageEnabled(true)
        }
        barcodeLauncher.launch(options)
    }
}