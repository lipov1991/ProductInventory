package pl.lanku.productinventory.data

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.lanku.productinventory.data.dao.ProductDao
import pl.lanku.productinventory.data.entity.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class ProductDb : RoomDatabase() {

    abstract val productDao: ProductDao
}
