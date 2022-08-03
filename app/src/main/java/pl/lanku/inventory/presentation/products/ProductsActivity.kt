package pl.lanku.inventory.presentation.products

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
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
            binding.name.setText(product.name)
            binding.description.setText(product.description)
            binding.category.setText(product.category)
        }
    }

    private fun cleanET() {
        binding.name.text = null
        binding.description.text = null
        binding.category.text = null
    }

    private fun cancelScanCode() {
        Toast.makeText(this@ProductsActivity, string.scaning_cancel, Toast.LENGTH_LONG).show()
    }

    private fun validateForm() {
        viewModel.nameContent = binding.name.text.toString()
        viewModel.descriptionContent = binding.description.text.toString()
        viewModel.categoryContent = binding.category.text.toString()
        binding.saveProductButton.isEnabled =
            viewModel.barcodeContent.isNotBlank() && viewModel.nameContent.isNotBlank() && viewModel.descriptionContent.isNotBlank() && viewModel.categoryContent.isNotBlank()
    }

    private fun setFormFieldsEnabled(enable: Boolean) {
        binding.name.isEnabled = enable
        binding.description.isEnabled = enable
        binding.category.isEnabled = enable
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
        onClickInputsClear()
        onClickScannerStartSet()
        onClickEditProductSet(productAdapter, layoutManager)
        changeLanguage()

        recyclerViewProducts.adapter = productAdapter
        recyclerViewProducts.layoutManager = layoutManager
        recyclerViewProducts.setHasFixedSize(true)

        viewModel.allProducts.observe(::getLifecycle) { products ->
            productAdapter.updateProducts(products)
        }
    }

    private fun onClickScannerStartSet() {
        binding.qrScanner.setOnClickListener {
            barcodeLauncher.launch(
                viewModel.scanBarcode(
                    getString(string.qr_scanner_prompt)
                )
            )
        }
    }

    private fun onClickSaveSet() {
        binding.saveProductButton.setOnClickListener {
            validateForm()
            viewModel.save(
                Product(
                    viewModel.barcodeContent,
                    viewModel.nameContent,
                    viewModel.descriptionContent,
                    viewModel.categoryContent
                )
            )
            clearInputs()
            setFormFieldsEnabled(false)
        }
    }

    private fun clearInputs() {
        viewModel.barcodeContent = null.toString()
        binding.name.text.clear()
        binding.description.text.clear()
        binding.category.text.clear()
    }

    private fun textChangeSet() {
        binding.name.addTextChangedListener(formFiledValueChangeListener)
        binding.description.addTextChangedListener(formFiledValueChangeListener)
        binding.category.addTextChangedListener(formFiledValueChangeListener)
    }

    private fun changeLanguage() {
        binding.eng.setOnClickListener {
            Toast.makeText(this@ProductsActivity, string.engchange,Toast.LENGTH_SHORT).show()
            changeBackgroundColor("0")
        }
        binding.pol.setOnClickListener {
            Toast.makeText(this@ProductsActivity, string.polchange,Toast.LENGTH_SHORT).show()
            changeBackgroundColor("1")
        }
        binding.ger.setOnClickListener {
            Toast.makeText(this@ProductsActivity, string.gerchange,Toast.LENGTH_SHORT).show()
            changeBackgroundColor("2")
        }
    }

    private fun changeBackgroundColor(lan: String){
        when (lan) {
            "0" -> {
                binding.eng.setBackgroundColor(Color.GREEN)
                binding.pol.setBackgroundColor(Color.alpha(0))
                binding.ger.setBackgroundColor(Color.alpha(0))
            }
            "1" -> {
                binding.eng.setBackgroundColor(Color.alpha(0))
                binding.pol.setBackgroundColor(Color.GREEN)
                binding.ger.setBackgroundColor(Color.alpha(0))
            }
            "2" -> {
                binding.eng.setBackgroundColor(Color.alpha(0))
                binding.pol.setBackgroundColor(Color.alpha(0))
                binding.ger.setBackgroundColor(Color.GREEN)
            }
        }
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
                            it.findViewById<TextView>(R.id.recycler_ean)?.text.toString()
                        binding.name.setText(it.findViewById<TextView>(R.id.recycler_name)?.text.toString())
                        binding.description.setText(it.findViewById<TextView>(R.id.recycler_description)?.text.toString())
                        binding.category.setText(it.findViewById<TextView>(R.id.recycler_category)?.text.toString())
                        setFormFieldsEnabled(true)
                        validateForm()
                    }
                }
            }
        })
    }

    private fun onClickInputsClear() {
        binding.clearInputs.setOnClickListener {
            clearInputs()
            validateForm()
        }
    }
}
