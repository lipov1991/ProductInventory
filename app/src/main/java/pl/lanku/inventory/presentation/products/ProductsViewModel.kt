package pl.lanku.inventory.presentation.products

import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.journeyapps.barcodescanner.ScanOptions
import com.journeyapps.barcodescanner.Util
import kotlinx.coroutines.launch
import pl.lanku.inventory.common.utils.BarcodeScannerUtils
import pl.lanku.inventory.data.ProductRepository
import pl.lanku.inventory.data.entity.Product

class ProductsViewModel(private val productsRepository: ProductRepository, private val barcodeScannerUtils: BarcodeScannerUtils) : ViewModel() {

    val allProducts: LiveData<List<Product>> = productsRepository.allProducts.asLiveData()

    fun save(product: Product) = viewModelScope.launch {
        productsRepository.save(product)
    }

    fun deleteAll() = viewModelScope.launch {
        productsRepository.deleteAll()
    }

    fun selectOneItem(barcodeContent:String) =
        this.productsRepository.selectOneItem(barcodeContent)

    fun scanBarcode(barcodeLauncher: ActivityResultLauncher<ScanOptions>, scanCaption:String) =
        barcodeScannerUtils.scanBarcode(barcodeLauncher,scanCaption)

}
