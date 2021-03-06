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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(product: Product)

    @Query("DELETE FROM product;")
    suspend fun deleteAll()

    @Query(
        "SELECT * FROM product "+
        "WHERE ean LIKE 5059334785836")
    fun bbCodeItem():Flow<List<Product>>

}
