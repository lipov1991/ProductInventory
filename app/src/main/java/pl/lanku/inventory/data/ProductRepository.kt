package pl.lanku.inventory.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import pl.lanku.inventory.data.dao.ProductDao
import pl.lanku.inventory.data.entity.Product

class ProductRepository(private val productDao: ProductDao) {

    val allProducts: Flow<List<Product>> = productDao.getAll()
    val selectOneProduct: Flow<List<Product>> = productDao.select(barcode)

    @WorkerThread
    suspend fun save(product: Product) {
        productDao.save(product)
    }

    @WorkerThread
    suspend fun deleteAll() {
        productDao.deleteAll()
    }

    @WorkerThread
    suspend fun select(barcode: String){
        productDao.select(barcode)
    }

}
