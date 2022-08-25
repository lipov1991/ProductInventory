package pl.lanku.inventory.presentation.products

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegate
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegateImpl
import kotlinx.coroutines.launch
import pl.lanku.inventory.common.utils.BarcodeScannerUtils
import pl.lanku.inventory.common.utils.LanguageUtils
import pl.lanku.inventory.common.utils.SettingsHelperUtils
import pl.lanku.inventory.common.utils.ViewModeChangerUtils
import pl.lanku.inventory.data.ProductRepository
import pl.lanku.inventory.data.entity.Product
import java.util.*

class ProductsViewModel(
    private val productsRepository: ProductRepository,
    private val barcodeScannerUtils: BarcodeScannerUtils,
    private val viewModeChangerUtils: ViewModeChangerUtils,
    private val settingsHelperUtils: SettingsHelperUtils,
    private val languageUtils: LanguageUtils
) : ViewModel() {

    //Value
    var barcodeContent: String = ""
    var nameContent: String = ""
    var descriptionContent: String = ""
    var categoryContent: String = ""
    var deviceLanguage : String = Locale.getDefault().language
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

    fun toLightModeChange(view_light: View, view_dark: View) =
        viewModeChangerUtils.toLightModeChange(view_light, view_dark)

    fun toDarkModeChange(view_light: View, view_dark: View) =
        viewModeChangerUtils.toDarkModeChange(view_light, view_dark)

    fun onClickSettingsShowHide(showLanguage: View, settingButton: View) =
        settingsHelperUtils.onClickSettingsShowHide(showLanguage, settingButton)


    fun changeBackgroundColor(temp:String,engButton:View,polButton:View, gerButton:View) =
        languageUtils.changeBackgroundColor(temp,engButton,polButton, gerButton)

    fun setApplicationLanguage(language:String) =
        languageUtils.setApplicationLanguage(language)

//    fun removeProduct(barcodeContent: String) =
//        productsRepository.removeProduct(barcodeContent)
}
