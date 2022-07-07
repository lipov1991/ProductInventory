package pl.lanku.inventory.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pl.lanku.inventory.data.entity.Product

@Dao
interface ProductDao {

    @Query("SELECT * FROM product;")
    fun getAll(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(product: Product)

    @Query("SELECT * FROM product WHERE ean LIKE :barcode")
    fun selectOneItem(barcode: String):LiveData<Product>

    @Query("SELECT COUNT(*) FROM product WHERE ean LIKE :barcode")
    fun getRowCount(barcode:String):LiveData<Int>

    @Query("DELETE FROM product WHERE ean LIKE :barcode")
    fun removeProduct(barcode:String)
}
