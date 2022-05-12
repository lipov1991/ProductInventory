package pl.lanku.inventory.data

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.lanku.inventory.data.dao.ProductDao
import pl.lanku.inventory.data.entity.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class ProductDb : RoomDatabase() {

    abstract val productDao: ProductDao
}
