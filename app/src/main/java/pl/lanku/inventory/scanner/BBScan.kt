package pl.lanku.inventory.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import pl.lanku.inventory.R
import pl.lanku.inventory.presentation.products.ProductsActivity

class BBScan : AppCompatActivity() {
    
        private lateinit var codeScanner: CodeScanner

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_products)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 123)
            } else {
                startScanning()
            }
            findViewById<EditText>(R.id.back).setOnClickListener{
                setContentView(R.layout.activity_products)
            }
        }
    
        private fun startScanning() {
            val scannerView: CodeScannerView = findViewById(R.id.scanner_view)
            codeScanner = CodeScanner(this, scannerView)
            codeScanner.camera = CodeScanner.CAMERA_BACK
            codeScanner.formats = CodeScanner.ALL_FORMATS
            codeScanner.autoFocusMode = AutoFocusMode.SAFE
            codeScanner.scanMode = ScanMode.SINGLE
            codeScanner.isAutoFocusEnabled = true
            codeScanner.isFlashEnabled = false

            codeScanner.decodeCallback = DecodeCallback {
                runOnUiThread {
                    ProductsActivity.ProductsActivity(it.text)
                }
            }
            codeScanner.errorCallback = ErrorCallback {
                runOnUiThread {
                    Toast.makeText(this, "Camera initialization error: ${it.message}",
                        Toast.LENGTH_LONG).show()
                }
            }

            scannerView.setOnClickListener {
                codeScanner.startPreview()
            }
        }

        override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == 123) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show()
                    startScanning()
                } else {
                    Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show()
                }
            }
        }

        override fun onResume() {
            super.onResume()
            if(::codeScanner.isInitialized) {
                codeScanner.startPreview()
            }
        }

        override fun onPause() {
            if(::codeScanner.isInitialized) {
                codeScanner.releaseResources()
            }
            super.onPause()
        }
}