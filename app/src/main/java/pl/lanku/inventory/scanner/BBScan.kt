package pl.lanku.inventory.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
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
        }

        private fun startScanning() {
            // Parameters (default values)
            val scannerView: CodeScannerView = findViewById(R.id.scanner_view)
            codeScanner = CodeScanner(this, scannerView)
            codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
            codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
            // ex. listOf(BarcodeFormat.QR_CODE)
            codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
            codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
            codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
            codeScanner.isFlashEnabled = false // Whether to enable flash or not

            // Callbacks
            codeScanner.decodeCallback = DecodeCallback {
                runOnUiThread {
                    Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
                }
            }
            codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
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