package pl.lanku.inventory.presentation.products

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.lanku.inventory.R
import pl.lanku.inventory.data.entity.Product

class ProductsActivity : AppCompatActivity() {

    private val viewModel: ProductsViewModel by viewModel()
    private var ean = ""
    private var name = ""
    private var description = ""
    private var category = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        viewModel.allProducts.observe(::getLifecycle) { products ->
            findViewById<TextView>(R.id.products_text_view)?.let {
                it.text = ""
                products.forEachIndexed { index, product ->
                    if (index == 0) {
                        it.text = String.format("%s", product.name)
                    } else {
                        it.text = String.format("%s, %s", it.text, product.name)
                    }
                }
            }
        }
        findViewById<FloatingActionButton>(R.id.add_product_button).setOnClickListener {
            ean = findViewById<EditText>(R.id.ean).toString()
            name = findViewById<EditText>(R.id.name).toString()
            description = findViewById<EditText>(R.id.description).toString()
            category = findViewById<EditText>(R.id.category).toString()
            val tempValid = validationData(ean,name,description,category)
            checkValid(tempValid)
        }
        findViewById<FloatingActionButton>(R.id.remove_product_button).setOnClickListener {
            viewModel.deleteAll()
        }
    }

    private fun checkValid(tempValid: Boolean) {
        if (tempValid)
            viewModel.save(Product(ean, name, description, category))
        else
            Toast.makeText(this,"Please check your data",Toast.LENGTH_LONG).show()
    }

    private fun validationData(tempEAN : String, tempName : String, tempDescription : String, tempCategory : String): Boolean {
        return tempEAN.isNotEmpty()&&tempName.isNotEmpty()&&tempDescription.isNotEmpty()&&tempCategory.isNotEmpty()
    }

}
