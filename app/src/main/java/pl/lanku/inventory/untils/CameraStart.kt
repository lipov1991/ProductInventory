package pl.lanku.inventory.untils

import android.os.Bundle
import android.widget.Toast
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import pl.lanku.inventory.R
import pl.lanku.inventory.presentation.products.ProductsActivity

class CameraStart
    : ProductsActivity() {

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
    companion object : ProductsActivity() {
        fun startCamera(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_products)
            val options = ScanOptions().apply {
                setPrompt(getString(R.string.qr_scanner_prompt))
                setCameraId(CameraStart.MAIN_CAMERA_ID)
                val beepEnabled = setBeepEnabled(false)
                setBarcodeImageEnabled(true)
            }
            barcodeLauncher.launch(options)
        }

        private const val MAIN_CAMERA_ID = 0
    }
}