package pl.lanku.inventory.data.dao

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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun save(product: Product)

    @Query("DELETE FROM product;")
    suspend fun deleteAll()
}
