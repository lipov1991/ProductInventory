package pl.lanku.inventory.data.bbscaner

import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import pl.lanku.inventory.R


class ProductsBBScannerActivity(contentLayoutId: Int) : ComponentActivity(contentLayoutId) {

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            findViewById<EditText>(R.id.name)?.let {
                it.text = String.format(result.contents)
            }
        }
    }

    fun onScanner(view: View?) {
        barcodeLauncher.launch(ScanOptions())
    }
}
