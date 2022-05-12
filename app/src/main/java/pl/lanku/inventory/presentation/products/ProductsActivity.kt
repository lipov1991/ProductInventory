package pl.lanku.inventory.presentation.products

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.lanku.inventory.R
import pl.lanku.inventory.data.entity.Product

class ProductsActivity : AppCompatActivity() {

    private val viewModel: ProductsViewModel by viewModel()

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
            viewModel.save(Product("ean", "Test product", "description...", "c1"))
        }
    }
}
