package pl.lanku.productinventory.presentation.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.lanku.productinventory.data.ProductRepository
import pl.lanku.productinventory.data.entity.Product

class ProductsViewModel(private val productsRepository: ProductRepository) : ViewModel() {

    val allProducts: LiveData<List<Product>> = productsRepository.allProducts.asLiveData()

    fun save(product: Product) = viewModelScope.launch {
        productsRepository.save(product)
    }
}
