package pl.lanku.inventory.presentation.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegate
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegateImpl
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

    //    var deviceLanguage : String = Locale.getDefault().getLanguage()
    val localeDelegate: LocaleHelperActivityDelegate = LocaleHelperActivityDelegateImpl()

    val allProducts: LiveData<List<Product>> = productsRepository.allProducts.asLiveData()

    fun save(product: Product) = viewModelScope.launch {
        productsRepository.save(product)
    }

    fun selectOneItem(barcodeContent: String): LiveData<Product> =
        productsRepository.selectOneItem(barcodeContent)

    fun scanBarcode(scanCaption: String) =
        barcodeScannerUtils.scanBarcode(scanCaption)

    fun getRowCount(barcode: String): LiveData<Int> =
        productsRepository.getRowCount(barcode)

//    fun removeProduct(barcodeContent: String) =
//        productsRepository.removeProduct(barcodeContent)
}
