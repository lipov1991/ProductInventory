package pl.lanku.inventory.presentation.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.lanku.inventory.data.ProductRepository
import pl.lanku.inventory.data.entity.Product

class ProductsViewModel(private val productsRepository: ProductRepository) : ViewModel() {

    val allProducts: LiveData<List<Product>> = productsRepository.allProducts.asLiveData()
    val selectedItem: LiveData<List<Product>> =  productsRepository.selectedItem.asLiveData()

    fun save(product: Product) = viewModelScope.launch {
        productsRepository.save(product)
    }

    fun deleteAll() = viewModelScope.launch {
        productsRepository.deleteAll()
    }

}
