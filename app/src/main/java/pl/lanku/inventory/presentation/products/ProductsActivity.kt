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
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.lanku.inventory.R
import pl.lanku.inventory.R.id.remove_product_button
import pl.lanku.inventory.R.id.save_product_button
import pl.lanku.inventory.data.entity.Product

open class ProductsActivity :
    AppCompatActivity() {

    private val viewModel: ProductsViewModel by viewModel()
    var barcodeContent: String = ""
    private var nameDC: String = ""
    private var descriptionDC: String = ""
    private var categoryDC: String = ""
    private val barcodeLauncher =
        registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
            if (result.contents.isNullOrBlank()) {
                setFormFieldsEnabled(false)
                cancelScanCode()
            } else {
                barcodeContent = result.contents
                barcodeCheck()
                setFormFieldsEnabled(true)
            }
        }

    private val formFiledValueChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        override fun afterTextChanged(editable: Editable) {
            validateForm()
        }
    }

    private fun barcodeCheck(){
        val itemFromBbContent = viewModel.selectOneItem(barcodeContent).toString()
        findViewById<EditText>(R.id.name).setText(itemFromBbContent)
        findViewById<EditText>(R.id.description).setText(itemFromBbContent)
        findViewById<EditText>(R.id.category).setText(itemFromBbContent)
    }

    private fun cancelScanCode(){
        Toast.makeText(this@ProductsActivity, "Skanowanie anulowane", Toast.LENGTH_LONG).show()
    }

    private fun validateForm() {
        nameDC = findViewById<EditText>(R.id.name).text.toString()
        descriptionDC = findViewById<EditText>(R.id.description).text.toString()
        categoryDC = findViewById<EditText>(R.id.category).text.toString()
        findViewById<FloatingActionButton>(save_product_button)?.isEnabled =
            barcodeContent.isNotBlank() && nameDC.isNotBlank() && descriptionDC.isNotBlank() && categoryDC.isNotBlank()

    }

    private fun setFormFieldsEnabled(enable: Boolean) {
        findViewById<EditText>(R.id.name).isEnabled = enable
        findViewById<EditText>(R.id.description).isEnabled = enable
        findViewById<EditText>(R.id.category).isEnabled = enable
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        val nameET = findViewById<EditText>(R.id.name)
        val descriptionET = findViewById<EditText>(R.id.description)
        val categoryET = findViewById<EditText>(R.id.category)
        viewModel.allProducts.observe(::getLifecycle) { products ->
            findViewById<TextView>(R.id.products_text_view)?.let { it ->
                it.text = ""
                products.forEachIndexed { index, product ->
                    if (index == 0) {
                        it.text = String.format(
                            "%s - %s - %s\n",
                            product.name,
                            product.description,
                            product.category
                        )
                    } else {
                        it.text = String.format(
                            "%s\n%s - %s - %s",
                            it.text,
                            product.name,
                            product.category,
                            product.description
                        )
                    }
                }
            }
        }

        findViewById<Button>(R.id.qrScanner).setOnClickListener{
            viewModel.scanBarcode(barcodeLauncher,getString(R.string.qr_scanner_prompt))
        }

        findViewById<FloatingActionButton>(save_product_button).setOnClickListener {
            viewModel.save(Product(barcodeContent, nameDC, descriptionDC, categoryDC))
            nameET.text.clear()
            descriptionET.text.clear()
            categoryET.text.clear()
            setFormFieldsEnabled(false)
        }

        findViewById<FloatingActionButton>(remove_product_button).setOnClickListener {
            viewModel.deleteAll()
        }

        nameET?.addTextChangedListener(formFiledValueChangeListener)
        descriptionET?.addTextChangedListener(formFiledValueChangeListener)
        categoryET?.addTextChangedListener(formFiledValueChangeListener)
    }
}