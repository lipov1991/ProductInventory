package pl.lanku.inventory.presentation.products

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.lanku.inventory.R
import pl.lanku.inventory.data.entity.Product


class ProductsActivity : AppCompatActivity() {

    companion object {
        private const val MAIN_CAMERA_ID = 0
    }

    private val viewModel: ProductsViewModel by viewModel()
    private var barcodeContent: String = ""
    private var name: String = ""
    private var description: String = ""
    private var category: String = ""
    private val barcodeLauncher =
        registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
            if (result.contents.isNullOrBlank()) {
                setFormFieldsEnabled(false)
                Toast.makeText(this@ProductsActivity, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                setFormFieldsEnabled(true)
                barcodeContent = result.contents
            }
        }
    private val formFiledValueChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable) {
            validateForm()
        }
    }

    private fun validateForm() {
        name = findViewById<EditText>(R.id.name).text.toString()
        description = findViewById<EditText>(R.id.description).text.toString()
        category = findViewById<EditText>(R.id.category).text.toString()
        findViewById<FloatingActionButton>(R.id.save_product_button)?.isEnabled =
            barcodeContent.isNotBlank() && name.isNotBlank() && description.isNotBlank() && category.isNotBlank()
    }

    private fun setFormFieldsEnabled(
        enable: Boolean
    ) {
        findViewById<EditText>(R.id.name).isEnabled = enable
        findViewById<EditText>(R.id.description).isEnabled = enable
        findViewById<EditText>(R.id.category).isEnabled = enable
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        viewModel.allProducts.observe(::getLifecycle) { products ->
            findViewById<TextView>(R.id.products_text_view)?.let {
                it.text = ""
                products.forEachIndexed { index, product ->
                    if (index == 0) {
                        it.text = String.format(
                            "%s - %s - %s \n",
                            product.ean,
                            product.name,
                            product.category
                        )
                    } else {
                        it.text = String.format(
                            "%s\n%s - %s - %s",
                            it.text,
                            product.ean,
                            product.name,
                            product.category
                        )
                    }
                }
            }
        }

        findViewById<Button>(R.id.qrScanner).setOnClickListener {
            val options = ScanOptions().apply {
                setPrompt(getString(R.string.qr_scanner_prompt))
                setCameraId(MAIN_CAMERA_ID)
                setBeepEnabled(false)
                setBarcodeImageEnabled(true)
            }
            barcodeLauncher.launch(options)
        }

        findViewById<FloatingActionButton>(R.id.save_product_button).setOnClickListener {
            viewModel.save(Product(barcodeContent, name, description, category))
            findViewById<EditText>(R.id.name).text.clear()
            findViewById<EditText>(R.id.description).text.clear()
            findViewById<EditText>(R.id.category).text.clear()
            setFormFieldsEnabled(false)
        }
        findViewById<FloatingActionButton>(R.id.remove_product_button).setOnClickListener {
            viewModel.deleteAll()
        }

        findViewById<EditText>(R.id.name)?.addTextChangedListener(formFiledValueChangeListener)
        findViewById<EditText>(R.id.description)?.addTextChangedListener(
            formFiledValueChangeListener
        )
        findViewById<EditText>(R.id.category)?.addTextChangedListener(formFiledValueChangeListener)
    }

}
