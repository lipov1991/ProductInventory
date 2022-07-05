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
    private val barcodeLauncher =
        registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
            if (result.contents.isNullOrBlank()) {
                setFormFieldsEnabled(false)
                cancelScanCode()
            } else {
                viewModel.barcodeContent = result.contents
                getRowCount()
                setFormFieldsEnabled(true)
            }
        }

    private val formFiledValueChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(editable: Editable) {
            validateForm()
        }
    }

    private fun getRowCount() {
        viewModel.getRowCount(viewModel.barcodeContent).observe(::getLifecycle) {
            if (it.toInt() > 0) {
                barcodeCheck()
            } else {
                cleanET()
            }
        }
    }

    private fun barcodeCheck() {
        viewModel.selectOneItem(viewModel.barcodeContent).observe(::getLifecycle) { product ->
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
        viewModel.nameContent = findViewById<EditText>(R.id.name).text.toString()
        viewModel.descriptionContent = findViewById<EditText>(R.id.description).text.toString()
        viewModel.categoryContent = findViewById<EditText>(R.id.category).text.toString()
        findViewById<FloatingActionButton>(save_product_button)?.isEnabled =
            viewModel.barcodeContent.isNotBlank() && viewModel.nameContent.isNotBlank() && viewModel.descriptionContent.isNotBlank() && viewModel.categoryContent.isNotBlank()
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

        productAdapter.setOnClickItemListener(object : ProductAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(this@ProductsActivity, "Wybrano produkt", Toast.LENGTH_SHORT).show()
                layoutManager.findViewByPosition(position).let {
                    if (it != null) {
                        viewModel.barcodeContent =
                            it.findViewById<TextView>(R.id.recycler_ean).text.toString()
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
            viewModel.save(
                Product(
                    viewModel.barcodeContent,
                    viewModel.nameContent,
                    viewModel.descriptionContent,
                    viewModel.categoryContent
                )
            )
            findViewById<EditText>(R.id.name).text.clear()
            findViewById<EditText>(R.id.description).text.clear()
            findViewById<EditText>(R.id.category).text.clear()
            setFormFieldsEnabled(false)
        }

        findViewById<FloatingActionButton>(remove_product_button).setOnClickListener {
            viewModel.deleteAll()
        }

        findViewById<EditText>(R.id.name)?.addTextChangedListener(formFiledValueChangeListener)
        findViewById<EditText>(R.id.description)?.addTextChangedListener(
            formFiledValueChangeListener
        )
        findViewById<EditText>(R.id.category)?.addTextChangedListener(formFiledValueChangeListener)
    }
}