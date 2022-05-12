package pl.lanku.productinventory.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import pl.lanku.productinventory.data.dao.ProductDao
import pl.lanku.productinventory.data.entity.Product

class ProductRepository(private val productDao: ProductDao) {

    val allProducts: Flow<List<Product>> = productDao.getAll()

    @WorkerThread
    suspend fun save(product: Product) {
        productDao.save(product)
    }

    @WorkerThread
    suspend fun deleteAll() {
        productDao.deleteAll()
    }
}
