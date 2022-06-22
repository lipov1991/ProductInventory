package pl.lanku.inventory.presentation.products

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.lanku.inventory.R
import pl.lanku.inventory.data.entity.Product
import pl.lanku.inventory.untils.CameraStart.startCamera


open class ProductsActivity : AppCompatActivity() {

    private val viewModel: ProductsViewModel by viewModel()
    var barcodeContent: String = ""
    private var name: String = ""
    private var description: String = ""
    private var category: String = ""
    private val nameET: EditText by lazy { this.findViewById(R.id.name) }
    private val descriptionET: EditText by lazy { this.findViewById(R.id.description) }
    private val categoryET: EditText by lazy { this.findViewById(R.id.description) }

    private val formFiledValueChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

        override fun afterTextChanged(editable: Editable) {
            validateForm()
        }
    }

    private fun validateForm() {
        name = nameET.text.toString()
        description = descriptionET.text.toString()
        category = categoryET.text.toString()
        findViewById<FloatingActionButton>(R.id.save_product_button)?.isEnabled =
            barcodeContent.isNotBlank() && name.isNotBlank() && description.isNotBlank() && category.isNotBlank()
    }

    fun setFormFieldsEnabled(
        enable: Boolean
    ) {
        nameET.isEnabled = enable
        descriptionET.isEnabled = enable
        categoryET.isEnabled = enable
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
                            "%s\t%s\t%s\t%s\n",
                            product.ean,
                            product.name,
                            product.description,
                            product.category
                        )
                    } else {
                        it.text = String.format(
                            "%s\n%s\t%s\t%s\t%s",
                            it.text,
                            product.ean,
                            product.name,
                            product.description,
                            product.category
                        )
                    }
                }
            }
        }

        findViewById<Button>(R.id.qrScanner).setOnClickListener{
            startCamera()
        }

        findViewById<FloatingActionButton>(R.id.save_product_button).setOnClickListener {
            viewModel.save(Product(barcodeContent, name, description, category))
            nameET.text.clear()
            descriptionET.text.clear()
            categoryET.text.clear()
            setFormFieldsEnabled(false)
        }

        findViewById<FloatingActionButton>(R.id.remove_product_button).setOnClickListener {
            viewModel.deleteAll()
        }

        nameET.addTextChangedListener(formFiledValueChangeListener)
        descriptionET.addTextChangedListener(
            formFiledValueChangeListener
        )

        categoryET.addTextChangedListener(formFiledValueChangeListener)
    }

}
