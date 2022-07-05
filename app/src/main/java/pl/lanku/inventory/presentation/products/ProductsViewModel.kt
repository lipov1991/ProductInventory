package pl.lanku.inventory.presentation.products

import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.launch
import pl.lanku.inventory.common.utils.BarcodeScannerUtils
import pl.lanku.inventory.data.ProductRepository
import pl.lanku.inventory.data.entity.Product

class ProductsViewModel(
    private val productsRepository: ProductRepository,
    private val barcodeScannerUtils: BarcodeScannerUtils
) : ViewModel() {

    //Value
    var barcodeContent: String = ""
    var nameContent: String = ""
    var descriptionContent: String = ""
    var categoryContent: String = ""

//    //ET = EditText
//    var nameET: TextView //= findViewById<EditText>(R.id.name)
//    lateinit var descriptionET: TextView //= findViewById<EditText>(R.id.description)
//    lateinit var categoryET: TextView //= findViewById<EditText>(R.id.category)
//    lateinit var saveButton: FloatingActionButton //= findViewById<EditText>(R.id.save_product_button)
//
//    //RI = Recycle Item
//    lateinit var eanRI: TextView //= findViewById<EditText>(R.id.recycler_ean)
//    lateinit var nameRI: TextView //= findViewById<EditText>(R.id.recycler_name)
//    lateinit var descriptionRI: TextView //= findViewById<EditText>(R.id.recycler_description)
//    lateinit var categoryRI: TextView //= findViewById<EditText>(R.id.recycler_category)

    val allProducts: LiveData<List<Product>> = productsRepository.allProducts.asLiveData()

    fun save(product: Product) = viewModelScope.launch {
        productsRepository.save(product)
    }

    fun deleteAll() = viewModelScope.launch {
        productsRepository.deleteAll()
    }

    fun selectOneItem(barcodeContent: String): LiveData<Product> =
        productsRepository.selectOneItem(barcodeContent)

    fun scanBarcode(barcodeLauncher: ActivityResultLauncher<ScanOptions>, scanCaption: String) =
        barcodeScannerUtils.scanBarcode(barcodeLauncher, scanCaption)

    fun getRowCount(barcode: String): LiveData<Integer> =
        productsRepository.getRowCount(barcode)
}
