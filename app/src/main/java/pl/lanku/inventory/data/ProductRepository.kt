package pl.lanku.inventory.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import pl.lanku.inventory.data.dao.ProductDao
import pl.lanku.inventory.data.entity.Product

class ProductRepository(private val productDao: ProductDao) {

    val allProducts: Flow<List<Product>> = productDao.getAll()

    fun selectOneItem(barcode: String):LiveData<Product> =
        productDao.selectOneItem(barcode)

    fun getRowCount(barcode:String):LiveData<Integer> =
        productDao.getRowCount(barcode)

    @WorkerThread
    suspend fun save(product: Product) {
        productDao.save(product)
    }

    @WorkerThread
    suspend fun deleteAll() {
        productDao.deleteAll()
    }

}
