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
import javax.sql.DataSource

class ProductsViewModel(private val productsRepository: ProductRepository, private val barcodeScannerUtils: BarcodeScannerUtils) : ViewModel() {

    val allProducts: LiveData<List<Product>> = productsRepository.allProducts.asLiveData()

    fun save(product: Product) = viewModelScope.launch {
        productsRepository.save(product)
    }

    fun deleteAll() = viewModelScope.launch {
        productsRepository.deleteAll()
    }

    fun selectOneItem(barcodeContent:String):LiveData<Product> =
        productsRepository.selectOneItem(barcodeContent)

    fun scanBarcode(barcodeLauncher: ActivityResultLauncher<ScanOptions>, scanCaption:String) =
        barcodeScannerUtils.scanBarcode(barcodeLauncher,scanCaption)

    fun getRowCount(barcode:String):LiveData<Integer> =
        productsRepository.getRowCount(barcode)
}
