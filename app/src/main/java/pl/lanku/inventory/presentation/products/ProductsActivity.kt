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
    private var viewModel: ProductsViewModel by viewModel()
    private var ean : String = ""
    private var name : String = ""
    private var description : String = ""
    private var category : String = ""

    class ProductsActivity(private val ean: String?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        viewModel.allProducts.observe(::getLifecycle) { products ->
            findViewById<TextView>(R.id.products_text_view)?.let {
                it.text = ""
                products.forEachIndexed { index, product ->
                    if (index == 0) {
                        it.text = String.format("%s - %s - %s \n", product.ean, product.name, product.category)
                    } else {
                        it.text = String.format("%s\n%s - %s - %s", it.text, product.ean, product.name, product.category)
                    }
                }
            }
        }

        findViewById<EditText>(R.id.ean).setOnClickListener{
            setContentView(R.layout.activity_scanner)
        }

        findViewById<FloatingActionButton>(R.id.add_product_button).setOnClickListener {
            ean = findViewById<EditText>(R.id.ean).text.toString()
            name = findViewById<EditText>(R.id.name).text.toString()
            description = findViewById<EditText>(R.id.description).text.toString()
            category = findViewById<EditText>(R.id.category).text.toString()
            val tempValid = validationData(ean,name,description,category)
            checkValid(tempValid)
        }
        findViewById<FloatingActionButton>(R.id.remove_product_button).setOnClickListener {
            viewModel.deleteAll()
        }
    }

    private fun checkValid(tempValid: Boolean) {
        if (tempValid) {
            viewModel.save(Product(ean, name, description, category))
            findViewById<EditText>(R.id.ean).text.clear()
            findViewById<EditText>(R.id.name).text.clear()
            findViewById<EditText>(R.id.description).text.clear()
            findViewById<EditText>(R.id.category).text.clear()
        }
        else
            Toast.makeText(this,"Please check your data",Toast.LENGTH_LONG).show()
    }

    private fun validationData(tempEAN : String, tempName : String, tempDescription : String, tempCategory : String): Boolean {
        return tempEAN.isNotEmpty()&&tempName.isNotEmpty()&&tempDescription.isNotEmpty()&&tempCategory.isNotEmpty()
    }

}
