package pl.lanku.inventory.data.bbscaner

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

class ProductsBBScannerActivity(contentLayoutId: Int) : ComponentActivity(contentLayoutId) {
    val getContent = registerForActivityResult(ActivityResultContracts.GetContent())
    {    }
}
