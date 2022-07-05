package pl.lanku.inventory.presentation.products

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import kotlinx.android.synthetic.main.activity_products.*
import kotlinx.android.synthetic.main.recycler_item.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.lanku.inventory.R
import pl.lanku.inventory.R.string
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
            name.setText(product.name)
            description.setText(product.description)
            category.setText(product.category)
        }
    }

    private fun cleanET() {
        name.text = null
        description.text = null
        category.text = null
    }

    private fun cancelScanCode() {
        Toast.makeText(this@ProductsActivity, string.scaning_cancel, Toast.LENGTH_LONG).show()
    }

    private fun validateForm() {
        viewModel.nameContent = name.text.toString()
        viewModel.descriptionContent = description.text.toString()
        viewModel.categoryContent = category.text.toString()
        save_product_button?.isEnabled =
            viewModel.barcodeContent.isNotBlank() && viewModel.nameContent.isNotBlank() && viewModel.descriptionContent.isNotBlank() && viewModel.categoryContent.isNotBlank()
    }

    private fun setFormFieldsEnabled(enable: Boolean) {
        name.isEnabled = enable
        description.isEnabled = enable
        category.isEnabled = enable
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_products)

        val recyclerViewProducts = binding.recycler
        val productAdapter = ProductAdapter(emptyList())
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        textChangeSet()
        onClickSaveSet()
        onClickClearDbSet()
        onClickScannerStartSet()
        onClickEditProductSet(productAdapter, layoutManager)

        recyclerViewProducts.adapter = productAdapter
        recyclerViewProducts.layoutManager = layoutManager
        recyclerViewProducts.setHasFixedSize(true)

        viewModel.allProducts.observe(::getLifecycle) { products ->
            productAdapter.updateProducts(products)
        }
    }

    private fun onClickScannerStartSet() {
        qrScanner.setOnClickListener {
            viewModel.scanBarcode(
                barcodeLauncher,
                getString(string.qr_scanner_prompt)
            )
        }
    }

    private fun onClickSaveSet() {
        save_product_button.setOnClickListener {
            viewModel.save(
                Product(
                    viewModel.barcodeContent,
                    viewModel.nameContent,
                    viewModel.descriptionContent,
                    viewModel.categoryContent
                )
            )
            name.text.clear()
            description.text.clear()
            category.text.clear()
            setFormFieldsEnabled(false)
        }
    }

    private fun onClickClearDbSet() {
        remove_product_button.setOnClickListener {
            viewModel.deleteAll()
        }
    }

    private fun textChangeSet() {
        name?.addTextChangedListener(formFiledValueChangeListener)
        description?.addTextChangedListener(formFiledValueChangeListener)
        category?.addTextChangedListener(formFiledValueChangeListener)
    }

    private fun onClickEditProductSet(
        productAdapter: ProductAdapter,
        layoutManager: RecyclerView.LayoutManager
    ) {
        productAdapter.setOnClickItemListener(object : ProductAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(this@ProductsActivity, string.chose_item, Toast.LENGTH_SHORT).show()
                layoutManager.findViewByPosition(position).let {
                    if (it != null) {
                        viewModel.barcodeContent =
                            it.recycler_ean.text.toString()
                        name.setText(it.recycler_name.text.toString())
                        description.setText(it.recycler_description.text.toString())
                        category.setText(it.recycler_category.text.toString())
                        setFormFieldsEnabled(true)
                        validateForm()
                    }
                }
            }
        })
    }
}