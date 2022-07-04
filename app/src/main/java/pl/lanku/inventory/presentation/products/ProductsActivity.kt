package pl.lanku.inventory.presentation.products

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.lanku.inventory.R
import pl.lanku.inventory.R.id.remove_product_button
import pl.lanku.inventory.R.id.save_product_button
import pl.lanku.inventory.data.entity.Product
import pl.lanku.inventory.databinding.ActivityProductsBinding
import pl.lanku.inventory.presentation.productadapter.ProductAdapter


class ProductsActivity : AppCompatActivity() {
    private val viewModel: ProductsViewModel by viewModel()
    private lateinit var binding: ActivityProductsBinding

    //Value
    private var barcodeContent: String = ""
    private var nameContent: String = ""
    private var descriptionContent: String = ""
    private var categoryContent: String = ""

    //ET = EditText
    private lateinit var nameET:TextView //= findViewById<EditText>(R.id.name)
    private lateinit var descriptionET: TextView //= findViewById<EditText>(R.id.description)
    private lateinit var categoryET: TextView //= findViewById<EditText>(R.id.category)
    private lateinit var saveButton: FloatingActionButton //= findViewById<EditText>(R.id.save_product_button)

    //RI = Recycle Item
    private lateinit var eanRI: TextView //= findViewById<EditText>(R.id.recycler_ean)
    private lateinit var nameRI: TextView //= findViewById<EditText>(R.id.recycler_name)
    private lateinit var descriptionRI:TextView //= findViewById<EditText>(R.id.recycler_description)
    private lateinit var categoryRI: TextView //= findViewById<EditText>(R.id.recycler_category)

    private val barcodeLauncher =
        registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
            if (result.contents.isNullOrBlank()) {
                setFormFieldsEnabled(false)
                cancelScanCode()
            } else {
                barcodeContent = result.contents
                getRowCount()
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

    private fun getRowCount() {
        viewModel.getRowCount(barcodeContent).observe(::getLifecycle) {
            if (it.toInt() > 0) {
                barcodeCheck()
            } else {
                cleanET()
            }
        }
    }

    private fun barcodeCheck() {
        viewModel.selectOneItem(barcodeContent).observe(::getLifecycle) { product ->
//            nameET.setText(product.name)
            findViewById<EditText>(R.id.name).setText(product.name)
            findViewById<EditText>(R.id.description).setText(product.description)
            findViewById<EditText>(R.id.category).setText(product.category)
        }
    }

    private fun cleanET() {
        findViewById<EditText>(R.id.name).text = null
        findViewById<EditText>(R.id.description).text = null
        findViewById<EditText>(R.id.category).text = null
    }

    private fun cancelScanCode() {
        Toast.makeText(this@ProductsActivity, "Skanowanie anulowane", Toast.LENGTH_LONG).show()
    }

    private fun validateForm() {
        nameContent = findViewById<EditText>(R.id.name).text.toString()
        descriptionContent = findViewById<EditText>(R.id.description).text.toString()
        categoryContent = findViewById<EditText>(R.id.category).text.toString()
        findViewById<FloatingActionButton>(save_product_button)?.isEnabled =
            barcodeContent.isNotBlank() && nameContent.isNotBlank() && descriptionContent.isNotBlank() && categoryContent.isNotBlank()
    }

    private fun setFormFieldsEnabled(enable: Boolean) {
        findViewById<EditText>(R.id.name).isEnabled = enable
        findViewById<EditText>(R.id.description).isEnabled = enable
        findViewById<EditText>(R.id.category).isEnabled = enable
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        nameET = findViewById<EditText>(R.id.name)
//        descriptionET = findViewById<EditText>(R.id.description)
//        categoryET = findViewById<EditText>(R.id.category)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_products)

        val recyclerViewProducts = binding.recycler
        val productAdapter = ProductAdapter(emptyList())
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        productAdapter.setOnClickItemListener(object : ProductAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                Toast.makeText(this@ProductsActivity,"Wybrano produkt",Toast.LENGTH_SHORT).show()
                layoutManager.findViewByPosition(position).let {
                    if (it != null) {
                        barcodeContent = it.findViewById<TextView>(R.id.recycler_ean).text.toString()
                        findViewById<EditText>(R.id.name).setText(it.findViewById<TextView>(R.id.recycler_name).text.toString())
                        findViewById<EditText>(R.id.description).setText(it.findViewById<TextView>(R.id.recycler_description).text.toString())
                        findViewById<EditText>(R.id.category).setText(it.findViewById<TextView>(R.id.recycler_category).text.toString())
                        setFormFieldsEnabled(true)
                        validateForm()
                    }
                }
            }
        })

        recyclerViewProducts.adapter = productAdapter
        recyclerViewProducts.layoutManager = layoutManager
        recyclerViewProducts.setHasFixedSize(true)
        viewModel.allProducts.observe(::getLifecycle) { products ->
            productAdapter.updateProducts(products)
        }

        findViewById<Button>(R.id.qrScanner).setOnClickListener {
            viewModel.scanBarcode(
                barcodeLauncher,
                getString(R.string.qr_scanner_prompt)
            )
        }

        findViewById<FloatingActionButton>(save_product_button).setOnClickListener {
            viewModel.save(Product(barcodeContent, nameContent, descriptionContent, categoryContent))
            findViewById<EditText>(R.id.name).text.clear()
            findViewById<EditText>(R.id.description).text.clear()
            findViewById<EditText>(R.id.category).text.clear()
            setFormFieldsEnabled(false)
        }

        findViewById<FloatingActionButton>(remove_product_button).setOnClickListener {
            viewModel.deleteAll()
        }

        findViewById<EditText>(R.id.name)?.addTextChangedListener(formFiledValueChangeListener)
        findViewById<EditText>(R.id.description)?.addTextChangedListener(formFiledValueChangeListener)
        findViewById<EditText>(R.id.category)?.addTextChangedListener(formFiledValueChangeListener)
    }
}